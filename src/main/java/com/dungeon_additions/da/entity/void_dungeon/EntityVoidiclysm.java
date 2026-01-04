package com.dungeon_additions.da.entity.void_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.blocks.boss.BlockEnumBossSummonState;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.ai.flying.FlyingMoveHelper;
import com.dungeon_additions.da.entity.ai.flying.TimedAttackInitiator;
import com.dungeon_additions.da.entity.ai.void_dungeon.EntityVoidclysmAttackAI;
import com.dungeon_additions.da.entity.frost_dungeon.IDirectionalRender;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.IMultiAction;
import com.dungeon_additions.da.entity.tileEntity.TileEntityBossReSummon;
import com.dungeon_additions.da.entity.util.IEntitySound;
import com.dungeon_additions.da.entity.void_dungeon.voidclysm_action.ActionBombBarrage;
import com.dungeon_additions.da.entity.void_dungeon.voidclysm_action.ActionCastVoidBolts;
import com.dungeon_additions.da.entity.void_dungeon.voidclysm_action.ActionClapAttack;
import com.dungeon_additions.da.entity.void_dungeon.voidclysm_action.ActionLazerTeleport;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.*;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityVoidiclysm extends EntityEndBase implements IAnimatable, IAnimationTickable, IPitch, IScreenShake, IAttack, IDirectionalRender, IEntitySound {
    private AnimationFactory factory = new AnimationFactory(this);
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.NOTCHED_10));
    private Consumer<EntityLivingBase> prevAttack;
    private int shakeTime = 0;
    public boolean standbyOnVel = false;
    public boolean clearCurrentVelocity = false;

    protected static final byte stopLazerByte = 70;
    public IMultiAction lazerAttack =  new ActionLazerTeleport(this, stopLazerByte, (vec3d) -> {});
    private final String ANIM_MODEL_IDLE = "idle";
    private final String ANIM_FLAPS_IDLE = "idle_flaps";

    private final String ANIM_CAST_SPELL = "cast_spell";
    private final String ANIM_SCREAM = "scream";
    private final String ANIM_LAZER_TELEPORT = "lazer_teleport";
    private final String ANIM_STRIKE_ONE = "strike_1";
    private final String ANIM_STRIKE_TWO = "strike_2";
    private final String ANIM_CLAP = "clap";
    private final String ANIM_RAISE_SPIKES = "raise_spikes";

    //Not Done Animations
    private final String ANIM_GRAB_ATTACK = "grab";
    private final String ANIM_GRAB_CONTINUE = "grab_continue";
    private final String ANIM_GRAB_END = "grab_finish";
    private final String ANIM_MELEE_SLAM = "melee_slam";
    private final String ANIM_ATOMIC_ATTACK = "atomic_attack";
    private final String ANIM_SUMMON_TRACK_PROJECTILES = "summon_track";
    private final String ANIM_DEFLECT = "deflect";
    private final String ANIM_PARRY = "parry";
    private final String ANIM_SUMMON = "summon";
    private final String ANIM_DEATH = "death";

    private static final DataParameter<Boolean> CAST_SPELL = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SCREAM_ATTACK = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LAZER_TELEPORT = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STRIKE_ONE = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STRIKE_TWO = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CLAP = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RAISE_SPIKES = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEE_MODE = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GRAB_ATTACK = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GRAB_CONTINUE = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GRAB_FINISH = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ATOMIC_ATTACK = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_TRACK = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEE_SLAM = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PARRY = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEFLECT = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> HAD_PREVIOUS_TARGET = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);

    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BLOCK_POS);
    public static DataParameter<Boolean> SET_SPAWN_LOC = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityVoidiclysm.class, DataSerializers.FLOAT);

    public void setCastSpell(boolean value) {this.dataManager.set(CAST_SPELL, Boolean.valueOf(value));}
    public boolean isCastSpell() {return this.dataManager.get(CAST_SPELL);}
    public void setScreamAttack(boolean value) {this.dataManager.set(SCREAM_ATTACK, Boolean.valueOf(value));}
    public boolean isScreamAttack() {return this.dataManager.get(SCREAM_ATTACK);}
    public void setLazerTeleport(boolean value) {this.dataManager.set(LAZER_TELEPORT, Boolean.valueOf(value));}
    public boolean isLazerTeleport() {return this.dataManager.get(LAZER_TELEPORT);}
    public void setStrikeOne(boolean value) {this.dataManager.set(STRIKE_ONE, Boolean.valueOf(value));}
    public boolean isStrikeOne() {return this.dataManager.get(STRIKE_ONE);}
    public void setStrikeTwo(boolean value) {this.dataManager.set(STRIKE_TWO, Boolean.valueOf(value));}
    public boolean isStrikeTwo() {return this.dataManager.get(STRIKE_TWO);}
    public void setClap(boolean value) {this.dataManager.set(CLAP, Boolean.valueOf(value));}
    public boolean isClap() {return this.dataManager.get(CLAP);}
    public void setRaiseSpikes(boolean value) {this.dataManager.set(RAISE_SPIKES, Boolean.valueOf(value));}
    public boolean isRaiseSpikes() {return this.dataManager.get(RAISE_SPIKES);}
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    public boolean isMeleeMode() {return this.dataManager.get(MELEE_MODE);}
    public void setMeleeMode(boolean value) {this.dataManager.set(MELEE_MODE, Boolean.valueOf(value));}
    public boolean isGrabAttack() {return this.dataManager.get(GRAB_ATTACK);}
    public void setGrabAttack(boolean value) {this.dataManager.set(GRAB_ATTACK, Boolean.valueOf(value));}
    public boolean isGrabContinue() {return this.dataManager.get(GRAB_CONTINUE);}
    public void setGrabContinue(boolean value) {this.dataManager.set(GRAB_CONTINUE, Boolean.valueOf(value));}
    protected boolean isSummon() {return this.dataManager.get(SUMMON);}
    protected void setSummon(boolean value) {this.dataManager.set(SUMMON, Boolean.valueOf(value));}
    public boolean isGrabFinish() {return this.dataManager.get(GRAB_FINISH);}
    public void setGrabFinish(boolean value) {this.dataManager.set(GRAB_FINISH, Boolean.valueOf(value));}
    public boolean isAtomicAttack() {return this.dataManager.get(ATOMIC_ATTACK);}
    public void setAtomicAttack(boolean value) {this.dataManager.set(ATOMIC_ATTACK, Boolean.valueOf(value));}
    public boolean isSummonTrack() {return this.dataManager.get(SUMMON_TRACK);}
    public void setSummonTrack(boolean value) {this.dataManager.set(SUMMON_TRACK, Boolean.valueOf(value));}
    public boolean isMeleeSlam() {return this.dataManager.get(MELEE_SLAM);}
    public void setMeleeSlam(boolean value) {this.dataManager.set(MELEE_SLAM, Boolean.valueOf(value));}
    public boolean isParry() {return this.dataManager.get(PARRY);}
    public void setParry(boolean value) {this.dataManager.set(PARRY, Boolean.valueOf(value));}
    public boolean isDeflect() {return this.dataManager.get(DEFLECT);}
    public void setDeflect(boolean value) {this.dataManager.set(DEFLECT, Boolean.valueOf(value));}

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
    private int attack_differential = 0;
    public boolean doesBossSlowDown = false;
    protected boolean performLazerAttack = false;

    public Vec3d renderLazerPos;
    public Vec3d prevRenderLazerPos;
    private EntityLivingBase grabbedEntity;
    private boolean grabDetection = false;
    public int wantedDistance = 20;

    private boolean hasDoneAtomic = false;
    private boolean setBossToFlyHigh = false;

    private int standByCooldown = 0;

    public EntityVoidiclysm(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.iAmBossMob = true;
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.experienceValue = MobConfig.voidclysm_experience_value;
        this.setSize(1.6F, 3.7F);
        this.isImmuneToExplosions();
        if(!world.isRemote) {
            initVoidclysmAI();
        }
        BlockPos offset = new BlockPos(x, y, z);
        this.setSpawnLocation(offset);
        this.setSetSpawnLoc(true);
        this.onSummonBoss();
    }

    public EntityVoidiclysm(World world, int timesUsed, BlockPos pos) {
        super(world, timesUsed, pos);
        this.timesUsed = timesUsed;
        this.experienceValue = MobConfig.voidclysm_experience_value;
        if(!MobConfig.obsidilith_two_part_boss) {
            this.timesUsed++;
        }
        this.doBossReSummonScaling();
        this.iAmBossMob = true;
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, world);
        this.setSize(1.6F, 3.7F);
        this.isImmuneToExplosions();
        if(!world.isRemote) {
            initVoidclysmAI();
        }
        this.setSpawnLocation(pos);
        this.setSetSpawnLoc(true);
        this.onSummonBoss();
    }

    public EntityVoidiclysm(World worldIn) {
        super(worldIn);
        this.iAmBossMob = true;
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.experienceValue = MobConfig.voidclysm_experience_value;
        this.setSize(1.6F, 3.7F);
        this.isImmuneToExplosions();
        if(!world.isRemote) {
            initVoidclysmAI();
        }
        this.onSummonBoss();
    }

    private void onSummonBoss() {
        this.setSummon(true);
        this.setFullBodyUsage(true);
        this.clearCurrentVelocity = true;
        this.setImmovable(true);
        world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);

        addEvent(()-> {
            this.playSound(SoundsHandler.VOIDCLYSM_SCREAM, 1.75f, 0.7F);
            this.setShaking(true);
            this.shakeTime = 65;
        }, 30);

        addEvent(()-> {
                this.setShaking(false);
        }, 95);

        addEvent(()-> {
            this.standbyOnVel = false;
            this.setImmovable(false);
            this.standByCooldown = base_cooldown * 2;
            this.setSummon(false);
            this.setFullBodyUsage(false);
        }, 60);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Cast_Spell", this.isCastSpell());
        nbt.setBoolean("Scream_Attack", this.isScreamAttack());
        nbt.setBoolean("Lazer_Attack", this.isLazerTeleport());
        nbt.setBoolean("Strike_One", this.isStrikeOne());
        nbt.setBoolean("Strike_Two", this.isStrikeTwo());
        nbt.setBoolean("Clap", this.isClap());
        nbt.setBoolean("Raise_Spikes", this.isRaiseSpikes());
        nbt.setBoolean("Shaking", this.isShaking());
        nbt.setBoolean("Melee_Mode", this.isMeleeMode());
        nbt.setBoolean("Grab_Attack", this.isGrabAttack());
        nbt.setBoolean("Grab_Continue", this.isGrabContinue());
        nbt.setBoolean("Summon", this.isSummon());
        nbt.setBoolean("Grab_Finish", this.isGrabFinish());
        nbt.setBoolean("Atomic_Attack", this.isAtomicAttack());
        nbt.setBoolean("Summon_Track", this.isSummonTrack());
        nbt.setBoolean("Melee_Slam", this.isMeleeSlam());
        nbt.setBoolean("Deflect", this.isDeflect());
        nbt.setBoolean("Parry", this.isParry());
        nbt.setBoolean("Had_Target", this.dataManager.get(HAD_PREVIOUS_TARGET));
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
        nbt.setBoolean("Set_Spawn_Loc", this.dataManager.get(SET_SPAWN_LOC));
        nbt.setFloat("Look", this.getPitch());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setCastSpell(nbt.getBoolean("Cast_Spell"));
        this.setScreamAttack(nbt.getBoolean("Scream_Attack"));
        this.setLazerTeleport(nbt.getBoolean("Lazer_Attack"));
        this.setStrikeOne(nbt.getBoolean("Strike_One"));
        this.setStrikeTwo(nbt.getBoolean("Strike_Two"));
        this.setClap(nbt.getBoolean("Clap"));
        this.setRaiseSpikes(nbt.getBoolean("Raise_Spikes"));
        this.setShaking(nbt.getBoolean("Shaking"));
        this.setMeleeMode(nbt.getBoolean("Melee_Mode"));
        this.setGrabAttack(nbt.getBoolean("Grab_Attack"));
        this.setGrabContinue(nbt.getBoolean("Grab_Continue"));
        this.setSummon(nbt.getBoolean("Summon"));
        this.setGrabFinish(nbt.getBoolean("Grab_Finish"));
        this.setAtomicAttack(nbt.getBoolean("Atomic_Attack"));
        this.setSummonTrack(nbt.getBoolean("Summon_Track"));
        this.setMeleeSlam(nbt.getBoolean("Melee_Slam"));
        this.setDeflect(nbt.getBoolean("Deflect"));
        this.setParry(nbt.getBoolean("Parry"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.setHadPreviousTarget(nbt.getBoolean("Had_Target"));
        this.dataManager.set(SET_SPAWN_LOC, nbt.getBoolean("Set_Spawn_Loc"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(38D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.20590D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.voidclysm_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.voidclysm_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.voidclysm_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.voidclysm_attack_damage);
    }

    private void initVoidclysmAI() {
        float attackDistance = 20;
        float attackDistanceFar = (float) (this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()) - 10;
        this.tasks.addTask(4, new EntityVoidclysmAttackAI(this, attackDistanceFar, attackDistance, 35, new TimedAttackInitiator<>(this, 60)));
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(CAST_SPELL, Boolean.valueOf(false));
        this.dataManager.register(SCREAM_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(LAZER_TELEPORT, Boolean.valueOf(false));
        this.dataManager.register(STRIKE_TWO, Boolean.valueOf(false));
        this.dataManager.register(STRIKE_ONE, Boolean.valueOf(false));
        this.dataManager.register(CLAP, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        this.dataManager.register(RAISE_SPIKES, Boolean.valueOf(false));
        this.dataManager.register(MELEE_MODE, Boolean.valueOf(false));
        this.dataManager.register(GRAB_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(GRAB_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(SUMMON, Boolean.valueOf(false));
        this.dataManager.register(GRAB_FINISH, Boolean.valueOf(false));
        this.dataManager.register(ATOMIC_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_TRACK, Boolean.valueOf(false));
        this.dataManager.register(MELEE_SLAM, Boolean.valueOf(false));
        this.dataManager.register(PARRY, Boolean.valueOf(false));
        this.dataManager.register(DEFLECT, Boolean.valueOf(false));
        this.dataManager.register(HAD_PREVIOUS_TARGET, Boolean.valueOf(false));
        this.dataManager.register(SET_SPAWN_LOC, Boolean.valueOf(false));
        //
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
    }

    private int atomicCooldown = 30 * 20;
    @Override
    public void onUpdate() {
        super.onUpdate();
        if(world.isRemote && ticksExisted == 1 && ModConfig.experimental_features) {
            this.playMusic(this);
        }
        this.shakeTime--;
        this.bossInfo.setPercent(getHealth() / getMaxHealth());
        this.atomicCooldown--;

        if(ticksExisted == 5 && !world.isRemote) {
            if(this.getSpawnLocation() != null) {
                List<EntityEnderman> nearbyMonsters = this.world.getEntitiesWithinAABB(EntityEnderman.class, this.getEntityBoundingBox().grow(20D), e -> !e.getIsInvulnerable());
                for(EntityEnderman monster : nearbyMonsters) {
                    monster.setDead();
                }
            }
        }


        if(!world.isRemote) {
            EntityLivingBase target = this.getAttackTarget();
            this.blockCooldown--;
            //switches distance after doing each attack
            if(target != null) {
                standByCooldown--;
                //need to incorporate taking too much damage in melee
                if(!this.isFightMode()) {
                    if (this.isMeleeMode()) {
                        if (attack_differential > 10) {
                            this.setMeleeMode(false);
                            this.wantedDistance = 20;
                            this.attack_differential = 0;
                        }
                    } else if (attack_differential > 15) {
                        this.setMeleeMode(true);
                        this.wantedDistance = 7;
                        this.attack_differential = 0;
                    }
                }
            }

            //this clears current motion and enables a boolean to keep it still for the time being
            // typically only used in red attack
            if(clearCurrentVelocity) {
                this.motionX = 0;
                this.motionY = 0;
                this.motionZ = 0;
                standbyOnVel = true;
                clearCurrentVelocity = false;
            }

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
                            if(this.timesUsed != 0) {
                                this.timesUsed--;
                                turnBossIntoSummonSpawner(this.getSpawnLocation());
                                this.setDead();
                            } else {
                                this.resetBossTask();
                            }
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
                //This basically makes it so the Wyrk will be teleported if they are too far away from the Arena
                if(!world.isRemote) {
                    if (distance > 40) {
                        this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                    }
                }
            }

            if(grabDetection && grabbedEntity == null) {
                List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                        this.getEntityBoundingBox().offset(ModUtils.getRelativeOffset(this, new Vec3d(1.5, -0.5, 0))).grow(1.5D, 3.5D, 1.5D),
                        e -> !e.getIsInvulnerable());

                if(!nearbyEntities.isEmpty()) {
                    for(EntityLivingBase base : nearbyEntities) {
                        if(!(base instanceof EntityEndBase)) {
                            grabbedEntity = base;
                            this.playSound(SoundsHandler.KING_GRAB_SUCCESS, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                        }
                    }
                }
            } else if (grabbedEntity != null) {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 0.4, 0)));
                grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);

                if(setBossToFlyHigh) {
                    this.motionY = 0.36;
                    this.motionX = 0;
                    this.motionZ = 0;
                }
            }
        }
    }

    protected void turnBossIntoSummonSpawner(BlockPos pos) {
        if(ModConfig.boss_resummon_enabled) {
            if (this.timesUsed <= ModConfig.boss_resummon_max_uses && !world.isRemote) {
                world.setBlockState(pos, ModBlocks.BOSS_RESUMMON_BLOCK.getDefaultState());
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileEntityBossReSummon) {
                    TileEntityBossReSummon boss_spawner = ((TileEntityBossReSummon) te);
                    boss_spawner.setState(BlockEnumBossSummonState.INACTIVE, this.timesUsed, "obsidilith");
                }
            }
        }
    }

    private static final ResourceLocation LOO_RESET = new ResourceLocation(ModReference.MOD_ID, "obsidian_arena_reset");

    private void resetBossTask() {
        this.setImmovable(true);
        this.setHadPreviousTarget(false);
        BlockPos pos = this.getSpawnLocation();
        world.setBlockState(pos, ModBlocks.OBSIDIAN_KEY_BLOCK.getDefaultState());
        world.setBlockState(pos.add(0, 1, 0), Blocks.CHEST.getDefaultState());
        TileEntity te = world.getTileEntity(pos.add(0, 1, 0));
        if(te instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) te;
            chest.setLootTable(LOO_RESET, rand.nextLong());
        }
        this.experienceValue = 0;
        this.setDropItemsWhenDead(false);
        this.setDead();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!world.isRemote && performLazerAttack) {
            lazerAttack.update();
        }
    }

    private final int base_cooldown = MobConfig.voidclysm_attack_cooldown;

    //We want the boss to be fast while having cooldowns inbetween attacks

    //TODO continue with Phase two attacks
    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double HealthChange = this.getHealth() / this.getMaxHealth();
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode() && standByCooldown < 0 && !this.isParry() && !this.isDeflect() && !this.isSummon()) {
            List<Consumer<EntityLivingBase>> close_attacks = new ArrayList<>(Arrays.asList(strike_attack, clap_attack, summon_spike_magic, grab_attack, cast_projectiles, scream_attack, teleport_lazer, summon_tracking_projectiles_attack, atomic_attack));
            double[] weights = {
                    (prevAttack != strike_attack && this.isMeleeMode() && distance < 8) ? 1/distance : 0, //strike attack
                    (prevAttack != clap_attack && this.isMeleeMode() && distance < 12 && HealthChange < 0.75) ? 1/distance : 0, //clap attack
                    (prevAttack != summon_spike_magic && this.isMeleeMode()) ? 1/distance : 0, //summon spikes
                    (prevAttack != grab_attack && this.isMeleeMode()) ? 1/distance : 0, //grab attack
                    (!this.isMeleeMode() && prevAttack != cast_projectiles) ? distance * 0.03 : (this.isMeleeMode() && prevAttack != cast_projectiles && distance > 11) ? distance * 0.02 : 0, //Cast Projectile LIne Attack
                    (!this.isMeleeMode() && prevAttack != scream_attack) ? distance * 0.02 : 0, //Scream Attack
                    (!this.isMeleeMode() && prevAttack != teleport_lazer && HealthChange < 0.85) ? distance * 0.02 : 0, // Teleport Lazer Attack
                    (!this.isMeleeMode() && prevAttack != summon_tracking_projectiles_attack && HealthChange < 0.5) ? distance * 0.02 : 0, // Cast Tracking Projectiles attack
                    (prevAttack != atomic_attack && this.isMeleeMode() && distance < 14 && HealthChange < 0.5) && !this.hasDoneAtomic ? 5/distance : (prevAttack != atomic_attack && this.isMeleeMode() && distance < 14 && atomicCooldown < 0 && HealthChange < 0.5) ? 1/distance : 0// Atomic Blackhole Attack
            };
            prevAttack = ModRand.choice(close_attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        return HealthChange < 0.5 ? (int) (base_cooldown * 0.5) : base_cooldown ;
    }

    //50% Health changes

    private final Consumer<EntityLivingBase> summon_tracking_projectiles_attack = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setSummonTrack(true);


        addEvent(()-> {
            this.playSound(SoundsHandler.VOIDCLYSM_CAST_TRACK, 2.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        }, 5);

     addEvent(()-> {
         for(int i = 0; i < 30; i += 10) {
             addEvent(()-> {
                 if(target != null) {
                     ProjectileTrackingVoid void_projectile = new ProjectileTrackingVoid(world, this, this.getAttack(), target);
                     Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.7,2.0,0)));
                     void_projectile.setPosition(offset.x, offset.y, offset.z);
                     void_projectile.setTravelRange(80F);
                     world.spawnEntity(void_projectile);
                 }
             }, i);
         }
     }, 35);

      addEvent(()-> {
          this.attack_differential += 2;
          this.standByCooldown = base_cooldown;
          this.setFullBodyUsage(false);
          this.setFightMode(false);
          this.setSummonTrack(false);
      }, 60);
    };

    private final Consumer<EntityLivingBase> atomic_attack = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setAtomicAttack(true);
      this.hasDoneAtomic = true;

      addEvent(()-> {
          this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 2.0F, 1.0F);
        if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
            this.setPosition(this.getSpawnLocation().getX(), this.getSpawnLocation().getY() + 0.5, this.getSpawnLocation().getZ());
        } else {
            if(target != null) {
                //just teleport it close to the player
                Vec3d targetRandomPos = target.getPositionVector().add(ModRand.range(-2, 2) + 2, 1, ModRand.range(-2, 2) + 2);
                this.setPosition(targetRandomPos.x, targetRandomPos.y, targetRandomPos.z);

            }
        }
          this.clearCurrentVelocity = true;
          this.setImmovable(true);
          this.lockLook = true;
      }, 10);

      addEvent(()-> {
          //summon blackhole entity
          EntityVoidBlackHole blackHole = new EntityVoidBlackHole(world);
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5,4.5,0)));
          blackHole.setPosition(relPos.x, relPos.y, relPos.z);
          world.spawnEntity(blackHole);
      }, 30);

      addEvent(() -> {
            this.lockLook = false;
      }, 170);

        addEvent(() -> {
            this.standbyOnVel = false;
            this.setImmovable(false);
        }, 195);

      addEvent(()-> {
          this.atomicCooldown = 30 * 20;
          this.attack_differential += 2;
          this.setFullBodyUsage(false);
          this.setFightMode(false);
          this.setAtomicAttack(false);
      }, 210);

    };
    private final Consumer<EntityLivingBase> teleport_lazer = (target) -> {
        this.setLazerTeleport(true);
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.doesBossSlowDown = true;
        this.performLazerAttack = true;
        lazerAttack.doAction();
        //
            //shoot lazer upon teleporting and exploding at the predicted position of the player
            //2.5 seconds or 50 ticks of lazer time

        addEvent(()-> {
            this.shakeTime = 15;
            this.setShaking(true);
        }, 70);
        addEvent(()-> {
            this.setImmovable(true);
            this.performLazerAttack = false;
        }, 75);

        addEvent(()-> {
            this.setShaking(false);
        }, 90);

        addEvent(()-> {
            this.setImmovable(false);
            this.doesBossSlowDown = false;
        this.attack_differential += 3;
        double healthDif = this.getHealth() / this.getMaxHealth();
        if(healthDif < 0.5) {
            this.standByCooldown = base_cooldown * 2;
        } else {
            this.standByCooldown = base_cooldown * 3;
        }

        this.setLazerTeleport(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        }, 120);
    };

    private final Consumer<EntityLivingBase> scream_attack = (target) -> {
        this.setScreamAttack(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.clearCurrentVelocity = true;
        this.playSound(SoundsHandler.VOIDIANT_CHARGE_LAZER, 3.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        addEvent(()-> {
        this.shakeTime = 40;
        this.setShaking(true);
        },10);

        addEvent(()-> {
            this.playSound(SoundsHandler.VOIDCLYSM_SCREAM, 3.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        }, 30);

        addEvent(()-> this.setShaking(false), 50);

        addEvent(()-> {
        this.setImmovable(false);
        this.standbyOnVel = false;
        //do scream attack
            if(!world.isRemote) {
                new ActionBombBarrage().performAction(this, target);
            }
        }, 35);

        addEvent(()-> {
        this.attack_differential += 5;
        double healthDif = this.getHealth() / this.getMaxHealth();
            if(healthDif < 0.5) {
                this.standByCooldown = base_cooldown * 2;
            } else {
                this.standByCooldown = base_cooldown * 4;
            }
        this.setScreamAttack(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        }, 75);
    };


    private final Consumer<EntityLivingBase> cast_projectiles = (target) -> {
      this.setCastSpell(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);

        addEvent(()-> {
            if(this.isMeleeMode()) {
                this.playSound(SoundsHandler.VOIDCLYSM_CAST_SPELL, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            } else {
                this.playSound(SoundsHandler.VOIDCLYSM_CAST_SPELL, 2.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            }
        }, 5);

      addEvent(()-> {
            //cast 5 projectiles
          new ActionCastVoidBolts().performAction(this, target);
      }, 25);

      addEvent(()-> {
        this.attack_differential += 1;
        this.setCastSpell(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
      }, 60);
    };

    private final Consumer<EntityLivingBase> grab_attack = (target) -> {
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.setGrabAttack(true);
        this.setImmovable(true);
        this.clearCurrentVelocity = true;

        addEvent(()-> {
            this.playSound(SoundsHandler.VOIDCLYSM_EQUIP, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 25);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-5));
            addEvent(()-> {
                this.setImmovable(false);
                ModUtils.attemptTeleport(targetedPos, this);
                this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.75f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 3);
        }, 37);

        addEvent(()-> {
            this.setImmovable(true);
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(3));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                this.grabDetection = true;
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 8);
        }, 37);

        addEvent(()-> {
            this.grabDetection = false;
            this.setGrabAttack(false);
            this.setImmovable(true);
            if(grabbedEntity != null) {
                this.setGrabTooContinue(target);
            } else {
                this.setGrabFinish(true);
                this.lockLook = false;

                addEvent(()-> {
                    this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.6f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                }, 5);
                addEvent(()-> {
                this.standbyOnVel = false;
                this.setImmovable(false);
                this.setGrabFinish(false);
                this.setFullBodyUsage(false);
                this.setFightMode(false);
                this.attack_differential += 1;
                }, 50);
            }
        }, 55);
    };

    private void setGrabTooContinue(EntityLivingBase target) {
        grab_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> grab_continue = (target) -> {
      this.setGrabContinue(true);
        this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.0, 0)));
        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
        float damage = (float) (0);
        ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.1f, 0, false);
        target.hurtResistantTime = 0;
        if(target.getMaxHealth() > 40) {
            target.setHealth((float) (target.getHealth() - 20));
        } else {
            target.setHealth((float) (target.getHealth() - (target.getMaxHealth() * MobConfig.voidclysm_grab_attack_damage)));
        }
        float healAmount = this.getMaxHealth() * 0.12F;
        this.heal(healAmount);
        target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 200, 0, false, false));
        target.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 100, 0, false, false));
        this.setImmovable(false);
        this.setBossToFlyHigh = true;

        addEvent(()-> {
            this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            this.setBossToFlyHigh = false;
            this.grabbedEntity = null;
            this.lockLook = false;
            this.setImmovable(true);
        }, 37);

        addEvent(()-> {
            this.setImmovable(false);
            this.setFightMode(false);
            this.standbyOnVel = false;
            this.setFullBodyUsage(false);
            this.setGrabContinue(false);
            this.attack_differential += 3;
            this.standByCooldown = base_cooldown;
        }, 80);

    };
    private final Consumer<EntityLivingBase> summon_spike_magic = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setRaiseSpikes(true);
      //strafe to right during this attack

        addEvent(()-> {
            this.playSound(SoundsHandler.OBSIDILITH_CAST, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        }, 20);

        addEvent(()-> {
            if(!world.isRemote) {
                addEvent(()-> {
                    Vec3d targetOldPos = target.getPositionVector();
                    addEvent(()-> {
                        Vec3d targetedPos = target.getPositionVector();
                        Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 3);
                        this.spawnSpikeAction(predictedPosition);
                    }, 3);
                }, 1);

                addEvent(()-> {
                    Vec3d targetOldPos = target.getPositionVector();
                    addEvent(()-> {
                        Vec3d targetedPos = target.getPositionVector();
                        Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 3);
                        this.spawnSpikeAction(predictedPosition);
                    }, 3);
                }, 27);

                addEvent(()-> {
                    Vec3d targetOldPos = target.getPositionVector();
                    addEvent(()-> {
                        Vec3d targetedPos = target.getPositionVector();
                        Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 3);
                        this.spawnSpikeAction(predictedPosition);
                    }, 3);
                }, 57);
            }
        }, 30);

        addEvent(()-> {
            this.attack_differential += 4;
            double healthDif = this.getHealth() / this.getMaxHealth();
            if(healthDif < 0.5) {
                this.standByCooldown = base_cooldown * 2;
            } else {
                this.standByCooldown = base_cooldown * 3;
            }
            this.setRaiseSpikes(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
        }, 60);
    };

    private final Consumer<EntityLivingBase> clap_attack = (target) -> {
      this.setFightMode(true);
      this.setClap(true);
      this.setImmovable(true);
      this.clearCurrentVelocity = true;
      addEvent(()-> {
          //summon rune sound
          this.playSound(SoundsHandler.VOIDCLYSM_CLAP_ATTACK, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
      }, 15);

      addEvent(()-> {
        //do spike action
          new ActionClapAttack().performAction(this, target);
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
          Main.proxy.spawnParticle(22,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
          Main.proxy.spawnParticle(22,world, relPos.x, this.posY + 1, relPos.z, 0, 0, 0);
          Main.proxy.spawnParticle(22,world, relPos.x, this.posY + 2, relPos.z, 0, 0, 0);
      }, 20);

        addEvent(()-> {
            //do spike action AOE and rune destroy sound
            this.shakeTime = 25;
            this.setShaking(true);
        }, 27);

        addEvent(()-> {
            this.setShaking(false);
        }, 52);
      addEvent(()-> {
          this.standbyOnVel = false;
          this.setImmovable(false);
      }, 35);
      addEvent(()-> {
          double healthDif = this.getHealth() / this.getMaxHealth();
          if(healthDif < 0.5) {
              this.standByCooldown = base_cooldown;
          } else {
              this.standByCooldown = base_cooldown * 2;
          }
        this.attack_differential += 2;
        this.setFightMode(false);
        this.setClap(false);
      }, 50);
    };

    private final Consumer<EntityLivingBase> strike_attack = (target) -> {
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.setImmovable(true);
        int randI = ModRand.range(1, 6);
        addEvent(()-> this.playSound(SoundsHandler.VOIDCLYSM_EQUIP, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
        //Two Strikes
        if(randI >= 4) {
            this.setStrikeTwo(true);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(2));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.16),0F);
                }, 5);
            }, 20);

            addEvent(()-> {
                this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.0, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.5f, 0, false);
            }, 30);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 40);

            addEvent(()-> this.playSound(SoundsHandler.VOIDCLYSM_EQUIP, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 42);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(2));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.16),0F);
                }, 5);
            }, 50);

            addEvent(()-> {
                this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.0, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.5f, 0, false);
            }, 60);

            addEvent(()-> {
                this.setImmovable(true);
                addEvent(()-> {
                    this.lockLook = false;
                }, 5);
            }, 75);

            addEvent(()-> {
                this.attack_differential += 3;
                this.setStrikeTwo(false);
                this.setFullBodyUsage(false);
                this.setFightMode(false);
                this.setImmovable(false);
            }, 90);
            //One Strike
        } else {
            this.setStrikeOne(true);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(2));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.16),0F);
                }, 5);
            }, 20);

            addEvent(()-> {
                this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.0, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.5f, 0, false);
            }, 30);

            addEvent(()-> {
                this.setImmovable(true);
            }, 40);

            addEvent(()-> {
                this.lockLook = false;
            }, 50);

            addEvent(()-> {
            this.attack_differential += 1;
            this.setStrikeOne(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            }, 65);
        }
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "idle_flaps__controller", 0, this::predicateIdleFlaps));
        data.addAnimationController(new AnimationController(this, "attacks_controller", 0, this::predicateAttacks));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFightMode() && !this.isParry() && !this.isDeflect() && !this.isSummon()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MODEL_IDLE, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdleFlaps(AnimationEvent<E> event) {
        if(!this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLAPS_IDLE, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isCastSpell()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_SPELL, false));
                return PlayState.CONTINUE;
            }
            if(this.isClap()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CLAP, false));
                return PlayState.CONTINUE;
            }
            if(this.isLazerTeleport()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LAZER_TELEPORT, false));
                return PlayState.CONTINUE;
            }
            if(this.isScreamAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SCREAM, false));
                return PlayState.CONTINUE;
            }
            if(this.isStrikeTwo()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRIKE_TWO, false));
                return PlayState.CONTINUE;
            }
            if(this.isStrikeOne()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRIKE_ONE, false));
                return PlayState.CONTINUE;
            }
            if(this.isRaiseSpikes()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_RAISE_SPIKES, false));
                return PlayState.CONTINUE;
            }
            if(this.isGrabAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_GRAB_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isGrabContinue()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_GRAB_CONTINUE, false));
                return PlayState.CONTINUE;
            }
            if(this.isGrabFinish()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_GRAB_END, false));
                return PlayState.CONTINUE;
            }
            if(this.isMeleeSlam()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MELEE_SLAM, false));
                return PlayState.CONTINUE;
            }
            if(this.isAtomicAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATOMIC_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isSummonTrack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_TRACK_PROJECTILES, false));
                return PlayState.CONTINUE;
            }
        }

        if(this.isParry()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PARRY, false));
            return PlayState.CONTINUE;
        }
        if(this.isDeflect()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DEFLECT, false));
            return PlayState.CONTINUE;
        }
        if(this.isSummon()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void setCustomNameTag(@Nonnull String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void addTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            ModUtils.performNTimes(50, (i) -> {
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + ModRand.getFloat(5), this.posY + ModRand.getFloat(5),
                        this.posZ + ModRand.getFloat(5), 0, 0, 0);
            });
        }

        if (id == stopLazerByte) {
            this.renderLazerPos = null;
        } else if(id == ModUtils.PARTICLE_BYTE) {
            for (int i = 0; i < 5; i++) {
                Vec3d lookVec = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
                Vec3d randOffset = ModUtils.rotateVector2(lookVec, lookVec.crossProduct(ModUtils.Y_AXIS), ModRand.range(-70, 70));
                randOffset = ModUtils.rotateVector2(randOffset, lookVec, ModRand.range(0, 360)).scale(1.5f);
                Vec3d velocity = Vec3d.ZERO.subtract(randOffset).normalize().scale(0.15f).add(new Vec3d(this.motionX, this.motionY, this.motionZ));
                Vec3d particlePos = this.getPositionEyes(1).add(ModUtils.getAxisOffset(lookVec, new Vec3d(0, -1, 0))).add(randOffset);
                ParticleManager.spawnColoredSmoke(world, particlePos, ModColors.LIGHT_PURPLE, velocity);
            }
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
    public int tickTimer() {
        return this.ticksExisted;
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if(!this.isImmovable()) {
            ModUtils.aerialTravel(this, strafe, vertical, forward);
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        //we want our boy to be stable while he's slinging melee attack or in a angered State
        if(this.isMeleeMode()) {
            this.dataManager.set(LOOK, 0F);
        } else {
            this.dataManager.set(LOOK, clampedLook);
        }
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 40.0F);
            if (dist >= 40.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.5F * screamMult);
        }
        return 0;
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

    public void spawnSpikeAction(Vec3d predictedPos) {
        EntityLivingBase target = this.getAttackTarget();
        //1
        if(target != null) {
            EntityBlueWave spike = new EntityBlueWave(this.world);
            BlockPos area = new BlockPos(predictedPos.x, predictedPos.y, predictedPos.z);
            int y = getSurfaceHeight(this.world, new BlockPos(area.getX(), 0, area.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike.setPosition(area.getX(), y + 1, area.getZ());
            world.spawnEntity(spike);
            spike.playSound(SoundsHandler.APPEARING_WAVE, 0.75f, 1.0f / getRNG().nextFloat() * 0.04F + 1.2F);;
            //2
            EntityBlueWave spike2 = new EntityBlueWave(this.world);
            BlockPos area2 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z);
            int y2 = getSurfaceHeight(this.world, new BlockPos(area2.getX(), 0, area2.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike2.setPosition(area2.getX(), y2 + 1, area2.getZ());
            world.spawnEntity(spike2);
            //3
            EntityBlueWave spike3 = new EntityBlueWave(this.world);
            BlockPos area3 = new BlockPos(predictedPos.x - 1, predictedPos.y, predictedPos.z);
            int y3 = getSurfaceHeight(this.world, new BlockPos(area3.getX(), 0, area3.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike3.setPosition(area3.getX(), y3 + 1, area3.getZ());
            world.spawnEntity(spike3);
            //4
            EntityBlueWave spike4 = new EntityBlueWave(this.world);
            BlockPos area4 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 1);
            int y4 = getSurfaceHeight(this.world, new BlockPos(area4.getX(), 0, area4.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike4.setPosition(area4.getX(),y4 + 1, area4.getZ());
            world.spawnEntity(spike4);
            //5
            EntityBlueWave spike5 = new EntityBlueWave(this.world);
            BlockPos area5 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z - 1);
            int y5 = getSurfaceHeight(this.world, new BlockPos(area5.getX(), 0, area5.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike5.setPosition(area5.getX(), y5 + 1, area5.getZ());
            world.spawnEntity(spike5);
            //6
            EntityBlueWave spike6 = new EntityBlueWave(this.world);
            BlockPos area6 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z + 1);
            int y6 = getSurfaceHeight(this.world, new BlockPos(area6.getX(), 0, area6.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike6.setPosition(area6.getX(), y6 + 1, area6.getZ());
            world.spawnEntity(spike6);
            //7
            EntityBlueWave spike7 = new EntityBlueWave(this.world);
            BlockPos area7 = new BlockPos(predictedPos.x +1 , predictedPos.y, predictedPos.z - 1);
            int y7 = getSurfaceHeight(this.world, new BlockPos(area7.getX(), 0, area7.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike7.setPosition(area7.getX(), y7 + 1, area7.getZ());
            world.spawnEntity(spike7);
            //8
            EntityBlueWave spike8 = new EntityBlueWave(this.world);
            BlockPos area8 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z - 1);
            int y8 = getSurfaceHeight(this.world, new BlockPos(area8.getX(), 0, area8.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike8.setPosition(area8.getX(), y8 + 1, area8.getZ());
            world.spawnEntity(spike8);
            //9
            EntityBlueWave spike9 = new EntityBlueWave(this.world);
            BlockPos area9 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z + 1);
            int y9 = getSurfaceHeight(this.world, new BlockPos(area9.getX(), 0, area9.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike9.setPosition(area9.getX(), y9 + 1, area9.getZ());
            world.spawnEntity(spike9);
            //10
            EntityBlueWave spike10 = new EntityBlueWave(this.world);
            BlockPos area10 = new BlockPos(predictedPos.x +2, predictedPos.y, predictedPos.z );
            int y10 = getSurfaceHeight(this.world, new BlockPos(area10.getX(), 0, area10.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike10.setPosition(area10.getX(), y10 + 1, area10.getZ());
            world.spawnEntity(spike10);
            //11
            EntityBlueWave spike11 = new EntityBlueWave(this.world);
            BlockPos area11 = new BlockPos(predictedPos.x - 2, predictedPos.y, predictedPos.z );
            int y11 = getSurfaceHeight(this.world, new BlockPos(area11.getX(), 0, area11.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike11.setPosition(area11.getX(), y11 + 1, area11.getZ());
            world.spawnEntity(spike11);
            //12
            EntityBlueWave spike12 = new EntityBlueWave(this.world);
            BlockPos area12 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 2);
            int y12 = getSurfaceHeight(this.world, new BlockPos(area12.getX(), 0, area12.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike12.setPosition(area12.getX(), y12 + 1, area12.getZ());
            world.spawnEntity(spike12);
            //13
            EntityBlueWave spike13 = new EntityBlueWave(this.world);
            BlockPos area13 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z -2);
            int y13 = getSurfaceHeight(this.world, new BlockPos(area13.getX(), 0, area13.getZ()), (int) this.posY - 25, (int) this.posY + 7);
            spike13.setPosition(area13.getX(), y13 + 1, area13.getZ());
            world.spawnEntity(spike13);
        }
    }

    public int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock()) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.grabbedEntity != null || this.isParry() || this.isSummon()) {
            return false;
        } else if (!this.isFightMode() && amount > 0.0F && this.canBlockDamageSource(source)) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }
            this.playSound(SoundsHandler.OBSIDILITH_SHIELD, 0.6f, 0.6f + ModRand.getFloat(0.2f));

            return false;
        }

        if(ModConfig.boss_cap_damage_enabled && amount > MobConfig.voidclysm_damage_cap) {
            return super.attackEntityFrom(source, MobConfig.voidclysm_damage_cap);
        }

        //do regular damage
        return super.attackEntityFrom(source, amount * 0.9F);
    }

    protected int blockCooldown = MobConfig.voidclysm_block_cooldown * 20;
    private boolean parriedLastAttack = false;


    @Override
    public void onDeath(DamageSource cause) {
        if(this.getSpawnLocation() != null && !world.isRemote) {
            this.placeLootOnDeath(new BlockPos(this.posX, this.posY, this.posZ));
            this.turnBossIntoSummonSpawner(this.getSpawnLocation());
        }
        super.onDeath(cause);
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "voidclysm");

    private void placeLootOnDeath(BlockPos blockpos) {
        if(!world.isRemote) {
            if (this.getSpawnLocation() != null) {
                world.setBlockState(this.getSpawnLocation().add(4, 0, 0), Blocks.BLACK_SHULKER_BOX.getDefaultState());
                TileEntity te = world.getTileEntity(this.getSpawnLocation().add(4, 0, 0));
                if (te instanceof TileEntityShulkerBox) {
                    TileEntityShulkerBox chest = (TileEntityShulkerBox) te;
                    chest.setLootTable(LOOT_MOB, rand.nextLong());
                }
            } else {
                world.setBlockState(blockpos, Blocks.BLACK_SHULKER_BOX.getDefaultState());
                TileEntity te = world.getTileEntity(blockpos);
                if (te instanceof TileEntityShulkerBox) {
                    TileEntityShulkerBox chest = (TileEntityShulkerBox) te;
                    chest.setLootTable(LOOT_MOB, rand.nextLong());
                }
            }
        }
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if(!this.isFightMode()) {
            if (!damageSourceIn.isUnblockable() && !this.isFightMode() && blockCooldown <= 0) {
                EntityLivingBase target = this.getAttackTarget();
                if(target != null && !this.isDeflect() && !this.isParry()) {
                    //We want a certain distance where the attack part if variable, greater than 6 and less than 14
                    if(damageSourceIn.getImmediateSource() instanceof EntityPlayer && this.isMeleeMode() && !this.parriedLastAttack) {
                        EntityPlayer player = ((EntityPlayer) damageSourceIn.getImmediateSource());
                        //parry the fool
                        if(player.getHeldItemMainhand().getItem() instanceof ItemAxe || player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                            this.doBlockActionParry(player);
                        } else {
                            this.doBlockAction();
                        }
                    } else {
                        if(this.isMeleeMode()) {
                            this.parriedLastAttack = false;
                        }
                        this.doBlockAction();
                    }
                } else if (!this.isDeflect() && !this.isParry()){
                    //if there is no target, just do a regular block with the cooldown met
                    this.doBlockAction();
                }
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

    private void doBlockActionParry(EntityPlayer player) {
        this.setParry(true);
        this.clearCurrentVelocity = true;
        this.setImmovable(true);
        this.parriedLastAttack = true;
        player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 5, false, false));

        addEvent(()-> {
            this.playSound(SoundsHandler.VOIDCLYSM_EQUIP, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 7);

        addEvent(()-> {
            this.playSound(SoundsHandler.IMPERIAL_SWORD_PARRY, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        }, 5);
        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = player.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d softTargetPos = player.getPositionVector().add(posSet.scale(-2));
            addEvent(()-> {
                this.setImmovable(false);
                //this.holdPosition = false;
                double distance = this.getPositionVector().distanceTo(softTargetPos);
                ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.16),0F);
            }, 5);
        }, 10);

        addEvent(()-> {
            this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (this.getAttack());
            ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0f, 0, false);
        }, 25);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(true);
        }, 32);

        addEvent(()-> {
            this.setImmovable(false);
            this.standbyOnVel = false;
            this.setParry(false);
            blockCooldown = MobConfig.voidclysm_block_cooldown * 20;
        }, 60);
    }

    private void doBlockAction() {
        this.setDeflect(true);
        addEvent(()-> {
            this.setDeflect(false);
            blockCooldown = MobConfig.voidclysm_block_cooldown * 20;
        }, 25);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.VOIDCLYSM_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.VOIDCLYSM_IDLE;
    }


    @Nullable
    @Override
    public SoundEvent getBossMusic() {
        return SoundsHandler.VOIDCLYSM_TRACK;
    }
}
