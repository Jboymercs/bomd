package com.dungeon_additions.da.world.forgotten_temple;

import com.deeperdepths.common.world.base.ModRand;
import com.deeperdepths.common.world.chambers.TrialChambersTemplate;
import com.dungeon_additions.da.config.WorldConfig;
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

public class ForgottenTemple {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;
    private int SIZE = 5;
    protected BlockPos posIdentified;
    private boolean generatedBossRoom;
    private boolean generatedKeyRoom;
    private boolean generatedMiniBossRoomOne;
    private boolean generatedMiniBossRoomTwo;

    protected int chamberTubeVarHeight = WorldConfig.temple_y_height + 12;

    private static final List<Tuple<Rotation, BlockPos>> SMALL_CROSS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(7, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 7)));

    private static final List<Tuple<Rotation, BlockPos>> BIG_CROSS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(15, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 15)));

    private static final List<Tuple<Rotation, BlockPos>> ENTRY_CROSS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(21, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 21)));
    public ForgottenTemple(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    public void startDungeon(BlockPos pos, Rotation rot) {
        String[] puzzle_types = {"puzzle/puzzle_1","puzzle/puzzle_2","puzzle/puzzle_3","puzzle/puzzle_4","puzzle/puzzle_5","puzzle/puzzle_6"};
        ForgottenTempleTemplate templeAdjusted = new ForgottenTempleTemplate(manager, ModRand.choice(puzzle_types), pos, rot, 0, true);
        components.add(templeAdjusted);
        this.posIdentified = pos.add(15, 0, 15);
        ForgottenTempleTemplate.resetTemplateCount();
        System.out.println("Generating Forgotten Temple at" + pos);
        List<StructureComponent> structures = new ArrayList<>(components);
        placeModifyTube(templeAdjusted, BlockPos.ORIGIN.add(-17, 12, 0), rot);
        constructCryptEntry(templeAdjusted, pos, rot);
    }

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

    private boolean constructCryptEntry(ForgottenTempleTemplate parent, BlockPos pos, Rotation rot) {
        ForgottenTempleTemplate entry_room = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-25, -12, 1), "entry_room_1", rot);
        components.add(entry_room);

        int failedHalls = 0;
        List<StructureComponent> structures = new ArrayList<>(components);

        for (Tuple<Rotation, BlockPos> tuple : ENTRY_CROSS) {
            if (!constructLongHall(entry_room, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedHalls++;
            }
        }

        if (failedHalls > 3) {
            components.clear();
            components.addAll(structures);
            //Generate End Piece
            return generateEndPiece(parent, pos, rot);
        }

        return true;
    }

    private boolean constructLongHall(ForgottenTempleTemplate parent, BlockPos pos, Rotation rot) {
        String[] long_halls = {"extras/long_hall_1","extras/long_hall_2","extras/long_hall_3","extras/long_hall_4"};
        ForgottenTempleTemplate long_hall = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(long_halls), rot);
        if(long_hall.isCollidingExcParent(manager, parent, components)) {
            return generateSealEnd(parent, pos, rot);
        }
        components.add(long_hall);

        constructCrossway(long_hall, BlockPos.ORIGIN, rot);
        return true;
    }



    private boolean constructStraight(ForgottenTempleTemplate parent, BlockPos pos, Rotation rot) {
        String[] small_straight_types = {"small/straight_1","small/straight_2","small/straight_3","small/straight_4","small/straight_5","small/straight_6"};
        String[] big_straight_types = {"big/b_straight_4","big/b_straight_5","big/b_straight_6"};
        String[] modified_big_straight_types = {"big/b_straight_1","big/b_straight_2"};
        ForgottenTempleTemplate hallway;
        int randI = ModRand.range(1, 11);
        boolean flag = false;

        if(randI <= 6) {
            int randB = ModRand.range(1, 10);
            if(randB <= 2) {
                hallway = addAdjustedPiece(parent, pos, ModRand.choice(modified_big_straight_types), rot);
                flag = true;
            } else {
                if(!generatedMiniBossRoomTwo) {
                    hallway = addAdjustedPiece(parent, pos, "big/b_straight_3", rot);
                    generatedMiniBossRoomTwo = true;
                } else {
                    hallway = addAdjustedPiece(parent, pos, ModRand.choice(big_straight_types), rot);
                }
            }

            if(hallway.isCollidingExcParent(manager, parent, components)) {
                //change it too a small piece
                hallway = addAdjustedPiece(parent, pos, ModRand.choice(small_straight_types), rot);
                flag = false;


            }

        } else {
            hallway = addAdjustedPiece(parent, pos, ModRand.choice(small_straight_types), rot);
        }

        //one final check to make sure its within size bounds and can fit in the area
        if(hallway.isCollidingExcParent(manager, parent, components) || hallway.getDistance() > SIZE) {
            //generate end piece
            return generateEndPiece(parent, pos, rot);
        }

        components.add(hallway);

        int r = ModRand.range(1, 10);
        boolean genSuccess;

        if(r > 6) {
            if(flag) {
                genSuccess = constructStraight(hallway, BlockPos.ORIGIN.add(0, 12, 0), rot);
            } else {
                genSuccess = constructStraight(hallway, BlockPos.ORIGIN, rot);
            }
        } else {
            //generate Crossway
            if(flag) {
                genSuccess = constructCrossway(hallway, BlockPos.ORIGIN.add(0, 12, 0), rot);
            } else {
                genSuccess = constructCrossway(hallway, BlockPos.ORIGIN, rot);
            }
        }

        if(!genSuccess) {
            components.remove(hallway);
            // generate End Piece
            return generateEndPiece(parent, pos, rot);
        }

        return true;
    }

    private boolean constructCrossway(ForgottenTempleTemplate parent, BlockPos pos, Rotation rot) {
        String[] small_cross_types = {"small/cross_1","small/cross_2","small/cross_3","small/cross_4","small/cross_5","small/cross_6"};
        String[] big_cross_types = {"big/b_cross_3","big/b_cross_4","big/b_cross_5","big/b_cross_6"};
        ForgottenTempleTemplate crossway;
        int randI = ModRand.range(1, 11);
        boolean flag = false;

        if(randI <= 5) {
            int randB = ModRand.range(1, 10);
            if(randB <= 3) {
                if(!generatedMiniBossRoomOne) {
                    crossway = addAdjustedPiece(parent, pos.add(0, -12, 0), "big/b_cross_2", rot);
                    generatedMiniBossRoomOne = true;
                } else {
                    crossway = addAdjustedPiece(parent, pos.add(0, -12, 0), "big/b_cross_1", rot);
                }
            } else {
                crossway = addAdjustedPiece(parent, pos, ModRand.choice(big_cross_types), rot);
            }
            flag = true;

            if(crossway.isCollidingExcParent(manager, parent, components)) {
                //change it too a small piece
                crossway = addAdjustedPiece(parent, pos, ModRand.choice(small_cross_types), rot);
                flag = false;
            }

        } else {
            crossway = addAdjustedPiece(parent, pos, ModRand.choice(small_cross_types), rot);
        }

        //one final check to make sure its within size bounds and can fit in the area
        if(crossway.isCollidingExcParent(manager, parent, components) || crossway.getDistance() > SIZE) {
            //generate end piece
            return generateEndPiece(parent, pos, rot);
        }

        components.add(crossway);
        int failedHalls = 0;
        List<StructureComponent> structures = new ArrayList<>(components);
        if(flag) {
            for (Tuple<Rotation, BlockPos> tuple : BIG_CROSS) {
                if (!constructStraight(crossway, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                    failedHalls++;
                }
            }
        } else {
            for (Tuple<Rotation, BlockPos> tuple : SMALL_CROSS) {
                if (!constructStraight(crossway, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                    failedHalls++;
                }
            }
        }

        if (failedHalls > 3) {
            components.clear();
            components.addAll(structures);
                //Generate End Piece
                return generateEndPiece(parent, pos, rot);
        }

        return true;
    }

    private boolean generateEndPiece(ForgottenTempleTemplate parent, BlockPos pos, Rotation rot) {
        String[] big_end_pieces = {"end/b_end_1","end/b_end_2","end/b_end_3"};
        String[] small_end_pieces = {"end/end_1","end/end_2","end/end_3","end/end_4","end/end_5","end/end_6","end/end_7"};

        if(SIZE > 1 && !generatedKeyRoom) {
            return constructKeyRoom(parent, pos, rot);
        }

        int randI = ModRand.range(1, 10);
        ForgottenTempleTemplate end_piece;

        if(randI <= 2) {
            end_piece = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(big_end_pieces), rot);
        } else if (randI >= 8) {
            return generateSealEnd(parent, pos, rot);
        } else {
            end_piece = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(small_end_pieces), rot);
        }

        if(end_piece.isCollidingExcParent(manager, parent, components)) {
            return generateSealEnd(parent, pos, rot);
        }

        components.add(end_piece);
        return true;
    }

    private boolean constructKeyRoom(ForgottenTempleTemplate parent, BlockPos pos, Rotation rot) {
        ForgottenTempleTemplate key_room = addAdjustedPieceWithoutDistance(parent, pos, "key_room", rot);

        if(key_room.isCollidingExcParent(manager, parent, components)) {
            return false;
        }
        components.add(key_room);
        generatedKeyRoom = true;
        return true;
    }


    private boolean generateSealEnd(ForgottenTempleTemplate parent, BlockPos pos, Rotation rot) {
        ForgottenTempleTemplate end_piece = addAdjustedPieceWithoutDistance(parent, pos, "end_final", rot);
        if(end_piece.isCollidingExcParent(manager, parent, components)) {
            return false;
        }
        components.add(end_piece);
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
