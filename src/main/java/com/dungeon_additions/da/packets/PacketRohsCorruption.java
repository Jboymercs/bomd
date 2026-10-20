package com.dungeon_additions.da.packets;

import com.dungeon_additions.da.capabilities.CapabilityRohsCorruption;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRohsCorruption implements IMessage {
    private int entityId;
    private boolean enableGreed;

    public PacketRohsCorruption() {}

    public PacketRohsCorruption(int entityId, boolean enableGreedIn)
    {
        this.entityId = entityId;
        this.enableGreed = enableGreedIn;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        entityId = ByteBufUtils.readVarInt(buf, 5);
        enableGreed = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeVarInt(buf, entityId, 5);
        buf.writeBoolean(enableGreed);
    }

    public static class Handler implements IMessageHandler<PacketRohsCorruption, IMessage>
    {
        @Override
        public IMessage onMessage(PacketRohsCorruption message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
                    {
                        Entity player = Minecraft.getMinecraft().world.getEntityByID(message.entityId);

                        if(player.hasCapability(CapabilityRohsCorruption.PLAYER_ROH_CORRUPTION_CAP, null))
                        {
                            player.getCapability(CapabilityRohsCorruption.PLAYER_ROH_CORRUPTION_CAP, null).setCorruptionEnabled(message.enableGreed);
                        }
                    }
            );
            return null;
        }
    }
}
