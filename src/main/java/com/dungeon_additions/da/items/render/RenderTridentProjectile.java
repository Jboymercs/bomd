package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelTridentProjectile;
import com.dungeon_additions.da.items.projectile.ItemTridentProjectile;
import com.dungeon_additions.da.items.tools.ItemSwordSpear;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderTridentProjectile extends GeoItemRenderer<ItemTridentProjectile> {
    public RenderTridentProjectile() {
        super(new ModelTridentProjectile());
    }
}
