package com.dungeon_additions.da.animation.AzLib;

import mod.azure.azurelib.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class DaItemAnimDispatcher {

    public void performItemAnimation(Entity entity, ItemStack stack, boolean loop, String animationString) {
        AzCommand command = AzCommand.create(DaReferenceStrings.STATE_CTR_ONE, animationString, loop ? AzPlayBehaviors.LOOP : AzPlayBehaviors.PLAY_ONCE);
        command.sendForItem(entity, stack);
    }

}
