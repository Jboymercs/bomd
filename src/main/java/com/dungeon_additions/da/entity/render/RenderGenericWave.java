package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.entity.model.ModelGenericWave;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderGenericWave extends GeoEntityRenderer<EntityGenericWave> {
    public RenderGenericWave(RenderManager renderManager) {

        super(renderManager, new ModelGenericWave());
    }
}
