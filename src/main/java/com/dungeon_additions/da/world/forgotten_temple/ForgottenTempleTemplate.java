package com.dungeon_additions.da.world.forgotten_temple;

import com.dungeon_additions.da.world.ModStructureTemplate;
import com.dungeon_additions.da.world.forgotten_temple.parts.WorldGenTempleTop;
import com.dungeon_additions.da.world.nether_arena.WorldGenCenterPiece;
import com.dungeon_additions.da.world.nether_arena.WorldGenChain;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class ForgottenTempleTemplate extends ModStructureTemplate {

    private static final WorldGenTempleTop temple_top = new WorldGenTempleTop("temple_top");
    public ForgottenTempleTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public ForgottenTempleTemplate() {

    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        if(function.startsWith("temple_top")) {
            temple_top.generate(world, rand, pos);
        }
    }
    @Override
    public String templateLocation() {
        return "forgotten_temple";
    }
}
