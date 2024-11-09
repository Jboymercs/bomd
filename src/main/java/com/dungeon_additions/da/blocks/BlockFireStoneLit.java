package com.dungeon_additions.da.blocks;

import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockFireStoneLit extends BlockBase{

    private String info_loc;

    public BlockFireStoneLit(String name, Material material) {
        super(name, material);
    }

    public BlockFireStoneLit(String name, Material material, float hardness, float resistance, SoundType soundType, String info_loc) {
        super(name, material, hardness, resistance, soundType);
        this.info_loc = info_loc;
    }


    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {

        ItemStack contained_loot;
        int randomInterval = rand.nextInt(12);

        if(randomInterval == 2) {
            contained_loot = Items.GOLD_INGOT.getDefaultInstance();
        } else if (randomInterval == 4) {
            contained_loot = ModItems.ENFLAMED_MEAT.getDefaultInstance();
        } else if (randomInterval == 5) {
            contained_loot = Items.GLOWSTONE_DUST.getDefaultInstance();
        } else if (randomInterval == 6) {
            contained_loot = Items.BLAZE_ROD.getDefaultInstance();
        } else if (randomInterval == 7) {
            contained_loot = Items.BLAZE_POWDER.getDefaultInstance();
        } else if (randomInterval == 9) {
            contained_loot = Items.MAGMA_CREAM.getDefaultInstance();
        } else if (randomInterval == 11) {
            contained_loot = Items.GOLDEN_CARROT.getDefaultInstance();
        } else {
            contained_loot = Items.GOLD_NUGGET.getDefaultInstance();
        }



        return contained_loot.getItem();
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;

        int count = quantityDropped(state, fortune, rand);
        for (int i = 0; i < count; i++) {
            Item item = this.getItemDropped(state, rand, fortune);
                if (item != Items.AIR) {
                    drops.add(new ItemStack(item, ModRand.range(2, 4), this.damageDropped(state)));
                }
        }
    }
}
