package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelDarkSicle;
import com.dungeon_additions.da.items.shield.ItemDarkShield;
import com.dungeon_additions.da.items.tools.ItemDarkSicle;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderDarkSicle extends GeoItemRenderer<ItemDarkSicle> {
    public RenderDarkSicle() {
        super(new ModelDarkSicle());
    }
}
