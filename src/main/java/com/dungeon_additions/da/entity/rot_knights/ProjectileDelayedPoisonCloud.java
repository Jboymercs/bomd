package com.dungeon_additions.da.entity.rot_knights;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ProjectileDelayedPoisonCloud extends Projectile {

    private int lifeTime = 40;
    public ProjectileDelayedPoisonCloud(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
    }

    public ProjectileDelayedPoisonCloud(World worldIn, EntityLivingBase throwerIn, float damage, int lifeTime) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.lifeTime = lifeTime;
    }

    public ProjectileDelayedPoisonCloud(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
    }

    public ProjectileDelayedPoisonCloud(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
    }

    @Override
    protected void spawnParticles() {
        //  ParticleManager.spawnSwirl2(world, this.getPositionVector(), ModColors.YELLOW, Vec3d.ZERO);
        ParticleManager.spawnColoredSmoke(world, this.getPositionVector(), ModColors.GREEN, new Vec3d(0,0,0));
    }

    private boolean nearbyEntity = false;
    private boolean startCloud = false;
    private int deathCountdown = 200;
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.lifeTime--;

        if(!world.isRemote) {

            List<EntityLivingBase> nearbyMonsters = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.2D), e -> !e.getIsInvulnerable());
            if(!nearbyMonsters.isEmpty()) {
                for(EntityLivingBase base : nearbyMonsters) {
                    if(!(base instanceof EntityRotKnightBoss) && !(base instanceof EntityRotKnight) && !(base instanceof EntityRotKnightRapier) &&
                            !(base instanceof EntityRotSpike) && base != this.shootingEntity && !(base instanceof EntityChevalier)) {
                        nearbyEntity = true;
                    }
                }
            }

            if((this.lifeTime < 0 || nearbyEntity) && !startCloud) {
                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.MAGIC)
                        .directEntity(this)
                        .indirectEntity(shootingEntity)
                        .stoppedByArmorNotShields().build();
                ModUtils.handleAreaImpact(1f, (e) -> this.getDamage(), this, this.getPositionVector(), source, 0.6f, 0, false, MobEffects.POISON, 0, 300, 0.2F);
                this.playSound(SoundsHandler.ROT_SELF_AOE, 0.4f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                this.motionX = 0;
                this.motionY = 0;
                this.motionZ = 0;
                this.startCloud = true;
            }

            if(startCloud) {
                //spawns particles
                if(ticksExisted % 2 == 0) {
                    Main.proxy.spawnParticle(23, world, this.posX + ModRand.getFloat(1.5F), this.posY + ModRand.getFloat(1F), this.posZ + ModRand.getFloat(1.5F), 0, 0.03, 0, 3145519);
                }
                if(ticksExisted % 5 == 0) {
                    //adds Poison to nearby targets
                    List<EntityLivingBase> nearbyMobs = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5D), e -> !e.getIsInvulnerable());
                    if (!nearbyMobs.isEmpty()) {
                        for (EntityLivingBase base : nearbyMobs) {
                            if (!(base instanceof EntityRotKnightBoss) && !(base instanceof EntityRotKnight) && !(base instanceof EntityRotKnightRapier) &&
                                    !(base instanceof EntityRotSpike) && base != this.shootingEntity && !(base instanceof EntityChevalier)) {
                                    base.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 0, false, true));
                            }
                        }
                    }
                }
                deathCountdown--;
                if(deathCountdown < 0) {
                    this.setDead();
                }
            }
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

    @Override
    protected void onHit(RayTraceResult result) {

    }
}
