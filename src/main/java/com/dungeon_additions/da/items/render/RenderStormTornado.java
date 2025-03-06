package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelStormTornado;
import com.dungeon_additions.da.items.projectile.ItemStormTornado;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderStormTornado extends GeoItemRenderer<ItemStormTornado> {
    public RenderStormTornado() {
        super(new ModelStormTornado());
    }
}
