package com.dungeon_additions.da.entity.flame_knight.misc;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityNetherAbberrant;
import com.dungeon_additions.da.entity.flame_knight.EntityBareant;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameBase;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityIncendium;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ProjectileTrackingFlame extends Projectile {

    private boolean isImmune;
    public ProjectileTrackingFlame(World worldIn, EntityLivingBase throwerIn, float damage, EntityLivingBase target) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.isImmuneToFire = true;
        this.selectedPlayer = target;
        this.setSize(0.5F, 0.5F);


    }

    public ProjectileTrackingFlame(World worldIn, EntityLivingBase throwerIn, float damage, EntityLivingBase target, boolean isImmune) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.isImmuneToFire = true;
        this.selectedPlayer = target;
        this.setSize(0.5F, 0.5F);
        this.isImmune = isImmune;
    }

    public ProjectileTrackingFlame(World worldIn) {
        super(worldIn);
    }

    public ProjectileTrackingFlame(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    private int randomMovement = 20;
    private int haltMovement = 15;
    private boolean setExplodeOnImpact = false;
    private Vec3d holdPos;
    private boolean setMovement;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.isImmune) {
            if(randomMovement > 0) {
            this.motionX += (world.rand.nextFloat() - world.rand.nextFloat())/3;
            this.motionZ += (world.rand.nextFloat() - world.rand.nextFloat())/3;
            this.motionY += 0.05;
            randomMovement--;
            } else {
                this.isImmune = false;
                this.setMovement = true;
                holdPos = this.getPositionVector();
            }
        } else if(this.setMovement){
            if(haltMovement > 0) {
                this.motionX = 0;
                this.motionY = 0;
                this.motionZ = 0;
                if(holdPos != null) {
                    ModUtils.setEntityPosition(this, holdPos);
                }
                haltMovement--;
            } else {
                setExplodeOnImpact = true;
                setMovement = false;
            }
        }
        this.onTrackUpdate();
    }

    public static final int PARTICLE_AMOUNT = 1;

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < PARTICLE_AMOUNT; i++) {
            world.spawnParticle(EnumParticleTypes.FLAME, this.posX, this.posY, this.posZ, 0, 0, 0, ModRand.range(10, 15));
        }
    }

    private EntityLivingBase selectedPlayer;

    public void onTrackUpdate() {
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(30, 5, 30));
        if(!list.isEmpty()) {
            for(Entity entityIn : list) {
                if(entityIn == selectedPlayer) {
                   // this.selectedPlayer = (EntityLivingBase) entityIn;
                }
            }
        }

        List<Entity> listNearby = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(0.5D, 0.5D, 0.5D));
        if(!listNearby.isEmpty()) {
            for(Entity entityIn : listNearby) {
                if(entityIn instanceof EntityNetherAbberrant || entityIn instanceof EntityIncendium) {
                    ((EntityFlameBase) entityIn).heal(15);
                    this.setDead();
                }
                else if(!(entityIn instanceof ProjectileTrackingFlame) && !(entityIn instanceof ProjectileFlameSling) && !(entityIn instanceof EntityFlameBase)) {
                    if(this.isImmune) {
                        if(!(entityIn instanceof EntityArrow)) {
                            if(!world.isRemote) {
                                if(this.shootingEntity instanceof EntityBareant) {
                                    this.world.newExplosion(this, this.posX, this.posY, this.posZ, 1, false, false);
                                    Vec3d offset = entityIn.getPositionVector().add(new Vec3d(0, 1, 0));
                                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
                                    float damage = (float) (MobConfig.bareant_attack_damage * 0.5);
                                    ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.3f, 3, false);
                                } else {
                                    this.world.newExplosion(this, this.posX, this.posY, this.posZ, 2, MobConfig.let_the_world_burn, false);
                                }
                            }
                        }
                    } else {
                        if (!world.isRemote) {
                            if(this.shootingEntity instanceof EntityBareant) {
                                this.world.newExplosion(this, this.posX, this.posY, this.posZ, 1, false, false);
                                Vec3d offset = entityIn.getPositionVector().add(new Vec3d(0, 1, 0));
                                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
                                float damage = (float) (MobConfig.bareant_attack_damage * 0.5);
                                ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.3f, 3, false);
                            } else {
                                this.world.newExplosion(this, this.posX, this.posY, this.posZ, 2, MobConfig.let_the_world_burn, false);
                            }
                        }
                    }
                    this.setDead();
                }
            }
        }
        //this.setFire(3);
        if(selectedPlayer != null && !this.isImmune) {
            Vec3d posToTravelToo = selectedPlayer.getPositionVector().add(ModUtils.yVec(1.4D));
            double d0 = ((posToTravelToo.x - this.posX) * 0.0010);
            double d1 = (((posToTravelToo.y) - this.posY) * 0.0008);
            double d2 = ((posToTravelToo.z - this.posZ) * 0.0010);
            this.addVelocity(d0, d1, d2);
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        if(this.setExplodeOnImpact && !world.isRemote) {
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 2, MobConfig.let_the_world_burn, false);
            Main.proxy.spawnParticle(18, this.posX, this.posY + 1.2, this.posZ, 0, 0, 0);
        }

        if(!this.isImmune) {
            super.onHit(result);
        }

    }
}
