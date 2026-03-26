package com.dungeon_additions.da.entity.night_lich;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class ProjectileMagicMissile extends Projectile {

    public ProjectileMagicMissile(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
    }

    private double spawnDelay = 0;
    private Vec3d firePos = null;
    private Vec3d endPos = null;

    public ProjectileMagicMissile(World worldIn, EntityLivingBase throwerIn, float damage, Vec3d lookPos, Vec3d scaledLookPos, double delayModif) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.firePos = lookPos;
        this.endPos = scaledLookPos;
        this.spawnDelay = 40 - delayModif;
    }

    public ProjectileMagicMissile(World worldIn) {
        super(worldIn);
    }

    public ProjectileMagicMissile(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!world.isRemote) {
            if(firePos != null) {
                if(spawnDelay <= 0) {
                    this.shootProjectile(firePos, endPos);
                } else {
                    spawnDelay--;
                    this.motionX = 0;
                    this.motionZ = 0;
                    this.motionY = 0;
                }
            }
        }
    }

    /**
     * Shoots Projectile at a delayed time
     * @param startPos
     * @param scaledPos
     */
    private void shootProjectile(Vec3d startPos, Vec3d scaledPos) {
        ProjectileMagicMissile missile = new ProjectileMagicMissile(world, this.shootingEntity, this.getDamage());
        missile.setPosition(this.posX, this.posY, this.posZ);
        Vec3d fromTargetTooActor = startPos.subtract(scaledPos);
        Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(0);
        Vec3d lineStart = scaledPos.subtract(lineDir);
        Vec3d lineEnd = scaledPos.add(lineDir);
        float speed = (float) 1.5;
        this.setDead();
        missile.setTravelRange(20F);
        world.spawnEntity(missile);
        ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
            ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
        });
        this.playSound(SoundsHandler.LICH_SHOOT_MISSILE, 0.75f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        this.firePos = null;
        this.endPos = null;
    }

    @Override
    protected void spawnParticles() {
      //  ParticleManager.spawnSwirl2(world, this.getPositionVector(), ModColors.YELLOW, Vec3d.ZERO);
        ParticleManager.spawnDust(world, this.getPositionVector(), ModColors.AZURE, Vec3d.ZERO, ModRand.range(10, 15));
    }

    @Override
    protected void onHit(RayTraceResult result) {
        DamageSource source = ModDamageSource.builder()
                .type(ModDamageSource.MAGIC)
                .directEntity(this)
                .indirectEntity(shootingEntity)
                .stoppedByArmorNotShields().build();

        ModUtils.handleBulletImpact(result.entityHit, this, this.getDamage(), source);
        this.playSound(SoundEvents.BLOCK_STONE_BREAK, 0.8f, 0.8f / (rand.nextFloat() * 0.4f + 0.4f));
        if(firePos != null) {
            super.onHit(result);
        }
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
