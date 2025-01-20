package com.dungeon_additions.da.entity.frost_dungeon;

import com.dungeon_additions.da.animation.IAnimatedEntity;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.ActionWyrkFootAOE;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
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

import java.util.List;

public class EntityWyrkFoot extends EntityFrostBase implements IAnimatable, IAnimationTickable {

    private final String ANIM_SUMMON = "summon";

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityWyrkFoot(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(0.8F, 1.8F);
    }

    public EntityWyrkFoot(World worldIn) {
        super(worldIn);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(0.8F, 1.8F);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.blossom_attack_damange * 0.75);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(this.ticksExisted == 2) {
            //play sounds
            this.playSound(SoundsHandler.BIG_WYRK_FOOT_IMPACT, 1.5f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }

        if(this.ticksExisted == 25) {
            new ActionWyrkFootAOE().performAction(this, this);
        }

        if(this.ticksExisted == 40) {
            this.setDead();
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
    }
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
        return PlayState.CONTINUE;
    }


    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
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
