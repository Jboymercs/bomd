package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.model.ModelRotKnight;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderRotKnight extends GeoEntityRenderer<EntityRotKnight> {


    public RenderRotKnight(RenderManager renderManager) {
        super(renderManager, new ModelRotKnight());
    }

    @Override
    public void doRender(EntityRotKnight entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
