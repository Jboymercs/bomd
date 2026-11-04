package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityAIRoyalAttack;
import com.dungeon_additions.da.entity.dark_dungeon.minion_action.RoyalShieldSmash;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import mod.azure.azurelib.util.MoveAnalysis;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityDarkRoyal extends EntityDarkBase implements IAttack {

    private final String ANIM_IDLE = "idle";
    private final String ANIM_MODEL_POSE = "default_pose";
    private final String ANIM_MODEL_POSE_SHIELDED = "default_pose_shield";

    private final String ANIM_WALK_LOWER = "walk";
    private final String ANIM_WALK_ARMS = "walk_upper";
    private final String ANIM_WALK_ARMS_SHIELDED = "walk_upper_shield";
    private final String ANIM_HIDE_SHIELD = "hide_shield";

    //Shield Up Attacks
    private final String ANIM_SWING_SHIELD = "swing_shield";
    private final String ANIM_THRUST = "thrust";
    private final String ANIM_THRUST_CONTINUE = "thrust_continue";
    private final String ANIM_THRUST_FINISH = "thrust_finish";
    private final String ANIM_SHIELD_SMASH = "shield_smash";
    // Shield Down Attacks
    private final String ANIM_SWING_SPEAR = "swing_spear";
    private final String ANIM_PIERCE_SPEAR = "pierce_spear";
    private final String ANIM_DASH_SPEAR = "dash_with_spear";
    //No Shield only attacks
    private final String ANIM_THROW_POTION = "throw_potion";

    private static final DataParameter<Boolean> HAS_SHIELD = EntityDataManager.createKey(EntityDarkRoyal.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DISABLED_SHIELD = EntityDataManager.createKey(EntityDarkRoyal.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> SKIN_TYPE = EntityDataManager.<Integer>createKey(EntityDarkRoyal.class, DataSerializers.VARINT);

    private static final DataParameter<ItemStack> ITEM_HAND = EntityDataManager.<ItemStack>createKey(EntityDarkRoyal.class, DataSerializers.ITEM_STACK);

    public boolean isHasShield() {
        return this.dataManager.get(HAS_SHIELD);
    }
    private void setHasShield(boolean value) {
        this.dataManager.set(HAS_SHIELD, Boolean.valueOf(value));
    }
    public boolean isDisabledShield() {return this.dataManager.get(DISABLED_SHIELD);}
    private void setDisabledShield(boolean value) {
        this.dataManager.set(DISABLED_SHIELD, Boolean.valueOf(value));
    }

    public void setSkin(int skinType)
    {
        this.dataManager.set(SKIN_TYPE, Integer.valueOf(skinType));
    }

    public int getSkin()
    {
        return this.dataManager.get(SKIN_TYPE).intValue();
    }

    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);
    private final MoveAnalysis moveAnalysis;
    private Consumer<EntityLivingBase> prevAttacks;
    private boolean hasShieldLowered = false;
    private int shieldDisableTime = 180;

    private boolean hasPotion = false;

    private int potionID = 1;

    public EntityDarkRoyal(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 2.2F);
        this.experienceValue = 12;
        this.isImmuneToFire = true;
        this.moveAnalysis = new MoveAnalysis(this);
        int randI = ModRand.range(1, 10);
        this.setSkin(rand.nextInt(5));
        if(randI >= MobConfig.crypt_guard_shield_chance) {
            this.setHasShield(true);
            this.equipItemInHand(ROYAL_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM, 1));
        } else {
            this.setHasShield(false);
            int randT = ModRand.range(1, 10);
            if(randT >= MobConfig.crypt_guard_potion_chance) {
                int randB = ModRand.range(1, 4);
                if(randB == 1) {
                    ItemStack potionStack = new ItemStack(Items.LINGERING_POTION, 1);
                    PotionUtils.addPotionToItemStack(potionStack, PotionTypes.POISON);
                    this.equipItemInHand(ROYAL_HAND.HAND, potionStack);
                    this.potionID = 1;
                } else if (randB == 2) {
                    ItemStack potionStack = new ItemStack(Items.LINGERING_POTION, 1);
                    PotionUtils.addPotionToItemStack(potionStack, PotionTypes.HARMING);
                    this.equipItemInHand(ROYAL_HAND.HAND, potionStack);
                    this.potionID = 2;
                } else {
                    ItemStack potionStack = new ItemStack(Items.LINGERING_POTION, 1);
                    PotionUtils.addPotionToItemStack(potionStack, PotionTypes.WEAKNESS);
                    this.equipItemInHand(ROYAL_HAND.HAND, potionStack);
                    this.potionID = 3;
                }
                this.hasPotion = true;
            } else {
                //unequip Potion
                this.equipItemInHand(ROYAL_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM, 1));
            }
        }
        this.falter_resistance = 1.3F;
        this.hemorrhage_resistance = 0.9F;
    }

    public EntityDarkRoyal(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 2.2F);
        this.experienceValue = 12;
        this.isImmuneToFire = true;
        int randI = ModRand.range(1, 10);
        this.moveAnalysis = new MoveAnalysis(this);
        this.setSkin(rand.nextInt(5));
        if(randI >= MobConfig.crypt_guard_shield_chance) {
            this.setHasShield(true);
            this.equipItemInHand(ROYAL_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM, 1));
        } else {
            this.setHasShield(false);
            int randT = ModRand.range(1, 10);
            if(randT >= MobConfig.crypt_guard_potion_chance) {
                int randB = ModRand.range(1, 4);
                if(randB == 1) {
                    ItemStack potionStack = new ItemStack(Items.LINGERING_POTION, 1);
                    PotionUtils.addPotionToItemStack(potionStack, PotionTypes.POISON);
                    this.equipItemInHand(ROYAL_HAND.HAND, potionStack);
                    this.potionID = 1;
                } else if (randB == 2) {
                    ItemStack potionStack = new ItemStack(Items.LINGERING_POTION, 1);
                    PotionUtils.addPotionToItemStack(potionStack, PotionTypes.HARMING);
                    this.equipItemInHand(ROYAL_HAND.HAND, potionStack);
                    this.potionID = 2;
                } else {
                    ItemStack potionStack = new ItemStack(Items.LINGERING_POTION, 1);
                    PotionUtils.addPotionToItemStack(potionStack, PotionTypes.WEAKNESS);
                    this.equipItemInHand(ROYAL_HAND.HAND, potionStack);
                    this.potionID = 3;
                }
                this.hasPotion = true;
            } else {
                //unequip Potion
                this.equipItemInHand(ROYAL_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM, 1));
            }
        }
        this.falter_resistance = 1.3F;
        this.hemorrhage_resistance = 0.9F;
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData) {
        this.setSkin(rand.nextInt(5));
        int randI = ModRand.range(1, 10);
        if(randI >= MobConfig.crypt_guard_shield_chance) {
            this.setHasShield(true);
            this.equipItemInHand(ROYAL_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM, 1));
        } else {
            this.setHasShield(false);
            int randT = ModRand.range(1, 10);
            if(randT >= MobConfig.crypt_guard_potion_chance) {
                int randB = ModRand.range(1, 4);
                if(randB == 1) {
                    ItemStack potionStack = new ItemStack(Items.LINGERING_POTION, 1);
                    PotionUtils.addPotionToItemStack(potionStack, PotionTypes.POISON);
                    this.equipItemInHand(ROYAL_HAND.HAND, potionStack);
                    this.potionID = 1;
                } else if (randB == 2) {
                    ItemStack potionStack = new ItemStack(Items.LINGERING_POTION, 1);
                    PotionUtils.addPotionToItemStack(potionStack, PotionTypes.HARMING);
                    this.equipItemInHand(ROYAL_HAND.HAND, potionStack);
                    this.potionID = 2;
                } else {
                    ItemStack potionStack = new ItemStack(Items.LINGERING_POTION, 1);
                    PotionUtils.addPotionToItemStack(potionStack, PotionTypes.WEAKNESS);
                    this.equipItemInHand(ROYAL_HAND.HAND, potionStack);
                    this.potionID = 3;
                }
                this.hasPotion = true;
            } else {
                //unequip Potion
                this.equipItemInHand(ROYAL_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM, 1));
            }
        }
        return super.onInitialSpawn(difficulty, entityLivingData);
    }

    private int dashCooldown = 200;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.moveAnalysis.update();
        if(!world.isRemote) {

            if(this.isDisabledShield()) {
                shieldDisableTime--;
                if(shieldDisableTime < 0 && !this.isFightMode()) {
                    this.setDisabledShield(false);
                }
            }

            EntityLivingBase target = this.getAttackTarget();
            if(target != null) {
                //Allows ones without shields to initiate an attack from afar
                if(this.getDistance(target) < 16 && this.getDistance(target) > 5 && !this.isFightMode() && !this.isHasShield()) {
                    dashCooldown--;
                    if(dashCooldown < 0) {
                        dash_with_spear.accept(target);
                    }
                }
            }
        }
    }

    @Override
    public void onLivingUpdate()
    {
        if (this.world.isDaytime() && !this.world.isRemote && MobConfig.assassin_burns_daylight)
        {
            //removes these guys when its daytime
            float f = this.getBrightness();

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ)))
            {
                //particles
                ModUtils.performNTimes(15, (i) -> {
                    Main.proxy.spawnParticle(23, world, this.posX + ModRand.getFloat(1), this.posY + ModRand.getFloat(1) + 0.5, this.posZ + ModRand.getFloat(1), 0,0.07,0, 0);
                });
                this.playSound(SoundsHandler.DARK_ASSASSIN_DASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.7f));
                this.setDead();
            }
        }

        super.onLivingUpdate();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Has_Shield", this.isHasShield());
        nbt.setBoolean("Disabled_Shield", this.isDisabledShield());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setHasShield(nbt.getBoolean("Has_Shield"));
        this.setDisabledShield(nbt.getBoolean("Disabled_Shield"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(ITEM_HAND, ItemStack.EMPTY);
        this.getDataManager().register(SKIN_TYPE, Integer.valueOf(0));
        this.dataManager.register(HAS_SHIELD, Boolean.valueOf(false));
        this.dataManager.register(DISABLED_SHIELD, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.crypt_guard_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.crypt_guard_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.crypt_guard_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(4);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.9D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIRoyalAttack<>(this, 1.0D, 20, 5, 0.3F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(swing_with_shield, thrust_begin, shield_smash, swing_with_spear, pierce_with_spear, dash_with_spear, throw_potion));
            double[] weights = {
                    (prevAttacks != swing_with_shield && this.isHasShield() && !this.isDisabledShield()) ? 1/distance : 0,
                    (prevAttacks != thrust_begin && this.isHasShield() && !this.isDisabledShield()) ? 1/distance : 0,
                    (prevAttacks != shield_smash && this.isHasShield() && !this.isDisabledShield()) ? 1/distance : 0,
                    (prevAttacks != swing_with_spear && this.isHasShield() && this.isDisabledShield()) ? 1/distance : (prevAttacks != swing_with_spear && !this.isHasShield()) ? 1/distance : 0,
                    (prevAttacks != pierce_with_spear && this.isHasShield() && this.isDisabledShield()) ? 1/distance : (prevAttacks != pierce_with_spear && !this.isHasShield()) ? 1/distance : 0,
                    (prevAttacks != dash_with_spear && !this.isHasShield()) ? distance * 0.02 : 0,
                    (prevAttacks != throw_potion && this.hasPotion && !this.isHasShield()) ? 100 : 0

            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return this.isHasShield() && !this.isDisabledShield() ? 90 : 30;
    }
    //A dash attack
    //a poke attack to disable shields
    // a throw potion attack only usable by those without spears
    //

    private final Consumer<EntityLivingBase> throw_potion = (target) -> {
      this.setFightMode(true);
      animator.performAttack(ANIM_THROW_POTION);

      addEvent(()-> {
          this.lockLook = true;
      }, 25);


      addEvent(()-> {
        this.setImmovable(true);
        this.equipItemInHand(ROYAL_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM, 1));
        this.hasPotion = false;
          Vec3d relpos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.5, 0)));
          double d0 = target.posY + (double)target.getEyeHeight() - 1.100000023841858D;
          double d1 = target.posX + target.motionX - relpos.x;
          double d2 = d0 - relpos.y;
          double d3 = target.posZ + target.motionZ - relpos.z;
          float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
          PotionType potionSet = PotionTypes.POISON;

          if(this.potionID == 2) {
              potionSet = PotionTypes.HARMING;
          } else if (this.potionID == 3) {
              potionSet = PotionTypes.WEAKNESS;
          }
          EntityPotion entitypotion = new EntityPotion(this.world, this, PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), potionSet));
          entitypotion.rotationPitch -= -20.0F;
          entitypotion.shoot(d1, d2 + (double)(f * 0.2F), d3, 0.75F, 4.0F);
          this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
          this.world.spawnEntity(entitypotion);
      }, 29);

      addEvent(()-> {
          this.lockLook = false;
      }, 37);

      addEvent(()-> {
          this.setImmovable(false);
        this.setFightMode(false);
      }, 45);
    };
    private final Consumer<EntityLivingBase> dash_with_spear = (target) -> {
      animator.performAttack(ANIM_DASH_SPEAR);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.hasShieldLowered = true;

      addEvent(()-> {
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-5));
          world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
          ModUtils.attemptTeleport(targetedPos, this);
          this.playSound(SoundsHandler.DARK_ROYAL_DASH, 1.2f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 25);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.35),0.1F);
            }, 5);
        }, 25);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.6, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack() * 1.5);
            ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.8f, 0, false, 0.6F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 36);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
            this.hasShieldLowered = false;
        }, 45);

      addEvent(()-> {
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
        this.dashCooldown = 200;
      }, 60);
    };
    private final Consumer<EntityLivingBase> pierce_with_spear = (target) -> {
      animator.performAttack(ANIM_PIERCE_SPEAR);
      this.setFightMode(true);
        this.hasShieldLowered = true;

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.2),0.2F);
            }, 5);
        }, 20);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.6, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.4F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 30);

        addEvent(()-> {
            this.lockLook = false;
        }, 40);

      addEvent(()-> {
        this.setFightMode(false);
          this.hasShieldLowered = false;
      }, 55);
    };
    private final Consumer<EntityLivingBase> swing_with_spear = (target) -> {
      animator.performAttack(ANIM_SWING_SPEAR);
      this.setFightMode(true);
        this.hasShieldLowered = true;

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.2),0.2F);
            }, 5);
        }, 20);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.6, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.2F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 30);

        addEvent(()-> {
            this.lockLook = false;
        }, 40);

      addEvent(()-> {
        this.setFightMode(false);
          this.hasShieldLowered = false;
      }, 55);
    };

    private final Consumer<EntityLivingBase> shield_smash = (target) -> {
      animator.performAttack(ANIM_SHIELD_SMASH);
      this.setFightMode(true);

      addEvent(()-> {
        this.setImmovable(true);
        this.lockLook = true;
      }, 20);

      addEvent(()-> {
          //Do Shield Stomp
          new RoyalShieldSmash().performAction(this, target);
          this.playSound(SoundsHandler.B_KNIGHT_STOMP, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 30);

      addEvent(()-> {
          this.lockLook = false;
      }, 45);

      addEvent(()-> {
        this.setFightMode(false);
        this.setImmovable(false);
      }, 60);
    };

    private final Consumer<EntityLivingBase> thrust_begin = (target) -> {
        animator.performAttack(ANIM_THRUST);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.24),0.2F);
                this.hasShieldLowered = true;
            }, 5);
        }, 18);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.6, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.7f, 0, false, 0.6F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.7f, 1.3f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 30);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 35);

        addEvent(()-> {
        int randI = ModRand.range(1, 11);

        if(randI > 6) {
            //continue thrust\
            this.setThrustTooContinue(target);
        } else {
            //finish Thrust
            animator.performAttack(ANIM_THRUST_FINISH);

            addEvent(()-> {
                this.hasShieldLowered = false;
            }, 10);

            addEvent(()-> {
            this.setFightMode(false);
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            }, 15);
        }
        }, 45);
    };

    private void setThrustTooContinue(EntityLivingBase target) {thrust_continue.accept(target);}

    private final Consumer<EntityLivingBase> thrust_continue = (target) -> {
      animator.performAttack(ANIM_THRUST_CONTINUE);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.3),0.2F);
                this.hasShieldLowered = true;
            }, 5);
        }, 24);

        addEvent(()-> {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.4, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(3.2f, (e) -> damage, this, offset, source, 0.7f, 0, false, 0.3F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.5f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 36);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 45);

        addEvent(()-> {
            this.hasShieldLowered = false;
        }, 65);

      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 70);
    };

    private final Consumer<EntityLivingBase> swing_with_shield = (target) -> {
        animator.performAttack(ANIM_SWING_SHIELD);
        this.setFightMode(true);

        addEvent(()-> {
            this.lockLook = true;
        }, 20);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.6, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.25F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.7f, 1.3f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 30);

        addEvent(()-> {
            this.lockLook = false;
        }, 40);

        addEvent(()-> {
            this.setFightMode(false);
        }, 50);
    };

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(4, 30, (pos)-> {
                pos = new Vec3d(pos.x, pos.z, pos.y);
                ParticleManager.spawnColoredSmoke(world, pos.add(this.getPositionVector().add(new Vec3d(0, 1.4, 0))), ModColors.BLACK, ModUtils.yVec(0.1));
            });
        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1)), ModColors.BLACK, Vec3d.ZERO, ModRand.range(10, 15));
            ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(2)), ModColors.BLACK, Vec3d.ZERO, ModRand.range(10, 15));
            ModUtils.circleCallback(1, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1)), ModColors.BLACK, pos.normalize().scale(0.1), ModRand.range(10, 15));
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(2)), ModColors.BLACK, pos.normalize().scale(0.1), ModRand.range(10, 15));
            });
        }
    }

    @Override
    public void updateAnimations() {
        if(!this.isHasShield()) {
            animator.setStateAnim(ANIM_HIDE_SHIELD, true);
        }
        if(!this.isFullBodyUsage()) {
            boolean isMovingOnGround = moveAnalysis.isMovingHorizontally() && this.onGround;
            if(isMovingOnGround) {
                if(!this.isFightMode()) {
                    if(this.isHasShield() && !this.isDisabledShield()) {
                        animator.setCustomUpperWalk(ANIM_WALK_ARMS_SHIELDED);
                    } else {
                        animator.setCustomUpperWalk(ANIM_WALK_ARMS);
                    }
                    animator.setCustomLowerWalk(ANIM_WALK_LOWER);
                }
            } else {
                if(!this.isFightMode()) {
                    if(this.isHasShield() && !this.isDisabledShield()) {
                        animator.setCustomUpperIdle(ANIM_MODEL_POSE_SHIELDED);
                    } else {
                        animator.setCustomUpperIdle(ANIM_MODEL_POSE);
                    }
                    animator.setCustomLowerIdle(ANIM_IDLE);
                }
            }
        }
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if(source.getImmediateSource() == this) {
            return false;
        }

        if (amount > 0.0F && this.canBlockDamageSource(source)) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }
            this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0f, 0.8f + ModRand.getFloat(0.2f));

            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if (!damageSourceIn.isUnblockable() && this.isHasShield() && !this.hasShieldLowered && !this.isDisabledShield()) {
            Vec3d vec3d = damageSourceIn.getDamageLocation();
            //Handler for Parrying specifically

            if(damageSourceIn.getImmediateSource() instanceof EntityPlayer) {
                Entity sourceAt = damageSourceIn.getImmediateSource();
                if(sourceAt != null && !damageSourceIn.isProjectile()) {
                    ItemStack stack =  ((EntityPlayer) sourceAt).inventory.getCurrentItem();
                    if(stack.getItem() instanceof ItemAxe || stack.getItem().canDisableShield(stack, null, this, ((EntityLivingBase) sourceAt))) {
                        this.setDisabledShield(true);
                        this.shieldDisableTime = 180;
                        this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 1.0f, 0.8f + ModRand.getFloat(0.2f));
                    }
                }
            }
            if (vec3d != null) {
                Vec3d vec3d1 = this.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

                return vec3d2.dotProduct(vec3d1) < 0.0D;
            }
        }

        return false;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        if(MobConfig.cult_crypt_guard_status) {
            return EnumCreatureAttribute.UNDEAD;
        } else {
            return EnumCreatureAttribute.UNDEFINED;
        }
    }

    public enum ROYAL_HAND {
        HAND("HoldJoint");

        public String getBoneName() {
            return this.boneName;
        }

        private String boneName;

        ROYAL_HAND(String bone) {
            this.boneName = bone;
        }

        @Nullable
        public static EntityDarkRoyal.ROYAL_HAND getFromBoneName(String boneName) {
            if ("HoldJoint".equals(boneName)) {
                return HAND;
            }
            return null;
        }

        public int getIndex() {
            if (this == ROYAL_HAND.HAND) {
                return 1;
            }
            return 0;
        }
    }

    public void equipItemInHand(EntityDarkRoyal.ROYAL_HAND head, ItemStack state) {
        if(world.isRemote) {
            return;
        }
        if (head == ROYAL_HAND.HAND) {
            this.dataManager.set(ITEM_HAND, state);
        }
    }

    public ItemStack getItemFromRoyalHand(EntityDarkRoyal.ROYAL_HAND head) {
        if (head == ROYAL_HAND.HAND) {
            return this.dataManager.get(ITEM_HAND);
        }
        return null;
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "dark_royal");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.DARK_ROYAL_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.DARK_ROYAL_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.DARK_ROYAL_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.DRAUGR_STEP, 0.3F, 0.8f + ModRand.getFloat(0.3F));
    }
}
