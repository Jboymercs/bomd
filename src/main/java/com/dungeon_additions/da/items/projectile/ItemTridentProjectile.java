package com.dungeon_additions.da.items.projectile;

import com.dungeon_additions.da.items.ItemBase;
import net.minecraft.creativetab.CreativeTabs;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ItemTridentProjectile extends ItemBase implements IAnimatable {
    public AnimationFactory factory = new AnimationFactory(this);
    public ItemTridentProjectile(String name) {
        super(name);
        this.setCreativeTab(CreativeTabs.SEARCH);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
