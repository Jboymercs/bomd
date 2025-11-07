package com.dungeon_additions.da.init;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.items.*;
import com.dungeon_additions.da.items.armor.*;
import com.dungeon_additions.da.items.armor.dark.ModDarkArmorBase;
import com.dungeon_additions.da.items.armor.dark.ModDarkMetalHelmet;
import com.dungeon_additions.da.items.armor.imperial.ItemImperialArmor;
import com.dungeon_additions.da.items.armor.imperial.ItemImperialChestplate;
import com.dungeon_additions.da.items.armor.novik.ItemApathyrHelmet;
import com.dungeon_additions.da.items.armor.novik.ItemNovikArmor;
import com.dungeon_additions.da.items.armor.novik.ItemNovikChestplate;
import com.dungeon_additions.da.items.armor.novik.ItemNovikHelmet;
import com.dungeon_additions.da.items.food.ItemBeetleMorsel;
import com.dungeon_additions.da.items.food.ItemUncookedFood;
import com.dungeon_additions.da.items.gun.ItemGolemCannon;
import com.dungeon_additions.da.items.gun.ItemSealedTornado;
import com.dungeon_additions.da.items.gun.ItemVoidiantCatalyst;
import com.dungeon_additions.da.items.keys.*;
import com.dungeon_additions.da.items.projectile.*;
import com.dungeon_additions.da.items.shield.*;
import com.dungeon_additions.da.items.tools.*;
import com.dungeon_additions.da.items.trinket.ItemTrinket;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    //Materials
    private static final Item.ToolMaterial CHAMPION_AXE_MATERIAL = EnumHelper.addToolMaterial("champion_set", 3, 874, 7.0F, (float) ModConfig.champion_axe_damage, 15);
    private static final Item.ToolMaterial APATHYR_AXE_MATERIAL = EnumHelper.addToolMaterial("apathyr_axe_set", 3, 974, 7.0F, (float) ModConfig.midnight_reign_damage, 25);
    private static final Item.ToolMaterial VOID_HAMMER_MATERIAL = EnumHelper.addToolMaterial("void_hammer_set", 3, 874, 7.0F, (float) ModConfig.void_hammer_damage, 25);
    private static final Item.ToolMaterial VOID_STAFF_MATERIAL = EnumHelper.addToolMaterial("void_staff_set", 1, 1074, 3.0F, (float) ModConfig.void_staff_damage, 15);
    private static final Item.ToolMaterial IMPERIAL_HALBERD_MATERIAL = EnumHelper.addToolMaterial("imperial_halberd_set", 3, 2463, 8.0F, (float) ModConfig.prosperous_assault_damage, 20);
    private static final Item.ToolMaterial SWORD_SPEAR_MATERIAL = EnumHelper.addToolMaterial("sword_spear_set", 3, 2031, 6.0F, (float) ModConfig.divine_sword_spear_damage, 40);
    private static final Item.ToolMaterial BLOODY_SWORD_SPEAR_MATERIAL = EnumHelper.addToolMaterial("bloody_sword_spear_set", 3, 2031, 7.0F, (float) ModConfig.bloodied_sword_spear_damage, 40);
    private static final Item.ToolMaterial KINGS_CLAW_MATERIAL = EnumHelper.addToolMaterial("king_claw_set", 3, 942, 7.0F, (float) ModConfig.kings_claw_damage, 40);
    private static final Item.ToolMaterial IMPERIAL_SWORD_MATERIAL = EnumHelper.addToolMaterial("imperial_material", 2, 2463, 6.0F, (float) ModConfig.master_parry_sword_damage, 30);
    private static final Item.ToolMaterial DAGGER_MATERIAL = EnumHelper.addToolMaterial("void_material", 2, 720, 6.0F, (float) ModConfig.void_dagger_damage, 45);
    private static final Item.ToolMaterial DARK_DAGGER_MATERIAL = EnumHelper.addToolMaterial("dark_material", 2, 520, 6.0F, (float) ModConfig.rah_void_Dagger_damage, 5);

    private static final Item.ToolMaterial DRAUGR_SWORD = EnumHelper.addToolMaterial("draugr_material", 2, 974, 6.0F, (float) ModConfig.frost_sword_damage, 45);
    private static final Item.ToolMaterial FLAME_BLADE_MATERIAL = EnumHelper.addToolMaterial("flame_blade_material", 2, 1002, 5.0F, (float) ModConfig.flame_blade_damage, 10);

    private static final Item.ToolMaterial DARK_SICLE_MATERIAL = EnumHelper.addToolMaterial("dark_sicle_material", 2, 766, 3.0F, (float) 6, 20);
    private static final Item.ToolMaterial FLAME_SWORD_MATERIAL = EnumHelper.addToolMaterial("flame_sword_material", 2, 988, 6.0F, ModConfig.sword_of_ambition_damage, 70);

    private static final Item.ToolMaterial SOUL_SPEAR_MATERIAL = EnumHelper.addToolMaterial("soul_spear_material", 2, 1200, 5.0F, ModConfig.soul_spear_damage, 10);

    private static final Item.ToolMaterial SOUL_SPEAR_WEAPON_MATERIAL = EnumHelper.addToolMaterial("soul_spear_weapon_material", 2, 1200, 5.0F, ModConfig.soul_weapon_damage, 20);
    private static final Item.ToolMaterial KNIGHT_RAPIER_MATERIAL = EnumHelper.addToolMaterial("knight_rapier_material", 2, 502, 5.0F, (float) ModConfig.rapier_damage, 10);
    private static final Item.ToolMaterial KOPIS_MATERIAL = EnumHelper.addToolMaterial("kopis_material", 1, 1200, 5.0F, (float) 5, 20);
    private static final Item.ToolMaterial NOVIK_MATERIAL = EnumHelper.addToolMaterial("novik_material", 2, 1200, 4.0F, (float) ModConfig.novik_sword_damage, 10);

    private static final ItemArmor.ArmorMaterial FLAME_ARMOR = EnumHelper.addArmorMaterial("flame", ModReference.MOD_ID + ":flame", 320, new int[]{4, 7,9,4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2F);
    private static final ItemArmor.ArmorMaterial DRAUGR_ARMOR = EnumHelper.addArmorMaterial("draugr", ModReference.MOD_ID + "draugr", 375, new int[]{3, 6, 8,3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 2F);
    private static final ItemArmor.ArmorMaterial LICH_HELMET = EnumHelper.addArmorMaterial("night_lich", ModReference.MOD_ID +":night_lich", 275, new int[] {3,6,8,3}, 40, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F);
    private static final ItemArmor.ArmorMaterial INCENDIUM_HELMET_SET = EnumHelper.addArmorMaterial("incendium_h", ModReference.MOD_ID +":incendium_helmet", 375, new int[] {3,6,8,3}, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2F);
    private static final ItemArmor.ArmorMaterial DARK_METAL_SET = EnumHelper.addArmorMaterial("dark_metal", ModReference.MOD_ID +":dark_metal", 451, new int[] {3,5,8,3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.5F);
    private static final ItemArmor.ArmorMaterial NOVIK_SET = EnumHelper.addArmorMaterial("novik", ModReference.MOD_ID +":novik", 351, new int[] {4,6,9,4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2F);
    private static final ItemArmor.ArmorMaterial APATHYR_SET = EnumHelper.addArmorMaterial("apathyr", ModReference.MOD_ID + ":apathyr", 470, new int[]{5,7,10,5}, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.5F);
    private static final ItemArmor.ArmorMaterial WYRK_ARMOR = EnumHelper.addArmorMaterial("wyrk", ModReference.MOD_ID +":wyrk", 325, new int[] {3,6,8,3}, 30, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.5F);
    private static final ItemArmor.ArmorMaterial IMPERIAL_ARMOR = EnumHelper.addArmorMaterial("imperial", ModReference.MOD_ID +":imperial", 670, new int[] {4,7,9,4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.5F);
    private static final ItemArmor.ArmorMaterial VOIDIANT_ARMOR = EnumHelper.addArmorMaterial("voidiant", ModReference.MOD_ID + "obsidian", 870, new int[]{4, 6, 7, 4}, 25, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3F);
    private static final ItemArmor.ArmorMaterial OBSIDIAN_HELMET_MAT = EnumHelper.addArmorMaterial("obsidian", ModReference.MOD_ID +":obsidian_gear", 1023, new int[] {4,7,9,4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 1.5F);
    private static final ItemArmor.ArmorMaterial FLOWER_HELMET_MAT = EnumHelper.addArmorMaterial("flower", ModReference.MOD_ID +":flower", 290, new int[] {2,4,6,2}, 30, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F);
    private static final ItemArmor.ArmorMaterial KINGS_HELMET_MAT = EnumHelper.addArmorMaterial("king", ModReference.MOD_ID +":king", 1023, new int[] {4,7,9,4}, 20, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 1.5F);
    public ModItems() {


    }
    public static final List<Item> ITEMS = new ArrayList<Item>();

    //Crafting Items
    public static final Item COPPER_COIN = new ItemCraftingMaterial("copper_coin","trader_coin_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item SILVER_COIN = new ItemCraftingMaterial("silver_coin","trader_coin_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item GOLDEN_COIN = new ItemCraftingMaterial("golden_coin","trader_coin_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ROT_KNIGHT_FRAGMENT = new ItemCraftingMaterial("knight_shard", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ROT_KNIGHT_INGOT = new ItemCraftingMaterial("knight_ingot", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item VOID_THORNS = new ItemCraftingMaterial("void_thorns", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ADVENTURE_METAL = new ItemCraftingMaterial("adventure_metal", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item MAGIC_LEATHER = new ItemCraftingMaterial("magic_leather","crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DARK_MANA = new ItemCraftingMaterial("dark_mana", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DARK_CORE = new ItemCraftingMaterial("dark_core", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item CRYPT_PLATE = new ItemCraftingMaterial("crypt_plate", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item RED_ANICIENT_MANA = new ItemCraftingMaterial("red_ancient_mana", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FROST_SHARD = new ItemCraftingMaterial("frost_shard", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FROST_INGOT = new ItemCraftingMaterial("frost_ingot", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DRAUGR_METAL = new ItemCraftingMaterial("draugr_shard", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DRAUGR_INGOT = new ItemCraftingMaterial("draugr_ingot", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item GREAT_WYRK_CRYSTAL = new ItemCraftingMaterial("great_wyrk_mana", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item BEETLE_SHELL = new ItemCraftingMaterial("beetle_shell", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item AEGYPTIA_METAL = new ItemCraftingMaterial("aegyptia_metal", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item AEGYPTIA_BONE = new ItemCraftingMaterial("aegyptia_bone", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item AEGYPTIA_METAL_INGOT = new ItemCraftingMaterial("aegyptia_metal_ingot", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item GLADIATOR_IDOL = new ItemCraftingMaterial("gladiator_idol", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item INCENDIUM_CORE = new ItemCraftingMaterial("incendium_core", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item VOLATILE_PEARL = new ItemCraftingMaterial("volatile_orb", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ABBERRANT_EYE = new ItemCraftingMaterial("abberrant_eye", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FLAME_METAL_SCRAP = new ItemCraftingMaterial("flame_metal_scrap", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ANCIENT_MANA = new ItemCraftingMaterial("ancient_mana", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item NOVIK_PLATE = new ItemCraftingMaterial("novik_plate", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item SENTINEL_PART = new ItemCraftingMaterial("sentinel_part","crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item GAELON_SHARD = new ItemCraftingMaterial("gaelon_shard", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item GAELON_INGOT = new ItemCraftingMaterial("gaelon_ingot", "gaelon_ingot_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item UNBRIDLED_SOUL = new ItemCraftingMaterial("unbridled_soul", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item VOIDIANT_CORE = new ItemCraftingMaterial("voidiant_core", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item OBSIDIAN_HEART = new ItemCraftingMaterial("obsdian_heart", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item VOIDCLYSM_CRYSTAL = new ItemCraftingMaterial("voidclysm_crystal", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item LIGHT_MANA = new ItemCraftingMaterial("light_mana", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item LITIC_SHARD = new ItemCraftingMaterial("lightning_shard", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item LITIC_INGOT = new ItemCraftingMaterial("lightning_ingot", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DIVINE_SCROLL = new ItemCraftingMaterial("divine_scroll", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DIVINE_METAL = new ItemCraftingMaterial("divine_metal", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DRAGON_SCALES = new ItemCraftingMaterial("dragon_scale", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);

    //Food
    public static final Item VOID_FRUIT = new ItemFoodBase("crystal_fruit", "regen_fruit", 5, 1, false).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item GLOW_BERRY = new ItemGlowBerry("glow_berry", 4, 0.4F, false, ModBlocks.AZAELA_VINES).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item UNCOOKED_BEETLE_MORSEL = new ItemUncookedFood("beetle_morsel", 3, 0, false).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 300), 0.2F).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item COOKED_BEETLE_MORSEL = new ItemBeetleMorsel("cooked_beetle_morsel", "cooked_beetle_morsel_desc", 7, 0.5F, false);

    public static final Item ENFLAMED_MEAT = new ItemEnflamedMeat("enflamed_meat", "flame_meat", 10, 0.8F, false).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item WYRK_SNACK = new ItemWyrkSnack("wyrk_snack", "wyrk_snack_desc", 6, 0.7F, false).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item CRUMBLE_BERRIES = new ItemCrumbleBerries("crumble_berries", "crumble_berries_desc", 4, 0.5F, false).setCreativeTab(DungeonAdditionsTab.ALL);

    //Utility & Locator Items (Not Weapons)
    public static final Item ENDLESS_PEARL = new ItemEndlessEnderpearl("endless_enderpearl", "pearl_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item SEALED_TORNADO_BOTTLE = new ItemSealedTornado("sealed_tornado","sealed_tornado_desc", DungeonAdditionsTab.ALL);
    public static final Item SEALED_SPEAR_WAVE_BOTTLE = new ItemSealedSpearWave("sealed_spear_wave", "sealed_spear_wave_desc", DungeonAdditionsTab.ALL);
    public static final Item SEALED_FLAME_BOTTLE = new ItemSealedFlame("sealed_flame", "sealed_flame_desc", DungeonAdditionsTab.ALL);
    public static final Item FROZEN_CRYSTAL_TRINKET = new ItemTrinket("crystal_trinket", "crystal_trinket_desc", 128);
    public static final Item GLASS_CANNON_TRINKET = new ItemTrinket("glass_cannon_trinket", "glass_cannon_trinket_desc", 128);
    public static final Item SPEED_BOOTS_TRINKET = new ItemTrinket("boots_trinket", "boots_trinket_desc", 128);
    public static final Item FLAMES_RAGE_TRINKET = new ItemTrinket("flame_trinket","flame_trinket_desc", 836);
    public static final Item DEATH_TRINKET = new ItemTrinket("death_trinket","death_trinket_desc", 96);
    public static final Item SHIELD_TRINKET = new ItemTrinket("shield_trinket", "shield_trinket_desc", 436);
    public static final Item DIAMOND_SHIELD_TRINKET = new ItemTrinket("diamond_shield_trinket", "diamond_shield_trinket_desc", 436);
    public static final Item CREEPER_TRINKET = new ItemTrinket("creeper_trinket","creeper_trinket_desc", 32);
    public static final Item MAGIC_CHARM_TRINKET = new ItemTrinket("charm_trinket", "charm_trinket_desc", 128);
    public static final Item FROZEN_SLAM_TRINKET = new ItemTrinket("slam_trinket", "slam_trinket_desc", 128);
    public static final Item VAMPIRIC_TRINKET = new ItemTrinket("vampire_trinket", "vampire_trinket_desc", 96);
    public static final Item HEART_TRINKET = new ItemTrinket("heart_trinket", "heart_trinket_desc", 336);
    public static final Item GAMBLE_TRINKET = new ItemTrinket("gamble_trinket", "gamble_trinket_desc", 46);
    public static final Item POISON_TRINKET = new ItemTrinket("poison_trinket", "poison_trinket_desc", 96);
    public static final Item VOID_TRINKET = new ItemTrinket("void_trinket","void_trinket_desc", 64);
    public static final Item WIND_TRINKET = new ItemTrinket("wind_trinket", "wind_trinket_desc", 96);
    public static final Item WEAKNESS_TRINKET = new ItemTrinket("weakness_trinket", "weakness_trinket_desc", 84);
    public static final Item VOID_HAND_TRINKET = new ItemTrinket("void_hand_trinket", "void_hand_trinket_desc", 84);
    public static final Item CONFETTI_TRINKET = new ItemTrinket("confetti_trinket", "confetti_trinket_desc", 64);
    public static final Item WYRK_TOTEM = new ItemWyrkTotem("wyrk_totem", "wyrk_totem_desc", DungeonAdditionsTab.ALL);
    public static final Item NOVIK_AID = new ItemNovikAid("call_to_arms", "call_to_arms_desc", DungeonAdditionsTab.ALL);
    public static final Item KINGS_AID = new ItemKingsAid("king_aid", "king_aid_desc", DungeonAdditionsTab.ALL);
    public static final Item ROTTEN_HOLD_LOCATOR = new ItemRottenHoldLocator("rot_locator", "rot_locator_desc");
    public static final Item FROZEN_CASTLE_LOCATOR = new ItemFrozenCastleLocator("frost_locator", "frost_locator_desc");
    public static final Item FORGOTTEN_TEMPLE_LOCATOR = new ItemForgottenTempleLocator("desert_locator", "desert_locator_desc");
    public static final Item DARK_LOCATOR = new ItemBase("dark_locator", DungeonAdditionsTab.ALL);
    public static final Item BURNING_FLAME_LOCATOR = new ItemBurningArenaLocator("flame_locator", "flame_locator_desc");
    public static final Item GAELON_LOCATOR = new ItemGaelonSanctuaryLocator("gaelon_locator", "gaelon_locator_desc");
    public static final Item OBSIDIAN_LOCATOR = new ItemObsidianLocator("obsidian_locator", "obsidian_locator_desc");
    public static final Item SOUL_STAR = new ItemSoulStar("soul_star_item", "soul_star_desc");
    public static final Item SKY_LOCATOR = new ItemSkyLocator("sky_locator", "sky_locator_desc");
    //Boss keys and regular keys
    public static final Item ROT_KNIGHT_KEY = new ItemRotKnightKey("knight_key", "rot_knight_key").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FROST_KEY = new ItemFrostKey("frost_key", "dungeon_frost_key_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FLAME_OF_AMBITION = new ItemAmbitionFlame("ambition_flame", "flame_of_ambition").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item APATHYR_HEART = new ItemCraftingMaterial("apathyr_heart", "apathyr_heart_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item GOLDEN_APATHYR_HEART = new ItemCraftingMaterial("golden_apathyr_heart", "golden_apathyr_heart_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item OBSIDIAN_EYE = new ItemObsidianKey("obsidian_eye", "obsidian_eye_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item LIGHTNING_KEY = new ItemLightningKey("lightning_key", "lighting_key_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item SOUL_KEY = new ItemSoulKey("soul_key", DungeonAdditionsTab.ALL, "soul_key_desc");

    //Weapons
    public static final Item DRAUGR_SHIELD = new ItemDraugrShield("draugr_shield", DungeonAdditionsTab.ALL, "draugr_shield_desc");
    public static final Item FLAME_SPIT_SHIELD = new ItemFlameShield("flame_shield", "flame_shield_desc");
    public static final Item DARK_METAL_SHIELD = new ItemDarkShield("dark_shield", DungeonAdditionsTab.ALL, "dark_shield_desc");
    public static final Item OBSIDIAN_SHIELD = new ItemObsidianShield("obsidian_shield", "obsidian_shield_desc", DungeonAdditionsTab.ALL);
    public static final Item DRAGON_SHIELD = new ItemDragonShield("dragon_shield", DungeonAdditionsTab.ALL, "dragon_shield_desc");
    public static final Item VOID_DAGGER = new ItemBlossomDagger("void_dagger", "dagger_desc", DAGGER_MATERIAL).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DARK_DAGGER = new ItemDarkDagger("dark_dagger", "dark_dagger_desc", DARK_DAGGER_MATERIAL).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ROT_KNIGHT_RAPIER = new ItemKnightRapier("knight_rapier", KNIGHT_RAPIER_MATERIAL, "rapier_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FROST_SWORD = new ItemDraugrSword("frost_sword", DRAUGR_SWORD, "draugr_sword_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item CHAMPION_AXE = new ItemChampionAxe("champion_axe", CHAMPION_AXE_MATERIAL, "champion_axe_desc");
    public static final Item KOPIS_BLADE = new ItemKopis("kopis", "kopis_desc", KOPIS_MATERIAL).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FLAME_BLADE = new ItemFlameBlade("flame_blade", "flame_blade_desc", FLAME_BLADE_MATERIAL);
    public static final Item SWORN_OF_AMBITION = new ItemAmbitionSword("ambition_sword", "flame_sword", FLAME_SWORD_MATERIAL).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item NOVIK_SWORD = new ItemNovikSword("novik_sword", NOVIK_MATERIAL, "novik_sword_desc");
    public static final Item APATHYR_AXE = new ItemApathyrAxe("apathyr_axe", APATHYR_AXE_MATERIAL, "apathyr_axe_desc");
    public static final Item DARK_SICLE = new ItemDarkSicle("dark_sicle", DARK_SICLE_MATERIAL, "dark_sicle_desc");
    public static final Item VOID_HAMMER = new ItemVoidHammer("void_hammer", VOID_HAMMER_MATERIAL, "void_hammer_desc");
    public static final Item SPEAR_OF_SOULS = new ItemSoulSpear("spear_staff", SOUL_SPEAR_MATERIAL, "soul_spear_desc");
    public static final Item SPEAR_OF_WARRIOR = new ItemWeaponSpear("spear_weapon", SOUL_SPEAR_WEAPON_MATERIAL, "soul_weapon_desc");
    public static final Item IMPERIAL_HALBERD_ITEM = new ItemImperialHalberd("imperial_halberd_item", IMPERIAL_HALBERD_MATERIAL, "imperial_halberd_desc");
    public static final Item IMPERIAL_SWORD = new ItemParrySword("imperial_sword_item", "imperial_sword_desc", IMPERIAL_SWORD_MATERIAL);
    public static final Item SWORD_SPEAR = new ItemSwordSpear("sword_spear", SWORD_SPEAR_MATERIAL, "sword_spear_desc");
    public static final Item BLOODY_SWORD_SPEAR = new ItemBloodySwordSpear("sword_spear_blood", BLOODY_SWORD_SPEAR_MATERIAL, "sword_spear_blood_desc");
    public static final Item KING_CLAW = new ItemKingClaw("king_claw", KINGS_CLAW_MATERIAL, "king_claw_desc");
    public static final Item WYRK_STAFF = new ItemWyrkStaff("wyrk_staff", "wyrk_staff_desc", DungeonAdditionsTab.ALL);
    public static final Item GOLEM_CANNON = new ItemGolemCannon("golem_cannon", DungeonAdditionsTab.ALL, "golem_cannon_desc");
    public static final Item VOIDIANT_CATALYST = new ItemVoidiantCatalyst("voidiant_catalyst", "voidiant_catalyst_desc", DungeonAdditionsTab.ALL);
    public static final Item VOID_STAFF = new ItemVoidStaff("void_staff", "void_staff_desc", VOID_STAFF_MATERIAL);
    public static final Item DRAGON_BOW = new ItemDragonBow("dragon_bow", "dragon_bow_desc");

    public static final Item SKY_ARROW = new ItemGoldenArrow("sky_arrow", "sky_arrow_desc", DungeonAdditionsTab.ALL);

    //Armor
    public static final ItemArmor VOID_FLOWER_CROWN = new ItemFlowerCrown("flower_crown", FLOWER_HELMET_MAT, 1, EntityEquipmentSlot.HEAD, "flower_crown", "flower_crown_desc");
    public static final ItemArmor DRAUGR_HELMET = new ModDraugrHelmet("draugr_helmet", DRAUGR_ARMOR, 1, EntityEquipmentSlot.HEAD, "draugr_helmet", "draugr_armor_desc");
    public static final ItemArmor DRAUGR_CHESTPLATE = new DraugrArmorBase("draugr_chestplate", DRAUGR_ARMOR, 1, EntityEquipmentSlot.CHEST, "draugr", "draugr_armor_desc");

    public static final ItemArmor WYRK_HELMET = new ModWyrkHelmet("wyrk_helmet", WYRK_ARMOR, 1, EntityEquipmentSlot.HEAD, "wyrk_helmet", "wyrk_helmet_desc");
    public static final ItemArmor WYRK_BOOTS = new ModWyrkArmor("wyrk_boots", WYRK_ARMOR, 1, EntityEquipmentSlot.FEET, "wyrk", "wyrk_boots_desc");
    public static final Item INCENDIUM_HELMET = new ModIncendiumHelmet("incendium_helmet", INCENDIUM_HELMET_SET, 1, EntityEquipmentSlot.HEAD, "incendium", "incendium_helmet_desc");
    public static final Item INCENDIUM_LEGGINGS = new ModArmorBase("incendium_leggings", INCENDIUM_HELMET_SET, 2, EntityEquipmentSlot.LEGS, "incendium", "incendium_leggings_desc");
    public static final Item FLAME_HELMET = new ModArmorBase("flame_helmet", FLAME_ARMOR, 1, EntityEquipmentSlot.HEAD, "flame", "flame_armor_desc");
    public static final Item FLAME_CHESTPLATE = new ModArmorBase("flame_chestplate", FLAME_ARMOR, 1, EntityEquipmentSlot.CHEST, "flame", "flame_armor_desc");
    public static final Item FLAME_LEGGINGS = new ModArmorBase("flame_leggings", FLAME_ARMOR, 2, EntityEquipmentSlot.LEGS, "flame", "flame_armor_desc");
    public static final ItemArmor FLAME_BOOTS = new ModArmorBase("flame_boots", FLAME_ARMOR, 1, EntityEquipmentSlot.FEET, "flame", "flame_armor_desc");
    public static final ItemArmor NOVIK_HELMET = new ItemNovikHelmet("novik_helmet", NOVIK_SET, 1, EntityEquipmentSlot.HEAD, "novik", "novik_helmet_desc");
    public static final ItemArmor NOVIK_CHESTPLATE = new ItemNovikChestplate("novik_chestplate", NOVIK_SET, 1, EntityEquipmentSlot.CHEST, "novik", "novik_armor_desc");
    public static final ItemArmor NOVIK_LEGGINGS = new ItemNovikArmor("novik_leggings", NOVIK_SET, 2, EntityEquipmentSlot.LEGS,"novik", "novik_armor_desc");
    public static final ItemArmor NOVIK_BOOTS = new ItemNovikArmor("novik_boots", NOVIK_SET, 1, EntityEquipmentSlot.FEET, "novik", "novik_armor_desc");
    public static final ItemArmor APATHYR_HELMET = new ItemApathyrHelmet("apathyr_helmet", APATHYR_SET, 1, EntityEquipmentSlot.HEAD, "apathyr", "apathyr_helmet_desc");
    public static final ItemArmor VOIDIANT_CHESTPLATE = new VoidiantChestplate("voidiant_chestplate",VOIDIANT_ARMOR, 1, EntityEquipmentSlot.CHEST, "voidiant", "voidiant_armor_desc");
    public static final ItemArmor OBSIDIAN_HELMET = new ItemObsidianHelmet("obsidian_helm", OBSIDIAN_HELMET_MAT, 1, EntityEquipmentSlot.HEAD, "obsidian", "obsidian_armor_desc");
    public static final ItemArmor NIGHT_LICH_HELMET = new ModLichArmor("lich_helmet", LICH_HELMET, 1, EntityEquipmentSlot.HEAD,"night_lich", "night_lich_desc");
    public static final ItemArmor DARK_METAL_HELMET = new ModDarkMetalHelmet("dark_metal_helmet", DARK_METAL_SET, 1, EntityEquipmentSlot.HEAD, "dark_metal_helmet", "dark_metal_helmet_desc");
    public static final ItemArmor DARK_METAL_CHESTPLATE = new ModDarkArmorBase("dark_metal_chestplate", DARK_METAL_SET, 1, EntityEquipmentSlot.CHEST, "dark_metal", "dark_metal_desc");
    public static final ItemArmor DARK_METAL_LEGGINGS = new ModDarkArmorBase("dark_metal_leggings", DARK_METAL_SET, 2, EntityEquipmentSlot.LEGS, "dark_metal", "dark_metal_desc");
    public static final ItemArmor DARK_METAL_BOOTS = new ModDarkArmorBase("dark_metal_boots", DARK_METAL_SET, 1, EntityEquipmentSlot.FEET, "dark_metal", "dark_metal_desc");
    public static final ItemArmor IMPERIAL_HELMET = new ItemImperialChestplate("imperial_helmet", IMPERIAL_ARMOR, 1, EntityEquipmentSlot.HEAD, "imperial_helmet", "imperial_helmet_desc");
    public static final ItemArmor IMPERIAL_CHESTPLATE = new ItemImperialChestplate("imperial_chestplate", IMPERIAL_ARMOR, 1, EntityEquipmentSlot.CHEST, "imperial_chestplate", "imperial_armor_desc");

    public static final ItemArmor IMPERIAL_LEGGINGS = new ItemImperialArmor("imperial_leggings", IMPERIAL_ARMOR, 2, EntityEquipmentSlot.LEGS, "imperial", "imperial_armor_desc");
    public static final ItemArmor IMPERIAL_BOOTS = new ItemImperialArmor("imperial_boots", IMPERIAL_ARMOR, 1, EntityEquipmentSlot.FEET, "imperial", "imperial_armor_desc");
    public static final ItemArmor KINGS_HELMET = new ItemKingHelmet("king_helmet", KINGS_HELMET_MAT, 1, EntityEquipmentSlot.HEAD, "king_helmet_desc");

    //Misc Utility
    public static final Item INVISIBLE = new ItemBase("invisible", null);

    public static final Item VOID_LEAF = new ItemBase("void_leaf", null);

    public static final Item SPORE_BALL = new AnimatedSporeItem("spore", null);
    public static final Item MAGIC_FIREBALL = new ItemMagicFireball("magic_fireball", null);

    public static final Item POISON_DART = new ItemDart("dart");
    public static final Item LIGHT_RING_PROJECTILE = new ItemLightRing("light_ring").setCreativeTab(null);
    public static final Item STORM_TORNADO_PROJECTILE = new ItemStormTornado("storm_tornado").setCreativeTab(null);
    public static final Item VOIDCLYSM_BOLT = new ItemVoidclysmBolt("voidclysm_bolt").setCreativeTab(null);
    public static final Item HOLY_WAVE_PROJ = new ItemHolyWave("holy_wave").setCreativeTab(null);
    public static final Item BLOOD_BALL_PROJ = new ItemBase("blood_ball", null);
    public static final Item FLAME_BLADE_PROJ = new ItemFlameBladeProj("proj_flame_blade").setCreativeTab(null);
    public static final Item DESERT_STORM_PROJ = new ItemDesertStorm("desert_storm").setCreativeTab(null);
    public static final Item YELLOW_WAVE_PROJ = new ItemYellowWave("yellow_wave").setCreativeTab(null);
    public static final Item CRYSTAL_WAVE_PROJ = new ItemYellowWave("crystal_wave").setCreativeTab(null);
    public static final Item GHOST_BOLT_PROJ = new ItemProjectileGhost("ghost_bolt").setCreativeTab(null);
    public static final Item FAST_CRYSTAL_PROJ = new ItemFastCrystal("fast_crystal").setCreativeTab(null);

    public static final Item INVISISBLE_ITEM = new ItemBase("invisible_item", null);
    public static final Item PROJECTILE_FLAME = new ItemBase("projectile_flame", null);
    public static final Item VOID_PROJECTILE = new ItemBase("void_projectile", null);

    public static final Item FAKE_HEALING_POTION = new ItemBase("fake_healing_potion", null);
    public static final Item MAGIC_PROJECTILE = new ItemBase("magic_projectile", null);
    public static final Item SKY_LOCATOR_PROJECTILE = new ItemBase("sky_locator_proj", null);
    public static final Item MAGIC_TRACK_PROJECTILE = new ItemBase("missile_projectile", null);
    public static final Item FROST_PROJECTILE = new ItemBase("frost_bullet", null);
    public static final Item FAKE_BOW = new ItemBase("fake_bow", null);
    public static final Item TRADER_BAG = new ItemBase("trader_bag", null);
    public static final Item DESERT_LOCATOR_PROJ = new ItemBase("desert_locator_projectile", null);

    public static final Item MOD_LOGO = new ItemBase("bomd_mod_logo", null);


}
