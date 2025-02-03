package com.dungeon_additions.da.entity.sky_dungeon.city_knights;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ActionSwordSpecial implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        for(int i = 2; i < 7; i += 1) {
            int finalI = i;
            actor.addEvent(()-> {
                EntitySkyBolt bolt = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI,10,0))));
                Vec3d relPos1 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI, 0, 0)));
                int yVar = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos1.x, relPos1.y, relPos1.z), (int) actor.posY - 4, (int) actor.posY + 3);
                bolt.setPosition(relPos1.x, yVar, relPos1.z);
                actor.world.spawnEntity(bolt);
            }, i * 2);
        }

        for(int i = 2; i < 7; i += 1) {
            int finalI = i;
            actor.addEvent(()-> {
                EntitySkyBolt bolt = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI,10,1))));
                Vec3d relPos1 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI, 0, 1)));
                int yVar = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos1.x, relPos1.y, relPos1.z), (int) actor.posY - 4, (int) actor.posY + 3);
                bolt.setPosition(relPos1.x, yVar, relPos1.z);
                actor.world.spawnEntity(bolt);
            }, i * 2);
        }

        for(int i = 2; i < 7; i += 1) {
            int finalI = i;
            actor.addEvent(()-> {
                EntitySkyBolt bolt = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI,10,-1))));
                Vec3d relPos1 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI, 0, -1)));
                int yVar = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos1.x, relPos1.y, relPos1.z), (int) actor.posY - 4, (int) actor.posY + 3);
                bolt.setPosition(relPos1.x, yVar, relPos1.z);
                actor.world.spawnEntity(bolt);
            }, i * 2);
        }

        for(int i = 0; i < 5; i += 1) {
            int finalI = i;
            actor.addEvent(()-> {
                EntitySkyBolt bolt = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI,10,2))));
                Vec3d relPos1 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI, 0, 2)));
                int yVar = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos1.x, relPos1.y, relPos1.z), (int) actor.posY - 4, (int) actor.posY + 3);
                bolt.setPosition(relPos1.x, yVar, relPos1.z);
                actor.world.spawnEntity(bolt);
            }, i * 2);
        }

        for(int i = 0; i < 5; i += 1) {
            int finalI = i;
            actor.addEvent(()-> {
                EntitySkyBolt bolt = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI,10,-2))));
                Vec3d relPos1 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI, 0, -2)));
                int yVar = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos1.x, relPos1.y, relPos1.z), (int) actor.posY - 4, (int) actor.posY + 3);
                bolt.setPosition(relPos1.x, yVar, relPos1.z);
                actor.world.spawnEntity(bolt);
            }, i * 2);
        }
    }
}
