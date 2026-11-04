package com.dungeon_additions.da.entity.render.frost_dungeon;


import com.dungeon_additions.da.animation.AzLib.animators.DraugrRangerAnimator;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugrRanger;
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


import javax.annotation.Nullable;
import java.util.UUID;

public class RenderDraugrRanger extends AzEntityRenderer<EntityDraugrRanger> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr/draugr_ranger.png");
    private static final ResourceLocation TEXTURE_TWO = new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr/draugr_ranger_2.png");
    private static final ResourceLocation TEXTURE_THREE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr/draugr_ranger_3.png");
    private static final ResourceLocation TEXTURE_FOUR = new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr/draugr_ranger_4.png");

    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/frost/geo.draugr_ranger.json");
    public RenderDraugrRanger(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityDraugrRanger>builder($ -> MODEL_RESLOC, entity -> entity.getSkin() == 4 ? TEXTURE_FOUR :
                                entity.getSkin() == 3 ? TEXTURE_THREE : entity.getSkin() == 2 ? TEXTURE_TWO : TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_TWO))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_THREE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_FOUR))
                .addRenderLayer( new AzBlockAndItemLayer<UUID, EntityDraugrRanger>() {
                    @Override
                    public ItemStack itemStackForBone(AzBone bone, EntityDraugrRanger animatable) {
                        if (bone.getName().equals("RHoldJoint")) {
                            return animatable.getItemFromKnightHand(EntityDraugrRanger.RANGER_HAND.HAND);
                        }

                        return null;
                    }

                    @Override
                    protected ItemCameraTransforms.TransformType getTransformTypeForStack(
                            AzBone bone,
                            ItemStack stack,
                            EntityDraugrRanger animatable
                    ) {
                        return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
                    }

                    @Override
                    protected void renderItemForBone(
                            AzRendererPipelineContext<UUID, EntityDraugrRanger> context,
                            AzBone bone,
                            ItemStack itemStack,
                            EntityDraugrRanger animatable
                    ) {
                        context.poseStack().mulPose(Vector3f.XP.rotationDegrees(0F));
                        context.poseStack().mulPose(Vector3f.YP.rotationDegrees(0F));
                        context.poseStack().mulPose(Vector3f.ZP.rotationDegrees(0f));
                        context.poseStack().translate(0D, -0.1D, 0D);
                        context.poseStack().scale(1f, 1f, 1f);
                        super.renderItemForBone(context, bone, itemStack, animatable);
                    }
                })
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DraugrRangerAnimator("draugr_ranger"))

                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.25F)
                        .build()
                , renderManager);
    }
}
