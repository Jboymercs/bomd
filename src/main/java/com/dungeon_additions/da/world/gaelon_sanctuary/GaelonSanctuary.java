package com.dungeon_additions.da.world.gaelon_sanctuary;

import com.dungeon_additions.da.blocks.BlockGaelonCrystal;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.rot_hold.RottenHoldTemplate;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;

public class GaelonSanctuary {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    private boolean generatedBossArena = false;
    private int SIZE = WorldConfig.gaelon_sanctuary_size;
    protected BlockPos posIdentified;

    private boolean generatedKeyRoom = false;
    private boolean generatedBossRoom = false;

    private static final List<Tuple<Rotation, BlockPos>> DUNGEON_CROSS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(22, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 22)));

    private static final List<Tuple<Rotation, BlockPos>> ENTRACE_CROSS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(24, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 24)));


    public GaelonSanctuary(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    public void startHold(BlockPos pos, Rotation rot) {
        GaelonSanctuaryTemplate templateAdjusted = new GaelonSanctuaryTemplate(manager, "entrance", pos, rot, 0, true);
        components.add(templateAdjusted);
        this.posIdentified = pos.add(14, 0, 12);
        GaelonSanctuaryTemplate.resetTemplateCount();
        System.out.println("Generated Gaelon Sanctuary at" + posIdentified);

        List<StructureComponent> structures = new ArrayList<>(components);
        int failedHalls = 0;
        for(Tuple<Rotation, BlockPos> tuple : ENTRACE_CROSS) {
            if(!generateStraight(templateAdjusted, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedHalls++;
            }
        }

        if(failedHalls > 3) {
            components.clear();
            components.addAll(structures);
            //generate Ends
        }

        placeModifyTube(templateAdjusted, BlockPos.ORIGIN.add(-16, 25, 0), rot);
    }


    private boolean generateStraight(GaelonSanctuaryTemplate parent, BlockPos pos, Rotation rot) {
        String[] straight_types = {"tiles/straight_1","tiles/straight_2","tiles/straight_3", "tiles/straight_4"};
        GaelonSanctuaryTemplate straightPiece = addAdjustedPiece(parent, pos, ModRand.choice(straight_types), rot);

        if(straightPiece.getDistance() > SIZE || straightPiece.isCollidingExcParent(manager, parent, components)) {
            return generateEnd(parent, pos, rot);
        }
        //
        components.add(straightPiece);
        //generate cross piece
        generateCross(straightPiece, BlockPos.ORIGIN, rot);
        return true;
    }

    private boolean generateCross(GaelonSanctuaryTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"tiles/cross_1", "tiles/cross_2", "tiles/cross_3","tiles/cross_4","tiles/cross_5"};
        GaelonSanctuaryTemplate crossPiece = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);
        if (crossPiece.getDistance() > SIZE || crossPiece.isCollidingExcParent(manager, parent, components)) {
            return generateEnd(parent, pos, rot);
        }
        components.add(crossPiece);
        List<StructureComponent> structures = new ArrayList<>(components);
        int failedHalls = 0;
        for(Tuple<Rotation, BlockPos> tuple : DUNGEON_CROSS) {
            if(!generateStraight(crossPiece, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedHalls++;
            }
        }

        if(failedHalls > 3) {
            components.clear();
            components.addAll(structures);
            //generate Ends
        }
        return true;
    }

    public boolean generateEnd(GaelonSanctuaryTemplate parent, BlockPos pos, Rotation rot) {
        String[] end_types = {"tiles/end_1","tiles/end_2","tiles/end_3","tiles/end_4","tiles/end_5","tiles/end_6"};
        GaelonSanctuaryTemplate end = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(end_types), rot);

        if(SIZE > 2 && !generatedBossArena) {
            return buildBossArena(parent, pos, rot);
        }

        if(end.isCollidingExcParent(manager, parent, components)) {
            return generateQuickEnd(parent, pos, rot);
        }

        int randI = ModRand.range(1, 11);
        if(randI >= 8) {
            return generateBigEnd(parent, pos, rot);
        }

        components.add(end);
        return true;
    }

    public boolean generateBigEnd(GaelonSanctuaryTemplate parent, BlockPos pos, Rotation rot) {
        String[] big_end = {"tiles/b_end_1","tiles/b_end_2","tiles/b_end_3"};
        GaelonSanctuaryTemplate end = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(big_end), rot);

        if(end.isCollidingExcParent(manager, parent, components)) {
            return generateQuickEnd(parent, pos, rot);
        }
        components.add(end);
        return true;
    }

    public boolean generateQuickEnd(GaelonSanctuaryTemplate parent, BlockPos pos, Rotation rot) {
        String[] end_types = {"q_end_1","q_end_2"};
        GaelonSanctuaryTemplate end = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(end_types), rot);
        components.add(end);
        return true;
    }

    public boolean buildBossArena(GaelonSanctuaryTemplate parent, BlockPos pos, Rotation rot) {
        GaelonSanctuaryTemplate arena_1 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0, 0, -8), "arena_1", rot);
        GaelonSanctuaryTemplate arena_3 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(18,0,-8), "arena_3", rot);
        GaelonSanctuaryTemplate arena_2 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0,0,9), "arena_2", rot);
        GaelonSanctuaryTemplate arena_4 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(18,0,9), "arena_4", rot);


        generatedBossArena = true;
        components.add(arena_1);
        components.add(arena_2);
        components.add(arena_3);
        components.add(arena_4);
        return true;
    }

    protected int chamberTubeVarHeight = WorldConfig.gaelon_sanctuary_y_height + 25;

    public boolean placeModifyTube(GaelonSanctuaryTemplate parent, BlockPos pos, Rotation rot) {
        GaelonSanctuaryTemplate tube = addAdjustedPieceWithoutDistance(parent, pos, "stairs", rot);
        components.add(tube);
        int yDifference = getGroundFromAbove(world, posIdentified.getX(), posIdentified.getZ());

        if(yDifference - 4 >= chamberTubeVarHeight) {
            chamberTubeVarHeight += 4;
            placeModifyTube(tube, BlockPos.ORIGIN.add(-7, 4, 0), rot);
        } else {
            //Place top of structure
            constructTop(tube, BlockPos.ORIGIN.add(-9, 4, 0), rot);
        }
        return true;
    }

    public boolean constructTop(GaelonSanctuaryTemplate parent, BlockPos pos, Rotation rot) {
        GaelonSanctuaryTemplate top = addAdjustedPieceWithoutDistance(parent, pos, "top", rot);
        components.add(top);
        return true;
    }


    public static int getGroundFromAbove(World world, int x, int z)
    {
        int y = WorldConfig.gaelon_sanctuary_max_y_height;
        boolean foundGround = false;
        while(!foundGround && y-- >= WorldConfig.gaelon_sanctuary_y_height + 25)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt != Blocks.AIR && blockAt != Blocks.LEAVES && blockAt != Blocks.LEAVES2;
        }

        return y;
    }


    private GaelonSanctuaryTemplate addAdjustedPieceWithoutDistance(GaelonSanctuaryTemplate parent, BlockPos pos, String type, Rotation rot) {
        GaelonSanctuaryTemplate newTemplate = new GaelonSanctuaryTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }

    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private GaelonSanctuaryTemplate addAdjustedPiece(GaelonSanctuaryTemplate parent, BlockPos pos, String type, Rotation rot) {
        GaelonSanctuaryTemplate newTemplate = new GaelonSanctuaryTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(GaelonSanctuaryTemplate parent, GaelonSanctuaryTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }

}
