package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemNovikSword extends ToolSword implements IHasModel {

    String info_loc;

    private boolean isEnraged = false;
    private int currentTick;

    public ItemNovikSword(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.info_loc = info_loc;
        this.setCreativeTab(DungeonAdditionsTab.ALL);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.INCENDIUM_HELMET ? (ModConfig.novik_sword_cooldown * 20) - 200 : ModConfig.novik_sword_cooldown * 20;
        if(!player.getCooldownTracker().hasCooldown(this)) {
            worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.REANIMATE_CAST, SoundCategory.NEUTRAL, 1.3f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
            ModUtils.performNTimes(10, (i) -> {
                Main.proxy.spawnParticle(15, player.posX, player.posY + ModRand.range(1, 3), player.posZ, worldIn.rand.nextFloat()/6 - worldIn.rand.nextFloat()/6, 0.06, worldIn.rand.nextFloat()/6 - worldIn.rand.nextFloat()/6, 100);
            });
            if(!worldIn.isRemote) {
                stack.damageItem(6, player);
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
                this.currentTick = player.ticksExisted + 160;
                this.isEnraged = true;
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (attacker.world.isRemote) return false;
        if(attacker instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) attacker);
            if(this.isEnraged) {
                float realAttackDamage = (float) (this.getAttackDamage());
                target.attackEntityFrom(ModUtils.causeAxeDamage(attacker).setDifficultyScaled(), (float) realAttackDamage);
                player.heal(1F);
            }
        }
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if(entityIn instanceof EntityPlayer && !worldIn.isRemote && this.isEnraged) {
            EntityPlayer player = ((EntityPlayer) entityIn);
            if(player.ticksExisted > currentTick) {
                this.currentTick = 0;
                this.isEnraged = false;
            }
            if(player.ticksExisted % 20 == 0) {
                Main.proxy.spawnParticle(15, player.posX, player.posY + 1.5, player.posZ, worldIn.rand.nextFloat()/6 - worldIn.rand.nextFloat()/6, 0.04, worldIn.rand.nextFloat()/6 - worldIn.rand.nextFloat()/6, 100);
            }
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }

    @Override
    protected double getAttackSpeed() {
        return -2.1000000953674316D;
    }

    @Override
    public void registerModels() {
        {
            Main.proxy.registerItemRenderer(this, 0, "inventory");
        }}
}
