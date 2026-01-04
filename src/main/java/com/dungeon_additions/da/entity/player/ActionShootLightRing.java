package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.sky_dungeon.ProjectileLightRing;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class ActionShootLightRing implements IActionPlayer{
    @Override
    public void performAction(EntityPlayer actor) {
         // Projectiles aim straight ahead always
        float inaccuracy = 0.0f;
        float speed = 1.0f;
        Vec3d playerLookVec = actor.getLookVec();
        boolean hasHelmet = actor.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.KINGS_HELMET;
        float damage = hasHelmet ? 21 : 13;
        ProjectileLightRing ring;
        Vec3d lazerEnd = actor.getPositionEyes(1).add(actor.getLookVec().scale(40));
        RayTraceResult raytraceresult = actor.world.rayTraceBlocks(actor.getPositionEyes(1), lazerEnd, false, true, false);
        if (raytraceresult != null) {
            // If we hit a block, make sure that any collisions with entities are detected up to the hit block
            lazerEnd = raytraceresult.hitVec;
        }

        Entity closestEntity = null;
        for (Entity entity : ModUtils.findEntitiesInLine(actor.getPositionEyes(1), lazerEnd, actor.world, actor)) {
            if (entity.canBeCollidedWith() && (closestEntity == null || entity.getDistanceSq(actor) < closestEntity.getDistanceSq(actor))) {
                closestEntity = entity;
            }
        }


        if (closestEntity != null) {
            if (closestEntity instanceof IEntityMultiPart) {
                if(closestEntity.getParts() != null) {
                    MultiPartEntityPart closestPart = null;
                    for (Entity entity : closestEntity.getParts()) {
                        RayTraceResult result = entity.getEntityBoundingBox().calculateIntercept(actor.getPositionEyes(1), lazerEnd);
                        if (result != null) {
                            if (entity instanceof MultiPartEntityPart && (closestPart == null || entity.getDistanceSq(actor) < closestPart.getDistanceSq(actor))) {
                                closestPart = (MultiPartEntityPart) entity;
                            }
                        }
                    }
                    if (closestPart != null) {
                        ring = new ProjectileLightRing(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0), ((EntityLivingBase) closestEntity));
                        Vec3d playerPos = new Vec3d(actor.posX + playerLookVec.x * 1.4D,actor.posY + playerLookVec.y + actor.getEyeHeight(), actor. posZ + playerLookVec.z * 1.4D);
                        ring.setPosition(playerPos.x, playerPos.y, playerPos.z);
                        ring.setTravelRange(60F);
                        actor.world.spawnEntity(ring);
                        ring.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
                    }
                }
            } else if (closestEntity instanceof EntityLivingBase){
                ring = new ProjectileLightRing(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0), ((EntityLivingBase) closestEntity));
                Vec3d playerPos = new Vec3d(actor.posX + playerLookVec.x * 1.4D,actor.posY + playerLookVec.y + actor.getEyeHeight(), actor. posZ + playerLookVec.z * 1.4D);
                ring.setPosition(playerPos.x, playerPos.y, playerPos.z);
                ring.setTravelRange(60F);
                actor.world.spawnEntity(ring);
                ring.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
            }
        } else {
            ring = new ProjectileLightRing(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0));
            Vec3d playerPos = new Vec3d(actor.posX + playerLookVec.x * 1.4D,actor.posY + playerLookVec.y + actor.getEyeHeight(), actor. posZ + playerLookVec.z * 1.4D);
            ring.setPosition(playerPos.x, playerPos.y, playerPos.z);
            ring.setTravelRange(60F);
            actor.world.spawnEntity(ring);
            ring.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        }
    }
}
