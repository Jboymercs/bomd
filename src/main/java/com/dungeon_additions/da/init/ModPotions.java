package com.dungeon_additions.da.init;

import com.dungeon_additions.da.potion.*;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public class ModPotions {


    private ModPotions(){}

    public static PotionEffectGoldenDevotion GOLDEN_DEVOTION = (PotionEffectGoldenDevotion) new PotionEffectGoldenDevotion("golden_devotion", false, 0);
    public static PotionEffectGoldenVow GOLDEN_VOW = (PotionEffectGoldenVow) new PotionEffectGoldenVow("golden_vow",false, 0);
    public static PotionEffectMarked HUNTERS_MARK = (PotionEffectMarked) new PotionEffectMarked("hunters_mark",true, 0);
    public static PotionPoisonGarnish POISON_GARNISH = (PotionPoisonGarnish) new PotionPoisonGarnish("poison_garnish", false, 0);
    public static PotionEffectFieryRespite FIERY_RESPITE = (PotionEffectFieryRespite) new PotionEffectFieryRespite("fiery_respite", false, 0);


    public static PotionType hunters_mark = new PotionType("potionHuntersMark", new PotionEffect[]{new PotionEffect(ModPotions.HUNTERS_MARK, 300)}).setRegistryName("hunters_marked");

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(
                GOLDEN_DEVOTION,
                GOLDEN_VOW,
                HUNTERS_MARK,
                POISON_GARNISH,
                FIERY_RESPITE
        );
    }

    @SubscribeEvent
    public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
        event.getRegistry().registerAll(
                hunters_mark
        );

    }

}
