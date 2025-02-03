package com.dungeon_additions.da.world.high_city;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.frozen_castle.FrozenCastleTemplate;
import com.dungeon_additions.da.world.lich_tower.LichTowerTemplate;
import com.google.common.collect.Lists;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;

public class HighCityDungeon {
    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    protected BlockPos posIdentified;

    private boolean generatedKeyRoom = false;
    private boolean generatedBossRoom = false;

    private int SIZE = WorldConfig.high_court_city_size;

    private static final List<Tuple<Rotation, BlockPos>> DUNGEON_CROSS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(30, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 30)));

    public HighCityDungeon(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    public void startHighCity(BlockPos pos, Rotation rot) {
        HighCityTemplate arena_1 = new HighCityTemplate(manager, "cross_1", pos.add(0, ModRand.range(-4, 5), 0), rot, 0, true);
        components.add(arena_1);
        this.posIdentified = pos;
        HighCityTemplate.resetTemplateCount();
        System.out.println("Generated High Court City at" + pos);

        List<StructureComponent> structures = new ArrayList<>(components);
        int failedHalls = 0;
        for (Tuple<Rotation, BlockPos> tuple : DUNGEON_CROSS) {
            if (!generateStraight(arena_1, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedHalls++;
            }
        }
        if (failedHalls > 3) {
            components.clear();
            components.addAll(structures);
        }
    }

    private boolean generateStraight(HighCityTemplate parent, BlockPos pos, Rotation rot) {
        String[] straight_types = {"straight_1", "straight_2", "straight_3"};
        HighCityTemplate straight_piece = addAdjustedPiece(parent, pos.add(0, ModRand.range(-4, 5), 0), ModRand.choice(straight_types), rot);

        if(straight_piece.getDistance() > SIZE || straight_piece.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        components.add(straight_piece);

        if(!generatedKeyRoom) {
            generateKeyRoom(straight_piece, BlockPos.ORIGIN, rot);
        } else {
            generateCross(straight_piece, BlockPos.ORIGIN, rot);
        }

        generateSidePiece(straight_piece, BlockPos.ORIGIN.add(0, 0, 30), rot);
        generateSidePiece(straight_piece, BlockPos.ORIGIN.add(0,0,-30), rot);
        return true;
    }

    private boolean generateCross(HighCityTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"cross_1", "cross_2", "cross_3", "cross_4", "cross_5"};
        HighCityTemplate cross_piece = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);

        if(cross_piece.getDistance() > SIZE || cross_piece.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        components.add(cross_piece);

        int failedHalls = 0;
        List<StructureComponent> structures = new ArrayList<>(components);
        for (Tuple<Rotation, BlockPos> tuple : DUNGEON_CROSS) {
            if(SIZE > 1 && !generatedBossRoom) {
                if(!generateBossRoom(parent, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                    failedHalls++;
                }
            } else if (!generateStraight(cross_piece, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedHalls++;
            }
        }
        if (failedHalls > 3) {
            components.clear();
            components.addAll(structures);
            if(SIZE > 1 && !generatedBossRoom) {
                //Generate Boss Room
                return generateBossRoom(parent, pos, rot);
            } else if (!generatedKeyRoom){
                return generateKeyRoom(parent, pos, rot);
                //Generate End Piece
                //return generateEndPiece(parent, pos, rot);
            }
        }
        return true;
    }

    protected boolean generateSidePiece(HighCityTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"cross_1", "cross_2", "cross_3", "cross_4", "cross_5", "straight_1", "straight_2", "straight_3"};
        HighCityTemplate pieceFor = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(cross_types), rot);
        if(pieceFor.isCollidingExcParent(manager, parent, components)) {
            return false;
        }
        components.add(pieceFor);
        return true;
    }

    protected boolean generateKeyRoom(HighCityTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"cross_key"};
        HighCityTemplate cross_piece = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(cross_types), rot);

        if(cross_piece.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        components.add(cross_piece);
        generatedKeyRoom = true;
        int failedHalls = 0;
        List<StructureComponent> structures = new ArrayList<>(components);
        for (Tuple<Rotation, BlockPos> tuple : DUNGEON_CROSS) {
            if(SIZE > 1 && !generatedBossRoom) {
                if(!generateBossRoom(parent, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                    failedHalls++;
                }
            } else if (!generateStraight(cross_piece, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedHalls++;
            }
        }
        if (failedHalls > 3) {
            components.clear();
            components.addAll(structures);
            if(SIZE > 1 && !generatedBossRoom) {
                //Generate Boss Room
                return generateBossRoom(parent, pos, rot);
            } else {
                //Generate End Piece
                //return generateEndPiece(parent, pos, rot);
            }
        }

        return true;
    }
    protected boolean generateBossRoom(HighCityTemplate parent, BlockPos pos, Rotation rot) {
        HighCityTemplate arena_1 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0, 0, -8), "arena_1", rot);
        HighCityTemplate arena_2 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(19, 0, -8), "arena_2", rot);
        HighCityTemplate arena_3 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(19,0,9), "arena_3", rot);
        HighCityTemplate arena_4 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0, 0, 9), "arena_4", rot);

        components.add(arena_1);
        components.add(arena_2);
        components.add(arena_3);
        components.add(arena_4);
        generatedBossRoom = true;

        return true;
    }



    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private HighCityTemplate addAdjustedPieceWithoutDistance(HighCityTemplate parent, BlockPos pos, String type, Rotation rot) {
        HighCityTemplate newTemplate = new HighCityTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }

    private HighCityTemplate addAdjustedPiece(HighCityTemplate parent, BlockPos pos, String type, Rotation rot) {
        HighCityTemplate newTemplate = new HighCityTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(HighCityTemplate parent, HighCityTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}
