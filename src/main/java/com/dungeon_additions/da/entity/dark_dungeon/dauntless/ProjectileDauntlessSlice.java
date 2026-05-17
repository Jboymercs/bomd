package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileThousandCuts;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProjectileDauntlessSlice extends Projectile {

    public ProjectileDauntlessSlice(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
    }

    public ProjectileDauntlessSlice(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }


    @Override
    protected void onHit(RayTraceResult result) {
        if(shootingEntity != null) {
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
