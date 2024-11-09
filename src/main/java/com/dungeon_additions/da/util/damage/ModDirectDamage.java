package com.dungeon_additions.da.util.damage;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

public class ModDirectDamage extends EntityDamageSource implements IElement {
    Element element;

    public ModDirectDamage(String damageTypeIn, Entity damageSourceEntityIn, Element element) {
        super(damageTypeIn, damageSourceEntityIn);
        this.element = element;
    }

    @Override
    public Element getElement() {
        return element;
    }
}
