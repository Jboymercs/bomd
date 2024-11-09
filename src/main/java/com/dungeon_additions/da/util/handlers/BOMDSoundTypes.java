package com.dungeon_additions.da.util.handlers;

import net.minecraft.block.SoundType;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class BOMDSoundTypes {

    public static final SoundType MOSS = new SoundType(1, 1, SoundsHandler.MOSS_BREAK, SoundsHandler.MOSS_STEP,
            SoundsHandler.MOSS_PLACE, SoundsHandler.MOSS_HIT, SoundsHandler.MOSS_HIT);

    public static final SoundType AZA_LEAVES = new SoundType(1, 1, SoundsHandler.AZA_LEAVES_BREAK, SoundsHandler.AZA_LEAVES_STEP,
            SoundsHandler.AZA_LEAVES_PLACE, SoundsHandler.AZA_LEAVES_HIT, SoundsHandler.AZA_LEAVES_HIT);
}
