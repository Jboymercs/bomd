package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelImperialHalberdItem;
import com.dungeon_additions.da.items.tools.ItemImperialHalberd;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderImperialHalberdItem extends GeoItemRenderer<ItemImperialHalberd> {
    public RenderImperialHalberdItem() {
        super(new ModelImperialHalberdItem());
    }
}
