package com.dungeon_additions.da.blocks.lich;

import net.minecraft.util.IStringSerializable;

public enum EnumLichSpawner implements IStringSerializable {


    INACTIVE("inactive", 0, false),
    ACTIVE("active", 10, true);

    private final String name;
    private final int light_level;
    private final boolean is_active;

    @Override
    public String getName() {
        return name;
    }

    EnumLichSpawner(String name, int light_level, boolean is_active) {
        this.name = name;
        this.light_level = light_level;
        this.is_active = is_active;
    }


    public int getLightLevel() {
        return light_level;
    }

    public boolean isActive() {
        return is_active;
    }
}
