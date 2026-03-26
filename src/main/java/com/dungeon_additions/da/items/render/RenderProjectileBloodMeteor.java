package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelFastCrystal;
import com.dungeon_additions.da.items.model.ModelProjectileBloodMeteor;
import com.dungeon_additions.da.items.projectile.ItemFastCrystal;
import com.dungeon_additions.da.items.projectile.ItemProjectileBloodMeteor;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderProjectileBloodMeteor extends GeoItemRenderer<ItemProjectileBloodMeteor> {
    public RenderProjectileBloodMeteor() {
        super(new ModelProjectileBloodMeteor());
    }
}
