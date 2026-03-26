package com.dungeon_additions.da.entity.dark_dungeon.boss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityAIDarkDriftDevilAttack;
import com.dungeon_additions.da.entity.blossom.EntityAbstractVoidBlossom;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.dark_dungeon.boss.action.*;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.generic.EntityDelayedExplosion;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.entity.util.IEntitySound;
import com.dungeon_additions.da.entity.void_dungeon.EntityObsidilith;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.ServerScaleUtil;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.sun.jna.platform.win32.WinDef;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityVillager;
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
import net.minecraft.util.SoundEvent;
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
import java.util.function.Consumer;

public class EntityDarkdriftDevil extends EntityDarkBase implements IAnimatable, IAnimationTickable, IAttack, IEntitySound {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_10));
    private EntityLivingBase grabbedEntity;
    private boolean grabDetection = false;

    private final String ANIM_IDLE_LOWER = "idle_lower";
    private final String ANIM_IDLE_UPPER = "idle_upper";
    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_IDLE_EYES = "idle_eyes";

    //meditation
    private final String ANIM_ENTER_MEDITATION = "enter_meditation";
    private final String ANIM_MEDITATION_LOOP = "meditation_loop";
    private final String ANIM_EXIT_MEDITATION = "exit_meditation";

    //attacks one anim
    private final String ANIM_TELEPORT_ATTACK = "teleport";
    private final String ANIM_CAST_SIGIL = "cast_sigil";
    private final String ANIM_SUMMON_METEORS = "summon_meteors";
    private final String ANIM_GREAT_DEATH = "great_death";
    private final String ANIM_CAST_SIGIL_ROUND = "cast_multi_sigil";
    private final String ANIM_DESPAIR = "despair";
    private final String ANIM_SPEAR_INCARNATE = "spear_incarnate";

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

    private final String ANIM_GRAB = "grab";
    private final String ANIM_GRAB_CONTINUE = "grab_continue";
    private final String ANIM_GRAB_FINISH = "grab_finish";



    private static final DataParameter<Boolean> TELEPORT_ATTACK = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_METEOR = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GRAB_ATTACK = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GRAB_CONTINUE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GRAB_FINISH = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LONG_SWING = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LONG_SWING_CONTINUE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LONG_SWING_FINISH = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_SIGIL = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STOMP = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STOMP_CONTINUE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STOMP_FINISH = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> THROW_DAGGERS = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> THROW_DAGGER_CONTINUE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> THROW_DAGGERS_DASH = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_SLAM = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_ROUND_SIGIL = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_DESPAIR = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> EVADE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPEAR_ULTIMATE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ENTER_MEDITATION = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MEDITATION_LOOP = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_MEDITATION = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_MELEE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_MELEE_FINISH = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_MELEE_CONTINUE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_MELEE_FINISH_TWO = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_MELEE_DASH = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GREAT_DEATH = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEMON_BUFF = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BLOCK_POS);
    public static DataParameter<Boolean> SET_SPAWN_LOC = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAD_PREVIOUS_TARGET = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> STAT_LINE = EntityDataManager.createKey(EntityDarkdriftDevil.class, DataSerializers.FLOAT);

    private void setTeleportAttack(boolean value) {this.dataManager.set(TELEPORT_ATTACK, Boolean.valueOf(value));}
    private boolean isTeleportAttack() {return this.dataManager.get(TELEPORT_ATTACK);}
    private void setSummonMeteor(boolean value) {this.dataManager.set(SUMMON_METEOR, Boolean.valueOf(value));}
    private boolean isSummonMeteor() {return this.dataManager.get(SUMMON_METEOR);}
    private void setGrabAttack(boolean value) {this.dataManager.set(GRAB_ATTACK, Boolean.valueOf(value));}
    private boolean isGrabAttack() {return this.dataManager.get(GRAB_ATTACK);}
    private void setGrabContinue(boolean value) {this.dataManager.set(GRAB_CONTINUE, Boolean.valueOf(value));}
    private boolean isGrabContinue() {return this.dataManager.get(GRAB_CONTINUE);}
    private void setGrabFinish(boolean value) {this.dataManager.set(GRAB_FINISH, Boolean.valueOf(value));}
    private boolean isGrabFinish() {return this.dataManager.get(GRAB_FINISH);}
    private void setLongSwing(boolean value) {this.dataManager.set(LONG_SWING, Boolean.valueOf(value));}
    private boolean isLongSwing() {return this.dataManager.get(LONG_SWING);}
    private void setLongSwingContinue(boolean value) {this.dataManager.set(LONG_SWING_CONTINUE, Boolean.valueOf(value));}
    private boolean isLongSwingContinue() {return this.dataManager.get(LONG_SWING_CONTINUE);}
    private void setCastSigil(boolean value) {this.dataManager.set(CAST_SIGIL, Boolean.valueOf(value));}
    private boolean isCastSigil() {return this.dataManager.get(CAST_SIGIL);}
    private void setStomp(boolean value) {this.dataManager.set(STOMP, Boolean.valueOf(value));}
    private boolean isStomp() {return this.dataManager.get(STOMP);}
    private void setStompContinue(boolean value) {this.dataManager.set(STOMP_CONTINUE, Boolean.valueOf(value));}
    private boolean isStompContinue() {return this.dataManager.get(STOMP_CONTINUE);}
    private void setStompFinish(boolean value) {this.dataManager.set(STOMP_FINISH, Boolean.valueOf(value));}
    private boolean isStompFinish() {return this.dataManager.get(STOMP_FINISH);}
    private void setThrowDaggers(boolean value) {this.dataManager.set(THROW_DAGGERS, Boolean.valueOf(value));}
    private boolean isThrowDaggers() {return this.dataManager.get(THROW_DAGGERS);}
    private void setThrowDaggerContinue(boolean value) {this.dataManager.set(THROW_DAGGER_CONTINUE, Boolean.valueOf(value));}
    private boolean isThrowDaggersContinue() {return this.dataManager.get(THROW_DAGGER_CONTINUE);}
    private void setThrowDaggersDash(boolean value) {this.dataManager.set(THROW_DAGGERS_DASH, Boolean.valueOf(value));}
    private boolean isThrowDaggersDash() {return this.dataManager.get(THROW_DAGGERS_DASH);}
    private void setJumpSlam(boolean value) {this.dataManager.set(JUMP_SLAM, Boolean.valueOf(value));}
    private boolean isJumpSlam() {return this.dataManager.get(JUMP_SLAM);}
    private void setCastRoundSigil(boolean value) {this.dataManager.set(CAST_ROUND_SIGIL, Boolean.valueOf(value));}
    private boolean isCastRoundSigil() {return this.dataManager.get(CAST_ROUND_SIGIL);}
    private void setCastDespair(boolean value) {this.dataManager.set(CAST_DESPAIR, Boolean.valueOf(value));}
    private boolean isCastDespair() {return this.dataManager.get(CAST_DESPAIR);}
    private void setEvade(boolean value) {this.dataManager.set(EVADE, Boolean.valueOf(value));}
    private boolean isEvade() {return this.dataManager.get(EVADE);}
    private void setSpearUltimate(boolean value) {this.dataManager.set(SPEAR_ULTIMATE, Boolean.valueOf(value));}
    private boolean isSpearUltimate() {return this.dataManager.get(SPEAR_ULTIMATE);}
    private void setEnterMeditation(boolean value) {this.dataManager.set(ENTER_MEDITATION, Boolean.valueOf(value));}
    private boolean isEnterMeditation() {return this.dataManager.get(ENTER_MEDITATION);}
    private void setMeditationLoop(boolean value) {this.dataManager.set(MEDITATION_LOOP, Boolean.valueOf(value));}
    public boolean isMeditationLoop() {return this.dataManager.get(MEDITATION_LOOP);}
    private void setDemonBuff(boolean value) {this.dataManager.set(DEMON_BUFF, Boolean.valueOf(value));}
    public boolean isDemonBuff() {return this.dataManager.get(DEMON_BUFF);}
    private void setEndMeditation(boolean value) {this.dataManager.set(END_MEDITATION, Boolean.valueOf(value));}
    private boolean isEndMeditation() {return this.dataManager.get(END_MEDITATION);}
    private void setSwingMelee(boolean value) {this.dataManager.set(SWING_MELEE, Boolean.valueOf(value));}
    private boolean isSwingMelee() {return this.dataManager.get(SWING_MELEE);}
    private void setSwingMeleeContinue(boolean value) {this.dataManager.set(SWING_MELEE_CONTINUE, Boolean.valueOf(value));}
    private boolean isSwingMeleeContinue() {return this.dataManager.get(SWING_MELEE_CONTINUE);}
    private void setSwingMeleeFinish(boolean value) {this.dataManager.set(SWING_MELEE_FINISH, Boolean.valueOf(value));}
    private boolean isSwingMeleeFinish() {return this.dataManager.get(SWING_MELEE_FINISH);}
    private void setSwingMeleeDash(boolean value) {this.dataManager.set(SWING_MELEE_DASH, Boolean.valueOf(value));}
    private boolean isSwingMeleeDash() {return this.dataManager.get(SWING_MELEE_DASH);}
    private void setSwingMeleeFinishTwo(boolean value) {this.dataManager.set(SWING_MELEE_FINISH_TWO, Boolean.valueOf(value));}
    private boolean isSwingMeleeFinishTwo() {return this.dataManager.get(SWING_MELEE_FINISH_TWO);}
    private void setLongSwingFinish(boolean value) {this.dataManager.set(LONG_SWING_FINISH, Boolean.valueOf(value));}
    private boolean isLongSwingFinish() {return this.dataManager.get(LONG_SWING_FINISH);}
    private void setGreatDeath(boolean value) {this.dataManager.set(GREAT_DEATH, Boolean.valueOf(value));}
    private boolean isGreatDeath() {return this.dataManager.get(GREAT_DEATH);}
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
    }

    public EntityDarkdriftDevil(World worldIn) {
        super(worldIn);
        this.setSize(1.1F, 3.45F);
        this.iAmBossMob = true;
        this.isImmuneToFire = true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Teleport_Attack", this.isTeleportAttack());
        nbt.setBoolean("Summon_Meteor", this.isSummonMeteor());
        nbt.setBoolean("Grab_Attack", this.isGrabAttack());
        nbt.setBoolean("Grab_Continue", this.isGrabContinue());
        nbt.setBoolean("Grab_Finish", this.isGrabFinish());
        nbt.setBoolean("Long_Swing", this.isLongSwing());
        nbt.setBoolean("Long_Swing_Continue", this.isLongSwingContinue());
        nbt.setBoolean("Cast_Sigil", this.isCastSigil());
        nbt.setBoolean("Stomp", this.isStomp());
        nbt.setBoolean("Stomp_Continue", this.isStompContinue());
        nbt.setBoolean("Stomp_Finish", this.isStompFinish());
        nbt.setBoolean("Throw_Daggers", this.isThrowDaggers());
        nbt.setBoolean("Throw_Daggers_Continue", this.isThrowDaggersContinue());
        nbt.setBoolean("Throw_Daggers_Dash", this.isThrowDaggersDash());
        nbt.setBoolean("Jump_Slam", this.isJumpSlam());
        nbt.setBoolean("Cast_Round_Sigil", this.isCastRoundSigil());
        nbt.setBoolean("Cast_Despair", this.isCastDespair());
        nbt.setBoolean("Evade", this.isEvade());
        nbt.setBoolean("Spear_Ultimate", this.isSpearUltimate());
        nbt.setBoolean("Enter_Meditation", this.isEnterMeditation());
        nbt.setBoolean("Meditation_Loop", this.isMeditationLoop());
        nbt.setBoolean("End_Meditation", this.isEndMeditation());
        nbt.setBoolean("Swing_Melee", this.isSwingMelee());
        nbt.setBoolean("Swing_Continue", this.isSwingMeleeContinue());
        nbt.setBoolean("Swing_Finish", this.isSwingMeleeFinish());
        nbt.setBoolean("Swing_Dash", this.isSwingMeleeDash());
        nbt.setBoolean("Swing_Finish_Two", this.isSwingMeleeFinishTwo());
        nbt.setBoolean("Long_Swing_Finish", this.isLongSwingFinish());
        nbt.setBoolean("Great_Death", this.isGreatDeath());
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
        this.setTeleportAttack(nbt.getBoolean("Teleport_Attack"));
        this.setSummonMeteor(nbt.getBoolean("Summon_Meteor"));
        this.setGrabAttack(nbt.getBoolean("Grab_Attack"));
        this.setGrabContinue(nbt.getBoolean("Grab_Continue"));
        this.setGrabFinish(nbt.getBoolean("Grab_Finish"));
        this.setLongSwing(nbt.getBoolean("Long_Swing"));
        this.setLongSwingContinue(nbt.getBoolean("Long_Swing_Continue"));
        this.setCastSigil(nbt.getBoolean("Cast_Sigil"));
        this.setStomp(nbt.getBoolean("Stomp"));
        this.setStompContinue(nbt.getBoolean("Stomp_Continue"));
        this.setStompFinish(nbt.getBoolean("Stomp_Finish"));
        this.setThrowDaggers(nbt.getBoolean("Throw_Daggers"));
        this.setThrowDaggerContinue(nbt.getBoolean("Throw_Daggers_Continue"));
        this.setThrowDaggersDash(nbt.getBoolean("Throw_Daggers_Dash"));
        this.setJumpSlam(nbt.getBoolean("Jump_Slam"));
        this.setCastRoundSigil(nbt.getBoolean("Cast_Round_Sigil"));
        this.setCastDespair(nbt.getBoolean("Cast_Despair"));
        this.setEvade(nbt.getBoolean("Evade"));
        this.setSpearUltimate(nbt.getBoolean("Spear_Ultimate"));
        this.setEnterMeditation(nbt.getBoolean("Enter_Meditation"));
        this.setMeditationLoop(nbt.getBoolean("Meditation_Loop"));
        this.setEndMeditation(nbt.getBoolean("End_Meditation"));
        this.setSwingMelee(nbt.getBoolean("Swing_Melee"));
        this.setDemonBuff(nbt.getBoolean("Demon_Buff"));
        this.setSwingMeleeDash(nbt.getBoolean("Swing_Dash"));
        this.setSwingMeleeContinue(nbt.getBoolean("Swing_Continue"));
        this.setSwingMeleeFinish(nbt.getBoolean("Swing_Finish"));
        this.setSwingMeleeFinishTwo(nbt.getBoolean("Swing_Finish_Two"));
        this.setLongSwingFinish(nbt.getBoolean("Long_Swing_Finish"));
        this.setGreatDeath(nbt.getBoolean("Great_Death"));
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
        this.dataManager.register(TELEPORT_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_METEOR, Boolean.valueOf(false));
        this.dataManager.register(GRAB_FINISH, Boolean.valueOf(false));
        this.dataManager.register(GRAB_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(GRAB_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(LONG_SWING, Boolean.valueOf(false));
        this.dataManager.register(LONG_SWING_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(CAST_SIGIL, Boolean.valueOf(false));
        this.dataManager.register(STOMP, Boolean.valueOf(false));
        this.dataManager.register(STOMP_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(STOMP_FINISH, Boolean.valueOf(false));
        this.dataManager.register(THROW_DAGGERS, Boolean.valueOf(false));
        this.dataManager.register(THROW_DAGGERS_DASH, Boolean.valueOf(false));
        this.dataManager.register(THROW_DAGGER_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(CAST_ROUND_SIGIL, Boolean.valueOf(false));
        this.dataManager.register(JUMP_SLAM, Boolean.valueOf(false));
        this.dataManager.register(CAST_DESPAIR, Boolean.valueOf(false));
        this.dataManager.register(EVADE, Boolean.valueOf(false));
        this.dataManager.register(SPEAR_ULTIMATE, Boolean.valueOf(false));
        this.dataManager.register(ENTER_MEDITATION, Boolean.valueOf(false));
        this.dataManager.register(MEDITATION_LOOP, Boolean.valueOf(false));
        this.dataManager.register(END_MEDITATION, Boolean.valueOf(false));
        this.dataManager.register(SWING_MELEE_FINISH_TWO, Boolean.valueOf(false));
        this.dataManager.register(SWING_MELEE_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(SWING_MELEE, Boolean.valueOf(false));
        this.dataManager.register(SWING_MELEE_DASH, Boolean.valueOf(false));
        this.dataManager.register(SWING_MELEE_FINISH, Boolean.valueOf(false));
        this.dataManager.register(LONG_SWING_FINISH,  Boolean.valueOf(false));
        this.dataManager.register(GREAT_DEATH, Boolean.valueOf(false));
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
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(29);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.31D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(525);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(14);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(6);
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
        double currentHealth = this.getHealth() / this.getMaxHealth();
        this.bossInfo.setPercent(getHealth() / getMaxHealth());

        if(world.isRemote && ticksExisted == 5 && ModConfig.experimental_features && MobConfig.darkdrift_devil_boss_music) {
            this.playMusic(this);
        }

        if(!world.isRemote) {

            //demon buff
            if(this.isDemonBuff()) {
                if(demon_buff_time <= 0) {
                    this.setDemonBuff(false);
                    this.demon_buff_time = 45 * 20;
                    this.demon_buff_damage = 0;
                } else {
                    demon_buff_time--;
                    demon_buff_damage = 5;
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

            if (this.ticksExisted == 5) {
                if (this.getSpawnLocation() != null && !this.isSetSpawnLoc()) {
                        this.setSpawnLocation(this.getPosition());
                        this.setSetSpawnLoc(true);
                }
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
                                // turnBossIntoSummonSpawner(this.getSpawnLocation());
                                this.setDead();
                            } else {
                                // this.resetBossTask();
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

    private void enterMeditationMode() {
        this.setEnterMeditation(true);
        this.setImmovable(true);
        this.setFullBodyUsage(true);
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

    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        if(!this.isFightMode() && !this.isFullBodyUsage() && !this.didAirAttack && !this.isMeditationLoop() && !this.isEndMeditation() && !this.isEnterMeditation() && !this.awaiting_meditation) {
            double distance = Math.sqrt(distanceSq);
            double healthFac = this.getHealth() / this.getMaxHealth();
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(teleport_attack, long_swing, stomp, throw_daggers, melee_attack, great_death, cast_sigil, cast_round_sigil, summon_meteors, grab_attack));
            double[] weights = {
                    (prevAttack != teleport_attack && distance > 5) ? distance * 0.02 : 0, //Teleport Attack
                    (prevAttack != long_swing && distance < 15) ? distance * 0.02 : 0, //Long Swing
                    (prevAttack != stomp && distance <= 18 && healthFac <= 0.75) ? 1/distance : 0, //Stomp Attack % 75
                    (prevAttack != throw_daggers && distance > 10) ? distance * 0.02 : 0, //Throw Daggers
                    (prevAttack != melee_attack && distance < 7) ? 1/distance : 0, //Melee Attack
                    (prevAttack != great_death && distance <=18) ? distance * 0.02 : 0, //Great Death
                    (prevAttack != cast_sigil) ? 1/distance : 0, //Cast Sigil
                    (prevAttack != cast_round_sigil && distance > 3 && distance <= 16 && healthFac <= 0.75) ? distance * 0.02 : 0, //Cast Round Sigil % 75
                    (prevAttack != summon_meteors && healthFac <= 0.75) ? distance * 0.02 : 0, //Summon Meteors % 75
                    (prevAttack != grab_attack && distance > 6 && healthFac <= 0.5) ? 1/distance : 0 //Grab Attack % 50
            };
            prevAttack = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttack.accept(target);
        }
        return 1;
    }

    private final Consumer<EntityLivingBase> despair_attack = (target) -> {
        this.setCastDespair(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);

        addEvent(()-> {
            this.setImmovable(false);
            this.setNoGravity(true);
            this.HoverTimeIncrease = 3;
        }, 10);

        addEvent(()-> {
            this.motionY = 0;
            this.setImmovable(true);
            this.setNoGravity(false);
        }, 35);

        addEvent(() -> {
            //Start casting
            this.lockLook = true;
        }, 50);

        addEvent(()-> {
            //end casting & remove tokens
            this.lockLook = false;
        }, 340);

        addEvent(()-> {
            this.setImmovable(false);
            this.didAirAttack = true;
        }, 365);
        addEvent(()-> {
            this.setCastDespair(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
        }, 370);
    };

    private final Consumer<EntityLivingBase> spear_incarnate = (target) -> {
      this.setSpearUltimate(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      addEvent(()-> this.lockLook = true, 20);
      addEvent(()-> {
          //do spear incarnate Attack
      }, 35);
        addEvent(()-> this.lockLook = false, 50);
      addEvent(()-> {
        this.setSpearUltimate(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
      }, 60);
    };

      private final Consumer<EntityLivingBase> grab_attack = (target) -> {
       this.setGrabAttack(true);
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
            this.setGrabAttack(false);
            this.grabDetection = false;
            this.setImmovable(true);

            if(grabbedEntity != null) {
                this.setGrabTooContinue(target);
            } else {
                this.setGrabFinish(true);

                addEvent(()-> {
                    this.lockLook = false;
                }, 15);

                addEvent(()-> {
                    this.setFightMode(false);
                    this.setFullBodyUsage(false);
                    this.setImmovable(false);
                    this.setGrabFinish(false);
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

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (this.getAttack() * 1.5F + demon_buff_damage);
            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0f, 0, false);
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
      this.setSummonMeteor(true);
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


      addEvent(()-> {
          this.setImmovable(false);
          this.didAirAttack = true;
          this.lockLook = true;
      }, 120);

      addEvent(()-> {
        this.setSummonMeteor(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
      }, 125);
    };

    private final Consumer<EntityLivingBase> cast_round_sigil = (target) -> {
      this.setCastRoundSigil(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);

        addEvent(()-> this.lockLook = true, 14);

        addEvent(()-> {
            EntityDemonSigil sigil = new EntityDemonSigil(world, this.rotationYaw, true, this.getAttack() * 0.75F);
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
            sigil.setPosition(relPos.x, relPos.y, relPos.z);
            world.spawnEntity(sigil);
        }, 23);

        addEvent(()-> this.lockLook = false, 35);

      addEvent(()-> {
          this.setCastRoundSigil(false);
          this.setFullBodyUsage(false);
          this.setFightMode(false);
          this.setImmovable(false);
      },45);
    };

    private final Consumer<EntityLivingBase> cast_sigil = (target) -> {
      this.setCastSigil(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);

      addEvent(()-> this.lockLook = true, 14);

      addEvent(()-> {
        EntityDemonSigil sigil = new EntityDemonSigil(world, this.rotationYaw, false, this.getAttack() * 0.75F);
        Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
        sigil.setPosition(relPos.x, relPos.y, relPos.z);
        world.spawnEntity(sigil);
        if(this.getHealth() / this.getMaxHealth() <= 0.75) {
            addEvent(() -> {
                EntityDemonSigil sigil2 = new EntityDemonSigil(world, this.rotationYaw, false, this.getAttack() * 0.75F);
                EntityDemonSigil sigil3 = new EntityDemonSigil(world, this.rotationYaw, false, this.getAttack() * 0.75F);
                Vec3d relPos2 = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 5)));
                Vec3d relPos3 = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, -5)));
                sigil2.setPosition(relPos2.x, relPos2.y, relPos2.z);
                sigil3.setPosition(relPos3.x, relPos3.y, relPos3.z);
                world.spawnEntity(sigil2);
                world.spawnEntity(sigil3);
            }, 5);
        }
      }, 17);

      addEvent(()-> this.lockLook = false, 20);
      addEvent(()-> {
        this.setCastSigil(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 30);
    };

    private final Consumer<EntityLivingBase> great_death = (target) -> {
      this.setGreatDeath(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
      this.lockLook = true;

      addEvent(()-> {
          double currentHealth = this.getHealth() / this.getMaxHealth();
          if(currentHealth <= 0.5 && rand.nextInt(3) == 0) {
              new ActionGreatDeathLine().performAction(this, target);
          } else {
              new ActionGreatDeath().performAction(this, target);
              if(currentHealth <= 0.75) {
                  addEvent(()-> {
                      new ActionGreatDeathTwo().performAction(this, target);
                  }, 40);
              }
          }
      }, 57);

      addEvent(()-> {
          this.lockLook = false;
      }, 80);
      addEvent(()-> {
        this.setGreatDeath(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 90);
    };

    private final Consumer<EntityLivingBase> melee_attack = (target) -> {
        this.setSwingMelee(true);
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.setImmovable(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 4);
        }, 17);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack() + demon_buff_damage);
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 27);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 38);

        addEvent(()-> {
            this.setSwingMelee(false);

            if(this.getDistance(target) <= 6 && rand.nextInt(5) != 0) {
                this.setMeleeSwingTooTwo(target);
            } else {
                this.setSwingMeleeFinish(true);

                addEvent(()-> {
                    this.setSwingMeleeFinish(false);
                    this.setFullBodyUsage(false);
                    this.setFightMode(false);
                    this.setImmovable(false);
                }, 15);
            }
        }, 40);
    };

    private void setMeleeSwingTooTwo(EntityLivingBase target) {
        melee_swing_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> melee_swing_continue = (target) -> {
      this.setSwingMeleeContinue(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 4);
        }, 14);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack() + demon_buff_damage);
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 23);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 32);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 4);
        }, 47);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (this.getAttack() + demon_buff_damage);
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 56);

        addEvent(()-> {
            this.setSwingMeleeContinue(false);
            this.setImmovable(true);
            this.lockLook = false;
            boolean randB = rand.nextBoolean();

            if(randB && this.getDistance(target) <= 12) {
                this.setSwingTooDash(target);
            } else {
                this.setSwingMeleeFinishTwo(true);

                addEvent(()-> {
                    this.setSwingMeleeFinishTwo(false);
                    this.setFullBodyUsage(false);
                    this.setFightMode(false);
                    this.setImmovable(false);
                }, 15);
            }
        }, 65);
    };

    private void setSwingTooDash(EntityLivingBase target) {
        melee_swing_dash.accept(target);
    }

    private final Consumer<EntityLivingBase> melee_swing_dash = (target) -> {
      this.setSwingMeleeDash(true);

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
            this.setSwingMeleeDash(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setImmovable(false);
      }, 70);
    };


    private final Consumer<EntityLivingBase> throw_daggers = (target) -> {
      this.setThrowDaggers(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);

      //daggers
      addEvent(()-> {
          new ActionThrowDaggers(false, false).performAction(this, target);
      }, 37);
        //daggers
        addEvent(()-> {
            new ActionThrowDaggers(false, true).performAction(this, target);
        }, 71);
        //daggers
        addEvent(()-> {
            new ActionThrowDaggers(false, false).performAction(this, target);
        }, 103);

        addEvent(()-> {
            this.setThrowDaggers(false);

            if(rand.nextInt(3) != 0 && this.getDistance(target) <= 14) {
                this.setDaggersTooDash(target);
            } else {
                this.setThrowDaggerContinue(true);

                //daggers
                addEvent(()-> {
                    new ActionThrowDaggers(false, true).performAction(this, target);
                }, 20);
                //daggers
                addEvent(()-> {
                    new ActionThrowDaggers(true, false).performAction(this, target);
                }, 60);

                addEvent(()-> {
                    this.setImmovable(false);
                    this.setThrowDaggerContinue(false);
                    this.setFightMode(false);
                    this.setFullBodyUsage(false);
                }, 85);
            }
        }, 115);
    };

    private void setDaggersTooDash(EntityLivingBase target){
        throw_daggers_dash.accept(target);
    };

    private final Consumer<EntityLivingBase> throw_daggers_dash = (target) -> {
      this.setThrowDaggersDash(true);

      addEvent(()-> {
          this.lockLook = true;
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.3),0.1F);
          }, 4);
      }, 39);

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
        }, 44);

      addEvent(()-> {
          this.setImmovable(true);
      }, 60);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (this.getAttack());
          ModUtils.handleAreaImpact(2.25f, (e) -> damage, this, offset, source, 0.8f, 0, false);
           this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 78);

      addEvent(()-> {
          this.lockLook = false;
          ModUtils.circleCallback(4, 5, (pos)-> {
              pos = new Vec3d(pos.x, 0, pos.y).add(this.getPositionVector());
              int y = ModUtils.getSurfaceHeightGeneral(world, new BlockPos(pos.x, 0, pos.z), (int) this.posY - 5, (int) this.posY + 3);
              EntityDelayedExplosion spike = new EntityDelayedExplosion(world, this, this.getAttack() * 0.8F, true, false);
              spike.setPosition(pos.x, y + 2, pos.z);
              world.spawnEntity(spike);
          });
      }, 85);

      addEvent(()-> {
          this.setImmovable(false);
          this.setThrowDaggersDash(false);
          this.setFightMode(false);
          this.setFullBodyUsage(false);
      }, 105);
    };

    private final Consumer<EntityLivingBase> stomp = (target) -> {
      this.setStomp(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.lockLook = true;

      addEvent(()-> {
          //stomp attack
          new ActionDemonStomp().performAction(this, target);
      }, 35);

      addEvent(()-> {
          this.lockLook = false;
      }, 55);

      addEvent(()-> {
        this.setStomp(false);

        if(this.getDistance(target) <= 5) {
            this.setStompTooContinue(target);
        } else {
            this.setStompFinish(true);

            addEvent(()-> {
                this.setStompFinish(false);
                this.setFullBodyUsage(false);
                this.setFightMode(false);
                this.setImmovable(false);
            }, 10);
        }
      }, 60);
    };

    private void setStompTooContinue(EntityLivingBase target) {
        stomp_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> stomp_continue = (target) -> {
      this.setStompContinue(true);
      this.lockLook = true;

        addEvent(()-> {
            for(int i = 0; i <= 18; i+= 3) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.0, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = (this.getAttack() + demon_buff_damage);
                    ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 0.8f, 0, false, MobEffects.WITHER, 0, 100);
                    // this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                }, i);
            }
        }, 25);

        addEvent(()-> {
            this.lockLook = false;
        }, 65);

      addEvent(()-> {
        this.setStompContinue(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      },    75);
    };

    private final Consumer<EntityLivingBase> long_swing = (target) -> {
      this.setLongSwing(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);

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

      //do hits
      addEvent(()-> {
        for(int i = 0; i <= 23; i+= 3) {
            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.0, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = (this.getAttack() + demon_buff_damage);
                ModUtils.handleAreaImpact(1.6f, (e) -> damage, this, offset, source, 0.4f, 0, false);
               // this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
            }, i);
        }
      }, 25);

      addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
      }, 45);

      addEvent(()-> {
        this.setLongSwing(false);
        boolean randB = rand.nextBoolean();
        if(randB) {
            //continue the attack
            this.setLongSwingTooContinue(target);
        } else {
            this.setLongSwingFinish(true);

            addEvent(()-> {
                this.setFullBodyUsage(false);
                this.setFightMode(false);
                this.setLongSwingFinish(false);
                this.setImmovable(false);
            }, 15);
        }
      }, 50);
    };

    private void setLongSwingTooContinue(EntityLivingBase target) {
        long_swing_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> long_swing_continue = (target) -> {
      this.setLongSwingContinue(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(4));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.33),0.1F);
            }, 4);
        }, 21);

        //do hits
        addEvent(()-> {
            for(int i = 0; i <= 23; i+= 3) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.0, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                    float damage = (this.getAttack() + demon_buff_damage);
                    ModUtils.handleAreaImpact(1.6f, (e) -> damage, this, offset, source, 0.4f, 0, false);
                    // this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                }, i);
            }
        }, 25);

        addEvent(()-> this.setImmovable(true), 45);

        addEvent(()-> this.lockLook = false, 60);

      addEvent(()-> {
          this.setLongSwingContinue(false);
          this.setFightMode(false);
          this.setFullBodyUsage(false);
          this.setImmovable(false);
      }, 70);
    };
    //Teleport Attack
    private final Consumer<EntityLivingBase> teleport_attack = (target) -> {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setTeleportAttack(true);
        this.setImmovable(true);

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
        }, 40);

        //do attack
        addEvent(()-> {
            this.setImmovable(true);
            new ActionTeleportAttack().performAction(this, target);
        }, 55);

        addEvent(()-> this.lockLook = false, 70);


        addEvent(()-> {
            this.setTeleportAttack(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);

        }, 80);

    };

    @Override
    public void registerControllers(AnimationData data) {
        //idle stuff
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateIdleArms));
        data.addAnimationController(new AnimationController(this, "arms_legs_controller", 0, this::predicateLegsUpper));
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "eyes_controller", 0, this::predicateEyes));
        data.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateFight));
        data.addAnimationController(new AnimationController(this, "predicate_states", 0, this::predicateStates));
        //attack controllers
    }

    private <E extends IAnimatable> PlayState predicateStates(AnimationEvent<E> event) {
        if(this.isEnterMeditation()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_ENTER_MEDITATION));
            return PlayState.CONTINUE;
        }
        if(this.isEndMeditation()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_EXIT_MEDITATION));
            return PlayState.CONTINUE;
        }
        if(this.isMeditationLoop()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MEDITATION_LOOP, true));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateFight(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isLongSwing()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_LONG_SWING));
                return PlayState.CONTINUE;
            }
            if(this.isLongSwingContinue()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_LONG_SWING_CONTINUE));
                return PlayState.CONTINUE;
            }
            if(this.isLongSwingFinish()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_LONG_SWING_FINISH));
                return PlayState.CONTINUE;
            }
            if(this.isTeleportAttack()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_TELEPORT_ATTACK));
                return PlayState.CONTINUE;
            }
            if(this.isCastSigil()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CAST_SIGIL));
                return PlayState.CONTINUE;
            }
            if(this.isCastRoundSigil()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CAST_SIGIL_ROUND));
                return PlayState.CONTINUE;
            }
            if(this.isSummonMeteor()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SUMMON_METEORS));
                return PlayState.CONTINUE;
            }
            if(this.isStomp()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_STOMP));
                return PlayState.CONTINUE;
            }
            if(this.isStompContinue()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_STOMP_CONTINUE));
                return PlayState.CONTINUE;
            }
            if(this.isStompFinish()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_STOMP_FINISH));
                return PlayState.CONTINUE;
            }
            if(this.isThrowDaggers()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_THROW_DAGGERS));
                return PlayState.CONTINUE;
            }
            if(this.isThrowDaggersDash()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_THROW_DAGGERS_DASH));
                return PlayState.CONTINUE;
            }
            if(this.isThrowDaggersContinue()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_THROW_DAGGERS_CONTINUE));
                return PlayState.CONTINUE;
            }
            if(this.isSwingMelee()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_MELEE_SWING));
                return PlayState.CONTINUE;
            }
            if(this.isSwingMeleeFinish()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_MELEE_SWING_FINISH));
                return PlayState.CONTINUE;
            }
            if(this.isSwingMeleeContinue()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_MELEE_SWING_CONTINUE));
                return PlayState.CONTINUE;
            }
            if(this.isSwingMeleeFinishTwo()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_MELEE_SWING_FINISH_TWO));
                return PlayState.CONTINUE;
            }
            if(this.isSwingMeleeDash()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_MELEE_SWING_DASH));
                return PlayState.CONTINUE;
            }
            if(this.isCastDespair()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_DESPAIR));
                return PlayState.CONTINUE;
            }
            if(this.isSpearUltimate()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SPEAR_INCARNATE));
                return PlayState.CONTINUE;
            }
            if(this.isGrabAttack()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_GRAB));
                return PlayState.CONTINUE;
            }
            if(this.isGrabContinue()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_GRAB_CONTINUE));
                return PlayState.CONTINUE;
            }
            if(this.isGrabFinish()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_GRAB_FINISH));
                return PlayState.CONTINUE;
            }
            if(this.isGreatDeath()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_GREAT_DEATH));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
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

        return pos.getY();
    }


    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if (event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage() && !this.isMeditationLoop()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdleArms(AnimationEvent<E> event) {
        if (event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode() && !this.isMeditationLoop()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if (!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage() && !this.isMeditationLoop()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegsUpper(AnimationEvent<E> event) {
        if (!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode() && !this.isMeditationLoop()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateEyes(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_EYES, true));
        return PlayState.CONTINUE;
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
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
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
}
