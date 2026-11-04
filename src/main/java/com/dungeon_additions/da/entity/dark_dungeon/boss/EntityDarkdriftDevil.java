package com.dungeon_additions.da.entity.dark_dungeon.boss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.blocks.boss.BlockEnumBossSummonState;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityAIDarkDriftDevilAttack;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.dark_dungeon.EntityRohsEye;
import com.dungeon_additions.da.entity.dark_dungeon.boss.action.*;
import com.dungeon_additions.da.entity.dark_dungeon.boss.util.RohUtils;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.ActionDauntlessImpact;
import com.dungeon_additions.da.entity.generic.EntityDelayedExplosion;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.action.ActionHolySpikeAOE;
import com.dungeon_additions.da.entity.tileEntity.TileEntityBossReSummon;
import com.dungeon_additions.da.entity.util.IEntitySound;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.util.*;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import mod.azure.azurelib.util.MoveAnalysis;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class EntityDarkdriftDevil extends EntityDarkBase implements  IAttack, IEntitySound {

    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);
    private final MoveAnalysis moveAnalysis;
    private Consumer<EntityLivingBase> prevAttack;
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_10));
    private EntityLivingBase grabbedEntity;
    private boolean grabDetection = false;
    private boolean castingDespair = false;
    private int despair_cooldown = 30 * 20;
    private int jump_amount = 0;
    private int cooldown_wait_time = 0;
    private boolean isStartingJumpStrike = false;

    private final String ANIM_IDLE_LOWER = "idle_lower";
    private final String ANIM_IDLE_UPPER = "idle_upper";
    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_IDLE_EYES = "idle_eyes";

    //meditation
    private final String ANIM_ENTER_MEDITATION = "enter_meditation";
    private final String ANIM_MEDITATION_LOOP = "meditation_loop";
    private final String ANIM_EXIT_MEDITATION = "exit_meditation";
    private final String ANIM_DEATH = "death";

    //attacks one anim
    private final String ANIM_TELEPORT_ATTACK = "teleport";
    private final String ANIM_CAST_SIGIL = "cast_sigil";
    private final String ANIM_SUMMON_METEORS = "summon_meteors";
    private final String ANIM_GREAT_DEATH = "great_death";
    private final String ANIM_CAST_SIGIL_ROUND = "cast_multi_sigil";
    private final String ANIM_DESPAIR = "despair";
    private final String ANIM_SPEAR_INCARNATE = "spear_incarnate";
    private final String ANIM_VERTICAL_SLASH = "vertical_slash";
    private final String ANIM_HORIZONTAL_SLASH = "horizontal_slash";
    private final String ANIM_JUMP_SLAM = "jump_slam";

    //combos
    private final String ANIM_LONG_SWING = "long_swing";
    private final String ANIM_LONG_SWING_CONTINUE = "long_swing_continue";
    private final String ANIM_LONG_SWING_FINISH = "long_swing_finish";

    private final String ANIM_STOMP = "stomp";
    private final String ANIM_STOMP_CONTINUE = "stomp_continue";
    private final String ANIM_STOMP_FINISH = "stomp_finish";

    private final String ANIM_THROW_DAGGERS = "throw_daggers";
    private final String ANIM_THROW_DAGGERS_CONTINUE = "throw_daggers_continue";
    private final String ANIM_THROW_DAGGERS_DASH = "throw_daggers_dash";

    private final String ANIM_MELEE_SWING = "melee_swing";
    private final String ANIM_MELEE_SWING_CONTINUE = "melee_swing_continue";
    private final String ANIM_MELEE_SWING_FINISH = "melee_swing_finish";
    private final String ANIM_MELEE_SWING_DASH = "melee_swing_dash";
    private final String ANIM_MELEE_SWING_FINISH_TWO = "melee_swing_finish_2";

    private final String ANIM_BEGIN_JUMPSTRIKE = "jump_strike";
    private final String ANIM_LOOP_JUMPSTRIKE = "jump_strike_hold";
    private final String ANIM_END_JUMPSTRIKE = "jump_strike_end";

    private final String ANIM_GRAB = "grab";
    private final String ANIM_GRAB_CONTINUE = "grab_continue";
    private final String ANIM_GRAB_FINISH = "grab_finish";


    private static final DataParameter<Boolean> GRAB_CONTINUE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_SLAM = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_DESPAIR = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> EVADE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ENTER_MEDITATION = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MEDITATION_LOOP = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_MEDITATION = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEATH_STATE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEMON_BUFF = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LOOP_JUMP_STRIKE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BLOCK_POS);
    public static DataParameter<Boolean> SET_SPAWN_LOC = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAD_PREVIOUS_TARGET = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> STAT_LINE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.FLOAT);


    public void setDeathState(boolean value) {this.dataManager.set(DEATH_STATE, Boolean.valueOf(value));}
    public boolean isDeathState() {return this.dataManager.get(DEATH_STATE);}
    private void setLoopJumpStrike(boolean value) {this.dataManager.set(LOOP_JUMP_STRIKE, Boolean.valueOf(value));}
    private boolean isLoopJumpStrike() {return this.dataManager.get(LOOP_JUMP_STRIKE);}
    private void setGrabContinue(boolean value) {this.dataManager.set(GRAB_CONTINUE, Boolean.valueOf(value));}
    private boolean isGrabContinue() {return this.dataManager.get(GRAB_CONTINUE);}
    private void setJumpSlam(boolean value) {this.dataManager.set(JUMP_SLAM, Boolean.valueOf(value));}
    private boolean isJumpSlam() {return this.dataManager.get(JUMP_SLAM);}
    private void setCastDespair(boolean value) {this.dataManager.set(CAST_DESPAIR, Boolean.valueOf(value));}
    public boolean isCastDespair() {return this.dataManager.get(CAST_DESPAIR);}
    private void setEvade(boolean value) {this.dataManager.set(EVADE, Boolean.valueOf(value));}
    private boolean isEvade() {return this.dataManager.get(EVADE);}
    private void setEnterMeditation(boolean value) {this.dataManager.set(ENTER_MEDITATION, Boolean.valueOf(value));}
    private boolean isEnterMeditation() {return this.dataManager.get(ENTER_MEDITATION);}
    private void setMeditationLoop(boolean value) {this.dataManager.set(MEDITATION_LOOP, Boolean.valueOf(value));}
    public boolean isMeditationLoop() {return this.dataManager.get(MEDITATION_LOOP);}
    private void setDemonBuff(boolean value) {this.dataManager.set(DEMON_BUFF, Boolean.valueOf(value));}
    public boolean isDemonBuff() {return this.dataManager.get(DEMON_BUFF);}
    private void setEndMeditation(boolean value) {this.dataManager.set(END_MEDITATION, Boolean.valueOf(value));}
    private boolean isEndMeditation() {return this.dataManager.get(END_MEDITATION);}
    public boolean isSetSpawnLoc() {
        return this.dataManager.get(SET_SPAWN_LOC);
    }
    public void setSetSpawnLoc(boolean value) {
        this.dataManager.set(SET_SPAWN_LOC, Boolean.valueOf(value));
    }
    public void setSpawnLocation(BlockPos pos) {
        this.dataManager.set(SPAWN_LOCATION, pos);
    }
    public void setStateLine(float value) {
        this.dataManager.set(STAT_LINE, Float.valueOf(value));
    }
    public float getStatLine() {
        return this.dataManager.get(STAT_LINE);
    }

    public BlockPos getSpawnLocation() {
        return this.dataManager.get(SPAWN_LOCATION);
    }
    public boolean isHadPreviousTarget() {return this.dataManager.get(HAD_PREVIOUS_TARGET);}
    public void setHadPreviousTarget(boolean value) {this.dataManager.set(HAD_PREVIOUS_TARGET, Boolean.valueOf(value));}


    public EntityDarkdriftDevil(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.1F, 3.45F);
        this.iAmBossMob = true;
        this.isImmuneToFire = true;
        this.falter_resistance = 1.5F;
        this.hemorrhage_resistance = 0.92F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public EntityDarkdriftDevil(World worldIn) {
        super(worldIn);
        this.setSize(1.1F, 3.45F);
        this.iAmBossMob = true;
        this.isImmuneToFire = true;
        this.falter_resistance = 1.5F;
        this.hemorrhage_resistance = 0.92F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public EntityDarkdriftDevil(World world, int timesUsed, BlockPos pos) {
        super(world);
        this.setSize(1.1F, 3.45F);
        this.timesUsed = timesUsed;
        this.iAmBossMob = true;
        this.isImmuneToFire = true;
        this.falter_resistance = 1.5F;
        this.hemorrhage_resistance = 0.92F;
        this.timesUsed++;
        this.doBossReSummonScaling();
        this.onSummon(pos);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public void onSummon(EntityLivingBase actor) {
        this.setPosition(actor.posX, actor.posY - 0.75, actor.posZ);
        BlockPos pos = new BlockPos(actor.posX, actor.posY - 1, actor.posZ);
        this.setSpawnLocation(pos);
        this.setSetSpawnLoc(true);
        world.spawnEntity(this);
        this.lockLook = true;
        actor.setDead();
        world.setBlockToAir(pos.add(0, 2, 0));
        world.setBlockToAir(pos.add(0, 3, 0));
        this.setEndMeditation(true);
        animator.performAttack(ANIM_EXIT_MEDITATION);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        ModUtils.performNTimes(15, (b) -> {
            // Main.proxy.spawnParticle(30, finalPos.x + ModRand.getFloat(1.5F), finalPos.y + ModRand.getFloat(1.4F) + 2, finalPos.z + ModRand.getFloat(1.5F), 0, -0.06, 0, 7340032);
            Main.proxy.spawnParticle(48, actor.world, this.posX + ModRand.getFloat(1.5F), this.posY + ModRand.getFloat(1.4F) + 1.4F , this.posZ + ModRand.getFloat(1.5F), 0, 0.06, 0, 11607828);
        });
        this.playSound(SoundsHandler.ROH_SUMMON, 1.5F, 1f + ModRand.getFloat(0.1f));
        addEvent(()-> {
            this.lockLook = false;
        }, 20);
        addEvent(()-> {
            this.setEndMeditation(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
        }, 25);
    }

    public void onSummon(BlockPos pos) {
        this.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        this.setSpawnLocation(pos.add(0.5, 0, 0.5));
        this.setSetSpawnLoc(true);
        world.spawnEntity(this);
        this.lockLook = true;
        this.setEndMeditation(true);
        animator.performAttack(ANIM_EXIT_MEDITATION);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        ModUtils.performNTimes(15, (b) -> {
            Main.proxy.spawnParticle(48, world, this.posX + ModRand.getFloat(1.5F), this.posY + ModRand.getFloat(1.4F) + 1.4F , this.posZ + ModRand.getFloat(1.5F), 0, 0.06, 0, 11607828);
        });
        this.playSound(SoundsHandler.ROH_SUMMON, 1.5F, 1f + ModRand.getFloat(0.1f));
        addEvent(()-> {
            this.lockLook = false;
        }, 20);
        addEvent(()-> {
            this.setEndMeditation(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
        }, 25);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Grab_Continue", this.isGrabContinue());
        nbt.setBoolean("Jump_Slam", this.isJumpSlam());
        nbt.setBoolean("Cast_Despair", this.isCastDespair());
        nbt.setBoolean("Evade", this.isEvade());
        nbt.setBoolean("Enter_Meditation", this.isEnterMeditation());
        nbt.setBoolean("Meditation_Loop", this.isMeditationLoop());
        nbt.setBoolean("End_Meditation", this.isEndMeditation());
        nbt.setBoolean("Loop_Jumpstrike", this.isLoopJumpStrike());
        nbt.setBoolean("Death_State", this.isDeathState());
        nbt.setBoolean("Demon_Buff", this.isDemonBuff());
        nbt.setBoolean("Had_Target", this.isHadPreviousTarget());
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
        nbt.setBoolean("Set_Spawn_Loc", this.dataManager.get(SET_SPAWN_LOC));
        nbt.setFloat("Stat_Line", this.getStatLine());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setGrabContinue(nbt.getBoolean("Grab_Continue"));
        this.setDeathState(nbt.getBoolean("Death_State"));
        this.setJumpSlam(nbt.getBoolean("Jump_Slam"));
        this.setCastDespair(nbt.getBoolean("Cast_Despair"));
        this.setEvade(nbt.getBoolean("Evade"));
        this.setLoopJumpStrike(nbt.getBoolean("Loop_Jumpstrike"));
        this.setEnterMeditation(nbt.getBoolean("Enter_Meditation"));
        this.setMeditationLoop(nbt.getBoolean("Meditation_Loop"));
        this.setEndMeditation(nbt.getBoolean("End_Meditation"));
        this.setDemonBuff(nbt.getBoolean("Demon_Buff"));
        this.setHadPreviousTarget(nbt.getBoolean("Had_Target"));
        this.dataManager.set(SET_SPAWN_LOC, nbt.getBoolean("Set_Spawn_Loc"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
        this.setStateLine(nbt.getFloat("Stat_Line"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(GRAB_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(JUMP_SLAM, Boolean.valueOf(false));
        this.dataManager.register(CAST_DESPAIR, Boolean.valueOf(false));
        this.dataManager.register(EVADE, Boolean.valueOf(false));
        this.dataManager.register(ENTER_MEDITATION, Boolean.valueOf(false));
        this.dataManager.register(MEDITATION_LOOP, Boolean.valueOf(false));
        this.dataManager.register(END_MEDITATION, Boolean.valueOf(false));
        this.dataManager.register(LOOP_JUMP_STRIKE, Boolean.valueOf(false));
        this.dataManager.register(DEATH_STATE, Boolean.valueOf(false));
        this.dataManager.register(SET_SPAWN_LOC, Boolean.valueOf(false));
        this.dataManager.register(DEMON_BUFF, Boolean.valueOf(false));
        this.dataManager.register(HAD_PREVIOUS_TARGET, Boolean.valueOf(false));
        //
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
        this.dataManager.register(STAT_LINE,  0.75F);
        super.entityInit();
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(45D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.roh_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.31D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.roh_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.roh_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.roh_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIDarkDriftDevilAttack<EntityDarkdriftDevil>(this, 1.0D, 5, 25, 0.25F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    private boolean didAirAttack = false;

    private int HoverTimeIncrease;

    private int meditation_time = 250;
    private boolean successful_ritual = true;
    private int demon_ritual_difficulty = 0;
    private int demon_buff_time = 45 * 20;
    private float demon_buff_damage = 0;
    private boolean awaiting_meditation = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.moveAnalysis.update();
        double currentHealth = this.getHealth() / this.getMaxHealth();
        this.bossInfo.setPercent(getHealth() / getMaxHealth());

        if(world.isRemote && ticksExisted == 5 && ModConfig.experimental_features && MobConfig.darkdrift_devil_boss_music) {
            this.playMusic(this);
        }

        if(!world.isRemote) {
            this.despair_cooldown--;
            if(cooldown_wait_time >= 0) {
                cooldown_wait_time--;
            }
            //demon buff
            if(this.isDemonBuff()) {
                if(demon_buff_time <= 0) {
                    this.setDemonBuff(false);
                    this.demon_buff_time = 45 * 20;
                    this.demon_buff_damage = 0;
                } else {
                    demon_buff_time--;
                    demon_buff_damage = 7;
                }
            }

            //Despair Attack
            if(this.castingDespair) {
                //Causes corruption
                List<EntityPlayer> nearby_players = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(40), e -> !e.getIsInvulnerable());
                if(!nearby_players.isEmpty()) {
                    for(EntityPlayer player : nearby_players) {
                        if(player.isEntityAlive()) {
                            PlayerRohCorruptionUtils.setRohsCorruption(player, PlayerRohCorruptionUtils.getRohsCorruption(player) + 1);
                        }
                    }
                }

                if(ticksExisted % 10 == 0) {
                    //total that can be active
                    int sigilAmount = 4 + (playersNearbyAmount * 2);
                    int currentCount = 0;
                    //How many can be spawned per second
                    int waveAmount = 1 + playersNearbyAmount;
                    List<EntityDemonRitual> nearby_sigils = this.world.getEntitiesWithinAABB(EntityDemonRitual.class, this.getEntityBoundingBox().grow(40), e -> !e.getIsInvulnerable());
                    if (!nearby_sigils.isEmpty()) {
                        for (EntityDemonRitual sigil : nearby_sigils) {
                            if (sigil.isEntityAlive()) {
                                currentCount++;
                            }
                        }
                    }
                    if (currentCount < sigilAmount) {
                        for (int i = 0; i <= waveAmount; i++) {
                            EntityDemonRitual ritual = new EntityDemonRitual(world);
                            if(this.getSpawnLocation() != null) {
                                ritual.setPosition(this.getSpawnLocation().getX() + ((ModRand.range(1, 11)) * ModRand.randSign()), this.getSpawnLocation().getY() + 1.5, this.getSpawnLocation().getZ() + ((ModRand.range(1, 11)) * ModRand.randSign()));
                            } else {
                                ritual.setPosition(this.posX + ((ModRand.range(1, 11)) * ModRand.randSign()), this.posY - 3, this.posZ + ((ModRand.range(1, 11)) * ModRand.randSign()));
                            }
                            world.spawnEntity(ritual);
                        }
                    }
                }
            }

            //meditation stuff
            if(!this.isDemonBuff() && !this.isEnterMeditation() && !this.isMeditationLoop() && !this.isEndMeditation()) {
                if(currentHealth <= this.getStatLine()) {
                    if(!this.isFightMode()) {
                        this.enterMeditationMode();
                        this.awaiting_meditation = false;
                    } else {
                        this.awaiting_meditation = true;
                    }
                }
            }

            //handles everything during the ritual
            if(this.isMeditationLoop() && this.getStatLine() != 0 && !this.isEndMeditation()) {

                List<EntityDemonRitual> nearby_sigils = this.world.getEntitiesWithinAABB(EntityDemonRitual.class, this.getEntityBoundingBox().grow(30), e -> !e.getIsInvulnerable());
                if(meditation_time < 0 || meditation_time < 200 && nearby_sigils.isEmpty()) {
                    if(nearby_sigils.isEmpty()) {
                        successful_ritual = false;
                        this.demon_ritual_difficulty++;
                    } else {
                        successful_ritual = true;
                    }
                    this.exitMeditationMode();
                } else {
                    meditation_time--;
                    if(ticksExisted % 25 == 0) {
                        this.playSound(SoundsHandler.ROH_CHANT, 1.25f, 1.0F);
                    }
                }

                //summon sigils
                if(meditation_time == 248) {
                    int sigil_amount = 0;
                    if(this.getStatLine() == 0.75) {
                        sigil_amount = 4 + ((playersNearbyAmount * 2) - 1);
                    } else if (this.getStatLine() == 0.5) {
                        sigil_amount = 5 + ((playersNearbyAmount * 2) - 2);
                    } else if (this.getStatLine() == 0.25) {
                        sigil_amount = 6 + ((playersNearbyAmount * 2) - 2);
                    }
                    //adds extra spawns
                    if(!successful_ritual) {
                        sigil_amount += demon_ritual_difficulty * 2;
                    }

                    for(int i = 0; i <= sigil_amount; i++) {
                        EntityDemonRitual ritual = new EntityDemonRitual(world);
                        ritual.setPosition(this.posX + ((ModRand.range(1, 8) + 3) * ModRand.randSign()), this.posY + ModRand.range(1, 6), this.posZ + ((ModRand.range(1, 8) + 3) * ModRand.randSign()));
                        world.spawnEntity(ritual);
                    }
                }
            }

            if(HoverTimeIncrease > 0) {
                this.motionY = 0.25;
                HoverTimeIncrease--;
            }

            if (this.didAirAttack && this.onGround) {
                this.didAirAttack = false;
                this.lockLook = false;
            }

            if (this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
                Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());

                double distSq = this.getDistanceSq(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                double distance = Math.sqrt(distSq);
                //This basically makes it so the Obsidilith will be teleported if they are too far away from the Arena
                if (!world.isRemote) {
                    if (distance > 35) {
                        this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                    }
                }
            }

            EntityLivingBase target = this.getAttackTarget();

            if (this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
                if (target != null) {
                    if (target instanceof EntityPlayer) {
                        this.setHadPreviousTarget(true);
                    }
                }

                //Boss Lives System
                if(!this.bossInfo.getPlayers().isEmpty() && ModConfig.boss_player_lives_enabled) {
                    for(EntityPlayerMP player : this.bossInfo.getPlayers()) {
                        if(!player.isEntityAlive()) {
                            this.bossInfo.removePlayer(player);
                            this.setBossPlayerLives(this.getBossPlayerLives() - 1);
                        }
                    }
                }

                //Creates a Target tracking to ensure if it can despawn or not
                if (target == null && this.isHadPreviousTarget() && ModConfig.boss_reset_enabled || this.getBossPlayerLives() <= 0 && ModConfig.boss_player_lives_enabled && this.getSpawnLocation() != null) {
                    int nearbyPlayers = ServerScaleUtil.getPlayersForReset(this, world);
                    if (nearbyPlayers == 0 || this.getBossPlayerLives() <= 0 && ModConfig.boss_player_lives_enabled) {
                        if (targetTrackingTimer > 0) {
                            targetTrackingTimer--;
                        }
                        if (targetTrackingTimer < 1 || this.getBossPlayerLives() <= 0 && ModConfig.boss_player_lives_enabled) {
                            if (this.timesUsed != 0) {
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

            if (target != null) {

                //grab Detection
                if (grabDetection && grabbedEntity == null) {
                    List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                            this.getEntityBoundingBox().offset(ModUtils.getRelativeOffset(this, new Vec3d(1.3, -0.5, 0))).grow(1.1D, 3.5D, 1.1D),
                            e -> !e.getIsInvulnerable());

                    if (!nearbyEntities.isEmpty()) {
                        for (EntityLivingBase base : nearbyEntities) {
                            if (!(base instanceof EntityDarkBase)) {
                                grabbedEntity = base;
                                this.playSound(SoundsHandler.KING_GRAB_SUCCESS, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                            }
                        }
                    }
                }

                if (grabbedEntity != null) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1, 0)));
                    grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                    grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);
                }
            }
        }
    }
    private static final ResourceLocation LOO_RESET = new ResourceLocation(ModReference.MOD_ID, "burning_arena_reset");

    private void resetBossTask() {
        this.setImmovable(true);
        this.setHadPreviousTarget(false);
        BlockPos pos = this.getSpawnLocation();
        EntityRohsEye pyre_reset = new EntityRohsEye(world);
        pyre_reset.setPosition(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D);
        world.spawnEntity(pyre_reset);
        world.setBlockState(pos.add(0, 3, 0), Blocks.CHEST.getDefaultState());
        TileEntity te = world.getTileEntity(pos.add(0, 3, 0));
        if(te instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) te;
            chest.setLootTable(LOO_RESET, rand.nextLong());
        }
        this.experienceValue = 0;
        this.setDropItemsWhenDead(false);
        this.setDead();
    }

    protected void turnBossIntoSummonSpawner(BlockPos pos) {
        if(ModConfig.boss_resummon_enabled) {
            if (this.timesUsed <= ModConfig.boss_resummon_max_uses && !world.isRemote) {
                world.setBlockState(pos, ModBlocks.BOSS_RESUMMON_BLOCK.getDefaultState());
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileEntityBossReSummon) {
                    TileEntityBossReSummon boss_spawner = ((TileEntityBossReSummon) te);
                    boss_spawner.setState(BlockEnumBossSummonState.INACTIVE, this.timesUsed, "roh");
                }
            }
        }
    }

    private void endDespairAttack() {
        //Remove all sigils
        List<EntityDemonRitual> nearby_sigils = this.world.getEntitiesWithinAABB(EntityDemonRitual.class, this.getEntityBoundingBox().grow(40), e -> !e.getIsInvulnerable());
        if(!nearby_sigils.isEmpty()) {
            for(EntityDemonRitual sigil : nearby_sigils) {
                sigil.setDead();
            }
        }
        //Removes corruption from players
        List<EntityPlayer> nearby_players = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(40), e -> !e.getIsInvulnerable());
        if(!nearby_players.isEmpty()) {
            for(EntityPlayer player : nearby_players) {
                PlayerRohCorruptionUtils.setRohsCorruption(player, 0);
            }
        }

    }

    private void enterMeditationMode() {
        this.setEnterMeditation(true);
        animator.performAttack(ANIM_ENTER_MEDITATION);
        this.setImmovable(true);
        this.setFullBodyUsage(true);
        addEvent(()-> this.playSound(SoundsHandler.ROH_BEGIN, 2.25f, 1.0F), 5);
        addEvent(() -> {
            this.setImmovable(false);
            if(this.getSpawnLocation() != null) {
                this.setPosition(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());
            } else {
                this.setPosition(this.posX, this.posY + 2, this.posZ);
            }
            this.setImmovable(true);
        }, 32);

        addEvent(()-> {
            this.setEnterMeditation(false);
            this.lockLook = true;
            this.setMeditationLoop(true);
        }, 35);
    }

    private void exitMeditationMode() {
        this.setMeditationLoop(false);
        this.setEndMeditation(true);
        animator.performAttack(ANIM_EXIT_MEDITATION);
        if(successful_ritual) {
            this.setDemonBuff(true);
        } else {
            this.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 600 ,1, false, false));
        }
        addEvent(()-> {
            List<EntityDemonRitual> nearby_sigils = this.world.getEntitiesWithinAABB(EntityDemonRitual.class, this.getEntityBoundingBox().grow(30), e -> !e.getIsInvulnerable());
            if(!nearby_sigils.isEmpty()) {
                for(EntityDemonRitual ritual : nearby_sigils) {
                    ritual.setDead();
                }
            }
        }, 10);

        addEvent(()-> this.lockLook = false, 15);
        addEvent(()-> {
            this.setEndMeditation(false);
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            this.didAirAttack = true;
            this.meditation_time = 250;
            this.setStateLine(this.getStatLine() - 0.25F);
        }, 25);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);

        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(2, (int) (10), (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Vec3d posToo = this.getPositionVector().add(0, 0.75, 0);
                Vec3d vel = pos.normalize().scale(Math.abs(ModRand.getFloat(0.25F) + 0.25)).add(ModUtils.yVec(0));
                Main.proxy.spawnParticle(45, world, posToo.x, posToo.y, posToo.z, vel.x, -0.06, vel.z);
            });

            ModUtils.circleCallback(2, (int) (13), (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Vec3d posToo = this.getPositionVector().add(0, 1.5, 0);
                Vec3d vel = pos.normalize().scale(Math.abs(ModRand.getFloat(0.25F) + 0.25)).add(ModUtils.yVec(0));
                Main.proxy.spawnParticle(45, world, posToo.x, posToo.y, posToo.z, vel.x, -0.06, vel.z);
            });

            ModUtils.circleCallback(2, (int) (5), (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Vec3d posToo = this.getPositionVector().add(0, 1.9, 0);
                Vec3d vel = pos.normalize().scale(Math.abs(ModRand.getFloat(0.25F) + 0.25)).add(ModUtils.yVec(0));
                Main.proxy.spawnParticle(45, world, posToo.x, posToo.y, posToo.z, vel.x, -0.06, vel.z);
            });
        }

        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            Main.proxy.spawnParticle(30, this.posX + ModRand.getFloat(1.5F), this.posY + ModRand.getFloat(1.75F) + 1.75, this.posZ + ModRand.getFloat(1.5F), 0, -0.03, 0, 16777030);
        }

        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            if(this.getSpawnLocation() != null) {
                ModUtils.performNTimes(3, (b) -> {
                    Main.proxy.spawnParticle(48, world, this.getSpawnLocation().getX() + ModRand.getFloat(16F), this.getSpawnLocation().getY() + ModRand.getFloat(2F) + 3F , this.getSpawnLocation().getZ() + ModRand.getFloat(16F), 0, 0.03, 0, 11607828);
                });
            } else {
                ModUtils.performNTimes(3, (b) -> {
                    Main.proxy.spawnParticle(48, world, this.posX + ModRand.getFloat(16F), this.posY + ModRand.getFloat(5F) + 1.4F , this.posZ + ModRand.getFloat(16F), 0, 0.03, 0, 11607828);
                });
            }
        }

    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(this.isStartingJumpStrike && world.rand.nextInt(2) == 0) {
            world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
        }

        if(this.castingDespair) {
            world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        if(!this.isFightMode() && !this.isFullBodyUsage() && !this.didAirAttack && !this.isMeditationLoop() && !this.isEndMeditation() && !this.isEnterMeditation() && !this.awaiting_meditation && cooldown_wait_time < 1) {
            double distance = Math.sqrt(distanceSq);
            double healthFac = this.getHealth() / this.getMaxHealth();
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(teleport_attack, long_swing, stomp, throw_daggers, melee_attack, great_death, cast_sigil, cast_round_sigil, summon_meteors, grab_attack, begin_jump_strike, spear_incarnate, despair_attack, jump_slam, horizontal_slash, vertical_slash));
            double[] weights = {
                    (prevAttack != teleport_attack && distance > 5) ? distance * 0.02 : 0, //Teleport Attack
                    (prevAttack != long_swing && distance < 15) ? distance * 0.02 : 0, //Long Swing
                    (prevAttack != stomp && distance <= 18 && healthFac <= 0.75) ? 1/distance : 0, //Stomp Attack % 75
                    (prevAttack != throw_daggers && distance > 7) ? distance * 0.02 : 0, //Throw Daggers
                    (prevAttack != melee_attack && distance < 7) ? 1/distance : 0, //Melee Attack
                    (prevAttack != great_death && distance <=18) ? distance * 0.02 : 0, //Great Death
                    (prevAttack != cast_sigil) ? 1/distance : 0, //Cast Sigil
                    (prevAttack != cast_round_sigil && distance > 3 && distance <= 16 && healthFac <= 0.75) ? distance * 0.02 : 0, //Cast Round Sigil % 75
                    (prevAttack != summon_meteors && healthFac <= 0.75) ? distance * 0.02 : 0, //Summon Meteors % 75
                    (prevAttack != grab_attack && distance > 6 && healthFac <= 0.5) ? 1/distance : 0, //Grab Attack % 50
                    (prevAttack != begin_jump_strike && healthFac <= 0.75) ? distance * 0.02 : 0, //Jumpstrike
                    (prevAttack != spear_incarnate && healthFac <= 0.75) ? distance * 0.02 + 0.01 : 0, //Spear Incarnate
                    (prevAttack != despair_attack && despair_cooldown < 0 && healthFac <= 0.5) ? distance * 0.03 : 0, //Despair Attack
                    (prevAttack != jump_slam && distance <= 13 && healthFac <= 0.75) ? distance * 0.02 : 0, //Jump Slam, can repeat 3 times
                    (prevAttack != horizontal_slash && distance < 12 && healthFac <= 0.5) ? distance * 0.02 : 0, // Horizontal Slash
                    (prevAttack != vertical_slash && distance < 20 && healthFac <= 0.5) ? distance * 0.02 : 0 // Vertical Slash
            };
            prevAttack = ModRand.choice(attacksMelee, rand, weights).next();
            jump_amount = 0;
            prevAttack.accept(target);
        }
        return MobConfig.roh_attack_cooldown;
    }

    private final Consumer<EntityLivingBase> vertical_slash = (target) -> {
      this.setImmovable(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      animator.performAttack(ANIM_VERTICAL_SLASH);
        addEvent(()-> this.playSound(SoundsHandler.ROH_CHARGE_SWORD, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_SWORD, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 7);
        addEvent(()-> this.playSound(SoundsHandler.ROH_SWING_SWORD_HEAVY, 2.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 27);
      addEvent(()-> this.lockLook = true, 22);

      addEvent(()-> {
          //Do Vertical Slash
            new ActionVerticalSlash((float) ((this.getAttack() * 1.3F) + demon_buff_damage), 26, 2F).performAction(this, target);
      }, 31);

      addEvent(()-> this.lockLook =false, 45);

      addEvent(()-> {
          this.setImmovable(false);
          this.setFullBodyUsage(false);
          this.setFightMode(false);
      }, 55);

    };

    private final Consumer<EntityLivingBase> horizontal_slash = (target) -> {
        this.setImmovable(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        animator.performAttack(ANIM_HORIZONTAL_SLASH);
        addEvent(()-> this.playSound(SoundsHandler.ROH_CHARGE_SWORD, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_SWORD, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.ROH_SWING_SWORD_HEAVY, 2.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 34);
        addEvent(()-> this.lockLook = true, 25);

        addEvent(()-> {
        //Do Ground Sweep
        new ActionHorizontalSlash((float) ((this.getAttack()) + demon_buff_damage), (int) this.getDistance(target) + 7).performAction(this, target);
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.25, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = ((this.getAttack() * 1.25F) + demon_buff_damage);
            ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 0.7f, 0, false, ModPotions.HEMORRHAGE, 2, 200, 1.1F);
        }, 37);

        addEvent(()-> {
            this.lockLook = false;
        }, 55);

        addEvent(()-> {
            this.setImmovable(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
        }, 65);
    };

    private final Consumer<EntityLivingBase> jump_slam = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setJumpSlam(true);
      this.setImmovable(true);
      jump_amount++;
          addEvent(()-> {
              this.setImmovable(false);
              this.motionY = 0;
              this.lockLook = true;
              double distance = this.getPositionVector().distanceTo(target.getPositionVector());
              ModUtils.leapTowardsAdjusted(this, target.getPositionVector(), (float) (distance * 0.21),0F);
              this.playSound(SoundsHandler.ROH_JUMP_SLAM, 1.5f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
              world.setEntityState(this, ModUtils.PARTICLE_BYTE);
              this.motionY += 0.55F;
          }, 15);

      addEvent(()-> this.setImmovable(true), 30);
      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0.25, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (this.getAttack() * 1.25F + demon_buff_damage);
          ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.9f, 0, false, MobEffects.SLOWNESS, 0, 200, 0.9F);
          new ActionDauntlessImpact(2).performAction(this, target);
          new ActionJumpSlamAOE().performAction(this, target);
      }, 34);

      addEvent(()-> this.lockLook = false, 45);

      addEvent(()-> {
          this.setJumpSlam(false);
          this.setImmovable(false);
          //Do another jump slam if the target is far and on a random
          if(this.getDistance(target) > 3 && ModRand.percentageOf(80 - (jump_amount * 10))) {
              this.continueJumpSlam(target);
          } else {
              this.setFightMode(false);
              this.setFullBodyUsage(false);
          }
      }, 55);
    };

    private void continueJumpSlam(EntityLivingBase target) {
        jump_slam.accept(target);
    }

    private final Consumer<EntityLivingBase> begin_jump_strike = (target) -> {
      animator.performAttack(ANIM_BEGIN_JUMPSTRIKE);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.HoverTimeIncrease = 2;
      this.isStartingJumpStrike = true;
      this.setNoGravity(true);
        addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_WEAPON, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 10);
      addEvent(()-> {
            this.motionY = 0;
            this.setNoGravity(false);
            this.setImmovable(true);
            this.setLoopJumpStrike(true);
            int randTimeInAir = ModRand.range(20, 55);

            //Ends the loop after the randTime
            addEvent(()-> {
                this.setLoopJumpStrike(false);
                this.doJumpStrikeAttack(target);
                this.isStartingJumpStrike = false;
            }, randTimeInAir);
      }, 20);
    };

    private void doJumpStrikeAttack(EntityLivingBase target) {
        end_jumpstrike.accept(target);
    }

    private final Consumer<EntityLivingBase> end_jumpstrike = (target) -> {
      animator.performAttack(ANIM_END_JUMPSTRIKE);

      addEvent(()-> {
          Vec3d targetedPos = target.getPositionVector();
          addEvent(()-> {
              //teleport boss to the ground on player
              this.setImmovable(false);
              int y_value = ModUtils.getSurfaceHeightGeneral(world, new BlockPos(targetedPos.x, 0, targetedPos.z), (int) targetedPos.y - 20, (int) targetedPos.y + 2);
              if(y_value != this.posY) {
                  this.setPosition(targetedPos.x, y_value + 1, targetedPos.z);
              } else {
                  //destroy blocks just incase
                  this.setPosition(targetedPos.x, target.posY, targetedPos.z);
              }
              this.playSound(SoundsHandler.VOIDCLYSM_IMPACT, 1.0f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
              ModUtils.destroyBlocksInSwing(this, new Vec3d(this.posX, this.posY, this.posZ), 1.0F);
              this.setImmovable(true);
              this.motionX = 0;
              this.motionZ = 0;
              this.lockLook = true;
          }, 4);
      }, 0);

      addEvent(()-> new ActionDauntlessImpact(4).performAction(this, target), 8);
      addEvent(()-> new ActionHolySpikeAOE(13).performAction(this, target), 16);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_SPEAR_IMPACT, 1.5f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 18);

      addEvent(()-> {
          this.lockLook = false;
      }, 25);

      addEvent(()-> {
          this.setImmovable(false);
          this.setFightMode(false);
          this.setFullBodyUsage(false);
          cooldown_wait_time = 13;
      }, 35);
    };

    private final Consumer<EntityLivingBase> despair_attack = (target) -> {
        this.setCastDespair(true);
        animator.performAttack(ANIM_DESPAIR);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.playSound(SoundsHandler.BLOOD_FLY_BEGIN, 1.0f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
        addEvent(()-> {
            this.setImmovable(false);
            this.setNoGravity(true);
            this.HoverTimeIncrease = 3;
        }, 10);

        addEvent(()-> this.playSound(SoundsHandler.ROH_DESPAIR, 2f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 25);

        addEvent(()-> {
            this.motionY = 0;
            this.setImmovable(true);
            this.setNoGravity(false);
            this.castingDespair = true;
        }, 35);

        addEvent(() -> {
            //Start casting
            this.lockLook = true;
        }, 50);

        addEvent(()-> {
            //end casting & remove tokens
            this.castingDespair = false;
            this.lockLook = false;
            this.endDespairAttack();
            this.playSound(SoundsHandler.ROH_END_DESPAIR, 1.0F, 1.0F);
        }, 340);

        addEvent(()-> {
            this.setImmovable(false);
            this.didAirAttack = true;
        }, 365);

        addEvent(()-> {
            this.setCastDespair(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.despair_cooldown = 30 * 20;
            this.cooldown_wait_time = 20;
        }, 370);
    };

    private final Consumer<EntityLivingBase> spear_incarnate = (target) -> {
      animator.performAttack(ANIM_SPEAR_INCARNATE);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        this.playSound(SoundsHandler.ROH_CHARGE_SWORD, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_WEAPON, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 3);
      addEvent(()-> this.lockLook = true, 20);
      addEvent(() -> {
          this.playSound(SoundsHandler.HIGH_KING_SWING_MAGIC, 1.75f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 16);
      addEvent(()-> {
          //do spear incarnate Attack
            new ActionSpearIncarnate().performAction(this, target);
      }, 22);
        addEvent(()-> this.lockLook = false, 50);
      addEvent(()-> {
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
      }, 45);
    };

      private final Consumer<EntityLivingBase> grab_attack = (target) -> {
       animator.performAttack(ANIM_GRAB);
       this.setFightMode(true);
       this.setFullBodyUsage(true);
       this.setImmovable(true);


          addEvent(()-> {
              Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
              Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-5));
              addEvent(()-> {
                  this.setImmovable(false);
                  ModUtils.attemptTeleport(targetedPos, this);
                  this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.75f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
              }, 3);
          }, 33);

          addEvent(()-> {
              this.setImmovable(true);
              Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
              Vec3d targetedPos = target.getPositionVector().add(posSet.scale(3));
              this.lockLook = true;
              addEvent(()-> {
                  this.setImmovable(false);
                  this.grabDetection = true;
                  double distance = this.getPositionVector().distanceTo(targetedPos);
                  this.playSound(SoundsHandler.BLOOD_GRAB_ATTACK, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                  ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.35),0.1F);
              }, 8);
          }, 33);

       addEvent(()-> {
            this.grabDetection = false;
            this.setImmovable(true);

            if(grabbedEntity != null) {
                this.setGrabTooContinue(target);
            } else {
                animator.performAttack(ANIM_GRAB_FINISH);

                addEvent(()-> {
                    this.lockLook = false;
                }, 15);

                addEvent(()-> {
                    this.setFightMode(false);
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                }, 30);
            }
       }, 55);
      };

      private void setGrabTooContinue(EntityLivingBase target) {
          grab_continue.accept(target);
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (1F);
          ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0f, 0, false);
      }


      private final Consumer<EntityLivingBase> grab_continue = (target) -> {
        this.setGrabContinue(true);
        animator.performAttack(ANIM_GRAB_CONTINUE);
          addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_WEAPON, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 5);
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (this.getAttack() * 1.5F + demon_buff_damage);
            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0f, 0, false, ModPotions.HEMORRHAGE, 2, 200, 1.1F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
            this.heal((float) (0.05 * this.getMaxHealth()));
        }, 25);

        addEvent(()-> {
            this.grabbedEntity = null;
            this.lockLook = false;
        }, 40);

        addEvent(()-> {
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setGrabContinue(false);
            this.setImmovable(false);
        }, 65);
      };


    private final Consumer<EntityLivingBase> summon_meteors = (target) -> {
      animator.performAttack(ANIM_SUMMON_METEORS);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);

      addEvent(()-> {
          this.setImmovable(false);
          this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 2F, 1.0F);
          if(this.getSpawnLocation() != null) {
              this.setPosition(this.getSpawnLocation().getX(), this.getSpawnLocation().getY() + 9, this.getSpawnLocation().getZ());
          } else {
                this.setPosition(this.posX, this.posY + 9, this.posZ);
          }
          this.setImmovable(true);
      }, 17);

      addEvent(()-> new ActionSummonMeteors().performAction(this, target), 30);
      addEvent(()-> this.playSound(SoundsHandler.ROH_CAST_GENERIC, 1.8f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 30);

        addEvent(()-> {
          this.setImmovable(false);
          this.didAirAttack = true;
          this.lockLook = true;
      }, 120);

      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setFightMode(false);
      }, 125);
    };

    private final Consumer<EntityLivingBase> cast_round_sigil = (target) -> {
      animator.performAttack(ANIM_CAST_SIGIL_ROUND);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.ROH_CAST_MULTI, 1.4f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 1);
        addEvent(()-> this.lockLook = true, 13);

        addEvent(()-> {
            if(this.getHealth()/this.getMaxHealth() < 0.5 && rand.nextInt(2) == 0) {
                addEvent(()-> {
                    EntityDemonSigil sigil = new EntityDemonSigil(world, this.rotationYaw, true, true, this.getAttack() * 0.75F);
                    Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0.75, -1.5)));
                    sigil.setPosition(relPos.x, relPos.y, relPos.z);
                    world.spawnEntity(sigil);
                }, 1);
                EntityDemonSigil sigil = new EntityDemonSigil(world, this.rotationYaw, true, false, this.getAttack() * 0.75F);
                Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0.75, 1.5)));
                sigil.setPosition(relPos.x, relPos.y, relPos.z);
                world.spawnEntity(sigil);
            } else {
                EntityDemonSigil sigil = new EntityDemonSigil(world, this.rotationYaw, true, false, this.getAttack() * 0.75F);
                Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                sigil.setPosition(relPos.x, relPos.y, relPos.z);
                world.spawnEntity(sigil);
            }
        }, 17);

        addEvent(()-> this.lockLook = false, 25);

      addEvent(()-> {
          this.setFullBodyUsage(false);
          this.setFightMode(false);
          this.setImmovable(false);
          this.cooldown_wait_time = 20;
      },35);
    };

    private final Consumer<EntityLivingBase> cast_sigil = (target) -> {
     animator.performAttack(ANIM_CAST_SIGIL);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.ROH_CAST_GENERIC, 1.4f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 1);
        addEvent(()-> this.lockLook = true, 14);

      addEvent(()-> {
        EntityDemonSigil sigil = new EntityDemonSigil(world, this.rotationYaw, false, false, this.getAttack() * 0.75F);
        Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
        sigil.setPosition(relPos.x, relPos.y, relPos.z);
        world.spawnEntity(sigil);
        if(this.getHealth() / this.getMaxHealth() <= 0.75) {
            addEvent(() -> {
                EntityDemonSigil sigil2 = new EntityDemonSigil(world, this.rotationYaw, false, false, this.getAttack() * 0.75F);
                EntityDemonSigil sigil3 = new EntityDemonSigil(world, this.rotationYaw, false, false, this.getAttack() * 0.75F);
                Vec3d relPos2 = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 5)));
                Vec3d relPos3 = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, -5)));
                sigil2.setPosition(relPos2.x, relPos2.y, relPos2.z);
                sigil3.setPosition(relPos3.x, relPos3.y, relPos3.z);
                world.spawnEntity(sigil2);
                world.spawnEntity(sigil3);
            }, 5);
        }
      }, 12);

      addEvent(()-> this.lockLook = false, 20);
      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 25);
    };

    private final Consumer<EntityLivingBase> great_death = (target) -> {
      animator.performAttack(ANIM_GREAT_DEATH);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
      this.lockLook = true;
      AtomicBoolean didSecondCircle = new AtomicBoolean(false);
      addEvent(()-> this.playSound(SoundsHandler.ROH_CHARGE_SWORD, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_SWORD, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 13);
        addEvent(()-> this.playSound(SoundsHandler.ROH_SWING_SWORD_HEAVY, 2.25f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 45);
        addEvent(()-> {
          double currentHealth = this.getHealth() / this.getMaxHealth();
          if(currentHealth <= 0.5 && rand.nextInt(3) == 0) {
              new ActionGreatDeathLine().performAction(this, target);
          } else {
              new ActionGreatDeath().performAction(this, target);
              if(currentHealth <= 0.75) {
                  addEvent(()-> {
                      new ActionGreatDeathTwo().performAction(this, target);
                      didSecondCircle.set(true);
                  }, 40);
              }
          }
      }, 57);

      addEvent(()-> {
          this.lockLook = false;
      }, 80);
      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
        this.cooldown_wait_time = didSecondCircle.get() ? 30 : 20;
      }, 90);
    };

    private final Consumer<EntityLivingBase> melee_attack = (target) -> {
        animator.performAttack(ANIM_MELEE_SWING);
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_SWORD, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 5);
        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 4);
        }, 12);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack() + demon_buff_damage);
            RohUtils.handleAreaImpactDarkdrift(1.5f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.6F, 3, 600);
            this.playSound(SoundsHandler.ROH_SWING_SWORD, 1.2f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 22);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 33);

        addEvent(()-> {

            if(this.getDistance(target) <= 6 && rand.nextInt(5) != 0) {
                this.setMeleeSwingTooTwo(target);
            } else {
                animator.performAttack(ANIM_MELEE_SWING_FINISH);

                addEvent(()-> {
                    this.setFullBodyUsage(false);
                    this.setFightMode(false);
                    this.setImmovable(false);
                }, 15);
            }
        }, 35);
    };

    private void setMeleeSwingTooTwo(EntityLivingBase target) {
        melee_swing_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> melee_swing_continue = (target) -> {
      animator.performAttack(ANIM_MELEE_SWING_CONTINUE);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.27),0.1F);
            }, 4);
        }, 9);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack() + demon_buff_damage);
            RohUtils.handleAreaImpactDarkdrift(1.5f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.6F, 3, 600);
            this.playSound(SoundsHandler.ROH_SWING_SWORD, 1.2f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 17);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 27);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.27),0.1F);
            }, 4);
        }, 37);

        addEvent(()-> this.playSound(SoundsHandler.ROH_SWING_SWORD, 1.2f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 44);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (this.getAttack() + demon_buff_damage);
            RohUtils.handleAreaImpactDarkdrift(1.5f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.9F, 3, 600);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 46);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
            boolean randB = rand.nextBoolean();

            if(randB && this.getDistance(target) <= 12) {
                this.setSwingTooDash(target);
            } else {
                animator.performAttack(ANIM_MELEE_SWING_FINISH_TWO);

                addEvent(()-> {
                    this.setFullBodyUsage(false);
                    this.setFightMode(false);
                    this.setImmovable(false);
                }, 15);
            }
        }, 55);
    };

    private void setSwingTooDash(EntityLivingBase target) {
        melee_swing_dash.accept(target);
    }

    private final Consumer<EntityLivingBase> melee_swing_dash = (target) -> {
      animator.performAttack(ANIM_MELEE_SWING_DASH);
        addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_WEAPON, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 18);
        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(2));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.35),0.1F);
            }, 4);
        }, 27);

        addEvent(()-> {
            this.playSound(SoundsHandler.HIGH_KING_SWING, 1.2f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 30);

        addEvent(()-> {
            for(int i = 0; i <= 20; i+= 3) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.0, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = (this.getAttack() + demon_buff_damage);
                    ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.8f, 0, false);
                    // this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                }, i);
            }
        }, 31);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 60);

      addEvent(()-> {
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setImmovable(false);
      }, 70);
    };


    private final Consumer<EntityLivingBase> throw_daggers = (target) -> {
      animator.performAttack(ANIM_THROW_DAGGERS);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);

      addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_WEAPON, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_WEAPON, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 37);
        addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_WEAPON, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 64);

      //daggers
      addEvent(()-> {
          new ActionThrowDaggers(false, false).performAction(this, target);
          this.playSound(SoundsHandler.ROH_THROW_DAGGERS, 1.2f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 27);
        //daggers
        addEvent(()-> {
            new ActionThrowDaggers(false, true).performAction(this, target);
            this.playSound(SoundsHandler.ROH_THROW_DAGGERS, 1.2f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 50);
        //daggers
        addEvent(()-> {
            new ActionThrowDaggers(false, false).performAction(this, target);
            this.playSound(SoundsHandler.ROH_THROW_DAGGERS, 1.2f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 78);

        addEvent(()-> {

            if(rand.nextInt(3) != 0 && this.getDistance(target) <= 14) {
                this.setDaggersTooDash(target);
            } else {
                animator.performAttack(ANIM_THROW_DAGGERS_CONTINUE);

                addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_WEAPON, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 3);
                addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_WEAPON, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 25);

                //daggers
                addEvent(()-> {
                    new ActionThrowDaggers(false, true).performAction(this, target);
                    this.playSound(SoundsHandler.ROH_THROW_DAGGERS, 1.2f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
                }, 14);
                //daggers
                addEvent(()-> {
                    new ActionThrowDaggers(true, false).performAction(this, target);
                    this.playSound(SoundsHandler.ROH_THROW_DAGGERS, 1.2f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
                }, 40);

                addEvent(()-> {
                    this.setImmovable(false);
                    this.setFightMode(false);
                    this.setFullBodyUsage(false);
                }, 70);
            }
        }, 90);
    };

    private void setDaggersTooDash(EntityLivingBase target){
        throw_daggers_dash.accept(target);
    };

    private final Consumer<EntityLivingBase> throw_daggers_dash = (target) -> {
      animator.performAttack(ANIM_THROW_DAGGERS_DASH);

      addEvent(()-> {
          this.lockLook = true;
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.3),0.1F);
          }, 4);
      }, 27);

      addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_SWING, 1.2f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 30);
       addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_SWING_MAGIC, 1.3f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 60);
      //do hits while dashing
        addEvent(()-> {
            for(int i = 0; i <= 18; i+= 3) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.0, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = (this.getAttack() + demon_buff_damage);
                    ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.8f, 0, false);
                    // this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                }, i);
            }
        }, 32);

      addEvent(()-> {
          this.setImmovable(true);
      }, 50);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (this.getAttack());
          ModUtils.handleAreaImpact(2.25f, (e) -> damage, this, offset, source, 0.8f, 0, false, 0.9F);
           this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 63);

      addEvent(()-> {
          this.lockLook = false;
          ModUtils.circleCallback(4, 5, (pos)-> {
              pos = new Vec3d(pos.x, 0, pos.y).add(this.getPositionVector());
              int y = ModUtils.getSurfaceHeightGeneral(world, new BlockPos(pos.x, 0, pos.z), (int) this.posY - 5, (int) this.posY + 3);
              EntityDelayedExplosion spike = new EntityDelayedExplosion(world, this, this.getAttack() * 0.8F, true, false);
              spike.setPosition(pos.x, y + 2, pos.z);
              world.spawnEntity(spike);
          });
      }, 68);

      addEvent(()-> {
          this.setImmovable(false);
          this.setFightMode(false);
          this.setFullBodyUsage(false);
      }, 90);
    };

    private final Consumer<EntityLivingBase> stomp = (target) -> {
      animator.performAttack(ANIM_STOMP);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.lockLook = true;

      addEvent(()-> {
          //stomp attack
          new ActionDemonStomp().performAction(this, target);
          this.playSound(SoundsHandler.B_KNIGHT_STOMP, 1.5f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 25);

      addEvent(()-> {
          this.lockLook = false;
      }, 45);

      addEvent(()-> {

        if(this.getDistance(target) <= 5) {
            this.setStompTooContinue(target);
        } else {
            animator.performAttack(ANIM_STOMP_FINISH);

            addEvent(()-> {
                this.setFullBodyUsage(false);
                this.setFightMode(false);
                this.setImmovable(false);
            }, 10);
        }
      }, 50);
    };

    private void setStompTooContinue(EntityLivingBase target) {
        stomp_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> stomp_continue = (target) -> {
      animator.performAttack(ANIM_STOMP_CONTINUE);
      this.lockLook = true;

        addEvent(()-> {
            for(int i = 0; i <= 15; i+= 3) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.0, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = (this.getAttack() + demon_buff_damage);
                    ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 0.8f, 0, false, MobEffects.WITHER, 0, 100);
                    // this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                }, i);
            }
        }, 20);

        addEvent(()-> {
            this.lockLook = false;
        }, 60);

      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      },    70);
    };

    private final Consumer<EntityLivingBase> long_swing = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      animator.performAttack(ANIM_LONG_SWING);

      addEvent(()-> {
          this.lockLook = true;
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(4));
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.33),0.1F);
          }, 5);
      }, 20);

      addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_SWING_MAGIC, 1.3f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 30);
      //do hits
      addEvent(()-> {
        for(int i = 0; i <= 23; i+= 3) {
            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.0, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = (this.getAttack() + demon_buff_damage);
                ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.4f, 0, false, 0.4F);
               // this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
            }, i);
        }
      }, 25);

      addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
      }, 45);

      addEvent(()-> {
        boolean randB = rand.nextBoolean();
        if(randB) {
            //continue the attack
            this.setLongSwingTooContinue(target);
        } else {
            animator.performAttack(ANIM_LONG_SWING_FINISH);

            addEvent(()-> {
                this.setFullBodyUsage(false);
                this.setFightMode(false);
                this.setImmovable(false);
            }, 15);
        }
      }, 50);
    };

    private void setLongSwingTooContinue(EntityLivingBase target) {
        long_swing_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> long_swing_continue = (target) -> {
      animator.performAttack(ANIM_LONG_SWING_CONTINUE);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(4));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.33),0.1F);
            }, 4);
        }, 14);

        addEvent(()-> {
            this.playSound(SoundsHandler.HIGH_KING_SWING_MAGIC, 1.3f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 24);
        //do hits
        addEvent(()-> {
            for(int i = 0; i <= 23; i+= 3) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.0, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                    float damage = (this.getAttack() + demon_buff_damage);
                    ModUtils.handleAreaImpact(1.7f, (e) -> damage, this, offset, source, 0.4f, 0, false, 0.4F);
                    // this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                }, i);
            }
        }, 18);

        addEvent(()-> this.setImmovable(true), 35);

        addEvent(()-> this.lockLook = false, 50);

      addEvent(()-> {
          this.setFightMode(false);
          this.setFullBodyUsage(false);
          this.setImmovable(false);
      }, 60);
    };
    //Teleport Attack
    private final Consumer<EntityLivingBase> teleport_attack = (target) -> {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        animator.performAttack(ANIM_TELEPORT_ATTACK);
        this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.ROH_SUMMON_SWORD, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 44);
        addEvent(()-> {
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.25F, 1.0F);
            this.setImmovable(false);
            if(this.getSpawnLocation() != null) {
                this.setPosition(this.getSpawnLocation().getX(), this.getSpawnLocation().getY() + 6, this.getSpawnLocation().getZ());
            } else {
                this.setPosition(this.posX, this.posY + 6, this.posZ);
            }
            this.setImmovable(true);
        }, 18);


        addEvent(()-> {
            int y = getSurfaceHeight(world, target.getPosition(), (int) target.posY - 15, (int) target.posY + 5);
            this.setImmovable(false);
            this.setPosition(target.posX + 1, target.posY + 2, target.posZ);
            this.lockLook = true;
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.5F, 1.0F);
            this.playSound(SoundsHandler.ROH_CHARGE_SWORD, 1.2f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 40);

        addEvent(()-> {
            this.playSound(SoundsHandler.ROH_SWORD_ABILITY, 1.4f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 50);

        //do attack
        addEvent(()-> {
            this.setImmovable(true);
            new ActionTeleportAttack().performAction(this, target);
        }, 55);

        addEvent(()-> this.lockLook = false, 70);


        addEvent(()-> {
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);

        }, 80);

    };

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

        return pos.getY();
    }

    @Override
    public void updateAnimations() {
        if(!this.isCastDespair()) {
            animator.setSpellIdle(ANIM_IDLE_EYES);
        }

        if(this.isMeditationLoop()) {
            animator.setCustomUpperIdle(ANIM_MEDITATION_LOOP);
        }

        if(this.isLoopJumpStrike()) {
            animator.performAttack(ANIM_LOOP_JUMPSTRIKE);
        }

        if(this.isJumpSlam()) {
            animator.performAttack(ANIM_JUMP_SLAM);
        }

        if(!this.isFullBodyUsage() && !this.isMeditationLoop()) {
            boolean isMovingOnGround = moveAnalysis.isMovingHorizontally() && this.onGround;
            if(isMovingOnGround) {
                if(!this.isFightMode()) {
                    animator.setCustomUpperWalk(ANIM_WALK_UPPER);
                }
                animator.setCustomLowerWalk(ANIM_WALK_LOWER);
            } else {
                if(!this.isFightMode()) {
                    animator.setCustomUpperIdle(ANIM_IDLE_UPPER);
                }
                animator.setCustomLowerIdle(ANIM_IDLE_LOWER);
            }
        }
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
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        if(rand.nextInt(10) == 0) {
            playInsultSound();
        }
        return SoundsHandler.ROH_HURT;
    }

    private void playInsultSound() {
        this.playSound(SoundsHandler.ROH_INSULT, 1.25F, 1.0F);
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.ROH_STEP, 0.5F, 0.7f + ModRand.getFloat(0.3F));
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    @Nullable
    @Override
    public SoundEvent getBossMusic() {
        return SoundsHandler.DARKDRIFT_DEVIL_TRACK;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if(source.getImmediateSource() instanceof ProjectileBloodMeteor || this.isMeditationLoop() || this.isGrabContinue()) {
            return false;
        }

        if(this.isDemonBuff()) {
            return super.attackEntityFrom(source, amount * 0.55F);
        } else if(ModConfig.boss_cap_damage_enabled && amount > MobConfig.high_king_damage_cap) {
            return super.attackEntityFrom(source, MobConfig.high_king_damage_cap);
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.00001F);
        if(!this.isDeathState()) {
            this.clearEvents();
        }
        this.setDeathState(true);
        animator.performAttack(ANIM_DEATH);
        this.setFullBodyUsage(true);
        if(this.getSpawnLocation() != null) {
            this.setPosition(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());
        }
        this.playSound(SoundsHandler.IMPERIAL_SWORD_PARRY, 1.25F, 1.0F);
        this.setImmovable(true);
        this.setFightMode(true);

        addEvent(()-> {
            this.playSound(SoundsHandler.ROH_DEATH_VL, 1.5F, 1.0F);
        }, 15);

        addEvent(() -> {
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1F, 1.0F);
        }, 70);
        addEvent(()-> {
            if(this.getSpawnLocation() != null && !world.isRemote) {
                this.turnBossIntoSummonSpawner(this.getSpawnLocation());
                this.createCoinSpawns(this.getPositionVector(), ModRand.range(8, 12), ModRand.range(1, 4), 2);
                this.createTreasureBag(this.getPositionVector(), "roh");
            }
            this.setDead();
            this.setDropItemsWhenDead(true);
            this.setDeathState(false);
        }, 79);
        super.onDeath(cause);
    }
}
