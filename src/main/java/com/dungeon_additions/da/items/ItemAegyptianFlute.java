package com.dungeon_additions.da.items;

import com.dungeon_additions.da.entity.desert_dungeon.friendly.EntityFriendlyScutterBeetle;
import com.dungeon_additions.da.entity.sky_dungeon.friendly.EntityFriendlyHalberd;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemAegyptianFlute extends ItemBase{
    public final int itemUseDuration;
    private String info_loc;

    public ItemAegyptianFlute(String name, String info_loc, CreativeTabs tab) {
        super(name, tab);
        this.itemUseDuration = 32;
        this.setMaxStackSize(1);
        this.setMaxDamage(8);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!playerIn.getCooldownTracker().hasCooldown(this)) {
            playerIn.setActiveHand(handIn);
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else {
            return new ActionResult(EnumActionResult.FAIL, itemstack);
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase living) {
        if (living instanceof EntityPlayer && !world.isRemote) {
            EntityPlayer player = ((EntityPlayer) living);
            ItemStack summonerTrinket = ModUtils.findTrinket(new ItemStack(ModItems.STALWART_SUMMONER), player);
            int SwordCoolDown = 90 * 20;
            itemStack.damageItem(1, player);
            EntityFriendlyScutterBeetle friend_halberd = new EntityFriendlyScutterBeetle(world);
            friend_halberd.setOwnerId(player.getUniqueID());
            friend_halberd.onSummonViaPlayer(player.getPosition(), player);
            player.world.spawnEntity(friend_halberd);
            if(!summonerTrinket.isEmpty()) {
                summonerTrinket.damageItem(1, player);
                friend_halberd.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 700, 0, false, true));
                friend_halberd.addPotionEffect(new PotionEffect(MobEffects.SPEED, 700, 0, false, true));
            }
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
            world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.LICH_SUMMON_MINION, SoundCategory.NEUTRAL, 1.0f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));

        }
        return super.onItemUseFinish(itemStack, world, living);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }

}
