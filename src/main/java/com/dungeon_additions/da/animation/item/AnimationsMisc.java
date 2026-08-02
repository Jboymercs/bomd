package com.dungeon_additions.da.animation.item;

import com.dungeon_additions.da.items.tools.ItemStormvierTrident;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class AnimationsMisc {

    public static void preformItemChargingFirstPerson(EntityPlayer player, float swing, float partialTicks, EnumHandSide hand)
    {
        float handAdjustment = hand == EnumHandSide.RIGHT ? 1 : -1;

        if (player.getItemInUseCount() > 0)
        {
            float totalUseTick = player.getItemInUseMaxCount() + partialTicks;
            float progress = Math.min(totalUseTick / 12F, 1F);

            GlStateManager.translate(progress * -0.15F * handAdjustment,progress * 0.45F,progress * 0.15F);
            GlStateManager.rotate(progress * -10.0F, 1.0F, 0.0F, 0);

            float shakeProgress = Math.min(totalUseTick / 300F, 1F);
            float shake = (MathHelper.cos(player.ticksExisted * 1.5F) * 0.005F) * (0.5F + shakeProgress);
            GlStateManager.translate(0, shake, 0);
        }
    }



    public static void preformItemCharging3personItem(EntityLivingBase living, EnumHandSide hand, float partialTicks, float swing, ModelBiped model)
    { preformItemCharging3personItem(living, hand, partialTicks, swing, model, false); }

    public static void preformItemCharging3personItem(EntityLivingBase living, EnumHandSide hand, float partialTicks, float swing, ModelBiped model, boolean useFullerTimings)
    {
        float totalUseTick = living.getItemInUseMaxCount() + partialTicks;
        float progress = Math.min(totalUseTick / 12F, 1F);

        GlStateManager.translate(0, 0,(progress * 0.15F));
        GlStateManager.rotate((progress * -190), 1, 0,0);
    }


    public static void preformItemCharging3personBody(Entity entityIn, ModelBiped model, float ageInTicks, float swing, float headYaw, float headPitch, EnumHandSide hand)
    {
        if (!(entityIn instanceof EntityLivingBase)) return;
        EntityLivingBase living = (EntityLivingBase)entityIn;

        EnumHandSide side = living.getHeldItemMainhand().getItem() instanceof ItemStormvierTrident ? living.getPrimaryHand() : living.getHeldItemOffhand().getItem() instanceof ItemStormvierTrident ? living.getPrimaryHand().opposite() : null;

        if (side == null) return;

        boolean rightHanded = side == EnumHandSide.RIGHT;
        ModelRenderer mainArm = side == EnumHandSide.RIGHT ? model.bipedRightArm : model.bipedLeftArm;
        ModelRenderer offArm  = side == EnumHandSide.RIGHT ? model.bipedLeftArm  : model.bipedRightArm;


        if (living.getItemInUseCount() > 0)
        {
            float totalUseTick = living.getItemInUseMaxCount() + (ageInTicks - entityIn.ticksExisted);
            float progress = Math.min(totalUseTick / 12F, 1F);

            model.bipedBody.rotationPointX = progress * 0.8F * (!rightHanded ? -1 : 1);
            model.bipedBody.rotationPointY = progress * 0.5F;
            model.bipedBody.rotationPointZ = progress * 1F;
            model.bipedBody.rotateAngleX += progress * -0.1F;
            model.bipedBody.rotateAngleY += progress * 0.2F * (!rightHanded ? -1 : 1);
            model.bipedBody.rotateAngleZ += progress * 0.025F * (!rightHanded ? -1 : 1);
            if (entityIn.isSneaking()) model.bipedBody.rotateAngleY *= 0.5F;

            model.bipedHead.rotationPointX += model.bipedBody.rotationPointX;
            model.bipedHead.rotationPointY += model.bipedBody.rotationPointY;
            model.bipedHead.rotationPointZ += model.bipedBody.rotationPointZ;

            offArm.rotateAngleX = progress * -1.4F;
            offArm.rotateAngleZ = progress * -0.5F * (!rightHanded ? -1 : 1);
            offArm.rotateAngleY -= model.bipedBody.rotateAngleY;
            offArm.rotationPointX += progress * 0.5F * (!rightHanded ? -1 : 1);
            offArm.rotationPointZ = progress * -0.7F;

            mainArm.rotateAngleX = progress * -3.4F;
            mainArm.rotateAngleX += Math.max(progress * model.bipedHead.rotateAngleX, -0.3F);
            mainArm.rotationPointY += model.bipedBody.rotationPointY;
            mainArm.rotateAngleZ = progress * -0.5F * (!rightHanded ? -1 : 1);
            mainArm.rotateAngleY += progress * DAPlayerAnimationMethods.getEntityHeadYaw(living, 90) * 0.75F;
            mainArm.rotationPointX += progress * 1F * (!rightHanded ? -1 : 1);
            mainArm.rotationPointZ = progress * 2F;

            mainArm.rotateAngleX += MathHelper.sin(ageInTicks * 2) * (0.005F + (totalUseTick * 0.00001F));
            mainArm.rotateAngleZ += MathHelper.sin(ageInTicks * 2) * (0.005F + (totalUseTick * 0.00001F));
        }
    }
}
