package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.model.ModelVoidSpike;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderVoidSpike extends RenderAbstractGeoEntity<EntityVoidSpike>{

    public RenderVoidSpike(RenderManager renderManager) {
        super(renderManager, new ModelVoidSpike());
    }
}
