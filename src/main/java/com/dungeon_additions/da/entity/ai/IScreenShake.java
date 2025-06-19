package com.dungeon_additions.da.entity.ai;

import net.minecraft.entity.Entity;

public interface IScreenShake {
        @Deprecated
        public float getShakeIntensity(Entity viewer, float partialTicks);


        public default float getShakeIntensity(Entity viewer) {
            return this.getShakeIntensity(viewer, 0);
        }
}
