package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelFastCrystal;
import com.dungeon_additions.da.items.projectile.ItemDesertStorm;
import com.dungeon_additions.da.items.projectile.ItemFastCrystal;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderFastCrystal extends GeoItemRenderer<ItemFastCrystal> {
    public RenderFastCrystal() {
        super(new ModelFastCrystal());
    }
}
