package com.dungeon_additions.da.util.damage;

import com.dungeon_additions.da.util.ModColors;
import com.google.common.collect.Maps;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

import java.util.Map;

public enum Element {

    NONE(ModColors.WHITE, ModColors.MAELSTROM, " ",TextFormatting.WHITE, 3);

    public Vec3d sweepColor;
    public Vec3d particleColor;
    public String symbol;
    public TextFormatting textColor;
    public int id;
    private static final Map<Integer, Element> FROM_ID = Maps.<Integer, Element>newHashMap();

    private Element(Vec3d sweepColor, Vec3d particleColor, String symbol, TextFormatting textColor, int id) {
        this.sweepColor = sweepColor;
        this.particleColor = particleColor;
        this.symbol = symbol;
        this.textColor = textColor;
        this.id = id;
    }

    /*
     * A function to determine where elemental effects should be applied given a
     * certain element
     */
    public boolean matchesElement(Element e) {
        return this == e && e != NONE && this != NONE;
    }

    public static Element getElementFromId(int id) {
        return FROM_ID.get(Integer.valueOf(id));
    }

    static {
        for (Element element : values()) {
            FROM_ID.put(Integer.valueOf(element.id), element);
        }
    }
}
