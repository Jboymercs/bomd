package com.dungeon_additions.da.entity.mini_blossom;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntityDart extends EntityBlossomDart{
    public EntityLivingBase victim;

    public EntityDart(World worldIn)
    {
        super(worldIn);

    }

    public EntityDart(World world, EntityLivingBase entity, float damage)
    {
        super(world, entity);
        this.setDamage(damage);
    }

    public void entityInit()
    {
        super.entityInit();
        this.setDamage(ModConfig.poison_dart_damage);
    }

    @Override
    protected void arrowHit(EntityLivingBase living)
    {
        if(living instanceof EntityMiniBlossom || living instanceof EntityVoidBlossom) {
            return;
        }
        super.arrowHit(living);

        if (!world.isRemote)
        {
            living.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0, false, false));
        }

        this.isDead = true;
    }

    @Override
    protected ItemStack getArrowStack()
    {
        return new ItemStack(ModItems.POISON_DART, 1, 1);
    }

}
