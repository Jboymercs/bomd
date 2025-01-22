package com.dungeon_additions.da.entity.frost_dungeon;

import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ProjectileFrostGround extends Projectile {
    protected static final float AREA_FACTOR = 0.5f;

    public static final int PARTICLE_AMOUNT = 1;
    protected int updates = 5;

    public ProjectileFrostGround(World worldIn, EntityLivingBase throwerIn, float baseDamage, ItemStack stack) {
        super(worldIn, throwerIn, baseDamage);
        this.setNoGravity(true);
        this.setSize(0.25F, 1);
    }

    public EntityPlayer player;
    public ProjectileFrostGround(World worldIn, EntityLivingBase throwerIn, float baseDamage, ItemStack stack, EntityPlayer owner) {
        super(worldIn, throwerIn, baseDamage);
        this.setNoGravity(true);
        this.setSize(0.25F, 1);
        this.player = owner;
    }

    public ProjectileFrostGround(World worldIn) {
        super(worldIn);
    }

    public ProjectileFrostGround(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void spawnParticles() {
        IBlockState block = world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ));
        if (block.isFullCube()) {
            for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
                float height = 2 + ModRand.getFloat(0.5f);
                for (float y = 0; y < height; y += 0.2f) {
                    Vec3d pos = ModUtils.entityPos(this).add(new Vec3d(this.motionX * ModRand.getFloat(0.5f), y, this.motionZ * ModRand.getFloat(0.5f)));
                    ParticleManager.spawnDust(world, pos, ModColors.WHITE, Vec3d.ZERO, ModRand.range(10, 15));
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

                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.PROJECTILE)
                        .indirectEntity(shootingEntity)
                        .directEntity(this)
                        .stoppedByArmorNotShields().disablesShields().build();

                entity.attackEntityFrom(source, (float) getDamage());

            }
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        if(result.entityHit != null && !world.isRemote) {
            Entity base = result.entityHit;

        }
    }
}
