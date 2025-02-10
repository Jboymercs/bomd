package com.dungeon_additions.da.entity.frost_dungeon.draugr;

import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.wyrk.EntityFriendWyrk;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ProjectileSoul extends Projectile {

    private EntityWyrk summoner;
    private EntityFriendWyrk summonerTwo;

    public ProjectileSoul(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.isLocatorItem = true;
    }

    public ProjectileSoul(World worldIn, EntityLivingBase throwerIn, float damage, EntityWyrk summoner) {
        super(worldIn, throwerIn, damage);
        this.summoner = summoner;
        this.setNoGravity(true);
        this.isLocatorItem = true;
    }

    public ProjectileSoul(World worldIn, EntityLivingBase throwerIn, float damage, EntityFriendWyrk summonerTwo) {
        super(worldIn, throwerIn, damage);
        this.summonerTwo = summonerTwo;
        this.setNoGravity(true);
        this.isLocatorItem = true;
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

    public void onTrackUpdate() {

        List<EntityWyrk> nearbyWyrk = this.world.getEntitiesWithinAABB(EntityWyrk.class, this.getEntityBoundingBox().grow(1.0D), e -> !e.getIsInvulnerable());
        List<EntityFriendWyrk> nearbyFWyrk = this.world.getEntitiesWithinAABB(EntityFriendWyrk.class, this.getEntityBoundingBox().grow(1.0D), e -> !e.getIsInvulnerable());

        if(!nearbyFWyrk.isEmpty()) {
            for(EntityFriendWyrk wyrk : nearbyFWyrk) {
                if(wyrk.isEntityAlive()) {
                    wyrk.setSummonCount(wyrk.getSummonCount() + 1);
                    this.setDead();
                }
            }
        }

        if(!nearbyWyrk.isEmpty()) {
            for(EntityWyrk wyrk : nearbyWyrk) {
                if(wyrk.isEntityAlive()) {
                    wyrk.setSummonCount(wyrk.getSummonCount() + 1);
                    this.setDead();
                }
            }
        }
        if(summoner != null) {
            Vec3d posToTravelToo = summoner.getPositionVector().add(ModUtils.yVec(1.0D));
            Vec3d currPos = this.getPositionVector();
            Vec3d dir = posToTravelToo.subtract(currPos).normalize();
            ModUtils.addEntityVelocity(this, dir.scale(0.01 * 0.4));
            //   double d0 = ((posToTravelToo.x - this.posX) * 0.0010);
            //   double d1 = (((posToTravelToo.y + 1) - this.posY) * 0.006);
            //   double d2 = ((posToTravelToo.z - this.posZ) * 0.0010);
            //   this.addVelocity(d0, d1, d2);
        } else if (summonerTwo != null) {
            Vec3d posToTravelToo = summonerTwo.getPositionVector().add(ModUtils.yVec(1.0D));
            Vec3d currPos = this.getPositionVector();
            Vec3d dir = posToTravelToo.subtract(currPos).normalize();
            ModUtils.addEntityVelocity(this, dir.scale(0.01 * 0.4));
        }
    }

    public ProjectileSoul(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.isLocatorItem = true;
    }

    public static final int PARTICLE_AMOUNT = 1;

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < PARTICLE_AMOUNT; i++) {
            ParticleManager.spawnDust(world, this.getPositionVector(), ModColors.WHITE, Vec3d.ZERO, ModRand.range(10, 15));
        }
    }

    public ProjectileSoul(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.isLocatorItem = true;
    }
}
