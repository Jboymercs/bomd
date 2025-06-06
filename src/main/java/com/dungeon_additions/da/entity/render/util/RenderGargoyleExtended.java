package com.dungeon_additions.da.entity.render.util;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.MatrixStack;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.function.Function;

public abstract class RenderGargoyleExtended <T extends EntityAbstractBase & IAnimatable> extends GeoEntityRenderer<T> {
    protected double widthScale;
    protected double heightScale;

    public final Function<T, ResourceLocation> TEXTURE_GETTER;
    public final Function<T, ResourceLocation> MODEL_ID_GETTER;

    protected RenderGargoyleExtended(RenderManager renderManager, AnimatedGeoModel<T> modelProvider) {
        this(renderManager, modelProvider, 1D, 1D, 0);
    }

    protected RenderGargoyleExtended(RenderManager renderManager, AnimatedGeoModel<T> modelProvider, double widthScale, double heightScale, float shadowSize) {
        super(renderManager, modelProvider);

        this.MODEL_ID_GETTER = modelProvider::getModelLocation;
        this.TEXTURE_GETTER = modelProvider::getTextureLocation;

        this.shadowSize = shadowSize;
        this.widthScale = widthScale;
        this.heightScale = heightScale;

        // layers

    }

    /*
     * 0 => Normal model
     * 1 => Magical armor overlay
     */
    private int currentModelRenderCycle = 0;

    // Entrypoint for rendering, calls everything else
    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.currentModelRenderCycle = 0;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    // Rendercall to render the model itself
    @Override
    public void render(GeoModel model, T animatable, float partialTicks, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTicks, red, green, blue, alpha);
        this.currentModelRenderCycle++;
    }

    protected double getWidthScale(T entity) {
        return this.widthScale * entity.getSizeVariation();
    }

    protected double getHeightScale(T entity) {
        return this.heightScale * entity.getSizeVariation();
    }

    @Override
    public void renderEarly(T animatable, float ticks, float red, float green, float blue, float partialTicks) {
        if (this.currentModelRenderCycle == 0 /* Pre-Layers */) {
            double width = this.getWidthScale(animatable);
            double height = this.getHeightScale(animatable);
            GlStateManager.scale(width, height, width);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.TEXTURE_GETTER.apply(entity);
    }

    private T currentEntityBeingRendered;

    @Override
    public void renderLate(T animatable, float ticks, float red, float green, float blue, float partialTicks) {
        super.renderLate(animatable, ticks, red, green, blue, partialTicks);
        this.currentEntityBeingRendered = animatable;
    }

    @Override
    public void renderRecursively(BufferBuilder builder, GeoBone bone, float red, float green, float blue, float alpha) {
        boolean customTextureMarker = this.currentModelRenderCycle == 0 && this.getTextureForBone(bone.getName(), this.currentEntityBeingRendered) != null;
        if (customTextureMarker) {
            this.bindTexture(this.getTextureForBone(bone.getName(), this.currentEntityBeingRendered));
        }

        if (this.currentModelRenderCycle == 0) {
            ItemStack boneItem = this.getHeldItemForBone(bone.getName(), this.currentEntityBeingRendered);
            IBlockState boneBlock = this.getHeldBlockForBone(bone.getName(), this.currentEntityBeingRendered);
            if (boneItem != null || boneBlock != null) {
                // Huge thanks to McHorse and Gecko to get this to work!!
                if (boneItem != null) {
                    Tessellator.getInstance().draw();

                    GlStateManager.pushMatrix();
                    multiplyMatrix(IGeoRenderer.MATRIX_STACK, bone);
                    GlStateManager.scale(0.5f, 0.5f, 0.5f);
                    GlStateManager.translate(0.f, 0.f, -0.4f);
                    //float angle = (System.currentTimeMillis() / 40) % 360;
                    GlStateManager.rotate(-90, 0.5F, 0, 0);
                    this.preRenderItem(boneItem, bone.getName(), this.currentEntityBeingRendered);

                    Minecraft.getMinecraft().getItemRenderer().renderItem(this.currentEntityBeingRendered, boneItem, this.getCameraTransformForItemAtBone(boneItem, bone.getName()));

                    this.postRenderItem(boneItem, bone.getName(), this.currentEntityBeingRendered);

                    GlStateManager.popMatrix();
                }
                if (boneBlock != null) {

                    Tessellator.getInstance().draw();

                    GlStateManager.pushMatrix();
                    multiplyMatrix(IGeoRenderer.MATRIX_STACK, bone);
                    GlStateManager.scale(0.8f, 0.8f, 0.8f);
                    GlStateManager.translate(0.f, 0.25f, -0.4f);
                    //float angle = (System.currentTimeMillis() / 40) % 360;
                    GlStateManager.rotate(0, 0F, 0, 0);
                    this.preRenderBlock(boneBlock, bone.getName(), this.currentEntityBeingRendered);

                    this.renderBlock(boneBlock, this.currentEntityBeingRendered);

                    this.postRenderBlock(boneBlock, bone.getName(), this.currentEntityBeingRendered);
                    GlStateManager.popMatrix();
                }

                this.bindTexture(this.getEntityTexture(this.currentEntityBeingRendered));

                builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            }
        }
        // TODO: ONly reimplement for the armor layer!!
        /*
         * if (bone.getName().equalsIgnoreCase("root") && this.renderPass == 1) {
         * bone.setScaleX(bone.getScaleX() + 0.05F);
         * bone.setScaleZ(bone.getScaleZ() + 0.05F);
         * bone.setScaleY(bone.getScaleY() + 0.025F);
         * }
         */
        super.renderRecursively(builder, bone, red, green, blue, alpha);
        if (customTextureMarker) {
            this.bindTexture(this.getEntityTexture(this.currentEntityBeingRendered));
        }
    }

    private void renderBlock(IBlockState iBlockState, Entity currentEntity) {
        BlockRenderUtil.renderBlockAtEntity(iBlockState, currentEntity, this);
    }

    // Code by McHorse
    private static Matrix4f matrix = new Matrix4f();

    /**
     * Multiply given matrix stack onto OpenGL's matrix stack
     */
    public static void multiplyMatrix(MatrixStack stack, GeoBone bone) {
        matrix.set(stack.getModelMatrix());
        matrix.transpose();

        MatrixUtil.matrixToFloat(MatrixUtil.floats, matrix);
        MatrixUtil.buffer.clear();
        MatrixUtil.buffer.put(MatrixUtil.floats);
        MatrixUtil.buffer.flip();

        GlStateManager.multMatrix(MatrixUtil.buffer);
        GlStateManager.translate(bone.rotationPointX / 16, bone.rotationPointY / 16, bone.rotationPointZ / 16);
    }

    /*
     * Return null if there is no item
     */
    @Nullable
    protected abstract ItemStack getHeldItemForBone(String boneName, T currentEntity);

    protected abstract ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName);

    /*
     * Return null if there is no held block
     */
    @Nullable
    protected abstract IBlockState getHeldBlockForBone(String boneName, T currentEntity);

    protected abstract void preRenderItem(ItemStack item, String boneName, T currentEntity);

    protected abstract void preRenderBlock(IBlockState block, String boneName, T currentEntity);

    protected abstract void postRenderItem(ItemStack item, String boneName, T currentEntity);

    protected abstract void postRenderBlock(IBlockState block, String boneName, T currentEntity);

    /*
     * Return null, if the entity's texture is used
     */
    @Nullable
    protected abstract ResourceLocation getTextureForBone(String boneName, T currentEntity);
}
