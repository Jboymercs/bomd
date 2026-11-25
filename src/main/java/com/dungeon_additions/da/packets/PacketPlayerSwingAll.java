package com.dungeon_additions.da.packets;

import com.dungeon_additions.da.capabilities.CapabilityItemAnimations;
import com.dungeon_additions.da.capabilities.CapabilityPlayerSwing;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPlayerSwingAll implements IMessage {
    private int entityId;
    private boolean cancelled;

    public PacketPlayerSwingAll() {}

    public PacketPlayerSwingAll(int entityId, boolean cancelled)
    {
        this.entityId = entityId;
        this.cancelled = cancelled;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        entityId = buf.readInt();
        cancelled = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeBoolean(cancelled);
    }

    public static class Handler implements IMessageHandler<PacketPlayerSwingAll, IMessage>
    {
        @Override
        public IMessage onMessage(PacketPlayerSwingAll message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                if (Minecraft.getMinecraft().world == null) return;
                Entity player = Minecraft.getMinecraft().world.getEntityByID(message.entityId);

                if(player != null && player.hasCapability(CapabilityPlayerSwing.PLAYER_SWING_CAP, null))
                {
                    player.getCapability(CapabilityPlayerSwing.PLAYER_SWING_CAP, null).setSwingEnabled(message.cancelled);
                }
            });
            return null;
        }
    }
}
