package com.dungeon_additions.da.util.handlers;


import com.dungeon_additions.da.blocks.lich.LichStateMapper;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.render.RenderMagicFireball;
import com.dungeon_additions.da.items.render.RenderSpore;
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
        ModItems.SPORE_BALL.setTileEntityItemStackRenderer(new RenderSpore());
        ModItems.MAGIC_FIREBALL.setTileEntityItemStackRenderer(new RenderMagicFireball());
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
