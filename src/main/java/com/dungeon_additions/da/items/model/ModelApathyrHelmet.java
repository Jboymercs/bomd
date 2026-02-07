package com.dungeon_additions.da.items.model;// Made with Blockbench 5.0.2
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
public class ModelApathyrHelmet extends ModelBiped {
	private final ModelRenderer head;

	public ModelApathyrHelmet(float size) {
		super(size, 0, 64,64);
		textureWidth = 64;
		textureHeight = 64;

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 27, 44, -4.5F, -9.0F, -4.5F, 9, 10, 9, 0.25F, false));
		head.cubeList.add(new ModelBox(head, 0, 19, -2.0F, -10.0F, -5.0F, 4, 4, 9, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 32, 3.0F, -7.0F, -1.5F, 7, 3, 3, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 26, 19, 7.0F, -7.0F, -7.5F, 3, 3, 6, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 36, 0, 7.0F, -14.0F, -7.5F, 3, 7, 3, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 26, 28, 7.0F, -14.0F, -4.5F, 2, 2, 7, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 32, -10.0F, -7.0F, -1.5F, 7, 3, 3, 0.0F, true));
		head.cubeList.add(new ModelBox(head, 26, 19, -10.0F, -7.0F, -7.5F, 3, 3, 6, 0.0F, true));
		head.cubeList.add(new ModelBox(head, 36, 0, -10.0F, -14.0F, -7.5F, 3, 7, 3, 0.0F, true));
		head.cubeList.add(new ModelBox(head, 26, 28, -9.0F, -14.0F, -4.5F, 2, 2, 7, 0.0F, true));
		head.cubeList.add(new ModelBox(head, 36, 10, -1.5F, -19.0F, -5.0F, 3, 9, 0, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 20, 32, 0.0F, -19.0F, -6.5F, 0, 11, 3, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 26, 37, 2.0F, -14.0F, -5.0F, 4, 7, 0, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 26, 37, -6.0F, -14.0F, -5.0F, 4, 7, 0, 0.0F, true));
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