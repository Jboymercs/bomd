package com.dungeon_additions.da.world.aspect_forge;

import com.dungeon_additions.da.world.ModStructureTemplate;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class AspectForgeTemplate extends ModStructureTemplate {

    public AspectForgeTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public AspectForgeTemplate() {

    }

    @Override
    public String templateLocation() {
        return "aspect_forge";
    }
}
