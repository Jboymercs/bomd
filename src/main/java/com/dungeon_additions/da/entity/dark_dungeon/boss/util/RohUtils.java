package com.dungeon_additions.da.entity.dark_dungeon.boss.util;

import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class RohUtils {

    public static void handleAreaImpactDarkdrift(float radius, Function<Entity, Float> maxDamage, Entity source, Vec3d pos, DamageSource damageSource,
                                        float knockbackFactor, int fireFactor, boolean damageDecay, float falterValue, int limit, int time) {
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

                    if(entity instanceof EntityLivingBase) {
                        ModUtils.addFalterTooEnemies(((EntityLivingBase) entity), falterValue, (int) (falterValue * 30));
                        if(!entity.world.isRemote) {
                        addDarkdriftTooEntity((EntityLivingBase) entity, limit, time);
                        }
                    }
                    // Velocity depends on the entity's size and the damage dealt squared
                    Vec3d velocity = getCenter(entity.getEntityBoundingBox()).subtract(pos).normalize().scale(damageFactorSq).scale(knockbackFactor).scale(entitySizeFactorSq);
                    entity.addVelocity(velocity.x, velocity.y, velocity.z);
                }
            }
        });
    }

    //This will add amplification if the limit from the selected attack is not exceed. Otherwise it will maintain the same level or initially add level 1.
    private static void addDarkdriftTooEntity(EntityLivingBase base, int limit, int time) {
        if(base.isPotionActive(ModPotions.DARKDRIFT)) {
            if(Objects.requireNonNull(base.getActivePotionEffect(ModPotions.DARKDRIFT)).getAmplifier() < limit) {
                base.addPotionEffect(new PotionEffect(ModPotions.DARKDRIFT, time, base.getActivePotionEffect(ModPotions.DARKDRIFT).getAmplifier() + 1));
            } else {
                base.addPotionEffect(new PotionEffect(ModPotions.DARKDRIFT, time, base.getActivePotionEffect(ModPotions.DARKDRIFT).getAmplifier()));
            }
        } else {
            base.addPotionEffect(new PotionEffect(ModPotions.DARKDRIFT, time, 1));
        }
    }


    private static Vec3d getCenter(AxisAlignedBB box) {
        return new Vec3d(box.minX + (box.maxX - box.minX) * 0.5D, box.minY + (box.maxY - box.minY) * 0.5D, box.minZ + (box.maxZ - box.minZ) * 0.5D);
    }


}
