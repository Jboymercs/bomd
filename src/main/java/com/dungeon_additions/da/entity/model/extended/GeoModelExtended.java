package com.dungeon_additions.da.entity.model.extended;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public abstract class GeoModelExtended <E extends EntityLivingBase & IAnimatable & IAnimationTickable> extends AnimatedTickingGeoModel<E> {
    protected final ResourceLocation MODEL_RESLOC;
    protected final ResourceLocation TEXTURE_DEFAULT;
    protected final String ENTITY_REGISTRY_PATH_NAME;

    protected ResourceLocation[] textureVariantCache = null;

    public GeoModelExtended(ResourceLocation model, ResourceLocation textureDefault, final String entityName) {
        super();
        this.MODEL_RESLOC = model;
        this.TEXTURE_DEFAULT = textureDefault;
        this.ENTITY_REGISTRY_PATH_NAME = entityName;
    }

    @Override
    public ResourceLocation getTextureLocation(E entity) {

        // Custom texture end

        return this.TEXTURE_DEFAULT;
    }

    @Override
    public ResourceLocation getModelLocation(E object) {
        return this.MODEL_RESLOC;
    }
}
