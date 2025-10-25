package com.dungeon_additions.da.entity.gaelon_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.blocks.boss.BlockEnumBossSummonState;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.ai.gaelon_dungeon.EntityApathyrAttackAI;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.desert_dungeon.miniboss.ActionEveratorProjectileYellow;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.IMultiAction;
import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.*;
import com.dungeon_additions.da.entity.night_lich.EntityLichSpawn;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.tileEntity.TileEntityBossReSummon;
import com.dungeon_additions.da.entity.util.IEntitySound;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.*;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EntityApathyr extends EntityGaelonBase implements IAnimatable, IAnimationTickable, IAttack, IScreenShake, IEntitySound {

    private BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_6));
    public IMultiAction superDashAttack =  new ActionSuperDash(this);
    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_IDLE_LOWER = "idle_lower";

    private final String ANIM_IDLE_UPPER = "idle_upper";

    private final String ANIM_WALK_UPPER = "walk_upper";

    //Attacks
    private final String ANIM_SWING = "swing";
    private final String ANIM_SWING_CONNECT = "swing_connect";
    private final String ANIM_PUSH = "push";
    private final  String ANIM_KICK = "kick";
    private final String ANIM_JAB = "jab";
    private final String ANIM_UPPER_SWING = "upper_swing";

    //Attacks 75%
    private final String ANIM_GRAB_BEGIN = "grab_begin";
    private final String ANIM_GRAB_FINISH = "grab_finish";
    private final String ANIM_GRAB_CONTINUE = "grab_continue";
    private final String ANIM_JUMP_ATTACK = "jump_attack";
    private final String ANIM_ARM_SWIPE = "arm_swipe";
    //Attacks 50%
    private final String ANIM_SUPER_DASH = "super_dash";
    private final String ANIM_CRYSTAL_DOMAIN = "cast_storm";
    private final String ANIM_CAST_FAST_PROJECTILE = "cast_fast";
    private final String ANIM_CAST_REGULAR = "cast_spell";

    //Attack Secondary
    private final String ANIM_PUSH_SECONDARY = "push_two";
    private final String ANIM_KICK_SECONDARY = "kick_two";
    private final String ANIM_JAB_SECONDARY = "jab_two";
    // 75%
    private final String ANIM_JUMP_ATTACK_SECONDARY = "jump_attack_two";
    private final String ANIM_JUMP_AWAY = "jump_away";

    //Attacks Tertiary
    private final String ANIM_SWING_THREE = "swing_three";
    private final String ANIM_JAB_THREE = "jab_three";
    private final String ANIM_UPPER_SWING_THREE = "upper_swing_three";
    //75%
    private final String ANIM_CIRCLE_ATTACK = "circle_swing";
    private final String ANIM_JUMP_AWAY_THREE = "jump_away_three";
    //50%

    //States
    private final String ANIM_IDLE_STATE = "idle_state";
    private final String ANIM_IDLE_AWAKE = "idle_awake";
    private final String ANIM_DEATH_ANIMATION = "death";

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;
    private boolean currentlyInIFrame = false;
    private int shakeTime = 0;
    public List<WeakReference<Entity>> current_mobs = Lists.newArrayList();
    public List<WeakReference<Entity>> current_mobs_golden = Lists.newArrayList();
    private static final DataParameter<Boolean> SWING = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_CONNECT = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JAB = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PUSH = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> KICK = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> UPPER_SWING = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GRAB_BEGIN = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GRAB_CONTINUE = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GRAB_FINISH = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_ATTACK = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_AWAY = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CIRCLE_SWING = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUPER_DASH = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TELEPORT_GHOST = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CRYSTAL_DOMAIN = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_FAST = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_SPELL = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ARM_SWIPE = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SECONDARY = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TERTIARY = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TRANSITION_TICK = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHIELDED = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IDLE_STATE = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IDLE_AWAKE = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEATH_STATE = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPAWNING_WAVES = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> STAT_LINE = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.FLOAT);
    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BLOCK_POS);
    public static DataParameter<Boolean> SET_SPAWN_LOC = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> USED_GOLDEN_HEART = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STEP_SHAKE = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAD_PREVIOUS_TARGET = EntityDataManager.createKey(EntityApathyr.class, DataSerializers.BOOLEAN);

    private void setSwingAttack(boolean value) {this.dataManager.set(SWING, Boolean.valueOf(value));}
    private boolean isSwingAttack() {return this.dataManager.get(SWING);}
    private void setSwingConnect(boolean value) {this.dataManager.set(SWING_CONNECT, Boolean.valueOf(value));}
    private boolean isSwingConnect() {return this.dataManager.get(SWING_CONNECT);}
    private void setJab(boolean value) {this.dataManager.set(JAB, Boolean.valueOf(value));}
    private boolean isJab() {return this.dataManager.get(JAB);}
    private void setPush(boolean value) {this.dataManager.set(PUSH, Boolean.valueOf(value));}
    private boolean isPush() {return this.dataManager.get(PUSH);}
    private void setKick(boolean value) {this.dataManager.set(KICK, Boolean.valueOf(value));}
    private boolean isKick() {return this.dataManager.get(KICK);}
    private void setUpperSwing(boolean value) {this.dataManager.set(UPPER_SWING, Boolean.valueOf(value));}
    private boolean isUpperSwing() {return this.dataManager.get(UPPER_SWING);}
    private void setSecondary(boolean value) {this.dataManager.set(SECONDARY, Boolean.valueOf(value));}
    private boolean isSecondary() {return this.dataManager.get(SECONDARY);}
    private void setTertiary(boolean value) {this.dataManager.set(TERTIARY, Boolean.valueOf(value));}
    private boolean isTertiary() {return this.dataManager.get(TERTIARY);}
    private void setTransitionTick(boolean value) {this.dataManager.set(TRANSITION_TICK, Boolean.valueOf(value));}
    private boolean isTransitionTick() {return this.dataManager.get(TRANSITION_TICK);}
    private void setGrabBegin(boolean value) {this.dataManager.set(GRAB_BEGIN, Boolean.valueOf(value));}
    private boolean isGrabBegin() {return this.dataManager.get(GRAB_BEGIN);}
    private void setGrabContinue(boolean value) {this.dataManager.set(GRAB_CONTINUE, Boolean.valueOf(value));}
    private boolean isGrabContinue() {return this.dataManager.get(GRAB_CONTINUE);}
    private void setGrabFinish(boolean value) {this.dataManager.set(GRAB_FINISH, Boolean.valueOf(value));}
    private boolean isGrabFinish() {return this.dataManager.get(GRAB_FINISH);}
    private void setJumpAttack(boolean value) {this.dataManager.set(JUMP_ATTACK, Boolean.valueOf(value));}
    private boolean isJumpAttack() {return this.dataManager.get(JUMP_ATTACK);}
    private void setJumpAway(boolean value) {this.dataManager.set(JUMP_AWAY, Boolean.valueOf(value));}
    public boolean isJumpAway() {return this.dataManager.get(JUMP_AWAY);}
    private void setCircleSwing(boolean value) {this.dataManager.set(CIRCLE_SWING, Boolean.valueOf(value));}
    private boolean isCircleSwing() {return this.dataManager.get(CIRCLE_SWING);}
    private void setSuperDash(boolean value) {this.dataManager.set(SUPER_DASH, Boolean.valueOf(value));}
    private boolean isSuperDash() {return this.dataManager.get(SUPER_DASH);}
    private void setTeleportGhost(boolean value) {this.dataManager.set(TELEPORT_GHOST, Boolean.valueOf(value));}
    private boolean isTeleportGhost() {return this.dataManager.get(TELEPORT_GHOST);}
    private void setCrystalDomain(boolean value) {this.dataManager.set(CRYSTAL_DOMAIN, Boolean.valueOf(value));}
    private boolean isCrystalDomain() {return this.dataManager.get(CRYSTAL_DOMAIN);}
    private void setCastFast(boolean value) {this.dataManager.set(CAST_FAST, Boolean.valueOf(value));}
    private boolean isCastFast() {return this.dataManager.get(CAST_FAST);}
    private void setCastSpell(boolean value) {this.dataManager.set(CAST_SPELL, Boolean.valueOf(value));}
    private boolean isCastSpell() {return this.dataManager.get(CAST_SPELL);}
    private void setDeathState(boolean value) {this.dataManager.set(DEATH_STATE, Boolean.valueOf(value));}
    public boolean isDeathState() {return this.dataManager.get(DEATH_STATE);}
    private void setArmSwipe(boolean value) {this.dataManager.set(ARM_SWIPE, Boolean.valueOf(value));}
    private boolean isArmSwipe() {return this.dataManager.get(ARM_SWIPE);}
    private void setShielded(boolean value) {this.dataManager.set(SHIELDED, Boolean.valueOf(value));}
    public boolean isShielded() {return this.dataManager.get(SHIELDED);}
    private void setUsedGoldenHeart(boolean value) {this.dataManager.set(USED_GOLDEN_HEART, Boolean.valueOf(value));}
    public boolean isUsedGoldenHeart() {return this.dataManager.get(USED_GOLDEN_HEART);}
    private void setIdleState(boolean value) {this.dataManager.set(IDLE_STATE, Boolean.valueOf(value));}
    public boolean isIdleState() {return this.dataManager.get(IDLE_STATE);}
    private void setIdleAwake(boolean value) {this.dataManager.set(IDLE_AWAKE, Boolean.valueOf(value));}
    public boolean isIdleAwake() {return this.dataManager.get(IDLE_AWAKE);}
    public boolean isStepShake() {return this.dataManager.get(STEP_SHAKE);}
    private void setStepShake(boolean value) {this.dataManager.set(STEP_SHAKE, Boolean.valueOf(value));}
    private void setSpawningWaves(boolean value) {this.dataManager.set(SPAWNING_WAVES, Boolean.valueOf(value));}
    public boolean isSpawningWaves() {return this.dataManager.get(SPAWNING_WAVES);}

    public void setStateLine(float value) {
        this.dataManager.set(STAT_LINE, Float.valueOf(value));
    }
    public float getStatLine() {
        return this.dataManager.get(STAT_LINE);
    }
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
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}

    private EntityLivingBase grabbedEntity;
    private boolean grabDetection = false;

    public EntityApathyr(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.2F, 3.5F);
        this.experienceValue = 300;
        this.iAmBossMob = true;
        this.setIdleState(true);
        this.bossInfo.setVisible(false);
    }

    public EntityApathyr(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 3.5F);
        this.experienceValue = 300;
        this.iAmBossMob = true;
        this.setIdleState(true);
        this.bossInfo.setVisible(false);
    }

    public EntityApathyr(World worldIn, int timesUsed, BlockPos pos) {
        super(worldIn);
        this.setSize(1.2F, 3.5F);
        this.timesUsed = timesUsed;
        this.timesUsed++;
        this.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        this.doBossReSummonScaling();
        this.experienceValue = 300;
        this.iAmBossMob = true;
        this.bossInfo.setVisible(false);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Swing", this.isSwingAttack());
        nbt.setBoolean("Swing_Connect", this.isSwingConnect());
        nbt.setBoolean("Jab", this.isJab());
        nbt.setBoolean("Push", this.isPush());
        nbt.setBoolean("Kick", this.isKick());
        nbt.setBoolean("Upper_Swing", this.isUpperSwing());
        nbt.setBoolean("Secondary", this.isSecondary());
        nbt.setBoolean("Tertiary", this.isTertiary());
        nbt.setBoolean("Transition_Tick", this.isTransitionTick());
        nbt.setBoolean("Grab_Begin", this.isGrabBegin());
        nbt.setBoolean("Grab_Continue", this.isGrabContinue());
        nbt.setBoolean("Grab_Finish", this.isGrabFinish());
        nbt.setBoolean("Circle_Swing", this.isCircleSwing());
        nbt.setBoolean("Jump_Away", this.isJumpAway());
        nbt.setBoolean("Jump_Attack", this.isJumpAttack());
        nbt.setBoolean("Super_Dash", this.isSuperDash());
        nbt.setBoolean("Teleport_Ghost", this.isTeleportGhost());
        nbt.setBoolean("Crystal_Domain", this.isCrystalDomain());
        nbt.setBoolean("Cast_Fast", this.isCastFast());
        nbt.setBoolean("Cast_Spell", this.isCastSpell());
        nbt.setBoolean("Arm_Swipe", this.isArmSwipe());
        nbt.setBoolean("Shielded", this.isShielded());
        nbt.setBoolean("Shaking", this.isShaking());
        nbt.setBoolean("Death_State", this.isDeathState());
        nbt.setBoolean("Step_Shake", this.isStepShake());
        nbt.setBoolean("Used_Golden_Heart", this.isUsedGoldenHeart());
        nbt.setBoolean("Idle_State", this.isIdleState());
        nbt.setBoolean("Idle_Awake", this.isIdleAwake());
        nbt.setBoolean("Spawning_Waves", this.isSpawningWaves());
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
        this.setSwingAttack(nbt.getBoolean("Swing"));
        this.setSwingConnect(nbt.getBoolean("Swing_Connect"));
        this.setJab(nbt.getBoolean("Jab"));
        this.setPush(nbt.getBoolean("Push"));
        this.setKick(nbt.getBoolean("Kick"));
        this.setUpperSwing(nbt.getBoolean("Upper_Swing"));
        this.setSecondary(nbt.getBoolean("Secondary"));
        this.setTertiary(nbt.getBoolean("Tertiary"));
        this.setTransitionTick(nbt.getBoolean("Transition_Tick"));
        this.setGrabBegin(nbt.getBoolean("Grab_Begin"));
        this.setGrabContinue(nbt.getBoolean("Grab_Continue"));
        this.setGrabFinish(nbt.getBoolean("Grab_Finish"));
        this.setCircleSwing(nbt.getBoolean("Circle_Swing"));
        this.setJumpAway(nbt.getBoolean("Jump_Away"));
        this.setJumpAttack(nbt.getBoolean("Jump_Attack"));
        this.setSuperDash(nbt.getBoolean("Super_Dash"));
        this.setUsedGoldenHeart(nbt.getBoolean("Used_Golden_Heart"));
        this.setDeathState(nbt.getBoolean("Death_State"));
        this.setTeleportGhost(nbt.getBoolean("Teleport_Ghost"));
        this.setCrystalDomain(nbt.getBoolean("Crystal_Domain"));
        this.setCastFast(nbt.getBoolean("Cast_Fast"));
        this.setCastSpell(nbt.getBoolean("Cast_Spell"));
        this.setArmSwipe(nbt.getBoolean("Arm_Swipe"));
        this.setSpawningWaves(nbt.getBoolean("Spawning_Waves"));
        this.setShielded(nbt.getBoolean("Shielded"));
        this.setStepShake(nbt.getBoolean("Step_Shake"));
        this.setIdleAwake(nbt.getBoolean("Idle_Awake"));
        this.setIdleState(nbt.getBoolean("Idle_State"));
        this.setHadPreviousTarget(nbt.getBoolean("Had_Target"));
        this.setShaking(nbt.getBoolean("Shaking"));
        this.dataManager.set(SET_SPAWN_LOC, nbt.getBoolean("Set_Spawn_Loc"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.setStateLine(nbt.getFloat("Stat_Line"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(SWING, Boolean.valueOf(false));
        this.dataManager.register(SWING_CONNECT, Boolean.valueOf(false));
        this.dataManager.register(JAB, Boolean.valueOf(false));
        this.dataManager.register(PUSH, Boolean.valueOf(false));
        this.dataManager.register(KICK, Boolean.valueOf(false));
        this.dataManager.register(UPPER_SWING, Boolean.valueOf(false));
        this.dataManager.register(SECONDARY, Boolean.valueOf(false));
        this.dataManager.register(TERTIARY, Boolean.valueOf(false));
        this.dataManager.register(TRANSITION_TICK, Boolean.valueOf(false));
        this.dataManager.register(GRAB_BEGIN, Boolean.valueOf(false));
        this.dataManager.register(GRAB_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(GRAB_FINISH, Boolean.valueOf(false));
        this.dataManager.register(CIRCLE_SWING, Boolean.valueOf(false));
        this.dataManager.register(JUMP_AWAY, Boolean.valueOf(false));
        this.dataManager.register(JUMP_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUPER_DASH, Boolean.valueOf(false));
        this.dataManager.register(DEATH_STATE, Boolean.valueOf(false));
        this.dataManager.register(TELEPORT_GHOST, Boolean.valueOf(false));
        this.dataManager.register(CRYSTAL_DOMAIN, Boolean.valueOf(false));
        this.dataManager.register(CAST_FAST, Boolean.valueOf(false));
        this.dataManager.register(CAST_SPELL, Boolean.valueOf(false));
        this.dataManager.register(ARM_SWIPE, Boolean.valueOf(false));
        this.dataManager.register(USED_GOLDEN_HEART, Boolean.valueOf(false));
        this.dataManager.register(SHIELDED, Boolean.valueOf(false));
        this.dataManager.register(SET_SPAWN_LOC, Boolean.valueOf(false));
        this.dataManager.register(HAD_PREVIOUS_TARGET, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        this.dataManager.register(SPAWNING_WAVES, Boolean.valueOf(false));
        this.dataManager.register(STEP_SHAKE, Boolean.valueOf(false));
        this.dataManager.register(IDLE_AWAKE, Boolean.valueOf(false));
        this.dataManager.register(IDLE_STATE, Boolean.valueOf(false));
        //
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
        this.dataManager.register(STAT_LINE,  0.75F);
        super.entityInit();
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(this.isIdleState()) {
            if (stack.getItem() == ModItems.APATHYR_HEART) {
                int playersNearby = ServerScaleUtil.getPlayers(this, world);
            if(playersNearby == 0 && !world.isRemote || MobConfig.apathyr_disable_solo_mode && !world.isRemote) {
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    if (!world.isRemote) {
                        this.setIdleState(false);
                        this.beginBossFight();
                    }
                }  else {
                if(!world.isRemote) {
                    player.sendStatusMessage(new TextComponentTranslation("da.solo_mode", new Object[0]), false);
                }
            }
            //Allows the player to fight the Apathyr with friends!
            } else if (stack.getItem() == ModItems.GOLDEN_APATHYR_HEART) {
                if(!world.isRemote) {
                    this.setUsedGoldenHeart(true);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    if (!world.isRemote) {
                        this.setIdleState(false);
                        this.beginBossFight();
                    }
                }
            } else if(player.getHeldItem(hand).isEmpty() && !world.isRemote) {
                player.sendStatusMessage(new TextComponentTranslation("da.heart_required", new Object[0]), true);
            }
        }
        return super.processInteract(player, hand);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(38D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.apathyr_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.apathyr_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.apathyr_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.apathyr_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityApathyrAttackAI<>(this, 1.0D, 10, 30, 0.5F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityDarkBase>(this, EntityDarkBase.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    /**
     * This ensures that active mobs are still within a distance and are still alive to be accounted for
     */
    private void clearInvalidEntities() {
        current_mobs = current_mobs.stream().filter(ref -> ref.get() != null && ref.get().getDistance(this) <= 45 && ref.get().isEntityAlive()).collect(Collectors.toList());
    }

    private void clearInvalidEntitiesGolden() {
        current_mobs_golden = current_mobs_golden.stream().filter(ref -> ref.get() != null && ref.get().getDistance(this) <= 45 && ref.get().isEntityAlive()).collect(Collectors.toList());
    }

    private void killCurrentSpawns() {
        if(!current_mobs.isEmpty()) {
            current_mobs.forEach(ref -> Objects.requireNonNull(ref.get()).setDead());
        }
        if(!current_mobs_golden.isEmpty()) {
            current_mobs_golden.forEach(ref -> Objects.requireNonNull(ref.get()).setDead());
        }
    }

    private int waveCount = 0;

    private int coolDownTime = 0;
    public boolean flagForStrafing = false;
    private boolean startCooldown = false;
    protected boolean hasLaunchedPhaseOne = false;
    protected boolean hasLaunchedPhaseTwo = false;
    protected boolean hasLaunchedPhaseThree = false;

    private int attackExhaustion = 0;

    private float randomTurn = ModRand.range(1, 360);

    private boolean performSuperDash = false;

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!world.isRemote && performSuperDash) {
            superDashAttack.update();
        }
    }

    private int spawnDelay = 0;
    private int crystal_domain_cooldown = 20 * 30;

    private double apathyr_aggrivation = 0;
    private boolean canAccessCombos = false;

    private boolean setTooDoDomain = false;

    private boolean test;

    @Override
    public void onUpdate() {
        super.onUpdate();
        double healthFac = this.getHealth() / this.getMaxHealth();
        if(this.bossInfo != null) {
            this.bossInfo.setPercent((float) healthFac);
        }
        this.shakeTime--;
        this.spawnDelay--;
        this.crystal_domain_cooldown--;

        if(this.isIdleState()) {
            this.rotationYaw = randomTurn;
            this.rotationYawHead = randomTurn;
            this.setImmovable(true);

            if(this.isShielded()) {
                this.setShielded(false);
            }

            //sets spawn location permanantly for this boss
            if(this.ticksExisted == 100 && !this.isSetSpawnLoc()) {
                this.setSpawnLocation(new BlockPos(this.getPosition()));
                this.setSetSpawnLoc(true);
            }
        } else {

            if(this.isDeathState()) {
                this.setImmovable(true);
                this.motionX = 0;
                this.motionZ = 0;
                this.motionY = 0;
            }

            this.coolDownTime--;
            //updates the mobs currently summoned by the Apathyr
            this.clearInvalidEntities();
            if(this.isUsedGoldenHeart()) {
                this.clearInvalidEntitiesGolden();
            }

            //if combos aren't played and enough attacks reach exhaustion, start the strafing
            if (attackExhaustion > 3) {
                this.flagForStrafing = true;
                this.attackExhaustion = 0;
            }

            //handles a random time for strafing
            if (flagForStrafing && !startCooldown) {
                if(healthFac < 0.5) {
                    coolDownTime = ModRand.range(10, 30);
                } else {
                    coolDownTime = ModRand.range(35, 60);
                }
                this.startCooldown = true;
            }

            //ends strafing
            if (startCooldown && coolDownTime <= 1) {
                this.flagForStrafing = false;
                this.startCooldown = false;
            }

            //Teleports boss if too far from its arena
            if (this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
                Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());

                double distSq = this.getDistanceSq(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                double distance = Math.sqrt(distSq);
                //This basically makes it so the Lamentor will be teleported if they are too far away from the Arena
                if (!world.isRemote) {
                    if (distance > 30) {
                        this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                    }
                }
            }


            if (!world.isRemote) {
                EntityLivingBase target = this.getAttackTarget();

                if(target != null) {
                    double targetHealth = target.getHealth() / target.getMaxHealth();

                    //resets boss if other players are nearby
                    if(!MobConfig.apathyr_disable_solo_mode && !this.isIdleState() && !this.isUsedGoldenHeart()) {
                        int playersNearby = ServerScaleUtil.getPlayers(this, world);

                        if(playersNearby != 0) {

                            if(ticksExisted % 80 == 0) {
                                if(!world.isRemote) {
                                    if(target instanceof EntityPlayer) {
                                        ((EntityPlayer)target).sendStatusMessage(new TextComponentTranslation("da.other_players", new Object[0]), false);
                                    }
                                }
                            }

                            if (targetTrackingTimer > 0) {
                                targetTrackingTimer--;
                            }

                            if (targetTrackingTimer < 1) {
                                if (this.timesUsed != 0) {
                                    this.timesUsed--;
                                    turnBossIntoSummonSpawner(this.getSpawnLocation());
                                    this.setDead();
                                } else {
                                    this.resetBoss();
                                }
                            }
                        }
                    }


                    //only adds to aggrivation when taking damage
                    if(this.hurtTime  == 1) {
                        apathyr_aggrivation = (targetHealth * 0.1);
                    }

                    if(healthFac >= 0.5) {
                        this.canAccessCombos = apathyr_aggrivation > 2;
                    } else{
                        this.canAccessCombos = apathyr_aggrivation > 1.5;
                    }

                    //separate challenge for multiplayer
                    if(this.isUsedGoldenHeart() && this.ticksExisted % 1200 == 0 && this.timesUsed == 0 && healthFac > 0.53) {
                        //only passes if more than one player fighting the boss
                        int playersNearby = ServerScaleUtil.getPlayers(this, world);
                        if(playersNearby != 0 && current_mobs_golden.size() < playersNearby + 3) {
                            //Spawns Cursed Revenants
                            for(int i= 0; i < playersNearby; i++) {
                                if(world.rand.nextInt(13) == 0) {
                                    this.summonEliteWaveEnemy(true);
                                } else {
                                    this.summonRegularWaveEnemy(true);
                                }
                            }
                        }
                    }
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
                                if (this.timesUsed != 0) {
                                    this.timesUsed--;
                                    turnBossIntoSummonSpawner(this.getSpawnLocation());
                                    this.setDead();
                                } else {
                                    this.resetBoss();
                                }
                            }
                        }
                    }
                }


                if (grabDetection && grabbedEntity == null) {
                    List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                            this.getEntityBoundingBox().offset(ModUtils.getRelativeOffset(this, new Vec3d(1.5, -0.5, 0))).grow(0.5D, 3.5D, 0.5D),
                            e -> !e.getIsInvulnerable());

                    if (!nearbyEntities.isEmpty()) {
                        for (EntityLivingBase base : nearbyEntities) {
                            if (!(base instanceof EntityGaelonBase)) {
                                grabbedEntity = base;
                                this.playSound(SoundsHandler.KING_GRAB_SUCCESS, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                            }
                        }
                    }
                } else if (grabbedEntity != null) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 0.4, 0)));
                    grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                    grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);
                }


                //Phases

                //Phase One
                //Replaces the 4 lower Pillar Blocks with lights too ones with eyes that send particles
                //tracking the bosses movement
                if (healthFac < 0.75 && !this.hasLaunchedPhaseOne) {
                    for (int i = 0; i < 5; i++) {
                        AxisAlignedBB box = getEntityBoundingBox().grow(40, 4, 40);
                        BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.DARK_GLOW_LIT_PILLAR.getDefaultState());
                        if (setTooPos != null) {
                            world.setBlockState(setTooPos, ModBlocks.DARK_GLOW_EYE_PILLAR.getDefaultState());

                            hasLaunchedPhaseOne = true;
                            this.setShielded(true);
                        }
                    }
                } else if (this.hasLaunchedPhaseOne && this.getStatLine() == 0.75) {
                    AxisAlignedBB box = getEntityBoundingBox().grow(40, 4, 40);
                    BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.DARK_GLOW_EYE_PILLAR.getDefaultState());
                    if (setTooPos == null) {
                        this.setStateLine((float) 0.5);
                        this.setShielded(false);
                    }
                }

                //Phase Two
                //Summons 3 Waves of mobs that the player must defeat
                // Boss Teleports to Spawnpoint or remains in spot of activation
                if (!this.hasLaunchedPhaseTwo && healthFac < 0.5) {
                                if(!this.isFightMode() && !this.isSpawningWaves()) {
                                    if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
                                        this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                                        this.setImmovable(false);
                                        this.setPosition(this.getSpawnLocation().getX() + 0.5, this.getSpawnLocation().getY(), this.getSpawnLocation().getZ() + 0.5);
                                    }
                                    this.setShielded(true);
                                    this.setImmovable(true);
                                    this.setSpawningWaves(true);
                                    this.coolDownTime = 100;
                                    this.hasLaunchedPhaseTwo = true;
                                }
                } else if (this.hasLaunchedPhaseTwo && !this.isSpawningWaves() && this.spawnDelay == 0 && this.waveCount == 3) {
                        this.setShielded(false);
                        this.setImmovable(false);
                    this.setStateLine(0.25F);
                }

                //wave handler
                if(this.isSpawningWaves()) {
                    if(ticksExisted % 150 == 0 && spawnDelay < 0) {
                        if(current_mobs.isEmpty()) {
                            if(waveCount == 0) {
                                for(int i = 0; i <= 2; i ++) {
                                    summonRegularWaveEnemy(false);
                                }
                                waveCount = 1;
                                this.spawnDelay = 200;
                            } else if (waveCount == 1) {
                                for(int i = 0; i <= 1; i ++) {
                                    summonRegularWaveEnemy(false);
                                }
                                summonEliteWaveEnemy(false);
                                waveCount = 2;
                                this.spawnDelay = 200;
                            } else if (waveCount == 2) {
                                for(int i = 0; i <= 2; i ++) {
                                    summonRegularWaveEnemy(false);
                                }
                                summonEliteWaveEnemy(false);
                                waveCount = 3;
                                this.spawnDelay = 200;

                            } else if (waveCount == 3) {
                                this.spawnDelay = 200;
                                this.setSpawningWaves(false);
                                this.setTooDoDomain = true;
                            }
                        }
                    }
                }

                //Phase Three
                //Summons Entity Eyes in the upper pillars
                // Summons Blocks in the original 4 pillars
                if (!this.hasLaunchedPhaseThree && healthFac < 0.25) {
                    for (int i = 0; i <= 4; i++) {
                        AxisAlignedBB box = getEntityBoundingBox().grow(40, 4, 40);
                        BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.DARK_GLOW_LIT_PILLAR.getDefaultState());
                        if (setTooPos != null) {
                            world.setBlockState(setTooPos, ModBlocks.DARK_GLOW_EYE_PILLAR.getDefaultState());

                            hasLaunchedPhaseThree = true;
                            this.setShielded(true);
                        }
                        AxisAlignedBB box2 = getEntityBoundingBox().grow(40, 5, 40).offset(0, 10, 0);
                        BlockPos setTooPos2 = ModUtils.searchForBlocks(box2, world, this, ModBlocks.DARK_GLOW_LIT_PILLAR.getDefaultState());
                        if (setTooPos2 != null) {
                            EntityApathyrEye eye = new EntityApathyrEye(world, this);
                            world.setBlockToAir(setTooPos2);
                            eye.setPosition(setTooPos2.getX() + 0.5, setTooPos2.getY(), setTooPos2.getZ() + 0.5);
                            world.spawnEntity(eye);
                        }
                    }
                } else if (this.hasLaunchedPhaseThree && this.getStatLine() == 0.25 && apathyrEyes().isEmpty()) {
                    AxisAlignedBB box = getEntityBoundingBox().grow(40, 4, 40);
                    BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.DARK_GLOW_EYE_PILLAR.getDefaultState());
                    if (setTooPos == null) {
                        this.setStateLine((float) 0);
                        this.setShielded(false);
                    }
                }
            }
        }
    }

    //Wave One - Three Re-Animates
    //Wave Two - Two Re-Animates / One Golem
    //Wave Three - Three Re-Animates / One Golem

    private void summonRegularWaveEnemy(boolean isGolden) {
        EntityLichSpawn mob = new EntityLichSpawn(world, this, false, isGolden);
        Vec3d relPos;
        if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
            Vec3d pos = new Vec3d(this.getSpawnLocation().getX() + 0.5, this.getSpawnLocation().getY(), this.getSpawnLocation().getZ() + 0.5);
            relPos = pos.add(ModRand.range(-1, 1) + ModRand.range(3, 7), 0, (ModRand.range(-1, 1) + ModRand.range(3, 7)));
        } else {
            relPos = this.getPositionVector().add(ModRand.range(-1, 1) + ModRand.range(3, 7), 0, (ModRand.range(-1, 1) + ModRand.range(3, 7)));
        }
        int yFor = ModUtils.getSurfaceHeightZeroReturn(world, new BlockPos(relPos.x, relPos.y, relPos.z), (int) relPos.y - 8, (int) relPos.y + 3);
        if(yFor != 0) {
            mob.setPosition(relPos.x + 0.5, yFor + 1, relPos.z + 0.5);
        } else {
            mob.setPosition(relPos.x + 0.5, this.posY, relPos.z + 0.5);
        }
        world.spawnEntity(mob);
    }

    private void summonEliteWaveEnemy(boolean isGolden) {
        EntityLichSpawn mob = new EntityLichSpawn(world, this, true, isGolden);
        Vec3d relPos;
        if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
            Vec3d pos = new Vec3d(this.getSpawnLocation().getX() + 0.5, this.getSpawnLocation().getY(), this.getSpawnLocation().getZ() + 0.5);
            relPos = pos.add(ModRand.range(-1, 1) + ModRand.range(3, 7), 0, (ModRand.range(-1, 1) + ModRand.range(3, 7)));
        } else {
            relPos = this.getPositionVector().add(ModRand.range(-1, 1) + ModRand.range(3, 7), 0, (ModRand.range(-1, 1) + ModRand.range(3, 7)));
        }
        int yFor = ModUtils.getSurfaceHeightZeroReturn(world, new BlockPos(relPos.x, relPos.y, relPos.z), (int) relPos.y - 8, (int) relPos.y + 3);
        if(yFor != 0) {
            mob.setPosition(relPos.x + 0.5, yFor + 1, relPos.z + 0.5);
        } else {
            mob.setPosition(relPos.x + 0.5, this.posY, relPos.z + 0.5);
        }
        world.spawnEntity(mob);
    }

    // resets boss to full health and in an idle state
    //spawns chest with Heart of Apathyr

    private static final ResourceLocation LOO_RESET = new ResourceLocation(ModReference.MOD_ID, "gaelon_sanctuary_reset");
    private static final ResourceLocation GOLDEN_LOO_RESET = new ResourceLocation(ModReference.MOD_ID, "golden_gaelon_sanctuary_reset");

    private void resetBoss() {
        //MAKE SURE TO SET TO IDLE STATE
        if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
            if(this.isUsedGoldenHeart()) {
                Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());
                this.setPosition(SpawnLoc.x + 0.5, SpawnLoc.y, SpawnLoc.z + 0.5);
                world.setBlockState(this.getSpawnLocation().add(3, 0, 0), Blocks.CHEST.getDefaultState());
                TileEntity te = world.getTileEntity(this.getSpawnLocation().add(3,0, 0));
                if(te instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) te;
                    chest.setLootTable(GOLDEN_LOO_RESET, rand.nextLong());
                }
                //reset this just in case
                this.setUsedGoldenHeart(false);
            } else {
                Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());
                this.setPosition(SpawnLoc.x + 0.5, SpawnLoc.y, SpawnLoc.z + 0.5);
                world.setBlockState(this.getSpawnLocation().add(3, 0, 0), Blocks.CHEST.getDefaultState());
                TileEntity te = world.getTileEntity(this.getSpawnLocation().add(3,0, 0));
                if(te instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) te;
                    chest.setLootTable(LOO_RESET, rand.nextLong());
                }
            }
        }
        this.clearEvents();
        this.setHealth((float) this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
        addEvent(()-> {
            this.hasLaunchedPhaseOne = false;
            this.hasLaunchedPhaseTwo = false;
            this.hasLaunchedPhaseThree = false;
            this.setStateLine(0.75F);
            this.killCurrentSpawns();
        }, 5);
        this.waveCount = 0;
        this.grabbedEntity = null;
        this.bossInfo.setVisible(false);
        this.setSpawningWaves(false);
        this.setIdleState(true);
        this.setHadPreviousTarget(false);
        targetTrackingTimer = ModConfig.boss_reset_timer * 20;
        this.setShielded(false);

    }

    protected void turnBossIntoSummonSpawner(BlockPos pos) {
        if(ModConfig.boss_resummon_enabled) {
            if (this.timesUsed <= ModConfig.boss_resummon_max_uses && !world.isRemote) {
                world.setBlockState(pos, ModBlocks.BOSS_RESUMMON_BLOCK.getDefaultState());
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileEntityBossReSummon) {
                    TileEntityBossReSummon boss_spawner = ((TileEntityBossReSummon) te);
                    boss_spawner.setState(BlockEnumBossSummonState.INACTIVE, this.timesUsed, "apathyr");
                }
            }
        }
    }

    public void onSummonViaBlock(BlockPos posFrom) {
        this.setPosition(posFrom.getX() + 0.5, posFrom.getY(), posFrom.getZ() + 0.5);
        this.setSpawnLocation(posFrom);
        this.setSetSpawnLoc(true);
        this.beginBossFight();
    }

    private void beginBossFight() {
        this.setImmovable(true);
        this.lockLook = true;
        this.setIdleState(false);
        this.setIdleAwake(true);
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_AWAKEN, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_STEP, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 30);
        addEvent(()-> this.playSound(SoundsHandler.OBSIDILITH_SHIELD, 0.7f, 0.4f / (rand.nextFloat() * 0.4f + 0.2f)), 100);
        addEvent(()-> this.playSound(SoundsHandler.OBSIDILITH_SHIELD, 0.7f, 0.5f / (rand.nextFloat() * 0.4f + 0.2f)), 110);
        addEvent(()-> this.playSound(SoundsHandler.OBSIDILITH_SHIELD, 0.7f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f)), 120);
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_SWING, 1.25f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 170);
        addEvent(()-> {
            this.setIdleAwake(false);
            this.setImmovable(false);
            this.lockLook = false;
            this.bossInfo.setVisible(true);
            this.playMusic(this);

        }, 220);
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double healthFac = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && coolDownTime <= 0 && !this.isIdleState() && !this.isIdleAwake() && !this.isDeathState()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(upper_swing, jab_attack, push, swing, cast_arm_swipe, grab_attack, jump_attack, super_dash, cast_fast_projectile, teleport_ghost, cast_storm, cast_spike_spell));
            this.setSecondary(false);
            this.setTertiary(false);
            double[] weights = {
                    (upper_swing != prevAttack) ? distance * 0.02 : 0, //Upper Swing        //ACTIVE
                    (jab_attack != prevAttack && !this.isSpawningWaves()) ? 1/distance : 0, //Jab or Thrust Attack
                    (push != prevAttack && !this.isSpawningWaves()) ? 1/distance : 0, // Push or Shove Attack
                    (swing != prevAttack && !this.isSpawningWaves()) ? 1/distance : 0, // Swing can swing twice
                    (cast_arm_swipe != prevAttack && distance > 10) ? distance * 0.02 : 0, //Cast Projectile Attack        //ACTIVE
                    //75% Attacks
                    (grab_attack != prevAttack && healthFac < 0.75 && !this.isSpawningWaves() && distance > 5) ? 1/distance : 0, //Grab attack that locks the player if caught
                    (jump_attack != prevAttack && healthFac < 0.75 && distance > 8 && !this.isSpawningWaves()) ? 1/distance : 0, //Jump Attack, summons spikes at 25% health
                    //50% Attacks
                    (super_dash != prevAttack && healthFac < 0.5 && distance > 5 && !this.isSpawningWaves()) ? distance * 0.02 : 0, //Super Dash Attack
                    (cast_fast_projectile != prevAttack && healthFac < 0.55) ? distance * 0.02 : 0, //Fast Projectile Cast        //ACTIVE
                    (teleport_ghost != prevAttack && distance < 8 && healthFac < 0.5 && !this.isSpawningWaves()) ? 1/distance : 0, //Teleport Ghost Attack
                    (setTooDoDomain) ? 100 : (cast_storm != prevAttack && healthFac < 0.5 && !this.isSpawningWaves() && crystal_domain_cooldown < 0) ? distance * 0.02 : 0, //Cast Crystal Domain Attack
                    (cast_spike_spell != prevAttack && healthFac < 0.55) ? distance * 0.02 : 0 //Cast Spike Spell
                    //25% Attacks


            };
            prevAttack = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttack.accept(target);
        }
        //When spawning waves of mobs, super slow. When Shielded, slowdown the attack a bit, and by itself it as such
        return this.isSpawningWaves() ? 260 : this.isShielded() ? 120 : healthFac < 0.25 ? (int) ((MobConfig.apathyr_cooldown * 20)/2) : (int) (MobConfig.apathyr_cooldown * 20);
    }

    private final Consumer<EntityLivingBase> cast_spike_spell = (target) -> {
      this.setFightMode(true);
      this.setCastSpell(true);

        addEvent(()-> this.playSound(SoundsHandler.APATHYR_CAST_SPIKES, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 20);
      addEvent(()-> {
        //action spike
          if(!world.isRemote) {
              // sends spikes to ALL players
              if(target instanceof EntityPlayer) {
                  List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable());
                  if(!nearbyPlayers.isEmpty()) {
                      for(EntityPlayer player : nearbyPlayers) {
                          addEvent(() -> {
                              Vec3d targetOldPos = player.getPositionVector();
                              addEvent(() -> {
                                  Vec3d targetedPos = player.getPositionVector();
                                  Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 4);
                                  this.spawnSpikeAction(predictedPosition);
                              }, 3);
                          }, 1);
                      }
                  }
              } else {
                  addEvent(() -> {
                      Vec3d targetOldPos = target.getPositionVector();
                      addEvent(() -> {
                          Vec3d targetedPos = target.getPositionVector();
                          Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 4);
                          this.spawnSpikeAction(predictedPosition);
                      }, 3);
                  }, 1);
              }
          }
      }, 24);

      addEvent(()-> {
        this.setFightMode(false);
        this.setCastSpell(false);
      }, 55);
    };

    private final Consumer<EntityLivingBase> teleport_ghost = (target) -> {
        this.setFightMode(true);
        this.setTeleportGhost(true);

        addEvent(()-> {
            //teleports boss and spawns ghost in place
            addEvent(()-> {
                this.setTeleportGhost(true);
                EntityApathyrGhost ghost = new EntityApathyrGhost(world, this);
                ghost.copyLocationAndAnglesFrom(this);
                Vec3d pos = this.canBossTeleport(target);
                this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                ghost.setPosition(pos.x, pos.y, pos.z);
                world.spawnEntity(ghost);
            }, 5);
            this.playSound(SoundsHandler.APATHYR_SUMMON_GHOST, 1.25f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 10);

        addEvent(()-> {
            this.setTeleportGhost(false);
            this.setFightMode(false);
        }, 20);
    };

    private final Consumer<EntityLivingBase> cast_fast_projectile = (target) -> {
       this.setFightMode(true);
       this.setCastFast(true);

        addEvent(()-> this.playSound(SoundsHandler.APATHYR_CAST_HEAVY, 1.25f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 22);
       addEvent(()-> this.lockLook = true, 22);
       addEvent(()-> {
           ProjectileFastGhostCrystal fast_ghost_bolt = new ProjectileFastGhostCrystal(world, this, this.getAttack());
           Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5,2,0)));
           fast_ghost_bolt.setPosition(relPos.x, relPos.y, relPos.z);
           fast_ghost_bolt.setTravelRange(40);
           fast_ghost_bolt.shoot(this, 0, this.rotationYaw, 0, 0.8F, 0);
           fast_ghost_bolt.rotationYaw = this.rotationYaw;
           world.spawnEntity(fast_ghost_bolt);
       }, 25);
       addEvent(()-> this.lockLook = false, 35);

       addEvent(()-> {
        this.setCastFast(false);
        this.setFightMode(false);
       }, 40);
    };

    private final Consumer<EntityLivingBase> cast_storm = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.setCrystalDomain(true);
      setTooDoDomain = false;

      //teleports boss to spawn location
      addEvent(()-> {
          this.setImmovable(false);
          if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
              this.setPosition(this.getSpawnLocation().getX() + 0.5, this.getSpawnLocation().getY(), this.getSpawnLocation().getZ() + 0.5);
              this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
              this.setImmovable(true);
          } else {
              this.setImmovable(true);
          }
          this.shakeTime = 200;
          this.setShaking(true);
      }, 5);

      addEvent(() -> {
          this.lockLook = true;
          EntityUltraAttack attack = new EntityUltraAttack(world, this);
          attack.setPosition(this.posX, this.posY, this.posZ);
          world.spawnEntity(attack);
          if(this.isUsedGoldenHeart()) {
              List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable());
              if(!nearbyPlayers.isEmpty()) {
                  for(EntityPlayer player : nearbyPlayers) {
                      for(int i = 0; i < 200; i += 20) {
                          addEvent(()-> {
                              EntityApathyrSpear spear = new EntityApathyrSpear(world);
                              spear.setPosition(player.posX, player.posY, player.posZ);
                              world.spawnEntity(spear);
                          }, i);
                      }
                  }
              }
          }
      }, 30);

      addEvent(()-> {
          this.setShaking(false);
      }, 205);

      addEvent(()-> this.lockLook = false, 220);

      addEvent(()-> {
          this.crystal_domain_cooldown = 20 * 30;
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            this.setCrystalDomain(false);
      }, 260);
    };

    private final Consumer<EntityLivingBase> super_dash = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
      this.setSuperDash(true);
      this.performSuperDash = true;
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_CHARGE_AXE, 2.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
      superDashAttack.doAction();
      addEvent(()-> this.lockLook = true, 41);
      addEvent(()-> this.setImmovable(false), 44);
      addEvent(()-> {
          //breaks blocks with given thing
          this.destroyBlocksInSwing(new Vec3d(2.5, 0.1, 0), 1.1);
      }, 50);

      addEvent(()-> {
          this.setShaking(true);
          this.shakeTime = 35;
      }, 50);
      //47 tick teleport

        addEvent(()-> new ActionApathyrStomp().performAction(this, target), 47);
        addEvent(()-> {
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2.25, 1.2, 0)));
            Main.proxy.spawnParticle(21, relPos.x, this.posY + 0.5, relPos.z, 0, 0, 0);
        }, 46);
        addEvent(()-> {
        this.setImmovable(true);
        this.performSuperDash = false;
        }, 52);

        //lead into follow ups
        addEvent(()-> {
            this.setShaking(false);
            this.lockLook = false;
            boolean followUp = rand.nextBoolean();
            if(followUp && !this.isTertiary()) {
                this.setTransitionTick(true);
                this.selectFollowUp(target);
                this.setFullBodyUsage(false);
                this.setImmovable(false);
                this.setSuperDash(false);
                addEvent(()-> {
                    this.setTransitionTick(false);
                }, 10);
            } else {
                addEvent(()-> {
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                    this.setSuperDash(false);
                    this.setFightMode(false);
                    this.coolDownTime = 30;
                    this.attackExhaustion++;
                }, 10);
            }
        }, 80);
    };

    private final Consumer<EntityLivingBase> cast_arm_swipe = (target) -> {
        this.setFightMode(true);
        this.setArmSwipe(true);

        addEvent(()-> this.playSound(SoundsHandler.APATHYR_CAST_MAGIC, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 21);
        addEvent(()-> {
            //cast regular Projectiles
            new ActionSummonProjectiles().performAction(this, target);
        }, 26);

        addEvent(() -> {
            this.setFightMode(false);
            this.setArmSwipe(false);
        }, 45);
    };

    private final Consumer<EntityLivingBase> jump_attack = (target) -> {
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.setImmovable(true);
        this.setJumpAttack(true);

        addEvent(()-> this.playSound(SoundsHandler.APATHYR_SLIGHT_DASH, 1.0f, 0.4f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_JUMP, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-2));
            this.faceEntity(target, 180, 30);

            addEvent(()-> {
                this.lockLook = true;
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                float y_variable = (float) (distance * 0.17);
                if(distance > 8) {
                    y_variable = (float) 1.36;
                }
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.3), y_variable);
            }, 4);
        }, 16);

        addEvent(()-> {
            double healthFac = this.getHealth() / this.getMaxHealth();
            if(healthFac < 0.3) {
                //summon spikes
                new ActionJumpAttackSpears().performAction(this, target);
            }
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack() * 1.5);
            ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 1.2f, 0, false);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75, 1.2, 0)));
            Main.proxy.spawnParticle(21, relPos.x, this.posY, relPos.z, 0, 0, 0);
            if(this.onGround) {
                this.setImmovable(true);
            }
            this.setShaking(true);
            this.shakeTime = 15;
        }, 35);

        addEvent(()-> this.setShaking(false), 50);

        addEvent(()-> {
            this.setImmovable(true);
        }, 40);

        addEvent(()-> {
            this.lockLook = false;
            boolean followUp = rand.nextBoolean();
            if(followUp && !this.isTertiary()) {
                this.setTransitionTick(true);
                this.selectFollowUp(target);
                this.setFullBodyUsage(false);
                this.setImmovable(false);
                this.setJumpAttack(false);
                addEvent(()-> {
                    this.setTransitionTick(false);
                }, 10);
            } else {
                addEvent(()-> {
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                    this.setJumpAttack(false);
                    this.setFightMode(false);
                    this.coolDownTime = 30;
                    this.attackExhaustion++;
                }, 10);
            }
        }, 60);
    };

    private final Consumer<EntityLivingBase> grab_attack = (target) -> {
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.setFightMode(true);
      this.setGrabBegin(true);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(0.5));
            this.faceEntity(target, 180, 30);
            addEvent(()-> {
                this.lockLook = true;
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.35),0.1F);
               this.playSound(SoundsHandler.APATHYR_SLIGHT_DASH, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                this.grabDetection = true;
            }, 4);
        }, 16);

        addEvent(()-> this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.9f, 0.4f / (rand.nextFloat() * 0.4f + 0.2f)), 20);

        addEvent(()-> {
            this.grabDetection = false;
            this.setGrabBegin(false);
            this.setImmovable(true);

            if(grabbedEntity != null) {
                this.setGrabTooContinue(target);
            } else {
                this.setGrabFinish(true);

                addEvent(()-> {
                    this.setImmovable(false);
                    this.setFullBodyUsage(false);
                    this.lockLook = false;
                    this.setTransitionTick(true);
                    this.selectFollowUp(target);
                    this.setGrabFinish(false);
                    addEvent(()-> {
                        this.setTransitionTick(false);
                    }, 5);

                }, 25);
            }
        }, 30);
    };

    private void setGrabTooContinue(EntityLivingBase target) {
        grab_continue.accept(target);
    }



    private final Consumer<EntityLivingBase> grab_continue = (target) -> {
        this.setGrabContinue(true);
        this.currentlyInIFrame = true;

        addEvent(()-> {
            this.grabbedEntity = null;
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack() * 1.25);
            ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 1.2f, 0, false);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 1.2f / (rand.nextFloat() * 0.4f + 0.2f));
            double healFac = this.getMaxHealth() * MobConfig.apathyr_life_steal_amount;
            this.heal((float) healFac);
        }, 25);

        addEvent(()-> {
            this.lockLook = false;
        }, 45);

        addEvent(()-> {
            this.currentlyInIFrame = false;
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setGrabContinue(false);
            this.attackExhaustion++;
        }, 55);
    };

    Supplier<Projectile> crystal_wave_projectiles = () -> new ProjectileCrystalWave(world, this, (float) this.getAttack(), null);
    private final Consumer<EntityLivingBase> upper_swing = (target) -> {
      this.setFightMode(true);
      this.setUpperSwing(true);

      addEvent(()-> this.playSound(SoundsHandler.APATHYR_ATTACK, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 20);
      addEvent(()-> {
          this.lockLook = true;
      }, 30);

        addEvent(()-> this.playSound(SoundsHandler.APATHYR_SWING, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 30);
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2.5, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.4f, 0, false);
            this.destroyBlocksInSwing(new Vec3d(2.5, 1.2, 0), 0.9);
            this.setImmovable(true);
            //do projectile attack
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2.75, 1.2, 0)));
            Main.proxy.spawnParticle(21, relPos.x, this.posY, relPos.z, 0, 0, 0);
                new ActionEveratorProjectileYellow(crystal_wave_projectiles).performAction(this, target);
                this.playSound(SoundsHandler.APATHYR_CAST_MAGIC, 0.75f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));

        }, 38);

        addEvent(()-> {
            this.lockLook = false;
            if(!this.isSpawningWaves()) {
                this.setImmovable(false);
            }
        }, 55);

        addEvent(()-> {
            if(this.isTertiary()) {
                this.setTertiary(false);
                this.apathyr_aggrivation-= 0.1;
                this.flagForStrafing = true;
            } else {
                this.coolDownTime = 45;
            }
            this.attackExhaustion++;
            this.setFightMode(false);
            this.setUpperSwing(false);
        }, 60);
    };

    private final Consumer<EntityLivingBase> jab_attack = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.setJab(true);

        addEvent(()-> this.playSound(SoundsHandler.APATHYR_ATTACK, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 16);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            this.faceEntity(target, 180, 30);
            addEvent(()-> {
                this.lockLook = true;
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.33),0.1F);
            }, 4);
        }, 19);

        addEvent(()-> {
            double healthFac = this.getHealth() / this.getMaxHealth();
            if(healthFac < 0.3) {
                //summon spikes
                new ActionJabLine().performAction(this, target);
            }
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.4f, 0, false);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            this.destroyBlocksInSwing(new Vec3d(1.5, 1.2, 0), 1.1);
        }, 30);

        addEvent(()-> this.setImmovable(true), 35);

        addEvent(()-> {
            this.lockLook = false;
            boolean followUp = rand.nextBoolean();
            if(followUp && !this.isTertiary()) {
                this.setTransitionTick(true);
                this.selectFollowUp(target);
                this.setFullBodyUsage(false);
                this.setImmovable(false);
                this.setJab(false);
                addEvent(()-> {
                    this.setTransitionTick(false);
                }, 10);
            } else {
                addEvent(()-> {
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                    this.setJab(false);
                    this.setFightMode(false);
                    this.attackExhaustion++;
                    this.coolDownTime = 30;
                    if(this.isTertiary()) {
                        this.setTertiary(false);
                        this.apathyr_aggrivation-= 0.1;
                        this.flagForStrafing = true;
                    }
                }, 10);
            }
        }, 45);
    };

    private final Consumer<EntityLivingBase> push = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.setPush(true);
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_ATTACK, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 16);
        addEvent(()-> {
            this.faceEntity(target, 180, 30);
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-0.5));
            addEvent(()-> {
                this.lockLook = true;
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.33),0.1F);
                this.playSound(SoundsHandler.APATHYR_SLIGHT_DASH, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 4);
        }, 15);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 1.3f, 0, false);
               this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.8f, 0.4f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 26);

        addEvent(()-> {
            this.setImmovable(true);
        }, 30);

        addEvent(()-> {
            this.lockLook = false;
        }, 35);

        addEvent(()-> {
            boolean followUp = rand.nextBoolean();
            if(followUp && !this.isTertiary()) {
                this.setFullBodyUsage(false);
                this.setImmovable(false);
                this.setTransitionTick(true);
                this.selectFollowUp(target);
                this.setPush(false);
                addEvent(()-> {
                    this.setTransitionTick(false);
                }, 10);
            } else {
                addEvent(()-> {
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                    this.setPush(false);
                    this.setFightMode(false);
                    this.attackExhaustion++;
                    this.coolDownTime = 30;
                    if(this.isTertiary()) {
                        this.setTertiary(false);
                        this.flagForStrafing = true;
                    }
                }, 10);
            }
        }, 40);

    };

    private final Consumer<EntityLivingBase> swing = (target) -> {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        boolean randI = rand.nextBoolean();

        if(randI) {
            this.setSwingConnect(true);
            addEvent(()-> this.playSound(SoundsHandler.APATHYR_ATTACK, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 15);

            addEvent(()-> this.faceEntity(target, 180, 30), 10);
            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1.5));
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
                }, 4);
            }, 14);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                this.destroyBlocksInSwing(new Vec3d(1, 1.2, 0), 1.1);
                double healthFac = this.getHealth()/this.getMaxHealth();
                if(healthFac < 0.5) {
                    new ActionApathyrSwing().performAction(this, target);
                }
            }, 23);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 30);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1.5));
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
                }, 4);
            }, 45);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                this.destroyBlocksInSwing(new Vec3d(1, 1.2, 0), 1.1);
                double healthFac = this.getHealth()/this.getMaxHealth();
                if(healthFac < 0.5) {
                    new ActionApathyrSwing().performAction(this, target);
                }
            }, 54);

            addEvent(()-> {
                this.setImmovable(true);
            }, 60);

            addEvent(()-> {
                this.lockLook = false;
            }, 70);

            addEvent(()-> {
                boolean followUp = rand.nextBoolean();
                if(followUp && !this.isTertiary()) {
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                    this.setTransitionTick(true);
                    this.selectFollowUp(target);
                    this.setSwingConnect(false);
                    addEvent(()-> {
                        this.setTransitionTick(false);
                    }, 10);
                } else {
                    addEvent(()-> {
                        this.setFullBodyUsage(false);
                        this.attackExhaustion++;
                        this.setImmovable(false);
                        this.setSwingConnect(false);
                        this.setFightMode(false);
                        this.coolDownTime = 30;
                        if(this.isTertiary()) {
                            this.setTertiary(false);
                            this.flagForStrafing = true;
                        }
                    }, 10);
                }
            }, 75);
        } else {
            this.setSwingAttack(true);
            addEvent(()-> this.playSound(SoundsHandler.APATHYR_ATTACK, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
            addEvent(()-> this.faceEntity(target, 180, 30), 10);
            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1.5));
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
                }, 4);
            }, 14);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                this.destroyBlocksInSwing(new Vec3d(1, 1.2, 0), 1.1);
                double healthFac = this.getHealth()/this.getMaxHealth();
                if(healthFac < 0.5) {
                    new ActionApathyrSwing().performAction(this, target);
                }
            }, 23);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 30);

            addEvent(()-> {
                boolean followUp = rand.nextBoolean();
                if(followUp && !this.isTertiary()) {
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                    this.setTransitionTick(true);
                    this.selectFollowUp(target);
                    this.setSwingAttack(false);
                    addEvent(()-> {
                        this.setTransitionTick(false);
                    }, 10);
                } else {
                    addEvent(()-> {
                    this.setFullBodyUsage(false);
                        this.attackExhaustion++;
                    this.setImmovable(false);
                    this.setSwingAttack(false);
                    this.setFightMode(false);
                        this.coolDownTime = 30;
                        if(this.isTertiary()) {
                            this.setTertiary(false);
                            this.flagForStrafing = true;
                        }
                    }, 10);
                }
            }, 40);
        }
    };


    private void selectFollowUp(EntityLivingBase target) {
        int randB = ModRand.range(1, 7);
        double healthFac = this.getHealth() / this.getMaxHealth();
        if(randB == 1) {
             if(!this.isJab()) {
                //do jab
                 jab_attack_followup.accept(target);
             } else {
                push_followup.accept(target);
             }
        } else if (randB == 2){
            if(!this.isPush()) {
                //do Push
                push_followup.accept(target);
            } else {
                kick_followup.accept(target);
            }
        } else if(randB == 3) {
            if(!this.isKick()) {
               //do kick
                kick_followup.accept(target);
            } else {
                jab_attack_followup.accept(target);
            }
        }  else if(randB == 4) {
            if(!this.isJumpAttack() && healthFac < 0.75) {
                //do jump Attack Follow Up
                jump_attack_followup.accept(target);
            } else {
                if(!this.isKick()) {
                    kick_followup.accept(target);
                } else {
                    jab_attack_followup.accept(target);
                }
            }
        } else if (randB == 5) {
            if(healthFac < 0.75) {
                jump_away.accept(target);
            } else {
                if(!this.isJab()) {
                    jab_attack_followup.accept(target);
                } else {
                    kick_followup.accept(target);
                }
            }
        } else if (randB == 6) {
            if(healthFac < 0.75) {
                circle_swing_attack_two.accept(target);
            } else {
                if(!this.isKick()) {
                    kick_followup.accept(target);
                } else {
                    jab_attack_followup.accept(target);
                }
            }
        }
        this.setSecondary(true);
    }

    private final Consumer<EntityLivingBase> circle_swing_attack_two = (target) -> {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.setCircleSwing(true);
        addEvent(()-> this.faceEntity(target, 180, 30), 17);
        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(3));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.33),0.1F);
            }, 4);
        }, 21);


        addEvent(() -> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.9f, 0, false);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 30);

        addEvent(() -> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(4f, (e) -> damage, this, offset, source, 0.9f, 0, false);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 35);

        addEvent(()-> {
            this.setImmovable(true);
            double healthFac = this.getHealth()/this.getMaxHealth();
            if(healthFac < 0.5) {
                new ActionApathyrSwing().performAction(this, target);
                if(healthFac < 0.3) {
                    new ActionApathyrCross().performAction(this, target);
                }
            }
        }, 45);

        addEvent(()-> {
            this.lockLook = false;
        }, 65);

        addEvent(() -> {
            int randC = ModRand.range(1, 4);
            if(randC == 1 || this.canAccessCombos) {
                this.setTransitionTick(true);
                this.selectTertiary(target);
                this.setFullBodyUsage(false);
                this.setImmovable(false);
                this.setCircleSwing(false);
                addEvent(()-> {
                    this.setTransitionTick(false);
                }, 10);
            } else {
                addEvent(() -> {
                    this.setImmovable(false);
                    this.setFightMode(false);
                    this.setFullBodyUsage(false);
                    this.setCircleSwing(false);
                    this.setSecondary(false);
                }, 10);
            }
        }, 65);
    };

    private final Consumer<EntityLivingBase> jump_away = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
      this.setJumpAway(true);

        addEvent(()-> {
            this.currentlyInIFrame = true;
            Vec3d dirToo = this.getPositionVector().subtract(target.getPositionVector()).normalize();
            Vec3d jumpTooPos = this.getPositionVector().add(dirToo.scale(47));
            this.setImmovable(false);
            ModUtils.leapTowards(this, jumpTooPos, 1.4F, 0.25F);
        }, 15);

        addEvent(()-> {
            if(this.onGround) {
                this.setImmovable(true);
            }
        }, 38);

        addEvent(() -> {
            this.setImmovable(true);
            this.currentlyInIFrame = false;
            int randC = ModRand.range(1, 4);
            if(randC == 1 || this.canAccessCombos) {
                this.setTransitionTick(true);
                this.selectTertiary(target);
                this.setFullBodyUsage(false);
                this.setImmovable(false);
                this.setJumpAway(false);
                addEvent(()-> {
                    this.setTransitionTick(false);
                }, 10);
            } else {
                addEvent(()-> {
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                    this.setJumpAway(false);
                    this.setFightMode(false);
                    this.setSecondary(false);
                }, 10);
            }
        }, 45);

    };

    private final Consumer<EntityLivingBase> jump_attack_followup = (target) -> {
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.setImmovable(true);
        this.setJumpAttack(true);

        addEvent(()-> this.playSound(SoundsHandler.APATHYR_SLIGHT_DASH, 1.0f, 0.4f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_JUMP, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 15);

        addEvent(()-> {
            this.faceEntity(this, 180, 30);
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-2));
            addEvent(()-> {
                this.lockLook = true;
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                float y_variable = (float) (distance * 0.11);
                if(distance > 8) {
                    y_variable = (float) 1.36;
                }
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.3), y_variable);
            }, 4);
        }, 16);

        addEvent(()-> {
            double healthFac = this.getHealth() / this.getMaxHealth();
            if(healthFac < 0.3) {
                //summon spikes
                new ActionJumpAttackSpears().performAction(this, target);
            }
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack() * 1.5);
            ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 1.2f, 0, false);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75, 1.2, 0)));
            Main.proxy.spawnParticle(21, relPos.x, this.posY, relPos.z, 0, 0, 0);
            if(this.onGround) {
                this.setImmovable(true);
            }
            this.setShaking(true);
            this.shakeTime = 15;
        }, 35);

        addEvent(()-> this.setShaking(false), 50);

        addEvent(()-> {
            this.setImmovable(true);
        }, 40);

        addEvent(()-> {
            this.lockLook = false;
            int randC = ModRand.range(1, 4);
            if(randC == 1 || this.canAccessCombos) {
                this.setTransitionTick(true);
                this.selectTertiary(target);
                this.setFullBodyUsage(false);
                this.setImmovable(false);
                this.setJumpAttack(false);
                addEvent(()-> {
                    this.setTransitionTick(false);
                }, 10);
            } else {
                addEvent(()-> {
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                    this.setJumpAttack(false);
                    this.setFightMode(false);
                    this.setSecondary(false);
                }, 10);
            }
        }, 60);
    };

    private final Consumer<EntityLivingBase> jab_attack_followup = (target) -> {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.setJab(true);

        addEvent(()-> this.playSound(SoundsHandler.APATHYR_ATTACK, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 16);
        addEvent(()-> {
            this.faceEntity(this, 180, 30);
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.lockLook = true;
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.33),0.1F);
            }, 4);
        }, 19);

        addEvent(()-> {
            double healthFac = this.getHealth() / this.getMaxHealth();
            if(healthFac < 0.3) {
                //summon spikes
                new ActionJabLine().performAction(this, target);
            }
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.4f, 0, false);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            this.destroyBlocksInSwing(new Vec3d(1.5, 1.2, 0), 1.1);
        }, 30);

        addEvent(()-> this.setImmovable(true), 35);

        addEvent(()-> {
            this.lockLook = false;
            int randC = ModRand.range(1, 4);
            if(randC == 1 || this.canAccessCombos) {
                this.setFullBodyUsage(false);
                this.setImmovable(false);
                this.setSecondary(false);
                this.setTransitionTick(true);
                this.selectTertiary(target);
                this.setJab(false);
                addEvent(()-> {
                    this.setTransitionTick(false);
                }, 10);
            } else {
                addEvent(()-> {
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                    this.setJab(false);
                    this.setFightMode(false);
                    this.setSecondary(false);
                }, 10);
            }
        }, 45);
    };

    private final Consumer<EntityLivingBase> push_followup = (target) -> {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.setPush(true);
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_ATTACK, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 16);
        addEvent(()-> {
            this.faceEntity(this, 180, 30);
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-0.5));
            addEvent(()-> {
                this.lockLook = true;
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.33),0.1F);
                this.playSound(SoundsHandler.APATHYR_SLIGHT_DASH, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 4);
        }, 15);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 1.3f, 0, false);
            //   this.playSound(SoundsHandler.REANIMATE_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 26);

        addEvent(()-> {
            this.setImmovable(true);
        }, 30);

        addEvent(()-> {
            this.lockLook = false;
        }, 35);

        addEvent(()-> {
            int randC = ModRand.range(1, 4);
            if(randC == 1 || this.canAccessCombos) {
                this.setFullBodyUsage(false);
                this.setImmovable(false);
                this.setSecondary(false);
                this.setTransitionTick(true);
                this.selectTertiary(target);
                this.setPush(false);
                addEvent(()-> {
                    this.setTransitionTick(false);
                }, 10);
            } else {
                addEvent(()-> {
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                    this.setPush(false);
                    this.setFightMode(false);
                    this.setSecondary(false);
                }, 10);
            }
        }, 40);

    };

    private final Consumer<EntityLivingBase> kick_followup = (target) -> {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.setKick(true);
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_ATTACK, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 18);
        addEvent(()-> this.faceEntity(target, 180, 30), 9);
        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-0.5));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.33),0.1F);
               this.playSound(SoundsHandler.APATHYR_SLIGHT_DASH, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 4);
        }, 13);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.1f, 0, false);
            this.setImmovable(true);
            //   this.playSound(SoundsHandler.REANIMATE_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 22);

        addEvent(()-> {
            this.lockLook = false;
            int randC = ModRand.range(1, 4);
            if(randC == 1 || this.canAccessCombos) {
                this.setFullBodyUsage(false);
                this.setImmovable(false);
                this.setSecondary(false);
                this.setTransitionTick(true);
                this.selectTertiary(target);
                this.setKick(false);
                addEvent(()-> {
                    this.setTransitionTick(false);
                }, 10);
            } else {
                addEvent(()-> {
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                    this.setKick(false);
                    this.setFightMode(false);
                    this.setSecondary(false);
                }, 5);
            }
        }, 35);
    };

    private void selectTertiary(EntityLivingBase target) {
        int randB = ModRand.range(1, 7);
        double healthChange = this.getHealth() / this.getMaxHealth();
        if(randB == 1) {
            //do swing
            swing_followup.accept(target);
        } else if (randB == 2){
            if(!this.isJab()) {
                //do jab
                jab_attack.accept(target);
            } else {
                swing_followup.accept(target);
            }
        } else if(randB == 3) {
            //do upper Swing
            upper_swing.accept(target);
        } else if (randB == 4) {
            // jump away target
            if(!this.isJumpAway() && healthChange < 0.75) {
                jump_away.accept(target);
            } else {
                swing_followup.accept(target);
            }
        } else if (randB == 5) {
            if(!this.isCircleSwing() && healthChange < 0.75) {
                circle_swing_attack.accept(target);
            } else {
                swing_followup.accept(target);
            }
        } else if (randB == 6) {
            if(healthChange < 0.5) {
                teleport_ghost.accept(target);
            } else {
                upper_swing.accept(target);
            }
        }
        this.setSecondary(false);
        this.setTertiary(true);
    }

    private final Consumer<EntityLivingBase> circle_swing_attack = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.setCircleSwing(true);
        addEvent(()-> this.faceEntity(target, 180, 30), 18);
        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(3));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.33),0.1F);
            }, 4);
        }, 21);


      addEvent(() -> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage =(float) (this.getAttack());
          ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.9f, 0, false);
          this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 30);

        addEvent(() -> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(4f, (e) -> damage, this, offset, source, 0.9f, 0, false);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 35);

        addEvent(()-> {
            this.setImmovable(true);
            double healthFac = this.getHealth()/this.getMaxHealth();
            if(healthFac < 0.5) {
                new ActionApathyrSwing().performAction(this, target);
                if(healthFac < 0.3) {
                    new ActionApathyrCross().performAction(this, target);
                }
            }
        }, 45);

        addEvent(()-> {
            this.lockLook = false;
        }, 65);

        addEvent(() -> {
            this.setImmovable(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setCircleSwing(false);
            this.attackExhaustion++;
            this.apathyr_aggrivation-= 0.1;
            this.flagForStrafing = true;
            this.setTertiary(false);
        }, 75);
    };
    private final Consumer<EntityLivingBase> swing_followup = (target) -> {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.setSwingAttack(true);
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_ATTACK, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
        addEvent(()-> this.faceEntity(target, 180, 30), 10);
            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1.5));
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
                }, 4);
            }, 14);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                this.destroyBlocksInSwing(new Vec3d(1, 1.2, 0), 1.1);
                double healthFac = this.getHealth()/this.getMaxHealth();
                if(healthFac < 0.5) {
                    new ActionApathyrSwing().performAction(this, target);
                }
            }, 23);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 30);

            addEvent(()-> {
                        this.setFullBodyUsage(false);
                        this.attackExhaustion++;
                        this.setImmovable(false);
                        this.setSwingAttack(false);
                        this.setFightMode(false);
                        this.setTertiary(false);
                        this.apathyr_aggrivation-= 0.1;
                        this.flagForStrafing = true;
            }, 50);
    };

    private Vec3d canBossTeleport(EntityLivingBase target) {
        if(target != null) {
            for(int i = 0; i < 20; i++) {
                Vec3d pos = ModRand.randVec().normalize().scale(2)
                        .add(target.getPositionVector());
                Vec3d posScaledWith = new Vec3d(pos.x * 7, this.posY + 2, pos.z * 7);
                boolean canSee = world.rayTraceBlocks(target.getPositionEyes(1), posScaledWith, false, true, false) == null;
                Vec3d prevPos = this.getPositionVector();
                int setYValue = ModUtils.getSurfaceHeightZeroReturn(world, new BlockPos(posScaledWith.x, prevPos.y, posScaledWith.z), (int) prevPos.y - 7, (int) prevPos.y + 1);
                if(setYValue != 0) {
                    if(canSee && ModUtils.attemptTeleport(new Vec3d(pos.x, setYValue + 1, pos.z), this)) {
                        this.setPositionAndUpdate(pos.x, setYValue + 1, pos.z);
                        return prevPos;
                    }
                }
            }
        }
        if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
            return new Vec3d(this.getSpawnLocation().getX() + 0.5, this.getSpawnLocation().getY(), this.getSpawnLocation().getZ() + 0.5);
        }
        return this.getPositionVector();
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "arms_controller_idle", 0, this::predicateArmsIdle));
        data.addAnimationController(new AnimationController(this, "attacks_controller_main_hold", 0, this::predicateAttacksMain));
        data.addAnimationController(new AnimationController(this, "idle_status_controller", 0, this::predicateIdleState));
    }

    private <E extends IAnimatable> PlayState predicateAttacksMain(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isIdleAwake() && !this.isIdleState() && !this.isTeleportGhost() && !this.isDeathState()) {
            AnimationBuilder builder = new AnimationBuilder();

            if(this.isTransitionTick()) {
                event.getController().transitionLengthTicks = 3;
                //System.out.println(event.getController().getAnimationState());
            } else {
                event.getController().transitionLengthTicks = 0;
            }

            //Secondary Attacks
            if(this.isSecondary()) {
                if(this.isJab()) {
                    event.getController().setAnimation(builder.playOnce(ANIM_JAB_SECONDARY));
                }
                if(this.isKick()) {
                    event.getController().setAnimation(builder.playOnce(ANIM_KICK_SECONDARY));
                }
                if(this.isPush()) {
                    event.getController().setAnimation(builder.playOnce(ANIM_PUSH_SECONDARY));
                }
                //75%
                if(this.isJumpAway()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_JUMP_AWAY));
                }
                if(this.isJumpAttack()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_JUMP_ATTACK_SECONDARY));
                }
                if(this.isCircleSwing()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_CIRCLE_ATTACK));
                }

                //Tertiary
            } else if(this.isTertiary()) {
                if(this.isSwingAttack()) {
                    event.getController().setAnimation(builder.playOnce(ANIM_SWING_THREE));
                }
                if(this.isJab()) {
                    event.getController().setAnimation(builder.playOnce(ANIM_JAB_THREE));
                }
                if(this.isUpperSwing()) {
                    event.getController().setAnimation(builder.playOnce(ANIM_UPPER_SWING_THREE));
                }
                //75%
                if(this.isJumpAway()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_JUMP_AWAY_THREE));
                }
                if(this.isCircleSwing()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_CIRCLE_ATTACK));
                }
                //50%
            //    if(this.isTeleportGhost()) {
            //        event.getController().setAnimation( builder.playOnce(ANIM_TELEPORT_GHOST_THREE));
            //    }
                //Primary
            }  else {
                if(this.isSwingAttack()) {
                    event.getController().setAnimation(builder.playOnce(ANIM_SWING));
                }
                if(this.isSwingConnect()) {
                    event.getController().setAnimation(builder.playOnce(ANIM_SWING_CONNECT));
                }
                if(this.isJab()) {
                    event.getController().setAnimation(builder.playOnce(ANIM_JAB));
                }
                if(this.isKick()) {
                    event.getController().setAnimation(builder.playOnce(ANIM_KICK));
                }
                if(this.isUpperSwing()) {
                    event.getController().setAnimation(builder.playOnce(ANIM_UPPER_SWING));
                }
                if(this.isPush()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_PUSH));
                }
                if(this.isArmSwipe()) {
                    event.getController().setAnimation(builder.playOnce(ANIM_ARM_SWIPE));
                }
                //75%
                if(this.isGrabBegin()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_GRAB_BEGIN));
                }
                if(this.isGrabContinue()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_GRAB_CONTINUE));
                }
                if(this.isGrabFinish()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_GRAB_FINISH));
                }
                if(this.isJumpAttack()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_JUMP_ATTACK));
                }
                //50%
                if(this.isSuperDash()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_SUPER_DASH));
                }
                if(this.isCrystalDomain()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_CRYSTAL_DOMAIN));
                }
                if(this.isCastFast()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_CAST_FAST_PROJECTILE));
                }
                if(this.isCastSpell()) {
                    event.getController().setAnimation( builder.playOnce(ANIM_CAST_REGULAR));
                }
            }
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage() && !this.isIdleAwake() && !this.isIdleState() && !this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArmsIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode() && !this.isIdleAwake() && !this.isIdleState() && !this.isDeathState() || event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && this.isTeleportGhost() && !this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage() && !this.isIdleAwake() && !this.isIdleState() && !this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
          //  event.getController().setAnimationSpeed(0.5 + (0.003 * event.getLimbSwing()));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode() && !this.isIdleAwake() && !this.isIdleState() && !this.isDeathState() || !(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && this.isTeleportGhost() && !this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
          //  event.getController().setAnimationSpeed(0.5 + (0.003 * event.getLimbSwing()));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdleState(AnimationEvent<E> event) {
        if(this.isIdleState() && !this.isIdleAwake()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_STATE, true));
            return PlayState.CONTINUE;
        } else if (this.isIdleAwake()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_AWAKE, false));
            return PlayState.CONTINUE;
        } else if (this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DEATH_ANIMATION, false));
            return PlayState.CONTINUE;
        }
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
        return SoundsHandler.APATHYR_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
            this.playSound(SoundsHandler.APATHYR_STEP, 0.35F, 0.8f + ModRand.getFloat(0.5F));
                this.setStepShake(true);
                this.shakeTime = 5;
                addEvent(() -> {
                    this.setStepShake(false);
                }, 5);

    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.currentlyInIFrame || this.isShielded() || this.isIdleAwake() || this.isIdleState()) {
            if(this.isIdleState() || this.isIdleAwake() || this.currentlyInIFrame) {
                this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.3f, 1.7f + ModRand.getFloat(0.2f));
            }
            if(this.isShielded()) {
                this.playSound(SoundsHandler.REANIMATE_SHIELD, 1f, 0.5f / (rand.nextFloat() * 0.4f + 0.2f));
            }
            return false;
        }

        if(ModConfig.boss_cap_damage_enabled && amount > MobConfig.apathyr_damage_cap) {
            return super.attackEntityFrom(source, MobConfig.apathyr_damage_cap);
        }

        return super.attackEntityFrom(source, amount);
    }

    private void destroyBlocksInSwing(Vec3d offset, double size) {
        AxisAlignedBB box = getEntityBoundingBox().grow(size, 0.1, size).offset(ModUtils.getRelativeOffset(this, new Vec3d(offset.x, offset.y, offset.z)));
        ModUtils.destroyBlocksInAABB(box, world, this);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    public List<EntityApathyrEye> apathyrEyes() {
       return ModUtils.getEntitiesInBox(this, this.getEntityBoundingBox().grow(38)).stream().filter(e -> e instanceof EntityApathyrEye)
               .filter(e ->  world.rayTraceBlocks(e.getPositionEyes(1), getPositionEyes(1), false, true, false) == null)
                .map(e -> (EntityApathyrEye)e)
               .collect(Collectors.toList());
    }

    @Override
    public void doRender(RenderManager renderManager, double x, double y, double z, float entityYaw, float partialTicks) {
        for (EntityApathyrEye e : apathyrEyes()) {
            RenderUtil.drawLazer(renderManager, this.getPositionVector().add(0, 2.5, 0), e.getPositionVector().add(0, 0.25, 0), new Vec3d(x, y, z), ModColors.AZURE, this, partialTicks);
        }
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "apathyr");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {

        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 20.0F);
            if (dist >= 20.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.5F) * 1.6F * screamMult);
        } else if(this.isStepShake()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 20.0F);
            if (dist >= 20.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 0.32F * screamMult);
        }
        return 0;
    }

    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.00001F);
        if(!this.isDeathState()) {
            this.clearEvents();
        }
        this.setImmovable(true);
        this.setDeathState(true);
        this.lockLook = true;
        //death sound
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_DEATH, 1.25f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_CAST_MAGIC, 0.6f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 40);
        //horns
        addEvent(()-> this.playSound(SoundsHandler.OBSIDILITH_SHIELD, 0.3f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 50);
        addEvent(()-> this.playSound(SoundsHandler.OBSIDILITH_SHIELD, 0.3f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 70);
        addEvent(()-> this.playSound(SoundsHandler.OBSIDILITH_SHIELD, 0.3f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 90);
        // Fall
        addEvent(()-> this.playSound(SoundsHandler.APATHYR_STEP, 1.5f, 0.3f / (rand.nextFloat() * 0.4f + 0.2f)), 130);
        //
        addEvent(()-> {
            if(!world.isRemote) {
                for(int i = 0; i <= 100; i+=5) {
                    if(!world.isRemote) {
                        addEvent(() -> {
                            EntityXPOrb orb = new EntityXPOrb(world, this.posX, this.posY, this.posZ, MobConfig.apathyr_experience_value / 20);
                            orb.setPosition(this.posX, this.posY + 1, this.posZ);
                            world.spawnEntity(orb);
                        }, i);
                    }
                }
            }
        }, 70);

        addEvent(()-> {
            //actual death
            if(this.getSpawnLocation() != null) {
                this.turnBossIntoSummonSpawner(this.getSpawnLocation());
            }
            this.setDead();
            this.setDropItemsWhenDead(true);
            this.setDeathState(false);
        }, 169);
        super.onDeath(cause);
    }

    public void spawnSpikeAction(Vec3d predictedPos) {
        EntityLivingBase target = this.getAttackTarget();
        //1
        if(target != null) {
            EntityApathyrSpear spike = new EntityApathyrSpear(this.world);
            BlockPos area = new BlockPos(predictedPos.x, predictedPos.y, predictedPos.z);
            int y = getSurfaceHeight(this.world, new BlockPos(target.posX, 0, target.posZ), (int) this.posY - 5, (int) this.posY + 3);
            spike.setPosition(area.getX(), y + 1, area.getZ());
            world.spawnEntity(spike);
            //2
            EntityApathyrSpear spike2 = new EntityApathyrSpear(this.world);
            BlockPos area2 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z);
            int y2 = getSurfaceHeight(this.world, new BlockPos(area2.getX(), 0, area2.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike2.setPosition(area2.getX(), y2 + 1, area2.getZ());
            world.spawnEntity(spike2);
            //3
            EntityApathyrSpear spike3 = new EntityApathyrSpear(this.world);
            BlockPos area3 = new BlockPos(predictedPos.x - 1, predictedPos.y, predictedPos.z);
            int y3 = getSurfaceHeight(this.world, new BlockPos(area3.getX(), 0, area3.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike3.setPosition(area3.getX(), y3 + 1, area3.getZ());
            world.spawnEntity(spike3);
            //4
            EntityApathyrSpear spike4 = new EntityApathyrSpear(this.world);
            BlockPos area4 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 1);
            int y4 = getSurfaceHeight(this.world, new BlockPos(area4.getX(), 0, area4.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike4.setPosition(area4.getX(),y4 + 1, area4.getZ());
            world.spawnEntity(spike4);
            //5
            EntityApathyrSpear spike5 = new EntityApathyrSpear(this.world);
            BlockPos area5 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z - 1);
            int y5 = getSurfaceHeight(this.world, new BlockPos(area5.getX(), 0, area5.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike5.setPosition(area5.getX(), y5 + 1, area5.getZ());
            world.spawnEntity(spike5);
            //6
            EntityApathyrSpear spike6 = new EntityApathyrSpear(this.world);
            BlockPos area6 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z + 1);
            int y6 = getSurfaceHeight(this.world, new BlockPos(area6.getX(), 0, area6.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike6.setPosition(area6.getX(), y6 + 1, area6.getZ());
            world.spawnEntity(spike6);
            //7
            EntityApathyrSpear spike7 = new EntityApathyrSpear(this.world);
            BlockPos area7 = new BlockPos(predictedPos.x +1 , predictedPos.y, predictedPos.z - 1);
            int y7 = getSurfaceHeight(this.world, new BlockPos(area7.getX(), 0, area7.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike7.setPosition(area7.getX(), y7 + 1, area7.getZ());
            world.spawnEntity(spike7);
            //8
            EntityApathyrSpear spike8 = new EntityApathyrSpear(this.world);
            BlockPos area8 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z - 1);
            int y8 = getSurfaceHeight(this.world, new BlockPos(area8.getX(), 0, area8.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike8.setPosition(area8.getX(), y8 + 1, area8.getZ());
            world.spawnEntity(spike8);
            //9
            EntityApathyrSpear spike9 = new EntityApathyrSpear(this.world);
            BlockPos area9 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z + 1);
            int y9 = getSurfaceHeight(this.world, new BlockPos(area9.getX(), 0, area9.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike9.setPosition(area9.getX(), y9 + 1, area9.getZ());
            world.spawnEntity(spike9);
            //10
            EntityApathyrSpear spike10 = new EntityApathyrSpear(this.world);
            BlockPos area10 = new BlockPos(predictedPos.x +2, predictedPos.y, predictedPos.z );
            int y10 = getSurfaceHeight(this.world, new BlockPos(area10.getX(), 0, area10.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike10.setPosition(area10.getX(), y10 + 1, area10.getZ());
            world.spawnEntity(spike10);
            //11
            EntityApathyrSpear spike11 = new EntityApathyrSpear(this.world);
            BlockPos area11 = new BlockPos(predictedPos.x - 2, predictedPos.y, predictedPos.z );
            int y11 = getSurfaceHeight(this.world, new BlockPos(area11.getX(), 0, area11.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike11.setPosition(area11.getX(), y11 + 1, area11.getZ());
            world.spawnEntity(spike11);
            //12
            EntityApathyrSpear spike12 = new EntityApathyrSpear(this.world);
            BlockPos area12 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 2);
            int y12 = getSurfaceHeight(this.world, new BlockPos(area12.getX(), 0, area12.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike12.setPosition(area12.getX(), y12 + 1, area12.getZ());
            world.spawnEntity(spike12);
            //13
            EntityApathyrSpear spike13 = new EntityApathyrSpear(this.world);
            BlockPos area13 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z -2);
            int y13 = getSurfaceHeight(this.world, new BlockPos(area13.getX(), 0, area13.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike13.setPosition(area13.getX(), y13 + 1, area13.getZ());
            world.spawnEntity(spike13);
            //14
            EntityApathyrSpear spike14 = new EntityApathyrSpear(this.world);
            BlockPos area14 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z -3);
            int y14 = getSurfaceHeight(this.world, new BlockPos(area14.getX(), 0, area14.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike14.setPosition(area14.getX(), y14 + 1, area14.getZ());
            world.spawnEntity(spike14);
            //15
            EntityApathyrSpear spike15 = new EntityApathyrSpear(this.world);
            BlockPos area15 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 3);
            int y15 = getSurfaceHeight(this.world, new BlockPos(area15.getX(), 0, area15.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike15.setPosition(area15.getX(), y15 + 1, area15.getZ());
            world.spawnEntity(spike15);
            //16
            EntityApathyrSpear spike16 = new EntityApathyrSpear(this.world);
            BlockPos area16 = new BlockPos(predictedPos.x - 3, predictedPos.y, predictedPos.z);
            int y16 = getSurfaceHeight(this.world, new BlockPos(area16.getX(), 0, area16.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike16.setPosition(area16.getX(), y16 + 1, area16.getZ());
            world.spawnEntity(spike16);
            //17
            EntityApathyrSpear spike17 = new EntityApathyrSpear(this.world);
            BlockPos area17 = new BlockPos(predictedPos.x + 3, predictedPos.y, predictedPos.z);
            int y17 = getSurfaceHeight(this.world, new BlockPos(area17.getX(), 0, area17.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike17.setPosition(area17.getX(), y17 + 1, area17.getZ());
            world.spawnEntity(spike17);

            this.setShaking(true);
            this.shakeTime = 20;
            addEvent(()-> this.setShaking(false), 20);
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

    @Nullable
    @Override
    public SoundEvent getBossMusic() {
        return SoundsHandler.APATHYR_TRACK;
    }
}
