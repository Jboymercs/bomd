package com.dungeon_additions.da.entity.rot_knights;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.EntityAIAttackRotKnightRapier;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import mod.azure.azurelib.util.MoveAnalysis;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityRotKnightRapier extends EntityRotBase implements IAttack {

    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK_LOWER = "walk";
    private final String ANIM_WALK_UPPER = "walk_upper";

    private final String ANIM_PIERCE_ATTACK = "pierce";
    private final String ANIM_SWING_ATTACK = "swing";
    private final String ANIM_PIERCE_COMBO = "pierce_extra";
    private final String ANIM_DRINK_POTION = "drink_potion";
    private final String ANIM_BLOCK = "block";

    private static final DataParameter<Boolean> BLOCK_ACTION = EntityDataManager.createKey(EntityRotKnightRapier.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> ITEM_HAND = EntityDataManager.<ItemStack>createKey(EntityRotKnightRapier.class, DataSerializers.ITEM_STACK);

    private static final DataParameter<Boolean> IDLE_MODE = EntityDataManager.createKey(EntityRotKnightRapier.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> SKIN_TYPE = EntityDataManager.<Integer>createKey(EntityRotKnightRapier.class, DataSerializers.VARINT);

    public boolean isIdleMode() {return this.dataManager.get(IDLE_MODE);}
    public boolean isBlockAction() {return this.dataManager.get(BLOCK_ACTION);}

    private void setBlockAction(boolean value) {this.dataManager.set(BLOCK_ACTION, Boolean.valueOf(value));}
    public void setIdleMode(boolean value) {this.dataManager.set(IDLE_MODE, Boolean.valueOf(value));}
    public void setSkin(int skinType)
    {
        this.dataManager.set(SKIN_TYPE, Integer.valueOf(skinType));
    }

    public int getSkin()
    {
        return this.dataManager.get(SKIN_TYPE).intValue();
    }
    private int blockTimer = 80;
    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, true);
    private final MoveAnalysis moveAnalysis;

    public EntityRotKnightRapier(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.75F, 1.95F);
        this.moveAnalysis = new MoveAnalysis(this);
        selectAnimationTooPlay();
        this.setIdleMode(true);
        this.setSkin(rand.nextInt(5));
        this.hemorrhage_resistance = 0.5F;
    }

    public EntityRotKnightRapier(World worldIn) {
        super(worldIn);
        this.setSize(0.75F, 1.95F);
        this.moveAnalysis = new MoveAnalysis(this);
        selectAnimationTooPlay();
        this.setIdleMode(true);
        this.setSkin(rand.nextInt(5));
        this.hemorrhage_resistance = 0.5F;
    }

    protected int selection = ModRand.range(1, 5);
    private String ANIM_SELECTION_STRING;
    private final String ANIM_IDLE_1 = "idle_1";
    private final String ANIM_IDLE_2 = "idle_2";
    private final String ANIM_IDLE_3 = "idle_3";
    private final String ANIM_IDLE_4 = "idle_4";
    private float randomTurn = ModRand.range(1, 360);

    private boolean hasEndedState = false;
    public void selectAnimationTooPlay() {
        if(selection == 1) {
            ANIM_SELECTION_STRING = ANIM_IDLE_1;
        } else if (selection == 2) {
            ANIM_SELECTION_STRING = ANIM_IDLE_2;
        }else if(selection == 3) {
            ANIM_SELECTION_STRING = ANIM_IDLE_3;
        }else if(selection == 4) {
            ANIM_SELECTION_STRING = ANIM_IDLE_4;
        } else {
            ANIM_SELECTION_STRING = ANIM_IDLE_1;
        }
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Block_Action", this.isBlockAction());
        nbt.setBoolean("Idle_State", this.isIdleMode());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setBlockAction(nbt.getBoolean("Block_Action"));
        this.setIdleMode(nbt.getBoolean("Idle_State"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.getDataManager().register(SKIN_TYPE, Integer.valueOf(0));
        this.dataManager.register(ITEM_HAND, ItemStack.EMPTY);
        this.dataManager.register(BLOCK_ACTION, Boolean.valueOf(false));
        this.dataManager.register(IDLE_MODE, Boolean.valueOf(false));
    }

    private Consumer<EntityLivingBase> prevAttack;
    public boolean isRandomGetAway = false;


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.rot_knights_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.rot_knights_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.rot_knights_armor);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData) {
        this.setSkin(rand.nextInt(5));
        return super.onInitialSpawn(difficulty, entityLivingData);
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        this.moveAnalysis.update();
        this.blockTimer--;

        if(this.isIdleMode() && !hasEndedState && !world.isRemote) {
            this.rotationYaw = randomTurn;
            this.rotationYawHead = randomTurn;
            this.setImmovable(true);
        }

        EntityLivingBase target = this.getAttackTarget();
        if(target != null && !world.isRemote) {

            if(!hasEndedState && this.getDistanceSq(target) <= 24 && this.isIdleMode()) {
                this.endIdleState();
            }
            if(this.isRandomGetAway) {
                double d0 = (this.posX - target.posX) * 0.018;
                double d1 = (this.posY - target.posY) * 0.005;
                double d2 = (this.posZ - target.posZ) * 0.018;
                this.addVelocity(d0, d1, d2);
                this.faceEntity(target, 35, 35);
                this.getLookHelper().setLookPositionWithEntity(target, 35, 35);
            }
        }

    }

    private void endIdleState() {
        this.hasEndedState = true;

        addEvent(()-> {
            this.setIdleMode(false);
            this.setImmovable(false);
        }, 10);

    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIAttackRotKnightRapier<>(this, 1.1, 20, 6, 0F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityZombie>(this, EntityZombie.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityZombieVillager>(this, EntityZombieVillager.class, 1, true, false, null));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget<EntityHusk>(this, EntityHusk.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double healthF = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && !isRandomGetAway && !this.isIdleMode()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(randomGetAway, basic_swing, pierce, pierce_combo, drink_potion));
            double[] weights = {
                    (distance <= 7 && prevAttack != randomGetAway) ? 1/distance : 0,
                    (distance <= 3 && prevAttack != basic_swing) ? 1/distance : 0,
                    (distance <= 7 && prevAttack != pierce && prevAttack != pierce_combo) ? 1/distance : 0,
                    (distance <= 7 && prevAttack != pierce_combo) ? 1/distance : 0,
                    (healthF <= 0.8) ? 1/distance : 0
            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        return 25;
    }

    private final Consumer<EntityLivingBase> drink_potion = (target) -> {
      this.setFightMode(true);
      animator.performAttack(ANIM_DRINK_POTION);
      this.isRandomGetAway = true;
      this.equipItemInHand(ROT_KNIGHT_HAND.HAND, new ItemStack(ModItems.FAKE_HEALING_POTION));

      addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 15);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 20);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 25);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 30);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 35);
      addEvent(()-> {
          //heal
        double healthTooHeal = this.getMaxHealth() * 0.25;
        this.heal((float) healthTooHeal);
          this.equipItemInHand(ROT_KNIGHT_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM));
      }, 35);


      addEvent(()-> {
          this.isRandomGetAway = false;
        this.setFightMode(false);
      }, 42);
    };

    private final Consumer<EntityLivingBase> pierce_combo = (target) -> {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        animator.performAttack(ANIM_PIERCE_COMBO);

        addEvent(()-> {
            Vec3d targetedpos = target.getPositionVector();
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedpos);
                ModUtils.leapTowards(this, targetedpos, (float) (distance * 0.18),0.25F);
            }, 10);
        }, 12);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 30);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 35);

        addEvent(()-> {
            this.setImmovable(false);
            Vec3d targetedpos = target.getPositionVector();
            double distance = this.getPositionVector().distanceTo(targetedpos);
            ModUtils.leapTowards(this, targetedpos, (float) (distance * 0.08),0.25F);
        }, 40);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.8, 1.3, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 45);


        addEvent(()-> this.setImmovable(true), 50);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedpos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedpos);
                ModUtils.leapTowards(this, targetedpos, (float) (distance * 0.22),0.25F);
            }, 10);
        }, 60);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 1.3, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.5F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 77);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 83);

        addEvent(()-> {
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
        }, 100);
    };

    private final Consumer<EntityLivingBase> pierce = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      animator.performAttack(ANIM_PIERCE_ATTACK);

      addEvent(()-> {
        Vec3d targetedpos = target.getPositionVector();
        this.lockLook = true;
        addEvent(()-> {
            this.setImmovable(false);
            double distance = this.getPositionVector().distanceTo(targetedpos);
            ModUtils.leapTowards(this, targetedpos, (float) (distance * 0.18),0.25F);
        }, 10);
      }, 12);


      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 30);

      addEvent(()-> {
          this.setImmovable(true);
          this.lockLook = false;
      }, 35);

      addEvent(()-> {
          this.setImmovable(false);
          Vec3d targetedpos = target.getPositionVector();
          double distance = this.getPositionVector().distanceTo(targetedpos);
          ModUtils.leapTowards(this, targetedpos, (float) (distance * 0.08),0.25F);
      }, 40);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.8, 1.3, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 45);


      addEvent(()-> this.setImmovable(true), 50);

      addEvent(()-> {
          this.setImmovable(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
      }, 65);
    };
    private final Consumer<EntityLivingBase> basic_swing = (target) -> {
      this.setFightMode(true);
      animator.performAttack(ANIM_SWING_ATTACK);
        addEvent(()-> this.lockLook = true, 12);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.9, 1.3, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.2F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            this.setImmovable(true);
        }, 24);

        addEvent(()-> this.lockLook = false, 30);

      addEvent(()-> {
          this.setImmovable(false);
        this.setFightMode(false);
      }, 40);
    };


    private final Consumer<EntityLivingBase> randomGetAway = (target) -> {
        this.isRandomGetAway = true;
        addEvent(()-> this.isRandomGetAway = false, 40);
    };

    private boolean currentlyBlocking = false;

    @Override
    public void updateAnimations() {
        if(this.isIdleMode()) {
            animator.setStateAnim(ANIM_SELECTION_STRING, true);
        }

        if(!this.isIdleMode() && !this.isFullBodyUsage()) {
            boolean isMovingOnGround = moveAnalysis.isMovingHorizontally() && this.onGround;
            if(isMovingOnGround) {
                if(!this.isFightMode()) {
                    animator.setCustomUpperWalk(ANIM_WALK_UPPER);
                }
                animator.setCustomLowerWalk(ANIM_WALK_LOWER);
            } else if(!this.isFightMode()) {
                animator.setCustomUpperIdle(ANIM_IDLE);
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {


        if(source.getImmediateSource() instanceof EntityRotKnight || source.getImmediateSource() instanceof EntityRotKnightRapier || this.isIdleMode()) {
            return false;
        }

        if(amount > 0.0F && this.canBlockDamageSource(source) && !this.isFightMode()) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }
            this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.7f, 1.1f + ModRand.getFloat(0.2f));

            return false;
        } else if (this.currentlyBlocking) {
            this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.7f, 1.1f + ModRand.getFloat(0.2f));
            return false;
        }else {
            return super.attackEntityFrom(source, amount);
        }
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if (!damageSourceIn.isUnblockable() && this.blockTimer < 0 && !this.isBlockAction() && !this.isFightMode()) {
            Vec3d vec3d = damageSourceIn.getDamageLocation();
            //Handler for other
            if (vec3d != null) {
                Vec3d vec3d1 = this.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);
                this.doBlockAction();
                return vec3d2.dotProduct(vec3d1) < 0.5D;
            }
        }

        return false;
    }

    private void doBlockAction() {
        this.setBlockAction(true);
        this.setFightMode(true);
        animator.performAttack(ANIM_BLOCK);
        this.blockTimer = 60;
        this.isRandomGetAway = true;
        this.currentlyBlocking = true;

        addEvent(()-> this.currentlyBlocking = false, 23);

        addEvent(()-> {
            this.setBlockAction(false);
            this.setFightMode(false);
            this.isRandomGetAway = false;
        }, 30);
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "rot_knight");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    public enum ROT_KNIGHT_HAND {
        HAND("HandEquip");

        public String getBoneName() {
            return this.boneName;
        }

        private String boneName;

        ROT_KNIGHT_HAND(String bone) {
            this.boneName = bone;
        }

        @Nullable
        public static EntityRotKnightRapier.ROT_KNIGHT_HAND getFromBoneName(String boneName) {
            if ("HandEquip".equals(boneName)) {
                return HAND;
            }
            return null;
        }

        public int getIndex() {
            if (this == EntityRotKnightRapier.ROT_KNIGHT_HAND.HAND) {
                return 1;
            }
            return 0;
        }
    }

    public void equipItemInHand(ROT_KNIGHT_HAND head, ItemStack state) {
        if(world.isRemote) {
            return;
        }
        if (head == ROT_KNIGHT_HAND.HAND) {
            this.dataManager.set(ITEM_HAND, state);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.ROT_KNIGHT_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.ROT_KNIGHT_WALK, 0.4F, 0.4f + ModRand.getFloat(0.3F));
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ROT_KNIGHT_DEATH;
    }


    public ItemStack getItemFromKnightHand(ROT_KNIGHT_HAND head) {
        if (head == ROT_KNIGHT_HAND.HAND) {
            return this.dataManager.get(ITEM_HAND);
        }
        return null;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
