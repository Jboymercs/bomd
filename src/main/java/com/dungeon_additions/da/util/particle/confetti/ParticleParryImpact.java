package com.dungeon_additions.da.util.particle.confetti;

import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.particle.ParticleSSBase;
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
public class ParticleParryImpact extends ParticleSSBase {
    private static final ResourceLocation PIXEL_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/particle/parry_impact.png");
    private int hangTime;
    protected ParticleParryImpact(TextureManager textureManager, World worldIn, double x, double y, double z, double movementX, double movementY, double movementZ)
    { this(textureManager, worldIn, x, y, z, movementX, movementY, movementZ, 1F, 1F, 1F); }

    public ParticleParryImpact(TextureManager textureManager, World world, double x, double y, double z, double movementX, double movementY, double movementZ, float red, float green, float blue)
    {
        super(textureManager, world, x, y, z, movementX, movementY, movementZ, PIXEL_TEXTURE, 0);
        this.textureManager = textureManager;
        this.motionX = movementX;
        this.motionY = movementY;
        this.motionZ = movementZ;
        this.particleMaxAge = (int)7;
        this.texSheetSeg = 2;
        this.renderYOffset = 0.01F;
        this.particleScale =  1;
    }

    public void onUpdate()
    {
        super.onUpdate();
        // this.texSpot = this.particleAge * 3 / (this.particleMaxAge);
        this.particleScale+= 2.55F;
        this.texSpot = Math.min(this.particleAge * 5 / (this.particleMaxAge), 3);
        if(particleAge > 3) {
            hangTime++;
            this.setAlphaF(1.0F - ((float) hangTime / (float) this.particleMaxAge));
        }
    }


    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleId, World world, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
        {
            return new ParticleParryImpact(Minecraft.getMinecraft().getTextureManager(), world, posX, posY, posZ, 0, 0, 0, 102, 145, 45); }
    }
}
