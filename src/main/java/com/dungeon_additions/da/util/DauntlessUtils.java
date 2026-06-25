package com.dungeon_additions.da.util;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.init.ModPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class DauntlessUtils {

    public static void handleAreaImpact(float radius, Function<Entity, Float> maxDamage, EntityDauntless source, Vec3d pos, DamageSource damageSource,
                                        float knockbackFactor, int fireFactor, boolean damageDecay, Potion effectIn, int amflifier, int time, float falterTime) {
        if (source == null) {
            return;
        }

        List<Entity> list = source.world.getEntitiesWithinAABBExcludingEntity(source, new AxisAlignedBB(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z).grow(radius));

        Predicate<Entity> isInstance = i -> i instanceof EntityLivingBase || i instanceof MultiPartEntityPart || i.canBeCollidedWith();
        double radiusSq = Math.pow(radius, 2);

        list.stream().filter(isInstance).forEach((entity) -> {

            // Get the hitbox size of the entity because otherwise explosions are less
            // effective against larger mobs
            double avgEntitySize = entity.getEntityBoundingBox().getAverageEdgeLength() * 0.75;

            // Choose the closest distance from the center or the head to encourage
            // headshots
            double distance = Math.min(Math.min(getCenter(entity.getEntityBoundingBox()).distanceTo(pos),
                            entity.getPositionVector().add(ModUtils.yVec(entity.getEyeHeight())).distanceTo(pos)),
                    entity.getPositionVector().distanceTo(pos));

            // Subtracting the average size makes it so that the full damage can be dealt
            // with a direct hit
            double adjustedDistance = Math.max(distance - avgEntitySize, 0);
            double adjustedDistanceSq = Math.pow(adjustedDistance, 2);
            double damageFactor = damageDecay ? Math.max(0, Math.min(1, (radiusSq - adjustedDistanceSq) / radiusSq)) : 1;

            // Damage decays by the square to make missed impacts less powerful
            double damageFactorSq = Math.pow(damageFactor, 2);
            double damage = maxDamage.apply(entity) * damageFactorSq;
            if (damage > 0 && adjustedDistanceSq < radiusSq) {
                entity.setFire((int) (fireFactor * damageFactorSq));
                if(entity.attackEntityFrom(damageSource, (float) damage)) {
                    double entitySizeFactor = avgEntitySize == 0 ? 1 : Math.max(0.5, Math.min(1, 1 / avgEntitySize));
                    double entitySizeFactorSq = Math.pow(entitySizeFactor, 2);
                    source.setSwordCharge(source.getSwordCharge() + 1);
                    //adds potion effect
                    if(entity instanceof EntityLivingBase) {
                        ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(effectIn, time, amflifier, false, true));
                        float falter_bonus = source.sword_charge_3 > 0 ? (float) MobConfig.dauntless_falter_bonus : 1;
                        ModUtils.addFalterTooEnemies(((EntityLivingBase) entity), falterTime * falter_bonus, (int) falterTime * 30);
                        if(source.sword_charge_one > 0) {
                            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(ModPotions.DEGRADATION, MobConfig.dauntless_sword_degradation * 20, 0, false, false));
                        }
                        if(source.sword_charge_two > 0) {
                            source.heal((float) ((source.getMaxHealth()) * MobConfig.dauntless_sword_heal_amount));
                        }
                    }

                    // Velocity depends on the entity's size and the damage dealt squared
                    Vec3d velocity = getCenter(entity.getEntityBoundingBox()).subtract(pos).normalize().scale(damageFactorSq).scale(knockbackFactor).scale(entitySizeFactorSq);
                    entity.addVelocity(velocity.x, velocity.y, velocity.z);
                }
            }
        });
    }

    public static void handleAreaImpact(float radius, Function<Entity, Float> maxDamage, EntityDauntless source, Vec3d pos, DamageSource damageSource,
                                        float knockbackFactor, int fireFactor, boolean damageDecay) {
        if (source == null) {
            return;
        }

        List<Entity> list = source.world.getEntitiesWithinAABBExcludingEntity(source, new AxisAlignedBB(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z).grow(radius));

        Predicate<Entity> isInstance = i -> i instanceof EntityLivingBase || i instanceof MultiPartEntityPart || i.canBeCollidedWith();
        double radiusSq = Math.pow(radius, 2);
        //Hitbox testing
        // Main.proxy.spawnParticle(29, pos.x, pos.y, pos.z, 0, 0, 0, (int) radius);
        list.stream().filter(isInstance).forEach((entity) -> {

            // Get the hitbox size of the entity because otherwise explosions are less
            // effective against larger mobs
            double avgEntitySize = entity.getEntityBoundingBox().getAverageEdgeLength() * 0.75;

            // Choose the closest distance from the center or the head to encourage
            // headshots
            double distance = Math.min(Math.min(getCenter(entity.getEntityBoundingBox()).distanceTo(pos),
                            entity.getPositionVector().add(ModUtils.yVec(entity.getEyeHeight())).distanceTo(pos)),
                    entity.getPositionVector().distanceTo(pos));

            // Subtracting the average size makes it so that the full damage can be dealt
            // with a direct hit
            double adjustedDistance = Math.max(distance - avgEntitySize, 0);
            double adjustedDistanceSq = Math.pow(adjustedDistance, 2);
            double damageFactor = damageDecay ? Math.max(0, Math.min(1, (radiusSq - adjustedDistanceSq) / radiusSq)) : 1;
            // Damage decays by the square to make missed impacts less powerful
            double damageFactorSq = Math.pow(damageFactor, 2);
            double damage = maxDamage.apply(entity) * damageFactorSq;
            if (damage > 0 && adjustedDistanceSq < radiusSq) {
                entity.setFire((int) (fireFactor * damageFactorSq));
                if(entity.attackEntityFrom(damageSource, (float) damage)) {
                    double entitySizeFactor = avgEntitySize == 0 ? 1 : Math.max(0.5, Math.min(1, 1 / avgEntitySize));
                    double entitySizeFactorSq = Math.pow(entitySizeFactor, 2);
                    source.setSwordCharge(source.getSwordCharge() + 1);
                    if(entity instanceof EntityLivingBase) {
                        ModUtils.addFalterTooEnemies(((EntityLivingBase) entity), 0.1F, 5);
                        if(source.sword_charge_one > 0) {
                            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(ModPotions.DEGRADATION, MobConfig.dauntless_sword_degradation * 20, 0, false, false));
                        }
                        if(source.sword_charge_two > 0) {
                            source.heal((float) ((source.getMaxHealth()) * MobConfig.dauntless_sword_heal_amount));
                        }
                    }
                    // Velocity depends on the entity's size and the damage dealt squared
                    Vec3d velocity = getCenter(entity.getEntityBoundingBox()).subtract(pos).normalize().scale(damageFactorSq).scale(knockbackFactor).scale(entitySizeFactorSq);
                    entity.addVelocity(velocity.x, velocity.y, velocity.z);
                }
            }
        });
    }

    public static void handleAreaImpact(float radius, Function<Entity, Float> maxDamage, EntityDauntless source, Vec3d pos, DamageSource damageSource,
                                        float knockbackFactor, int fireFactor, boolean damageDecay, float falterValue) {
        if (source == null) {
            return;
        }

        List<Entity> list = source.world.getEntitiesWithinAABBExcludingEntity(source, new AxisAlignedBB(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z).grow(radius));

        Predicate<Entity> isInstance = i -> i instanceof EntityLivingBase || i instanceof MultiPartEntityPart || i.canBeCollidedWith();
        double radiusSq = Math.pow(radius, 2);
        //Hitbox testing
        // Main.proxy.spawnParticle(29, pos.x, pos.y, pos.z, 0, 0, 0, (int) radius);
        list.stream().filter(isInstance).forEach((entity) -> {

            // Get the hitbox size of the entity because otherwise explosions are less
            // effective against larger mobs
            double avgEntitySize = entity.getEntityBoundingBox().getAverageEdgeLength() * 0.75;

            // Choose the closest distance from the center or the head to encourage
            // headshots
            double distance = Math.min(Math.min(getCenter(entity.getEntityBoundingBox()).distanceTo(pos),
                            entity.getPositionVector().add(ModUtils.yVec(entity.getEyeHeight())).distanceTo(pos)),
                    entity.getPositionVector().distanceTo(pos));

            // Subtracting the average size makes it so that the full damage can be dealt
            // with a direct hit
            double adjustedDistance = Math.max(distance - avgEntitySize, 0);
            double adjustedDistanceSq = Math.pow(adjustedDistance, 2);
            double damageFactor = damageDecay ? Math.max(0, Math.min(1, (radiusSq - adjustedDistanceSq) / radiusSq)) : 1;
            // Damage decays by the square to make missed impacts less powerful
            double damageFactorSq = Math.pow(damageFactor, 2);
            double damage = maxDamage.apply(entity) * damageFactorSq;
            if (damage > 0 && adjustedDistanceSq < radiusSq) {
                entity.setFire((int) (fireFactor * damageFactorSq));
                if(entity.attackEntityFrom(damageSource, (float) damage)) {
                    double entitySizeFactor = avgEntitySize == 0 ? 1 : Math.max(0.5, Math.min(1, 1 / avgEntitySize));
                    double entitySizeFactorSq = Math.pow(entitySizeFactor, 2);
                    source.setSwordCharge(source.getSwordCharge() + 1);
                    if(entity instanceof EntityLivingBase) {
                        float falter_bonus = source.sword_charge_3 > 0 ? (float) MobConfig.dauntless_falter_bonus : 1;
                        ModUtils.addFalterTooEnemies(((EntityLivingBase) entity), falterValue * falter_bonus, (int) (falterValue * 30));
                        if(source.sword_charge_one > 0) {
                            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(ModPotions.DEGRADATION, MobConfig.dauntless_sword_degradation * 20, 0, false, false));
                        }
                        if(source.sword_charge_two > 0) {
                            source.heal((float) ((source.getMaxHealth()) * MobConfig.dauntless_sword_heal_amount));
                        }
                    }
                    // Velocity depends on the entity's size and the damage dealt squared
                    Vec3d velocity = getCenter(entity.getEntityBoundingBox()).subtract(pos).normalize().scale(damageFactorSq).scale(knockbackFactor).scale(entitySizeFactorSq);
                    entity.addVelocity(velocity.x, velocity.y, velocity.z);
                }
            }
        });
    }

    private static Vec3d getCenter(AxisAlignedBB box) {
        return new Vec3d(box.minX + (box.maxX - box.minX) * 0.5D, box.minY + (box.maxY - box.minY) * 0.5D, box.minZ + (box.maxZ - box.minZ) * 0.5D);
    }
}
