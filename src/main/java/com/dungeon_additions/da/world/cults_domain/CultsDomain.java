package com.dungeon_additions.da.world.cults_domain;

import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.forgotten_temple.ForgottenTempleTemplate;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;

public class CultsDomain {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    public CultsDomain(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }


    public void startDungeon(BlockPos pos, Rotation rot) {
        CultsDomainTemplate templeAdjusted = new CultsDomainTemplate(manager, "base_6", pos, rot, 0, true);
        components.add(templeAdjusted);
        CultsDomainTemplate.resetTemplateCount();
        System.out.println("Generating Cults Domain at" + pos);
        List<StructureComponent> structures = new ArrayList<>(components);
        this.constructDungeon(templeAdjusted, pos, rot);
        this.createCeiling(templeAdjusted, pos, rot);
    }

    private boolean constructDungeon(CultsDomainTemplate parent, BlockPos pos, Rotation rot) {
        //middle
        CultsDomainTemplate dungeonPart2 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0, 0, 0),"base_9", rot);
        CultsDomainTemplate dungeonPart3 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(31, 0, 0), "base_11", rot);
        CultsDomainTemplate dungeonPart4 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-62, 0, 0), "base_2", rot);
        //right
        CultsDomainTemplate dungeonPart5 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-31, 0, 31), "base_7", rot);
        CultsDomainTemplate dungeonPart6 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0, 0, 31), "base_10", rot);
        CultsDomainTemplate dungeonPart7 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-62, 0, 31), "base_3", rot);
        //left
        CultsDomainTemplate dungeonPart8 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-31, 0, -31), "base_5", rot);
        CultsDomainTemplate dungeonPart9 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0, 0, -31), "base_8", rot);
        CultsDomainTemplate dungeonPart10 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-62, 0, -31), "base_1", rot);

        CultsDomainTemplate dungeonPart11 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-31, 0, -62), "base_4", rot);
        components.add(dungeonPart2);
        components.add(dungeonPart3);
        components.add(dungeonPart4);

        components.add(dungeonPart5);
        components.add(dungeonPart6);
        components.add(dungeonPart7);

        components.add(dungeonPart8);
        components.add(dungeonPart9);
        components.add(dungeonPart10);

        components.add(dungeonPart11);
        return true;
    }

    private boolean createCeiling(CultsDomainTemplate parent, BlockPos pos, Rotation rot) {
        CultsDomainTemplate dungeonPart1 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-31, 32, 0),"celing_6", rot);
        CultsDomainTemplate dungeonPart2 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0, 32, 0),"celing_9", rot);
        CultsDomainTemplate dungeonPart3 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(31, 32, 0), "celing_11", rot);
        CultsDomainTemplate dungeonPart4 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-62, 32, 0), "celing_2", rot);
        //right
        CultsDomainTemplate dungeonPart5 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-31, 32, 31), "celing_7", rot);
        CultsDomainTemplate dungeonPart6 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0, 32, 31), "celing_10", rot);
        CultsDomainTemplate dungeonPart7 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-62, 32, 31), "celing_3", rot);
        //left
        CultsDomainTemplate dungeonPart8 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-31, 32, -31), "celing_5", rot);
        CultsDomainTemplate dungeonPart9 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0, 32, -31), "celing_8", rot);
        CultsDomainTemplate dungeonPart10 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-62, 32, -31), "celing_1", rot);

        CultsDomainTemplate dungeonPart11 = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-31, 32, -62), "celing_4", rot);
        components.add(dungeonPart1);
        components.add(dungeonPart2);
        components.add(dungeonPart3);
        components.add(dungeonPart4);

        components.add(dungeonPart5);
        components.add(dungeonPart6);
        components.add(dungeonPart7);

        components.add(dungeonPart8);
        components.add(dungeonPart9);
        components.add(dungeonPart10);

        components.add(dungeonPart11);
      return true;
    };

    private CultsDomainTemplate addAdjustedPiece(CultsDomainTemplate parent, BlockPos pos, String type, Rotation rot) {
        CultsDomainTemplate newTemplate = new CultsDomainTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(CultsDomainTemplate parent, CultsDomainTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}
