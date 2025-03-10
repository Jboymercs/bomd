package com.dungeon_additions.da.entity.ai.sky_dungeon;

import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.ai.flying.AIPassiveCircle;
import com.dungeon_additions.da.entity.ai.flying.IAttackInitiator;
import com.dungeon_additions.da.entity.ai.flying.gargoyle.AIPassiveCircleG;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.entity.sky_dungeon.EntityTridentGargoyle;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;

public class EntityGargoyleTridentAI extends EntityAIBase {
    private final EntityTridentGargoyle entity;
    private final float maxAttackDistSq;
    private final float lookSpeed;
    private final IAttackInitiator attackInitiator;
    private int unseeTime;
    private final AIPassiveCircleG<EntityTridentGargoyle> circleAI;

    private static final int MEMORY = 100;

    public EntityGargoyleTridentAI(EntityTridentGargoyle entity, float maxAttackDistance, float idealAttackDistance, float lookSpeed, IAttackInitiator attackInitiator) {
        this.entity = entity;
        this.maxAttackDistSq = maxAttackDistance * maxAttackDistance;
        this.lookSpeed = lookSpeed;
        this.attackInitiator = attackInitiator;
        circleAI = new AIPassiveCircleG<>(entity, idealAttackDistance);
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
        attackInitiator.resetTask();
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = this.entity.getAttackTarget();

        if (target == null) {
            return;
        }

        double distSq = this.entity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        boolean canSee = this.entity.getEntitySenses().canSee(target);

        // Implements some sort of memory mechanism (can still attack a short while after the enemy isn't seen)
        if (canSee) {
            unseeTime = 0;
        } else {
            unseeTime += 1;
        }

        canSee = canSee || unseeTime < MEMORY;

        move(target, distSq, canSee);

        if (distSq <= this.maxAttackDistSq && canSee) {
            attackInitiator.update(target);
        }
    }

    public void move(EntityLivingBase target, double distSq, boolean canSee) {
        if(!this.entity.lockLook) {
            circleAI.updateTask();
            this.entity.getLookHelper().setLookPositionWithEntity(target, this.lookSpeed, this.lookSpeed);
            this.entity.faceEntity(target, this.lookSpeed, this.lookSpeed);
        }

    }
}
