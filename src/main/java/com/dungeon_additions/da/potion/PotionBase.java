package com.dungeon_additions.da.potion;

import com.dungeon_additions.da.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class PotionBase extends Potion {
    private final ResourceLocation TEXTURE = new ResourceLocation("da:textures/potion/potions.png");
    private static final float TEXTURE_SIZE = 64;
    public static final int ICON_SIZE = 18;
    public static final int ROW_SIZE = 3;


    protected PotionBase(String name, boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
        this.setPotionName("effect." + ModReference.MOD_ID + "." + name);
        setRegistryName(name);

    }

    @Override
    public final boolean hasStatusIcon() {
        return false;
    }

    @Override
    public final PotionBase setIconIndex(int x, int y) {
        super.setIconIndex(x + y * ROW_SIZE, 0);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(PotionEffect effect, net.minecraft.client.gui.Gui gui, int x, int y, float z)
    {
        drawEffect(x + 6, y + 7);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(PotionEffect effect, net.minecraft.client.gui.Gui gui, int x, int y, float z, float alpha)
    {
        drawEffect(x + 3, y + 3);
    }

    private void drawEffect(int x, int y) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        int index = getStatusIconIndex();
        int u = index % ROW_SIZE * ICON_SIZE;
        int v = index / ROW_SIZE * ICON_SIZE;
        int width = ICON_SIZE;
        int height = ICON_SIZE;
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder buf = tes.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buf.pos(x, y + height, 0).tex(u / TEXTURE_SIZE, (v + height) / TEXTURE_SIZE).endVertex();
        buf.pos(x + width, y + height, 0).tex((u + width) / TEXTURE_SIZE, (v + height) / TEXTURE_SIZE).endVertex();
        buf.pos(x + width, y, 0).tex((u + width) / TEXTURE_SIZE, v / TEXTURE_SIZE).endVertex();
        buf.pos(x, y, 0).tex(u / TEXTURE_SIZE, v / TEXTURE_SIZE).endVertex();
        tes.draw();
    }
}
