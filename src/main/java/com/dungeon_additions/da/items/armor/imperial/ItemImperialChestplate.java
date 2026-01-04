package com.dungeon_additions.da.items.armor.imperial;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.armor.ModLichArmor;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

public class ItemImperialChestplate extends ItemArmor implements IHasModel {

    private String texture;
    private String info_loc;
    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("a3578781-e4a8-4d70-9d32-cd952aeae1df"),
            UUID.fromString("e2d1f056-f539-48c7-b353-30d7a367ebd0"), UUID.fromString("db13047a-bb47-4621-a025-65ed22ce461a"),
            UUID.fromString("abb5df20-361d-420a-8ec7-4bdba33378eb")};

    private final AttributeModifier movementSpeedModif;
    private final AttributeModifier healthBoostModif;

    private String armorBonusDesc = "";
    public ItemImperialChestplate(String name, ArmorMaterial materialIn, int renderIdx, EntityEquipmentSlot slotIn, String textureName, String info_loc) {
        super(materialIn, renderIdx, slotIn);
        setCreativeTab(DungeonAdditionsTab.ALL);
        this.info_loc = info_loc;
        setTranslationKey(name);
        setRegistryName(name);
        this.texture = textureName;
        ModItems.ITEMS.add(this);
        this.movementSpeedModif = new AttributeModifier("imperialMovementDebuff_" + slotIn.getName(), -0.05, 1);
        this.healthBoostModif = new AttributeModifier("imperialHealthBoost_" + slotIn.getName(), 2, 0);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (equipmentSlot == this.armorType) {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", this.damageReduceAmount * ModConfig.armor_scaling, 0));

            // Override armor toughness to make is adjustable in game
            //Come back to Re-use when needed IF needed
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", 3F * ModConfig.armor_toughness_scaling, 0));
            multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), movementSpeedModif);
            multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), healthBoostModif);

        }


        return multimap;
    }

    public ItemImperialChestplate setArmorBonusDesc(String armorBonusDesc) {
        this.armorBonusDesc = armorBonusDesc;
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        if (!this.armorBonusDesc.isEmpty()) {
            tooltip.add(ModUtils.translateDesc(this.armorBonusDesc));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped defaultModel) {
        return (ModelBiped) Main.proxy.getArmorModel(this, entityLiving);
    }
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return ModReference.MOD_ID + ":textures/models/armor/" + this.texture + ".png";
    }
}
