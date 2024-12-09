package com.dungeon_additions.da.entity.night_lich.action;


import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.night_lich.EntityAbstractNightLich;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import net.minecraft.entity.EntityLivingBase;

public interface IActionLich {
    void performAction(EntityNightLich actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
