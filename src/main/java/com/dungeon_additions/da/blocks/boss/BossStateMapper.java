package com.dungeon_additions.da.blocks.boss;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class BossStateMapper extends StateMapperBase {

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation("da:" + (((BlockBossReSummon)state.getBlock()).byState(state)),
                "state="+state.getValue(BlockBossReSummon.STATE).getName());
    }
}
