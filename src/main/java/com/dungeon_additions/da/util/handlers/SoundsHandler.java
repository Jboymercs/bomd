package com.dungeon_additions.da.util.handlers;

import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundsHandler {


    public static SoundEvent VOID_SPIKE_SHOOT;
    public static SoundEvent APPEARING_WAVE;

    public static SoundEvent SPORE_PREPARE;

    public static SoundEvent BLOSSOM_BURROW;
    public static SoundEvent SPORE_IMPACT;

    public static SoundEvent BLOSSOM_HURT;
    public static SoundEvent BLOSSOM_PETAL_WAVE;
    public static SoundEvent BLOSSOM_DEATH;
    public static SoundEvent BLOSSOM_SUMMON_MINION;

    //Burning Knight of Flame
    public static SoundEvent B_KNIGHT_STEP;
    public static SoundEvent B_KNIGHT_HURT;
    public static SoundEvent B_KNIGHT_SLING;
    public static SoundEvent B_KNIGHT_FLAME_SLING;
    public static SoundEvent B_KNIGHT_FLAME_SLING_FAST;
    public static SoundEvent B_KNIGHT_MOVEMENT_ARMOR;
    public static SoundEvent B_KNIGHT_PREPARE;
    public static SoundEvent B_KNIGHT_STOMP;
    public static SoundEvent B_KNIGHT_DASH;

    //Rot Knight
    public static SoundEvent ROT_KNIGHT_HURT;
    public static SoundEvent ROT_SPIKE_SHOOT;
    public static SoundEvent ROT_SELF_AOE;
    public static SoundEvent ROT_KNIGHT_WALK;
    public static SoundEvent ROT_KNIGHT_ARM_BREAK;
    public static SoundEvent ROT_KNIGHT_DEATH;
    public static SoundEvent ROT_KNIGHT_CAST;

    public static SoundEvent MOSS_BREAK;
    public static SoundEvent MOSS_STEP;
    public static SoundEvent MOSS_PLACE;
    public static SoundEvent MOSS_HIT;

    public static SoundEvent AZA_LEAVES_PLACE;
    public static SoundEvent AZA_LEAVES_BREAK;
    public static SoundEvent AZA_LEAVES_HIT;
    public static SoundEvent AZA_LEAVES_STEP;

    //PetroGloom
    public static SoundEvent GLOOM_STEP;
    public static SoundEvent GLOOM_BREAK;
    public static SoundEvent GLOOM_PLACE;
    public static void registerSounds() {
        //
        MOSS_BREAK = registerSound("moss.break", "block");
        MOSS_STEP = registerSound("moss.step", "block");
        MOSS_HIT = registerSound("moss.hit", "block");
        MOSS_PLACE = registerSound("moss.place", "block");
        //
        AZA_LEAVES_BREAK = registerSound("leaves.break", "block");
        AZA_LEAVES_HIT = registerSound("leaves.hit", "block");
        AZA_LEAVES_STEP = registerSound("leaves.step", "block");
        AZA_LEAVES_PLACE = registerSound("leaves.place", "block");
        //
        GLOOM_BREAK = registerSound("gloom.break", "block");
        GLOOM_PLACE = registerSound("gloom.place", "block");
        GLOOM_STEP = registerSound("gloom.step", "block");
        //
        VOID_SPIKE_SHOOT = registerSound("spike.shoot", "entity");
        APPEARING_WAVE = registerSound("spike.wave", "entity");
        BLOSSOM_BURROW = registerSound("spike.burrow", "entity");
        SPORE_PREPARE = registerSound("spike.prepare", "entity");
        SPORE_IMPACT = registerSound("spike.impact", "entity");
        BLOSSOM_HURT = registerSound("spike.hurt", "entity");
        BLOSSOM_PETAL_WAVE = registerSound("spike.petal_blade", "entity");
        BLOSSOM_DEATH = registerSound("spike.death", "entity");
        BLOSSOM_SUMMON_MINION = registerSound("spike.summon", "entity");
        //
        B_KNIGHT_STEP = registerSound("b_knight.step", "entity");
        B_KNIGHT_MOVEMENT_ARMOR = registerSound("b_knight.movement", "entity");
        B_KNIGHT_PREPARE = registerSound("b_knight.prepare", "entity");
        B_KNIGHT_SLING = registerSound("b_knight.sling", "entity");
        B_KNIGHT_HURT = registerSound("b_knight.hurt", "entity");
        B_KNIGHT_FLAME_SLING = registerSound("b_knight.flame_sling", "entity");
        B_KNIGHT_FLAME_SLING_FAST =registerSound("b_knight.quick_sling", "entity");
        B_KNIGHT_STOMP = registerSound("b_knight.stomp", "entity");
        B_KNIGHT_DASH = registerSound("b_knight.dash", "entity");
        //
        ROT_SPIKE_SHOOT = registerSound("rot_spike.shoot", "entity");
        ROT_KNIGHT_HURT = registerSound("rot_knight.hurt", "entity");
        ROT_SELF_AOE = registerSound("rot_knight.self_aoe", "entity");
        ROT_KNIGHT_WALK = registerSound("rot_knight.step", "entity");
        ROT_KNIGHT_ARM_BREAK = registerSound("rot_knight.break_arm", "entity");
        ROT_KNIGHT_DEATH = registerSound("rot_knight.death", "entity");
        ROT_KNIGHT_CAST = registerSound("rot_knight.cast", "entity");
    }


    private static SoundEvent registerSound(String name, String category) {
        String fullName = category + "." + name;
        ResourceLocation location = new ResourceLocation(ModReference.MOD_ID, fullName);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(fullName);
        ForgeRegistries.SOUND_EVENTS.register(event);

        return event;
    }
}
