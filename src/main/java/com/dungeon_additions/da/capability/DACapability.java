package com.dungeon_additions.da.capability;

import com.dungeon_additions.da.event.EventScheduler;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class DACapability {
    public DACapability() {

    }

    public static EventScheduler getLevelEventScheduler(World world) {
        return new EventScheduler();
    }


}
