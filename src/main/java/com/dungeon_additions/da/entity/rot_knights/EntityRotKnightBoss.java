package com.dungeon_additions.da.entity.rot_knights;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.blocks.boss.BlockBossReSummon;
import com.dungeon_additions.da.blocks.boss.BlockEnumBossSummonState;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.EntityAIAttackRotKnight;
import com.dungeon_additions.da.entity.ai.EntityAIFallenAttack;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.ActionDauntlessImpact;
import com.dungeon_additions.da.entity.frost_dungeon.EntityAbstractGreatWyrk;
import com.dungeon_additions.da.entity.rot_knights.actions.*;
import com.dungeon_additions.da.entity.tileEntity.TileEntityBossReSummon;
import com.dungeon_additions.da.entity.util.IEntitySound;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.util.*;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.dungeon_additions.da.world.ModStructureTemplate;
import com.sun.jna.platform.win32.WinBase;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
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

public class EntityRotKnightBoss extends EntityAbstractBase implements IAnimatable, IAttack, IAnimationTickable, IEntitySound {
    private Consumer<EntityLivingBase> prevAttack;
    public boolean isRandomGetAway = false;
    public boolean currentlyJumping = false;
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.NOTCHED_6));
    protected boolean hasDonePhaseTransition = false;
    private int blockTimer = 80;
    private boolean grabDetection = false;
    private boolean setBossToFlyHigh = false;
    private EntityLivingBase grabbedEntity;
    private final String ANIM_WALK = "walk";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_IDLE = "idle";
    private final String ANIM_HIDE_ARMS = "hide_arm";
    private final String ANIM_PHASE_TRANSITION = "phase";
    private final String ANIM_SUMMON = "summon";
    private final String ANIM_DEATH = "death";


    //remove

    //implemented
    private final String ANIM_SWING = "swing";
    private final String ANIM_COMBO_SWING = "combo_swing";
    private final String ANIM_COMBO_SWING_ARMS = "combo_swing_arm";
    private final String ANIM_AURA = "aura";
    //add
    private final String ANIM_PIERCE_BEGIN = "pierce_begin";
    private final String ANIM_PIERCE_CONT = "pierce_continue";
    private final String ANIM_PIERCE_FINISH = "pierce_finish";
    private final String ANIM_SWING_JUMP = "swing_jump";
    private final String ANIM_CAST_CLOUD = "cast_cloud";
    private final String ANIM_CAST_LINE_SWORD = "cast_line_sword";
    private final String ANIM_SLAM_ORB = "slam_orb";
    private final String ANIM_PARRY = "parry";
    private final String ANIM_ROTTEN_AMBITION = "rotten_ambition";

    private final AnimationFactory factory = new AnimationFactory(this);
    private static final DataParameter<Boolean> PHASE_TRANSITION = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_BEGIN = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_CONTINUE = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_FINISH = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_SWING = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_SWING_ARMS = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> HIDE_ARM_STATE = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> AURA = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> CAST_LINE = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> SWING_JUMP = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> CAST_CLOUD = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> SLAM_ORB = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> PARRY = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ROTTEN_AMBITION = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEATH_STATE = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> HAD_PREVIOUS_TARGET = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);

    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BLOCK_POS);
    public static DataParameter<Boolean> SET_SPAWN_LOC = EntityDataManager.createKey(EntityRotKnightBoss.class, DataSerializers.BOOLEAN);
    private boolean isPhaseTransition() {return this.dataManager.get(PHASE_TRANSITION);}
    private boolean isSwingJump() {return this.dataManager.get(SWING_JUMP);}
    public boolean isRottenAmbition() {return this.dataManager.get(ROTTEN_AMBITION);}
    public boolean isDeathState() {return this.dataManager.get(DEATH_STATE);}
    private boolean isCastCloud() {return this.dataManager.get(CAST_CLOUD);}
    private boolean isSlamOrb() {return this.dataManager.get(SLAM_ORB);}
    private boolean isParry() {return this.dataManager.get(PARRY);}
    private boolean isPierceBegin() {return this.dataManager.get(PIERCE_BEGIN);}
    private boolean isSwing() {return this.dataManager.get(SWING);}
    private boolean isPierceContinue() {return this.dataManager.get(PIERCE_CONTINUE);}
    private boolean isPierceFinish() {return this.dataManager.get(PIERCE_FINISH);}
    private boolean isComboSwing() {return this.dataManager.get(COMBO_SWING);}
    private boolean isComboSwingArms() {return this.dataManager.get(COMBO_SWING_ARMS);}
    public boolean isHideArms() {return this.dataManager.get(HIDE_ARM_STATE);}
    public boolean isAura() {return this.dataManager.get(AURA);}
    public boolean isCastLine() {return this.dataManager.get(CAST_LINE);}
    private boolean isSummon() {return this.dataManager.get(SUMMON);}
    private void setPhaseTransition(boolean value) {this.dataManager.set(PHASE_TRANSITION, Boolean.valueOf(value));}
    private void setSwingJump(boolean value) {this.dataManager.set(SWING_JUMP, Boolean.valueOf(value));}
    private void setRottenAmbition(boolean value) {this.dataManager.set(ROTTEN_AMBITION, Boolean.valueOf(value));}
    private void setDeathState(boolean value) {this.dataManager.set(DEATH_STATE, Boolean.valueOf(value));}
    private void setCastCloud(boolean value) {this.dataManager.set(CAST_CLOUD, Boolean.valueOf(value));}
    private void setSlamOrb(boolean value) {this.dataManager.set(SLAM_ORB, Boolean.valueOf(value));}
    private void setParry(boolean value) {this.dataManager.set(PARRY, Boolean.valueOf(value));}
    private void setPierceBegin(boolean value) {this.dataManager.set(PIERCE_BEGIN, Boolean.valueOf(value));}
    private void setPierceContinue(boolean value) {this.dataManager.set(PIERCE_CONTINUE, Boolean.valueOf(value));}
    private void setPierceFinish(boolean value) {this.dataManager.set(PIERCE_FINISH, Boolean.valueOf(value));}
    private void setSwing(boolean value) {this.dataManager.set(SWING, Boolean.valueOf(value));}
    private void setComboSwing(boolean value) {this.dataManager.set(COMBO_SWING, Boolean.valueOf(value));}
    private void setComboSwingArms(boolean value) {this.dataManager.set(COMBO_SWING_ARMS, Boolean.valueOf(value));}
    private void setHideArmState(boolean value) {this.dataManager.set(HIDE_ARM_STATE, Boolean.valueOf(value));}
    private void setAura(boolean value) {this.dataManager.set(AURA, Boolean.valueOf(value));}
    private void setCastLine(boolean value) {this.dataManager.set(CAST_LINE, Boolean.valueOf(value));}
    private void setSummon(boolean value) {this.dataManager.set(SUMMON, Boolean.valueOf(value));}

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
    public EntityRotKnightBoss(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.75F, 1.95F);
        this.setHideArmState(true);
        this.iAmBossMob = true;
        BlockPos offset = new BlockPos(x, y, z);
        this.setSpawnLocation(offset);
        this.setSetSpawnLoc(true);
        this.onSummonBoss();
        this.falter_resistance = 1.2F;
        this.hemorrhage_resistance = 0.8F;
    }

    public EntityRotKnightBoss(World worldIn) {
        super(worldIn);
        this.setSize(0.75F, 1.95F);
        this.setHideArmState(true);
        this.iAmBossMob = true;
        this.onSummonBoss();
        this.falter_resistance = 1.2F;
        this.hemorrhage_resistance = 0.8F;
    }

    public EntityRotKnightBoss(World world, int timesUsed, BlockPos pos) {
        super(world);
        this.timesUsed = timesUsed;
        this.setSize(0.75F, 1.95F);
        this.setHideArmState(true);
        this.iAmBossMob = true;
        this.timesUsed++;
        this.doBossReSummonScaling();
        this.onSummonBoss();
        BlockPos offset = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        this.setSpawnLocation(offset);
        this.setSetSpawnLoc(true);
        this.falter_resistance = 1.2F;
        this.hemorrhage_resistance = 0.8F;
    }

    public void onSummonBoss() {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setSummon(true);
        this.setImmovable(true);
        addEvent(()-> {
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setSummon(false);
        this.setImmovable(false);
        }, 125);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Phase", this.isPhaseTransition());
        nbt.setBoolean("Swing_Jump", this.isSwingJump());
        nbt.setBoolean("Death_State", this.isDeathState());
        nbt.setBoolean("Rotten_Ambition", this.isRottenAmbition());
        nbt.setBoolean("Cast_Cloud", this.isCastCloud());
        nbt.setBoolean("Slam_Orb", this.isSlamOrb());
        nbt.setBoolean("Parry", this.isParry());
        nbt.setBoolean("Pierce_Begin", this.isPierceBegin());
        nbt.setBoolean("Pierce_Continue", this.isPierceContinue());
        nbt.setBoolean("Pierce_Finish", this.isPierceFinish());
        nbt.setBoolean("Swing", this.isSwing());
        nbt.setBoolean("Combo_Swing", this.isComboSwing());
        nbt.setBoolean("Combo_Swing_Arms", this.isComboSwingArms());
        nbt.setBoolean("Hide_Arms", this.isHideArms());
        nbt.setBoolean("Aura", this.isAura());
        nbt.setBoolean("Cast_Line", this.isCastLine());
        nbt.setBoolean("Summon", this.isSummon());
        nbt.setBoolean("Had_Target", this.dataManager.get(HAD_PREVIOUS_TARGET));
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
        nbt.setBoolean("Set_Spawn_Loc", this.dataManager.get(SET_SPAWN_LOC));
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setPhaseTransition(nbt.getBoolean("Phase"));
        this.setSwingJump(nbt.getBoolean("Swing_Jump"));
        this.setDeathState(nbt.getBoolean("Death_State"));
        this.setRottenAmbition(nbt.getBoolean("Rotten_Ambition"));
        this.setCastCloud(nbt.getBoolean("Cast_Cloud"));
        this.setSlamOrb(nbt.getBoolean("Slam_Orb"));
        this.setParry(nbt.getBoolean("Parry"));
        this.setPierceBegin(nbt.getBoolean("Pierce_Begin"));
        this.setPierceContinue(nbt.getBoolean("Pierce_Continue"));
        this.setPierceFinish(nbt.getBoolean("Pierce_Finish"));
        this.setSwing(nbt.getBoolean("Swing"));
        this.setComboSwing(nbt.getBoolean("Combo_Swing"));
        this.setComboSwingArms(nbt.getBoolean("Combo_Swing_Arms"));
        this.setHideArmState(nbt.getBoolean("Hide_Arms"));
        this.setAura(nbt.getBoolean("Aura"));
        this.setCastLine(nbt.getBoolean("Cast_Line"));
        this.setSummon(nbt.getBoolean("Summon"));
        this.setHadPreviousTarget(nbt.getBoolean("Had_Target"));
        this.dataManager.set(SET_SPAWN_LOC, nbt.getBoolean("Set_Spawn_Loc"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(PHASE_TRANSITION, Boolean.valueOf(false));
        this.dataManager.register(SWING_JUMP, Boolean.valueOf(false));
        this.dataManager.register(DEATH_STATE, Boolean.valueOf(false));
        this.dataManager.register(ROTTEN_AMBITION, Boolean.valueOf(false));
        this.dataManager.register(CAST_CLOUD, Boolean.valueOf(false));
        this.dataManager.register(SLAM_ORB, Boolean.valueOf(false));
        this.dataManager.register(PARRY, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_BEGIN, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_FINISH, Boolean.valueOf(false));
        this.dataManager.register(SWING, Boolean.valueOf(false));
        this.dataManager.register(COMBO_SWING, Boolean.valueOf(false));
        this.dataManager.register(COMBO_SWING_ARMS, Boolean.valueOf(false));
        this.dataManager.register(HIDE_ARM_STATE, Boolean.valueOf(false));
        this.dataManager.register(CAST_LINE, Boolean.valueOf(false));
        this.dataManager.register(AURA, Boolean.valueOf(false));
        this.dataManager.register(SUMMON, Boolean.valueOf(false));
        this.dataManager.register(HAD_PREVIOUS_TARGET, Boolean.valueOf(false));
        this.dataManager.register(SET_SPAWN_LOC, Boolean.valueOf(false));
        //
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.fallen_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.fallen_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.fallen_armor);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    private int HoverTimeIncrease = 0;
    private boolean hasHoverMovement = false;
    private boolean shownUltimate = false;
    private int ultimateCooldown = 600;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.blockTimer--;
        this.ultimateCooldown--;

        this.bossInfo.setPercent(getHealth() / getMaxHealth());
        if(world.isRemote && ticksExisted == 1 && ModConfig.experimental_features && MobConfig.fallen_stormvier_boss_music) {
            this.playMusic(this);
        }

        if(ticksExisted % 5 == 0) {
            if(this.isPotionActive(MobEffects.POISON)) {
                this.removePotionEffect(MobEffects.POISON);
            }
        }

        EntityLivingBase target = this.getAttackTarget();
        double healthCurrent = this.getHealth() / this.getMaxHealth();
        if(target != null) {
            if(!hasDonePhaseTransition && this.isHideArms()) {

                if (healthCurrent <= 0.7 && !this.isFightMode()) {
                    doPhaseTransition();
                }
            }


            //hovering movement
            if (HoverTimeIncrease > 0) {
                this.motionY = 0.25;
                HoverTimeIncrease--;
            }

            if (this.hasHoverMovement) {
                    double d0 = (target.posX - this.posX) * 0.018;
                    double d2 = (target.posZ - this.posZ) * 0.018;
                    this.addVelocity(d0, 0, d2);
                    this.faceEntity(target, 30F, 30F);
            }


            if(grabDetection && grabbedEntity == null) {
                List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                        this.getEntityBoundingBox().offset(ModUtils.getRelativeOffset(this, new Vec3d(1.2, -0.5, 0))).grow(0.7D, 3.5D, 0.7D),
                        e -> !e.getIsInvulnerable());

                if(!nearbyEntities.isEmpty()) {
                    for(EntityLivingBase base : nearbyEntities) {
                        if((base != this) && !(base instanceof EntityRotSpike) && !(base instanceof EntityRotKnight) && !(base instanceof EntityRotKnightRapier)) {
                            grabbedEntity = base;
                            this.playSound(SoundsHandler.KING_GRAB_SUCCESS, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                        }
                    }
                }
            } else if (grabbedEntity != null) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.2, 0)));
                    grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                    grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);
            }

            if(this.isRandomGetAway) {
                double d0 = (this.posX - target.posX) * 0.03;
                double d1 = (this.posY - target.posY) * 0.005;
                double d2 = (this.posZ - target.posZ) * 0.03;
                this.addVelocity(d0, d1, d2);
                this.faceEntity(target, 35, 35);
                this.getLookHelper().setLookPositionWithEntity(target, 35, 35);
            }

        }

        if (hasDonePhaseTransition || !this.isHideArms()) {
            if(target == null && healthCurrent >= 0.95) {
                this.setHideArmState(true);
                this.hasDonePhaseTransition = false;
            }
        }

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

            //Spawn Telporting Location
            //This is too keep the boss at it's starting location and keep it from getting too far away

            if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
                Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());

                double distSq = this.getDistanceSq(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                double distance = Math.sqrt(distSq);
                //This basically makes it so the Wyrk will be teleported if they are too far away from the Arena
                if(!world.isRemote) {
                    if (distance > 25) {
                        this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
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
                    boss_spawner.setState(BlockEnumBossSummonState.INACTIVE, this.timesUsed, "ancient_fallen");
                }
            }
        }
    }

    private static final ResourceLocation LOO_RESET = new ResourceLocation(ModReference.MOD_ID, "rot_fort_reset");

    private void resetBossTask() {
        this.setImmovable(true);
        this.setHadPreviousTarget(false);
        BlockPos pos = this.getSpawnLocation();
        world.setBlockState(pos, ModBlocks.KNIGHT_KEY_BLOCK.getDefaultState());
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

    private void doPhaseTransition() {
        this.setPhaseTransition(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);

        //hide arms somewhere in here set to false

        addEvent(()-> this.setHideArmState(false), 54);

        addEvent(()-> this.playSound(SoundsHandler.ROT_KNIGHT_ARM_BREAK, 1.5f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 55);

        addEvent(()-> {
            this.setImmovable(false);
            this.hasDonePhaseTransition = true;
            this.setPhaseTransition(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
        }, 80);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIFallenAttack<>(this, 1.15, MobConfig.fallen_cool_down, 12, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double healthF = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && !this.isPhaseTransition() && !this.isRandomGetAway && !this.isSummon() && !this.isDeathState()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(swing, pierce, combo_swing, aura, cast_line, swing_jump, slam_orb, cast_arm, rotten_ambition));
            double[] weights = {
                    (distance <= 6 && prevAttack != swing) ? 1/distance : 0, //basic swing
                    (distance <= 13 && distance >= 3) ? 1/distance : 0, // pierce has altercations at 2nd phase
                    (distance <= 8 && prevAttack != combo_swing) ? 1/distance : 0, //combo swing has altercations at 2nd phase
                    (distance <= 4 && prevAttack != aura) ? 1/distance : 0, //Aura Attack
                    (distance <= 13 && prevAttack != cast_line && distance > 3 && hasDonePhaseTransition) ? 1/distance : 0, //Cast Line Only if phaseTransition has been cast
                    (distance <= 13 && prevAttack != swing_jump) ? 1/distance : 0, //Swing and Jump away
                    (distance <= 13 && hasDonePhaseTransition && prevAttack != slam_orb && distance > 2) ? 1/distance : 0, //Slam Orb Attack
                    (distance <= 13 && hasDonePhaseTransition && prevAttack != cast_arm && distance > 2) ? 1/distance : 0, //Cast Arm Attack
                    (healthF <= 0.5 && !shownUltimate) ? 100 : (healthF <= 0.5 && prevAttack != rotten_ambition && ultimateCooldown < 0) ? 2/distance : 0 // Rotten Ambition Ultimate
            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        return (hasDonePhaseTransition) ? MobConfig.fallen_cool_down - (MobConfig.fallen_cool_down/2) : MobConfig.fallen_cool_down;
    }

    private final Consumer<EntityLivingBase> rotten_ambition = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setRottenAmbition(true);
      this.setImmovable(true);
      this.lockLook = true;
      this.shownUltimate = true;

      addEvent(()-> {
          this.setImmovable(false);
            this.HoverTimeIncrease = 2;
            this.setNoGravity(true);
          this.playSound(SoundsHandler.COLOSSUS_HILT_SLAM, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            addEvent(()-> {
                this.motionY = 0;
                this.setImmovable(true);
            }, 15);
      }, 5);

      addEvent(()-> new ActionSpreadCloud().performAction(this, target), 40);

      addEvent(()-> {
          this.setImmovable(false);
            this.setNoGravity(false);
      }, 147);

      addEvent(()-> {
          int y_value = ModUtils.getSurfaceHeightGeneral(world, new BlockPos(this.posX, 0, this.posZ), (int) this.posY - 20, (int) this.posY + 2);
          if(y_value != this.posY) {
              this.setPosition(this.posX, y_value + 1, this.posZ);
          } else {
              //destroy blocks just incase
              this.setPosition(this.posX, target.posY, this.posZ);
          }
          this.fallDistance = 0;
          this.setImmovable(true);
          this.playSound(SoundsHandler.DAUNTLESS_IMPACT, 1.4f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            new ActionDauntlessImpact(2).performAction(this, target);
            new ActionRotProgressive((int) this.getDistance(target) + 3).performAction(this, target);
      }, 155);

      addEvent(()-> this.lockLook = false, 190);
      addEvent(()-> {
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setRottenAmbition(false);
            this.setImmovable(false);
            this.ultimateCooldown = 600;
      }, 200);
    };
    private final Consumer<EntityLivingBase> cast_arm = (target) -> {
      this.setCastCloud(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.setFightMode(true);

      addEvent(()-> {
          this.lockLook = true;
            new ActionRotCloud().performAction(this, target);
      }, 20);

      addEvent(()-> this.lockLook = false, 55);

      addEvent(()-> {
        this.setCastCloud(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 60);
    };

    private final Consumer<EntityLivingBase> slam_orb = (target) -> {
      this.setSlamOrb(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);

      addEvent(()-> {
          this.setImmovable(false);
          this.setNoGravity(true);
          this.HoverTimeIncrease = 2;
         this.playSound(SoundsHandler.COLOSSUS_HILT_SLAM, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 18);

      addEvent(()-> {
          this.hasHoverMovement = true;
          addEvent(()-> {
              this.motionY = 0;
          }, 5);
      }, 24);

      addEvent(()-> {
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
          this.setNoGravity(false);
          this.lockLook = true;
      }, 60);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 1.2f, 0, false, 0.6F);
          this.playSound(SoundsHandler.ROT_SELF_AOE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0, 0)));
          Main.proxy.spawnParticle(19,world, relPos.x, this.posY + 0.1, relPos.z, 0, 0, 0);
          new ActionRotContinousAOE().performAction(this, target);
      }, 65);

      addEvent(()-> {
          this.lockLook = false;
      }, 95);

      addEvent(()-> {
        this.setSlamOrb(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 100);
    };

    private final Consumer<EntityLivingBase> cast_line = (target) -> {
      this.setCastLine(true);
      this.setFightMode(true);

      addEvent(()-> {
          this.setImmovable(true);
      }, 15);

      addEvent(()-> this.playSound(SoundsHandler.ROT_KNIGHT_CAST, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 15);
      addEvent(()-> world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE), 20);
      addEvent(()-> {
          new ActionRotLineAOE().performAction(this, target);
          this.lockLook = true;
      }, 27);

      addEvent(()-> {
            this.setImmovable(false);
            this.lockLook = false;
      }, 45);
      addEvent(()-> {
        this.setCastLine(false);
        this.setFightMode(false);
      }, 30);
    };

    private boolean setAuraParticle = false;
    private final Consumer<EntityLivingBase> aura = (target) -> {
      this.setFightMode(true);
      this.setImmovable(true);
      this.setFullBodyUsage(true);
      this.setAura(true);
      this.lockLook = true;

      addEvent(()-> setAuraParticle = true, 5);
      addEvent(()-> setAuraParticle = false, 17);
        addEvent(()-> {
            if(this.hasDonePhaseTransition) {
                // DO AOE Outer Ring
                new ActionRotFarAOE().performAction(this, target);
            }
        }, 20);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(3.0f, (e) -> damage, this, offset, source, 0.8f, 0, false, MobEffects.POISON, 0, 200, 0.6F);
          this.playSound(SoundsHandler.ROT_SELF_AOE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
      }, 23);

      addEvent(()-> {
          this.lockLook =false;
          this.setFightMode(false);
          this.setImmovable(false);
          this.setFullBodyUsage(false);
          this.setAura(false);
      }, 45);

    };

    private final Consumer<EntityLivingBase> swing_jump = (target) -> {
        this.setFullBodyUsage(true);
        this.setSwingJump(true);
        this.setFightMode(true);
        this.setImmovable(true);

        addEvent(()-> {
            Vec3d targetedPos = target.getPositionVector();
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.20F);
            }, 8);
        }, 5);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 1.4, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.2F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 21);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 30);

        addEvent(()-> {
        //do jump back
            this.lockLook = true;
            this.currentlyJumping = true;
            this.getNavigator().clearPath();
            Vec3d dirToo = this.getPositionVector().subtract(target.getPositionVector()).normalize();
            Vec3d jumpTooPos = this.getPositionVector().add(dirToo.scale(144));
            this.setImmovable(false);
            ModUtils.leapTowards(this, jumpTooPos, 1.9F, 0.25F);
            this.playSound(SoundsHandler.APATHYR_SLIGHT_DASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 46);

        addEvent(()-> {
            this.lockLook = false;
        }, 72);

        addEvent(()-> {
            this.currentlyJumping = false;
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setSwingJump(false);
        }, 75);
    };

    private final Consumer<EntityLivingBase> swing = (target) -> {
        this.setFightMode(true);
        this.setSwing(true);

        addEvent(()-> this.lockLook = true, 10);

        addEvent(()-> {
            Vec3d targetedPos = target.getPositionVector();
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.20F);
            }, 5);
        }, 6);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 1.4, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.1F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 17);

        addEvent(()-> this.lockLook = false, 25);

        addEvent(()-> {
            this.setSwing(false);
            this.setFightMode(false);
        }, 30);
    };

    private boolean wasLastCombo = false;
    private final Consumer<EntityLivingBase> combo_swing = (target) -> {
      int randAttackDeter = ModRand.range(1, 10);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
        this.setImmovable(true);
      if(this.hasDonePhaseTransition && !wasLastCombo) {
          this.setComboSwingArms(true);


          addEvent(()-> {
              Vec3d targetedPos = target.getPositionVector();
              this.lockLook = true;
              addEvent(()-> {
                  this.setImmovable(false);
                  double distance = this.getPositionVector().distanceTo(targetedPos);
                  ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.20F);
              }, 8);
          }, 5);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.6, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
              float damage = this.getAttack();
              ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.2F);
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          }, 16);

          addEvent(()-> {this.lockLook = false;
              this.setImmovable(true);}, 20);

          addEvent(()-> {
              Vec3d targetedPos = target.getPositionVector();
              this.lockLook = true;
              addEvent(()-> {
                  this.setImmovable(false);
                  double distance = this.getPositionVector().distanceTo(targetedPos);
                  ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.20F);
              }, 8);
          }, 30);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 0.5, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
              float damage = this.getAttack();
              ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.5F);
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          }, 43);

          addEvent(()-> {
              this.lockLook = false;
              this.setImmovable(true);
          }, 49);

          addEvent(()-> {
              Vec3d targetedPos = target.getPositionVector();
              this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.5f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
              addEvent(()-> {
                  this.setImmovable(false);
                  double distance = this.getPositionVector().distanceTo(targetedPos);
                  ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.20F);
              }, 8);
          }, 50);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 0.5, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
              float damage = (float) (this.getAttack() * 1.3);
              ModUtils.handleAreaImpact(1.2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.5F);
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          }, 64);

          addEvent(()-> {
            this.setImmovable(true);
          }, 70);

          addEvent(()-> {
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setComboSwingArms(false);
            this.wasLastCombo = true;
          }, 85);
      } else {
          this.setComboSwing(true);

          addEvent(()-> {
            Vec3d targetedPos = target.getPositionVector();
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.20F);
            }, 8);
          }, 5);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.6, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
              float damage = this.getAttack();
              ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.2F);
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          }, 16);

          addEvent(()-> {this.lockLook = false;
              this.setImmovable(true);}, 20);

          addEvent(()-> {
              Vec3d targetedPos = target.getPositionVector();
              this.lockLook = true;
              addEvent(()-> {
                  this.setImmovable(false);
                  double distance = this.getPositionVector().distanceTo(targetedPos);
                  ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.20),0.20F);
              }, 8);
          }, 30);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 0.5, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
              float damage = this.getAttack();
              ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.5F);
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          }, 43);

          addEvent(()-> {
              this.lockLook = false;
              this.setImmovable(true);
          }, 49);
          addEvent(()-> {
              this.setImmovable(false);
              this.setComboSwing(false);
              this.setFightMode(false);
              this.setFullBodyUsage(false);
              this.wasLastCombo = false;
          }, 60);
      }
    };

    private final Consumer<EntityLivingBase> pierce = (target) -> {
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.setPierceBegin(true);
        this.setImmovable(true);
        //27
        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(4));
            this.lockLook = true;
            this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
            addEvent(()-> {
                this.playSound(SoundsHandler.APATHYR_SLIGHT_DASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                this.setImmovable(false);
                this.grabDetection = true;
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.2),0.05F);
            }, 5);
        }, 22);

        addEvent(()-> {
            this.grabDetection = false;
            this.setImmovable(true);
            this.setPierceBegin(false);
            //continue
            if(grabbedEntity != null) {
                this.setPierceTooContinue(grabbedEntity);
            } else {
                this.setPierceFinish(true);

                addEvent(()-> {
                    this.lockLook = false;
                }, 10);
                addEvent(()-> {
                    this.setPierceFinish(false);
                    this.setImmovable(false);
                    this.setFullBodyUsage(false);
                    this.setFightMode(false);
                }, 20);
            }
        }, 50);
    };

    private void setPierceTooContinue(EntityLivingBase target) {
        this.pierce_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> pierce_continue = (target) -> {
        this.setPierceContinue(true);
        this.setAuraParticle = true;
        //do damage and effects
        addEvent(()-> {
            this.setAuraParticle = false;
            this.grabbedEntity = null;
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (this.getAttack());
            ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.6f, 0, false, MobEffects.POISON, 0, 300, 0.9F);
        }, 48);

        addEvent(()-> {
            this.lockLook = false;
        }, 60);

        addEvent(()-> {
            this.setPierceContinue(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
        }, 70);
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "phase_controller", 0, this::predicatePhaseChange));
        data.addAnimationController(new AnimationController(this, "right_arm_controller", 0, this::predicateRightArm));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }


    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if (!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateRightArm(AnimationEvent<E> event) {
        if(this.isHideArms()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HIDE_ARMS, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isPhaseTransition() && !this.isDeathState()) {
            if(this.isPierceBegin()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PIERCE_BEGIN));
            }
            if(this.isPierceContinue()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PIERCE_CONT));
            }
            if(this.isPierceFinish()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PIERCE_FINISH));
            }
            if(this.isComboSwing()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_COMBO_SWING));
            }
            if(this.isComboSwingArms()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_COMBO_SWING_ARMS));
            }
            if(this.isSwing()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING));
            }
            if(this.isCastLine()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CAST_LINE_SWORD));
            }
            if(this.isAura()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_AURA));
            }
            if(this.isSwingJump()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_JUMP));
            }
            if(this.isCastCloud()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CAST_CLOUD));
            }
            if(this.isSlamOrb()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SLAM_ORB));
            }
            if(this.isParry()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PARRY));
            }
            if(this.isRottenAmbition()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_ROTTEN_AMBITION));
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicatePhaseChange(AnimationEvent<E> event) {
        if(this.isPhaseTransition()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PHASE_TRANSITION));
            return PlayState.CONTINUE;
        }
        if(this.isSummon()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SUMMON));
            return PlayState.CONTINUE;
        }
        if(this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_DEATH));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "rot_knight_boss");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
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
    public void tick() {

    }

    @Override
    public void handleStatusUpdate(byte id) {
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 35, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.0, 0))), ModColors.GREEN, new Vec3d(0,0,0));
            });

            ModUtils.circleCallback(2, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0))), ModColors.GREEN, new Vec3d(0,0,0));
            });

            ModUtils.circleCallback(2, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0))), ModColors.GREEN, new Vec3d(0,0,0));
            });

        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 35, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.0, 0))), ModColors.GREEN, pos.normalize().scale(0.25).add(ModUtils.yVec(0)));
            });
            ModUtils.circleCallback(2, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0))), ModColors.GREEN, pos.normalize().scale(0.25).add(ModUtils.yVec(0)));
            });
            ModUtils.circleCallback(2, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0))), ModColors.GREEN, pos.normalize().scale(0.25).add(ModUtils.yVec(0)));
            });
        }

        super.handleStatusUpdate(id);
    }

    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(this.setAuraParticle) {
            if(rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            }
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.ROT_KNIGHT_WALK, 0.7F, 0.4f + ModRand.getFloat(0.3F));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.ROT_KNIGHT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ROT_KNIGHT_DEATH;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    private boolean currentlyBlocking = false;

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {


        if(this.isPhaseTransition() || this.isSummon() || this.isPierceContinue() || this.currentlyJumping) {
            return false;
        }
        if(source.getImmediateSource() instanceof EntityRotSpike) {
            return false;
        }

        if(amount > 0.0F && this.canBlockDamageSource(source) && !this.isFightMode()) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }
            this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.7f, 1.1f + ModRand.getFloat(0.2f));

            return false;
        } else if (this.currentlyBlocking) {
            this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.7f, 1.1f + ModRand.getFloat(0.2f));
            return false;
        }

        if(ModConfig.boss_cap_damage_enabled && amount > MobConfig.fallen_damage_cap) {
            return super.attackEntityFrom(source, MobConfig.fallen_damage_cap);
        }

        return super.attackEntityFrom(source, amount);
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if (!damageSourceIn.isUnblockable() && this.blockTimer < 0 && !this.isParry() && !this.isFightMode() && !this.isPhaseTransition() && !this.isSummon()) {
            Vec3d vec3d = damageSourceIn.getDamageLocation();
            //Handler for other
            if (vec3d != null) {
                Vec3d vec3d1 = this.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);
                this.doBlockAction();
                return vec3d2.dotProduct(vec3d1) < 0.5D;
            }
        }

        return false;
    }

    private void doBlockAction() {
        this.setParry(true);
        this.setFightMode(true);
        this.blockTimer = 60;
        this.isRandomGetAway = true;
        this.currentlyBlocking = true;

        addEvent(()-> {
            this.currentlyBlocking = false;
            this.setParry(false);
            this.setFightMode(false);
            this.isRandomGetAway = false;
        }, 20);
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    private void destroyBlocksInSwing(Vec3d offset, double size) {
        AxisAlignedBB box = getEntityBoundingBox().grow(size, 0.1, size).offset(ModUtils.getRelativeOffset(this, new Vec3d(offset.x, 0.1, offset.z)));
        ModUtils.destroyBlocksInAABB(box, world, this);
    }

    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.00001F);
        if(!this.isDeathState()) {
            this.clearEvents();
        }
        this.setDeathState(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.lockLook = true;

        addEvent(()-> {
            this.playSound(SoundsHandler.ROT_KNIGHT_WALK, 1F, 0.6f + ModRand.getFloat(0.3F));
        }, 15);
        addEvent(()-> {
            if(this.getSpawnLocation() != null && !world.isRemote) {
                this.turnBossIntoSummonSpawner(this.getSpawnLocation());
                this.createCoinSpawns(this.getPositionVector(), ModRand.range(2, 6), 0, 0);
            }
            this.setDead();
            this.setDropItemsWhenDead(true);
            this.setDeathState(false);
        }, 104);
        super.onDeath(cause);
    }

    @Nullable
    @Override
    public SoundEvent getBossMusic() {
        return SoundsHandler.FALLEN_STORMVIER_TRACK;
    }
}
