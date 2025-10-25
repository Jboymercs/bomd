package com.dungeon_additions.da.items.gun;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.ProjectileFastGhostCrystal;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.ItemBase;
import com.dungeon_additions.da.items.ItemGoldenArrow;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ItemGolemCannon extends ItemBase implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);
    private String info_loc;

    public ItemGolemCannon(String name, CreativeTabs tab, String info_loc) {
        super(name, tab);
        this.info_loc = info_loc;
        this.maxStackSize = 1;
        this.setMaxDamage(200);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn)
    {
        ItemStack itemstack = player.getHeldItem(handIn);

        if(!world.isRemote && !player.getCooldownTracker().hasCooldown(this) ) {
            ItemStack ammo_stack = this.findAmmo(player);

            if (!ammo_stack.isEmpty() || player.isCreative()) {
                ammo_stack.shrink(1);
                ProjectileFastGhostCrystal ghostCrystal = new ProjectileFastGhostCrystal(world, player, (float) ModConfig.sentinel_cannon_damage);
                Vec3d playerLookVec = player.getLookVec();
                Vec3d playerPos = new Vec3d(player.posX + playerLookVec.x * 1.4D, player.posY + playerLookVec.y + player.getEyeHeight(), player.posZ + playerLookVec.z * 1.4D);
                ghostCrystal.setPosition(playerPos.x, playerPos.y, playerPos.z);
                ghostCrystal.shoot(playerLookVec.x, playerLookVec.y, playerLookVec.z, 1.2f, 1.0f);
                world.spawnEntity(ghostCrystal);
                ghostCrystal.setTravelRange(32F);
                itemstack.damageItem(1, player);
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.VOLACTILE_SHOOT_CANNON, SoundCategory.NEUTRAL, 1.25f, 0.4f / (world.rand.nextFloat() * 0.4F + 0.2f));
                player.getCooldownTracker().setCooldown(this, (int) (ModConfig.sentinel_cannon_cooldown * 20));
                if(player.canBePushed()) {
                    Vec3d moveVec = player.getLookVec().scale(-((4.4 * 0.2) + 0.1D));
                    player.motionX = moveVec.x;
                    player.motionY = moveVec.y;
                    player.motionZ = moveVec.z;
                    player.velocityChanged = true;
                }
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }


    protected ItemStack findAmmo(EntityPlayer player)
    {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemstack))
                {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    protected boolean isArrow(ItemStack stack)
    {
        return stack.getItem() == ModItems.GAELON_SHARD;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 20;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
