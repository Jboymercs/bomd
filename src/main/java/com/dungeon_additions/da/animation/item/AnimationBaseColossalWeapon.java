package com.dungeon_additions.da.animation.item;

import com.dungeon_additions.da.event.ClientEventHandler;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;

public class AnimationBaseColossalWeapon {

    public static void preformColossalItemRotations1stPerson(EntityPlayer player, float partialTicks, float swing, EnumHandSide hand)
    {
        float side = hand == EnumHandSide.LEFT ? -1F : 1F;

        DAPlayerAnimationMethods.applyFirstPersonBaseTransform(hand == EnumHandSide.LEFT, 0);

        float axeRotate1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.0F, 0.2F);
        float axeRotate2 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.4F, 0.48F);
        float returnTooNuetral = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.66F, 0.95F);
        float returnTooNuetralDelay = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.85F, 0.99F);

        /* Translation helps offset the rotation's adjustment of the model position. */
        GlStateManager.translate((axeRotate1 * 0F) + (axeRotate2 * (-0.6F * side)) + (returnTooNuetral * (0.55F * side)), (axeRotate1 * 0F) + (axeRotate2 * -0.1F) + (returnTooNuetral * 0.1F), (axeRotate1 * -0.5F) + (axeRotate2 * -0.3F) + (returnTooNuetral * 0.8F));

        GlStateManager.rotate((axeRotate1) * 70F, 1F, 0.1F * side, side * -0.5F);
        GlStateManager.rotate((axeRotate2) * 90F, -1F, -0.3F * side, 0F);
        GlStateManager.rotate((returnTooNuetralDelay) * 30F, 1F, -1F * side, 1.7F * side);

        if (swing >= 1) ClientEventHandler.swingingCustom = false;
    }

    public static void preformChampionAxeItemRotations1stPerson(EntityPlayer player, float partialTicks, float swing, EnumHandSide hand)
    {
        float side = hand == EnumHandSide.LEFT ? -1F : 1F;

        DAPlayerAnimationMethods.applyFirstPersonBaseTransform(hand == EnumHandSide.LEFT, 0);

        float axeRotate1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.0F, 0.2F);
        float axeRotate2 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.4F, 0.48F);
        float returnTooNuetral = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.66F, 0.95F);
        float returnTooNuetralDelay = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.85F, 0.99F);

        /* Translation helps offset the rotation's adjustment of the model position. */
        GlStateManager.translate((axeRotate1 * 0F) + (axeRotate2 * (-0.4F * side)) + (returnTooNuetral * (0.35F * side)), (axeRotate1 * 0F) + (axeRotate2 * -0.5F) + (returnTooNuetral * 0.5F), (axeRotate1 * -0.4F) + (axeRotate2 * -0.3F) + (returnTooNuetral * 0.7F));

        GlStateManager.rotate((axeRotate1) * 70F, 1F, 0.1F * side, side * 0.2F);
        GlStateManager.rotate((axeRotate2) * 110F, -1F, 0.3F * side, 0F);
        GlStateManager.rotate((returnTooNuetralDelay) * 30F, 1F, -0.4F * side, -0.2F * side);

        if (swing >= 1) ClientEventHandler.swingingCustom = false;
    }

    /**
     * Third Person Player
     * */
    public static void preformColossalArmRotations3edPerson(Entity entityIn, ModelBiped model, float ageInTicks, float swing, float headYaw, float headPitch, EnumHandSide hand)
    {
        if (!(entityIn instanceof EntityLivingBase)) return;
        if (swing <= 0) return;
        EntityLivingBase living = (EntityLivingBase)entityIn;

        boolean rightHanded = hand == EnumHandSide.RIGHT;
        ModelRenderer mainArm = rightHanded ? model.bipedRightArm : model.bipedLeftArm;
        ModelRenderer offArm  = rightHanded ? model.bipedLeftArm  : model.bipedRightArm;

        /* Arching Back. */
        float archBack1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.0F, 0.2F);
        /* Swing Forward. */
        float archForward1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.4F, 0.48F);
        /* Reset to neutral. */
        float ending = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.66F, 0.95F);

        model.bipedBody.rotationPointX = (archBack1 * 0.5F) + (archForward1 * -0.75F) + (ending * 0.25F);
        model.bipedBody.rotationPointY = (archBack1 * 0.5F) + (archForward1 * -0.75F) + (ending * 0.25F);
        model.bipedBody.rotationPointZ = (archBack1 * 0.5F) + (archForward1 * -1F) + (ending * 0.5F);

        model.bipedBody.rotateAngleX += (archBack1 * -0.1F) + (archForward1 * 0.1F) + (ending * 0F);
        model.bipedBody.rotateAngleY += (archBack1 * 0.7F) + (archForward1 * -0.9F) + (ending * 0.2F);
        model.bipedBody.rotateAngleZ = (archBack1 * 0.03F) + (archForward1 * -0.03F) + (ending * 0F);

        model.bipedHead.rotationPointX = model.bipedBody.rotationPointX;
        model.bipedHead.rotationPointY = model.bipedBody.rotationPointY;
        model.bipedHead.rotationPointZ = model.bipedBody.rotationPointZ;


        if (entityIn.isSneaking())
        {
            model.bipedBody.rotateAngleY *= 0.5F;
        }

        int handSign = rightHanded ? 1 : -1;
        offArm.rotateAngleX = archBack1 * -1.5F + (archForward1 * 1.6F) + (ending * -0.1F);
        offArm.rotateAngleY = 0F;
        offArm.rotateAngleZ = handSign * (archBack1 * -0.6F + (ending * 0.6F));
        offArm.rotationPointX += model.bipedBody.rotationPointX;
        offArm.rotationPointY = 2F + model.bipedBody.rotationPointY;
        offArm.offsetZ = archBack1 * 0.15F + (archForward1 * -0.3F) + (ending * 0.15F);

        mainArm.rotationPointX = (-5 * handSign) + (archBack1 * -0.25F) + (archForward1 * 0.25F);
        mainArm.rotationPointY = 2 + (archForward1 * 1F) + (ending * -3.0F);
        mainArm.rotationPointZ =  (archForward1 * -2F) + (ending * 2F);

        mainArm.rotateAngleX = (archBack1 * -3F) + (archForward1 * 2.6F) + (ending * 0.35F);
        mainArm.rotateAngleY = (archBack1 * 0.3F) + (archForward1 * -0.5F) + (ending * 0.2F);
        mainArm.rotateAngleZ = (archBack1 * -0.5F) + (archForward1 * 0.2F) + (ending * 0.35F);


        if (!rightHanded)
        {
            model.bipedBody.rotationPointX *= -1;
            model.bipedBody.rotateAngleY *= -1;
            model.bipedBody.rotateAngleZ *= -1;

            model.bipedHead.rotationPointX *= -1;

            offArm.rotationPointX += (archBack1 * 3F) + (archForward1 * -3F);

            mainArm.rotateAngleZ *= -1;
            mainArm.rotateAngleY *= -1;
            mainArm.rotationPointX += (archBack1 * 3F) + (archForward1 * -7F);
        }
    }

}
