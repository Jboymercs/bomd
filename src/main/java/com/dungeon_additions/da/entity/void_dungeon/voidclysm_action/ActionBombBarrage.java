package com.dungeon_additions.da.entity.void_dungeon.voidclysm_action;

import com.dungeon_additions.da.entity.ai.IActionVoidclysm;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidBomb;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiclysm;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionBombBarrage implements IActionVoidclysm {
    @Override
    public void performAction(EntityVoidiclysm actor, EntityLivingBase target) {
        double health = actor.getHealth() / actor.getMaxHealth();

        if(health < 0.5) {
            if(actor.getSpawnLocation() != null) {
                for(int i = 0; i <= 140; i += 5) {
                    actor.addEvent(() -> {
                        EntityVoidBomb bomb = new EntityVoidBomb(actor.world);
                        Vec3d randIPos = new Vec3d(actor.getSpawnLocation().getX(), actor.getSpawnLocation().getY(), actor.getSpawnLocation().getZ()).add(ModRand.range(-18, 18), 24, ModRand.range(-18, 18));
                        bomb.setPosition(randIPos.x, randIPos.y, randIPos.z);
                        actor.world.spawnEntity(bomb);
                    }, i);
                }
            } else {
                Vec3d startLocation = target.getPositionVector();
                for(int i = 0; i <= 140; i += 5) {
                    actor.addEvent(() -> {
                        EntityVoidBomb bomb = new EntityVoidBomb(actor.world);
                        Vec3d randIPos = new Vec3d(startLocation.x, startLocation.y, startLocation.z).add(ModRand.range(-18, 18), 24, ModRand.range(-18, 18));
                        bomb.setPosition(randIPos.x, randIPos.y, randIPos.z);
                        actor.world.spawnEntity(bomb);
                    }, i);
                }
            }
        } else {
            if(actor.getSpawnLocation() != null) {
                for(int i = 0; i <= 80; i += 5) {
                    actor.addEvent(() -> {
                        EntityVoidBomb bomb = new EntityVoidBomb(actor.world);
                        Vec3d randIPos = new Vec3d(actor.getSpawnLocation().getX(), actor.getSpawnLocation().getY(), actor.getSpawnLocation().getZ()).add(ModRand.range(-18, 18), 24, ModRand.range(-18, 18));
                        bomb.setPosition(randIPos.x, randIPos.y, randIPos.z);
                        actor.world.spawnEntity(bomb);
                    }, i);
                }
            } else {
                Vec3d startLocation = target.getPositionVector();
                for(int i = 0; i <= 80; i += 5) {
                    actor.addEvent(() -> {
                        EntityVoidBomb bomb = new EntityVoidBomb(actor.world);
                        Vec3d randIPos = new Vec3d(startLocation.x, startLocation.y, startLocation.z).add(ModRand.range(-18, 18), 24, ModRand.range(-18, 18));
                        bomb.setPosition(randIPos.x, randIPos.y, randIPos.z);
                        actor.world.spawnEntity(bomb);
                    }, i);
                }
            }
        }

        Vec3d targetOldPos = target.getPositionVector();
        actor.addEvent(()-> {
            Vec3d targetedPos = target.getPositionVector();
            Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 3);
            EntityVoidBomb bomb = new EntityVoidBomb(actor.world);
            bomb.setPosition(predictedPosition.x, predictedPosition.y + 20, predictedPosition.z);
            actor.world.spawnEntity(bomb);
        }, 3);

    }
}
