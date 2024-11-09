package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAir extends Item
{
    private final Block block;

    public ItemAir(Block blockIn)
    {
        this.block = blockIn;
    }

    public String getTranslationKey(ItemStack stack)
    {
        return this.block.getTranslationKey();
    }

    public String getTranslationKey()
    {
        return this.block.getTranslationKey();
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        this.block.addInformation(stack, worldIn, tooltip, flagIn);
    }
}