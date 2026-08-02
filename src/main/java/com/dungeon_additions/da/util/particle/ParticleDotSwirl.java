package com.dungeon_additions.da.util.particle;

import com.dungeon_additions.da.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleDotSwirl extends ParticleSSBase{

    private static final ResourceLocation PIXEL_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/particle/confetti.png");
    private int hangTime;
    final float swirlScrew;
    private float swirlRadius;
    final float swirlSpeed;
    final Vec3d originPos;
    double orbitX;
    double orbitY;
    double orbitZ;


    public ParticleDotSwirl(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, int color)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.originPos = new Vec3d(x, y, z);
        this.orbitX = posX;
        this.orbitY = posY;
        this.orbitZ = posZ;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.swirlScrew = rand.nextFloat() * ((float)Math.PI * 2F);
        this.swirlRadius = 1.5F;
        this.swirlSpeed = 0.3F;
        this.particleMaxAge = (int)(100);
        this.texSheetSeg = 1;
        this.renderYOffset = this.height / 2;
        this.particleScale =  0.5F;
        this.setAlphaF(0.9F);
        float[] colors = decimalIntToRGB(color);
        float shade = 0.9F;
        setRBGColorF( 1.0F - (1.0F - colors[0]) * shade, 1.0F - (1.0F - colors[1]) * shade, 1.0F - (1.0F - colors[2]) * shade );
    }

    public void onUpdate()
    {
       // super.onUpdate();
        this.prevParticleAngle = this.particleAngle;

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        double x = Math.cos(particleAge * swirlSpeed + swirlScrew) * swirlRadius;
        double z = Math.sin(particleAge * swirlSpeed + swirlScrew) * swirlRadius;

        if (originPos != null)
        {
            orbitX = originPos.x;

            orbitZ = originPos.z;
        }

        this.posX = orbitX + x;
       // this.posY = orbitY + y;
        this.posZ = orbitZ + z;
        this.posY += 0.02;
        this.swirlRadius -= 0.02F;

        this.particleScale -= 0.01F;

        if(this.onGround) {
            this.hangTime++;
            this.setAlphaF(1.0F - ((float) this.hangTime / (float) this.particleMaxAge));
        }
        if (this.particleAge++ >= this.particleMaxAge) this.setExpired();
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
            return new ParticleDotSwirl(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ, parameters[0]); }
    }
}
