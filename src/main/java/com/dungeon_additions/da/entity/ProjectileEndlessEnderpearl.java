package com.dungeon_additions.da.entity;

import com.dungeon_additions.da.entity.projectiles.EntityModThrowable;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ProjectileEndlessEnderpearl extends EntityThrowable {
    private EntityLivingBase perlThrower;

    public ProjectileEndlessEnderpearl(World worldIn)
    {
        super(worldIn);
    }

    public ProjectileEndlessEnderpearl(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
        this.perlThrower = throwerIn;
    }

    @SideOnly(Side.CLIENT)
    public ProjectileEndlessEnderpearl(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static void registerFixesEnderPearl(DataFixer fixer)
    {
        EntityThrowable.registerFixesThrowable(fixer, "ThrownEnderpearl");
    }

    protected void onImpact(RayTraceResult result)
    {
        EntityLivingBase entitylivingbase = this.getThrower();

        if (result.entityHit != null)
        {
            if (result.entityHit == this.perlThrower)
            {
                return;
            }

            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, entitylivingbase), 0.0F);
        }

        if (result.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos blockpos = result.getBlockPos();
            TileEntity tileentity = this.world.getTileEntity(blockpos);

            if (tileentity instanceof TileEntityEndGateway)
            {
                TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)tileentity;

                if (entitylivingbase != null)
                {
                    if (entitylivingbase instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.ENTER_BLOCK.trigger((EntityPlayerMP)entitylivingbase, this.world.getBlockState(blockpos));
                    }

                    tileentityendgateway.teleportEntity(entitylivingbase);
                    this.setDead();
                    return;
                }

                tileentityendgateway.teleportEntity(this);
                return;
            }
        }

       // for (int i = 0; i < 5; ++i)
      //  {
           // this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
           // this.spawnImpactParticles();
       // }

        if (!this.world.isRemote)
        {
            if (entitylivingbase instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)entitylivingbase;

                if (entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.world == this.world && !entityplayermp.isPlayerSleeping())
                {
                    net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(entityplayermp, this.posX, this.posY, this.posZ, 5.0F);
                    if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
                    { // Don't indent to lower patch size
                        if (this.rand.nextFloat() < 0.05F && this.world.getGameRules().getBoolean("doMobSpawning"))
                        {
                            EntityEndermite entityendermite = new EntityEndermite(this.world);
                            entityendermite.setSpawnedByPlayer(true);
                            entityendermite.setLocationAndAngles(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, entitylivingbase.rotationYaw, entitylivingbase.rotationPitch);
                            this.world.spawnEntity(entityendermite);
                        }

                        if (entitylivingbase.isRiding())
                        {
                            entitylivingbase.dismountRidingEntity();
                        }

                        entitylivingbase.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                        entitylivingbase.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 0));
                        entitylivingbase.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.8F, 0.4F);
                        entitylivingbase.fallDistance = 0.0F;
                        entitylivingbase.attackEntityFrom(DamageSource.FALL, event.getAttackDamage());
                    }
                }
            }
            else if (entitylivingbase != null)
            {
                entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                entitylivingbase.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 0));
                entitylivingbase.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.8F, 0.4F);
                entitylivingbase.fallDistance = 0.0F;
            }

            this.setDead();
        }
    }

    public void onUpdate()
    {
        EntityLivingBase entitylivingbase = this.getThrower();

        if (entitylivingbase != null && entitylivingbase instanceof EntityPlayer && !entitylivingbase.isEntityAlive())
        {
            this.setDead();
        }
        else
        {
            super.onUpdate();
        }
    }

    @SideOnly(Side.CLIENT)
    protected void spawnImpactParticles() {
            //spawns the impact Particles
            ModUtils.circleCallback(1, 10, (pos) -> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnColoredSmoke(world, pos.add(this.getPositionVector()), ModColors.MAELSTROM, ModUtils.yVec(0.1));
            });
        ModUtils.circleCallback(1, 10, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y);
            ParticleManager.spawnColoredSmoke(world, pos.add(this.getPositionVector()), ModColors.MAELSTROM, ModUtils.yVec(0.6));
        });

        ModUtils.circleCallback(1, 10, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y);
            ParticleManager.spawnColoredSmoke(world, pos.add(this.getPositionVector()), ModColors.MAELSTROM, ModUtils.yVec(1));
        });
        ModUtils.circleCallback(1, 10, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y);
            ParticleManager.spawnColoredSmoke(world, pos.add(this.getPositionVector()), ModColors.MAELSTROM, ModUtils.yVec(1.6));
        });
        ModUtils.circleCallback(1, 10, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y);
            ParticleManager.spawnColoredSmoke(world, pos.add(this.getPositionVector()), ModColors.MAELSTROM, ModUtils.yVec(2));
        });

    }

    @Nullable
    public Entity changeDimension(int dimensionIn, net.minecraftforge.common.util.ITeleporter teleporter)
    {
        if (this.thrower.dimension != dimensionIn)
        {
            this.thrower = null;
        }

        return super.changeDimension(dimensionIn, teleporter);
    }
}
