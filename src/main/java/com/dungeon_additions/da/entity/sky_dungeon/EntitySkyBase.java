package com.dungeon_additions.da.entity.sky_dungeon;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntitySkyBase extends EntityAbstractBase {

    private static final DataParameter<Boolean> HAS_SPAWN = EntityDataManager.createKey(EntitySkyBase.class, DataSerializers.BOOLEAN);
    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntitySkyBase.class, DataSerializers.BLOCK_POS);
    public boolean hasFallTpOverride = false;
    public boolean damageOverride = false;
    public EntitySkyBase(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    private boolean isHasSpawn() {return this.dataManager.get(HAS_SPAWN);}
    private void setHasSpawn(boolean value) {this.dataManager.set(HAS_SPAWN, Boolean.valueOf(value));}
    public BlockPos getSpawnLocation() {
        return this.dataManager.get(SPAWN_LOCATION);
    }

    public void setSpawnLocation(BlockPos pos) {
        this.dataManager.set(SPAWN_LOCATION, pos);
    }

    public EntitySkyBase(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getImmediateSource() instanceof EntitySkyBase) {
            EntitySkyBase base = (EntitySkyBase) source.getImmediateSource();
            if(!base.damageOverride) {
                return false;
            }
        }


        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
        nbt.setBoolean("Has_Spawn", this.isHasSpawn());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setHasSpawn(nbt.getBoolean("Has_Spawn"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!this.isHasSpawn()) {
            this.setSpawnLocation(new BlockPos(this.posX, this.posY, this.posZ));
            this.setHasSpawn(true);
        }

        if(!world.isRemote) {
            if(this.ticksExisted % 40 == 0 && this.isPotionActive(MobEffects.POISON)) {
                this.removePotionEffect(MobEffects.POISON);
            }

            if(this.fallDistance > 24 && hasFallTpOverride) {
                if(this.getSpawnLocation() != null) {
                    Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());
                    this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                    this.fallDistance = 0;
                }
            }
        }
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(HAS_SPAWN, Boolean.valueOf(false));
        //
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
    }


    @Override
    protected boolean canDespawn() {
        return false;
    }
}
