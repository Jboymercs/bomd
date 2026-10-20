package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityAIAssassinAttack;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityBrineCreepAttackAI;
import com.dungeon_additions.da.entity.ai.flying.EntityAIRandomFlyBrine;
import com.dungeon_additions.da.entity.ai.flying.FlyingMoveHelper;
import com.dungeon_additions.da.entity.ai.flying.TimedAttackInitiator;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityGreatDeath;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntitySpearIncarnate;
import com.dungeon_additions.da.entity.dark_dungeon.minion_action.ActionCreepLaser;
import com.dungeon_additions.da.entity.flame_knight.EntityBareant;
import com.dungeon_additions.da.entity.frost_dungeon.IDirectionalRender;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.IMultiAction;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.entity.void_dungeon.voidclysm_action.ActionLazerTeleport;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityBrineCreep extends EntityDarkBase implements IAnimatable, IAnimationTickable, IPitch, IAttack, IDirectionalRender {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;
    private Vec3d chargePos;
    public Vec3d renderLazerPos;
    public Vec3d prevRenderLazerPos;
    public IMultiAction lazerAttack =  new ActionCreepLaser(this, stopLazerByte, (vec3d) -> {});
    protected boolean performLazerAttack = false;

    private static final DataParameter<Boolean> CAST_SPELL = EntityDataManager.createKey(EntityBrineCreep.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PREPARE_BITE = EntityDataManager.createKey(EntityBrineCreep.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BITE_CHARGE = EntityDataManager.createKey(EntityBrineCreep.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHOOT_LASER = EntityDataManager.createKey(EntityBrineCreep.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityBrineCreep.class, DataSerializers.FLOAT);
    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityBrineCreep.class, DataSerializers.BLOCK_POS);
    private static final DataParameter<Boolean> HAS_SPAWN = EntityDataManager.createKey(EntityBrineCreep.class, DataSerializers.BOOLEAN);

    private final String ANIM_IDLE = "idle_anim";
    private final String ANIM_IDLE_TAIL = "idle_tendrils";
    private final String ANIM_SHOOT_LASER = "shoot_laser";
    private final String ANIM_PREPARE_BITE = "prepare_bite";
    private final String ANIM_CHARGE_BITE = "bite_charge";
    private final String ANIM_CAST_SPELL = "cast_spell";

    public boolean standbyOnVel = false;
    public boolean clearCurrentVelocity = false;
    public boolean facingTarget = false;
    protected static final byte stopLazerByte = 70;

    private boolean isCastSpell() {
        return this.dataManager.get(CAST_SPELL);
    }
    private void setCastSpell(boolean value) {
        this.dataManager.set(CAST_SPELL, Boolean.valueOf(value));
    }
    private boolean isHasSpawn() {
        return this.dataManager.get(HAS_SPAWN);
    }
    private void setHasSpawn(boolean value) {
        this.dataManager.set(HAS_SPAWN, Boolean.valueOf(value));
    }
    private boolean isPrepareBite() {
        return this.dataManager.get(PREPARE_BITE);
    }
    private void setPrepareBite(boolean value) {
        this.dataManager.set(PREPARE_BITE, Boolean.valueOf(value));
    }
    private boolean isBiteCharge() {
        return this.dataManager.get(BITE_CHARGE);
    }
    private void setBiteCharge(boolean value) {
        this.dataManager.set(BITE_CHARGE, Boolean.valueOf(value));
    }
    private boolean isShootLaser() {
        return this.dataManager.get(SHOOT_LASER);
    }
    private void setShootLaser(boolean value) {
        this.dataManager.set(SHOOT_LASER, Boolean.valueOf(value));
    }
    public BlockPos getSpawnLocation() {
        return this.dataManager.get(SPAWN_LOCATION);
    }

    public void setSpawnLocation(BlockPos pos) {
        this.dataManager.set(SPAWN_LOCATION, pos);
    }


    public EntityBrineCreep(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 20;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(1.5F, 1.5F);
        this.setNoGravity(true);
        this.falter_resistance = 0.4F;
        this.hemorrhage_resistance = 0.2F;
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
    }

    public EntityBrineCreep(World worldIn) {
        super(worldIn);
        this.experienceValue = 20;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(1.5F, 1.5F);
        this.setNoGravity(true);
        this.falter_resistance = 0.4F;
        this.hemorrhage_resistance = 0.2F;
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Shoot_Laser", this.isShootLaser());
        nbt.setBoolean("Bite_Charge", this.isBiteCharge());
        nbt.setBoolean("Prepare_Bite", this.isPrepareBite());
        nbt.setBoolean("Has_Spawn", this.isHasSpawn());
        nbt.setBoolean("Cast_Spell", this.isCastSpell());
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
        nbt.setFloat("Look", this.getPitch());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setPrepareBite(nbt.getBoolean("Prepare_Bite"));
        this.setBiteCharge(nbt.getBoolean("Bite_Charge"));
        this.setHasSpawn(nbt.getBoolean("Has_Spawn"));
        this.setShootLaser(nbt.getBoolean("Shoot_Laser"));
        this.setCastSpell(nbt.getBoolean("Cast_Spell"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(BITE_CHARGE, Boolean.valueOf(false));
        this.dataManager.register(PREPARE_BITE, Boolean.valueOf(false));
        this.dataManager.register(CAST_SPELL, Boolean.valueOf(false));
        this.dataManager.register(HAS_SPAWN, Boolean.valueOf(false));
        this.dataManager.register(SHOOT_LASER, Boolean.valueOf(false));
        //
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(28);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.31D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(35);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.6D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
      //  this.tasks.addTask(4, new EntityAIAssassinAttack<EntityDarkAssassin>(this, 1.0D, 20, 6, 0.25F));
        this.tasks.addTask(4, new EntityBrineCreepAttackAI(this, 25, 35, new TimedAttackInitiator<>(this, 100)));
        this.tasks.addTask(6, new EntityAIRandomFlyBrine(this, 200, 16));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!world.isRemote) {
            if(!this.isHasSpawn()) {
                this.setSpawnLocation(new BlockPos(this.posX, this.posY + 10, this.posZ));
                this.setHasSpawn(true);
            }
            if(clearCurrentVelocity) {
                this.motionX = 0;
                this.motionY = 0;
                this.motionZ = 0;
                standbyOnVel = true;
                clearCurrentVelocity = false;
            }

            if(this.chargePos != null) {
                Vec3d vec3d = chargePos;
                ModUtils.facePosition(vec3d, this, 35, 35);
                Vec3d entityPos = this.getPositionEyes(1);
                Vec3d forwardVec = ModUtils.direction(entityPos, vec3d);
                ((IPitch) this).setPitch(forwardVec);
                double speed = this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue();
                Vec3d direction = vec3d.subtract(this.getPositionVector()).normalize();
                ModUtils.addEntityVelocity(this, direction.scale(0.45f * speed));
                AxisAlignedBB box = this.getEntityBoundingBox();
                //either explode at position or when hitting a block
                if(this.getDistance(chargePos.x, chargePos.y, chargePos.z) <= 1.5D || ModUtils.collisionNearby(box, world, this)) {
                    //Do explosion
                    this.setBiteCharge(false);
                    this.setFightMode(false);
                    DamageSource source = ModDamageSource.builder()
                            .type(ModDamageSource.EXPLOSION)
                            .directEntity(this)
                            .stoppedByArmorNotShields().disablesShields().build();
                    ModUtils.handleAreaImpact(5, (e) -> this.getAttack(), this, this.getPositionVector().add(ModUtils.yVec(1)), source, 0, 0, false, 1.5F);
                    this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f + ModRand.getFloat(0.1f));
                    world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
                    this.lockLook = false;
                    this.facingTarget = false;
                    this.chargePos = null;
                }
            }
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!world.isRemote && performLazerAttack) {
            lazerAttack.update();
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);

        if (id == stopLazerByte) {
            this.renderLazerPos = null;
        }
        if (id == ModUtils.THIRD_PARTICLE_BYTE) {
            ModUtils.performNTimes(50, (i) -> {
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + ModRand.getFloat(5), this.posY + ModRand.getFloat(5),
                        this.posZ + ModRand.getFloat(5), 0, 0, 0);
            });
        }

        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(2, (int) (18), (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Vec3d posToo = this.getPositionVector().add(0, 0.8, 0);
                Vec3d vel = pos.normalize().scale(Math.abs(ModRand.getFloat(0.25F) + 0.25)).add(ModUtils.yVec(0));
                Main.proxy.spawnParticle(45, world, posToo.x, posToo.y, posToo.z, vel.x, -0.06, vel.z);
            });
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> close_attacks = new ArrayList<>(Arrays.asList(cast_spell, prepare_bite_attack, shoot_laser));
            double[] weights = {
              distance * 0.02, // Cast Spell
              prevAttacks != prepare_bite_attack && distance > 10 ? distance * 0.02 : 0, // Bite Attack
              prevAttacks != shoot_laser && distance > 5 ? distance * 0.02 : 0 //Shoot Laser
            };
            prevAttacks = ModRand.choice(close_attacks, rand, weights).next();
            prevAttacks.accept(target);
        }
        return 50;
    }

    private final Consumer<EntityLivingBase> shoot_laser = (target) -> {
      this.setFightMode(true);
      this.setShootLaser(true);
      this.facingTarget = true;
      this.performLazerAttack = true;
      this.lazerAttack.doAction();
      this.playSound(SoundsHandler.BRINE_CREEP_SHOOT_LASER, 2.0f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));

      addEvent(()-> this.performLazerAttack = false, 81);
      addEvent(()-> {
          this.facingTarget = false;
      }, 90);
      addEvent(()-> {
            this.setFightMode(false);
            this.setShootLaser(false);
      }, 100);
    };

    private final Consumer<EntityLivingBase> cast_spell = (target) -> {
      this.setFightMode(true);
      this.setCastSpell(true);
      addEvent(()-> {
          world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
          this.playSound(SoundsHandler.BLOSSOM_PETAL_WAVE, 1.5f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 15);
      addEvent(()-> {
          Vec3d targetOldPos = target.getPositionVector();
          addEvent(()-> {
              Vec3d targetedPos = target.getPositionVector();
              Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 3);
              EntityGreatDeath spearIncarnate = new EntityGreatDeath(world, this.getAttack() * 0.9F);
              int y = getSurfaceHeight(world, new BlockPos(target.posX, 0, target.posZ), (int) targetedPos.y - 5, (int) targetedPos.y + 7);
              spearIncarnate.setPosition(predictedPosition.x, y + 1, predictedPosition.z);
              world.spawnEntity(spearIncarnate);
          }, 3);
      }, 30);

      addEvent(()-> {
            this.setFightMode(false);
            this.setCastSpell(false);
      }, 40);
    };

    private final Consumer<EntityLivingBase> prepare_bite_attack = (target) -> {
      this.facingTarget = true;
      this.setPrepareBite(true);
      this.setFightMode(true);

      addEvent(()-> {
          this.facingTarget = false;
          this.lockLook = true;
          this.chargePos = target.getPositionVector();
          this.clearCurrentVelocity = true;
        this.setPrepareBite(false);
        this.setBiteCharge(true);
          this.playSound(SoundsHandler.BRINE_CREEP_CHARGE, 2.0f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 30);
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "idle_controller_tail", 0, this::predicateIdleTail));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isCastSpell()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CAST_SPELL));
            }
            if(this.isPrepareBite()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PREPARE_BITE));
            }
            if(this.isBiteCharge()) {
                event.getController().setAnimation(new AnimationBuilder().loop(ANIM_CHARGE_BITE));
            }
            if(this.isShootLaser()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SHOOT_LASER));
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if (!this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().loop(ANIM_IDLE));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdleTail(AnimationEvent<E> event) {
        if (!this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().loop(ANIM_IDLE_TAIL));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    protected boolean canDespawn() {

        // Edit this to restricting them not despawning in Dungeons
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if(!this.isImmovable()) {
            ModUtils.aerialTravel(this, strafe, vertical, forward);
        }
    }

    public int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote) {
                return currentY;
            }

            currentY--;
        }

        return 0;
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

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);

        this.dataManager.set(LOOK, clampedLook);
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.BRINE_CREEP_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.BRINE_CREEP_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.BRINE_CREEP_HURT;
    }

    @Override
    public void setRenderDirection(Vec3d dir) {
        if (this.renderLazerPos != null) {
            this.prevRenderLazerPos = this.renderLazerPos;
        } else {
            this.prevRenderLazerPos = dir;
        }
        this.renderLazerPos = dir;
    }
}
