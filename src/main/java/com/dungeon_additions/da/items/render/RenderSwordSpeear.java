package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelSwordSpear;
import com.dungeon_additions.da.items.tools.ItemBloodySwordSpear;
import com.dungeon_additions.da.items.tools.ItemSwordSpear;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderSwordSpeear extends GeoItemRenderer<ItemSwordSpear> {
    public RenderSwordSpeear() {
        super(new ModelSwordSpear());
    }
}
