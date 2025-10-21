package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.player.ActionPlayerShootBloodSpray;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityBloodPile;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
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

public class ItemBloodySwordSpear extends ToolSword implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);
    private String info_loc;

    private String ANIM_STRIKE = "use_blade";
    private String controllerName = "attack_controller";
    private boolean isDashing = false;
    private int dashSpawnBlood = 0;
    public ItemBloodySwordSpear(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.info_loc = info_loc;
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        this.setMaxDamage(2031);
      //  this.reachDistanceValue = 1.0F;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        attacker.world.playSound((EntityPlayer) null, attacker.posX, attacker.posY, attacker.posZ, SoundsHandler.HIGH_KING_SWING, SoundCategory.NEUTRAL, 0.4f, 0.7f / (attacker.world.rand.nextFloat() * 0.4F + 0.2f));
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn)
    {
        ItemStack itemstack = player.getHeldItem(handIn);
        if(!world.isRemote && !player.getCooldownTracker().hasCooldown(this) ) {
            if(player.isSneaking()) {
                player.setActiveHand(handIn);
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.BLOOD_SPEAR_USE, SoundCategory.NEUTRAL, 1.0f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            } else {
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.HIGH_KING_SWING_IMPALE, SoundCategory.NEUTRAL, 1.0f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));
                new ActionPlayerShootBloodSpray().performAction(player);
                itemstack.damageItem(1, player);
                player.getCooldownTracker().setCooldown(this, (ModConfig.bloody_sword_spear_cooldown * 20));
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
            if(!world.isRemote && !player.getCooldownTracker().hasCooldown(this) && i >= 70) {
                //initiate the Dash
                Vec3d moveVec = player.getLookVec().scale(2.4F);
                if(player.canBePushed()) {
                    player.motionX = moveVec.x;
                    player.motionY = moveVec.y * 0.3;
                    player.getCooldownTracker().setCooldown(this, (ModConfig.bloody_sword_spear_cooldown * 20) * 2);
                    player.motionZ = moveVec.z;
                    player.velocityChanged = true;
                }
                this.isDashing = true;
                this.dashSpawnBlood = 20;
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 1.0f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));
                stack.damageItem(2, player);
            } else {

                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0f, 0.8f / (world.rand.nextFloat() * 0.4F + 0.2f));
            }
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if(entityIn instanceof EntityPlayer && !worldIn.isRemote) {

            if(isDashing) {
                if (entityIn.motionX < 0.08 && entityIn.motionZ < 0.08 && dashSpawnBlood < 15 || dashSpawnBlood < 1) {
                    this.isDashing = false;
                    this.dashSpawnBlood = 0;
                }
                EntityBloodPile pile = new EntityBloodPile(worldIn, true, ((EntityPlayer) entityIn));
                int y = getSurfaceHeight(worldIn, new BlockPos(entityIn.posX, 0, entityIn.posZ), (int) entityIn.posY - 6, (int) entityIn.posY + 3);
                pile.setPosition(entityIn.posX, y + 1, entityIn.posZ);
                worldIn.spawnEntity(pile);
                dashSpawnBlood--;
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
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

        @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 120;
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
        return -3D;
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
