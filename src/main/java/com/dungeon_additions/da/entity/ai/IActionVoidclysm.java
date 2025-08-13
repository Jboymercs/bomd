package com.dungeon_additions.da.entity.ai;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiclysm;
import net.minecraft.entity.EntityLivingBase;

public interface IActionVoidclysm {

    void performAction(EntityVoidiclysm actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
