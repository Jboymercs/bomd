package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.tools.ItemBloodySwordSpear;
import com.dungeon_additions.da.items.tools.ItemKingClaw;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelKingClaw extends AnimatedGeoModel<ItemKingClaw> {
    @Override
    public ResourceLocation getModelLocation(ItemKingClaw object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.king_claw.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemKingClaw object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/king_claw.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemKingClaw animatable) {
       return null;
    }
}
