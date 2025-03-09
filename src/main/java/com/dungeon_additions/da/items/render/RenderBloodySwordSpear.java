package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelBloodySwordSpear;
import com.dungeon_additions.da.items.tools.ItemBloodySwordSpear;
import com.dungeon_additions.da.items.tools.ItemChampionAxe;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderBloodySwordSpear extends GeoItemRenderer<ItemBloodySwordSpear> {
    public RenderBloodySwordSpear() {
        super(new ModelBloodySwordSpear());
    }
}
