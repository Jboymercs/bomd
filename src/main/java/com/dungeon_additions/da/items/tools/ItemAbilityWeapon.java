package com.dungeon_additions.da.items.tools;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemAbilityWeapon extends ToolSword{

    public ItemAbilityWeapon(String name, ToolMaterial material) {
        super(name, material);
    }

    /**
     * Used for adding NBT stats to each weapon
     * @param stack
     * @param worldIn
     * @param entityIn
     * @param itemSlot
     * @param isSelected
     */
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if(stack.getItem() instanceof ItemAbilityWeapon) {
                if(!stack.hasTagCompound()) {
                    this.setNBTonWeapon(stack, false);
                }
            }
        }
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public void setNBTonWeapon(ItemStack stack, boolean val)
    {
        NBTTagCompound nbt;
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("ability"))
        { nbt = stack.getTagCompound(); }
        else
        { nbt = new NBTTagCompound(); }

        nbt.setBoolean("ability", val);
        stack.setTagCompound(nbt);
    }

    public boolean getAbilityVal(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("ability") && stack.getTagCompound().getBoolean("ability");
    }

    public void setAbilityVal(ItemStack stack, boolean val) {
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("ability")) {
            NBTTagCompound nbt = stack.getTagCompound();
            nbt.setBoolean("ability", val);
            stack.setTagCompound(nbt);
        }
    }
}
