package com.dungeon_additions.da.world.dauntless;

import com.dungeon_additions.da.world.outposts.OutpostsTemplate;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;

public class DauntlessArena {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    public DauntlessArena(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    public void startBuilding(BlockPos pos, Rotation rot, String name) {
        DauntlessArenaTemplate template = new DauntlessArenaTemplate(manager, name, pos.add(0, -12, 0), rot, 0, true);
        components.add(template);
        System.out.println("Generated Dauntless Arena At" + pos);
    }
}
