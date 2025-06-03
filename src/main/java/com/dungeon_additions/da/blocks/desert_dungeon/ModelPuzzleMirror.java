package com.dungeon_additions.da.blocks.desert_dungeon;// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPuzzleMirror extends ModelBase {
	private final ModelRenderer Main;

	public ModelPuzzleMirror() {
		textureWidth = 64;
		textureHeight = 64;

		Main = new ModelRenderer(this);
		Main.setRotationPoint(0.0F, -4F, 0.0F);
		Main.cubeList.add(new ModelBox(Main, 0, 0, -3.0F, 6.0F, -3.0F, 6, 2, 6, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 24, 0, -1.0F, 5.0F, -1.0F, 2, 1, 2, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 0, 8, -6.0F, 3.0F, -1.0F, 12, 2, 2, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 18, 16, 4.0F, -5.0F, -1.0F, 2, 8, 2, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 0, 25, -6.0F, -5.0F, -1.0F, 2, 8, 2, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 0, 12, -6.0F, -7.0F, -1.0F, 12, 2, 2, 0.0F, false));
		Main.cubeList.add(new ModelBox(Main, 0, 16, -4.0F, -5.0F, -0.5F, 8, 8, 1, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.Main.rotateAngleY = netHeadYaw * 0.017453292F;
		this.Main.rotateAngleX = headPitch * 0.017453292F;
		Main.render(scale);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}