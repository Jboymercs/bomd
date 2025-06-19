package com.dungeon_additions.da.entity.flame_knight;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.ai.flame_dungeon.EntityBareantAI;
import com.dungeon_additions.da.entity.ai.flame_dungeon.EntityVolatileSpiritAI;
import com.dungeon_additions.da.entity.ai.flying.TimedAttackInitiator;
import com.dungeon_additions.da.entity.flame_knight.misc.ActionFlameSling;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileFlameSling;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileTrackingFlame;
import com.dungeon_additions.da.entity.flame_knight.volatile_action.ActionFlameWave;
import com.dungeon_additions.da.entity.flame_knight.volatile_action.ActionSecondFlameWave;
import com.dungeon_additions.da.entity.frost_dungeon.EntityAbstractGreatWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityEliteDraugr;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.ServerScaleUtil;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import scala.tools.nsc.backend.icode.Primitives;
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
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityVolatileSpirit extends EntityFlameBase implements IAnimatable, IAnimationTickable, IAttack, IScreenShake {

    private Consumer<EntityLivingBase> prevAttacks;

    Supplier<Projectile> flame_sling_projectiles = () -> new ProjectileFlameSling(world, this, 13, null);
    private AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_IDLE = "idle";
    private final String ANIM_BODY_IDLE = "body_idle";
    private final String ANIM_SHIELD_IDLE = "shield_idle";
    private final String ANIM_ORB_IDLE = "orb_idle";

    //Attacks
    private final String ANIM_SWING_BEGIN = "simple_swing";
    private final String ANIM_SWING_CONTINUE = "simple_swing_continue";
    private final String ANIM_SWING_FINISH = "simple_swing_finish";

    private final String ANIM_DASH = "dash";
    private final String ANIM_DASH_SWING = "dash_swing";
    private final String ANIM_DASH_STOMP = "dash_stomp";

    private final String ANIM_LEAP = "leap";
    private final String ANIM_LEAP_CONTINUE = "leap_continue";
    private final String ANIM_LEAP_FINISH = "leap_finish";

    private final String ANIM_FLAME_SPIT = "flame_spit";
    private final String ANIM_MORTAR_FIRE = "mortar_fire";
    private final String ANIM_FLAME_WAVE = "flame_wave";
    private final String ANIM_BUFF_SELF = "buff_self";
    private final String ANIM_BLOCK = "block";
    private final String ANIM_TRIPLE_STRIKE = "triple_strike";

    private boolean isShieldActive = false;

    private static final DataParameter<Boolean> SIMPLE_SWING = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_CONTINUE = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_FINISH = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DASH = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DASH_SWING = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DASH_STOMP = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LEAP_BEGIN = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LEAP_CONTINUE = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LEAP_FINISH = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLAME_SPIT = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MORTAR_FIRE = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLAME_WAVE = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BUFF_SELF = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLOCK_ACTION = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TRIPLE_STRIKE = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> HAD_PREVIOUS_TARGET = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);

    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BLOCK_POS);
    public static DataParameter<Boolean> SET_SPAWN_LOC = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityVolatileSpirit.class, DataSerializers.BOOLEAN);

    public void setSimpleSwing(boolean value) {this.dataManager.set(SIMPLE_SWING, Boolean.valueOf(value));}
    public void setSwingContinue(boolean value) {this.dataManager.set(SWING_CONTINUE, Boolean.valueOf(value));}
    public void setSwingFinish(boolean value) {this.dataManager.set(SWING_FINISH, Boolean.valueOf(value));}
    public void setDashBegin(boolean value) {this.dataManager.set(DASH, Boolean.valueOf(value));}
    public void setDashSwing(boolean value) {this.dataManager.set(DASH_SWING, Boolean.valueOf(value));}
    public void setDashStomp(boolean value) {this.dataManager.set(DASH_STOMP, Boolean.valueOf(value));}
    public void setLeapBegin(boolean value) {this.dataManager.set(LEAP_BEGIN, Boolean.valueOf(value));}
    public void setLeapContinue(boolean value) {this.dataManager.set(LEAP_CONTINUE, Boolean.valueOf(value));}
    public void setLeapFinish(boolean value) {this.dataManager.set(LEAP_FINISH, Boolean.valueOf(value));}
    public void setFlameSpit(boolean value) {this.dataManager.set(FLAME_SPIT, Boolean.valueOf(value));}
    public void setMortarFire(boolean value) {this.dataManager.set(MORTAR_FIRE, Boolean.valueOf(value));}
    public void setFlameWave(boolean value) {this.dataManager.set(FLAME_WAVE, Boolean.valueOf(value));}
    public void setBuffSelf(boolean value) {this.dataManager.set(BUFF_SELF, Boolean.valueOf(value));}
    public void setBlockAction(boolean value) {this.dataManager.set(BLOCK_ACTION, Boolean.valueOf(value));}
    public void setTripleStrike(boolean value) {this.dataManager.set(TRIPLE_STRIKE, Boolean.valueOf(value));}
    public boolean isSimpleSwing() {return this.dataManager.get(SIMPLE_SWING);}
    private boolean isSwingContinue() {return this.dataManager.get(SWING_CONTINUE);}
    private boolean isSwingFinish() {return this.dataManager.get(SWING_FINISH);}
    private boolean isDashBegin() {return this.dataManager.get(DASH);}
    private boolean isDashSwing() {return this.dataManager.get(DASH_SWING);}
    private boolean isDashStomp() {return this.dataManager.get(DASH_STOMP);}
    private boolean isLeapBegin() {return this.dataManager.get(LEAP_BEGIN);}
    private boolean isLeapContinue() {return this.dataManager.get(LEAP_CONTINUE);}
    private boolean isLeapFinish() {return this.dataManager.get(LEAP_FINISH);}
    private boolean isFlameSpit() {return this.dataManager.get(FLAME_SPIT);}
    private boolean isMortarFire() {return this.dataManager.get(MORTAR_FIRE);}
    private boolean isFlameWave() {return this.dataManager.get(FLAME_WAVE);}
    private boolean isBuffSelf() {return this.dataManager.get(BUFF_SELF);}
    private boolean isBlockAction() {return this.dataManager.get(BLOCK_ACTION);}
    private boolean isTripleStrike() {return this.dataManager.get(TRIPLE_STRIKE);}

    public boolean isHadPreviousTarget() {return this.dataManager.get(HAD_PREVIOUS_TARGET);}
    public void setHadPreviousTarget(boolean value) {this.dataManager.set(HAD_PREVIOUS_TARGET, Boolean.valueOf(value));}

    public boolean isSetSpawnLoc() {
        return this.dataManager.get(SET_SPAWN_LOC);
    }
    public void setSetSpawnLoc(boolean value) {
        this.dataManager.set(SET_SPAWN_LOC, Boolean.valueOf(value));
    }
    public void setSpawnLocation(BlockPos pos) {
        this.dataManager.set(SPAWN_LOCATION, pos);
    }

    public BlockPos getSpawnLocation() {
        return this.dataManager.get(SPAWN_LOCATION);
    }
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}

    private int blockCooldown = 80;
    private int shakeTime = 0;

    public EntityVolatileSpirit(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(1.7F, 3.2F);
        this.iAmBossMob = true;
        this.experienceValue = 95;
    }

    public EntityVolatileSpirit(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(1.7F, 3.2F);
        this.iAmBossMob = true;
        this.experienceValue = 95;
    }

    public void onSummonBoss(double x, double y, double z) {
        BlockPos offset = new BlockPos(x, y, z);
        this.setSpawnLocation(offset);
        this.setSetSpawnLoc(true);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Simple_Swing", this.isSimpleSwing());
        nbt.setBoolean("Swing_Continue", this.isSwingContinue());
        nbt.setBoolean("Swing_Finish", this.isSwingFinish());
        nbt.setBoolean("Dash_Begin", this.isDashBegin());
        nbt.setBoolean("Dash_Stomp", this.isDashStomp());
        nbt.setBoolean("Dash_Swing", this.isDashSwing());
        nbt.setBoolean("Leap_Begin", this.isLeapBegin());
        nbt.setBoolean("Leap_Continue", this.isLeapContinue());
        nbt.setBoolean("Leap_Finish", this.isLeapFinish());
        nbt.setBoolean("Flame_Spit", this.isFlameSpit());
        nbt.setBoolean("Mortar_Fire", this.isMortarFire());
        nbt.setBoolean("Flame_Wave", this.isFlameWave());
        nbt.setBoolean("Buff_Self", this.isBuffSelf());
        nbt.setBoolean("Block_Action", this.isBlockAction());
        nbt.setBoolean("Shaking", this.isShaking());
        nbt.setBoolean("Triple_Strike", this.isTripleStrike());
        nbt.setBoolean("Had_Target", this.dataManager.get(HAD_PREVIOUS_TARGET));
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
        nbt.setBoolean("Set_Spawn_Loc", this.dataManager.get(SET_SPAWN_LOC));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setSimpleSwing(nbt.getBoolean("Simple_Swing"));
        this.setSwingContinue(nbt.getBoolean("Swing_Continue"));
        this.setSwingFinish(nbt.getBoolean("Swing_Finish"));
        this.setDashBegin(nbt.getBoolean("Dash_Begin"));
        this.setDashStomp(nbt.getBoolean("Dash_Stomp"));
        this.setDashSwing(nbt.getBoolean("Dash_Swing"));
        this.setLeapBegin(nbt.getBoolean("Leap_Begin"));
        this.setLeapContinue(nbt.getBoolean("Leap_Continue"));
        this.setLeapFinish(nbt.getBoolean("Leap_Finish"));
        this.setFlameSpit(nbt.getBoolean("Flame_Spit"));
        this.setMortarFire(nbt.getBoolean("Mortar_Fire"));
        this.setFlameWave(nbt.getBoolean("Flame_Wave"));
        this.setBuffSelf(nbt.getBoolean("Buff_Self"));
        this.setShaking(nbt.getBoolean("Shaking"));
        this.setBlockAction(nbt.getBoolean("Block_Action"));
        this.setTripleStrike(nbt.getBoolean("Triple_Strike"));
        this.setHadPreviousTarget(nbt.getBoolean("Had_Target"));
        this.dataManager.set(SET_SPAWN_LOC, nbt.getBoolean("Set_Spawn_Loc"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SIMPLE_SWING, Boolean.valueOf(false));
        this.dataManager.register(SWING_FINISH, Boolean.valueOf(false));
        this.dataManager.register(SWING_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(DASH, Boolean.valueOf(false));
        this.dataManager.register(DASH_STOMP, Boolean.valueOf(false));
        this.dataManager.register(DASH_SWING, Boolean.valueOf(false));
        this.dataManager.register(LEAP_BEGIN, Boolean.valueOf(false));
        this.dataManager.register(LEAP_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(LEAP_FINISH, Boolean.valueOf(false));
        this.dataManager.register(FLAME_SPIT, Boolean.valueOf(false));
        this.dataManager.register(MORTAR_FIRE, Boolean.valueOf(false));
        this.dataManager.register(FLAME_WAVE, Boolean.valueOf(false));
        this.dataManager.register(BUFF_SELF, Boolean.valueOf(false));
        this.dataManager.register(BLOCK_ACTION, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        this.dataManager.register(TRIPLE_STRIKE, Boolean.valueOf(false));
        this.dataManager.register(HAD_PREVIOUS_TARGET, Boolean.valueOf(false));
        this.dataManager.register(SET_SPAWN_LOC, Boolean.valueOf(false));
        //
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(26D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.volactile_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.volactile_spirit_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.volactile_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.volactile_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityVolatileSpiritAI<>(this, 1.25, 20, 15, 0.3F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    private boolean isMeleeViable = false;
    private boolean isInspired = false;
    private int inspiredTimer = 400;
    private int inspiredCooldown = 0;

    private boolean fastDestroyBlocks = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.shakeTime--;
        if(!world.isRemote) {
            EntityLivingBase target = this.getAttackTarget();


            if (this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
                if (target != null) {
                    if (target instanceof EntityPlayer) {
                        this.setHadPreviousTarget(true);
                    }
                }

                //Creates a Target tracking to ensure if it can despawn or not
                if (target == null && this.isHadPreviousTarget() && ModConfig.boss_reset_enabled) {
                    int nearbyPlayers = ServerScaleUtil.getPlayersForReset(this, world);
                    if (nearbyPlayers == 0) {
                        if (targetTrackingTimer > 0) {
                            targetTrackingTimer--;
                        }
                        if (targetTrackingTimer < 1) {
                            this.resetBossTask();
                        }
                    }
                }
            }

            //Spawn Telporting Location
            //This is too keep the boss at it's starting location and keep it from getting too far away

            if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
                Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());

                double distSq = this.getDistanceSq(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                double distance = Math.sqrt(distSq);
                //This basically makes it so the mob will be teleported if they are too far away from the Arena
                if(!world.isRemote) {
                    if (distance > 22) {
                        this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                    }
                }
            }

            blockCooldown--;
            inspiredCooldown--;

            //resets the loop once condition is met
            if(this.isInspired) {
                inspiredTimer--;
                if(inspiredTimer < 0) {
                    this.isInspired = false;
                    inspiredTimer = 400;
                }
            }

            if(this.fastDestroyBlocks) {
                AxisAlignedBB box = getEntityBoundingBox().grow(1.25, 0.5, 1.25).offset(0, 0.75, 0);
                ModUtils.destroyBlocksInAABB(box, world, this);
                this.fastDestroyBlocks = false;
            }

            double healthFac = this.getHealth() / this.getMaxHealth();
            if(healthFac <= 0.5) {
                if(world.rand.nextInt(2) == 0) {
                    world.setEntityState(this, ModUtils.PARTICLE_BYTE);
                }
            }

            if(target != null) {
                if(this.getDistance(target) <= 9) {
                    this.isMeleeViable = true;
                } else {
                    this.isMeleeViable = false;
                }

                if(this.isFlameSpit()) {
                    if(this.getDistance(target) <= 5) {
                        double d0 = (this.posX - target.posX) * 0.013;
                        double d1 = (this.posY - target.posY) * 0.005;
                        double d2 = (this.posZ - target.posZ) * 0.013;
                        this.addVelocity(d0, d1, d2);
                    }
                }
            }
        }
    }


    private void resetBossTask() {
        this.setImmovable(true);
        this.setHadPreviousTarget(false);
        BlockPos pos = this.getSpawnLocation();
        EntityFlameOrb orb = new EntityFlameOrb(world);
        orb.setPosition(pos.getX(), pos.getY(), pos.getZ());
        world.spawnEntity(orb);
        this.experienceValue = 0;
        this.setDropItemsWhenDead(false);
        this.setDead();
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double healthFac = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && !this.isBlockAction()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(simple_swing, dash_begin, flame_spit, mortar_fire, flame_wave, leap_attack, self_buff, triple_strike));
            double[] weights = {
                    (distance <= 6 && prevAttacks != simple_swing) ? 1/distance : 0,
                    (distance > 5 && prevAttacks != dash_begin) ? 1/distance : 0,
                    (distance <= 6 && prevAttacks != flame_spit) ? 1/distance : 0,
                    (distance > 8 && prevAttacks != mortar_fire) ? 1/distance : 0,
                    (distance > 4 && prevAttacks != flame_wave) ? 1/distance : 0,
                    (distance > 5 && prevAttacks != leap_attack) ? distance * 0.02 : 0,
                    (prevAttacks != self_buff && inspiredCooldown < 0 && healthFac <= 0.5) ? 1/distance : 0,
                    (healthFac <= 0.5 && distance < 9 && prevAttacks != triple_strike) ? 1/distance : 0
            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return (healthFac <= 0.5) ? 10 : 30;
    }

    private final Consumer<EntityLivingBase> triple_strike = (target) -> {
      this.setTripleStrike(true);
      this.setFightMode(true);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
            this.lockLook = true;
            this.setImmovable(false);
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.24),0F);
            }, 5);
        }, 15);

        addEvent(()-> {
            this.fastDestroyBlocks = true;
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.isInspired ? (float)(this.getAttack() * MobConfig.volactile_inspired_multiplier) : (float)(this.getAttack());
            ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.4f, 4, false);
            this.playSound(SoundsHandler.INCENDIUM_HEAVY_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 25);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 35);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
            this.lockLook = true;
            this.setImmovable(false);
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.28),0F);
            }, 5);
        }, 50);

        addEvent(()-> {
            this.fastDestroyBlocks = true;
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.isInspired ? (float)(this.getAttack() * MobConfig.volactile_inspired_multiplier) : (float)(this.getAttack());
            ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.4f, 4, false);
            this.playSound(SoundsHandler.INCENDIUM_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 60);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 80);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.isInspired ? (float)(this.getAttack() * MobConfig.volactile_inspired_multiplier) : (float)(this.getAttack());
            ModUtils.handleAreaImpact(1.7f, (e) -> damage, this, offset, source, 0.4f, 4, false);
            this.playSound(SoundsHandler.INCENDIUM_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            new ActionFlameSling(flame_sling_projectiles, false).performAction(this, target);
            this.lockLook = true;
        }, 97);

        addEvent(()-> {
            this.lockLook = false;
        }, 125);

      addEvent(()-> {
          this.setImmovable(false);
        this.setTripleStrike(false);
        this.setFightMode(false);
      }, 140);
    };

    private final Consumer<EntityLivingBase> self_buff = (target) -> {
      this.setBuffSelf(true);
      this.setFightMode(true);

      addEvent(()-> this.playSound(SoundsHandler.VOLACTILE_CAST, 1.5f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 10);
      addEvent(()-> {
        //do particles and sounds
          this.isInspired = true;
          this.inspiredCooldown = 500;
          world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
      }, 20);

      addEvent(()-> {
        this.setBuffSelf(false);
        this.setFightMode(false);
      }, 50);
    };
    private final Consumer<EntityLivingBase> leap_attack = (target) -> {
      this.setLeapBegin(true);
      this.setFightMode(true);
      this.setImmovable(true);

      addEvent(()-> {
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(0));
          this.lockLook = true;
          this.setImmovable(false);
          addEvent(()-> {
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.13),0.7F);
          }, 5);
      }, 25);

      addEvent(()-> {
          this.setImmovable(true);
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.1, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.isInspired ? (float)(this.getAttack() * MobConfig.volactile_inspired_multiplier) : (float)(this.getAttack());
          ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.8f, 0, false);
          this.playSound(SoundsHandler.VOLACTILE_SMASH, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
          new ActionTileAOE((int) this.getDistance(target) + 4).performAction(this, target);
          this.setShaking(true);
          this.shakeTime = 30;
      }, 55);

        addEvent(()-> {
            this.setShaking(false);
        }, 75);

      addEvent(()-> {
        this.setLeapBegin(false);
        int randI = ModRand.range(1, 11);
        if(!this.isMeleeViable && randI > 5 || randI > 8) {
            this.setLeapTooContinue(target);
        } else {
            this.lockLook= false;
            this.setLeapFinish(true);
            addEvent(()-> {
            this.setImmovable(false);
            this.setLeapFinish(false);
            this.setFightMode(false);
            }, 25);
        }
      }, 60);
    };

    private void setLeapTooContinue(EntityLivingBase target) {
        leap_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> leap_continue = (target) -> {
      this.setLeapContinue(true);
      this.lockLook = false;

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(0));
            this.lockLook = true;
            this.setImmovable(false);
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.13),0.7F);
            }, 5);
        }, 13);

        addEvent(()-> {
            this.setImmovable(true);
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.isInspired ? (float)(this.getAttack() * MobConfig.volactile_inspired_multiplier) : (float)(this.getAttack());
            ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.8f, 0, false);
            this.playSound(SoundsHandler.VOLACTILE_SMASH, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
            new ActionTileAOE((int) this.getDistance(target) + 4).performAction(this, target);
            this.setShaking(true);
            this.shakeTime = 30;
        }, 40);

        addEvent(()-> {
            this.setShaking(false);
        }, 60);

        addEvent(()-> {
            this.lockLook = false;
        }, 60);

      addEvent(() -> {
        this.setLeapContinue(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 70);
    };

    private final Consumer<EntityLivingBase> flame_wave = (target) -> {
      this.setFlameWave(true);
      this.setFightMode(true);

      addEvent(()-> {
          this.setImmovable(true);
      }, 20);

      addEvent(()-> {
        this.lockLook = true;
      }, 25);

      addEvent(()-> {
          //first Flame wave
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.1, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.isInspired ? (float)(this.getAttack() * MobConfig.volactile_inspired_multiplier) : (float)(this.getAttack());
          ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.8f, 0, false);
          new ActionFlameWave(flame_sling_projectiles).performAction(this, target);
      }, 30);

      addEvent(()-> {
          //second Flame Wave
          new ActionSecondFlameWave(flame_sling_projectiles).performAction(this, target);
      }, 46);

      addEvent(()-> {
            this.lockLook= false;
            this.setImmovable(false);
      }, 60);

      addEvent(() -> {
        this.setFlameWave(false);
        this.setFightMode(false);
      }, 85);
    };

    private final Consumer<EntityLivingBase> mortar_fire = (target) -> {
        this.setMortarFire(true);
        this.setFightMode(true);

        addEvent(()-> {
            ProjectileTrackingFlame newFlame = new ProjectileTrackingFlame(world, this, 18, target, false);
            this.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 1.0f, 0.6f / (world.rand.nextFloat() * 0.4f + 0.2f));
            newFlame.setPosition(this.posX, this.posY + 3.8, this.posZ);

            world.spawnEntity(newFlame);
            newFlame.setTravelRange(40);
        }, 27);

        addEvent(()-> {
            ProjectileTrackingFlame newFlame = new ProjectileTrackingFlame(world, this, 18, target, false);
            this.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 1.0f, 0.6f / (world.rand.nextFloat() * 0.4f + 0.2f));
            newFlame.setPosition(this.posX, this.posY + 3.8, this.posZ);
            world.spawnEntity(newFlame);
            newFlame.setTravelRange(40);
        }, 43);

        addEvent(()-> {
            ProjectileTrackingFlame newFlame = new ProjectileTrackingFlame(world, this, 18, target, false);
            this.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 1.0f, 0.6f / (world.rand.nextFloat() * 0.4f + 0.2f));
            newFlame.setPosition(this.posX, this.posY + 3.8, this.posZ);
            world.spawnEntity(newFlame);
            newFlame.setTravelRange(40);
        }, 57);

        addEvent(()-> {
            this.setMortarFire(false);
            this.setFightMode(false);
        }, 85);
    };
    private final Consumer<EntityLivingBase> flame_spit = (target) -> {
       this.setFlameSpit(true);
       this.setFightMode(true);

        addEvent(()-> this.playSound(SoundsHandler.VOLACTILE_SHIELD_USE, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 13);

       addEvent(()-> this.isShieldActive = true, 15);

       addEvent(()-> {
           //flame Spit
           for(int i = 0; i <= 65; i += 5) {
               addEvent(()-> {
                   this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.4f, 0.4f / (rand.nextFloat() * 0.4F + 0.4f));
                   ProjectileFlameSpit spit = new ProjectileFlameSpit(world, this, (float) (this.getAttack() * 0.6));
                   Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2,1.5,0)));
                   spit.setPosition(relPos.x, relPos.y, relPos.z);
                   spit.shoot(this, 0, this.rotationYaw, 0.0F, 0.7F, 0F);
                   spit.setTravelRange(9F);
                   world.spawnEntity(spit);
               }, i);
           }
       }, 25);

       addEvent(()-> this.isShieldActive = false, 100);

       addEvent(()-> {
        this.setFlameSpit(false);
        this.setFightMode(false);
       }, 115);
    };


    private final Consumer<EntityLivingBase> dash_begin = (target) -> {
      this.setDashBegin(true);
      this.setFightMode(true);
      this.setImmovable(true);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-5));
            addEvent(()-> {
                this.setImmovable(false);
                ModUtils.attemptTeleport(targetedPos, this);
                this.fastDestroyBlocks = true;
                this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.75f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 3);
        }, 17);

      addEvent(()-> {
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
          this.lockLook = true;
          addEvent(()-> {
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.35),0F);
          }, 5);
      }, 23);

      addEvent(()-> {
          for(int i = 0; i <= 10; i += 2) {
              addEvent(()-> {
                  this.fastDestroyBlocks = true;
                  Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.1, 0)));
                  DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                  float damage = this.isInspired ? (float)(this.getAttack() * MobConfig.volactile_inspired_multiplier) : (float)(this.getAttack());
                  ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.8f, 4, false);
              }, i);
          }
          this.playSound(SoundsHandler.INCENDIUM_HEAVY_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 33);

      addEvent(()-> {
          this.setImmovable(true);
          this.setDashBegin(false);

          if(this.isMeleeViable) {
              this.setDashTooContinue(target);
          } else {
              this.setDashStomp(true);

              addEvent(()-> {
                  this.playSound(SoundsHandler.VOLACTILE_SMASH, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
                new ActionTileAOE((int) this.getDistance(target) + 4).performAction(this, target);
                this.setShaking(true);
                this.shakeTime = 30;
              }, 38);

              addEvent(()-> {
                  this.setShaking(false);
              }, 58);

              addEvent(()-> {
                  this.lockLook = false;
              }, 50);

              addEvent(()-> {
                this.setImmovable(false);
                this.setDashStomp(false);
                this.setFightMode(false);
              }, 60);
          }
      }, 45);
    };

    private void setDashTooContinue(EntityLivingBase target) {
        dash_swing_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> dash_swing_continue = (target) -> {
      this.setDashSwing(true);
      this.lockLook = false;

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
            this.lockLook = true;
            this.setImmovable(false);
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.3),0F);
            }, 5);
        }, 20);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.isInspired ? (float)(this.getAttack() * MobConfig.volactile_inspired_multiplier) : (float)(this.getAttack());
            ModUtils.handleAreaImpact(1.7f, (e) -> damage, this, offset, source, 0.4f, 4, false);
            this.playSound(SoundsHandler.INCENDIUM_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            this.fastDestroyBlocks = true;
        }, 30);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 55);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            this.lockLook = true;
            this.setImmovable(false);
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.35),0F);
            }, 5);
        }, 65);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.7, 1.1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.isInspired ? (float)(this.getAttack() * MobConfig.volactile_inspired_multiplier) : (float)(this.getAttack());
            ModUtils.handleAreaImpact(1.7f, (e) -> damage, this, offset, source, 0.6f, 4, false);
            this.playSound(SoundsHandler.INCENDIUM_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            double healthFac = this.getHealth() / this.getMaxHealth();
            if(healthFac <= 0.5) {
                new ActionFlameSling(flame_sling_projectiles, false).performAction(this, target);
            }
        }, 75);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(true);
        }, 83);

        addEvent(()-> {
            this.setImmovable(false);
            this.setFightMode(false);
            this.setDashSwing(false);
        }, 95);
    };

    private final Consumer<EntityLivingBase> simple_swing = (target) -> {
      this.setSimpleSwing(true);
      this.setFightMode(true);

      addEvent(()-> {
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
          this.lockLook = true;
          addEvent(()-> {
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.2),0F);
          }, 5);
      }, 13);
      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.1, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.isInspired ? (float)(this.getAttack() * MobConfig.volactile_inspired_multiplier) : (float)(this.getAttack());
          ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.2f, 4, false);
          this.playSound(SoundsHandler.INCENDIUM_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          this.setImmovable(true);
      }, 23);

      addEvent(()-> {
          this.setImmovable(false);
        this.setSimpleSwing(false);
        int randI = ModRand.range(1, 11);

        if(isMeleeViable && randI > 5 || randI > 8) {
            this.setSwingTooContinue(target);
        } else {
            this.setSwingFinish(true);
            addEvent(()-> {
                this.lockLook = false;
            }, 10);
            addEvent(()-> {
                this.setSwingFinish(false);
                this.setFightMode(false);
            }, 20);
        }
      }, 30);
    };

    private void setSwingTooContinue(EntityLivingBase target) {
        swing_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> swing_continue = (target) -> {
      this.setSwingContinue(true);
      addEvent(()-> {
          this.lockLook = false;
          this.setImmovable(true);
      }, 10);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            this.lockLook = true;
            this.setImmovable(false);
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.35),0F);
            }, 5);
        }, 29);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.6, 1.1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.isInspired ? (float)(this.getAttack() * MobConfig.volactile_inspired_multiplier) : (float)(this.getAttack());
            ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.2f, 4, false);
            this.playSound(SoundsHandler.INCENDIUM_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            this.setImmovable(true);
        }, 39);

        addEvent(()-> {
            this.lockLook = false;
        }, 50);

      addEvent(()-> {
        this.setSwingContinue(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 60);
    };

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == ModUtils.PARTICLE_BYTE) {
            Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(ModRand.range(-2, 2), ModRand.range(1, 2), ModRand.range(-2, 2))));
            Vec3d vel = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) / 3, 0.1, (world.rand.nextFloat() - world.rand.nextFloat()) / 3);
            world.spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, ModRand.range(40, 50));
        }
        if (id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 25, (pos) -> {
                pos = new Vec3d(pos.x, 0, pos.y);
                world.spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y + 4, pos.z, 0, 0.08, 0, ModRand.range(40, 50));
            });
        }
        super.handleStatusUpdate(id);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "orb_idle_controller", 0, this::predicateOrbIdle));
        data.addAnimationController(new AnimationController(this, "shield_idle_controller", 0, this::predicateShieldIdle));
        data.addAnimationController(new AnimationController(this, "body_idle_controller", 0, this::predicateBodyIdle));
        data.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "block_controller", 0, this::predicateBlock));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateOrbIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ORB_IDLE, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateShieldIdle(AnimationEvent<E> event) {
        //add paramaters
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHIELD_IDLE, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateBodyIdle(AnimationEvent<E> event) {
        if(!this.isFightMode() && !this.isBlockAction()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BODY_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateBlock(AnimationEvent<E> event) {
        if(this.isBlockAction()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLOCK, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isBlockAction()) {
            if(this.isSimpleSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_BEGIN, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingContinue()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_CONTINUE, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingFinish()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_FINISH, false));
                return PlayState.CONTINUE;
            }
            if(this.isDashBegin()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DASH, false));
                return PlayState.CONTINUE;
            }
            if(this.isDashStomp()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DASH_STOMP, false));
                return PlayState.CONTINUE;
            }
            if(this.isDashSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DASH_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isLeapBegin()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LEAP, false));
                return PlayState.CONTINUE;
            }
            if(this.isLeapContinue()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LEAP_CONTINUE, false));
                return PlayState.CONTINUE;
            }
            if(this.isLeapFinish()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LEAP_FINISH, false));
                return PlayState.CONTINUE;
            }
            if(this.isFlameSpit()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLAME_SPIT, false));
                return PlayState.CONTINUE;
            }
            if(this.isMortarFire()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MORTAR_FIRE, false));
                return PlayState.CONTINUE;
            }
            if(this.isFlameWave()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLAME_WAVE, false));
                return PlayState.CONTINUE;
            }
            if(this.isBuffSelf()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BUFF_SELF, false));
                return PlayState.CONTINUE;
            }
            if(this.isTripleStrike()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_TRIPLE_STRIKE, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
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
    public boolean canBePushed() {
        return false;
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (amount > 0.0F && this.canBlockDamageSource(source)) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }
            //REPLACE SOUND
            this.playSound(SoundsHandler.VOLACTILE_SHIELD_BLOCK, 0.6f, 0.8f + ModRand.getFloat(0.2f));

            return false;
        } else if(source.isProjectile()) {
            return super.attackEntityFrom(source, (float) (amount * MobConfig.volactile_projectile_resistance));
        }
        return super.attackEntityFrom(source, amount);
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if(this.isShieldActive) {
            if (!damageSourceIn.isUnblockable()) {
                Vec3d vec3d = damageSourceIn.getDamageLocation();

                if (vec3d != null) {
                    Vec3d vec3d1 = this.getLook(1.0F);
                    Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                    vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

                    return vec3d2.dotProduct(vec3d1) < 0.0D;
                }
            }
        } else if (!this.isFightMode() && this.blockCooldown < 0) {
            if (!damageSourceIn.isUnblockable()) {
                this.doBlockAction();
                Vec3d vec3d = damageSourceIn.getDamageLocation();

                if (vec3d != null) {
                    Vec3d vec3d1 = this.getLook(1.0F);
                    Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                    vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

                    return vec3d2.dotProduct(vec3d1) < 0.0D;
                }
            }
        }

        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
    }

    @Override
    protected boolean canDespawn() {

        // Edit this to restricting them not despawning in Dungeons
        return false;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.VOLACTILE_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.VOLACTILE_HURT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.VOLACTILE_HURT;
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "volactile_spirit");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    public void doBlockAction() {
        this.setBlockAction(true);
        addEvent(()-> {
            this.setBlockAction(false);
            this.blockCooldown = 80;
        }, 40);
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 16.0F);
            if (dist >= 16.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.5F * screamMult);
        }
        return 0;
    }
}
