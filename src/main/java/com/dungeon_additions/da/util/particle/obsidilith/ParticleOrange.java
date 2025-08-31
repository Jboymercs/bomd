package com.dungeon_additions.da.util.particle.obsidilith;


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
public class ParticleOrange extends ParticleSSBase {
    private static final ResourceLocation PIXEL_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/particle/particle_orange.png");

    public ParticleOrange(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int texSpotIn) {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = 20;
        this.texSheetSeg = 8;
        this.canCollide = false;
        this.particleScale = 2.0F;
        this.particleGravity = 0;
    }

    public ParticleOrange(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, float red, float green, float blue) {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = 80;
        this.texSheetSeg = 1;
        this.canCollide = false;
        this.particleScale = 2.0F;
        this.particleGravity = 0;
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return brightnessIncreaseToFull(partialTicks); }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        { return new ParticleOrange(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, 58, 224,  216); }
    }
}
