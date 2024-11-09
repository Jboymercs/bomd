package com.dungeon_additions.da.util.handlers;

import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModParticle;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.particle.EffectParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class ParticleManager {


    public static void spawnColoredSmoke(World worldIn, Vec3d pos, Vec3d baseColor, Vec3d vel) {
        Particle particle = new ParticleSmokeNormal.Factory().createParticle(0, worldIn, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);

        baseColor = ModColors.variateColor(baseColor, 0.2f);
        particle.setRBGColorF((float) baseColor.x, (float) baseColor.y, (float) baseColor.z);

        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static void spawnDust(World worldIn, Vec3d pos, Vec3d baseColor, Vec3d motion, int age) {
        ModParticle particle = new ModParticle(worldIn, pos, motion, 3, age, true);
        particle.setParticleTextureRange(64, 14, 2);
        spawnParticleWithColor(particle, baseColor);
    }

    private static void spawnParticleWithColor(Particle particle, Vec3d baseColor) {
        baseColor = ModColors.variateColor(baseColor, 0.2f);
        particle.setRBGColorF((float) baseColor.x, (float) baseColor.y, (float) baseColor.z);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static void spawnEffect(World world, Vec3d pos, Vec3d baseColor) {
        Particle particle = new EffectParticle.Factory().createParticle(0, world, pos.x, pos.y, pos.z, 0, 0, 0);
        baseColor = ModColors.variateColor(baseColor, 0.2f);
        particle.setRBGColorF((float) baseColor.x, (float) baseColor.y, (float) baseColor.z);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static void spawnColoredExplosion(World worldIn, Vec3d pos, Vec3d baseColor) {
        Particle particle = new ParticleExplosionLarge.Factory().createParticle(0, worldIn, pos.x, pos.y, pos.z, ModRand.getFloat(0.05f), 0.0f, ModRand.getFloat(0.05f));
        baseColor = ModColors.variateColor(baseColor, 0.2f);
        particle.setRBGColorF((float) baseColor.x, (float) baseColor.y, (float) baseColor.z);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static void spawnBrightFlames(World worldIn, Vec3d pos, Vec3d vel) {
        Particle particle = new ParticleFlame.Factory().createParticle(0, worldIn, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, ModRand.range(10, 15));

        float f = ModRand.getFloat(0.1f);
        particle.setRBGColorF(0.325F, 0.902F, 0.886F);

        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static void spawnPollen(World world, Vec3d pos, Vec3d baseColor, Vec3d motion, int age) {
        ModParticle particle = new ModParticle(world, pos, motion, 2, age, true);
        particle.setParticleTextureRange(32, 8, 2);
        spawnParticleWithColor(particle, baseColor);
    }

    public static void spawnBreak(World world, Vec3d pos, Item item, Vec3d vel) {
        Particle particle = new ParticleBreaking.Factory().createParticle(0, world, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, Item.getIdFromItem(item));
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

}
