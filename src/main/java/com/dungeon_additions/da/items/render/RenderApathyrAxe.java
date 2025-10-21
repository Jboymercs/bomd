package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelApathyrAxe;
import com.dungeon_additions.da.items.tools.ItemApathyrAxe;
import com.dungeon_additions.da.items.tools.ItemChampionAxe;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderApathyrAxe extends GeoItemRenderer<ItemApathyrAxe> {
    public RenderApathyrAxe() {
        super(new ModelApathyrAxe());
    }
}
