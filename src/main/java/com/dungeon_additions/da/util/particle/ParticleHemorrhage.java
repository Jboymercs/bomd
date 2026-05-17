package com.dungeon_additions.da.util.particle;

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
public class ParticleHemorrhage extends ParticleSSBase {
    private static final ResourceLocation PIXEL_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/particle/hemorrhage.png");

    /** Time before the Drip falls */
    private int hangTime;
    /** Time after the drip has landed */
    private int landedTime;

    public ParticleHemorrhage(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, float red, float green, float blue)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = (int)(50);
        this.texSheetSeg = 2;
        this.renderYOffset = this.height / 2;
        this.particleScale =  2.0F;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (!this.onGround)
        {
            ++hangTime;
            this.texSpot = Math.min(2, (this.hangTime / 4) * 3);
            this.motionY -= (double)0.1F;
        }
        else
        {
            ++landedTime;

            this.texSpot = Math.min(4, 2 + (this.landedTime / 2));
            this.setAlphaF(1.0F - ((float)landedTime / (float)this.particleMaxAge));
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            return new ParticleHemorrhage(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, speedX, speedY, speedZ,  224, 224, 224); }
    }

}
