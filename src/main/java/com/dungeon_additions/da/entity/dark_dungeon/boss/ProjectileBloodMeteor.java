package com.dungeon_additions.da.entity.dark_dungeon.boss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProjectileBloodMeteor extends Projectile {

    public ProjectileBloodMeteor(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setSize(0.8F, 0.8F);
        this.setNoGravity(true);
    }

    public ProjectileBloodMeteor(World worldIn, EntityLivingBase throwerIn, float damage, boolean noGravity) {
        super(worldIn, throwerIn, damage);
        this.setSize(0.8F, 0.8F);
        this.setNoGravity(noGravity);
    }

    public ProjectileBloodMeteor(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 0.8F);
        this.setNoGravity(true);
    }

    public ProjectileBloodMeteor(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setSize(1.0F, 0.8F);
        this.setNoGravity(true);
    }

    @Override
    protected void spawnParticles() {
        //  ParticleManager.spawnSwirl2(world, this.getPositionVector(), ModColors.YELLOW, Vec3d.ZERO);
        Main.proxy.spawnParticle(30, this.posX, this.posY + 0.4, this.posZ, 0, 0, 0, 7340032);
    }

    @Override
    protected void spawnImpactParticles() {
        ModUtils.performNTimes(20, (i) -> {
            Main.proxy.spawnParticle(30, this.posX + ModRand.range(-3, 3), this.posY + ModRand.range(-2, 3), this.posZ + ModRand.range(-3, 3), 0, 0.06, 0, 7340032);
        });
    }

    @Override
    protected void onHit(RayTraceResult result) {
            DamageSource source = ModDamageSource.builder()
                    .type(ModDamageSource.PROJECTILE)
                    .directEntity(this)
                    .indirectEntity(shootingEntity)
                    .stoppedByArmorNotShields().build();

            ModUtils.handleBulletImpact(result.entityHit, this, this.getDamage(), source);
            if (!world.isRemote) {
                DamageSource source2 = ModDamageSource.builder()
                        .type(ModDamageSource.MAGIC)
                        .directEntity(this)
                        .stoppedByArmorNotShields().disablesShields().build();

                ModUtils.handleAreaImpact(3, (e) -> this.getDamage(), this, this.getPositionVector(), source2);
                //this.entity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f + ModRand.getFloat(0.1f));
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.25F, 1.0f + ModRand.getFloat(0.1f));
                this.playSound(SoundsHandler.VOIDCLYSM_IMPACT, 1.1F, 1.0f + ModRand.getFloat(0.1f));
            }
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
