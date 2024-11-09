package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.entity.logic.MobSpawnerLogic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;

public abstract class tileEntityMobSpawner extends TileEntity implements ITickable {
    private final MobSpawnerLogic spawnerLogic = this.getSpawnerLogic();

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.spawnerLogic.readFromNBT(compound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.spawnerLogic.writeToNBT(compound);
        return compound;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update() {
        this.spawnerLogic.updateSpawner();
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
        nbttagcompound.removeTag("SpawnPotentials");
        return nbttagcompound;
    }

    public boolean onlyOpsCanSetNbt() {
        return true;
    }

    /*
     * Override this to specify what kind of logic you want to use
     */
    protected abstract MobSpawnerLogic getSpawnerLogic();

    public MobSpawnerLogic getSpawnerBaseLogic() {
        return this.spawnerLogic;
    }
}
