package com.dungeon_additions.da.entity.void_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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

public class EntityVoidBlackHole extends EntityEndBase implements IAnimatable, IScreenShake {

    private int shakeTime = 0;
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityVoidBlackHole.class, DataSerializers.BOOLEAN);

    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_SHOOT = "shoot";


    private int blackHoleTime = 0;


    public EntityVoidBlackHole(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.0F, 1.0F);
        this.setImmovable(true);
        this.setNoGravity(true);
        this.noClip = true;
    }

    public EntityVoidBlackHole(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setImmovable(true);
        this.setNoGravity(true);
        this.noClip = true;
    }

    public EntityVoidBlackHole(World worldIn, EntityPlayer immunePlayer, float damage) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setImmovable(true);
        this.setNoGravity(true);
        this.noClip = true;
        this.immunePlayer = immunePlayer;
        this.damageFromPlayer = damage;
    }

    private float damageFromPlayer;
    private EntityPlayer immunePlayer;

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Shaking", this.isShaking());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setShaking(nbt.getBoolean("Shaking"));
    }

    private int attackTime = 0;
    boolean flag;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;
        this.shakeTime--;
        blackHoleTime++;
       attackTime--;

       if(ticksExisted == 20) {
           this.setShaking(true);
           this.shakeTime = 140;
       }

        if(blackHoleTime < 56) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }

        if(blackHoleTime > 50) {
            world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
        }

        if(ticksExisted == 1) {
            this.playSound(SoundsHandler.VOIDCLYSM_BLACK_HOLE, 2.0F, 1.0F);
        }


        if(!world.isRemote) {
            double blackholeSize = (double) (blackHoleTime * 2)/16;

            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(blackholeSize), e -> !e.getIsInvulnerable() && (!(e instanceof EntityEndBase)));
            if(!targets.isEmpty()) {
                if (immunePlayer != null) {
                    for (EntityLivingBase target : targets) {
                        if (!(target instanceof EntityEndBase) && target != immunePlayer) {

                            //do hurt damage
                            if (this.getDistance(target) < blackholeSize / 2) {
                                if (attackTime < 0) {
                                    target.setHealth((float) target.getHealth() - 1);
                                    target.hurtResistantTime = 0;
                                    DamageSource source = ModDamageSource.builder()
                                            .type(ModDamageSource.MAGIC)
                                            .directEntity(this)
                                            .stoppedByArmorNotShields().build();

                                    ModUtils.handleAreaImpact(0.5F, (e) -> 0f, this, target.getPositionVector(), source);
                                    flag = true;
                                }
                            }

                            //draw Player In
                            if (target.canBePushed() && !target.isBeingRidden()) {
                                double d0 = (this.posX - target.posX) * 0.0006;
                                double d1 = (this.posY - target.posY) * 0.0006;
                                double d2 = (this.posZ - target.posZ) * 0.0006;
                                target.motionX += d0 * blackholeSize;
                                target.motionY += d1 * blackholeSize;
                                target.motionZ += d2 * blackholeSize;
                                target.velocityChanged = true;
                                //  target.addVelocity(d0 * blackholeSize, d1 * blackholeSize, d2 * blackholeSize);
                            }
                        }
                    }

                    if (flag) {
                        attackTime = 10;
                        flag = false;
                    }
                } else {
                    for (EntityLivingBase target : targets) {
                        if (!(target instanceof EntityEndBase)) {

                            //do hurt damage
                            if (this.getDistance(target) < blackholeSize / 2) {
                                if (attackTime < 0) {
                                    target.setHealth((float) target.getHealth() - 1);
                                    target.hurtResistantTime = 0;
                                    DamageSource source = ModDamageSource.builder()
                                            .type(ModDamageSource.MAGIC)
                                            .directEntity(this)
                                            .stoppedByArmorNotShields().build();

                                    ModUtils.handleAreaImpact(0.5F, (e) -> 0f, this, target.getPositionVector(), source);
                                    flag = true;
                                }
                            }

                            //draw Player In
                            if (target.canBePushed() && !target.isBeingRidden()) {
                                double d0 = (this.posX - target.posX) * 0.0006;
                                double d1 = (this.posY - target.posY) * 0.0006;
                                double d2 = (this.posZ - target.posZ) * 0.0006;
                                target.motionX += d0 * blackholeSize;
                                target.motionY += d1 * blackholeSize;
                                target.motionZ += d2 * blackholeSize;
                                target.velocityChanged = true;
                                //  target.addVelocity(d0 * blackholeSize, d1 * blackholeSize, d2 * blackholeSize);
                            }
                        }
                    }

                    if (flag) {
                        attackTime = 10;
                        flag = false;
                    }
                }
            }
        }

        if(this.ticksExisted == 140) {
            if(immunePlayer != null) {

                if(damageFromPlayer * .66 > ModConfig.void_hammer_blackhole_damage_cap) {
                    damageFromPlayer = ModConfig.void_hammer_blackhole_damage_cap;
                } else {
                    damageFromPlayer = damageFromPlayer * 0.66F;
                }

                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.MOB)
                        .directEntity(this)
                        .stoppedByArmorNotShields().build();

                ModUtils.handleAreaImpact(9F, (e) -> damageFromPlayer, this, this.getPositionVector(), source);
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 3.0f, 1.0f + ModRand.getFloat(0.1f));
                world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
            } else {
                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.MOB)
                        .directEntity(this)
                        .stoppedByArmorNotShields().build();

                ModUtils.handleAreaImpact(9F, (e) -> this.getAttack(), this, this.getPositionVector(), source);
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 3.0f, 1.0f + ModRand.getFloat(0.1f));
                world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
            }
    }

        if(ticksExisted == 150) {
            this.setDead();
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.voidclysm_attack_damage * 2);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
    }

    @Override
    protected void initEntityAI() {

    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);

        if(id == ModUtils.PARTICLE_BYTE) {
            int number = 18 - blackHoleTime/3;
            ModUtils.performNTimes( number, (i) -> {
                Vec3d randomPos = this.getPositionVector().add(ModRand.range(-18, 18), ModRand.range(-2, 3), ModRand.range(-18, 18));
                Main.proxy.spawnParticle(9, world, randomPos.x , randomPos.y, randomPos.z, 0, -0.02, 0, ModRand.range(40, 50));
            });
        }

        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.performNTimes( 10, (i) -> {
                double blackholeSize = (double) (blackHoleTime * 2)/16;
                Vec3d randomPos = this.getPositionVector().add(ModRand.range((int) -blackholeSize, (int) blackholeSize), ModRand.range(-2, 3), ModRand.range((int) -blackholeSize, (int) blackholeSize));
                Vec3d motion = this.getPositionVector().subtract(randomPos).scale(0.2);
                Main.proxy.spawnParticle(9, world, randomPos.x , randomPos.y, randomPos.z, motion.x, motion.y, motion.z, ModRand.range(60, 70));
            });
        }

        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            ModUtils.performNTimes( 100, (i) -> {
                Vec3d randomPos = this.getPositionVector().add(ModRand.range(-9, 9), ModRand.range(-2, 3), ModRand.range(-9, 9));
                Main.proxy.spawnParticle(9, world, randomPos.x , randomPos.y, randomPos.z, 0, 0.02, 0, ModRand.range(90, 130));
            });
        }
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT, false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
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
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 20.0F + 0.2F);
            if (dist >= 20.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.7F * screamMult);
        }
        return 0;
    }
}
