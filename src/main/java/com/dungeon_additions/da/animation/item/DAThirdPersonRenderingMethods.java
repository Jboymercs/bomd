package com.dungeon_additions.da.animation.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;

public class DAThirdPersonRenderingMethods {
    public static void renderStaffOfDominanceOrbFirstPerson(EntityLivingBase entity, RenderLivingBase livingRender, ItemStack stack, ItemCameraTransforms.TransformType transforms, EnumHandSide hand)
    {
        boolean flag = hand == EnumHandSide.LEFT;
        float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();

        ItemStack part2 = stack.copy();
        part2.setTagCompound(new NBTTagCompound());
        part2.getTagCompound().setInteger("model", 2);

        GlStateManager.pushMatrix();

        if (entity.isSneaking())  GlStateManager.translate(0.0F, 0.2F, 0.0F);
        ((ModelBiped)livingRender.getMainModel()).postRenderArm(0.0625F, hand);
        GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        //boolean flag = hand == EnumHandSide.LEFT;
        GlStateManager.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);

        float spin = (entity.ticksExisted + partialTicks) * -3F;

        if (entity.getItemInUseCount() > 0)
        {
            float totalUseTick = entity.getItemInUseMaxCount() + partialTicks;
            float progress = Math.min(totalUseTick / 15F, 1F);
            spin = (entity.ticksExisted + partialTicks) * (-3 + (60 * progress));
            GlStateManager.translate(0, (progress * -0.3F), (progress * 0.15F));
            GlStateManager.rotate((progress * -60), 1, 0,0);
        }
        GlStateManager.translate(0, 1.42F, -0.4F);
        GlStateManager.rotate(spin, 0F, 1F, 1F);

        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, part2, transforms, flag);
        resetLightmap();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    /** Resets the lightmap, so it doesn't bleed into other renderings.*/
    public static void resetLightmap()
    {
        AbstractClientPlayer abstractclientplayer = Minecraft.getMinecraft().player;
        int i = Minecraft.getMinecraft().world.getCombinedLight(new BlockPos(abstractclientplayer.posX, abstractclientplayer.posY + (double)abstractclientplayer.getEyeHeight(), abstractclientplayer.posZ), 0);
        float f = (float)(i & 65535);
        float f1 = (float)(i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
    }
}
