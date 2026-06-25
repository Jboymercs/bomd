package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelDauntlessCrystal;
import com.dungeon_additions.da.items.projectile.ItemDauntlessCrystal;
import com.dungeon_additions.da.items.projectile.ItemProjectileBloodMeteor;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderDauntlessCrystal extends GeoItemRenderer<ItemDauntlessCrystal> {


    public RenderDauntlessCrystal() {
        super(new ModelDauntlessCrystal());
    }
}
