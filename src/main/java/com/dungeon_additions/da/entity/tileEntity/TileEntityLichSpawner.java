package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.blocks.base.IBlockUpdater;
import com.dungeon_additions.da.blocks.lich.BlockSoulStar;
import com.dungeon_additions.da.blocks.lich.EnumLichSpawner;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityLichSpawner extends TileEntity implements ITickable {

    private EnumLichSpawner state = EnumLichSpawner.INACTIVE;
    private int check_others_cooldown = 60;
    public TileEntityLichSpawner() {

    }


    @Override
    public void update() {

        if (world.isRemote && this.getBlockType() instanceof IBlockUpdater) {
            ((IBlockUpdater) this.getBlockType()).update(world, pos);
        }

        if(world == null) {
            return;
        }

        if(check_others_cooldown > 0) {
            check_others_cooldown--;
            return;
        }

        if(state == EnumLichSpawner.ACTIVE) {
            AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
         BlockPos posToo = ModUtils.searchForBlocksTileEntity(box.grow(15, 15, 15), world, ModBlocks.LICH_SOUL_STAR_BLOCK.getDefaultState());

         if(posToo != null) {
             Block soul_star_blocks = world.getBlockState(posToo).getBlock();
             if(soul_star_blocks instanceof BlockSoulStar) {
                 TileEntity te = world.getTileEntity(posToo);
                 if(te instanceof TileEntityLichSpawner) {
                     if(((TileEntityLichSpawner) te).getState() == EnumLichSpawner.INACTIVE) {
                         check_others_cooldown = 60;
                         return;
                     } else {
                         //summon boss
                         List<EntityRotKnight> nearbyBoss = this.world.getEntitiesWithinAABB(EntityRotKnight.class, box.grow(25D), e -> !e.getIsInvulnerable());
                         if(!nearbyBoss.isEmpty()) {
                             //just deletes itself
                             for(int i = 0; i <= 4; i++) {
                                 BlockPos posDelete = ModUtils.searchForBlocksTileEntity(box.grow(15, 15, 15), world, ModBlocks.LICH_SOUL_STAR_BLOCK.getDefaultState());
                                 if(posDelete != null) {
                                     world.setBlockToAir(pos);
                                 }
                                 world.setBlockToAir(pos);
                             }
                         } else {
                             //if the boss hasn't been summoned, summon it
                             EntityRotKnight knight = new EntityRotKnight(world);
                             knight.setPosition(pos.getX() + 10, pos.getY() + 1, pos.getZ() + 10);
                             world.spawnEntity(knight);
                             for (int i = 0; i <= 4; i++) {
                                 BlockPos posDelete = ModUtils.searchForBlocksTileEntity(box.grow(15, 15, 15), world, ModBlocks.LICH_SOUL_STAR_BLOCK.getDefaultState());
                                 if (posDelete != null) {
                                     world.setBlockToAir(pos);
                                 }
                                 world.setBlockToAir(pos);
                             }
                         }
                     }
                 }
             }
         }
        }

    }



    public void setState(EnumLichSpawner state) {
        this.state = state;
        markDirty();
    }

    public EnumLichSpawner getState() {
        return state;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("state", (byte)state.ordinal());
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        if (nbt.hasKey("state", 1)) state = EnumLichSpawner.values()[nbt.getByte("state")];
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

    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("state", 1)) state = EnumLichSpawner.values()[nbt.getByte("state")];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("state", (byte)state.ordinal());
        return nbt;
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
