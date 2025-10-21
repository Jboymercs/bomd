package com.dungeon_additions.da.entity.desert_dungeon;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityDesertBeam;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ProjectileDesertStorm extends Projectile {
    protected static final float AREA_FACTOR = 0.5f;
    protected int updates = 5;

    public ProjectileDesertStorm(World worldIn, EntityLivingBase throwerIn, float baseDamage, ItemStack stack) {
        super(worldIn, throwerIn, baseDamage);
        this.setNoGravity(true);
        this.setSize(0.25F, 1);
    }

    public EntityPlayer player;


    public ProjectileDesertStorm(World worldIn, EntityLivingBase throwerIn, float baseDamage, ItemStack stack, EntityPlayer owner) {
        super(worldIn, throwerIn, baseDamage);
        this.setNoGravity(true);
        this.setSize(0.25F, 1);
        this.player = owner;
    }

    public ProjectileDesertStorm(World worldIn) {
        super(worldIn);
    }

    public ProjectileDesertStorm(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void spawnParticles() {
        IBlockState block = world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ));
        if (block.isFullCube()) {
            for (int i = 0; i < 5; i++) {
                ParticleManager.spawnBreak(world, this.getPositionVector().add(ModRand.randVec().scale(1.0f).add(ModUtils.yVec(0.75f))), Item.getItemFromBlock(block.getBlock()), ModRand.randVec().scale(0.1).add(ModUtils.yVec(0.1f)));
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        // Keeps the projectile on the surface of the ground
        for (int i = 0; i < updates; i++) {
            if (!world.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).isFullCube()) {
                this.setPosition(this.posX, this.posY - 0.25f, this.posZ);
            } else if (world.getBlockState(new BlockPos(this.posX, this.posY + 1, this.posZ)).isFullCube()) {
                this.setPosition(this.posX, this.posY + 0.25f, this.posZ);
            }
        }

        if(grabbedEntity != null) {
            if(world.getBlockState(this.getPosition().add(0, 1, 0)).causesSuffocation()) {
                this.setDead();
            }
            if(grabbedEntityTime != 0) {
                Vec3d offset = this.getPositionVector().add(0, 0.5, 0);
                grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);
            } else {
                this.setDead();
            }
            grabbedEntityTime--;
        }

        if(grabbedEntity == null) {
            onQuakeUpdate();
        }

        // If the projectile hits water and looses all of its velocity, despawn
        if (!world.isRemote && Math.abs(this.motionX) + Math.abs(this.motionZ) < 0.01f) {
            this.setDead();
        }
    }

    private int grabbedEntityTime = 100;
    private EntityLivingBase grabbedEntity;
    protected void onQuakeUpdate() {
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(AREA_FACTOR).expand(0, 0.25f, 0));
        for (Entity entity : list) {
            if (entity instanceof EntityLivingBase && this.shootingEntity != null && entity != this.shootingEntity && !(entity instanceof EntityDesertBase)) {
                if (player != null) {
                    //player summoned Spell
                            if(entity instanceof EntityAbstractBase) {
                                EntityAbstractBase base = ((EntityAbstractBase) entity);
                                if(base.iAmBossMob) {
                                    if(!base.isImmovable()) {
                                        grabbedEntity = ((EntityLivingBase) entity);
                                    }
                                } else if(base.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getBaseValue() != 1) {
                                    grabbedEntity = base;
                                }
                            } else {
                                EntityLivingBase base = ((EntityLivingBase) entity);
                                if(base.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getBaseValue() != 1) {
                                    grabbedEntity = ((EntityLivingBase) entity);
                                }
                            }
                    DamageSource source = ModDamageSource.builder()
                            .type(ModDamageSource.PROJECTILE)
                            .indirectEntity(shootingEntity)
                            .directEntity(this)
                            .stoppedByArmorNotShields().disablesShields().build();

                    entity.attackEntityFrom(source, (float) getDamage());
                } else {
                    if (entity instanceof EntityAbstractBase) {
                        if (!((EntityAbstractBase) entity).iAmBossMob) {
                            grabbedEntity = ((EntityLivingBase) entity);
                        }
                    } else {
                        grabbedEntity = ((EntityLivingBase) entity);
                    }

                    DamageSource source = ModDamageSource.builder()
                            .type(ModDamageSource.PROJECTILE)
                            .indirectEntity(shootingEntity)
                            .directEntity(this)
                            .stoppedByArmorNotShields().disablesShields().build();

                    entity.attackEntityFrom(source, (float) getDamage());

                }
            }
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {

    }
}
