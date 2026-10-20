package com.dungeon_additions.da.items;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.items.tools.ToolSword;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ItemTreasureBag extends ItemBase {

    private long bossTreasureSeed;
    private List<ItemStack> rewards = Lists.newArrayList();

    public ItemTreasureBag(String name, CreativeTabs tab) {
        super(name, tab);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if(stack.getItem() instanceof ItemTreasureBag) {
                if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("treasure_reward")) {
                }
            }
        }
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    protected boolean getTreasureReward(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("treasure_reward");
    }

    protected String getTreasureTag(ItemStack stack) {
        if(getTreasureReward(stack)) {
            return stack.getTagCompound().getString("treasure_reward");
        }
        return "voidiant";
    }



    public void setTreasureLootTable(ItemStack stack, String name) {
        if(getTreasureReward(stack)) {
            NBTTagCompound nbt = stack.getTagCompound();
            assert nbt != null;
            nbt.setString("treasure_reward", name);
            stack.setTagCompound(nbt);
        } else {
            NBTTagCompound nbt;
            nbt = new NBTTagCompound();
            nbt.setString("treasure_reward", name);
            stack.setTagCompound(nbt);
        }
    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if(!worldIn.isRemote && stack.getItem() == this) {
                this.spawnBossLootTable(worldIn, player, stack);
                worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.OPEN_TREASURE_BAG, SoundCategory.NEUTRAL, 1f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
                stack.shrink(1);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(getTreasureReward(stack)) {
            //add tool tips for each unique boss or drop
            if(Objects.equals(getTreasureTag(stack), "rot_knight_boss")) {
                tooltip.add(TextFormatting.GREEN + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.ancient_fallen.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "void_blossom")) {
                tooltip.add(TextFormatting.DARK_PURPLE + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.void_blossom.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "great_wyrk")) {
                tooltip.add(TextFormatting.AQUA + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.great_wyrk.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "aegyptian_colossus")) {
                tooltip.add(TextFormatting.YELLOW + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.aegyptian_colossus.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "aegyptian_warlord")) {
                tooltip.add(TextFormatting.YELLOW + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.aegyptian_warlord.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "flame_knight")) {
                tooltip.add(TextFormatting.RED + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.flame_knight.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "night_lich")) {
                tooltip.add(TextFormatting.BLUE + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.night_lich.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "apathyr")) {
                tooltip.add(TextFormatting.AQUA + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.apathyr.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "voidclysm")) {
                tooltip.add(TextFormatting.LIGHT_PURPLE + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.voidclysm.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "obsidilith")) {
                tooltip.add(TextFormatting.LIGHT_PURPLE + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.obsidilith.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "dauntless")) {
                tooltip.add(TextFormatting.RED + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.dauntless.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "high_king")) {
                tooltip.add(TextFormatting.GOLD + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.high_king.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "high_dragon")) {
                tooltip.add(TextFormatting.GOLD + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.high_king_drake.name")));
            }
            if(Objects.equals(getTreasureTag(stack), "roh")) {
                tooltip.add(TextFormatting.RED + net.minecraft.client.resources.I18n.format(net.minecraft.util.text.translation.I18n.translateToLocal("entity.darkdrift_devil.name")));
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }


    protected ItemStack spawnBossLootTable(World world, EntityPlayer player, ItemStack stack) {
        if(!world.isRemote) {
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer) world)).withLootedEntity(player);
            rewards = world.getLootTableManager().getLootTableFromLocation(new ResourceLocation(ModReference.MOD_ID, getTreasureTag(stack))).generateLootForPools(this.bossTreasureSeed == 0 ? new Random() :new Random(this.bossTreasureSeed), lootcontext$builder.build());
            if(rewards != null) {
                for (ItemStack item : rewards) {
                    //spawn all the items!
                    Vec3d playerLookVec = player.getLookVec();
                    Vec3d playerPos = new Vec3d(player.posX + playerLookVec.x * 1.2D,player.posY + playerLookVec.y + player.getEyeHeight(), player. posZ + playerLookVec.z * 1.2D);
                    EntityItem itemToThrow = new EntityItem(world, playerPos.x, playerPos.y, playerPos.z, item);
                    world.spawnEntity(itemToThrow);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

}
