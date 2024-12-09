package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.AnimatedSporeItem;
import com.dungeon_additions.da.items.ItemMagicFireball;
import com.dungeon_additions.da.items.model.ModelMagicFireball;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderMagicFireball extends GeoItemRenderer<ItemMagicFireball> {


    public RenderMagicFireball() {
        super(new ModelMagicFireball());
    }
}
