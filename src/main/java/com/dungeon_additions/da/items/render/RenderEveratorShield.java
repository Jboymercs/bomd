package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelEveratorShield;
import com.dungeon_additions.da.items.shield.ItemDraugrShield;
import com.dungeon_additions.da.items.shield.ItemEveratorShield;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderEveratorShield extends GeoItemRenderer<ItemEveratorShield> {

    public RenderEveratorShield() {
        super(new ModelEveratorShield());
    }
}
