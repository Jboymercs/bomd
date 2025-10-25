package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.gun.ItemGolemCannon;
import com.dungeon_additions.da.items.model.ModelGolemCannon;
import com.dungeon_additions.da.items.shield.ItemFlameShield;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderGolemCannon extends GeoItemRenderer<ItemGolemCannon> {

    public RenderGolemCannon() {
        super(new ModelGolemCannon());
    }
}
