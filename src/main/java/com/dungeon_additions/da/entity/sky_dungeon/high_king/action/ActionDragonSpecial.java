package com.dungeon_additions.da.entity.sky_dungeon.high_king.action;

import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityDragonSpecial;
import com.dungeon_additions.da.util.ModRand;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionDragonSpecial implements IActionDrake{
    @Override
    public void performAction(EntityHighKingDrake actor, EntityLivingBase target) {
        EntityDragonSpecial special_attack = new EntityDragonSpecial(actor.world, target.getPositionVector(), target, actor);
        actor.world.spawnEntity(special_attack);

       double actorHealth = actor.getHealth() / actor.getMaxHealth();

       if(actorHealth <= 0.7) {
           actor.addEvent(() -> {
               EntityDragonSpecial special_attack2 = new EntityDragonSpecial(actor.world, target.getPositionVector(), target, actor);
               actor.world.spawnEntity(special_attack2);
           }, 120);

           if(actorHealth <= 0.3 && actor.world.rand.nextBoolean()) {
               actor.addEvent(() -> {
                   EntityDragonSpecial special_attack2 = new EntityDragonSpecial(actor.world, target.getPositionVector(), target, actor);
                   actor.world.spawnEntity(special_attack2);
               }, 240);
           }
       }
    }

}
