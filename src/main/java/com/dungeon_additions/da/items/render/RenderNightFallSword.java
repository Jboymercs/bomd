package com.dungeon_additions.da.items.render;

import com.dungeon_additions.da.items.model.ModelNightfallSword;
import com.dungeon_additions.da.items.tools.ItemNightfallSword;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import java.util.Collections;

public class RenderNightFallSword extends GeoItemRenderer<ItemNightfallSword> {

    public static final ResourceLocation SWORD_CHARGE_1 = new ResourceLocation(ModReference.MOD_ID, "textures/items/nightfall/nightfall_sword_1.png");
    public static final ResourceLocation SWORD_CHARGE_2 = new ResourceLocation(ModReference.MOD_ID, "textures/items/nightfall/nightfall_sword_2.png");
    public static final ResourceLocation SWORD_CHARGE_3 = new ResourceLocation(ModReference.MOD_ID, "textures/items/nightfall/nightfall_sword_3.png");
    public RenderNightFallSword() {
        super(new ModelNightfallSword());
    }

    @Override
    public void render(ItemNightfallSword animatable, ItemStack itemStack) {
        this.currentItemStack = itemStack;
        GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(animatable));
        AnimationEvent itemEvent = new AnimationEvent(animatable, 0, 0,
                Minecraft.getMinecraft().getRenderPartialTicks(), false, Collections.singletonList(itemStack));
        modelProvider.setLivingAnimations(animatable, this.getUniqueID(animatable), itemEvent);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0.01f, 0);
        GlStateManager.translate(0.5, 0.5, 0.5);

        if(itemStack.getItem() instanceof ItemNightfallSword) {
            ItemNightfallSword sword = ((ItemNightfallSword)itemStack.getItem());
            if(sword.getAbilityVal(itemStack) == 1) {
                Minecraft.getMinecraft().renderEngine.bindTexture(SWORD_CHARGE_1);
            } else if(sword.getAbilityVal(itemStack) == 2) {
                Minecraft.getMinecraft().renderEngine.bindTexture(SWORD_CHARGE_2);
            } else if(sword.getAbilityVal(itemStack) >= 3) {
                Minecraft.getMinecraft().renderEngine.bindTexture(SWORD_CHARGE_3);
            } else {
                Minecraft.getMinecraft().renderEngine.bindTexture(getTextureLocation(animatable));
            }
        }  else {
            Minecraft.getMinecraft().renderEngine.bindTexture(getTextureLocation(animatable));
        }
        Color renderColor = getRenderColor(animatable, 0f);
        render(model, animatable, 0f, (float) renderColor.getRed() / 255f, (float) renderColor.getGreen() / 255f,
                (float) renderColor.getBlue() / 255f, (float) renderColor.getAlpha() / 255);
        GlStateManager.popMatrix();
    }
}
