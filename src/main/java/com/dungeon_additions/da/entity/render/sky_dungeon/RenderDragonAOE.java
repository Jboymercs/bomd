package com.dungeon_additions.da.entity.render.sky_dungeon;

import com.dungeon_additions.da.entity.model.sky_dungeon.ModelDragonAOE;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityDragonAOE;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderDragonAOE extends GeoEntityRenderer<EntityDragonAOE> {


    public RenderDragonAOE(RenderManager renderManager) {
        super(renderManager, new ModelDragonAOE());
    }

    @Override
    public void doRender(EntityDragonAOE entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
