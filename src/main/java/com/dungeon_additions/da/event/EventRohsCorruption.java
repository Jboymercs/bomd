package com.dungeon_additions.da.event;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.packets.PacketRohsProgress;
import com.dungeon_additions.da.util.PlayerRohCorruptionUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class EventRohsCorruption {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        if(event.player.getHealth() == 0) return;

        EntityPlayer player = event.player;

        if(!player.world.isRemote) {

            Main.network.sendToAll(new PacketRohsProgress(PlayerRohCorruptionUtils.getRohsCorruption(player), player.getEntityId()));

            if(PlayerRohCorruptionUtils.getRohsCorruption(player) > 60) {
                player.addPotionEffect(new PotionEffect(ModPotions.DARKDRIFT, 600, 2));
                player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 200, 1));
                PlayerRohCorruptionUtils.setRohsCorruption(player, 0);
            }

            if(player.ticksExisted % 80 == 0 && PlayerRohCorruptionUtils.getRohsCorruption(player) > 0) {
                PlayerRohCorruptionUtils.setRohsCorruption(player, PlayerRohCorruptionUtils.getRohsCorruption(player) - 1);
            }
        }
    }


}
