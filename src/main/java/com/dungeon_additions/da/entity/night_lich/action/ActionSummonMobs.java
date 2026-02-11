package com.dungeon_additions.da.entity.night_lich.action;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.night_lich.EntityLichSpawn;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.util.ModRand;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.*;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.lang.ref.WeakReference;
import java.util.List;

public class ActionSummonMobs implements IActionLich{

    /**
     * Mob summoning task, implement this in with the projectiles
     */


    public ActionSummonMobs() {

    }

    @Override
    public void performAction(EntityNightLich actor, EntityLivingBase target) {
    double currHealth = actor.getHealth() / actor.getMaxHealth();
    boolean is_second_phase = currHealth <= 0.5;
    if(!actor.world.isRemote) {
        actor.addEvent(() -> {
            BlockPos randIPos = new BlockPos(target.posX + ModRand.range(-10, 10), target.posY, target.posZ + ModRand.range(-10, 10));
            int y = getSurfaceHeight(actor.world, new BlockPos(randIPos.getX(), 0, randIPos.getZ()), (int) target.posY - 2, (int) target.posY + 4);
            EntityLivingBase new_mob = new EntityLichSpawn(actor.world, actor);
            new_mob.setPosition(randIPos.getX() + 0.5, y + 1, randIPos.getZ() + 0.5);
            actor.world.spawnEntity(new_mob);
        }, 1);

        actor.addEvent(() -> {
            BlockPos randIPos = new BlockPos(target.posX + ModRand.range(-10, 10), target.posY, target.posZ + ModRand.range(-10, 10));
            int y = getSurfaceHeight(actor.world, new BlockPos(randIPos.getX(), 0, randIPos.getZ()), (int) target.posY - 2, (int) target.posY + 4);
            EntityLivingBase new_mob = new EntityLichSpawn(actor.world, actor);
            new_mob.setPosition(randIPos.getX() + 0.5, y + 1, randIPos.getZ() + 0.5);
            actor.world.spawnEntity(new_mob);
        }, 10);

        actor.addEvent(() -> {
            BlockPos randIPos = new BlockPos(target.posX + ModRand.range(-10, 10), target.posY, target.posZ + ModRand.range(-10, 10));
            int y = getSurfaceHeight(actor.world, new BlockPos(randIPos.getX(), 0, randIPos.getZ()), (int) target.posY - 2, (int) target.posY + 4);
            EntityLivingBase new_mob = new EntityLichSpawn(actor.world, actor);
            new_mob.setPosition(randIPos.getX() + 0.5, y + 1, randIPos.getZ() + 0.5);
            actor.world.spawnEntity(new_mob);
        }, 20);

        actor.addEvent(() -> {
            BlockPos randIPos = new BlockPos(target.posX + ModRand.range(-10, 10), target.posY, target.posZ + ModRand.range(-10, 10));
            int y = getSurfaceHeight(actor.world, new BlockPos(randIPos.getX(), 0, randIPos.getZ()), (int) target.posY - 2, (int) target.posY + 4);
            EntityLivingBase new_mob = new EntityLichSpawn(actor.world, actor);
            new_mob.setPosition(randIPos.getX() + 0.5, y + 1, randIPos.getZ() + 0.5);
            actor.world.spawnEntity(new_mob);
        }, 30);
    }
    }

    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }


}
