package com.dungeon_additions.da.items.shield;

import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BOMDShieldItem extends ItemShield {

    protected boolean usedAbility = false;

    public BOMDShieldItem(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        ModItems.ITEMS.add(this);
        this.setMaxDamage(256);
        this.setMaxStackSize(1);
        //this.setNBTonShield(new ItemStack(this),this.hitCounter);
        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    public void setNBTonShield(ItemStack stack, int hitCounter)
    {
        NBTTagCompound nbt;
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("counter"))
        { nbt = stack.getTagCompound(); }
        else
        { nbt = new NBTTagCompound(); }

        nbt.setInteger("counter", hitCounter);
        stack.setTagCompound(nbt);
    }

    /** Preforms detection logic for the Lodestone. */
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if(stack.getItem() instanceof BOMDShieldItem) {
                if(!stack.hasTagCompound()) {
                    this.setNBTonShield(stack, 0);
                }
            }
        }
    }


    public int getHitCounter(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("counter") ? stack.getTagCompound().getInteger("counter") : 0;
    }

    public void setHitCounter(ItemStack stack, int amount) {
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("counter")) {
            NBTTagCompound nbt = stack.getTagCompound();
            nbt.setInteger("counter", amount);
            stack.setTagCompound(nbt);
            System.out.println("Hit counter has gone up");
        }
    }

    public static void handleDamageEvent(LivingAttackEvent e) {
        float damage = e.getAmount();
        ItemStack activeItemStack;
        EntityPlayer player;
        if (e.getEntityLiving() instanceof EntityPlayer) {
            player = (EntityPlayer) e.getEntityLiving();
            if (player.getActiveItemStack() != null) {
                activeItemStack = player.getActiveItemStack();
                if (damage > 0.0F && activeItemStack != null && activeItemStack.getItem() instanceof BOMDShieldItem) {
                    ((BOMDShieldItem) activeItemStack.getItem()).onBlockingDamage(activeItemStack, player);
                }
            }
        }
    }




    public boolean onApplyButtonPressed(EntityPlayer player, World world, ItemStack stack){
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BLOCK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    /**
     * Called when the player is attacked when blocking with the shield.
     */
    public void onBlockingDamage(ItemStack shield, EntityPlayer player) {
        shield.damageItem(1, player);
    }
}
