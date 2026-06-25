package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelNightfallGauntlets;
import com.dungeon_additions.da.items.tools.ItemNightfallGauntlets;
import com.dungeon_additions.da.items.tools.ItemNightfallSword;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderNightfallGauntlets extends GeoItemRenderer<ItemNightfallGauntlets> {
    public RenderNightfallGauntlets() {
        super(new ModelNightfallGauntlets());
    }
}
