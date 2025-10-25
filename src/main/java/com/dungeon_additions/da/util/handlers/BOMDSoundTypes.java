package com.dungeon_additions.da.util.handlers;

import net.minecraft.block.SoundType;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class BOMDSoundTypes {

    public static final SoundType MOSS = new SoundType(1, 1, SoundsHandler.MOSS_BREAK, SoundsHandler.MOSS_STEP,
            SoundsHandler.MOSS_PLACE, SoundsHandler.MOSS_HIT, SoundsHandler.MOSS_HIT);

    public static final SoundType AZA_LEAVES = new SoundType(1, 1, SoundsHandler.AZA_LEAVES_BREAK, SoundsHandler.AZA_LEAVES_STEP,
            SoundsHandler.AZA_LEAVES_PLACE, SoundsHandler.AZA_LEAVES_HIT, SoundsHandler.AZA_LEAVES_HIT);

    public static final SoundType PETRO_GLOOM = new SoundType(0.75F, 1, SoundsHandler.GLOOM_BREAK, SoundsHandler.GLOOM_STEP,
            SoundsHandler.GLOOM_PLACE, SoundsHandler.GLOOM_BREAK, SoundsHandler.GLOOM_BREAK);

    public static final SoundType GRUM_STONE = new SoundType(0.75F, 1, SoundsHandler.GRUM_BREAK, SoundsHandler.GRUM_STEP,
            SoundsHandler.GRUM_PLACE, SoundsHandler.GRUM_BREAK, SoundsHandler.GRUM_BREAK);

    public static final SoundType GAELON_CRYSTAL = new SoundType(0.75F, 1.35F, SoundsHandler.GAELON_BREAK, SoundsHandler.GAELON_STEP,
            SoundsHandler.GAELON_PLACE, SoundsHandler.GAELON_BREAK, SoundsHandler.GAELON_BREAK);

    public static final SoundType METAL_BLOCK = new SoundType(0.75F, 1, SoundsHandler.STORMVIER_BLOCK_BREAK, SoundsHandler.STORMVIER_BLOCK_STEP,
            SoundsHandler.STORMVIER_BLOCK_STEP, SoundsHandler.STORMVIER_BLOCK_BREAK, SoundsHandler.STORMVIER_BLOCK_BREAK);
}
