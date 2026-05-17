package com.dungeon_additions.da.items.projectile;

import com.dungeon_additions.da.items.ItemBase;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ItemDauntlessSpear extends ItemBase implements IAnimatable {
    public AnimationFactory factory = new AnimationFactory(this);

    public ItemDauntlessSpear(String name) {
        super(name);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
