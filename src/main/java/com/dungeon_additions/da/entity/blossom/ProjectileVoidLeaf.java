package com.dungeon_additions.da.entity.blossom;

import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProjectileVoidLeaf extends Projectile {
    public ProjectileVoidLeaf(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
    }

    public ProjectileVoidLeaf(World worldIn) {
        super(worldIn);
    }

    public ProjectileVoidLeaf(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void spawnParticles() {
        ParticleManager.spawnEffect(world, getPositionVector(), ModColors.PINK);
    }

    @Override
    protected void onHit(RayTraceResult result) {

        if (result.entityHit != null  && this.shootingEntity != null && result.entityHit != this.shootingEntity) {
            DamageSource source = ModDamageSource.builder()
                    .type(ModDamageSource.MAGIC)
                    .indirectEntity(this)
                    .directEntity(shootingEntity)
                    .build();

            result.entityHit.attackEntityFrom(source, this.getDamage());
        }
        this.playSound(SoundEvents.BLOCK_SNOW_BREAK, 1.0f + ModRand.getFloat(0.2f), 1.0f + ModRand.getFloat(0.2f));
        super.onHit(result);
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
}
