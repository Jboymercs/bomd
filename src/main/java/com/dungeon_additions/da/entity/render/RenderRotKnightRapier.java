package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightRapier;
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

public class RenderRotKnightRapier extends AzEntityRenderer<EntityRotKnightRapier> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/rot_knight/rapier_1.png");
    private static final ResourceLocation TEXTURE_TWO = new ResourceLocation(ModReference.MOD_ID, "textures/entity/rot_knight/rapier_2.png");
    private static final ResourceLocation TEXTURE_THREE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/rot_knight/rapier_3.png");
    private static final ResourceLocation TEXTURE_FOUR = new ResourceLocation(ModReference.MOD_ID, "textures/entity/rot_knight/rapier_4.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/rotknight/geo.rot_knight_r.json");

    public RenderRotKnightRapier(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityRotKnightRapier>builder($ -> MODEL_RESLOC, entity -> entity.getSkin() == 4 ? TEXTURE_FOUR :
                                entity.getSkin() == 3 ? TEXTURE_THREE : entity.getSkin() == 2 ? TEXTURE_TWO : TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_TWO))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_THREE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_FOUR))
                .addRenderLayer(new AzBlockAndItemLayer< UUID, EntityRotKnightRapier>() {
                    @Override
                    public ItemStack itemStackForBone(AzBone bone, EntityRotKnightRapier animatable) {
                        if (bone.getName().equals("HandEquip")) {
                            return animatable.getItemFromKnightHand(EntityRotKnightRapier.ROT_KNIGHT_HAND.HAND);
                        }

                        return null;
                    }

                    @Override
                    protected ItemCameraTransforms.TransformType getTransformTypeForStack(
                            AzBone bone,
                            ItemStack stack,
                            EntityRotKnightRapier animatable
                    ) {
                        return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
                    }

                    @Override
                    protected void renderItemForBone(
                            AzRendererPipelineContext<UUID, EntityRotKnightRapier> context,
                            AzBone bone,
                            ItemStack itemStack,
                            EntityRotKnightRapier animatable
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

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("rot_knight_r", false, true))
                        .setShadowRadius(0.3F)
                        .build()
                , renderManager);
    }
}
