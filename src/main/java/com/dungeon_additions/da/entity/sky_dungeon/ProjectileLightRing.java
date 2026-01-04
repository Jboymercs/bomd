package com.dungeon_additions.da.entity.sky_dungeon;

import com.dungeon_additions.da.entity.night_lich.EntityLichStaffAOE;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicMissile;
import com.dungeon_additions.da.entity.night_lich.ProjectileTrackingMagicMissile;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ProjectileLightRing extends Projectile {

    private EntityLivingBase selectedPlayer;

    private EntityLivingBase owner;
    private boolean breakPathing = false;

    public ProjectileLightRing(World worldIn, EntityLivingBase throwerIn, float damage, EntityLivingBase target) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.setSize(1.0F, 0.3F);
        this.selectedPlayer = target;
        this.owner = throwerIn;
    }

    public ProjectileLightRing(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.setSize(1.0F, 0.3F);
    }

    public ProjectileLightRing(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setSize(1.0F, 0.3F);
    }

    public ProjectileLightRing(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.setSize(1.0F, 0.3F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
            this.onTrackUpdate();

            if(ticksExisted == 2) {
            this.playSound(SoundsHandler.GARGOYLE_RING_SUMMON, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
        }
        if(ticksExisted > 34 && ticksExisted % 90 == 0) {
            this.playSound(SoundsHandler.GARGOYLE_RING_IDLE, 0.75f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
        }
    }

    public static final int PARTICLE_AMOUNT = 1;

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < PARTICLE_AMOUNT; i++) {
            ParticleManager.spawnDust(world, this.getPositionVector(), ModColors.YELLOW, new Vec3d(ModRand.getFloat(0.5F), 0, ModRand.getFloat(0.5F)), ModRand.range(10, 15));
        }
    }

    @Override
    protected void spawnImpactParticles() {
        ModUtils.circleCallback(1, 20, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y);
            ParticleManager.spawnDust(world, this.getPositionVector(), ModColors.YELLOW, pos.normalize().scale(0.2), ModRand.range(30, 40));
        });
    }

    public void onTrackUpdate() {
        if(breakPathing) {
            Vec3d currentVec = ModUtils.getEntityVelocity(this);
            ModUtils.setEntityVelocity(this, currentVec.scale(1.01));
        }
        if(selectedPlayer != null && !breakPathing) {
            Vec3d posToTravelToo = selectedPlayer.getPositionVector().add(ModUtils.yVec(1.0D));
            Vec3d currPos = this.getPositionVector();
            Vec3d dir = posToTravelToo.subtract(currPos).normalize();
            if(owner instanceof EntityPlayer) {
                ModUtils.addEntityVelocity(this, dir.scale(0.15 * 0.4));
            } else {
                ModUtils.addEntityVelocity(this, dir.scale(0.09 * 0.4));
            }
            if(this.getDistanceSq(selectedPlayer) < 3) {
                this.breakPathing = true;
            }
        }
    }


    @Override
    protected void onHit(RayTraceResult result) {
        if(!(result.entityHit instanceof EntitySkyBase) && !(result.entityHit instanceof Projectile) && !(result.entityHit instanceof IEntityMultiPart)) {
            DamageSource source = ModDamageSource.builder()
                    .type(ModDamageSource.MAGIC)
                    .directEntity(this)
                    .indirectEntity(shootingEntity)
                    .stoppedByArmorNotShields().build();
            ModUtils.handleBulletImpact(result.entityHit, this, this.getDamage(), source);
            super.onHit(result);
        }
        this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.8f, 0.8f / (rand.nextFloat() * 0.4f + 0.4f));
    }
}
