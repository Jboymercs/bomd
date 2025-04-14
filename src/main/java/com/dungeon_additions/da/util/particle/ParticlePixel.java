package com.dungeon_additions.da.util.particle;

import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticlePixel extends ParticleSSBase{
    private static final ResourceLocation PIXEL_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/particle/pixel.png");

    protected ParticlePixel(TextureManager textureManager, World worldIn, double x, double y, double z, double movementX, double movementY, double movementZ)
    { this(textureManager, worldIn, x, y, z, movementX, movementY, movementZ, 1F, 1F, 1F); }

    public ParticlePixel(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, float red, float green, float blue)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = (int)(64.0 / (Math.random() * 0.8 + 0.2));
        setRBGColorF(red * 0.00392156862F, green * 0.00392156862F,blue * 0.00392156862F);
        this.texSheetSeg = 1;
        this.renderYOffset = this.height / 2;
        this.particleScale =  0.4F + (float) (Math.random() * 0.7F);

    }

    @Override
    public int getBrightnessForRender(float partialTick)
    {
        return 115;
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            return new ParticlePixel(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, world.rand.nextFloat()/3 - world.rand.nextFloat()/3, -0.04, world.rand.nextFloat()/3 - world.rand.nextFloat()/3, 102, 145, 45); }
    }
}
