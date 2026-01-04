package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.frost_dungeon.ProjectileFrostBullet;
import com.dungeon_additions.da.entity.player.ActionSummonVoidSpikes;
import com.dungeon_additions.da.entity.void_dungeon.ProjectileVoidClysmBolt;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ItemVoidStaff extends ToolSword implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);
    private String info_loc;

    public ItemVoidStaff(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        this.info_loc = info_loc;
        this.setCreativeTab(DungeonAdditionsTab.ALL);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = ModConfig.void_staff_cooldown * 20;

        if(!world.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            double healthCurr = player.getHealth() / player.getMaxHealth();
            int damage_bonus = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
            int damage_bonus_2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, stack);
            int damage_bonus_3 = EnchantmentHelper.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, stack);
            float damage = (float) (this.getAttackDamage() + 2 + damage_bonus + (damage_bonus_2 * 0.5) + (damage_bonus_3 * 0.5));
            if(healthCurr > 0.95) {
                damage = (float) (this.getAttackDamage() + 2 + damage_bonus + ModConfig.void_staff_health_modif + (damage_bonus_2 * 0.5) + (damage_bonus_3 * 0.5));
            }
            if(player.isSneaking()) {
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.VOIDCLYSM_CLAP_ATTACK, SoundCategory.NEUTRAL, 1.0f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));
                new ActionSummonVoidSpikes(damage + 4).performAction(player);
                stack.damageItem(6, player);
                player.getCooldownTracker().setCooldown(this, SwordCoolDown * 7);
            } else {
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.LICH_SHOOT_MISSILE, SoundCategory.NEUTRAL, 1.0f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));
                Vec3d playerLookVec = player.getLookVec();
                Vec3d playerPos = new Vec3d(player.posX + playerLookVec.x * 1.4D,player.posY + playerLookVec.y + player.getEyeHeight(), player. posZ + playerLookVec.z * 1.4D);
                ProjectileVoidClysmBolt bullet = new ProjectileVoidClysmBolt(world, player, damage + ModUtils.addMageSetBonus(player, 0));
                bullet.setPosition(playerPos.x, playerPos.y, playerPos.z);
                bullet.shoot(playerLookVec.x, playerLookVec.y, playerLookVec.z, 1.6f, 1.0f);
                bullet.rotationPitch = player.rotationPitch;
                bullet.rotationYaw = player.rotationYaw;
                world.spawnEntity(bullet);
                bullet.setTravelRange(32F);
                stack.damageItem(1, player);
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
            }

        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }


    @Override
    protected double getAttackSpeed() {
        return -2.7000000953674316D;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
