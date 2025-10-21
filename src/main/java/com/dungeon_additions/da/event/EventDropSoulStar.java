package com.dungeon_additions.da.event;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.frost_dungeon.EntityFrostBase;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.ProjectileSoul;
import com.dungeon_additions.da.entity.frost_dungeon.wyrk.EntityFriendWyrk;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber
public class EventDropSoulStar {
    /**
     * Tells Minecraft on entity death of a zombie, skeleton, stray
     */
    @SubscribeEvent
    public static void onEntityDrop(LivingDropsEvent event)
    {
        EntityLivingBase target = event.getEntityLiving();

        boolean hasFoundWyrk = false;
        //keep consitency with the undead only able to spawn souls to fuel Wyrks
        if(target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {

            List<EntityWyrk> nearbyWyrkEnemy = target.world.getEntitiesWithinAABB(EntityWyrk.class, target.getEntityBoundingBox().grow(20.0D), e -> !e.getIsInvulnerable());
            if(!nearbyWyrkEnemy.isEmpty()) {
                for(EntityWyrk wyrk : nearbyWyrkEnemy) {
                    if (!hasFoundWyrk) {
                        ProjectileSoul soul = new ProjectileSoul(target.world, target, 0, wyrk);
                        soul.setPosition(target.posX, target.posY + 1.5D, target.posZ);
                        soul.setTravelRange(40F);
                        target.world.spawnEntity(soul);
                        hasFoundWyrk = true;
                    }
                }
            }

            List<EntityFriendWyrk> nearbyWyrk = target.world.getEntitiesWithinAABB(EntityFriendWyrk.class, target.getEntityBoundingBox().grow(20.0D), e -> !e.getIsInvulnerable());
            if (!nearbyWyrk.isEmpty() && !target.world.isRemote) {
                for (EntityFriendWyrk wyrk : nearbyWyrk) {
                    if (!hasFoundWyrk) {
                        ProjectileSoul soul = new ProjectileSoul(target.world, target, 0, wyrk);
                        soul.setPosition(target.posX, target.posY + 1.5D, target.posZ);
                        soul.setTravelRange(40F);
                        target.world.spawnEntity(soul);
                        hasFoundWyrk = true;
                    }
                }
            }
        }

        if(event.getSource().getImmediateSource() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) event.getSource().getImmediateSource());

            if(ModUtils.getAdvancementCompletionAsList(player, ModConfig.soul_star_progress)) {
                if (target != null)
                {
                    if(ModConfig.soul_star_drops_everywhere) {

                        if (target instanceof EntityMob) {
                            if (target.world.rand.nextFloat() <= ((float) ModConfig.soul_star_drop_chance * 0.01) + (0.01 * event.getLootingLevel())) {
                                event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(ModItems.SOUL_STAR)));
                            }
                        }

                    } else {
                        //registers all undead under the category this item can drop under instead of creatures that extend the Mob Class
                        if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                            if (target.world.rand.nextFloat() <= ((float) ModConfig.soul_star_drop_chance * 0.01) + (0.01 * event.getLootingLevel())) {
                                event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(ModItems.SOUL_STAR)));
                            }
                        }
                    }
                }
            }
        } else {
            if (target != null)
            {
                if(ModConfig.soul_star_drops_everywhere) {

                    if (target instanceof EntityMob) {
                        if (target.world.rand.nextFloat() <= ((float) ModConfig.soul_star_drop_chance * 0.01) + (0.01 * event.getLootingLevel())) {
                            event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(ModItems.SOUL_STAR)));
                        }
                    }

                } else {
                    if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                        if (target.world.rand.nextFloat() <= ((float) ModConfig.soul_star_drop_chance * 0.01) + (0.01 * event.getLootingLevel())) {
                            event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(ModItems.SOUL_STAR)));
                        }
                    }
                }
            }
        }


    }

    //Gaelon Ingot Anvil List

    @SubscribeEvent
    public static void addGaelonRepairRecipes(AnvilUpdateEvent event) {

        ItemStack leftInput = event.getLeft();
        ItemStack rightInput = event.getRight();
        ItemStack output = event.getOutput();

        if(ModUtils.canItemBeRepaired(leftInput.getItem()) && rightInput.getItem() == ModItems.GAELON_INGOT) {
            NBTTagCompound tags = leftInput.getTagCompound();
            output = new ItemStack(leftInput.getItem());
            int itemDamage = (int) ModUtils.getPercentageOf(leftInput.getMaxDamage(), leftInput.getItemDamage());
            int calculatedDamage = (int) ModUtils.calculateValueWithPrecentage(output.getMaxDamage(), (itemDamage - 30));
            output.setItemDamage((calculatedDamage));
            output.setTagCompound(tags);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(5);
        }
    }

}
