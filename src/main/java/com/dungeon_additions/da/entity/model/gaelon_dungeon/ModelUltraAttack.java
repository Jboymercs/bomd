package com.dungeon_additions.da.entity.model.gaelon_dungeon;

import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.EntityUltraAttack;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;

public class ModelUltraAttack extends GeoModelExtended<EntityUltraAttack> {

    public ModelUltraAttack(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityUltraAttack animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.ultra_attack.json");
    }
}
