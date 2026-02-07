package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelColossusMace;
import com.dungeon_additions.da.items.tools.ItemChampionAxe;
import com.dungeon_additions.da.items.tools.ItemColossusMace;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderColossusMace extends GeoItemRenderer<ItemColossusMace> {

    public RenderColossusMace() {
        super(new ModelColossusMace());
    }
}
