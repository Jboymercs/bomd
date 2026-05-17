package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelDauntlessSpear;
import com.dungeon_additions.da.items.projectile.ItemDauntlessSpear;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderDauntlessSpear extends GeoItemRenderer<ItemDauntlessSpear> {

    public RenderDauntlessSpear() {
        super(new ModelDauntlessSpear());
    }
}
