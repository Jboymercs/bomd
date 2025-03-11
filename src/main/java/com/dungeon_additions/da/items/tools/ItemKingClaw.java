package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.player.ActionDoClawSlash;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ItemKingClaw extends ToolSword implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);

    private String info_loc;


    public ItemKingClaw(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.setMaxDamage(1213);
        this.setHarvestLevel("spade", 4);
        setCreativeTab(DungeonAdditionsTab.ALL);
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
        boolean hasOffhandClaw = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() == ModItems.KING_CLAW && player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.KING_CLAW;
        int SwordCoolDown = hasOffhandClaw ? (int) ((ModConfig.king_claw_cooldown * 10) * 0.5) : ModConfig.king_claw_cooldown * 10;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.HIGH_KING_CAST_CLAW, SoundCategory.NEUTRAL, 1.0f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
            new ActionDoClawSlash(hasOffhandClaw).performAction(player);
            stack.damageItem(5, player);
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }


    protected double getAttackSpeed() {
        return -2.0D;
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
    public AnimationFactory getFactory() {
        return factory;
    }
}
