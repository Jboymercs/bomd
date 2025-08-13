package com.dungeon_additions.da.blocks.boss;

import net.minecraft.util.IStringSerializable;

public enum BlockEnumBossSummonState implements IStringSerializable {

    INACTIVE("inactive", false),
    FALLEN_STORMVIER("fallen", true),
    VOID_BLOSSOM("void", true),
    ANCIENT_WYRK("ancient", true),
    FLAME_KNIGHT("flame", true),
    NIGHT_LICH("night", true),
    HIGH_KING("high", true),

    OBSIDILITH("obsidian", true);


    private final String name;
    private boolean isActive;

    @Override
    public String getName() {
        return name;
    }

    BlockEnumBossSummonState(String name, boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }
}
