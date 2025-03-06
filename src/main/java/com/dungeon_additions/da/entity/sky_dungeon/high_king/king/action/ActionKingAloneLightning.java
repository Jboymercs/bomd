package com.dungeon_additions.da.entity.sky_dungeon.high_king.king.action;

import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ActionKingAloneLightning implements IActionKing{
    @Override
    public void performAction(EntityHighKing actor, EntityLivingBase target) {
        //King Spawn Lightning Bolt
        Vec3d setPos = actor.getPositionVector();
        EntityLightningBolt lightningBolt = new EntityLightningBolt(actor.world, setPos.x, setPos.y, setPos.z, true);
        actor.world.spawnEntity(lightningBolt);


        for(int i = 10; i < 60; i += 20) {
            actor.addEvent(()-> {
                EntitySkyBolt bolt = new EntitySkyBolt(actor.world, new Vec3d(target.posX + ModRand.getFloat(1.5F), target.posY + 12, target.posZ + ModRand.getFloat(1.5F)));
                EntitySkyBolt bolt2 = new EntitySkyBolt(actor.world, new Vec3d(target.posX + ModRand.getFloat(1.5F) + 1, target.posY + 12, target.posZ + ModRand.getFloat(1.5F)));
                EntitySkyBolt bolt3 = new EntitySkyBolt(actor.world, new Vec3d(target.posX + ModRand.getFloat(1.5F) - 1, target.posY + 12, target.posZ + ModRand.getFloat(1.5F)));
                EntitySkyBolt bolt4 = new EntitySkyBolt(actor.world, new Vec3d(target.posX + ModRand.getFloat(1.5F), target.posY + 12, target.posZ + ModRand.getFloat(1.5F) + 1));
                EntitySkyBolt bolt5 = new EntitySkyBolt(actor.world, new Vec3d(target.posX + ModRand.getFloat(1.5F), target.posY + 12, target.posZ + ModRand.getFloat(1.5F) - 1));
                Vec3d pos = target.getPositionVector();
                int yVar = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(pos.x, pos.y, pos.z), (int) pos.y - 5, (int) pos.y + 2);
                if(yVar != pos.y) {
                    bolt.setPosition(pos.x, yVar + 1, pos.z);
                    bolt2.setPosition(pos.x + 1.35, yVar + 1, pos.z + 0.5);
                    bolt3.setPosition(pos.x - 1.35, yVar + 1, pos.z - 0.5);
                    bolt4.setPosition(pos.x - 0.5, yVar + 1, pos.z + 1.35);
                    bolt5.setPosition(pos.x + 0.5, yVar + 1, pos.z - 1.35);
                } else {
                    bolt.setPosition(pos.x, yVar, pos.z);
                    bolt2.setPosition(pos.x + 1.35, yVar, pos.z);
                    bolt3.setPosition(pos.x - 1.35, yVar, pos.z);
                    bolt4.setPosition(pos.x, yVar, pos.z + 1.35);
                    bolt5.setPosition(pos.x, yVar, pos.z - 1.35);
                }
                actor.world.spawnEntity(bolt);
                actor.world.spawnEntity(bolt2);
                actor.world.spawnEntity(bolt3);
                actor.world.spawnEntity(bolt4);
                actor.world.spawnEntity(bolt5);
            }, i);
        }
    }
}
