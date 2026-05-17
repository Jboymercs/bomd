package com.dungeon_additions.da.mixin;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.event.ClientEventHandler;
import com.dungeon_additions.da.items.tools.ToolSword;
import com.dungeon_additions.da.packets.PacketPlayerSwingCapability;
import com.dungeon_additions.da.util.PlayerCustomSwingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow public RayTraceResult objectMouseOver;
    @Shadow public EntityPlayerSP player;


    @Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/entity/EntityPlayerSP;isRowingBoat()Z"), method = "clickMouse", cancellable = true)
    public void mounts$clickMouse(CallbackInfo callback)
    {
        ItemStack stack = player.getHeldItemMainhand();
        if (!(stack.getItem() instanceof ToolSword)) return;
        if (player.getCooledAttackStrength(0) < 1) return;
        //removes the delayed swing
        if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            Main.network.sendToServer(new PacketPlayerSwingCapability(player.getEntityId(), true));
            return;
        }
        ClientEventHandler.swingingCustom = true;
    }


}
