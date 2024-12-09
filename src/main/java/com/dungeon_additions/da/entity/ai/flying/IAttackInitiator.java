package com.dungeon_additions.da.entity.ai.flying;

import net.minecraft.entity.EntityLivingBase;

public interface IAttackInitiator {

    void update(EntityLivingBase target);
    void resetTask();
}
