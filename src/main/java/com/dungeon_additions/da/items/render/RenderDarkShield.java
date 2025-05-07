package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelDarkShield;
import com.dungeon_additions.da.items.shield.ItemDarkShield;
import com.dungeon_additions.da.items.shield.ItemDragonShield;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderDarkShield extends GeoItemRenderer<ItemDarkShield> {
    public RenderDarkShield() {
        super(new ModelDarkShield());
    }
}
