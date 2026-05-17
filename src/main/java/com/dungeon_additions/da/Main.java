package com.dungeon_additions.da;


import com.dungeon_additions.da.capabilities.CapabilityItemAnimations;
import com.dungeon_additions.da.capabilities.CapabilityPlayerFalter;
import com.dungeon_additions.da.capabilities.CapabilityPlayerSwing;
import com.dungeon_additions.da.init.ModEntities;
import com.dungeon_additions.da.init.ModRecipes;
import com.dungeon_additions.da.proxy.CommonProxy;
import com.dungeon_additions.da.util.DALogger;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.commands.CommandLocateMod;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.dungeon_additions.da.util.handlers.StructureHandler;
import com.dungeon_additions.da.world.ModWorldGen;
import com.dungeon_additions.da.world.ore_gen.BOMDOreGen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import software.bernie.geckolib3.GeckoLib;

@Mod(modid = ModReference.MOD_ID, name = ModReference.NAME, version = ModReference.VERSION)
public class Main {


    @SidedProxy(clientSide = ModReference.CLIENT_PROXY_CLASS, serverSide = ModReference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;
    public static SimpleNetworkWrapper network;


    public static <MSG extends IMessage> void sendMSGToAll(MSG message) {

      //  for(EntityPlayerMP playerMP : Minecraft.getMinecraft().) {
          //  sendNonLocal(message, playerMP);
      //  }
        //network.sendToAll(message);
    }


    public static <MSG extends IMessage> void sendNonLocal(MSG message, EntityPlayerMP playerMP) {
     //   network.sendTo(message, playerMP);
    }
    @Mod.Instance
    public static Main instance;

    public Main() {
        DALogger.clearLog();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GeckoLib.initialize();
        SoundsHandler.registerSounds();
        proxy.init();
        ModEntities.registerEntities();
        ModEntities.RegisterEntitySpawns();
        GameRegistry.registerWorldGenerator(new ModWorldGen(), 1);
        GameRegistry.registerWorldGenerator(new BOMDOreGen(), 1);
        proxy.registerKeyBindings();
        //
        CapabilityManager.INSTANCE.register(CapabilityPlayerFalter.IPlayerFalterCapability.class, new CapabilityPlayerFalter.Storage(), CapabilityPlayerFalter.DAPlayerFalterMethods::new);
        CapabilityManager.INSTANCE.register(CapabilityItemAnimations.ICapabilityItemAnimations.class, new CapabilityItemAnimations.Storage(), CapabilityItemAnimations.AnimationMethods::new);
        CapabilityManager.INSTANCE.register(CapabilityPlayerSwing.IPlayerSwingCapability.class, new CapabilityPlayerSwing.Storage(), CapabilityPlayerSwing.DAPlayerSwingMethods::new);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        StructureHandler.handleStructureRegistries();
        ModRecipes.init();
        proxy.registerEventHandlers();
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        // register server commands
        event.registerServerCommand(new CommandLocateMod());
    }
}
