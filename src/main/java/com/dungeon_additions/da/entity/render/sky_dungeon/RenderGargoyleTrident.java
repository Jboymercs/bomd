package com.dungeon_additions.da.entity.render.sky_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.sky_dungeon.EntityTridentGargoyle;
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

public class RenderGargoyleTrident extends AzEntityRenderer<EntityTridentGargoyle> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/trident_gargoyle.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/sky/geo.trident_gargoyle.json");
    public RenderGargoyleTrident(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityTridentGargoyle>builder(MODEL_RESLOC, TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("trident_gargoyle", false, true))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .addRenderLayer(new AzBlockAndItemLayer<UUID, EntityTridentGargoyle>() {
                            @Override
                            public ItemStack itemStackForBone(AzBone bone, EntityTridentGargoyle animatable) {
                                if (bone.getName().equals("HoldPotion")) {
                                    return animatable.getItemFromGargoyleHand(EntityTridentGargoyle.GARGOYLE_HAND.HAND);
                                }

                                return null;
                            }

                            @Override
                            protected ItemCameraTransforms.TransformType getTransformTypeForStack(
                                    AzBone bone,
                                    ItemStack stack,
                                    EntityTridentGargoyle animatable
                            ) {
                                return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
                            }

                            @Override
                            protected void renderItemForBone(
                                    AzRendererPipelineContext<UUID, EntityTridentGargoyle> context,
                                    AzBone bone,
                                    ItemStack itemStack,
                                    EntityTridentGargoyle animatable
                            ) {
                                context.poseStack().mulPose(Vector3f.XP.rotationDegrees(90));
                                context.poseStack().mulPose(Vector3f.YP.rotationDegrees(0));
                                context.poseStack().mulPose(Vector3f.ZP.rotationDegrees(0f));
                                context.poseStack().translate(0D, -0.1D, 0D);
                                context.poseStack().scale(0.75f, 0.75f, 0.75f);
                                super.renderItemForBone(context, bone, itemStack, animatable);
                            }
                        })
                        .setShadowRadius(0.2F)
                        .build()
                , renderManager);
    }
}
