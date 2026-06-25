package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.frost_dungeon.IDirectionalRender;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ProjectileVerticalLazer extends Projectile implements IDirectionalRender {

    private Vec3d orbitPoint;
    private double radius = 3;

    private double speed = 0.05F;
    private float swirl_screw = 0.2F * ((float)Math.PI * 2F);
    public static final int PARTICLE_AMOUNT = 1;
    protected int updates = 5;
    private Vec3d renderLazerPos;
    private float radiusAdditive = 0.1F;

    public ProjectileVerticalLazer(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setSize(0.5F, 5);
        this.isImmuneToFire = true;
    }

    public ProjectileVerticalLazer(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.setSize(0.5F, 5);
        this.isImmuneToFire = true;
    }

    public ProjectileVerticalLazer(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.setSize(0.5F, 5);
        this.orbitPoint = throwerIn.getPositionVector();
        this.renderLazerPos = this.getPositionVector().add(0, 10, 0);
        this.isImmuneToFire = true;
    }

    public ProjectileVerticalLazer(World worldIn, EntityLivingBase throwerIn, float damage, Vec3d orbitPoint) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.setSize(0.5F, 5);
        this.orbitPoint = orbitPoint;
        this.renderLazerPos = this.getPositionVector().add(0, 10, 0);
        this.isImmuneToFire = true;
    }

    public ProjectileVerticalLazer(World worldIn, EntityLivingBase throwerIn, float damage, float radiusAdditive, float startingRadius) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.setSize(0.5F, 5);
        this.orbitPoint = throwerIn.getPositionVector();
        this.radiusAdditive = radiusAdditive;
        this.radius = startingRadius;
        this.renderLazerPos = this.getPositionVector().add(0, 10, 0);
        this.isImmuneToFire = true;
    }


    @Override
    protected void spawnParticles() {
        IBlockState block = world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ));
        if (block.isFullCube()) {
            for (int i = 0; i < 5; i++) {
                ParticleManager.spawnBreak(world, this.getPositionVector().add(ModRand.randVec().scale(1.0f).add(ModUtils.yVec(0.75f))), Item.getItemFromBlock(block.getBlock()), ModRand.randVec().scale(0.1).add(ModUtils.yVec(0.1f)));
            }
            for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
                Main.proxy.spawnParticle(9, world, this.posX , this.posY + 0.1, this.posZ, world.rand.nextFloat()/3 - world.rand.nextFloat()/3, 0.15, world.rand.nextFloat()/3 - world.rand.nextFloat()/3, ModRand.range(20, 50));
            }
        }
    }



    @Override
    public void onUpdate() {
        super.onUpdate();
        this.radius += radiusAdditive;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.renderLazerPos = this.getPositionVector().add(0, 10, 0);

        if(ticksExisted == 1) {
            this.playSound(SoundsHandler.DAUNTLESS_LAZER_CONSTANT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        }
        if(ticksExisted % 40 == 0) {
            this.playSound(SoundsHandler.DAUNTLESS_LAZER_CONSTANT, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        }

        if(orbitPoint != null) {
            double x = Math.cos(this.ticksExisted * speed + swirl_screw) * radius;
            double z = Math.sin(this.ticksExisted * speed + swirl_screw) * radius;

            this.posX = orbitPoint.x + x;
            this.posZ = orbitPoint.z + z;
        }

        // Keeps the projectile on the surface of the ground
        for (int i = 0; i < updates; i++) {
            if (!world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).isFullCube()) {
                this.setPosition(this.posX, this.posY - 0.25f, this.posZ);
            } else if (world.getBlockState(new BlockPos(this.posX, this.posY + 1, this.posZ)).isFullCube()) {
                this.setPosition(this.posX, this.posY + 0.25f, this.posZ);
            }
        }

        onQuakeUpdate();

        // If the projectile hits water and looses all of its velocity, despawn
        if (!world.isRemote &&  this.ticksExisted == 200) {
            this.setDead();
        }
    }



    protected void onQuakeUpdate() {
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(0.75F).expand(0, 0.25f, 0));
        for (Entity entity : list) {
            if (entity instanceof EntityLivingBase && this.shootingEntity != null && entity != this.shootingEntity) {
                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.MAGIC)
                        .indirectEntity(shootingEntity)
                        .directEntity(this)
                        .stoppedByArmorNotShields().disablesShields().build();

                entity.attackEntityFrom(source, (float) getDamage());

            }
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {

    }

    public Vec3d getRenderDirection() {
        return this.renderLazerPos;
    }

    @Override
    public void setRenderDirection(Vec3d dir) {
        this.renderLazerPos = dir;
    }
}
