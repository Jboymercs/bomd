package com.dungeon_additions.da.event;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventDropSoulStar {
    /**
     * Tells Minecraft on entity death of a zombie, skeleton, stray
     */
    @SubscribeEvent
    public static void onEntityDrop(LivingDropsEvent event)
    {
        EntityLivingBase target = event.getEntityLiving();

        if (target != null && ModConfig.enable_experimental)
        {
            if(ModConfig.soul_star_drops_everywhere) {

                if (target instanceof EntityMob) {
                    if (target.world.rand.nextFloat() <= ((float) ModConfig.soul_star_drop_chance * 0.01) + (0.05 * event.getLootingLevel())) {
                        event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(ModItems.SOUL_STAR)));
                    }
                }

            } else {
                if (target instanceof EntityZombie) {
                    if (target.world.rand.nextFloat() <= ((float) ModConfig.soul_star_drop_chance * 0.01) + (0.05 * event.getLootingLevel())) {
                        event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(ModItems.SOUL_STAR)));
                    }
                }
                if (target instanceof AbstractSkeleton) {
                    if (target.world.rand.nextFloat() <= ((float) ModConfig.soul_star_drop_chance * 0.01) + (0.5 * event.getLootingLevel())) {
                        event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(ModItems.SOUL_STAR)));
                    }
                }

            }
        }


    }
}
