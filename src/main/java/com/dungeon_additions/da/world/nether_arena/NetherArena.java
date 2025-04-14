package com.dungeon_additions.da.world.nether_arena;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.nether_arena.NetherArenaTemplate;
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

public class NetherArena {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    private final int SIZE = WorldConfig.burning_arena_size;

    private boolean hasGeneratedMiniBossRoom = false;
    protected BlockPos posIdentified;

    private static final List<Tuple<Rotation, BlockPos>> CROSS_POS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(16, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 16)));

    private static final List<Tuple<Rotation, BlockPos>> BIG_CROSS = Lists.newArrayList(new Tuple(Rotation.NONE, new BlockPos(0, 0, 0)),
            new Tuple(Rotation.CLOCKWISE_90, new BlockPos(26, 0, 0)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 26)));

    public NetherArena(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;

    }

    public void startVault(BlockPos pos, Rotation rot) {
        NetherArenaTemplate templateAdjusted = new NetherArenaTemplate(manager, "arena/arena_1", pos, rot, 0, true);
        components.add(templateAdjusted);
        this.posIdentified = pos;
        NetherArenaTemplate.resetTemplateCount();
        System.out.println("Generated Nether Arena At" + pos);
        generateArena(templateAdjusted, pos, rot);

    }

    //Generates the rest of the boss arena
    private boolean generateArena(NetherArenaTemplate parent, BlockPos pos, Rotation rot) {
        NetherArenaTemplate arenaTwo = addAdjustedPieceWithoutCount(parent, BlockPos.ORIGIN.add(0,0,0),"arena/arena_2", rot);
        NetherArenaTemplate arenaThree = addAdjustedPieceWithoutCount(parent, BlockPos.ORIGIN.add(-24,0,23), "arena/arena_3", rot);
        NetherArenaTemplate arenaFour = addAdjustedPieceWithoutCount(parent, BlockPos.ORIGIN.add(0,0,23), "arena/arena_4", rot);
        components.add(arenaTwo);
        components.add(arenaThree);
        components.add(arenaFour);
        generateHold(arenaTwo, BlockPos.ORIGIN.add(0,3,11), rot);
        return true;
    }

    private boolean generateStairs(NetherArenaTemplate parent, BlockPos pos, Rotation rot) {
        String[] stair_types = {"stairs_1", "stairs_2", "stairs_3", "stairs_4"};
        NetherArenaTemplate stairs = addAdjustedPieceWithoutCount(parent, BlockPos.ORIGIN.add(0,-8,0), ModRand.choice(stair_types), rot);

        if(stairs.isCollidingExcParent(manager, parent, components) || stairs.getDistance() > SIZE) {
            return false;
        }
        components.add(stairs);
        generateSecondStairs(stairs, pos, rot);
        return true;
    }

    private boolean generateSecondStairs(NetherArenaTemplate parent, BlockPos pos, Rotation rot) {
        String[] stair_types = {"stairs_1", "stairs_2", "stairs_3", "stairs_4"};
        NetherArenaTemplate stairs = addAdjustedPieceWithoutCount(parent, BlockPos.ORIGIN.add(0,-8,0), ModRand.choice(stair_types), rot);

        if(stairs.isCollidingExcParent(manager, parent, components) || stairs.getDistance() > SIZE) {
            return false;
        }
        components.add(stairs);

        int r = world.rand.nextInt(2);
        boolean genSuccess;

        if(r == 0) {
            if(world.rand.nextInt(2) == 0) {
                genSuccess = generateTurn(stairs, BlockPos.ORIGIN, rot);
            } else {
                genSuccess = generateHall(stairs, BlockPos.ORIGIN, rot);
            }
        }
        else {
            if(!this.hasGeneratedMiniBossRoom && SIZE > WorldConfig.burning_arena_size - 2) {
                genSuccess = generateMiniBossRoom(stairs, BlockPos.ORIGIN, rot);
            } else {
                genSuccess = generateHold(stairs, BlockPos.ORIGIN, rot);
            }
        }

        if(!genSuccess) {
            components.remove(stairs);
            return this.generateEndPiece(parent, pos, rot);
        }

        return true;
    }


    private boolean generateTurn(NetherArenaTemplate parent, BlockPos pos, Rotation rot) {
        String[] l_turn_types = {"left_turn_1", "left_turn_2", "left_turn_3"};
        String[] r_turn_types = {"right_turn_1", "right_turn_2", "right_turn_3"};
        NetherArenaTemplate left_turn = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0,0,-3), ModRand.choice(l_turn_types), rot);
        NetherArenaTemplate right_turn = addAdjustedPiece(parent, BlockPos.ORIGIN.add(0,0,3), ModRand.choice(r_turn_types), rot);

        if(left_turn.isCollidingExcParent(manager, parent, components) || left_turn.getDistance() > SIZE) {
            if(right_turn.isCollidingExcParent(manager, parent, components) || right_turn.getDistance() > SIZE) {
                return false;
            }
            components.add(right_turn);
            if(!generateHall(right_turn, BlockPos.ORIGIN.add(6,0,7), rot.add(Rotation.CLOCKWISE_90))) {
                if(!generateStairs(right_turn, BlockPos.ORIGIN.add(6,0,7), rot.add(Rotation.CLOCKWISE_90))) {
                    //generate End Piece
                    return this.generateEndPiece(right_turn, BlockPos.ORIGIN.add(6, 0, 7), rot.add(Rotation.CLOCKWISE_90));
                }
            }

        } else {
            components.add(left_turn);
            if(!generateHall(left_turn, BlockPos.ORIGIN.add(-3,0,4), rot.add(Rotation.COUNTERCLOCKWISE_90))) {
                if(!generateStairs(left_turn, BlockPos.ORIGIN.add(-3, 0, 4), rot.add(Rotation.COUNTERCLOCKWISE_90))) {
                    //generate End piece
                    return this.generateEndPiece(left_turn, BlockPos.ORIGIN.add(-3, 0, 4), rot.add(Rotation.COUNTERCLOCKWISE_90));
                }
            }
        }

        return true;
    }

    private boolean generateHold(NetherArenaTemplate parent, BlockPos pos, Rotation rot) {
        String[] hold_types = {"cross_1", "cross_2", "cross_2","cross_3","cross_3","cross_4","cross_5"};
        NetherArenaTemplate hold = addAdjustedPiece(parent, pos, ModRand.choice(hold_types), rot);
        if(hold.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        List<StructureComponent> structures = new ArrayList<>(components);
        components.add(hold);
        //generates the lower layer
        generateFloor(hold, pos, rot);

        //generates like a regular cross
        int failedHalls = 0;
        for(Tuple<Rotation, BlockPos> tuple : BIG_CROSS) {
            if(!generateHall(hold, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                if(!generateStairs(hold, tuple.getSecond(), tuple.getFirst())) {
                    failedHalls++;
                }
            }
        }

        if(failedHalls > 3) {
            components.clear();
            components.addAll(structures);
             return this.generateEndPiece(parent, pos, rot);
        }
        return true;
    }

    private boolean generateMiniBossRoom(NetherArenaTemplate parent, BlockPos pos, Rotation rot) {
        NetherArenaTemplate hold = addAdjustedPiece(parent, pos, "mini_boss_room", rot);
        if(hold.isCollidingExcParent(manager, parent, components)) {
            return false;
        }

        List<StructureComponent> structures = new ArrayList<>(components);
        components.add(hold);
        this.hasGeneratedMiniBossRoom = true;
        //generates the lower layer
        generateFloor(hold, pos, rot);

        //generates like a regular cross
        int failedHalls = 0;
        for(Tuple<Rotation, BlockPos> tuple : BIG_CROSS) {
            if(!generateHall(hold, tuple.getSecond(), rot.add(tuple.getFirst()))) {
                if(!generateStairs(hold, tuple.getSecond(), tuple.getFirst())) {
                    failedHalls++;
                }
            }
        }

        if(failedHalls > 3) {
            components.clear();
            components.addAll(structures);
            return this.generateEndPiece(parent, pos, rot);
        }
        return true;
    }



    private boolean generateFloor(NetherArenaTemplate parent, BlockPos pos, Rotation rot) {
    String[] big_rooms = {"hold/floor_1", "hold/floor_2", "hold/floor_3", "hold/floor_4", "hold/floor_5"};
    String[] small_rooms = {"hold/small_floor_1", "hold/small_floor_2"};
    NetherArenaTemplate LRoom = addAdjustedPieceWithoutCount(parent, BlockPos.ORIGIN.add(-27,-17,0), ModRand.choice(big_rooms), rot);
    NetherArenaTemplate SRoom = addAdjustedPieceWithoutCount(parent, BlockPos.ORIGIN.add(-27,-7,0), ModRand.choice(small_rooms), rot);
        int randI = ModRand.range(1, 11);
        if(LRoom.isCollidingExcParent(manager, parent, components) || randI >= 8) {

            if(SRoom.isCollidingExcParent(manager, parent, components)) {
                return false;
            }
            components.add(SRoom);
        } else {
            components.add(LRoom);
        }

        return true;
    }

    private boolean generateEndPiece(NetherArenaTemplate parent, BlockPos pos, Rotation rotation) {
        String[] end_types = {"end_1", "end_2", "end_3"};
        NetherArenaTemplate end = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(end_types), rotation);
        if(end.isCollidingExcParent(manager, parent, components)) {
            return false;
        }
        components.add(end);
        return true;
    }

    private boolean generateHall(NetherArenaTemplate parent, BlockPos pos, Rotation rot) {
        String[] straight_types = {"straight_1", "straight_2", "straight_3", "straight_4"};
        NetherArenaTemplate straight = addAdjustedPieceWithoutCount(parent, pos, ModRand.choice(straight_types), rot);

        if(straight.isCollidingExcParent(manager, parent, components) || straight.getDistance() > SIZE) {

            return false;
        }

        components.add(straight);

        int r = world.rand.nextInt(2);
        int b = world.rand.nextInt(3);
        boolean genSuccess;

        if(r == 0) {
            genSuccess = generateHall(straight, BlockPos.ORIGIN, rot);
        }
        else {
            if(b == 0) {
                if(world.rand.nextInt(2) == 0) {
                    genSuccess = generateTurn(straight, BlockPos.ORIGIN, rot);
                } else {
                    genSuccess = generateStairs(straight, BlockPos.ORIGIN, rot);
                }
            } else {
                if(!this.hasGeneratedMiniBossRoom && SIZE > WorldConfig.burning_arena_size - 2) {
                    genSuccess = generateMiniBossRoom(straight, BlockPos.ORIGIN, rot);

                } else {
                    genSuccess = generateHold(straight, BlockPos.ORIGIN, rot);
                }
            }
        }

        if(!genSuccess) {
                    components.remove(straight);
                    return this.generateEndPiece(parent, pos, rot);
        }

        return true;
    }



    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private NetherArenaTemplate addAdjustedPiece(NetherArenaTemplate parent, BlockPos pos, String type, Rotation rot) {
        NetherArenaTemplate newTemplate = new NetherArenaTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance() + 1, true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Adds a new piece, with the previous template a reference for position and
     * rotation
     */
    private NetherArenaTemplate addAdjustedPieceWithoutCount(NetherArenaTemplate parent, BlockPos pos, String type, Rotation rot) {
        NetherArenaTemplate newTemplate = new NetherArenaTemplate(manager, type, parent.getTemplatePosition(), rot, parent.getDistance(), true);
        BlockPos blockpos = parent.getTemplate().calculateConnectedPos(parent.getPlacementSettings(), pos, newTemplate.getPlacementSettings(), BlockPos.ORIGIN);
        newTemplate.offset(blockpos.getX(), blockpos.getY(), blockpos.getZ());
        adjustAndCenter(parent, newTemplate, rot);
        return newTemplate;
    }


    /**
     * Centers a template to line up on the x, and in the center with z
     */
    private void adjustAndCenter(NetherArenaTemplate parent, NetherArenaTemplate child, Rotation rot) {
        BlockPos adjustedPos = new BlockPos(parent.getTemplate().getSize().getX(), 0, (parent.getTemplate().getSize().getZ() - child.getTemplate().getSize().getZ()) / 2f)
                .rotate(rot);
        child.offset(adjustedPos.getX(), adjustedPos.getY(), adjustedPos.getZ());
    }
}
