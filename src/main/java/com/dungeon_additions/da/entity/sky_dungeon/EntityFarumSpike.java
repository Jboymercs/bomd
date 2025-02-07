package com.dungeon_additions.da.entity.sky_dungeon;

import com.dungeon_additions.da.entity.mini_blossom.EntityBlossomDart;
import com.dungeon_additions.da.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntityFarumSpike extends EntityBlossomDart {
    public EntityFarumSpike(World worldIn) {
        super(worldIn);
    }

    public EntityFarumSpike(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    public void entityInit()
    {
        super.entityInit();
        this.setDamage(10D);
    }

    @Override
    protected void arrowHit(EntityLivingBase living)
    {
        if(living instanceof EntitySkyBase) {
            return;
        }
        super.arrowHit(living);

        if (!world.isRemote)
        {
            living.addPotionEffect(new PotionEffect(MobEffects.WITHER, 80, 0, false, false));
        }

        this.isDead = true;
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ModItems.POISON_DART, 1, 2);
    }
}
