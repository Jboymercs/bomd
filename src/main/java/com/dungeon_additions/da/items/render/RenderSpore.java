package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.AnimatedSporeItem;
import com.dungeon_additions.da.items.model.ModelSpore;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderSpore extends GeoItemRenderer<AnimatedSporeItem> {
    public RenderSpore() {
        super(new ModelSpore());
    }
}
