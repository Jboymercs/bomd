package com.dungeon_additions.da.items.model;// Made with Blockbench 5.1.4
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
public class ModelNightfallHelmet extends ModelBiped {
	private final ModelRenderer head;
	private final ModelRenderer cube_r1;
	private final ModelRenderer Top;
	private final ModelRenderer HairL;
	private final ModelRenderer HairTieL;
	private final ModelRenderer HairR;
	private final ModelRenderer HairTieR;

	public ModelNightfallHelmet(float size) {
		super(size, 0, 64,64);
		textureWidth = 64;
		textureHeight = 64;

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 0.0F, 0.0F);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 3.0F);
		setRotationAngle(cube_r1, 0.1309F, 0.0F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 16, -5.0F, -3.0F, -7.5F, 10, 3, 10, 0.4F, false));

		Top = new ModelRenderer(this);
		Top.setRotationPoint(0.0F, -3.0F, 2.0F);
		setRotationAngle(Top, -0.3491F, 0.0F, 0.0F);
		Top.cubeList.add(new ModelBox(Top, 0, 48, -5.0F, -6.0F, -7.0F, 10, 6, 10, 0.45F, false));

		HairL = new ModelRenderer(this);
		Top.addChild(HairL);
		HairL.setRotationPoint(5.0F, -6.0F, 0.0F);
		setRotationAngle(HairL, 0.0F, -0.3054F, -0.3491F);
		HairL.cubeList.add(new ModelBox(HairL, 0, 29, -1.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F, false));

		HairTieL = new ModelRenderer(this);
		HairTieL.setRotationPoint(0.0F, 0.0F, 0.0F);
		HairL.addChild(HairTieL);
		setRotationAngle(HairTieL, 0.3927F, 0.0F, 0.1745F);
		HairTieL.cubeList.add(new ModelBox(HairTieL, 32, 29, 0.0F, 7.0F, -1.0F, 2, 5, 2, 0.0F, false));
		HairTieL.cubeList.add(new ModelBox(HairTieL, 4, 33, 0.5F, 0.0F, -0.5F, 1, 7, 1, 0.0F, false));

		HairR = new ModelRenderer(this);
		Top.addChild(HairR);
		HairR.setRotationPoint(-5.0F, -6.0F, 0.0F);
		setRotationAngle(HairR, 0.0F, 0.3054F, 0.3491F);
		HairR.cubeList.add(new ModelBox(HairR, 0, 29, -3.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F, true));

		HairTieR = new ModelRenderer(this);
		HairTieR.setRotationPoint(-1.0F, 1.0F, 0.0F);
		HairR.addChild(HairTieR);
		setRotationAngle(HairTieR, 0.3927F, 0.0F, -0.1745F);
		HairTieR.cubeList.add(new ModelBox(HairTieR, 24, 29, -1.0F, 6.0F, -1.0F, 2, 5, 2, 0.0F, false));
		HairTieR.cubeList.add(new ModelBox(HairTieR, 0, 33, -0.5F, -1.0F, -0.5F, 1, 7, 1, 0.0F, false));

		this.bipedHead.addChild(head);
		this.bipedHead.addChild(cube_r1);
		this.bipedHead.addChild(Top);
		//this.bipedHead.addChild(HairL);
		//this.bipedHead.addChild(HairTieL);
		//this.bipedHead.addChild(HairR);
		//this.bipedHead.addChild(HairTieR);
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