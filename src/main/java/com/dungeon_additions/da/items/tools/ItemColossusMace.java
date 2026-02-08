package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntityColossusSigil;
import com.dungeon_additions.da.entity.player.ActionPlayerDesertSlam;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ItemColossusMace extends ToolSword implements IAnimatable {
    public AnimationFactory factory = new AnimationFactory(this);

    private String info_loc;

    public ItemColossusMace(String name, Item.ToolMaterial material, String info_loc) {
        super(name, material);
        this.setMaxDamage(1032);
        ModItems.ITEMS.add(this);
        setCreativeTab(DungeonAdditionsTab.ALL);
        this.info_loc = info_loc;
    }


    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        attacker.world.playSound((EntityPlayer) null, attacker.posX, attacker.posY, attacker.posZ, SoundsHandler.COLOSSUS_SWING, SoundCategory.NEUTRAL, 0.6f, 0.7f / (attacker.world.rand.nextFloat() * 0.4F + 0.2f));
            if(!attacker.world.isRemote) {
                target.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 60, 0, false, false));
            }
            return true;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = ModConfig.colossal_mace_cooldown * 20;

        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            //summon sigil
            if(player.isSneaking()) {
                float damage = ModConfig.colossal_mace_ability_damage + ModUtils.addAbilityBonusDamage(player.getHeldItemMainhand(), 1) + ModUtils.addMageSetBonus(player, 1);
                worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.DESERT_BOSS_SUMMON_HELPER, SoundCategory.NEUTRAL, 1f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
                EntityColossusSigil sigil = new EntityColossusSigil(worldIn, player, damage);
                sigil.setPosition(player.posX, player.posY + 2, player.posZ);
                worldIn.spawnEntity(sigil);
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
                stack.damageItem(2, player);
            } else {
                worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.COLOSSUS_MACE_SPELL, SoundCategory.NEUTRAL, 1f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
                double reverseAOEBOOSt = player.getMaxHealth() / player.getHealth();
                if(reverseAOEBOOSt > 8) {
                    reverseAOEBOOSt = 8;
                }
                new ActionPlayerDesertSlam((int) reverseAOEBOOSt).performAction(player);
                player.getCooldownTracker().setCooldown(this, SwordCoolDown/2);
                stack.damageItem(1, player);
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
    public void registerControllers(AnimationData data) {

    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker)
    {
        return true;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected double getAttackSpeed() {
        return -3.4000000953674316D;
    }
}
