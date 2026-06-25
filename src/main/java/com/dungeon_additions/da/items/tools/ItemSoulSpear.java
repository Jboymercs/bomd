package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.animation.item.EnumWeaponType;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.player.ActionPlayerShootComet;
import com.dungeon_additions.da.entity.player.ActionPlayerShootSpead;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
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

public class ItemSoulSpear extends ToolSword {

    private String info_loc;

    public ItemSoulSpear(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.info_loc = info_loc;
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        this.setMaxDamage(1400);
        this.weapon_type = EnumWeaponType.SPEAR;
        this.weaponReach += 1.25F;
        this.swingSound = SoundsHandler.LICH_MAGIC_SWING;
        this.falter_value = 0.09F;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.AQUA + ModUtils.translateDesc(info_loc));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = ModConfig.soul_spear_cooldown * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            if(player.isSneaking()) {
                new ActionPlayerShootComet().performAction(player);
                Vec3d pos = player.getPositionVector();
                worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.LICH_SHOOT_FIREBALL, SoundCategory.NEUTRAL, 1.0f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
                stack.damageItem(10, player);
                player.getCooldownTracker().setCooldown(this, SwordCoolDown * 2);
            } else {
                new ActionPlayerShootSpead().performAction(player);
                Vec3d pos = player.getPositionVector();
                worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.LICH_SHOOT_MISSILE, SoundCategory.NEUTRAL, 1.0f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
                stack.damageItem(5, player);
                player.getCooldownTracker().setCooldown(this, SwordCoolDown/2 + 20);
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
    public double getAttackSpeed() {
        return -2.6000000953674316D;
    }

}
