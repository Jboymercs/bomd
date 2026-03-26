package com.dungeon_additions.da.entity.dark_dungeon.boss.action;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.dark_dungeon.ProjectileSorceryDagger;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionThrowDaggers implements IAction {

    private boolean isLarge;
    private boolean isAlternate;

    public ActionThrowDaggers(boolean isLarge, boolean isAlternate) {
    this.isLarge = isLarge;
    this.isAlternate = isAlternate;
    }
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        float inaccuracy = 0.0f;
        float speed = 1.4f;
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 1.25, 0)));

        ProjectileSorceryDagger projectile = new ProjectileSorceryDagger(actor.world, actor, actor.getAttack() * 0.75F);
        ProjectileSorceryDagger projectile2 = new ProjectileSorceryDagger(actor.world, actor, actor.getAttack() * 0.75F);
        ProjectileSorceryDagger projectile3 = new ProjectileSorceryDagger(actor.world, actor, actor.getAttack() * 0.75F);

        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile2.setPosition(relPos.x, relPos.y, relPos.z);
        projectile3.setPosition(relPos.x, relPos.y, relPos.z);

        if(this.isAlternate) {
            ProjectileSorceryDagger projectile4 = new ProjectileSorceryDagger(actor.world, actor, actor.getAttack() * 0.75F);
            projectile4.setPosition(relPos.x, relPos.y, relPos.z);
            projectile.shoot(actor, 0, actor.rotationYaw - 10, 0.0F, speed, inaccuracy);
            projectile2.shoot(actor, 0, actor.rotationYaw + 10, 0.0F, speed, inaccuracy);
            projectile3.shoot(actor, 0, actor.rotationYaw - 30, 0.0F, speed, inaccuracy);
            projectile4.shoot(actor, 0, actor.rotationYaw + 30, 0.0F, speed, inaccuracy);

            projectile.rotationYaw = actor.rotationYaw - 10;
            projectile2.rotationYaw = actor.rotationYaw + 10;
            projectile3.rotationYaw = actor.rotationYaw - 30;
            projectile4.rotationYaw = actor.rotationYaw + 30;

            projectile.setTravelRange(24F);
            projectile2.setTravelRange(24F);
            projectile3.setTravelRange(24F);
            projectile4.setTravelRange(24F);

            actor.world.spawnEntity(projectile4);
        } else {
            projectile.shoot(actor, 0, actor.rotationYaw, 0.0F, speed, inaccuracy);
            projectile2.shoot(actor, 0, actor.rotationYaw - 20, 0.0F, speed, inaccuracy);
            projectile3.shoot(actor, 0, actor.rotationYaw + 20, 0.0F, speed, inaccuracy);

            projectile.rotationYaw = actor.rotationYaw;
            projectile2.rotationYaw = actor.rotationYaw - 20;
            projectile3.rotationYaw = actor.rotationYaw + 20;

            projectile.setTravelRange(24F);
            projectile2.setTravelRange(24F);
            projectile3.setTravelRange(24F);
        }

        actor.world.spawnEntity(projectile);
        actor.world.spawnEntity(projectile2);
        actor.world.spawnEntity(projectile3);

        if(this.isLarge || actor.getHealth() / actor.getMaxHealth() <= 0.5) {
            this.addDaggers(actor);
        }
    }

    private void addDaggers(EntityAbstractBase actor) {
        float inaccuracy = 0.0f;
        float speed = 1.4f;

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 1.25, 0)));

        ProjectileSorceryDagger projectile = new ProjectileSorceryDagger(actor.world, actor, actor.getAttack() * 0.75F);
        ProjectileSorceryDagger projectile2 = new ProjectileSorceryDagger(actor.world, actor, actor.getAttack() * 0.75F);

        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile2.setPosition(relPos.x, relPos.y, relPos.z);

        projectile.shoot(actor, 0, actor.rotationYaw - 45, 0.0F, speed, inaccuracy);
        projectile2.shoot(actor, 0, actor.rotationYaw + 45, 0.0F, speed, inaccuracy);

        projectile.rotationYaw = actor.rotationYaw - 45;
        projectile2.rotationYaw = actor.rotationYaw + 45;

        projectile.setTravelRange(24F);
        projectile2.setTravelRange(24F);

        actor.world.spawnEntity(projectile);
        actor.world.spawnEntity(projectile2);
    }
}
