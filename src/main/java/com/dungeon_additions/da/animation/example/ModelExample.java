package com.dungeon_additions.da.animation.example;

import com.dungeon_additions.da.animation.IAnimatedEntity;
import com.dungeon_additions.da.animation.model.BasicModelEntity;
import com.dungeon_additions.da.animation.model.BasicModelPart;
import com.dungeon_additions.da.animation.model.EZModelAnimator;
import com.dungeon_additions.da.animation.util.EZMath;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class ModelExample extends BasicModelEntity {
    private final BasicModelPart Ground;
    private final BasicModelPart Torso;
    private final BasicModelPart HeadJ;
    private final BasicModelPart LArmJ;
    private final BasicModelPart RArmJ;
    private final BasicModelPart LeftLegJ;
    private final BasicModelPart RightLegJ;
    private final EZModelAnimator animator;

    public ModelExample() {
        textureWidth = 64;
        textureHeight = 64;

        Ground = new BasicModelPart(this);
        Ground.setRotationPoint(0.0F, 24.0F, 0.0F);


        Torso = new BasicModelPart(this);
        Torso.setRotationPoint(0.0F, -16.0F, 0.0F);
        Ground.addChild(Torso);
        Torso.cubeList.add(new ModelBox(Torso, 32, 0, -4.0F, -8.0F, -2.0F, 8, 8, 4, 0.0F, false));

        HeadJ = new BasicModelPart(this);
        HeadJ.setRotationPoint(0.0F, -8.0F, 0.0F);
        Torso.addChild(HeadJ);
        HeadJ.cubeList.add(new ModelBox(HeadJ, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));

        LArmJ = new BasicModelPart(this);
        LArmJ.setRotationPoint(4.0F, -7.0F, 0.0F);
        Torso.addChild(LArmJ);
        LArmJ.cubeList.add(new ModelBox(LArmJ, 32, 28, 0.0F, -1.0F, -1.5F, 3, 13, 3, 0.0F, false));

        RArmJ = new BasicModelPart(this);
        RArmJ.setRotationPoint(-4.0F, -7.0F, 0.0F);
        Torso.addChild(RArmJ);
        RArmJ.cubeList.add(new ModelBox(RArmJ, 32, 12, -3.0F, -1.0F, -1.5F, 3, 13, 3, 0.0F, false));

        LeftLegJ = new BasicModelPart(this);
        LeftLegJ.setRotationPoint(2.0F, 0.0F, 0.0F);
        Torso.addChild(LeftLegJ);
        LeftLegJ.cubeList.add(new ModelBox(LeftLegJ, 16, 16, -2.0F, 0.0F, -2.0F, 4, 16, 4, 0.0F, false));

        RightLegJ = new BasicModelPart(this);
        RightLegJ.setRotationPoint(-2.0F, 0.0F, 0.0F);
        Torso.addChild(RightLegJ);
        RightLegJ.cubeList.add(new ModelBox(RightLegJ, 0, 16, -2.0F, 0.0F, -2.0F, 4, 16, 4, 0.0F, false));
        //import all basic parts of the Entity and update this current thing to be the default pose
        this.updateDefaultPose();
        this.animator = EZModelAnimator.create();
    }

    @Override
    public Iterable<BasicModelPart> getAllParts() {
        return ImmutableList.of(Ground, Torso, HeadJ, LArmJ, RArmJ, LeftLegJ, RightLegJ);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        Ground.render(f5);
    }



    //this is where you set up specific animations
    @Override
    public void animate(IAnimatedEntity entity) {
        animator.update(entity);

        animator.setAnimation(EntityExample.ANIMATION_WAVE);

        System.out.println("Playing Animation in Model Class");

        //starts a key frame and how long it will last for the below code to play
        animator.startKeyframe(5);
        animator.rotate(RArmJ, (float) Math.toRadians(-120), (float) Math.toRadians(40), 0);
        animator.rotate(LArmJ, (float) Math.toRadians(-120), (float) Math.toRadians(-40), 0);
        animator.move(Ground, 0, -4, 0);
        animator.rotate(LeftLegJ, (float) Math.toRadians(-40), (float) Math.toRadians(-20), 0);
        animator.rotate(RightLegJ, (float) Math.toRadians(-40), (float) Math.toRadians(20), 0);
        //ends on what needs to move during that keyframe
        animator.endKeyframe();
        //duration for the static of the current pose
        animator.setStaticKeyframe(5);
        //you can start more keyframes as you wish
        animator.startKeyframe(5);
        animator.rotate(HeadJ, (float) Math.toRadians(30), 0, 0);
        animator.endKeyframe();

        animator.setStaticKeyframe(5);
        animator.startKeyframe(5);
        animator.rotate(HeadJ, (float) Math.toRadians(-20), 0, 0);
        animator.endKeyframe();
        //end of the animation, this basically resets the model from the most recent pose to the default pose in the given duration
        animator.resetKeyframe(5);

    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {

            float walkSpeed = 0.5F;
            float walkDegree = 1F;
            //Walking Cycle, animate whatever boxes you want
            this.walk(RightLegJ, walkSpeed, walkDegree * 0.5F, false, 1, -0.2F, limbSwing, limbSwingAmount);
            this.walk(LeftLegJ, walkSpeed, walkDegree * 0.5F, true, 1, 0.2F, limbSwing, limbSwingAmount);
            LeftLegJ.rotationPointY += Math.min(0, EZMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 2, true));
            RightLegJ.rotationPointY += Math.min(0, EZMath.walkValue(limbSwing, limbSwingAmount, walkSpeed, -0.5F, 2, false));
            //Individual components that look around such as the head
            this.faceTarget(netHeadYaw, headPitch, 1, HeadJ);

        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {

        super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);

    }
}
