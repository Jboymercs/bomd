package com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles;

import com.dungeon_additions.da.entity.blossom.action.ActionSporeBomb;
import com.dungeon_additions.da.entity.player.ActionPlayerBloodPile;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.action.ActionBloodBombs;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.action.ActionSingleBloodPile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileKingBlood extends Projectile {

    protected boolean isLargeImpact;
    protected EntityPlayer owner;
    private static final int PARTICLE_AMOUNT = 1;

    public ProjectileKingBlood(World world, EntityLivingBase throwerIn, float damage, boolean isLarge) {
        super(world, throwerIn, damage);
        this.isLargeImpact = isLarge;
        if(throwerIn instanceof EntityPlayer) {
            this.owner = ((EntityPlayer) throwerIn);
        }

    }

    public ProjectileKingBlood(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
    }

    public ProjectileKingBlood(World worldIn) {
        super(worldIn);
    }

    public ProjectileKingBlood(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.RED, Vec3d.ZERO);
        }
    }

    @Override
    protected void spawnImpactParticles() {
        ModUtils.circleCallback(2, 15, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y);
            ParticleManager.spawnDust(world, pos.add(this.getPositionVector()).add(ModUtils.yVec(5.6)), ModColors.RED, pos.normalize().scale(0.3).add(ModUtils.yVec(0.1)), ModRand.range(2, 6));
        } );
    }

    @Override
    protected void onHit(RayTraceResult result) {
        if(!world.isRemote) {
            if(this.isLargeImpact) {
                new ActionBloodBombs().performAction(this, null);
            } else if (owner != null) {
                new ActionPlayerBloodPile().performAction(this, null);
            } else {
                new ActionSingleBloodPile().performAction(this, null);
            }

            if(!(result.entityHit instanceof EntitySkyBase) && !(result.entityHit instanceof Projectile)) {
                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.MAGIC)
                        .directEntity(this)
                        .indirectEntity(shootingEntity)
                        .stoppedByArmorNotShields().build();
                ModUtils.handleBulletImpact(result.entityHit, this, this.getDamage(), source);
                if(this.owner != null) {
                    if(result.entityHit instanceof EntityLivingBase && !(result.entityHit instanceof EntityPlayer)) {
                        EntityLivingBase base = ((EntityLivingBase)result.entityHit);
                        base.addPotionEffect(new PotionEffect(MobEffects.WITHER, 200, 0, false, false));
                    }
                }
                super.onHit(result);
            }
        }
        this.playSound(SoundEvents.BLOCK_SLIME_PLACE, 0.75F, 0.6F / (rand.nextFloat() * 0.4F + 0.8F));
        super.onHit(result);
    }
}
