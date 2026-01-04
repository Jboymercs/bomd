package com.dungeon_additions.da.entity.blossom;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.blossom.action.ActionSporeBomb;
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

public class ProjectileSporeBomb extends Projectile {
    private static final int PARTICLE_AMOUNT = 1;
    private static final int EXPOSION_AREA_FACTOR = 2;

    public ProjectileSporeBomb(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
    }

    public ProjectileSporeBomb(World worldIn) {
        super(worldIn);
    }

    public ProjectileSporeBomb(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.GREEN, new Vec3d(0, 0.1, 0));
        }
    }

    @Override
    protected void spawnImpactParticles() {
        ModUtils.circleCallback(2, 30, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y);
            ParticleManager.spawnDust(world, pos.add(this.getPositionVector()).add(ModUtils.yVec(5.6)), ModColors.GREEN, pos.normalize().scale(0.3).add(ModUtils.yVec(0.1)), ModRand.range(2, 6));
        } );
    }
    @Override
    protected void onHit(RayTraceResult result) {
        if(!world.isRemote) {
            Main.proxy.spawnParticle(19,world, this.posX, this.posY + 1.25, this.posZ, 0, 0, 0);
            new ActionSporeBomb().performAction(this, null);
        }
        this.playSound(SoundEvents.BLOCK_SLIME_PLACE, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
        super.onHit(result);
    }
}
