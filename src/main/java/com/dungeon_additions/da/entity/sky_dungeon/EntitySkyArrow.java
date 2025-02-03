package com.dungeon_additions.da.entity.sky_dungeon;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.mini_blossom.EntityBlossomDart;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySkyArrow extends EntityBlossomDart {

    private EntityLivingBase target;
    private EntityLivingBase shooter;

    private final byte PARTICLE_BYTE = 4;
    public EntitySkyArrow(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
    }

    public EntitySkyArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.setNoGravity(true);
    }

    public EntitySkyArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase intendedTarget) {
        super(worldIn, shooter);
        this.target = intendedTarget;
        this.shooter = shooter;
        this.setNoGravity(true);
    }

    public void entityInit()
    {
        super.entityInit();
        this.setDamage(15);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if(!this.inGround) {
            this.world.setEntityState(this, this.PARTICLE_BYTE);
        }

        if(target != null && !world.isRemote) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            if (distance <= 16 && !this.onGround) {
                Vec3d targetPos = target.getPositionVector().add(ModUtils.yVec(1.0D));
                Vec3d currPos = this.getPositionVector();
                Vec3d dir = targetPos.subtract(currPos).normalize();
                ModUtils.addEntityVelocity(this, dir.scale(0.45));
            }
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == this.PARTICLE_BYTE) {
            ParticleManager.spawnDust(world, this.getPositionVector(), ModColors.YELLOW, Vec3d.ZERO, ModRand.range(80, 100));
        }
    }


    @Override
    protected void arrowHit(EntityLivingBase living)
    {
        if(!(shooter instanceof EntityPlayer)) {
            if(living instanceof EntitySkyBase) {
                return;
            }
        }
        super.arrowHit(living);

        if (!world.isRemote)
        {
       //     living.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0, false, false));
        }

        this.isDead = true;
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ModItems.SKY_ARROW, 1, 0);
    }


}
