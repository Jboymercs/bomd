package com.dungeon_additions.da.util.handlers;

import com.dungeon_additions.da.animation.example.EntityExample;
import com.dungeon_additions.da.animation.example.ModelExample;
import com.dungeon_additions.da.animation.example.exampletwo.EntityEverator;
import com.dungeon_additions.da.animation.example.exampletwo.ModelEverator;
import com.dungeon_additions.da.entity.EntityNetherAbberrant;
import com.dungeon_additions.da.entity.ProjectileEndlessEnderpearl;
import com.dungeon_additions.da.entity.blossom.*;
import com.dungeon_additions.da.entity.boss.EntityWreathKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityPyre;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileFlameSling;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileTrackingFlame;
import com.dungeon_additions.da.entity.mini_blossom.EntityDart;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.projectiles.ProjectileAbberrantAttack;
import com.dungeon_additions.da.entity.render.*;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightRapier;
import com.dungeon_additions.da.entity.util.EntityBossSpawner;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
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

    public static void registerProjectileRenderers() {
        registerProjectileRenderer(ProjectileVoidLeaf.class, ModItems.VOID_LEAF);
        registerProjectileRenderer(ProjectileSporeBomb.class, ModItems.SPORE_BALL);
        registerProjectileRenderer(ProjectileAbberrantAttack.class, ModItems.PROJECTILE_FLAME);
        registerProjectileRenderer(ProjectileEndlessEnderpearl.class, ModItems.ENDLESS_PEARL);
        registerProjectileRenderer(ProjectileFlameSling.class, ModItems.INVISISBLE_ITEM);
        registerProjectileRenderer(ProjectileTrackingFlame.class, ModItems.PROJECTILE_FLAME);
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
    }

}
