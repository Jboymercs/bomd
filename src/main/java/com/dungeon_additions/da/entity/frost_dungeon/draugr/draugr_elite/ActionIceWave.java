package com.dungeon_additions.da.entity.frost_dungeon.draugr.draugr_elite;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.frost_dungeon.EntityIcicleSpike;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionIceWave implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        EntityIcicleSpike tile = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_2 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_3 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_4 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_5 = new EntityIcicleSpike(actor.world);

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2,0,1)));
        Vec3d relPos_2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2,0,0)));
        Vec3d relPos_3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2,0,-1)));
        Vec3d relPos_4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1,0,2)));
        Vec3d relPos_5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1,0,-2)));

        int y = getSurfaceHeight(actor.world, new BlockPos(relPos.x, 0, relPos.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo = new BlockPos(relPos.x, y, relPos.z);
        tile.setLocationAndAngles(posToo.getX(), posToo.getY() + 1, posToo.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile);

        int y2 = getSurfaceHeight(actor.world, new BlockPos(relPos_2.x, 0, relPos_2.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo2 = new BlockPos(relPos_2.x, y2, relPos_2.z);
        tile_2.setLocationAndAngles(posToo2.getX(), posToo2.getY() + 1, posToo2.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_2);

        int y3 = getSurfaceHeight(actor.world, new BlockPos(relPos_3.x, 0, relPos_3.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo3 = new BlockPos(relPos_3.x, y3, relPos_3.z);
        tile_3.setLocationAndAngles(posToo3.getX(), posToo3.getY() + 1, posToo3.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_3);

        int y4 = getSurfaceHeight(actor.world, new BlockPos(relPos_4.x, 0, relPos_4.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo4 = new BlockPos(relPos_4.x, y4, relPos_4.z);
        tile_4.setLocationAndAngles(posToo4.getX(), posToo4.getY() + 1, posToo4.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_4);

        int y5 = getSurfaceHeight(actor.world, new BlockPos(relPos_5.x, 0, relPos_5.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo5 = new BlockPos(relPos_5.x, y5, relPos_5.z);
        tile_5.setLocationAndAngles(posToo5.getX(), posToo5.getY() + 1, posToo5.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_5);

        actor.addEvent(()-> this.doSecondWave(actor), 5);
        actor.addEvent(()-> this.doThirdWave(actor), 5);
        actor.addEvent(()-> this.doFourthWave(actor), 10);
        actor.addEvent(()-> this.doLastWave(actor), 10);
        actor.addEvent(()-> this.doBOneWave(actor), 15);
        actor.addEvent(()-> this.doBTwoWave(actor), 15);
        actor.addEvent(()-> this.doBThreeWave(actor), 20);
    }

    private void doSecondWave(EntityLivingBase actor) {
        EntityIcicleSpike tile = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_2 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_3 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_4 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_5 = new EntityIcicleSpike(actor.world);

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(3,0,1)));
        Vec3d relPos_2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(3,0,0)));
        Vec3d relPos_3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(3,0,-1)));
        Vec3d relPos_4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(3,0,2)));
        Vec3d relPos_5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(3,0,-2)));

        int y = getSurfaceHeight(actor.world, new BlockPos(relPos.x, 0, relPos.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo = new BlockPos(relPos.x, y, relPos.z);
        tile.setLocationAndAngles(posToo.getX(), posToo.getY() + 1, posToo.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile);

        int y2 = getSurfaceHeight(actor.world, new BlockPos(relPos_2.x, 0, relPos_2.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo2 = new BlockPos(relPos_2.x, y2, relPos_2.z);
        tile_2.setLocationAndAngles(posToo2.getX(), posToo2.getY() + 1, posToo2.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_2);

        int y3 = getSurfaceHeight(actor.world, new BlockPos(relPos_3.x, 0, relPos_3.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo3 = new BlockPos(relPos_3.x, y3, relPos_3.z);
        tile_3.setLocationAndAngles(posToo3.getX(), posToo3.getY() + 1, posToo3.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_3);

        int y4 = getSurfaceHeight(actor.world, new BlockPos(relPos_4.x, 0, relPos_4.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo4 = new BlockPos(relPos_4.x, y4, relPos_4.z);
        tile_4.setLocationAndAngles(posToo4.getX(), posToo4.getY() + 1, posToo4.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_4);

        int y5 = getSurfaceHeight(actor.world, new BlockPos(relPos_5.x, 0, relPos_5.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo5 = new BlockPos(relPos_5.x, y5, relPos_5.z);
        tile_5.setLocationAndAngles(posToo5.getX(), posToo5.getY() + 1, posToo5.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_5);
    }

    private void doThirdWave(EntityLivingBase actor) {
        EntityIcicleSpike tile = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_2 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_3 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_4 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_5 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_6 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_7 = new EntityIcicleSpike(actor.world);

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4,0,1)));
        Vec3d relPos_2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4,0,0)));
        Vec3d relPos_3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4,0,-1)));
        Vec3d relPos_4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4,0,2)));
        Vec3d relPos_5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4,0,-2)));
        Vec3d relPos_6 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4,0,3)));
        Vec3d relPos_7 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4,0,-3)));

        int y = getSurfaceHeight(actor.world, new BlockPos(relPos.x, 0, relPos.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo = new BlockPos(relPos.x, y, relPos.z);
        tile.setLocationAndAngles(posToo.getX(), posToo.getY() + 1, posToo.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile);

        int y2 = getSurfaceHeight(actor.world, new BlockPos(relPos_2.x, 0, relPos_2.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo2 = new BlockPos(relPos_2.x, y2, relPos_2.z);
        tile_2.setLocationAndAngles(posToo2.getX(), posToo2.getY() + 1, posToo2.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_2);

        int y3 = getSurfaceHeight(actor.world, new BlockPos(relPos_3.x, 0, relPos_3.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo3 = new BlockPos(relPos_3.x, y3, relPos_3.z);
        tile_3.setLocationAndAngles(posToo3.getX(), posToo3.getY() + 1, posToo3.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_3);

        int y4 = getSurfaceHeight(actor.world, new BlockPos(relPos_4.x, 0, relPos_4.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo4 = new BlockPos(relPos_4.x, y4, relPos_4.z);
        tile_4.setLocationAndAngles(posToo4.getX(), posToo4.getY() + 1, posToo4.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_4);

        int y5 = getSurfaceHeight(actor.world, new BlockPos(relPos_5.x, 0, relPos_5.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo5 = new BlockPos(relPos_5.x, y5, relPos_5.z);
        tile_5.setLocationAndAngles(posToo5.getX(), posToo5.getY() + 1, posToo5.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_5);

        int y6 = getSurfaceHeight(actor.world, new BlockPos(relPos_6.x, 0, relPos_6.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo6 = new BlockPos(relPos_6.x, y6, relPos_6.z);
        tile_6.setLocationAndAngles(posToo6.getX(), posToo6.getY() + 1, posToo6.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_6);

        int y7 = getSurfaceHeight(actor.world, new BlockPos(relPos_7.x, 0, relPos_7.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo7 = new BlockPos(relPos_7.x, y7, relPos_7.z);
        tile_7.setLocationAndAngles(posToo7.getX(), posToo7.getY() + 1, posToo7.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_7);
    }

    private void doFourthWave(EntityLivingBase actor) {
        EntityIcicleSpike tile = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_2 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_3 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_4 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_5 = new EntityIcicleSpike(actor.world);

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(5,0,1)));
        Vec3d relPos_2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(5,0,0)));
        Vec3d relPos_3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(5,0,-1)));
        Vec3d relPos_4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(5,0,2)));
        Vec3d relPos_5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(5,0,-2)));

        int y = getSurfaceHeight(actor.world, new BlockPos(relPos.x, 0, relPos.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo = new BlockPos(relPos.x, y, relPos.z);
        tile.setLocationAndAngles(posToo.getX(), posToo.getY() + 1, posToo.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile);

        int y2 = getSurfaceHeight(actor.world, new BlockPos(relPos_2.x, 0, relPos_2.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo2 = new BlockPos(relPos_2.x, y2, relPos_2.z);
        tile_2.setLocationAndAngles(posToo2.getX(), posToo2.getY() + 1, posToo2.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_2);

        int y3 = getSurfaceHeight(actor.world, new BlockPos(relPos_3.x, 0, relPos_3.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo3 = new BlockPos(relPos_3.x, y3, relPos_3.z);
        tile_3.setLocationAndAngles(posToo3.getX(), posToo3.getY() + 1, posToo3.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_3);

        int y4 = getSurfaceHeight(actor.world, new BlockPos(relPos_4.x, 0, relPos_4.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo4 = new BlockPos(relPos_4.x, y4, relPos_4.z);
        tile_4.setLocationAndAngles(posToo4.getX(), posToo4.getY() + 1, posToo4.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_4);

        int y5 = getSurfaceHeight(actor.world, new BlockPos(relPos_5.x, 0, relPos_5.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo5 = new BlockPos(relPos_5.x, y5, relPos_5.z);
        tile_5.setLocationAndAngles(posToo5.getX(), posToo5.getY() + 1, posToo5.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_5);
    }

    private void doLastWave(EntityLivingBase actor) {
        EntityIcicleSpike tile = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_2 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_3 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_4 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_5 = new EntityIcicleSpike(actor.world);

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(6,0,1)));
        Vec3d relPos_2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(6,0,0)));
        Vec3d relPos_3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(6,0,-1)));
        Vec3d relPos_4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(6,0,2)));
        Vec3d relPos_5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(6,0,-2)));

        int y = getSurfaceHeight(actor.world, new BlockPos(relPos.x, 0, relPos.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo = new BlockPos(relPos.x, y, relPos.z);
        tile.setLocationAndAngles(posToo.getX(), posToo.getY() + 1, posToo.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile);

        int y2 = getSurfaceHeight(actor.world, new BlockPos(relPos_2.x, 0, relPos_2.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo2 = new BlockPos(relPos_2.x, y2, relPos_2.z);
        tile_2.setLocationAndAngles(posToo2.getX(), posToo2.getY() + 1, posToo2.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_2);

        int y3 = getSurfaceHeight(actor.world, new BlockPos(relPos_3.x, 0, relPos_3.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo3 = new BlockPos(relPos_3.x, y3, relPos_3.z);
        tile_3.setLocationAndAngles(posToo3.getX(), posToo3.getY() + 1, posToo3.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_3);

        int y4 = getSurfaceHeight(actor.world, new BlockPos(relPos_4.x, 0, relPos_4.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo4 = new BlockPos(relPos_4.x, y4, relPos_4.z);
        tile_4.setLocationAndAngles(posToo4.getX(), posToo4.getY() + 1, posToo4.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_4);

        int y5 = getSurfaceHeight(actor.world, new BlockPos(relPos_5.x, 0, relPos_5.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo5 = new BlockPos(relPos_5.x, y5, relPos_5.z);
        tile_5.setLocationAndAngles(posToo5.getX(), posToo5.getY() + 1, posToo5.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_5);
    }

    private void doBOneWave(EntityLivingBase actor) {
        EntityIcicleSpike tile = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_2 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_3 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_4 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_5 = new EntityIcicleSpike(actor.world);

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(7,0,1)));
        Vec3d relPos_2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(7,0,0)));
        Vec3d relPos_3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(7,0,-1)));
        Vec3d relPos_4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(7,0,2)));
        Vec3d relPos_5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(7,0,-2)));

        int y = getSurfaceHeight(actor.world, new BlockPos(relPos.x, 0, relPos.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo = new BlockPos(relPos.x, y, relPos.z);
        tile.setLocationAndAngles(posToo.getX(), posToo.getY() + 1, posToo.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile);

        int y2 = getSurfaceHeight(actor.world, new BlockPos(relPos_2.x, 0, relPos_2.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo2 = new BlockPos(relPos_2.x, y2, relPos_2.z);
        tile_2.setLocationAndAngles(posToo2.getX(), posToo2.getY() + 1, posToo2.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_2);

        int y3 = getSurfaceHeight(actor.world, new BlockPos(relPos_3.x, 0, relPos_3.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo3 = new BlockPos(relPos_3.x, y3, relPos_3.z);
        tile_3.setLocationAndAngles(posToo3.getX(), posToo3.getY() + 1, posToo3.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_3);

        int y4 = getSurfaceHeight(actor.world, new BlockPos(relPos_4.x, 0, relPos_4.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo4 = new BlockPos(relPos_4.x, y4, relPos_4.z);
        tile_4.setLocationAndAngles(posToo4.getX(), posToo4.getY() + 1, posToo4.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_4);

        int y5 = getSurfaceHeight(actor.world, new BlockPos(relPos_5.x, 0, relPos_5.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo5 = new BlockPos(relPos_5.x, y5, relPos_5.z);
        tile_5.setLocationAndAngles(posToo5.getX(), posToo5.getY() + 1, posToo5.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_5);
    }

    private void doBTwoWave(EntityLivingBase actor) {
        EntityIcicleSpike tile = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_2 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_3 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_4 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_5 = new EntityIcicleSpike(actor.world);

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(8,0,1)));
        Vec3d relPos_2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(8,0,0)));
        Vec3d relPos_3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(8,0,-1)));
        Vec3d relPos_4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(8,0,2)));
        Vec3d relPos_5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(8,0,-2)));

        int y = getSurfaceHeight(actor.world, new BlockPos(relPos.x, 0, relPos.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo = new BlockPos(relPos.x, y, relPos.z);
        tile.setLocationAndAngles(posToo.getX(), posToo.getY() + 1, posToo.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile);

        int y2 = getSurfaceHeight(actor.world, new BlockPos(relPos_2.x, 0, relPos_2.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo2 = new BlockPos(relPos_2.x, y2, relPos_2.z);
        tile_2.setLocationAndAngles(posToo2.getX(), posToo2.getY() + 1, posToo2.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_2);

        int y3 = getSurfaceHeight(actor.world, new BlockPos(relPos_3.x, 0, relPos_3.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo3 = new BlockPos(relPos_3.x, y3, relPos_3.z);
        tile_3.setLocationAndAngles(posToo3.getX(), posToo3.getY() + 1, posToo3.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_3);

        int y4 = getSurfaceHeight(actor.world, new BlockPos(relPos_4.x, 0, relPos_4.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo4 = new BlockPos(relPos_4.x, y4, relPos_4.z);
        tile_4.setLocationAndAngles(posToo4.getX(), posToo4.getY() + 1, posToo4.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_4);

        int y5 = getSurfaceHeight(actor.world, new BlockPos(relPos_5.x, 0, relPos_5.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo5 = new BlockPos(relPos_5.x, y5, relPos_5.z);
        tile_5.setLocationAndAngles(posToo5.getX(), posToo5.getY() + 1, posToo5.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_5);
    }

    private void doBThreeWave(EntityLivingBase actor) {
        EntityIcicleSpike tile = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_2 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_3 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_4 = new EntityIcicleSpike(actor.world);
        EntityIcicleSpike tile_5 = new EntityIcicleSpike(actor.world);

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(9,0,1)));
        Vec3d relPos_2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(9,0,0)));
        Vec3d relPos_3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(9,0,-1)));
        Vec3d relPos_4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(9,0,2)));
        Vec3d relPos_5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(9,0,-2)));

        int y = getSurfaceHeight(actor.world, new BlockPos(relPos.x, 0, relPos.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo = new BlockPos(relPos.x, y, relPos.z);
        tile.setLocationAndAngles(posToo.getX(), posToo.getY() + 1, posToo.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile);

        int y2 = getSurfaceHeight(actor.world, new BlockPos(relPos_2.x, 0, relPos_2.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo2 = new BlockPos(relPos_2.x, y2, relPos_2.z);
        tile_2.setLocationAndAngles(posToo2.getX(), posToo2.getY() + 1, posToo2.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_2);

        int y3 = getSurfaceHeight(actor.world, new BlockPos(relPos_3.x, 0, relPos_3.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo3 = new BlockPos(relPos_3.x, y3, relPos_3.z);
        tile_3.setLocationAndAngles(posToo3.getX(), posToo3.getY() + 1, posToo3.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_3);

        int y4 = getSurfaceHeight(actor.world, new BlockPos(relPos_4.x, 0, relPos_4.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo4 = new BlockPos(relPos_4.x, y4, relPos_4.z);
        tile_4.setLocationAndAngles(posToo4.getX(), posToo4.getY() + 1, posToo4.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_4);

        int y5 = getSurfaceHeight(actor.world, new BlockPos(relPos_5.x, 0, relPos_5.z), (int) actor.posY - 3, (int) actor.posY + 2);
        BlockPos posToo5 = new BlockPos(relPos_5.x, y5, relPos_5.z);
        tile_5.setLocationAndAngles(posToo5.getX(), posToo5.getY() + 1, posToo5.getZ(), 0.0f, 0.0F);
        actor.world.spawnEntity(tile_5);
    }


    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.isBlockFullCube(pos.add(0, currentY, 0))) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
