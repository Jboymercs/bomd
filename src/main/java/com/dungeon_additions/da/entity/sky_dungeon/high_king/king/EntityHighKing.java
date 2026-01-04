package com.dungeon_additions.da.entity.sky_dungeon.high_king.king;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyTornado;
import com.dungeon_additions.da.entity.sky_dungeon.city_knights.ActionHalberdSpecial;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingBoss;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.action.ActionBloodSpray;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.action.*;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.ai.EntityHighKingTimedAttack;
import com.dungeon_additions.da.entity.util.IEntitySound;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.ServerScaleUtil;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
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

public class EntityHighKing extends EntityHighKingBoss implements IAnimatable, IAnimationTickable, IAttack, IScreenShake, IEntitySound {

    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.NOTCHED_6));

    protected Vec3d chargeDir;
    public boolean currentlyInIFrame = false;
    protected int blockCooldown = MobConfig.high_king_block_cooldown * 20;
    private int HoverTimeIncrease = 0;

    private boolean hasHoverMovement = false;
    private boolean spearThrustGrabDetection = false;

    private int aoeCooldown = MobConfig.high_king_aoe_cooldown * 20;
    private EntityLivingBase grabbedEntity;
    //IDLE Animations
    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK_LOWER = "walk_lower";
    //State Animations
    private final String ANIM_SUMMON = "summon";
    private final String ANIM_PHASE_TRANSITION = "phase_transition";
    private final String ANIM_DEATH = "death";

    //Action Animations
    private final String ANIM_BLOCK = "block";
    private final String ANIM_BLOCK_RANGED = "block_with_ranged_attack";
    private final String ANIM_DODGE = "dodge";
    private final String ANIM_STRAFE_DODGE = "strafe_dodge";

    //Phase One Attacks
    //Single Animation Attacks
    private final String ANIM_FAST_AOE = "fast_aoe";
    private final String ANIM_CAST_LIGHTNING = "cast_lightning";
    private final String ANIM_CAST_CLAW = "cast_claw";
    private final String ANIM_HOLY_WAVE = "holy_wave";
    //Connecting Animations
    private final String ANIM_HEAVY_SWING = "heavy_swing";
    private final String ANIM_HEAVY_SWING_CONTINUE = "heavy_swing_continue";
    private final String ANIM_HEAVY_SWING_FINISH = "heavy_swing_finish";

    private final String ANIM_DOUBLE_SWING = "double_swing";
    private final String ANIM_DOUBLE_SWING_FINISH = "double_swing_finish";
    private final String ANIM_DOUBLE_SWING_JUMP = "double_swing_jump";

    private final String ANIM_SPEAR_THRUST = "spear_thrust";
    private final String ANIM_SPEAR_THRUST_CONTINUE = "spear_thrust_continue";
    private final String ANIM_SPEAR_THRUST_FINISH = "spear_thrust_finish";

    private final String ANIM_CIRCLE_AIR_ATTACK = "circle_air_attack";
    private final String ANIM_CIRCLE_AIR_RANGED = "circle_air_attack_ranged";
    private final String ANIM_CIRCLE_AIR_CONTINUE = "circle_air_attack_continue";

    private final String ANIM_STOMP_ATTACK = "stomp_attack";
    private final String ANIM_STOMP_CONTINUE = "stomp_continue";
    private final String ANIM_STOMP_FINISH = "stomp_finish";

    //Phase Two Attacks
    //Simple Animation Attacks
    private final String ANIM_BLOODY_FLY = "bloody_fly";
    //Connecting Animations
    private final String ANIM_STRAFE_THRUST = "strafe_thrust";
    private final String ANIM_STRAFE_THRUST_CONTINUE = "strafe_thrust_continue";
    private final String ANIM_STRAFE_THRUST_FINISH = "strafe_thrust_finish";

    private final String ANIM_BLOODY_GRAB = "bloody_grab";
    private final String ANIM_BLOODY_GRAB_END = "bloody_grab_end";
    private final String ANIM_BLOODY_GRAB_CONTINUE = "bloody_grab_continue";

    private static final DataParameter<Boolean> SUMMON_BOSS = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PHASE_TRANSITION = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEATH_BOSS = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FAST_AOE = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HOLY_WAVE = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLOCK_ACTION = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLOCK_WITH_RANGED = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DODGE = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STRAFE_DODGE = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HEAVY_SWING = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HEAVY_SWING_CONTINUE = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HEAVY_SWING_FINISH = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DOUBLE_SWING = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DOUBLE_SWING_FINISH = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DOUBLE_SWING_JUMP = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPEAR_THRUST = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPEAR_THRUST_CONTINUE = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPEAR_THRUST_FINISH = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> AIR_CIRCLE_ATTACK = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> AIR_CIRCLE_RANGED = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> AIR_CIRCLE_CONTINUE = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STOMP = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STOMP_CONTINUE = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STOMP_FINISH = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLOODY_FLY = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STRAFE_THRUST = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STRAFE_THRUST_CONTINUE = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STRAFE_THRUST_FINISH = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLOODY_GRAB = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLOODY_GRAB_END = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLOODY_GRAB_CONTINUE = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLOODIED = EntityDataManager.createKey(EntityHighKing.class, DataSerializers.BOOLEAN);
    public boolean isSummonBoss() {return this.dataManager.get(SUMMON_BOSS);}
    private void setSummonBoss(boolean value) {this.dataManager.set(SUMMON_BOSS, Boolean.valueOf(value));}
    public boolean isPhaseTransition() {return this.dataManager.get(PHASE_TRANSITION);}
    private void setPhaseTransition(boolean value) {this.dataManager.set(PHASE_TRANSITION, Boolean.valueOf(value));}
    public boolean isDeathBoss() {return this.dataManager.get(DEATH_BOSS);}
    private void setDeathBoss(boolean value) {this.dataManager.set(DEATH_BOSS, Boolean.valueOf(value));}
    private boolean isFastAoe() {return this.dataManager.get(FAST_AOE);}
    private void setFastAoe(boolean value) {this.dataManager.set(FAST_AOE, Boolean.valueOf(value));}
    private boolean isHolyWave() {return this.dataManager.get(HOLY_WAVE);}
    private void setHolyWave(boolean value) {this.dataManager.set(HOLY_WAVE, Boolean.valueOf(value));}
    private boolean isBlockAction() {return this.dataManager.get(BLOCK_ACTION);}
    private void setBlockAction(boolean value) {this.dataManager.set(BLOCK_ACTION, Boolean.valueOf(value));}
    private boolean isBlockWithRanged() {return this.dataManager.get(BLOCK_WITH_RANGED);}
    private void setBlockWithRanged(boolean value) {this.dataManager.set(BLOCK_WITH_RANGED, Boolean.valueOf(value));}
    public boolean isDodge() {return this.dataManager.get(DODGE);}
    private void setDodge(boolean value) {this.dataManager.set(DODGE, Boolean.valueOf(value));}
    public boolean isStrafeDodge() {return this.dataManager.get(STRAFE_DODGE);}
    private void setStrafeDodge(boolean value) {this.dataManager.set(STRAFE_DODGE, Boolean.valueOf(value));}
    private boolean isHeavySwing() {return this.dataManager.get(HEAVY_SWING);}
    private void setHeavySwing(boolean value) {this.dataManager.set(HEAVY_SWING, Boolean.valueOf(value));}
    private boolean isheavySwingContinue() {return this.dataManager.get(HEAVY_SWING_CONTINUE);}
    private void setHeavySwingContinue(boolean value) {this.dataManager.set(HEAVY_SWING_CONTINUE, Boolean.valueOf(value));}
    private boolean isHeavySwingFinish() {return this.dataManager.get(HEAVY_SWING_FINISH);}
    private void setHeavySwingFinish(boolean value) {this.dataManager.set(HEAVY_SWING_FINISH, Boolean.valueOf(value));}
    private boolean isDoubleSwing() {return this.dataManager.get(DOUBLE_SWING);}
    private void setDoubleSwing(boolean value) {this.dataManager.set(DOUBLE_SWING, Boolean.valueOf(value));}
    private boolean isDoubleSwingFinish() {return this.dataManager.get(DOUBLE_SWING_FINISH);}
    private void setDoubleSwingFinish(boolean value) {this.dataManager.set(DOUBLE_SWING_FINISH, Boolean.valueOf(value));}
    public boolean isDoubleSwingJump() {return this.dataManager.get(DOUBLE_SWING_JUMP);}
    private void setDoubleSwingJump(boolean value) {this.dataManager.set(DOUBLE_SWING_JUMP, Boolean.valueOf(value));}
    private boolean isSpearThrust() {return this.dataManager.get(SPEAR_THRUST);}
    private void setSpearThrust(boolean value) {this.dataManager.set(SPEAR_THRUST, Boolean.valueOf(value));}
    private boolean isSpearThrustContinue() {return this.dataManager.get(SPEAR_THRUST_CONTINUE);}
    private void setSpearThrustContinue(boolean value) {this.dataManager.set(SPEAR_THRUST_CONTINUE, Boolean.valueOf(value));}
    private boolean isSpearThrustFinish() {return this.dataManager.get(SPEAR_THRUST_FINISH);}
    private void setSpearThrustFinish(boolean value) {this.dataManager.set(SPEAR_THRUST_FINISH, Boolean.valueOf(value));}
    private boolean isAirCircleAttack() {return this.dataManager.get(AIR_CIRCLE_ATTACK);}
    private void setAirCircleAttack(boolean value) {this.dataManager.set(AIR_CIRCLE_ATTACK, Boolean.valueOf(value));}
    private boolean isAirCircleRanged() {return this.dataManager.get(AIR_CIRCLE_RANGED);}
    private void setAirCircleRanged(boolean value) {this.dataManager.set(AIR_CIRCLE_RANGED, Boolean.valueOf(value));}
    private boolean isAirCircleContinue() {return this.dataManager.get(AIR_CIRCLE_CONTINUE);}
    private void setAirCircleContinue(boolean value) {this.dataManager.set(AIR_CIRCLE_CONTINUE, Boolean.valueOf(value));}
    private boolean isStomp() {return this.dataManager.get(STOMP);}
    private void setStomp(boolean value) {this.dataManager.set(STOMP, Boolean.valueOf(value));}
    private boolean isStompContinue() {return this.dataManager.get(STOMP_CONTINUE);}
    private void setStompContinue(boolean value) {this.dataManager.set(STOMP_CONTINUE, Boolean.valueOf(value));}
    private boolean isStompFinish() {return this.dataManager.get(STOMP_FINISH);}
    private void setStompFinish(boolean value) {this.dataManager.set(STOMP_FINISH, Boolean.valueOf(value));}
    private boolean isBloodyFly() {return this.dataManager.get(BLOODY_FLY);}
    private void setBloodyFly(boolean value) {this.dataManager.set(BLOODY_FLY, Boolean.valueOf(value));}
    public boolean isStrafeThrust() {return this.dataManager.get(STRAFE_THRUST);}
    private void setStrafeThrust(boolean value) {this.dataManager.set(STRAFE_THRUST, Boolean.valueOf(value));}
    public boolean isStrafeThrustContinue() {return this.dataManager.get(STRAFE_THRUST_CONTINUE);}
    private void setStrafeThrustContinue(boolean value) {this.dataManager.set(STRAFE_THRUST_CONTINUE, Boolean.valueOf(value));}
    private boolean isStrafeThrustFinish() {return this.dataManager.get(STRAFE_THRUST_FINISH);}
    private void setStrafeThrustFinish(boolean value) {this.dataManager.set(STRAFE_THRUST_FINISH, Boolean.valueOf(value));}
    private boolean isBloodyGrab() {return this.dataManager.get(BLOODY_GRAB);}
    private void setBloodyGrab(boolean value) {this.dataManager.set(BLOODY_GRAB, Boolean.valueOf(value));}
    private boolean isBloodyGrabEnd() {return this.dataManager.get(BLOODY_GRAB_END);}
    private void setBloodyGrabEnd(boolean value) {this.dataManager.set(BLOODY_GRAB_END, Boolean.valueOf(value));}
    private boolean isBloodyGrabContinue() {return this.dataManager.get(BLOODY_GRAB_CONTINUE);}
    private void setBloodyGrabContinue(boolean value) {this.dataManager.set(BLOODY_GRAB_CONTINUE, Boolean.valueOf(value));}
    public boolean isBloodied() {return this.dataManager.get(BLOODIED);}
    private void setBloodied(boolean value) {this.dataManager.set(BLOODIED, Boolean.valueOf(value));}
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Bloodied", this.isBloodied());
        nbt.setBoolean("Bloody_Grab", this.isBloodyGrab());
        nbt.setBoolean("Bloody_Grab_Finish", this.isBloodyGrabEnd());
        nbt.setBoolean("Bloody_Grab_Continue", this.isBloodyGrabContinue());
        nbt.setBoolean("Bloody_Fly", this.isBloodyFly());
        nbt.setBoolean("Strafe_Thrust", this.isStrafeThrust());
        nbt.setBoolean("Strafe_Thrust_Continue", this.isStrafeThrustContinue());
        nbt.setBoolean("Strafe_Thrust_Finish", this.isStrafeThrustFinish());
        nbt.setBoolean("Air_Circle_Attack", this.isAirCircleAttack());
        nbt.setBoolean("Air_Circle_Ranged", this.isAirCircleRanged());
        nbt.setBoolean("Air_Circle_Continue", this.isAirCircleContinue());
        nbt.setBoolean("Stomp", this.isStomp());
        nbt.setBoolean("Stomp_Continue", this.isStompContinue());
        nbt.setBoolean("Stomp_Finish", this.isStompFinish());
        nbt.setBoolean("Double_Swing", this.isDoubleSwing());
        nbt.setBoolean("Double_Swing_Finish", this.isDoubleSwingFinish());
        nbt.setBoolean("Double_Swing_Jump", this.isDoubleSwingJump());
        nbt.setBoolean("Spear_Thrust", this.isSpearThrust());
        nbt.setBoolean("Spear_Thrust_Continue", this.isSpearThrustContinue());
        nbt.setBoolean("Spear_Thrust_Finish", this.isSpearThrustFinish());
        nbt.setBoolean("Block_Action", this.isBlockAction());
        nbt.setBoolean("Block_Ranged", this.isBlockWithRanged());
        nbt.setBoolean("Dodge", this.isDodge());
        nbt.setBoolean("Strafe_Dodge", this.isStrafeDodge());
        nbt.setBoolean("Heavy_Swing", this.isHeavySwing());
        nbt.setBoolean("Heavy_Swing_Continue", this.isheavySwingContinue());
        nbt.setBoolean("Heavy_Swing_Finish", this.isHeavySwingFinish());
        nbt.setBoolean("Summon", this.isSummonBoss());
        nbt.setBoolean("Phase_Transition", this.isPhaseTransition());
        nbt.setBoolean("Death_Boss", this.isDeathBoss());
        nbt.setBoolean("Fast_aoe", this.isFastAoe());
        nbt.setBoolean("Holy_Wave", this.isHolyWave());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setBloodied(nbt.getBoolean("Bloodied"));
        this.setBloodyGrab(nbt.getBoolean("Bloody_Grab"));
        this.setBloodyGrabContinue(nbt.getBoolean("Bloody_Grab_Continue"));
        this.setBloodyGrabEnd(nbt.getBoolean("Bloody_Grab_End"));
        this.setBloodyFly(nbt.getBoolean("Bloody_Fly"));
        this.setStrafeThrust(nbt.getBoolean("Strafe_Thrust"));
        this.setStrafeThrustContinue(nbt.getBoolean("Strafe_Thrust_Continue"));
        this.setStrafeThrustFinish(nbt.getBoolean("Strafe_Thrust_Finish"));
        this.setAirCircleAttack(nbt.getBoolean("Air_Circle_Attack"));
        this.setAirCircleRanged(nbt.getBoolean("Air_Circle_Ranged"));
        this.setAirCircleContinue(nbt.getBoolean("Air_Circle_Continue"));
        this.setStomp(nbt.getBoolean("Stomp"));
        this.setStompContinue(nbt.getBoolean("Stomp_Continue"));
        this.setStompFinish(nbt.getBoolean("Stomp_Finish"));
        this.setDoubleSwing(nbt.getBoolean("Double_Swing"));
        this.setDoubleSwingFinish(nbt.getBoolean("Double_Swing_Finish"));
        this.setDoubleSwingJump(nbt.getBoolean("Double_Swing_Jump"));
        this.setSpearThrust(nbt.getBoolean("Spear_Thrust"));
        this.setSpearThrustContinue(nbt.getBoolean("Spear_Thrust_Continue"));
        this.setSpearThrustFinish(nbt.getBoolean("Spear_Thrust_Finish"));
        this.setBlockAction(nbt.getBoolean("Block_Action"));
        this.setBlockWithRanged(nbt.getBoolean("Block_Ranged"));
        this.setDodge(nbt.getBoolean("Dodge"));
        this.setStrafeDodge(nbt.getBoolean("Strafe_Dodge"));
        this.setHeavySwing(nbt.getBoolean("Heavy_Swing"));
        this.setHeavySwingContinue(nbt.getBoolean("Heavy_Swing_Continue"));
        this.setHeavySwingFinish(nbt.getBoolean("Heavy_Swing_Finish"));
        this.setSummonBoss(nbt.getBoolean("Summon"));
        this.setPhaseTransition(nbt.getBoolean("Phase_Transition"));
        this.setDeathBoss(nbt.getBoolean("Death_Boss"));
        this.setFastAoe(nbt.getBoolean("Fast_aoe"));
        this.setHolyWave(nbt.getBoolean("Holy_Wave"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(BLOODIED, Boolean.valueOf(false));
        this.dataManager.register(BLOODY_GRAB, Boolean.valueOf(false));
        this.dataManager.register(BLOODY_GRAB_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(BLOODY_GRAB_END, Boolean.valueOf(false));
        this.dataManager.register(BLOODY_FLY, Boolean.valueOf(false));
        this.dataManager.register(STRAFE_THRUST, Boolean.valueOf(false));
        this.dataManager.register(STRAFE_THRUST_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(STRAFE_THRUST_FINISH, Boolean.valueOf(false));
        this.dataManager.register(AIR_CIRCLE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(AIR_CIRCLE_RANGED, Boolean.valueOf(false));
        this.dataManager.register(AIR_CIRCLE_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(STOMP, Boolean.valueOf(false));
        this.dataManager.register(STOMP_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(STOMP_FINISH, Boolean.valueOf(false));
        this.dataManager.register(DOUBLE_SWING, Boolean.valueOf(false));
        this.dataManager.register(DOUBLE_SWING_FINISH, Boolean.valueOf(false));
        this.dataManager.register(DOUBLE_SWING_JUMP, Boolean.valueOf(false));
        this.dataManager.register(SPEAR_THRUST, Boolean.valueOf(false));
        this.dataManager.register(SPEAR_THRUST_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(SPEAR_THRUST_FINISH, Boolean.valueOf(false));
        this.dataManager.register(BLOCK_ACTION, Boolean.valueOf(false));
        this.dataManager.register(BLOCK_WITH_RANGED, Boolean.valueOf(false));
        this.dataManager.register(DODGE, Boolean.valueOf(false));
        this.dataManager.register(STRAFE_DODGE, Boolean.valueOf(false));
        this.dataManager.register(HEAVY_SWING, Boolean.valueOf(false));
        this.dataManager.register(HEAVY_SWING_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(HEAVY_SWING_FINISH, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_BOSS, Boolean.valueOf(false));
        this.dataManager.register(PHASE_TRANSITION, Boolean.valueOf(false));
        this.dataManager.register(DEATH_BOSS, Boolean.valueOf(false));
        this.dataManager.register(FAST_AOE, Boolean.valueOf(false));
        this.dataManager.register(HOLY_WAVE, Boolean.valueOf(false));
    }
    private final AnimationFactory factory = new AnimationFactory(this);

    public EntityHighKing(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.8F, 2.45F);
        this.setHasSpawn(true);
        this.setSpawnLocation(new BlockPos(x,y,z));
        this.doBossSummoning();
    }

    public EntityHighKing(World worldIn, int timesUsed, BlockPos pos) {
        super(worldIn);
        this.timesUsed = timesUsed;
        if(!MobConfig.dragon_starts_first) {
            this.timesUsed++;
        }
        this.doBossReSummonScaling();
        this.setSize(0.8F, 2.45F);
        this.setHasSpawn(true);
        this.setSpawnLocation(pos);
        this.doBossSummoning();
    }


    private void doBossSummoning() {
        this.setSummonBoss(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 120);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 160);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 210);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_CLAW_DRAW, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 185);
        addEvent(()-> {
            new ActionSummonKing().performAction(this, null);
        }, 80);

        addEvent(()-> {
          this.setSummonBoss(false);
          this.setFightMode(false);
          this.setFullBodyUsage(false);
          this.setImmovable(false);
        }, 230);
    }

    public EntityHighKing(World worldIn) {
        super(worldIn);
        this.setSize(0.8F, 2.45F);
    }

    private void performPhaseTransition() {
        this.setPhaseTransition(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.lockLook = true;
        this.phaseTransition = true;

        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 22);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_CLAW_DRAW, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 37);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 65);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 130);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 150);
        addEvent(()-> {
        this.setBloodied(true);
        if(!MobConfig.disable_blood_attacks) {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + "The king is bloody..."));
            }
        }
        }, 64);

        addEvent(()-> {
            if(!MobConfig.disable_blood_attacks) {
                for (EntityPlayer player : this.bossInfo.getPlayers()) {
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "May you be engulfed in despair."));
                }
            }
        }, 130);

        addEvent(()-> this.lockLook = false, 150);
        addEvent(()-> {
            this.setPhaseTransition(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            this.phaseTransition = false;
            this.setUpBloodyAttack = true;
        }, 160);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.high_king_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.high_king_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.high_king_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }
    private boolean isMeleeViable = false;

    private boolean maintainSpearPose = false;

    private boolean grabDetection = false;
    private boolean maintainGrabPose = false;
    private boolean phaseTransition = false;
    private boolean setUpBloodyAttack = false;
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(getHealth() / getMaxHealth());
        if(world.isRemote && ticksExisted == 1 && ModConfig.experimental_features) {
            this.playMusic(this);
        }

        if(!world.isRemote) {
            blockCooldown--;
            aoeCooldown--;

            if(ticksExisted == 10 && this.getSpawnLocation() != null) {
                List<EntitySkyTornado> nearbyEntities = this.world.getEntitiesWithinAABB(EntitySkyTornado.class, this.getEntityBoundingBox().grow(25, 15, 25), e -> !e.getIsInvulnerable());
                if(!nearbyEntities.isEmpty()) {
                    for(EntitySkyTornado tornado : nearbyEntities) {
                        tornado.setDead();
                    }
                }
            }

            EntityLivingBase target = this.getAttackTarget();

            //Boss Reset Timer
            if(target == null && this.isHadPreviousTarget() && ModConfig.boss_reset_enabled) {
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

            //Teleport Boss if the spawn location is not null and is too far from its spawnpoint
            if(this.getSpawnLocation() != null && this.isHasSpawn()) {
                Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());
                double distSq = this.getDistanceSq(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                double distance = Math.sqrt(distSq);
                if(!world.isRemote) {
                    if (distance > 50) {
                        this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                    }
                }
            }

            if(target != null) {

                //These booleans will constantly be going on and off to help best have the king select an attack or switch up combos
                double HealthChange = this.getHealth()/this.getMaxHealth();
                if(HealthChange <= 0.5 && !this.isFightMode() && !this.isBlockAction() && !this.isBlockWithRanged() && !this.isBloodied()) {
                    if(!this.phaseTransition) {
                        this.performPhaseTransition();
                    }
                }
                if(this.getDistance(target) <= 9) {
                    this.isMeleeViable = true;
                } else {
                    this.isMeleeViable = false;
                }

                if(HoverTimeIncrease > 0) {
                    this.motionY = 0.25;
                    HoverTimeIncrease--;
                }

                if(this.isStrafeThrustContinue()) {
                    this.faceEntity(target, 30F, 30F);
                }

                if(this.hasHoverMovement) {
                    double d0 = (target.posX - this.posX) * 0.012;
                    double d2 = (target.posZ - this.posZ) * 0.012;
                    this.addVelocity(d0, 0, d2);
                    this.faceEntity(target, 30F, 30F);
                }

                if(spearThrustGrabDetection && grabbedEntity == null) {
                    List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                            this.getEntityBoundingBox().offset(ModUtils.getRelativeOffset(this, new Vec3d(0.9, -0.5, 0))).grow(1.1D, 3.5D, 1.1D),
                            e -> !e.getIsInvulnerable());

                    if(!nearbyEntities.isEmpty()) {
                        for(EntityLivingBase base : nearbyEntities) {
                            if(!(base instanceof EntitySkyBase)) {
                                this.maintainSpearPose = true;
                                grabbedEntity = base;
                                this.playSound(SoundsHandler.KING_GRAB_SUCCESS, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                            }
                        }
                    }
                } else if(grabDetection && grabbedEntity == null) {
                    List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                            this.getEntityBoundingBox().offset(ModUtils.getRelativeOffset(this, new Vec3d(1.3, -0.5, 0))).grow(1.25D, 3.5D, 1.25D),
                            e -> !e.getIsInvulnerable());

                    if(!nearbyEntities.isEmpty()) {
                        for(EntityLivingBase base : nearbyEntities) {
                            if(!(base instanceof EntitySkyBase)) {
                                this.maintainGrabPose = true;
                                grabbedEntity = base;
                                this.playSound(SoundsHandler.KING_GRAB_SUCCESS, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                            }
                        }
                    }
                } else if (grabbedEntity != null) {
                    if(this.maintainSpearPose) {
                        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.7, 4.5, 0)));
                        grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                        grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);
                    }

                    if(this.maintainGrabPose) {
                        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 0.2, 0)));
                        grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                        grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);
                    }
                }
            }
        }
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityHighKingTimedAttack<EntityHighKing>(this, 1.1, 30, 30, 0.25F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double healthFactor = this.getHealth() / this.getMaxHealth();

        if(!this.isFightMode() && !this.isBlockWithRanged() && !this.isBlockAction() && !this.isDeathBoss()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(heavy_swing_begin, fast_aoe_attack, double_swing, cast_lightning_attack, cast_claw_attack, dodge_action, circle_air_attack, spear_thrust_attack, stomp_attack, holy_wave, bloody_fly_attack, bloody_grab_attack, strafe_thrust_attack));
            double[] weights = {
                    (prevAttacks != heavy_swing_begin && distance <= 9) ? 1/distance : 0, //Heavy Swing, can do a second swing as well
                    (prevAttacks != fast_aoe_attack && distance <= 12 && aoeCooldown < 0) ? 1/distance : 0, //Fast AOE attack
                    (prevAttacks != double_swing && distance <= 9) ? 1/distance : 0, //Double Swing that can end with a jump AOE
                    (prevAttacks != cast_lightning_attack && distance > 9) ? distance * 0.02 : 0, //Cast Lightning Attack
                    (prevAttacks != cast_claw_attack && distance > 9) ? distance * 0.02 : 0, //Cast Claw Attack
                    (prevAttacks != dodge_action && distance <= 6) ? 1/distance : 0, //Dodge Action, can do backwards and sidestrafe
                    (prevAttacks != circle_air_attack && distance <= 9) ? 1/distance : 0, //Circle Air Attack
                    (prevAttacks != spear_thrust_attack && distance > 6) ? distance * 0.02 : 0, //Spear Thrust, can do insane damage if far from the king
                    (prevAttacks != stomp_attack && aoeCooldown < 0 && distance > 3 && distance <= 16 && healthFactor <= 0.8) ? 1/distance : 0, //Stomp Attack, can do a second one on random chance
                    (prevAttacks != holy_wave && distance > 14 && healthFactor <= 0.8) ? 1/distance : 0, //Holy Wave Attack that only activates at distance
                    (setUpBloodyAttack && healthFactor <= 0.5 && !MobConfig.disable_blood_attacks) ? 1000 : (prevAttacks != bloody_fly_attack && distance <= 16 && healthFactor <= 0.5 && !MobConfig.disable_blood_attacks) ? 1/distance : 0, //Bloody Fly Attack
                    (prevAttacks != bloody_grab_attack && distance > 6 && healthFactor <= 0.5 && !MobConfig.disable_blood_attacks) ? 1/distance : 0, //Bloody Grab Attack
                    (prevAttacks != strafe_thrust_attack && distance <= 11 && healthFactor <= 0.5) ? 1/distance : 0 //Strafe Thrust
            };
            prevAttacks = ModRand.choice(attacks, rand, weights).next();
            prevAttacks.accept(target);
        }
        return healthFactor <= 0.5 && this.isBloodied() ? 0 : ModRand.range(MobConfig.high_king_cooldown_min * 20, MobConfig.high_king_cooldown_max * 20);
    }

    private final Consumer<EntityLivingBase> strafe_thrust_attack = (target) -> {
      this.setStrafeThrust(true);
      this.setImmovable(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 25);
        addEvent(()-> {
            this.currentlyInIFrame = true;
            this.setImmovable(false);
            Vec3d jumpPos = this.getCircleRadiPoint((int) 7, target.getPositionVector());
            Vec3d dirToo = this.getPositionVector().subtract(jumpPos).normalize();
            Vec3d finalPos = this.getPositionVector().add(dirToo.scale(47));
            ModUtils.leapTowards(this, finalPos, 1.6F, 0.25F);
        }, 5);

        addEvent(()-> {
            this.currentlyInIFrame = false;
            this.setImmovable(true);
        }, 30);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0F);
            }, 5);
        }, 35);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.75, 1.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (this.getAttack() * 1.25);
            ModUtils.handleAreaImpact(2.75f, (e) -> damage, this, offset, source, 0.3f, 0, false);
            // Summon a Lightning Ring
            this.playSound(SoundsHandler.HIGH_KING_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 45);

        addEvent(()-> {
            this.setImmovable(true);
        }, 50);

        addEvent(()-> {
        this.setStrafeThrust(false);
        int randI = ModRand.range(1, 11);
        if(this.isMeleeViable && randI >= 4) {
            setThrustTooContinue(target);
        } else {
            this.setStrafeThrustFinish(true);
            addEvent(()-> {
                this.lockLook = false;
            }, 10);
            addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
            addEvent(()-> {
                this.setStrafeThrustFinish(false);
                this.setFullBodyUsage(false);
                this.setFightMode(false);
                this.setImmovable(false);
            }, 15);
        }
        }, 55);
    };

    private void setThrustTooContinue(EntityLivingBase target) {
      strafe_thrust_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> strafe_thrust_continue = (target) -> {
        this.setStrafeThrustContinue(true);
        this.setImmovable(false);
        this.HoverTimeIncrease = 1;
        this.setNoGravity(true);
        this.lockLook = false;
        addEvent(()-> {
            this.setImmovable(true);
            this.HoverTimeIncrease = 0;
            this.motionY = 0;
        }, 7);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 30);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 85);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 100);
        addEvent(()-> {

            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.06),0F);
            }, 5);
        }, 10);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.75, 1.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack() * 1.25);
            ModUtils.handleAreaImpact(2.75f, (e) -> damage, this, offset, source, 0.6f, 0, false);
            // Summon a Lightning Ring
            if(!MobConfig.disable_blood_attacks) {
                new ActionBloodSpray().performAction(this, target);
            }
            this.playSound(SoundsHandler.HIGH_KING_SWING_IMPALE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 21);

        addEvent(()-> {
            this.setImmovable(true);
            }, 23);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.04),0F);
            }, 5);
        }, 30);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.75, 1.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack());
            ModUtils.handleAreaImpact(2.75f, (e) -> damage, this, offset, source, 0.6f, 0, false);
            // Summon a Lightning Ring
            if(!MobConfig.disable_blood_attacks) {
                new ActionBloodSpray().performAction(this, target);
            }
            this.playSound(SoundsHandler.HIGH_KING_SWING_IMPALE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 40);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.04),0F);
            }, 5);
        }, 45);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.75, 1.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack());
            ModUtils.handleAreaImpact(2.75f, (e) -> damage, this, offset, source, 0.6f, 0, false);
            // Summon a Lightning Ring
            if(!MobConfig.disable_blood_attacks) {
                new ActionBloodSpray().performAction(this, target);
            }
            this.playSound(SoundsHandler.HIGH_KING_SWING_IMPALE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 55);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.04),0F);
            }, 5);
        }, 60);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.75, 1.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack());
            ModUtils.handleAreaImpact(2.75f, (e) -> damage, this, offset, source, 0.6f, 0, false);
            // Summon a Lightning Ring
            if(!MobConfig.disable_blood_attacks) {
                new ActionBloodSpray().performAction(this, target);
            }
            this.playSound(SoundsHandler.HIGH_KING_SWING_IMPALE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 70);

        addEvent(()-> this.setNoGravity(false), 82);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 100);

        addEvent(()-> {
            this.setStrafeThrustContinue(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
        }, 110);
    };
    private final Consumer<EntityLivingBase> bloody_grab_attack = (target) -> {
        this.setFullBodyUsage(true);
        this.setBloodyGrab(true);
        this.setFightMode(true);
        this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_CLAW_DRAW, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 20);
        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-5));
            addEvent(()-> {
                this.setImmovable(false);
                ModUtils.attemptTeleport(targetedPos, this);
                this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.75f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 3);
        }, 32);


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
        }, 32);

        addEvent(()-> {
            this.grabDetection = false;
            this.setImmovable(true);
            this.setBloodyGrab(false);

            if(grabbedEntity != null) {
                this.setGrabTooContinue(target);
            } else {
                this.setBloodyGrabEnd(true);
                addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
                addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 25);
                addEvent(()-> this.lockLook = false, 20);
                addEvent(()-> {
                this.setBloodyGrabEnd(false);
                this.setImmovable(false);
                this.setFightMode(false);
                this.setFullBodyUsage(false);
                }, 30);
            }
        }, 60);
    };

    private void setGrabTooContinue(EntityLivingBase target) {
        bloody_grab_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> bloody_grab_continue = (target) -> {
      this.setBloodyGrabContinue(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 75);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 90);
        double healthFac = this.getMaxHealth() * MobConfig.high_king_lifesteal_grab;
      addEvent(()-> target.addPotionEffect(new PotionEffect(MobEffects.WITHER, 200, 0)), 10);

      addEvent(()-> {
          if(grabbedEntity != null) {
              Vec3d offset = grabbedEntity.getPositionVector().add(0, 1, 0);
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
              float damage = (float) (this.getAttack() * 1.25);
              ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.9f, 0, false);
              this.playSound(SoundsHandler.HIGH_KING_SWING_IMPALE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
              this.heal((float) healthFac);
              this.maintainGrabPose = false;
              this.grabbedEntity = null;
          }
      }, 34);

      addEvent(()-> this.lockLook = false, 80);

      addEvent(()-> {
        this.setImmovable(false);
        this.setBloodyGrabContinue(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
      }, 95);
    };
    private final Consumer<EntityLivingBase> bloody_fly_attack = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setBloodyFly(true);
      this.HoverTimeIncrease = 9;
      this.setNoGravity(true);
      this.setUpBloodyAttack = false;
        this.playSound(SoundsHandler.BLOOD_FLY_BEGIN, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 80);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 120);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 135);

      addEvent(()-> {
          this.setImmovable(true);
      }, 20);
      addEvent(()-> {
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-2));
         // ModUtils.attemptTeleport(targetedPos, this);
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.1),0.05F);
              new ActionBloodBomb().performAction(this, target);
              this.playSound(SoundsHandler.BLOODY_FLY_SWING, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
          }, 5);
      }, 37);

      addEvent(()-> {
        this.setImmovable(true);
      },55 );


        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-2));
          //  ModUtils.attemptTeleport(targetedPos, this);
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.1),0.05F);
                new ActionBloodBomb().performAction(this, target);
                this.playSound(SoundsHandler.BLOODY_FLY_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            }, 5);
        }, 90);

        addEvent(()-> {
            this.setImmovable(true);
        },110);

        addEvent(()-> {
            this.setImmovable(false);
            this.setNoGravity(false);
        }, 120);

      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setBloodyFly(false);
        this.setImmovable(false);
      }, 140);
    };

    private final Consumer<EntityLivingBase> holy_wave = (target) -> {
      this.setHolyWave(true);
      this.setFightMode(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_CAST_CLAW, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 18);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 30);
      addEvent(()-> {
        //Do Holy Wave Action
          new ActionHolyWave((int) this.getDistance(target)).performAction(this, target);
      }, 20);
      addEvent(()-> {
        this.setHolyWave(false);
        this.setFightMode(false);
      }, 40);
    };
    private final Consumer<EntityLivingBase> stomp_attack = (target) -> {
      this.setStomp(true);
      this.setImmovable(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 3);
      addEvent(()-> this.lockLook = true, 15);

        addEvent(()-> this.playSound(SoundsHandler.DRAUGR_ELITE_STOMP, 1.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 23);
      addEvent(()-> new ActionKingStomp().performAction(this, target), 25);
      addEvent(()-> {
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
          Main.proxy.spawnParticle(20,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
      }, 25);
      addEvent(()-> {
          this.setShaking(true);
          this.shakeTime = 20;
      }, 25);

      addEvent(()-> {
          this.setShaking(false);
      }, 40);

      addEvent(()-> {
        this.setStomp(false);
        int randI = ModRand.range(1, 11);
        if(randI >= 6) {
            this.setStompTooContinue(target);
        } else {
            this.setStompFinish(true);
            addEvent(()-> {
            this.lockLook = false;
            }, 5);
            addEvent(()-> {
                this.setStompFinish(false);
                addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
                this.setFightMode(false);
                this.setImmovable(false);
                this.setFullBodyUsage(false);
                this.aoeCooldown = (int) ((MobConfig.high_king_aoe_cooldown * 20) * 0.75);
            }, 15);
        }
      }, 40);
    };

    private void setStompTooContinue(EntityLivingBase target) {
        stomp_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> stomp_continue = (target) -> {
        this.setStompContinue(true);
        this.lockLook = false;
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 13);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 55);
        addEvent(()-> this.lockLook = true, 25);
        addEvent(()-> this.playSound(SoundsHandler.DRAUGR_ELITE_STOMP, 1.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 35);
        addEvent(()-> new ActionKingStomp().performAction(this, target), 37);
        addEvent(()-> {
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            Main.proxy.spawnParticle(20,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
        }, 37);
        addEvent(()-> {
            this.setShaking(true);
            this.shakeTime = 20;
        }, 37);

        addEvent(()-> {
            this.setShaking(false);
        }, 52);

        addEvent(()-> this.lockLook = false, 55);

        addEvent(()-> {
            this.setStompContinue(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            this.aoeCooldown = MobConfig.high_king_aoe_cooldown * 20;
        }, 65);
    };

    private final Consumer<EntityLivingBase> spear_thrust_attack = (target) -> {
        this.setSpearThrust(true);
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 20);
        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-7));
            ModUtils.attemptTeleport(targetedPos, this);
            this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.75f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 25);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
          //  ModUtils.attemptTeleport(targetedPos, this);
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                this.spearThrustGrabDetection = true;
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.35),0.1F);
                this.playSound(SoundsHandler.SPEAR_GRAB_ATTACK, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 7);
        }, 28);

        addEvent(()-> {
            this.spearThrustGrabDetection = false;
            this.setSpearThrust(false);
            this.setImmovable(true);
            if(grabbedEntity != null) {
                setThrustTooFinisher(target);
            } else {
                this.setSpearThrustFinish(true);
                addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 8);
                this.lockLook = false;
                addEvent(()-> {
                    this.setSpearThrustFinish(false);
                    this.setImmovable(false);
                    this.setFullBodyUsage(false);
                    this.setFightMode(false);
                }, 20);
            }
        }, 55);
    };

    private void setThrustTooFinisher(EntityLivingBase target) {
        spear_thrust_finisher.accept(target);
    }

    private final Consumer<EntityLivingBase> spear_thrust_finisher = (target) -> {
      this.setSpearThrustContinue(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 110);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 124);
      this.maintainSpearPose = true;
      double healthFac = this.getMaxHealth() * MobConfig.high_king_lifesteal_thrust;
      addEvent(()-> {
          if(grabbedEntity != null) {
              Vec3d offset = grabbedEntity.getPositionVector().add(0, 1, 0);
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
              float damage = (float) (this.getAttack() * 0.75);
              ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.6f, 0, false);
              this.playSound(SoundsHandler.HIGH_KING_SWING_IMPALE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
              this.heal((float) healthFac);
          }
      }, 35);

        addEvent(()-> {
            if(grabbedEntity != null) {
                Vec3d offset = grabbedEntity.getPositionVector().add(0, 1, 0);
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack() * 0.75);
                ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                this.playSound(SoundsHandler.HIGH_KING_SWING_IMPALE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                this.heal((float) healthFac);
            }
        }, 60);

        addEvent(()-> {
            this.maintainSpearPose = false;
            if(grabbedEntity != null) {
                Vec3d offset = grabbedEntity.getPositionVector().add(0, 0.25, 0);
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack() * 1.25);
                ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 1.2f, 0, false);
                this.playSound(SoundsHandler.HIGH_KING_SWING_IMPALE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                this.heal((float) healthFac);
            }
        }, 100);

        addEvent(()-> this.lockLook = false, 120);

      addEvent(() -> {
          this.grabbedEntity = null;
          this.setSpearThrustContinue(false);
          this.setFullBodyUsage(false);
          this.setFightMode(false);
          this.setImmovable(false);
      }, 135);
    };

    private final Consumer<EntityLivingBase> circle_air_attack = (target) -> {
       this.setAirCircleAttack(true);
       this.setFullBodyUsage(true);
       this.setFightMode(true);
       this.setNoGravity(true);
       this.HoverTimeIncrease = 4;
       this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
       addEvent(()-> {
           Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
           Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
           addEvent(()-> {
               this.setImmovable(false);
               double distance = this.getPositionVector().distanceTo(targetedPos);
               ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0F);
           }, 5);
       }, 27);

       addEvent(()-> {
           Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.0, 0)));
           DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
           float damage = (float) (this.getAttack() * 1.25);
           ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.6f, 0, false);
           // Summon a Lightning Ring
           this.playSound(SoundsHandler.HIGH_KING_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
       }, 37);

       addEvent(()-> this.setImmovable(true), 45);

       addEvent(()-> {
        this.setAirCircleAttack(false);
        int randI = ModRand.range(1, 11);
        if(randI >= 7 || !this.isMeleeViable) {
            setAirCircleTooRanged(target);
        } else {
            //continue with melee
            this.setAirCircleTooMelee(target);
        }
       }, 50);
    };

    private void setAirCircleTooMelee(EntityLivingBase target) {
        air_circle_melee.accept(target);
    }

    private final Consumer<EntityLivingBase> air_circle_melee = (target) -> {
        this.setAirCircleContinue(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 50);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 60);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 100);
        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.1F);
            }, 5);
        }, 15);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.6f, 0, false);
            // Summon a Lightning Ring
            if(world.rand.nextBoolean()) {
                this.playSound(SoundsHandler.HIGH_KING_SWING_MAGIC, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                new ActionKingLightningRing().performAction(this, target);
            } else {
                this.playSound(SoundsHandler.HIGH_KING_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            }
            this.setNoGravity(false);
        }, 26);

        addEvent(()-> this.setImmovable(true), 40);

        addEvent(()-> {
            Vec3d targetedPos = target.getPositionVector();
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.1F);
            }, 5);
        }, 65);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2.2, 1.4, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (this.getAttack() * 1.25);
            ModUtils.handleAreaImpact(2.2f, (e) -> damage, this, offset, source, 0.4f, 0, false);
            this.playSound(SoundsHandler.HIGH_KING_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 75);

        addEvent(()-> {
            this.setImmovable(true);
            new ActionHalberdSpecial().performAction(this, target);
        }, 79);

        addEvent(()-> this.lockLook = false, 90);

        addEvent(()-> {
            this.setAirCircleContinue(false);
            int randI = ModRand.range(1, 11);
            if(randI <= 3 ||this.isMeleeViable && randI <= 7) {
                this.setAttackTooJumpAOE(target);
            } else {
                this.setDoubleSwingFinish(true);
                addEvent(()-> {
                    this.setImmovable(false);
                    this.setFightMode(false);
                    this.setFullBodyUsage(false);
                    this.setDoubleSwingFinish(false);
                }, 15);
            }
        }, 110);
    };

    private void setAirCircleTooRanged(EntityLivingBase target) {
        air_circle_ranged.accept(target);
    }

    private final Consumer<EntityLivingBase> air_circle_ranged = (target) -> {
      this.setAirCircleRanged(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_CLAW_DRAW, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_CAST_CLAW, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 29);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 45);
      addEvent(()-> {
          this.lockLook = true;
      }, 22);

      addEvent(()-> {
        //Do Claw Action
          new ActionScatteredSmallHolyWave((int) this.getDistance(target)).performAction(this, target);
      }, 25);

      addEvent(()-> {
          this.setImmovable(false);
        this.setNoGravity(false);
        this.lockLook = false;
      }, 35);

      addEvent(()-> {
        this.setAirCircleRanged(false);
        this.setImmovable(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
      }, 55);
    };
    private final Consumer<EntityLivingBase> dodge_action = (target) -> {
        int randI = ModRand.range(1, 11);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        if(randI >= 6) {
            this.setStrafeDodge(true);
            addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 3);
            addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 20);
            addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 33);
            addEvent(()-> {
                this.currentlyInIFrame = true;
                this.setImmovable(false);
                Vec3d jumpPos = this.getCircleRadiPoint((int) this.getDistance(target) + 2, target.getPositionVector());
                Vec3d dirToo = this.getPositionVector().subtract(jumpPos).normalize();
                Vec3d finalPos = this.getPositionVector().add(dirToo.scale(47));
                ModUtils.leapTowards(this, finalPos, 1.6F, 0.25F);
            }, 5);

            addEvent(()-> {
                this.currentlyInIFrame = false;
                this.setImmovable(true);
            }, 30);

            addEvent(()-> {
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setStrafeDodge(false);
            this.setImmovable(false);
            }, 40);
        } else {
            //regular Dodge
            this.setDodge(true);
            addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 2);
            addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
            addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 25);
            addEvent(()-> {
                this.currentlyInIFrame = true;
                Vec3d dirToo = this.getPositionVector().subtract(target.getPositionVector()).normalize();
                Vec3d jumpTooPos = this.getPositionVector().add(dirToo.scale(25));
                double distanceToo = this.getPositionVector().distanceTo(jumpTooPos);
                this.setImmovable(false);
                ModUtils.leapTowards(this, jumpTooPos, 1.4F, 0.25F);
            }, 9);

            addEvent(()-> {
            this.currentlyInIFrame = false;
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setDodge(false);
            }, 30);
        }
    };

    private Vec3d getCircleRadiPoint(int distance, Vec3d originPos) {
        Vec3d pos = new Vec3d(0,0,0);
        float degrees = -100f / 4;
        for (int i = 0; i < 4; i++) {
            double radians = Math.toRadians(i * degrees);
            Vec3d offset = new Vec3d(Math.sin(radians), Math.cos(radians), 0).scale(distance);
            pos.add(offset.x, 0, offset.y).add(originPos);
        }

        return pos;
    }


    private final Consumer<EntityLivingBase> cast_claw_attack = (target) -> {
      this.setKingCast(true);
      this.setFightMode(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 7);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_CLAW_DRAW, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 14);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_CAST_CLAW, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 25);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 43);
      addEvent(()-> {
        //Do Claw Action
          new ActionScatteredSmallHolyWave((int) this.getDistance(target)).performAction(this, target);
      }, 25);

      addEvent(()-> {
        this.setKingCast(false);
        this.setFightMode(false);
      }, 50);
    };
    private final Consumer<EntityLivingBase> cast_lightning_attack = (target) -> {
      this.setKingCastLightning(true);
      this.setFightMode(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 45);
      addEvent(()-> {
        //Cast Lightning Attack On Player
          new ActionKingAloneLightning().performAction(this,target);
      }, 35);

      addEvent(()-> {
        this.setKingCastLightning(false);
        this.setFightMode(false);
      }, 50);
    };

    private final Consumer<EntityLivingBase> double_swing = (target) -> {
      this.setDoubleSwing(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 40);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 55);
      addEvent(()-> {
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
          addEvent(()-> {
              this.setImmovable(false);
              //this.holdPosition = false;
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.25F);
          }, 5);
      }, 13);


      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.4f, 0, false);
          // Summon a Lightning Ring
          if(world.rand.nextBoolean()) {
              this.playSound(SoundsHandler.HIGH_KING_SWING_MAGIC, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
              new ActionKingLightningRing().performAction(this, target);
          } else {
              this.playSound(SoundsHandler.HIGH_KING_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          }
          double healthFac = this.getHealth() / this.getMaxHealth();
          if(healthFac <= 0.5 && world.rand.nextBoolean() && !MobConfig.disable_blood_attacks) {
              new ActionBloodSpray().performAction(this, target);
          }
      }, 23);

      addEvent(()-> this.setImmovable(true), 35);

      addEvent(() -> {
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
          addEvent(()-> {
              this.setImmovable(false);
              //this.holdPosition = false;
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.25F);
          }, 5);
      }, 60);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.4f, 0, false);
          // Summon a Lightning Ring
          if(world.rand.nextBoolean()) {
              this.playSound(SoundsHandler.HIGH_KING_SWING_MAGIC, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
              new ActionKingLightningRing().performAction(this, target);
          } else {
              this.playSound(SoundsHandler.HIGH_KING_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          }
          double healthFac = this.getHealth() / this.getMaxHealth();
          if(healthFac <= 0.5 && world.rand.nextBoolean() && !MobConfig.disable_blood_attacks) {
              new ActionBloodSpray().performAction(this, target);
          }
      }, 70);

      addEvent(()-> {
          this.setImmovable(true);
        this.setDoubleSwing(false);

        int randI = ModRand.range(1, 10);
        if(randI <= 3 || this.isMeleeViable && randI <= 7) {
            //Continue with a jump attack
            setAttackTooJumpAOE(target);
        } else {
            this.setDoubleSwingFinish(true);
            addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
            addEvent(()-> {
                this.setDoubleSwingFinish(false);
                this.setImmovable(false);
                this.setFightMode(false);
                this.setFullBodyUsage(false);
            }, 15);
        }

      }, 80);
    };

    private void setAttackTooJumpAOE(EntityLivingBase target) {
        double_swing_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> double_swing_continue = (target) -> {
        this.setDoubleSwingJump(true);
        this.setNoGravity(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 75);
        this.HoverTimeIncrease = 3;
        addEvent(()-> {
            this.setImmovable(false);
            this.hasHoverMovement = true;
        }, 15);

        addEvent(()-> {
            this.hasHoverMovement = false;
            this.setNoGravity(false);
        }, 32);
        addEvent(()-> {
            this.lockLook = true;
            this.setImmovable(true);
            //Spawn AOE
            this.playSound(SoundsHandler.HIGH_KING_SPEAR_IMPACT, 1.5f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            new ActionHolySpikeAOE((int) this.getDistance(target) + 5).performAction(this, target);
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.25, 1.2, 0)));
            Main.proxy.spawnParticle(20,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
        }, 46);

        addEvent(()-> {
            this.setShaking(true);
            this.shakeTime = 20;
        }, 46);

        addEvent(()-> {
            this.setShaking(false);
        }, 61);

        addEvent(()-> this.lockLook = false, 75);

        addEvent(()-> {
            this.setDoubleSwingJump(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setImmovable(false);
        }, 85);
    };
    private final Consumer<EntityLivingBase> fast_aoe_attack = (target) -> {
      this.setFastAoe(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 2);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 55);
      addEvent(()-> {
          this.lockLook = true;
      }, 25);

      addEvent(()-> {
          this.playSound(SoundsHandler.GARGOYLE_CAST_SPECIAL, 1.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 40);
      addEvent(()-> {
          //Action
          new ActionKingProgAOE().performAction(this, target);
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.2, 0)));
          Main.proxy.spawnParticle(20,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
          this.setShaking(true);
          this.shakeTime = 40;
      }, 45);

        addEvent(()-> {
            this.setShaking(false);
        }, 70);

      addEvent(()-> this.lockLook = false, 55);

      addEvent(()-> {
          this.aoeCooldown = MobConfig.high_king_aoe_cooldown * 20;
          this.setImmovable(false);
          this.setFastAoe(false);
          this.setFightMode(false);
          this.setFullBodyUsage(false);
      },70);
    };

    private final Consumer<EntityLivingBase> heavy_swing_begin = (target) -> {
        this.setHeavySwing(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 35);
        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-2));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                //this.holdPosition = false;
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.25F);
            }, 5);
        }, 15);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.7, 1.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack() * 1.3);
            ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.4f, 0, false);
            // Summon a Lightning Ring
            new ActionKingLightningRing().performAction(this, target);
            this.playSound(SoundsHandler.HIGH_KING_SWING_MAGIC, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 25);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(true);
        }, 35);

        addEvent(() -> {
            this.setHeavySwing(false);
            int randI = ModRand.range(1, 10);
            if(randI <= 3 || this.isMeleeViable) {
                //Continue with a Pierce
                this.setHeavySwingTooContinue(target);
            } else {
                //End the Current Attack
                this.setHeavySwingFinish(true);
                addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 10);
                addEvent(()-> this.setImmovable(false), 15);

                addEvent(()-> {
                    this.setHeavySwingFinish(false);
                    this.setFightMode(false);
                    this.setFullBodyUsage(false);
                }, 20);
            }
        }, 45);
    };

    private void setHeavySwingTooContinue(EntityLivingBase target) {
        heavy_swing_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> heavy_swing_continue = (target) -> {
      this.setHeavySwingContinue(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 15);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 45);
      addEvent(()-> {
          Vec3d targetedPos = target.getPositionVector();
          this.lockLook = true;
          addEvent(()-> {
              this.setImmovable(false);
              //this.holdPosition = false;
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.25F);
          }, 5);
      }, 23);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2.2, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.4f, 0, false);
          this.playSound(SoundsHandler.HIGH_KING_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          double healthFac = this.getHealth() / this.getMaxHealth();
          if(healthFac <= 0.5 && !MobConfig.disable_blood_attacks) {
              new ActionBloodSpray().performAction(this, target);
          }
      }, 34);

      addEvent(()-> this.setImmovable(true), 43);
      addEvent(()-> this.lockLook = false, 50);

      addEvent(()-> {
        this.setHeavySwingContinue(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
      }, 60);
    };


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "state_controller", 0, this::predicateStates));
        data.addAnimationController(new AnimationController(this, "main_attack_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "followup_attack_controller", 0, this::predicateAttacksFollowUp));
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "walk_controller", 0, this::predicateWalk));
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFightMode() && !this.isBlockAction() && !this.isBlockWithRanged() && !this.isDeathBoss()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateWalk(AnimationEvent<E> event) {

        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage() && !this.isDeathBoss()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateStates(AnimationEvent<E> event) {
        if(this.isSummonBoss()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
            return PlayState.CONTINUE;
        }
        if(this.isPhaseTransition()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PHASE_TRANSITION, false));
            return PlayState.CONTINUE;
        }
        if(this.isDeathBoss()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DEATH, false));
            return PlayState.CONTINUE;
        }
        if(this.isBlockAction() && !this.isDeathBoss()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLOCK, false));
            return PlayState.CONTINUE;
        }
        if(this.isBlockWithRanged() && !this.isDeathBoss()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLOCK_RANGED, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isDeathBoss()) {
            if(this.isDodge()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DODGE, false));
                return PlayState.CONTINUE;
            }
            if(this.isStrafeDodge()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRAFE_DODGE, false));
                return PlayState.CONTINUE;
            }
            if(this.isHeavySwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HEAVY_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isKingCastLightning()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_LIGHTNING, false));
                return PlayState.CONTINUE;
            }
            if(this.isKingCast()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_CLAW, false));
                return PlayState.CONTINUE;
            }
            if(this.isHolyWave()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HOLY_WAVE, false));
                return PlayState.CONTINUE;
            }
            if(this.isFastAoe()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FAST_AOE, false));
                return PlayState.CONTINUE;
            }
            if(this.isDoubleSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DOUBLE_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isSpearThrust()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SPEAR_THRUST, false));
                return PlayState.CONTINUE;
            }
            if(this.isAirCircleAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CIRCLE_AIR_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isStomp()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STOMP_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isBloodyFly()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLOODY_FLY, false));
                return PlayState.CONTINUE;
            }
            if(this.isStrafeThrust()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRAFE_THRUST, false));
                return PlayState.CONTINUE;
            }
            if(this.isBloodyGrab()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLOODY_GRAB, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacksFollowUp(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isDeathBoss()) {
            if(this.isheavySwingContinue()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HEAVY_SWING_CONTINUE));
                return PlayState.CONTINUE;
            }
            if(this.isHeavySwingFinish()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HEAVY_SWING_FINISH, false));
                return PlayState.CONTINUE;
            }
            if(this.isDoubleSwingJump()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DOUBLE_SWING_JUMP, false));
                return PlayState.CONTINUE;
            }
            if(this.isDoubleSwingFinish()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DOUBLE_SWING_FINISH, false));
                return PlayState.CONTINUE;
            }
            if(this.isSpearThrustContinue()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SPEAR_THRUST_CONTINUE, false));
                return PlayState.CONTINUE;
            }
            if(this.isSpearThrustFinish()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SPEAR_THRUST_FINISH, false));
                return PlayState.CONTINUE;
            }
            if(this.isAirCircleRanged()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CIRCLE_AIR_RANGED, false));
                return PlayState.CONTINUE;
            }
            if(this.isAirCircleContinue()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CIRCLE_AIR_CONTINUE, false));
                return PlayState.CONTINUE;
            }
            if(this.isStompContinue()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STOMP_CONTINUE, false));
                return PlayState.CONTINUE;
            }
            if(this.isStompFinish()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STOMP_FINISH, false));
                return PlayState.CONTINUE;
            }
            if(this.isStrafeThrustContinue()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRAFE_THRUST_CONTINUE, false));
                return PlayState.CONTINUE;
            }
            if(this.isStrafeThrustFinish()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRAFE_THRUST_FINISH, false));
                return PlayState.CONTINUE;
            }
            if(this.isBloodyGrabContinue()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLOODY_GRAB_CONTINUE, false));
                return PlayState.CONTINUE;
            }
            if(this.isBloodyGrabEnd()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLOODY_GRAB_END, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);

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
    public final boolean attackEntityFrom(DamageSource source, float amount) {

        if(this.isBloodyGrabContinue() || this.isSummonBoss() || this.isPhaseTransition() || this.isSpearThrustContinue() || this.currentlyInIFrame) {
            return false;
            //Check to see if we can block this attack
        } else if (!this.isFightMode() && amount > 0.0F && this.canBlockDamageSource(source)) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }
            this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.6f, 0.6f + ModRand.getFloat(0.2f));

            return false;
        }

        if(ModConfig.boss_cap_damage_enabled && amount > MobConfig.high_king_damage_cap) {
            return super.attackEntityFrom(source, MobConfig.high_king_damage_cap);
        }

        //do regular damage
        return super.attackEntityFrom(source, amount);
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if(!this.isFightMode() && !this.isDeathBoss()) {
            if (!damageSourceIn.isUnblockable() && !this.isFightMode() && blockCooldown <= 0) {
                EntityLivingBase target = this.getAttackTarget();
                if(target != null && this.getDistanceSq(target) >= 196 && !this.isBlockWithRanged() && !this.isBlockAction()) {
                    //We want a certain distance where the attack part if variable, greater than 6 and less than 14
                                if(world.rand.nextBoolean()) {
                                    this.doBlockAction();
                                } else {
                                    this.doBlockActionWithAttack(target);
                                }
                } else if (!this.isBlockWithRanged() && !this.isBlockAction()){
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

    private void doBlockAction() {
        this.setBlockAction(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 22);
        addEvent(()-> {
            this.setBlockAction(false);
            this.blockCooldown = MobConfig.high_king_block_cooldown * 20;
        }, 25);
    }

    private void doBlockActionWithAttack(EntityLivingBase target) {
        this.setBlockWithRanged(true);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 22);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_CLAW_DRAW, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 32);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_CAST_CLAW, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 37);
        addEvent(()-> {
            this.setImmovable(true);
        }, 30);

        addEvent(()-> {
            //Do Action
            new ActionScatteredSmallHolyWave((int) this.getDistance(target)).performAction(this, target);
        }, 37);
        addEvent(()-> {
        this.setImmovable(false);
        this.setBlockWithRanged(false);
        this.blockCooldown = MobConfig.high_king_block_cooldown * 20;
        }, 60);
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
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.00001F);
        if(!this.isDeathBoss()) {
            this.clearEvents();
        }
        this.setDeathBoss(true);
        this.setImmovable(true);
        this.lockLook = true;
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 40);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 80);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_KING_ARMOR_MOVEMENT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 105);
        addEvent(()-> {
            if(!world.isRemote) {
                for(int i = 0; i <= 100; i+=5) {
                    if(!world.isRemote) {
                        addEvent(() -> {
                            EntityXPOrb orb = new EntityXPOrb(world, this.posX, this.posY, this.posZ, MobConfig.high_king_experience_value / 20);
                            orb.setPosition(this.posX, this.posY + 1, this.posZ);
                            world.spawnEntity(orb);
                        }, i);
                    }
                }
            }
        }, 180);

        addEvent(()-> {
            if(this.getSpawnLocation() != null) {
                this.turnBossIntoSummonSpawner(this.getSpawnLocation());
            }
            this.setDead();
            this.setDropItemsWhenDead(true);
            this.setDeathBoss(false);
        }, 280);

        super.onDeath(cause);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.HIGH_KING_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        if(!this.hasNoGravity()) {
            this.playSound(SoundsHandler.HIGH_KING_STEP, 0.4F, 0.4f + ModRand.getFloat(0.3F));
        }

    }

    private static final ResourceLocation LOOT_BOSS = new ResourceLocation(ModReference.MOD_ID, "high_king");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_BOSS;
    }


    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 20.0F);
            if (dist >= 20.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.25F * screamMult);
        }
        return 0;
    }

    @Nullable
    @Override
    public SoundEvent getBossMusic() {
        return SoundsHandler.HIGH_KING_TRACK;
    }
}
