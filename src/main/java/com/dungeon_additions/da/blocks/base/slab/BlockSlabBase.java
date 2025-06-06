package com.dungeon_additions.da.blocks.base.slab;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockSlabBase extends BlockSlab implements IHasModel {
    Block half;
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.<Variant>create("variant", Variant.class);

    public BlockSlabBase(String name, Material materialIn, BlockSlab half) {
        super(materialIn);
        setTranslationKey(name);
        setRegistryName(name);
        this.useNeighborBrightness = !this.isDouble();
        this.setLightOpacity(255);
        IBlockState state = this.blockState.getBaseState().withProperty(VARIANT, Variant.DEFAULT);
        if (!this.isDouble()) state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);

        this.half = half;

        ModBlocks.BLOCKS.add(this);
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(half);
    }
    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (!this.isDouble())
        {
            return this.getDefaultState().withProperty(HALF, EnumBlockHalf.values()[meta % EnumBlockHalf.values().length]);
        }

        return this.getDefaultState();
    }
    @Override
    public int getMetaFromState(IBlockState state) {
        if(this.isDouble())
            return 0;

        return ((EnumBlockHalf)state.getValue(HALF)).ordinal() + 1;
    }
    @Override
    protected BlockStateContainer createBlockState() {
        if(!this.isDouble()) return new BlockStateContainer(this, new IProperty[]{VARIANT, HALF});
        else return new BlockStateContainer(this, new IProperty[]{VARIANT});
    }
    @Override
    public String getTranslationKey(int meta) {
        return super.getTranslationKey();
    }
    @Override
    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }
    @Override
    public Comparable<?> getTypeForItem(ItemStack itemStack) {
        return Variant.DEFAULT;
    }

    public static enum Variant implements IStringSerializable {
        DEFAULT;

        @Override
        public String getName() {
            return "default";
        }
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
