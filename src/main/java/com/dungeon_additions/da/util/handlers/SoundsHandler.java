package com.dungeon_additions.da.util.handlers;

import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
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
    public static SoundEvent B_KNIGHT_SWING;

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

    //GRUM
    public static SoundEvent GRUM_PLACE;
    public static SoundEvent GRUM_BREAK;
    public static SoundEvent GRUM_STEP;
    //GAELON
    public static SoundEvent GAELON_PLACE;
    public static SoundEvent GAELON_BREAK;
    public static SoundEvent GAELON_STEP;
    //METAL BLOCKS
    public static SoundEvent STORMVIER_BLOCK_STEP;
    public static SoundEvent STORMVIER_BLOCK_BREAK;

    //Soul Star Block
    public static SoundEvent SOUL_STAR_ACTIVATE;

    //Frozen Icicle
    public static SoundEvent ICICLE_DROP;
    //
    public static SoundEvent MIRROR_MOVE;
    public static SoundEvent MIRROR_PLING;
    public static SoundEvent V_MIRROR_PLING;
    public static SoundEvent MIRROR_DISPENSE;

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
    public static SoundEvent IMPERIAL_SWORD_PARRY;

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

    //Dark Assassin
    public static SoundEvent DARK_ASSASSIN_HURT;
    public static SoundEvent DARK_ASSASSIN_APPEAR;
    public static SoundEvent DARK_SUMMON_INK;
    public static SoundEvent DARK_ASSASSIN_DASH;
    //Shadow Hand
    public static SoundEvent SHADOW_HAND_SUMMON;
    public static SoundEvent SHADOW_HAND_ATTACK;

    //High King Drake
    public static SoundEvent HIGH_DRAKE_IDLE;
    public static SoundEvent HIGH_DRAKE_HURT;
    public static SoundEvent HIGH_DRAKE_STEP;
    public static SoundEvent HIGH_DRAKE_IMPACT_GROUND;
    public static SoundEvent HIGH_DRAKE_ROAR_GROUND;
    public static SoundEvent HIGH_DRAKE_ROAR_AIR;
    public static SoundEvent DRAKE_CAST_LIGHTNING;
    public static SoundEvent HIGH_DRAKE_BREATH_GROUND;
    public static SoundEvent HIGH_DRAKE_BREATH_AIR;

    public static SoundEvent HIGH_DRAKE_WING_FLAP;
    public static SoundEvent HIGH_DRAKE_ROCK_AOE;
    public static SoundEvent HIGH_DRAKE_SUMMON_SPECIAL;
    public static SoundEvent HIGH_DRAKE_SPECIAL_DAMAGE_ENTITY;
    public static SoundEvent HIGH_DRAKE_BITE;
    public static SoundEvent HIGH_DRAKE_TAIL_SWIPE;

    //High King
    public static SoundEvent HIGH_KING_HURT;
    public static SoundEvent HIGH_KING_ARMOR_MOVEMENT;
    public static SoundEvent HIGH_KING_SWING;
    public static SoundEvent HIGH_KING_SWING_MAGIC;
    public static SoundEvent HIGH_KING_STEP;
    public static SoundEvent HIGH_KING_SWING_IMPALE;

    public static SoundEvent HIGH_KING_CLAW_DRAW;

    public static SoundEvent HIGH_KING_CAST_CLAW;
    public static SoundEvent HIGH_KING_SPEAR_IMPACT;
    public static SoundEvent HOLY_SPIKE_SUMMON;
    public static SoundEvent BLOOD_GRAB_ATTACK;
    public static SoundEvent SPEAR_GRAB_ATTACK;
    public static SoundEvent BLOODY_FLY_SWING;
    public static SoundEvent BLOOD_FLY_BEGIN;
    public static SoundEvent KING_GRAB_SUCCESS;

    public static SoundEvent BLOOD_SPEAR_USE;

    public static SoundEvent INCENDIUM_HURT;
    public static SoundEvent INCENDIUM_IDLE;
    public static SoundEvent INCENDIUM_SWING;
    public static SoundEvent INCENDIUM_HEAVY_SWING;

    public static SoundEvent BAREANT_IDLE;
    public static SoundEvent BAREANT_HURT;
    public static SoundEvent BAREANT_DASH;

    public static SoundEvent VOLACTILE_SHIELD_USE;
    public static SoundEvent VOLACTILE_SHIELD_BLOCK;
    public static SoundEvent VOLACTILE_SMASH;
    public static SoundEvent VOLACTILE_SHOOT_CANNON;
    public static SoundEvent VOLACTILE_HURT;
    public static SoundEvent VOLACTILE_IDLE;
    public static SoundEvent VOLACTILE_CAST;
    public static SoundEvent VOLATILE_ORB_FAIL;

    public static SoundEvent ABBERRANT_IDLE;
    public static SoundEvent ABBERRANT_HURT;

    public static SoundEvent DARK_SORCERER_HURT;
    public static SoundEvent DARK_SORCERER_IDLE;
    public static SoundEvent DARK_SORCERER_SUMMON_MINION;
    public static SoundEvent DARK_SORCERER_EXCHANGE;
    public static SoundEvent DARK_ROYAL_IDLE;
    public static SoundEvent DARK_ROYAL_HURT;
    public static SoundEvent DARK_ROYAL_DASH;

    public static SoundEvent BEETLE_IDLE;
    public static SoundEvent BEETLE_ATTACK;
    public static SoundEvent BEETLE_HURT;
    public static SoundEvent BEETLE_WALK;
    public static SoundEvent BEETLE_WAR_CRY;
    public static SoundEvent BEETLE_DEATH;

    //Obsidilith
    public static SoundEvent OBSIDILITH_CAST;
    public static SoundEvent OBSIDILITH_BURST;
    public static SoundEvent OBSIDILITH_SHIELD;
    public static SoundEvent OBSIDILITH_HURT;
    public static SoundEvent OBSIDILITH_WAVE_DING;
    //VOIDIANT
    public static SoundEvent VOIDIANT_HURT;
    public static SoundEvent VOIDIANT_WALK;
    public static SoundEvent VOIDIANT_JUMP;
    public static SoundEvent VOIDIANT_OPEN;
    public static SoundEvent VOIDIANT_SHOOT_LAZER;
    public static SoundEvent VOIDIANT_CHARGE_LAZER;
    public static SoundEvent VOIDIANT_LAZER_GUN;
    //VOICLYSM
    public static SoundEvent VOIDCLYSM_HURT;
    public static SoundEvent VOIDCLYSM_IDLE;
    public static SoundEvent VOIDCLYSM_SCREAM;
    public static SoundEvent VOIDCLYSM_BEAM_TELEPORT;
    public static SoundEvent VOIDCLYSM_IMPACT;
    public static SoundEvent VOIDCLYSM_CAST_SPELL;
    public static SoundEvent VOIDCLYSM_CAST_TRACK;
    public static SoundEvent VOIDCLYSM_BLACK_HOLE;
    public static SoundEvent VOIDCLYSM_CLAP_ATTACK;
    public static SoundEvent VOIDCLYSM_EQUIP;
    public static SoundEvent VOIDCLYSM_BOMB_EXPLODE;

    //Aegyptia
    public static SoundEvent AEGYPTIA_HURT;
    public static SoundEvent AEGYPTIA_IDLE;
    public static SoundEvent AEGYPTIA_STEP;
    public static SoundEvent AEGYPTIA_PIERCE;
    public static SoundEvent AEGYPTIA_SWING_STORM;

    //EVERATOR
    public static SoundEvent EVERATOR_SWING;
    public static SoundEvent EVERATOR_HURT;
    public static SoundEvent EVERATOR_STEP;
    public static SoundEvent EVERATOR_SMASH_SWING;
    public static SoundEvent EVERATOR_TRIBUTE;

    //REANIMATE
    public static SoundEvent REANIMATE_HURT;
    public static SoundEvent REANIMATE_IDLE;
    public static SoundEvent REANIMATE_STEP;
    public static SoundEvent REANIMATE_SWING;
    public static SoundEvent REANIMATE_APPEAR;
    public static SoundEvent REANIMATE_DISAPPEAR;
    public static SoundEvent REANIMATE_CAST;
    public static SoundEvent REANIMATE_SHIELD;

    //APATHYR
    public static SoundEvent APATHYR_HURT;
    public static SoundEvent APATHYR_ATTACK;
    public static SoundEvent APATHYR_SWING;
    public static SoundEvent APATHYR_STEP;
    public static SoundEvent APATHYR_JUMP;
    public static SoundEvent APATHYR_CAST_MAGIC;
    public static SoundEvent APATHYR_CAST_HEAVY;
    public static SoundEvent APATHYR_CHARGE_AXE;
    public static SoundEvent APATHYR_AXE_IMPACT;
    public static SoundEvent APATHYR_SUMMON_SPEAR;
    public static SoundEvent APATHYR_CAST_SPIKES;
    public static SoundEvent APATHYR_DOMAIN_IDLE;
    public static SoundEvent APATHYR_SUMMON_GHOST;
    public static SoundEvent APATHYR_SLIGHT_DASH;
    public static SoundEvent APATHYR_AWAKEN;
    public static SoundEvent APATHYR_DEATH;

    //Cursed Sentinel
    public static SoundEvent CURSED_SENTINEL_STEP;
    public static SoundEvent CURSED_SENTINEL_HURT;
    public static SoundEvent CURSED_SENTINEL_AWAKE;

    //MUSIC
    public static SoundEvent HIGH_DRAGON_TRACK;
    public static SoundEvent HIGH_KING_TRACK;
    public static SoundEvent NIGHT_LICH_TRACK;
    public static SoundEvent FLAME_KNIGHT_TRACK;
    public static SoundEvent ANCIENT_WYRK_TRACK;
    public static SoundEvent VOID_BLOSSOM_TRACK;
    public static SoundEvent FALLEN_STORMVIER_TRACK;
    public static SoundEvent OBSIDILITH_TRACK;
    public static SoundEvent VOIDCLYSM_TRACK;
    public static SoundEvent APATHYR_TRACK;

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
        MIRROR_MOVE = registerSound("mirror.move", "block");
        MIRROR_PLING = registerSound("mirror.pling", "block");
        V_MIRROR_PLING = registerSound("mirror.v_pling", "block");
        MIRROR_DISPENSE = registerSound("mirror.dispense", "block");
        //
        GRUM_BREAK = registerSound("grum.break", "block");
        GRUM_PLACE = registerSound("grum.place", "block");
        GRUM_STEP = registerSound("grum.step", "block");
        //
        GAELON_BREAK = registerSound("gaelon.break", "block");
        GAELON_PLACE = registerSound("gaelon.place","block");
        GAELON_STEP = registerSound("gaelon.step","block");
        //
        STORMVIER_BLOCK_BREAK = registerSound("stormvier.break","block");
        STORMVIER_BLOCK_STEP = registerSound("stormvier.step","block");
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
        B_KNIGHT_SWING = registerSound("b_knight.swing", "entity");
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
        IMPERIAL_SWORD_PARRY = registerSound("imperial_knight.parry", "entity");
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
        //
        DARK_ASSASSIN_APPEAR = registerSound("dark_assassin.appear", "entity");
        DARK_ASSASSIN_HURT = registerSound("dark_assassin.hurt", "entity");
        DARK_SUMMON_INK = registerSound("dark_assassin.summon_ink", "entity");
        DARK_ASSASSIN_DASH = registerSound("dark_assassin.dash", "entity");
        //
        SHADOW_HAND_ATTACK = registerSound("shadow_hand.attack", "entity");
        SHADOW_HAND_SUMMON = registerSound("shadow_hand.summon", "entity");
        //
        HIGH_DRAKE_HURT = registerSound("high_drake.hurt", "entity");
        HIGH_DRAKE_IDLE = registerSound("high_drake.idle", "entity");
        HIGH_DRAKE_IMPACT_GROUND = registerSound("high_drake.impact", "entity");
        HIGH_DRAKE_STEP = registerSound("high_drake.step", "entity");
        HIGH_DRAKE_ROAR_AIR = registerSound("high_drake.roar_air", "entity");
        HIGH_DRAKE_ROAR_GROUND = registerSound("high_drake.roar_ground", "entity");
        DRAKE_CAST_LIGHTNING = registerSound("high_drake.drake_lightning", "entity");
        HIGH_DRAKE_BREATH_AIR = registerSound("high_drake.breath_air", "entity");
        HIGH_DRAKE_BREATH_GROUND = registerSound("high_drake.breath_ground", "entity");
        HIGH_DRAKE_WING_FLAP = registerSound("high_drake.wing_flap", "entity");
        HIGH_DRAKE_ROCK_AOE = registerSound("high_drake.rock_aoe", "entity");
        HIGH_DRAKE_SUMMON_SPECIAL = registerSound("high_drake.summon_special", "entity");
        HIGH_DRAKE_SPECIAL_DAMAGE_ENTITY = registerSound("high_drake.special_damage", "entity");
        HIGH_DRAKE_BITE = registerSound("high_drake.bite", "entity");
        HIGH_DRAKE_TAIL_SWIPE = registerSound("high_drake.tail_swipe", "entity");
        //
        HIGH_KING_HURT = registerSound("high_king.hurt", "entity");
        HIGH_KING_SWING = registerSound("high_king.swing", "entity");
        HIGH_KING_SWING_MAGIC = registerSound("high_king.swing_magic", "entity");
        HIGH_KING_ARMOR_MOVEMENT = registerSound("high_king.armor", "entity");
        HIGH_KING_STEP = registerSound("high_king.step", "entity");
        HIGH_KING_SWING_IMPALE = registerSound("high_king.swing_blood", "entity");
        HIGH_KING_CLAW_DRAW = registerSound("high_king.claw_draw", "entity");
        HIGH_KING_CAST_CLAW = registerSound("high_king.cast_claw", "entity");
        HIGH_KING_SPEAR_IMPACT = registerSound("high_king.spear_impact", "entity");
        HOLY_SPIKE_SUMMON = registerSound("high_king.spike_summon", "entity");
        BLOOD_GRAB_ATTACK = registerSound("high_king.king_dash_blood", "entity");
        SPEAR_GRAB_ATTACK = registerSound("high_king.king_dash", "entity");
        BLOOD_FLY_BEGIN = registerSound("high_king.blood_fly_begin", "entity");
        BLOODY_FLY_SWING = registerSound("high_king.blood_fly", "entity");
        KING_GRAB_SUCCESS = registerSound("high_king.grab_success", "entity");
        //
        BLOOD_SPEAR_USE = registerSound("high_king.blood_spear_use", "entity");
        //
        INCENDIUM_HURT = registerSound("incendium.hurt", "entity");
        INCENDIUM_IDLE = registerSound("incendium.idle", "entity");
        INCENDIUM_HEAVY_SWING = registerSound("incendium.heavy_swing", "entity");
        INCENDIUM_SWING = registerSound("incendium.swing", "entity");
        //
        BAREANT_HURT = registerSound("bareant.hurt", "entity");
        BAREANT_DASH = registerSound("bareant.dash", "entity");
        BAREANT_IDLE = registerSound("bareant.idle", "entity");
        //
        VOLACTILE_SHIELD_BLOCK = registerSound("volatile.block", "entity");
        VOLACTILE_SHIELD_USE = registerSound("volatile.shield_use", "entity");
        VOLACTILE_SMASH = registerSound("volatile.smash", "entity");
        VOLACTILE_HURT = registerSound("volatile.hurt", "entity");
        VOLACTILE_IDLE = registerSound("volatile.idle", "entity");
        VOLACTILE_CAST = registerSound("volatile.cast", "entity");
        VOLACTILE_SHOOT_CANNON = registerSound("volatile.cannon", "entity");
        VOLATILE_ORB_FAIL = registerSound("volatile.orb_fail", "entity");
        ABBERRANT_HURT = registerSound("aberrant.hurt", "entity");
        ABBERRANT_IDLE = registerSound("aberrant.idle", "entity");
        //
        DARK_SORCERER_HURT = registerSound("dark_sorcerer.hurt", "entity");
        DARK_SORCERER_IDLE = registerSound("dark_sorcerer.idle", "entity");
        DARK_SORCERER_SUMMON_MINION = registerSound("dark_sorcerer.summon", "entity");
        DARK_SORCERER_EXCHANGE = registerSound("dark_sorcerer.exchange", "entity");
        //
        DARK_ROYAL_HURT = registerSound("dark_crypt.hurt", "entity");
        DARK_ROYAL_IDLE = registerSound("dark_crypt.idle", "entity");
        DARK_ROYAL_DASH = registerSound("dark_crypt.dash", "entity");
        //
        BEETLE_ATTACK = registerSound("beetle.bite", "entity");
        BEETLE_HURT = registerSound("beetle.hurt", "entity");
        BEETLE_IDLE = registerSound("beetle.idle", "entity");
        BEETLE_WALK = registerSound("beetle.step", "entity");
        BEETLE_WAR_CRY = registerSound("beetle.warcry", "entity");
        BEETLE_DEATH = registerSound("beetle.death", "entity");
        //
        OBSIDILITH_BURST = registerSound("obsi.burst", "entity");
        OBSIDILITH_CAST = registerSound("obsi.cast", "entity");
        OBSIDILITH_SHIELD = registerSound("obsi.shield", "entity");
        OBSIDILITH_WAVE_DING = registerSound("obsi.step", "entity");
        OBSIDILITH_HURT = registerSound("obsi.hurt","entity");
        //
        VOIDIANT_HURT = registerSound("voidiant.hurt", "entity");
        VOIDIANT_JUMP = registerSound("voidiant.jump", "entity");
        VOIDIANT_OPEN = registerSound("voidiant.open", "entity");
        VOIDIANT_WALK = registerSound("voidiant.walk", "entity");
        VOIDIANT_CHARGE_LAZER = registerSound("voidiant.charge", "entity");
        VOIDIANT_SHOOT_LAZER = registerSound("voidiant.shoot", "entity");
        VOIDIANT_LAZER_GUN = registerSound("voidiant.lazer_gun", "entity");
        //
        VOIDCLYSM_HURT = registerSound("voidclysm.hurt", "entity");
        VOIDCLYSM_IDLE = registerSound("voidclysm.idle", "entity");
        VOIDCLYSM_SCREAM = registerSound("voidclysm.scream", "entity");
        VOIDCLYSM_BEAM_TELEPORT = registerSound("voidclysm.beam_teleport", "entity");
        VOIDCLYSM_IMPACT = registerSound("voidclysm.impact", "entity");
        VOIDCLYSM_CAST_SPELL = registerSound("voidclysm.cast_spell", "entity");
        VOIDCLYSM_CAST_TRACK = registerSound("voidclysm.cast_track", "entity");
        VOIDCLYSM_BLACK_HOLE = registerSound("voidclysm.blackhole", "entity");
        VOIDCLYSM_CLAP_ATTACK = registerSound("voidclysm.aoe", "entity");
        VOIDCLYSM_EQUIP = registerSound("voidclysm.equip", "entity");
        VOIDCLYSM_BOMB_EXPLODE = registerSound("voidclysm.bomb_explode", "entity");
        //
        AEGYPTIA_HURT = registerSound("aegyptia.hurt", "entity");
        AEGYPTIA_IDLE = registerSound("aegyptia.idle", "entity");
        AEGYPTIA_STEP = registerSound("aegyptia.step", "entity");
        AEGYPTIA_SWING_STORM = registerSound("aegyptia.swing_storm", "entity");
        AEGYPTIA_PIERCE = registerSound("aegyptia.pierce", "entity");
        //
        EVERATOR_SWING = registerSound("everator.swing", "entity");
        EVERATOR_STEP = registerSound("everator.step", "entity");
        EVERATOR_TRIBUTE = registerSound("everator.tribute", "entity");
        EVERATOR_HURT = registerSound("everator.hurt", "entity");
        //
        REANIMATE_APPEAR = registerSound("reanimate.appear", "entity");
        REANIMATE_DISAPPEAR = registerSound("reanimate.disappear","entity");
        REANIMATE_SWING = registerSound("reanimate.swing","entity");
        REANIMATE_HURT = registerSound("reanimate.hurt","entity");
        REANIMATE_IDLE = registerSound("reanimate.idle","entity");
        REANIMATE_STEP = registerSound("reanimate.step","entity");
        REANIMATE_CAST = registerSound("reanimate.cast", "entity");
        REANIMATE_SHIELD = registerSound("reanimate.shield", "entity");
        //APATHYR
        APATHYR_ATTACK = registerSound("apathyr.attack", "entity");
        APATHYR_HURT = registerSound("apathyr.hurt", "entity");
        APATHYR_SWING = registerSound("apathyr.swing","entity");
        APATHYR_STEP = registerSound("apathyr.step","entity");
        APATHYR_JUMP = registerSound("apathyr.jump","entity");
        APATHYR_SLIGHT_DASH = registerSound("apathyr.small_dash","entity");
        APATHYR_CHARGE_AXE = registerSound("apathyr.charge_axe", "entity");
        APATHYR_AXE_IMPACT = registerSound("apathyr.impact", "entity");
        APATHYR_CAST_MAGIC = registerSound("apathyr.cast", "entity");
        APATHYR_CAST_HEAVY = registerSound("apathyr.cast_heavy", "entity");
        APATHYR_SUMMON_SPEAR = registerSound("apathyr.summon_spear", "entity");
        APATHYR_CAST_SPIKES = registerSound("apathyr.cast_spikes","entity");
        APATHYR_SUMMON_GHOST = registerSound("apathyr.summon_ghost","entity");
        APATHYR_DOMAIN_IDLE = registerSound("apathyr.domain_idle","entity");
        APATHYR_AWAKEN = registerSound("apathyr.awaken","entity");
        APATHYR_DEATH = registerSound("apathyr.death","entity");
        //
        CURSED_SENTINEL_HURT = registerSound("cursed_sentinel.hurt","entity");
        CURSED_SENTINEL_STEP = registerSound("cursed_sentinel.step","entity");
        CURSED_SENTINEL_AWAKE = registerSound("cursed_sentinel.awake","entity");
        //MUSIC
        HIGH_DRAGON_TRACK = registerSound("ambient.high_dragon", "music");
        HIGH_KING_TRACK = registerSound("ambient.high_king", "music");
        FLAME_KNIGHT_TRACK = registerSound("ambient.flame_knight", "music");
        VOID_BLOSSOM_TRACK = registerSound("ambient.void_blossom", "music");
        FALLEN_STORMVIER_TRACK = registerSound("ambient.fallen_stormvier", "music");
        NIGHT_LICH_TRACK = registerSound("ambient.night_lich", "music");
        ANCIENT_WYRK_TRACK = registerSound("ambient.ancient_wyrk", "music");
        VOIDCLYSM_TRACK = registerSound("ambient.voidclysm", "music");
        OBSIDILITH_TRACK = registerSound("ambient.obsidilith", "music");
        APATHYR_TRACK = registerSound("ambient.apathyr","music");
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
