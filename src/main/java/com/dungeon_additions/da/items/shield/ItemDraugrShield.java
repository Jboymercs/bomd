package com.dungeon_additions.da.items.shield;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.packets.MessageModParticles;
import com.dungeon_additions.da.proxy.ClientProxy;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.*;
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

public class ItemDraugrShield extends BOMDShieldItem implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);
    private String info_loc;

    private int hitCounter = 0;

    private int dashTime = 0;

    private boolean isDashing = false;

    public ItemDraugrShield(String name, CreativeTabs tabs, String info_loc) {
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
            return I18n.translateToLocal("da:item.draugr_shield." + enumdyecolor.getTranslationKey() + ".name");
        }
        else
        {
            return I18n.translateToLocal("da.desc.draugr_shield_name");
        }
    }

    @Override
    public void onBlockingDamage(ItemStack shield, EntityPlayer player) {
        hitCounter++;
        if(hitCounter < 6 && !player.world.isRemote) {
            player.world.playSound(player.posX + 0.5D, player.posY, player.posZ + 0.5D, SoundEvents.BLOCK_NOTE_CHIME, SoundCategory.BLOCKS,(float) 0.2 * hitCounter, 1.0F, false);
        }
        super.onBlockingDamage(shield, player);
    }


    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;

            if(this.isDashing && !worldIn.isRemote) {
                Main.proxy.spawnParticle(2, worldIn, player.posX, player.posY + 1, player.posZ, 0,0,0);
                List<EntityLivingBase> targets = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(1.5), e -> e != player);

                for(EntityLivingBase target : targets) {
                   // Vec3d dir = target.getPositionVector().subtract(player.getPositionVector()).normalize();
                    Vec3d moveDir = player.getPositionVector().add(player.getLookVec().add(0, 1.5, 0)).scale(1.5D);

                    if(target.canBeCollidedWith()) {
                        this.onEnemyRammed(player, target, moveDir);
                    }
                }

                dashTime++;

                if(player.motionX < 0.08 && player.motionZ < 0.08 && dashTime > 5 || dashTime > 35) {
                    this.isDashing = false;
                    player.motionX = 0;
                    player.motionZ = 0;
                    player.motionY = 0;
                    dashTime = 0;
                    if(!worldIn.isRemote) {
                        player.resetActiveHand();
                        player.disableShield(true);
                        player.getCooldownTracker().setCooldown(stack.getItem(), ModConfig.frostborn_shield_cooldown * 20);
                    }
                }

            }
        }

        }


    @Override
    public boolean onApplyButtonPressed(EntityPlayer player, World world, ItemStack stack){
        if (stack == player.getActiveItemStack() && hitCounter > 4) {
            Vec3d moveVec = player.getLookVec().scale(2.6F);
            if(player.canBePushed()) {
                player.motionX = moveVec.x;
                player.motionY = 0.1;
                player.motionZ = moveVec.z;
                player.velocityChanged = true;
                hitCounter = 0;
                this.isDashing = true;
                world.playSound(player, new BlockPos(player.posX, player.posY, player.posZ), SoundsHandler.DRAUGR_ELITE_STOMP, SoundCategory.BLOCKS,(float) 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }


    public void onEnemyRammed(EntityLivingBase user, EntityLivingBase enemy, Vec3d rammingDir) {
        boolean attacked = false;
        float damage = (ModConfig.frostborn_shield_damage + ModUtils.addShieldBonusDamage(user.getHeldItemOffhand(), 1.5F));

        if(user instanceof EntityPlayer) {
            damage = (ModConfig.frostborn_shield_damage + ModUtils.addShieldBonusDamage(user.getHeldItemOffhand(), 1.5F)) * ModUtils.addDarkArmorBonusMultiplier((EntityPlayer) user, 1);
        }

        if(user instanceof EntityPlayer) {
            attacked = enemy.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)user), damage);

        } else {
            attacked = enemy.attackEntityFrom(DamageSource.causeMobDamage(user), damage);
        }

        if(attacked) {
            enemy.knockBack(user, 1.4F, -rammingDir.x, -rammingDir.z);
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        if(ModConfig.enable_scaling_tooltips) {
            tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
        }
        tooltip.add(TextFormatting.GOLD + I18n.translateToLocal("description.dungeon_additions.shield_pre.name") + TextFormatting.AQUA + I18n.translateToLocal(ClientProxy.SHIELD_ABILITY.getDisplayName()));
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
