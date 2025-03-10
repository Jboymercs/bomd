package com.dungeon_additions.da.items.tools;

import com.deeperdepths.common.Constants;
import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.ItemBase;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ItemChampionAxe extends ItemSword implements IAnimatable, IHasModel {

    public AnimationFactory factory = new AnimationFactory(this);

    private String info_loc;




    public ItemChampionAxe(String name, Item.ToolMaterial material, String info_loc) {
        super(material);
        this.setMaxDamage(986);
        setTranslationKey(name);
        setRegistryName(name);
        ModItems.ITEMS.add(this);
        setCreativeTab(DungeonAdditionsTab.ALL);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public void registerModels() {
        {
            Main.proxy.registerItemRenderer(this, 0, "inventory");
        }}

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (attacker.world.isRemote) return false;
        int axeCoolDown = (int) 2 * 20;
        stack.damageItem(1, attacker);
        float realAttackDamage = this.getAttackDamage() + 1;
        float bonus_damage = (ModConfig.champion_axe_damage_scaling /(attacker.getHealth() / attacker.getMaxHealth()) ) - ModConfig.champion_axe_damage_scaling;
        float regular_damage = (realAttackDamage / 2) + bonus_damage;
        float armor_damage = (realAttackDamage / 2);
        System.out.println("Detecting Damage Dealing, bonus Damage is at" + bonus_damage);
        System.out.println("Regular Damage + Bonus is" + regular_damage);
        System.out.println("Armor Damage is" + armor_damage);

        if(attacker instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) attacker);
            if(!player.getCooldownTracker().hasCooldown(this)) {
                target.attackEntityFrom(ModUtils.causeAxeDamage(attacker), (float) regular_damage);
                target.attackEntityFrom(ModUtils.causeAxeDamage(attacker).setDamageBypassesArmor(), (float) armor_damage);
            }
            player.getCooldownTracker().setCooldown(this, axeCoolDown);
        }


        return true;
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


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected double getAttackSpeed() {
        return -3.3D;
    }
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player)
    { return false; }



    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker)
    {
        return true;
    }
}
