package com.dungeon_additions.da.packets;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.capabilities.CapabilityItemAnimations;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketServerSwingItem implements IMessage {
    private int entityId;
    private int duration;

    public PacketServerSwingItem() {}

    public PacketServerSwingItem(int entityId, int durationIn)
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

    public static class Handler implements IMessageHandler<PacketServerSwingItem, IMessage>
    {
        @Override
        public IMessage onMessage(PacketServerSwingItem message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;

            if(player.hasCapability(CapabilityItemAnimations.ANIM_CAP, null))
            {
                CapabilityItemAnimations.ICapabilityItemAnimations anim = player.getCapability(CapabilityItemAnimations.ANIM_CAP, null);
                anim.setCustomSwingStartTime(player.ticksExisted);
                anim.setCustomSwingEndTime(player.ticksExisted + message.duration);

                /* Server then auto-informs Clients. */
                Main.network.sendToAllTracking(new PacketSwingItem(player.getEntityId(), message.duration), new NetworkRegistry.TargetPoint(player.world.provider.getDimension(), player.posX, player.posY, player.posZ, 0.0D));
            }
            return null;
        }
    }
}
