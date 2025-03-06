package com.dungeon_additions.da.entity.sky_dungeon;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.flying.EntityAIRandomFly;
import com.dungeon_additions.da.entity.ai.flying.FlyingMoveHelper;
import com.dungeon_additions.da.entity.ai.flying.TimedAttackInitiator;
import com.dungeon_additions.da.entity.ai.sky_dungeon.EntityGargoyleTridentAI;
import com.dungeon_additions.da.entity.ai.sky_dungeon.EntityImperialHalberdAI;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightRapier;
import com.dungeon_additions.da.entity.sky_dungeon.gargoyle.ActionGargoyleShoot;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.ServerScaleUtil;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.Sys;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityTridentGargoyle extends EntitySkyBase implements IAnimatable, IAttack, IAnimationTickable {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;

    private int spikes_cooldown = 40;

    private int checkNearbyTargets = 40;
    private boolean hasPotion = false;
    private int potionType;
    public boolean overrideInstincts = false;
    public boolean clearCurrentVelocity = false;
    public boolean standbyOnVel = false;
    private final String ANIM_IDLE = "idle";
    private final String ANIM_IDLE_BODY = "idle_body";

    private final String ANIM_SWING = "attack";
    private final String ANIM_STRIKE__ALT = "attack_2";
    private final String ANIM_SHOOT_SPIKES = "shoot_wings";

    private final String ANIM_THROW_POTION = "throw_potion";
    private static final DataParameter<Boolean> SWING_MELEE = EntityDataManager.createKey(EntityTridentGargoyle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_ALTERNATIVE = EntityDataManager.createKey(EntityTridentGargoyle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHOOT_SPIKES = EntityDataManager.createKey(EntityTridentGargoyle.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> THROW_POTION = EntityDataManager.createKey(EntityTridentGargoyle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> ITEM_HAND = EntityDataManager.<ItemStack>createKey(EntityTridentGargoyle.class, DataSerializers.ITEM_STACK);
    private boolean isSwingMelee() {return this.dataManager.get(SWING_MELEE);}
    private void setSwingMelee(boolean value) {this.dataManager.set(SWING_MELEE, Boolean.valueOf(value));}
    private boolean isSwingAlternative() {return this.dataManager.get(SWING_ALTERNATIVE);}
    private void setSwingAlternative(boolean value) {this.dataManager.set(SWING_ALTERNATIVE, Boolean.valueOf(value));}
    private boolean isShootSpikes() {return this.dataManager.get(SHOOT_SPIKES);}
    private void setShootSpikes(boolean value) {this.dataManager.set(SHOOT_SPIKES, Boolean.valueOf(value));}

    private boolean isThrowPotion() {return this.dataManager.get(THROW_POTION);}
    private void setThrowPotion(boolean value) {this.dataManager.set(THROW_POTION, Boolean.valueOf(value));}


    public EntityTridentGargoyle(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 1.95F);
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.setNoGravity(true);
        this.experienceValue = 15;
        if(rand.nextInt(3) == 0) {
            int potionID = ModRand.range(1, 4);
            if(potionID == 1) {
                ItemStack potionStack = new ItemStack(Items.SPLASH_POTION, 1);
                PotionUtils.addPotionToItemStack(potionStack, PotionTypes.LONG_POISON);
                this.equipItemInHand(GARGOYLE_HAND.HAND, potionStack);
                this.potionType = 1;
                this.hasPotion = true;
            }else if (potionID == 2) {
                ItemStack potionStack = new ItemStack(Items.SPLASH_POTION, 1);
                PotionUtils.addPotionToItemStack(potionStack, PotionTypes.LONG_SLOWNESS);
                this.equipItemInHand(GARGOYLE_HAND.HAND, potionStack);
                this.potionType = 2;
                this.hasPotion = true;
            } else if (potionID == 3) {
                ItemStack potionStack = new ItemStack(Items.SPLASH_POTION, 1);
                PotionUtils.addPotionToItemStack(potionStack, PotionTypes.LONG_WEAKNESS);
                this.equipItemInHand(GARGOYLE_HAND.HAND, potionStack);
                this.potionType = 3;
                this.hasPotion = true;
            }
        }
    }

    public EntityTridentGargoyle(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 1.95F);
        this.setNoGravity(true);
        this.experienceValue = 15;
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        if(rand.nextInt(3) == 0) {
            int potionID = ModRand.range(1, 4);
            if(potionID == 1) {
                ItemStack potionStack = new ItemStack(Items.SPLASH_POTION, 1);
                PotionUtils.addPotionToItemStack(potionStack, PotionTypes.LONG_POISON);
                this.equipItemInHand(GARGOYLE_HAND.HAND, potionStack);
                this.potionType = 1;
                this.hasPotion = true;
            }else if (potionID == 2) {
                ItemStack potionStack = new ItemStack(Items.SPLASH_POTION, 1);
                PotionUtils.addPotionToItemStack(potionStack, PotionTypes.LONG_SLOWNESS);
                this.equipItemInHand(GARGOYLE_HAND.HAND, potionStack);
                this.potionType = 2;
                this.hasPotion = true;
            } else if (potionID == 3) {
                ItemStack potionStack = new ItemStack(Items.SPLASH_POTION, 1);
                PotionUtils.addPotionToItemStack(potionStack, PotionTypes.LONG_WEAKNESS);
                this.equipItemInHand(GARGOYLE_HAND.HAND, potionStack);
                this.potionType = 3;
                this.hasPotion = true;
            }
        }
    }

    private boolean renewSound = false;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!world.isRemote) {
            spikes_cooldown--;
            checkNearbyTargets--;
            //this clears current motion and enables a boolean to keep it still for the time being
            // typically only used in red attack
            if(clearCurrentVelocity) {
                this.motionX = 0;
                this.motionY = 0;
                this.motionZ = 0;
                standbyOnVel = true;
                clearCurrentVelocity = false;
            }

            if(!renewSound && !this.isShootSpikes()) {
                this.syncWingsWithSound();
                this.renewSound = true;
            }

         //   EntityLivingBase target = this.getAttackTarget();
        //    if(target != null && checkNearbyTargets < 0) {

            //    int currentNearbyMobCount = 0;
            //    List<EntitySkyBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntitySkyBase.class, target.getEntityBoundingBox().grow(6, 3, 6), e -> !e.getIsInvulnerable());

             //   if(!nearbyEntities.isEmpty()) {
             //       for(EntitySkyBase base : nearbyEntities) {
              //          if(base.getEntitySenses().canSee(target)) {
             //               if(base instanceof EntityTridentGargoyle) {
            //                    currentNearbyMobCount++;
           ////                 }
            //            }
              //      }
            //    }

                //prevent overwhelming the player
            //    if(ServerScaleUtil.getPlayers(this, world) > 0) {
              //      if(currentNearbyMobCount > 3) {
               ////         this.overrideInstincts = true;
                //        System.out.println("Overidding Instincts");
               //     } else {
                //        this.overrideInstincts = false;
                //    }
               // }
               // checkNearbyTargets = 40;
         //   }

            if(this.ticksExisted % 40 == 0 && this.isPotionActive(MobEffects.WEAKNESS) ||
                    this.ticksExisted % 40 == 0 && this.isPotionActive(MobEffects.SLOWNESS)) {
                this.removePotionEffect(MobEffects.WEAKNESS);
                this.removePotionEffect(MobEffects.SLOWNESS);
            }
        }
    }

    private void syncWingsWithSound() {
        addEvent(()-> this.playSound(SoundsHandler.GARGOYLE_WING_FLAP, 0.75f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f)), 12);
        addEvent(()-> this.renewSound = false, 40);
    }
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Swing_Melee", this.isSwingMelee());
        nbt.setBoolean("Swing_Alt", this.isSwingAlternative());
        nbt.setBoolean("Shoot_Spikes", this.isShootSpikes());
        nbt.setBoolean("Throw_Potion", this.isThrowPotion());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSwingMelee(nbt.getBoolean("Swing_Melee"));
        this.setSwingAlternative(nbt.getBoolean("Swing_Alt"));
        this.setShootSpikes(nbt.getBoolean("Shoot_Spikes"));
        this.setThrowPotion(nbt.getBoolean("Throw_Potion"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(ITEM_HAND, ItemStack.EMPTY);
        this.dataManager.register(SWING_MELEE, Boolean.valueOf(false));
        this.dataManager.register(SWING_ALTERNATIVE, Boolean.valueOf(false));
        this.dataManager.register(SHOOT_SPIKES, Boolean.valueOf(false));
        this.dataManager.register(THROW_POTION, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.gargoyle_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.gargoyle_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.gargoyle_armor);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.75D);
    }


    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityGargoyleTridentAI(this, 6, 3, 35,new TimedAttackInitiator<>(this, 10)));
        this.tasks.addTask(6, new EntityAIRandomFly(this, 200, 4));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(swing_alternative, swing_regular, shoot_spikes, throw_potion_action));
            double[] weights = {
                    (prevAttacks != swing_alternative) ? 1/distance : 0,
                    (prevAttacks != swing_regular) ? 1/distance : 0,
                    (prevAttacks != shoot_spikes && spikes_cooldown < 0) ? 1/distance : 0,
                    (distance <= 4 && this.hasPotion) ? 100 : 0
            };

            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return prevAttacks == shoot_spikes ? 100 : 50;
    }

    private final Consumer<EntityLivingBase> throw_potion_action = (target) -> {
      this.setThrowPotion(true);
      this.setFightMode(true);
      this.setImmovable(true);
      this.clearCurrentVelocity = true;

        addEvent(()-> {
            this.playSound(SoundsHandler.GARGOYLE_MOVE, 0.7f, 0.9f / (rand.nextFloat() * 0.4F + 0.6f));
        }, 20);
      addEvent(()-> {
        this.equipItemInHand(GARGOYLE_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM, 1));
        this.hasPotion = false;
            Vec3d relpos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.5, 0)));
          double d0 = target.posY + (double)target.getEyeHeight() - 1.100000023841858D;
          double d1 = target.posX + target.motionX - relpos.x;
          double d2 = d0 - relpos.y;
          double d3 = target.posZ + target.motionZ - relpos.z;
          float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
          PotionType potionSet = PotionTypes.STRONG_HARMING;

          if(potionType == 1) {
              potionSet = PotionTypes.LONG_POISON;
          } if (potionType == 2) {
              potionSet = PotionTypes.LONG_SLOWNESS;
          } if (potionType == 3) {
              potionSet = PotionTypes.LONG_WEAKNESS;
          }

          EntityPotion entitypotion = new EntityPotion(this.world, this, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionSet));
          entitypotion.rotationPitch -= -20.0F;
          entitypotion.shoot(d1, d2 + (double)(f * 0.2F), d3, 0.75F, 4.0F);
          this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
          this.world.spawnEntity(entitypotion);
      }, 25);

      addEvent(()-> {
        this.setThrowPotion(false);
        this.setFightMode(false);
        this.setImmovable(false);
        this.standbyOnVel = false;
      }, 50);
    };
    private final Consumer<EntityLivingBase> swing_alternative = (target) -> {
      this.setFightMode(true);
      this.setSwingAlternative(true);
        addEvent(()-> this.playSound(SoundsHandler.GARGOYLE_MOVE, 0.7f, 0.9f / (rand.nextFloat() * 0.4F + 0.6f)), 2);
      addEvent(()-> {
          Vec3d targetpos = target.getPositionVector();
          this.clearCurrentVelocity = true;
          this.setImmovable(true);
          this.lockLook = true;
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetpos);
              ModUtils.leapTowards(this, targetpos, (float) (distance * 0.18),0F);
          }, 6);
      }, 13);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 0.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.35f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.7f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 25);

      addEvent(()-> this.setImmovable(true), 30);
      addEvent(()-> this.lockLook = false, 35);

        addEvent(()-> this.playSound(SoundsHandler.GARGOYLE_MOVE, 0.7f, 0.9f / (rand.nextFloat() * 0.4F + 0.6f)), 40);
      addEvent(()-> {
        this.lockLook = true;
        Vec3d targetpos = target.getPositionVector();
        addEvent(()-> {
            this.setImmovable(false);
            double distance = this.getPositionVector().distanceTo(targetpos);
            ModUtils.leapTowards(this, targetpos, (float) (distance * 0.18),0F);
        }, 7);
      }, 47);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 0.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.35f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.7f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 60);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 70);

      addEvent(()-> {
          this.setImmovable(false);
          this.standbyOnVel = false;
        this.setFightMode(false);
        this.setSwingAlternative(false);
      }, 90);
    };

    private final Consumer<EntityLivingBase> shoot_spikes = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setShootSpikes(true);
      this.setImmovable(true);

      addEvent(()-> {
        //Shoot Spikes
          this.playSound(SoundsHandler.GARGOYLE_SHOOT_WINGS, 1.25f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
          new ActionGargoyleShoot().performAction(this, target);
      }, 20);

      addEvent(()-> {
          this.setImmovable(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setShootSpikes(false);
        spikes_cooldown = 400;
      }, 50);
    };
    private final Consumer<EntityLivingBase> swing_regular = (target) -> {
        this.setSwingMelee(true);
        this.setFightMode(true);

        addEvent(()-> this.playSound(SoundsHandler.GARGOYLE_MOVE, 0.7f, 0.9f / (rand.nextFloat() * 0.4F + 0.6f)), 5);
        addEvent(()-> {
            Vec3d targetedPos = target.getPositionVector();
            this.setImmovable(true);
            this.clearCurrentVelocity = true;
            addEvent(()-> {
                this.setImmovable(false);
                this.lockLook = true;
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18),0F);
            }, 6);
        }, 13);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 0.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.35f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.7f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 25);

        addEvent(()-> this.setImmovable(true), 35);

        addEvent(()-> {
            this.lockLook = false;
            this.standbyOnVel = false;
            }, 40);

        addEvent(()-> {
            this.setImmovable(false);
            this.setSwingMelee(false);
            this.setFightMode(false);
        }, 50);
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "idle_body_controller", 0, this::predicateIdleBody));
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSwingMelee()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingAlternative()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRIKE__ALT, false));
                return PlayState.CONTINUE;
            }
            if(this.isShootSpikes()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT_SPIKES, false));
                return PlayState.CONTINUE;
            }
            if(this.isThrowPotion()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_THROW_POTION, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdleBody(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_BODY, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if(!this.isImmovable()) {
            ModUtils.aerialTravel(this, strafe, vertical, forward);
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {

    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.GARGOYLE_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.GARGOYLE_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.GARGOYLE_HURT;
    }

    public enum GARGOYLE_HAND {
        HAND("HoldPotion");

        public String getBoneName() {
            return this.boneName;
        }

        private String boneName;

        GARGOYLE_HAND(String bone) {
            this.boneName = bone;
        }

        @Nullable
        public static EntityTridentGargoyle.GARGOYLE_HAND getFromBoneName(String boneName) {
            if ("HoldPotion".equals(boneName)) {
                return HAND;
            }
            return null;
        }

        public int getIndex() {
            if (this == GARGOYLE_HAND.HAND) {
                return 1;
            }
            return 0;
        }
    }

    public void equipItemInHand(EntityTridentGargoyle.GARGOYLE_HAND head, ItemStack state) {
        if(world.isRemote) {
            return;
        }
        if (head == GARGOYLE_HAND.HAND) {
            this.dataManager.set(ITEM_HAND, state);
        }
    }

    public ItemStack getItemFromGargoyleHand(EntityTridentGargoyle.GARGOYLE_HAND head) {
        if (head == GARGOYLE_HAND.HAND) {
            return this.dataManager.get(ITEM_HAND);
        }
        return null;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "gargoyle_mage");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
