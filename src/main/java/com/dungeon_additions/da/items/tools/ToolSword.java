package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ToolSword extends ItemSword implements IHasModel {
    private float level;

    public int ticksExisted;

    private Consumer<List<String>> information = (info) -> {
    };

    public ToolSword(String name, ToolMaterial material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.COMBAT);
        ModItems.ITEMS.add(this);
        this.level = level;
    }







    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }



    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit
     * damage.
     */
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.getAttackDamage(), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", getAttackSpeed(), 0));
        }

        return multimap;
    }

    @Override
    public float getAttackDamage() {
        return super.getAttackDamage();
    }

    protected double getAttackSpeed() {
        return -2.4000000953674316D;
    }

    public Item setInformation(Consumer<List<String>> information) {
        this.information = information;
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        information.accept(tooltip);
    }



    public static UUID getAttackDamageModifier() {
        return ATTACK_DAMAGE_MODIFIER;
    }


}
