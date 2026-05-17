package com.dungeon_additions.da.animation.item;

import com.dungeon_additions.da.event.ClientEventHandler;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;

public class AnimationBaseDagger {

    /**
     * First Person
     * */
    public static void preformDaggerItemRotations1stPerson(EntityPlayer player, float partialTicks, float swing, EnumHandSide hand)
    {
        float side = hand == EnumHandSide.LEFT ? -1F : 1F;

        DAPlayerAnimationMethods.applyFirstPersonBaseTransform(hand == EnumHandSide.LEFT, 0);

        float axeRotate1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.0F, 0.2F);
        float axeRotate2 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.3F, 0.5F);
        float returnTooNuetral = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.55F, 0.8F);

        /* Translation helps offset the rotation's adjustment of the model position. */
        GlStateManager.translate((axeRotate2 * (-0.9F * side)) + (returnTooNuetral * (0.6F * side)), (axeRotate1 * 0.4F) + (axeRotate2 * -0.3F), (axeRotate2 * -0.4F) + (returnTooNuetral * 0.7F));

        GlStateManager.rotate((axeRotate1) * 60.0F, 0.0F, 0.0F, -side);
        GlStateManager.rotate((axeRotate2) * 130.0F, -1.0F, 0.2F, 0F);

        if (swing >= 1) ClientEventHandler.swingingCustom = false;
    }

    /**
     * Third Person Model
     * */
    public static void preformDaggerArmRotations3edPerson(Entity entityIn, ModelBiped model, float ageInTicks, float swing, float headYaw, float headPitch, EnumHandSide hand)
    {
        if (!(entityIn instanceof EntityLivingBase)) return;
        if (swing <= 0) return;
        EntityLivingBase living = (EntityLivingBase)entityIn;

        boolean rightHanded = hand == EnumHandSide.RIGHT;
        ModelRenderer mainArm = rightHanded ? model.bipedRightArm : model.bipedLeftArm;
        ModelRenderer offArm  = rightHanded ? model.bipedLeftArm  : model.bipedRightArm;

        /* Arching Back. */
        float archBack1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.0F, 0.05F);
        /* Swing Forward. */
        float archForward1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.1F, 0.4F);
        /* Reset to neutral. */
        float ending = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.41F, 0.9F);

        model.bipedBody.rotationPointX = (archBack1 * -2F) + (archForward1 * 3F) + (ending * -1F);
        model.bipedBody.rotationPointY = (archBack1 * 1F) + (ending * -1F);

        model.bipedBody.rotationPointZ = (archBack1 * 1F) + (archForward1 * -4F) + (ending * 3.3F);
        model.bipedBody.rotateAngleX += (archBack1 * -0.1F) + (archForward1 * 0.6F) + (ending * -0.4F);
        model.bipedBody.rotateAngleY += (archBack1 * -0.7F) + (ending * 0.5F);
        model.bipedBody.rotateAngleZ = (archBack1 * -0.1F) + (ending * 0.1F);

        model.bipedHead.rotationPointX = model.bipedBody.rotationPointX;
        model.bipedHead.rotationPointY = model.bipedBody.rotationPointY;
        model.bipedHead.rotationPointZ = model.bipedBody.rotationPointZ;


        if (entityIn.isSneaking())
        {
            model.bipedBody.rotateAngleY *= 0.5F;
        }

        int handSign = rightHanded ? 1 : -1;
        offArm.rotateAngleX = archBack1 * -1.25F + (archForward1 * 2F) + (ending * -0.75F);
        offArm.rotateAngleY = 0F;
        offArm.rotateAngleZ = handSign * (archBack1 * -0.5F + (ending * 0.5F));
        offArm.rotationPointX += model.bipedBody.rotationPointX;
        offArm.rotationPointY = 2F + model.bipedBody.rotationPointY;
        offArm.offsetZ = archBack1 * 0.15F + (archForward1 * -0.3F) + (ending * 0.15F);

        mainArm.rotationPointX = (-5 * handSign) + (archBack1 * -1) + (archForward1 * 1.5F);
        mainArm.rotationPointY = 0 + (archForward1 * 0F) + (ending * 0F);
        mainArm.rotationPointZ =  (archForward1 * -5F) + (ending * 4F);
        mainArm.rotateAngleX = (archBack1 * 0.7F) + (archForward1 * -2.8F) + (ending * 2.4F);
        mainArm.rotateAngleY = (archBack1 * 0F) + (ending * -0.1F);
        mainArm.rotateAngleZ = (archBack1 * 0.4F) + (archForward1 * 0.9F) + (ending * -1F);


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

    /**
     * Third Person ITEM
     * */
    public static void preformDaggerItemRotations3edPerson(EntityLivingBase living, EnumHandSide hand, float partialTicks, float swing, ModelBiped model)
    {
        if (swing > 0)
        {
            float archBack1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.0F, 0.05F);
            float archForward1 = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.1F, 0.4F);
            float ending = DAPlayerAnimationMethods.segmentAnimationTime(swing, 0.41F, 0.9F);

            //GlStateManager.translate(0, (archForward1 * -0.3F) + (ending * 0.3F),(archForward1 * -0.2F) + (ending * 0.2F));
            GlStateManager.rotate((archBack1 * -120) + (archForward1 * 160) + (ending * -40), 1, 0,0);
        }
    }
}
