package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemCloth extends ItemBlock
{
    public ItemCloth(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int damage)
    {
        return damage;
    }

    public String getTranslationKey(ItemStack stack)
    {
        return super.getTranslationKey() + "." + EnumDyeColor.byMetadata(stack.getMetadata()).getTranslationKey();
    }
}