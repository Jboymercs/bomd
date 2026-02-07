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
public class ModelNovikHelmet extends ModelBiped {
	private final ModelRenderer head;
	private final ModelRenderer Detail_r1;
	private final ModelRenderer Detail_r2;

	public ModelNovikHelmet(float size) {
		super(size, 0, 64,64);
		textureWidth = 64;
		textureHeight = 64;

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 2, 45, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.55F, false));
		head.cubeList.add(new ModelBox(head, 22, 16, 4.0F, -6.0F, -1.0F, 5, 2, 2, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 22, 20, 7.0F, -6.0F, -5.0F, 2, 2, 4, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 22, 26, 7.0F, -12.0F, -5.0F, 2, 6, 2, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 22, 26, -9.0F, -12.0F, -5.0F, 2, 6, 2, 0.0F, true));
		head.cubeList.add(new ModelBox(head, 22, 16, -9.0F, -6.0F, -1.0F, 5, 2, 2, 0.0F, true));
		head.cubeList.add(new ModelBox(head, 22, 20, -9.0F, -6.0F, -5.0F, 2, 2, 4, 0.0F, true));
		head.cubeList.add(new ModelBox(head, 30, 26, 4.0F, -9.0F, 0.0F, 4, 8, 0, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 30, 26, -8.0F, -9.0F, 0.0F, 4, 8, 0, 0.0F, true));

		Detail_r1 = new ModelRenderer(this);
		Detail_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(Detail_r1);
		setRotationAngle(Detail_r1, 0.0F, -0.6109F, 0.0F);
		Detail_r1.cubeList.add(new ModelBox(Detail_r1, 0, 16, -2.0F, -14.0F, -2.0F, 0, 14, 11, 0.0F, true));

		Detail_r2 = new ModelRenderer(this);
		Detail_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(Detail_r2);
		setRotationAngle(Detail_r2, 0.0F, 0.6109F, 0.0F);
		Detail_r2.cubeList.add(new ModelBox(Detail_r2, 0, 16, 2.0F, -14.0F, -2.0F, 0, 14, 11, 0.0F, false));

		this.bipedHead.addChild(head);
		this.bipedHead.addChild(Detail_r1);
		this.bipedHead.addChild(Detail_r2);
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