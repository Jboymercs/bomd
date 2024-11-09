package com.dungeon_additions.da.util.interfaces;

import com.dungeon_additions.da.event.EventScheduler;
import net.minecraft.world.World;

public interface IModUtilsHandler {
    EventScheduler getLevelEventScheduler(World world);
}
