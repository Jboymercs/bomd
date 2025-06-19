package com.dungeon_additions.da.util.handlers;


import com.dungeon_additions.da.blocks.boss.BossStateMapper;
import com.dungeon_additions.da.blocks.lich.LichStateMapper;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.render.*;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.mapper.AdvancedStateMap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class RegistryHandler {
    private static IForgeRegistry<Item> itemRegistry;
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        itemRegistry = event.getRegistry();
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegister(ModelRegistryEvent event) {
        //Have to call these here due to being geckolib animated models
        ModelLoader.setCustomModelResourceLocation(ModItems.SPORE_BALL, 0, new ModelResourceLocation(ModReference.MOD_ID + ":spore","inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.MAGIC_FIREBALL, 0, new ModelResourceLocation(ModReference.MOD_ID + ":magic_fireball", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.CHAMPION_AXE, 0, new ModelResourceLocation(ModReference.MOD_ID + ":champion_axe", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.DRAUGR_SHIELD, 0, new ModelResourceLocation(ModReference.MOD_ID + ":draugr_shield", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.IMPERIAL_HALBERD_ITEM, 0, new ModelResourceLocation(ModReference.MOD_ID + ":imperial_halberd_item", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.LIGHT_RING_PROJECTILE, 0, new ModelResourceLocation(ModReference.MOD_ID + ":light_ring", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.STORM_TORNADO_PROJECTILE, 0, new ModelResourceLocation(ModReference.MOD_ID + ":storm_tornado", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.HOLY_WAVE_PROJ, 0, new ModelResourceLocation(ModReference.MOD_ID + ":holy_wave", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.SWORD_SPEAR, 0, new ModelResourceLocation(ModReference.MOD_ID + ":sword_spear", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.BLOODY_SWORD_SPEAR, 0, new ModelResourceLocation(ModReference.MOD_ID + ":sword_spear_blood", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.KING_CLAW, 0, new ModelResourceLocation(ModReference.MOD_ID + ":king_claw", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.DRAGON_SHIELD, 0, new ModelResourceLocation(ModReference.MOD_ID + ":dragon_shield", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.FLAME_BLADE, 0, new ModelResourceLocation(ModReference.MOD_ID + ":flame_blade", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.FLAME_BLADE_PROJ, 0, new ModelResourceLocation(ModReference.MOD_ID + ":proj_flame_blade", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.FLAME_SPIT_SHIELD, 0, new ModelResourceLocation(ModReference.MOD_ID + ":flame_shield", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.DARK_METAL_SHIELD, 0, new ModelResourceLocation(ModReference.MOD_ID + ":dark_shield", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.DARK_SICLE, 0, new ModelResourceLocation(ModReference.MOD_ID + ":dark_sicle", "inventory"));
        ModItems.SPORE_BALL.setTileEntityItemStackRenderer(new RenderSpore());
        ModItems.MAGIC_FIREBALL.setTileEntityItemStackRenderer(new RenderMagicFireball());
        ModItems.CHAMPION_AXE.setTileEntityItemStackRenderer(new RenderChampionAxe());
        ModItems.DRAUGR_SHIELD.setTileEntityItemStackRenderer(new RenderDraugrShield());
        ModItems.IMPERIAL_HALBERD_ITEM.setTileEntityItemStackRenderer(new RenderImperialHalberdItem());
        ModItems.LIGHT_RING_PROJECTILE.setTileEntityItemStackRenderer(new RenderLightRing());
        ModItems.STORM_TORNADO_PROJECTILE.setTileEntityItemStackRenderer(new RenderStormTornado());
        ModItems.HOLY_WAVE_PROJ.setTileEntityItemStackRenderer(new RenderHolyWave());
        ModItems.SWORD_SPEAR.setTileEntityItemStackRenderer(new RenderSwordSpeear());
        ModItems.BLOODY_SWORD_SPEAR.setTileEntityItemStackRenderer(new RenderBloodySwordSpear());
        ModItems.KING_CLAW.setTileEntityItemStackRenderer(new RenderKingClaw());
        ModItems.DRAGON_SHIELD.setTileEntityItemStackRenderer(new RenderDragonShield());
        ModItems.FLAME_BLADE.setTileEntityItemStackRenderer(new RenderFlameBlade());
        ModItems.FLAME_BLADE_PROJ.setTileEntityItemStackRenderer(new RenderFlameBladeProj());
        ModItems.FLAME_SPIT_SHIELD.setTileEntityItemStackRenderer(new RenderFlameShield());
        ModItems.DARK_METAL_SHIELD.setTileEntityItemStackRenderer(new RenderDarkShield());
        ModItems.DARK_SICLE.setTileEntityItemStackRenderer(new RenderDarkSicle());
        for (Item item : ModItems.ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }

        for (Block block : ModBlocks.BLOCKS) {
            if (block instanceof IStateMappedBlock) {
                AdvancedStateMap.Builder builder = new AdvancedStateMap.Builder();
                ((IStateMappedBlock) block).setStateMapper(builder);
                ModelLoader.setCustomStateMapper(block, builder.build());
            }

            if (block instanceof IHasModel) {
                ((IHasModel) block).registerModels();
            }
        }

        ModelLoader.setCustomStateMapper(ModBlocks.BOSS_RESUMMON_BLOCK, new BossStateMapper());
        ModelLoader.setCustomStateMapper(ModBlocks.LICH_SOUL_STAR_BLOCK, new LichStateMapper());
    }



    public interface IStateMappedBlock {
        /**
         * Sets the statemap
         *
         * @param builder
         */
        @SideOnly(Side.CLIENT)
        void setStateMapper(AdvancedStateMap.Builder builder);
    }
}
