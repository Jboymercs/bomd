package com.dungeon_additions.da.init;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.items.*;
import com.dungeon_additions.da.items.armor.*;
import com.dungeon_additions.da.items.armor.dark.ModDarkArmorBase;
import com.dungeon_additions.da.items.armor.dark.ModDarkMetalHelmet;
import com.dungeon_additions.da.items.armor.imperial.ItemImperialArmor;
import com.dungeon_additions.da.items.armor.imperial.ItemImperialChestplate;
import com.dungeon_additions.da.items.food.ItemBeetleMorsel;
import com.dungeon_additions.da.items.food.ItemUncookedFood;
import com.dungeon_additions.da.items.gun.ItemVoidiantCatalyst;
import com.dungeon_additions.da.items.keys.*;
import com.dungeon_additions.da.items.projectile.*;
import com.dungeon_additions.da.items.shield.*;
import com.dungeon_additions.da.items.tools.*;
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
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    //Materials
    private static final Item.ToolMaterial CHAMPION_AXE_MATERIAL = EnumHelper.addToolMaterial("champion_set", 3, 874, 7.0F, (float) ModConfig.champion_axe_damage, 15);
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
    private static final Item.ToolMaterial KOPIS_MATERIAL = EnumHelper.addToolMaterial("kopis_material", 1, 1200, 5.0F, (float) 6, 20);

    private static final ItemArmor.ArmorMaterial FLAME_ARMOR = EnumHelper.addArmorMaterial("flame", ModReference.MOD_ID + ":flame", 320, new int[]{4, 7,9,4}, 70, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2F);
    private static final ItemArmor.ArmorMaterial DRAUGR_ARMOR = EnumHelper.addArmorMaterial("draugr", ModReference.MOD_ID + "draugr", 375, new int[]{3, 6, 8,3}, 20, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 2F);
    private static final ItemArmor.ArmorMaterial LICH_HELMET = EnumHelper.addArmorMaterial("night_lich", ModReference.MOD_ID +":night_lich", 275, new int[] {3,6,8,3}, 70, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F);
    private static final ItemArmor.ArmorMaterial INCENDIUM_HELMET_SET = EnumHelper.addArmorMaterial("incendium_h", ModReference.MOD_ID +":incendium_helmet", 375, new int[] {3,6,8,3}, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2F);
    private static final ItemArmor.ArmorMaterial DARK_METAL_SET = EnumHelper.addArmorMaterial("dark_metal", ModReference.MOD_ID +":dark_metal", 451, new int[] {3,5,8,3}, 30, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.5F);
    private static final ItemArmor.ArmorMaterial WYRK_ARMOR = EnumHelper.addArmorMaterial("wyrk", ModReference.MOD_ID +":wyrk", 325, new int[] {3,6,8,3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.5F);
    private static final ItemArmor.ArmorMaterial IMPERIAL_ARMOR = EnumHelper.addArmorMaterial("imperial", ModReference.MOD_ID +":imperial", 670, new int[] {4,7,9,4}, 30, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.5F);
    private static final ItemArmor.ArmorMaterial VOIDIANT_ARMOR = EnumHelper.addArmorMaterial("voidiant", ModReference.MOD_ID + "voidiant", 275, new int[]{2, 5, 7, 2}, 30, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3F);
    private static final ItemArmor.ArmorMaterial KINGS_HELMET_MAT = EnumHelper.addArmorMaterial("king", ModReference.MOD_ID +":king", 1023, new int[] {4,7,9,4}, 30, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 1.5F);
    public ModItems() {


    }
    public static final List<Item> ITEMS = new ArrayList<Item>();

    //Crafting Items
    public static final Item ROT_KNIGHT_FRAGMENT = new ItemCraftingMaterial("knight_shard", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ROT_KNIGHT_INGOT = new ItemCraftingMaterial("knight_ingot", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item VOID_THORNS = new ItemCraftingMaterial("void_thorns", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DARK_MANA = new ItemCraftingMaterial("dark_mana", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DARK_CORE = new ItemCraftingMaterial("dark_core", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item CRYPT_PLATE = new ItemCraftingMaterial("crypt_plate", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    //TO BE USED LATER
  //  public static final Item LITIC_SMITHING_STONE = new ItemCraftingMaterial("lightning_smithing_stone", "lightning_stone_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item RED_ANICIENT_MANA = new ItemCraftingMaterial("red_ancient_mana", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FROST_SHARD = new ItemCraftingMaterial("frost_shard", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FROST_INGOT = new ItemCraftingMaterial("frost_ingot", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DRAUGR_METAL = new ItemCraftingMaterial("draugr_shard", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DRAUGR_INGOT = new ItemCraftingMaterial("draugr_ingot", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item GREAT_WYRK_CRYSTAL = new ItemCraftingMaterial("great_wyrk_mana", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item BEETLE_SHELL = new ItemCraftingMaterial("beetle_shell", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item INCENDIUM_CORE = new ItemCraftingMaterial("incendium_core", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item VOLATILE_PEARL = new ItemCraftingMaterial("volatile_orb", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ABBERRANT_EYE = new ItemCraftingMaterial("abberrant_eye", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FLAME_METAL_SCRAP = new ItemCraftingMaterial("flame_metal_scrap", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ANCIENT_MANA = new ItemCraftingMaterial("ancient_mana", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
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
    public static final Item SEALED_FLAME_BOTTLE = new ItemSealedFlame("sealed_flame", "sealed_flame_desc", DungeonAdditionsTab.ALL);
    public static final Item WYRK_TOTEM = new ItemWyrkTotem("wyrk_totem", "wyrk_totem_desc", DungeonAdditionsTab.ALL);
    public static final Item KINGS_AID = new ItemKingsAid("king_aid", "king_aid_desc", DungeonAdditionsTab.ALL);
    public static final Item ROTTEN_HOLD_LOCATOR = new ItemRottenHoldLocator("rot_locator", "rot_locator_desc");
    public static final Item FROZEN_CASTLE_LOCATOR = new ItemFrozenCastleLocator("frost_locator", "frost_locator_desc");
    public static final Item FORGOTTEN_TEMPLE_LOCATOR = new ItemForgottenTempleLocator("desert_locator", "desert_locator_desc");
    public static final Item DARK_LOCATOR = new ItemBase("dark_locator", DungeonAdditionsTab.ALL);
    public static final Item BURNING_FLAME_LOCATOR = new ItemBurningArenaLocator("flame_locator", "flame_locator_desc");
    public static final Item OBSIDIAN_LOCATOR = new ItemObsidianLocator("obsidian_locator", "obsidian_locator_desc");
    public static final Item SOUL_STAR = new ItemSoulStar("soul_star_item", "soul_star_desc");
    public static final Item SKY_LOCATOR = new ItemSkyLocator("sky_locator", "sky_locator_desc");
    //Boss keys and regular keys
    public static final Item ROT_KNIGHT_KEY = new ItemRotKnightKey("knight_key", "rot_knight_key").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FROST_KEY = new ItemFrostKey("frost_key", "dungeon_frost_key_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FLAME_OF_AMBITION = new ItemAmbitionFlame("ambition_flame", "flame_of_ambition").setCreativeTab(DungeonAdditionsTab.ALL);
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
    public static final Item DARK_SICLE = new ItemDarkSicle("dark_sicle", DARK_SICLE_MATERIAL, "dark_sicle_desc");
    public static final Item SPEAR_OF_SOULS = new ItemSoulSpear("spear_staff", SOUL_SPEAR_MATERIAL, "soul_spear_desc");
    public static final Item SPEAR_OF_WARRIOR = new ItemWeaponSpear("spear_weapon", SOUL_SPEAR_WEAPON_MATERIAL, "soul_weapon_desc");
    public static final Item IMPERIAL_HALBERD_ITEM = new ItemImperialHalberd("imperial_halberd_item", IMPERIAL_HALBERD_MATERIAL, "imperial_halberd_desc");
    public static final Item IMPERIAL_SWORD = new ItemParrySword("imperial_sword_item", "imperial_sword_desc", IMPERIAL_SWORD_MATERIAL);
    public static final Item SWORD_SPEAR = new ItemSwordSpear("sword_spear", SWORD_SPEAR_MATERIAL, "sword_spear_desc");
    public static final Item BLOODY_SWORD_SPEAR = new ItemBloodySwordSpear("sword_spear_blood", BLOODY_SWORD_SPEAR_MATERIAL, "sword_spear_blood_desc");
    public static final Item KING_CLAW = new ItemKingClaw("king_claw", KINGS_CLAW_MATERIAL, "king_claw_desc");
    public static final Item WYRK_STAFF = new ItemWyrkStaff("wyrk_staff", "wyrk_staff_desc", DungeonAdditionsTab.ALL);
    public static final Item VOIDIANT_CATALYST = new ItemVoidiantCatalyst("voidiant_catalyst", "voidiant_catalyst_desc", DungeonAdditionsTab.ALL);
    public static final Item DRAGON_BOW = new ItemDragonBow("dragon_bow", "dragon_bow_desc");

    public static final Item SKY_ARROW = new ItemGoldenArrow("sky_arrow", "sky_arrow_desc", DungeonAdditionsTab.ALL);

    //Armor
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
    public static final ItemArmor VOIDIANT_CHESTPLATE = new VoidiantChestplate("voidiant_chestplate",VOIDIANT_ARMOR, 1, EntityEquipmentSlot.CHEST, "voidiant", "voidiant_armor_desc");
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
    public static final Item LIGHT_RING_PROJECTILE = new ItemLightRing("light_ring");
    public static final Item STORM_TORNADO_PROJECTILE = new ItemStormTornado("storm_tornado");
    public static final Item VOIDCLYSM_BOLT = new ItemVoidclysmBolt("voidclysm_bolt");
    public static final Item HOLY_WAVE_PROJ = new ItemHolyWave("holy_wave");
    public static final Item BLOOD_BALL_PROJ = new ItemBase("blood_ball");
    public static final Item FLAME_BLADE_PROJ = new ItemFlameBladeProj("proj_flame_blade");

    public static final Item INVISISBLE_ITEM = new ItemBase("invisible_item").setCreativeTab(CreativeTabs.SEARCH);
    public static final Item PROJECTILE_FLAME = new ItemBase("projectile_flame").setCreativeTab(CreativeTabs.SEARCH);

    public static final Item FAKE_HEALING_POTION = new ItemBase("fake_healing_potion").setCreativeTab(CreativeTabs.SEARCH);
    public static final Item MAGIC_PROJECTILE = new ItemBase("magic_projectile");
    public static final Item SKY_LOCATOR_PROJECTILE = new ItemBase("sky_locator_proj");
    public static final Item MAGIC_TRACK_PROJECTILE = new ItemBase("missile_projectile");
    public static final Item FROST_PROJECTILE = new ItemBase("frost_bullet");
    public static final Item FAKE_BOW = new ItemBase("fake_bow");
    public static final Item DESERT_LOCATOR_PROJ = new ItemBase("desert_locator_projectile");

    public static final Item MOD_LOGO = new ItemBase("bomd_mod_logo", null);


}
