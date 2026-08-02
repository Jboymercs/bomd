package com.dungeon_additions.da.world.aspect_forge;

import com.dungeon_additions.da.world.dauntless.DauntlessArenaTemplate;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;

public class AspectForge {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    public AspectForge(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    public void startBuilding(BlockPos pos, Rotation rot, String name) {
        AspectForgeTemplate template = new AspectForgeTemplate(manager, name, pos.add(0, -6, 0), rot, 0, true);
        components.add(template);
        System.out.println("Generated Aspect Forge At" + pos);
    }
}
