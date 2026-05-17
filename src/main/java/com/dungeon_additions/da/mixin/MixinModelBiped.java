package com.dungeon_additions.da.mixin;

import com.dungeon_additions.da.animation.item.*;
import com.dungeon_additions.da.capabilities.AnimationCapabilityHelper;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.items.tools.ToolSword;
import com.dungeon_additions.da.util.interfaces.IRotationStorage;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(Side.CLIENT)
@Mixin(ModelBiped.class)
public class MixinModelBiped implements IRotationStorage {
    public float smmm$rightArmZ;
    public float smmm$leftArmZ;

    @Inject(method = "setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V", at = @At(value = "TAIL"))
    private void renderTrident(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo info)
    {

        if (entityIn instanceof EntityLivingBase)
        {
            if (!ModConfig.custom_swing_animations || !ModConfig.combat_system_enabled) return;

            /* Endermen use different offsets compared to traditional bipeds. */
            if (!(entityIn instanceof EntityPlayer)) return;
            boolean busyAnimating = false;

            EntityLivingBase living = (EntityLivingBase) entityIn;
            ModelBiped model = (ModelBiped)(Object)this;
            float partialTicks = ageInTicks - entityIn.ticksExisted;



            /* Let's just hope resetting everything manually doesn't cause conflicts with other mods! */
            //This side handles 3rd person Body rotations
            DAPlayerAnimationMethods.preformPlayerAnimReset(model);

            EnumHandSide swordHand = DAPlayerAnimationMethods.getHandSide(living, ToolSword.class);

            if (living instanceof EntityPlayer)
            {
                float customSwing = AnimationCapabilityHelper.getPlayerCustomSwingAnimProgress((EntityPlayer)living, partialTicks);
                if(swordHand != null) {
                    if(AnimationCapabilityHelper.isPlayerCustomSwingAnimating((EntityPlayer) living)) {

                        if(DAPlayerAnimationMethods.getWeaponType(living) == 1 && ModConfig.enable_sword_weapons) {
                            AnimationsBaseSword.preformSwordArmRotations3edPerson(living, model, ageInTicks, customSwing, netHeadYaw, headPitch, swordHand);
                            busyAnimating = true;
                        }

                        if(DAPlayerAnimationMethods.getWeaponType(living) == 2 && ModConfig.enable_dagger_weapons) {
                            AnimationBaseDagger.preformDaggerArmRotations3edPerson(living, model, ageInTicks, customSwing, netHeadYaw, headPitch, swordHand);
                            busyAnimating = true;
                        }

                        if(DAPlayerAnimationMethods.getWeaponType(living) == 3 && ModConfig.enable_parry_sword_weapons) {
                            AnimationsBaseSword.preformSwordArmRotations3edPerson(living, model, ageInTicks, customSwing, netHeadYaw, headPitch, swordHand);
                            busyAnimating = true;
                        }

                        if(DAPlayerAnimationMethods.getWeaponType(living) == 4 && ModConfig.enable_spear_weapons) {
                            AnimationBaseSpear.preformSpearArmRotations3edPerson(living, model, ageInTicks, customSwing, netHeadYaw, headPitch, swordHand);
                            busyAnimating = true;
                        }

                        if(DAPlayerAnimationMethods.getWeaponType(living) == 5 && ModConfig.enable_heavy_weapons) {
                            AnimationBaseColossalWeapon.preformColossalArmRotations3edPerson(living, model, ageInTicks, customSwing, netHeadYaw, headPitch, swordHand);
                            busyAnimating = true;
                        }
                    }

                    if(AnimationCapabilityHelper.isPlayerParryItemAnimation((EntityPlayer) living)) {
                        float customTime = AnimationCapabilityHelper.getPlayerParryAnimProgress(((EntityPlayer) living), partialTicks);
                        AnimationsBaseSword.preformSwordParryArmRotations3edPerson(living, model, customTime, swordHand);
                    }

                    if(DAPlayerAnimationMethods.getWeaponType(living) == 1 && !busyAnimating && ModConfig.enable_sword_weapons) {
                        AnimationsBaseSword.preformSwordArmRotations3edPerson(living, model, ageInTicks, model.swingProgress, netHeadYaw, headPitch, swordHand);
                    }

                    if(DAPlayerAnimationMethods.getWeaponType(living) == 2 && !busyAnimating && ModConfig.enable_dagger_weapons) {
                        AnimationBaseDagger.preformDaggerArmRotations3edPerson(living, model, ageInTicks, model.swingProgress, netHeadYaw, headPitch, swordHand);
                    }

                    if(DAPlayerAnimationMethods.getWeaponType(living) == 4 && !busyAnimating && ModConfig.enable_spear_weapons) {
                        AnimationBaseSpear.preformSpearArmRotations3edPerson(living, model, ageInTicks, model.swingProgress, netHeadYaw, headPitch, swordHand);
                    }

                    if(DAPlayerAnimationMethods.getWeaponType(living) == 5 && !busyAnimating && ModConfig.enable_heavy_weapons) {
                        AnimationBaseColossalWeapon.preformColossalArmRotations3edPerson(living, model, ageInTicks, model.swingProgress, netHeadYaw, headPitch, swordHand);
                    }

                }
            }


            /* Force the Headwear to follow head adjustments. */
            model.bipedHeadwear.rotationPointX = model.bipedHead.rotationPointX;
            model.bipedHeadwear.rotationPointY = model.bipedHead.rotationPointY;
            model.bipedHeadwear.rotationPointZ = model.bipedHead.rotationPointZ;

            /* Copies values that need to be read by LayerItem later. */
            this.smmm$rightArmZ = model.bipedRightArm.rotateAngleZ;
            this.smmm$leftArmZ = model.bipedLeftArm.rotateAngleZ;
        }
    }

    public float getLeftArmRotateZ() { return smmm$leftArmZ; }
    public void setLeftArmRotateZ(float value) { this.smmm$leftArmZ = value; }

    public float getRightArmRotateZ() { return smmm$rightArmZ; }
    public void setRightArmRotateZ(float value) { this.smmm$rightArmZ = value; }
}
