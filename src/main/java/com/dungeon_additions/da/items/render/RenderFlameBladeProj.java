package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelFlameBladeProj;
import com.dungeon_additions.da.items.projectile.ItemFlameBladeProj;
import com.dungeon_additions.da.items.tools.ItemFlameBlade;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderFlameBladeProj extends GeoItemRenderer<ItemFlameBladeProj> {
    public RenderFlameBladeProj() {
        super(new ModelFlameBladeProj());
    }
}
