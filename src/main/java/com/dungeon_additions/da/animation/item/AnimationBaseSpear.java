package com.dungeon_additions.da.animation.item;

import com.dungeon_additions.da.event.ClientEventHandler;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;

public class AnimationBaseSpear {

    /**
     * First Person
     * */
    public static void preformSpearItemRotations1stPerson(EntityPlayer player, float partialTicks, float swing, EnumHandSide hand)
    {
        float side = hand == EnumHandSide.LEFT ? -1F : 1F;

        DAPlayerAnimationMethods.applyFirstPersonBaseTransform(hand == EnumHandSide.LEFT, 0);

        float axeRotate1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.0F, 0.3F);
        float axeRotate2 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.4F, 0.65F);
        float returnTooNuetral = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.66F, 0.95F);
        float returnTooNuetralDelay = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.85F, 0.99F);

        /* Translation helps offset the rotation's adjustment of the model position. */
        GlStateManager.translate((axeRotate1 * 0F) + (axeRotate2 * (-0.3F * side)) + (returnTooNuetral * (0.3F * side)), (axeRotate1 * -0.2F) + (axeRotate2 * 0.4F) + (returnTooNuetral * -0.2F), (axeRotate1 * 0.5F) + (axeRotate2 * -1.25F) + (returnTooNuetral * 0.75F));

        GlStateManager.rotate((axeRotate1) * 90.0F, -1.0F, 0.2F * side, side * 0.1F);
        GlStateManager.rotate((axeRotate2) * 10.0F, -1F, 0F, 0F);
        GlStateManager.rotate((returnTooNuetralDelay) * 100.0F, 1F, 0F, 0F);

        if (swing >= 1) ClientEventHandler.swingingCustom = false;
    }

    /**
     * First Person
     * */
    public static void preformBigSpearItemRotations1stPerson(EntityPlayer player, float partialTicks, float swing, EnumHandSide hand)
    {
        float side = hand == EnumHandSide.LEFT ? -1F : 1F;

        DAPlayerAnimationMethods.applyFirstPersonBaseTransform(hand == EnumHandSide.LEFT, 0);

        float axeRotate1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.0F, 0.3F);
        float axeRotate2 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.4F, 0.5F);
        float returnTooNuetral = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.66F, 0.95F);
        float returnTooNuetralDelay = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.85F, 0.99F);

        /* Translation helps offset the rotation's adjustment of the model position. */
        GlStateManager.translate((axeRotate1 * 0F) + (axeRotate2 * (-0.3F * side)) + (returnTooNuetral * (0.3F * side)), (axeRotate1 * -0.2F) + (axeRotate2 * 0.4F) + (returnTooNuetral * -0.2F), (axeRotate1 * 0.5F) + (axeRotate2 * -1.25F) + (returnTooNuetral * 0.75F));

        GlStateManager.rotate((axeRotate1) * 30.0F, -1.0F, 0.2F * side, side * 0.1F);
        GlStateManager.rotate((axeRotate2) * 10.0F, -1F, 0F, 0F);
        GlStateManager.rotate((returnTooNuetralDelay) * 35.0F, 1F, 0F, 0F);

        if (swing >= 1) ClientEventHandler.swingingCustom = false;
    }

    /**
     * Third Person ITEM
     * */
    public static void preformSpearItemRotations3edPerson(EntityLivingBase living, EnumHandSide hand, float partialTicks, float swing, ModelBiped model)
    {
        if (swing > 0)
        {
            float archBack1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.0F, 0.3F);
            float archForward1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.4F, 0.65F);
            float ending = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.66F, 0.95F);

            GlStateManager.translate((archForward1 * 0F) + (ending * 0F) , (archForward1 * 0F) + (ending * 0F), (archForward1 * -0.15F) + (ending * 0.15F));
            //GlStateManager.translate(0, (archForward1 * -0.3F) + (ending * 0.3F),(archForward1 * -0.2F) + (ending * 0.2F));
            GlStateManager.rotate((archBack1 * 40) + (archForward1 * -90) + (ending * 40), 1F, 0F,0F);
        }
    }


    /**
     * Third Person Model
     * */
    public static void preformSpearArmRotations3edPerson(Entity entityIn, ModelBiped model, float ageInTicks, float swing, float headYaw, float headPitch, EnumHandSide hand)
    {
        if (!(entityIn instanceof EntityLivingBase)) return;
        if (swing <= 0) return;
        EntityLivingBase living = (EntityLivingBase)entityIn;

        boolean rightHanded = hand == EnumHandSide.RIGHT;
        ModelRenderer mainArm = rightHanded ? model.bipedRightArm : model.bipedLeftArm;
        ModelRenderer offArm  = rightHanded ? model.bipedLeftArm  : model.bipedRightArm;

        /* Arching Back. */
        float archBack1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.0F, 0.3F);
        /* Swing Forward. */
        float archForward1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.4F, 0.65F);
        /* Reset to neutral. */
        float ending = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.66F, 0.95F);

        model.bipedBody.rotationPointX = (archBack1 * 0.5F) + (archForward1 * -0.7F) + (ending * 0.2F);
        model.bipedBody.rotationPointY = (archBack1 * 0F) + (archForward1 * -0.2F) + (ending * 0.2F);
        model.bipedBody.rotationPointZ = (archBack1 * -0.5F) + (archForward1 * 0.5F) + (ending * -0.5F);

        model.bipedBody.rotateAngleX += (archBack1 * 0.2F) + (archForward1 * -0.3F) + (ending * 0F);
        model.bipedBody.rotateAngleY += (archBack1 * 0.4F) + (archForward1 * -1F) + (ending * 0.6F);
        model.bipedBody.rotateAngleZ = (archForward1 * -0.05F) + (ending * 0.05F);

        model.bipedHead.rotationPointX = model.bipedBody.rotationPointX;
        model.bipedHead.rotationPointY = model.bipedBody.rotationPointY;
        model.bipedHead.rotationPointZ = model.bipedBody.rotationPointZ;


        if (entityIn.isSneaking())
        {
            model.bipedBody.rotateAngleY *= 0.5F;
        }

        int handSign = rightHanded ? 1 : -1;
        offArm.rotateAngleX = archBack1 * -1.25F + (archForward1 * 2.5F) + (ending * -1.05F);
        offArm.rotateAngleY = 0F;
        offArm.rotateAngleZ = handSign * (archBack1 * 0.1F + (ending * 0.25F) + (archForward1 * -0.6F));
        offArm.rotationPointX += model.bipedBody.rotationPointX;
        offArm.rotationPointY = 2F + model.bipedBody.rotationPointY;
        offArm.offsetZ = archBack1 * 0.15F + (archForward1 * -0.3F) + (ending * 0.15F);

        mainArm.rotationPointX = (-5 * handSign) + (archBack1 * 0F) + (archForward1 * 0F) + (ending * 0F);
        mainArm.rotationPointY = (archForward1 * 0F) + (ending * 0F);
        mainArm.rotationPointZ =  (archForward1 * -0.75F) + (ending * 0.75F);

        mainArm.rotateAngleX = (archBack1 * 0.9F) + (archForward1 * -2.2F) + (ending * 1.05F);
        mainArm.rotateAngleY = (archBack1 * 0.2F) + (archForward1 * -0.4F) + (ending * 0.2F);
        mainArm.rotateAngleZ = (archBack1 * 0.7F) + (archForward1 * -0.6F) + (ending * -0.05F);


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
