package com.dungeon_additions.da.items.shield;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.desert_dungeon.miniboss.ProjectileYellowWave;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
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

public class ItemEveratorShield extends BOMDShieldItem implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);
    private String info_loc;

    private int hitCounter = 0;


    public ItemEveratorShield(String name, CreativeTabs tabs, String  info_loc) {
        super(name);
        setCreativeTab(tabs);
        this.setMaxDamage(1032);
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
            return I18n.translateToLocal("da:item.everator_shield." + enumdyecolor.getTranslationKey() + ".name");
        }
        else
        {
            return I18n.translateToLocal("da.desc.everator_shield_name");
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if (stack == player.getActiveItemStack() && player.isSneaking() && hitCounter > 2 && !worldIn.isRemote) {
                float damage = 0;
                if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.DARK_METAL_HELMET && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.DARK_METAL_CHESTPLATE &&
                        player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.DARK_METAL_LEGGINGS && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.DARK_METAL_BOOTS) {
                    damage = (float) ((1) * ModConfig.dark_armor_multiplier) + hitCounter + ModUtils.addShieldBonusDamage(player.getHeldItemOffhand(), 1F);
                } else {
                    damage = (float) (1 + hitCounter + ModUtils.addShieldBonusDamage(player.getHeldItemOffhand(), 1F));
                }
                ProjectileYellowWave wave = new ProjectileYellowWave(worldIn, player, damage, null, true);
                wave.shoot(player, 0, player.rotationYaw, 0F, 0.6F, 0F);
                wave.setPosition(player.posX, player.posY, player.posZ);
                wave.rotationYaw = player.rotationYaw;
                wave.setTravelRange(16F);
                worldIn.spawnEntity(wave);
                this.hitCounter = 0;
                player.resetActiveHand();
                player.disableShield(true);
                player.getCooldownTracker().setCooldown(stack.getItem(), ModConfig.frostborn_shield_cooldown * 20);
            }
        }
    }

    @Override
    public void onBlockingDamage(ItemStack shield, EntityPlayer player) {
        if(hitCounter < 17) {
            hitCounter++;
        }
        player.motionX = 0;
        player.motionY = 0;
        player.motionZ = 0;
        player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 0, false, false));
        if(hitCounter < 8 && !player.world.isRemote) {
            player.world.playSound(player.posX + 0.5D, player.posY, player.posZ + 0.5D, SoundEvents.BLOCK_NOTE_CHIME, SoundCategory.BLOCKS,(float) 0.2 * hitCounter, 1.0F, false);
        }
        super.onBlockingDamage(shield, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
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
