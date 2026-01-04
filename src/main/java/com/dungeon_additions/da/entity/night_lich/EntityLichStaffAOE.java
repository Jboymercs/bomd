package com.dungeon_additions.da.entity.night_lich;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.night_lich.action.ActionStaffAOE;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
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

public class EntityLichStaffAOE extends EntityAbstractBase implements IAnimatable, IAnimationTickable {

    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_BASE = "base";
    private final String ANIM_BASE_1 = "base_1";
    private final String ANIM_BASE_2 = "base_2";
    private final String ANIM_BASE_3 = "base_3";
    private final String ANIM_BASE_4 = "base_4";
    private boolean isFromSky = false;

    protected int selection = ModRand.range(1, 6);
    private String ANIM_FROM_SELECTION;


    public EntityLichStaffAOE(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1f, 2.0f);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        if(isFromSky) {
            ANIM_FROM_SELECTION = "from_sky";
        } else {
            selectAnimationTooPlay();
        }
    }

    public EntityLichStaffAOE(World worldIn) {
        super(worldIn);
        this.setSize(1f, 2.0f);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        if(isFromSky) {
            ANIM_FROM_SELECTION = "from_sky";
        } else {
            selectAnimationTooPlay();
        }
    }

    public EntityLichStaffAOE(World worldIn, boolean isFromSky) {
        super(worldIn);
        this.isFromSky = isFromSky;
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(1f, 2.0f);
        if(isFromSky) {
            ANIM_FROM_SELECTION = "from_sky";
        } else {
            selectAnimationTooPlay();
        }
    }

    public EntityPlayer owner;
    public float damageFromOwner;

    public EntityLichStaffAOE(World worldIn, EntityPlayer owner, float damage) {
        super(worldIn);
        this.isFromSky = false;
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(1f, 2.0f);
        this.owner = owner;
        this.damageFromOwner = damage;
        if(isFromSky) {
            ANIM_FROM_SELECTION = "from_sky";
        } else {
            selectAnimationTooPlay();
        }
    }

    private boolean impactParticles = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(this.isFromSky) {

            if(ticksExisted == 7) {
                this.playSound(SoundsHandler.LICH_STAFF_IMPACT, 1.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.5f));
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
                //damage Entities
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityLichStaffAOE || e instanceof EntityNightLich | e instanceof EntityMob)));

                if(!targets.isEmpty()) {
                    for(EntityLivingBase base : targets) {
                        if(base != this && !(base instanceof EntityNightLich) && !(base instanceof EntityMob)) {
                            Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.0D));
                            DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MAGIC).directEntity(this).build();
                            float damage = this.getAttack();
                            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
                        }
                    }

                }
            }
            //summons the AOE
            if(ticksExisted == 10) {
                new ActionStaffAOE().performAction(this, this);
            }

            if(ticksExisted == 85) {
                this.setDead();
            }

        } else {
            //sets particles to spawn at there locations
            if(ticksExisted == 1) {
                this.impactParticles = true;
            }
            if(ticksExisted == 11) {
                this.impactParticles = false;
            }
            if(ticksExisted == 13 && rand.nextInt(3) == 0) {
                this.playSound(SoundsHandler.LICH_SHOOT_STAFF, 0.8f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            }

            if(ticksExisted == 18) {
                //damage Entities
                if(owner != null) {
                    List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityPlayer)));
                    if(!targets.isEmpty()) {
                        for(EntityLivingBase base : targets) {
                            if(base != this && base != owner) {
                                Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.0D));
                                DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.PLAYER).directEntity(owner).build();
                                float damage = damageFromOwner;
                                ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.8f, 0, false);
                            }
                        }

                    }
                } else {
                    List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.1), e -> !e.getIsInvulnerable() && (!(e instanceof EntityLichStaffAOE || e instanceof EntityNightLich | e instanceof EntityMob)));


                    if(!targets.isEmpty()) {
                        for(EntityLivingBase base : targets) {
                            if(base != this && !(base instanceof EntityNightLich) && !(base instanceof EntityMob)) {
                                Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.0D));
                                DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MAGIC).directEntity(this).build();
                                float damage = this.getAttack();
                                ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.8f, 0, false);
                            }
                        }

                    }
                }
            }

            if(ticksExisted == 44) {
                this.setDead();
            }

        }
    }


    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(this.impactParticles) {
            if(world.rand.nextInt(2)==0) {
                world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            }
        }
    }


    @Override
    public void handleStatusUpdate(byte id) {
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(1, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(0, 0.5, 0), ModColors.AZURE, pos.normalize().scale(0.25).add(ModUtils.yVec(0)), ModRand.range(10, 15));
            });
        } else if (id == ModUtils.SECOND_PARTICLE_BYTE) {
                ParticleManager.spawnDust(world, this.getPositionVector().add(0, 0.5, 0), ModColors.AZURE, Vec3d.ZERO, ModRand.range(10, 15));
            }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.night_lich_attack_damage * MobConfig.night_lich_staff_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }


    public void selectAnimationTooPlay() {
        if(selection == 1) {
            ANIM_FROM_SELECTION = ANIM_BASE_1;
        } else if (selection == 2) {
            ANIM_FROM_SELECTION = ANIM_BASE_2;
        }else if(selection == 3) {
            ANIM_FROM_SELECTION = ANIM_BASE_3;
        }else if(selection == 4) {
            ANIM_FROM_SELECTION = ANIM_BASE_4;
        } else if (selection == 5) {
            ANIM_FROM_SELECTION = ANIM_BASE;
        } else {
            ANIM_FROM_SELECTION = ANIM_BASE;
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "staff_controller", 0, this::predicateIdle));
    }
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FROM_SELECTION, false));
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
