package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.dark_dungeon.ProjectileDarkMatter;
import com.dungeon_additions.da.entity.frost_dungeon.ProjectileFrostBullet;
import com.dungeon_additions.da.items.util.ISweepAttackOverride;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDarkSicle extends ToolSword implements IAnimatable, ISweepAttackOverride {

    public AnimationFactory factory = new AnimationFactory(this);
    private String info_loc;

    public ItemDarkSicle(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.info_loc = info_loc;
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        this.setMaxDamage(766);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn)
    {
        ItemStack itemstack = player.getHeldItem(handIn);

        if(!world.isRemote && !player.getCooldownTracker().hasCooldown(this) ) {
            world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.SHADOW_HAND_SUMMON, SoundCategory.NEUTRAL, 1.0f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));
            Vec3d playerLookVec = player.getLookVec();
            Vec3d playerPos = new Vec3d(player.posX + playerLookVec.x * 1.4D,player.posY + playerLookVec.y + player.getEyeHeight(), player. posZ + playerLookVec.z * 1.4D);
            ProjectileDarkMatter bullet = new ProjectileDarkMatter(world, player, 10);
            bullet.setPosition(playerPos.x, playerPos.y, playerPos.z);
            bullet.shoot(playerLookVec.x, playerLookVec.y, playerLookVec.z, 1.3f, 1.0f);
            world.spawnEntity(bullet);
            bullet.setTravelRange(16F);
            itemstack.damageItem(1, player);
            player.getCooldownTracker().setCooldown(this, (ModConfig.dark_sicle_cooldown * 20));
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }


    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }

    protected double getAttackSpeed() {
        return -3.0D;
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker)
    {
        return true;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void doSweepAttack(EntityPlayer player, @Nullable EntityLivingBase entity) {
        ModUtils.doSweepAttack(player, entity, (e) -> {
        }, 9, 3);
    }
}
