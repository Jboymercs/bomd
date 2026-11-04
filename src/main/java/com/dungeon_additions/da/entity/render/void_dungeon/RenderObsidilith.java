package com.dungeon_additions.da.entity.render.void_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.render.layer.LayerObsidilithShield;
import com.dungeon_additions.da.entity.void_dungeon.EntityObsidilith;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderObsidilith extends AzEntityRenderer<EntityObsidilith> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/obsidilith.png");
    private static final ResourceLocation TEXTURE_FLAME = new ResourceLocation(ModReference.MOD_ID, "textures/entity/obsidilith/obsidilith_orange.png");
    private static final ResourceLocation TEXTURE_BLUE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/obsidilith/obsidilith_blue.png");
    private static final ResourceLocation TEXTURE_YELLOW = new ResourceLocation(ModReference.MOD_ID, "textures/entity/obsidilith/obsidilith_yellow.png");
    private static final ResourceLocation TEXTURE_PURPLE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/obsidilith/obsidilith_purple.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/obsidilith/geo.obsidilith.json");

    public RenderObsidilith(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityObsidilith>builder($ -> MODEL_RESLOC, e ->
                                e.isFlameAttack() ? TEXTURE_FLAME : e.isBlueAttack() ? TEXTURE_BLUE :
                                        e.isPurpleAttack() ? TEXTURE_PURPLE : e.isYellowAttack() ? TEXTURE_YELLOW :TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_FLAME))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_BLUE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_YELLOW))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_PURPLE))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("obsidilith", false, false))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .addRenderLayer(new LayerObsidilithShield())
                        .setShadowRadius(0.7F)
                        .build()
                , renderManager);
    }
}
