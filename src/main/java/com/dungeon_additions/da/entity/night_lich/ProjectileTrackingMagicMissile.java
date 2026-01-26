package com.dungeon_additions.da.entity.night_lich;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileFlameSling;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileTrackingFlame;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ProjectileTrackingMagicMissile extends Projectile {

    private EntityLivingBase selectedPlayer;

    public ProjectileTrackingMagicMissile(World worldIn, EntityLivingBase throwerIn, float damage, EntityLivingBase target) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.isImmuneToFire = true;
        this.selectedPlayer = target;
        this.setSize(0.5F, 0.5F);
    }

    public ProjectileTrackingMagicMissile(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
    }

    public ProjectileTrackingMagicMissile(World worldIn) {
        super(worldIn);
    }

    public ProjectileTrackingMagicMissile(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        if(ticksExisted < 6) {
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
            ParticleManager.spawnDust(world, this.getPositionVector(), ModColors.WHITE, Vec3d.ZERO, ModRand.range(10, 15));
        }
    }

    public void onTrackUpdate() {
        List<Entity> listNearby = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(0.5D, 0.5D, 0.5D));
        if(!listNearby.isEmpty()) {
            for(Entity entityIn : listNearby) {
                if(!(entityIn instanceof EntityNightLich) && !(entityIn instanceof EntityMob) && !(entityIn instanceof EntityLichStaffAOE) && !(entityIn instanceof ProjectileMagicMissile) &&
                !(entityIn instanceof ProjectileTrackingMagicMissile)) {
      {
                        if (!world.isRemote) {
                            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 2, false, false);
                        }
                    }
                    this.setDead();
                }
            }
        }
        //this.setFire(3);
        if(selectedPlayer != null) {
            Vec3d posToTravelToo = selectedPlayer.getPositionVector().add(ModUtils.yVec(1.0D));
            Vec3d currPos = this.getPositionVector();
            Vec3d dir = posToTravelToo.subtract(currPos).normalize();
            ModUtils.addEntityVelocity(this, dir.scale(0.04 * 0.4));
         //   double d0 = ((posToTravelToo.x - this.posX) * 0.0010);
         //   double d1 = (((posToTravelToo.y + 1) - this.posY) * 0.006);
         //   double d2 = ((posToTravelToo.z - this.posZ) * 0.0010);
         //   this.addVelocity(d0, d1, d2);
        }
    }

    public float getBrightness()
    {
        return 1.0F;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        return 15728880;
    }

    @Override
    protected void onHit(RayTraceResult result) {
        this.playSound(SoundEvents.BLOCK_STONE_BREAK, 0.8f, 0.8f / (rand.nextFloat() * 0.4f + 0.4f));
        super.onHit(result);
    }
}
