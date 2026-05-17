package com.dungeon_additions.da.world.dauntless;

import com.dungeon_additions.da.world.ModStructureTemplate;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class DauntlessArenaTemplate extends ModStructureTemplate {

    public DauntlessArenaTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public DauntlessArenaTemplate() {

    }

    @Override
    public String templateLocation() {
        return "dark_ruins";
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        //Dauntless Boss
        if(function.startsWith("dauntless")) {

        }
    }


}
