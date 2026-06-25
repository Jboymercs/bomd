package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.mini_blossom.EntityBlossomDart;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.render.RenderDartBase;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProjectileDauntlessSlice extends EntityBlossomDart {

    private int explodeTime = 40;

    public ProjectileDauntlessSlice(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn);
        this.setNoGravity(true);
        this.setDamage(damage);
    }

    public ProjectileDauntlessSlice(World worldIn) {
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
                this.doExplodeAttack();
            }
        }
    }

    private void doExplodeAttack() {
        Main.proxy.spawnParticle(35, world, this.posX, this.posY + 0.3F, this.posZ, 0,0,0, 16);

        if(shootingEntity != null) {
            Vec3d offset = this.getPositionVector().add(ModUtils.yVec(0.25D));
            DamageSource source;
            source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(shootingEntity).build();
            float damage = (float) this.getDamage();
            ModUtils.handleAreaImpact(2.5F, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.2F);
        }
        this.playSound(SoundsHandler.DAUNTLESS_AOE_EXPLODE, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.4f));
        this.setDead();
    }


    @Override
    protected void onHit(RayTraceResult result) {
        if(shootingEntity != null && result.entityHit != null) {
            DamageSource source = ModDamageSource.builder()
                    .type(ModDamageSource.MAGIC)
                    .directEntity(this)
                    .indirectEntity(shootingEntity)
                    .stoppedByArmorNotShields().build();
            this.setNoGravity(false);

            ModUtils.handleAreaImpact(0.75F, (e) -> (float) this.getDamage(), this, this.getPositionVector(), source, 0f, 0, false, 0.2F);
           // super.onHit(result);
        }
        this.playSound(SoundEvents.BLOCK_STONE_BREAK, 0.8f, 0.8f / (rand.nextFloat() * 0.4f + 0.4f));
        super.onHit(result);
    }

    @Override
    protected ItemStack getArrowStack() {
        return null;
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
