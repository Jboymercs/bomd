package com.dungeon_additions.da.entity.night_lich.action;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.packets.EnumModParticles;
import com.dungeon_additions.da.packets.MessageModParticles;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionLichTeleport implements IActionLich{

    Vec3d teleportColor;
    Vec3d posToo;

    public ActionLichTeleport(Vec3d teleportColor, Vec3d PosToo) {
        this.teleportColor = teleportColor;
        this.posToo = PosToo;
    }

    @Override
    public void performAction(EntityNightLich actor, EntityLivingBase target) {
        ModUtils.attemptTeleport(posToo, actor);
        actor.world.setEntityState(actor, ModUtils.SECOND_PARTICLE_BYTE);
     //   for(int i = 0; i < 30; i++) {
      //      Vec3d pos = (posToo);


    //        Vec3d prevPos = actor.getPositionVector();
    //        if(ModUtils.attemptTeleport(pos, actor)){
     //           ModUtils.lineCallback(prevPos, pos, 30, (particlePos, j) ->
     //                   Main.network.sendToAllTracking(new MessageModParticles(EnumModParticles.EFFECT, particlePos, Vec3d.ZERO, teleportColor), actor));
     //           actor.world.setEntityState(actor, ModUtils.SECOND_PARTICLE_BYTE);
     //           break;
        //    }
      //  }
    }
}
