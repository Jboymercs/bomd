package com.dungeon_additions.da.world.rot_hold;

import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.blossom.BlossomTemplate;
import com.google.common.collect.Lists;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RottenHold {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    private int SIZE = 5;
    protected BlockPos posIdentified;

    private boolean generatedKeyRoom = false;
    private boolean generatedBossRoom = false;
    private int STAIRCASE_AMOUNT = 0;
    private int STAIRCASE_FINAL_AMOUNT = 3;

    private static final List<Tuple<Rotation, BlockPos>> STRONG_HOLD_CROSS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(30, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -4, 30)));


    private static final List<Tuple<Rotation, BlockPos>> STRONG_HOLD_CROSS_DUNGEON = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(30, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 30)));

    private static final List<Tuple<Rotation, BlockPos>> DUNGEON_CROSS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(14, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 14)));

    private static final List<Tuple<Rotation, BlockPos>> DUNGEON_CROSS_4 = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 3, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(14, 3, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 3, 14)));

    public RottenHold(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    public void startHold(BlockPos pos, Rotation rot) {
        RottenHoldTemplate templateAdjusted = new RottenHoldTemplate(manager, "hold_main", pos.add(0, -7, 0), rot, 0, true);
        components.add(templateAdjusted);
        this.posIdentified = pos;
        RottenHoldTemplate.resetTemplateCount();
        System.out.println("Generated Rotten Hold at" + pos);
        List<StructureComponent> structures = new ArrayList<>(components);
        int failedHalls = 0;
        for(Tuple<Rotation, BlockPos> tuple : STRONG_HOLD_CROSS) {
            if(!generateStrongholdPart(templateAdjusted, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                failedHalls++;
            }
        }

        if(failedHalls > 3) {
            components.clear();
            components.addAll(structures);
            //generate Ends
        }

        generateDungeonStart(templateAdjusted, pos, rot);
    }

    /**
     * Generates parts to add onto the main stronghold
     * @param parent
     * @param pos
     * @param rot
     * @return
     */
    private boolean generateStrongholdPart(RottenHoldTemplate parent, BlockPos pos, Rotation rot) {
        String[] parts = {"part_1", "part_2", "part_3", "part_4", "part_5"};
        RottenHoldTemplate piece_template = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(parts), rot);
        components.add(piece_template);
        return true;
    }

    /**
     * Generates the start of the dungeon part of the Hold
     * @param parent
     * @param pos
     * @param rot
     * @return
     */
    private boolean generateDungeonStart(RottenHoldTemplate parent, BlockPos pos, Rotation rot) {
        RottenHoldTemplate startPiece = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(-31, -17,0), "start", rot);
        components.add(startPiece);
        System.out.println("Generating Dungeon Piece");

        List<StructureComponent> structures = new ArrayList<>(components);
        int failedHalls = 0;
        for(Tuple<Rotation, BlockPos> tuple : STRONG_HOLD_CROSS_DUNGEON) {
                if(!generateStraight(startPiece, tuple.getSecond(), rot.add(tuple.getFirst()))) {
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

    private boolean generateCrossWay(RottenHoldTemplate parent, BlockPos pos, Rotation rot, int identification) {
        String[] cross_types = {"tiles/cross_1", "tiles/cross_2", "tiles/cross_3"};
        RottenHoldTemplate crossPiece;

        if(identification <= 3) {
            crossPiece = addAdjustedPiece(parent, pos, ModRand.choice(cross_types), rot);

            //return false if collision or size is exceeded
            if (crossPiece.getDistance() > SIZE || crossPiece.isCollidingExcParent(manager, parent, components)) {
                return this.generateHelperStraight(parent, pos, rot);
            }
            components.add(crossPiece);
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
                if(SIZE > 2 && !generatedBossRoom) {
                    return generateBossHelperStraight(parent, pos, rot);
                } else {
                    int randID = ModRand.range(1, 4);
                    return generateHelperStraight(parent, pos, rot);
                }
            }

        } else if (identification == 4) {
            crossPiece = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0, -3, 0), "tiles/cross_4", rot);

            //return false if collision or size is exceeded
            if (crossPiece.getDistance() > SIZE || crossPiece.isCollidingExcParent(manager, parent, components)) {
                return this.generateHelperStraight(parent, pos, rot);
            }
            components.add(crossPiece);
            int failedHalls = 0;
            List<StructureComponent> structures = new ArrayList<>(components);
            for (Tuple<Rotation, BlockPos> tuple : DUNGEON_CROSS_4) {
                    if (!generateStraight(crossPiece, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                        failedHalls++;
                    }
            }
            if (failedHalls > 3) {
                components.clear();
                components.addAll(structures);
                if(SIZE > 2 && !generatedBossRoom) {
                    return generateBossHelperStraight(parent, pos, rot);
                } else {
                    int randID = ModRand.range(1, 4);
                    return generateHelperStraight(parent, pos, rot);
                }
            }
        }

        return true;
    }

    private boolean generateStaircase(RottenHoldTemplate parent, BlockPos pos, Rotation rot) {
        String[] stair_types = {"tiles/stairs_1", "tiles/stairs_2"};
        RottenHoldTemplate stairPiece = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0, -8, 0), ModRand.choice(stair_types), rot);

        if(STAIRCASE_AMOUNT > STAIRCASE_FINAL_AMOUNT) {
            //generate an End Piece here
            return false;
        }

        if(stairPiece.isCollidingExcParent(manager, parent, components)) {
            return this.generateHelperStraight(parent, pos, rot);
        }

        components.add(stairPiece);
        boolean genSuccess;
        //try generating other things


        genSuccess = generateStraight(stairPiece, BlockPos.ORIGIN, rot);


        if(!genSuccess) {
            components.remove(stairPiece);
            if(SIZE > 2 && !generatedBossRoom) {
                return generateBossHelperStraight(parent, pos, rot);
            } else {
                int randID = ModRand.range(1, 4);
                return this.generateEndDungeonPiece(parent, pos, rot, randID);
            }
        }

        STAIRCASE_AMOUNT++;
        return true;
    }
    private boolean generateStraight(RottenHoldTemplate parent, BlockPos pos, Rotation rot) {
        String[] straight_types = {"tiles/straight_1","tiles/straight_2","tiles/straight_3", "tiles/straight_4", "tiles/straight_5"};
        RottenHoldTemplate straightPiece = addAdjustedPiece(parent, pos, ModRand.choice(straight_types), rot);

        if(straightPiece.getDistance() > SIZE || straightPiece.isCollidingExcParent(manager, parent, components)) {
            return this.generateHelperStraight(parent, pos, rot);
        }

        components.add(straightPiece);
        boolean genSuccess;
        //try generating other things

        int r = ModRand.range(1, 10);

        if(r > 5) {
            genSuccess = generateStraight(straightPiece, BlockPos.ORIGIN, rot);
        } else {
            int randChoice = ModRand.range(1, 10);
            if(STAIRCASE_AMOUNT <= STAIRCASE_FINAL_AMOUNT && randChoice >= 6) {
                genSuccess = generateStaircase(straightPiece, pos, rot);
            } else {
            int randI = ModRand.range(1, 5);
                genSuccess = generateCrossWay(straightPiece, BlockPos.ORIGIN, rot, randI);
            }
        }

        if(!genSuccess) {
            components.remove(straightPiece);
            //generate End Piece
            if(SIZE > 2 && !generatedBossRoom) {
               return generateMiniBossRoom(parent, pos, rot);
            } else {
                int randID = ModRand.range(1, 4);
             return this.generateEndDungeonPiece(parent, pos, rot, randID);
            }
        }

        return true;
    }

    private boolean generateMiniBossRoom(RottenHoldTemplate parent, BlockPos pos, Rotation rot) {
        RottenHoldTemplate boss_room = addAdjustedPieceWithoutDistance(parent, BlockPos.ORIGIN.add(0, -6, 0), "tiles/boss_room", rot);

        if(boss_room.isCollidingExcParent(manager, parent, components) || generatedBossRoom) {
            return this.generateMediumEnd(parent, pos, rot);
        }

        components.add(boss_room);
        generatedBossRoom = true;
        return true;
    }

    private boolean hasgeneratedChamberLast = false;

    private boolean generateBossHelperStraight(RottenHoldTemplate parent, BlockPos pos, Rotation rot) {
        RottenHoldTemplate piece = addAdjustedPieceWithoutDistance(parent, pos, "tiles/helper_straight", rot);

        if(piece.isCollidingExcParent(manager, parent, components)) {
            return this.generateMediumEnd(parent, pos, rot);
        }
        components.add(piece);
        generateMiniBossRoom(piece, BlockPos.ORIGIN, rot);
        return true;
    }
    private boolean generateHelperStraight(RottenHoldTemplate parent, BlockPos pos, Rotation rot) {
        RottenHoldTemplate piece = addAdjustedPieceWithoutDistance(parent, pos, "tiles/helper_straight", rot);

        if(piece.isCollidingExcParent(manager, parent, components)) {
        return this.generateQuickEnd(parent, pos, rot);
        }

        components.add(piece);
        int randI = ModRand.range(1, 5);
        generateEndDungeonPiece(piece, BlockPos.ORIGIN, rot, randI);
        return true;
    }
    private boolean generateEndDungeonPiece(RottenHoldTemplate parent, BlockPos pos, Rotation rot, int identifier) {
        if(SIZE > 2 && !generatedBossRoom) {
            return this.generateBossHelperStraight(parent, pos, rot);
        }
        if(SIZE > 4 && !generatedKeyRoom) {
            RottenHoldTemplate key_room = addAdjustedPieceWithoutDistance(parent, pos, "tiles/key_end", rot);

            if(key_room.isCollidingExcParent(manager, parent, components)) {
                //if a key room can't fit here than nothing else probably can
                hasgeneratedChamberLast = false;
                return this.generateQuickEnd(parent, pos, rot);
            }
            components.add(key_room);
            generatedKeyRoom = true;
            return true;
        } else {
            //Identifer Stuff for each "chamber"
            RottenHoldTemplate chamberRoom;
            if(identifier == 1) {
                chamberRoom = addAdjustedPieceWithoutDistance(parent, pos.add(0, -4, 0), "tiles/chamber_1", rot);
            } else if (identifier == 2) {
                chamberRoom = addAdjustedPieceWithoutDistance(parent, pos.add(0, -4, 0), "tiles/chamber_2", rot);
            } else if (identifier == 3) {
                chamberRoom = addAdjustedPieceWithoutDistance(parent, pos.add(0, -4, 0), "tiles/chamber_3", rot);
            } else {
                chamberRoom = addAdjustedPieceWithoutDistance(parent, pos.add(0, -10, 0), "tiles/chamber_4", rot);
            }

            if(chamberRoom != null && !hasgeneratedChamberLast) {
                if(chamberRoom.isCollidingExcParent(manager, parent, components)) {
                    return this.generateMediumEnd(parent, pos, rot);
                }

                components.add(chamberRoom);
                hasgeneratedChamberLast = true;
                return true;
            } else {
                hasgeneratedChamberLast = false;
                return this.generateMediumEnd(parent, pos, rot);
            }
        }
    }

    //Medium sized end point if a large one doesn't work here
    private boolean generateMediumEnd(RottenHoldTemplate parent, BlockPos pos, Rotation rot) {
        String[] endTypes = {"tiles/m_end_1", "tiles/m_end_2", "tiles/m_end_3"};
        RottenHoldTemplate part = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(endTypes), rot);

        if(part.isCollidingExcParent(manager, parent, components)) {
            return this.generateQuickEnd(parent, pos, rot);
        } else {
            components.add(part);
        }

        return true;
    }

    //just a quick seperate generate Quick end, for tight spaces where things cannot fit
    private boolean generateQuickEnd(RottenHoldTemplate parent, BlockPos pos, Rotation rot) {
        String[] endTypes = {"tiles/q_end_1", "tiles/q_end_2"};
        RottenHoldTemplate part = addAdjustedPieceWithoutDistance(parent, pos, ModRand.choice(endTypes), rot);
        components.add(part);
        return true;
    }

    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private RottenHoldTemplate addAdjustedPieceWithoutDistance(RottenHoldTemplate parent, BlockPos pos, String type, Rotation rot) {
        RottenHoldTemplate newTemplate = new RottenHoldTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }

    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private RottenHoldTemplate addAdjustedPiece(RottenHoldTemplate parent, BlockPos pos, String type, Rotation rot) {
        RottenHoldTemplate newTemplate = new RottenHoldTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(RottenHoldTemplate parent, RottenHoldTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}
