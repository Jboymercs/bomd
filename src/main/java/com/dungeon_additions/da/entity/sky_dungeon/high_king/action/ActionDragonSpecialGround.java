package com.dungeon_additions.da.entity.sky_dungeon.high_king.action;

import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityDragonSpecial;
import com.dungeon_additions.da.util.ModRand;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionDragonSpecialGround implements IActionDrake{
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


        Vec3d currPos = actor.getPositionVector();

        for(int i = 0; i <= 60; i += 4) {
            actor.addEvent(()-> {
                Vec3d randIPos = currPos.add(ModRand.range(-30, 30), 0, ModRand.range(-30, 30));
                int y = getSurfaceHeight(actor.world, new BlockPos(randIPos.x, 0, randIPos.z), (int) randIPos.y - 9, (int) randIPos.y + 5);

                if(y != 0) {
                    EntitySkyBolt bolt = new EntitySkyBolt(actor.world, randIPos.add(ModRand.range(-2, 2), 10, ModRand.range(-2, 2)));
                    bolt.setPosition(randIPos.x, randIPos.y, randIPos.z);
                    actor.world.spawnEntity(bolt);
                }
            }, i);
        }
    }

    public int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock()) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
