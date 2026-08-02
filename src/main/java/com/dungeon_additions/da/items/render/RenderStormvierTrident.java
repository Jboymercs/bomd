package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelStormvierTrident;
import com.dungeon_additions.da.items.projectile.ItemTridentProjectile;
import com.dungeon_additions.da.items.tools.ItemStormvierTrident;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderStormvierTrident extends GeoItemRenderer<ItemStormvierTrident> {

    public RenderStormvierTrident() {
        super(new ModelStormvierTrident());
    }
}
