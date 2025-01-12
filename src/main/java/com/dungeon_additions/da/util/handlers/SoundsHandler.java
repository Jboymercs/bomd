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
    //Soul Star Block
    public static SoundEvent SOUL_STAR_ACTIVATE;

    //Frozen Icicle
    public static SoundEvent ICICLE_DROP;

    //Night Lich
    public static SoundEvent LICH_PREPARE_MISSILE;
    public static SoundEvent LICH_SHOOT_MISSILE;
    public static SoundEvent LICH_PREPARE_FIREBALL;
    public static SoundEvent LICH_SHOOT_FIREBALL;
    public static SoundEvent LICH_MINION_RUNE;
    public static SoundEvent LICH_SUMMON_MINION;
    public static SoundEvent LICH_SHOOT_STAFF;
    public static SoundEvent LICH_STAFF_IMPACT;
    public static SoundEvent LICH_MAGIC_SWING;
    public static SoundEvent LICH_HURT;
    public static SoundEvent LICH_USE_SPEAR;

    public static SoundEvent LICH_PREPARE_SPELL;
    public static SoundEvent LICH_PREPARE_COMBO;
    public static SoundEvent LICH_RAGE_PREPARE;

    //Draugr
    public static SoundEvent DRAUGR_IDLE;
    public static SoundEvent DRAUGR_HURT;
    public static SoundEvent DRAUGR_DEATH;
    public static SoundEvent DRAUGR_STEP;

    //Draugr Elite
    public static SoundEvent DRAUGR_ELITE_IDLE;
    public static SoundEvent DRAUGR_ELITE_HURT;
    public static SoundEvent DRAUGR_ELITE_STEP;
    public static SoundEvent DRAUGR_ELITE_STOMP;
    public static SoundEvent DRAUGR_ELITE_SWING;
    public static SoundEvent DRAUGR_ELITE_SWING_IMPACT;
    public static SoundEvent DRAUGR_ELITE_WAR_CRY;
    public static SoundEvent DRAUGR_ELITE_DEATH;

    //Ice Spike
    public static SoundEvent ICE_SPIKE_SUMMON;

    //WYRK
    public static SoundEvent WYRK_IDLE;
    public static SoundEvent WYRK_HURT;
    public static SoundEvent WYRK_STEP;
    public static SoundEvent WYRK_CAST;

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
        SOUL_STAR_ACTIVATE = registerSound("soul_star.activate", "block");
        //
        ICICLE_DROP = registerSound("icicle.drop", "block");
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
        //
        LICH_MINION_RUNE = registerSound("night_lich.minion_rune", "entity");
        LICH_PREPARE_FIREBALL = registerSound("night_lich.prepare_fireball", "entity");
        LICH_SHOOT_FIREBALL = registerSound("night_lich.shoot_fireball", "entity");
        LICH_PREPARE_MISSILE = registerSound("night_lich.prepare_missile", "entity");
        LICH_SHOOT_MISSILE = registerSound("night_lich.shoot_missile", "entity");
        LICH_SUMMON_MINION = registerSound("night_lich.summon", "entity");
        LICH_SHOOT_STAFF = registerSound("night_lich.shoot_staff", "entity");
        LICH_STAFF_IMPACT = registerSound("night_lich.staff_impact", "entity");
        LICH_MAGIC_SWING = registerSound("night_lich.magic_swing", "entity");
        LICH_HURT = registerSound("night_lich.hurt", "entity");
        LICH_USE_SPEAR = registerSound("night_lich.spear", "entity");
        LICH_PREPARE_COMBO = registerSound("night_lich.prepare_combo", "entity");
        LICH_PREPARE_SPELL = registerSound("night_lich.prepare_spell", "entity");
        LICH_RAGE_PREPARE = registerSound("night_lich.rage_prepare", "entity");
        //
        DRAUGR_IDLE = registerSound("draugr.idle", "entity");
        DRAUGR_HURT = registerSound("draugr.hurt", "entity");
        DRAUGR_DEATH = registerSound("draugr.death", "entity");
        DRAUGR_STEP = registerSound("draugr.step", "entity");
        //
        DRAUGR_ELITE_HURT = registerSound("draugr_elite.hurt", "entity");
        DRAUGR_ELITE_IDLE = registerSound("draugr_elite.idle", "entity");
        DRAUGR_ELITE_STEP = registerSound("draugr_elite.step", "entity");
        DRAUGR_ELITE_STOMP = registerSound("draugr_elite.stomp", "entity");
        DRAUGR_ELITE_SWING = registerSound("draugr_elite.swing", "entity");
        DRAUGR_ELITE_SWING_IMPACT = registerSound("draugr_elite.swing_impact", "entity");
        DRAUGR_ELITE_WAR_CRY = registerSound("draugr_elite.war_cry", "entity");
        DRAUGR_ELITE_DEATH = registerSound("draugr_elite.death", "entity");
        //
        ICE_SPIKE_SUMMON = registerSound("ice_spike.summon", "entity");
        //
        WYRK_CAST = registerSound("wyrk.cast", "entity");
        WYRK_HURT = registerSound("wyrk.hurt", "entity");
        WYRK_IDLE = registerSound("wyrk.idle", "entity");
        WYRK_STEP = registerSound("wyrk.step", "entity");
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
