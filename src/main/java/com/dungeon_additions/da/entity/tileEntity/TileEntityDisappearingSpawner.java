package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.entity.logic.DisappearingSpawnerLogic;
import com.dungeon_additions.da.entity.logic.MobSpawnerLogic;
import com.dungeon_additions.da.init.ModBlocks;
import net.minecraft.util.ITickable;

public class TileEntityDisappearingSpawner extends tileEntityMobSpawner implements ITickable {
    @Override
    protected MobSpawnerLogic getSpawnerLogic() {
        return new DisappearingSpawnerLogic(() -> world, () -> pos, ModBlocks.DISAPPEARING_SPAWNER);
    }
}
