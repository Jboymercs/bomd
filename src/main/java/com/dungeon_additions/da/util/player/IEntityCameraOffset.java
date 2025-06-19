package com.dungeon_additions.da.util.player;

import net.minecraft.entity.Entity;

public interface IEntityCameraOffset {
    public boolean applyOffset(Entity view, float partialTicks);
}
