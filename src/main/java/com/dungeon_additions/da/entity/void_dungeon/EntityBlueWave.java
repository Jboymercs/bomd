package com.dungeon_additions.da.entity.void_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
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

public class EntityBlueWave extends EntityEndBase implements IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);
    private static final DataParameter<Boolean> RED_STYLE = EntityDataManager.createKey(EntityBlueWave.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLAME_STYLE = EntityDataManager.createKey(EntityBlueWave.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PURPLE_STYLE = EntityDataManager.createKey(EntityBlueWave.class, DataSerializers.BOOLEAN);
    public boolean isRedStyle() {return this.dataManager.get(RED_STYLE);}
    public void setRedStyle(boolean value) {this.dataManager.set(RED_STYLE, Boolean.valueOf(value));}
    public boolean isFlameStyle() {return this.dataManager.get(FLAME_STYLE);}
    public void setFlameStyle(boolean value) {this.dataManager.set(FLAME_STYLE, Boolean.valueOf(value));}

    public boolean isPurpleStyle() {return this.dataManager.get(PURPLE_STYLE);}
    public void setPurpleStyle(boolean value) {this.dataManager.set(PURPLE_STYLE, Boolean.valueOf(value));}
    private final String ANIM_SHOOT = "shoot";
    public EntityBlueWave(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.isImmuneToFire = true;
        this.setSize(0.6f, 2.0f);
        this.noClip = true;
    }
    public EntityBlueWave(World worldIn, boolean isFlame, boolean isRed, boolean isPurple) {
        super(worldIn);
        this.setImmovable(true);
        this.setFlameStyle(isFlame);
        this.setRedStyle(isRed);
        this.setPurpleStyle(isPurple);
        this.setSize(0.8f, 3.0f);
        this.isImmuneToFire = true;
        this.noClip = true;
    }

    public EntityBlueWave(World worldIn, EntityPlayer owner, float damage) {
        super(worldIn);
        this.setFlameStyle(false);
        this.setPurpleStyle(false);
        this.setRedStyle(false);
        this.setSize(0.8f, 3.0f);
        this.noClip = true;
        this.owner = owner;
        this.damageFromOwner = damage;
        this.isImmuneToFire = true;
        this.setImmovable(true);
    }

    public EntityBlueWave(World worldIn, boolean isFlame, boolean isRed, boolean isPurple, EntityPlayer owner, float damage) {
        super(worldIn);
        this.setImmovable(true);
        this.setFlameStyle(isFlame);
        this.setRedStyle(isRed);
        this.setPurpleStyle(isPurple);
        this.setSize(0.8f, 3.0f);
        this.damageFromOwner = damage;
        this.owner = owner;
        this.isImmuneToFire = true;
        this.noClip = true;
    }

    private boolean enableDamage;

    public EntityBlueWave(World worldIn, boolean enableDamage, boolean isPurple) {
        super(worldIn);
        this.setFlameStyle(false);
        this.setPurpleStyle(isPurple);
        this.setRedStyle(false);
        this.isImmuneToFire = true;
        this.enableDamage = enableDamage;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Flame_Style", this.isFlameStyle());
        nbt.setBoolean("Red_Style", this.isRedStyle());
        nbt.setBoolean("Purple_Style", this.isPurpleStyle());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setFlameStyle(nbt.getBoolean("Flame_Style"));
        this.setRedStyle(nbt.getBoolean("Red_Style"));
        this.setPurpleStyle(nbt.getBoolean("Purple_Style"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(RED_STYLE, Boolean.valueOf(false));
        this.dataManager.register(FLAME_STYLE, Boolean.valueOf(false));
        this.dataManager.register(PURPLE_STYLE, Boolean.valueOf(false));
        super.entityInit();
    }

    private boolean hasSentParticles = false;
    private EntityPlayer owner;
    private float damageFromOwner;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(ticksExisted > 20 && ticksExisted < 27) {
            if(this.isRedStyle()) {
                if(!hasSentParticles) {
                    world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
                    this.hasSentParticles = true;
                }
            } else if (this.isFlameStyle()) {
                world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            } else if (this.isPurpleStyle()) {
                if(!hasSentParticles) {
                    world.setEntityState(this, ModUtils.FIFTH_PARTICLE_BYTE);
                    this.hasSentParticles = true;
                }
            }else {
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
                if(!hasSentParticles) {
                    world.setEntityState(this, ModUtils.FOURTH_PARTICLE_BYTE);
                    this.hasSentParticles = true;
                }
            }
        }
        if(ticksExisted == 20) {

            if (this.isFlameStyle()) {
                if(this.rand.nextInt(5) == 0) {
                    this.playSound(SoundsHandler.OBSIDILITH_BURST, 0.2f, 1.0f);
                }
                //player owned spell
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(0.9D), e -> !e.getIsInvulnerable() && (!(e instanceof EntityVoidBlossom || e instanceof EntityVoidSpike || e instanceof EntityGenericWave)));

                if(owner != null) {
                    if (!targets.isEmpty()) {
                        for (EntityLivingBase base : targets) {
                            if (base != this && !(base instanceof EntityEndBase) && base != null && base != owner) {
                                Vec3d offset = base.getPositionVector().add(ModUtils.yVec(0.25D));
                                DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MAGIC).directEntity(this).build();
                                float damage = this.getAttack();
                                ModUtils.handleAreaImpact(0.25f, (e) -> this.damageFromOwner, this, offset, source, 0.3f, 0, false);
                                if (!world.isRemote) {
                                    base.setFire(8);
                                }
                            }
                        }
                    }
                } else {
                    if (!targets.isEmpty()) {
                        for (EntityLivingBase base : targets) {
                            if (base != this && !(base instanceof EntityEndBase) && base != null) {
                                Vec3d offset = base.getPositionVector().add(ModUtils.yVec(0.25D));
                                DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MAGIC).directEntity(this).build();
                                float damage = this.getAttack();
                                ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.3f, 0, false);
                                if (!world.isRemote) {
                                    base.setFire(8);
                                }
                            }
                        }
                    }
                }

            } else if (this.isRedStyle()) {
                if(this.rand.nextInt(5) == 0) {
                    this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.2f, 1.0f);
                }
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1D), e -> !e.getIsInvulnerable() && (!(e instanceof EntityVoidBlossom || e instanceof EntityVoidSpike || e instanceof EntityGenericWave)));

                if (!targets.isEmpty()) {
                    for (EntityLivingBase base : targets) {
                        if (base != this && !(base instanceof EntityEndBase) && base != null) {
                            Vec3d offset = base.getPositionVector().add(ModUtils.yVec(0.25D));
                            DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MAGIC).directEntity(this).build();
                            float damage = this.getAttack();
                            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 1.3f, 0, false);
                        }
                    }
                }
            }  else if (this.isPurpleStyle()) {
            //nothing occurs on impact
                if(this.enableDamage) {
                    if(this.rand.nextInt(5) == 0) {
                        this.playSound(SoundsHandler.SPORE_IMPACT, 0.75f, 1.0f);
                    }
                    List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(0.9D), e -> !e.getIsInvulnerable() && (!(e instanceof EntityVoidBlossom || e instanceof EntityVoidSpike || e instanceof EntityGenericWave)));

                    if (!targets.isEmpty()) {
                        for (EntityLivingBase base : targets) {
                            if (base != this && !(base instanceof EntityEndBase) && base != null) {
                                Vec3d offset = base.getPositionVector().add(ModUtils.yVec(0.25D));
                                DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MAGIC).directEntity(this).build();
                                float damage = this.getAttack();
                                ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                            }
                        }
                    }
                }
                //Player Owned Spell
            }else if (owner != null){
                this.playSound(SoundsHandler.OBSIDILITH_BURST, 0.2f, 1.0f);
                if(rand.nextInt(4) == 0) {
                    this.playSound(SoundsHandler.OBSIDILITH_WAVE_DING, 0.6f, 1.0f);
                }
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(0.7D), e -> !e.getIsInvulnerable() && (!(e == owner)));

                if (!targets.isEmpty()) {
                    for (EntityLivingBase base : targets) {
                        if (base != this && base != owner && base != null) {
                            Vec3d offset = base.getPositionVector().add(ModUtils.yVec(0.25D));
                            DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MAGIC).directEntity(this).build();
                            float damage = damageFromOwner;
                            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
                            base.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 2, false, false));
                            if (!world.isRemote) {
                                double distSq = this.getDistanceSq(base.posX, base.getEntityBoundingBox().minY, base.posZ);
                                double distance = Math.sqrt(distSq);
                                if (base.getActivePotionEffect(MobEffects.SLOWNESS) == null && distance < 0.9) {
                                    base.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 2, false, false));
                                }
                            }
                        }
                    }
                }

            } else {
                this.playSound(SoundsHandler.OBSIDILITH_BURST, 0.2f, 1.0f);
                if(rand.nextInt(4) == 0) {
                    this.playSound(SoundsHandler.OBSIDILITH_WAVE_DING, 0.6f, 1.0f);
                }
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(0.8D), e -> !e.getIsInvulnerable() && (!(e instanceof EntityVoidBlossom || e instanceof EntityVoidSpike || e instanceof EntityGenericWave)));

                if (!targets.isEmpty()) {
                    for (EntityLivingBase base : targets) {
                        if (base != this && !(base instanceof EntityEndBase) && base != null) {
                            Vec3d offset = base.getPositionVector().add(ModUtils.yVec(0.25D));
                            DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MAGIC).directEntity(this).build();
                            float damage = this.getAttack();
                            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
                            base.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 2, false, false));
                            if (!world.isRemote) {
                                double distSq = this.getDistanceSq(base.posX, base.getEntityBoundingBox().minY, base.posZ);
                                double distance = Math.sqrt(distSq);
                                if (base.getActivePotionEffect(MobEffects.SLOWNESS) == null && distance < 1) {
                                    base.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 2, false, false));
                                }
                            }
                        }
                    }
                }

            }
        }
        if(ticksExisted == 30) {
            this.setDead();
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(0.5D)), ModColors.AZURE, new Vec3d(0,0,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(1.5D)), ModColors.AZURE, new Vec3d(0,0,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(3.0D)), ModColors.AZURE, new Vec3d(0,0,0));
        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            world.spawnParticle(EnumParticleTypes.FLAME, this.posX, this.posY + 0.5, this.posZ, 0, 0, 0, ModRand.range(20, 75));
            world.spawnParticle(EnumParticleTypes.FLAME, this.posX, this.posY + 1.5, this.posZ, 0, 0, 0, ModRand.range(20, 75));
            world.spawnParticle(EnumParticleTypes.FLAME, this.posX, this.posY + 2.5, this.posZ, 0, 0, 0, ModRand.range(20, 75));
            world.spawnParticle(EnumParticleTypes.FLAME, this.posX, this.posY + 3.5, this.posZ, 0, 0, 0, ModRand.range(20, 75));
        }
        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            Main.proxy.spawnParticle(5, this.posX, this.posY + 0.5, this.posZ, 0,0.05,0);
            Main.proxy.spawnParticle(5, this.posX, this.posY + 1.5, this.posZ, 0,0.05,0);
            Main.proxy.spawnParticle(5, this.posX, this.posY + 2.5, this.posZ, 0,0.05,0);
            Main.proxy.spawnParticle(5, this.posX, this.posY + 3.5, this.posZ, 0,0.05,0);
        }
        if(id == ModUtils.FOURTH_PARTICLE_BYTE) {
            Main.proxy.spawnParticle(6, this.posX, this.posY + 0.25 + ModRand.getFloat(2.5F), this.posZ, 0,0,0);
        }
        if(id == ModUtils.FIFTH_PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(0.5D)), ModColors.LIGHT_PURPLE, new Vec3d(0,0,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(1.5D)), ModColors.LIGHT_PURPLE, new Vec3d(0,0,0));
        }
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.obsidilith_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "wave_controller", 0, this::predicateIdle));
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT, false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
