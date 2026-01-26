package com.dungeon_additions.da.entity.desert_dungeon.boss;

import com.dungeon_additions.da.blocks.boss.BlockEnumBossSummonState;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.desert_dungeon.EntityDesertBase;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityPyre;
import com.dungeon_additions.da.entity.gaelon_dungeon.friendly.EntityFriendlyCursedRevenant;
import com.dungeon_additions.da.entity.tileEntity.TileEntityBossReSummon;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ServerScaleUtil;
import com.google.common.base.Optional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class EntitySharedDesertBoss extends EntityDesertBase {

    public final String ANIM_SET_SHIELDED = "set_shielded";
    public final String ANIM_SHIELDED = "shielded";
    public final String ANIM_END_SHIELDED = "end_shielded";
    public final String ANIM_PHASE_TRANSITION = "phase_transition";

    protected boolean inLowHealthState = false;
    protected int lowHealthTimer = MobConfig.desert_bosses_shield_time * 20;

 protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntitySharedDesertBoss.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Boolean> SHIELDED = EntityDataManager.createKey(EntitySharedDesertBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> START_SHIELDED = EntityDataManager.createKey(EntitySharedDesertBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_SHIELDED = EntityDataManager.createKey(EntitySharedDesertBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ENRAGED = EntityDataManager.createKey(EntitySharedDesertBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_PHASE_TRANSITIONED = EntityDataManager.createKey(EntitySharedDesertBoss.class, DataSerializers.BOOLEAN);
    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntitySharedDesertBoss.class, DataSerializers.BLOCK_POS);
    public static DataParameter<Boolean> SET_SPAWN_LOC = EntityDataManager.createKey(EntitySharedDesertBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAD_PREVIOUS_TARGET = EntityDataManager.createKey(EntitySharedDesertBoss.class, DataSerializers.BOOLEAN);

    public void setShielded(boolean value) {this.dataManager.set(SHIELDED, Boolean.valueOf(value));}
    public boolean isShielded() {return this.dataManager.get(SHIELDED);}
    public void setStartShielded(boolean value) {this.dataManager.set(START_SHIELDED, Boolean.valueOf(value));}
    public boolean isStartShielded() {return this.dataManager.get(START_SHIELDED);}
    public void setEndShielded(boolean value) {this.dataManager.set(END_SHIELDED, Boolean.valueOf(value));}
    public boolean isEndShielded() {return this.dataManager.get(END_SHIELDED);}
    public void setEnraged(boolean value) {this.dataManager.set(ENRAGED, Boolean.valueOf(value));}
    public boolean isEnraged() {return this.dataManager.get(ENRAGED);}
    public void setHasPhaseTransitioned(boolean value) {this.dataManager.set(HAS_PHASE_TRANSITIONED, Boolean.valueOf(value));}
    public boolean isHasPhaseTransitioned() {return this.dataManager.get(HAS_PHASE_TRANSITIONED);}
    public void setSetSpawnLoc(boolean value) {
        this.dataManager.set(SET_SPAWN_LOC, Boolean.valueOf(value));
    }
    public boolean isSetSpawnLoc() {
        return this.dataManager.get(SET_SPAWN_LOC);
    }
    public void setSpawnLocation(BlockPos pos) {
        this.dataManager.set(SPAWN_LOCATION, pos);
    }

    public BlockPos getSpawnLocation() {
        return this.dataManager.get(SPAWN_LOCATION);
    }
    public boolean isHadPreviousTarget() {return this.dataManager.get(HAD_PREVIOUS_TARGET);}
    public void setHadPreviousTarget(boolean value) {this.dataManager.set(HAD_PREVIOUS_TARGET, Boolean.valueOf(value));}


    @Nullable
    public UUID getOtherBossId()
    {
        return (UUID)((Optional)this.dataManager.get(OWNER_UNIQUE_ID)).orNull();
    }

    public void setOtherBossId(@Nullable UUID p_184754_1_)
    {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(p_184754_1_));
    }

    // We want to ensure that when this method is called it'll return exactly the other boss
    @Nullable
    public EntityLivingBase getOtherBoss()
    {
        UUID uuid = this.getOtherBossId();
        if(uuid == null) {
            return null;
        }
        else {
            List<EntitySharedDesertBoss> nearbyBoss = this.world.getEntitiesWithinAABB(EntitySharedDesertBoss.class, this.getEntityBoundingBox().grow(50D), e -> !e.getIsInvulnerable());
            if(!nearbyBoss.isEmpty()) {
                for(EntitySharedDesertBoss boss : nearbyBoss) {
                    if(boss.getUniqueID() == uuid) {
                        return boss;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Shielded", this.isShielded());
        nbt.setBoolean("Start_Shielded", this.isStartShielded());
        nbt.setBoolean("End_Shielded", this.isEndShielded());
        nbt.setBoolean("Enraged", this.isEnraged());
        nbt.setBoolean("Has_Phase_Transitioned", this.isHasPhaseTransitioned());
        nbt.setBoolean("Had_Target", this.isHadPreviousTarget());
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
        nbt.setBoolean("Set_Spawn_Loc", this.dataManager.get(SET_SPAWN_LOC));
        if (this.getOtherBossId() == null)
        {
            nbt.setString("OwnerUUID", "");
        }
        else
        {
            nbt.setString("OwnerUUID", this.getOtherBossId().toString());
        }
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void onUpdate() {

        if(!world.isRemote) {

            if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
                Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());

                double distSq = this.getDistanceSq(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                double distance = Math.sqrt(distSq);
                if(!world.isRemote) {
                    if (distance > 28) {
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
                            if(this.timesUsed != 0) {
                                this.timesUsed--;
                                turnBossIntoSummonSpawner(this.getSpawnLocation());
                                this.setDead();
                                if(this.getOtherBoss() != null) {
                                    this.getOtherBoss().setDead();
                                }
                            } else {
                                this.resetBossTask();
                            }
                        }
                    }
                }
            }


            if(this.isShielded()) {
                if(lowHealthTimer <= 0) {
                    this.setEndLowHealthState();
                    this.lowHealthTimer = 20 * MobConfig.desert_bosses_shield_time;
                } else {
                    lowHealthTimer--;
                }


            }

            //slight handling for the other relative boss
            if(this.getOtherBoss() != null) {
                    if(!this.getOtherBoss().isEntityAlive()) {
                        this.setOtherBossId(null);
                        this.setEnraged(true);
                        //have this do something with phase transitioning
                }

                if(this.getOtherBoss() instanceof EntitySharedDesertBoss) {
                    EntitySharedDesertBoss boss = ((EntitySharedDesertBoss) this.getOtherBoss());

                    //sets for a faster cooldown
                    if(boss.isShielded()) {
                        this.setEnraged(true);
                    } else {
                        this.setEnraged(false);
                    }

                    //lessens the cooldown time if both bosses are shielded
                    if(boss.isShielded() && this.isShielded()) {
                        lowHealthTimer -=5;
                    }
                }
            }
        }
        super.onUpdate();
    }

    private static final ResourceLocation LOO_RESET = new ResourceLocation(ModReference.MOD_ID, "forgotten_temple_reset");

    private void resetBossTask() {
        this.setImmovable(true);
        this.setHadPreviousTarget(false);
        BlockPos pos = this.getSpawnLocation();
        world.setBlockState(pos, ModBlocks.AEGYPTIAN_KEY_BLOCK.getDefaultState());
        world.setBlockState(pos.add(0, 2, 0), Blocks.CHEST.getDefaultState());
        TileEntity te = world.getTileEntity(pos.add(0, 2, 0));
        if(te instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) te;
            chest.setLootTable(LOO_RESET, rand.nextLong());
        }
        this.experienceValue = 0;
        this.setDropItemsWhenDead(false);
        this.setDead();

        if(this.getOtherBoss() != null) {
            this.getOtherBoss().setDropItemsWhenDead(false);
            this.getOtherBoss().setDead();
        }
    }

    protected void turnBossIntoSummonSpawner(BlockPos pos) {
        if(ModConfig.boss_resummon_enabled) {
            if (this.timesUsed <= ModConfig.boss_resummon_max_uses && !world.isRemote) {
                world.setBlockState(pos, ModBlocks.BOSS_RESUMMON_BLOCK.getDefaultState());
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileEntityBossReSummon) {
                    TileEntityBossReSummon boss_spawner = ((TileEntityBossReSummon) te);
                    boss_spawner.setState(BlockEnumBossSummonState.INACTIVE, this.timesUsed, "aegyptian");
                }
            }
        }
    }


    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setShielded(nbt.getBoolean("Shielded"));
        this.setStartShielded(nbt.getBoolean("Start_Shielded"));
        this.setEndShielded(nbt.getBoolean("End_Shielded"));
        this.setEnraged(nbt.getBoolean("Enraged"));
        this.setHasPhaseTransitioned(nbt.getBoolean("Has_Phase_Transitioned"));
        this.setHadPreviousTarget(nbt.getBoolean("Had_Target"));
        this.dataManager.set(SET_SPAWN_LOC, nbt.getBoolean("Set_Spawn_Loc"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
        String s;
        if (nbt.hasKey("OwnerUUID", 8))
        {
            s = nbt.getString("OwnerUUID");
        }
        else
        {
            String s1 = nbt.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(Objects.requireNonNull(this.getServer()), s1);
        }

        if (!s.isEmpty())
        {
            this.setOtherBossId(UUID.fromString(s));
        }
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SHIELDED, Boolean.valueOf(false));
        this.dataManager.register(START_SHIELDED, Boolean.valueOf(false));
        this.dataManager.register(END_SHIELDED, Boolean.valueOf(false));
        this.dataManager.register(ENRAGED, Boolean.valueOf(false));
        this.dataManager.register(HAS_PHASE_TRANSITIONED, Boolean.valueOf(false));
        this.dataManager.register(SET_SPAWN_LOC, Boolean.valueOf(false));
        this.dataManager.register(HAD_PREVIOUS_TARGET, Boolean.valueOf(false));
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
    }


    public EntitySharedDesertBoss(World world, int timesUsed, BlockPos pos) {
        super(world);
    }

    public EntitySharedDesertBoss(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntitySharedDesertBoss(World worldIn) {
        super(worldIn);
    }


    protected void setLowHealthState() {
        this.clearEvents();
        this.setStartShielded(true);
        this.setFightMode(true);
        this.setImmovable(true);
        addEvent(()-> {
            this.setStartShielded(false);
            this.setFightMode(false);
            this.setShielded(true);
            this.lockLook = true;
            this.setImmovable(true);
        }, 40);
    }

    protected void setEndLowHealthState() {
        this.setShielded(false);
        this.setEndShielded(true);
        this.setFightMode(true);
        //play sound for healing
        this.heal((float) (this.getMaxHealth() * MobConfig.desert_bosses_revive_health_bonus));
        this.inLowHealthState = false;

        addEvent(() -> {
            this.setFightMode(false);
            this.setEndShielded(false);
            this.setImmovable(false);
            this.lockLook = false;
        }, 30);
    }


    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }
}
