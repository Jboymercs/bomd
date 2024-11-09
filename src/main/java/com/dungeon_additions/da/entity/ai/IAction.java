package com.dungeon_additions.da.entity.ai;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import net.minecraft.entity.EntityLivingBase;

public interface IAction {
    void performAction(EntityAbstractBase actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
