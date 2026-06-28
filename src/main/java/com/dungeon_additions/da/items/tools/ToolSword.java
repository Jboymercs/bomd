package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.item.EnumWeaponType;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.util.ISweepAttackOverride;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ToolSword extends ItemSword implements IHasModel, ISweepAttackOverride {

    public int ticksExisted;
    protected float falter_value = 0.1F;
    protected SoundEvent swingSound;
    protected EnumWeaponType weapon_type = EnumWeaponType.DAGGER;
//    private final AttributeModifier reach_distance;
 //   protected float reachDistanceValue = 0;
    public float weaponReach = 4.5F;
    public float swingRadius = 0.5F;

    //delay for weapons when they are fully charged and the player swings, changes based upon
    protected int weaponDelay = (int) ((20/(4 + this.getAttackSpeed())) * 0.3);

    private Consumer<List<String>> information = (info) -> {
    };

    public ToolSword(String name, ToolMaterial material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.COMBAT);
        ModItems.ITEMS.add(this);
        this.swingSound = SoundsHandler.SWING_REGULAR;
    //    this.reach_distance = new AttributeModifier("Weapon modifier", this.reachDistanceValue, 1);
    }



    public EnumWeaponType getWeaponAnimationType() {
        return weapon_type;
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
           // multimap.put(EntityPlayer.REACH_DISTANCE.getName(), this.reach_distance);

        }

        return multimap;
    }

    @Override
    public float getAttackDamage() {
        return super.getAttackDamage();
    }

    public double getAttackSpeed() {
        return -2.4000000953674316D;
    }

    public Item setInformation(Consumer<List<String>> information) {
        this.information = information;
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(ModConfig.players_cause_falter && falter_value != 0 && ModConfig.enable_falter_tooltips) {
            tooltip.add(TextFormatting.GRAY + I18n.format((ModUtils.DF_0.format(falter_value * 10)) + "") + net.minecraft.util.text.translation.I18n.translateToLocal("description.dungeon_additions.falter_value"));
        }
        information.accept(tooltip);
    }

    //* Allows us to add staggering to enemies
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
      //  stack.damageItem(1, attacker);
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if(stack.getItem() instanceof ToolSword) {
                if(!stack.hasTagCompound()) {
                    this.setNBTonWeapon(stack, this.weaponDelay);
                    System.out.println("Setting NBT");
                }
            }
        }
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public SoundEvent getSwingSound() {
        if(swingSound != null) {
            return swingSound;
        }
        return null;
    }

    public void setNBTonWeapon(ItemStack stack, int weaponDelay)
    {
        NBTTagCompound nbt;
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("weaponDelay"))
        { nbt = stack.getTagCompound(); }
        else
        { nbt = new NBTTagCompound(); }

        nbt.setInteger("weaponDelay", weaponDelay);
        stack.setTagCompound(nbt);
    }

    public int getWeaponDelay(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("weaponDelay") ? stack.getTagCompound().getInteger("weaponDelay") : 0;
    }

    public void setWeaponDelay(ItemStack stack, int amount) {
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("weaponDelay")) {
            NBTTagCompound nbt = stack.getTagCompound();
            nbt.setInteger("weaponDelay", amount);
            stack.setTagCompound(nbt);
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        if(player != null) {
            //we want to ensure that whatever is disabled in the config will reflect everywhere
            if (this.getWeaponAnimationType() == EnumWeaponType.SWORD && !ModConfig.enable_sword_weapons ||
                    this.getWeaponAnimationType() == EnumWeaponType.DAGGER && !ModConfig.enable_dagger_weapons ||
                    this.getWeaponAnimationType() == EnumWeaponType.PARRY_SWORD && !ModConfig.enable_parry_sword_weapons ||
                    this.getWeaponAnimationType() == EnumWeaponType.SPEAR && !ModConfig.enable_spear_weapons ||
                    this.getWeaponAnimationType() == EnumWeaponType.HEAVY_AXE && !ModConfig.enable_heavy_weapons || !ModConfig.combat_system_enabled) {
                return false;
            }
            float atkCooldown = player.getCooledAttackStrength(0.5F);
            if (atkCooldown > 0.5) {
                //cancel immediate damage due to delayed swing taking place
                return true;
            }
        }
        return false;
    }



    public static UUID getAttackDamageModifier() {
        return ATTACK_DAMAGE_MODIFIER;
    }


    @Override
    public void doSweepAttack(EntityPlayer player, @Nullable EntityLivingBase entity) {
        if(entity != null && ModConfig.players_cause_falter) {
            float falterOffHandBonus = ModUtils.addOffhandDualBonuses(player);
            float totalBonus = player.onGround ? falterOffHandBonus : (falterOffHandBonus + 0.25F);
            ModUtils.addFalterTooEnemies(entity, falter_value * totalBonus, (int) (((falter_value * totalBonus) * 20)));
        }
    }
}
