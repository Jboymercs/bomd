package com.dungeon_additions.da.entity.desert_dungeon.boss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileDesertOrb;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityColossusSigil extends Projectile {

    private EntityLivingBase target;

    public EntityColossusSigil(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.setSize(0.5F, 0.5F);
    }

    public EntityColossusSigil(World worldIn, EntityLivingBase throwerIn, float damage, EntityLivingBase target) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.setSize(0.5F, 0.5F);
        this.target = target;
    }

    public EntityColossusSigil(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setSize(0.5F, 0.5F);
    }

    public EntityColossusSigil(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.setSize(0.5F, 0.5F);
    }

    @Override
    public void onUpdate() {
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        if(!world.isRemote) {

            //spawns a particle that stays at this position
            if(this.ticksExisted % 20 == 0) {
                Main.proxy.spawnParticle(26, this.posX, this.posY + 0.5, this.posZ, 0,0,0);
            }

            //spawns a projectile every second and a half
            if(this.ticksExisted % 30 == 0 && this.target != null) {
                ProjectileDesertOrb orb = new ProjectileDesertOrb(world, this.shootingEntity, this.getDamage());
                orb.setPosition(this.posX, this.posY + 0.5, this.posZ);
                world.spawnEntity(orb);
                Vec3d targetPos = target.getPositionEyes(1.0F);
                Vec3d fromTargetTooActor = this.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);
                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, orb, 0F, 1.2F);
                });
            }



            if(ticksExisted >= 255) {
                this.setDead();
            }
        }
        super.onUpdate();
    }





    @Override
    protected void onHit(RayTraceResult result) {

    }
}
