package com.dungeon_additions.da.blocks.blossom;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.blocks.base.IBlockUpdater;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.projectiles.EntityLily;
import com.dungeon_additions.da.entity.projectiles.EntitySoulStar;
import com.dungeon_additions.da.entity.tileEntity.TileEntityUpdater;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.*;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockVoidLily extends BlockBush implements IHasModel, ITileEntityProvider, IBlockUpdater {
    /**
     * Plant for Locating Void Blossom Cave
     */
    private String info_loc;

    private BlockPos particlePosToo;
    protected static final AxisAlignedBB CRYSTAL_AABB = new AxisAlignedBB(0.35D, 0.0D, 0.35D, 0.65D, 0.8D, 0.65D);
    private Item itemDropped;

    public BlockVoidLily(String name, String info_loc) {
        super(Material.PLANTS);
        this.setRegistryName(name);
        this.setTranslationKey(name);
        setSoundType(SoundType.PLANT);
        this.info_loc = info_loc;
        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    @Override
    public void update(World world, BlockPos pos) {
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && player.getHeldItemMainhand().isEmpty()) {
            if(particlePosToo != null) {
                if (isWithinRadius(particlePosToo, pos)) {
                    particlePosToo = null;
                    player.sendStatusMessage(new TextComponentTranslation("da.within_bounds", new Object[0]), true);
                }
            }
        }

        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if(!world.isRemote) {
            if(particlePosToo == null) {
                particlePosToo = findNearestPos(pos, world);
            }

            if(particlePosToo != null) {
                EntityLily entityendereye = new EntityLily(world, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5);
                entityendereye.moveTowards(particlePosToo);
                world.spawnEntity(entityendereye);
                world.playSound((EntityPlayer) null, pos.getX(), pos.getY(), pos.getZ(), SoundsHandler.BLOSSOM_PETAL_WAVE, SoundCategory.NEUTRAL, 0.3F, 0.2F / (world.rand.nextFloat() * 0.4F + 0.4F));
            }
        }
    }


    public static boolean isWithinRadius(BlockPos setPos, BlockPos pos) {
        if(setPos != null) {
            if(setPos.getDistance(pos.getX(), pos.getY(), pos.getZ()) < ModConfig.locator_reset_pos) {
                System.out.println("Within Radius of structure, resetting particlePos");
                return true;
            }
        }
        return false;
    }


    public static BlockPos findNearestPos(BlockPos pos, World world) {
        BlockPos resultpos = null;
        Chunk chunk = world.getChunk(pos);
        //probably laggy as hell but hey it works
        for (int i = -ModConfig.void_blosom_search_distance; i < ModConfig.void_blosom_search_distance + 1; i++) {
            for (int j = -ModConfig.void_blosom_search_distance; j < ModConfig.void_blosom_search_distance + 1; j++) {
                // boolean validspawn = this.IsVillageAtPos(world, (pos.getX() - 8)  >> 4 + i, (pos.getZ() - 8)  >> 4 + j);
                //if(validspawn){
                //Chunk chunk = world.getChunkFromBlockCoords(pos);
                boolean c = canStructureSpawnAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, 100, (chunk.z + j) << 4);
                    break;
                }
                // }
            }
        }
        return resultpos;
    }


    public static boolean isAllowedDimensionTooSpawnIn(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }


    protected static boolean canStructureSpawnAtPos(World world, int chunkX, int chunkZ) {
        int spacing = WorldConfig.void_blossom_cave_weight;
        int separation = 16;
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0) {
            chunkX -= spacing - 1;
        }

        if (chunkZ < 0) {
            chunkZ -= spacing - 1;
        }

        int k = chunkX / spacing;
        int l = chunkZ / spacing;
        Random random = world.setRandomSeed(k, l, 10383709);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l && isAllowedDimensionTooSpawnIn(world.provider.getDimension())) {
            BlockPos pos = new BlockPos((i << 4), 0, (j << 4));
            return isAbleToSpawnHereBlossom(pos, world);
        } else {

            return false;
        }
    }

    public static boolean isAbleToSpawnHereBlossom(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypesBlossom()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return false;
            }
        }
        return true;
    }

    private static List<BiomeDictionary.Type> blossomTowerBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypesBlossom() {
        if(blossomTowerBiomeTypes == null) {
            blossomTowerBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.biome_types_blossom) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) blossomTowerBiomeTypes.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return blossomTowerBiomeTypes;
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock().isFullBlock(state);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CRYSTAL_AABB;
    }

    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        return EnumOffsetType.NONE;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityUpdater();
    }
}
