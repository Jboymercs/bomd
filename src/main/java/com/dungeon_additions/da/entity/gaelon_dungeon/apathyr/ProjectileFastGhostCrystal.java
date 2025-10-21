package com.dungeon_additions.da.entity.gaelon_dungeon.apathyr;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileFastGhostCrystal extends Projectile {
    public ProjectileFastGhostCrystal(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setSize(0.8F, 0.8F);
        this.setNoGravity(true);
    }

    public ProjectileFastGhostCrystal(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 0.8F);
        this.setNoGravity(true);
    }

    public ProjectileFastGhostCrystal(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setSize(1.0F, 0.8F);
        this.setNoGravity(true);
    }

    @Override
    protected void spawnParticles() {
        //  ParticleManager.spawnSwirl2(world, this.getPositionVector(), ModColors.YELLOW, Vec3d.ZERO);
        Main.proxy.spawnParticle(15, this.posX, this.posY, this.posZ, 0, 0, 0, 20);
    }

    @Override
    protected void spawnImpactParticles() {
        ModUtils.performNTimes(60, (i) -> {
            Main.proxy.spawnParticle(17, this.posX + ModRand.range(-4, 4), this.posY + ModRand.range(-2, 3), this.posZ + ModRand.range(-4, 4), 0, 0.05, 0, 20);
        });
    }

    @Override
    protected void onHit(RayTraceResult result) {
        DamageSource source = ModDamageSource.builder()
                .type(ModDamageSource.PROJECTILE)
                .directEntity(this)
                .indirectEntity(shootingEntity)
                .stoppedByArmorNotShields().build();

        ModUtils.handleBulletImpact(result.entityHit, this, this.getDamage(), source);
        if(!world.isRemote) {
            DamageSource source2 = ModDamageSource.builder()
                    .type(ModDamageSource.EXPLOSION)
                    .directEntity(this)
                    .stoppedByArmorNotShields().disablesShields().build();

            ModUtils.handleAreaImpact(4, (e) -> this.getDamage(), this, this.getPositionVector(), source2);
            //this.entity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f + ModRand.getFloat(0.1f));
            this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.25F, 1.0f + ModRand.getFloat(0.1f));
            this.playSound(SoundsHandler.VOIDCLYSM_IMPACT, 1.1F, 1.0f + ModRand.getFloat(0.1f));
        }
        super.onHit(result);
    }
}
