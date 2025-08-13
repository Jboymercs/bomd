package com.dungeon_additions.da.entity.render.void_dungeon;

import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.model.ModelVoidSpike;
import com.dungeon_additions.da.entity.model.void_dungeon.ModelVoidclysmSpike;
import com.dungeon_additions.da.entity.render.RenderAbstractGeoEntity;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidclysmSpike;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderVoidclysmSpike extends RenderAbstractGeoEntity<EntityVoidclysmSpike> {

    public RenderVoidclysmSpike(RenderManager renderManager) {
        super(renderManager, new ModelVoidclysmSpike());
    }
}
