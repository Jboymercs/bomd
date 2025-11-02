package com.dungeon_additions.da.world.mysterious_trader;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;

public class MysteriousTraderPostGeneric {

    private List<StructureComponent> components;
    private World world;
    private TemplateManager manager;

    public MysteriousTraderPostGeneric(World worldIn, TemplateManager template, List<StructureComponent> components) {
        this.world = worldIn;
        this.manager = template;
        this.components = components;
    }

    public void startBuilding(BlockPos pos, Rotation rot, String name) {
        MysteriousTraderPostTemplate template = new MysteriousTraderPostTemplate(manager, name, pos, rot, 0, true);
        components.add(template);
        System.out.println("Generated Trader Post At" + pos);
    }

}
