package com.dungeon_additions.da.entity.desert_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntitySharedDesertBoss;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.dungeon_additions.da.world.ModStructureTemplate;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ProjectileThousandCuts extends Projectile {

    private static final int PARTICLE_AMOUNT = 1;
    private EntityPlayer ownerIn;

    public ProjectileThousandCuts(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setSize(0.5F, 0.5F);
        this.setNoGravity(true);
    }

    public ProjectileThousandCuts(World worldIn, EntityPlayer ownerIn, float damage) {
        super(worldIn);
        this.setDamage(damage);
        this.ownerIn = ownerIn;
        this.setSize(0.5F, 0.5F);
        this.setNoGravity(true);
    }

    public ProjectileThousandCuts(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
        this.setDamage(10F);
        this.setNoGravity(true);
    }

    public ProjectileThousandCuts(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setSize(0.5F, 0.5F);
        this.setNoGravity(true);
    }

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            //left
            ModUtils.performNTimes(2, (b) -> {
                Main.proxy.spawnParticle(24, world, this.posX + ModRand.getFloat(2.5F), this.posY + ModRand.getFloat(2.5F), this.posZ + ModRand.getFloat(2.5F), 0, 0, 0, 15128888);
            });
            //right
            ModUtils.performNTimes(2, (b) -> {
                Main.proxy.spawnParticle(25, world, this.posX + ModRand.getFloat(2.5F), this.posY + ModRand.getFloat(2.5F), this.posZ + ModRand.getFloat(2.5F), 0, 0, 0, 15128888);
            });
        }
    }

    @Override
    public void onUpdate() {
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;

        if(ticksExisted == 2) {
            this.playSound(SoundsHandler.WARLORD_CUTS_CAST, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f));
        }

        if(!world.isRemote) {
            //do damage
            List<EntityLivingBase> nearbyMobs = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(2.5D), e -> !e.getIsInvulnerable());
            if(ticksExisted % 20 == 0) {
                if(ownerIn != null) {
                    if(!nearbyMobs.isEmpty()) {
                        for(EntityLivingBase base: nearbyMobs) {
                            if(base != ownerIn) {
                                Vec3d offset = this.getPositionVector();
                                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(this).build();
                                float damage =(float) (this.getDamage());
                                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.7f, 0, false);
                            }
                        }
                    }
                } else {
                    if(!nearbyMobs.isEmpty()) {
                        for (EntityLivingBase base : nearbyMobs) {
                            if(!(base instanceof EntityDesertBase)) {
                                Vec3d offset = this.getPositionVector();
                                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                                float damage =(float) (this.getDamage());
                                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.7f, 0, false);
                            }
                        }
                    }
                }
                this.playSound(SoundsHandler.WARLORD_CUTS_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.2f + 0.2f));
            }
        }

        if(ticksExisted > 110) {
            this.setDead();
        }
        super.onUpdate();
    }

    @Override
    protected void onHit(RayTraceResult result) {

    }
}
