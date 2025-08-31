package com.dungeon_additions.da.items.model;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelObsidianHelmet extends ModelBiped {
	private final ModelRenderer head;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer RotateJoin;
	private final ModelRenderer LowerRotate;

	public ModelObsidianHelmet(float size) {
		super(size, 0, 128,128);
		textureWidth = 128;
		textureHeight = 128;

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 21, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.4F, false));
		head.cubeList.add(new ModelBox(head, 57, 41, -1.5F, -10.0F, -3.0F, 3, 9, 12, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 20, 47, -1.5F, -13.0F, 1.0F, 3, 3, 8, 0.0F, false));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(1.5F, -1.0F, 6.0F);
		head.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.5672F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 51, 0.0F, -10.0F, 0.0F, 0, 11, 5, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(-1.5F, -1.0F, 6.0F);
		head.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, -0.5672F, 0.0F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 42, 47, 0.0F, -10.0F, 0.0F, 0, 11, 5, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, -10.0F, 5.0F);
		head.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.3927F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 37, 0.0F, -4.0F, -5.0F, 0, 4, 10, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(0.0F, -10.0F, 5.0F);
		head.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, -0.3927F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 32, 33, 0.0F, -4.0F, -5.0F, 0, 4, 10, 0.0F, false));

		RotateJoin = new ModelRenderer(this);
		RotateJoin.setRotationPoint(0.0F, -5.0F, -1.0F);
		head.addChild(RotateJoin);
		setRotationAngle(RotateJoin, -0.6109F, 0.0F, 0.0F);
		RotateJoin.cubeList.add(new ModelBox(RotateJoin, 32, 12, -5.0F, -2.0F, -5.0F, 10, 4, 7, 0.35F, false));
		RotateJoin.cubeList.add(new ModelBox(RotateJoin, 66, 0, -5.0F, -7.2F, -5.0F, 10, 5, 7, 0.0F, false));

		LowerRotate = new ModelRenderer(this);
		LowerRotate.setRotationPoint(0.0F, -1.0F, 0.0F);
		head.addChild(LowerRotate);
		setRotationAngle(LowerRotate, 0.2182F, 0.0F, 0.0F);
		LowerRotate.cubeList.add(new ModelBox(LowerRotate, 32, 23, -5.0F, -2.0F, -5.9F, 10, 3, 7, 0.22F, false));
		this.bipedHead.addChild(head);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale) {
		this.setRotationAngles(f, f1, f2, f3, f4, scale, entity);
		GlStateManager.pushMatrix();
		if(this.isChild) {
			GlStateManager.scale(1F, 1F, 1F);
			GlStateManager.translate(0.0F, 14.0F * scale, 0.0F);
			this.bipedHead.render(scale);
			this.bipedHeadwear.render(0F);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 28.0F * scale, 0.0F);
		}
		else
		{
			if (entity.isSneaking())
			{
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			this.bipedHead.render(scale);
			this.bipedHeadwear.render(0F);

		}
		this.bipedBody.render(scale);
		this.bipedRightArm.render(scale);
		this.bipedLeftArm.render(scale);
		this.bipedRightLeg.render(scale);
		this.bipedLeftLeg.render(scale);

		GlStateManager.popMatrix();
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {

		if (entityIn instanceof EntityArmorStand) {
			EntityArmorStand entityarmorstand = (EntityArmorStand) entityIn;
			this.bipedHead.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
			this.bipedHead.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
			this.bipedHead.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
			this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
			this.bipedBody.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
			this.bipedBody.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
			this.bipedBody.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
			this.bipedLeftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
			this.bipedLeftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
			this.bipedLeftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
			this.bipedRightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
			this.bipedRightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
			this.bipedRightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
			this.bipedLeftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
			this.bipedLeftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
			this.bipedLeftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
			this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
			this.bipedRightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
			this.bipedRightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
			this.bipedRightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
			this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
			copyModelAngles(this.bipedHead, this.bipedHeadwear);
		}
		else {
			super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		}
	}
}