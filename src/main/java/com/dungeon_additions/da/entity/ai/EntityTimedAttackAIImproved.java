package com.dungeon_additions.da.entity.ai;

import com.dungeon_additions.da.entity.EntityAdvancedMover;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class EntityTimedAttackAIImproved <E extends EntityAdvancedMover & IAttack> extends EntityAIBase {

    private final E entity;

    private final double moveSpeedAmp;

    private final int attackCooldown;

    private final float maxAttackDistanceSq;

    private final float idealAttackDistanceSq;

    private int attackTime;

    private float lookSpeed;

    public EntityTimedAttackAIImproved(E entity, double moveSpeedAmp, int attackCooldown, float idealAttackDistance, float maxAttackDistance) {
        this(entity, moveSpeedAmp, attackCooldown, idealAttackDistance, maxAttackDistance, 30.0f);
    }

    public EntityTimedAttackAIImproved(E entity, double moveSpeedAmp, int attackCooldown, float idealAttackDistance, float maxAttackDistance, float lookSpeed) {
        this.entity = entity;
        this.moveSpeedAmp = moveSpeedAmp;
        this.attackCooldown = attackCooldown;
        this.attackTime = attackCooldown;
        this.idealAttackDistanceSq = idealAttackDistance * idealAttackDistance;
        this.maxAttackDistanceSq = maxAttackDistance * maxAttackDistance;
        this.lookSpeed = lookSpeed;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.getAttackTarget() != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !this.entity.getNavigator().noPath());
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.attackTime = Math.max(attackTime, attackCooldown);
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = this.entity.getAttackTarget();

        if(target == null) {
            return;
        }

        double distSq = this.entity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        boolean canSee = this.entity.getEntitySenses().canSee(target);

        moveController(target, distSq, canSee);

        if (distSq <= this.maxAttackDistanceSq && canSee) {
            this.attackTime--;
            if (this.attackTime <= 0) {
                this.attackTime = this.entity.startAttack(target, (float) distSq, false);
            }
        }
    }



    private int strafeLock = 0;
    private boolean flag;
    private void moveController(EntityLivingBase target, double distSq, boolean canSee) {
        if(!entity.lockLook) {
            Vec3d targetPos = target.getPositionVector();
            Vec3d currentPos = this.entity.getPositionVector();
            double modifiedDist = distSq - 1;
            Vec3d dirChangeOne = targetPos.subtract(currentPos).scale(modifiedDist - maxAttackDistanceSq);
            if (distSq > maxAttackDistanceSq) {
                this.entity.getNavigator().tryMoveToXYZ(dirChangeOne.x, dirChangeOne.y, dirChangeOne.z, moveSpeedAmp);
                this.entity.getLookHelper().setLookPositionWithEntity(target, this.lookSpeed, this.lookSpeed);
            }
            else   {
                Vec3d nextPointToMove = getNextPointInCircle(targetPos.add(0, 1, 0), (int) idealAttackDistanceSq);
                this.entity.getLookHelper().setLookPositionWithEntity(target, this.lookSpeed, this.lookSpeed);

                if (!hasClearPath(nextPointToMove) || lineBlocked(nextPointToMove, targetPos)) {
                    planeVectorPath = getNewPlaneVector();
                    flag = false;
                }

                if(!flag) {
                    this.entity.getNavigator().tryMoveToXYZ(nextPointToMove.x, nextPointToMove.y, nextPointToMove.z, moveSpeedAmp - 0.1);
                    flag = true;
                }
                if(nextPointToMove == this.entity.getPositionVector()) {
                    flag = false;
                }

            }
        }
    }


    private Vec3d getNewPlaneVector() {
        return ModUtils.Y_AXIS.add(ModRand.randVec().scale(2)).normalize();
    }

    private boolean hasClearPath(Vec3d nextPointToFollow) {
        return ModUtils.getBoundingBoxCorners(entity.getEntityBoundingBox()).stream().noneMatch(vec3d -> lineBlocked(vec3d, nextPointToFollow));
    }

    private boolean lineBlocked(Vec3d start, Vec3d nextPointToFollow) {
        RayTraceResult rayTraceResult = entity.world.rayTraceBlocks(start, nextPointToFollow, false, true, false);
        return rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK;
    }
    private @Nullable Vec3d planeVectorPath = getNewPlaneVector();

    private Vec3d getNextPointInCircle(Vec3d center, int radius) {
        Vec3d circlePointInWorld = entity.getPositionVector().add(ModRand.randVec());
        for(int i = 0; i < radius; i++) {
            for(int sign : new int[]{1, -1}) {
                Vec3d entityVelocity = ModUtils.getEntityVelocity(entity);
                Vec3d entityDirection = entity.getPositionVector().subtract(center);
                Vec3d projectedEntityDirection = ModUtils.planeProject(entityDirection, planeVectorPath).normalize().scale(radius + (i * sign));
                Vec3d nextPointOnCircle = ModUtils.rotateVector2(projectedEntityDirection, planeVectorPath, 15 * entityVelocity.length());
                circlePointInWorld = nextPointOnCircle.add(center);
                if (hasClearPath(circlePointInWorld)) {
                    return circlePointInWorld;
                } else {
                    planeVectorPath = getNewPlaneVector();
                }
            }
        }
        return circlePointInWorld;
    }
}
