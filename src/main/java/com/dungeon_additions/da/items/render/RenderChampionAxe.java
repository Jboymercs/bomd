package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelChampionAxe;
import com.dungeon_additions.da.items.tools.ItemChampionAxe;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderChampionAxe extends GeoItemRenderer<ItemChampionAxe> {
    public RenderChampionAxe() {
        super(new ModelChampionAxe());
    }
}
