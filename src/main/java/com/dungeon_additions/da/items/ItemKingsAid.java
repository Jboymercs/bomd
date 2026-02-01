package com.dungeon_additions.da.items;

import com.dungeon_additions.da.entity.frost_dungeon.wyrk.EntityFriendWyrk;
import com.dungeon_additions.da.entity.sky_dungeon.friendly.EntityFriendlyHalberd;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemKingsAid extends ItemBase{

    private String info_loc;

    public ItemKingsAid(String name, String info_loc, CreativeTabs tab) {
        super(name, tab);
        this.setMaxStackSize(1);
        this.setMaxDamage(15);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = 120 * 20;

        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            List<EntityFriendlyHalberd> nearbyWyrk = player.world.getEntitiesWithinAABB(EntityFriendlyHalberd.class, player.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable());
            ItemStack summonerTrinket = ModUtils.findTrinket(new ItemStack(ModItems.STALWART_SUMMONER), player);
            boolean flag = false;

            if(!nearbyWyrk.isEmpty()) {
                flag = true;
            }

            if(!flag) {
                stack.damageItem(1, player);
                EntityFriendlyHalberd friend_halberd = new EntityFriendlyHalberd(worldIn);
                friend_halberd.setOwnerId(player.getUniqueID());
                friend_halberd.onSummonViaPlayer(player.getPosition(), player);
                player.world.spawnEntity(friend_halberd);
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
                if(!summonerTrinket.isEmpty()) {
                    summonerTrinket.damageItem(1, player);
                    friend_halberd.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 700, 0, false, true));
                    friend_halberd.addPotionEffect(new PotionEffect(MobEffects.SPEED, 700, 0, false, true));
                }
                worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.LICH_SUMMON_MINION, SoundCategory.NEUTRAL, 1.0f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
            } else {
                player.sendStatusMessage(new TextComponentTranslation("da.friend_halberd", new Object[0]), true);
            }

        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }
}
