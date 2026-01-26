package com.dungeon_additions.da.entity.flame_knight.misc;

import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ProjectileFlameBlade extends Projectile {

    protected static final float AREA_FACTOR = 0.5f;

    public static final int PARTICLE_AMOUNT = 1;

    public ProjectileFlameBlade(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.setSize(0.5F, 0.5F);
        this.isImmuneToFire = true;
    }

    public ProjectileFlameBlade(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setSize(0.5F, 0.5F);
        this.isImmuneToFire = true;
    }

    public ProjectileFlameBlade(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.setSize(0.5F, 0.5F);
        this.isImmuneToFire = true;
    }

    @Override
    protected void spawnParticles() {

            for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {

                    Vec3d pos = ModUtils.entityPos(this).add(new Vec3d(this.motionX * ModRand.getFloat(0.25f), 0.25, this.motionZ * ModRand.getFloat(0.25f)));
                    world.spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y, pos.z, 0, 0, 0, ModRand.range(10, 15));

            }

    }

    protected int hitDelay = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();
        // If the projectile hits water and looses all of its velocity, despawn
        if (!world.isRemote && Math.abs(this.motionX) + Math.abs(this.motionZ) < 0.01f) {
            this.setDead();
        }
        hitDelay--;

        if(hitDelay < 0) {
            this.onQuakeUpdate();
        }

    }

    protected void onQuakeUpdate() {
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(AREA_FACTOR).expand(0, 0.25f, 0));
        for (Entity entity : list) {
            if (entity instanceof EntityLivingBase && this.shootingEntity != null && entity != this.shootingEntity) {
                    entity.setFire(4);
                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.PROJECTILE)
                        .indirectEntity(shootingEntity)
                        .directEntity(this)
                        .stoppedByArmorNotShields().build();

                entity.attackEntityFrom(source, this.getDamage());
                hitDelay = 4;
                this.playSound(SoundsHandler.INCENDIUM_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            }
        }
    }

    public float getBrightness()
    {
        return 1.0F;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        return 15720000;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {return false;}

    @Override
    protected void onHit(RayTraceResult result) {

    }
}
