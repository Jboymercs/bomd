package com.dungeon_additions.da.entity.gaelon_dungeon.apathyr;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityGaelonBase;
import com.dungeon_additions.da.entity.gaelon_dungeon.ProjectileGhost;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityDragonAOE;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
import java.util.function.Consumer;

public class EntityUltraAttack extends EntityGaelonBase implements IAnimatable, IAnimationTickable {
    private static final DataParameter<Boolean> SUMMON_STATE = EntityDataManager.createKey(EntityUltraAttack.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEATH_STATE = EntityDataManager.createKey(EntityUltraAttack.class, DataSerializers.BOOLEAN);
    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;

    private final String ANIM_IDLE = "idle";
    private final String ANIM_SUMMON = "summon";
    private final String ANIM_DEATH = "death";

    private void setSummonState(boolean value) {this.dataManager.set(SUMMON_STATE, Boolean.valueOf(value));}
    private boolean isSummonState() {return this.dataManager.get(SUMMON_STATE);}
    private void setDeathState(boolean value) {this.dataManager.set(DEATH_STATE, Boolean.valueOf(value));}
    private boolean isDeathState() {return this.dataManager.get(DEATH_STATE);}

    public EntityUltraAttack(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.doSummon();
        this.setImmovable(true);
        this.noClip = true;
        this.setSize(1F, 4.0F);
    }

    public EntityUltraAttack(World worldIn) {
        super(worldIn);
        this.doSummon();
        this.setImmovable(true);
        this.noClip = true;
        this.setSize(1F, 4.0F);
    }

    private EntityApathyr parent;

    public EntityUltraAttack(World worldIn, EntityApathyr parent) {
        super(worldIn);
        this.doSummon();
        this.setImmovable(true);
        this.noClip = true;
        this.parent = parent;
        this.setSize(1F, 4.0F);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.apathyr_damage * 0.8);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }

    private void doSummon() {
        this.setSummonState(true);
        addEvent(()-> {
                this.setSummonState(false);
        }, 20);
    }

    private void doDeath() {
        this.setDeathState(true);
        addEvent(()-> {
            this.setDeathState(false);
        }, 20);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Summon", this.isSummonState());
        nbt.setBoolean("Death_State", this.isDeathState());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSummonState(nbt.getBoolean("Summon"));
        this.setDeathState(nbt.getBoolean("Death_State"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(SUMMON_STATE, Boolean.valueOf(true));
        this.dataManager.register(DEATH_STATE, Boolean.valueOf(false));
        super.entityInit();
    }

    private int hitDelay = 0;
    boolean wasDifferent = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.rotationPitch = 0;
        hitDelay--;

        if(this.ticksExisted == 1) {
            this.playSound(SoundsHandler.APATHYR_DOMAIN_IDLE, 2.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
        }

        if(world.rand.nextInt(3) == 0) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }

        if(world.rand.nextInt(2) == 0) {
            world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
        }

        if(parent != null) {
            EntityLivingBase target = this.parent.getAttackTarget();
            if(target != null) {
                this.faceEntity(target, 30F, 0F);
            }
        }

        //spawn a projectile towards the player
        if(ticksExisted % 20 == 0 && ticksExisted < 190 && !world.isRemote) {
            if(parent != null) {
                EntityLivingBase target = this.parent.getAttackTarget();
                if(target != null) {
                    if(wasDifferent) {
                        this.ActionDoPhalanxOne(target);
                        wasDifferent = false;
                    } else {
                        this.ActionDoPhalanxTwo(target);
                        wasDifferent = true;
                    }
                }
            }
        }

        //hurt nearbyPlayers
        if(ticksExisted > 10 && ticksExisted < 200 && hitDelay < 0) {
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(2.3), e -> !e.getIsInvulnerable());
            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase base : nearbyEntities) {
                    if(!(base instanceof EntityGaelonBase) && hitDelay <= 0) {
                        Vec3d pos = base.getPositionVector().add(ModUtils.yVec(0.5));
                        DamageSource source = ModDamageSource.builder()
                                .type(ModDamageSource.MOB)
                                .directEntity(this).disablesShields()
                                .build();
                        float damage = (float) (this.getAttack());
                        ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, pos, source, 0.9F, 0, false);
                        hitDelay = 5;
                    }
                }
            }
        }

        if(ticksExisted == 180) {
            this.doDeath();
        }
        if(ticksExisted == 200) {
            this.setDead();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }


    private void ActionDoPhalanxOne(EntityLivingBase target) {
        Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 4.2, 0)));

        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0, 0)));
        this.playSound(SoundsHandler.APATHYR_CAST_HEAVY, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        Vec3d fromTargetTooActor = this.getPositionVector().subtract(targetPos);
        Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, ModRand.range(-10, 10)).normalize().scale(4);
        Vec3d lineStart = targetPos.subtract(lineDir);
        Vec3d lineEnd = targetPos.add(lineDir);
        ModUtils.lineCallback(lineStart, lineEnd, 5, (pos, i) -> {
            ProjectileGhost missile = new ProjectileGhost(this.world, this, (float) (this.getAttack() * 0.8));
            missile.setPosition(relPos.x, relPos.y, relPos.z);
            missile.setTravelRange(40);
            float speed = (float) 0.65;
            missile.rotationPitch = 0;
            missile.rotationYaw = this.rotationYaw;
            world.spawnEntity(missile);
            ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
        });
    }

    private void ActionDoPhalanxTwo(EntityLivingBase target) {
        Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 4.2, 0)));

        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0, 0)));
        this.playSound(SoundsHandler.APATHYR_CAST_MAGIC, 1.25f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        Vec3d fromTargetTooActor = this.getPositionVector().subtract(targetPos);
        Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, ModRand.range(-10, 10)).normalize().scale(5);
        Vec3d lineStart = targetPos.subtract(lineDir);
        Vec3d lineEnd = targetPos.add(lineDir);
        ModUtils.lineCallback(lineStart, lineEnd, 3, (pos, i) -> {
            ProjectileGhost missile = new ProjectileGhost(this.world, this, (float) (this.getAttack() * 0.8));
            missile.setPosition(relPos.x, relPos.y, relPos.z);
            missile.setTravelRange(40);
            float speed = (float) 0.65;
            missile.rotationPitch = 0;
            missile.rotationYaw = this.rotationYaw;
            world.spawnEntity(missile);
            ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
        });
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "states_controller", 0, this::predicateStates));
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    private <E extends IAnimatable> PlayState predicateStates(AnimationEvent<E> event) {
        if(this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DEATH, false));
            return PlayState.CONTINUE;
        }
        if(this.isSummonState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isSummonState() && !this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            Main.proxy.spawnParticle(15, world, posX + + ModRand.range(-5, 4), posY + ModRand.range(1, 5), posZ + ModRand.range(-5, 4), world.rand.nextFloat()/6 - world.rand.nextFloat()/6, 0.04, world.rand.nextFloat()/6 - world.rand.nextFloat()/6, 100);
        }

        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.performNTimes(5, (i) -> {
            Main.proxy.spawnParticle(17, this.posX + ModRand.range(-9, 8), this.posY + ModRand.range(1, 3), this.posZ + ModRand.range(-9, 8), 0, 0.08, 0, 20);
            });
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
