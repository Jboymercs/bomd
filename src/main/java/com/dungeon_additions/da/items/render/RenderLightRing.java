package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.animation.AzLib.DaItemAnimator;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.item.AzItemRenderer;
import mod.azure.azurelib.render.item.AzItemRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import net.minecraft.util.ResourceLocation;

public class RenderLightRing extends AzItemRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/items/light_ring.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.light_ring.json");

    public RenderLightRing() {
        super(
                AzItemRendererConfig.builder(itemStack -> MODEL_RESLOC, itemStack -> TEXTURE)
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setAnimatorProvider(() -> new DaItemAnimator("light_ring"))
                        .build()
        );
    }
}
