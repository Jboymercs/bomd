package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ProjectileDauntlessFist extends Projectile {
    public ProjectileDauntlessFist(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
    }

    public ProjectileDauntlessFist(World worldIn, EntityLivingBase throwerIn, float damage, Vec3d lookPos, Vec3d scaledLookPos, double delayModif) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
    }

    public ProjectileDauntlessFist(World worldIn) {
        super(worldIn);
    }

    public ProjectileDauntlessFist(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    protected void spawnParticles() {
        //  ParticleManager.spawnSwirl2(world, this.getPositionVector(), ModColors.YELLOW, Vec3d.ZERO);
        ParticleManager.spawnDust(world, this.getPositionVector(), ModColors.RED, Vec3d.ZERO, ModRand.range(3, 5));
    }

    @Override
    protected void onHit(RayTraceResult result) {
        DamageSource source = ModDamageSource.builder()
                .type(ModDamageSource.MAGIC)
                .directEntity(this)
                .indirectEntity(shootingEntity)
                .stoppedByArmorNotShields().build();

        ModUtils.handleBulletImpact(result.entityHit, this, this.getDamage(), source);
        if(result.entityHit != null && shootingEntity instanceof EntityPlayer) {
            this.shootingEntity.heal(2);
        }
        this.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 0.8f, 0.8f / (rand.nextFloat() * 0.4f + 0.4f));
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
