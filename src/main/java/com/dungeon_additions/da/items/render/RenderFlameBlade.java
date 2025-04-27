package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelFlameBlade;
import com.dungeon_additions.da.items.tools.ItemBloodySwordSpear;
import com.dungeon_additions.da.items.tools.ItemFlameBlade;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderFlameBlade extends GeoItemRenderer<ItemFlameBlade> {
    public RenderFlameBlade() {
        super(new ModelFlameBlade());
    }
}
