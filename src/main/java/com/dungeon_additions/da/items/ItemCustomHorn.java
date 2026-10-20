package com.dungeon_additions.da.items;

import com.dungeon_additions.da.entity.desert_dungeon.friendly.EntityFriendlyScutterBeetle;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemCustomHorn extends ItemBase{

    public final int itemUseDuration;
    private String info_loc;
    private Potion potionUse;
    private int potionDuration;


    public ItemCustomHorn(String name, CreativeTabs tab, String info_loc, int itemUseDuration, Potion potion, int time) {
        super(name, tab);
        this.itemUseDuration = itemUseDuration;
        this.info_loc = info_loc;
        this.setMaxStackSize(1);
        this.setMaxDamage(20);
        this.potionDuration = time;
        this.potionUse = potion;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!playerIn.getCooldownTracker().hasCooldown(this)) {

            if(itemstack.getItem() == ModItems.DEVOTION_HORN) {
                worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundsHandler.HORN_DEVOTION, SoundCategory.NEUTRAL, 1.5f, 1.0F + ModRand.getFloat(0.3F));
            }
            if(itemstack.getItem() == ModItems.GOLDEN_HORN) {
                worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundsHandler.HORN_GOLDEN, SoundCategory.NEUTRAL, 1.5f, 1.0F + ModRand.getFloat(0.3F));
            }
            if(itemstack.getItem() == ModItems.RESPITE_HORN || itemstack.getItem() == ModItems.RALLY_HORN) {
                worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundsHandler.HORN_REGULAR, SoundCategory.NEUTRAL, 1.5f, 1.0F + ModRand.getFloat(0.3F));
            }
            if(itemstack.getItem() == ModItems.MAGIC_HORN) {
                worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundsHandler.HORN_MAGIC, SoundCategory.NEUTRAL, 1.5f, 1.0F + ModRand.getFloat(0.3F));
            }
            if(itemstack.getItem() == ModItems.POISON_HORN) {
                worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundsHandler.HORN_POISON, SoundCategory.NEUTRAL, 1.5f, 1.0F + ModRand.getFloat(0.3F));
            }
           // worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundsHandler.AEGYPTIAN_FLUTE_PLAY, SoundCategory.NEUTRAL, 1.5f, 0.7f / (worldIn.rand.nextFloat() * 0.2F + 0.2f));
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
            int SwordCoolDown = 300 * 20;
            itemStack.damageItem(1, player);
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
            player.addPotionEffect(new PotionEffect(potionUse, potionDuration, 0));
            List<EntityPlayer> nearbyPlayers = world.getEntitiesWithinAABB(EntityPlayer.class, player.getEntityBoundingBox().grow(5D), e -> !e.getIsInvulnerable());
            if(!nearbyPlayers.isEmpty()) {
                for(EntityPlayer players : nearbyPlayers) {
                    players.addPotionEffect(new PotionEffect(potionUse, potionDuration, 0));
                    if(itemStack.getItem() == ModItems.RALLY_HORN) {
                       players.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 1200, 0));
                    }
                }
            }
            //world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.APATHYR_CAST_SPIKES, SoundCategory.NEUTRAL, 0.75f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));

        }
        return super.onItemUseFinish(itemStack, world, living);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
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
