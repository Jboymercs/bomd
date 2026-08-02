package com.dungeon_additions.da.event;


import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.item.*;
import com.dungeon_additions.da.capabilities.AnimationCapabilityHelper;
import com.dungeon_additions.da.capabilities.CapabilityItemAnimations;
import com.dungeon_additions.da.capabilities.CapabilityPlayerFalter;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.armor.ModIncendiumHelmet;
import com.dungeon_additions.da.items.shield.BOMDShieldItem;
import com.dungeon_additions.da.items.tools.*;
import com.dungeon_additions.da.items.trinket.ItemTrinket;
import com.dungeon_additions.da.packets.PacketControlInput;
import com.dungeon_additions.da.packets.PacketParryAnimationItem;
import com.dungeon_additions.da.packets.PacketPlayerSwingCapability;
import com.dungeon_additions.da.packets.PacketServerSwingItem;
import com.dungeon_additions.da.proxy.ClientProxy;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.PlayerCustomSwingUtils;
import com.dungeon_additions.da.util.PlayerFalterUtils;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;

import javax.annotation.Nonnull;
import java.io.FilterOutputStream;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ModReference.MOD_ID)
public class ClientEventHandler {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean swingingCustom = false;
    private static final ResourceLocation TEXTURE_FALTER_FRAME = new ResourceLocation(ModReference.MOD_ID, "textures/gui/falter_gui.png");
    private static final ResourceLocation TEXTURE_FALTER_PROGRESS = new ResourceLocation(ModReference.MOD_ID, "textures/gui/falter_progress.png");

    private static float currFalterProg = 0.0F;
    private static float prevCurrFalterProg = 0.0F;
    private static float currFalterResistance = 0.0F;
    private static float prevCurrFalterResistance = 0.0F;

    @SubscribeEvent
    public static void onFalterUpdateTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        prevCurrFalterProg = currFalterProg;
        prevCurrFalterResistance = currFalterResistance;

