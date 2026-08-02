package com.dungeon_additions.da.entity.rot_knights;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.mini_blossom.EntityBlossomDart;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProjectileStormTrident extends EntityBlossomDart {
    private int explodeTime = 400;
    private float falterBuildup;

    public ProjectileStormTrident(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn);
        this.setNoGravity(true);
        this.setDamage(damage);
    }

    public ProjectileStormTrident(World worldIn, EntityLivingBase throwerIn, float damage, float falterBuildup) {
        super(worldIn, throwerIn);
        this.setNoGravity(true);
        this.setDamage(damage);
        this.falterBuildup = falterBuildup;
    }

    public ProjectileStormTrident(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.inGround) {
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
            this.setPosition(this.posX, this.posY, this.posZ);
            explodeTime--;
            if(explodeTime < 0) {
                this.setDead();
            }
        }
    }


    @Override
    protected void onHit(RayTraceResult result) {
        if(shootingEntity != null && result.entityHit != null) {
            this.playSound(SoundsHandler.ROT_TRIDENT_IMPACT, 1.3f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
            DamageSource source = ModDamageSource.builder()
                    .type(ModDamageSource.MAGIC)
                    .directEntity(this)
                    .indirectEntity(shootingEntity)
                    .stoppedByArmorNotShields().build();
            this.setNoGravity(false);
            if(shootingEntity instanceof EntityPlayer) {
                ModUtils.handleAreaImpact(0.75F, (e) -> (float) this.getDamage(), this, this.getPositionVector(), source, 0f, 0, false, falterBuildup);
                ((EntityPlayer) shootingEntity).addPotionEffect(new PotionEffect(MobEffects.SPEED, 200, 0, false, true));
            } else {
                ModUtils.handleAreaImpact(0.75F, (e) -> (float) this.getDamage(), this, this.getPositionVector(), source, 0f, 0, false, 0.7F);
                // super.onHit(result);
            }
        } else {
            super.onHit(result);
            this.playSound(SoundsHandler.ROT_TRIDENT_IMPACT, 1.3f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return null;
    }
}
