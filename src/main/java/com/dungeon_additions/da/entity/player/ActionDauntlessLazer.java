package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.entity.dark_dungeon.dauntless.ProjectileVerticalLazer;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.Vec3d;

public class ActionDauntlessLazer implements IActionPlayer{
    private float damage;
    public ActionDauntlessLazer(float damage) {
        this.damage = damage;
    }
    @Override
    public void performAction(EntityPlayer actor) {
        this.SummonLazer(actor.getPositionVector(), actor, 0.04F, 0.5F);
        this.SummonLazer(actor.getPositionVector(), actor, 0.06F, 1F);
        boolean hasHelmet = actor.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NIGHTFALL_HELMET;
        if(hasHelmet) {
            this.SummonLazer(actor.getPositionVector(), actor, 0.08F, 1.5F);
        }
    }

    private void SummonLazer(Vec3d savedPos, EntityPlayer actor, float radiusAdditive, float startingRadius) {
        Vec3d relPos = savedPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(startingRadius, 0, 0)));
        ProjectileVerticalLazer lazer = new ProjectileVerticalLazer(actor.world, actor, this.damage, radiusAdditive, startingRadius);
        lazer.setPosition(relPos.x, relPos.y, relPos.z);
        actor.world.spawnEntity(lazer);
    }
}
