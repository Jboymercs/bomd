package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileDarkMatter extends Projectile {
    public ProjectileDarkMatter(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
    }

    public ProjectileDarkMatter(World worldIn) {
        super(worldIn);
    }

    public ProjectileDarkMatter(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void spawnParticles() {
        //  ParticleManager.spawnSwirl2(world, this.getPositionVector(), ModColors.YELLOW, Vec3d.ZERO);
        ParticleManager.spawnColoredSmoke(world, this.getPositionVector(), ModColors.BLACK, Vec3d.ZERO);
    }

    @Override
    public int getBrightnessForRender() {
        return Math.min(super.getBrightnessForRender() + 30, 200);
    }

    @Override
    protected void onHit(RayTraceResult result) {
        DamageSource source = ModDamageSource.builder()
                .type(ModDamageSource.MAGIC)
                .directEntity(this)
                .indirectEntity(shootingEntity)
                .stoppedByArmorNotShields().build();

        ModUtils.handleBulletImpact(result.entityHit, this, this.getDamage(), source);
        this.playSound(SoundEvents.BLOCK_SLIME_PLACE, 0.6f, 0.8f / (rand.nextFloat() * 0.4f + 0.4f));
        if(result.entityHit != null) {
            if(result.entityHit instanceof EntityLivingBase) {
                EntityLivingBase base = ((EntityLivingBase) result.entityHit);
                base.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0, false, false));
            }
        }
        super.onHit(result);
    }
}
