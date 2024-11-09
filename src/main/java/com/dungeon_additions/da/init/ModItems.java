package com.dungeon_additions.da.init;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.items.*;
import com.dungeon_additions.da.items.armor.ModArmorBase;
import com.dungeon_additions.da.items.tools.ItemAmbitionSword;
import com.dungeon_additions.da.items.tools.ItemBlossomDagger;
import com.dungeon_additions.da.items.tools.ItemKnightRapier;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.List;

public class ModItems {


    private static final Item.ToolMaterial DAGGER_MATERIAL = EnumHelper.addToolMaterial("void_material", 2, 320, 6.0F, ModConfig.void_dagger_damage, 45);
    private static final Item.ToolMaterial FLAME_SWORD_MATERIAL = EnumHelper.addToolMaterial("flame_sword_material", 2, 988, 6.0F, ModConfig.sword_of_ambition_damage, 70);
    private static final Item.ToolMaterial KNIGHT_RAPIER_MATERIAL = EnumHelper.addToolMaterial("knight_rapier_material", 2, 502, 5.0F, (float) ModConfig.rapier_damage, 10);

    private static final ItemArmor.ArmorMaterial FLAME_ARMOR = EnumHelper.addArmorMaterial("flame", ModReference.MOD_ID + ":flame", 450, new int[]{4, 7,9,4}, 70, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2F);
    public ModItems() {


    }
    public static final List<Item> ITEMS = new ArrayList<Item>();
    //Nonfindable Items
    public static final Item INVISIBLE = new ItemBase("invisible", null);

    public static final Item VOID_LEAF = new ItemBase("void_leaf", null);

    public static final Item SPORE_BALL = new AnimatedSporeItem("spore", null);

    public static final Item POISON_DART = new ItemDart("dart");

    //Findable Items
    public static final Item VOID_FRUIT = new ItemFoodBase("crystal_fruit", "regen_fruit", 5, 3, false).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item VOID_THORNS = new ItemCraftingMaterial("void_thorns", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ABBERRANT_ETE = new ItemCraftingMaterial("abberrant_eye", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ENDLESS_PEARL = new ItemEndlessEnderpearl("endless_enderpearl", "pearl_desc").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item VOID_DAGGER = new ItemBlossomDagger("void_dagger", "dagger_desc", DAGGER_MATERIAL).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item FLAME_OF_AMBITION = new ItemAmbitionFlame("ambition_flame", "flame_of_ambition").setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item ENFLAMED_MEAT = new ItemEnflamedMeat("enflamed_meat", "flame_meat", 10, 1, false).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item SWORN_OF_AMBITION = new ItemAmbitionSword("ambition_sword", "flame_sword", FLAME_SWORD_MATERIAL).setCreativeTab(DungeonAdditionsTab.ALL);
    public static final Item INVISISBLE_ITEM = new ItemBase("invisible_item").setCreativeTab(CreativeTabs.SEARCH);
    public static final Item PROJECTILE_FLAME = new ItemBase("projectile_flame").setCreativeTab(CreativeTabs.SEARCH);

    public static final Item FAKE_HEALING_POTION = new ItemBase("fake_healing_potion").setCreativeTab(CreativeTabs.SEARCH);

    public static final Item FLAME_METAL_SCRAP = new ItemCraftingMaterial("flame_metal_scrap", "crafting_material").setCreativeTab(DungeonAdditionsTab.ALL);

    public static final Item FLAME_HELMET = new ModArmorBase("flame_helmet", FLAME_ARMOR, 1, EntityEquipmentSlot.HEAD, "flame", "flame_armor_desc");
    public static final Item FLAME_CHESTPLATE = new ModArmorBase("flame_chestplate", FLAME_ARMOR, 1, EntityEquipmentSlot.CHEST, "flame", "flame_armor_desc");
    public static final Item FLAME_LEGGINGS = new ModArmorBase("flame_leggings", FLAME_ARMOR, 2, EntityEquipmentSlot.LEGS, "flame", "flame_armor_desc");
    public static final ItemArmor FLAME_BOOTS = new ModArmorBase("flame_boots", FLAME_ARMOR, 1, EntityEquipmentSlot.FEET, "flame", "flame_armor_desc");

    public static final Item ROT_KNIGHT_FRAGMENT = new ItemCraftingMaterial("knight_shard", "crafting_material");
    public static final Item ROT_KNIGHT_INGOT = new ItemCraftingMaterial("knight_ingot", "crafting_material");
    public static final Item ROT_KNIGHT_KEY = new ItemRotKnightKey("knight_key", "rot_knight_key");
    public static final Item ROT_KNIGHT_RAPIER = new ItemKnightRapier("knight_rapier", KNIGHT_RAPIER_MATERIAL, "rapier_desc");

}
