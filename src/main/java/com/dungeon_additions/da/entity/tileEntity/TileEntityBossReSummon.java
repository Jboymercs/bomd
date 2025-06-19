package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.blocks.boss.BlockBossReSummon;
import com.dungeon_additions.da.blocks.boss.BlockEnumBossSummonState;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.frost_dungeon.EntityGreatWyrk;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightBoss;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;

public class TileEntityBossReSummon extends TileEntity implements ITickable {

    private BlockEnumBossSummonState state = BlockEnumBossSummonState.INACTIVE;

    protected int timesUsed;

    private int inactive_cooldown;

    private String boss;


    @Override
    public void update() {
        if(world == null) {
            return;
        }

        if(inactive_cooldown == 0) {
            inactive_cooldown = (int) (ModConfig.boss_resummon_cooldown * 60) * 20;
        }


        if(inactive_cooldown > 1) {
            inactive_cooldown--;
        }

        if(inactive_cooldown < 3 && !world.isRemote) {
            //sets the new state of the block to reflect what boss id has been put into it
            if(boss == null) {
                boss = "ancient_fallen";
                this.setState(BlockEnumBossSummonState.FALLEN_STORMVIER);
            } else if (boss.equals("ancient_fallen")) {
                this.setState(BlockEnumBossSummonState.FALLEN_STORMVIER);
            } else if (boss.equals("void_blossom")) {
                this.setState(BlockEnumBossSummonState.VOID_BLOSSOM);
            } else if (boss.equals("great_wyrk")) {
                this.setState(BlockEnumBossSummonState.ANCIENT_WYRK);
            } else if (boss.equals("flame_knight")) {
                this.setState(BlockEnumBossSummonState.FLAME_KNIGHT);
            } else if (boss.equals("night_lich")) {
                this.setState(BlockEnumBossSummonState.NIGHT_LICH);
            } else if (boss.equals("high_king")) {
                this.setState(BlockEnumBossSummonState.HIGH_KING);
            }
        }


    }


    public void setState(BlockEnumBossSummonState state) {
        this.state = state;
        markDirty();
    }

    public void setState(BlockEnumBossSummonState state, int timesUsed, String boss_name) {
        this.state = state;
        this.timesUsed = timesUsed;
        this.boss = boss_name;
        markDirty();
    }

    public int getTimesUsed() {
        return this.timesUsed;
    }

    public BlockEnumBossSummonState getState() {
        return state;
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("state", (byte)state.ordinal());
        compound.setInteger("times_used", this.timesUsed);
        compound.setString("boss_spawned", this.boss);
        compound.setInteger("cooldown", this.inactive_cooldown);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey("state", 1)) state = BlockEnumBossSummonState.values()[compound.getByte("state")];
        this.timesUsed = compound.getInteger("times_used");
        this.inactive_cooldown = compound.getInteger("cooldown");
        this.boss = compound.getString("boss_spawned");
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("state", (byte)state.ordinal());
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        if (nbt.hasKey("state", 1)) state = BlockEnumBossSummonState.values()[nbt.getByte("state")];

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
