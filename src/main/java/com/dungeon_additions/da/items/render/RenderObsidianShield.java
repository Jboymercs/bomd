package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelObsidianShield;
import com.dungeon_additions.da.items.shield.ItemObsidianShield;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderObsidianShield extends GeoItemRenderer<ItemObsidianShield> {
    public RenderObsidianShield() {
        super(new ModelObsidianShield());
    }
}
