package com.dungeon_additions.da.animation.example.exampletwo;

import com.dungeon_additions.da.animation.IAnimatedEntity;
import com.dungeon_additions.da.animation.model.BasicModelEntity;
import com.dungeon_additions.da.animation.model.BasicModelPart;
import com.dungeon_additions.da.animation.model.EZModelAnimator;
import com.dungeon_additions.da.animation.util.EZMath;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelBox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;


//Make sure you have your model files extend BasicModelPart
public class ModelEverator extends BasicModelEntity {

    //you'll want all the ModelRenderer stuff changed to BasicModelPart
    private final BasicModelPart Ground;
    private final BasicModelPart Torso;
    private final BasicModelPart UpperTorso;
    private final BasicModelPart LeftArm;
    private final BasicModelPart RightArm;
    private final BasicModelPart HeadJ;
    private final BasicModelPart LeftLeg;
    private final BasicModelPart RightLeg;

    //This is your animator where most of the functions for animation are used
    private final EZModelAnimator animator;

    public ModelEverator() {
        textureWidth = 64;
        textureHeight = 64;

        Ground = new BasicModelPart(this);
        Ground.setRotationPoint(0.0F, 24.0F, 0.0F);


        Torso = new BasicModelPart(this);
        Torso.setRotationPoint(0.0F, -15.0F, 0.0F);
        Ground.addChild(Torso);
        Torso.cubeList.add(new ModelBox(Torso, 0, 29, -4.0F, -3.0F, -2.0F, 8, 6, 4, 0.0F, false));

        UpperTorso = new BasicModelPart(this);
        UpperTorso.setRotationPoint(0.0F, -3.0F, 0.0F);
        Torso.addChild(UpperTorso);
        UpperTorso.cubeList.add(new ModelBox(UpperTorso, 0, 16, -4.5F, -6.0F, -2.5F, 9, 6, 5, 0.0F, false));

        LeftArm = new BasicModelPart(this);
        LeftArm.setRotationPoint(4.5F, -5.0F, 0.0F);
        UpperTorso.addChild(LeftArm);
        LeftArm.cubeList.add(new ModelBox(LeftArm, 24, 33, 0.0F, -1.0F, -1.5F, 3, 12, 3, 0.0F, false));
        LeftArm.cubeList.add(new ModelBox(LeftArm, 36, 33, 0.0F, -1.5F, -2.0F, 4, 4, 4, 0.0F, false));

        RightArm = new BasicModelPart(this);
        RightArm.setRotationPoint(-4.5F, -5.0F, 0.0F);
        UpperTorso.addChild(RightArm);
        RightArm.cubeList.add(new ModelBox(RightArm, 24, 33, -3.0F, -1.0F, -1.5F, 3, 12, 3, 0.0F, true));

        HeadJ = new BasicModelPart(this);
        HeadJ.setRotationPoint(0.0F, -10.5F, 0.0F);
        UpperTorso.addChild(HeadJ);
        HeadJ.cubeList.add(new ModelBox(HeadJ, 22, 21, -3.0F, -2.5F, -3.0F, 6, 6, 6, 0.0F, false));
        HeadJ.cubeList.add(new ModelBox(HeadJ, 0, 0, -4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F, false));

        LeftLeg = new BasicModelPart(this);
        LeftLeg.setRotationPoint(2.0F, 3.0F, 0.0F);
        Torso.addChild(LeftLeg);
        LeftLeg.cubeList.add(new ModelBox(LeftLeg, 32, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, true));

        RightLeg = new BasicModelPart(this);
        RightLeg.setRotationPoint(-2.0F, 3.0F, 0.0F);
        Torso.addChild(RightLeg);
        RightLeg.cubeList.add(new ModelBox(RightLeg, 32, 0, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));

        //ALWAYS include this in the bottom, first statement sets this as the default pose
        //that way the animator knows what default is and after each animation will go back to it's original pose
        this.updateDefaultPose();

        this.animator = EZModelAnimator.create();
    }

    //Define all BasicModelParts in this function below, helps with animations and as well allows what can and can't be animated, I'd just put everything in there
    @Override
    public Iterable<BasicModelPart> getAllParts() {
        return ImmutableList.of(Ground, Torso, UpperTorso, LeftArm, RightArm, HeadJ, LeftLeg, RightLeg);
    }

