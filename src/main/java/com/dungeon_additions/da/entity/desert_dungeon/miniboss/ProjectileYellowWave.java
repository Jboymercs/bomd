package com.dungeon_additions.da.entity.desert_dungeon.miniboss;

import com.dungeon_additions.da.entity.desert_dungeon.EntityDesertBase;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityGaelonBase;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.void_dungeon.EntityObsidilith;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ProjectileYellowWave extends Projectile {

    public static final int PARTICLE_AMOUNT = 1;
    protected int updates = 5;

    public ProjectileYellowWave(World worldIn, EntityLivingBase throwerIn, float baseDamage, ItemStack stack) {
        super(worldIn, throwerIn, baseDamage);
        this.setNoGravity(true);
        this.setSize(1.4F, 2);
    }

    private boolean byPass;

    public ProjectileYellowWave(World worldIn, EntityLivingBase throwerIn, float baseDamage, ItemStack stack, boolean bypass) {
        super(worldIn, throwerIn, baseDamage);
        this.setNoGravity(true);
        this.setSize(1.4F, 2);
        this.byPass = bypass;
        this.noClip = true;
    }

    public EntityPlayer player;
    public ProjectileYellowWave(World worldIn, EntityLivingBase throwerIn, float baseDamage, ItemStack stack, EntityPlayer owner) {
        super(worldIn, throwerIn, baseDamage);
        this.setNoGravity(true);
        this.setSize(1.4F, 2);
        this.player = owner;
    }

    public ProjectileYellowWave(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setSize(1.4F, 2);
    }

    public ProjectileYellowWave(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.setSize(1.4F, 2);
    }

    @Override
    protected void spawnParticles() {
        IBlockState block = world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ));
        if (block.isFullCube()) {
            for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
                float height = 2 + ModRand.getFloat(0.5f);
                for (float y = 0; y < height; y += 0.5f) {
                    Vec3d pos = ModUtils.entityPos(this).add(new Vec3d(this.motionX * ModRand.getFloat(0.5f), y, this.motionZ * ModRand.getFloat(0.5f)));
                    ParticleManager.spawnDust(world, pos, ModColors.YELLOW, Vec3d.ZERO, ModRand.range(5, 10));
                }

            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
            onQuakeUpdate();
        // Keeps the projectile on the surface of the ground
        if(!this.byPass) {
            for (int i = 0; i < updates; i++) {
                if (!world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).isFullCube()) {
                    this.setPosition(this.posX, this.posY - 0.1F, this.posZ);
                } else if (world.getBlockState(new BlockPos(this.posX, this.posY + 1, this.posZ)).isFullCube()) {
                    this.setPosition(this.posX, this.posY, this.posZ);
                }
            }
        }

        if(byPass) {
            this.motionY = 0;
        }

        // If the projectile hits water and looses all of its velocity, despawn
        if (!world.isRemote && Math.abs(this.motionX) + Math.abs(this.motionZ) < 0.01f) {
            this.setDead();
        }
    }

    protected void onQuakeUpdate() {
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(0.7));
        for (Entity entity : list) {
            if (entity instanceof EntityLivingBase && this.shootingEntity != null && entity != this.shootingEntity && !(entity instanceof EntityDesertBase) && (!(entity instanceof EntityGaelonBase))) {

                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.MAGIC)
                        .indirectEntity(shootingEntity)
                        .directEntity(this)
                        .stoppedByArmorNotShields().disablesShields().build();

                entity.attackEntityFrom(source, (float) getDamage());
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
        return 15728880;
    }

    @Override
    protected void onHit(RayTraceResult result) {
    }
}
