package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public interface IActionPlayer {

    void performAction(EntityPlayer actor);

    IAction NONE = (actor, target) -> {
    };
}
