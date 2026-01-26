package com.dungeon_additions.da.util.particle.cuts;

import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.particle.ParticleSSBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleRightCut extends ParticleSSBase {
    private static final ResourceLocation PIXEL_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/particle/right_cut.png");

    public ParticleRightCut(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int color)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.texSheetSeg = 2;
        this.renderYOffset = this.height / 2;
        this.particleScale =  1F + (float) (Math.random() * 2.4F);
        this.setAlphaF(0.9F);
        float[] colors = decimalIntToRGB(color);
        float shade = 0.9F;
        setRBGColorF( 1.0F - (1.0F - colors[0]) * shade, 1.0F - (1.0F - colors[1]) * shade, 1.0F - (1.0F - colors[2]) * shade );
    }

    public void onUpdate()
    {
        super.onUpdate();
        // this.texSpot = this.particleAge * 3 / (this.particleMaxAge);
        this.texSpot = Math.min(this.particleAge * 5 / (this.particleMaxAge), 3);
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return 15728880; }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            return new ParticleRightCut(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]); }
    }
}
