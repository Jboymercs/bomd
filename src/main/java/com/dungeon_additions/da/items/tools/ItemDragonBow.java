package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyArrow;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.ItemGoldenArrow;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemDragonBow extends ItemBow implements IHasModel {

    private String info_loc;

    public ItemDragonBow(String name, String info_loc) {
        super();
        this.info_loc = info_loc;
        setTranslationKey(name);
        setRegistryName(name);
        ModItems.ITEMS.add(this);
        this.maxStackSize = 1;
        this.setMaxDamage(780);
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if (entityIn == null)
                {
                    return 0.0F;
                }
                else
                {
                    return !(entityIn.getActiveItemStack().getItem() instanceof ItemBow) ? 0.0F : (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = this.findAmmo(entityplayer);

            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag)
            {
                if (itemstack.isEmpty())
                {
                    itemstack = new ItemStack(ModItems.SKY_ARROW);
                }

                float f = getArrowVelocity(i);

                if ((double)f >= 0.1D)
                {
                    boolean flag1 = entityplayer.capabilities.isCreativeMode || (itemstack.getItem() instanceof ItemGoldenArrow && ((ItemGoldenArrow) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer));

                    if (!worldIn.isRemote)
                    {
                        ItemGoldenArrow itemarrow = (ItemGoldenArrow)(itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : ModItems.SKY_ARROW);
                        EntitySkyArrow skyArrow = null;
                        Vec3d playerLookVec = entityplayer.getLookVec();
                        Vec3d lazerEnd = entityplayer.getPositionEyes(1).add(entityplayer.getLookVec().scale(60));
                        RayTraceResult raytraceresult = entityplayer.world.rayTraceBlocks(entityplayer.getPositionEyes(1), lazerEnd, false, true, false);
                        if (raytraceresult != null) {
                            // If we hit a block, make sure that any collisions with entities are detected up to the hit block
                            lazerEnd = raytraceresult.hitVec;
                        }
                        //Check entity SightLine

                        Entity closestEntity = null;
                        for (Entity entity : ModUtils.findEntitiesInLine(entityplayer.getPositionEyes(1), lazerEnd, worldIn, entityplayer)) {
                            if (entity.canBeCollidedWith() && (closestEntity == null || entity.getDistanceSq(entityplayer) < closestEntity.getDistanceSq(entityplayer))) {
                                closestEntity = entity;
                            }
                        }


                        if (closestEntity != null) {
                            if (closestEntity instanceof IEntityMultiPart) {
                                if(closestEntity.getParts() != null) {
                                    MultiPartEntityPart closestPart = null;
                                    for (Entity entity : closestEntity.getParts()) {
                                        RayTraceResult result = entity.getEntityBoundingBox().calculateIntercept(entityplayer.getPositionEyes(1), lazerEnd);
                                        if (result != null) {
                                            if (entity instanceof MultiPartEntityPart && (closestPart == null || entity.getDistanceSq(entityplayer) < closestPart.getDistanceSq(entityplayer))) {
                                                closestPart = (MultiPartEntityPart) entity;
                                            }
                                        }
                                    }
                                    if (closestPart != null) {
                                        skyArrow = new EntitySkyArrow(worldIn, entityplayer, ((EntityLivingBase) closestEntity));
                                        Vec3d playerPos = new Vec3d(entityplayer.posX + playerLookVec.x * 1.4D,entityplayer.posY + playerLookVec.y + entityplayer.getEyeHeight(), entityplayer. posZ + playerLookVec.z * 1.4D);
                                        skyArrow.setPosition(playerPos.x, playerPos.y, playerPos.z);
                                        skyArrow.setDamage(ModConfig.dragon_bow_damage);
                                    }
                                }
                            } else if (closestEntity instanceof EntityLivingBase){
                                skyArrow = new EntitySkyArrow(worldIn, entityplayer, ((EntityLivingBase) closestEntity));
                                Vec3d playerPos = new Vec3d(entityplayer.posX + playerLookVec.x * 1.4D,entityplayer.posY + playerLookVec.y + entityplayer.getEyeHeight(), entityplayer. posZ + playerLookVec.z * 1.4D);
                                skyArrow.setPosition(playerPos.x, playerPos.y, playerPos.z);
                                skyArrow.setDamage(ModConfig.dragon_bow_damage);
                            }
                        } else {
                            skyArrow = new EntitySkyArrow(worldIn, entityplayer);
                            Vec3d playerPos = new Vec3d(entityplayer.posX + playerLookVec.x * 1.4D,entityplayer.posY + playerLookVec.y + entityplayer.getEyeHeight(), entityplayer. posZ + playerLookVec.z * 1.4D);
                            skyArrow.setPosition(playerPos.x, playerPos.y, playerPos.z);
                            skyArrow.setDamage(ModConfig.dragon_bow_damage);
                        }

                      //  entityarrow.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                        if(skyArrow != null) {
                            skyArrow.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                            if (f == 1.0F)
                            {
                                skyArrow.setIsCritical(true);
                            }

                            int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                            if (j > 0)
                            {
                                skyArrow.setDamage(skyArrow.getDamage() + (double)j * 0.5D + 4.5D);
                            }

                            int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                            if (k > 0)
                            {
                                skyArrow.setKnockbackStrength(k);
                            }

                            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
                            {
                                skyArrow.setFire(100);
                            }

                            stack.damageItem(1, entityplayer);

                            entityplayer.getCooldownTracker().setCooldown(this, ModConfig.dragon_bow_cooldown * 20);
                            worldIn.spawnEntity(skyArrow);
                        } else {
                            entityplayer.sendStatusMessage(new TextComponentTranslation("da.sky_arrow_fail", new Object[0]), true);
                        }


                    }

                    worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundsHandler.IMPERIAL_SHOOT_ARROW, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!flag1 && !entityplayer.capabilities.isCreativeMode)
                    {
                        itemstack.shrink(1);

                        if (itemstack.isEmpty())
                        {
                            entityplayer.inventory.deleteStack(itemstack);
                        }
                    }

                    entityplayer.addStat(Objects.requireNonNull(StatList.getObjectUseStats(this)));
                }
            }
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        boolean flag = !this.findAmmo(playerIn).isEmpty();
        if(!playerIn.getCooldownTracker().hasCooldown(this) ) {
            ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
            if (ret != null) return ret;

            if (!playerIn.capabilities.isCreativeMode && !flag) {
                return flag ? new ActionResult(EnumActionResult.PASS, itemstack) : new ActionResult(EnumActionResult.FAIL, itemstack);
            } else {
                playerIn.setActiveHand(handIn);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            }
        } else {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }

    public static float getArrowVelocity(int charge)
    {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 5.0F;

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        return f;
    }

    public int getItemEnchantability()
    {
        return 30;
    }

    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }


    @Override
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

    @Override
    protected boolean isArrow(ItemStack stack)
    {
        return stack.getItem() instanceof ItemGoldenArrow;
    }


    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
