package com.dungeon_additions.da.animation.AzLib;

import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.animation.controller.AzAnimationController;
import mod.azure.azurelib.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.animation.impl.AzItemAnimator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class DaItemAnimator extends AzItemAnimator {

    private final String animationName;

    public DaItemAnimator(String animationName) {
    this.animationName = animationName;
    }

    @Override
    public void registerControllers(AzAnimationControllerContainer<ItemStack> animationControllerContainer) {
        animationControllerContainer.add(
                AzAnimationController.builder(this, DaReferenceStrings.STATE_CTR_ONE)
                        .build()
        );
    }

    @Nonnull
    @Override
    public ResourceLocation getAnimationLocation(ItemStack stack) {
        if(animationName != null) {
            return new ResourceLocation(ModReference.MOD_ID, "animations/animation." + animationName + ".json");
        }
        return null;
    }
}
