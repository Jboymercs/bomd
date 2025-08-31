package com.dungeon_additions.da.entity.render.void_dungeon;

import com.dungeon_additions.da.entity.EntityNetherAbberrant;
import com.dungeon_additions.da.entity.model.void_dungeon.ModelBlueWave;
import com.dungeon_additions.da.entity.model.void_dungeon.ModelVoidBlackHole;
import com.dungeon_additions.da.entity.render.RenderAbstractGeoEntity;
import com.dungeon_additions.da.entity.void_dungeon.EntityBlueWave;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidBlackHole;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderVoidBlackHole extends RenderAbstractGeoEntity<EntityVoidBlackHole> {

    public RenderVoidBlackHole(RenderManager renderManager) {
        super(renderManager, new ModelVoidBlackHole());
    }

    @Override
    public void doRender(EntityVoidBlackHole entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
