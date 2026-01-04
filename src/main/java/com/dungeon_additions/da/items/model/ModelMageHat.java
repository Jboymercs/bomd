package com.dungeon_additions.da.items.model;// Made with Blockbench 5.0.7
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
public class ModelMageHat extends ModelBiped {
	private final ModelRenderer head;
	private final ModelRenderer Hat;
	private final ModelRenderer HatPart;
	private final ModelRenderer HatPart2;
	private final ModelRenderer HatPart3;

	public ModelMageHat(float size) {
		super(size, 0, 128,128);
		textureWidth = 128;
		textureHeight = 128;

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 4, 71, -6.0F, -10.0F, -6.0F, 12, 9, 12, 0.4F, false));

		Hat = new ModelRenderer(this);
		Hat.setRotationPoint(0.0F, -8.0F, 0.0F);
		head.addChild(Hat);
		setRotationAngle(Hat, -0.0873F, 0.0F, 0.0873F);
		Hat.cubeList.add(new ModelBox(Hat, 4, 54, -7.0F, 0.0F, -7.0F, 14, 3, 14, 0.2F, false));

		HatPart = new ModelRenderer(this);
		HatPart.setRotationPoint(0.0F, 0.0F, 0.0F);
		Hat.addChild(HatPart);
		setRotationAngle(HatPart, -0.0873F, 0.0F, 0.0873F);
		HatPart.cubeList.add(new ModelBox(HatPart, 4, 92, -3.5F, -2.0F, -3.5F, 7, 4, 7, 0.2F, false));
		HatPart.cubeList.add(new ModelBox(HatPart, 32, 92, -3.5F, -2.5F, -3.5F, 7, 3, 7, 0.4F, false));

		HatPart2 = new ModelRenderer(this);
		HatPart2.setRotationPoint(0.0F, -3.0F, 0.0F);
		HatPart.addChild(HatPart2);
		setRotationAngle(HatPart2, -0.0873F, 0.0F, 0.0873F);
		HatPart2.cubeList.add(new ModelBox(HatPart2, 52, 71, -3.0F, -2.5F, -3.0F, 6, 4, 6, 0.2F, false));

		HatPart3 = new ModelRenderer(this);
		HatPart3.setRotationPoint(0.0F, -2.0F, 0.0F);
		HatPart2.addChild(HatPart3);
		setRotationAngle(HatPart3, -0.0873F, 0.0F, 0.0F);
		HatPart3.cubeList.add(new ModelBox(HatPart3, 52, 81, -2.0F, -2.0F, -2.0F, 4, 2, 4, 0.2F, false));
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