package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelDesertStorm;
import com.dungeon_additions.da.items.model.ModelFlameBlade;
import com.dungeon_additions.da.items.projectile.ItemDesertStorm;
import com.dungeon_additions.da.items.tools.ItemFlameBlade;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderDesertStorm extends GeoItemRenderer<ItemDesertStorm> {

    public RenderDesertStorm() {
        super(new ModelDesertStorm());
    }
}
