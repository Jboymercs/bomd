package com.dungeon_additions.da.entity.render.trader;

import com.dungeon_additions.da.animation.AzLib.animators.MysteriousTraderAnimator;
import com.dungeon_additions.da.entity.trader.EntityMysteriousTrader;
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

public class RenderMysteriousTrader extends AzEntityRenderer<EntityMysteriousTrader> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/mysterious_trader.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/trader/geo.mysterious_trader.json");

    public RenderMysteriousTrader(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityMysteriousTrader>builder(MODEL_RESLOC, TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .addRenderLayer(new AzBlockAndItemLayer<UUID, EntityMysteriousTrader>() {
                            @Override
                            public ItemStack itemStackForBone(AzBone bone, EntityMysteriousTrader animatable) {
                                if (bone.getName().equals("ItemJoint")) {
                                    return animatable.getItemFromRoyalHand(EntityMysteriousTrader.TRADER_HAND.HAND);
                                }

                                return null;
                            }

                            @Override
                            protected ItemCameraTransforms.TransformType getTransformTypeForStack(
                                    AzBone bone,
                                    ItemStack stack,
                                    EntityMysteriousTrader animatable
                            ) {
                                return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
                            }

                            @Override
                            protected void renderItemForBone(
                                    AzRendererPipelineContext<UUID, EntityMysteriousTrader> context,
                                    AzBone bone,
                                    ItemStack itemStack,
                                    EntityMysteriousTrader animatable
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

                        .setAnimatorProvider(() -> new MysteriousTraderAnimator("mysterious_trader"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.25F)
                        .build()
                , renderManager);
    }

}
