package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDelayedLazer;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.Vec3d;

public class ActionPlayerDauntlesLazerBarrage implements IActionPlayer{
    private float damage;
    public ActionPlayerDauntlesLazerBarrage(float damage) {
        this.damage = damage;
    }
    @Override
    public void performAction(EntityPlayer actor) {
        this.summonLazerOne(actor, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0, 0))));
        this.summonLazerOne(actor, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0, 1))));
        this.summonLazerOne(actor, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0,  - 1))));
        boolean hasHelmet = actor.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NIGHTFALL_HELMET;
        if(hasHelmet) {
            this.summonLazerOne(actor, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0, -2))));
            this.summonLazerOne(actor, actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0, 2))));
        }
    }

    private void summonLazerOne(EntityPlayer actor, Vec3d actorPos) {
        Vec3d lookVec = actor.getLookVec();
        Vec3d relPos = new Vec3d(actorPos.x + lookVec.x * 1.4D,actorPos.y + lookVec.y + actor.getEyeHeight(), actorPos.z + lookVec.z * 1.4D);
        Vec3d targetPos = new Vec3d(actorPos.x + lookVec.x * 10D,(actorPos.y + lookVec.y * 10D) + actor.getEyeHeight(), actorPos.z + lookVec.z * 10D);
        //We want to ensure the offset is in place. so target will remain null
        EntityDelayedLazer lazer = new EntityDelayedLazer(actor.world, 15, targetPos, actor, (float) (damage), null);
        lazer.setPosition(relPos.x, relPos.y, relPos.z);
        actor.world.spawnEntity(lazer);
    }
}
