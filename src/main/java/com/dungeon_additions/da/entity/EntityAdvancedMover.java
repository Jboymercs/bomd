package com.dungeon_additions.da.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityAdvancedMover extends EntityAbstractBase{
    /**
     * A class that will most likely have future impact on entity movement
     * @param worldIn
     */
    //Used for setting the boss into a readyStance when within paramaters
    protected boolean setReadyStance = false;

    public double setWaistLook;
    public int readyStanceDistance = 5;

    private int easingStanceTick = 0;
    public EntityAdvancedMover(World worldIn) {
        super(worldIn);

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        EntityLivingBase target = this.getAttackTarget();

        if(target != null) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);

            if(distance <= readyStanceDistance + 1) {
                setReadyStance = true;
            }

            if(distance > readyStanceDistance + 1 && easingStanceTick > 20) {
                setReadyStance = false;
                easingStanceTick = 0;
            } else {
                easingStanceTick++;
            }



        }
    }








    public static int easeAxisControl(int current, int goal) {
        return 0;
    }


}
