package com.dungeon_additions.da.entity.desert_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProjectileDesertOrb extends Projectile {

    public ProjectileDesertOrb(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
    }

    private boolean hasNoGravity = true;

    public ProjectileDesertOrb(World worldIn, EntityLivingBase throwerIn, float damage, boolean hasNoGravity) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(hasNoGravity);
        this.hasNoGravity = hasNoGravity;
    }

    public ProjectileDesertOrb(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
    }

    public ProjectileDesertOrb(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
    }

    @Override
    protected void spawnParticles() {
        //  ParticleManager.spawnSwirl2(world, this.getPositionVector(), ModColors.YELLOW, Vec3d.ZERO);
        Main.proxy.spawnParticle(23, world, this.posX, this.posY, this.posZ, 0, 0.02, 0, 15128888);
        if(!hasNoGravity) {
            Main.proxy.spawnParticle(23, world, this.posX, this.posY, this.posZ, 0, -0.02, 0, 8454153);
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        if(shootingEntity != null) {
            if (!hasNoGravity && !world.isRemote) {
                ProjectileThousandCuts cuts_projectile = new ProjectileThousandCuts(world, this.shootingEntity, this.getDamage());
                cuts_projectile.setPosition(this.posX, this.posY + 2, this.posZ);
                world.spawnEntity(cuts_projectile);
            }
            DamageSource source = ModDamageSource.builder()
                    .type(ModDamageSource.MAGIC)
                    .directEntity(this)
                    .indirectEntity(shootingEntity)
                    .stoppedByArmorNotShields().build();

            ModUtils.handleBulletImpact(result.entityHit, this, this.getDamage(), source);
        }
        this.playSound(SoundEvents.BLOCK_STONE_BREAK, 0.8f, 0.8f / (rand.nextFloat() * 0.4f + 0.4f));
        super.onHit(result);
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
}
