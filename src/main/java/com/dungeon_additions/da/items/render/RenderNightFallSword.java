package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.animation.AzLib.DaItemAnimator;
import com.dungeon_additions.da.items.tools.ItemNightfallSword;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.item.AzItemRenderer;
import mod.azure.azurelib.render.item.AzItemRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderNightFallSword extends AzItemRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/items/nightfall/nightfall_sword.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.nightfall_sword.json");
    public static final ResourceLocation SWORD_CHARGE_1 = new ResourceLocation(ModReference.MOD_ID, "textures/items/nightfall/nightfall_sword_1.png");
    public static final ResourceLocation SWORD_CHARGE_2 = new ResourceLocation(ModReference.MOD_ID, "textures/items/nightfall/nightfall_sword_2.png");
    public static final ResourceLocation SWORD_CHARGE_3 = new ResourceLocation(ModReference.MOD_ID, "textures/items/nightfall/nightfall_sword_3.png");
    public RenderNightFallSword() {
        super(
                AzItemRendererConfig.builder(itemStack -> MODEL_RESLOC, itemStack -> TEXTURE)
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setAnimatorProvider(() -> new DaItemAnimator(null))
                        .build()
        );
    }

    public void render(ItemStack stack, ItemCameraTransforms.TransformType transformType) {
        super.render(stack, transformType);
        if(stack.getItem() instanceof ItemNightfallSword) {
            ItemNightfallSword sword = (ItemNightfallSword) stack.getItem();
            GlStateManager.pushMatrix();
            if(sword.getAbilityVal(stack) == 1) {
                Minecraft.getMinecraft().renderEngine.bindTexture(SWORD_CHARGE_1);
            } else if(sword.getAbilityVal(stack) == 2) {
                Minecraft.getMinecraft().renderEngine.bindTexture(SWORD_CHARGE_2);
            } else if(sword.getAbilityVal(stack) >= 3) {
                Minecraft.getMinecraft().renderEngine.bindTexture(SWORD_CHARGE_3);
            } else {
                Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
            }
            GlStateManager.popMatrix();
        }
    }
}
