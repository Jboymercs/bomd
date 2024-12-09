package com.dungeon_additions.da.entity.ai.flying;

import com.dungeon_additions.da.entity.ai.IAttack;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

public class TimedAttackInitiator<T extends EntityLiving & IAttack> implements IAttackInitiator{
    private final T entity;
    private int attackTime;
    private final int attackCooldown;

    public TimedAttackInitiator(T entity, int startingCooldown) {
        this.entity = entity;
        this.attackTime = startingCooldown;
        this.attackCooldown = startingCooldown;
    }

    @Override
    public void update(EntityLivingBase target) {
        this.attackTime--;
        if (this.attackTime <= 0) {
            double distSq = entity.getDistanceSq(target);
            this.attackTime = this.entity.startAttack(target, (float) distSq, false);
        }
    }

    @Override
    public void resetTask() {
        this.attackTime = Math.max(attackTime, attackCooldown);
    }
}
