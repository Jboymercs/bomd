package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelDragonShield;
import com.dungeon_additions.da.items.shield.ItemDragonShield;
import com.dungeon_additions.da.items.shield.ItemDraugrShield;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderDragonShield extends GeoItemRenderer<ItemDragonShield> {
    public RenderDragonShield() {
        super(new ModelDragonShield());
    }
}
