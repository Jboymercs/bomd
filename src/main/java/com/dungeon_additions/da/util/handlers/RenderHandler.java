package com.dungeon_additions.da.util.handlers;

import com.dungeon_additions.da.animation.example.EntityExample;
import com.dungeon_additions.da.animation.example.ModelExample;
import com.dungeon_additions.da.animation.example.exampletwo.EntityEverator;
import com.dungeon_additions.da.animation.example.exampletwo.ModelEverator;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.EntityNetherAbberrant;
import com.dungeon_additions.da.entity.ProjectileEndlessEnderpearl;
import com.dungeon_additions.da.entity.blossom.*;
import com.dungeon_additions.da.entity.boss.EntityWreathKnight;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkAssassin;
import com.dungeon_additions.da.entity.dark_dungeon.EntityShadowHand;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityPyre;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
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
import com.dungeon_additions.da.entity.render.*;
import com.dungeon_additions.da.entity.render.dark_dungeon.RenderDarkAssassin;
import com.dungeon_additions.da.entity.render.dark_dungeon.RenderShadowHand;
import com.dungeon_additions.da.entity.render.frost_dungeon.*;
import com.dungeon_additions.da.entity.render.lich.RenderLichSpawn;
import com.dungeon_additions.da.entity.render.lich.RenderLichStaff;
import com.dungeon_additions.da.entity.render.lich.RenderNightLich;
import com.dungeon_additions.da.entity.render.sky_dungeon.*;
import com.dungeon_additions.da.entity.render.sky_dungeon.boss.RenderHighKing;
import com.dungeon_additions.da.entity.render.sky_dungeon.boss.RenderHighKingDrake;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightBoss;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightRapier;
import com.dungeon_additions.da.entity.rot_knights.EntityRotSpike;
import com.dungeon_additions.da.entity.sky_dungeon.*;
import com.dungeon_additions.da.entity.sky_dungeon.friendly.EntityFriendlyHalberd;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.*;
import com.dungeon_additions.da.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.function.Function;

public class RenderHandler {

    private static <T extends Entity, U extends ModelBase, V extends RenderModEntity> void registerModEntityRenderer(Class<T> entityClass, U model, String... textures) {
        registerModEntityRenderer(entityClass, (manager) -> new RenderModEntity(manager, model, textures));
    }

