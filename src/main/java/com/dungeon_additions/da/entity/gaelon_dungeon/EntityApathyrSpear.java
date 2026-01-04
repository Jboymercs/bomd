package com.dungeon_additions.da.entity.gaelon_dungeon;

import com.dungeon_additions.da.Main;
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
import net.minecraft.entity.player.EntityPlayer;
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

public class EntityApathyrSpear extends EntityGaelonBase implements IAnimatable, IAnimationTickable {

    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_SHOOT = "summon";

    private final String ANIM_SHOOT_2 = "summon2";
    private final String ANIM_SHOOT_3 = "summon3";
    private final String ANIM_SHOOT_4 = "summon4";
    private final String ANIM_SHOOT_5 = "summon5";

    protected int selection = ModRand.range(1, 5);

    private String ANIM_SELECTION_STRING;

    public EntityApathyrSpear(World worldIn) {
        super(worldIn);
        selectAnimationTooPlay();
        this.setSize(0.6f, 2.7f);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    private EntityPlayer player;
    private float damageIn;

    public EntityApathyrSpear(World worldIn, EntityPlayer owner, float damage) {
        super(worldIn);
        this.player = owner;
        this.damageIn = damage;
        selectAnimationTooPlay();
        this.setSize(0.6f, 2.7f);
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
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.apathyr_damage * 0.8);
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

        if(ticksExisted == 1) {
            playSound(SoundsHandler.APATHYR_SUMMON_SPEAR, 0.4f, 1.0f / getRNG().nextFloat() * 0.04F + 0.8F);
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
        if(ticksExisted > 17 && ticksExisted < 35) {
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityVoidBlossom || e instanceof EntityVoidSpike || e instanceof EntityGenericWave)));

            if(player != null) {
                if(!targets.isEmpty()) {
                    for(EntityLivingBase base : targets) {
                        if(base != this && base != player) {
                            Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.0D));
                            DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.PLAYER).directEntity(player).build();
                            ModUtils.handleAreaImpact(0.5f, (e) -> damageIn, this, offset, source, 0.2f, 0, false);
                        }
                    }

                }
            } else {
                if(!targets.isEmpty()) {
                    for(EntityLivingBase base : targets) {
                        if(base != this && !(base instanceof EntityVoidBlossom) && !(base instanceof EntityMiniBlossom)) {
                            Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.0D));
                            DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MAGIC).directEntity(this).build();
                            float damage = this.getAttack();
                            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
                        }
                    }

                }
            }

        }

        if(ticksExisted > 45) {
            this.setDead();
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if(id == ModUtils.PARTICLE_BYTE) {
            Main.proxy.spawnParticle(16, this.posX, this.posY + 0.5, this.posZ, 0, 0, 0, 20);
        }
        super.handleStatusUpdate(id);
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

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
