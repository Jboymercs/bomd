package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelGhostProjectile;
import com.dungeon_additions.da.items.projectile.ItemProjectileGhost;
import com.dungeon_additions.da.items.projectile.ItemVoidclysmBolt;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderGhostProjectile extends GeoItemRenderer<ItemProjectileGhost> {
    public RenderGhostProjectile() {
        super(new ModelGhostProjectile());
    }
}
