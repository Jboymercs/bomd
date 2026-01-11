package com.dungeon_additions.da.entity.void_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.blocks.BlockBase;
import com.dungeon_additions.da.blocks.boss.BlockEnumBossSummonState;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.EntityAIBlossom;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.ai.void_dungeon.EntityAIObsidilith;
import com.dungeon_additions.da.entity.blossom.EntityAbstractVoidBlossom;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.desert_dungeon.miniboss.ProjectileYellowWave;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityPyre;
import com.dungeon_additions.da.entity.generic.EntityDelayedExplosion;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.tileEntity.TileEntityBossReSummon;
import com.dungeon_additions.da.entity.tileEntity.TileEntityObsidilithRune;
import com.dungeon_additions.da.entity.util.IEntitySound;
import com.dungeon_additions.da.entity.void_dungeon.obsidilith_action.ActionFlameRing;
import com.dungeon_additions.da.entity.void_dungeon.obsidilith_action.ActionRedWave;
import com.dungeon_additions.da.entity.void_dungeon.obsidilith_action.ActionYellowWave;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.*;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
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
import java.util.function.Supplier;

public class EntityObsidilith extends EntityEndBase implements IAnimatable, IAnimationTickable, IAttack, IScreenShake, IEntitySound {

    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.NOTCHED_6));
    private static final DataParameter<Boolean> SUMMON_STATE = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEATH_STATE = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LEAP_ATTACK = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHIELDED = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PURPLE_ATTACK = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RED_ATTACK = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLAME_ATTACK = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLUE_ATTACK = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> YELLOW_ATTACK = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> STAT_LINE = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.FLOAT);
    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BLOCK_POS);
    public static DataParameter<Boolean> SET_SPAWN_LOC = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAD_PREVIOUS_TARGET = EntityDataManager.createKey(EntityObsidilith.class, DataSerializers.BOOLEAN);
    private final String ANIM_IDLE = "placeholder";
    private final String ANIM_SUMMON = "summon";
    private final String ANIM_DEATH = "death";

    private int shakeTime = 0;
    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;
    public boolean isSummonState() {return this.dataManager.get(SUMMON_STATE);}
    public void setSummonState(boolean value) {this.dataManager.set(SUMMON_STATE, Boolean.valueOf(value));}
    public boolean isPurpleAttack() {return this.dataManager.get(PURPLE_ATTACK);}
    public void setPurpleAttack(boolean value) {this.dataManager.set(PURPLE_ATTACK, Boolean.valueOf(value));}
    public boolean isFlameAttack() {return this.dataManager.get(FLAME_ATTACK);}
    public void setFlameAttack(boolean value) {this.dataManager.set(FLAME_ATTACK, Boolean.valueOf(value));}
    public boolean isRedAttack() {return this.dataManager.get(RED_ATTACK);}
    public void setRedAttack(boolean value) {this.dataManager.set(RED_ATTACK, Boolean.valueOf(value));}
    public boolean isBlueAttack() {return this.dataManager.get(BLUE_ATTACK);}
    public void setBlueAttack(boolean value) {this.dataManager.set(BLUE_ATTACK, Boolean.valueOf(value));}
    public boolean isDeathState() {return this.dataManager.get(DEATH_STATE);}
    public void setDeathState(boolean value) {this.dataManager.set(DEATH_STATE, Boolean.valueOf(value));}
    public boolean isLeapAttack() {return this.dataManager.get(LEAP_ATTACK);}
    public void setLeapAttack(boolean value) {this.dataManager.set(LEAP_ATTACK, Boolean.valueOf(value));}
    public boolean isYellowAttack() {return this.dataManager.get(YELLOW_ATTACK);}
    public void setYellowAttack(boolean value) {this.dataManager.set(YELLOW_ATTACK, Boolean.valueOf(value));}
    public boolean isShielded() {return this.dataManager.get(SHIELDED);}
    public void setShielded(boolean value) {this.dataManager.set(SHIELDED, Boolean.valueOf(value));}
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
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

    public EntityObsidilith(World world, int timesUsed, BlockPos pos) {
        super(world, timesUsed, pos);
        this.timesUsed = timesUsed;
        this.timesUsed++;
        this.doBossReSummonScaling();
        this.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        this.setImmovable(true);
        this.setShielded(false);
        this.setSize(1.75F, 4.4F);
        this.iAmBossMob = true;
        this.isImmuneToFire = true;
        this.setSpawnLocation(new BlockPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
        this.setSetSpawnLoc(true);
        this.onSummonBoss();
    }

    public EntityObsidilith(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.setShielded(false);
        this.setSize(1.75F, 4.4F);
        this.iAmBossMob = true;
        this.isImmuneToFire = true;
        this.onSummonBoss();
    }

    public EntityObsidilith(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setImmovable(true);
        this.setShielded(false);
        this.setSize(1.75F, 4.4F);
        this.iAmBossMob = true;
        this.isImmuneToFire = true;
        this.setSpawnLocation(new BlockPos(x, y, z));
        this.setSetSpawnLoc(true);
        this.onSummonBoss();
    }

    public void onSummonBoss() {
        this.setSummonState(true);
        addEvent(()-> {
            this.setSummonState(false);
        }, 30);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(SUMMON_STATE, Boolean.valueOf(false));
        this.dataManager.register(DEATH_STATE, Boolean.valueOf(false));
        this.dataManager.register(LEAP_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SHIELDED, Boolean.valueOf(false));
        this.dataManager.register(PURPLE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(RED_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(FLAME_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(BLUE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        this.dataManager.register(STAT_LINE,  0.75F);
        this.dataManager.register(SET_SPAWN_LOC, Boolean.valueOf(false));
        this.dataManager.register(HAD_PREVIOUS_TARGET, Boolean.valueOf(false));
        this.dataManager.register(YELLOW_ATTACK, Boolean.valueOf(false));
        //
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
        super.entityInit();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Summon", this.isSummonState());
        nbt.setBoolean("Death_State", this.isDeathState());
        nbt.setBoolean("Leap_Attack", this.isLeapAttack());
        nbt.setBoolean("Shielded", this.isShielded());
        nbt.setBoolean("Purple_Attack", this.isPurpleAttack());
        nbt.setBoolean("Red_Attack", this.isRedAttack());
        nbt.setBoolean("Flame_Attack", this.isFlameAttack());
        nbt.setBoolean("Blue_Attack", this.isBlueAttack());
        nbt.setBoolean("Shaking", this.isShaking());
        nbt.setBoolean("Yellow_Attack", this.isYellowAttack());
        nbt.setFloat("Stat_Line", this.getStatLine());
        nbt.setBoolean("Had_Target", this.isHadPreviousTarget());
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
        nbt.setBoolean("Set_Spawn_Loc", this.dataManager.get(SET_SPAWN_LOC));
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSummonState(nbt.getBoolean("Summon"));
        this.setDeathState(nbt.getBoolean("Death_State"));
        this.setLeapAttack(nbt.getBoolean("Leap_Attack"));
        this.setPurpleAttack(nbt.getBoolean("Purple_Attack"));
        this.setRedAttack(nbt.getBoolean("Red_Attack"));
        this.setFlameAttack(nbt.getBoolean("Flame_Attack"));
        this.setBlueAttack(nbt.getBoolean("Blue_Attack"));
        this.setYellowAttack(nbt.getBoolean("Yellow_Attack"));
        this.setShielded(nbt.getBoolean("Shielded"));
        this.setShaking(nbt.getBoolean("Shaking"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.setStateLine(nbt.getFloat("Stat_Line"));
        this.setHadPreviousTarget(nbt.getBoolean("Had_Target"));
        this.dataManager.set(SET_SPAWN_LOC, nbt.getBoolean("Set_Spawn_Loc"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
        super.readEntityFromNBT(nbt);
    }

    protected boolean hasLaunchedRunesOne = false;
    protected boolean hasLaunchedRunesTwo = false;
    protected boolean hasLaunchedRunesThree = false;


    @Override
    public void onUpdate() {
        super.onUpdate();
        if(world.isRemote && ticksExisted == 1 && ModConfig.experimental_features) {
            this.playMusic(this);
        }
        double currentHealth = this.getHealth() / this.getMaxHealth();
        this.bossInfo.setPercent(getHealth() / getMaxHealth());
        this.setRotation(0, 0);
        this.shakeTime--;
        this.setRotationYawHead(0);
        if(!this.isImmovable()) {
            this.motionX = 0;
            this.motionZ = 0;
        }

        if(!this.isFightMode() && this.ticksExisted % 10 == 0) {
            if(this.getSpawnLocation() != null) {
                if(this.getPosition() != this.getSpawnLocation()) {
                    this.setPosition(this.getSpawnLocation().getX() + 0.5, this.getSpawnLocation().getY(), this.getSpawnLocation().getZ() + 0.5);
                }
            }
        }


        //clears the field of all nearby Endermen to help with the fight
        if(ticksExisted == 5 && !world.isRemote) {
            if(this.getSpawnLocation() != null) {
                List<EntityEnderman> nearbyMonsters = this.world.getEntitiesWithinAABB(EntityEnderman.class, this.getEntityBoundingBox().grow(20D), e -> !e.getIsInvulnerable());
                for(EntityEnderman monster : nearbyMonsters) {
                    monster.setDead();
                }
            }
        }

        if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
            Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());

            double distSq = this.getDistanceSq(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
            double distance = Math.sqrt(distSq);
            //This basically makes it so the Obsidilith will be teleported if they are too far away from the Arena
            if(!world.isRemote) {
                if (distance > 35) {
                    this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                }
            }
        }


        if(!world.isRemote) {
            if(this.isLeapAttack() && this.onGround) {
                this.onStopLeaping();
            }
            holdByte--;
            //start Runes Handling
                if (currentHealth < 0.75 && !hasLaunchedRunesOne) {
                    this.performRunePlacement(3);
                    hasLaunchedRunesOne = true;
                }
                if (hasLaunchedRunesOne && holdByte < 0) {
                    AxisAlignedBB box = getEntityBoundingBox().grow(40, 6, 40).offset(0, -2, 0);
                    BlockPos setTooPos = ModUtils.searchForBlocksObsdilith(box, world, this, ModBlocks.OBSIDILITH_RUNE.getDefaultState());
                    if (setTooPos == null) {
                        //Remove Outer Shield from Entity
                        this.setShielded(false);
                        this.setStateLine((float) 0.5);
                    }
                }

                if (currentHealth < 0.5 && !hasLaunchedRunesTwo) {
                    this.performRunePlacement(4);
                    hasLaunchedRunesTwo = true;
                }
                if (hasLaunchedRunesTwo && holdByte < 0) {
                    AxisAlignedBB box = getEntityBoundingBox().grow(40, 6, 40).offset(0, -2, 0);
                    BlockPos setTooPos = ModUtils.searchForBlocksObsdilith(box, world, this, ModBlocks.OBSIDILITH_RUNE.getDefaultState());
                    if (setTooPos == null) {
                        //Remove Outer Shield from Entity
                        this.setShielded(false);
                        this.setStateLine((float) 0.25);
                    }
                }

            if (currentHealth < 0.25 && !hasLaunchedRunesThree) {
                this.performRunePlacement(5);
                hasLaunchedRunesThree = true;
            }
            if (hasLaunchedRunesThree && holdByte < 0) {
                AxisAlignedBB box = getEntityBoundingBox().grow(40, 6, 40).offset(0, -2, 0);
                BlockPos setTooPos = ModUtils.searchForBlocksObsdilith(box, world, this, ModBlocks.OBSIDILITH_RUNE.getDefaultState());
                if (setTooPos == null) {
                    //Remove Outer Shield from Entity
                    this.setShielded(false);
                    this.setStateLine((float) 0);
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

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "obsidilith");

    private void placeLootOnDeath(BlockPos blockpos) {
        if(!world.isRemote && !MobConfig.obsidilith_two_part_boss) {
            if (this.getSpawnLocation() != null) {
                world.setBlockState(this.getSpawnLocation().add(4, 0, 0), Blocks.PINK_SHULKER_BOX.getDefaultState());
                TileEntity te = world.getTileEntity(this.getSpawnLocation().add(4, 0, 0));
                if (te instanceof TileEntityShulkerBox) {
                    TileEntityShulkerBox chest = (TileEntityShulkerBox) te;
                    chest.setLootTable(LOOT_MOB, rand.nextLong());
                }
            } else {
                world.setBlockState(blockpos, Blocks.PINK_SHULKER_BOX.getDefaultState());
                TileEntity te = world.getTileEntity(blockpos);
                if (te instanceof TileEntityShulkerBox) {
                    TileEntityShulkerBox chest = (TileEntityShulkerBox) te;
                    chest.setLootTable(LOOT_MOB, rand.nextLong());
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

    int holdByte = 0;
    private void performRunePlacement(int times) {
        this.holdByte = 30;
        this.setShielded(true);
        for (int i = 0; i < times; i++) {
            this.addEvent(()-> {
                AxisAlignedBB box = getEntityBoundingBox().grow(40, 6, 40).offset(0, -2, 0);
                BlockPos setTooPos = ModUtils.searchForBlocksObsdilith(box, world, this, ModBlocks.FAKE_OBSIDIAN.getDefaultState());
                if (setTooPos != null) {
                    if(world.rand.nextInt(3) == 0) {
                        world.setBlockState(setTooPos.add(0, 1, 0), Blocks.OBSIDIAN.getDefaultState());
                        world.setBlockState(setTooPos.add(0, 2, 0), ModBlocks.OBSIDILITH_RUNE.getDefaultState());
                    } else {
                        world.setBlockState(setTooPos.add(0, 1, 0), ModBlocks.OBSIDILITH_RUNE.getDefaultState());
                    }

                }
            }, i * 10);
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(27D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.obsidilith_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.obsidilith_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.obsidilith_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.obsidilith_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIObsidilith<>(this, 0, 100, 18F, 0F));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        int cooldown_degradation = MobConfig.obsidilith_degradation_cooldown * playersNearbyAmount;
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && !this.isSummonState() && !this.isDeathState()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(spikeAttack, flame_attack, red_attack, purple_attack, yellow_attack));
            double[] weights = {
                    (prevAttacks != spikeAttack && HealthChange < 0.5) ? distance * 0.02 : (HealthChange >= 0.5) ? distance * 0.02 : 0, // Spike Attack Simple
                    (distance < 8 && prevAttacks != flame_attack) ? distance * 0.03 : 0, //Flame Ring Attack
                    (distance > 4 && prevAttacks != red_attack) ? distance * 0.02 : 0, //Red Attack
                    (prevAttacks !=purple_attack && !this.isShielded() && HealthChange < 0.75) ? distance * 0.02 : 0, //Purple Attack Teleport
                    (prevAttacks != yellow_attack && distance > 2 && HealthChange < 0.5) ? distance * 0.02 : 0 //Yellow Attack
                    //maybe a minion summon attack
            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return this.isShielded() ? (int) (MobConfig.obsidilith_cooldown_shielded * 20) - cooldown_degradation : (int) (MobConfig.obsidilith_cooldown * 20) - cooldown_degradation;
    }

    Supplier<Projectile> yellow_wave_projectiles = () -> new ProjectileYellowWave(world, this, (float) this.getAttack(), null, true);

    private final Consumer<EntityLivingBase> yellow_attack = (target) -> {
      this.setFightMode(true);
      this.setYellowAttack(true);
        world.setEntityState(this, ModUtils.SIXTH_PARTICLE_BYTE);
        addEvent(()-> playSound(SoundsHandler.OBSIDILITH_CAST, 2.0f, 2.2f), 5);

        addEvent(()-> {
            new ActionYellowWave(yellow_wave_projectiles).performAction(this, target);
        }, 40);


        addEvent(()-> {
            this.setYellowAttack(false);
            this.setFightMode(false);
        }, 160);
    };

   private Vec3d savedPos;

    private final Consumer<EntityLivingBase> purple_attack = (target) -> {
      this.setFightMode(true);
      this.setPurpleAttack(true);
        world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
        addEvent(()-> playSound(SoundsHandler.OBSIDILITH_CAST, 2.0f, 0.5f), 5);
        addEvent(()-> {
            savedPos = this.getPositionVector();
            //spawn indication for where the Obsidlith is traveling too

            //after about 1.5 seconds the Obsidilith will drop causing an explosion
            this.setImmovable(false);
            this.motionY = 0;
            Vec3d pos = target.getPositionVector().add(target.getLookVec()).add(ModUtils.yVec(20));
            this.spawnTeleportAction(target.getPositionVector());
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
            this.setPosition(pos.x, pos.y, pos.z);
            //set Leaping
            this.setLeapAttack(true);
        }, 35);
    };

    private void onStopLeaping() {
        this.setLeapAttack(false);
        DamageSource source = ModDamageSource.builder()
                .type(ModDamageSource.EXPLOSION)
                .directEntity(this)
                .stoppedByArmorNotShields().disablesShields().build();

        ModUtils.handleAreaImpact(5, (e) -> this.getAttack() * 1.5F, this, this.getPositionVector().add(ModUtils.yVec(1)), source);
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f + ModRand.getFloat(0.1f));
        this.world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
        Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
        Main.proxy.spawnParticle(20,world, relPos.x, this.posY + 0.1, relPos.z, 0, 0, 0);
        this.setShaking(true);
        this.shakeTime = 30;

        if(this.getHealth() / this.getMaxHealth() <= 0.5) {
            if(savedPos != null) {
                this.doExplosivesMove(savedPos);
            } else if (this.getSpawnLocation() != null) {
                this.doExplosivesMove(new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ()));
            }
        }
        addEvent(()-> {
            //teleports entity back
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
            if(savedPos != null) {
                this.setPosition(savedPos.x, savedPos.y, savedPos.z);
            }
            this.setShaking(false);
            this.setImmovable(true);
            this.setNoGravity(false);
            this.setFightMode(false);
            this.setPurpleAttack(false);
        }, 30);
    }

    private void doExplosivesMove(Vec3d posToo) {
        for(int i = 0; i <= 40; i+= 1) {
            addEvent(()-> {
                EntityDelayedExplosion explosion = new EntityDelayedExplosion(world, this, this.getAttack(), false, true);
                Vec3d randIPos = new Vec3d(posToo.x + ModRand.getFloat(14), posToo.y, posToo.z + ModRand.getFloat(14));
                explosion.setPosition(randIPos.x, randIPos.y, randIPos.z);
                world.spawnEntity(explosion);
            }, i * 5);
        }
    }

    private final Consumer<EntityLivingBase> red_attack = (target) -> {
      this.setFightMode(true);
      this.setRedAttack(true);
      world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        addEvent(()-> playSound(SoundsHandler.OBSIDILITH_CAST, 1.5f, 0.5f), 5);
        addEvent(()-> new ActionRedWave().performAction(this, target), 30);
        addEvent(()-> this.setFightMode(false), 100);
        addEvent(()-> this.setRedAttack(false), 100);
    };
    private final Consumer<EntityLivingBase> flame_attack = (target) -> {
      this.setFightMode(true);
      this.setFlameAttack(true);
        world.setEntityState(this, ModUtils.FOURTH_PARTICLE_BYTE);
        addEvent(()-> playSound(SoundsHandler.OBSIDILITH_CAST, 1.5f, 1.5f), 5);
      addEvent(()-> new ActionFlameRing().performAction(this, target), 15);
      addEvent(()-> this.setFightMode(false), 45);
      addEvent(()-> this.setFlameAttack(false), 45);
    };
    private final Consumer<EntityLivingBase> spikeAttack = (target)-> {
        this.setFightMode(true);
        this.setBlueAttack(true);
        world.setEntityState(this, ModUtils.FIFTH_PARTICLE_BYTE);
        addEvent(()-> playSound(SoundsHandler.OBSIDILITH_CAST, 1.5f, 1.0f), 5);

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
                }, 47);

                addEvent(()-> {
                    Vec3d targetOldPos = target.getPositionVector();
                    addEvent(()-> {
                        Vec3d targetedPos = target.getPositionVector();
                        Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 3);
                        this.spawnSpikeAction(predictedPosition);
                    }, 3);
                }, 87);
            }
        }, 21);
        addEvent(()-> {
            this.setFightMode(false);
            this.setBlueAttack(false);
        }, 135);
    };

    public void spawnSpikeAction(Vec3d predictedPos) {
        EntityLivingBase target = this.getAttackTarget();
        //1
        if(target != null) {
            EntityBlueWave spike = new EntityBlueWave(this.world);
            BlockPos area = new BlockPos(predictedPos.x, predictedPos.y, predictedPos.z);
            int y = getSurfaceHeight(this.world, new BlockPos(target.posX, 0, target.posZ), (int) this.posY - 5, (int) this.posY + 7);
            spike.setPosition(area.getX(), y + 1, area.getZ());
            world.spawnEntity(spike);
            spike.playSound(SoundsHandler.APPEARING_WAVE, 0.75f, 1.0f / getRNG().nextFloat() * 0.04F + 1.2F);;
            //2
            EntityBlueWave spike2 = new EntityBlueWave(this.world);
            BlockPos area2 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z);
            int y2 = getSurfaceHeight(this.world, new BlockPos(area2.getX(), 0, area2.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike2.setPosition(area2.getX(), y2 + 1, area2.getZ());
            world.spawnEntity(spike2);
            //3
            EntityBlueWave spike3 = new EntityBlueWave(this.world);
            BlockPos area3 = new BlockPos(predictedPos.x - 1, predictedPos.y, predictedPos.z);
            int y3 = getSurfaceHeight(this.world, new BlockPos(area3.getX(), 0, area3.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike3.setPosition(area3.getX(), y3 + 1, area3.getZ());
            world.spawnEntity(spike3);
            //4
            EntityBlueWave spike4 = new EntityBlueWave(this.world);
            BlockPos area4 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 1);
            int y4 = getSurfaceHeight(this.world, new BlockPos(area4.getX(), 0, area4.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike4.setPosition(area4.getX(),y4 + 1, area4.getZ());
            world.spawnEntity(spike4);
            //5
            EntityBlueWave spike5 = new EntityBlueWave(this.world);
            BlockPos area5 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z - 1);
            int y5 = getSurfaceHeight(this.world, new BlockPos(area5.getX(), 0, area5.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike5.setPosition(area5.getX(), y5 + 1, area5.getZ());
            world.spawnEntity(spike5);
            //6
            EntityBlueWave spike6 = new EntityBlueWave(this.world);
            BlockPos area6 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z + 1);
            int y6 = getSurfaceHeight(this.world, new BlockPos(area6.getX(), 0, area6.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike6.setPosition(area6.getX(), y6 + 1, area6.getZ());
            world.spawnEntity(spike6);
            //7
            EntityBlueWave spike7 = new EntityBlueWave(this.world);
            BlockPos area7 = new BlockPos(predictedPos.x +1 , predictedPos.y, predictedPos.z - 1);
            int y7 = getSurfaceHeight(this.world, new BlockPos(area7.getX(), 0, area7.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike7.setPosition(area7.getX(), y7 + 1, area7.getZ());
            world.spawnEntity(spike7);
            //8
            EntityBlueWave spike8 = new EntityBlueWave(this.world);
            BlockPos area8 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z - 1);
            int y8 = getSurfaceHeight(this.world, new BlockPos(area8.getX(), 0, area8.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike8.setPosition(area8.getX(), y8 + 1, area8.getZ());
            world.spawnEntity(spike8);
            //9
            EntityBlueWave spike9 = new EntityBlueWave(this.world);
            BlockPos area9 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z + 1);
            int y9 = getSurfaceHeight(this.world, new BlockPos(area9.getX(), 0, area9.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike9.setPosition(area9.getX(), y9 + 1, area9.getZ());
            world.spawnEntity(spike9);
            //10
            EntityBlueWave spike10 = new EntityBlueWave(this.world);
            BlockPos area10 = new BlockPos(predictedPos.x +2, predictedPos.y, predictedPos.z );
            int y10 = getSurfaceHeight(this.world, new BlockPos(area10.getX(), 0, area10.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike10.setPosition(area10.getX(), y10 + 1, area10.getZ());
            world.spawnEntity(spike10);
            //11
            EntityBlueWave spike11 = new EntityBlueWave(this.world);
            BlockPos area11 = new BlockPos(predictedPos.x - 2, predictedPos.y, predictedPos.z );
            int y11 = getSurfaceHeight(this.world, new BlockPos(area11.getX(), 0, area11.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike11.setPosition(area11.getX(), y11 + 1, area11.getZ());
            world.spawnEntity(spike11);
            //12
            EntityBlueWave spike12 = new EntityBlueWave(this.world);
            BlockPos area12 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 2);
            int y12 = getSurfaceHeight(this.world, new BlockPos(area12.getX(), 0, area12.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike12.setPosition(area12.getX(), y12 + 1, area12.getZ());
            world.spawnEntity(spike12);
            //13
            EntityBlueWave spike13 = new EntityBlueWave(this.world);
            BlockPos area13 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z -2);
            int y13 = getSurfaceHeight(this.world, new BlockPos(area13.getX(), 0, area13.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike13.setPosition(area13.getX(), y13 + 1, area13.getZ());
            world.spawnEntity(spike13);
        }
    }

    public void spawnTeleportAction(Vec3d predictedPos) {
        EntityLivingBase target = this.getAttackTarget();
        //1
        if(target != null) {
            EntityBlueWave spike = new EntityBlueWave(this.world, false, false, true);
            BlockPos area = new BlockPos(predictedPos.x, predictedPos.y, predictedPos.z);
            int y = getSurfaceHeight(this.world, new BlockPos(target.posX, 0, target.posZ), (int) this.posY - 5, (int) this.posY + 7);
            spike.setPosition(area.getX(), y + 1, area.getZ());
            world.spawnEntity(spike);
            spike.playSound(SoundsHandler.APPEARING_WAVE, 0.75f, 1.0f / getRNG().nextFloat() * 0.04F + 1.2F);;
            //2
            EntityBlueWave spike2 = new EntityBlueWave(this.world, false, false, true);
            BlockPos area2 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z);
            int y2 = getSurfaceHeight(this.world, new BlockPos(area2.getX(), 0, area2.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike2.setPosition(area2.getX(), y2 + 1, area2.getZ());
            world.spawnEntity(spike2);
            //3
            EntityBlueWave spike3 = new EntityBlueWave(this.world, false, false, true);
            BlockPos area3 = new BlockPos(predictedPos.x - 1, predictedPos.y, predictedPos.z);
            int y3 = getSurfaceHeight(this.world, new BlockPos(area3.getX(), 0, area3.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike3.setPosition(area3.getX(), y3 + 1, area3.getZ());
            world.spawnEntity(spike3);
            //4
            EntityBlueWave spike4 = new EntityBlueWave(this.world, false, false, true);
            BlockPos area4 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 1);
            int y4 = getSurfaceHeight(this.world, new BlockPos(area4.getX(), 0, area4.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike4.setPosition(area4.getX(),y4 + 1, area4.getZ());
            world.spawnEntity(spike4);
            //5
            EntityBlueWave spike5 = new EntityBlueWave(this.world, false, false, true);
            BlockPos area5 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z - 1);
            int y5 = getSurfaceHeight(this.world, new BlockPos(area5.getX(), 0, area5.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike5.setPosition(area5.getX(), y5 + 1, area5.getZ());
            world.spawnEntity(spike5);
            //6
            EntityBlueWave spike6 = new EntityBlueWave(this.world, false, false, true);
            BlockPos area6 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z + 1);
            int y6 = getSurfaceHeight(this.world, new BlockPos(area6.getX(), 0, area6.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike6.setPosition(area6.getX(), y6 + 1, area6.getZ());
            world.spawnEntity(spike6);
            //7
            EntityBlueWave spike7 = new EntityBlueWave(this.world, false, false, true);
            BlockPos area7 = new BlockPos(predictedPos.x +1 , predictedPos.y, predictedPos.z - 1);
            int y7 = getSurfaceHeight(this.world, new BlockPos(area7.getX(), 0, area7.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike7.setPosition(area7.getX(), y7 + 1, area7.getZ());
            world.spawnEntity(spike7);
            //8
            EntityBlueWave spike8 = new EntityBlueWave(this.world, false, false, true);
            BlockPos area8 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z - 1);
            int y8 = getSurfaceHeight(this.world, new BlockPos(area8.getX(), 0, area8.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike8.setPosition(area8.getX(), y8 + 1, area8.getZ());
            world.spawnEntity(spike8);
            //9
            EntityBlueWave spike9 = new EntityBlueWave(this.world, false, false, true);
            BlockPos area9 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z + 1);
            int y9 = getSurfaceHeight(this.world, new BlockPos(area9.getX(), 0, area9.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike9.setPosition(area9.getX(), y9 + 1, area9.getZ());
            world.spawnEntity(spike9);
            //10
            EntityBlueWave spike10 = new EntityBlueWave(this.world, false, false, true);
            BlockPos area10 = new BlockPos(predictedPos.x +2, predictedPos.y, predictedPos.z );
            int y10 = getSurfaceHeight(this.world, new BlockPos(area10.getX(), 0, area10.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike10.setPosition(area10.getX(), y10 + 1, area10.getZ());
            world.spawnEntity(spike10);
            //11
            EntityBlueWave spike11 = new EntityBlueWave(this.world, false, false, true);
            BlockPos area11 = new BlockPos(predictedPos.x - 2, predictedPos.y, predictedPos.z );
            int y11 = getSurfaceHeight(this.world, new BlockPos(area11.getX(), 0, area11.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike11.setPosition(area11.getX(), y11 + 1, area11.getZ());
            world.spawnEntity(spike11);
            //12
            EntityBlueWave spike12 = new EntityBlueWave(this.world, false, false, true);
            BlockPos area12 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 2);
            int y12 = getSurfaceHeight(this.world, new BlockPos(area12.getX(), 0, area12.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike12.setPosition(area12.getX(), y12 + 1, area12.getZ());
            world.spawnEntity(spike12);
            //13
            EntityBlueWave spike13 = new EntityBlueWave(this.world, false, false, true);
            BlockPos area13 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z -2);
            int y13 = getSurfaceHeight(this.world, new BlockPos(area13.getX(), 0, area13.getZ()), (int) this.posY - 5, (int) this.posY + 7);
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
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "blossom_idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "state_controller", 0, this::predicateStates));
    }

    /**
     * Add a bit of brightness to the entity for it's Arena
     */
    @Override
    public int getBrightnessForRender() {
        return Math.min(super.getBrightnessForRender() + 30, 200);
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isSummonState() && !this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateStates(AnimationEvent<E> event) {
        if(this.isSummonState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
            return PlayState.CONTINUE;
        }
        if(this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DEATH, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
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
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            ModUtils.performNTimes(50, (i) -> {
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + ModRand.getFloat(5), this.posY + ModRand.getFloat(5),
                        this.posZ + ModRand.getFloat(5), 0, 0, 0);
            });
        }
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Main.proxy.spawnParticle(10, this.posX + pos.x, this.posY + 1, this.posZ + pos.z, 0,0.1,0);
            });
        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Main.proxy.spawnParticle(11, this.posX + pos.x, this.posY + 1, this.posZ + pos.z, 0,0.1,0);
            });
        }
        if(id == ModUtils.FOURTH_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Main.proxy.spawnParticle(12, this.posX + pos.x, this.posY + 1, this.posZ + pos.z, 0,0.1,0);
            });
        }
        if(id == ModUtils.FIFTH_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Main.proxy.spawnParticle(13, this.posX + pos.x, this.posY + 1, this.posZ + pos.z, 0,0.1,0);
            });
        }

        if(id == ModUtils.SIXTH_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Main.proxy.spawnParticle(14, this.posX + pos.x, this.posY + 1, this.posZ + pos.z, 0,0.1,0);
            });
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.isSummonState()) {
            return false;
        }
        if(this.isShielded()) {
            this.playSound(SoundsHandler.OBSIDILITH_SHIELD, 0.75f, 0.8f + ModRand.getFloat(0.4f));
            return false;
        }

        if(source.isProjectile()) {
            return super.attackEntityFrom(source, amount * 0.5F);
        }

        if(ModConfig.boss_cap_damage_enabled && amount > MobConfig.obsidilith_damage_cap) {
            return super.attackEntityFrom(source, MobConfig.obsidilith_damage_cap);
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.00001F);
        if(!this.isDeathState()) {
            this.clearEvents();
        }
        this.setDeathState(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.setShaking(true);
        this.shakeTime = 80;

        addEvent(()-> {
            if(!world.isRemote) {
                for(int i = 0; i <= 80; i+=5) {
                    if(!world.isRemote) {
                        addEvent(() -> {
                            EntityXPOrb orb = new EntityXPOrb(world, this.posX, this.posY, this.posZ, MobConfig.obsidilith_experience_orb_value);
                            orb.setPosition(this.posX, this.posY + 1, this.posZ);
                            world.spawnEntity(orb);
                        }, i);
                    }
                }
            }
        }, 20);

        addEvent(()-> {
            if(!world.isRemote) {
                for(int i = 0; i <= 60; i+=10) {
                    if(!world.isRemote) {
                        addEvent(() -> {
                            this.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 0.9f, 0.5f);
                        }, i);
                    }
                }
            }
        }, 1);

        addEvent(()-> {
            this.playSound(SoundsHandler.VOIDCLYSM_IMPACT, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            if(this.getSpawnLocation() != null) {
                if(MobConfig.obsidilith_two_part_boss) {
                    //spawns two part boss fight
                    this.experienceValue = 0;
                    this.setDropItemsWhenDead(false);
                    if(!world.isRemote) {
                        EntityVoidiclysm voidiclysm;
                        if (timesUsed != 0) {
                            voidiclysm = new EntityVoidiclysm(world, timesUsed, this.getSpawnLocation());
                            voidiclysm.setPosition(this.getSpawnLocation().getX(), this.getSpawnLocation().getY() + 1, this.getSpawnLocation().getZ());
                        } else {
                            voidiclysm = new EntityVoidiclysm(world, this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());
                        }
                        world.spawnEntity(voidiclysm);
                    }
                } else if (ModConfig.boss_resummon_enabled && this.timesUsed <= ModConfig.boss_resummon_max_uses){
                    //turns boss into spawner
                    this.turnBossIntoSummonSpawner(this.getSpawnLocation());
                }
            }
            this.setShaking(false);
        }, 85);

        addEvent(()-> {
            this.placeLootOnDeath(new BlockPos(this.posX, this.posY, this.posZ));
            this.setDeathState(false);
            this.setDead();
        }, 100);
        super.onDeath(cause);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.OBSIDILITH_HURT;
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 20.0F);
            if (dist >= 40.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.8F * screamMult);
        }
        return 0;
    }

    @Nullable
    @Override
    public SoundEvent getBossMusic() {
        return SoundsHandler.OBSIDILITH_TRACK;
    }
}
