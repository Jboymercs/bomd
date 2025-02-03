package com.dungeon_additions.da.entity.sky_dungeon.city_knights;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ActionHalberdSpecial implements IAction {


    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        EntitySkyBolt bolt = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2,10,-2))));
        Vec3d relPos1 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 0, -2)));
        int yVar = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos1.x, relPos1.y, relPos1.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt.setPosition(relPos1.x, yVar, relPos1.z);
        actor.world.spawnEntity(bolt);
        //2
        EntitySkyBolt bolt2 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(3,10,-2.5))));
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(3, 0, -2.5)));
        int yVar2 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos2.x, relPos2.y, relPos2.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt2.setPosition(relPos2.x, yVar2, relPos2.z);
        actor.world.spawnEntity(bolt2);
        //3
        EntitySkyBolt bolt3 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4,10,-3))));
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4, 0, -3)));
        int yVar3 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos3.x, relPos3.y, relPos3.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt3.setPosition(relPos3.x, yVar3, relPos3.z);
        actor.world.spawnEntity(bolt3);
        //4
        EntitySkyBolt bolt4 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(5,10,-3.5))));
        Vec3d relPos4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(5, 0, -3.5)));
        int yVar4 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos4.x, relPos4.y, relPos4.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt4.setPosition(relPos4.x, yVar4, relPos4.z);
        actor.world.spawnEntity(bolt4);
        //5
        EntitySkyBolt bolt5 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2,10,2))));
        Vec3d relPos5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 0, 2)));
        int yVar5 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt5.setPosition(relPos5.x, yVar5, relPos5.z);
        actor.world.spawnEntity(bolt5);
        //6
        EntitySkyBolt bolt6 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(3,10,2.5))));
        Vec3d relPos6 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(3, 0, 2.5)));
        int yVar6 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos6.x, relPos6.y, relPos6.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt6.setPosition(relPos6.x, yVar6, relPos6.z);
        actor.world.spawnEntity(bolt6);
        //7
        EntitySkyBolt bolt7 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4,10,3))));
        Vec3d relPos7 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4, 0, 3)));
        int yVar7 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos7.x, relPos7.y, relPos7.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt7.setPosition(relPos7.x, yVar7, relPos7.z);
        actor.world.spawnEntity(bolt7);
        //8
        EntitySkyBolt bolt8 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(5,10,3.5))));
        Vec3d relPos8 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(5, 0, 3.5)));
        int yVar8 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos8.x, relPos8.y, relPos8.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt8.setPosition(relPos8.x, yVar8, relPos8.z);
        actor.world.spawnEntity(bolt8);
        //9
        EntitySkyBolt bolt9 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(6,10,-4))));
        Vec3d relPos9 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(6, 0, -4)));
        int yVar9 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos9.x, relPos9.y, relPos9.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt9.setPosition(relPos9.x, yVar9, relPos9.z);
        actor.world.spawnEntity(bolt9);
        //10
        EntitySkyBolt bolt10 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(6,10,4))));
        Vec3d relPos10 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(6, 0, 4)));
        int yVar10 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos10.x, relPos10.y, relPos10.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt10.setPosition(relPos10.x, yVar10, relPos10.z);
        actor.world.spawnEntity(bolt10);

        actor.addEvent(()-> doLineAction(actor), 5);
    }


    private void doLineAction(EntityLivingBase actor) {
        EntitySkyBolt bolt5 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0,10,0))));
        Vec3d relPos5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 0, 0)));
        int yVar5 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt5.setPosition(relPos5.x, yVar5, relPos5.z);
        actor.world.spawnEntity(bolt5);
        //6
        EntitySkyBolt bolt6 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0,10,0))));
        Vec3d relPos6 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(3, 0, 0)));
        int yVar6 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos6.x, relPos6.y, relPos6.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt6.setPosition(relPos6.x, yVar6, relPos6.z);
        actor.world.spawnEntity(bolt6);
        //7
        EntitySkyBolt bolt7 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0,10,0))));
        Vec3d relPos7 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4, 0, 0)));
        int yVar7 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos7.x, relPos7.y, relPos7.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt7.setPosition(relPos7.x, yVar7, relPos7.z);
        actor.world.spawnEntity(bolt7);
        //8
        EntitySkyBolt bolt8 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0,10,0))));
        Vec3d relPos8 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(5, 0, 0)));
        int yVar8 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos8.x, relPos8.y, relPos8.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt8.setPosition(relPos8.x, yVar8, relPos8.z);
        actor.world.spawnEntity(bolt8);
        //9
        EntitySkyBolt bolt9 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0,10,0))));
        Vec3d relPos9 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(6, 0, 0)));
        int yVar9 = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(relPos9.x, relPos9.y, relPos9.z), (int) actor.posY - 4, (int) actor.posY + 3);
        bolt9.setPosition(relPos9.x, yVar9, relPos9.z);
        actor.world.spawnEntity(bolt9);
    }
}
