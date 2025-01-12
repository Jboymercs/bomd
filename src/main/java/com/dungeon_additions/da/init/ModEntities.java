package com.dungeon_additions.da.init;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.EntityFireResistantItems;
import com.dungeon_additions.da.entity.EntityNetherAbberrant;
import com.dungeon_additions.da.entity.ProjectileEndlessEnderpearl;
import com.dungeon_additions.da.entity.blossom.*;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityPyre;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileFlameSling;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileTrackingFlame;
import com.dungeon_additions.da.entity.frost_dungeon.EntityIcicleSpike;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.ProjectileFrostBullet;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugrRanger;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityEliteDraugr;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.ProjectileSoul;
import com.dungeon_additions.da.entity.mini_blossom.EntityDart;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.night_lich.*;
import com.dungeon_additions.da.entity.night_lich.EntityLichSpawn;
import com.dungeon_additions.da.entity.projectiles.EntityFrozenCastleLocator;
import com.dungeon_additions.da.entity.projectiles.EntityLily;
import com.dungeon_additions.da.entity.projectiles.EntitySoulStar;
import com.dungeon_additions.da.entity.projectiles.ProjectileAbberrantAttack;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightBoss;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightRapier;
import com.dungeon_additions.da.entity.rot_knights.EntityRotSpike;
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
    public static Vec3i rot_knights = new Vec3i(0x63615e,0x66005,0);
    public static Vec3i kobf = new Vec3i(0xbf922a, 0xeb4034, 0);
    public static Vec3i rot_knight_rapier = new Vec3i(0x63615e,0xc7a548,0);
    public static Vec3i rot_knight_boss = new Vec3i(0x63615e, 0x03ff18, 0);
    public static Vec3i night_lich = new Vec3i(0x021a1a,0x6eebeb,0);
    public static void registerEntities() {
      //  registerEntityWithID("wreath_knight", EntityWreathKnight.class, ENTITY_START_ID++, 50, knight_mobs);
        registerEntityWithID("void_blossom", EntityVoidBlossom.class, ENTITY_START_ID++, 50, void_blossom);
        registerEntityWithID("mini_blossom", EntityMiniBlossom.class, ENTITY_START_ID++, 50, mini_void_blossom);
        registerEntityWithID("nether_aberant", EntityNetherAbberrant.class, ENTITY_START_ID++, 50, nether_spirit);

        //Example Entity
      //  registerEntity("example_entity", EntityExample.class, ENTITY_START_ID++, 50, knight_mobs);

        //Example Entity 2
      //  registerEntity("everator", EntityEverator.class, ENTITY_START_ID++, 50, knight_mobs);
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
            registerEntityWithID("wyrk", EntityWyrk.class, ENTITY_START_ID++, 70, night_lich);
            registerEntity("ice_spike", EntityIcicleSpike.class, ENTITY_START_ID++, 70);
            registerEntity("ice_bullet", ProjectileFrostBullet.class, ENTITY_START_ID++, 70);
            registerEntityWithID("frost_draugr", EntityDraugr.class, ENTITY_START_ID++, 70, night_lich);
            registerEntityWithID("frost_draugr_ranger", EntityDraugrRanger.class, ENTITY_START_ID++, 70, night_lich);
            registerEntity("soul_bullet", ProjectileSoul.class, ENTITY_START_ID++, 70);
            registerEntityWithID("frost_draugr_elite", EntityEliteDraugr.class, ENTITY_START_ID++, 70, night_lich);
            registerEntity("frost_locator", EntityFrozenCastleLocator.class, ENTITY_START_ID++, 70);

        registerTileEntity(TileEntitySporeBlossom.class, "spore_blossom");
        registerTileEntity(TileEntityMegaStructure.class, "mega_structure");
        registerTileEntity(TileEntityDisappearingSpawner.class, "disappearing_spawner_entity");
        registerTileEntity(TileEntityUpdater.class, "block_updates_entity");
        registerTileEntity(TileEntityLichSpawner.class, "lich_spawner_blocks");
        registerTileEntity(TileEntityLevitationBlock.class, "levitation_block_entity");

    }

    public static void RegisterEntitySpawns() {

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