    //Render Everything
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        Ground.render(f5);
    }

    //This method is where you'll use the animations mentioned in the Entity class and actually set up animations using the model animator
    @Override
    public void animate(IAnimatedEntity entity) {
        //Always include this in the beginning of this method
        animator.update(entity);

        //
        animator.setAnimation(EntityEverator.ANIMATION_ATTACK_MOB);
        //
        animator.startKeyframe(10);
        animator.rotate(Torso, (float) Math.toRadians(-5), 0, 0);
        animator.rotate(UpperTorso, (float) Math.toRadians(-10), 0, 0);
        animator.rotate(LeftArm, (float) Math.toRadians(-179), 0, (float) Math.toRadians(30));
        animator.rotate(RightArm, (float) Math.toRadians(-179), 0, (float) Math.toRadians(-30));
        animator.endKeyframe();
        //
        animator.setStaticKeyframe(5);
        //
        animator.startKeyframe(4);
        animator.rotate(Torso, (float) Math.toRadians(10), 0, 0);
        animator.rotate(UpperTorso,(float) Math.toRadians(10), 0,0);
        animator.rotate(LeftArm, (float) Math.toRadians(-50), 0, (float) Math.toRadians(20));
        animator.rotate(RightArm, (float) Math.toRadians(-50), 0, (float) Math.toRadians(-20));
        animator.endKeyframe();
        //
        animator.startKeyframe(6);
        animator.rotate(LeftArm, (float) Math.toRadians(40), 0, (float) Math.toRadians(-30));
        animator.rotate(RightArm, (float) Math.toRadians(40), 0, (float) Math.toRadians(30));
        animator.endKeyframe();
        //
        animator.setStaticKeyframe(5);
        animator.resetKeyframe(10);

        animator.setAnimation(EntityEverator.ANIMATION_SWING_HEAD);
        animator.startKeyframe(10);
        animator.rotate(LeftArm, 0, 0, (float) Math.toRadians(-40));
        animator.rotate(RightArm, 0, 0, (float) Math.toRadians(40));
        animator.move(HeadJ, 0, -4, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(5);
        animator.resetKeyframe(5);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        //Scaling to come in the future woooooooooooooooooooooooooooo kill me
        //this.HeadJ.setScaleWithPart(HeadJ, (1F + (float) Math.sin(ageInTicks * 0.5F + 2F)) * 0.1F + 1F, 1F, (1F + (float) Math.sin(ageInTicks * 0.5F)) * 0.1F + 1F);

        float walkSpeed = 0.5F;
        float walkDegree = 1F;
        //Legs Walking
        this.walk(RightLeg, walkSpeed, walkDegree, true, 0F, 0.1F, limbSwing, limbSwingAmount);
       this.walk(LeftLeg, walkSpeed, walkDegree, false, 0F, 0.1F, limbSwing, limbSwingAmount);

        //Body bobbing
        float bodyBob = EZMath.walkValue(limbSwing, limbSwingAmount, walkSpeed * 1.5F, 0.5F, 1F, true);
        this.Torso.rotationPointY += bodyBob;

        //flapping of the head when moving in the opposite direction of the main torso
        this.flap(HeadJ, walkSpeed, walkDegree * -0.1F, false, 3F, 0F, limbSwing, limbSwingAmount);
        this.flap(Torso, walkSpeed, walkDegree * 0.1F, false, 3F, 0F, limbSwing, limbSwingAmount);

        //Arm Movements
        this.walk(RightArm, walkSpeed, walkDegree * 1.3F, false, 1F, 0F, limbSwing, limbSwingAmount);
        this.walk(LeftArm, walkSpeed, walkDegree * 1.3F, false, -1F, 0F, limbSwing, limbSwingAmount);


        //Little bit of random movement with the head
       this.flap(HeadJ, 0.15F, 0.1F, false, 2F, 0F, ageInTicks, 1);
        this.flap(HeadJ, 0.55F, 0.1F, false, 2F, 0F, ageInTicks, 0.5F + 0.5F * (float) Math.sin(ageInTicks * 0.5F));

        //Again this is for Individual components such as heads to look as they please
        this.faceTarget(netHeadYaw, headPitch, 1, HeadJ);

        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {


        super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);

    }


}
