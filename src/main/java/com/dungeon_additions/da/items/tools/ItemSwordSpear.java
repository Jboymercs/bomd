package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.entity.player.ActionShootLightRing;
import com.dungeon_additions.da.entity.player.ActionSummonHolySpikes;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ItemSwordSpear extends ToolSword implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);
    private String info_loc;

    private String ANIM_STRIKE = "use_blade";
    private String controllerName = "attack_controller";

    private boolean setRising = false;

    private boolean setTooAir = false;
    public ItemSwordSpear(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.info_loc = info_loc;
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        this.setMaxDamage(2031);
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
            if(player.isSneaking()) {
                player.setActiveHand(handIn);
                this.setRising = true;
                this.hoverTime = 100;
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.HIGH_KING_CAST_CLAW, SoundCategory.NEUTRAL, 1.0f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            } else {
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.HIGH_KING_SWING_MAGIC, SoundCategory.NEUTRAL, 1.0f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));
                itemstack.damageItem(1, player);
                player.getCooldownTracker().setCooldown(this, (7 * 20));
                new ActionShootLightRing().performAction(player);

                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = ((EntityPlayer) entityLiving);
            int i = this.getMaxItemUseDuration(stack) - timeLeft;

            if(i >= 60) {
                this.setTooAir = true;
                Vec3d moveVec = player.getLookVec().scale(1.3F);
                if(player.canBePushed()) {
                    player.motionX = moveVec.x;
                    player.motionY = moveVec.y * 0.3;
                    player.getCooldownTracker().setCooldown(this, 20 * 20);
                    player.motionZ = moveVec.z;
                    player.velocityChanged = true;
                    player.fallDistance = 0;
                }
            } else {
                player.getCooldownTracker().setCooldown(this, 10 * 20);
            }


            this.setRising = false;
        }
    }

    private int hoverTime = 100;
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        if(entityIn instanceof EntityPlayer && !worldIn.isRemote) {

            if(this.setRising) {
                int y = getSurfaceHeight(worldIn, new BlockPos(entityIn.posX, 0, entityIn.posZ), (int) entityIn.posY - 20, (int) entityIn.posY + 1);
                if(entityIn.canBePushed()) {
                    if(hoverTime > 1) {
                        if (entityIn.posY >= y + 4) {
                            entityIn.motionY = 0;
                            entityIn.fallDistance = 0;
                            entityIn.velocityChanged = true;
                        } else {
                            entityIn.motionY = 0.2;
                            entityIn.motionZ = 0;
                            entityIn.motionX = 0;
                            entityIn.velocityChanged = true;
                        }
                    } else {
                        this.setRising = false;
                        hoverTime = 100;
                    }
                    hoverTime--;
                }
            }

            if(this.setTooAir) {
                entityIn.fallDistance = 0;
                boolean hasGround = worldIn.isAirBlock(entityIn.getPosition().down());
                if (!hasGround) {
                    worldIn.playSound((EntityPlayer) null, entityIn.posX, entityIn.posY, entityIn.posZ, SoundsHandler.HIGH_KING_SPEAR_IMPACT, SoundCategory.NEUTRAL, 1.0f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
                    new ActionSummonHolySpikes().performAction(((EntityPlayer) entityIn));
                    this.setTooAir = false;
                }
            }
        }
    }

    public int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock()) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 150;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    protected double getAttackSpeed() {
        return -2.9D;
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker)
    {
        return true;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
