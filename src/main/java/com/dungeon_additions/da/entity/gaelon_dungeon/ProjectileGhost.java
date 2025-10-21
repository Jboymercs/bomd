package com.dungeon_additions.da.entity.gaelon_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.projectiles.Projectile;
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

public class ProjectileGhost extends Projectile {

    public ProjectileGhost(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
    }

    public ProjectileGhost(World worldIn) {
        super(worldIn);
    }

    public ProjectileGhost(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void spawnParticles() {
        //  ParticleManager.spawnSwirl2(world, this.getPositionVector(), ModColors.YELLOW, Vec3d.ZERO);
        Main.proxy.spawnParticle(17, this.posX, this.posY, this.posZ, 0, 0, 0, 20);
    }

    @Override
    protected void onHit(RayTraceResult result) {
        if(result.entityHit != null) {
            if(!(result.entityHit instanceof EntityApathyr)) {
                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.PROJECTILE)
                        .directEntity(this)
                        .indirectEntity(shootingEntity)
                        .stoppedByArmorNotShields().build();

                ModUtils.handleBulletImpact(result.entityHit, this, this.getDamage(), source);
                this.playSound(SoundEvents.BLOCK_STONE_BREAK, 0.8f, 0.8f / (rand.nextFloat() * 0.4f + 0.4f));
                super.onHit(result);
            }
        }
    }
}
