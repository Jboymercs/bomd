package com.dungeon_additions.da.entity.sky_dungeon.high_king.king.action;

import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import net.minecraft.entity.EntityLivingBase;

public interface IActionKing {
    void performAction(EntityHighKing actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
