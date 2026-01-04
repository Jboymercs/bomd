package com.dungeon_additions.da.entity.night_lich;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileMagicFireBall extends Projectile {

    private static final int PARTICLE_AMOUNT = 1;

    public ProjectileMagicFireBall(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
    }

    protected EntityPlayer player;
    public ProjectileMagicFireBall(World worldIn, EntityLivingBase throwerIn, float damage, EntityPlayer owner) {
        super(worldIn, throwerIn, damage);
        this.player = owner;
    }

    public ProjectileMagicFireBall(World worldIn) {
        super(worldIn);
    }

    public ProjectileMagicFireBall(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            ParticleManager.spawnDust(world, this.getPositionVector(), ModColors.AZURE, Vec3d.ZERO, ModRand.range(10, 15));
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        //Spawns an Explosion on Impact
        if(!world.isRemote) {
            Main.proxy.spawnParticle(21,world, this.posX, this.posY, this.posZ, 0, 0, 0);
            if(player != null) {
                if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NIGHT_LICH_HELMET) {
                    world.newExplosion(this, this.posX, this.posY, this.posZ, 2, false, false);
                } else {
                    world.newExplosion(this, this.posX, this.posY, this.posZ, 1, false, false);
                }
            } else {
                world.newExplosion(this, this.posX, this.posY, this.posZ, 2, false, false);
            }
        }

        DamageSource source = ModDamageSource.builder()
                .type(ModDamageSource.MAGIC)
                .directEntity(this)
                .indirectEntity(shootingEntity)
                .stoppedByArmorNotShields().build();

        ModUtils.handleBulletImpact(result.entityHit, this, this.getDamage(), source);
        super.onHit(result);
    }
}
