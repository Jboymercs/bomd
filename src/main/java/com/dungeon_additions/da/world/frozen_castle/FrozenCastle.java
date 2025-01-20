package com.dungeon_additions.da.world.frozen_castle;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.rot_hold.RottenHoldTemplate;
import com.google.common.collect.Lists;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.List;

public class FrozenCastle {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    private int SIZE = WorldConfig.frozen_castle_size;
    protected BlockPos posIdentified;

    private boolean generatedKeyRoom = false;
    private boolean generatedBossRoom = false;

    private static final List<Tuple<Rotation, BlockPos>> DUNGEON_CROSS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(24, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 24)));

    public FrozenCastle(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    public void startCastle(BlockPos pos, Rotation rot) {
        FrozenCastleTemplate templateAdjusted = new FrozenCastleTemplate(manager, "tiles/cross_1", pos, rot, 0, true);
        components.add(templateAdjusted);
        this.posIdentified = pos;
        FrozenCastleTemplate.resetTemplateCount();
        System.out.println("Generated Castle at" + pos);
        List<StructureComponent> structures = new ArrayList<>(components);
        int failedHalls = 0;
        generateBaseTower(templateAdjusted, pos, rot);
        for (Tuple<Rotation, BlockPos> tuple : DUNGEON_CROSS) {
            if (!generateStraight(templateAdjusted, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedHalls++;
            }
        }
        if (failedHalls > 3) {
            components.clear();
            components.addAll(structures);
        }
    }


    private boolean generateStraight(FrozenCastleTemplate parent, BlockPos pos, Rotation rot) {
        String[] straight_types = {"tiles/straight_1","tiles/straight_2","tiles/straight_3", "tiles/straight_4", "tiles/straight_5", "tiles/straight_6"};
        FrozenCastleTemplate straightPiece = addAdjustedPiece(parent, pos, ModRand.choice(straight_types), rot);

        if(straightPiece.getDistance() > SIZE || straightPiece.isCollidingExcParent(manager, parent, components)) {
                return generateRoom(parent, pos, rot);
        }

        components.add(straightPiece);
        boolean genSuccess;
        //try generating other things

        int r = ModRand.range(1, 10);

        if(r > 6) {
            genSuccess = generateStraight(straightPiece, BlockPos.ORIGIN, rot);
        } else {
            if(!generatedKeyRoom) {
                genSuccess = generateCrossWayKeyRoom(straightPiece, BlockPos.ORIGIN, rot);
            } else {
                    genSuccess = generateCrossWay(straightPiece, BlockPos.ORIGIN, rot);
            }
        }

        if(!genSuccess) {
            components.remove(straightPiece);
            //generate End Piece
            if(SIZE > 1 && !generatedBossRoom) {
                //Generate BossRoom
                return generateBossRoom(parent, pos, rot);
            } else {
                return this.generateEndPiece(parent, pos, rot);
            }


        }

        return true;
    }


    private boolean generateCrossWay(FrozenCastleTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"tiles/cross_1", "tiles/cross_2", "tiles/cross_3"};
        FrozenCastleTemplate crossPiece = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);

        if (crossPiece.getDistance() > SIZE || crossPiece.isCollidingExcParent(manager, parent, components)) {
                return generateRoom(parent, pos, rot);
        }

        components.add(crossPiece);
        generateBaseTower(crossPiece, pos, rot);
        int failedHalls = 0;
        List<StructureComponent> structures = new ArrayList<>(components);
        for (Tuple<Rotation, BlockPos> tuple : DUNGEON_CROSS) {
            if (!generateStraight(crossPiece, tuple.getSecond(), rot.add(tuple.getFirst()))) {
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
                return generateEndPiece(parent, pos, rot);
            }
        }

        return true;
    }

    private boolean generateEndPiece(FrozenCastleTemplate parent, BlockPos pos, Rotation rot) {
        String[] cross_types = {"tiles/end_1", "tiles/end_2", "tiles/end_3"};
        FrozenCastleTemplate endPiece = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(cross_types), rot);

        if(endPiece.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        components.add(endPiece);

        return true;
    }

    /**
     * specifies spawning the tower with the key in it
     * @param parent
     * @param pos
     * @param rot
     * @return
     */
    private boolean generateCrossWayKeyRoom(FrozenCastleTemplate parent, BlockPos pos, Rotation rot) {
        FrozenCastleTemplate crossPiece = addAdjustedPiece(parent, pos, "tiles/cross_4", rot);

        if (crossPiece.isCollidingExcParent(manager, parent, components)) {

            return generateRoom(parent, pos, rot);
        }

        components.add(crossPiece);
        generatedKeyRoom = true;

        generateBaseTowerKey(crossPiece, pos, rot);
        int failedHalls = 0;
        List<StructureComponent> structures = new ArrayList<>(components);
        for (Tuple<Rotation, BlockPos> tuple : DUNGEON_CROSS) {
            if (!generateStraight(crossPiece, tuple.getSecond(), rot.add(tuple.getFirst()))) {
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
                 return generateEndPiece(parent, pos, rot);
            }
        }

        return true;
    }

    private boolean generateRoom(FrozenCastleTemplate parent, BlockPos pos, Rotation rot) {
        String[] base_types = {"tiles/room_1", "tiles/room_2","tiles/room_3","tiles/room_4","tiles/room_5"};
        FrozenCastleTemplate crossPiece = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(base_types), rot);

        if(SIZE > 1 && !generatedBossRoom) {
            return generateBossRoom(parent, pos, rot);
        }

        if(crossPiece.isCollidingExcParent(manager, parent, components)) {
            return generateEndPiece(parent, pos, rot);
        }

        components.add(crossPiece);
        return true;
    }

    private boolean generateBaseTower(FrozenCastleTemplate parent, BlockPos pos, Rotation rot) {
        String[] base_types = {"tower/base_1", "tower/base_3"};
        FrozenCastleTemplate basePiece = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-21, 12, 0), ModRand.choice(base_types), rot);

        if(basePiece.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        components.add(basePiece);
        generateMiddleTower(basePiece, pos, rot);
        return true;
    }

    private boolean generateBaseTowerKey(FrozenCastleTemplate parent, BlockPos pos, Rotation rot) {
        FrozenCastleTemplate basePiece = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-21, 12, 0), "set/key_base", rot);

        if(basePiece.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        components.add(basePiece);
        generateMiddleTower(basePiece, pos, rot);
        return true;
    }

    private boolean generateMiddleTower(FrozenCastleTemplate parent, BlockPos pos, Rotation rot) {
        String[] middle_types = {"tower/middle_1", "tower/middle_2", "tower/middle_3"};
        FrozenCastleTemplate middlePiece = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-16, 10, 0), ModRand.choice(middle_types), rot);

        if(middlePiece.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        components.add(middlePiece);
        generateTopTower(middlePiece, pos, rot);
        return true;
    }

    private boolean generateTopTower(FrozenCastleTemplate parent, BlockPos pos, Rotation rot) {
        String[] top_types = {"tower/top_1", "tower/top_2", "tower/top_3"};
        FrozenCastleTemplate topPiece = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-14, 19, 0), ModRand.choice(top_types), rot);

        if(topPiece.isCollidingExcParent(manager, parent, components)) {
                  return false;
        }

        components.add(topPiece);
        return true;
    }

    //Generates the Boss Room of the dungeon
    private boolean generateBossRoom(FrozenCastleTemplate parent, BlockPos pos, Rotation rot) {

        FrozenCastleTemplate arenaPiece = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0, -1, -10), "set/arena_1", rot);

        FrozenCastleTemplate arenaPiece2 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(21, -1, -10), "set/arena_2", rot);

        FrozenCastleTemplate arenaPiece3 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(21, -1, 11), "set/arena_3", rot);

        FrozenCastleTemplate arenaPiece4 = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0, -1, 11), "set/arena_4", rot);

        if(arenaPiece.isCollidingExcParent(manager, parent, components) || arenaPiece2.isCollidingExcParent(manager, parent, components) ||
        arenaPiece3.isCollidingExcParent(manager, parent, components) || arenaPiece4.isCollidingExcParent( manager, parent, components)) {
            return generateEndPiece(parent, pos, rot);
        }

        components.add(arenaPiece);
        components.add(arenaPiece2);
        components.add(arenaPiece3);
        components.add(arenaPiece4);
        generatedBossRoom = true;

        return true;
    }



    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private FrozenCastleTemplate addAdjustedPieceWithoutDistance(FrozenCastleTemplate parent, BlockPos pos, String type, Rotation rot) {
        FrozenCastleTemplate newTemplate = new FrozenCastleTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }

    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private FrozenCastleTemplate addAdjustedPiece(FrozenCastleTemplate parent, BlockPos pos, String type, Rotation rot) {
        FrozenCastleTemplate newTemplate = new FrozenCastleTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(FrozenCastleTemplate parent, FrozenCastleTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }

}
