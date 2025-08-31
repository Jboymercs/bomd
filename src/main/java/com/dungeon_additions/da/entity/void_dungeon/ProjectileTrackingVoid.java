package com.dungeon_additions.da.entity.void_dungeon;

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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ProjectileTrackingVoid extends Projectile {
    private EntityLivingBase selectedPlayer;

    public ProjectileTrackingVoid(World worldIn, EntityLivingBase throwerIn, float damage, EntityLivingBase target) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.isImmuneToFire = true;
        this.selectedPlayer = target;
        this.setSize(0.5F, 0.5F);
    }

    public ProjectileTrackingVoid(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
    }

    public ProjectileTrackingVoid(World worldIn) {
        super(worldIn);
    }

    public ProjectileTrackingVoid(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        if(ticksExisted < 10) {
            ModUtils.setEntityPosition(this, new Vec3d(this.posX, this.posY, this.posZ));
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
        } else {
            this.onTrackUpdate();
        }

    }

    public static final int PARTICLE_AMOUNT = 1;

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < PARTICLE_AMOUNT; i++) {
            ParticleManager.spawnDust(world, this.getPositionVector(), ModColors.LIGHT_PURPLE, Vec3d.ZERO, ModRand.range(10, 15));
        }
    }

    public void onTrackUpdate() {
        List<Entity> listNearby = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(0.5D));
        if(!listNearby.isEmpty()) {
            for(Entity entityIn : listNearby) {
                if(!(entityIn instanceof EntityEndBase) &&
                        !(entityIn instanceof ProjectileTrackingVoid)) {
                    {
                            DamageSource source = ModDamageSource.builder()
                                    .type(ModDamageSource.EXPLOSION)
                                    .directEntity(this)
                                    .stoppedByArmorNotShields().build();

                            ModUtils.handleAreaImpact(3.5F, (e) -> this.getDamage(), this, this.getPositionVector().add(ModUtils.yVec(0.5)), source);
                        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f + ModRand.getFloat(0.1f));
                    }
                    this.setDead();
                }
            }
        }
        //this.setFire(3);
        if(selectedPlayer != null) {
            Vec3d posToTravelToo = selectedPlayer.getPositionVector().add(ModUtils.yVec(1.0D));
            double d0 = ((posToTravelToo.x - this.posX) * 0.0010);
            double d1 = (((posToTravelToo.y) - this.posY) * 0.0008);
            double d2 = ((posToTravelToo.z - this.posZ) * 0.0010);
            this.addVelocity(d0, d1, d2);
            //   double d0 = ((posToTravelToo.x - this.posX) * 0.0010);
            //   double d1 = (((posToTravelToo.y + 1) - this.posY) * 0.006);
            //   double d2 = ((posToTravelToo.z - this.posZ) * 0.0010);
            //   this.addVelocity(d0, d1, d2);
        }
    }

    @Override
    protected void spawnImpactParticles() {
        ModUtils.circleCallback(2, 15, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y);
            ParticleManager.spawnDust(world, pos.add(this.getPositionVector()).add(ModUtils.yVec(0.1)), ModColors.LIGHT_PURPLE, pos.normalize().scale(-0.3).add(ModUtils.yVec(0.1)), ModRand.range(40, 90));
            ParticleManager.spawnDust(world, pos.add(this.getPositionVector()).add(ModUtils.yVec(1.1)), ModColors.LIGHT_PURPLE, pos.normalize().scale(-0.3).add(ModUtils.yVec(0.1)), ModRand.range(40, 90));
            ParticleManager.spawnDust(world, pos.add(this.getPositionVector()).add(ModUtils.yVec(2.1)), ModColors.LIGHT_PURPLE, pos.normalize().scale(-0.3).add(ModUtils.yVec(0.1)), ModRand.range(40, 90));
        } );
    }

    @Override
    protected void onHit(RayTraceResult result) {
        this.playSound(SoundEvents.BLOCK_STONE_BREAK, 0.8f, 0.8f / (rand.nextFloat() * 0.4f + 0.4f));
            DamageSource source = ModDamageSource.builder()
                    .type(ModDamageSource.EXPLOSION)
                    .directEntity(this)
                    .stoppedByArmorNotShields().build();

            ModUtils.handleAreaImpact(3.5F, (e) -> this.getDamage(), this, this.getPositionVector().add(ModUtils.yVec(0.5)), source);
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f + ModRand.getFloat(0.1f));
        super.onHit(result);
    }
}
