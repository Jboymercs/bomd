package com.dungeon_additions.da.animation.item;

import net.minecraft.util.IStringSerializable;

public enum EnumWeaponType implements IStringSerializable {
    PARRY_SWORD("parry_sword"),
    HEAVY_AXE("heavy_axe"),
    HEAVY_SWORD("heavy_sword"),
    SPEAR("spear"),
    DAGGER("dagger"),
    SWORD("sword");

    private final String name;

    EnumWeaponType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
