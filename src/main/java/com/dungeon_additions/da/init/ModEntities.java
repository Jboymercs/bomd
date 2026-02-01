package com.dungeon_additions.da.init;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityFireResistantItems;
import com.dungeon_additions.da.entity.EntityNetherAbberrant;
import com.dungeon_additions.da.entity.ProjectileEndlessEnderpearl;
import com.dungeon_additions.da.entity.blossom.*;
import com.dungeon_additions.da.entity.dark_dungeon.*;
import com.dungeon_additions.da.entity.desert_dungeon.EntityScutterBeetle;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileDesertOrb;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileDesertStorm;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileThousandCuts;
import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityAegyptia;
import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityDesertBeam;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntityAegyptianColossus;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntityAegyptianWarlord;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntityColossusSigil;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntitySummonedMace;
import com.dungeon_additions.da.entity.desert_dungeon.friendly.EntityFriendlyScutterBeetle;
import com.dungeon_additions.da.entity.desert_dungeon.miniboss.EntityEverator;
import com.dungeon_additions.da.entity.desert_dungeon.miniboss.ProjectileYellowWave;
import com.dungeon_additions.da.entity.flame_knight.*;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileFlameBlade;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileFlameSling;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileTrackingFlame;
import com.dungeon_additions.da.entity.frost_dungeon.*;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugrRanger;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityEliteDraugr;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.ProjectileSoul;
import com.dungeon_additions.da.entity.frost_dungeon.wyrk.EntityFriendWyrk;
import com.dungeon_additions.da.entity.gaelon_dungeon.*;
import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.*;
import com.dungeon_additions.da.entity.gaelon_dungeon.friendly.EntityFriendlyCursedRevenant;
import com.dungeon_additions.da.entity.generic.EntityDelayedExplosion;
import com.dungeon_additions.da.entity.mini_blossom.EntityDart;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.night_lich.*;
import com.dungeon_additions.da.entity.night_lich.EntityLichSpawn;
import com.dungeon_additions.da.entity.player.EntityWyrkLazer;
import com.dungeon_additions.da.entity.projectiles.*;
import com.dungeon_additions.da.entity.projectiles.puzzle.ProjectilePuzzleBall;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightBoss;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightRapier;
import com.dungeon_additions.da.entity.rot_knights.EntityRotSpike;
import com.dungeon_additions.da.entity.sky_dungeon.*;
import com.dungeon_additions.da.entity.sky_dungeon.friendly.EntityFriendlyHalberd;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.*;
import com.dungeon_additions.da.entity.tileEntity.*;
import com.dungeon_additions.da.entity.trader.EntityMysteriousTrader;
import com.dungeon_additions.da.entity.util.EntityBossSpawner;
import com.dungeon_additions.da.entity.void_dungeon.*;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModEntities {
    private static final Map<Class<? extends Entity>, String> ID_MAP = new HashMap<>();
    private static int ENTITY_START_ID = 123;
    private static int PROJECTILE_START_ID = 230;

    public static Vec3i void_blossom = new Vec3i(6433126, 8111156, 0);
    public static Vec3i mini_void_blossom = new Vec3i(0x9e02e0, 8111156, 0);
    public static Vec3i nether_spirit = new Vec3i(0x3b0009, 0xeb4034, 0);
    public static Vec3i incendium_spirit = new Vec3i(0x3b0009, 0xe3d646, 0);
    public static Vec3i rot_knights = new Vec3i(0x63615e,0x66005,0);
    public static Vec3i bareant_spirit = new Vec3i(0xffb206, 0x940700, 0);
    public static Vec3i volactile_spirit = new Vec3i(0x3b0009, 0xf3ec9b, 0);
    public static Vec3i kobf = new Vec3i(0xbf922a, 0xeb4034, 0);
    public static Vec3i rot_knight_rapier = new Vec3i(0x63615e,0xc7a548,0);
    public static Vec3i rot_knight_boss = new Vec3i(0x63615e, 0x03ff18, 0);
    public static Vec3i night_lich = new Vec3i(0x021a1a,0x6eebeb,0);

    public static Vec3i wyrk_mob = new Vec3i(0x6eebeb,0x021a1a,0);
    public static Vec3i ancient_wyrk = new Vec3i(0x021a1a,0xedc12f,0);
    public static Vec3i draugr_champion = new Vec3i(0xa3a19b,0x6eebeb,0);
    public static Vec3i draugr_melee = new Vec3i(0x00338c,0x6eebeb,0);
    public static Vec3i draugr_ranged = new Vec3i(0x62117d,0x6eebeb,0);

    public static Vec3i imperial_halberd = new Vec3i(0x5a6e75,0x7d5e00,0);
    public static Vec3i imperial_sword = new Vec3i(0x5a6e75,0xf5bb0c,0);
    public static Vec3i farum_gargoyle = new Vec3i(0x404040,0xf5bb0c,0);
    public static Vec3i farum_elder = new Vec3i(0x404040,0xebff0a,0);
    public static Vec3i dark_assassin = new Vec3i(0x233547,0x030303,0);
    public static Vec3i high_drake = new Vec3i(0x262852,0xd4d4cd,0);
    public static Vec3i high_king = new Vec3i(0x262852,0xe0c03f,0);
    public static Vec3i dark_sorcerer = new Vec3i(0x233547,0x69057b,0);
    public static Vec3i aegyptian_recurian = new Vec3i(0xEDEDCE,0x6E2C36,0);
    public static Vec3i everator = new Vec3i(0xEDEDCE,0xEBD463,0);

    public static Vec3i dark_royal = new Vec3i(0x233547,0xb42507,0);
    public static Vec3i scutter_beetle = new Vec3i(0x0e588a, 0xf2c31e, 0);
    public static Vec3i obsidilith = new Vec3i(0x161117, 0x5B016B, 0);
    public static Vec3i voidclysm = new Vec3i(0x161117, 0xDE28D9, 0);
    public static Vec3i voidiant = new Vec3i(0xD473D9, 0x91094D, 0);
    public static Vec3i reanimate = new Vec3i(0x615429,0x6eebeb,0);
    public static Vec3i cursed_sentinel = new Vec3i(0x2B241E,0x6eebeb,0);
    public static Vec3i apathyr = new Vec3i(0xBD8F04,0x6eebeb,0);
    public static Vec3i mysterious_trader = new Vec3i(0x57514E,0x75000D,0);
    public static void registerEntities() {
        //One day, you'll be back in game my son
       // registerEntityWithID("wreath_knight", EntityWreathKnight.class, ENTITY_START_ID++, 50, void_blossom);
        //Rotten Hold
        registerEntityWithID("ancient_fallen", EntityRotKnightBoss.class, ENTITY_START_ID++, 50, rot_knight_boss);
        registerEntityWithID("ancient_knight", EntityRotKnight.class, ENTITY_START_ID++, 70, rot_knights);
        registerEntityWithID("ancient_knight_rapier", EntityRotKnightRapier.class, ENTITY_START_ID++, 50 ,rot_knight_rapier);
        //Void Blossom
        registerEntityWithID("mini_blossom", EntityMiniBlossom.class, ENTITY_START_ID++, 70, mini_void_blossom);
        registerEntityWithID("void_blossom", EntityVoidBlossom.class, ENTITY_START_ID++, 90, void_blossom);
        //Frozen Castle
        registerEntityWithID("wyrk", EntityWyrk.class, ENTITY_START_ID++, 60, wyrk_mob);
        registerEntityWithID("frost_draugr", EntityDraugr.class, ENTITY_START_ID++, 60, draugr_melee);
        registerEntityWithID("frost_draugr_ranger", EntityDraugrRanger.class, ENTITY_START_ID++, 60, draugr_ranged);
        registerEntityWithID("frost_draugr_elite", EntityEliteDraugr.class, ENTITY_START_ID++, 70, draugr_champion);
        registerEntityWithID("great_wyrk", EntityGreatWyrk.class, ENTITY_START_ID++, 90, ancient_wyrk);
        //Forgotten Temple
        registerEntityWithID("scutter_beetle", EntityScutterBeetle.class, ENTITY_START_ID++, 70, scutter_beetle);
        registerEntity("scutter_beetle_friendly", EntityFriendlyScutterBeetle.class, ENTITY_START_ID++, 70);
        registerEntityWithID("aegyptia", EntityAegyptia.class, ENTITY_START_ID++, 70, aegyptian_recurian);
        registerEntityWithID("everator", EntityEverator.class, ENTITY_START_ID++, 70, everator);
        registerEntityWithID("aegyptian_warlord", EntityAegyptianWarlord.class, ENTITY_START_ID++, 70, night_lich);
        registerEntityWithID("aegyptian_colossus", EntityAegyptianColossus.class, ENTITY_START_ID++, 70, night_lich);
        // Burning Flame Arena
        registerEntityWithID("nether_aberant", EntityNetherAbberrant.class, ENTITY_START_ID++, 50, nether_spirit);
        registerEntityWithID("incendium_spirit", EntityIncendium.class, ENTITY_START_ID++, 70, incendium_spirit);
        registerEntityWithID("bareant", EntityBareant.class, ENTITY_START_ID++, 70, bareant_spirit);
        registerEntityWithID("volatile_spirit", EntityVolatileSpirit.class, ENTITY_START_ID++, 90, volactile_spirit);
        registerEntityWithID("flame_knight", EntityFlameKnight.class, ENTITY_START_ID++, 70, kobf);
        //Obsidilith
        registerEntityWithID("voidiant", EntityVoidiant.class, ENTITY_START_ID++, 60, voidiant);
        registerEntityWithID("obsidilith", EntityObsidilith.class, ENTITY_START_ID++, 70, obsidilith);
        registerEntityWithID("voidclysm", EntityVoidiclysm.class, ENTITY_START_ID++, 70, voidclysm);
        registerEntity("blue_wave", EntityBlueWave.class, ENTITY_START_ID++, 50);
        //Gaelon Dungeon
        registerEntityWithID("reanimate", EntityReAnimate.class, ENTITY_START_ID++, 60, reanimate);
        registerEntity("cursed_revenant", EntityFriendlyCursedRevenant.class, ENTITY_START_ID++, 60);
        registerEntity("cursed_sentinel_friendly", EntityFriendlyCursedGolem.class, ENTITY_START_ID++, 60);
        registerEntityWithID("cursed_sentinel",EntityCursedSentinel.class, ENTITY_START_ID++, 60, cursed_sentinel);
        registerEntityWithID("apathyr", EntityApathyr.class, ENTITY_START_ID++, 80, apathyr);
        //Night Lich's Tower
        registerEntityWithID("night_lich", EntityNightLich.class, ENTITY_START_ID++, 90, night_lich);
        //High Court City
        registerEntityWithID("trident_gargoyle", EntityTridentGargoyle.class, ENTITY_START_ID++, 70, farum_gargoyle);
        registerEntityWithID("mage_gargoyle", EntityMageGargoyle.class, ENTITY_START_ID++, 70, farum_elder);
        registerEntityWithID("imperial_sword", EntityImperialSword.class, ENTITY_START_ID++, 90, imperial_sword);
        registerEntityWithID("imperial_halberd", EntityImperialHalberd.class, ENTITY_START_ID++, 90, imperial_halberd);
        registerEntityWithID("high_king_drake", EntityHighKingDrake.class, ENTITY_START_ID++, 110, high_drake);
        registerEntityWithID("high_king", EntityHighKing.class, ENTITY_START_ID++, 100, high_king);
        //Dark Dungeon
        registerEntityWithID("dark_assassin", EntityDarkAssassin.class, ENTITY_START_ID++, 70, dark_assassin);
        registerEntityWithID("dark_royal", EntityDarkRoyal.class, ENTITY_START_ID++, 70, dark_royal);
        registerEntityWithID("dark_sorcerer", EntityDarkSorcerer.class, ENTITY_START_ID++, 70, dark_sorcerer);
        //Traders
        registerEntityWithID("mysterious_trader", EntityMysteriousTrader.class, ENTITY_START_ID++, 50, mysterious_trader);

        //Other Projectles/Utilities
        registerEntity("spore_cloud", EntityGenericWave.class, ENTITY_START_ID++, 50);
        registerEntity("void_spike", EntityVoidSpike.class, ENTITY_START_ID++, 50);
        registerEntity("void_leaf", ProjectileVoidLeaf.class, PROJECTILE_START_ID++, 30);
        registerEntity("spore_bomb", ProjectileSporeBomb.class, PROJECTILE_START_ID++, 30);
        registerEntity("abberrant_projectile", ProjectileAbberrantAttack.class, PROJECTILE_START_ID++, 30);
        registerEntity("pearl_projectile", ProjectileEndlessEnderpearl.class, PROJECTILE_START_ID++, 30);
        registerEntity("dart_entity", EntityDart.class, ENTITY_START_ID++, 30);
        registerEntity("boss_spawner", EntityBossSpawner.class, ENTITY_START_ID++, 40);
        registerEntity("aoe_tile", EntityMoveTile.class, ENTITY_START_ID++, 50);
            registerEntity("flame_sling", ProjectileFlameSling.class, ENTITY_START_ID++, 50);
            registerEntity("flame_projectile", ProjectileTrackingFlame.class, PROJECTILE_START_ID++, 30);
            registerEntity("nether_pyre", EntityPyre.class, ENTITY_START_ID++, 30);
            registerEntity("fire_resist_item", EntityFireResistantItems.class, ENTITY_START_ID++, 30);
            registerEntity("rot_spike", EntityRotSpike.class, ENTITY_START_ID++, 30);
            registerEntity("soul_star", EntitySoulStar.class, PROJECTILE_START_ID++, 30);
            registerEntity("lily_locator", EntityLily.class, PROJECTILE_START_ID++, 30);
            registerEntity("magic_missile", ProjectileMagicMissile.class, PROJECTILE_START_ID++, 30);
            registerEntity("magic_fireball", ProjectileMagicFireBall.class, PROJECTILE_START_ID++, 30);
            registerEntity("lich_staff", EntityLichStaffAOE.class, ENTITY_START_ID++, 30);
            registerEntity("track_missile", ProjectileTrackingMagicMissile.class, PROJECTILE_START_ID++, 30);
            registerEntity("ground_missile", ProjectileMagicGround.class, PROJECTILE_START_ID++, 30);
            registerEntity("lich_spawn", EntityLichSpawn.class, ENTITY_START_ID++, 40);
            registerEntity("ice_spike", EntityIcicleSpike.class, ENTITY_START_ID++, 40);
            registerEntity("ice_bullet", ProjectileFrostBullet.class, PROJECTILE_START_ID++, 30);
            registerEntity("soul_bullet", ProjectileSoul.class, PROJECTILE_START_ID++, 30);
            registerEntity("frost_locator", EntityFrozenCastleLocator.class, PROJECTILE_START_ID++, 30);
            registerEntity("frost_magic", ProjectileFrostGround.class, PROJECTILE_START_ID++, 30);
            registerEntity("wyrk_foot", EntityWyrkFoot.class, ENTITY_START_ID++, 70);
            registerEntity("wyrk_lazer", EntityWyrkLazer.class,PROJECTILE_START_ID++, 90);
            registerEntity("voidiant_lazer", EntityVoidiantLazer.class, ENTITY_START_ID++, 90);
            registerEntity("sky_lightning_bolt", EntitySkyBolt.class, ENTITY_START_ID++, 50);
            registerEntity("sky_arrow", EntitySkyArrow.class, ENTITY_START_ID++, 60);
            registerEntity("sky_tornado", EntitySkyTornado.class, ENTITY_START_ID++, 30);
            registerEntity("farum_dart", EntityFarumSpike.class, ENTITY_START_ID++, 30);
            registerEntity("light_ring_projectile", ProjectileLightRing.class, ENTITY_START_ID++, 30);
            registerEntity("gargoyle_lazer", EntityGargoyleLazer.class, ENTITY_START_ID++, 60);
            registerEntity("friend_wyrk", EntityFriendWyrk.class, ENTITY_START_ID++, 90);
            registerEntity("shadow_hand", EntityShadowHand.class, ENTITY_START_ID++, 50);
            registerEntity("storm_tornado", ProjectileStormBreath.class, ENTITY_START_ID++, 30);
            registerEntity("storm_breath", ProjectileStormWind.class, ENTITY_START_ID++, 30);
            registerEntity("dragon_aoe", EntityDragonAOE.class, ENTITY_START_ID++, 70);
            registerEntity("dragon_special", EntityDragonSpecial.class, ENTITY_START_ID++, 70);
            registerEntity("sky_locator_projectile", EntitySkyDungeonLocator.class, ENTITY_START_ID++, 30);
            registerEntity("king_holy_aoe", EntityKingHolyAOE.class, ENTITY_START_ID++, 60);
            registerEntity("king_holy_projectile", EntityKingHolyWave.class, ENTITY_START_ID++, 40);
            registerEntity("blood_pile", EntityBloodPile.class, ENTITY_START_ID++, 60);
            registerEntity("blood_ball_proj", ProjectileKingBlood.class, ENTITY_START_ID++, 30);
            registerEntity("imperial_knight_friendly", EntityFriendlyHalberd.class, ENTITY_START_ID++, 70);
            registerEntity("burning_flame_projectile", EntityBurningFlameArenaLocator.class, ENTITY_START_ID++, 30);
            registerEntity("flame_spit", ProjectileFlameSpit.class, ENTITY_START_ID++, 30);
            registerEntity("volatile_orb", EntityFlameOrb.class, ENTITY_START_ID++, 30);
            registerEntity("flame_blade_proj", ProjectileFlameBlade.class, ENTITY_START_ID++, 30);
            registerEntity("proj_dark_matter", ProjectileDarkMatter.class, ENTITY_START_ID++, 30);
            registerEntity("puzzle_orb", ProjectilePuzzleBall.class, ENTITY_START_ID++, 50);
            registerEntity("desert_locator_proj", EntityForgottenTempleLocator.class, ENTITY_START_ID++, 30);
            registerEntity("rot_locator_proj", EntityRottenHoldLocator.class, ENTITY_START_ID++, 30);
            registerEntity("voidclysm_bolt", ProjectileVoidClysmBolt.class, ENTITY_START_ID++, 30);
            registerEntity("void_bomb", EntityVoidBomb.class, ENTITY_START_ID++, 30);
            registerEntity("voidclysm_spike", EntityVoidclysmSpike.class, ENTITY_START_ID++, 30);
            registerEntity("obsidian_locator", EntityObsidianLocator.class, ENTITY_START_ID++, 30);
            registerEntity("void_projectile", ProjectileTrackingVoid.class, ENTITY_START_ID++, 30);
            registerEntity("void_hole", EntityVoidBlackHole.class, ENTITY_START_ID++, 30);
            registerEntity("desert_beam", EntityDesertBeam.class, ENTITY_START_ID++, 30);
            registerEntity("desert_storm", ProjectileDesertStorm.class, ENTITY_START_ID++, 35);
            registerEntity("yellow_wave", ProjectileYellowWave.class, ENTITY_START_ID++, 35);
            registerEntity("crystal_wave", ProjectileCrystalWave.class, ENTITY_START_ID++, 35);
            registerEntity("ghost_bolt", ProjectileGhost.class, ENTITY_START_ID++, 35);
            registerEntity("fast_ghost_bolt", ProjectileFastGhostCrystal.class, ENTITY_START_ID++, 35);
            registerEntity("ghost_apathyr", EntityApathyrGhost.class, ENTITY_START_ID++, 40);
            registerEntity("ultra_attack", EntityUltraAttack.class, ENTITY_START_ID++, 40);
            registerEntity("apathyr_spear", EntityApathyrSpear.class, ENTITY_START_ID++, 40);
            registerEntity("apathyr_eye", EntityApathyrEye.class, ENTITY_START_ID++, 40);
            registerEntity("gaelon_locator", EntityGaelonSanctuaryLocator.class, ENTITY_START_ID++, 40);
            registerEntity("delayed_explosion_da", EntityDelayedExplosion.class, ENTITY_START_ID++, 60);
            registerEntity("summoned_mace", EntitySummonedMace.class, ENTITY_START_ID++, 50);
            registerEntity("thousand_cuts", ProjectileThousandCuts.class, PROJECTILE_START_ID++, 40);
            registerEntity("desert_projectile", ProjectileDesertOrb.class, PROJECTILE_START_ID++, 40);
            registerEntity("colossus_sigil", EntityColossusSigil.class, PROJECTILE_START_ID++, 50);



        registerTileEntity(TileEntitySporeBlossom.class, "spore_blossom");
        registerTileEntity(TileEntityMegaStructure.class, "mega_structure");
        registerTileEntity(TileEntityDisappearingSpawner.class, "disappearing_spawner_entity");
        registerTileEntity(TileEntityUpdater.class, "block_updates_entity");
        registerTileEntity(TileEntityUpdaterFrost.class, "block_updates_entity_frost");
        registerTileEntity(TileEntityLichSpawner.class, "lich_spawner_blocks");
        registerTileEntity(TileEntityLevitationBlock.class, "levitation_block_entity");
        registerTileEntity(TileEntityFrostBrick.class, "frost_brick_entity");
        registerTileEntity(TileEntityPuzzleMirror.class, "mirror_puzzle");
        registerTileEntity(TileEntityGrumDispenser.class, "grum_dispenser");
        registerTileEntity(TileEntityGrumBlocker.class, "grum_blocker");
        registerTileEntity(TileEntityBossReSummon.class, "boss_resummon");
        registerTileEntity(TileEntityObsidilithRune.class, "obsidilith_rune");
        registerTileEntity(TileEntityEyePillar.class, "eye_pillar");

    }

    public static void RegisterEntitySpawns() {
        spawnRate(EntityDarkAssassin.class, EnumCreatureType.MONSTER, ModConfig.assassin_spawn_rate, 1, 2, BiomeDictionary.Type.SNOWY);
        spawnRate(EntityDarkAssassin.class, EnumCreatureType.MONSTER, ModConfig.assassin_spawn_rate, 1, 2, BiomeDictionary.Type.FOREST);
        spawnRate(EntityDarkAssassin.class, EnumCreatureType.MONSTER, ModConfig.assassin_spawn_rate, 1, 2, BiomeDictionary.Type.PLAINS);
        spawnRate(EntityDarkAssassin.class, EnumCreatureType.MONSTER, ModConfig.assassin_spawn_rate, 1, 2, BiomeDictionary.Type.SANDY);
        spawnRate(EntityDarkSorcerer.class, EnumCreatureType.MONSTER, ModConfig.sorcerer_spawn_rate, 1, 1, BiomeDictionary.Type.SNOWY);
        spawnRate(EntityDarkSorcerer.class, EnumCreatureType.MONSTER, ModConfig.sorcerer_spawn_rate, 1, 1, BiomeDictionary.Type.FOREST);
        spawnRate(EntityDarkSorcerer.class, EnumCreatureType.MONSTER, ModConfig.sorcerer_spawn_rate, 1, 1, BiomeDictionary.Type.PLAINS);
        spawnRate(EntityDarkSorcerer.class, EnumCreatureType.MONSTER, ModConfig.sorcerer_spawn_rate, 1, 1, BiomeDictionary.Type.SANDY);
    }


    private static void spawnRate(Class<? extends EntityLiving> entityClass, EnumCreatureType creatureType, int weight, int min, int max, BiomeDictionary.Type biomesAllowed) {
        for(Biome biome: BiomeDictionary.getBiomes(biomesAllowed)) {
            if(biome != null && weight > 0) {
                EntityRegistry.addSpawn(entityClass, weight, min, max, creatureType, biome);

            }
        }
    }


    private static void spawnRateBiomeSpecific(Class<? extends EntityLiving> entityClass, EnumCreatureType creatureType, int weight, int min, int max, Biome biome) {
        if(biome != null && weight > 0) {
            EntityRegistry.addSpawn(entityClass, weight, min, max, creatureType, biome);

        }

    }

    public static String getID(Class<? extends Entity> entity) {
        if (ID_MAP.containsKey(entity)) {
            return ModReference.MOD_ID + ":" + ID_MAP.get(entity);
        }
        throw new IllegalArgumentException("Mapping of an entity has not be registered for the maelstrom mod spawner system.");
    }

    private static void registerEntityWithID(String name, Class<? extends Entity> entity, int id, int range, Vec3i eggColor) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true, eggColor.getX(), eggColor.getY());
        ID_MAP.put(entity, name);
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, Vec3i eggColor) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true, eggColor.getX(), eggColor.getY());
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true);
    }

    private static void registerFastProjectile(String name, Class<? extends Entity> entity, int id, int range) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, false);
    }

    private static void registerTileEntity(Class<? extends TileEntity> entity, String name) {
        GameRegistry.registerTileEntity(entity, new ResourceLocation(ModReference.MOD_ID + ":" + name));
    }
}
