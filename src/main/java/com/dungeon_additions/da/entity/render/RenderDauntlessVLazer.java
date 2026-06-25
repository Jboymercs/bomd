package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDelayedLazer;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.ProjectileVerticalLazer;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.RenderUtil;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderDauntlessVLazer extends Render<ProjectileVerticalLazer> {
    public static final ResourceLocation DAUNTLESS_BEAM = new ResourceLocation(ModReference.MOD_ID, "textures/entity/beam/d_beam.png");

    public RenderDauntlessVLazer(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(ProjectileVerticalLazer entity, double x, double y, double z, float entityYaw, float partialTicks) {
        renderManager.renderEngine.bindTexture(DAUNTLESS_BEAM);
        if (entity.getRenderDirection() != null) {
            RenderUtil.drawBeam(renderManager, entity.getPositionVector(), entity.getRenderDirection(), new Vec3d(x, y, z), ModColors.LIGHTER_PURPLE, entity, partialTicks, new Vec3d(2, 3, 2));
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }


    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(ProjectileVerticalLazer projectileVerticalLazer) {
        return null;
    }
}
