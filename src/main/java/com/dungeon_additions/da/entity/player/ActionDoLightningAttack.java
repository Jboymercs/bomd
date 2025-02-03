package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ActionDoLightningAttack implements IActionPlayer {
    @Override
    public void performAction(EntityPlayer actor) {

        Vec3d playerLookVec = actor.getLookVec();
        for(int i = 1; i < 11; i += 1) {
            Vec3d playerPos = new Vec3d(actor.posX + playerLookVec.x * i, actor.posY, actor.posZ + playerLookVec.z * i);
            EntitySkyBolt bolt = new EntitySkyBolt(actor.world, playerPos.add(0, 10, 0), actor, 0);
                int yVar = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(playerPos.x, actor.posY, playerPos.z), (int) actor.posY - 6, (int) actor.posY + 3);
                bolt.setPosition(playerPos.x, yVar, playerPos.z);
                actor.world.spawnEntity(bolt);
        }

    }


}
