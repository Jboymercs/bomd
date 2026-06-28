package com.dungeon_additions.da.mixin;

import com.dungeon_additions.da.animation.item.*;
import com.dungeon_additions.da.capabilities.AnimationCapabilityHelper;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.items.tools.ItemFlameBlade;
import com.dungeon_additions.da.items.tools.ToolSword;
import com.dungeon_additions.da.util.interfaces.IRotationStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin(LayerHeldItem.class)
public class MixinLayerHeldItem {
    @Shadow
    @Final
    protected RenderLivingBase<?> livingEntityRenderer;

    @Inject( method = "renderHeldItem", at = @At("HEAD"), cancellable = true)
    public void renderSMMMItemHeld(EntityLivingBase entity, ItemStack stack, ItemCameraTransforms.TransformType transforms, EnumHandSide hand, CallbackInfo callback)
    {
        if (entity == null || stack.isEmpty()) return;
        if (!ModConfig.custom_swing_animations) return;
        if(!ModConfig.combat_system_enabled) return;
        if(!(entity instanceof EntityPlayer)) return;
        if (!(livingEntityRenderer.getMainModel() instanceof ModelBiped)) return;
        Item itemType = stack.getItem();
        if (!(itemType instanceof ToolSword)) return;
        /* If everything else passed, move on to the full overrides! */
        callback.cancel();

        ModelBiped model = (ModelBiped) livingEntityRenderer.getMainModel();
        float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();

        /* First, all the normal translations. */
        GlStateManager.pushMatrix();

        /** NOW we apply them funky animations */
        IRotationStorage data = (IRotationStorage) model;
        model.bipedRightArm.rotateAngleZ = data.getRightArmRotateZ();
        model.bipedLeftArm.rotateAngleZ = data.getLeftArmRotateZ();


        if (entity.isSneaking())  GlStateManager.translate(0.0F, 0.2F, 0.0F);
        ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, hand);
        GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        boolean flag = hand == EnumHandSide.LEFT;
        GlStateManager.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);

        /* Then, I take over with special transforms. */
        //This side handles 3rd person Item Rotation

      //  if (itemType instanceof ItemBuckChuckets) AnimationsBuckChuckets.preformBuckChuketsItemRotations3edPerson(entity, hand, partialTicks, stack);
      //  if (itemType instanceof ItemSaxophone) AnimationsSaxophone.preformSaxophoneItemRotations3edPerson(entity, hand, partialTicks, stack);
        if(itemType instanceof ToolSword && entity instanceof EntityPlayer) {

                if(AnimationCapabilityHelper.isPlayerCustomSwingAnimating(((EntityPlayer) entity)) && !(itemType instanceof ItemFlameBlade)) {
                    //Dagger
                    if(DAPlayerAnimationMethods.getWeaponType(entity) == 2 && ModConfig.enable_dagger_weapons) {
                        float swing = AnimationCapabilityHelper.getPlayerCustomSwingAnimProgress((EntityPlayer)entity, partialTicks);
                        AnimationBaseDagger.preformDaggerItemRotations3edPerson(entity, hand, partialTicks, swing, model);
                    } //else {
                      //  AnimationBaseDagger.preformDaggerItemRotations3edPerson(entity, hand, partialTicks, model.swingProgress, model);
                   // }

                    //Spear
                    if(DAPlayerAnimationMethods.getWeaponType(entity) == 4 && ModConfig.enable_spear_weapons) {
                        float swing = AnimationCapabilityHelper.getPlayerCustomSwingAnimProgress((EntityPlayer)entity, partialTicks);
                        AnimationBaseSpear.preformSpearItemRotations3edPerson(entity, hand, partialTicks, swing, model);
                    }
                }

                if(AnimationCapabilityHelper.isPlayerParryItemAnimation(((EntityPlayer) entity))) {
                    if(DAPlayerAnimationMethods.getWeaponType(entity) == 3) {
                        float customTime = AnimationCapabilityHelper.getPlayerParryAnimProgress(((EntityPlayer) entity), partialTicks);
                        AnimationsBaseSword.preformParryItemRotations3edPerson(entity, hand, partialTicks, customTime, model);
                    } else {
                        AnimationsBaseSword.preformParryItemRotations3edPerson(entity, hand, partialTicks, model.swingProgress, model);
                    }
                }
        }

        Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, transforms, flag);
        GlStateManager.popMatrix();
    }
}
