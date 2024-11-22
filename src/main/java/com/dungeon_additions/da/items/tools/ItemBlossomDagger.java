package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.mini_blossom.EntityDart;
import com.dungeon_additions.da.util.ModUtils;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemBlossomDagger extends ToolSword{

    private String info_loc;

    public ItemBlossomDagger(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.info_loc = info_loc;
        this.setMaxDamage(320);
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
        int SwordCoolDown = ModConfig.dagger_cooldown * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            float inaccuracy = 0.0f;
            float speed = 1.4f;
            float pitch = player.rotationPitch; // Projectiles aim straight ahead always

            // Shoots projectiles in a small arc
            for (int i = 0; i < 5; i++) {
                EntityDart projectile = new EntityDart(worldIn, player);
                projectile.shoot(player, pitch, player.rotationYaw - 30 + (i * 15), 0.0F, speed, inaccuracy);
                projectile.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
                player.world.spawnEntity(projectile);
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
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
