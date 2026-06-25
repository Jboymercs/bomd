package com.dungeon_additions.da.packets;

import com.dungeon_additions.da.capabilities.CapabilityPlayerFalter;
import com.dungeon_additions.da.capabilities.CapabilityPlayerSwing;
import com.dungeon_additions.da.util.PlayerFalterUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketFalterProgress implements IMessage {

    private float falterProg;
    private float falterResistance;
    private int entityId;
    public PacketFalterProgress() {
    }

    public PacketFalterProgress(float falterProg, float falterResistance, int entityID) {
        this.falterProg = falterProg;
        this.falterResistance = falterResistance;
        this.entityId = entityID;
    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        entityId = byteBuf.readInt();
        falterProg = byteBuf.readFloat();
        falterResistance = byteBuf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(entityId);
        byteBuf.writeFloat(falterProg);
        byteBuf.writeFloat(falterResistance);
    }

    public static class Handler implements IMessageHandler<PacketFalterProgress, IMessage>
    {
        @Override
        public IMessage onMessage(PacketFalterProgress message, MessageContext ctx)
        {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() ->
                {
                    Entity player = Minecraft.getMinecraft().world.getEntityByID(message.entityId);

                    if (player.hasCapability(CapabilityPlayerFalter.PLAYER_FALTER_CAP, null)) {
                        PlayerFalterUtils.setPlayerFalterResistance((EntityPlayer) player, message.falterResistance);
                        PlayerFalterUtils.setPlayerGreedProgress((EntityPlayer) player, message.falterProg);
                    }
                });
            }
            return null;
        }
    }
}
