package com.dungeon_additions.da.blocks;

import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

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
        return null;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) <= 0)
        { spawnPickedItems(worldIn, pos, null); }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        Item item = itemstack.getItem();

        if (item instanceof ItemPickaxe)
        {
            spawnPickedItems(worldIn, pos, facing);
            itemstack.damageItem(1, playerIn);

            return true;
        }
        return false;
    }

    private static final ResourceLocation LOOT_STONE = new ResourceLocation(ModReference.MOD_ID, "arena_fire_stone");

    /** Spawns the item from the Shell Sand Loot table. */
    public void spawnPickedItems(World worldIn, BlockPos pos, @Nullable EnumFacing facing)
    {
        if (!worldIn.isRemote)
        {
            Random rand = worldIn.rand;
            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)worldIn);
            List<ItemStack> result = worldIn.getLootTableManager().getLootTableFromLocation(LOOT_STONE).generateLootForPools(rand, lootcontext$builder.build());

            for (ItemStack lootItem : result)
            {
                double d0 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
                double d1 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
                double d2 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
                BlockPos itemDirection = pos;

                if (facing != null)
                {
                    switch (facing)
                    {
                        case DOWN: d1 = 0.75D;
                        case EAST: d0 = 0.25D;
                        case NORTH: d2 = 0.25D;
                        case SOUTH: d2 = 0.25D;
                        case UP: d1 = 0.25D;
                        case WEST: d0 = 0.75D;
                        default: break;
                    }

                    itemDirection = pos.offset(facing);

                }

                EntityItem entityitem = new EntityItem(worldIn, (double)itemDirection.getX() + d0, (double)itemDirection.getY() + d1, (double)itemDirection.getZ() + d2, lootItem);
                entityitem.setDefaultPickupDelay();
                worldIn.spawnEntity(entityitem);
            }
        }
    }
}
