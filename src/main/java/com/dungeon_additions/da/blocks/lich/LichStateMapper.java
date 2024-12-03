package com.dungeon_additions.da.blocks.lich;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class LichStateMapper extends StateMapperBase {

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation("da:" + (((BlockSoulStar)state.getBlock()).byState(state)),
                "state="+state.getValue(BlockSoulStar.STATE).getName());
    }
}
