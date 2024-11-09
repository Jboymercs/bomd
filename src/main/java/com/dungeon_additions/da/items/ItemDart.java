package com.dungeon_additions.da.items;

import com.dungeon_additions.da.entity.mini_blossom.EntityBlossomDart;
import com.dungeon_additions.da.entity.mini_blossom.EntityDart;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.util.EnumDartType;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemDart extends Item {

    public static final IBehaviorDispenseItem DISPENSER_BEHAVIOR = new BehaviorProjectileDispense()
    {
        @Override
        protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
        {
            EntityBlossomDart dart = new EntityDart(worldIn);

            if(stackIn.getItem() == ModItems.POISON_DART && stackIn.getMetadata() == EnumDartType.Poison.meta)
            {
                dart = new EntityDart(worldIn);
            }


            dart.setNoGravity(true);
            dart.pickupStatus = EntityDart.PickupStatus.DISALLOWED;
            dart.setPosition(position.getX(), position.getY(), position.getZ());

            return dart;
        }
    };

    public ItemDart(String name)
    {
        super();
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.COMBAT);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DISPENSER_BEHAVIOR);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return stack.getMetadata() == 2 ? EnumRarity.RARE : super.getRarity(stack);
    }

    @Override
    public String getTranslationKey(ItemStack itemstack)
    {
        int i = itemstack.getItemDamage();

        return this.getTranslationKey() + "_" + EnumDartType.values()[i].toString();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        if (this.isInCreativeTab(tab))
        {
            for (int meta = 0; meta < EnumDartType.values().length; ++meta)
            {
                subItems.add(new ItemStack(this, 1, meta));
            }
        }
    }
}
