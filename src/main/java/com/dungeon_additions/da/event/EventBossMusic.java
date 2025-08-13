package com.dungeon_additions.da.event;

import com.dungeon_additions.da.entity.util.EntityMusicPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EventBossMusic {

    public static EventBossMusic INSTANCE = new EventBossMusic();
    @SubscribeEvent
    public void onMusicPlay(PlaySoundEvent ev) {
        if (ev.getSound() != null && ev.getSound().getCategory() == SoundCategory.MUSIC && !(ev.getSound() instanceof EntityMusicPlayer) && EntityMusicPlayer.currentlyBlockingMusic())
            ev.setResultSound(null);
    }
}
