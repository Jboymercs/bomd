package com.dungeon_additions.da.entity.ai.dark_dungeon;

import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkRoyal;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkSorcerer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIRoyalAttack <T extends EntityDarkRoyal & IAttack> extends EntityAIBase {
    private final T entity;
    private final double moveSpeedAmp;
    private final int attackCooldown;
    private final float maxAttackDistSq;
    private int attackTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    private final float strafeAmount;
    private float lookSpeed;

    private static final float STRAFING_STOP_FACTOR = 0.75f;
    private static final float STRAFING_BACKWARDS_FACTOR = 0.25f;
    private static final float STRAFING_DIRECTION_TICK = 20;
    private static final float STRAFING_DIRECTION_CHANGE_CHANCE = 0.3f;

    public EntityAIRoyalAttack(T entity, double moveSpeedAmp, int attackCooldown, float maxAttackDistance, float strafeAmount) {
        this(entity, moveSpeedAmp, attackCooldown, maxAttackDistance, strafeAmount, 30.0f);
    }

    public EntityAIRoyalAttack(T entity, double moveSpeedAmp, int attackCooldown, float maxAttackDistance, float strafeAmount, float lookSpeed) {
        this.entity = entity;
        this.moveSpeedAmp = moveSpeedAmp;
        this.attackCooldown = attackCooldown;
        this.maxAttackDistSq = maxAttackDistance * maxAttackDistance;
        this.strafeAmount = strafeAmount;
        this.attackTime = attackCooldown;
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

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void updateTask() {
        EntityLivingBase target = this.entity.getAttackTarget();

        if (target == null) {
            return;
        }

        double distSq = this.entity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        boolean canSee = this.entity.getEntitySenses().canSee(target);
        move(target, distSq, canSee);

        if (distSq <= this.maxAttackDistSq && canSee) {
            this.attackTime--;
            if (this.attackTime <= 0) {
                this.attackTime = this.entity.startAttack(target, (float) distSq, this.strafingBackwards);
            }
        }
    }

    public void move(EntityLivingBase target, double distSq, boolean canSee) {
        if(!entity.lockLook) {
            if(this.entity.isHasShield() && !this.entity.isDisabledShield()) {
                if(distSq <= 12 && canSee) {
                    double d0 = (this.entity.posX - target.posX) * 0.009;
                    double d1 = (this.entity.posY - target.posY) * 0.005;
                    double d2 = (this.entity.posZ - target.posZ) * 0.009;
                    this.entity.addVelocity(d0, d1, d2);
                    this.entity.faceEntity(target, 35, 35);
                    this.entity.getLookHelper().setLookPositionWithEntity(target, 35, 35);
                } else {
                    //move to the entity if not in combat range
                    this.entity.getNavigator().tryMoveToEntityLiving(target, this.moveSpeedAmp);
                    this.strafingTime = -1;
                }
            } else {
                if(canSee && distSq < 16) {
                    //when close the gargoyle will engage in melee combat
                    this.entity.getNavigator().clearPath();
                    this.entity.faceEntity(target, this.lookSpeed, this.lookSpeed);
                    this.strafingTime++;
                } else {
                    //move to the entity if not in combat range
                    this.entity.getNavigator().tryMoveToEntityLiving(target, this.moveSpeedAmp * 1.2);
                    this.strafingTime = -1;
                }
            }

            if (this.strafingTime >= STRAFING_DIRECTION_TICK) {
                if ((double) this.entity.getRNG().nextFloat() < STRAFING_DIRECTION_CHANGE_CHANCE) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double) this.entity.getRNG().nextFloat() < STRAFING_DIRECTION_CHANGE_CHANCE) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (distSq > this.maxAttackDistSq * STRAFING_STOP_FACTOR) {
                    this.strafingBackwards = false;
                } else if (distSq < this.maxAttackDistSq * STRAFING_BACKWARDS_FACTOR) {
                    this.strafingBackwards = true;
                }

                this.entity.getMoveHelper().strafe((this.strafingBackwards ? -1 : 1) * this.strafeAmount, (this.strafingClockwise ? 1 : -1) * this.strafeAmount);
                this.entity.faceEntity(target, this.lookSpeed, this.lookSpeed);
            } else {
                this.entity.getLookHelper().setLookPositionWithEntity(target, this.lookSpeed, this.lookSpeed);
            }
        }
    }
}
