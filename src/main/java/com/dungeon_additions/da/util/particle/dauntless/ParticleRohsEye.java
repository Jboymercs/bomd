package com.dungeon_additions.da.util.particle.dauntless;

import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.particle.ParticleFieryRespite;
import com.dungeon_additions.da.util.particle.ParticleSSBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleRohsEye extends ParticleSSBase {
    private static final ResourceLocation PIXEL_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/particle/rohs_eye.png");

    public ParticleRohsEye(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, float red, float green, float blue) {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = (int)(40);
        this.particleAngle = this.rand.nextFloat() - this.rand.nextFloat();
        this.texSheetSeg = 4;
        this.renderYOffset = this.height / 2;
        this.particleScale =  3F + ModRand.getFloat(1F);
    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return 15728880; }

    public void onUpdate()
    {
        super.onUpdate();
        this.prevParticleAngle = this.particleAngle;
        this.texSpot = this.particleAge * 15 / (this.particleMaxAge);
    }

    public Vec3d[] particleVertexRendering(BufferBuilder buffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, float particleSize)
    {
        Vec3d[] wow = new Vec3d[]{
                new Vec3d(-rotationX * particleSize - rotationXY * particleSize, -rotationZ * particleSize, -rotationYZ * particleSize - rotationXZ * particleSize),
                new Vec3d(-rotationX * particleSize + rotationXY * particleSize, rotationZ * particleSize, -rotationYZ * particleSize + rotationXZ * particleSize),
                new Vec3d(rotationX * particleSize + rotationXY * particleSize, rotationZ * particleSize, rotationYZ * particleSize + rotationXZ * particleSize),
                new Vec3d(rotationX * particleSize - rotationXY * particleSize, -rotationZ * particleSize, rotationYZ * particleSize - rotationXZ * particleSize)
        };

        float angle = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
        float f9 = MathHelper.cos(angle * 0.5F);
        float f10 = MathHelper.sin(angle * 0.5F) * (float)cameraViewDir.x;
        float f11 = MathHelper.sin(angle * 0.5F) * (float)cameraViewDir.y;
        float f12 = MathHelper.sin(angle * 0.5F) * (float)cameraViewDir.z;
        Vec3d vec3d = new Vec3d((double)f10, (double)f11, (double)f12);

        for (int l = 0; l < 4; ++l)
        {
            wow[l] = vec3d.scale(2.0D * wow[l].dotProduct(vec3d)).add(wow[l].scale((double)(f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(wow[l]).scale((double)(3.0F * f9)));
        }

        return wow;
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            return new ParticleRohsEye(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ,  224, 224, 224); }
    }
}
