package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.player.ActionDoLightningAttack;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.UUID;

public class ItemImperialHalberd extends ItemSword implements IAnimatable, IHasModel {

    public AnimationFactory factory = new AnimationFactory(this);
    public static final UUID MOVEMENT_SPEED_MODIFIER = UUID.fromString("0483aa4a-af8d-36a2-8693-22bec9caa265");
    private final AttributeModifier knockbackResistance;
    private String info_loc;
    public ItemImperialHalberd(String name, ToolMaterial material, String info_loc) {
        super(material);
        this.setMaxDamage(1624);
        setTranslationKey(name);
        setRegistryName(name);
        ModItems.ITEMS.add(this);
        setCreativeTab(DungeonAdditionsTab.ALL);
        this.info_loc = info_loc;
        this.knockbackResistance = new AttributeModifier("halberdKnockbackResistance", 0.1, 0);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = 16 * 20;

        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.IMPERIAL_START_MAGIC, SoundCategory.NEUTRAL, 0.6f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
            player.motionX = 0;
            player.motionZ = 0;
            player.velocityChanged = true;
            new ActionDoLightningAttack().performAction(player);
            stack.damageItem(8, player);
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
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
            multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(MOVEMENT_SPEED_MODIFIER, "Movement Speed", -0.01, 0));
            multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), this.knockbackResistance);
        }

        return multimap;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public float getAttackDamage() {
        return super.getAttackDamage();
    }

    protected double getAttackSpeed() {
        return -2.7D;
    }

    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player)
    { return false; }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker)
    {
        return true;
    }
}
