package com.dungeon_additions.da.packets;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.capabilities.CapabilityPlayerSwing;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPlayerSwingCapability implements IMessage {
    private int entityId;
    private boolean enableGreed;
 //   private int playerSwingProg;

    public PacketPlayerSwingCapability() {}

    public PacketPlayerSwingCapability(int entityId, boolean enableGreedIn)
    {
        this.entityId = entityId;
        this.enableGreed = enableGreedIn;
      //  this.playerSwingProg = playerSwingProg;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        entityId = ByteBufUtils.readVarInt(buf, 5);
    //    playerSwingProg = ByteBufUtils.readVarInt(buf, 5);
        enableGreed = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeVarInt(buf, entityId, 5);
    //    ByteBufUtils.writeVarInt(buf, playerSwingProg, 5);
        buf.writeBoolean(enableGreed);
    }

    public static class Handler implements IMessageHandler<PacketPlayerSwingCapability, IMessage>
    {
        @Override
        public IMessage onMessage(PacketPlayerSwingCapability message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                        EntityPlayerMP player = ctx.getServerHandler().player;

                        if(player.hasCapability(CapabilityPlayerSwing.PLAYER_SWING_CAP, null))
                        {
                            player.getCapability(CapabilityPlayerSwing.PLAYER_SWING_CAP, null).setSwingEnabled(message.enableGreed);
                        //    player.getCapability(CapabilityPlayerSwing.PLAYER_SWING_CAP, null).setPlayerSwingProgress(message.playerSwingProg);
                           // Main.network.sendToAllTracking(new PacketPlayerSwingAll(player.getEntityId(), message.enableGreed), new NetworkRegistry.TargetPoint(player.world.provider.getDimension(), player.posX, player.posY, player.posZ, 0.0D));
                        }
            });
            return null;
        }
    }
}
