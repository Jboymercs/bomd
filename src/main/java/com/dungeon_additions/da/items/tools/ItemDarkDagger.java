package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.mini_blossom.EntityDart;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemDarkDagger extends ToolSword{

    private String info_loc;

    public ItemDarkDagger(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        this.info_loc = info_loc;
        this.setMaxDamage(520);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = 10 * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            Vec3d moveVec = player.getLookVec().scale(-((1.4 * 0.5) + 0.1D));
            if(player.canBePushed()) {
                player.motionX = moveVec.x;
                player.motionY = moveVec.y * 0.7;
                player.fallDistance = 0;
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
                player.motionZ = moveVec.z;
                player.velocityChanged = true;
            }
            stack.damageItem(2, player);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }


    @Override
    protected double getAttackSpeed() {
        return -2.1000000953674316D;
    }
}
