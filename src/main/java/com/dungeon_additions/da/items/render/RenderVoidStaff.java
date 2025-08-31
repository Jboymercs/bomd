package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelVoidStaff;
import com.dungeon_additions.da.items.tools.ItemVoidStaff;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderVoidStaff extends GeoItemRenderer<ItemVoidStaff> {


    public RenderVoidStaff() {
        super(new ModelVoidStaff());
    }
}
