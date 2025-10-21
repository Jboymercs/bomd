package com.dungeon_additions.da.entity.util;

import com.dungeon_additions.da.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class EntityMusicPlayer extends MovingSound {


    private static final SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
    private static EntityMusicPlayer currentlyPlaying = null;

    private boolean overrideMusic;

    private EntityPlayerSP player = null;
    private Entity linkedEntity = null;
    public EntityMusicPlayer(Entity entityLinkedIN, SoundEvent event) {
        super(event, SoundCategory.MUSIC);
        this.linkedEntity = entityLinkedIN;
        this.repeat = true;
        this.repeatDelay = 0;
        this.pitch = 1.0f;
        this.volume = (float) ModConfig.boss_music_volume;
    }

    public EntityMusicPlayer(Entity entityLinkedIN, SoundEvent event, boolean value) {
        super(event, SoundCategory.MUSIC);
        this.linkedEntity = entityLinkedIN;
        this.repeat = true;
        this.repeatDelay = 0;
        this.pitch = 1.0f;
        this.overrideMusic = value;
        this.volume = (float) ModConfig.boss_music_volume;
    }

    @Override
    public void update() {
        if (player == null)
            player = Minecraft.getMinecraft().player;

        if (currentlyPlaying != this) {
            if (currentlyPlaying != null)
                currentlyPlaying.donePlaying = true;

            currentlyPlaying = this;
        }

        xPosF = (float)player.posX;
        yPosF = (float)player.posY;
        zPosF = (float)player.posZ;
    }

    @Override
    public boolean isDonePlaying() {
        if (donePlaying || !soundHandler.isSoundPlaying(this) || (linkedEntity != null && linkedEntity.isDead) || overrideMusic) {
            if (currentlyPlaying != null)
                currentlyPlaying.donePlaying = true;

            currentlyPlaying = null;

            return true;
        }

        return false;
    }

    public static boolean currentlyBlockingMusic() {
        return currentlyPlaying != null && !currentlyPlaying.isDonePlaying();
    }

    public static boolean isPlaying(SoundEvent soundEvent, @Nullable Entity linkedEntity) {
        return currentlyPlaying != null && currentlyPlaying.getSoundLocation().equals(soundEvent.getSoundName()) && (linkedEntity == null ? currentlyPlaying.linkedEntity == null : currentlyPlaying.linkedEntity != null && currentlyPlaying.linkedEntity.getClass() == linkedEntity.getClass());
    }

}
