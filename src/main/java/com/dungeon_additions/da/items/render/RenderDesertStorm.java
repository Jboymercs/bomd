package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.animation.AzLib.DaItemAnimator;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.item.AzItemRenderer;
import mod.azure.azurelib.render.item.AzItemRendererConfig;
import net.minecraft.util.ResourceLocation;

public class RenderDesertStorm extends AzItemRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/items/desert_storm.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.desert_storm.json");

    public RenderDesertStorm() {
        super(
                AzItemRendererConfig.builder(itemStack -> MODEL_RESLOC, itemStack -> TEXTURE)
                        .setAnimatorProvider(() -> new DaItemAnimator("desert_storm"))
                        .build()
        );
    }
}
