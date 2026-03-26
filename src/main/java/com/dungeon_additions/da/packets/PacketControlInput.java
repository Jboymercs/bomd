package com.dungeon_additions.da.packets;

import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.shield.BOMDShieldItem;
import com.dungeon_additions.da.items.trinket.ItemTrinket;
import com.dungeon_additions.da.util.ModUtils;
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

                    case TRINKET_KEY:
                        ItemStack wind_trinket = ModUtils.findTrinket(new ItemStack(ModItems.WIND_TRINKET), player);
                        ItemStack sigil_trinket = ModUtils.findTrinket(new ItemStack(ModItems.SIGIL_TRINKET), player);
                        ItemStack teleport_trinket = ModUtils.findTrinket(new ItemStack(ModItems.TELEPORT_TRINKET), player);
                        ItemStack dodge_trinket = ModUtils.findTrinket(new ItemStack(ModItems.DODGE_TRINKET), player);
                        ItemStack rotten_trinket = ModUtils.findTrinket(new ItemStack(ModItems.ROTTEN_TRINKET), player);
                        ItemStack blue_trinket = ModUtils.findTrinket(new ItemStack(ModItems.BLUE_TRINKET), player);
                        ItemStack flame_explosion_trinket = ModUtils.findTrinket(new ItemStack(ModItems.FLAME_EXPLOSION_TRINKET), player);
                        ItemStack mace_trinket = ModUtils.findTrinket(new ItemStack(ModItems.MACE_TRINKET), player);
                        ItemStack rally_trinket = ModUtils.findTrinket(new ItemStack(ModItems.RALLY_TRINKET), player);
                        ItemStack petal_trinket = ModUtils.findTrinket(new ItemStack(ModItems.PETAL_TRINKET), player);
                        if(!wind_trinket.isEmpty()) {
                            if(wind_trinket.getItem() instanceof ItemTrinket) {
                                ((ItemTrinket)wind_trinket.getItem()).onApplyButtonPressed(player, player.world, wind_trinket, 1);
                            }
                        } else if (!sigil_trinket.isEmpty()) {
                            if(sigil_trinket.getItem() instanceof ItemTrinket) {
                                ((ItemTrinket)sigil_trinket.getItem()).onApplyButtonPressed(player, player.world, sigil_trinket, 2);
                            }
                        } else if (!teleport_trinket.isEmpty()) {
                            if(teleport_trinket.getItem() instanceof ItemTrinket) {
                                ((ItemTrinket)teleport_trinket.getItem()).onApplyButtonPressed(player, player.world, teleport_trinket, 3);
                            }
                        } else if (!dodge_trinket.isEmpty()) {
                            if(dodge_trinket.getItem() instanceof ItemTrinket) {
                                ((ItemTrinket)dodge_trinket.getItem()).onApplyButtonPressed(player, player.world, dodge_trinket, 4);
                            }
                        } else if (!rotten_trinket.isEmpty()) {
                            if(rotten_trinket.getItem() instanceof ItemTrinket) {
                                ((ItemTrinket)rotten_trinket.getItem()).onApplyButtonPressed(player, player.world, rotten_trinket, 5);
                            }
                        } else if (!blue_trinket.isEmpty()) {
                            if(blue_trinket.getItem() instanceof ItemTrinket) {
                                ((ItemTrinket)blue_trinket.getItem()).onApplyButtonPressed(player, player.world, blue_trinket, 6);
                            }
                        } else if (!flame_explosion_trinket.isEmpty()) {
                            if(flame_explosion_trinket.getItem() instanceof ItemTrinket) {
                                ((ItemTrinket)flame_explosion_trinket.getItem()).onApplyButtonPressed(player, player.world, flame_explosion_trinket, 7);
                            }
                        } else if (!mace_trinket.isEmpty()) {
                            if(mace_trinket.getItem() instanceof ItemTrinket) {
                                ((ItemTrinket)mace_trinket.getItem()).onApplyButtonPressed(player, player.world, mace_trinket, 8);
                            }
                        } else if (!rally_trinket.isEmpty()) {
                            if(rally_trinket.getItem() instanceof ItemTrinket) {
                                ((ItemTrinket)rally_trinket.getItem()).onApplyButtonPressed(player, player.world, rally_trinket, 9);
                            }
                        } else if (!petal_trinket.isEmpty()) {
                            if(petal_trinket.getItem() instanceof ItemTrinket) {
                                ((ItemTrinket)petal_trinket.getItem()).onApplyButtonPressed(player, player.world, petal_trinket, 10);
                            }
                        }
                            break;
                }
            });
        }

        return null;
    }






    public enum ControlType {
        SHIELD_KEY, TRINKET_KEY
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
