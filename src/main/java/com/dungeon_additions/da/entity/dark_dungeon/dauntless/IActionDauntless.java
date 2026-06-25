package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiclysm;
import net.minecraft.entity.EntityLivingBase;

public interface IActionDauntless {
    void performAction(EntityDauntless actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
