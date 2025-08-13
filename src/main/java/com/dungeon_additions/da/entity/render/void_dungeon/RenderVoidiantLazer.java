package com.dungeon_additions.da.entity.render.void_dungeon;


import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiantLazer;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.RenderUtil;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVoidiantLazer extends Render<EntityVoidiantLazer> {

    public RenderVoidiantLazer(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void doRender(EntityVoidiantLazer entity, double x, double y, double z, float entityYaw, float partialTicks) {
        renderManager.renderEngine.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
        if (entity.getRenderDirection() != null) {
            double scale = (EntityVoidiantLazer.TICK_LIFE / (entity.ticksExisted + partialTicks)) / EntityVoidiantLazer.TICK_LIFE;
            RenderUtil.drawBeam(renderManager, entity.getPositionVector(), entity.getRenderDirection(), new Vec3d(x, y, z), ModColors.WHITE, entity, partialTicks, new Vec3d(scale, 1, scale));
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityVoidiantLazer entity) {
        return null;
    }
}
