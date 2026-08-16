package com.dungeon_additions.da.world;

import com.dungeon_additions.da.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenMossGrowth extends WorldGenerator {
    private final IBlockState mossBlock = ModBlocks.MOSS_BLOCK.getDefaultState();
    private final IBlockState grassBlock = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
    private final IBlockState doubleGrassBlock = Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS);
    private final IBlockState mossCarpetBlock = ModBlocks.MOSS_CARPET.getDefaultState();

    /* The max radius for moss to generate.*/
    private final int maxRadius;
    private final float foilagePlacementChance;

    public WorldGenMossGrowth(int radiusIn, float carpetChanceIn)
    {
        this.maxRadius = radiusIn;
        this.foilagePlacementChance = carpetChanceIn;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int radiusX = (this.maxRadius - 1) / 2;
        int radiusZ = (this.maxRadius - 1) / 2;

        for (int x = position.getX() - radiusX; x <= position.getX() + radiusX; x++)
        {
            for (int z = position.getZ() - radiusZ; z <= position.getZ() + radiusZ; z++)
            {
                float dx = x - position.getX();
                float dz = z - position.getZ();

                BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos(x, position.getY(), z);

                /* Crawls the Y-level based on if the given position is an Air Block. */
                if (worldIn.isAirBlock(checkPos))
                {
                    while (!(worldIn.getBlockState(checkPos).isSideSolid(worldIn, checkPos, EnumFacing.UP)) && checkPos.getY() > position.getY() - 7)
                    { checkPos.setY(checkPos.getY() - 1); }
                }
                else
                {
                    while (worldIn.getBlockState(checkPos).isSideSolid(worldIn, checkPos, EnumFacing.UP) && checkPos.getY() < position.getY() + 5)
                    { checkPos.setY(checkPos.getY() + 1); }
                }

                /* Messes with the edges of the otherwise spheroid thingy */
                float wobble = (rand.nextFloat() - 0.5f) * 1.2f;
                float dist = (dx * dx) / (radiusX * radiusX) + (dz * dz) / (radiusZ * radiusZ) + wobble;

                if (dist <= 1.0f)
                { preformBlockPlacements(worldIn, checkPos.down(), rand); }
            }
        }

        return true;
    }

    /** Preforms the actual placement of blocks at the given position. */
    public void preformBlockPlacements(World worldIn, BlockPos pos, Random rand)
    {
        /* First replace any ground with Pale Moss Blocks. */
        if (worldIn.isAirBlock(pos.up()) && (worldIn.getBlockState(pos).getMaterial() == Material.GROUND || worldIn.getBlockState(pos).getMaterial() == Material.GRASS ||
                worldIn.getBlockState(pos).getBlock() == Blocks.STONE))
        {
            worldIn.setBlockState(pos, mossBlock, 2);
        }

        /* After, if the position is Pale Moss, attempt foilage placement. */
        if (worldIn.getBlockState(pos) == mossBlock)
        {
            BlockPos above = pos.up();
            if (worldIn.isAirBlock(above) && rand.nextFloat() < foilagePlacementChance)
            {
                IBlockState state = getRandomGroundCover(rand);

                Block plantBlock = state.getBlock();

                if (plantBlock == Blocks.DOUBLE_PLANT)
                {
                    if (Blocks.DOUBLE_PLANT.canPlaceBlockAt(worldIn, above))
                    {
                        worldIn.setBlockState(above, state.withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), 2);
                        worldIn.setBlockState(above.up(), state.withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
                    }
                }
                else
                { worldIn.setBlockState(above, state, 2); }
            }
        }
    }

    public IBlockState getRandomGroundCover(Random rand)
    {
        int roll = rand.nextInt(60);

        if (roll < 25)  return grassBlock;
        else if (roll < 35) return doubleGrassBlock;
        else return mossCarpetBlock;
    }
}
