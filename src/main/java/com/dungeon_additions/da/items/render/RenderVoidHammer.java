package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelVoidHammer;
import com.dungeon_additions.da.items.tools.ItemChampionAxe;
import com.dungeon_additions.da.items.tools.ItemVoidHammer;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderVoidHammer extends GeoItemRenderer<ItemVoidHammer> {
    public RenderVoidHammer() {
        super(new ModelVoidHammer());
    }
}
