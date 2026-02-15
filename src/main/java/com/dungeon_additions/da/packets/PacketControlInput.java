package com.dungeon_additions.da.packets;

import com.dungeon_additions.da.items.shield.BOMDShieldItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketControlInput implements IMessageHandler<PacketControlInput.Message, IMessage> {

    @Override
    public IMessage onMessage(Message message, MessageContext ctx){

        // Just to make sure that the side is correct
        if(ctx.side.isServer()){

            final EntityPlayerMP player = ctx.getServerHandler().player;

            player.getServerWorld().addScheduledTask(() -> {

                ItemStack wand = player.getHeldItemMainhand();

                switch(message.controlType){

                    case SHIELD_KEY:

                        if(player.getHeldItemMainhand().getItem() instanceof BOMDShieldItem) {
                            ((BOMDShieldItem)player.getHeldItemMainhand().getItem()).onApplyButtonPressed(player, player.world, player.getHeldItemMainhand());
                        } else if(player.getHeldItemOffhand().getItem() instanceof BOMDShieldItem) {
                            ((BOMDShieldItem)player.getHeldItemOffhand().getItem()).onApplyButtonPressed(player, player.world, player.getHeldItemOffhand());
                        }
                        break;

                    case ARMOR_KEY:

                            break;
                }
            });
        }

        return null;
    }






    public enum ControlType {
        SHIELD_KEY, ARMOR_KEY
    }



    public static class Message implements IMessage {

        private ControlType controlType;

        // This constructor is required otherwise you'll get errors (used somewhere in fml through reflection)
        public Message(){
        }

        public Message(ControlType type){
            this.controlType = type;
        }

        @Override
        public void fromBytes(ByteBuf buf){
            // The order is important
            this.controlType = ControlType.values()[buf.readInt()];
        }

        @Override
        public void toBytes(ByteBuf buf){
            buf.writeInt(controlType.ordinal());
        }
    }
}
