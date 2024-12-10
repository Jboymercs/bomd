package com.dungeon_additions.da.items.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;

public interface ISweepAttackOverride {

    public void doSweepAttack(EntityPlayer player, @Nullable EntityLivingBase entity);
}
