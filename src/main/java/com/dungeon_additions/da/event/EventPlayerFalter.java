package com.dungeon_additions.da.event;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.item.DAPlayerAnimationMethods;
import com.dungeon_additions.da.capabilities.AnimationCapabilityHelper;
import com.dungeon_additions.da.capabilities.CapabilityItemAnimations;
import com.dungeon_additions.da.capabilities.CapabilityPlayerFalter;
import com.dungeon_additions.da.capabilities.CapabilityPlayerSwing;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.PotionTrinketConfig;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.items.tools.ItemBloodySwordSpear;
import com.dungeon_additions.da.items.tools.ItemImperialHalberd;
import com.dungeon_additions.da.items.tools.ItemSwordSpear;
import com.dungeon_additions.da.items.tools.ToolSword;
import com.dungeon_additions.da.packets.PacketFalterCapability;
import com.dungeon_additions.da.packets.PacketPlayerSwingCapability;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.PlayerCustomSwingUtils;
import com.dungeon_additions.da.util.PlayerFalterUtils;
import com.dungeon_additions.da.util.player.PlayerMeleeAttack;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.lwjgl.Sys;

@Mod.EventBusSubscriber
public class EventPlayerFalter {

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        { event.addCapability(CapabilityPlayerFalter.ID, new CapabilityPlayerFalter.Provider(new CapabilityPlayerFalter.DAPlayerFalterMethods(), CapabilityPlayerFalter.PLAYER_FALTER_CAP, null));
          event.addCapability(CapabilityItemAnimations.ID, new CapabilityItemAnimations.Provider(new CapabilityItemAnimations.AnimationMethods(), CapabilityItemAnimations.ANIM_CAP, null));
            event.addCapability(CapabilityPlayerSwing.ID, new CapabilityPlayerSwing.Provider(new CapabilityPlayerSwing.DAPlayerSwingMethods(), CapabilityPlayerSwing.PLAYER_SWING_CAP, null));}
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        CapabilityPlayerFalter.IPlayerFalterCapability capFog = PlayerFalterUtils.getCapability(event.player);
        CapabilityPlayerSwing.IPlayerSwingCapability swingProg = PlayerCustomSwingUtils.getCapability(event.player);
        if (capFog == null) return;

