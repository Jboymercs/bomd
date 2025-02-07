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

    //Ancient Wyrk
    public static SoundEvent BIG_WYRK_IDLE;
    public static SoundEvent BIG_WYRK_STEP;
    public static SoundEvent BIG_WYRK_HURT;
    public static SoundEvent BIG_WYRK_BARRAGE;
    public static SoundEvent BIG_WYRK_ROLL;
    public static SoundEvent BIG_WYRK_RISE;
    public static SoundEvent BIG_WYRK_STRIKE;
    public static SoundEvent BIG_WYRK_DROP;
    public static SoundEvent BIG_WYRK_LAZER;
    public static SoundEvent BIG_WYRK_SUMMON_FOOT;
    public static SoundEvent BIG_WYRK_FOOT_IMPACT;
    public static SoundEvent BIG_WYRK_DEFLECT;
    public static SoundEvent BIG_WYRK_SUMMON;

    public static SoundEvent WYRK_STAFF_SHOOT;
    public static SoundEvent WYRK_STAFF_CHARGE;
    public static SoundEvent WYRK_STAFF_LAZER;

    //Imperial Knights
    public static SoundEvent SKY_LIGHTNING_CAST;
    public static SoundEvent IMPERIAL_COUNTER;
    public static SoundEvent IMPERIAL_HALBERD_CAST;
    public static SoundEvent IMPERIAL_START_MAGIC;
    public static SoundEvent IMPERIAL_HURT;
    public static SoundEvent IMPERIAL_IDLE;
    public static SoundEvent IMPERIAL_STORM_STOMP;
    public static SoundEvent IMPERIAL_STORM_LONG;
    public static SoundEvent IMPERIAL_STORM_SHORT;

    public static SoundEvent IMPERIAL_SHOOT_ARROW;

    //Gargoyles
    public static SoundEvent GARGOYLE_WING_FLAP;
    public static SoundEvent GARGOYLE_SHOOT_WINGS;
    public static SoundEvent GARGOYLE_HURT;
    public static SoundEvent GARGOYLE_IDLE;
    public static SoundEvent GARGOYLE_MOVE;
    public static SoundEvent GARGOYLE_CAST_BASIC;
    public static SoundEvent GARGOYLE_CAST_SPECIAL;
    public static SoundEvent GARGOYLE_RING_SUMMON;
    public static SoundEvent GARGOYLE_RING_IDLE;
    public static SoundEvent GARGOYLE_LAZER;

    public static SoundEvent GARGOYLE_STEP;
    public static SoundEvent GARGOYLE_SUMMON_LAZER;

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
        //
        BIG_WYRK_HURT = registerSound("great_wyrk.hurt", "entity");
        BIG_WYRK_IDLE = registerSound("great_wyrk.idle", "entity");
        BIG_WYRK_STEP = registerSound("great_wyrk.step", "entity");
        BIG_WYRK_BARRAGE = registerSound("great_wyrk.barrage", "entity");
        BIG_WYRK_ROLL = registerSound("great_wyrk.roll", "entity");
        BIG_WYRK_RISE = registerSound("great_wyrk.arise", "entity");
        BIG_WYRK_STRIKE = registerSound("great_wyrk.strike", "entity");
        BIG_WYRK_DROP = registerSound("great_wyrk.drop", "entity");
        BIG_WYRK_LAZER = registerSound("great_wyrk.lazer", "entity");
        BIG_WYRK_SUMMON_FOOT = registerSound("great_wyrk.summon", "entity");
        BIG_WYRK_FOOT_IMPACT = registerSound("great_wyrk.foot", "entity");
        BIG_WYRK_DEFLECT = registerSound("great_wyrk.deflect", "entity");
        BIG_WYRK_SUMMON = registerSound("great_wyrk.summon_boss", "entity");
        WYRK_STAFF_SHOOT = registerSound("great_wyrk.player_shoot", "entity");
        WYRK_STAFF_CHARGE = registerSound("great_wyrk.player_charge", "entity");
        WYRK_STAFF_LAZER = registerSound("great_wyrk.player_lazer", "entity");
        //
        IMPERIAL_COUNTER = registerSound("imperial_knight.counter", "entity");
        SKY_LIGHTNING_CAST = registerSound("imperial_knight.cast_lightning", "entity");
        IMPERIAL_HALBERD_CAST = registerSound("imperial_knight.cast_halberd", "entity");
        IMPERIAL_START_MAGIC = registerSound("imperial_knight.start_magic", "entity");
        IMPERIAL_HURT = registerSound("imperial_knight.hurt", "entity");
        IMPERIAL_IDLE = registerSound("imperial_knight.idle", "entity");
        IMPERIAL_STORM_STOMP = registerSound("imperial_knight.storm_stomp", "entity");
        IMPERIAL_STORM_LONG = registerSound("imperial_knight.storm_long", "entity");
        IMPERIAL_STORM_SHORT = registerSound("imperial_knight.storm_short", "entity");
        IMPERIAL_SHOOT_ARROW = registerSound("imperial_knight.shoot_arrow", "entity");
        //
        GARGOYLE_WING_FLAP = registerSound("gargoyle.flap", "entity");
        GARGOYLE_SHOOT_WINGS = registerSound("gargoyle.shoot", "entity");
        GARGOYLE_HURT = registerSound("gargoyle.hurt", "entity");
        GARGOYLE_IDLE = registerSound("gargoyle.idle", "entity");
        GARGOYLE_MOVE = registerSound("gargoyle.move", "entity");
        GARGOYLE_CAST_BASIC = registerSound("gargoyle.cast_basic", "entity");
        GARGOYLE_CAST_SPECIAL = registerSound("gargoyle.cast_special", "entity");
        GARGOYLE_LAZER = registerSound("gargoyle.lazer", "entity");
        GARGOYLE_RING_IDLE = registerSound("gargoyle.ring_idle", "entity");
        GARGOYLE_RING_SUMMON = registerSound("gargoyle.ring_summon", "entity");
        GARGOYLE_SUMMON_LAZER = registerSound("gargoyle.summon_lazer", "entity");
        GARGOYLE_STEP = registerSound("gargoyle.step", "entity");
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