        if (mc.player.hasCapability(CapabilityPlayerFalter.PLAYER_FALTER_CAP, null))
        {
            //currSpaceHeld = mc.player.getCapability(CapabilitySpearMovement.MOUNTS_PLAYER_CAP, null).getSpaceHeldTime();
            currFalterProg = mc.player.getCapability(CapabilityPlayerFalter.PLAYER_FALTER_CAP, null).getPlayerFalterProgress();
            currFalterResistance = mc.player.getCapability(CapabilityPlayerFalter.PLAYER_FALTER_CAP, null).getPlayerFalterResistance();
        }
    }

    @SubscribeEvent
    public static void onTickEvent(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.END) return;

        if(Main.proxy instanceof ClientProxy) {

            EntityPlayer player = Minecraft.getMinecraft().player;

            if(player != null) {

                ItemStack stack = player.getHeldItemOffhand();
                ItemStack stack2 = player.getHeldItemMainhand();
                if(ClientProxy.SHIELD_ABILITY.isKeyDown() && Minecraft.getMinecraft().inGameHasFocus) {
                    //shields
                    if(stack.getItem() instanceof BOMDShieldItem && player.getActiveItemStack() == stack || stack2.getItem() instanceof BOMDShieldItem&& player.getActiveItemStack() == stack2) {
                        performShieldAbility();
                        //trinket abilities
                    } else {
                        performTrinketAbility();
                    }
                }

            }
        }
    }


    @SubscribeEvent
    public static void onRenderOverlayPost(RenderGameOverlayEvent.Post event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            if (mc.player != null && currFalterProg > 0)
            {
                renderFalterBar(mc); }
        }
    }



    private static void renderFalterBar(Minecraft mc)
    {
            ScaledResolution res = new ScaledResolution(mc);
            int width = res.getScaledWidth();
            int height = res.getScaledHeight();
            int barX = width - 76;
            int barY = height - 146;

            float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
          //  float spaceHeldTime = prevSpaceHeld + (currSpaceHeld - prevSpaceHeld) * partialTicks;

            GlStateManager.enableTexture2D();
            GlStateManager.color(1F, 1F, 1F, 1F);
            mc.getTextureManager().bindTexture(TEXTURE_FALTER_FRAME);
            //renders the frame
            mc.ingameGUI.drawTexturedModalRect(barX, barY, 0, 0, 30, 132);

            float currF = prevCurrFalterProg + (currFalterProg - prevCurrFalterProg) * partialTicks;
            float currFR = prevCurrFalterResistance + (currFalterResistance - prevCurrFalterResistance) * partialTicks;
          //    if(PlayerFalterUtils.getPlayerFalterProgress(player) > 0) {
                  float percentage = currF / currFR;
                  int filled = (int) (percentage * 100);
                 // System.out.println("Player Falter Prog at" + PlayerFalterUtils.getPlayerFalterResistance(player));
                  mc.ingameGUI.drawTexturedModalRect(barX + 12, barY + 26 + (100 - filled), 30, 0, 6, filled);
            //  }

        //    int filled = (int) (powerResult * 182);
        //    if (filled > 0) {
                //System.out.print("Power Result: " + powerResult);
         //       mc.ingameGUI.drawTexturedModalRect(barX, barY, 0, 5, filled, 5);
        //    }

         //   if (ridden.getDashCooldown() > 0) {
         //       mc.ingameGUI.drawTexturedModalRect(barX, barY, 0, 10, 182, 5);
         //   }
    }


    private static void performTrinketAbility() {
        IMessage msg = new PacketControlInput.Message(PacketControlInput.ControlType.TRINKET_KEY);
        Main.network.sendToServer(msg);
    }

    //does shield ability handling
    private static void performShieldAbility() {
        IMessage msg = new PacketControlInput.Message(PacketControlInput.ControlType.SHIELD_KEY);
        Main.network.sendToServer(msg);
    }

    @SubscribeEvent
    public static void logIn(PlayerEvent.PlayerLoggedInEvent event) { swingingCustom = false; }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onMouseClick(MouseEvent event)
    {
        if (event.getButton() != 0 || !event.isButtonstate()) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        ItemStack stack = player.getHeldItemMainhand();

        if (!(stack.getItem() instanceof ToolSword)) return;

        //cycle out any disabled weapon types
        if(DAPlayerAnimationMethods.getWeaponType(player) == 1 && !ModConfig.enable_sword_weapons) return;
        if(DAPlayerAnimationMethods.getWeaponType(player) == 2 && !ModConfig.enable_dagger_weapons) return;
        if(DAPlayerAnimationMethods.getWeaponType(player) == 3 && !ModConfig.enable_parry_sword_weapons) return;
        if(DAPlayerAnimationMethods.getWeaponType(player) == 4 && !ModConfig.enable_spear_weapons) return;
        if( DAPlayerAnimationMethods.getWeaponType(player) == 5 && !ModConfig.enable_heavy_weapons) return;

        //we will want a custom swing delay to tie in the players animations
        if(stack.getItem() instanceof ToolSword && ModConfig.weapon_hit_delays && ModConfig.combat_system_enabled) {
            ToolSword weapon = ((ToolSword) stack.getItem());
            //if the weapon has a delay greater than 0
            float cooldownStrength = player.getCooledAttackStrength(0.5F);
            if(cooldownStrength > 0.9) {
                if(PlayerCustomSwingUtils.getCapability(player) != null) {
                    boolean swingCancelled = PlayerCustomSwingUtils.getPlayerSwingCancelled(player);
                        //I hope this does this correctly
                        Main.network.sendToServer(new PacketPlayerSwingCapability(player.getEntityId(), false));

                }
            }
        }


        double attackSpeed = 4.0D;
        Multimap<String, AttributeModifier> modifiers = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);

        if (modifiers.containsKey(SharedMonsterAttributes.ATTACK_SPEED.getName()))
        {
            for (AttributeModifier mod : modifiers.get(SharedMonsterAttributes.ATTACK_SPEED.getName()))
            { attackSpeed += mod.getAmount(); }
        }

        int duration = (int)(20.0D / attackSpeed);


        CapabilityItemAnimations.ICapabilityItemAnimations anim = player.getCapability(CapabilityItemAnimations.ANIM_CAP, null);
        if (anim == null) return;

        anim.setCustomSwingStartTime(player.ticksExisted);
        anim.setCustomSwingEndTime(player.ticksExisted + duration);
        Main.network.sendToServer(new PacketServerSwingItem(player.getEntityId(), duration));
    }

    /** Remember, this hook leads to `ItemRenderer`, so check it for transformation info and bullshit. */
    //This handles first person Item rotations
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    static void renderFirstPersonTrident(@Nonnull RenderSpecificHandEvent event)
    {
        if (!ModConfig.custom_swing_animations) return;
        if(!ModConfig.combat_system_enabled) return;

        final ItemStack stack = event.getItemStack();
        final EntityPlayer player = Minecraft.getMinecraft().player;
        final ItemRenderer renderer = Minecraft.getMinecraft().getItemRenderer();
        float partialTicks = event.getPartialTicks();
        final EnumHandSide arm = event.getHand() == EnumHand.MAIN_HAND ? player.getPrimaryHand() : player.getPrimaryHand().opposite();

        boolean isRightArm = arm == EnumHandSide.RIGHT;

        float cooldownStrength = player.getCooledAttackStrength(partialTicks);

   // if (swingingCustom && event.getHand() == EnumHand.MAIN_HAND) {swingingCustom = false; }

        if(stack.getItem() instanceof ToolSword)
        {
            //Swords
            if(DAPlayerAnimationMethods.getWeaponType(player) == 1 && ModConfig.enable_sword_weapons) {
                //specifically for geckolib models
                if(player.getHeldItemMainhand().getItem() instanceof ItemNightfallSword && cooldownStrength > 0 && swingingCustom) {
                    GlStateManager.pushMatrix();
                    AnimationsBaseSword.preformSwordItemRotations1stPersonGeckolib(Minecraft.getMinecraft().player, partialTicks, cooldownStrength, arm);
                    renderer.renderItemSide(player, stack, isRightArm ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isRightArm);
                    GlStateManager.popMatrix();

                    event.setCanceled(true);
                } else if (cooldownStrength > 0 && swingingCustom)
                {
                    GlStateManager.pushMatrix();
                    AnimationsBaseSword.preformSwordItemRotations1stPerson(Minecraft.getMinecraft().player, partialTicks, cooldownStrength, arm);
                    renderer.renderItemSide(player, stack, isRightArm ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isRightArm);
                    GlStateManager.popMatrix();

                    event.setCanceled(true);
                }
                //Daggers
            } else if (DAPlayerAnimationMethods.getWeaponType(player) == 2 && ModConfig.enable_dagger_weapons) {
                if(player.getHeldItemMainhand().getItem() instanceof ItemNightfallGauntlets && cooldownStrength > 0 && swingingCustom) {
                    GlStateManager.pushMatrix();
                    AnimationBaseDagger.preformDaggerItemRotations1stPersonFist(Minecraft.getMinecraft().player, partialTicks, cooldownStrength, arm);
                    renderer.renderItemSide(player, stack, isRightArm ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isRightArm);
                    GlStateManager.popMatrix();

                    event.setCanceled(true);
                } else if (cooldownStrength > 0 && swingingCustom)
                {
                    GlStateManager.pushMatrix();
                    AnimationBaseDagger.preformDaggerItemRotations1stPerson(Minecraft.getMinecraft().player, partialTicks, cooldownStrength, arm);
                    renderer.renderItemSide(player, stack, isRightArm ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isRightArm);
                    GlStateManager.popMatrix();

                    event.setCanceled(true);
                }
                //Parry Sword
            } else if (DAPlayerAnimationMethods.getWeaponType(player) == 3 && ModConfig.enable_parry_sword_weapons) {
                if (cooldownStrength > 0 && swingingCustom)
                {
                    GlStateManager.pushMatrix();
                    AnimationsBaseSword.preformSwordItemRotations1stPerson(Minecraft.getMinecraft().player, partialTicks, cooldownStrength, arm);
                    renderer.renderItemSide(player, stack, isRightArm ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isRightArm);
                    GlStateManager.popMatrix();

                    event.setCanceled(true);
                }
                if(AnimationCapabilityHelper.isPlayerParryItemAnimation(player)) {
                    GlStateManager.pushMatrix();
                    float customTime = AnimationCapabilityHelper.getPlayerParryAnimProgress(player, partialTicks);
                    AnimationsBaseSword.preformSwordParryItemRotations(Minecraft.getMinecraft().player, partialTicks, customTime, arm);
                    renderer.renderItemSide(player, stack, isRightArm ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isRightArm);
                    GlStateManager.popMatrix();

                    event.setCanceled(true);
                }
                //Spears
            } else if (DAPlayerAnimationMethods.getWeaponType(player) == 4 && ModConfig.enable_spear_weapons) {

                //charging animation for trident
                if(stack.getItem() instanceof ItemStormvierTrident && player.getActiveItemStack() == stack) {
                    GlStateManager.pushMatrix();
                    AnimationsMisc.preformItemChargingFirstPerson(player, cooldownStrength, partialTicks, arm);
                    renderer.renderItemInFirstPerson(Minecraft.getMinecraft().player, partialTicks, event.getInterpolatedPitch(), event.getHand(), 0, stack, 0);
                    GlStateManager.popMatrix();
                    event.setCanceled(true);
                } else if (cooldownStrength > 0 && swingingCustom)
                {
                    if(stack.getItem() instanceof ItemBloodySwordSpear || stack.getItem() instanceof ItemSwordSpear || stack.getItem() instanceof ItemImperialHalberd || stack.getItem() instanceof ItemStormvierTrident) {
                        GlStateManager.pushMatrix();
                        AnimationBaseSpear.preformBigSpearItemRotations1stPerson(Minecraft.getMinecraft().player, partialTicks, cooldownStrength, arm);
                        renderer.renderItemSide(player, stack, isRightArm ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isRightArm);
                        GlStateManager.popMatrix();

                        event.setCanceled(true);
                    } else {
                        GlStateManager.pushMatrix();
                        AnimationBaseSpear.preformSpearItemRotations1stPerson(Minecraft.getMinecraft().player, partialTicks, cooldownStrength, arm);
                        renderer.renderItemSide(player, stack, isRightArm ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isRightArm);
                        GlStateManager.popMatrix();

                        event.setCanceled(true);
                    }
                }
            }  else if (DAPlayerAnimationMethods.getWeaponType(player) == 5 && ModConfig.enable_heavy_weapons) {
                if (cooldownStrength > 0 && swingingCustom)
                {
                    if(stack.getItem() instanceof ItemChampionAxe) {
                        GlStateManager.pushMatrix();
                        AnimationBaseColossalWeapon.preformChampionAxeItemRotations1stPerson(Minecraft.getMinecraft().player, partialTicks, cooldownStrength, arm);
                        renderer.renderItemSide(player, stack, isRightArm ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isRightArm);
                        GlStateManager.popMatrix();

                        event.setCanceled(true);
                    } else {
                        GlStateManager.pushMatrix();
                        AnimationBaseColossalWeapon.preformColossalItemRotations1stPerson(Minecraft.getMinecraft().player, partialTicks, cooldownStrength, arm);
                        renderer.renderItemSide(player, stack, isRightArm ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !isRightArm);
                        GlStateManager.popMatrix();

                        event.setCanceled(true);
                    }
                    }
            }
        }
    }
}
