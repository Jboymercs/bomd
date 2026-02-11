package com.dungeon_additions.da.world.lich_tower;

import com.dungeon_additions.da.config.CompatConfig;
import com.dungeon_additions.da.integration.ModIntegration;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;

public class LichTowerForest {
    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    protected BlockPos posIdentified;

    public LichTowerForest(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    public void startTower(BlockPos pos, Rotation rot) {
        LichTowerTemplate templateAdjusted;
        if(ModIntegration.IS_DEEPER_DEPTHS_LOADED && CompatConfig.deeper_depths_compat) {
            templateAdjusted = new LichTowerTemplate(manager, "tower_base_compat", pos, rot, 0, true);
        } else {
            templateAdjusted = new LichTowerTemplate(manager, "tower_base_f", pos, rot, 0, true);
        }


        components.add(templateAdjusted);
        this.posIdentified = pos;
        LichTowerTemplate.resetTemplateCount();
        //gen other parts of the tower
        generateSecondFloor(templateAdjusted, pos, rot);
    }

    //Second part
    public boolean generateSecondFloor(LichTowerTemplate parent, BlockPos pos, Rotation rot) {
        LichTowerTemplate second_floor;
        if(ModIntegration.IS_DEEPER_DEPTHS_LOADED && CompatConfig.deeper_depths_compat) {
            second_floor = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-27, 21, 0), "tower_middle_compat", rot);
        } else {
            second_floor = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-27, 21, 0), "tower_middle_f", rot);
        }

        components.add(second_floor);
        generateTopFloor(second_floor, pos, rot);
        return true;
    }
    //Third Part and last part
    public boolean generateTopFloor(LichTowerTemplate parent, BlockPos pos, Rotation rotation) {
        LichTowerTemplate top_floor;
        if(ModIntegration.IS_DEEPER_DEPTHS_LOADED && CompatConfig.deeper_depths_compat) {
            top_floor = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-27, 25,0), "tower_top_compat", rotation);
        } else {
            top_floor = addAdjustedPiece(parent, BlockPos.ORIGIN.add(-27, 25,0), "tower_top_f", rotation);
        }

        components.add(top_floor);
        return true;
    }


    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private LichTowerTemplate addAdjustedPiece(LichTowerTemplate parent, BlockPos pos, String type, Rotation rot) {
        LichTowerTemplate newTemplate = new LichTowerTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(LichTowerTemplate parent, LichTowerTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}
