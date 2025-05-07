package com.dungeon_additions.da.items.shield;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.flame_knight.ProjectileFlameSpit;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.SoundCategory;
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

public class ItemFlameShield extends BOMDShieldItem implements IAnimatable {
    public AnimationFactory factory = new AnimationFactory(this);
    private String info_loc;
    private int hitCounter = 0;
    private int usedHitCounter = 0;


    public ItemFlameShield(String name, String info_loc) {
        super(name);
        this.info_loc = info_loc;
        this.setMaxDamage(950);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.getSubCompound("BlockEntityTag") != null)
        {
            EnumDyeColor enumdyecolor = TileEntityBanner.getColor(stack);
            return I18n.translateToLocal("da:item.flame_shield." + enumdyecolor.getTranslationKey() + ".name");
        }
        else
        {
            return I18n.translateToLocal("da.desc.flame_shield_name");
        }
    }

    @Override
    public void onBlockingDamage(ItemStack shield, EntityPlayer player) {
        hitCounter++;
        super.onBlockingDamage(shield, player);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (usedAbility) {
                player.getCooldownTracker().setCooldown(stack.getItem(), ModConfig.incendium_shield_cooldown * 20);
                if (worldIn.isRemote) {
                    worldIn.playSound(player.posX + 0.5D, player.posY, player.posZ + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                }
                usedAbility = false;
            }
        }
        return stack;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if(hitCounter > 5) {
                hitCounter--;
            }
            if (stack == player.getActiveItemStack() && player.isSneaking() && hitCounter > 0) {
                if(player.ticksExisted % 12 == 0) {
                    hitCounter--;
                    usedHitCounter++;

                    float damage = ModConfig.incendium_shield_damage;

                    if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.DARK_METAL_HELMET && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.DARK_METAL_CHESTPLATE &&
                            player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.DARK_METAL_LEGGINGS && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.DARK_METAL_BOOTS) {
                        damage = (float) (ModConfig.incendium_shield_damage * ModConfig.dark_armor_multiplier);
                    }

                        ProjectileFlameSpit spit = new ProjectileFlameSpit(worldIn, player, damage);
                        Vec3d playerLookVec = player.getLookVec();
                        Vec3d playerPos = new Vec3d(player.posX + playerLookVec.x * 1.4D, player.posY + playerLookVec.y + 1.4, player.posZ + playerLookVec.z * 1.4D);
                        spit.setPosition(playerPos.x, playerPos.y, playerPos.z);
                        spit.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 0.7F, 1F);
                        spit.setTravelRange(8);
                        worldIn.spawnEntity(spit);

                    worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 0.6f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.4f));
                    usedAbility = true;

                    if (usedHitCounter > 6) {
                        usedHitCounter = 0;
                        hitCounter = 0;
                        usedAbility = false;
                        if(!worldIn.isRemote) {
                            player.resetActiveHand();
                            player.disableShield(true);
                            player.getCooldownTracker().setCooldown(stack.getItem(), ModConfig.incendium_shield_cooldown * 20);
                        }
                        worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 0.9f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.4f));
                    }
                }
            }
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        ItemBanner.appendHoverTextFromTileEntityTag(stack, tooltip);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
