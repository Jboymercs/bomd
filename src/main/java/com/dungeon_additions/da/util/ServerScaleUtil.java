package com.dungeon_additions.da.util;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.List;

public class ServerScaleUtil {
    //This sets the Current Health in accordance with how many players are current
    //Each Player after 1 adding 0.5 * currentHealth
    public static float changeHealthAccordingToPlayers(EntityLivingBase actor, World world) {

        int playersNearby = 0;

        if (!world.isRemote) {
            List<EntityPlayer> nearbyPlayers = actor.world.getEntitiesWithinAABB(EntityPlayer.class, actor.getEntityBoundingBox().grow(60D), e -> !e.getIsInvulnerable());

            if (!nearbyPlayers.isEmpty()) {
                for (EntityPlayer playerCap : nearbyPlayers) {
                    if (!playerCap.isCreative() && !playerCap.isSpectator()) {
                        playersNearby++;
                    }
                }
            }

            if (playersNearby > 1) {
                double additiveHealth = actor.getHealth() * ((playersNearby * ModConfig.boss_health_scaling) - ModConfig.boss_health_scaling); //We have to subtract atleats 0.5D due to the player count 1 not adding anything
                //Returns the additive amount and this is what sets the health
                return (float) (actor.getHealth() + additiveHealth);
            } else {
                return actor.getHealth();
            }
        }
       return actor.getHealth();
    }

    //This sets the Max Health of an Entity in accordance with how many players are nearby
    //Why we need to adjust the max health is to show little to no difference in health bar when a player joins the fight

    public static double setMaxHealthWithPlayers(EntityLivingBase actor, World world) {

        int playersNearby = 0;
        if(!world.isRemote) {
            List<EntityPlayer> nearbyPlayers = actor.world.getEntitiesWithinAABB(EntityPlayer.class, actor.getEntityBoundingBox().grow(60D), e -> !e.getIsInvulnerable());

            if (!nearbyPlayers.isEmpty()) {
                for (EntityPlayer playerCap : nearbyPlayers) {
                    if (!playerCap.isCreative() && !playerCap.isSpectator()) {
                        playersNearby++;
                    }
                }
            }

            if (playersNearby > 1) {
                double additiveHealth = actor.getMaxHealth() * ((playersNearby * ModConfig.boss_health_scaling) - ModConfig.boss_health_scaling); //We have to subtract atleats 0.5D due to the player count 1 not adding anything
                //Returns the additive amount and this is what sets the health

                return (float) (actor.getMaxHealth() + additiveHealth);
            } else {
                return actor.getMaxHealth();
            }
        }
        return actor.getMaxHealth();
    }

    //This scales the Attack Damage with how many players are near on startUp
    public static double scaleAttackDamageInAccordanceWithPlayers(EntityLivingBase actor, World world) {
        double currentAttackDamage = actor.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int playersNearby = 0;
        if(!world.isRemote) {
            List<EntityPlayer> nearbyPlayers = actor.world.getEntitiesWithinAABB(EntityPlayer.class, actor.getEntityBoundingBox().grow(60D), e -> !e.getIsInvulnerable());
            if (!nearbyPlayers.isEmpty()) {
                for (EntityPlayer playerCap : nearbyPlayers) {
                    if (!playerCap.isCreative() && !playerCap.isSpectator()) {
                        playersNearby++;
                    }
                }
            }

            if (playersNearby > 1) {
                double additiveAttackDamage = currentAttackDamage * ((playersNearby * ModConfig.boss_attack_damage_scaling) - ModConfig.boss_attack_damage_scaling);

                return currentAttackDamage + additiveAttackDamage;
            } else {
                return currentAttackDamage;
            }
        }
        return currentAttackDamage;
    }

    //This functions allows us to switch between targets and allow everyone to get a bit of the fun

    public static EntityLivingBase targetSwitcher(EntityAbstractBase actor, World world) {
        EntityLivingBase currentTarget = actor.getAttackTarget();
        if(currentTarget != null && !world.isRemote && currentTarget instanceof EntityPlayer) {
            double range = actor.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
            List<EntityPlayer> nearbySimilarTargets = actor.world.getEntitiesWithinAABB(EntityPlayer.class, actor.getEntityBoundingBox().grow(range), e-> !e.getIsInvulnerable());

            int currentPlayerCountCanSee = 0;
            if(!nearbySimilarTargets.isEmpty()) {
                //Firts gets a count
                for(EntityPlayer baseToo : nearbySimilarTargets) {
                    if(!baseToo.isSpectator() && !baseToo.isCreative()) {
                        currentPlayerCountCanSee++;
                    }
                }
                //After checking how many players it runs a test to see if there is more than one this entity can see and then selects them as it's new target
                for(EntityPlayer baseFrom : nearbySimilarTargets) {
                    if(currentPlayerCountCanSee > 1 && actor.getEntitySenses().canSee(baseFrom) && !baseFrom.isCreative() && !baseFrom.isSpectator()) {
                        return baseFrom;
                    } else {
                        //If there is only one, return current target
                        return currentTarget;
                    }

                }
            }
        }

        return currentTarget;
    }
}
