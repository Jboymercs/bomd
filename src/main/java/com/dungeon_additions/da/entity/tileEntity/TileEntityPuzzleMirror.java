package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.blocks.base.IBlockUpdater;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileEntityPuzzleMirror extends TileEntity implements ITickable
{
    public long lastInteractTime = 0;
    private int skullRotation;


    public int serverSkullRotation;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByte("Rot", (byte)(this.skullRotation & 255));
        compound.setInteger("Server_Rot", this.serverSkullRotation);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.skullRotation = compound.getByte("Rot");
        this.serverSkullRotation = compound.getInteger("Server_Rot");
    }

    /**
     * WHY CANT I GET THE DAMN NUMBER TO READ IN THE PROJECTILE CLASS
     *
     */
    public void addRotationTooSkull() {
        if(skullRotation == 16) {
            skullRotation = 1;
        } else {
            this.skullRotation++;
        }
        this.markDirty();
    }


    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 4, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }


    @SideOnly(Side.CLIENT)
    public int getSkullRotation()
    {
        return this.skullRotation;
    }

    public void setSkullRotation(int rotation)
    {
        this.skullRotation = rotation;
    }

    @Override
    public void mirror(Mirror mirrorIn)
    {
        if (this.world != null)
        {
            this.skullRotation = mirrorIn.mirrorRotation(this.skullRotation, 16);
        }
    }

    @Override
    public void rotate(Rotation rotationIn)
    {
        if (this.world != null)
        {
            this.skullRotation = rotationIn.rotate(this.skullRotation, 16);
        }
    }

    @Override
    public void update() {
        if (world.isRemote && this.getBlockType() instanceof IBlockUpdater) {
            ((IBlockUpdater) this.getBlockType()).update(world, pos);
        }

        if(!world.isRemote) {
            serverSkullRotation = skullRotation;
        }


    }

    @Override
    public void markDirty() {
        IBlockState state = world.getBlockState(pos);
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
        super.markDirty();
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    @SideOnly(Side.CLIENT)
    public float getAnimationProgress(float p_184295_1_)
    { return 0; }
}
