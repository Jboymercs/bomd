package com.dungeon_additions.da.blocks.aspect_forge;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class AspectForgeStateMapper extends StateMapperBase {

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation("da:" + (((BlockAspectForge)state.getBlock()).byState(state)),
               "state="+state.getValue(BlockAspectForge.STATE).getName());
    }

}
