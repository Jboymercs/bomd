package com.dungeon_additions.da.entity.night_lich;

import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileMagicFireBall extends Projectile {

    private static final int PARTICLE_AMOUNT = 1;

    public ProjectileMagicFireBall(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
    }

    public ProjectileMagicFireBall(World worldIn) {
        super(worldIn);
    }

    public ProjectileMagicFireBall(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            ParticleManager.spawnDust(world, this.getPositionVector(), ModColors.AZURE, Vec3d.ZERO, ModRand.range(10, 15));
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        //Spawns an Explosion on Impact
        if(!world.isRemote) {
            world.newExplosion(this, this.posX, this.posY, this.posZ, 2, false, false);
        }

        DamageSource source = ModDamageSource.builder()
                .type(ModDamageSource.PROJECTILE)
                .directEntity(this)
                .indirectEntity(shootingEntity)
                .stoppedByArmorNotShields().build();

        ModUtils.handleBulletImpact(result.entityHit, this, this.getDamage(), source);
        super.onHit(result);
    }
}
