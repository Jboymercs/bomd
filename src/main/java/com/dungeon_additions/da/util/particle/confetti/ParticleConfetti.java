package com.dungeon_additions.da.util.particle.confetti;

import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.particle.ParticlePixel;
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
public class ParticleConfetti extends ParticleSSBase {

    private static final ResourceLocation PIXEL_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/particle/confetti.png");

    public ParticleConfetti(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int color)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = (int)(48.0 / (Math.random() * 0.8 + 0.2));
        this.texSheetSeg = 1;
        this.renderYOffset = this.height / 2;
        this.particleScale =  0.4F + (float) (Math.random() * 0.7F);
        this.setAlphaF(0.9F);
        float[] colors = decimalIntToRGB(color);
        float shade = 0.9F;
        setRBGColorF( 1.0F - (1.0F - colors[0]) * shade, 1.0F - (1.0F - colors[1]) * shade, 1.0F - (1.0F - colors[2]) * shade );
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            return new ParticleConfetti(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, 0, -0.03, 0, parameters[0]); }
    }
}
