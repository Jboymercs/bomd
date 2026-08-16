package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.blocks.boss.BlockEnumBossSummonState;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.EntityDraugrMeleeAI;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityDauntlessAttackAI;
import com.dungeon_additions.da.entity.ai.flying.FlyingMoveHelper;
import com.dungeon_additions.da.entity.ai.flying.TimedAttackInitiator;
import com.dungeon_additions.da.entity.ai.void_dungeon.EntityVoidclysmAttackAI;
import com.dungeon_additions.da.entity.blossom.EntityAbstractVoidBlossom;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityDarkdriftDevil;
import com.dungeon_additions.da.entity.dark_dungeon.dark_void.EntityDarkVoid;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.*;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicMissile;
import com.dungeon_additions.da.entity.tileEntity.TileEntityBossReSummon;
import com.dungeon_additions.da.entity.util.IEntitySound;
import com.dungeon_additions.da.entity.void_dungeon.EntityEndBase;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiclysm;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.util.*;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
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
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import org.lwjgl.Sys;
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

public class EntityDauntless extends EntityDarkBase implements IAnimatable, IAnimationTickable, IPitch, IAttack, IEntitySound {

    private final String ANIM_SWING_ONE = "swing";
    private final String ANIM_SWING_TWO = "swing_two";
    private final String ANIM_SWING_THREE = "swing_three";
    private final String ANIM_GROUND_STRIKE = "ground_strike";
    private final String ANIM_SWORD_SLASH = "sword_slash";
    private final String ANIM_TELEPORT = "teleport";
    private final String ANIM_SUMMON_PROJECTILES = "summon_projectiles";
    private final String ANIM_SELF_AOE = "self_aoe";
    private final String ANIM_USE_SWORD = "use_sword";
    private final String ANIM_DASH_SWEEP = "dash_sweep";
    private final String ANIM_THROW_SWORD = "throw_sword";
    private final String ANIM_PIERCE_START = "pierce_start";
    private final String ANIM_PIERCE_FAIL = "pierce_fail";
    private final String ANIM_PIERCE_FINISH = "pierce_finish";
    private final String ANIM_CIRCLE_SWING = "circle_swing";
    private final String ANIM_RAGE_LOOP = "rage_loop";
    private final String ANIM_CICLE_SWING_CONTINUE = "circle_swing_continue";

