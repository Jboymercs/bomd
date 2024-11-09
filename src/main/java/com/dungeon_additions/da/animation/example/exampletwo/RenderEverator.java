package com.dungeon_additions.da.animation.example.exampletwo;


import com.dungeon_additions.da.entity.render.RenderModEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;


public class RenderEverator extends RenderModEntity<EntityEverator> {
    //Nothing changes when coming to rendering using the animation system, have fun
    public <U extends ModelBase> RenderEverator(RenderManager rendermanagerIn) {

        super(rendermanagerIn, "everator.png", new ModelEverator());
    }

    @Override
    public void doRender(EntityEverator entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
