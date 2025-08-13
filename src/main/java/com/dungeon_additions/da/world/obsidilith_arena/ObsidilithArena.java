package com.dungeon_additions.da.world.obsidilith_arena;

import com.dungeon_additions.da.world.nether_arena.NetherArenaTemplate;
import com.google.common.collect.Lists;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;

public class ObsidilithArena {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    protected BlockPos posIdentified;

    private boolean generatedKeyRoom = false;
    private boolean generatedAtrium = false;
    private boolean generatedLab = false;

    private static final List<Tuple<Rotation, BlockPos>> DUNGEON_CROSS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(16, -4, 8)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(26, -4, 14)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(10, -4, 18)));


    public ObsidilithArena(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    public void startArena(BlockPos pos, Rotation rot) {
        ObsidilithArenaTemplate templateAdjusted = new ObsidilithArenaTemplate(manager, "top_1", pos, rot, 0, true);
        components.add(templateAdjusted);
        NetherArenaTemplate.resetTemplateCount();
        System.out.println("Generated Obsdilith Arena At" + pos);
        generateArena(templateAdjusted, pos, rot);
    }

    private boolean generateArena(ObsidilithArenaTemplate parent, BlockPos pos, Rotation rot) {
        ObsidilithArenaTemplate arenaTwo = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,0,0),"top_3", rot);
        ObsidilithArenaTemplate arenaThree = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-19,0,17), "top_2", rot);
        ObsidilithArenaTemplate arenaFour = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,0,17), "top_4", rot);
        ObsidilithArenaTemplate bottomOne = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-19,-29,0),"bottom_1", rot);
        ObsidilithArenaTemplate bottomTwo = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,-29,0),"bottom_3", rot);
        ObsidilithArenaTemplate bottomThree = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-19,-29,17), "bottom_2", rot);
        ObsidilithArenaTemplate bottomFour = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,-29,17), "bottom_4", rot);
        components.add(arenaTwo);
        components.add(arenaThree);
        components.add(arenaFour);
        components.add(bottomOne);
        components.add(bottomTwo);
        components.add(bottomThree);
        components.add(bottomFour);
        //generateHold(arenaTwo, BlockPos.ORIGIN.add(0,3,11), rot);
        int failedHalls = 0;
        List<StructureComponent> structures = new ArrayList<>(components);
        for (Tuple<Rotation, BlockPos> tuple : DUNGEON_CROSS) {
             if (!generateBridge(parent, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedHalls++;
            }
        }
        if (failedHalls > 3) {
            components.clear();
            components.addAll(structures);
        }
        return true;
    }

    private boolean generateBridge(ObsidilithArenaTemplate parent, BlockPos pos, Rotation rot) {
        ObsidilithArenaTemplate bridge = addAdjustedPieceWithoutDistance(parent, pos, "bridge", rot);
        components.add(bridge);
        //generate specific parts of the dungeon
        if(!generatedKeyRoom) {
            generateEndPiece(bridge, BlockPos.ORIGIN, rot);
            generatedKeyRoom = true;
        } else if (!generatedAtrium) {
            generateEndPieceTwo(bridge, BlockPos.ORIGIN, rot);
            generatedAtrium = true;
        } else {
            generateEndPieceThree(bridge, BlockPos.ORIGIN, rot);
        }
        return true;
    }

    private boolean generateEndPiece(ObsidilithArenaTemplate parent, BlockPos pos, Rotation rot) {
        ObsidilithArenaTemplate endPiece = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0, -10, 0), "end_1", rot);
        components.add(endPiece);
        //generate specific parts of the dungeon
        return true;
    }

    private boolean generateEndPieceTwo(ObsidilithArenaTemplate parent, BlockPos pos, Rotation rot) {
        ObsidilithArenaTemplate endPiece = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0, -17, 0), "end_2", rot);
        components.add(endPiece);
        //generate specific parts of the dungeon
        return true;
    }

    private boolean generateEndPieceThree(ObsidilithArenaTemplate parent, BlockPos pos, Rotation rot) {
        ObsidilithArenaTemplate endPiece = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0, -8, 0), "end_3", rot);
        components.add(endPiece);
        //generate specific parts of the dungeon
        return true;
    }

    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private ObsidilithArenaTemplate addAdjustedPieceWithoutDistance(ObsidilithArenaTemplate parent, BlockPos pos, String type, Rotation rot) {
        ObsidilithArenaTemplate newTemplate = new ObsidilithArenaTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }

    private ObsidilithArenaTemplate addAdjustedPiece(ObsidilithArenaTemplate parent, BlockPos pos, String type, Rotation rot) {
        ObsidilithArenaTemplate newTemplate = new ObsidilithArenaTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(ObsidilithArenaTemplate parent, ObsidilithArenaTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }

}
