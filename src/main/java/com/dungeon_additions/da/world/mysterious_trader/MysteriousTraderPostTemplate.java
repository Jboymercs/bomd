package com.dungeon_additions.da.world.mysterious_trader;

import com.dungeon_additions.da.entity.trader.EntityMysteriousTrader;
import com.dungeon_additions.da.world.ModStructureTemplate;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class MysteriousTraderPostTemplate extends ModStructureTemplate {

    public MysteriousTraderPostTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public MysteriousTraderPostTemplate() {

    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        if(function.startsWith("trader")) {
            world.setBlockToAir(pos);
            world.setBlockToAir(pos.up());
            EntityMysteriousTrader trader = new EntityMysteriousTrader(world);
            trader.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            world.spawnEntity(trader);
        }
    }


    @Override
    public String templateLocation() {
        return "trader";
    }
}
