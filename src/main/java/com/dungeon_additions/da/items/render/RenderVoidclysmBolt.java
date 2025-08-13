package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelStormTornado;
import com.dungeon_additions.da.items.model.ModelVoidclysmBolt;
import com.dungeon_additions.da.items.projectile.ItemStormTornado;
import com.dungeon_additions.da.items.projectile.ItemVoidclysmBolt;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderVoidclysmBolt extends GeoItemRenderer<ItemVoidclysmBolt> {

    public RenderVoidclysmBolt() {
        super(new ModelVoidclysmBolt());
    }
}
