package com.dungeon_additions.da.items.util;

public enum EnumDartType {
    Golden(0, "golden"), Poison(1, "poison"), Farum(2, "farum");

    public int meta;

    public String name;

    EnumDartType(int meta, String name)
    {
        this.meta = meta;
        this.name = name;
    }

    public static EnumDartType getType(int meta)
    {
        return meta == 1 ? Poison :  meta == 2 ? Farum : Golden;
    }

    public int getMeta()
    {
        return this.meta;
    }

    public String toString()
    {
        return this.name;
    }
}
