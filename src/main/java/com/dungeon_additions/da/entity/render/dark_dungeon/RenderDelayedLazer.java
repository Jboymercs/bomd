package com.dungeon_additions.da.entity.render.dark_dungeon;


import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDelayedLazer;
import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityDesertBeam;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.RenderUtil;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderDelayedLazer extends Render<EntityDelayedLazer> {

    public static final ResourceLocation DAUNTLESS_BEAM = new ResourceLocation(ModReference.MOD_ID, "textures/entity/beam/d_beam.png");

    public RenderDelayedLazer(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityDelayedLazer entity, double x, double y, double z, float entityYaw, float partialTicks) {
        renderManager.renderEngine.bindTexture(DAUNTLESS_BEAM);
        if (entity.getRenderDirection() != null) {
            double scale = (EntityDelayedLazer.TICK_LIFE + entity.delayedLazer / ((entity.ticksExisted + entity.delayedLazer) + partialTicks)) / EntityDelayedLazer.TICK_LIFE + entity.delayedLazer;
            double y_scale = entity.ticksExisted > entity.delayedLazer ?  entity.ticksExisted + partialTicks / EntityDelayedLazer.TICK_LIFE : 0;
            RenderUtil.drawBeam(renderManager, entity.getPositionVector(), entity.getRenderDirection(), new Vec3d(x, y, z), ModColors.LIGHTER_PURPLE, entity, partialTicks, new Vec3d(scale * 0.5, y_scale * 1, scale * 0.5));
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDelayedLazer entity) {
        return null;
    }


}
