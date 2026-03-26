package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelSorceryDagger;
import com.dungeon_additions.da.items.projectile.ItemSorceryDagger;
import com.dungeon_additions.da.items.projectile.ItemVoidclysmBolt;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderSorceryDagger extends GeoItemRenderer<ItemSorceryDagger> {
    public RenderSorceryDagger() {
        super(new ModelSorceryDagger());
    }
}
