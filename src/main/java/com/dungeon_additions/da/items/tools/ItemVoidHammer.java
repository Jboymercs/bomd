package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.player.ActionPlayerShootBloodSpray;
import com.dungeon_additions.da.entity.player.ActionVoidFlay;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidBlackHole;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.util.ISweepAttackOverride;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;

public class ItemVoidHammer extends ItemSword implements IAnimatable, IHasModel, ISweepAttackOverride {

    public AnimationFactory factory = new AnimationFactory(this);

    private String info_loc;

    public ItemVoidHammer(String name, Item.ToolMaterial material, String info_loc) {
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
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if(!world.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            if(player.isSneaking()) {
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.NEUTRAL, 3.0f, 0.3f / (world.rand.nextFloat() * 0.4F + 0.2f));
                EntityVoidBlackHole blackHole = new EntityVoidBlackHole(world, player, this.getAttackDamage() + ModUtils.addMageSetBonus(player, 0) + ModUtils.addAbilityBonusDamage(stack, 0.75F));
                Vec3d playerLookVec = player.getLookVec();
                blackHole.setPosition(player.posX + playerLookVec.x * 1.4D, player.posY + player.getEyeHeight() + playerLookVec.y * 1.4D, player.posZ + playerLookVec.z * 1.4D);
                world.spawnEntity(blackHole);
                player.getCooldownTracker().setCooldown(this, (ModConfig.void_hammer_blackhole_cooldown * 20));
            } else {
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.LICH_SHOOT_MISSILE, SoundCategory.NEUTRAL, 1.0f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));
                new ActionVoidFlay().performAction(player);
                stack.damageItem(2, player);
                player.getCooldownTracker().setCooldown(this, (ModConfig.void_hammer_projectile_cooldown * 20));
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
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
        float realAttackDamage = this.getAttackDamage();
        float regular_damage = (realAttackDamage / 2) - 2;
        float armor_damage = (realAttackDamage / 2);

        if(attacker instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) attacker);
            if(!player.getCooldownTracker().hasCooldown(this)) {
                target.attackEntityFrom(ModUtils.causeAxeDamage(attacker).setDifficultyScaled(), (float) regular_damage);
                target.hurtResistantTime = 0;
                target.attackEntityFrom(ModUtils.causeAxeDamage(attacker).setDamageBypassesArmor(), (float) armor_damage / 2);
                player.getCooldownTracker().setCooldown(this, axeCoolDown);
            }
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
        return -3.5D;
    }
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player)
    { return false; }



    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker)
    {
        return true;
    }

    @Override
    public void doSweepAttack(EntityPlayer player, @Nullable EntityLivingBase entity) {
        ModUtils.doSweepAttack(player, entity, (e) -> {
        }, 9, 3);
    }
}
