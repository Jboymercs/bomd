package com.dungeon_additions.da.blocks;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockLeaveBase extends BlockLeaves implements IHasModel {
    public BlockLeaveBase(String name) {
        super();
        setTranslationKey(name);
        setRegistryName(name);

        // Set fancy graphics to true for these leaves
        Main.proxy.setFancyGraphics(this, true);

        // Adds states so that we can use the BlockLeaves decaying feature
        setDefaultState(blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.valueOf(false))
                .withProperty(DECAYABLE, Boolean.valueOf(false)));

        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public BlockLeaveBase(String name, float hardness, float resistance, SoundType soundType) {
        this(name);
        setHardness(hardness);
        setResistance(resistance);
        setSoundType(soundType);
    }

    public void setFancyGraphics(boolean isFancy) {
        this.setGraphicsLevel(isFancy);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return null;
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return new ArrayList<ItemStack>();
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{CHECK_DECAY, DECAYABLE});
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;

        if (!((Boolean) state.getValue(DECAYABLE)).booleanValue()) {
            i |= 4;
        }

        if (((Boolean) state.getValue(CHECK_DECAY)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0))
                .withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
    }
}
