package com.dungeon_additions.da.entity.flame_knight;

import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileFlameSpit extends Projectile {

    public ProjectileFlameSpit(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setDamage(damage);
        this.setNoGravity(true);
    }

    public ProjectileFlameSpit(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
    }

    public ProjectileFlameSpit(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
    }

    @Override
    protected void spawnParticles() {
            world.spawnParticle(EnumParticleTypes.FLAME, this.posX + ModRand.getFloat(0.25F), this.posY + ModRand.getFloat(0.25F), this.posZ + ModRand.getFloat(0.25F), 0, 0, 0, ModRand.range(10, 15));
    }

    @Override
    protected void onHit(RayTraceResult result) {
        DamageSource source = ModDamageSource.builder()
                .indirectEntity(shootingEntity)
                .directEntity(this)
                .type(ModDamageSource.MAGIC)
                .stoppedByArmorNotShields().build();

        if(result.entityHit instanceof EntityLivingBase) {
            result.entityHit.setFire(4);
        }
        ModUtils.handleAreaImpact(0.5F, (e) -> this.getDamage(), this.shootingEntity, this.getPositionVector(), source);
        this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.5F, 0.6F / (rand.nextFloat() * 0.4F + 0.8F));
        super.onHit(result);
    }
}
