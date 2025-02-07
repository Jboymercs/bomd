package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelDraugrShield;
import com.dungeon_additions.da.items.model.ModelLightRing;
import com.dungeon_additions.da.items.projectile.ItemLightRing;
import com.dungeon_additions.da.items.shield.ItemDraugrShield;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderLightRing extends GeoItemRenderer<ItemLightRing> {
    public RenderLightRing() {
        super(new ModelLightRing());
    }
}
