package com.dungeon_additions.da.items.projectile;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.items.ItemBase;
import com.dungeon_additions.da.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ItemLightRing extends ItemBase implements IAnimatable, IHasModel {

    private final String ANIM_IDLE = "idle";
    public AnimationFactory factory = new AnimationFactory(this);

    public ItemLightRing(String name) {
        super(name);
        this.setCreativeTab(CreativeTabs.SEARCH);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void registerModels() {
        {
            Main.proxy.registerItemRenderer(this, 0, "inventory");
        }}
}
