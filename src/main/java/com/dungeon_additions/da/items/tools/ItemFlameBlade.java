package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.player.ActionShootFlameBlade;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
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

public class ItemFlameBlade extends ToolSword implements IAnimatable, IHasModel {

    public AnimationFactory factory = new AnimationFactory(this);

    String info_loc;

    public ItemFlameBlade(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        this.info_loc = info_loc;
        this.setCreativeTab(DungeonAdditionsTab.ALL);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        if(ModConfig.enable_scaling_tooltips) {
            tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = ModConfig.flame_blade_cooldown * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            new ActionShootFlameBlade().performAction(player);
            worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.INCENDIUM_SWING, SoundCategory.NEUTRAL, 1.0f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
            stack.damageItem(6, player);
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }

    @Override
    protected double getAttackSpeed() {
        return -2.3000000953674316D;
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
    public AnimationFactory getFactory() {
        return factory;
    }
}
