package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.boss.EntityWreathKnight;
import com.dungeon_additions.da.entity.model.ModelWreathKnight;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderWreathKnight extends GeoEntityRenderer<EntityWreathKnight> {

    public RenderWreathKnight(RenderManager renderManager) {
        super(renderManager, new ModelWreathKnight());
    }

    @Override
    public void doRender(EntityWreathKnight entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
