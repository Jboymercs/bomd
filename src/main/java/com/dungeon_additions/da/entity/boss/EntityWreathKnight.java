package com.dungeon_additions.da.entity.boss;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.EntityAdvancedMover;
import com.dungeon_additions.da.entity.ai.EntityAiTimedAttackBoss;
import com.dungeon_additions.da.entity.ai.EntityTimedAttackAIImproved;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.model.ModelWreathKnight;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.Sys;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.processor.IBone;

public class EntityWreathKnight extends EntityAdvancedMover implements IAnimatable, IAttack {
    private static final DataParameter<Boolean> READY_STANCE = EntityDataManager.createKey(EntityWreathKnight.class, DataSerializers.BOOLEAN);
    public void setReadyStanceAnimation(boolean value) {this.dataManager.set(READY_STANCE, Boolean.valueOf(value));}
    public boolean isReadyStance() {return this.dataManager.get(READY_STANCE);}
    private AnimationFactory factory = new AnimationFactory(this);


    protected Vec3d wantedPosition;
    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_WALK_UPPER_ARMS = "walk_upper";
    private final String ANIM_READY_STANCE = "ready_stance";

    private final String ANIM_IDLE = "idle";

    public EntityWreathKnight(World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 2.7f);
        this.readyStanceDistance = 7;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(READY_STANCE, Boolean.valueOf(false));
    }

    protected Vec3d pointAlongPathing(Vec3d originPoint, Vec3d direction, double distance) {
        return originPoint.add(direction).scale(distance);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();


        if(this.setReadyStance && !this.isFightMode()) {
            this.setReadyStanceAnimation(true);

        } else {
            this.setReadyStanceAnimation(false);
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(90);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(14.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
    }


    private<E extends IAnimatable>PlayState predicateArms(AnimationEvent<E> event) {
        if(!this.isFightMode() && !this.isReadyStance()) {
            if(!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER_ARMS, true));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {

        if(!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
          event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            if (event.getLimbSwingAmount() > -0.09F && event.getLimbSwingAmount() < 0.09F) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }


    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityTimedAttackAIImproved<>(this, 1.2, 40, readyStanceDistance, 14F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {


        return 60;
    }
}
