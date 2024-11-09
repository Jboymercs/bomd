package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.entity.render.util.BlockRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderMoveTile extends Render<EntityMoveTile> {
    private static final BlockRenderer blockRenderer = new BlockRenderer();

    public RenderMoveTile(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityMoveTile entity, double x, double y, double z, float yaw, float tick) {
        if(entity.posY != entity.origin.getY())
            renderShockwaveBlock(entity, x, y, z, yaw, tick);
    }


    public void renderShockwaveBlock(EntityMoveTile entity, double x, double y, double z, float yaw, float tick) {

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x - 0.5F, (float) y, (float) z - 0.5F);
        //Lighting is already handled in the block renderer
        GlStateManager.disableLighting();
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        @SuppressWarnings("deprecation")
        IBlockState state = entity.block.getStateFromMeta(entity.blockMeta);
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        if(model != null) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

            blockRenderer.setLighting((IBlockState blockState, @Nullable EnumFacing facing) -> {
                return state.getPackedLightmapCoords(entity.world, facing != null ? entity.origin.up().offset(facing) : entity.origin.up());
            }).setTint((IBlockState blockState, int tintIndex) -> {
                if(blockState.getBlock() == entity.block)
                    return Minecraft.getMinecraft().getBlockColors().colorMultiplier(state, entity.world, entity.origin, tintIndex);
                else
                    return Minecraft.getMinecraft().getBlockColors().colorMultiplier(state, null, null, tintIndex);
            });

            blockRenderer.renderModel(entity.world, entity.origin, model, state, MathHelper.getPositionRandom(entity.origin), buffer);

            tessellator.draw();
        }
        GlStateManager.enableLighting();
        GL11.glPopMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityMoveTile entity) {
        return null;
    }
}
