package com.dungeon_additions.da.entity.projectiles;

import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileAbberrantAttack extends Projectile{
    private static final int PARTICLE_AMOUNT = 1;
    private static final int IMPACT_PARTICLE_AMOUNT = 20;
    private static final int EXPOSION_AREA_FACTOR = 2;

    public ProjectileAbberrantAttack(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
    }

    public ProjectileAbberrantAttack(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
    }

    public ProjectileAbberrantAttack(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
    }

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.FIREBALL_ORANGE, new Vec3d(0, 0.1, 0));
        }
    }

    @Override
    protected void spawnImpactParticles() {
        for (int i = 0; i < this.IMPACT_PARTICLE_AMOUNT; i++) {
            Vec3d vec1 = ModRand.randVec().scale(EXPOSION_AREA_FACTOR * 0.25).add(getPositionVector());
            ParticleManager.spawnColoredExplosion(world, vec1, ModColors.FIREBALL_ORANGE);
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        DamageSource source = ModDamageSource.builder()
                .indirectEntity(shootingEntity)
                .directEntity(this)
                .type(ModDamageSource.EXPLOSION)
                .stoppedByArmorNotShields().build();

        ModUtils.handleAreaImpact(EXPOSION_AREA_FACTOR, (e) -> this.getDamage(), this.shootingEntity, this.getPositionVector(), source);
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
        super.onHit(result);
    }
}
