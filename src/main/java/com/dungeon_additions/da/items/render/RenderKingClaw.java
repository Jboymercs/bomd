package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelKingClaw;
import com.dungeon_additions.da.items.tools.ItemBloodySwordSpear;
import com.dungeon_additions.da.items.tools.ItemKingClaw;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderKingClaw extends GeoItemRenderer<ItemKingClaw> {
    public RenderKingClaw() {
        super(new ModelKingClaw());
    }
}
