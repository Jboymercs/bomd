package com.dungeon_additions.da.packets;

import com.dungeon_additions.da.capabilities.CapabilityPlayerFalter;
import com.dungeon_additions.da.capabilities.CapabilityRohsCorruption;
import com.dungeon_additions.da.util.PlayerFalterUtils;
import com.dungeon_additions.da.util.PlayerRohCorruptionUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketRohsProgress implements IMessage {
    private int falterProg;
    private int entityId;

    public PacketRohsProgress() {
    }

    public PacketRohsProgress(int falterProg, int entityID) {
        this.falterProg = falterProg;
        this.entityId = entityID;
    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        entityId = byteBuf.readInt();
        falterProg = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(entityId);
        byteBuf.writeInt(falterProg);
    }

    public static class Handler implements IMessageHandler<PacketRohsProgress, IMessage>
    {
        @Override
        public IMessage onMessage(PacketRohsProgress message, MessageContext ctx)
        {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() ->
                {
                    Entity player = Minecraft.getMinecraft().world.getEntityByID(message.entityId);

                    if (player.hasCapability(CapabilityRohsCorruption.PLAYER_ROH_CORRUPTION_CAP, null)) {
                        PlayerRohCorruptionUtils.setRohsCorruption((EntityPlayer)player, message.falterProg);
                    }
                });
            }
            return null;
        }
    }
}