    private final String ANIM_ENTER_RAGE_MODE = "enter_enragement";
    private final String ANIM_END_RAGE_MODE = "end_enragement";
    private final String ANIM_BITE = "bite";
    private final String ANIM_SMASH = "smash";
    private final String ANIM_PUNCH = "punch";
    private final String ANIM_CHARGE_FIST = "charge_fist";
    private final String ANIM_FIST_BARRAGE = "fist_barrage";
    private int shakeTime = 0;
    public int wantedDistance = 18;
    protected int teleportCooldownTimer = 10 * 20;
    public boolean doesBossSlowDown = false;
    public boolean standbyOnVel = false;
    private int general_use_sword_cooldown = 30 * 20;
    public int sword_charge_one = 0;
    public int sword_charge_two = 0;
    public int sword_charge_3 = 0;
    private EntityLivingBase grabbedEntity;
    public boolean clearCurrentVelocity = false;
    private Consumer<EntityLivingBase> prevAttack;
    private boolean setBossToFlyHigh = false;
    private boolean grabDetection = false;
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.NOTCHED_12));
    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.FLOAT);
    protected static final DataParameter<Integer> SWORD_CHARGE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_ONE= EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_TWO = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_THREE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GROUND_STRIKE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWORD_SLASH = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_PROJECTILES = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SELF_AOE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> USE_SWORD = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DASH_SWEEP = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_START = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_FAIL = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_FINISH = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RANGED_MODE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TELEPORT_ATTACK = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> THROW_SWORD = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CIRCLE_SWING = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CIRCLE_SWING_CONTINUE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ENRAGEMENT_MODE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ENTER_RAGE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_RAGE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PUNCH = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BITE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SMASH = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RAGE_LOOP = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CHARGE_FIST = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FIST_BARRAGE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPAWNED_NATURALLY = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BLOCK_POS);
    public static DataParameter<Boolean> SET_SPAWN_LOC = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAD_PREVIOUS_TARGET = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);

    public void setSwordCharge(int skinType)
    {
        this.dataManager.set(SWORD_CHARGE, Integer.valueOf(skinType));
    }

    public int getSwordCharge()
    {
        return this.dataManager.get(SWORD_CHARGE).intValue();
    }
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    public void setSwingOne(boolean value) {this.dataManager.set(SWING_ONE, Boolean.valueOf(value));}
    public boolean isEnterRage() {return this.dataManager.get(ENTER_RAGE);}
    public void setEnterRage(boolean value) {this.dataManager.set(ENTER_RAGE, Boolean.valueOf(value));}
    public boolean isBiteAttack() {return this.dataManager.get(BITE);}
    public void setBiteAttack(boolean value) {this.dataManager.set(BITE, Boolean.valueOf(value));}
    public boolean isSmashAttack() {return this.dataManager.get(SMASH);}
    public void setSmashAttack(boolean value) {this.dataManager.set(SMASH, Boolean.valueOf(value));}
    public boolean isChargeFist() {return this.dataManager.get(CHARGE_FIST);}
    public void setChargeFist(boolean value) {this.dataManager.set(CHARGE_FIST, Boolean.valueOf(value));}
    public boolean isFistBarrage() {return this.dataManager.get(FIST_BARRAGE);}
    public void setFistBarrage(boolean value) {this.dataManager.set(FIST_BARRAGE, Boolean.valueOf(value));}
    public boolean isEndRage() {return this.dataManager.get(END_RAGE);}
    public void setEndRage(boolean value) {this.dataManager.set(END_RAGE, Boolean.valueOf(value));}
    public boolean isPunch() {return this.dataManager.get(PUNCH);}
    public void setPunch(boolean value) {this.dataManager.set(PUNCH, Boolean.valueOf(value));}
    public boolean isRageLoop() {return this.dataManager.get(RAGE_LOOP);}
    public void setRageLoop(boolean value) {this.dataManager.set(RAGE_LOOP, Boolean.valueOf(value));}
    public boolean isCircleSwing() {return this.dataManager.get(CIRCLE_SWING);}
    public void setCircleSwing(boolean value) {this.dataManager.set(CIRCLE_SWING, Boolean.valueOf(value));}
    public boolean isCircleSwingContinue() {return this.dataManager.get(CIRCLE_SWING_CONTINUE);}
    public void setCircleSwingContinue(boolean value) {this.dataManager.set(CIRCLE_SWING_CONTINUE, Boolean.valueOf(value));}
    public boolean isEnragementMode() {return this.dataManager.get(ENRAGEMENT_MODE);}
    public void setEnragementMode(boolean value) {this.dataManager.set(ENRAGEMENT_MODE, Boolean.valueOf(value));}
    public boolean isSwingOne() {return this.dataManager.get(SWING_ONE);}
    public void setSwingTwo(boolean value) {this.dataManager.set(SWING_TWO, Boolean.valueOf(value));}
    public boolean isSwingTwo() {return this.dataManager.get(SWING_TWO);}
    public void setSwingThree(boolean value) {this.dataManager.set(SWING_THREE, Boolean.valueOf(value));}
    public boolean isSwingThree() {return this.dataManager.get(SWING_THREE);}
    public void setGroundStrike(boolean value) {this.dataManager.set(GROUND_STRIKE, Boolean.valueOf(value));}
    public boolean isGroundStrike() {return this.dataManager.get(GROUND_STRIKE);}
    public void setSwordSlash(boolean value) {this.dataManager.set(SWORD_SLASH, Boolean.valueOf(value));}
    public boolean isSwordSlash() {return this.dataManager.get(SWORD_SLASH);}
    public void setSummonProjectiles(boolean value) {this.dataManager.set(SUMMON_PROJECTILES, Boolean.valueOf(value));}
    public boolean isSummonProjectiles() {return this.dataManager.get(SUMMON_PROJECTILES);}
    public void setSelfAoe(boolean value) {this.dataManager.set(SELF_AOE, Boolean.valueOf(value));}
    public boolean isSelfAoe() {return this.dataManager.get(SELF_AOE);}
    public void setUseSword(boolean value) {this.dataManager.set(USE_SWORD, Boolean.valueOf(value));}
    public boolean isUseSword() {return this.dataManager.get(USE_SWORD);}
    public void setDashSweep(boolean value) {this.dataManager.set(DASH_SWEEP, Boolean.valueOf(value));}
    public boolean isDashSweep() {return this.dataManager.get(DASH_SWEEP);}
    public void setPierceStart(boolean value) {this.dataManager.set(PIERCE_START, Boolean.valueOf(value));}
    public boolean isPierceStart() {return this.dataManager.get(PIERCE_START);}
    public void setPierceFail(boolean value) {this.dataManager.set(PIERCE_FAIL, Boolean.valueOf(value));}
    public boolean isPierceFail() {return this.dataManager.get(PIERCE_FAIL);}
    public void setPierceFinish(boolean value) {this.dataManager.set(PIERCE_FINISH, Boolean.valueOf(value));}
    public boolean isPierceFinish() {return this.dataManager.get(PIERCE_FINISH);}
    public void setRangedMode(boolean value) {this.dataManager.set(RANGED_MODE, Boolean.valueOf(value));}
    public boolean isRangedMode() {return this.dataManager.get(RANGED_MODE);}
    public void setTeleportAttack(boolean value) {this.dataManager.set(TELEPORT_ATTACK, Boolean.valueOf(value));}
    public boolean isTeleportAttack() {return this.dataManager.get(TELEPORT_ATTACK);}
    public void setThrowSword(boolean value) {this.dataManager.set(THROW_SWORD, Boolean.valueOf(value));}
    public boolean isThrowSword() {return this.dataManager.get(THROW_SWORD);}
    public void setSpawnedNaturally(boolean value) {this.dataManager.set(SPAWNED_NATURALLY, Boolean.valueOf(value));}
    public boolean isSpawnedNaturally() {return this.dataManager.get(SPAWNED_NATURALLY);}
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
    public boolean isHadPreviousTarget() {return this.dataManager.get(HAD_PREVIOUS_TARGET);}
    public void setHadPreviousTarget(boolean value) {this.dataManager.set(HAD_PREVIOUS_TARGET, Boolean.valueOf(value));}

    private final AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_IDLE = "idle";
    private int rage_loop_count = 0;

    public EntityDauntless(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.iAmBossMob = true;
        this.experienceValue = 200;
        this.setSize(0.8F, 2.45F);
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.hemorrhage_resistance = 0.93F;
        this.falter_resistance = 1.7F;
        if(!world.isRemote) {
            initDauntlessAI();
        }
        if(!this.isSpawnedNaturally()) {
            BlockPos offset = new BlockPos(x, y - 6, z);
            this.setSpawnLocation(offset);
            this.setSetSpawnLoc(true);
        }
        this.setRangedMode(true);
    }

    public EntityDauntless(World worldIn, int timesUsed, BlockPos pos) {
        super(worldIn);
        this.iAmBossMob = true;
        this.timesUsed = timesUsed;
        this.experienceValue = 200;
        this.setSize(0.8F, 2.45F);
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.hemorrhage_resistance = 0.93F;
        this.falter_resistance = 1.7F;
        BlockPos offset = new BlockPos(pos.getX(), pos.getY() - 6, pos.getZ());
        this.setSpawnLocation(offset);
        //this.timesUsed++;
        this.setSetSpawnLoc(true);
        this.doBossReSummonScaling();
        this.setRangedMode(true);
        if(!world.isRemote) {
            initDauntlessAI();
        }
    }

    public EntityDauntless(World worldIn) {
        super(worldIn);
        this.iAmBossMob = true;
        this.experienceValue = 200;
        this.setSize(0.8F, 2.45F);
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.hemorrhage_resistance = 0.93F;
        this.falter_resistance = 1.7F;
        if(!world.isRemote) {
            initDauntlessAI();
        }
        this.setRangedMode(true);
    }

    private void initDauntlessAI() {
        float attackDistance = 12;
        float attackDistanceFar = (float) (this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()) - 22;
        this.tasks.addTask(4, new EntityDauntlessAttackAI(this, attackDistanceFar, attackDistance, 35, new TimedAttackInitiator<>(this, 30)));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setFloat("Look", this.getPitch());
        nbt.setInteger("Sword_Charge", this.getSwordCharge());
        nbt.setBoolean("Swing_One", this.isSwingOne());
        nbt.setBoolean("Swing_Two", this.isSwingTwo());
        nbt.setBoolean("Swing_Three", this.isSwingThree());
        nbt.setBoolean("Ground_Strike", this.isGroundStrike());
        nbt.setBoolean("Sword_Slash", this.isSwordSlash());
        nbt.setBoolean("Summon_Projectiles", this.isSummonProjectiles());
        nbt.setBoolean("Self_Aoe", this.isSelfAoe());
        nbt.setBoolean("Use_Sword", this.isUseSword());
        nbt.setBoolean("Dash_Sweep", this.isDashSweep());
        nbt.setBoolean("Pierce_Start", this.isPierceStart());
        nbt.setBoolean("Pierce_Fail", this.isPierceFail());
        nbt.setBoolean("Pierce_Finish", this.isPierceFinish());
        nbt.setBoolean("Circle_Swing", this.isCircleSwing());
        nbt.setBoolean("Circle_Swing_Continue", this.isCircleSwingContinue());
        nbt.setBoolean("Enragement_Mode", this.isEnragementMode());
        nbt.setBoolean("Enter_Rage", this.isEnterRage());
        nbt.setBoolean("Rage_Loop", this.isRageLoop());
        nbt.setBoolean("End_Rage", this.isEndRage());
        nbt.setBoolean("Punch", this.isPunch());
        nbt.setBoolean("Smash", this.isSmashAttack());
        nbt.setBoolean("Bite", this.isBiteAttack());
        nbt.setBoolean("Fist_Barrage", this.isFistBarrage());
        nbt.setBoolean("Charge_Fist", this.isChargeFist());
        nbt.setBoolean("Shaking", this.isShaking());
        nbt.setBoolean("Ranged_Mode", this.isRangedMode());
        nbt.setBoolean("Teleport_Attack", this.isTeleportAttack());
        nbt.setBoolean("Throw_Sword", this.isThrowSword());
        nbt.setBoolean("Spawned_Naturally", this.isSpawnedNaturally());
        nbt.setBoolean("Had_Target", this.isHadPreviousTarget());
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
        nbt.setBoolean("Set_Spawn_Loc", this.dataManager.get(SET_SPAWN_LOC));
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
        this.setSwordCharge(nbt.getInteger("Sword_Charge"));
        this.setSwingOne(nbt.getBoolean("Swing_One"));
        this.setSwingTwo(nbt.getBoolean("Swing_Two"));
        this.setSwingThree(nbt.getBoolean("Swing_Three"));
        this.setGroundStrike(nbt.getBoolean("Ground_Strike"));
        this.setSwordSlash(nbt.getBoolean("Sword_Slash"));
        this.setSummonProjectiles(nbt.getBoolean("Summon_Projectiles"));
        this.setSelfAoe(nbt.getBoolean("Self_Aoe"));
        this.setCircleSwing(nbt.getBoolean("Circle_Swing"));
        this.setCircleSwingContinue(nbt.getBoolean("Circle_Swing_Continue"));
        this.setEnragementMode(nbt.getBoolean("Enragement_Mode"));
        this.setEnterRage(nbt.getBoolean("Enter_Rage"));
        this.setEndRage(nbt.getBoolean("End_Rage"));
        this.setPunch(nbt.getBoolean("Punch"));
        this.setRageLoop(nbt.getBoolean("Rage_Loop"));
        this.setSmashAttack(nbt.getBoolean("Smash"));
        this.setBiteAttack(nbt.getBoolean("Bite"));
        this.setFistBarrage(nbt.getBoolean("Fist_Barrage"));
        this.setChargeFist(nbt.getBoolean("Charge_Fist"));
        this.setUseSword(nbt.getBoolean("Use_Sword"));
        this.setDashSweep(nbt.getBoolean("Dash_Sweep"));
        this.setPierceStart(nbt.getBoolean("Pierce_Start"));
        this.setPierceFail(nbt.getBoolean("Pierce_Fail"));
        this.setPierceFinish(nbt.getBoolean("Pierce_Finish"));
        this.setShaking(nbt.getBoolean("Shaking"));
        this.setRangedMode(nbt.getBoolean("Ranged_Mode"));
        this.setTeleportAttack(nbt.getBoolean("Teleport_Attack"));
        this.setThrowSword(nbt.getBoolean("Throw_Sword"));
        this.setSpawnedNaturally(nbt.getBoolean("Spawned_Naturally"));
        this.setHadPreviousTarget(nbt.getBoolean("Had_Target"));
        this.dataManager.set(SET_SPAWN_LOC, nbt.getBoolean("Set_Spawn_Loc"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
        super.readEntityFromNBT(nbt);
    }

    private int attack_differential = 0;
    private int HoverTimeIncrease = 0;
    private boolean hasHoverMovement = false;
    private int attack_cooldown = 0;

    @Override
    public void entityInit() {
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(SWORD_CHARGE, 0);
        this.dataManager.register(SWING_ONE, Boolean.valueOf(false));
        this.dataManager.register(SWING_TWO, Boolean.valueOf(false));
        this.dataManager.register(SWING_THREE, Boolean.valueOf(false));
        this.dataManager.register(GROUND_STRIKE, Boolean.valueOf(false));
        this.dataManager.register(SWORD_SLASH, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_PROJECTILES, Boolean.valueOf(false));
        this.dataManager.register(CHARGE_FIST, Boolean.valueOf(false));
        this.dataManager.register(FIST_BARRAGE, Boolean.valueOf(false));
        this.dataManager.register(BITE, Boolean.valueOf(false));
        this.dataManager.register(SMASH, Boolean.valueOf(false));
        this.dataManager.register(SELF_AOE, Boolean.valueOf(false));
        this.dataManager.register(USE_SWORD, Boolean.valueOf(false));
        this.dataManager.register(DASH_SWEEP, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_START, Boolean.valueOf(false));
        this.dataManager.register(CIRCLE_SWING, Boolean.valueOf(false));
        this.dataManager.register(CIRCLE_SWING_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(ENRAGEMENT_MODE, Boolean.valueOf(false));
        this.dataManager.register(ENTER_RAGE, Boolean.valueOf(false));
        this.dataManager.register(END_RAGE, Boolean.valueOf(false));
        this.dataManager.register(PUNCH, Boolean.valueOf(false));
        this.dataManager.register(RAGE_LOOP, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_FAIL, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_FINISH, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        this.dataManager.register(RANGED_MODE, Boolean.valueOf(false));
        this.dataManager.register(THROW_SWORD, Boolean.valueOf(false));
        this.dataManager.register(TELEPORT_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SPAWNED_NATURALLY, Boolean.valueOf(false));
        this.dataManager.register(SET_SPAWN_LOC, Boolean.valueOf(false));
        this.dataManager.register(HAD_PREVIOUS_TARGET, Boolean.valueOf(false));
        //
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
        super.entityInit();
    }

    private int rageTimer = MobConfig.dauntless_rage_timer * 20;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(getHealth() / getMaxHealth());
        this.shakeTime--;
        this.teleportCooldownTimer--;
        this.attack_cooldown--;
        this.rageTimer--;

        if(world.isRemote && ticksExisted == 1 && MobConfig.dauntless_boss_music) {
            this.playMusic(this);
        }

        if(!world.isRemote) {

            if(ticksExisted == 1 && this.isSpawnedNaturally() && !this.isHadPreviousTarget()) {
                this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.dauntless_damage_naturally);
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.dauntless_health_naturally);
                this.setHealth((float) MobConfig.dauntless_health_naturally);
            }
            if(general_use_sword_cooldown > -1) {
                general_use_sword_cooldown--;
            }
            if(sword_charge_one > 0) {
                sword_charge_one--;
            }
            if(sword_charge_two > 0) {
                sword_charge_two--;
            }
            if(sword_charge_3 > 0) {
                sword_charge_3--;
            }

            EntityLivingBase target = this.getAttackTarget();
            if(clearCurrentVelocity) {
                this.motionX = 0;
                this.motionY = 0;
                this.motionZ = 0;
                standbyOnVel = true;
                clearCurrentVelocity = false;
            }

            //switches the ai between distance and melee
                if(this.isRangedMode()) {
                    if(attack_differential > 6) {
                        this.setRangedMode(false);
                        this.attack_differential = 0;
                        System.out.println("Changed Ranged Mode");
                    }
                } else if (attack_differential > 13){
                        this.setRangedMode(true);
                        this.attack_differential = 0;
                }

            //target specific stuff
            if(target != null) {

                double healthFac = this.getHealth() / this.getMaxHealth();

                //handles the rage mechanic within the boss
                if(healthFac <= 0.4 && !this.isRangedMode() && !this.isEnragementMode()) {
                    double targetHealthFac = target.getHealth() / target.getMaxHealth();
                    //basically if the target has high health, the boss gets pissed
                    if(attack_differential > 10 && targetHealthFac >= 0.6) {
                        if(this.isFightMode()) {
                            this.attack_cooldown = 20;
                        } else {
                            this.enterRage();
                        }
                    }
                }

                //takes the boss out of rage mode and immediately into ranged
                if(this.isEnragementMode() && this.rageTimer < 0) {
                    if(this.isFightMode()) {
                        this.attack_cooldown = 20;
                    } else {
                        this.exitRage();
                    }
                }

                if(sword_charge_3 > 0 && ticksExisted % 100 == 0) {
                    new ActionSummonDelayedLazer(this.getPositionVector().add(ModRand.getFloat(2), 2, ModRand.getFloat(2))).performAction(this, target);
                }

                if (HoverTimeIncrease > 0) {
                    this.motionY = 0.25;
                    HoverTimeIncrease--;
                }

                if (this.hasHoverMovement) {
                    if(this.isRageLoop()) {
                        double d0 = (target.posX - this.posX) * 0.009;
                        double d2 = (target.posZ - this.posZ) * 0.009;
                        this.addVelocity(d0, 0, d2);
                        this.faceEntity(target, 30F, 30F);
                    } else {
                        double d0 = (target.posX - this.posX) * 0.013;
                        double d2 = (target.posZ - this.posZ) * 0.013;
                        this.addVelocity(d0, 0, d2);
                        this.faceEntity(target, 30F, 30F);
                    }
                }


                if(grabDetection && grabbedEntity == null) {
                    List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                            this.getEntityBoundingBox().offset(ModUtils.getRelativeOffset(this, new Vec3d(1.5, -0.5, 0))).grow(1.5D, 3.5D, 1.5D),
                            e -> !e.getIsInvulnerable());

                    if(!nearbyEntities.isEmpty()) {
                        for(EntityLivingBase base : nearbyEntities) {
                            if(!(base instanceof EntityDarkBase)) {
                                grabbedEntity = base;
                                this.playSound(SoundsHandler.KING_GRAB_SUCCESS, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                            }
                        }
                    }
                } else if (grabbedEntity != null) {
                    if(setBossToFlyHigh) {
                        this.motionY = 0.36;
                        this.motionX = 0;
                        this.motionZ = 0;
                        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.8, 1.2, 0)));
                        grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                        grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);
                    } else {
                        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.8, 0.4, 0)));
                        grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                        grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);
                    }

                }
            }


            if (this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
                Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());

                double distSq = this.getDistanceSq(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                double distance = Math.sqrt(distSq);
                //This basically makes it so the Obsidilith will be teleported if they are too far away from the Arena
                    if (distance > 50) {
                        this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                    }
            }

            if (this.getSpawnLocation() != null && this.isSetSpawnLoc() || this.isSpawnedNaturally()) {
                if (target != null) {
                    if (target instanceof EntityPlayer) {
                        this.setHadPreviousTarget(true);
                    }
                }

                if(!this.bossInfo.getPlayers().isEmpty() && ModConfig.boss_player_lives_enabled) {
                    for(EntityPlayerMP player : this.bossInfo.getPlayers()) {
                        if(!player.isEntityAlive()) {
                            this.bossInfo.removePlayer(player);
                            this.setBossPlayerLives(this.getBossPlayerLives() - 1);
                        }
                    }
                }

                //Creates a Target tracking to ensure if it can despawn or not
                if (target == null && this.isHadPreviousTarget() && ModConfig.boss_reset_enabled || this.getBossPlayerLives() <= 0 && ModConfig.boss_player_lives_enabled && (this.getSpawnLocation() != null || this.isSpawnedNaturally())) {
                    int nearbyPlayers = ServerScaleUtil.getPlayersForReset(this, world);
                    if (nearbyPlayers == 0 || this.getBossPlayerLives() <= 0 && ModConfig.boss_player_lives_enabled) {
                        if (targetTrackingTimer > 0) {
                            targetTrackingTimer--;
                        }
                        if (targetTrackingTimer < 1 || this.getBossPlayerLives() <= 0 && ModConfig.boss_player_lives_enabled) {
                            if(this.isSpawnedNaturally()) {
                                //removes Dauntless if left inactive
                              this.setDead();
                            } else if (this.timesUsed != 0) {
                                this.timesUsed--;
                                 turnBossIntoSummonSpawner(this.getSpawnLocation().add(0, -6, 0));
                                this.setDead();
                            } else {
                                 this.resetBossTask();
                            }
                        }
                    }
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
                    boss_spawner.setState(BlockEnumBossSummonState.INACTIVE, this.timesUsed, "dauntless");
                }
            }
        }
    }

    private void resetBossTask() {
        this.setImmovable(true);
        this.setHadPreviousTarget(false);

        if(!this.isSpawnedNaturally()) {
            BlockPos pos = this.getSpawnLocation();
            EntityDarkVoid void_spawn = new EntityDarkVoid(world, 1, false, true);
            void_spawn.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            world.spawnEntity(void_spawn);
        }
        this.experienceValue = 0;
        this.setDropItemsWhenDead(false);
        this.setDead();
    }

    private void enterRage() {
        this.setEnterRage(true);
        this.setFightMode(true);
        this.setEnragementMode(true);
        addEvent(()-> {
            this.setImmovable(true);
        }, 30);

        addEvent(()-> this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.5f, 1.0f), 20);
        addEvent(()-> this.playSound(SoundsHandler.DAUNTLESS_PUNCH, 1.5f, 1.0f), 43);
        addEvent(()-> {
            this.playSound(SoundsHandler.DAUNTLESS_YELL_SHORT, 1.5f, 0.6f);
            this.setShaking(true);
            this.shakeTime = 15;
            addEvent(()-> this.setShaking(false), 15);
        }, 20);
        addEvent(()-> {
            this.setEnterRage(false);
            this.setFightMode(false);
            this.setImmovable(false);
            this.rageTimer = MobConfig.dauntless_rage_timer * 20;
            this.attack_cooldown = 0;
        }, 70);
    }

    private void exitRage() {
        this.setEndRage(true);
        this.setFightMode(true);
        this.setRangedMode(true);
        this.setEnragementMode(false);
        this.attack_differential = 0;

        addEvent(()-> this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.5f, 1.0f), 16);

        addEvent(()-> {
            this.setEndRage(false);
            this.setFightMode(false);
        }, 35);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.isSpawnedNaturally() ? MobConfig.dauntless_damage_naturally : MobConfig.dauntless_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.20590D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.isSpawnedNaturally() ? MobConfig.dauntless_health_naturally : MobConfig.dauntless_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.dauntless_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.dauntless_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double HealthChange = this.getHealth() / this.getMaxHealth();
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode() && this.attack_cooldown < 1 && !this.isEnterRage() && !this.isEndRage()) {
            //rage mode attacks
            if(this.isSpawnedNaturally()){
                List<Consumer<EntityLivingBase>> close_attacks = new ArrayList<>(Arrays.asList(swing_attack, ground_strike, dash_sweep, self_aoe, pierce_start, use_sword_ability, throw_sword, summon_projectiles, sword_slash, use_teleport_ranged));
                double[] weights = {
                        (prevAttack != swing_attack && distance < 9 && !this.isRangedMode()) ? 1 / distance : 0, //Swing attack
                        (prevAttack != ground_strike && distance < 15 && !this.isRangedMode()) ? 1 / distance : 0, //Ground Strike
                        (prevAttack != dash_sweep && distance < 15 && !this.isRangedMode()) ? 1 / distance : 0, //Dash Sweep Attack
                        (prevAttack != self_aoe && distance < 8 && !this.isRangedMode()) ? 1.1 / distance : 0, //Self AOE
                        (prevAttack != pierce_start && distance < 24 && !this.isRangedMode() && HealthChange <= 0.5) ? 1 / distance : 0, //Pierce Teleport Grab Attack
                        (prevAttack != use_sword_ability && this.getSwordCharge() > 0 && general_use_sword_cooldown < 1 && !this.isRangedMode()) ? 2 / distance : 0, //Use Sword Ability
                        (prevAttack != throw_sword && this.isRangedMode()) ? distance * 0.02 : (prevAttack != throw_sword && distance > 13) ? distance * 0.02 : 0, //Throw Sword Attack
                        (prevAttack != summon_projectiles && distance > 5) ? distance * 0.02 : 0, //Summon Projectiles
                        (prevAttack != sword_slash && this.isRangedMode() && HealthChange <= 0.5) ? distance * 0.02 : (prevAttack != sword_slash && distance > 13 && HealthChange <= 0.5) ? distance * 0.01 : 0, //Sword Slash
                        (prevAttack != use_teleport_ranged && this.isRangedMode()) ? distance * 0.01 : 0, //Use Teleport ability as back up
                };
                prevAttack = ModRand.choice(close_attacks, rand, weights).next();
                prevAttack.accept(target);
            }else if(this.isEnragementMode()) {
                List<Consumer<EntityLivingBase>> close_attacks = new ArrayList<>(Arrays.asList(punch, bite_attack, smash_attack, charge_fist, loop_fist_barrage));
                double[] weights = {
                        (prevAttack != punch && distance < 8) ? 1/distance : 0,
                        (prevAttack != bite_attack && distance < 8) ? 1/distance : 0,
                        (prevAttack != smash_attack && distance < 8) ? 1/distance : 0,
                        (prevAttack != charge_fist && distance > 3) ? distance * 0.02 : 0,
                        (prevAttack != loop_fist_barrage && distance > 7) ? distance * 0.02 : 0
                };
                prevAttack = ModRand.choice(close_attacks, rand, weights).next();
                prevAttack.accept(target);
            } else {
                List<Consumer<EntityLivingBase>> close_attacks = new ArrayList<>(Arrays.asList(swing_attack, ground_strike, dash_sweep, self_aoe, pierce_start, use_sword_ability, throw_sword, summon_projectiles, sword_slash, use_teleport_ranged, circle_swing, rage_loop));
                double[] weights = {
                        (prevAttack != swing_attack && distance < 9 && !this.isRangedMode()) ? 1 / distance : 0, //Swing attack
                        (prevAttack != ground_strike && distance < 15 && !this.isRangedMode()) ? 1 / distance : 0, //Ground Strike
                        (prevAttack != dash_sweep && distance < 15 && !this.isRangedMode()) ? 1 / distance : 0, //Dash Sweep Attack
                        (prevAttack != self_aoe && distance < 8 && !this.isRangedMode()) ? 1.1 / distance : 0, //Self AOE
                        (prevAttack != pierce_start && distance < 24 && !this.isRangedMode() && HealthChange <= 0.75) ? 1 / distance : 0, //Pierce Teleport Grab Attack
                        (prevAttack != use_sword_ability && this.getSwordCharge() > 0 && general_use_sword_cooldown < 1 && !this.isRangedMode()) ? 2 / distance : 0, //Use Sword Ability
                        (prevAttack != throw_sword && this.isRangedMode()) ? distance * 0.02 : (prevAttack != throw_sword && distance > 13) ? distance * 0.02 : 0, //Throw Sword Attack
                        (prevAttack != summon_projectiles && distance > 5) ? distance * 0.02 : 0, //Summon Projectiles
                        (prevAttack != sword_slash && this.isRangedMode() && HealthChange <= 0.75) ? distance * 0.02 : (!this.isRangedMode() && prevAttack != sword_slash && distance > 5 && HealthChange <= 0.75) ? 1/distance : 0, //Sword Slash
                        (prevAttack != use_teleport_ranged && this.isRangedMode()) ? distance * 0.01 : 0, //Use Teleport ability as back up
                        (prevAttack != circle_swing && !this.isRangedMode() && HealthChange <= 0.6) ? 1 / distance : 0, //Circle Swing Attack
                        (prevAttack != rage_loop && !this.isRangedMode() && HealthChange <= 0.3) ? 1/distance : 0 //Rage Loop
                };
                prevAttack = ModRand.choice(close_attacks, rand, weights).next();
                prevAttack.accept(target);
            }
        }
        return 0;
    }

    private final Consumer<EntityLivingBase> use_teleport_ranged = (target) -> {
      this.teleportAbility(target, true);
    };

    private final Consumer<EntityLivingBase> use_sword_ability = (target) -> {
        this.setUseSword(true);
        this.setFightMode(true);
        this.setImmovable(true);

        addEvent(()-> {
            this.lockLook = true;
            //allows the boss to use various different buffs to it's aid
            //Degradation
            if(this.getSwordCharge() == 1) {
                this.sword_charge_one = MobConfig.dauntless_ability_timer * 20;
                world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
                for(int i = 0; i < 4; i++) {
                    addEvent(()-> Main.proxy.spawnParticle(41, world, this.posX, this.posY + 1.5, this.posZ, 0, 0, 0, this.getEntityId()), i * 5);
                }
                this.playSound(SoundsHandler.DAUNTLESS_USE_SWORD, 1.0f, 1.3f);
                //Blood
            } else if (this.getSwordCharge() == 2) {
                this.sword_charge_two = (MobConfig.dauntless_ability_timer * 20) + 10;
                world.setEntityState(this, ModUtils.FOURTH_PARTICLE_BYTE);
                for(int i = 0; i < 4; i++) {
                    addEvent(()-> Main.proxy.spawnParticle(40, world, this.posX, this.posY + 1.5, this.posZ, 0, 0, 0, this.getEntityId()), i * 5);
                }
                this.playSound(SoundsHandler.DAUNTLESS_USE_SWORD, 1.0f, 1.0f);
                //Faltering
            } else if (this.getSwordCharge() >= 3) {
                for(int i = 0; i < 4; i++) {
                    addEvent(()-> Main.proxy.spawnParticle(39, world, this.posX, this.posY + 1.5, this.posZ, 0, 0, 0, this.getEntityId()), i * 5);
                }
                this.playSound(SoundsHandler.DAUNTLESS_USE_SWORD, 1.0f, 0.7f);
                this.sword_charge_3 = MobConfig.dauntless_ability_timer * 20;
                world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
                //summons giant sword on the player's position
                addEvent(()-> {
                    Vec3d targetPos = target.getPositionVector();
                    addEvent(() -> {
                        Vec3d targetedPos = target.getPositionVector();
                        Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetPos, targetedPos, 4);
                        EntityDauntlessSword sword = new EntityDauntlessSword(world, true);
                        sword.setPosition(predictedPosition.x, target.posY + 16, predictedPosition.z);
                        world.spawnEntity(sword);
                    }, 3);
                }, 1);
            }
            this.setSwordCharge(0);
        }, 35);

        addEvent(()-> {
            this.general_use_sword_cooldown = 10 * 20;
            this.lockLook = false;
            this.setUseSword(false);
            this.setFightMode(false);
            this.setImmovable(false);
            this.attack_cooldown = 10;
        }, 60);
    };

    private final Consumer<EntityLivingBase> throw_sword = (target) -> {
        this.setThrowSword(true);
        this.setFightMode(true);
        this.playSound(SoundsHandler.DAUNTLESS_PREPARE_SPELL, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));

        addEvent(() -> new ActionDauntlessThrowSword().performAction(this, target), 35);
        addEvent(()-> this.playSound(SoundsHandler.DAUNTLESS_THROW_SWORD, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f)), 30);

        addEvent(()-> {
            this.setThrowSword(false);
            this.setFightMode(false);
            if(this.isRangedMode()) {
                this.attack_differential += 2;
                this.attack_cooldown = 70;
            } else {
                this.attack_differential += 1;
                this.attack_cooldown = 40;
            }
        }, 80);
    };

    private final Consumer<EntityLivingBase> summon_projectiles = (target) -> {
      this.setSummonProjectiles(true);
      this.setFightMode(true);
        this.playSound(SoundsHandler.DAUNTLESS_PREPARE_SPELL, 1.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
      if(!this.isRangedMode()) {
          this.HoverTimeIncrease = 4;
      }
      addEvent(()-> {
          this.playSound(SoundsHandler.DAUNTLESS_CAST_PROJECTILE, 1.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
          new ActionDauntlessSpearSplay().performAction(this, target);
      }, 18);

      addEvent(()-> {
        this.setSummonProjectiles(false);
        this.setFightMode(false);
          if(this.isRangedMode()) {
              this.attack_differential += 1;
          } else {
              this.attack_cooldown = 40;
          }
      }, 30);
    };

    private final Consumer<EntityLivingBase> sword_slash = (target) -> {
      this.setSwordSlash(true);
      this.setFightMode(true);
        this.playSound(SoundsHandler.DAUNTLESS_PREPARE_SPELL, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
      addEvent(()-> {

        new ActionDauntlessShootCrystal().performAction(this, target);
      }, 30);

      addEvent(()-> {
            this.setSwordSlash(false);
            this.setFightMode(false);
          if(this.isRangedMode()) {
              this.attack_differential += 1;
          }
      }, 65);
    };

    private final Consumer<EntityLivingBase> pierce_start = (target) -> {
      this.setPierceStart(true);
      this.setFightMode(true);
      this.setImmovable(true);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-6));
            addEvent(()-> {
                this.setImmovable(false);
                ModUtils.attemptTeleport(targetedPos.add(0, 0.25, 0), this);
                this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.75f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 3);
        }, 20);

        addEvent(()-> {
            this.setImmovable(true);
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(4));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                this.grabDetection = true;
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.13),0.1F);
            }, 8);
        }, 20);

      addEvent(() -> {
            this.grabDetection = false;
            this.setImmovable(true);
            this.setPierceStart(false);
            if(this.grabbedEntity != null) {
                this.setGrabTooContinue(target);
            } else {
                this.setPierceFail(true);

                addEvent(()-> this.lockLook = false, 5);
                addEvent(()-> {
                this.setPierceFail(false);
                this.setFightMode(false);
                this.setImmovable(false);
                this.attack_differential += 1;
                this.attack_cooldown = 10;
                }, 20);
            }
      }, 55);
    };

    private void setGrabTooContinue(EntityLivingBase target) {
        this.pierce_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> pierce_continue = (target) -> {
        this.setPierceFinish(true);
        addEvent(()-> {
            this.setBossToFlyHigh = true;
            this.setImmovable(false);
            this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (0);
            DauntlessUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.1f, 0, false, 0);
        }, 3);

        addEvent(()-> {
            this.setImmovable(true);
            addEvent(()-> {
                this.setBossToFlyHigh = false;
                this.grabbedEntity = null;
                this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.0, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.1f, 0, false, ModPotions.HEMORRHAGE, 1, 300, 2F);
            }, 2);
        }, 30);

        addEvent(()-> {
            this.lockLook = false;
        }, 55);

        addEvent(()-> {
            this.setImmovable(false);
            this.setPierceFinish(false);
            this.setFightMode(false);
            this.attack_differential += 2;
            this.attack_cooldown = 30;
        }, 60);
    };

    private final Consumer<EntityLivingBase> self_aoe = (target) -> {
      this.setFightMode(true);
      this.setSelfAoe(true);
      this.setImmovable(true);

      addEvent(()-> {
        this.lockLook = true;
      }, 20);

        addEvent(()-> {
            Main.proxy.spawnParticle(22,world, this.posX, this.posY + 0.1, this.posZ, 0, 0, 0);
            Main.proxy.spawnParticle(22,world, this.posX, this.posY + 3.1, this.posZ, 0, 0, 0);
            this.playSound(SoundsHandler.DAUNTLESS_SELF_AOE, 1.25f, 0.4f / (rand.nextFloat() * 0.4f + 0.6f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.25, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (this.getAttack());
            DauntlessUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.9F);
            //do AOE action
            new ActionDauntlessSelfAOE().performAction(this, target);
        }, 27);

        addEvent(()-> {
            this.lockLook = false;
        }, 40);

      addEvent(()-> {
        this.setFightMode(false);
        this.setSelfAoe(false);
        this.setImmovable(false);
        this.attack_differential += 2;
        this.attack_cooldown = 40;
      }, 45);
    };

    private final Consumer<EntityLivingBase> dash_sweep = (target) -> {
        this.setFightMode(true);
        this.setDashSweep(true);
        this.setImmovable(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                //this.holdPosition = false;
                double distance = this.getPositionVector().distanceTo(softTargetPos);
                ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
            }, 5);
        }, 19);

        addEvent(()-> {
            this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 0.75, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (this.getAttack());
            DauntlessUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.4F);
            double healthFac = this.getHealth() / this.getMaxHealth();
            if(healthFac <= 0.3 && !this.isSpawnedNaturally()) {
                new ActionDauntlessRing().performAction(this, target);
            }
        }, 29);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 40);

        addEvent(()-> {
            this.teleportAbility(target, false);
        }, 55);

        addEvent(()-> {
            this.setDashSweep(false);
            this.setFightMode(false);
            this.setImmovable(false);
            this.attack_cooldown = 10;
        }, 60);
    };

    private final Consumer<EntityLivingBase> loop_fist_barrage = (target) -> {
      int times = ModRand.range(3, 6);
      this.setFistBarrage(true);
      this.setFightMode(true);
      this.setImmovable(true);

      addEvent(()-> {
          //do first launch then iterate more
           addEvent(()-> {
               ProjectileDauntlessFist missile = new ProjectileDauntlessFist(world, this, (float) (this.getAttack() * 0.8));
               Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3,1.7,0)));
               missile.setPosition(relPos.x, relPos.y, relPos.z);
               this.world.spawnEntity(missile);
               missile.setTravelRange(20);
               Vec3d targetPos = target.getPositionEyes(1.0F).add(0, -0.3, 0);
               Vec3d fromTargetTooActor = relPos.subtract(targetPos);
               Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(0);
               Vec3d lineStart = targetPos.subtract(lineDir);
               Vec3d lineEnd = targetPos.add(lineDir);
               float speed = (float) 1.4;
               this.playSound(SoundsHandler.DAUNTLESS_PUNCH, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
               ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                   ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
               });
           }, 1);
          //next interate times every second minus the first one
          for(int b = 0; b <= (2 * times) - 1; b++) {
              addEvent(()-> {
                  ProjectileDauntlessFist missile = new ProjectileDauntlessFist(world, this, (float) (this.getAttack() * 0.8));
                  Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3,1.7,0)));
                  missile.setPosition(relPos.x, relPos.y, relPos.z);
                  this.world.spawnEntity(missile);
                  missile.setTravelRange(20);
                  Vec3d targetPos = target.getPositionEyes(1.0F).add(0, -0.3, 0);
                  Vec3d fromTargetTooActor = relPos.subtract(targetPos);
                  this.playSound(SoundsHandler.DAUNTLESS_PUNCH, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                  Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(0);
                  Vec3d lineStart = targetPos.subtract(lineDir);
                  Vec3d lineEnd = targetPos.add(lineDir);
                  float speed = (float) 1.4;

                  ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                      ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
                  });
              }, b * 10);
          }
      }, 10);
      addEvent(()-> {
          this.setFistBarrage(false);
          this.setFightMode(false);
          this.setImmovable(false);
      }, 20 * times);
    };

    private final Consumer<EntityLivingBase> charge_fist = (target) -> {
        this.setChargeFist(true);
        this.setFightMode(true);
        this.setImmovable(true);
        this.playSound(SoundsHandler.WYRK_STAFF_CHARGE, 1f, 0.4f / (rand.nextFloat() * 0.4f + 0.6f));

        addEvent(()-> {
            this.setImmovable(false);
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-0.75));
            this.setPosition(softTargetPos.x, softTargetPos.y, softTargetPos.z);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            this.setImmovable(true);
        }, 23);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (this.getAttack() * 1.2F);
            this.playSound(SoundsHandler.DAUNTLESS_PUNCH, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.6f));
            DauntlessUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 1.2f, 0, false, 0.3F);
        }, 30);

        addEvent(()-> {
            this.lockLook  =false;
            this.setImmovable(false);
        }, 45);

        addEvent(()-> {
            this.setChargeFist(false);
            this.setFightMode(false);
        }, 50);
    };

    private final Consumer<EntityLivingBase> smash_attack = (target) -> {
        this.setSmashAttack(true);
        this.setFightMode(true);

        addEvent(()-> {
            //teleport boss to the ground
            int y_value = ModUtils.getSurfaceHeightGeneral(world, new BlockPos(this.posX, 0, this.posZ), (int) this.posY - 20, (int) this.posY + 2);
            if(y_value != this.posY) {
                this.setPosition(this.posX, y_value + 1, this.posZ);
            } else {
                //destroy blocks just incase
                this.setPosition(this.posX, target.posY, this.posZ);
            }
            this.destroyBlocksInSwing(new Vec3d(this.posX, this.posY, this.posZ), 1.0F);
            this.hasHoverMovement = false;
            this.setImmovable(true);
            this.lockLook = true;
        }, 15);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0.25, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack());
            DauntlessUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.9f, 0, false, 0.3F);
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0, 0)));
            Main.proxy.spawnParticle(22,world, relPos.x, this.posY + 0.1, relPos.z, 0, 0, 0);
            this.destroyBlocksInSwing(offset, 3.5F);
            this.playSound(SoundsHandler.DAUNTLESS_PUNCH, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.6f));
            this.playSound(SoundsHandler.DAUNTLESS_IMPACT, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.6f));
            new ActionDauntlessImpact(5).performAction(this, target);
            this.setShaking(true);
            this.shakeTime = 7;
            addEvent(()-> {
                this.setShaking(false);
            }, 7);
        }, 20);

        addEvent(()-> {
            this.lockLook = false;
        }, 30);

        addEvent(()-> {
            this.setImmovable(false);
            this.setSmashAttack(false);
            this.setFightMode(false);
        }, 40);
    };

    private final Consumer<EntityLivingBase> bite_attack = (target) -> {
      this.setBiteAttack(true);
      this.setFightMode(true);

      addEvent(()-> this.playSound(SoundsHandler.DAUNTLESS_YELL_SHORT, 1.3f, 1f / (rand.nextFloat() * 0.4f + 0.6f)), 5);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(softTargetPos);
                ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
            }, 6);
        }, 22);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 0.75, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.8f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            float damage = (float) (this.getAttack());
            DauntlessUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.1f, 0, false, MobEffects.BLINDNESS, 0, 100, 0.3F);
            this.setImmovable(true);
        }, 32);

        addEvent(()-> this.lockLook = false, 40);

      addEvent(()-> {
          this.setImmovable(false);
        this.setBiteAttack(false);
        this.setFightMode(false);
      }, 45);
    };

    private final Consumer<EntityLivingBase> punch = (target) -> {
        this.setPunch(true);
        this.setFightMode(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(0));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(softTargetPos);
                ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
            }, 4);
        }, 9);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0.75, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack() * 0.8);
            this.playSound(SoundsHandler.DAUNTLESS_PUNCH, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            DauntlessUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.1f, 0, false, 0.1F);
        }, 17);

        addEvent(()-> {
            this.lockLook = false;
        }, 23);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(0));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(softTargetPos);
                ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
            }, 4);
        }, 29);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0.75, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack() * 0.8);
            this.playSound(SoundsHandler.DAUNTLESS_PUNCH, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            DauntlessUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.1f, 0, false, 0.1F);
        }, 38);

        addEvent(()-> this.lockLook = false, 45);

        addEvent(()-> {
            this.setPunch(false);
            this.setFightMode(false);
        }, 50);

    };

    private final Consumer<EntityLivingBase> circle_swing = (target) -> {
      this.setFightMode(true);
      this.setImmovable(true);
      double healthFac = this.getHealth() / this.getMaxHealth();
      boolean randB = rand.nextBoolean() && healthFac <= 0.5;

      //double
      if(randB) {
            this.setCircleSwingContinue(true);

          addEvent(()-> {
              this.lockLook = true;
              Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
              Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(3));
              addEvent(()-> {
                  this.setImmovable(false);
                  double distance = this.getPositionVector().distanceTo(softTargetPos);
                  ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
              }, 5);
          }, 19);

          addEvent(()-> {
              for(int i = 0; i <= 9; i += 3) {
                  addEvent(()-> {
                      this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
                      Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0.75, 0)));
                      DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                      float damage = (float) (this.getAttack());
                      DauntlessUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.3F);
                  }, i);
              }
          }, 25);

          addEvent(()-> {
              this.setImmovable(true);
              this.lockLook = false;
          }, 45);

          addEvent(()-> {
              this.lockLook = true;
              Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
              Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(3));
              addEvent(()-> {
                  this.setImmovable(false);
                  //this.holdPosition = false;
                  double distance = this.getPositionVector().distanceTo(softTargetPos);
                  ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
              }, 5);
          }, 54);

          addEvent(()-> {
              for(int i = 0; i <= 9; i += 3) {
                  addEvent(()-> {
                      this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
                      Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0.75, 0)));
                      DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                      float damage = (float) (this.getAttack());
                      DauntlessUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.3F);
                  }, i);
              }
          }, 60);

          addEvent(()-> {
              this.setImmovable(true);
              this.lockLook = false;
          }, 80);

          addEvent(()-> {
              this.setCircleSwingContinue(false);
              this.setFightMode(false);
              this.setImmovable(false);
              this.attack_differential += 2;
              this.attack_cooldown = 10;
          }, 95);

      } else {
          this.setCircleSwing(true);

          addEvent(()-> {
              this.lockLook = true;
              Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
              Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(3));
              addEvent(()-> {
                  this.setImmovable(false);
                  //this.holdPosition = false;
                  double distance = this.getPositionVector().distanceTo(softTargetPos);
                  ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
              }, 5);
          }, 19);

          addEvent(()-> {
              for(int i = 0; i <= 9; i += 3) {
                  addEvent(()-> {
                      this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
                      Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0.75, 0)));
                      DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                      float damage = (float) (this.getAttack());
                      DauntlessUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.3F);
                  }, i);
              }
          }, 25);

          addEvent(()-> {
              this.setImmovable(true);
              this.lockLook = false;
          }, 45);

          addEvent(()-> {
                this.setFightMode(false);
                this.setImmovable(false);
                this.setCircleSwing(false);
                this.attack_differential += 1;
                this.attack_cooldown = 10;
          }, 55);
      }
    };

    private final Consumer<EntityLivingBase> rage_loop = (target) -> {
      this.setFightMode(true);
      this.setRageLoop(true);
      this.HoverTimeIncrease = 4;
        this.playSound(SoundsHandler.DAUNTLESS_YELL, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
      addEvent(()-> {
          this.hasHoverMovement = true;
      }, 12);

        addEvent(()-> {
            //teleport boss to the ground
            int y_value = ModUtils.getSurfaceHeightGeneral(world, new BlockPos(this.posX, 0, this.posZ), (int) this.posY - 20, (int) this.posY + 2);
            if(y_value != this.posY) {
                this.setPosition(this.posX, y_value + 1, this.posZ);
            } else {
                //destroy blocks just incase
                this.setPosition(this.posX, target.posY, this.posZ);
            }
            this.destroyBlocksInSwing(new Vec3d(this.posX, this.posY, this.posZ), 1.0F);
            this.hasHoverMovement = false;
            this.setImmovable(true);
            this.lockLook = true;
        }, 35);

        addEvent(()-> {
            this.playSound(SoundsHandler.DAUNTLESS_SWING, 2.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
        }, 30);

        addEvent(()-> {
            //do explosion attack
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0.25, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack() * 1.5F);
            DauntlessUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 0.9f, 0, false, 1.2F);
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0, 0)));
            Main.proxy.spawnParticle(22,world, relPos.x, this.posY + 0.1, relPos.z, 0, 0, 0);
            this.playSound(SoundsHandler.DAUNTLESS_IMPACT, 2.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.6f));
            this.destroyBlocksInSwing(offset, 3.5F);
            new ActionDauntlessImpact(2).performAction(this, target);
            this.setShaking(true);
            this.shakeTime = 15;
            addEvent(()-> {
                this.setShaking(false);
            }, 15);
        }, 40);

        addEvent(()-> {
            //ground shake and summon projectiles
            boolean doLazers = false;
            if(rage_loop_count == 0 || rage_loop_count == 2 || rage_loop_count == 4) {
                doLazers = true;
            }
            new ActionDauntlessBigAOE(doLazers).performAction(this, target);
        }, 45);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(false);
        }, 70);

        addEvent(()-> {
            this.rage_loop_count++;

            if(rage_loop_count < 5 && world.rand.nextInt(2) != 0) {
                this.setRageLoopTooContinue(target);
            } else {
                this.setFightMode(false);
                this.setRageLoop(false);
                this.attack_differential += this.rage_loop_count;
                this.rage_loop_count = 0;
            }
        }, 80);
    };

    private void setRageLoopTooContinue(EntityLivingBase target) {
        if(target != null) {
            rage_loop.accept(target);
        } else {
            this.setFightMode(false);
            this.setRageLoop(false);
            this.rage_loop_count = 0;
        }
    }

    private final Consumer<EntityLivingBase> ground_strike = (target) -> {
      this.setFightMode(true);
      this.setGroundStrike(true);
      this.HoverTimeIncrease = 3;
      //we basically want dauntless to glide up then track the player
        addEvent(()-> {
            this.hasHoverMovement = true;
        }, 12);

        addEvent(()-> {
            //teleport boss to the ground
            int y_value = ModUtils.getSurfaceHeightGeneral(world, new BlockPos(this.posX, 0, this.posZ), (int) this.posY - 20, (int) this.posY + 2);
            if(y_value != this.posY) {
                this.setPosition(this.posX, y_value + 1, this.posZ);
            } else {
                //destroy blocks just incase
                this.setPosition(this.posX, target.posY, this.posZ);
            }
            this.destroyBlocksInSwing(new Vec3d(this.posX, this.posY, this.posZ), 1.0F);
            this.hasHoverMovement = false;
            this.setImmovable(true);
            this.lockLook = true;
        }, 30);

        addEvent(()-> {
            //do explosion attack
            this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0.25, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack() * 1.5F);
            DauntlessUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 0.9f, 0, false, 1.1F);
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0, 0)));
            Main.proxy.spawnParticle(22,world, relPos.x, this.posY + 0.1, relPos.z, 0, 0, 0);
            this.playSound(SoundsHandler.DAUNTLESS_IMPACT, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.6f));
            this.destroyBlocksInSwing(offset, 3.5F);
            new ActionDauntlessImpact(3).performAction(this, target);
            this.setShaking(true);
            this.shakeTime = 7;
            addEvent(()-> {
                this.setShaking(false);
            }, 7);
        }, 35);

        addEvent(()-> {
            this.lockLook = false;
        }, 62);


      addEvent(()-> {
        this.setFightMode(false);
        this.setGroundStrike(false);
        this.setImmovable(false);
        this.attack_differential += 1;
        this.attack_cooldown = 20;
      }, 75);
    };

    private final Consumer<EntityLivingBase> swing_attack = (target) -> {
        this.setFightMode(true);
        this.setImmovable(true);
        int randB;
        if(this.isSpawnedNaturally()) {
            randB = ModRand.range(1, 2);
        } else {
           randB = ModRand.range(1, 4);
        }
        this.playSound(SoundsHandler.DAUNTLESS_YELL_SHORT, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
        this.setShaking(true);
        this.shakeTime = 25;
        addEvent(()-> {
            this.setShaking(false);
        }, 25);

        //Swings sword once
        if(randB == 1) {
            this.setSwingOne(true);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
                }, 5);
            }, 15);

            addEvent(()-> {
                this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.6F);
            }, 26);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 40);

            addEvent(()-> {
                this.setImmovable(false);
                this.setSwingOne(false);
                this.setFightMode(false);
                this.attack_cooldown = 10;
            }, 60);
        }
        //swings sword Twice
        if(randB == 2) {
            this.setSwingTwo(true);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
                }, 5);
            }, 15);

            addEvent(()-> {
                this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.6F);
            }, 26);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 40);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
                }, 5);
            }, 50);

            addEvent(()-> {
                this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.6F);

            }, 61);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 73);

            addEvent(()-> {
                this.setImmovable(false);
                this.setSwingTwo(false);
                this.setFightMode(false);
                this.attack_differential += 1;
                this.attack_cooldown = 20;
            }, 85);
        }

        //Swing sword three
        if(randB >= 3) {
            this.setSwingThree(true);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
                }, 5);
            }, 15);

            addEvent(()-> {
                this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.6F);
            }, 26);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 40);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
                }, 5);
            }, 50);

            addEvent(()-> {
                this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.6F);
            }, 61);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 80);

            addEvent(()-> {
                this.playSound(SoundsHandler.DAUNTLESS_YELL_SHORT, 1.5f, 0.4f / (rand.nextFloat() * 0.4f + 0.6f));
                this.setShaking(true);
                this.shakeTime = 25;
                addEvent(()-> {
                    this.setShaking(false);
                }, 25);
            }, 75);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
                }, 5);
            }, 86);

            addEvent(()-> {
                this.playSound(SoundsHandler.DAUNTLESS_SWING, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.6F);
                double healthFac = this.getHealth() / this.getMaxHealth();
                if(healthFac <= 0.3) {
                    new ActionDauntlessRing().performAction(this, target);
                }
            }, 97);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 110);

            addEvent(()-> {
                this.setSwingThree(false);
                this.setFightMode(false);
                this.setImmovable(false);
                this.attack_differential += 2;
                this.attack_cooldown = 30;
            }, 130);
        }
    };

    private void teleportAbility(EntityLivingBase target, boolean needsAnimation) {
        if(needsAnimation) {
            this.setFightMode(true);
            this.setTeleportAttack(true);
        }
        addEvent(()-> {
            if(!this.isRangedMode()) {
                if (target != null) {
                    //calculated teleport attack
                    boolean backwards = rand.nextBoolean();
                    Vec3d lookPos = target.getLookVec();
                    Vec3d targetPos = new Vec3d(target.posX + lookPos.x * 0.5D, target.posY, target.posZ + lookPos.z * 0.5);
                    Vec3d teleportPos = null;
                    for (int i = 1; i <= 256; i++) {
                        Vec3d posSet = targetPos.subtract(this.getPositionVector()).normalize();
                        Vec3d targetedPos = targetPos.add(posSet.scale(backwards ? i : -i));
                        int y = ModUtils.getSurfaceHeightZeroReturn(world, new BlockPos(targetedPos.x, 0, targetedPos.z), (int) target.posY - 4, (int) target.posY + 3);
                        if (y != 0 && (backwards && target.getDistance(targetedPos.x, y + 1, targetedPos.z) > 5 || !backwards && target.getDistance(targetedPos.x, y + 1, targetedPos.z) > 7)) {
                            teleportPos = new Vec3d(targetedPos.x, y + 1, targetedPos.z);
                            break;
                        }
                    }

                    if (teleportPos != null) {
                        this.setPosition(teleportPos.x, teleportPos.y, teleportPos.z);
                    } else {
                        //random teleport for ground mode
                        for (int i = 4; i <= 25; i++) {
                            Vec3d idealPos = new Vec3d(this.posX + ModRand.getFloat(i), this.posY, this.posZ + ModRand.getFloat(i));
                            int y = ModUtils.getSurfaceHeightZeroReturn(world, new BlockPos(idealPos.x, 0, idealPos.z), (int) this.posY - 12, (int) this.posY + 5);
                            if (y != 0 && target.getDistance(idealPos.x, y + 1, idealPos.z) > 6) {
                                this.setPosition(idealPos.x, y + 1, idealPos.z);
                            }
                        }
                    }
                    this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.8F, 1.0F / (rand.nextFloat() * 0.4F + 0.6F));
                    this.setImmovable(true);
                }
            } else {
                //random teleport for ranged mode
                for(int i = 0; i < 20; i++) {
                    Vec3d pos = ModRand.randVec().normalize().scale(7)
                            .add(this.getPositionVector());
                    boolean canSee = this.world.rayTraceBlocks(target.getPositionEyes(1), pos, false, true, false) == null;
                    Vec3d prevPos = this.getPositionVector();
                    this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 2.3F, 1.0F / (rand.nextFloat() * 0.4F + 0.6F));
                    if(!world.getBlockState(new BlockPos(pos.x, pos.y, pos.z)).causesSuffocation()) {
                        if (canSee && ModUtils.attemptTeleport(pos, this)) {
                            // ModUtils.lineCallback(prevPos, pos, 20, (particlePos, j) ->
                            //   actor.world.setEntityState(actor, ModUtils.SECOND_PARTICLE_BYTE));
                            break;
                        }
                    }

                }
            }
            this.destroyBlocksInSwing(new Vec3d(this.posX, this.posY, this.posZ), 1.0F);
        }, 3);

        if(needsAnimation) {
            addEvent(() -> {
                this.setFightMode(false);
                this.setTeleportAttack(false);
                this.setImmovable(false);
                if(this.isRangedMode()) {
                    this.attack_cooldown = 50;
                } else {
                    this.attack_cooldown = 10;
                }
            }, 5);
        }
    }


    private void destroyBlocksInSwing(Vec3d offset, double size) {
        AxisAlignedBB box = getEntityBoundingBox().grow(size, 0.1, size).offset(ModUtils.getRelativeOffset(this, new Vec3d(offset.x, 0.1, offset.z)));
        ModUtils.destroyBlocksInAABB(box, world, this);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateFight));
        data.addAnimationController(new AnimationController(this, "rage_controller", 0, this::predicateRage));
    }

    private <E extends IAnimatable> PlayState predicateRage(AnimationEvent<E> event) {
        if(this.isEnterRage()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_ENTER_RAGE_MODE));
            return PlayState.CONTINUE;
        }
        if(this.isEndRage()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_END_RAGE_MODE));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateFight(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isEndRage() && !this.isEnterRage()) {
            if(this.isRageLoop()) {
                event.getController().setAnimation(new AnimationBuilder().loop(ANIM_RAGE_LOOP));
                return PlayState.CONTINUE;
            }
            if(this.isSwingOne()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_ONE));
                return PlayState.CONTINUE;
            }
            if(this.isSwingTwo()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_TWO));
                return PlayState.CONTINUE;
            }
            if(this.isSwingThree()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_THREE));
                return PlayState.CONTINUE;
            }
            if(this.isSwingOne()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_ONE));
                return PlayState.CONTINUE;
            }
            if(this.isGroundStrike()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_GROUND_STRIKE));
                return PlayState.CONTINUE;
            }
            if(this.isSwordSlash()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWORD_SLASH));
                return PlayState.CONTINUE;
            }
            if(this.isTeleportAttack()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_TELEPORT));
                return PlayState.CONTINUE;
            }
            if(this.isSummonProjectiles()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SUMMON_PROJECTILES));
                return PlayState.CONTINUE;
            }
            if(this.isSelfAoe()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SELF_AOE));
                return PlayState.CONTINUE;
            }
            if(this.isUseSword()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_USE_SWORD));
                return PlayState.CONTINUE;
            }
            if(this.isDashSweep()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_DASH_SWEEP));
                return PlayState.CONTINUE;
            }
            if(this.isThrowSword()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_THROW_SWORD));
                return PlayState.CONTINUE;
            }
            if(this.isPierceStart()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PIERCE_START));
                return PlayState.CONTINUE;
            }
            if(this.isPierceFail()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PIERCE_FAIL));
                return PlayState.CONTINUE;
            }
            if(this.isPierceFinish()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PIERCE_FINISH));
                return PlayState.CONTINUE;
            }
            if(this.isCircleSwing()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CIRCLE_SWING));
                return PlayState.CONTINUE;
            }
            if(this.isCircleSwingContinue()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CICLE_SWING_CONTINUE));
                return PlayState.CONTINUE;
            }
            if(this.isBiteAttack()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_BITE));
                return PlayState.CONTINUE;
            }
            if(this.isSmashAttack()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SMASH));
                return PlayState.CONTINUE;
            }
            if(this.isFistBarrage()) {
                event.getController().setAnimation(new AnimationBuilder().loop(ANIM_FIST_BARRAGE));
                return PlayState.CONTINUE;
            }
            if(this.isChargeFist()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CHARGE_FIST));
                return PlayState.CONTINUE;
            }
            if(this.isPunch()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PUNCH));
                return PlayState.CONTINUE;
            }
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.performNTimes(25, (i) -> {
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + ModRand.getFloat(3.5F), this.posY + ModRand.getFloat(3.5F),
                        this.posZ + ModRand.getFloat(3.5F), 0, 0, 0);
            });
        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Main.proxy.spawnParticle(32, this.posX + pos.x, this.posY + 2, this.posZ + pos.z, 0,0.1,0);
            });
        }

        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Main.proxy.spawnParticle(34, this.posX + pos.x, this.posY + 2, this.posZ + pos.z, 0,0.1,0);
            });
        }

        if(id == ModUtils.FOURTH_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Main.proxy.spawnParticle(33, this.posX + pos.x, this.posY + 2, this.posZ + pos.z, 0,-0.25,0);
            });
        }

    }



    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        //teleports the boss to avoid attacks, differs both modes of the boss
        if(!this.isFightMode() && teleportCooldownTimer < 1) {
            if(this.getAttackTarget() != null) {
                this.teleportAbility(this.getAttackTarget(), true);
                this.teleportCooldownTimer = 10 * 20;
                return false;
            }
        }
        if(this.grabbedEntity != null || this.isEnterRage()) {
            return false;
        }

        //damage halves since the Dauntless is always in a defensive stance
        if(this.isEnragementMode()) {
            return super.attackEntityFrom(source, amount * 0.75F);
        }

        if(ModConfig.boss_cap_damage_enabled && amount > MobConfig.dauntless_damage_cap) {
            return super.attackEntityFrom(source, MobConfig.dauntless_damage_cap);
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected boolean canDropLoot() {
        return true;
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

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "dauntless");
    private static final ResourceLocation LOOT_MOB_NATURAL = new ResourceLocation(ModReference.MOD_ID, "dauntless_natural");
    @Override
    protected ResourceLocation getLootTable() {
        if(this.isSpawnedNaturally()) {
            return LOOT_MOB_NATURAL;
        } else {
            return LOOT_MOB;
        }
    }


    @Override
    public void removeTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        if(!this.isRangedMode()) {
            this.dataManager.set(LOOK, 0.0F);
            this.dataManager.set(LOOK, 0.0F);
        } else {
            this.dataManager.set(LOOK, clampedLook);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.DAUNTLESS_HURT;
    }

    @Override
    public void onDeath(DamageSource cause) {

        if(this.getSpawnLocation() != null && !world.isRemote && !this.isSpawnedNaturally()) {
            this.turnBossIntoSummonSpawner(this.getSpawnLocation().add(0, -6, 0));
            this.createCoinSpawns(this.getPositionVector(), ModRand.range(3, 6), ModRand.range(2, 4), 1);
        }
        super.onDeath(cause);
    }


    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }

    @Nullable
    @Override
    public SoundEvent getBossMusic() {
        return SoundsHandler.DAUNTLESS_TRACK;
    }
}
