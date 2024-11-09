package com.dungeon_additions.da.entity.ai;

import com.dungeon_additions.da.entity.projectiles.Projectile;
import net.minecraft.entity.EntityLivingBase;

public interface IActionProjectile {
    void performAction(Projectile actor, EntityLivingBase target);

    IAction NONE = (actor, target) -> {
    };
}
