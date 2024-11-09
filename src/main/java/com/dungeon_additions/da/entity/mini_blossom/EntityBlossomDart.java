package com.dungeon_additions.da.entity.mini_blossom;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

public abstract class EntityBlossomDart extends EntityArrow implements IProjectile {
    private int ticksInAir;

    private boolean hasNoGravity;

    public EntityBlossomDart(World worldIn)
    {
        super(worldIn);
    }

    public EntityBlossomDart(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter);
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.ticksInAir == 500)
        {
            this.setDead();
        }

        if (!this.onGround)
        {
            ++this.ticksInAir;
        }
    }

    @Override
    public boolean hasNoGravity()
    {
        return this.hasNoGravity;
    }

    @Override
    public void setNoGravity(boolean flight)
    {
        this.hasNoGravity = flight;
    }
}
