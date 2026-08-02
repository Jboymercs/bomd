package com.dungeon_additions.da.blocks.aspect_forge;

import net.minecraft.util.IStringSerializable;

public enum BlockEnumAspectForge implements IStringSerializable {

    BASE("base"),
    SWORD("aspect_sword"),
    COLOSSAL("aspect_colossal"),
    SPEAR("aspect_spear"),
    DAGGER("aspect_dagger"),
    SHIELD("aspect_shield"),
    BOW("aspect_bow"),
    DUELIST("aspect_duelist"),
    MAGE("aspect_mage");



    private final String name;

    @Override
    public String getName() {
        return name;
    }

    BlockEnumAspectForge(String name) {
        this.name = name;
    }
}
