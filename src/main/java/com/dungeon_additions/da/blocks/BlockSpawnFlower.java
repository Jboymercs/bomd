package com.dungeon_additions.da.blocks;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockSpawnFlower extends BlockBase {

    public BlockSpawnFlower(String name, Material material, float hardness, float resistance, SoundType soundType) {

        super(name, material, hardness, resistance, soundType);
        this.setBlockUnbreakable();
    }
}
