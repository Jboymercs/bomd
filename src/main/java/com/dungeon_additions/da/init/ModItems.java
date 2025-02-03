package com.dungeon_additions.da.init;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.items.*;
import com.dungeon_additions.da.items.armor.*;
import com.dungeon_additions.da.items.shield.ItemDraugrShield;
import com.dungeon_additions.da.items.tools.*;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    private static final Item.ToolMaterial CHAMPION_AXE_MATERIAL = EnumHelper.addToolMaterial("champion_set", 3, 874, 7.0F, (float) ModConfig.champion_axe_damage, 15);
    private static final Item.ToolMaterial IMPERIAL_HALBERD_MATERIAL = EnumHelper.addToolMaterial("imperial_halberd_set", 3, 1263, 8.0F, (float) 10, 20);
    private static final Item.ToolMaterial DAGGER_MATERIAL = EnumHelper.addToolMaterial("void_material", 2, 320, 6.0F, (float) ModConfig.void_dagger_damage, 45);

    private static final Item.ToolMaterial DRAUGR_SWORD = EnumHelper.addToolMaterial("void_material", 2, 974, 6.0F, (float) ModConfig.frost_sword_damage, 45);
    private static final Item.ToolMaterial FLAME_SWORD_MATERIAL = EnumHelper.addToolMaterial("flame_sword_material", 2, 988, 6.0F, ModConfig.sword_of_ambition_damage, 70);

    private static final Item.ToolMaterial SOUL_SPEAR_MATERIAL = EnumHelper.addToolMaterial("soul_spear_material", 2, 1200, 5.0F, ModConfig.soul_spear_damage, 10);

    private static final Item.ToolMaterial SOUL_SPEAR_WEAPON_MATERIAL = EnumHelper.addToolMaterial("soul_spear_weapon_material", 2, 1200, 5.0F, ModConfig.soul_weapon_damage, 20);
    private static final Item.ToolMaterial KNIGHT_RAPIER_MATERIAL = EnumHelper.addToolMaterial("knight_rapier_material", 2, 502, 5.0F, (float) ModConfig.rapier_damage, 10);

    private static final ItemArmor.ArmorMaterial FLAME_ARMOR = EnumHelper.addArmorMaterial("flame", ModReference.MOD_ID + ":flame", 320, new int[]{4, 7,9,4}, 70, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2F);
    private static final ItemArmor.ArmorMaterial DRAUGR_ARMOR = EnumHelper.addArmorMaterial("draugr", ModReference.MOD_ID + "draugr", 375, new int[]{3, 6, 8,3}, 20, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 2F);
    private static final ItemArmor.ArmorMaterial LICH_HELMET = EnumHelper.addArmorMaterial("night_lich", ModReference.MOD_ID +":night_lich", 275, new int[] {3,6,8,3}, 70, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F);
    private static final ItemArmor.ArmorMaterial WYRK_ARMOR = EnumHelper.addArmorMaterial("wyrk", ModReference.MOD_ID +":wyrk", 325, new int[] {3,6,8,3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.5F);
    public ModItems() {


    }
    public static final List<Item> ITEMS = new ArrayList<Item>();
    //Nonfindable Items
    public static final Item INVISIBLE = new ItemBase("invisible", null);

    public static final Item VOID_LEAF = new ItemBase("void_leaf", null);

    public static final Item SPORE_BALL = new AnimatedSporeItem("spore", null);
    public static final Item MAGIC_FIREBALL = new ItemMagicFireball("magic_fireball", null);

    public static final Item POISON_DART = new ItemDart("dart");
    public static final Item SKY_ARROW = new ItemGoldenArrow("sky_arrow", "sky_arrow_desc", DungeonAdditionsTab.ALL);

    //Findable Items
    public static final Item VOID_FRUIT = new ItemFoodBase("crystal_fruit", "regen_fruit", 5, 1, false).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item VOID_THORNS = new ItemCraftingMaterial("void_thorns", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ABBERRANT_ETE = new ItemCraftingMaterial("abberrant_eye", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ENDLESS_PEARL = new ItemEndlessEnderpearl("endless_enderpearl", "pearl_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item VOID_DAGGER = new ItemBlossomDagger("void_dagger", "dagger_desc", DAGGER_MATERIAL).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FLAME_OF_AMBITION = new ItemAmbitionFlame("ambition_flame", "flame_of_ambition").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ENFLAMED_MEAT = new ItemEnflamedMeat("enflamed_meat", "flame_meat", 10, 0.8F, false).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item SWORN_OF_AMBITION = new ItemAmbitionSword("ambition_sword", "flame_sword", FLAME_SWORD_MATERIAL).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item INVISISBLE_ITEM = new ItemBase("invisible_item").setCreativeTab(CreativeTabs.SEARCH);
    public static final Item PROJECTILE_FLAME = new ItemBase("projectile_flame").setCreativeTab(CreativeTabs.SEARCH);

    public static final Item FAKE_HEALING_POTION = new ItemBase("fake_healing_potion").setCreativeTab(CreativeTabs.SEARCH);

    public static final Item FLAME_METAL_SCRAP = new ItemCraftingMaterial("flame_metal_scrap", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);

    public static final Item FLAME_HELMET = new ModArmorBase("flame_helmet", FLAME_ARMOR, 1, EntityEquipmentSlot.HEAD, "flame", "flame_armor_desc");
    public static final Item FLAME_CHESTPLATE = new ModArmorBase("flame_chestplate", FLAME_ARMOR, 1, EntityEquipmentSlot.CHEST, "flame", "flame_armor_desc");
    public static final Item FLAME_LEGGINGS = new ModArmorBase("flame_leggings", FLAME_ARMOR, 2, EntityEquipmentSlot.LEGS, "flame", "flame_armor_desc");
    public static final ItemArmor FLAME_BOOTS = new ModArmorBase("flame_boots", FLAME_ARMOR, 1, EntityEquipmentSlot.FEET, "flame", "flame_armor_desc");
    public static final ItemArmor NIGHT_LICH_HELMET = new ModLichArmor("lich_helmet", LICH_HELMET, 1, EntityEquipmentSlot.HEAD,"night_lich", "night_lich_desc");
    public static final ItemArmor DRAUGR_HELMET = new ModDraugrHelmet("draugr_helmet", DRAUGR_ARMOR, 1, EntityEquipmentSlot.HEAD, "draugr_helmet", "draugr_armor_desc");
    public static final ItemArmor DRAUGR_CHESTPLATE = new DraugrArmorBase("draugr_chestplate", DRAUGR_ARMOR, 1, EntityEquipmentSlot.CHEST, "draugr", "draugr_armor_desc");

    public static final ItemArmor WYRK_HELMET = new ModWyrkHelmet("wyrk_helmet", WYRK_ARMOR, 1, EntityEquipmentSlot.HEAD, "wyrk_helmet", "wyrk_helmet_desc");
    public static final ItemArmor WYRK_BOOTS = new ModWyrkArmor("wyrk_boots", WYRK_ARMOR, 1, EntityEquipmentSlot.FEET, "wyrk", "wyrk_boots_desc");


    public static final Item ROT_KNIGHT_FRAGMENT = new ItemCraftingMaterial("knight_shard", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ROT_KNIGHT_INGOT = new ItemCraftingMaterial("knight_ingot", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ROT_KNIGHT_KEY = new ItemRotKnightKey("knight_key", "rot_knight_key").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ROT_KNIGHT_RAPIER = new ItemKnightRapier("knight_rapier", KNIGHT_RAPIER_MATERIAL, "rapier_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item GLOW_BERRY = new ItemGlowBerry("glow_berry", 4, 0.4F, false, ModBlocks.AZAELA_VINES).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item WYRK_SNACK = new ItemWyrkSnack("wyrk_snack", "wyrk_snack_desc", 6, 0.7F, false).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item WYRK_STAFF = new ItemWyrkStaff("wyrk_staff", "wyrk_staff_desc", DungeonAdditionsTab.ALL);
    public static final Item WYRK_TOTEM = new ItemWyrkTotem("wyrk_totem", "wyrk_totem_desc", DungeonAdditionsTab.ALL);
    public static final Item LITIC_SHARD = new ItemCraftingMaterial("lightning_shard", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item LITIC_INGOT = new ItemCraftingMaterial("lightning_ingot", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item LITIC_SMITHING_STONE = new ItemCraftingMaterial("lightning_smithing_stone", "lightning_stone_desc").setCreativeTab(DungeonAdditionsTab.ALL);



    public static final Item SOUL_STAR = new ItemSoulStar("soul_star_item", "soul_star_desc");
    public static final Item MAGIC_PROJECTILE = new ItemBase("magic_projectile");
    public static final Item MAGIC_TRACK_PROJECTILE = new ItemBase("missile_projectile");
    public static final Item FROST_PROJECTILE = new ItemBase("frost_bullet");
    public static final Item ANCIENT_MANA = new ItemCraftingMaterial("ancient_mana", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item SPEAR_OF_SOULS = new ItemSoulSpear("spear_staff", SOUL_SPEAR_MATERIAL, "soul_spear_desc");
    public static final Item SPEAR_OF_WARRIOR = new ItemWeaponSpear("spear_weapon", SOUL_SPEAR_WEAPON_MATERIAL, "soul_weapon_desc");
    public static final Item RED_ANICIENT_MANA = new ItemCraftingMaterial("red_ancient_mana", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FROST_SHARD = new ItemCraftingMaterial("frost_shard", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FROST_INGOT = new ItemCraftingMaterial("frost_ingot", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FROST_KEY = new ItemFrostKey("frost_key", "dungeon_frost_key_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FROST_SWORD = new ItemDraugrSword("frost_sword", DRAUGR_SWORD, "draugr_sword_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item LIGHTNING_KEY = new ItemLightningKey("lightning_key", "lighting_key_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DRAUGR_SHIELD = new ItemDraugrShield("draugr_shield", DungeonAdditionsTab.ALL, "draugr_shield_desc");
    public static final Item CHAMPION_AXE = new ItemChampionAxe("champion_axe", CHAMPION_AXE_MATERIAL, "champion_axe_desc");
    public static final Item IMPERIAL_HALBERD_ITEM = new ItemImperialHalberd("imperial_halberd_item", IMPERIAL_HALBERD_MATERIAL, "imperial_halberd_desc");
    public static final Item DRAUGR_METAL = new ItemCraftingMaterial("draugr_shard", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item DRAUGR_INGOT = new ItemCraftingMaterial("draugr_ingot", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item GREAT_WYRK_CRYSTAL = new ItemCraftingMaterial("great_wyrk_mana", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FROZEN_CASTLE_LOCATOR = new ItemFrozenCastleLocator("frost_locator", "frost_locator_desc");
    public static final Item FAKE_BOW = new ItemBase("fake_bow");

    public static final Item MOD_LOGO = new ItemBase("bomd_mod_logo", null);


}
