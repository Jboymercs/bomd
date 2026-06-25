package com.dungeon_additions.da.util.particle;


import com.dungeon_additions.da.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleDauntlessBeam extends ParticleSSBase {
    //unused
    final Entity target;


    private static final ResourceLocation PIXEL_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/particle/dauntless_beam.png");
    public ParticleDauntlessBeam(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, Entity target)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = (int)(35);
        this.texSheetSeg = 4;
       // this.renderYOffset = this.height / 12;
        this.particleScale =  7.0F;
        this.target = target;

    }

    @Override
    public int getBrightnessForRender(float partialTicks)
    { return 15728880; }

    public void onUpdate()
    {
        super.onUpdate();
        this.texSpot = this.particleAge * 11 / (this.particleMaxAge);

        if (target != null && target.isEntityAlive())
        {
            this.posX = target.posX;
            this.posY = target.posY;
            this.posZ = target.posZ;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            Entity target = null;

            if (parameters.length > 0) target = world.getEntityByID(parameters[0]);

            return new ParticleDauntlessBeam(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ,  target); }
    }
}
