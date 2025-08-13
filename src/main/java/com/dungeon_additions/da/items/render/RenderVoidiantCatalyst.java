package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.gun.ItemVoidiantCatalyst;
import com.dungeon_additions.da.items.model.ModelVoidiantCatalyst;
import com.dungeon_additions.da.items.tools.ItemSwordSpear;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderVoidiantCatalyst extends GeoItemRenderer<ItemVoidiantCatalyst> {


    public RenderVoidiantCatalyst() {
        super(new ModelVoidiantCatalyst());
    }
}
