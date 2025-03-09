package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityKingHolyAOE;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionSummonHolySpikes implements IActionPlayer{
    @Override
    public void performAction(EntityPlayer actor) {
        Vec3d savedPos = actor.getPositionVector();

            ModUtils.circleCallback(1, 4, (pos) -> {
                pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                EntityKingHolyAOE tile = new EntityKingHolyAOE(actor.world, actor);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 2);
                tile.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(tile);

            });

        ModUtils.circleCallback(2, 8, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
            EntityKingHolyAOE tile = new EntityKingHolyAOE(actor.world, actor);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 2);
            tile.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(tile);

        });

        ModUtils.circleCallback(3, 12, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
            EntityKingHolyAOE tile = new EntityKingHolyAOE(actor.world, actor);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 2);
            tile.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(tile);

        });

        ModUtils.circleCallback(4, 16, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
            EntityKingHolyAOE tile = new EntityKingHolyAOE(actor.world, actor);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 2);
            tile.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(tile);

        });

        ModUtils.circleCallback(5, 8, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
            EntityKingHolyAOE tile = new EntityKingHolyAOE(actor.world, actor);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 2);
            tile.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(tile);

        });

    }


    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
