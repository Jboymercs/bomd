package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.blocks.aspect_forge.BlockEnumAspectForge;
import com.dungeon_additions.da.blocks.boss.BlockEnumBossSummonState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;

public class TileEntityAspectForge extends TileEntity implements ITickable {

    private BlockEnumAspectForge state = BlockEnumAspectForge.BASE;

    private String type = "base";

    @Override
    public void update() {
        if(world == null) {
            return;
        }

        if(!world.isRemote) {
            if(type == null) {
                this.type = "base";
            } else if(type.equals("aspect_shield")) {
                this.setState(BlockEnumAspectForge.SHIELD);
            } else if(type.equals("aspect_spear")) {
                this.setState(BlockEnumAspectForge.SPEAR);
            } else if(type.equals("aspect_sword")) {
                this.setState(BlockEnumAspectForge.SWORD);
            }  else if(type.equals("aspect_dagger")) {
                this.setState(BlockEnumAspectForge.DAGGER);
            }  else if(type.equals("aspect_colossal")) {
                this.setState(BlockEnumAspectForge.COLOSSAL);
            }  else if(type.equals("aspect_duelist")) {
                this.setState(BlockEnumAspectForge.DUELIST);
            }  else if(type.equals("aspect_mage")) {
                this.setState(BlockEnumAspectForge.MAGE);
            }  else if(type.equals("aspect_bow")) {
                this.setState(BlockEnumAspectForge.BOW);
            }  else if(type.equals("base")) {
                this.setState(BlockEnumAspectForge.BASE);
            }
        }
    }

    public BlockEnumAspectForge getState() {
        return state;
    }

    public void setState(BlockEnumAspectForge state) {
        this.state = state;
        if(state == BlockEnumAspectForge.BASE) {
            this.type = "base";
        } else if (state == BlockEnumAspectForge.BOW) {
            this.type = "aspect_bow";
        } else if (state == BlockEnumAspectForge.SPEAR) {
            this.type = "aspect_spear";
        } else if (state == BlockEnumAspectForge.SWORD) {
            this.type = "aspect_sword";
        } else if (state == BlockEnumAspectForge.DUELIST) {
            this.type = "aspect_duelist";
        } else if (state == BlockEnumAspectForge.DAGGER) {
            this.type = "aspect_dagger";
        } else if (state == BlockEnumAspectForge.SHIELD) {
            this.type = "aspect_shield";
        } else if (state == BlockEnumAspectForge.COLOSSAL) {
            this.type = "aspect_colossal";
        } else if (state == BlockEnumAspectForge.MAGE) {
            this.type = "aspect_mage";
        }
        markDirty();
    }

    public void setState(BlockEnumAspectForge state, String type) {
        this.state = state;
        this.type = type;
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("state", (byte)state.ordinal());
        compound.setString("aspect_type", this.type);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey("state", 1)) state = BlockEnumAspectForge.values()[compound.getByte("state")];
        this.type = compound.getString("aspect_type");
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("state", (byte)state.ordinal());
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        if (nbt.hasKey("state", 1)) state = BlockEnumAspectForge.values()[nbt.getByte("state")];

    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    @Override
    public void markDirty() {
        IBlockState state = world.getBlockState(pos);
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
        super.markDirty();
    }
}
