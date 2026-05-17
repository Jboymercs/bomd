package com.dungeon_additions.da.packets;

import com.dungeon_additions.da.capabilities.CapabilityItemAnimations;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketParryAnimationItem implements IMessage {
    private int entityId;
    private int duration;

    public PacketParryAnimationItem() {}

    public PacketParryAnimationItem(int entityId, int durationIn)
    {
        this.entityId = entityId;
        this.duration = durationIn;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        entityId = buf.readInt();
        duration = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeInt(duration);
    }

    public static class Handler implements IMessageHandler<PacketParryAnimationItem, IMessage>
    {
        @Override
        public IMessage onMessage(PacketParryAnimationItem message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                Entity player = Minecraft.getMinecraft().world.getEntityByID(message.entityId);

                if(player.hasCapability(CapabilityItemAnimations.ANIM_CAP, null))
                {
                    CapabilityItemAnimations.ICapabilityItemAnimations riptide = player.getCapability(CapabilityItemAnimations.ANIM_CAP, null);
                    riptide.setParryStartTime(player.ticksExisted);
                    riptide.setParryEndTime(player.ticksExisted + message.duration);
                }
                //Helper.setRiptideCapability(player, message.isActive);
            });
            return null;
        }
    }
}
