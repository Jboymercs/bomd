package com.dungeon_additions.da.entity.frost_dungeon.draugr.draugr_elite;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionDraugrTeleport implements IAction {
    Vec3d posToo;

    public ActionDraugrTeleport(Vec3d PosToo) {

        this.posToo = PosToo;
    }

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        ModUtils.attemptTeleport(posToo, actor);
        actor.world.setEntityState(actor, ModUtils.SECOND_PARTICLE_BYTE);
    }
}
