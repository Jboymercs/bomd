package com.dungeon_additions.da.animation.example;

import com.dungeon_additions.da.entity.render.RenderModEntity;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderExample extends RenderModEntity<EntityExample> {


    public <U extends ModelBase> RenderExample(RenderManager rendermanagerIn) {
        super(rendermanagerIn, "example.png", new ModelExample());
    }

}
