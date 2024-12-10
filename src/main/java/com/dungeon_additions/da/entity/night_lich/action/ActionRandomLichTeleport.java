package com.dungeon_additions.da.entity.night_lich.action;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.packets.EnumModParticles;
import com.dungeon_additions.da.packets.MessageModParticles;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionRandomLichTeleport implements IActionLich{
    Vec3d teleportColor;

    public ActionRandomLichTeleport(Vec3d teleportColor) {
        this.teleportColor = teleportColor;
    }

    @Override
    public void performAction(EntityNightLich actor, EntityLivingBase target) {
        for(int i = 0; i < 20; i++) {
            Vec3d pos = ModRand.randVec().normalize().scale(7)
                    .add(target.getPositionVector());

            boolean canSee = actor.world.rayTraceBlocks(target.getPositionEyes(1), pos, false, true, false) == null;
            Vec3d prevPos = actor.getPositionVector();
            if(canSee && ModUtils.attemptTeleport(pos, actor)){
                ModUtils.lineCallback(prevPos, pos, 20, (particlePos, j) ->
                      //  Main.network.sendToAllTracking(new MessageModParticles(EnumModParticles.EFFECT, particlePos, Vec3d.ZERO, teleportColor), actor));
                actor.world.setEntityState(actor, ModUtils.SECOND_PARTICLE_BYTE));
                break;
            }
        }
    }
}
