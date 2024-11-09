package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.entity.mini_blossom.EntityDart;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
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

import java.util.List;

public class ItemAmbitionSword extends ToolSword{
    private String info_loc;

    public ItemAmbitionSword(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        this.info_loc = info_loc;
        this.setMaxDamage(1200);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GOLD + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = ModConfig.sword_of_ambition_cooldown * 20;

        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.B_KNIGHT_STOMP, SoundCategory.NEUTRAL, 1.0f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
            ModUtils.circleCallback(1, 8, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(player.getPositionVector());
                int y = getSurfaceHeight(worldIn, new BlockPos(pos.x, 0, pos.z), (int) player.posY - 5, (int) player.posY + 2);
                EntityMoveTile spike = new EntityMoveTile(worldIn, player);
                spike.setPosition(pos.x, y + 1, pos.z);
                BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                spike.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                spike.setLocationAndAngles(posToo.getX() + 0.5D, y, posToo.getZ() + 0.5D, 0.0f, 0.0F);
                spike.setBlock(Blocks.MAGMA, 0);
                worldIn.spawnEntity(spike);
            });
            ModUtils.circleCallback(2, 16, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(player.getPositionVector());
                int y = getSurfaceHeight(worldIn, new BlockPos(pos.x, 0, pos.z), (int) player.posY - 5, (int) player.posY + 2);
                EntityMoveTile spike = new EntityMoveTile(worldIn, player);
                spike.setPosition(pos.x, y + 1, pos.z);
                BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                spike.setOrigin(posToo, 10, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                spike.setLocationAndAngles(posToo.getX() + 0.5D, y, posToo.getZ() + 0.5D, 0.0f, 0.0F);
                spike.setBlock(Blocks.MAGMA, 0);
                worldIn.spawnEntity(spike);
            });
            ModUtils.circleCallback(3, 24, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(player.getPositionVector());
                int y = getSurfaceHeight(worldIn, new BlockPos(pos.x, 0, pos.z), (int) player.posY - 5, (int) player.posY + 2);
                EntityMoveTile spike = new EntityMoveTile(worldIn, player);
                spike.setPosition(pos.x, y + 1, pos.z);
                BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                spike.setOrigin(posToo, 15, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                spike.setLocationAndAngles(posToo.getX() + 0.5D, y, posToo.getZ() + 0.5D, 0.0f, 0.0F);
                spike.setBlock(Blocks.MAGMA, 0);
                worldIn.spawnEntity(spike);
            });
            ModUtils.circleCallback(4, 32, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(player.getPositionVector());
                int y = getSurfaceHeight(worldIn, new BlockPos(pos.x, 0, pos.z), (int) player.posY - 5, (int) player.posY + 2);
                EntityMoveTile spike = new EntityMoveTile(worldIn, player);
                spike.setPosition(pos.x, y + 1, pos.z);
                BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                spike.setOrigin(posToo, 20, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                spike.setLocationAndAngles(posToo.getX() + 0.5D, y, posToo.getZ() + 0.5D, 0.0f, 0.0F);
                spike.setBlock(Blocks.MAGMA, 0);
                worldIn.spawnEntity(spike);
            });

            if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.FLAME_HELMET && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.FLAME_CHESTPLATE &&
                    player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.FLAME_LEGGINGS && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.FLAME_BOOTS) {
                ModUtils.circleCallback(5, 40, (pos)-> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(player.getPositionVector());
                    int y = getSurfaceHeight(worldIn, new BlockPos(pos.x, 0, pos.z), (int) player.posY - 5, (int) player.posY + 2);
                    EntityMoveTile spike = new EntityMoveTile(worldIn, player);
                    spike.setPosition(pos.x, y + 1, pos.z);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    spike.setOrigin(posToo, 25, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    spike.setLocationAndAngles(posToo.getX() + 0.5D, y, posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    spike.setBlock(Blocks.MAGMA, 0);
                    worldIn.spawnEntity(spike);
                });
                ModUtils.circleCallback(6, 48, (pos)-> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(player.getPositionVector());
                    int y = getSurfaceHeight(worldIn, new BlockPos(pos.x, 0, pos.z), (int) player.posY - 5, (int) player.posY + 2);
                    EntityMoveTile spike = new EntityMoveTile(worldIn, player);
                    spike.setPosition(pos.x, y + 1, pos.z);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    spike.setOrigin(posToo, 30, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    spike.setLocationAndAngles(posToo.getX() + 0.5D, y, posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    spike.setBlock(Blocks.MAGMA, 0);
                    worldIn.spawnEntity(spike);
                });
                ModUtils.circleCallback(7, 56, (pos)-> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(player.getPositionVector());
                    int y = getSurfaceHeight(worldIn, new BlockPos(pos.x, 0, pos.z), (int) player.posY - 5, (int) player.posY + 2);
                    EntityMoveTile spike = new EntityMoveTile(worldIn, player);
                    spike.setPosition(pos.x, y + 1, pos.z);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    spike.setOrigin(posToo, 35, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    spike.setLocationAndAngles(posToo.getX() + 0.5D, y, posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    spike.setBlock(Blocks.MAGMA, 0);
                    worldIn.spawnEntity(spike);
                });
            }
            stack.damageItem(2, player);
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
        }


        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
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
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    protected double getAttackSpeed() {
        return -2.7000000953674316D;
    }
}
