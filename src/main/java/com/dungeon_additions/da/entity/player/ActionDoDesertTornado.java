package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.entity.desert_dungeon.ProjectileDesertStorm;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ActionDoDesertTornado implements IActionPlayer{
    @Override
    public void performAction(EntityPlayer actor) {
        float inaccuracy = 0.0f;
        float speed = 0.55f;
        float pitch = 0; // Projectiles aim straight ahead always

        ProjectileDesertStorm storm = new ProjectileDesertStorm(actor.world, actor, 8, null, actor);
        Vec3d relPos = actor.getPositionVector();
        storm.setPosition(relPos.x, relPos.y, relPos.z);
        storm.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        storm.setTravelRange(14);
        actor.world.spawnEntity(storm);
    }
}
