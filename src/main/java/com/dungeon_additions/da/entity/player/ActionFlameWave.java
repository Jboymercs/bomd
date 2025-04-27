package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileFlameSling;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicGround;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ActionFlameWave implements IActionPlayer{
    @Override
    public void performAction(EntityPlayer actor) {
        float inaccuracy = 0.0f;
        float speed = 0.65f;
        float pitch = 0; // Projectiles aim straight ahead always
        actor.playSound(SoundsHandler.B_KNIGHT_FLAME_SLING, 1.0f, 0.7f / (actor.world.rand.nextFloat() * 0.4f + 0.2f));

        ProjectileFlameSling missile = new ProjectileFlameSling(actor.world, actor, (float) (4F), null, true);
        ProjectileFlameSling missile_2 = new ProjectileFlameSling(actor.world, actor, (float) (4F), null, true);
        ProjectileFlameSling missile_3 = new ProjectileFlameSling(actor.world, actor, (float) (4F), null, true);
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -1)));
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 1)));
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 0)));
        missile.setPosition(relPos.x, relPos.y, relPos.z);
        missile_2.setPosition(relPos2.x, relPos2.y, relPos2.z);
        missile_3.setPosition(relPos3.x, relPos3.y, relPos3.z);
        missile.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        missile_2.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        missile_3.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        missile.setTravelRange(24f);
        missile_2.setTravelRange(24f);
        missile_3.setTravelRange(24f);
        actor.world.spawnEntity(missile);
        actor.world.spawnEntity(missile_2);
        actor.world.spawnEntity(missile_3);
    }
}
