package com.dungeon_additions.da.entity.ai;

import net.minecraft.entity.EntityLivingBase;

public interface IAttack {

    /**
     * Called when the entity is ready to begin an attack sequence
     *
     * @param target
     * @param distanceSq
     * @param strafingBackwards
     * @return The number of seconds before launching another attack (calling this
     * method again)
     */
    int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards);

}
