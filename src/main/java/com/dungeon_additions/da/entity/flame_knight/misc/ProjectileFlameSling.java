package com.dungeon_additions.da.entity.flame_knight.misc;

import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ProjectileFlameSling extends Projectile {
    protected static final float AREA_FACTOR = 0.5f;

    public static final int PARTICLE_AMOUNT = 1;
    protected int updates = 5;

    public ProjectileFlameSling(World worldIn, EntityLivingBase throwerIn, float baseDamage, ItemStack stack) {
        super(worldIn, throwerIn, baseDamage);
        this.setNoGravity(true);
        this.setSize(0.25F, 1);
        this.isImmuneToFire = true;
    }

    private boolean setFlame;

    public ProjectileFlameSling(World worldIn, EntityLivingBase throwerIn, float baseDamage, ItemStack stack, boolean setAflame) {
        super(worldIn, throwerIn, baseDamage);
        this.setNoGravity(true);
        this.setSize(0.25F, 1);
        this.setFlame = setAflame;
        this.isImmuneToFire = true;
    }

    public ProjectileFlameSling(World worldIn) {
        super(worldIn);
    }

    public ProjectileFlameSling(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void spawnParticles() {
        IBlockState block = world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ));
        if (block.isFullCube()) {
            for (int i = 0; i < 5; i++) {
                ParticleManager.spawnBreak(world, this.getPositionVector().add(ModRand.randVec().scale(1.0f).add(ModUtils.yVec(0.75f))), Item.getItemFromBlock(block.getBlock()), ModRand.randVec().scale(0.1).add(ModUtils.yVec(0.1f)));
            }

            for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
                float height = 2 + ModRand.getFloat(0.5f);
                for (float y = 0; y < height; y += 0.2f) {
                    Vec3d pos = ModUtils.entityPos(this).add(new Vec3d(this.motionX * ModRand.getFloat(0.5f), y, this.motionZ * ModRand.getFloat(0.5f)));
                    world.spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y, pos.z, 0, 0, 0, ModRand.range(10, 15));
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        // Keeps the projectile on the surface of the ground
        for (int i = 0; i < updates; i++) {
            if (!world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).isFullCube()) {
                this.setPosition(this.posX, this.posY - 0.25f, this.posZ);
            } else if (world.getBlockState(new BlockPos(this.posX, this.posY + 1, this.posZ)).isFullCube()) {
                this.setPosition(this.posX, this.posY + 0.25f, this.posZ);
            }
        }

        onQuakeUpdate();

        // If the projectile hits water and looses all of its velocity, despawn
        if (!world.isRemote && Math.abs(this.motionX) + Math.abs(this.motionZ) < 0.01f) {
            this.setDead();
        }
    }

    protected void onQuakeUpdate() {
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(AREA_FACTOR).expand(0, 0.25f, 0));
        for (Entity entity : list) {
            if (entity instanceof EntityLivingBase && this.shootingEntity != null && entity != this.shootingEntity) {
                int burnTime = this.isBurning() ? 5 : 0;
                entity.setFire(burnTime);

                if(this.setFlame) {
                    entity.setFire(8);
                }
                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.PROJECTILE)
                        .indirectEntity(shootingEntity)
                        .directEntity(this)
                        .stoppedByArmorNotShields().build();

                entity.attackEntityFrom(source, this.getDamage());

            }
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {

    }
}
