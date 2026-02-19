package com.dungeon_additions.da.event;


import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.items.shield.BOMDShieldItem;
import com.dungeon_additions.da.items.tools.ItemExaltedKopis;
import com.dungeon_additions.da.items.tools.ItemParrySword;
import com.dungeon_additions.da.packets.PacketControlInput;
import com.dungeon_additions.da.proxy.ClientProxy;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ModReference.MOD_ID)
public class ClientEventHandler {

    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public static void onRenderHand(RenderSpecificHandEvent event)
    {
        EntityPlayerSP player = mc.player;
        if (player != null && player.isHandActive() && player.getActiveHand() == event.getHand() && (player.getHeldItem(player.getActiveHand()).getItem() instanceof ItemParrySword || player.getHeldItem(player.getActiveHand()).getItem() instanceof ItemExaltedKopis))
        {
            GlStateManager.pushMatrix();
            boolean rightHanded = (((event.getHand() == EnumHand.MAIN_HAND) ? player.getPrimaryHand() : player.getPrimaryHand().opposite()) == EnumHandSide.RIGHT);
            transformSideFirstPerson(rightHanded ? 1.0F : -1.0F, event.getEquipProgress());
            mc.getItemRenderer().renderItemSide(player, event.getItemStack(), rightHanded ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !rightHanded);
            GlStateManager.popMatrix();
            event.setCanceled(true);
        }
    }

    private static void transformSideFirstPerson(float side, float equippedProg)
    {
        GlStateManager.translate(side * 0.56F, -0.52F + equippedProg * -0.6F, -0.72F);
        GlStateManager.translate(side * -0.14142136F, 0.08F, 0.14142136F);
        GlStateManager.rotate(-102.25F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(side * 13.365F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(side * 78.05F, 0.0F, 0.0F, 1.0F);
    }


    @SubscribeEvent
    public static void onTickEvent(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.END) return;

        if(Main.proxy instanceof ClientProxy) {

            EntityPlayer player = Minecraft.getMinecraft().player;

            if(player != null) {

                ItemStack stack = player.getHeldItemOffhand();
                ItemStack stack2 = player.getHeldItemMainhand();
                if(stack.getItem() instanceof BOMDShieldItem || stack2.getItem() instanceof BOMDShieldItem) {
                    if(ClientProxy.SHIELD_ABILITY.isKeyDown() && Minecraft.getMinecraft().inGameHasFocus) {
                        performShieldAbility();
                    }
                }
            }
        }
    }



    //does shield ability handling
    private static void performShieldAbility() {
        IMessage msg = new PacketControlInput.Message(PacketControlInput.ControlType.SHIELD_KEY);
        Main.network.sendToServer(msg);
    }
}
