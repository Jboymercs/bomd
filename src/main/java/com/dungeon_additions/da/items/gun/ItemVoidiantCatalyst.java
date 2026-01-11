package com.dungeon_additions.da.items.gun;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.player.EntityWyrkLazer;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiantLazer;
import com.dungeon_additions.da.items.ItemBase;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ItemVoidiantCatalyst extends ItemBase implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);
    private String info_loc;

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
    }


    public ItemVoidiantCatalyst(String name, String info_loc, CreativeTabs tab) {
        super(name, tab);
        this.info_loc = info_loc;
        this.maxStackSize = 1;
        this.setMaxDamage(300);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn)
    {
        ItemStack itemstack = player.getHeldItem(handIn);
        if(!world.isRemote && !player.getCooldownTracker().hasCooldown(this) ) {
                int SwordCoolDown = ModConfig.voidiant_catalyst_cooldown * 20;
                Vec3d lazerEnd = player.getPositionEyes(1).add(player.getLookVec().scale(20));

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

                int damage_buffer = (int) (ModConfig.voidiant_catalyst_damage + ModUtils.addShieldBonusDamage(player.getHeldItemMainhand(), 1));
                int damage_boost = 0;
                if(player.isBurning()) {
                    damage_boost = 6;
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
                                ((IEntityMultiPart) closestEntity).attackEntityFromPart(closestPart, ModUtils.causeStaffDamage(player).setDamageBypassesArmor(), (float) damage_buffer * 0.5F);
                                closestEntity.hurtResistantTime = 0;
                                ((IEntityMultiPart) closestEntity).attackEntityFromPart(closestPart, ModUtils.causeStaffDamage(player), (float) damage_buffer * 0.5F + 1F + damage_boost);
                            }
                        }
                    } else {
                        lazerEnd = closestEntity.getEntityBoundingBox().calculateIntercept(player.getPositionEyes(1), lazerEnd).hitVec;
                        closestEntity.attackEntityFrom(ModUtils.causeStaffDamage(player).setDamageBypassesArmor(), (float)damage_buffer * 0.5F);
                        closestEntity.hurtResistantTime = 0;
                        closestEntity.attackEntityFrom(ModUtils.causeStaffDamage(player), (float)damage_buffer * 0.5F + 1F + damage_boost);
                    }
                }
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.VOIDIANT_LAZER_GUN, SoundCategory.NEUTRAL, 1.35f, 0.8f / (world.rand.nextFloat() * 0.4F + 0.2f));
                // Spawn an entity to render the ray and additional particles
                EntityVoidiantLazer renderer = new EntityVoidiantLazer(world, player.getPositionEyes(1).add(ModUtils.getAxisOffset(player.getLookVec(), new Vec3d(0.5, 0, 0.5))));
                renderer.setPosition(lazerEnd.x, lazerEnd.y, lazerEnd.z);
                world.spawnEntity(renderer);
                if(player.canBePushed()) {
                    if(player.isAirBorne) {
                        Vec3d moveVec = player.getLookVec().scale(-((4.4 * 0.2) + 0.1D));
                        player.motionX = moveVec.x;
                        player.motionY = moveVec.y;
                        player.motionZ = moveVec.z;
                        player.velocityChanged = true;
                    } else {
                        Vec3d moveVec = player.getLookVec().scale(-((2.0 * 0.2) + 0.1D));
                        player.motionX = moveVec.x;
                        player.motionY = moveVec.y;
                        player.motionZ = moveVec.z;
                        player.velocityChanged = true;
                    }
                }
                if(player.isBurning()) {
                    player.getCooldownTracker().setCooldown(this, (int) (SwordCoolDown * 0.5));
                    itemstack.damageItem(2, player);
                } else {
                    player.getCooldownTracker().setCooldown(this, SwordCoolDown);
                    itemstack.damageItem(1, player);
                }

        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
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
