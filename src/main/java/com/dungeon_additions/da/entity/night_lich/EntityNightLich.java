package com.dungeon_additions.da.entity.night_lich;

import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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

public class EntityNightLich extends EntityAbstractNightLich implements IAnimatable, IAttack, IAnimationTickable {

    private Consumer<EntityLivingBase> prevAttack;

    //Idle Stuff
    private final String ANIM_MODEL_IDLE = "model_idle";
    private final String ANIM_BOOK_IDLE = "book_idle";
    private final String ANIM_FLY_IDLE = "fly_idle";
    //Attack Anims
    private final String ANIM_BLUE = "shoot_projectiles";
    private final String ANIM_YELLOW = "shoot_fireball";
    private final String ANIM_RED = "red_dash";
    private final String ANIM_PURPLE = "summon_mobs";
    private final String ANIM_GREEN = "green_aoe";
    //Combos
    private final String ANIM_TRACK_PROJECTILES = "shoot_track_projectiles";
    private final String ANIM_SHOOT_PATTERN = "shoot_projectiles_pattern";
    private final String ANIM_COMBO_AOE = "combo_aoe";
    //Non Colored
    private final String ANIM_SWING = "swing";
    private final String ANIM_DOUBLE_SWING = "double_swing";

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityNightLich(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.1F, 2.5F);
    }

    public EntityNightLich(World worldIn) {
        super(worldIn);
        this.setSize(1.1F, 2.5F);

    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        return 0;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "book_controller", 0, this::predicateIdleBook));
        data.addAnimationController(new AnimationController(this, "model_idle", 0, this::predicateModelIdle));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttack));
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isGreenAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_GREEN, false));
            }
            if(this.isBlueAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLUE, false));
            }
            if(this.isRedAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_RED, false));
            }
            if(this.isYellowAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_YELLOW, false));
            }
            if(this.isPurpleAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PURPLE, false));
            }
            if(this.isSwingAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING, false));
            }
            if(this.isDoubleSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DOUBLE_SWING, false));
            }
            if(this.isComboAOEAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_AOE, false));
            }
            if(this.isComboShootProjectiles()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT_PATTERN, false));
            }
            if(this.isComboTrackProjectiles()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_TRACK_PROJECTILES, false));
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateModelIdle(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MODEL_IDLE, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLY_IDLE, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateIdleBook(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BOOK_IDLE, true));
        return PlayState.CONTINUE;
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
