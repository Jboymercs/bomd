package com.dungeon_additions.da.util.particle.dauntless;

import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.particle.ParticleSSBase;
import com.sun.jna.platform.win32.WinBase;
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
public class ParticleDauntlessBuff extends ParticleSSBase {
    private static final ResourceLocation PIXEL_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/particle/dauntless_buff.png");
    final float swirlScrew;
    final float swirlRadius;
    final float swirlSpeed;
    final Entity target;
    double orbitX;
    double orbitY;
    double orbitZ;

    public ParticleDauntlessBuff(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, Entity target)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = 600;
        this.particleAge = 0;
        this.particleScale = 3.5F;
        this.texSheetSeg = 1;
        this.canCollide = false;

        this.particleAngle = this.rand.nextFloat() - this.rand.nextFloat();
        this.swirlScrew = rand.nextFloat() * ((float)Math.PI * 2F);
        this.swirlRadius = (rand.nextFloat() * 0.3F) + 0.8F;
        this.swirlSpeed = 0.1F;
        this.target = target;
        this.orbitX = posX;
        this.orbitY = posY;
        this.orbitZ = posZ;
    }

    public void onUpdate()
    {
        this.prevParticleAngle = this.particleAngle;

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        double x = Math.cos(particleAge * swirlSpeed + swirlScrew) * swirlRadius;
        double z = Math.sin(particleAge * swirlSpeed + swirlScrew) * swirlRadius;
        double y = Math.sin(particleAge * swirlSpeed) * 0.1F;

        if (target != null && target.isEntityAlive())
        {
            orbitX = target.posX;
            orbitY = target.posY + 1.5;
            orbitZ = target.posZ;
        }

        this.posX = orbitX + x;
        this.posY = orbitY + y;
        this.posZ = orbitZ + z;

        if (this.particleAge > this.particleMaxAge / 2) this.setAlphaF(1.2F - (float)particleAge / (float)this.particleMaxAge - ((float)(this.particleMaxAge / 2)));
        if (this.particleAge++ >= this.particleMaxAge) this.setExpired();
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
            wow[l] = vec3d.scale(2.0D * wow[l].dotProduct(vec3d)).add(wow[l].scale((double)(f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(wow[l]).scale((double)(2.0F * f9)));
        }

        return wow;
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
            Entity target = null;

            if (parameters.length > 0) target = world.getEntityByID(parameters[0]);

            return new ParticleDauntlessBuff(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, target);
        }
    }
}
