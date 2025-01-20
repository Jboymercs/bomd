package com.dungeon_additions.da.entity.frost_dungeon.draugr;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.EntityDraugrMeleeAI;
import com.dungeon_additions.da.entity.ai.EntityWyrkAttackAI;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.frost_dungeon.EntityFrostBase;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.sun.jna.platform.win32.WinBase;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class EntityDraugr extends EntityFrostBase implements IAnimatable, IAnimationTickable, IAttack {

    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK = "walk";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_WALK_UPPER_SHIELD = "walk_upper_shield";

    private final String ANIM_HIDE_SHIELD = "shield_hide";
    private final String ANIM_MODEL_ADJUST = "model_adjustments";
    private final String ANIM_MODEL_ADJUST_SHIELD = "model_adjustments_shield";

    private final String ANIM_SWING = "swing";
    private final String ANIM_SWING_TWO = "swing_two";

    private final String ANIM_SHIELD_SWING = "swing_shield";
    private static final DataParameter<Boolean> HAS_SHIELD = EntityDataManager.createKey(EntityDraugr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_MELEE = EntityDataManager.createKey(EntityDraugr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_MELEE = EntityDataManager.createKey(EntityDraugr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_SHIELD = EntityDataManager.createKey(EntityDraugr.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Integer> SKIN_TYPE = EntityDataManager.<Integer>createKey(EntityDraugr.class, DataSerializers.VARINT);
    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;
    private boolean hasShieldLowered = false;
    private boolean isHasShield() {return this.dataManager.get(HAS_SHIELD);}
    private void setHasShield(boolean value) {this.dataManager.set(HAS_SHIELD, Boolean.valueOf(value));}
    private boolean isSwingMelee() {return this.dataManager.get(SWING_MELEE);}
    private void setSwingMelee(boolean value) {this.dataManager.set(SWING_MELEE, Boolean.valueOf(value));}
    private boolean isPierceMelee() {return this.dataManager.get(PIERCE_MELEE);}
    private void setPierceMelee(boolean value) {this.dataManager.set(PIERCE_MELEE, Boolean.valueOf(value));}
    private boolean isSwingShield() {return this.dataManager.get(SWING_SHIELD);}
    private void setSwingShield(boolean value) {this.dataManager.set(SWING_SHIELD, Boolean.valueOf(value));}

    public void setSkin(int skinType)
    {
        this.dataManager.set(SKIN_TYPE, Integer.valueOf(skinType));
    }

    public int getSkin()
    {
        return this.dataManager.get(SKIN_TYPE).intValue();
    }

    public EntityDraugr(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 1.95F);
        this.experienceValue = 8;
        this.setSkin(rand.nextInt(5));
        int randI = ModRand.range(1, 10);
        if(randI >= 7) {
            this.setHasShield(true);
        } else {
            this.setHasShield(false);
        }
    }

    public EntityDraugr(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 1.95F);
        this.experienceValue = 8;
        this.setSkin(rand.nextInt(5));
        int randI = ModRand.range(1, 10);
        if(randI >= 7) {
            this.setHasShield(true);
        } else {
            this.setHasShield(false);
        }
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Has_Shield", this.isHasShield());
        nbt.setBoolean("Swing_Melee", this.isSwingMelee());
        nbt.setBoolean("Pierce_Melee", this.isPierceMelee());
        nbt.setBoolean("Swing_Shield", this.isSwingShield());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setHasShield(nbt.getBoolean("Has_Shield"));
        this.setSwingMelee(nbt.getBoolean("Swing_Melee"));
        this.setPierceMelee(nbt.getBoolean("Pierce_Melee"));
        this.setSwingShield(nbt.getBoolean("Swing_Shield"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.getDataManager().register(SKIN_TYPE, Integer.valueOf(0));
        this.dataManager.register(HAS_SHIELD, Boolean.valueOf(false));
        this.dataManager.register(SWING_SHIELD, Boolean.valueOf(false));
        this.dataManager.register(SWING_MELEE, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_MELEE, Boolean.valueOf(false));
    }

    //Hopefully this will fix the weird skin issues going across the models
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData) {
        this.setSkin(rand.nextInt(5));
        int randI = ModRand.range(1, 10);
        if(randI >= 7) {
            this.setHasShield(true);
        }
        return super.onInitialSpawn(difficulty, entityLivingData);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.draugr_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.draugr_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(14D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityDraugrMeleeAI<>(this, 1, 30, 4, 0.15F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        if(!this.isFightMode()) {
            if(this.isHasShield()) {
                prevAttack = swing_shield;
            } else {
                if(prevAttack == swing_one) {
                    prevAttack = swing_two;
                } else {
                    prevAttack = swing_one;
                }
            }

            prevAttack.accept(target);
        }
        return this.isHasShield() ? 140 : 100;
    }

    private final Consumer<EntityLivingBase> swing_one = (target) -> {
        this.setSwingMelee(true);
        this.setFightMode(true);

        addEvent(()-> {
            this.setImmovable(true);
            Vec3d targetedPos = target.getPositionVector();
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.12),0.2F);
            }, 7);
        }, 20);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(0.75f, (e) -> damage, this, offset, source, 0.1f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 33);

        addEvent(()-> this.lockLook = false, 40);

        addEvent(()-> {
            this.setSwingMelee(false);
            this.setFightMode(false);
        }, 55);
    };

    private final Consumer<EntityLivingBase> swing_two = (target) -> {
        this.setPierceMelee(true);
        this.setFightMode(true);

        addEvent(()-> this.lockLook = true, 15);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1, 0)));
            DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(0.75f, (e) -> damage, this, offset, source, 0.1f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 33);

        addEvent(()-> this.setImmovable(true), 35);

        addEvent(()-> {
            this.setImmovable(false);
            this.lockLook = false;
        }, 55);

        addEvent(()-> {
            this.setPierceMelee(false);
            this.setFightMode(false);
        }, 65);
    };

    private final Consumer<EntityLivingBase> swing_shield = (target) -> {
        this.setSwingShield(true);
        this.setFightMode(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.12),0.2F);
            }, 8);
        }, 17);

        addEvent(()-> this.hasShieldLowered = true, 30);
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(0.75f, (e) -> damage, this, offset, source, 0.1f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 32);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 35);

        addEvent(()-> {
            this.setImmovable(false);
        }, 65);

        addEvent(()-> this.hasShieldLowered = false, 70);

        addEvent(()-> {
            this.setSwingShield(false);
            this.setFightMode(false);
        }, 75);
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "shield_controller", 0, this::predicateHideShield));
        data.addAnimationController(new AnimationController(this, "model_adjustments_from", 0, this::predicateModelAdjustments));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSwingShield()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHIELD_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isPierceMelee()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_TWO, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingMelee()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateHideShield(AnimationEvent<E> event) {
        if(!this.isHasShield()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HIDE_SHIELD, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateModelAdjustments(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode()) {
            if(this.isHasShield()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MODEL_ADJUST_SHIELD, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MODEL_ADJUST, true));
            }

            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode()) {
            if(this.isHasShield()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER_SHIELD, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
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
        if (!damageSourceIn.isUnblockable() && this.isHasShield() && !this.hasShieldLowered) {
            Vec3d vec3d = damageSourceIn.getDamageLocation();
            //Handler for Parrying specifically
             if (vec3d != null) {
                Vec3d vec3d1 = this.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

                return vec3d2.dotProduct(vec3d1) < 0.0D;
            }
        }

        return false;
    }

    private boolean hasFoundWyrk = false;

    @Override
    public void onDeath(DamageSource cause) {
        List<EntityWyrk> nearbyWyrk = this.world.getEntitiesWithinAABB(EntityWyrk.class, this.getEntityBoundingBox().grow(20.0D), e -> !e.getIsInvulnerable());
        if(!nearbyWyrk.isEmpty()) {
            for(EntityWyrk wyrk : nearbyWyrk) {
                if(!hasFoundWyrk && !world.isRemote) {
                    ProjectileSoul soul = new ProjectileSoul(world, this, 0, wyrk);
                    soul.setPosition(this.posX, this.posY + 1.5D, this.posZ);
                    soul.setTravelRange(40F);
                    this.world.spawnEntity(soul);
                    this.hasFoundWyrk = true;
                }
            }
        }

        super.onDeath(cause);
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "draugr");

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
        return SoundsHandler.DRAUGR_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.DRAUGR_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.DRAUGR_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.DRAUGR_STEP, 0.5F, 0.4f + ModRand.getFloat(0.3F));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
