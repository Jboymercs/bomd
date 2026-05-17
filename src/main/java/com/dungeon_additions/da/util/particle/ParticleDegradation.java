package com.dungeon_additions.da.util.particle;

import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.particle.confetti.ParticleDotLight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleDegradation extends ParticleSSBase{
    private static final ResourceLocation PIXEL_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/particle/degradation.png");

    public ParticleDegradation(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, float red, float green, float blue)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = (int)(50);
        this.texSheetSeg = 4;
        this.renderYOffset = this.height / 2;
        this.particleScale =  2.0F;
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return 15728880; }

    public void onUpdate()
    {
        super.onUpdate();
        this.texSpot = this.particleAge * 15 / (this.particleMaxAge);
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            return new ParticleDegradation(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ,  224, 224, 224); }
    }
}
