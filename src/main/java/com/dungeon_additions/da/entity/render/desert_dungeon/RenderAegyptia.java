package com.dungeon_additions.da.entity.render.desert_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityAegyptia;
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

public class RenderAegyptia extends AzEntityRenderer<EntityAegyptia> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptia.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptia_2.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptia_3.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/desert/geo.aegyptia.json");


    public RenderAegyptia(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityAegyptia>builder($ -> MODEL_RESLOC, entity -> entity.getSkin() == 3 ? TEXTURE_3 :
                                entity.getSkin() == 2 ? TEXTURE_2 : TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_2))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_3))
                        .addRenderLayer(new AzBlockAndItemLayer<UUID, EntityAegyptia>() {
                            @Override
                            public ItemStack itemStackForBone(AzBone bone, EntityAegyptia animatable) {
                                if (bone.getName().equals("HoldHandL")) {
                                    return animatable.getItemFromGargoyleHand(EntityAegyptia.AEGYPTIA_HAND.HAND);
                                }

                                return null;
                            }

                            @Override
                            protected ItemCameraTransforms.TransformType getTransformTypeForStack(
                                    AzBone bone,
                                    ItemStack stack,
                                    EntityAegyptia animatable
                            ) {
                                return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
                            }

                            @Override
                            protected void renderItemForBone(
                                    AzRendererPipelineContext<UUID, EntityAegyptia> context,
                                    AzBone bone,
                                    ItemStack itemStack,
                                    EntityAegyptia animatable
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

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("aegyptia", false, true, "HeadJoint"))

                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.25F)
                        .build()
                , renderManager);
    }
}
