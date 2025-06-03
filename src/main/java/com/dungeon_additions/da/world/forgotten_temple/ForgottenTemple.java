package com.dungeon_additions.da.world.forgotten_temple;

import com.deeperdepths.common.world.base.ModRand;
import com.deeperdepths.common.world.chambers.TrialChambersTemplate;
import com.dungeon_additions.da.config.WorldConfig;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;

public class ForgottenTemple {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;
    private int SIZE = 4;
    protected BlockPos posIdentified;
    private boolean generatedBossRoom;
    private boolean generatedKeyRoom;

    protected int chamberTubeVarHeight = WorldConfig.temple_y_height + 12;

    public ForgottenTemple(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    public void startDungeon(BlockPos pos, Rotation rot) {
        ForgottenTempleTemplate templeAdjusted = new ForgottenTempleTemplate(manager, "puzzle/puzzle_1", pos, rot, 0, true);
        components.add(templeAdjusted);
        this.posIdentified = pos.add(15, 0, 15);
        ForgottenTempleTemplate.resetTemplateCount();
        System.out.println("Generating Desert Dungeon at" + pos);
        List<StructureComponent> structures = new ArrayList<>(components);
        placeModifyTube(templeAdjusted, BlockPos.ORIGIN.add(-17, 12, 0), rot);
    }


    protected int tube_count = 1;

    public boolean placeModifyTube(ForgottenTempleTemplate parent, BlockPos pos, Rotation rot) {
        String[] tube_types = {"extras/tube_1","extras/tube_2","extras/tube_3"};
        ForgottenTempleTemplate tube = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(tube_types), rot);
        components.add(tube);
        int yDifference = getGroundFromAbove(world, posIdentified.getX(), posIdentified.getZ());

        if(yDifference - 6 >= chamberTubeVarHeight) {
            chamberTubeVarHeight += 3;
            placeModifyTube(tube, BlockPos.ORIGIN.add(-5, 3, 0), rot);
        } else {
            //Place top of structure
            constructHold(tube, BlockPos.ORIGIN.add(0, 3, 0), rot);
            System.out.println("Ran Through Tube Code");
        }
        return true;
    }

    private boolean constructHold(ForgottenTempleTemplate parent, BlockPos pos, Rotation rot) {
        ForgottenTempleTemplate part_1 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-22, 0, -8), "temple_1", rot);
        ForgottenTempleTemplate part_2 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-22, 0, 9), "temple_2", rot);
        ForgottenTempleTemplate part_3 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-2, 0, -8), "temple_3", rot);
        ForgottenTempleTemplate part_4 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-2, 0, 9), "temple_4", rot);

        components.add(part_1);
        components.add(part_2);
        components.add(part_3);
        components.add(part_4);
        return true;
    }



    public static int getGroundFromAbove(World world, int x, int z)
    {
        int y = WorldConfig.temple_max_y_height;
        boolean foundGround = false;
        while(!foundGround && y-- >= WorldConfig.temple_y_height + 12)
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
    private ForgottenTempleTemplate addAdjustedPieceWithoutDistance(ForgottenTempleTemplate parent, BlockPos pos, String type, Rotation rot) {
        ForgottenTempleTemplate newTemplate = new ForgottenTempleTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }

    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private ForgottenTempleTemplate addAdjustedPiece(ForgottenTempleTemplate parent, BlockPos pos, String type, Rotation rot) {
        ForgottenTempleTemplate newTemplate = new ForgottenTempleTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(ForgottenTempleTemplate parent, ForgottenTempleTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}
