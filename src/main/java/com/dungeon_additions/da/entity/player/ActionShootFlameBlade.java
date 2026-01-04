package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileFlameBlade;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.Vec3d;

public class ActionShootFlameBlade implements IActionPlayer{
    @Override
    public void performAction(EntityPlayer actor) {
        float damage = actor.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.INCENDIUM_HELMET ? (float) (7 * ModConfig.incendium_helmet_multipler): 7;
        ProjectileFlameBlade blade = new ProjectileFlameBlade(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0) + ModUtils.addAbilityBonusDamage(actor.getHeldItemMainhand(), 1.25F));
        blade.setTravelRange(10);
        float speed = 0.7F;

        Vec3d playerLookVec = actor.getLookVec();
        Vec3d playerPos = new Vec3d(actor.posX + playerLookVec.x * 1.4D,actor.posY + 1.3, actor. posZ + playerLookVec.z * 1.4D);
        blade.setPosition(playerPos.x, playerPos.y, playerPos.z);
        blade.shoot(actor, 0, actor.rotationYaw, 0, speed, 0F);
        actor.world.spawnEntity(blade);
    }
}
