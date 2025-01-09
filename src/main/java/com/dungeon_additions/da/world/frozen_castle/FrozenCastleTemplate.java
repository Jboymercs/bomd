package com.dungeon_additions.da.world.frozen_castle;

import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.ModStructureTemplate;
import com.dungeon_additions.da.world.WorldGenStructure;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class FrozenCastleTemplate extends ModStructureTemplate {


    private WorldGenCenterPiece[] structure_piece = {new WorldGenCenterPiece("piece_1"),new WorldGenCenterPiece("piece_2"),new WorldGenCenterPiece("piece_3"),
            new WorldGenCenterPiece("piece_4"),new WorldGenCenterPiece("piece_5"),new WorldGenCenterPiece("piece_6")};
    public FrozenCastleTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public FrozenCastleTemplate() {

    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {


        //mobs
        if(function.startsWith("mob")) {

        } else if (function.startsWith("big_mob")) {

        }

        //loot
        if(function.startsWith("chest")) {

        } else if (function.startsWith("secret_chest")) {

        } else if (function.startsWith("key_chest")) {

        }

        //function blocks
        if(function.startsWith("core")) {

        } else if (function.startsWith("center_piece")) {
            WorldGenCenterPiece piece = ModRand.choice(structure_piece);
            piece.generate(world, rand, pos.add(-4, 0, -4));
        }
    }
    @Override
    public String templateLocation() {
        return "frozen_castle";
    }
}
