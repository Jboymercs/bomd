package com.dungeon_additions.da.entity.render.sky_dungeon;

import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.entity.model.sky_dungeon.ModelBloodPile;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityBloodPile;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderBloodPile  extends GeoEntityRenderer<EntityBloodPile> {
    public RenderBloodPile(RenderManager renderManager) {
        super(renderManager, new ModelBloodPile());
    }
}
