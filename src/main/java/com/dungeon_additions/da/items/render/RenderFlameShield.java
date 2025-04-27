package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelFlameShield;
import com.dungeon_additions.da.items.shield.ItemDraugrShield;
import com.dungeon_additions.da.items.shield.ItemFlameShield;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderFlameShield extends GeoItemRenderer<ItemFlameShield> {
    public RenderFlameShield() {
        super(new ModelFlameShield());
    }
}
