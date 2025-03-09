package com.dungeon_additions.da.items.shield;

import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDraugrShield extends ItemShield implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);
    private String info_loc;

    public ItemDraugrShield(String name, CreativeTabs tabs, String info_loc) {
        super();
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(tabs);
        this.setMaxDamage(832);
        this.info_loc = info_loc;
        this.maxStackSize = 1;
        ModItems.ITEMS.add(this);
        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.getSubCompound("BlockEntityTag") != null)
        {
            EnumDyeColor enumdyecolor = TileEntityBanner.getColor(stack);
            return I18n.translateToLocal("da:item.draugr_shield." + enumdyecolor.getTranslationKey() + ".name");
        }
        else
        {
            return I18n.translateToLocal("da.desc.draugr_shield_name");
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        ItemBanner.appendHoverTextFromTileEntityTag(stack, tooltip);
    }


    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {

        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity)
    {
        return true;
    }


    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
