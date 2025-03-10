package com.dungeon_additions.da.entity.frost_dungeon;

import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileFrostBullet extends Projectile {
    public ProjectileFrostBullet(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
    }

    public ProjectileFrostBullet(World worldIn) {
        super(worldIn);
    }

    public ProjectileFrostBullet(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }


    @Override
    protected void spawnParticles() {
        ParticleManager.spawnDust(world, this.getPositionVector(), ModColors.WHITE, Vec3d.ZERO, ModRand.range(10, 15));
    }

    @Override
    protected void onHit(RayTraceResult result) {
        DamageSource source = ModDamageSource.builder()
                .type(ModDamageSource.PROJECTILE)
                .directEntity(this)
                .indirectEntity(shootingEntity)
                .stoppedByArmorNotShields().build();

        if(result.entityHit != null && !world.isRemote) {
            Entity base = result.entityHit;
            if (!(base instanceof EntityFrostBase) && base instanceof EntityLivingBase) {
                ((EntityLivingBase)base).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 2));
            }
        }
        ModUtils.handleBulletImpact(result.entityHit, this, this.getDamage(), source);
        this.playSound(SoundEvents.BLOCK_STONE_BREAK, 0.8f, 0.8f / (rand.nextFloat() * 0.4f + 0.4f));
        super.onHit(result);
    }

}
