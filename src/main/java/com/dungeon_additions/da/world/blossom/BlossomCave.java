package com.dungeon_additions.da.world.blossom;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.integration.ModIntegration;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.nether_arena.NetherArenaTemplate;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import sun.java2d.pipe.SpanClipRenderer;

import java.util.List;

public class BlossomCave {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    protected BlockPos posIdentified;

    public BlossomCave(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;

    }

    public void startVault(BlockPos pos, Rotation rot) {
        BlossomTemplate templateAdjusted;
        if(ModIntegration.IS_DEEPER_DEPTHS_LOADED) {
            templateAdjusted = new BlossomTemplate(manager, "dd/arena_1", pos, rot, 0, true);
            generateArenaDD(templateAdjusted, pos, rot);
        } else {
           templateAdjusted = new BlossomTemplate(manager, "arena_1", pos, rot, 0, true);
            generateArena(templateAdjusted, pos, rot);
        }

        components.add(templateAdjusted);
        this.posIdentified = pos;
        BlossomTemplate.resetTemplateCount();
       // System.out.println("Generated Blossom Cave at " + pos);
        //Generates a top portion to help players indicate that a Blossom Cave is underneath
        //generateTopIndicationTower(templateAdjusted, pos, rot);
    }

    private boolean generateArena(BlossomTemplate parent, BlockPos pos, Rotation rot) {
        String[] arena_2 = {"arena_2","arena_2_b"};
        String[] arena_3 = {"arena_3","arena_3_b"};
        String[] arena_4 = {"arena_4","arena_4_b"};
        BlossomTemplate arenaTwo = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0,0,0), ModRand.choice(arena_2), rot);
        BlossomTemplate arenaThree = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-26,0,26), ModRand.choice(arena_3), rot);
        BlossomTemplate arenaFour = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0,0,26), ModRand.choice(arena_4), rot);
        components.add(arenaTwo);
        components.add(arenaThree);
        components.add(arenaFour);
        return true;
    }

    private boolean generateArenaDD(BlossomTemplate parent, BlockPos pos, Rotation rot) {
        String[] arena_2 = {"dd/arena_2","dd/arena_2_b","dd/arena_2_c"};
        String[] arena_3 = {"dd/arena_3","dd/arena_3_b","dd/arena_3_c"};
        String[] arena_4 = {"dd/arena_4","dd/arena_4_b","dd/arena_4_c"};
        BlossomTemplate arenaTwo = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0,0,0), ModRand.choice(arena_2), rot);
        BlossomTemplate arenaThree = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-26,0,26), ModRand.choice(arena_3), rot);
        BlossomTemplate arenaFour = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0,0,26), ModRand.choice(arena_4), rot);
        components.add(arenaTwo);
        components.add(arenaThree);
        components.add(arenaFour);
        return true;
    }


    public boolean generateTopIndicationTower(BlossomTemplate parent, BlockPos pos, Rotation rot) {
        int yDifference = getGroundFromAbove(world, posIdentified.getX() - 25, pos.getZ() - 25);
        BlockPos posToo = new BlockPos(posIdentified.getX() + 25, yDifference - 4, posIdentified.getZ() + 25);
        String[] arena_2 = {"blossom_top","blossom_top_2","blossom_top_3"};
        BlossomTemplate top = addAdjustedPiece(parent, posToo, ModRand.choice(arena_2), rot);
        components.add(top);
        return true;
    }


    public static int getGroundFromAbove(World world, int x, int z)
    {
        int y = WorldConfig.max_top_part;
        boolean foundGround = false;
        while(!foundGround && y-- >= 31)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt != Blocks.AIR && blockAt != Blocks.LEAVES && blockAt != Blocks.LEAVES2;
        }

        return y;
    }



    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private BlossomTemplate addAdjustedPiece(BlossomTemplate parent, BlockPos pos, String type, Rotation rot) {
        BlossomTemplate newTemplate = new BlossomTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(BlossomTemplate parent, BlossomTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}
