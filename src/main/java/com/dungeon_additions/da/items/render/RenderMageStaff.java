package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelApathyrAxe;
import com.dungeon_additions.da.items.model.ModelMageStaff;
import com.dungeon_additions.da.items.tools.ItemApathyrAxe;
import com.dungeon_additions.da.items.tools.ItemMageStaff;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderMageStaff extends GeoItemRenderer<ItemMageStaff> {
    public RenderMageStaff() {
        super(new ModelMageStaff());
    }
}
