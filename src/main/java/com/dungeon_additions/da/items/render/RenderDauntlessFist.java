package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelDauntlessFist;
import com.dungeon_additions.da.items.projectile.ItemDauntlessCrystal;
import com.dungeon_additions.da.items.projectile.ItemDauntlessFist;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderDauntlessFist extends GeoItemRenderer<ItemDauntlessFist> {
    public RenderDauntlessFist() {
        super(new ModelDauntlessFist());
    }
}
