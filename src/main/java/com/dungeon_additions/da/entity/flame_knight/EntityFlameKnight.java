package com.dungeon_additions.da.entity.flame_knight;

import com.dungeon_additions.da.blocks.boss.BlockEnumBossSummonState;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.EntityAIFlameKnightAttack;
import com.dungeon_additions.da.entity.ai.EntityTimedAttackAIImproved;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.flame_knight.misc.*;
import com.dungeon_additions.da.entity.frost_dungeon.EntityAbstractGreatWyrk;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.tileEntity.TileEntityBossReSummon;
import com.dungeon_additions.da.entity.util.IEntitySound;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.*;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.base.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityFlameKnight extends EntityFlameBase implements IAnimatable, IAttack, IAnimationTickable, IScreenShake, IEntitySound {
    protected Vec3d chargeDir;
    private Consumer<EntityLivingBase> prevAttack;

    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_6));
    public int wantedDistance;
    private static final DataParameter<Boolean> COMBO_MODE = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_ONE = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_TWO = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_THREE = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SMALL_STOMP = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLOCK_MOTION = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> OVERHEAD_DASH = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLAME_SLING = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLAME_SLING_TRIPLE = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_METEOR = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_FLAME = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BEGIN_COMBO = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_COMBO = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GROUND_SWEEP = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_SWING_ONE = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_SWING_TWO = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_FAR_DASH = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_WATERFOUL = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_STRIKE = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DRINK_POTION = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> LOST_HEAD = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> PHASE_TRANSISTION = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RELEASE_FLAMES = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> JUMP_WITHOUT_ATTACK = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_WITH_ATTACK = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEATH_PlAY = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPAM_DETECTED = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);

    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BLOCK_POS);
    public static DataParameter<Boolean> SET_SPAWN_LOC = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> ITEM_HAND = EntityDataManager.<ItemStack>createKey(EntityFlameKnight.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAD_PREVIOUS_TARGET = EntityDataManager.createKey(EntityFlameKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Optional<IBlockState>> BLOCK_HEAD = EntityDataManager.<Optional<IBlockState>>createKey(EntityFlameKnight.class, DataSerializers.OPTIONAL_BLOCK_STATE);

    public void setComboMode(boolean value) {this.dataManager.set(COMBO_MODE, Boolean.valueOf(value));}
    public boolean isComboMode() {return this.dataManager.get(COMBO_MODE);}
    public void setBlockMotion(boolean value) {this.dataManager.set(BLOCK_MOTION, Boolean.valueOf(value));}
    public void setSwingOne(boolean value) {this.dataManager.set(SWING_ONE, Boolean.valueOf(value));}
    public void setSwingTwo(boolean value) {this.dataManager.set(SWING_TWO, Boolean.valueOf(value));}
    public void setSwingThree(boolean value) {this.dataManager.set(SWING_THREE, Boolean.valueOf(value));}
    public void setSmallStomp(boolean value) {this.dataManager.set(SMALL_STOMP, Boolean.valueOf(value));}
    public void setOverheadDash(boolean value) {this.dataManager.set(OVERHEAD_DASH, Boolean.valueOf(value));}
    public void setFlameSling(boolean value) {this.dataManager.set(FLAME_SLING, Boolean.valueOf(value));}
    public void setFlameSlingTriple(boolean value) {this.dataManager.set(FLAME_SLING_TRIPLE, Boolean.valueOf(value));}
    public void setSummonMeteor(boolean value) {this.dataManager.set(SUMMON_METEOR, Boolean.valueOf(value));}
    public void setSummonFlame(boolean value) {this.dataManager.set(SUMMON_FLAME, Boolean.valueOf(value));}
    public void setBeginCombo(boolean value) {this.dataManager.set(BEGIN_COMBO, Boolean.valueOf(value));}
    public void setEndCombo(boolean value) {this.dataManager.set(END_COMBO, Boolean.valueOf(value));}
    public void setGroundSweep(boolean value) {this.dataManager.set(GROUND_SWEEP, Boolean.valueOf(value));}
    public void setComboSwingOne(boolean value) {this.dataManager.set(COMBO_SWING_ONE, Boolean.valueOf(value));}
    public void setComboSwingTwo(boolean value) {this.dataManager.set(COMBO_SWING_TWO, Boolean.valueOf(value));}
    public void setComboFarDash(boolean value) {this.dataManager.set(COMBO_FAR_DASH, Boolean.valueOf(value));}
    public void setComboWaterfoul(boolean value) {this.dataManager.set(COMBO_WATERFOUL, Boolean.valueOf(value));}
    public void setComboStrike(boolean value) {this.dataManager.set(COMBO_STRIKE, Boolean.valueOf(value));}
    public void setDrinkPotion(boolean value) {this.dataManager.set(DRINK_POTION, Boolean.valueOf(value));}
    public void setLostHead(boolean value) {this.dataManager.set(LOST_HEAD, Boolean.valueOf(value));}
    public void setPhaseTransistion(boolean value) {this.dataManager.set(PHASE_TRANSISTION, Boolean.valueOf(value));}
    public void setReleaseFlames(boolean value) {this.dataManager.set(RELEASE_FLAMES, Boolean.valueOf(value));}
    public void setJumpWithoutAttack(boolean value) {this.dataManager.set(JUMP_WITHOUT_ATTACK, Boolean.valueOf(value));}
    public void setJumpWithAttack(boolean value) {this.dataManager.set(JUMP_WITH_ATTACK, Boolean.valueOf(value));}
    public void setSummon(boolean value) {this.dataManager.set(SUMMON, Boolean.valueOf(value));}
    public void setDeathPlay(boolean value) {this.dataManager.set(DEATH_PlAY, Boolean.valueOf(value));}
    public void setSpamDetected(boolean value) {this.dataManager.set(SPAM_DETECTED, Boolean.valueOf(value));}
    public boolean isBlockMotion() {return this.dataManager.get(BLOCK_MOTION);}
    public boolean isSwingOne() {return this.dataManager.get(SWING_ONE);}
    public boolean isSwingTwo() {return this.dataManager.get(SWING_TWO);}
    public boolean isSwingThree() {return this.dataManager.get(SWING_THREE);}
    public boolean isSmallStomp() {return this.dataManager.get(SMALL_STOMP);}
    public boolean isOverheadDash() {return this.dataManager.get(OVERHEAD_DASH);}
    public boolean isFlameSling() {return this.dataManager.get(FLAME_SLING);}
    public boolean isFlameSlingTriple() {return this.dataManager.get(FLAME_SLING_TRIPLE);}
    public boolean isSummonMeteor() {return this.dataManager.get(SUMMON_METEOR);}
    public boolean isSummonFlame() {return this.dataManager.get(SUMMON_FLAME);}
    public boolean isBeginCombo() {return this.dataManager.get(BEGIN_COMBO);}
    public boolean isEndCombo() {return this.dataManager.get(END_COMBO);}
    public boolean isGroundSweep() {return this.dataManager.get(GROUND_SWEEP);}
    public boolean isComboSwingOne() {return this.dataManager.get(COMBO_SWING_ONE);}
    public boolean isComboSwingTwo() {return this.dataManager.get(COMBO_SWING_TWO);}
    public boolean isComboFarDash() {return this.dataManager.get(COMBO_FAR_DASH);}
    public boolean isComboStrike() {return this.dataManager.get(COMBO_STRIKE);}
    public boolean isComboWaterFoul() {return this.dataManager.get(COMBO_WATERFOUL);}
    public boolean isDrinkPotion() {return this.dataManager.get(DRINK_POTION);}
    public boolean isReleaseFlames() {return this.dataManager.get(RELEASE_FLAMES);}
    public boolean isJumpWithoutAttack() {return this.dataManager.get(JUMP_WITHOUT_ATTACK);}
    public boolean isJumpWitAttack() {return this.dataManager.get(JUMP_WITH_ATTACK);}
    public boolean isSummon() {return this.dataManager.get(SUMMON);}
    public boolean isDeathPlay() {return this.dataManager.get(DEATH_PlAY);}
    public boolean isSetSpawnLoc() {
        return this.dataManager.get(SET_SPAWN_LOC);
    }
    public boolean isSpamDetected() {return this.dataManager.get(SPAM_DETECTED);}
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

    public boolean isLostHead() {return this.dataManager.get(LOST_HEAD);}
    public boolean isPhaseTransition() {return this.dataManager.get(PHASE_TRANSISTION);}
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}

    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIMATION_WALK_LOWER = "walk_lower";
    private final String ANIMATION_WALK_UPPER = "walk_upper";
    private final String ANIMATION_IDLE = "idle";

    //Block
    private final String ANIM_BLOCK = "block";
    //Regular Close Attacks
    private final String ANIM_SWING_ONE = "swing";
    private final String ANIM_SWING_TWO = "swing2";
    private final String ANIM_SWING_THREE = "swing3";
    private final String ANIM_GROUND_SWEEP = "ground_sweep";
    //Regular Close-Mid Attacks
    private final String ANIM_SMALL_STOMP = "small_stomp";

    private final String ANIM_OVERHEAD_DASH = "overhead_dash";

    //Regular-Far Attacks
    private final String ANIM_FLAME_SLING = "flame_sling";
    private final String ANIM_FLAME_SWLING_TRIPLE = "flame_sling_triple";
    private final String ANIM_SUMMON_METEOR = "summon_meteor";
    private final String ANIM_SUMMON_FLAME = "summon_flame";

    //Combo Related Animations
    private final String ANIM_COMBO_BEGIN = "prepare_combo";
    private final String ANIM_COMBO_END = "end_combo";
    private final String ANIM_COMBO_ARMS = "combo_arms";
    private final String ANIM_COMBO_FAR_DASH = "combo_dash_far";
    private final String ANIM_COMBO_SWING_ONE = "combo_swing";
    private final String ANIM_COMBO_SWING_TWO = "combo_swing2";
    private final String ANIM_COMBO_WATER_FOUL = "combo_waterfoul";
    private final String ANIM_COMBO_STRIKE = "combo_strike";

    //2nd Phase
    private final String ANIM_RELEASE_FLAMES = "release_flames";
    private final String ANIM_JUMP_WITHOUT = "jump";
    private final String ANIM_JUMP_WITH_ATTACK = "jump_with_attack";

    //Misc
    private final String ANIM_DRINK_POTION = "drink_potion";
    private final String ANIM_PHASE_TRANSITION = "phase_transition";
    private final String ANIM_LOST_HEAD = "lost_head_hold";
    private final String ANIM_SET_SUMMON = "summon";
    private final String ANIM_DEATH = "death";
    private final String ANIM_SPAM_DETECTED = "quick_aoe";

    private boolean startFlameSpawnsParticles = false;

    private boolean currentlyInIFrame = false;
    private int shakeTime = 0;
    Supplier<Projectile> flame_sling_projectiles = () -> new ProjectileFlameSling(world, this, 15, null);

    public EntityFlameKnight(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7f, 2.7f);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.wantedDistance = 4;
        this.iAmBossMob = true;
        this.combo_aggrivation = 0;

    }

    public EntityFlameKnight(World worldIn) {
        super(worldIn);
        this.setSize(0.7f, 2.7f);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.wantedDistance = 4;
        this.iAmBossMob = true;
        this.combo_aggrivation = 0;
        this.experienceValue = 400;
    }

    public EntityFlameKnight(World worldIn, int timesUsed, BlockPos pos) {
        super(worldIn);
        this.setSize(0.7f, 2.7f);
        this.timesUsed = timesUsed;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.timesUsed++;
        this.doBossReSummonScaling();
        this.wantedDistance = 4;
        this.iAmBossMob = true;
        this.combo_aggrivation = 0;
        this.experienceValue = 400;
    }


    public void onSummon(EntityLivingBase actor) {
        this.setPosition(actor.posX, actor.posY, actor.posZ);
        BlockPos pos = new BlockPos(actor.posX, actor.posY, actor.posZ);
        this.setSpawnLocation(pos);
        this.setSetSpawnLoc(true);
        world.spawnEntity(this);
        this.setFullBodyUsage(true);
        this.setSummon(true);
        this.setImmovable(true);
        this.lockLook = true;
        actor.setDead();
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 40);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 76);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 90);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 120);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.4f, 0.4f / (rand.nextFloat() * 0.4f + 0.2f)), 133);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 142);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 203);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 240);
        addEvent(()-> {
            this.setSummon(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            this.lockLook = false;
        }, 245);
    }

    public void onSummonViaBlock(BlockPos posFrom) {
        this.setPosition(posFrom.getX() + 0.5, posFrom.getY(), posFrom.getZ() + 0.5);
        this.setSpawnLocation(posFrom);
        this.setSetSpawnLoc(true);
        this.setFullBodyUsage(true);
        this.setSummon(true);
        this.setImmovable(true);
        this.lockLook = true;
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 40);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 76);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 90);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 120);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.4f, 0.4f / (rand.nextFloat() * 0.4f + 0.2f)), 133);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 142);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 203);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 240);
        addEvent(()-> {
            this.setSummon(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            this.lockLook = false;
        }, 245);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.burning_knight_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.burning_knight_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.burning_knight_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.burning_knight_damage);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIFlameKnightAttack<>(this, 1.1, 20, 20, 0.3F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ITEM_HAND, ItemStack.EMPTY);
        this.dataManager.register(BLOCK_HEAD,  Optional.of(Blocks.BARRIER.getDefaultState()));
        this.dataManager.register(COMBO_MODE, Boolean.valueOf(false));
        this.dataManager.register(BLOCK_MOTION, Boolean.valueOf(false));
        this.dataManager.register(SWING_ONE, Boolean.valueOf(false));
        this.dataManager.register(SWING_TWO, Boolean.valueOf(false));
        this.dataManager.register(SWING_THREE, Boolean.valueOf(false));
        this.dataManager.register(SMALL_STOMP, Boolean.valueOf(false));
        this.dataManager.register(OVERHEAD_DASH, Boolean.valueOf(false));
        this.dataManager.register(FLAME_SLING, Boolean.valueOf(false));
        this.dataManager.register(FLAME_SLING_TRIPLE, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_FLAME, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_METEOR, Boolean.valueOf(false));
        this.dataManager.register(END_COMBO, Boolean.valueOf(false));
        this.dataManager.register(BEGIN_COMBO, Boolean.valueOf(false));
        this.dataManager.register(GROUND_SWEEP, Boolean.valueOf(false));
        this.dataManager.register(COMBO_WATERFOUL, Boolean.valueOf(false));
        this.dataManager.register(COMBO_STRIKE, Boolean.valueOf(false));
        this.dataManager.register(COMBO_FAR_DASH, Boolean.valueOf(false));
        this.dataManager.register(COMBO_SWING_TWO, Boolean.valueOf(false));
        this.dataManager.register(COMBO_SWING_ONE, Boolean.valueOf(false));
        this.dataManager.register(DRINK_POTION, Boolean.valueOf(false));
        this.dataManager.register(LOST_HEAD, Boolean.valueOf(false));
        this.dataManager.register(PHASE_TRANSISTION, Boolean.valueOf(false));
        this.dataManager.register(RELEASE_FLAMES, Boolean.valueOf(false));
        this.dataManager.register(JUMP_WITH_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(JUMP_WITHOUT_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON, Boolean.valueOf(false));
        this.dataManager.register(DEATH_PlAY, Boolean.valueOf(false));
        this.dataManager.register(SPAM_DETECTED, Boolean.valueOf(false));
        this.dataManager.register(SET_SPAWN_LOC, Boolean.valueOf(false));
        this.dataManager.register(HAD_PREVIOUS_TARGET, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        //
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Combo_Mode", this.isComboMode());
        nbt.setBoolean("Block_Motion", this.isBlockMotion());
        nbt.setBoolean("Swing_One", this.isSwingOne());
        nbt.setBoolean("Swing_Two", this.isSwingTwo());
        nbt.setBoolean("Swing_Three", this.isSwingThree());
        nbt.setBoolean("Small_Stomp", this.isSmallStomp());
        nbt.setBoolean("Overhead_Dash", this.isOverheadDash());
        nbt.setBoolean("Flame_Sling", this.isFlameSling());
        nbt.setBoolean("Flame_Sling_Triple", this.isFlameSlingTriple());
        nbt.setBoolean("Summon_Flame", this.isSummonFlame());
        nbt.setBoolean("Summon_Meteor", this.isSummonMeteor());
        nbt.setBoolean("Ground_Sweep", this.isGroundSweep());
        nbt.setBoolean("Begin_Combo", this.isBeginCombo());
        nbt.setBoolean("End_Combo", this.isEndCombo());
        nbt.setBoolean("Combo_Swing_One", this.isComboSwingOne());
        nbt.setBoolean("Combo_Swing_Two", this.isComboSwingTwo());
        nbt.setBoolean("C_Waterfoul", this.isComboWaterFoul());
        nbt.setBoolean("C_Far_Dash", this.isComboFarDash());
        nbt.setBoolean("C_Strike", this.isComboStrike());
        nbt.setBoolean("Drink_Potion", this.isDrinkPotion());
        nbt.setBoolean("Phase_Transition", this.isPhaseTransition());
        nbt.setBoolean("Lost_Head", this.isLostHead());
        nbt.setBoolean("Release_Flames", this.isReleaseFlames());
        nbt.setBoolean("Jump_With_Attack", this.isJumpWitAttack());
        nbt.setBoolean("Jump_Without", this.isJumpWithoutAttack());
        nbt.setBoolean("Summon", this.isSummon());
        nbt.setBoolean("Shaking", this.isShaking());
        nbt.setBoolean("Death_Play", this.isDeathPlay());
        nbt.setBoolean("Had_Target", this.isHadPreviousTarget());
        nbt.setBoolean("Spam_Detected", this.isSpamDetected());
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
        nbt.setBoolean("Set_Spawn_Loc", this.dataManager.get(SET_SPAWN_LOC));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setComboMode(nbt.getBoolean("Combo_Mode"));
        this.setBlockMotion(nbt.getBoolean("Block_Motion"));
        this.setSwingOne(nbt.getBoolean("Swing_One"));
        this.setSwingTwo(nbt.getBoolean("Swing_Two"));
        this.setSwingThree(nbt.getBoolean("Swing_Three"));
        this.setSmallStomp(nbt.getBoolean("Small_Stomp"));
        this.setOverheadDash(nbt.getBoolean("Overhead_Dash"));
        this.setFlameSling(nbt.getBoolean("Flame_Sling"));
        this.setFlameSlingTriple(nbt.getBoolean("Flame_Sling_Triple"));
        this.setSummonFlame(nbt.getBoolean("Summon_Flame"));
        this.setSummonMeteor(nbt.getBoolean("Summon_Meteor"));
        this.setGroundSweep(nbt.getBoolean("Ground_Sweep"));
        this.setBeginCombo(nbt.getBoolean("Begin_Combo"));
        this.setEndCombo(nbt.getBoolean("End_Combo"));
        this.setShaking(nbt.getBoolean("Shaking"));
        this.setComboSwingOne(nbt.getBoolean("Combo_Swing_One"));
        this.setComboSwingTwo(nbt.getBoolean("Combo_Swing_Two"));
        this.setComboWaterfoul(nbt.getBoolean("C_Waterfoul"));
        this.setComboFarDash(nbt.getBoolean("C_Far_Dash"));
        this.setComboStrike(nbt.getBoolean("C_Strike"));
        this.setDrinkPotion(nbt.getBoolean("Drink_Potion"));
        this.setPhaseTransistion(nbt.getBoolean("Phase_Transition"));
        this.setLostHead(nbt.getBoolean("Lost_Head"));
        this.setReleaseFlames(nbt.getBoolean("Release_Flames"));
        this.setJumpWithoutAttack(nbt.getBoolean("Jump_Without"));
        this.setJumpWithAttack(nbt.getBoolean("Jump_With_Attack"));
        this.setSummon(nbt.getBoolean("Summon"));
        this.setHadPreviousTarget(nbt.getBoolean("Had_Target"));
        this.setDeathPlay(nbt.getBoolean("Death_Play"));
        this.setSpamDetected(nbt.getBoolean("Spam_Detected"));
        this.dataManager.set(SET_SPAWN_LOC, nbt.getBoolean("Set_Spawn_Loc"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateAttack));
        data.addAnimationController(new AnimationController(this, "block_controller", 0, this::predicateBlock));
        data.addAnimationController(new AnimationController(this, "head_hold_controller", 0, this::predicateHead));
    }

    private <E extends IAnimatable>PlayState predicateHead(AnimationEvent<E> event) {
        if(this.isLostHead()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LOST_HEAD, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateBlock(AnimationEvent<E> event) {
        if(this.isSummon()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SET_SUMMON, false));
            return PlayState.CONTINUE;
        }
        if(this.isDeathPlay()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DEATH, false));
            return PlayState.CONTINUE;
        }
        if(this.isPhaseTransition()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PHASE_TRANSITION, false));
            return PlayState.CONTINUE;
        }
        if(this.isBlockMotion()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLOCK, false));
            return PlayState.CONTINUE;
        }
        if(this.isDrinkPotion()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DRINK_POTION, false));
            return PlayState.CONTINUE;
        }
        if(this.isBeginCombo()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_BEGIN, false));
            return PlayState.CONTINUE;
        } else if (this.isEndCombo()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_END, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }


    private <E extends IAnimatable>PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isBlockMotion() && !this.isBeginCombo() && !this.isEndCombo() && !this.isSummon() && !this.isDeathPlay()) {

                if(this.isJumpWitAttack()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_JUMP_WITH_ATTACK, false));
                }
                if(this.isJumpWithoutAttack()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_JUMP_WITHOUT, false));
                }
                 if(this.isComboFarDash()) {
                      event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_FAR_DASH, false));
                 }
                 if(this.isComboStrike()) {
                      event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_STRIKE, false));
                 }
                 if(this.isComboSwingOne()) {
                       event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_SWING_ONE, false));
                 }
                 if(this.isComboSwingTwo()) {
                       event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_SWING_TWO, false));
                 }
                 if(this.isComboWaterFoul()) {
                     event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_WATER_FOUL, false));
                 }
                 if(this.isReleaseFlames()) {
                     event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_RELEASE_FLAMES, false));
                 }
                if (this.isSwingOne()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_ONE, false));
                }
                if (this.isSwingTwo()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_TWO, false));
                }
                if (this.isSwingThree()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_THREE, false));
                }
                if (this.isSmallStomp()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SMALL_STOMP, false));
                }
                if (this.isOverheadDash()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_OVERHEAD_DASH, false));
                }
                if (this.isFlameSling()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLAME_SLING, false));
                }
                if (this.isFlameSlingTriple()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLAME_SWLING_TRIPLE, false));
                }
                if (this.isSummonFlame()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_FLAME, false));
                }
                if (this.isSummonMeteor()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_METEOR, false));
                }
                if (this.isGroundSweep()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_GROUND_SWEEP, false));
                }
                if(this.isSpamDetected()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SPAM_DETECTED, false));
                }

            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!this.isFightMode() && !this.isBlockMotion() && !this.isSummon() && !this.isDeathPlay()) {
            if(!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATION_WALK_UPPER, true));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        if(!this.isFullBodyUsage() && !(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATION_WALK_LOWER, true));
                return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFightMode() && !this.isBlockMotion() && !this.isSummon() && !this.isDeathPlay()) {
            if (event.getLimbSwingAmount() > -0.09F && event.getLimbSwingAmount() < 0.09F) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATION_IDLE, true));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public void doBlockAction() {
        this.setBlockMotion(true);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> {
            this.setBlockMotion(false);
            this.blockTimerCooldown = 80;
            }, 15);
    }
    private int blockTimerCooldown = (int) (MobConfig.burning_knight_block_cooldown * 20);
    private int damageTaken = 0;


    private float combo_aggrivation;

    private double combo_degradation;

    private final double getHealthTooHealAmount = this.getMaxHealth() * MobConfig.burning_knight_heal_amount;

    private int potionDrankAmount = 0;

    private int potionCooldown = 0;
    private boolean hasNotDrankPotion = false;

    private boolean hasStartedPhase = false;

    private boolean fixState = false;

    private int stopSpammingAttacks = 0;
    private int reCheckDelay = 80;
    private boolean setSpamCheck = false;
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.shakeTime--;
        this.bossInfo.setPercent(getHealth() / getMaxHealth());
        //timer for Blocking
        blockTimerCooldown--;
        if(this.isComboMode()) {
            combat_delay--;
        }
        //timer for Drinking Potions
        potionCooldown--;
        if(potionCooldown < 0) {
            hasNotDrankPotion = false;
        }

        if(world.isRemote && ticksExisted == 1 && ModConfig.experimental_features) {
            this.playMusic(this);
        }

        if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
            Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());

            double distSq = this.getDistanceSq(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
            double distance = Math.sqrt(distSq);
            //This basically makes it so the Lamentor will be teleported if they are too far away from the Arena
            if(!world.isRemote) {
                if (distance > 30) {
                    this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                }
            }
        }

        EntityLivingBase target = this.getAttackTarget();
        //Target Tracking
        if(!world.isRemote) {
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
        }

        if(target != null && !world.isRemote) {
            double HealthChange = this.getHealth()/this.getMaxHealth();
            if(HealthChange <= 0.5 && !this.isFightMode() && !this.isLostHead()) {
                //Phase Transition
                if(this.isComboMode()) {
                    this.setComboMode(false);
                }
                //Initiates the animation and stuff
                if(!hasStartedPhase) {
                    this.performPhaseTransition();
                }
            }

            //this is to prevent circle strafing around the boss
            if(reCheckDelay > 0) {
                if(this.hurtTime > 0) {
                    stopSpammingAttacks++;
                }
                if(stopSpammingAttacks > 20) {
                    //do a quick self push back
                    this.setSpamCheck = true;

                }
                reCheckDelay--;
            }
            if(reCheckDelay < 1) {
                stopSpammingAttacks = 0;
                reCheckDelay = 100;
                this.setSpamCheck = false;
            }


            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            this.movementBoss(target, distSq, this.getEntitySenses().canSee(target));

            if(wantedDistance == 4) {
                if(this.hurtTime > 0 && !this.isComboMode() || world.rand.nextInt(40) == 0 && !this.isComboMode()) {
                    this.damageTaken++;
                }

                if(damageTaken >= 50) {
                    this.wantedDistance = 14;
                }

            }

            if(wantedDistance == 14) {
                if(this.hurtTime > 0) {
                    damageTaken -=2;
                }

                if(!hasNotDrankPotion && !this.isComboMode() && !this.isFightMode() && MobConfig.knight_enable_potion_use) {
                    double currHealth = this.getHealth() / this.getMaxHealth();
                    if((currHealth - getHealthTooHealAmount) < 1 && currHealth >= 0.75 && potionDrankAmount <= 1) {
                        this.healWithPotion();
                    } else if (currHealth < 0.75 && currHealth >= 0.5 && potionDrankAmount <= 2) {
                        this.healWithPotion();
                    } else if (currHealth < 0.45 && currHealth >= 0.25 && potionDrankAmount <= 3) {
                        this.healWithPotion();
                    } else if (currHealth < 0.25 && currHealth >= 0.03 && potionDrankAmount <= 4) {
                        this.healWithPotion();
                    }
                }

                if(this.isComboMode()) {
                    this.wantedDistance = 4;
                    this.damageTaken = 0;
                }
                if(damageTaken <= 0) {
                    this.wantedDistance = 4;
                }
            }
        } else if (target == null && (this.getHealth() / this.getMaxHealth()) > 0.95 && !fixState) {
            if(!world.isRemote) {
                this.setLostHead(false);
                this.hasStartedPhase = false;
               // this.equipBlockHead(HEAD_REPLACEMENT.HEAD, Blocks.BARRIER.getDefaultState());
                this.potionCooldown = 0;
                this.fixState = true;
            }

        }

        double HealthChange = this.getHealth()/this.getMaxHealth();
        combo_degradation = (HealthChange >= 0.5) ? HealthChange * 2 : 1;
        //allows for aggrivation to get quicker over the more health it loses
        if(combo_aggrivation >= combo_degradation && !this.isComboMode()) {
            if(!this.isFightMode()) {
                this.setBeginComboMode();
            }
        }
        //once it's in this state and reaches zero it ends combo mode
        if(this.isComboMode()) {
            if(combo_aggrivation <= 0) {
                if(!this.isFightMode()) {
                    this.setEndComboMode();
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
                    boss_spawner.setState(BlockEnumBossSummonState.INACTIVE, this.timesUsed, "flame_knight");
                }
            }
        }
    }

    private static final ResourceLocation LOO_RESET = new ResourceLocation(ModReference.MOD_ID, "burning_arena_reset");

    private void resetBossTask() {
        this.setImmovable(true);
        this.setHadPreviousTarget(false);
        BlockPos pos = this.getSpawnLocation();
        EntityPyre pyre_reset = new EntityPyre(world);
        pyre_reset.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        world.spawnEntity(pyre_reset);
        world.setBlockState(pos.add(0, 2, 0), Blocks.CHEST.getDefaultState());
        TileEntity te = world.getTileEntity(pos.add(0, 2, 0));
        if(te instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) te;
            chest.setLootTable(LOO_RESET, rand.nextLong());
        }
        this.experienceValue = 0;
        this.setDropItemsWhenDead(false);
        this.setDead();
    }

    private void performPhaseTransition() {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setPhaseTransistion(true);
        this.setImmovable(true);
        this.hasStartedPhase = true;
        this.fixState = false;
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 6);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 25);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 30);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 60);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 75);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 105);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 110);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 205);

        addEvent(()-> {
          //  this.equipBlockHead(HEAD_REPLACEMENT.HEAD, Blocks.FIRE.getDefaultState());
            this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
            this.setLostHead(true);
            EntityLivingBase target = this.getAttackTarget();
            if(target != null) {
                new ActionReleaseFlames(60).performAction(this, target);
            }
        }, 145);


        addEvent(()-> {
            this.setPhaseTransistion(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            this.setFightMode(false);
            this.setComboMode(true);
        }, 220);
    }



    private void healWithPotion() {
        this.setFightMode(true);
        this.setDrinkPotion(true);
        this.potionCooldown = MobConfig.burning_knight_heal_cooldown * 20;
        this.hasNotDrankPotion = true;
        this.potionDrankAmount++;
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 20);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 55);

        addEvent(()-> this.equipBlock(KNIGHT_HAND.HAND, new ItemStack(ModItems.FAKE_HEALING_POTION)), 8);

        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 30);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 33);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 35);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 37);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 40);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 43);
        addEvent(()-> {
            this.heal((float) getHealthTooHealAmount);
            this.equipBlock(KNIGHT_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM));
            }, 45);

        addEvent(()-> {
            this.setFightMode(false);
            this.setDrinkPotion(false);
        }, 58);
    }
    //Combo Animation Begin
    private int combat_delay = 0;

    private void resetAnyTriggers() {
        this.setSwingTwo(false);
        this.setSwingTwo(false);
        this.setSwingThree(false);
        this.setSmallStomp(false);
        this.setOverheadDash(false);
        this.setFlameSling(false);
        this.setFlameSlingTriple(false);
        this.setSummonFlame(false);
        this.setJumpWithoutAttack(false);
        this.setJumpWithAttack(false);
        this.setSpamDetected(false);
    }
    private void setBeginComboMode() {
        this.clearEvents();
        this.resetAnyTriggers();
        this.setBeginCombo(true);
        this.setImmovable(true);
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_SLING, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f)), 35);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_SLING, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f)), 46);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_SLING, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f)), 60);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 65);

        addEvent(()-> {
            this.setBeginCombo(false);
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            this.setComboMode(true);
            //addEvent(()-> this.setFightMode(false), 25);
            this.setFightMode(false);
            double HealthChange = this.getHealth() / this.getMaxHealth();
            this.combat_delay = 20;
            //This makes it so that combo aggrivation last longer on lower health, but gets activated quicker
            if(HealthChange > 0.75) {
              combo_aggrivation -= 1;
            } else if (HealthChange <= 0.75 && HealthChange > 0.5) {
                combo_aggrivation += 0.5;
            } else if (HealthChange <= 0.5 && HealthChange > 0.25) {
                combo_aggrivation += 1;
            } else {
                combo_aggrivation += 1;
            }
        }, 75);
    }

    //End Combo Begin
    private void setEndComboMode() {
        this.setEndCombo(true);
        this.setImmovable(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);

        addEvent(()-> {
            this.setEndCombo(false);
            this.setFightMode(false);
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            this.setComboMode(false);
        }, 15);
    }

    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    private final float strafeAmount = 0.3F;

    private static final float STRAFING_STOP_FACTOR = 0.75f;
    private static final float STRAFING_BACKWARDS_FACTOR = 0.25f;
    private static final float STRAFING_DIRECTION_TICK = 20;
    private static final float STRAFING_DIRECTION_CHANGE_CHANCE = 0.3f;

    public void movementBoss(EntityLivingBase target, double distSq, boolean canSee) {
        int maxAttackDistance = wantedDistance * wantedDistance;
        if (!this.lockLook) {
            if(!this.isJumpWithoutAttack() && !this.isJumpWitAttack()) {
                if (wantedDistance == 4 || this.isComboMode()) {
                    if (distSq <= maxAttackDistance && canSee) {
                        this.getNavigator().clearPath();
                        ++this.strafingTime;
                    } else {
                        double getHealth = this.getHealth() / this.getMaxHealth();
                        if (getHealth <= 0.5 && !this.isComboMode()) {
                            this.getNavigator().tryMoveToEntityLiving(target, 0.7);
                            this.strafingTime = -1;
                        } else {
                            this.getNavigator().tryMoveToEntityLiving(target, 1.1);
                            this.strafingTime = -1;
                        }
                    }
                } else if (wantedDistance == 14) {
                    if (distSq <= maxAttackDistance && canSee) {
                        this.getNavigator().clearPath();
                        //System.out.println("Within 14 block distance");
                        ++this.strafingTime;

                    } else {
                        // System.out.println("Gathering Navigator to move too");
                        this.getNavigator().tryMoveToEntityLiving(target, 1.1);
                        this.strafingTime = -1;
                    }
                }

            } else {
                this.strafingTime = -1;
                this.getNavigator().clearPath();
                this.getLookHelper().setLookPositionWithEntity(target, 35, 35);
            }


            if (this.strafingTime >= STRAFING_DIRECTION_TICK) {
                if ((double) this.getRNG().nextFloat() < STRAFING_DIRECTION_CHANGE_CHANCE) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double) this.getRNG().nextFloat() < STRAFING_DIRECTION_CHANGE_CHANCE) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }


            if(this.strafingTime > -1 && wantedDistance == 14 && !this.isComboMode()) {
                if(distSq < maxAttackDistance - 4) {
                   // System.out.println("Trying to strafe away from enemy");
                    double d0 = (this.posX - target.posX) * 0.009;
                    double d1 = (this.posY - target.posY) * 0.005;
                    double d2 = (this.posZ - target.posZ) * 0.009;
                    this.addVelocity(d0, d1, d2);
                    this.faceEntity(target, 35, 35);
                    this.getLookHelper().setLookPositionWithEntity(target, 35, 35);
                } else {
                   // System.out.println("Moving through strafe system");
                    if(distSq > maxAttackDistance * STRAFING_STOP_FACTOR) {
                            this.strafingBackwards = false;
                    } else if (distSq >= maxAttackDistance - 4) {
                        this.strafingBackwards = true;
                    }
                    this.getMoveHelper().strafe(1 * this.strafeAmount, (this.strafingClockwise ? 2 : -2) * this.strafeAmount);
                    this.faceEntity(target, 35, 35);
                }
            } else if (this.strafingTime > -1 && wantedDistance == 4 || this.strafingTime > -1 && this.isComboMode()) {
                if (distSq > maxAttackDistance * STRAFING_STOP_FACTOR) {
                    this.strafingBackwards = false;
                } else if (distSq < maxAttackDistance * STRAFING_BACKWARDS_FACTOR) {
                    this.strafingBackwards = true;
                }

                this.getMoveHelper().strafe((this.strafingBackwards ? -1 : 1) * this.strafeAmount, (this.strafingClockwise ? 1 : -1) * this.strafeAmount);
                this.faceEntity(target, 35, 35);
            } else {
                this.getLookHelper().setLookPositionWithEntity(target, 35, 35);
            }
        }
    }

    /**
     * Ideal Attack Pattern
     * When Close, a flurry of attacks, when entering what's called a ready stance
     * the knight will enter combo mode, where 2-3 attacks can be performed quickly
     * This knight will mostly specialize in melee
     * Phase 1
     * 2-3 different close melee attacks - DONE
     * 1 Pierce close-far attack - DONE
     * 1 overhead close-far attack - DONE
     * 1 Stomp attack - DONE
     *
     * combo attacks will be a float that will add with each attack the knight does, it will then do an animation such as prepping the sword
     * inspirations can be waterfoul dance from Elden Ring
     *
     *
     * When Far, The flame knight will reign meteors on the foe, or do massive AOE attacks
     * while strafing from the enemy
     *
     * Far Attacks include
     * AOE Jump, the knight will jump in the air and slam the ground causing massive AOE to the arena
     * FLAME SHOOT - something like flame particles shooting across in a 5 block width, does this 3 times
     * Summon Fire Ring, summons a ring that shoots fireballs
     *
     * The knight should feel fast and fluid in it's movements and be difficult to find an opening for
     *
     * maybe something like armor is useless in the fight, the damage will go through armor, as if it we're a flame searing skin
     * maybe also a block while it's not attacking, not available during combo mode
     *
     * @param target
     * @param distanceSq
     * @param strafingBackwards
     * @return
     */

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && !this.isBlockMotion() && !this.isBeginCombo() && !this.isEndCombo() && !this.isDrinkPotion() && !this.isPhaseTransition() && !this.isSummon() && !this.isDeathPlay()) {

            if (this.isComboMode() && combat_delay < 0) {
                //Combo Attacks
                List<Consumer<EntityLivingBase>> combo_attacks = new ArrayList<>(Arrays.asList(combo_first_strike, combo_second_strike, combo_circle_strike, combo_waterfoul, combo_far_dash));
                double[] weights = {
                        (distance <= 4 && prevAttack != combo_first_strike) ? 1/distance : 0,
                        (distance <= 4 && prevAttack != combo_second_strike) ? 1/distance : 0,
                        (distance <= 8 && prevAttack != combo_circle_strike) ? 1/distance : 0,
                        (distance <= 8 && prevAttack != combo_waterfoul) ? 1/distance : 0,
                        (distance <= 21 && distance >= 7) ? distance * 0.02 : 0
                };

                prevAttack = ModRand.choice(combo_attacks, rand, weights).next();
                prevAttack.accept(target);
            } else {
                List<Consumer<EntityLivingBase>> close_attacks = new ArrayList<>(Arrays.asList(swing_one_attack, swing_two_attack, swing_three_attack, small_stomp, overhead_Dash, Flame_Sling, summon_flame, jump_without_attack, stop_spam_attacks));
                double[] weights = {
                        (distance <= 3 && prevAttack != swing_one_attack) ? 1 / distance : 0, //Swing One
                        (distance <= 7 && prevAttack != swing_two_attack) ? 1 / distance : 0, //Swing Two, uses flame slings in 2nd Phase
                        (distance <= 3 && prevAttack != swing_three_attack) ? 1 / distance : 0, // Swing Three
                        (distance <= 8 && prevAttack != small_stomp) ? 1 / distance : 0, // Uses another melee attack but switches to a stomp following 2nd Phase
                        (distance <= 16 && distance >= 7 && prevAttack != overhead_Dash && wantedDistance == 4) ? 1 / distance : (distance <= 12 && distance >= 7 && prevAttack != overhead_Dash) ? 1/distance : 0, //Spawns Flame SLings on 2nd Phase
                        (distance <= 21 && distance >= 12 && prevAttack != Flame_Sling) ? 1 / distance : 0, //Doesn't change during the fight
                        (distance <= 21 && distance >= 7 && prevAttack != summon_flame) ? 1 / distance : 0, //Changes to a Flame Barrage during 2nd phase
                        (distance <= 4 && prevAttack != jump_without_attack && wantedDistance == 14) ? 2/distance : (distance <= 4 && prevAttack != jump_without_attack) ? 1/distance : 0, //2nd Phase has a chance to do an attack while evading
                        (distance <= 4 && prevAttack != stop_spam_attacks && setSpamCheck) ? 150 : 0

                };

                prevAttack = ModRand.choice(close_attacks, rand, weights).next();
                prevAttack.accept(target);
            }

        }
        //old 60, 15
        return this.isComboMode() ? MobConfig.knight_aggrivate_state_cooldown : this.wantedDistance == 14 ? MobConfig.knight_cooldown_long_distance + 5 : distance >= 8 ? MobConfig.knight_cooldown_long_distance : HealthChange <= 0.5 ? MobConfig.knight_cooldown_short_distance - 5 : MobConfig.knight_cooldown_short_distance;
    }

    private final Consumer<EntityLivingBase> stop_spam_attacks = (target) -> {
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.lockLook =true;
      this.setFightMode(true);
      this.setSpamDetected(true);

      //do a quick AOE around
      addEvent(()-> {
          world.setEntityState(this, ModUtils.FIFTH_PARTICLE_BYTE);
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = (float) (this.getAttack() * 1.3);
          ModUtils.handleAreaImpact(3.25f, (e) -> damage, this, offset, source, 1.2f, 0, false);
          this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          this.setShaking(true);
          this.shakeTime = 7;
      }, 20);

        addEvent(()-> {
            this.setShaking(false);
        }, 28);

      addEvent(()-> {
          this.lockLook =false;
          this.setFightMode(false);
          this.setImmovable(false);
          this.setFullBodyUsage(false);
          this.setSpamDetected(false);
      }, 40);
    };

    private final Consumer<EntityLivingBase> jump_without_attack = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 3);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 20);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 25);
        addEvent(()-> this.currentlyInIFrame  =true, 6);
        addEvent(()-> this.currentlyInIFrame = false, 25);
      double getHealth = this.getHealth() / this.getMaxHealth();
      int randomInt = ModRand.range(1, 8);
      if(getHealth <= 0.5 && randomInt >= 6) {
          this.setJumpWithAttack(true);
          //jumps entity
          addEvent(()-> {
              Vec3d dirToo = this.getPositionVector().subtract(target.getPositionVector()).normalize();
              Vec3d jumpTooPos = this.getPositionVector().add(dirToo.scale(14));
              double distanceToo = this.getPositionVector().distanceTo(jumpTooPos);
              this.setImmovable(false);
              ModUtils.leapTowards(this, jumpTooPos, 1.4F, 0.25F);
          }, 9);

          addEvent(()-> new ActionQuickFlameSling(flame_sling_projectiles).performAction(this, target), 16);

          addEvent(()-> {
              chargeDir = null;
            this.setJumpWithAttack(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
          }, 29);
      } else {
          this.setJumpWithoutAttack(true);
          addEvent(()-> {
              Vec3d dirToo = this.getPositionVector().subtract(target.getPositionVector()).normalize();
              Vec3d jumpTooPos = this.getPositionVector().add(dirToo.scale(14));
              double distanceToo = this.getPositionVector().distanceTo(jumpTooPos);
              this.setImmovable(false);
              ModUtils.leapTowards(this, jumpTooPos, 1.4F, 0.25F);
          }, 9);

          addEvent(()-> {
              chargeDir = null;
              this.setJumpWithoutAttack(false);
              this.setFullBodyUsage(false);
              this.setFightMode(false);
          }, 29);
      }
    };

    private final Consumer<EntityLivingBase> combo_first_strike = (target) -> {
      this.setFightMode(true);
      this.setComboSwingOne(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 20);
      //this.holdPosition = true;

      addEvent(()-> {
        Vec3d savedPos = target.getPositionVector();
        this.lockLook = true;
        addEvent(()-> {
            this.setImmovable(false);
            //this.holdPosition = false;
            double distance = this.getPositionVector().distanceTo(savedPos);
            ModUtils.leapTowards(this, savedPos, (float) (distance * 0.18),0.25F);
        }, 10);
      }, 5);


      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.4f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 17);

      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.lockLook = false;
        this.setComboSwingOne(false);
        this.combo_aggrivation -= 0.2;
      }, 25);
    };

    private final Consumer<EntityLivingBase> combo_second_strike = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
     // this.holdPosition = true;
      this.setComboSwingTwo(true);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 35);

      addEvent(()-> {
          Vec3d savedPos = target.getPositionVector();
          this.lockLook = true;
          addEvent(()-> {
              this.setImmovable(false);
              //this.holdPosition = false;
              double distance = this.getPositionVector().distanceTo(savedPos);
              ModUtils.leapTowards(this, savedPos, (float) (distance * 0.18),0.25F);
          }, 10);
      }, 5);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.8, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.4f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 22);

      addEvent(()->{
          this.setImmovable(true);
          //this.holdPosition = true;
          }, 26);

      addEvent(()-> {
          this.lockLook =false;
          this.setImmovable(false);
          //this.holdPosition = false;
          this.setFullBodyUsage(false);
          this.setFightMode(false);
          this.setComboSwingTwo(false);
          this.combo_aggrivation -= 0.2;
      }, 38);
    };

    private final Consumer<EntityLivingBase> combo_far_dash = (target) -> {
      this.setImmovable(true);
     // this.holdPosition = true;
      this.setComboFarDash(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.9f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f)), 20);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 40);

      addEvent(()-> {
          this.lockLook = true;
          Vec3d savedPos = target.getPositionVector().add(ModUtils.yVec(0.5));
          addEvent(()-> {
              this.setImmovable(false);
              //this.holdPosition = false;
              this.DoDash(savedPos);
              this.startFlameSpawnsParticles = true;
              addEvent(()-> this.startFlameSpawnsParticles = false, 12);

          }, 12);
      }, 10);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
          float damage = (float) (this.getAttack() * 1.5);
          ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.9f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          double getHealth = this.getHealth()/this.getMaxHealth();
          if(getHealth <= 0.5) {
              new ActionQuickFlameSling(flame_sling_projectiles).performAction(this, target);
          }
          this.setShaking(true);
          this.shakeTime = 7;
      }, 27);

        addEvent(()-> {
            this.setShaking(false);
        }, 35);

      addEvent(()-> {
          this.setImmovable(true);
         // this.holdPosition = true;
          }, 30);

      addEvent(()-> {
          this.lockLook = false;
        this.setImmovable(false);
        //this.holdPosition = false;
        this.setComboFarDash(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.combo_aggrivation -= 0.27;
      }, 49);
    };

    private final Consumer<EntityLivingBase> combo_circle_strike = (target) -> {
      this.setComboStrike(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
      //this.holdPosition = true;
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 3);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 35);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 87);

      addEvent(()-> {
          this.lockLook = true;
          Vec3d savedPos = target.getPositionVector();
          addEvent(()-> {
              this.setImmovable(false);
             // this.holdPosition = false;
              double distance = this.getPositionVector().distanceTo(savedPos);
              ModUtils.leapTowards(this, savedPos, (float) (distance * 0.34),0.25F);
          }, 10);
      }, 3);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.4, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
          float damage = (float) (this.getAttack());
          ModUtils.handleAreaImpact(2F, (e) -> damage, this, offset, source, 0.4f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 17);

      addEvent(()-> {
            this.setImmovable(true);
           // this.holdPosition = true;
            this.lockLook =false;
            Vec3d savedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                //this.holdPosition = false;
                this.lockLook = true;
                double distance = this.getPositionVector().distanceTo(savedPos);
                ModUtils.leapTowards(this, savedPos, (float) (distance * 0.34),0.25F);
            }, 13);
      }, 32);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.4, 0.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
          float damage = (float) (this.getAttack());
          ModUtils.handleAreaImpact(2F, (e) -> damage, this, offset, source, 0.6f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          double getHealth = this.getHealth()/this.getMaxHealth();
          if(getHealth <= 0.5) {
              new ActionQuickFlameSling(flame_sling_projectiles).performAction(this, target);
          }
      }, 50);

      addEvent(()-> {
        this.lockLook =false;
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setComboStrike(false);
        this.combo_aggrivation -= 0.3;
      }, 93);
    };

    private final Consumer<EntityLivingBase> combo_waterfoul = (target) -> {
      this.setImmovable(true);
      //this.holdPosition = true;
      this.setFightMode(true);
      this.setComboWaterfoul(true);
      this.setFullBodyUsage(true);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 3);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.9f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 35);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 53);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 37);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 65);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 97);

      addEvent(()-> {
          this.lockLook = true;
          Vec3d savedPos = target.getPositionVector();
          addEvent(()-> {
              this.setImmovable(false);
              //this.holdPosition = false;
              double distance = this.getPositionVector().distanceTo(savedPos);
              ModUtils.leapTowards(this, savedPos, (float) (distance * 0.4),0.25F);
          }, 10);
      }, 10);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.7, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
          float damage = (float) (this.getAttack() * 1.7);
          ModUtils.handleAreaImpact(1.9F, (e) -> damage, this, offset, source, 0.4f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          addEvent(() -> {
              ModUtils.handleAreaImpact(1.5F, (e) -> damage, this, offset, source, 0.8f, 0, false);
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
              addEvent(()-> {
                  this.lockLook =false;
                  this.setImmovable(true);
                  //this.holdPosition = true;
              }, 6);
          }, 6);
      }, 23);

      addEvent(()-> {
        this.lockLook = true;
        Vec3d savedPos = target.getPositionVector();
        addEvent(()-> {
            this.setImmovable(false);
            //this.holdPosition = false;
            double distance = this.getPositionVector().distanceTo(savedPos);
            ModUtils.leapTowards(this, savedPos, (float) (distance * 0.4),0.25F);
        }, 10);
      }, 42);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
          float damage = (float) (this.getAttack() * 1.7);
          ModUtils.handleAreaImpact(1.9F, (e) -> damage, this, offset, source, 0.4f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 56);

      addEvent(()-> {
          this.lockLook = false;
          this.setImmovable(true);
          //this.holdPosition = true;
      }, 61);

      addEvent(()-> {
        this.lockLook = true;
        Vec3d savedPos = target.getPositionVector();
        addEvent(()-> {
            this.setImmovable(false);
            //this.holdPosition = false;
            double distance = this.getPositionVector().distanceTo(savedPos);
            ModUtils.leapTowards(this, savedPos, (float) (distance * 0.2),0.2F);
        }, 10);
      }, 70);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
          float damage = (float) (this.getAttack());
          ModUtils.handleAreaImpact(1.9F, (e) -> damage, this, offset, source, 0.4f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          new ActionFlameSling(flame_sling_projectiles, false).performAction(this, target);
      }, 85);

      addEvent(()-> {
        this.lockLook =false;
        this.setFightMode(false);
        this.setComboWaterfoul(false);
        this.setFullBodyUsage(false);
        this.combo_aggrivation -= 0.35;
      }, 105);
    };



    private final Consumer<EntityLivingBase> swing_one_attack = (target) -> {
      this.setFightMode(true);
      this.setSwingOne(true);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 25);
      addEvent(()-> this.lockLook = true, 10);
      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.4f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 20);

      addEvent(()-> {
          this.lockLook = false;
        this.setSwingOne(false);
        this.setFightMode(false);
        this.combo_aggrivation += 0.15;
      }, 25);
    };

    private final Consumer<EntityLivingBase> swing_two_attack = (target) -> {
      this.setSwingTwo(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 3);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 25);
     // this.holdPosition = true;
      addEvent(()-> {
        this.lockLook = true;
        Vec3d targetedPos = target.getPositionVector().add(ModUtils.yVec(1));
        addEvent(()-> {
            this.setImmovable(false);
            //this.holdPosition = false;
            double distance = this.getPositionVector().distanceTo(targetedPos);
            ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.25F);
        }, 8);
      }, 5);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.6, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.9f, (e) -> damage, this, offset, source, 0.4f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          double getHealth = this.getHealth()/this.getMaxHealth();
          if(getHealth <= 0.5) {
              new ActionQuickFlameSling(flame_sling_projectiles).performAction(this, target);
          }
      }, 18);

      addEvent(()-> {
          this.lockLook = false;
        this.setSwingTwo(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
          this.combo_aggrivation += 0.2;
      }, 27);
    };

    private final Consumer<EntityLivingBase> swing_three_attack = (target) -> {
      this.setSwingThree(true);
      this.setFightMode(true);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 16);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
          float damage = (float) (this.getAttack() * 0.8);
          ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.4f, 0, false);
      }, 16);

      addEvent(()-> {
        this.setSwingThree(false);
        this.setFightMode(false);
          this.combo_aggrivation += 0.15;
      }, 23);
    };

    private final Consumer<EntityLivingBase> small_stomp = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      //this.holdPosition = true;
      double HealthChange = this.getHealth() / this.getMaxHealth();
      boolean doStomp = rand.nextBoolean();

      if(doStomp && HealthChange <= 0.5) {
        this.setSmallStomp(true);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_STOMP, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 12);
          addEvent(()-> {
              //Do AOE event
              float distance = this.getDistance(target);
              new ActionTileAOE((int) (distance + 2)).performAction(this, target);
              this.setShaking(true);
              this.shakeTime = 20;
          }, 15);

          addEvent(()-> {
              this.setShaking(false);
          }, 35);

          addEvent(()-> {
              this.setImmovable(false);
              //this.holdPosition = false;
              this.setSmallStomp(false);
              this.setFightMode(false);
              this.setFullBodyUsage(false);
              this.combo_aggrivation += 0.15;
          }, 20);
      } else {
        this.setGroundSweep(true);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 37);
        addEvent(()-> {
        Vec3d savedPos = target.getPositionVector().add(ModUtils.yVec(1.0d));
        this.lockLook = true;
        addEvent(()-> {
            this.setImmovable(false);
            //this.holdPosition = false;
            double distance = this.getPositionVector().distanceTo(savedPos);
            ModUtils.leapTowards(this, savedPos, (float) (distance * 0.25),0.3F);
        }, 10);
        }, 5);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            addEvent(()-> {
                this.setImmovable(true);
                //this.holdPosition = true;
            }, 9);
        }, 21);

        addEvent(()-> {
            this.setImmovable(false);
            this.lockLook =false;
            //this.holdPosition = false;
            this.setGroundSweep(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.combo_aggrivation += 0.1;
        }, 45);

      }

    };

    private final Consumer<EntityLivingBase> overhead_Dash = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setOverheadDash(true);
      this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.9f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f)), 20);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 40);
      //this.holdPosition = true;
      addEvent(()-> {
          this.lockLook = true;
          Vec3d savedPos = target.getPositionVector().add(ModUtils.yVec(0.5));
            addEvent(()-> {
                this.setImmovable(false);
                //this.holdPosition = false;
                this.DoDash(savedPos);
                this.startFlameSpawnsParticles = true;
                addEvent(()-> this.startFlameSpawnsParticles = false, 12);
            }, 10);
      }, 17);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
          float damage = (float) (this.getAttack() * 1.5);
          ModUtils.handleAreaImpact(3.0f, (e) -> damage, this, offset, source, 0.7f, 4, false);
          double getHealth = this.getHealth()/this.getMaxHealth();
          if(getHealth <= 0.5) {
              new ActionQuickFlameSling(flame_sling_projectiles).performAction(this, target);
          }
          this.setShaking(true);
          this.shakeTime = 7;
      }, 32);

      addEvent(()-> {
          this.setShaking(false);
      }, 39);

      addEvent(()-> {
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setOverheadDash(false);
        this.lockLook = false;
          this.combo_aggrivation += 0.25;
      }, 43);
    };


    private final Consumer<EntityLivingBase> Flame_Sling = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      //this.holdPosition = true;
      boolean isTriple = rand.nextBoolean();
      //selects between being triple attack or not
      if(isTriple) {
        this.setFlameSlingTriple(true);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 9);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 17);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 30);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 37);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.9f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f)), 60);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 85);
          addEvent(()-> new ActionFlameSling(flame_sling_projectiles, false).performAction(this, target), 16);
          addEvent(()-> new ActionFlameSling(flame_sling_projectiles, false).performAction(this, target), 35);
          addEvent(()-> this.lockLook = true, 60);
          addEvent(()-> new ActionFlameSling(flame_sling_projectiles, true).performAction(this, target), 75);
          addEvent(()-> {
              this.lockLook = false;
            this.setFlameSlingTriple(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            //this.holdPosition = false;
              this.combo_aggrivation += 0.2;
          }, 87);
      } else {
          this.setFlameSling(true);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 9);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 17);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 30);
          addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 37);
          addEvent(()-> new ActionFlameSling(flame_sling_projectiles, false).performAction(this, target), 16);
          addEvent(()-> new ActionFlameSling(flame_sling_projectiles, false).performAction(this, target), 35);

          addEvent(()-> {
            this.setFlameSling(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
           // this.holdPosition = false;
            this.setFightMode(false);
              this.combo_aggrivation += 0.2;
          }, 43);
      }
    };

    private final Consumer<EntityLivingBase> summon_flame = (target) -> {
        this.setImmovable(true);
        double getHealth = this.getHealth() / this.getMaxHealth();
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        if(getHealth <= 0.5 && this.isLostHead()) {
            //release flames attack, an upgrade from the prev
            this.setReleaseFlames(true);
            addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
            addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 35);
            addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 100);
            addEvent(()-> this.lockLook = true, 30);
            addEvent(()-> new ActionReleaseFlames(60).performAction(this, target), 35);
            addEvent(()-> {
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            this.setReleaseFlames(false);
            this.setFightMode(false);
            this.lockLook = false;
            this.combo_aggrivation += 0.15;
            }, 110);
        } else {
            this.lockLook = true;
            this.setSummonFlame(true);
            addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
            addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
            addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 46);
            addEvent(() -> {
                //Summons Tracking Flame
                new ActionSummonTrackFlame().performAction(this, target);
            }, 30);

            addEvent(() -> {
                this.lockLook = false;
                this.setImmovable(false);
                //this.holdPosition = false;
                this.setFullBodyUsage(false);
                this.setFightMode(false);
                this.setSummonFlame(false);
                this.combo_aggrivation += 0.15;
            }, 56);
        }
    };



    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.isPhaseTransition() || this.isReleaseFlames() || this.isSummon() || source.isExplosion() || this.currentlyInIFrame || this.isBeginCombo()) {
            return false;
        }
        if (amount > 0.0F && this.canBlockDamageSource(source) && !this.isFightMode() && !this.isComboMode()) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }
            this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.6f, 0.6f + ModRand.getFloat(0.2f));

            return false;
        } else if (source.isProjectile()) {
            return super.attackEntityFrom(source, (float) (amount * MobConfig.burning_knight_projectile_resistance));
        }
        return super.attackEntityFrom(source, amount);
    }

    public boolean isNotCurrentlyInDeath = false;

    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.00001F);
        if(!this.isDeathPlay()) {
            this.clearEvents();
        }
        this.setDeathPlay(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.setFightMode(false);
        this.setComboMode(false);
        this.lockLook = true;

        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 3);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 12);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
        addEvent(()-> {
            this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
            isNotCurrentlyInDeath = true;
            //this.equipBlockHead(HEAD_REPLACEMENT.HEAD, Blocks.BARRIER);

            }, 35);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 45);
        addEvent(()-> this.playSound(SoundsHandler.B_KNIGHT_MOVEMENT_ARMOR, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 48);
        addEvent(()-> {
            if(!world.isRemote) {
                for(int i = 0; i <= 80; i+=5) {
                    if(!world.isRemote) {
                        addEvent(() -> {
                            EntityXPOrb orb = new EntityXPOrb(world, this.posX, this.posY, this.posZ, MobConfig.kobf_experience_orb_value);
                            orb.setPosition(this.posX, this.posY + 1, this.posZ);
                            world.spawnEntity(orb);
                        }, i);
                    }
                }
            }
        }, 50);
        addEvent(()-> {
        this.setDeathPlay(false);
        if(this.getSpawnLocation() != null) {
            this.turnBossIntoSummonSpawner(this.getSpawnLocation());
        }
        this.setDead();
        this.setDropItemsWhenDead(true);

        }, 140);
        super.onDeath(cause);
    }


    private void DoDash(Vec3d enemyPosToo) {
        //this.playSound(ModSoundHandler.KING_DASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
        int randomDeterminedDistance = 8;
        Vec3d enemyPos = enemyPosToo;

        Vec3d startPos = this.getPositionVector().add(ModUtils.yVec(getEyeHeight()));

        Vec3d dir = enemyPos.subtract(startPos).normalize();

        AtomicReference<Vec3d> teleportPos = new AtomicReference<>(enemyPos);

        ModUtils.lineCallback(enemyPos.add(dir),enemyPos.scale(randomDeterminedDistance), randomDeterminedDistance * 2, (pos, r) -> {
            boolean safeLanding = ModUtils.cubePoints(0, -2, 0, 1, 0, 1).stream()
                    .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                            .isSideSolid(world, new BlockPos(pos.add(off)).down(), EnumFacing.UP));
            boolean notOpen = ModUtils.cubePoints(-2, 1, -2, 2, 4, 2).stream()
                    .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                            .causesSuffocation());

            if (safeLanding && !notOpen) {
                teleportPos.set(pos);
            }
        });
        this.chargeDir = teleportPos.get();
        this.setPositionAndUpdate(chargeDir.x, chargeDir.y, chargeDir.z);
       // world.playSound(chargeDir.x, chargeDir.y, chargeDir.z, SoundsHandler.B_KNIGHT_DASH, SoundCategory.HOSTILE,1.0f,0.8f / (rand.nextFloat() * 0.4f + 0.2f), false);
        this.playSound(SoundsHandler.B_KNIGHT_DASH, 1.0f, 0.8f + ModRand.getFloat(0.2f));
    }


    @Override
    public void handleStatusUpdate(byte id) {
        if(id == ModUtils.PARTICLE_BYTE) {
                    Vec3d particleVel = this.getPositionVector().normalize();
                    Vec3d pos1 = this.getPositionVector().add(ModUtils.yVec(1));
            Vec3d flameVel = new Vec3d(ModRand.randVec().x, ModRand.randVec().y, ModRand.randVec().z).scale(0.1);
                    world.spawnParticle(EnumParticleTypes.FLAME, pos1.x, pos1.y, pos1.z, flameVel.x, flameVel.y, flameVel.z, ModRand.range(10, 15));
                    world.spawnParticle(EnumParticleTypes.FLAME, pos1.x, pos1.y + 0.5, pos1.z, flameVel.x, flameVel.y, flameVel.z, ModRand.range(10, 15));
                    world.spawnParticle(EnumParticleTypes.FLAME, pos1.x, pos1.y + 1, pos1.z, flameVel.x, flameVel.y, flameVel.z, ModRand.range(10, 15));
                this.chargeDir = null;
        }

        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0))), ModColors.FIREBALL_ORANGE, new Vec3d((world.rand.nextFloat() - world.rand.nextFloat())/3,0.1,(world.rand.nextFloat() - world.rand.nextFloat())/3));
        }

        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            Vec3d particleVel = this.getPositionVector().normalize();
            Vec3d pos1 = this.getPositionVector().add(ModUtils.yVec(1));
            Vec3d flameVel = new Vec3d(ModRand.randVec().x, ModRand.randVec().y, ModRand.randVec().z).scale(0.1);
            ParticleManager.spawnBrightFlames(world, pos1, flameVel);
            ParticleManager.spawnBrightFlames(world, pos1.add(ModUtils.yVec(0.5)), flameVel);
            ParticleManager.spawnBrightFlames(world, pos1.add(ModUtils.yVec(1.0)), flameVel);
            this.chargeDir = null;
        }

        if(id == ModUtils.FOURTH_PARTICLE_BYTE) {
            Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 2.5,0)));
            Vec3d vel = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat())/3,  0.1, (world.rand.nextFloat() - world.rand.nextFloat())/3);
            world.spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, ModRand.range(10, 15));
        }
        if(id == ModUtils.FIFTH_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 25, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1)), ModColors.FIREBALL_ORANGE, pos.normalize().scale(0.1), ModRand.range(10, 15));
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(2)), ModColors.FIREBALL_ORANGE, pos.normalize().scale(0.1), ModRand.range(10, 15));
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(3)), ModColors.FIREBALL_ORANGE, pos.normalize().scale(0.1), ModRand.range(10, 15));
            });
        }
        super.handleStatusUpdate(id);
    }

    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(this.isComboMode()) {
            if(rand.nextInt(4) == 0) {
                world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            }
        }

        if(this.isLostHead() && !isNotCurrentlyInDeath) {
            if(world.rand.nextInt(4) == 0) {
                world.setEntityState(this, ModUtils.FOURTH_PARTICLE_BYTE);
            }
        }

        if(startFlameSpawnsParticles) {
            if(this.isComboMode()) {
                if(world.rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
                }
            } else {
                if(world.rand.nextInt(2) == 0) {
                    world.setEntityState(this, ModUtils.PARTICLE_BYTE);
                }
            }
        }
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if(!this.isComboMode()) {
            if (!damageSourceIn.isUnblockable() && !this.isFightMode() && blockTimerCooldown <= 0 && !this.isDrinkPotion() && !this.isDeathPlay()) {
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
    protected boolean canDespawn() {
        return false;
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


    private static final ResourceLocation LOOT_BOSS = new ResourceLocation(ModReference.MOD_ID, "flame_knight");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_BOSS;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 16.0F);
            if (dist >= 16.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.1F * screamMult);
        }
        return 0;
    }

    @Nullable
    @Override
    public SoundEvent getBossMusic() {
        return SoundsHandler.FLAME_KNIGHT_TRACK;
    }

    public enum KNIGHT_HAND {
        HAND("HoldItem");

        public String getBoneName() {
            return this.boneName;
        }

        private String boneName;

        KNIGHT_HAND(String bone) {
            this.boneName = bone;
        }

        @Nullable
        public static KNIGHT_HAND getFromBoneName(String boneName) {
            if ("HoldItem".equals(boneName)) {
                return HAND;
            }
            return null;
        }

        public int getIndex() {
            if (this == KNIGHT_HAND.HAND) {
                return 1;
            }
            return 0;
        }
    }


    public enum HEAD_REPLACEMENT {
        HEAD("HeadJ");

        public String getBoneName() {
            return this.boneName;
        }

        private String boneName;

        HEAD_REPLACEMENT(String bone) {
            this.boneName = bone;
        }

        @Nullable
        public static HEAD_REPLACEMENT getFromBoneName(String boneName) {
            if ("HeadJ".equals(boneName)) {
                return HEAD;
            }
            return null;
        }

        public int getIndex() {
            if (this == HEAD_REPLACEMENT.HEAD) {
                return 1;
            }
            return 0;
        }
    }


    public void equipBlockHead(HEAD_REPLACEMENT head, Optional<IBlockState> state) {
        if(world.isRemote) {
            return;
        }
        if (head == HEAD_REPLACEMENT.HEAD) {
            this.dataManager.set(BLOCK_HEAD, state);
        }
    }

    public void equipBlockHead(HEAD_REPLACEMENT hand, Block block) {
        this.equipBlockHead(hand, block.getDefaultState());
    }

    public void equipBlockHead(HEAD_REPLACEMENT hand, IBlockState blockstate) {
        this.equipBlockHead(hand, Optional.of(blockstate));
    }

    public Optional<IBlockState> getBlockFromHead(HEAD_REPLACEMENT head) {
        if (head == HEAD_REPLACEMENT.HEAD) {
            return this.dataManager.get(BLOCK_HEAD);
        }
        return Optional.absent();
    }

    public void equipBlock(KNIGHT_HAND head, ItemStack state) {
        if(world.isRemote) {
            return;
        }
        if (head == KNIGHT_HAND.HAND) {
            this.dataManager.set(ITEM_HAND, state);
        }
    }


    public ItemStack getItemFromKnightHand(KNIGHT_HAND head) {
        if (head == KNIGHT_HAND.HAND) {
            return this.dataManager.get(ITEM_HAND);
        }
        return null;
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }


    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.B_KNIGHT_HURT;
    }

   // @Override
   // protected SoundEvent getDeathSound() {
      //  return ModSoundHandler.KNIGHT_DEATH;
    //}

   // @Override
  //  protected SoundEvent getAmbientSound() {
  //      return ModSoundHandler.KNIGHT_IDLE;
 //   }


    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.B_KNIGHT_STEP, 0.4F, 0.4f + ModRand.getFloat(0.3F));
    }
}
