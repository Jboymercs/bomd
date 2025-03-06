package com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles;

import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileStormWind extends Projectile {
    public ProjectileStormWind(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.setSize(0.4F, 0.4F);
    }

    public ProjectileStormWind(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setSize(0.4F, 0.4F);
    }

    public ProjectileStormWind(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.setSize(0.4F, 0.4F);
    }

    @Override
    protected void spawnParticles() {
        //  ParticleManager.spawnSwirl2(world, this.getPositionVector(), ModColors.YELLOW, Vec3d.ZERO);
        ParticleManager.spawnDust(world, this.getPositionVector(), ModColors.WHITE, Vec3d.ZERO, ModRand.range(50, 60));
        ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(0, 0.2, 0), ModColors.WHITE, new Vec3d(ModRand.getFloat(0.15F) - 0.1F, 0.08, ModRand.getFloat(0.15F) - 0.1F));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote && Math.abs(this.motionX) + Math.abs(this.motionZ) < 0.01f || this.ticksExisted > 300) {
            this.setDead();
        }
    }


    @Override
    protected void onHit(RayTraceResult result) {

        if(result.entityHit != null && !(result.entityHit instanceof EntitySkyBase) && !(result.entityHit instanceof Projectile) && !(result.entityHit instanceof IEntityMultiPart)) {
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
