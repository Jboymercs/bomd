package com.dungeon_additions.da.entity.dark_dungeon.boss.action;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;

public class ActionVerticalSlash implements IAction {
    private float damage;
    private float distance;
    private float impactRange;

    public ActionVerticalSlash(float damage, float distance, float impactRange) {
        this.damage = damage;
        this.distance = distance;
        this.impactRange = impactRange;
    }
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d savedPos = actor.getPositionVector();
        for(int i = 0; i <= distance; i += (int) impactRange) {
            int finalI = i;
            Vec3d finalPos = savedPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI, 1.9, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(actor).disablesShields().build();
            ModUtils.handleAreaImpact(impactRange, (e) -> damage, actor, finalPos, source, 0.2f, 0, false, 1.3F);
            ModUtils.performNTimes(4, (b) -> {
               // Main.proxy.spawnParticle(30, finalPos.x + ModRand.getFloat(1.5F), finalPos.y + ModRand.getFloat(1.4F) + 2, finalPos.z + ModRand.getFloat(1.5F), 0, -0.06, 0, 7340032);
                Main.proxy.spawnParticle(48, actor.world, finalPos.x + ModRand.getFloat(1.5F), finalPos.y + ModRand.getFloat(1.4F) + 1.4F , finalPos.z + ModRand.getFloat(1.5F), 0, 0, 0, 11607828);
            });
        }
    }
}
