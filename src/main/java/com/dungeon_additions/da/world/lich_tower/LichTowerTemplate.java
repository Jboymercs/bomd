package com.dungeon_additions.da.world.lich_tower;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.util.EntityBossSpawner;
import com.dungeon_additions.da.world.ModStructureTemplate;
import com.dungeon_additions.da.world.WorldGenStructure;
import com.dungeon_additions.da.world.blossom.WorldGenBlossomTop;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class LichTowerTemplate extends ModStructureTemplate {

    public LichTowerTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public LichTowerTemplate() {

    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        //Loot
        if (function.startsWith("ice_chest")) {

            world.setBlockState(pos, Blocks.ICE.getDefaultState());
        } else if (function.startsWith("chest")) {

        }
    }
    @Override
    public String templateLocation() {
        return "lich_tower";
    }
}
