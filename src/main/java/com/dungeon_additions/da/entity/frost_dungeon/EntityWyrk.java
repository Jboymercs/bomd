package com.dungeon_additions.da.entity.frost_dungeon;

import com.dungeon_additions.da.entity.ai.EntityWyrkAttackAI;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.frost_dungeon.wyrk.ActionShootProjectileWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.wyrk.ActionSummonAOEWyrk;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.function.Consumer;

public class EntityWyrk extends EntityFrostBase implements IAnimatable, IAnimationTickable, IAttack {

    private final String ANIM_IDLE = "idle";

    private final String ANIM_WALK = "walk";

    private final String ANIM_ATTACK = "stomp";
    private final AnimationFactory factory = new AnimationFactory(this);

    private boolean doSummonParticles = false;
    private Consumer<EntityLivingBase> prevAttack;
    private static final DataParameter<Boolean> STOMP = EntityDataManager.createKey(EntityWyrk.class, DataSerializers.BOOLEAN);

    private boolean isStomp() {return this.dataManager.get(STOMP);}

    private void setStomp(boolean value) {this.dataManager.set(STOMP, Boolean.valueOf(value));}


    public EntityWyrk(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.8F, 1.95F);
        this.experienceValue = 12;
    }

    public EntityWyrk(World worldIn) {
        super(worldIn);
        this.setSize(0.8F, 1.95F);
        this.experienceValue = 12;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Stomp", this.isStomp());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setStomp(nbt.getBoolean("Stomp"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(STOMP, Boolean.valueOf(false));
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.32);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }


    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityWyrkAttackAI<>(this, 1.1, 120, 16, 0.5F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            if(distance >= 6) {
                //do ranged attack
                prevAttack = ranged_attack;
            } else {
                //do stomp attack
                prevAttack = stomp_attack;
            }
            prevAttack.accept(target);
        }
        return 120;
    }

    private final Consumer<EntityLivingBase> ranged_attack = (target) -> {
        this.setFightMode(true);
        this.doSummonParticles =  true;

        addEvent(()-> {
            this.doSummonParticles = false;
            world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            new ActionShootProjectileWyrk().performAction(this, target);
        }, 40);

        addEvent(()-> this.setFightMode(false), 100);
    };

    private final Consumer<EntityLivingBase> stomp_attack = (target) -> {
        this.setFightMode(true);
        this.setStomp(true);
        this.setImmovable(true);

        //causes icicles to fall
        addEvent(()-> {
            this.setUpdateIcicles = true;
            addEvent(()-> this.setUpdateIcicles = false, 5);
        }, 35);

        addEvent(()-> {
            //do shockwave AOE
            new ActionSummonAOEWyrk(6).performAction(this, target);
        }, 40);

        addEvent(()-> {
            this.setImmovable(false);
        this.setFightMode(false);
        this.setStomp(false);
        }, 60);
    };

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(this.doSummonParticles && rand.nextInt(2) == 0) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 2.4, 0))), ModColors.WHITE, new Vec3d(ModRand.getFloat(1) * 0.02,ModRand.getFloat(1) * 0.02,ModRand.getFloat(1) * 0.02));
        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(1, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(2.4)), ModColors.WHITE, pos.normalize().scale(0.1), ModRand.range(10, 15));
            });
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isStomp()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    /**
     * Add a bit of brightness to the entity, because otherwise it looks pretty black
     */
    @Override
    public int getBrightnessForRender() {
        return Math.min(super.getBrightnessForRender() + 20, 150);
    }


    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "wyrk");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

}
