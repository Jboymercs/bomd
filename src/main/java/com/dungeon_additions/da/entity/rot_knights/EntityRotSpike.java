package com.dungeon_additions.da.entity.rot_knights;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
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

public class EntityRotSpike extends EntityAbstractBase implements IAnimatable, IAnimationTickable {
    private AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_SUMMON = "summon";

    public EntityRotSpike(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.6f, 1.0f);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public EntityRotSpike(World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 0.75f);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.fallen_attack_damage * 0.8);
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

        if(ticksExisted == 19) {
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityRotKnightBoss)));

            if(rand.nextInt(3) == 0) {
                this.playSound(SoundsHandler.ROT_SPIKE_SHOOT, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
            }
            if(!targets.isEmpty()) {
                for(EntityLivingBase base : targets) {
                    if(base != this && !(base instanceof EntityRotKnightBoss)) {
                        Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.0D));
                        DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
                        float damage = this.getAttack();
                        ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
                    }
                }

            }

        }

        if (ticksExisted > 36) {
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
