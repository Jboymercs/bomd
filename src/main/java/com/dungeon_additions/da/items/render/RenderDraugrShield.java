package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelDraugrShield;
import com.dungeon_additions.da.items.shield.ItemDraugrShield;
import com.dungeon_additions.da.items.tools.ItemChampionAxe;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderDraugrShield extends GeoItemRenderer<ItemDraugrShield> {
    public RenderDraugrShield() {
        super(new ModelDraugrShield());
    }
}