    private static <T extends Entity, U extends ModelBase, V extends RenderModEntity> void registerModEntityRenderer(Class<T> entityClass, Function<RenderManager, Render<? super T>> renderClass) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new IRenderFactory<T>() {
            @Override
            public Render<? super T> createRenderFor(RenderManager manager) {
                return renderClass.apply(manager);
            }
        });
    }

    private static <T extends Entity> void registerProjectileRenderer(Class<T> projectileClass) {
        registerProjectileRenderer(projectileClass, null);
    }

    /**
     * Makes a projectile render with the given item
     *
     * @param projectileClass
     */
    private static <T extends Entity> void registerProjectileRenderer(Class<T> projectileClass, Item item) {
        RenderingRegistry.registerEntityRenderingHandler(projectileClass, new IRenderFactory<T>() {
            @Override
            public Render<? super T> createRenderFor(RenderManager manager) {
                return new RenderProjectile<T>(manager, Minecraft.getMinecraft().getRenderItem(), item);
            }
        });
    }

    private static <T extends Entity> void registerProjectileRenderer3DModel(Class<T> projectileClass, Item item) {
        RenderingRegistry.registerEntityRenderingHandler(projectileClass, new IRenderFactory<T>() {
            @Override
            public Render<? super T> createRenderFor(RenderManager manager) {
                return new RenderHolyWaveProjectile<>(manager, Minecraft.getMinecraft().getRenderItem(), item);
            }
        });
    }

    public static void registerProjectileRenderers() {
        registerProjectileRenderer(ProjectileVoidLeaf.class, ModItems.VOID_LEAF);
        registerProjectileRenderer(ProjectileSporeBomb.class, ModItems.SPORE_BALL);
        registerProjectileRenderer(ProjectileAbberrantAttack.class, ModItems.PROJECTILE_FLAME);
        registerProjectileRenderer(ProjectileEndlessEnderpearl.class, ModItems.ENDLESS_PEARL);
        registerProjectileRenderer(ProjectileFlameSling.class, ModItems.INVISISBLE_ITEM);
        registerProjectileRenderer(ProjectileTrackingFlame.class, ModItems.PROJECTILE_FLAME);
        registerProjectileRenderer(EntitySoulStar.class, ModItems.SOUL_STAR);
        registerProjectileRenderer(EntityLily.class, ModItems.INVISISBLE_ITEM);
        registerProjectileRenderer(ProjectileMagicMissile.class, ModItems.MAGIC_PROJECTILE);
        registerProjectileRenderer(ProjectileMagicFireBall.class, ModItems.MAGIC_FIREBALL);
        registerProjectileRenderer(ProjectileTrackingMagicMissile.class, ModItems.MAGIC_TRACK_PROJECTILE);
        registerProjectileRenderer(ProjectileMagicGround.class, ModItems.INVISISBLE_ITEM);
        registerProjectileRenderer(ProjectileFrostBullet.class, ModItems.FROST_PROJECTILE);
        registerProjectileRenderer(ProjectileSoul.class, ModItems.INVISISBLE_ITEM);
        registerProjectileRenderer(EntityFrozenCastleLocator.class, ModItems.FROZEN_CASTLE_LOCATOR);
        registerProjectileRenderer(ProjectileFrostGround.class, ModItems.INVISISBLE_ITEM);
        registerProjectileRenderer(ProjectileLightRing.class, ModItems.LIGHT_RING_PROJECTILE);
        registerProjectileRenderer(ProjectileStormBreath.class, ModItems.STORM_TORNADO_PROJECTILE);
        registerProjectileRenderer(ProjectileStormWind.class, ModItems.INVISISBLE_ITEM);
        registerProjectileRenderer(EntitySkyDungeonLocator.class, ModItems.SKY_LOCATOR_PROJECTILE);
        registerProjectileRenderer3DModel(EntityKingHolyWave.class, ModItems.HOLY_WAVE_PROJ);
        registerProjectileRenderer(ProjectileKingBlood.class, ModItems.BLOOD_BALL_PROJ);
    }

    //Handles Rendering
    public static void registerEntityRenderers() {
        //Example Entity Rendering
        registerModEntityRenderer(EntityExample.class, new ModelExample(), "example.png");
        //Example 2 Everator
        registerModEntityRenderer(EntityEverator.class, new ModelEverator(), "everator.png");
    }

    public static void registerGeoEntityRenderers() {
        //Test Boss AI
        RenderingRegistry.registerEntityRenderingHandler(EntityWreathKnight.class, RenderWreathKnight::new);
        // Void Blossom - Lush Dimension
        RenderingRegistry.registerEntityRenderingHandler(EntityVoidBlossom.class, RenderVoidBlossom::new);
        //Void Spike
        RenderingRegistry.registerEntityRenderingHandler(EntityVoidSpike.class, RenderVoidSpike::new);
        //Spore Bomb Wave
        RenderingRegistry.registerEntityRenderingHandler(EntityGenericWave.class, RenderGenericWave::new);
        //Mini Blossom
        RenderingRegistry.registerEntityRenderingHandler(EntityMiniBlossom.class, RenderMiniBlossom::new);
        //Dart
        RenderingRegistry.registerEntityRenderingHandler(EntityDart.class, RenderDartBase::new);
        //Farum Spike
        RenderingRegistry.registerEntityRenderingHandler(EntityFarumSpike.class, RenderFarumSpike::new);
        //Sky Arrow
        RenderingRegistry.registerEntityRenderingHandler(EntitySkyArrow.class, RenderSkyArrow::new);
        //Nether Abberrant
        RenderingRegistry.registerEntityRenderingHandler(EntityNetherAbberrant.class, RenderNetherAbberrant::new);
        //Flame Knight
        RenderingRegistry.registerEntityRenderingHandler(EntityFlameKnight.class, RenderFlameKnight::new);
        //AOE tile
        RenderingRegistry.registerEntityRenderingHandler(EntityMoveTile.class, RenderMoveTile::new);
        //EntityPyre
        RenderingRegistry.registerEntityRenderingHandler(EntityPyre.class, RenderPyre::new);
        //Ancient Knight
        RenderingRegistry.registerEntityRenderingHandler(EntityRotKnight.class, RenderRotKnight::new);
        //Ancient Knight Rapier
        RenderingRegistry.registerEntityRenderingHandler(EntityRotKnightRapier.class, RenderRotKnightRapier::new);
        //Ancient Fallen Knight
        RenderingRegistry.registerEntityRenderingHandler(EntityRotKnightBoss.class, RenderRotKnightBoss::new);
        //Rot Spike
        RenderingRegistry.registerEntityRenderingHandler(EntityRotSpike.class, RenderRotSpike::new);
        //Night Lich
        RenderingRegistry.registerEntityRenderingHandler(EntityNightLich.class, RenderNightLich::new);
        //Lich Staff AOE
        RenderingRegistry.registerEntityRenderingHandler(EntityLichStaffAOE.class, RenderLichStaff::new);
        //Lich Spawn
        RenderingRegistry.registerEntityRenderingHandler(EntityLichSpawn.class, RenderLichSpawn::new);
        //Wyrk
        RenderingRegistry.registerEntityRenderingHandler(EntityWyrk.class, RenderWyrk::new);
        //Icicle Spike
        RenderingRegistry.registerEntityRenderingHandler(EntityIcicleSpike.class, RenderIceSpike::new);
        //Frost Draugr
        RenderingRegistry.registerEntityRenderingHandler(EntityDraugr.class, RenderDraugr::new);
        //Frost Draugr Ranger
        RenderingRegistry.registerEntityRenderingHandler(EntityDraugrRanger.class, RenderDraugrRanger::new);
        //Draugr Elite
        RenderingRegistry.registerEntityRenderingHandler(EntityEliteDraugr.class, RenderDraugrElite::new);
        //Great Wyrk
        RenderingRegistry.registerEntityRenderingHandler(EntityGreatWyrk.class, RenderGreatWyrk::new);
        //Wyrk Foot
        RenderingRegistry.registerEntityRenderingHandler(EntityWyrkFoot.class, RenderWyrkFoot::new);
        //Wyrk Lazer
        registerModEntityRenderer(EntityWyrkLazer.class, RenderWyrkLazer::new);
        //Imperial Halberd
        registerModEntityRenderer(EntityImperialHalberd.class, RenderImperialHalberd::new);
        //Sky Bolt
        RenderingRegistry.registerEntityRenderingHandler(EntitySkyBolt.class, RenderSkyBolt::new);
        //Imperial Sword
        RenderingRegistry.registerEntityRenderingHandler(EntityImperialSword.class, RenderImperialSword::new);
        //Sky Tornado
        RenderingRegistry.registerEntityRenderingHandler(EntitySkyTornado.class, RenderSkyTornado::new);
        //
        RenderingRegistry.registerEntityRenderingHandler(EntityTridentGargoyle.class, RenderGargoyleTrident::new);
        //
        RenderingRegistry.registerEntityRenderingHandler(EntityMageGargoyle.class, RenderGargoyleMage::new);
        //
        RenderingRegistry.registerEntityRenderingHandler(EntityGargoyleLazer.class, RenderGargoyleLazer::new);
        //Friendly Wyrk
        RenderingRegistry.registerEntityRenderingHandler(EntityFriendWyrk.class, RenderFriendWyrk::new);
        //Dark Assassin
        RenderingRegistry.registerEntityRenderingHandler(EntityDarkAssassin.class, RenderDarkAssassin::new);
        //Shadow hand
        RenderingRegistry.registerEntityRenderingHandler(EntityShadowHand.class, RenderShadowHand::new);
        //High King Drake
        RenderingRegistry.registerEntityRenderingHandler(EntityHighKingDrake.class, RenderHighKingDrake::new);
        //Dragon AOE
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonAOE.class, RenderDragonAOE::new);
        //Dragon Special Attack
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonSpecial.class, RenderDragonSpecial::new);
        //High King
        RenderingRegistry.registerEntityRenderingHandler(EntityHighKing.class, RenderHighKing::new);
        //High King AOE
        RenderingRegistry.registerEntityRenderingHandler(EntityKingHolyAOE.class, RenderKingHolyAOE::new);
        //Blood Pile
        RenderingRegistry.registerEntityRenderingHandler(EntityBloodPile.class, RenderBloodPile::new);
        //Friendly Imperial Halberd
        RenderingRegistry.registerEntityRenderingHandler(EntityFriendlyHalberd.class, RenderFriendlyHalberd::new);
    }

}
