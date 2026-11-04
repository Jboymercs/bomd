package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.animation.AzLib.animators.FlameKnightAnimator;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.render.layer.LayerFlameHat;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.model.AzBone;
import mod.azure.azurelib.render.AzRendererPipelineContext;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.layer.AzBlockAndItemLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import mod.azure.azurelib.util.math.Vector3f;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import java.util.UUID;

public class RenderFlameKnight extends AzEntityRenderer<EntityFlameKnight> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/flame_knight.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/flame_knight/geo.flame_knight.json");
    public RenderFlameKnight(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityFlameKnight>builder(MODEL_RESLOC, TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .addRenderLayer(new AzBlockAndItemLayer<UUID, EntityFlameKnight>() {
                    @Override
                    public ItemStack itemStackForBone(AzBone bone, EntityFlameKnight animatable) {
                        if (bone.getName().equals("HoldItem")) {
                            return animatable.getItemFromKnightHand(EntityFlameKnight.KNIGHT_HAND.HAND);
                        }

                        return null;
                    }

                    @Override
                    protected ItemCameraTransforms.TransformType getTransformTypeForStack(
                            AzBone bone,
                            ItemStack stack,
                            EntityFlameKnight animatable
                    ) {
                        return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
                    }

                    @Override
                    protected void renderItemForBone(
                            AzRendererPipelineContext<UUID, EntityFlameKnight> context,
                            AzBone bone,
                            ItemStack itemStack,
                            EntityFlameKnight animatable
                    ) {
                        context.poseStack().mulPose(Vector3f.XP.rotationDegrees(90));
                        context.poseStack().mulPose(Vector3f.YP.rotationDegrees(0));
                        context.poseStack().mulPose(Vector3f.ZP.rotationDegrees(0f));
                        context.poseStack().translate(0D, -0.1D, 0D);
                        context.poseStack().scale(0.75f, 0.75f, 0.75f);
                        super.renderItemForBone(context, bone, itemStack, animatable);
                    }
                })
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new FlameKnightAnimator("flame_knight"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .addRenderLayer(new LayerFlameHat())
                        .setShadowRadius(0.4F)
                        .build()
                , renderManager);
    }
}
