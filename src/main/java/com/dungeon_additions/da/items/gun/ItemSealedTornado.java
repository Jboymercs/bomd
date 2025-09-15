package com.dungeon_additions.da.items.gun;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.player.ActionDoDesertTornado;
import com.dungeon_additions.da.entity.player.ActionFlameWave;
import com.dungeon_additions.da.items.ItemBase;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemSealedTornado extends ItemBase {

    private String info_loc;

    public ItemSealedTornado(String name, String info_loc, CreativeTabs tab) {
        super(name, tab);
        this.info_loc = info_loc;
        this.setCreativeTab(tab);
        this.setMaxStackSize(16);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = ModConfig.sealed_bottle_cooldown * 20;

        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.AEGYPTIA_SWING_STORM, SoundCategory.NEUTRAL, 1.0f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
            stack.shrink(1);
            new ActionDoDesertTornado().performAction(player);
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
