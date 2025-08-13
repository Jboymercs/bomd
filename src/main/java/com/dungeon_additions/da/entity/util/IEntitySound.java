package com.dungeon_additions.da.entity.util;

import com.dungeon_additions.da.Main;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;

public interface IEntitySound {

    @Nullable
    public SoundEvent getBossMusic();

    default void playMusic(Entity entity) {
        if (getBossMusic() != null)
            Main.proxy.playMusic(getBossMusic(), entity);
    }
}
