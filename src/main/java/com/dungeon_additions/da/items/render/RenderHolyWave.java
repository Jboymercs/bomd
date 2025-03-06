package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelHolyWave;
import com.dungeon_additions.da.items.projectile.ItemHolyWave;
import com.dungeon_additions.da.items.projectile.ItemLightRing;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderHolyWave extends GeoItemRenderer<ItemHolyWave> {
    public RenderHolyWave() {
        super(new ModelHolyWave());
    }
}
