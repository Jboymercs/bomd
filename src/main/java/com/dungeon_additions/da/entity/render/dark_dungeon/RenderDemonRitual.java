package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityDemonRitual;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityGreatDeath;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelDemonRitual;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelGreatDeath;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderDemonRitual extends RenderGeoExtended<EntityDemonRitual> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/demon_ritual.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.great_death.json");

    public RenderDemonRitual(RenderManager renderManager) {
        super(renderManager, new ModelDemonRitual(MODEL_RESLOC, TEXTURE, "demon_ritual"));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityDemonRitual currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityDemonRitual currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityDemonRitual currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityDemonRitual currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityDemonRitual currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityDemonRitual currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityDemonRitual currentEntity) {
        return null;
    }
}
