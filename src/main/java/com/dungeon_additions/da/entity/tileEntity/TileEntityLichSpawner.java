package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.blocks.base.IBlockUpdater;
import com.dungeon_additions.da.blocks.lich.BlockSoulStar;
import com.dungeon_additions.da.blocks.lich.EnumLichSpawner;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.blossom.EntityAbstractVoidBlossom;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;

public class TileEntityLichSpawner extends TileEntity implements ITickable {

    private EnumLichSpawner state = EnumLichSpawner.INACTIVE;
    private int check_others_cooldown = 60;

    private List<WeakReference<TileEntityLichSpawner>> current_altars = Lists.newArrayList();
    public boolean hasPriority = false;

    public int nearbyTileCount = 0;
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

        if(!world.isRemote && state == EnumLichSpawner.ACTIVE) {
            AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            List<EntityZombie> nearbyBossFrom = this.world.getEntitiesWithinAABB(EntityZombie.class, box.grow(60D), e -> !e.getIsInvulnerable());
            if (!nearbyBossFrom.isEmpty()) {
                if(ModConfig.soul_star_blocks_remove) {
                    world.setBlockToAir(pos);
                } else {
                    this.setState(EnumLichSpawner.INACTIVE);
                }
            }
        }

        if(check_others_cooldown > 0) {
            check_others_cooldown--;
            return;
        }

        if(state == EnumLichSpawner.ACTIVE) {
            AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            if(ModUtils.searchForInactiveTileEntities(box.grow(20), world, ModBlocks.LICH_SOUL_STAR_BLOCK.getDefaultState())) {
                if(ModUtils.createBlockPriotity(box.grow(20), world, ModBlocks.LICH_SOUL_STAR_BLOCK.getDefaultState())) {
                    hasPriority = true;
                    //Summon Boss
                    List<EntityZombie> nearbyBoss = this.world.getEntitiesWithinAABB(EntityZombie.class, box.grow(60D), e -> !e.getIsInvulnerable());
                    if(nearbyBoss.isEmpty()) {
                        EntityZombie knight = new EntityZombie(world);
                        BlockPos pos1 = ModUtils.findBossSpawnLocation(box.grow(8),world, knight,pos.getY() - 3, pos.getY() + 3);
                        if(pos1 != null && !world.isRemote) {
                            knight.setPosition(pos1.getX(), pos1.getY(), pos1.getZ());
                            world.spawnEntity(knight);
                            world.setBlockToAir(pos);
                        } else {
                            hasPriority = false;
                        }

                    }
                } else {
                    world.setBlockToAir(pos);
                }


            } else {
                check_others_cooldown = 10;
            }
        }

    }


    public boolean areAllActivated() {
        for(WeakReference<TileEntityLichSpawner> tiles : current_altars) {
            if(tiles == null && !hasPriority && this.getState() == EnumLichSpawner.ACTIVE) {
                world.setBlockToAir(pos);
                return false;
            }
            //returns false if any tiles are inactive
            assert tiles != null;
            if(tiles.get().getState() == EnumLichSpawner.INACTIVE) {
                return false;
            }

        }
        return true;
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
