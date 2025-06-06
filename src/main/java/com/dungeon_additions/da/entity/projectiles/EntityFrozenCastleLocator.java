package com.dungeon_additions.da.entity.projectiles;

import com.dungeon_additions.da.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFrozenCastleLocator extends Projectile{
    public EntityFrozenCastleLocator(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
    }

    public EntityFrozenCastleLocator(World worldIn) {
        super(worldIn);
        this.setSize(0.25F, 0.25F);
        this.setNoGravity(true);
        this.isLocatorItem = true;
    }

    public EntityFrozenCastleLocator(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.despawnTimer = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(x, y, z);
        this.setNoGravity(true);
        this.isLocatorItem = true;
    }

    private double targetX;
    private double targetY;
    private double targetZ;
    private int despawnTimer;
    private boolean shatterOrDrop;



    protected void entityInit()
    {
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;

        if (Double.isNaN(d0))
        {
            d0 = 4.0D;
        }

        d0 = d0 * 64.0D;
        return distance < d0 * d0;
    }



    public void moveTowards(BlockPos pos)
    {
        double d0 = (double)pos.getX();
        int i = pos.getY();
        double d1 = (double)pos.getZ();
        double d2 = d0 - this.posX;
        double d3 = d1 - this.posZ;
        float f = MathHelper.sqrt(d2 * d2 + d3 * d3);

        if (f > 12.0F)
        {
            this.targetX = this.posX + d2 / (double)f * 12.0D;
            this.targetZ = this.posZ + d3 / (double)f * 12.0D;
            this.targetY = this.posY + 8.0D;
        }
        else
        {
            this.targetX = d0;
            this.targetY = (double)i;
            this.targetZ = d1;
        }

        this.despawnTimer = 0;
        this.shatterOrDrop = this.rand.nextInt(5) > 0;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * (180D / Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }
    }

    public void onUpdate()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

        for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        if (!this.world.isRemote)
        {
            double d0 = this.targetX - this.posX;
            double d1 = this.targetZ - this.posZ;
            float f1 = (float)Math.sqrt(d0 * d0 + d1 * d1);
            float f2 = (float)MathHelper.atan2(d1, d0);
            double d2 = (double)f + (double)(f1 - f) * 0.0025D;

            if (f1 < 1.0F)
            {
                d2 *= 0.8D;
                this.motionY *= 0.8D;
            }

            this.motionX = Math.cos((double)f2) * d2;
            this.motionZ = Math.sin((double)f2) * d2;

            if (this.posY < this.targetY)
            {
                this.motionY += (1.0D - this.motionY) * 0.014999999664723873D;
            }
            else
            {
                this.motionY += (-1.0D - this.motionY) * 0.014999999664723873D;
            }
        }

        float f3 = 0.25F;

        if (this.isInWater())
        {
            for (int i = 0; i < 4; ++i)
            {
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
            }
        }
        else
        {
            this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX - this.motionX * 0.25D + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * 0.25D - 0.5D, this.posZ - this.motionZ * 0.25D + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ);
        }

        if (!this.world.isRemote)
        {
            this.setPosition(this.posX, this.posY, this.posZ);
            ++this.despawnTimer;

            if (this.despawnTimer > 80 && !this.world.isRemote)
            {
                this.playSound(SoundEvents.ENTITY_ENDEREYE_DEATH, 1.0F, 1.0F);
                this.setDead();
                    this.world.playEvent(2003, new BlockPos(this), 0);
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
    }

    public float getBrightness()
    {
        return 1.0F;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        return 15728880;
    }

    public boolean canBeAttackedWithItem()
    {
        return false;
    }
}
