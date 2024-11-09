package com.dungeon_additions.da.blocks;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockMossBlock extends BlockGrass implements IHasModel {

    public BlockMossBlock(String name) {
        super();
        setTranslationKey(name);
        setRegistryName(name);

        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }



    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
