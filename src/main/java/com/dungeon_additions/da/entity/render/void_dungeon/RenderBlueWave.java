package com.dungeon_additions.da.entity.render.void_dungeon;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.model.void_dungeon.ModelBlueWave;
import com.dungeon_additions.da.entity.render.RenderAbstractGeoEntity;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.void_dungeon.EntityBlueWave;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RenderBlueWave extends RenderAbstractGeoEntity<EntityBlueWave> {

    public RenderBlueWave(RenderManager renderManager) {
        super(renderManager, new ModelBlueWave());
    }
}
