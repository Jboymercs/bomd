package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.List;

public class ItemAdventureSword extends ToolSword{

    private String info_loc;
    private boolean isGroundSlam;
    private int ticksSaved;

    public ItemAdventureSword(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        this.info_loc = info_loc;
        this.setMaxDamage(736);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        attacker.world.playSound((EntityPlayer) null, attacker.posX, attacker.posY, attacker.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 0.4f, 0.3f / (attacker.world.rand.nextFloat() * 0.4F + 0.2f));
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = 15 * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            Vec3d moveVec = player.getLookVec().scale(((1.1 * 0.6) + 0.1D));
            if(player.canBePushed()) {
                player.motionX = moveVec.x;
                player.motionY += 0.9;
                player.fallDistance = 0;
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
                player.motionZ = moveVec.z;
                player.velocityChanged = true;
                worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.APATHYR_SLIGHT_DASH, SoundCategory.NEUTRAL, 1.0f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.4f));
                this.isGroundSlam = true;
                this.ticksSaved = player.ticksExisted;
            }
            stack.damageItem(5, player);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer && !worldIn.isRemote) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if(this.isGroundSlam && ticksSaved + 10 < player.ticksExisted && player.getHeldItemMainhand().getItem() == this) {
                if (player.onGround) {
                    List<EntityLivingBase> targets = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(2.75, 0.1, 2.75), e -> e != player);
                    if (!targets.isEmpty()) {
                        for (EntityLivingBase entity : targets) {
                            if (entity.canBeCollidedWith()) {
                                Vec3d dir = player.getPositionVector().subtract(entity.getPositionVector());
                                this.onEnemyHit(player, entity, dir);
                            }
                        }
                    }
                    //do some particles
                    this.isGroundSlam = false;
                    ticksSaved = 1;
                    Main.proxy.spawnParticle( 18, worldIn, player.posX, player.posY + 0.1, player.posZ, 0, 0, 0);
                    player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 0.7f, 0.3f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
                } else {
                    player.fallDistance = 0;
                }
            }
        }
    }

    public void onEnemyHit(EntityLivingBase user, EntityLivingBase enemy, Vec3d rammingDir) {
        double attackDamage = (double) (this.getAttackDamage() + 1);
        if(user instanceof EntityPlayer) {
            if(user.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.INCENDIUM_HELMET) {
               attackDamage = (double) (this.getAttackDamage() + 1) * ModConfig.incendium_helmet_multipler;
            }
        }
        enemy.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)user), (float) attackDamage + ModUtils.addAbilityBonusDamage(user.getHeldItemMainhand(), 1.25F));
        enemy.knockBack(user, 1.5F, rammingDir.x, rammingDir.z);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    protected double getAttackSpeed() {
        return -2.7000000953674316D;
    }
}
