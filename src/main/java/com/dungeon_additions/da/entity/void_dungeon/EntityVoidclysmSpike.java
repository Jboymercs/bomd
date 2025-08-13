package com.dungeon_additions.da.entity.void_dungeon;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntityVoidclysmSpike extends EntityEndBase implements IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_SHOOT = "shoot";

    private final String ANIM_SHOOT_2 = "shoot_2";
    private final String ANIM_SHOOT_3 = "shoot_3";
    private final String ANIM_SHOOT_4 = "shoot_4";
    private final String ANIM_SHOOT_5 = "shoot_5";

    protected int selection = ModRand.range(1, 5);

    private String ANIM_SELECTION_STRING;

    public EntityVoidclysmSpike(World worldIn) {
        super(worldIn);
        selectAnimationTooPlay();
        this.setSize(0.6f, 2.0f);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }
    public void selectAnimationTooPlay() {
        if(selection == 1) {
            ANIM_SELECTION_STRING = ANIM_SHOOT_2;
        } else if (selection == 2) {
            ANIM_SELECTION_STRING = ANIM_SHOOT_3;
        }else if(selection == 3) {
            ANIM_SELECTION_STRING = ANIM_SHOOT_4;
        }else if(selection == 4) {
            ANIM_SELECTION_STRING = ANIM_SHOOT_5;
        } else {
            ANIM_SELECTION_STRING = ANIM_SHOOT;
        }
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
    protected void initEntityAI() {

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

        if(MobConfig.spike_lag_reducer && this.ticksExisted == 2) {
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(20D, 5D, 20D), e -> !e.getIsInvulnerable() && (!(e instanceof EntityVoidBlossom || e instanceof EntityVoidSpike || e instanceof EntityGenericWave || e instanceof EntityMiniBlossom)));
            if(targets.isEmpty()) {
                this.setDead();
            }
        }

        if(ticksExisted == 2) {
            playSound(SoundsHandler.APPEARING_WAVE, 0.4f, 1.0f / getRNG().nextFloat() * 0.04F + 0.8F);
        }
        if(ticksExisted == 21) {
            playSound(SoundsHandler.VOID_SPIKE_SHOOT, 0.2f, 1.0f);
        }
        if(ticksExisted > 24 && ticksExisted < 30) {
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityEndBase)));

            if(!targets.isEmpty()) {
                for(EntityLivingBase base : targets) {
                    if(base != this && !(base instanceof EntityEndBase)) {
                        Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.0D));
                        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(this).build();
                        float damage = this.getAttack();
                        ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                        if (!world.isRemote) {
                            double distSq = this.getDistanceSq(base.posX, base.getEntityBoundingBox().minY, base.posZ);
                            double distance = Math.sqrt(distSq);
                            if (base.getActivePotionEffect(MobEffects.SLOWNESS) == null && distance < 1) {
                                base.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 60, 3, false, false));
                            }
                        }
                    }
                }

            }

        }

        if(ticksExisted > 34) {
            this.setDead();
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
    }
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SELECTION_STRING, false));
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
}
