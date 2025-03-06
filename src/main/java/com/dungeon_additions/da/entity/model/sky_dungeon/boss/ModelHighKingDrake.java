package com.dungeon_additions.da.entity.model.sky_dungeon.boss;

import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.sky_dungeon.EntityMageGargoyle;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelHighKingDrake extends GeoModelExtended<EntityHighKingDrake> {

    public ModelHighKingDrake(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityHighKingDrake animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.high_king_drake.json");
    }

    @Override
    public void setLivingAnimations(EntityHighKingDrake entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("InvisibleHead");
        IBone body = this.getAnimationProcessor().getBone("Ground");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        if(entity != null) {
            //Pitch
            if(entity.isGrounded()) {
                body.setRotationX((float) 0);
            } else {
                body.setRotationX((float) Math.toRadians(((IPitch) entity).getPitch() * 0.35));

            }
        }
    }


    private void wrapAndSetPositionPart(EntityHighKingDrake e, IBone bone, MultiPartEntityPart part) {
        Vec3d lookVec = e.getLookVec();
        Vec3d bonePos = new Vec3d(bone.getPivotX() * 0.0625, bone.getPivotY() * 0.0625, bone.getPivotZ() * 0.0625);

        part.setPosition((e.posX + bonePos.x + lookVec.x), e.posY + bonePos.y - (part.height/2), (e.posZ + bonePos.z + lookVec.z) + (part.width / 2));
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
