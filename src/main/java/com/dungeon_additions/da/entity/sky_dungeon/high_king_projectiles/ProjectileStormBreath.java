package com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles;

import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ProjectileStormBreath extends Projectile {

    private int tornadoTimer = 20;

    public ProjectileStormBreath(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setSize(1.25F, 1.0F);
        this.setNoGravity(true);
    }

    private boolean breakPathing = false;
    private EntityLivingBase target;

    public ProjectileStormBreath(World worldIn, EntityLivingBase throwerIn, float damage, EntityLivingBase target) {
        super(worldIn, throwerIn, damage);
        this.setSize(1.25F, 1.0F);
        this.setNoGravity(true);
        this.target = target;
    }

    public ProjectileStormBreath(World worldIn) {
        super(worldIn);
        this.setSize(1.25F, 1.0F);
        this.setNoGravity(true);
    }

    public ProjectileStormBreath(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setSize(1.25F, 1.0F);
        this.setNoGravity(true);
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!world.isRemote) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            tornadoTimer--;

            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(0.7, 1.5, 0.7), e -> !e.getIsInvulnerable() && (!(e instanceof EntitySkyBase)));
            if(!targets.isEmpty()) {
                for(EntityLivingBase base : targets) {
                    if(!(base instanceof EntitySkyBase)) {
                        if(base.canBePushed() && tornadoTimer < 0 && this.ticksExisted >= 30) {
                            base.motionY += 1.4;
                            base.velocityChanged = true;
                            tornadoTimer = 10;
                            playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.4F, 1.5f / (this.rand.nextFloat() * 0.4f + 0.8f));
                        }
                    }
                }
            }

            if(ticksExisted > 5 && target != null && !this.breakPathing) {

                Vec3d posToTravelToo = target.getPositionVector().add(ModUtils.yVec(1.0D));
                Vec3d currPos = this.getPositionVector();
                Vec3d dir = posToTravelToo.subtract(currPos).normalize();
                ModUtils.addEntityVelocity(this, dir.scale(0.07 * 0.4));

                if(this.getDistanceSq(target) <= 2) {
                    this.breakPathing = true;
                }
            }

            if(world.getBlockState(this.getPosition().add(0, -1, 0)).isFullBlock()) {
                this.motionY = 0;
            }


            if (!world.isRemote && this.ticksExisted > 300) {
                this.setDead();
            }

        }
    }


    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            if(world.rand.nextInt(2) == 0) {
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(0.2F - ModRand.getFloat(0.4F),0.25,0.2F - ModRand.getFloat(0.4F)), ModColors.WHITE, new Vec3d(0, 0.08, 0));
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(0.2F - ModRand.getFloat(0.4F),0.25,0.2F - ModRand.getFloat(0.4F)), ModColors.WHITE, new Vec3d(ModRand.getFloat(0.15F) - 0.1F, 0.08, ModRand.getFloat(0.15F) - 0.1F));
            }

        }
    }

    @Override
    protected void onHit(RayTraceResult result) {

    }
}
