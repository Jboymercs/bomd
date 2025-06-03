package com.dungeon_additions.da.init;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityFireResistantItems;
import com.dungeon_additions.da.entity.EntityNetherAbberrant;
import com.dungeon_additions.da.entity.ProjectileEndlessEnderpearl;
import com.dungeon_additions.da.entity.blossom.*;
import com.dungeon_additions.da.entity.boss.EntityWreathKnight;
import com.dungeon_additions.da.entity.dark_dungeon.*;
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
import com.dungeon_additions.da.entity.mini_blossom.EntityDart;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.night_lich.*;
import com.dungeon_additions.da.entity.night_lich.EntityLichSpawn;
import com.dungeon_additions.da.entity.player.EntityWyrkLazer;
import com.dungeon_additions.da.entity.projectiles.*;
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
import com.dungeon_additions.da.entity.util.EntityBossSpawner;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeForest;
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

    public static Vec3i dark_royal = new Vec3i(0x233547,0xb42507,0);
    public static void registerEntities() {
        //One day, you'll be back in game my son
       // registerEntityWithID("wreath_knight", EntityWreathKnight.class, ENTITY_START_ID++, 50, void_blossom);
        registerEntityWithID("void_blossom", EntityVoidBlossom.class, ENTITY_START_ID++, 50, void_blossom);
        registerEntityWithID("mini_blossom", EntityMiniBlossom.class, ENTITY_START_ID++, 50, mini_void_blossom);
        registerEntityWithID("nether_aberant", EntityNetherAbberrant.class, ENTITY_START_ID++, 50, nether_spirit);
        registerEntity("spore_cloud", EntityGenericWave.class, ENTITY_START_ID++, 50);
        registerEntity("void_spike", EntityVoidSpike.class, ENTITY_START_ID++, 50);
        registerEntity("void_leaf", ProjectileVoidLeaf.class, PROJECTILE_START_ID++, 30);
        registerEntity("spore_bomb", ProjectileSporeBomb.class, PROJECTILE_START_ID++, 30);
        registerEntity("abberrant_projectile", ProjectileAbberrantAttack.class, PROJECTILE_START_ID++, 30);
        registerEntity("pearl_projectile", ProjectileEndlessEnderpearl.class, PROJECTILE_START_ID++, 30);
        registerEntity("dart_entity", EntityDart.class, ENTITY_START_ID++, 50);
        registerEntity("boss_spawner", EntityBossSpawner.class, ENTITY_START_ID++, 50);
        registerEntity("aoe_tile", EntityMoveTile.class, ENTITY_START_ID++, 50);
            registerEntity("flame_sling", ProjectileFlameSling.class, ENTITY_START_ID++, 50);
            registerEntity("flame_projectile", ProjectileTrackingFlame.class, ENTITY_START_ID++, 60);
            registerEntity("nether_pyre", EntityPyre.class, ENTITY_START_ID++, 50);
            registerEntity("fire_resist_item", EntityFireResistantItems.class, ENTITY_START_ID++, 60);
            registerEntityWithID("flame_knight", EntityFlameKnight.class, ENTITY_START_ID++, 50, kobf);
            registerEntityWithID("ancient_knight", EntityRotKnight.class, ENTITY_START_ID++, 50, rot_knights);
            registerEntityWithID("ancient_knight_rapier", EntityRotKnightRapier.class, ENTITY_START_ID++, 50 ,rot_knight_rapier);
            registerEntityWithID("ancient_fallen", EntityRotKnightBoss.class, ENTITY_START_ID++, 50, rot_knight_boss);
            registerEntity("rot_spike", EntityRotSpike.class, ENTITY_START_ID++, 60);
            registerEntity("soul_star", EntitySoulStar.class, ENTITY_START_ID++, 60);
            registerEntity("lily_locator", EntityLily.class, ENTITY_START_ID++, 60);
            registerEntityWithID("night_lich", EntityNightLich.class, ENTITY_START_ID++, 60, night_lich);
            registerEntity("magic_missile", ProjectileMagicMissile.class, ENTITY_START_ID++, 60);
            registerEntity("magic_fireball", ProjectileMagicFireBall.class, ENTITY_START_ID++, 60);
            registerEntity("lich_staff", EntityLichStaffAOE.class, ENTITY_START_ID++, 60);
            registerEntity("track_missile", ProjectileTrackingMagicMissile.class, ENTITY_START_ID++, 60);
            registerEntity("ground_missile", ProjectileMagicGround.class, ENTITY_START_ID++, 60);
            registerEntity("lich_spawn", EntityLichSpawn.class, ENTITY_START_ID++, 60);
            registerEntityWithID("wyrk", EntityWyrk.class, ENTITY_START_ID++, 70, wyrk_mob);
            registerEntity("ice_spike", EntityIcicleSpike.class, ENTITY_START_ID++, 70);
            registerEntity("ice_bullet", ProjectileFrostBullet.class, ENTITY_START_ID++, 70);
            registerEntityWithID("frost_draugr", EntityDraugr.class, ENTITY_START_ID++, 70, draugr_melee);
            registerEntityWithID("frost_draugr_ranger", EntityDraugrRanger.class, ENTITY_START_ID++, 70, draugr_ranged);
            registerEntity("soul_bullet", ProjectileSoul.class, ENTITY_START_ID++, 70);
            registerEntityWithID("frost_draugr_elite", EntityEliteDraugr.class, ENTITY_START_ID++, 70, draugr_champion);
            registerEntity("frost_locator", EntityFrozenCastleLocator.class, ENTITY_START_ID++, 70);
            registerEntityWithID("great_wyrk", EntityGreatWyrk.class, ENTITY_START_ID++, 70, ancient_wyrk);
            registerEntity("frost_magic", ProjectileFrostGround.class, ENTITY_START_ID++, 80);
            registerEntity("wyrk_foot", EntityWyrkFoot.class, ENTITY_START_ID++, 80);
            registerEntity("wyrk_lazer", EntityWyrkLazer.class, ENTITY_START_ID++, 80);
            registerEntityWithID("imperial_halberd", EntityImperialHalberd.class, ENTITY_START_ID++, 80, imperial_halberd);
            registerEntity("sky_lightning_bolt", EntitySkyBolt.class, ENTITY_START_ID++, 80);
            registerEntityWithID("imperial_sword", EntityImperialSword.class, ENTITY_START_ID++, 80, imperial_sword);
            registerEntity("sky_arrow", EntitySkyArrow.class, ENTITY_START_ID++, 80);
            registerEntity("sky_tornado", EntitySkyTornado.class, ENTITY_START_ID++, 80);
            registerEntityWithID("trident_gargoyle", EntityTridentGargoyle.class, ENTITY_START_ID++, 80, farum_gargoyle);
            registerEntity("farum_dart", EntityFarumSpike.class, ENTITY_START_ID++, 90);
            registerEntityWithID("mage_gargoyle", EntityMageGargoyle.class, ENTITY_START_ID++, 90, farum_elder);
            registerEntity("light_ring_projectile", ProjectileLightRing.class, ENTITY_START_ID++, 90);
            registerEntity("gargoyle_lazer", EntityGargoyleLazer.class, ENTITY_START_ID++, 90);
            registerEntity("friend_wyrk", EntityFriendWyrk.class, ENTITY_START_ID++, 90);
            registerEntityWithID("dark_assassin", EntityDarkAssassin.class, ENTITY_START_ID++, 90, dark_assassin);
            registerEntity("shadow_hand", EntityShadowHand.class, ENTITY_START_ID++, 90);
            registerEntityWithID("high_king_drake", EntityHighKingDrake.class, ENTITY_START_ID++, 90, high_drake);
            registerEntity("storm_tornado", ProjectileStormBreath.class, ENTITY_START_ID++, 90);
            registerEntity("storm_breath", ProjectileStormWind.class, ENTITY_START_ID++, 90);
            registerEntity("dragon_aoe", EntityDragonAOE.class, ENTITY_START_ID++, 100);
            registerEntity("dragon_special", EntityDragonSpecial.class, ENTITY_START_ID++, 100);
            registerEntity("sky_locator_projectile", EntitySkyDungeonLocator.class, ENTITY_START_ID++, 100);
            registerEntityWithID("high_king", EntityHighKing.class, ENTITY_START_ID++, 100, high_king);
            registerEntity("king_holy_aoe", EntityKingHolyAOE.class, ENTITY_START_ID++, 100);
            registerEntity("king_holy_projectile", EntityKingHolyWave.class, ENTITY_START_ID++, 100);
            registerEntity("blood_pile", EntityBloodPile.class, ENTITY_START_ID++, 100);
            registerEntity("blood_ball_proj", ProjectileKingBlood.class, ENTITY_START_ID++, 100);
            registerEntity("imperial_knight_friendly", EntityFriendlyHalberd.class, ENTITY_START_ID++, 100);
            registerEntity("burning_flame_projectile", EntityBurningFlameArenaLocator.class, ENTITY_START_ID++, 100);
            registerEntityWithID("incendium_spirit", EntityIncendium.class, ENTITY_START_ID++, 110, incendium_spirit);
            registerEntityWithID("bareant", EntityBareant.class, ENTITY_START_ID++, 110, bareant_spirit);
            registerEntityWithID("volatile_spirit", EntityVolatileSpirit.class, ENTITY_START_ID++, 110, volactile_spirit);
            registerEntity("flame_spit", ProjectileFlameSpit.class, ENTITY_START_ID++, 110);
            registerEntity("volatile_orb", EntityFlameOrb.class, ENTITY_START_ID++, 110);
            registerEntity("flame_blade_proj", ProjectileFlameBlade.class, ENTITY_START_ID++, 110);
            registerEntityWithID("dark_sorcerer", EntityDarkSorcerer.class, ENTITY_START_ID++, 110, dark_sorcerer);
            registerEntity("proj_dark_matter", ProjectileDarkMatter.class, ENTITY_START_ID++, 110);
            registerEntityWithID("dark_royal", EntityDarkRoyal.class, ENTITY_START_ID++, 110, dark_royal);


        registerTileEntity(TileEntitySporeBlossom.class, "spore_blossom");
        registerTileEntity(TileEntityMegaStructure.class, "mega_structure");
        registerTileEntity(TileEntityDisappearingSpawner.class, "disappearing_spawner_entity");
        registerTileEntity(TileEntityUpdater.class, "block_updates_entity");
        registerTileEntity(TileEntityUpdaterFrost.class, "block_updates_entity_frost");
        registerTileEntity(TileEntityLichSpawner.class, "lich_spawner_blocks");
        registerTileEntity(TileEntityLevitationBlock.class, "levitation_block_entity");
        registerTileEntity(TileEntityFrostBrick.class, "frost_brick_entity");
        registerTileEntity(TileEntityPuzzleMirror.class, "mirror_puzzle");

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
