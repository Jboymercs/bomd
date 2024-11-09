package com.dungeon_additions.da.blocks.base;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockStairBase extends BlockStairs implements IHasModel {
    protected BlockStairBase(IBlockState modelState) {
        super(modelState);
    }

    public BlockStairBase(String name, IBlockState modelState) {
        super(modelState);
        setTranslationKey(name);
        setRegistryName(name);
        this.setLightOpacity(255);
        this.useNeighborBrightness = true;
        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public BlockStairBase(String name, IBlockState modelState, float hardness, float resistance, SoundType soundType) {
        this(name, modelState);
        setHardness(hardness);
        setResistance(resistance);
        setSoundType(soundType);
        this.setLightOpacity(255);
        this.useNeighborBrightness = true;
    }




    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
