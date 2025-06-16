package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.entity.blossom.EntityAbstractVoidBlossom;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityGrumBlocker extends TileEntity implements ITickable {

    private int doMiningTick = 0;
    @Override
    public void update() {

        doMiningTick++;

        if(!world.isRemote && doMiningTick > 400) {
            AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, box.grow(18D), e -> !e.getIsInvulnerable());
            if(!nearbyPlayers.isEmpty()) {
                for(EntityPlayer player : nearbyPlayers) {
                    player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 420, 1, false, false));
                }
            }

            doMiningTick = 0;
        }
    }


    public void doExplodeBlock() {
        if(!world.isRemote) {
            doMiningTick = 0;
            //removes Mining Fatigue
            AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, box.grow(18D), e -> !e.getIsInvulnerable());
            if(!nearbyPlayers.isEmpty()) {
                for(EntityPlayer player : nearbyPlayers) {
                    if(player.isPotionActive(MobEffects.MINING_FATIGUE)) {
                        player.removePotionEffect(MobEffects.MINING_FATIGUE);
                    }
                }
            }
            //removes the door blocks
            for(int i = 0; i <= 16; i++) {
               BlockPos setTooPos = ModUtils.searchForBlocks(box.grow(5), world, ModBlocks.PUZZLE_DOOR.getDefaultState());
                if(setTooPos != null) {
                    world.setBlockToAir(setTooPos);
                }
            }
            world.setBlockToAir(pos);
        }
    }
}
