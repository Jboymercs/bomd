package com.dungeon_additions.da.proxy;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.AnimationMessage;
import com.dungeon_additions.da.blocks.BlockLeaveBase;
import com.dungeon_additions.da.event.EventSwordResistance;
import com.dungeon_additions.da.packets.MessageDirectionForRender;
import com.dungeon_additions.da.packets.MessageModParticles;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy{

    public void init() {
        MinecraftForge.EVENT_BUS.register(new EventSwordResistance());
        int packetId = 0;
        Main.network = NetworkRegistry.INSTANCE.newSimpleChannel(ModReference.CHANNEL_NETWORK_NAME);
        Main.network.registerMessage(MessageModParticles.MessageHandler.class, MessageModParticles.class, packetId++, Side.CLIENT);
        Main.network.registerMessage(MessageDirectionForRender.Handler.class, MessageDirectionForRender.class, packetId++, Side.CLIENT);
    }



    public void registerEventHandlers() {

    }

    public Object getArmorModel(Item item, EntityLivingBase entity) {
        return null;
    }

    public void setFancyGraphics(BlockLeaveBase block, boolean isFancy) {
    }

    public void handleAnimationPacket(int entityId, int index) {

    }

    public boolean doesPlayerHaveXAdvancement(EntityPlayer player, ResourceLocation Id) {
        if (player instanceof EntityPlayerMP) {
            Advancement adv = ((EntityPlayerMP) player).getServerWorld().getAdvancementManager().getAdvancement(Id);
            return adv != null && ((EntityPlayerMP) player).getAdvancements().getProgress(adv).isDone();
        }
        return false;
    }

    public void registerItemRenderer(Item item, int meta, String id) {
    }
}