        Main.network.sendTo(new PacketFalterCapability(event.player.getEntityId(), true), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public static void onPlayerLoggedInSwing(PlayerEvent.PlayerLoggedInEvent event)
    {
        CapabilityPlayerSwing.IPlayerSwingCapability swingProg = PlayerCustomSwingUtils.getCapability(event.player);
        if (swingProg == null) return;
        //We will want to ensure that on logging in, there is no delayed swings happening
       // Main.network.sendToAllTracking(new PacketPlayerSwingCapability(event.player.getEntityId(), true, 0), new NetworkRegistry.TargetPoint(event.player.world.provider.getDimension(), event.player.posX, event.player.posY, event.player.posZ, 0.0D));
    }

    //Handler for player faltering, will work with several different functions
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;

        EntityPlayer player = event.player;
        if(!player.world.isRemote) {
            //armor stats will boost a players falter cap
            float player_falter_bonus = (float) ((player.getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue() * PotionTrinketConfig.armor_additive_falter_resistance) + (player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue() * PotionTrinketConfig.armor_additive_falter_resistance_t));
            ItemStack stack = ModUtils.findTrinket(new ItemStack(ModItems.GOAT_TRINKET), player);
            //Raises the players faltering resistance
            float goat_trinket = !stack.isEmpty() ? (float) PotionTrinketConfig.goat_tenacity_resistance : 0F;
            //cap that when exceeded will cause the player to be faltered
            float falter_cap = (float) (PotionTrinketConfig.player_default_falter_resistance + player_falter_bonus + goat_trinket);

            //staggers the player
            if(PlayerFalterUtils.getPlayerFalterProgress(player) > falter_cap) {
                //we want the time to scale with the players current falter. So if a enemy causes a lot of stun damage, it will stun you greatly.
                int time = (int) ((PlayerFalterUtils.getPlayerFalterProgress(player) * 20) + 50);
                player.addPotionEffect(new PotionEffect(ModPotions.FALTERED, time, 0, false, false));
                if(!stack.isEmpty()) {
                    stack.damageItem(1, player);
                }
                //resets it
                PlayerFalterUtils.setPlayerGreedProgress(player, 0);
            }
        }

        /* Only update every second. */
        if (player.ticksExisted % 20 != 0) return;

        if (!player.world.isRemote)
        {
            //every end of the second the player will lose falter stats. need to balance this perfectly
            //we will have trinkets or potions that can make this value decrease faster
            if(PlayerFalterUtils.getPlayerFalterProgress(player) > 0.01) {
                PlayerFalterUtils.setPlayerGreedProgress(player, PlayerFalterUtils.getPlayerFalterProgress(player) - 0.07F);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerSwingCustom(TickEvent.PlayerTickEvent event) {
        if(PlayerCustomSwingUtils.getCapability(event.player) == null) return;
        if(event.player.world.isRemote) return;

        EntityPlayer player = event.player;

        if(!(player.getHeldItemMainhand().getItem() instanceof ToolSword)) {
            PlayerCustomSwingUtils.setPlayerSwingCancelled(player, true);
            PlayerCustomSwingUtils.setPlayerSwingProgress(player, 0);

        }else if(!PlayerCustomSwingUtils.getPlayerSwingCancelled(player)) {
            ToolSword weapon = ((ToolSword) player.getHeldItemMainhand().getItem());
            int ticksAhead = player.ticksExisted + weapon.getWeaponDelay(player.getHeldItemMainhand());
            //sets the time for the attack and cancels this statement
            PlayerCustomSwingUtils.setPlayerSwingProgress(player, ticksAhead);
            PlayerCustomSwingUtils.setPlayerSwingCancelled(player, true);

        } else if (PlayerCustomSwingUtils.getPlayerSwingProgress(player) != 0) {
            ToolSword weapon = ((ToolSword) player.getHeldItemMainhand().getItem());
            //when the time is met
            if (PlayerCustomSwingUtils.getPlayerSwingProgress(player) < player.ticksExisted) {
                //when the time is reached, a line will be drawn at the players look
                Entity closestEntity = doesPlayerSeeEntity(player.world, player, weapon.weaponReach);
                if (closestEntity != null) {


                    PlayerMeleeAttack.attackTargetEntityWithCurrentItemCustom(player, closestEntity);

                }
                //sets to 0 even if it misses
                PlayerCustomSwingUtils.setPlayerSwingProgress(player, 0);
            }
        }

        if(!PlayerCustomSwingUtils.getPlayerSwingCancelled(player) || PlayerCustomSwingUtils.getPlayerSwingProgress(player) != 0) {
            if ((DAPlayerAnimationMethods.getWeaponType(player) == 5 || player.getHeldItemMainhand().getItem() instanceof ItemSwordSpear ||
                    player.getHeldItemMainhand().getItem() instanceof ItemBloodySwordSpear || player.getHeldItemMainhand().getItem() instanceof ItemImperialHalberd)
                    && player.canBePushed()) {
                player.motionX = 0;
                player.motionZ = 0;
                player.velocityChanged = true;
            }
        }
    }


    //Handler for delayed swings with UDA's weapons


    /**
     * Used for determinding player reach and cone
     */
    private static Entity doesPlayerSeeEntity(World world, EntityPlayer player, float itemReach) {
        if(!world.isRemote && player != null) {
            Vec3d lazerEnd = player.getPositionEyes(1).add(player.getLookVec().scale(itemReach));

            // Ray trace both blocks and entities
            RayTraceResult raytraceresult = world.rayTraceBlocks(player.getPositionEyes(1), lazerEnd, false, true, false);
            if (raytraceresult != null) {
                // If we hit a block, make sure that any collisions with entities are detected up to the hit block
                lazerEnd = raytraceresult.hitVec;
            }

            Entity closestEntity = null;
            for (Entity entity : ModUtils.findEntitiesInLine(player.getPositionEyes(1), lazerEnd, world, player)) {
                if (entity.canBeCollidedWith() && (closestEntity == null || entity.getDistanceSq(player) < closestEntity.getDistanceSq(player))) {
                    closestEntity = entity;
                }
            }

            if (closestEntity != null) {
                if (closestEntity instanceof IEntityMultiPart) {
                    if(closestEntity.getParts() != null) {
                        MultiPartEntityPart closestPart = null;
                        for (Entity entity : closestEntity.getParts()) {
                            RayTraceResult result = entity.getEntityBoundingBox().calculateIntercept(player.getPositionEyes(1), lazerEnd);
                            if (result != null) {
                                if (entity instanceof MultiPartEntityPart && (closestPart == null || entity.getDistanceSq(player) < closestPart.getDistanceSq(player))) {
                                    return entity;
                                }
                            }
                        }
                        if (closestPart != null) {
                            lazerEnd = closestPart.getEntityBoundingBox().calculateIntercept(player.getPositionEyes(1), lazerEnd).hitVec;
                            return closestEntity;
                        }
                    }
                } else {
                    lazerEnd = closestEntity.getEntityBoundingBox().calculateIntercept(player.getPositionEyes(1), lazerEnd).hitVec;
                    return closestEntity;
                }
            }
        }
        return null;
    }

}
