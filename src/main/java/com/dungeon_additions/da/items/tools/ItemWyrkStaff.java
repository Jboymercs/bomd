package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.frost_dungeon.ProjectileFrostBullet;
import com.dungeon_additions.da.entity.player.EntityWyrkLazer;
import com.dungeon_additions.da.items.ItemBase;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemWyrkStaff extends ItemBase {

    private String info_loc;

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    public ItemWyrkStaff(String name, String info_loc, CreativeTabs tab) {
        super(name, tab);
        this.info_loc = info_loc;
        this.maxStackSize = 1;
        this.setMaxDamage(520);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 7200;
    }


    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = ((EntityPlayer) entityLiving);
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            if(!world.isRemote && !player.getCooldownTracker().hasCooldown(this) && i >= 40) {
                int SwordCoolDown = (ModConfig.wyrk_staff_cooldown * 12) * 20;
                Vec3d lazerEnd = player.getPositionEyes(1).add(player.getLookVec().scale(40));

                // Ray trace both blocks and entities
                RayTraceResult raytraceresult = world.rayTraceBlocks(player.getPositionEyes(1), lazerEnd, false, true, false);
                if (raytraceresult != null) {
                    // If we hit a block, make sure that any collisions with entities are detected up to the hit block
                    lazerEnd = raytraceresult.hitVec;
                }

                Entity closestEntity = null;
                for (Entity entity : ModUtils.findEntitiesInLine(player.getPositionEyes(1), lazerEnd, world, player)) {
                    if (entity.canBeCollidedWith() && (closestEntity == null || entity.getDistanceSq(player) < closestEntity.getDistanceSq(player))) {
                        closestEntity = entity;
                    }
                }

                int damage_buffer;

                if(i >= ModConfig.wyrk_staff_lazer_damage * 20) {
                    damage_buffer = (int) ModConfig.wyrk_staff_lazer_damage + 11;
                } else {
                    damage_buffer = (int) (i * 0.16);
                }

                if (closestEntity != null) {
                    if (closestEntity instanceof IEntityMultiPart) {
                        if(closestEntity.getParts() != null) {
                            MultiPartEntityPart closestPart = null;
                            for (Entity entity : closestEntity.getParts()) {
                                RayTraceResult result = entity.getEntityBoundingBox().calculateIntercept(player.getPositionEyes(1), lazerEnd);
                                if (result != null) {
                                    if (entity instanceof MultiPartEntityPart && (closestPart == null || entity.getDistanceSq(player) < closestPart.getDistanceSq(player))) {
                                        closestPart = (MultiPartEntityPart) entity;
                                    }
                                }
                            }
                            if (closestPart != null) {
                                lazerEnd = closestPart.getEntityBoundingBox().calculateIntercept(player.getPositionEyes(1), lazerEnd).hitVec;
                                ((IEntityMultiPart) closestEntity).attackEntityFromPart(closestPart, ModUtils.causeStaffDamage(player).setDamageBypassesArmor(), (float) 4 + damage_buffer);
                            }
                        }
                    } else {
                        lazerEnd = closestEntity.getEntityBoundingBox().calculateIntercept(player.getPositionEyes(1), lazerEnd).hitVec;
                        closestEntity.attackEntityFrom(ModUtils.causeStaffDamage(player).setDamageBypassesArmor(), (float) 4 + damage_buffer);
                    }
                }
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.WYRK_STAFF_LAZER, SoundCategory.NEUTRAL, 1.0f, 0.8f / (world.rand.nextFloat() * 0.4F + 0.2f));
                // Spawn an entity to render the ray and additional particles
                EntityWyrkLazer renderer = new EntityWyrkLazer(world, player.getPositionEyes(1).add(ModUtils.getAxisOffset(player.getLookVec(), new Vec3d(0.5, 0, 0.5))));
                renderer.setPosition(lazerEnd.x, lazerEnd.y, lazerEnd.z);
                world.spawnEntity(renderer);
                stack.damageItem(8, player);
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
            } else {
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0f, 0.8f / (world.rand.nextFloat() * 0.4F + 0.2f));
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn)
    {
        ItemStack itemstack = player.getHeldItem(handIn);

      if(!world.isRemote && !player.getCooldownTracker().hasCooldown(this) ) {
          if(player.isSneaking()) {
              world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.WYRK_STAFF_CHARGE, SoundCategory.NEUTRAL, 1.0f, 0.8f / (world.rand.nextFloat() * 0.4F + 0.2f));
              player.setActiveHand(handIn);
              return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
          } else {
              world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.WYRK_STAFF_SHOOT, SoundCategory.NEUTRAL, 1.0f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));
              Vec3d playerLookVec = player.getLookVec();
              Vec3d playerPos = new Vec3d(player.posX + playerLookVec.x * 1.4D,player.posY + playerLookVec.y + player.getEyeHeight(), player. posZ + playerLookVec.z * 1.4D);
              ProjectileFrostBullet bullet = new ProjectileFrostBullet(world, player, ModConfig.wyrk_staff_bullet_damage);
              bullet.setPosition(playerPos.x, playerPos.y, playerPos.z);
              bullet.shoot(playerLookVec.x, playerLookVec.y, playerLookVec.z, 1.7f, 1.0f);
              world.spawnEntity(bullet);
              bullet.setTravelRange(60F);
              itemstack.damageItem(1, player);

              player.getCooldownTracker().setCooldown(this, (ModConfig.wyrk_staff_cooldown * 20));
              return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
          }
         }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
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

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }
}
