package com.dungeon_additions.da.entity.desert_dungeon.boss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileDesertOrb;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityColossusSigil extends Projectile {

    private EntityLivingBase target;

    public EntityColossusSigil(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.setSize(0.5F, 0.5F);
    }

    private boolean summonedFromTrinket;

    public EntityColossusSigil(World worldIn, EntityLivingBase throwerIn, float damage, boolean summonedFromTrinket) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.setSize(0.5F, 0.5F);
        this.summonedFromTrinket = summonedFromTrinket;
    }

    public EntityColossusSigil(World worldIn, EntityLivingBase throwerIn, float damage, EntityLivingBase target) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.setSize(0.5F, 0.5F);
        this.summonedFromTrinket = false;
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

            if(ticksExisted == 2) {
                this.playSound(SoundsHandler.WYRK_STAFF_SHOOT, 0.75f, 0.7f / (rand.nextFloat() * 0.2f + 0.2f));
            }

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
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(0);
                Vec3d lineStart = targetPos.subtract(lineDir);
                this.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 0.75f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f));
                Vec3d lineEnd = targetPos.add(lineDir);
                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, orb, 0F, 1.2F);
                });
            } else if (this.shootingEntity instanceof EntityPlayer && this.summonedFromTrinket ? this.ticksExisted % 30 == 0 : this.ticksExisted % 15 == 0) {
                boolean firedOrb = false;
                List<EntityAbstractBase> nearbyMobs = this.world.getEntitiesWithinAABB(EntityAbstractBase.class, this.getEntityBoundingBox().grow(16D), e -> !e.getIsInvulnerable());
                List<EntityMob> nearbyCreatures = this.world.getEntitiesWithinAABB(EntityMob.class, this.getEntityBoundingBox().grow(16D), e -> !e.getIsInvulnerable());
                if(!nearbyMobs.isEmpty()) {
                    for(EntityAbstractBase base : nearbyMobs) {
                        if(!base.isFriendlyCreature && this.canEntityBeSeen(base) && !firedOrb) {
                            ProjectileDesertOrb orb = new ProjectileDesertOrb(world, this.shootingEntity, this.getDamage());
                            orb.setPosition(this.posX, this.posY + 0.5, this.posZ);
                            world.spawnEntity(orb);
                            Vec3d targetPos = base.getPositionEyes(1.0F).add(0, -0.25, 0);
                            Vec3d fromTargetTooActor = this.getPositionVector().subtract(targetPos);
                            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(0);
                            Vec3d lineStart = targetPos.subtract(lineDir);
                            this.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 0.75f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f));
                            Vec3d lineEnd = targetPos.add(lineDir);
                            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                                ModUtils.throwProjectileNoSpawn(pos, orb, 0F, 1.2F);
                            });
                            firedOrb = true;
                        }
                    }
                } else if (!nearbyCreatures.isEmpty()) {
                    for(EntityMob base : nearbyCreatures) {
                        if(this.canEntityBeSeen(base) && !firedOrb) {
                            ProjectileDesertOrb orb = new ProjectileDesertOrb(world, this.shootingEntity, this.getDamage());
                            orb.setPosition(this.posX, this.posY + 0.5, this.posZ);
                            world.spawnEntity(orb);
                            Vec3d targetPos = base.getPositionEyes(1.0F);
                            Vec3d fromTargetTooActor = this.getPositionVector().subtract(targetPos).add(0, -0.25, 0);
                            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(0);
                            Vec3d lineStart = targetPos.subtract(lineDir);
                            this.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 0.75f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f));
                            Vec3d lineEnd = targetPos.add(lineDir);
                            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                                ModUtils.throwProjectileNoSpawn(pos, orb, 0F, 1.2F);
                            });
                            firedOrb = true;
                        }
                    }
                }
            }



            if(ticksExisted >= 255) {
                this.setDead();
            }
        }
        super.onUpdate();
    }

    public boolean canEntityBeSeen(Entity entityIn) {
        return this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), new Vec3d(entityIn.posX, entityIn.posY + (double)entityIn.getEyeHeight(), entityIn.posZ), false, true, false) == null;
    }





    @Override
    protected void onHit(RayTraceResult result) {

    }
}
