package com.dungeon_additions.da.entity.frost_dungeon;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IDirectionalRender {

    @SideOnly(Side.CLIENT)
    public void setRenderDirection(Vec3d dir);

}
