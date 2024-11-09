package com.dungeon_additions.da.blocks;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.capability.DACapability;
import com.dungeon_additions.da.entity.blossom.EntityAbstractVoidBlossom;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.tileEntity.TileEntitySporeBlossom;
import com.dungeon_additions.da.event.EventScheduler;
import com.dungeon_additions.da.event.TimedEvent;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModParticle;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSporeBlossom extends BlockLeaveBase implements  ITileEntityProvider {
    /**
     * Plant for the Barrend Concept
     */

    protected static final AxisAlignedBB CRYSTAL_AABB = new AxisAlignedBB(0.1D, 0D, 0.1D, 0.9D, 0.25D, 0.9D);

    protected int healDelay = 64;
    public BlockSporeBlossom(String name, float hardness, float resistance, SoundType soundType) {
        super(name, hardness, resistance, soundType);

        // Add both an item as a block and the block itself

    }

    @Override
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
    {
        destroyNearbyFences(worldIn, pos);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
          //  if(!worldIn.isRemote) {
         //       worldIn.scheduleUpdate(pos, this, healDelay);
         //       healNearbyEntities(worldIn, new Vec3d(pos), pos);
        //    }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {

            if (rand.nextInt(10) == 0) {
                for(int i = 0; i < 20; i++) {
                    Vec3d particlePos = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5);
                    ParticleManager.spawnColoredSmoke(worldIn, particlePos, ModColors.PINK, new Vec3d(0, 0, 0));
                }
            }

    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {

            worldIn.scheduleUpdate(pos, this, 1);

        if(worldIn.rand.nextInt(2) == 0) {

            createFences(worldIn, pos);
        }

    }

    @SideOnly(Side.CLIENT)
    private void healNearbyEntities(World world, Vec3d source, BlockPos pos) {

        Vec3d particlePos = source.add(0.5, 1, 0.5);

        System.out.println("Attempting Spawning of Particles");
        for(int i = 0; i < 20; i++) {
            ParticleManager.spawnColoredSmoke(world, particlePos, ModColors.PINK, new Vec3d(0, 0, 0));
            if(i == 19) {
              //  sendParticleHeal(world, source, pos);
            }
        }

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CRYSTAL_AABB;
    }




    @Override
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {

        return null;
    }



protected void createFences(World world, BlockPos pos) {
    //First Layer
    if(world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(1, 1, 0), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(-1, 1, 0), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(1, 1, 1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(1, 1, 1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(1, 1, -1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(1, 1, -1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(-1, 1, -1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(-1, 1, 1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(0, 1, 1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(0, 1, -1), ModBlocks.VINE_WALL.getDefaultState());
    }
    //Second Layer
    if(world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(1, 0, 0), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(-1, 0, 0), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(1, 0, 1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(1, 0, 1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(1, 0, -1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(1, 0, -1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(-1, 0, 1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(-1, 0, -1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(0, 0, 1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(0, 0, -1), ModBlocks.VINE_WALL.getDefaultState());
    }
    //Third Layer
    if(world.getBlockState(pos.add(1, -1, 0)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(1, -1, 0), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(-1, -1, 0)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(-1, -1, 0), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(1, -1, 1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(1, -1, 1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(1, -1, -1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(1, -1, -1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(-1, -1, 1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(-1, -1, 1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(-1, -1, -1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(-1, -1, -1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(0, -1, 1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(0, -1, 1), ModBlocks.VINE_WALL.getDefaultState());
    }
    if(world.getBlockState(pos.add(0, -1, -1)).getBlock() == Blocks.AIR) {
        world.setBlockState(pos.add(0, -1, -1), ModBlocks.VINE_WALL.getDefaultState());
    }
}

    protected void destroyNearbyFences(World world, BlockPos pos) {
        //First Layer
        if(world.getBlockState(pos.add(1, 1, 0)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(1, 1, 0));
        }
        if(world.getBlockState(pos.add(-1, 1, 0)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(-1, 1, 0));
        }
        if(world.getBlockState(pos.add(1, 1, 1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(1, 1, 1));
        }
        if(world.getBlockState(pos.add(1, 1, -1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(1, 1, -1));
        }
        if(world.getBlockState(pos.add(-1, 1, -1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(-1, 1, -1));
        }
        if(world.getBlockState(pos.add(-1, 1, 1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(-1, 1, 1));
        }
        if(world.getBlockState(pos.add(0, 1, 1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(0, 1, 1));
        }
        if(world.getBlockState(pos.add(0, 1, -1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(0, 1, -1));
        }
        //Second Layer
        if(world.getBlockState(pos.add(1, 0, 0)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(1, 0, 0));
        }
        if(world.getBlockState(pos.add(-1, 0, 0)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(-1, 0, 0));
        }
        if(world.getBlockState(pos.add(1, 0, 1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(1, 0, 1));
        }
        if(world.getBlockState(pos.add(1, 0, -1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(1, 0, -1));
        }
        if(world.getBlockState(pos.add(-1, 0, 1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(-1, 0, 1));
        }
        if(world.getBlockState(pos.add(-1, 0, -1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(-1, 0, -1));
        }
        if(world.getBlockState(pos.add(0, 0, 1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(0, 0, 1));
        }
        if(world.getBlockState(pos.add(0, 0, -1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(0, 0, -1));
        }
        //Third Layer
        if(world.getBlockState(pos.add(1, -1, 0)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(1, -1, 0));
        }
        if(world.getBlockState(pos.add(-1, -1, 0)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(-1, -1, 0));
        }
        if(world.getBlockState(pos.add(1, -1, 1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(1, -1, 1));
        }
        if(world.getBlockState(pos.add(1, -1, -1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(1, -1, -1));
        }
        if(world.getBlockState(pos.add(-1, -1, 1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(-1, -1, 1));
        }
        if(world.getBlockState(pos.add(-1, -1, -1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(-1, -1, -1));
        }
        if(world.getBlockState(pos.add(0, -1, 1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(0, -1, 1));
        }
        if(world.getBlockState(pos.add(0, -1, -1)).getBlock() == ModBlocks.VINE_WALL) {
            world.setBlockToAir(pos.add(0, -1, -1));
        }
    }

    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        return EnumOffsetType.NONE;
    }



    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySporeBlossom();
    }
}
