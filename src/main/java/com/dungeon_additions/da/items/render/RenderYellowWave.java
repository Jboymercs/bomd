package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelVoidclysmBolt;
import com.dungeon_additions.da.items.model.ModelYellowWave;
import com.dungeon_additions.da.items.projectile.ItemVoidclysmBolt;
import com.dungeon_additions.da.items.projectile.ItemYellowWave;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderYellowWave extends GeoItemRenderer<ItemYellowWave> {
    public RenderYellowWave() {
        super(new ModelYellowWave());
    }
}
