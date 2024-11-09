package com.dungeon_additions.da.world.blossom;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.integration.ModIntegration;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

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
            templateAdjusted = new BlossomTemplate(manager, "blossom_arena_dd_compat", pos, rot, 0, true);
        } else {
            templateAdjusted = new BlossomTemplate(manager, "blossom_arena", pos, rot, 0, true);
        }

        components.add(templateAdjusted);
        this.posIdentified = pos;
        BlossomTemplate.resetTemplateCount();
       // System.out.println("Generated Blossom Cave at " + pos);
        //Generates a top portion to help players indicate that a Blossom Cave is underneath
        generateTopIndicationTower(templateAdjusted, pos, rot);
    }


    public boolean generateTopIndicationTower(BlossomTemplate parent, BlockPos pos, Rotation rot) {
        int yDifference = getGroundFromAbove(world, posIdentified.getX() - 26, pos.getZ() - 26);
        BlockPos posToo = new BlockPos(posIdentified.getX() + 26, yDifference - 4, posIdentified.getZ() + 26);
       // System.out.println("Generated Tower Top At" + posToo);
        BlossomTemplate top = addAdjustedPiece(parent, posToo, "blossom_top", rot);
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
