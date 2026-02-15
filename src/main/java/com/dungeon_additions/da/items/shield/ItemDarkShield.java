package com.dungeon_additions.da.items.shield;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.proxy.ClientProxy;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
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

public class ItemDarkShield extends BOMDShieldItem implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);
    private String info_loc;

    public int hitCounter = 0;

    public ItemDarkShield(String name, CreativeTabs tabs, String info_loc) {
        super(name);
        setCreativeTab(tabs);
        this.setMaxDamage(832);
        this.info_loc = info_loc;
        this.maxStackSize = 1;
        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.getSubCompound("BlockEntityTag") != null)
        {
            EnumDyeColor enumdyecolor = TileEntityBanner.getColor(stack);
            return I18n.translateToLocal("da:item.dark_shield." + enumdyecolor.getTranslationKey() + ".name");
        }
        else
        {
            return I18n.translateToLocal("da.desc.dark_shield_name");
        }
    }

    @Override
    public void onBlockingDamage(ItemStack shield, EntityPlayer player) {
        hitCounter++;
        super.onBlockingDamage(shield, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
        tooltip.add(TextFormatting.GOLD + I18n.translateToLocal("description.dungeon_additions.shield_pre.name") + TextFormatting.AQUA + I18n.translateToLocal(ClientProxy.SHIELD_ABILITY.getDisplayName()));
        ItemBanner.appendHoverTextFromTileEntityTag(stack, tooltip);
    }

    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote) {
                return currentY;
            }

            currentY--;
        }

        return pos.getY();
    }


    @Override
    public boolean onApplyButtonPressed(EntityPlayer player, World world, ItemStack stack){
        if(this.hitCounter > 4 && stack == player.getActiveItemStack()) {
            if(player.canBePushed()) {
                player.motionX = 0;
                player.motionY = 0;
                player.motionZ = 0;
            }

            world.playSound(player, new BlockPos(player.posX, player.posY, player.posZ), SoundsHandler.B_KNIGHT_STOMP, SoundCategory.BLOCKS,(float) 1.0F, 1.0F);

            //Do Quick AOE

            if(!world.isRemote) {
                ModUtils.circleCallback(1, 8, (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(player.getPositionVector());
                    int y = getSurfaceHeight(world, new BlockPos(pos.x, 0, pos.z), (int) player.posY - 5, (int) player.posY + 2);
                    float damage = ModConfig.dark_shield_damage;
                    if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.DARK_METAL_HELMET && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.DARK_METAL_CHESTPLATE &&
                            player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.DARK_METAL_LEGGINGS && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.DARK_METAL_BOOTS) {
                        damage = (float) (ModConfig.dark_shield_damage * ModConfig.dark_armor_multiplier);
                    }
                    EntityMoveTile spike = new EntityMoveTile(world, player, damage);
                    spike.setPosition(pos.x, y + 1, pos.z);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    spike.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    spike.setLocationAndAngles(posToo.getX() + 0.5D, y, posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    spike.setBlock(Blocks.STONE, 0);
                    world.spawnEntity(spike);
                });
                ModUtils.circleCallback(2, 16, (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(player.getPositionVector());
                    int y = getSurfaceHeight(world, new BlockPos(pos.x, 0, pos.z), (int) player.posY - 5, (int) player.posY + 2);
                    float damage = ModConfig.dark_shield_damage;
                    if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.DARK_METAL_HELMET && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.DARK_METAL_CHESTPLATE &&
                            player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.DARK_METAL_LEGGINGS && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.DARK_METAL_BOOTS) {
                        damage = (float) (ModConfig.dark_shield_damage * ModConfig.dark_armor_multiplier);
                    }
                    EntityMoveTile spike = new EntityMoveTile(world, player, damage);
                    spike.setPosition(pos.x, y + 1, pos.z);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    spike.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    spike.setLocationAndAngles(posToo.getX() + 0.5D, y, posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    spike.setBlock(Blocks.STONE, 0);
                    world.spawnEntity(spike);
                });
                ModUtils.circleCallback(3, 24, (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(player.getPositionVector());
                    int y = getSurfaceHeight(world, new BlockPos(pos.x, 0, pos.z), (int) player.posY - 5, (int) player.posY + 2);
                    float damage = ModConfig.dark_shield_damage + ModUtils.addShieldBonusDamage(player.getHeldItemOffhand(), 1);
                    if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.DARK_METAL_HELMET && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.DARK_METAL_CHESTPLATE &&
                            player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.DARK_METAL_LEGGINGS && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.DARK_METAL_BOOTS) {
                        damage = (float) (ModConfig.dark_shield_damage * ModConfig.dark_armor_multiplier) + ModUtils.addShieldBonusDamage(player.getHeldItemOffhand(), 1);
                    }
                    EntityMoveTile spike = new EntityMoveTile(world, player, damage);
                    spike.setPosition(pos.x, y + 1, pos.z);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    spike.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    spike.setLocationAndAngles(posToo.getX() + 0.5D, y, posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    spike.setBlock(Blocks.STONE, 0);
                    world.spawnEntity(spike);
                });

                player.resetActiveHand();
                player.disableShield(true);
                player.getCooldownTracker().setCooldown(stack.getItem(), ModConfig.dark_shield_cooldown * 20);
                this.hitCounter = 0;
                return true;
            }

        }

        return false;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
