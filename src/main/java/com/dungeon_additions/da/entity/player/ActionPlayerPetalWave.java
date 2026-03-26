package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.config.PotionTrinketConfig;
import com.dungeon_additions.da.entity.blossom.ProjectileVoidLeaf;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ActionPlayerPetalWave implements IActionPlayer{


    @Override
    public void performAction(EntityPlayer actor) {
        Vec3d playerLookVec = actor.getLookVec();
        Vec3d setPos = new Vec3d(actor.posX + playerLookVec.x * 1.4, (actor.posY + playerLookVec.y * 1.4) + actor.getEyeHeight(), actor.posZ + playerLookVec.z * 1.4);
        Vec3d targetPos = new Vec3d(actor.posX + playerLookVec.x * 10, (actor.posY + playerLookVec.y * 10) + actor.getEyeHeight(), actor.posZ + playerLookVec.z * 10);
        Vec3d fromTargetToActor = setPos.subtract(targetPos);
        Vec3d lineDirection = ModUtils.rotateVector2(
                        fromTargetToActor.crossProduct(ModUtils.Y_AXIS),
                        fromTargetToActor,
                        ModRand.range(-20, 20))
                .normalize()
                .scale(3);

        Vec3d lineStart = targetPos.subtract(lineDirection);
        Vec3d lineEnd = targetPos.add(lineDirection);

        ModUtils.lineCallback(lineStart, lineEnd, 5, (pos, i) -> {
            ProjectileVoidLeaf projectile = new ProjectileVoidLeaf(actor.world, actor, PotionTrinketConfig.thorn_ring_trinket_damage + ModUtils.addMageSetBonus(actor, 0, 2));
            projectile.setTravelRange(16);
            projectile.setNoGravity(true);
            ModUtils.throwProjectile(actor, pos, projectile, 0, 1.2F);
        });
    }
}
