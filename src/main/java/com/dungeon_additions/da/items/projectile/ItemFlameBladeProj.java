package com.dungeon_additions.da.items.projectile;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.AzLib.DaItemAnimDispatcher;
import com.dungeon_additions.da.items.ItemBase;
import com.dungeon_additions.da.util.IHasModel;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFlameBladeProj extends ItemBase implements IHasModel {
    private final String ANIM_IDLE = "spin";
    public final DaItemAnimDispatcher animator;

    public ItemFlameBladeProj(String name) {
        super(name);
        this.animator = new DaItemAnimDispatcher();
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if (stack.getItem() == this) {
                animator.performItemAnimation(entityIn, stack, true, ANIM_IDLE);
            }
        }
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void registerModels() {
        {
            Main.proxy.registerItemRenderer(this, 0, "inventory");
        }}
}
