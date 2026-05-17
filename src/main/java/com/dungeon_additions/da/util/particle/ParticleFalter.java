package com.dungeon_additions.da.util.particle;

import com.dungeon_additions.da.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleFalter extends ParticleSSBase {

    private int hangTime;

    private static final ResourceLocation PIXEL_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/particle/faltered.png");

    public ParticleFalter(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, float red, float green, float blue)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = (int)(30);
        this.texSheetSeg = 1;
        this.renderYOffset = this.height / 2;
        this.particleScale =  1.0F;
    }

    public void onUpdate()
    {
        super.onUpdate();
        if(particleMaxAge >= 15) {
            ++hangTime;
            this.setAlphaF(1.0F - ((float)hangTime / (float)this.particleMaxAge));
        }
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return 15728880; }

    public Vec3d[] particleVertexRendering(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, float particleSize)
    {
        /* 90 degrees tilt, since this current math makes 0 completely vertical, when we want a more horizontal tilt. */
        double tilt = Math.toRadians(particleAge * 2);
        double cos = Math.cos(tilt);
        double sin = Math.sin(tilt);
        rotationZ = (float) (rotationZ + tilt);
        return new Vec3d[]
                {
                        new Vec3d((double) (-rotationX * particleSize - rotationXY * particleSize), (double) (-rotationZ * particleSize), (double) (-rotationYZ * particleSize - rotationXZ * particleSize)),
                        new Vec3d((double) (-rotationX * particleSize + rotationXY * particleSize), (double) (rotationZ * particleSize), (double) (-rotationYZ * particleSize + rotationXZ * particleSize)),
                        new Vec3d((double) (rotationX * particleSize + rotationXY * particleSize), (double) (rotationZ * particleSize), (double) (rotationYZ * particleSize + rotationXZ * particleSize)),
                        new Vec3d((double) (rotationX * particleSize - rotationXY * particleSize), (double) (-rotationZ * particleSize), (double) (rotationYZ * particleSize - rotationXZ * particleSize))
                };
    }


    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            return new ParticleFalter(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ,  224, 224, 224); }
    }
}
