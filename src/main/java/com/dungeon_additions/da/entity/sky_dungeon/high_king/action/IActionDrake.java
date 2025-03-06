package com.dungeon_additions.da.entity.sky_dungeon.high_king.action;

import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import net.minecraft.entity.EntityLivingBase;

public interface IActionDrake {
    void performAction(EntityHighKingDrake actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
