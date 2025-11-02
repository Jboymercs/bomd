package com.dungeon_additions.da.event;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.frost_dungeon.EntityFrostBase;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.ProjectileSoul;
import com.dungeon_additions.da.entity.frost_dungeon.wyrk.EntityFriendWyrk;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityFriendlyCursedGolem;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class EventDropSoulStar {



    /**
     * Tells Minecraft on entity death of a zombie, skeleton, stray
     */
    @SubscribeEvent
    public static void onEntityDrop(LivingDropsEvent event)
    {
        EntityLivingBase target = event.getEntityLiving();

        boolean hasFoundWyrk = false;
        //keep consitency with the undead only able to spawn souls to fuel Wyrks

        //Death's Prosper Trinket
        if(event.getSource().getImmediateSource() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) event.getSource().getImmediateSource());
            ItemStack DeathStack = ModUtils.findTrinket(ModItems.DEATH_TRINKET.getDefaultInstance(), player);
            ItemStack gambleStack = ModUtils.findTrinket(ModItems.GAMBLE_TRINKET.getDefaultInstance(), player);
            if(!DeathStack.isEmpty()) {
                int randI = ModRand.range(1, 101);
                if(randI < 8) {
                    player.heal(4);
                    DeathStack.damageItem(1, player);
                }
            }

            if(!gambleStack.isEmpty()) {
                int randI = ModRand.range(1, 101);
                if(randI < 5) {
                    //prevents bosses from this effect being applied too.
                    if(target.isNonBoss()) {
                        event.getDrops().clear();
                        event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(ModItems.COPPER_COIN, ModRand.range(1, 2))));
                        gambleStack.damageItem(1, player);
                    }
                }
            }
        }

        if(target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {

            List<EntityWyrk> nearbyWyrkEnemy = target.world.getEntitiesWithinAABB(EntityWyrk.class, target.getEntityBoundingBox().grow(20.0D), e -> !e.getIsInvulnerable());
            if(!nearbyWyrkEnemy.isEmpty()) {
                for(EntityWyrk wyrk : nearbyWyrkEnemy) {
                    if (!hasFoundWyrk) {
                        ProjectileSoul soul = new ProjectileSoul(target.world, target, 0, wyrk);
                        soul.setPosition(target.posX, target.posY + 1.5D, target.posZ);
                        soul.setTravelRange(40F);
                        target.world.spawnEntity(soul);
                        hasFoundWyrk = true;
                    }
                }
            }

            List<EntityFriendWyrk> nearbyWyrk = target.world.getEntitiesWithinAABB(EntityFriendWyrk.class, target.getEntityBoundingBox().grow(20.0D), e -> !e.getIsInvulnerable());
            if (!nearbyWyrk.isEmpty() && !target.world.isRemote) {
                for (EntityFriendWyrk wyrk : nearbyWyrk) {
                    if (!hasFoundWyrk) {
                        ProjectileSoul soul = new ProjectileSoul(target.world, target, 0, wyrk);
                        soul.setPosition(target.posX, target.posY + 1.5D, target.posZ);
                        soul.setTravelRange(40F);
                        target.world.spawnEntity(soul);
                        hasFoundWyrk = true;
                    }
                }
            }
        }

        if(event.getSource().getImmediateSource() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) event.getSource().getImmediateSource());

            if(ModUtils.getAdvancementCompletionAsList(player, ModConfig.soul_star_progress)) {
                if (target != null)
                {
                    if(ModConfig.soul_star_drops_everywhere) {

                        if (target instanceof EntityMob) {
                            if (target.world.rand.nextFloat() <= ((float) ModConfig.soul_star_drop_chance * 0.01) + (0.01 * event.getLootingLevel())) {
                                event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(ModItems.SOUL_STAR)));
                            }
                        }

                    } else {
                        //registers all undead under the category this item can drop under instead of creatures that extend the Mob Class
                        if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                            if (target.world.rand.nextFloat() <= ((float) ModConfig.soul_star_drop_chance * 0.01) + (0.01 * event.getLootingLevel())) {
                                event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(ModItems.SOUL_STAR)));
                            }
                        }
                    }
                }
            }
        } else {
            if (target != null)
            {
                if(ModConfig.soul_star_drops_everywhere) {

                    if (target instanceof EntityMob) {
                        if (target.world.rand.nextFloat() <= ((float) ModConfig.soul_star_drop_chance * 0.01) + (0.01 * event.getLootingLevel())) {
                            event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(ModItems.SOUL_STAR)));
                        }
                    }

                } else {
                    if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                        if (target.world.rand.nextFloat() <= ((float) ModConfig.soul_star_drop_chance * 0.01) + (0.01 * event.getLootingLevel())) {
                            event.getDrops().add(new EntityItem(target.world, target.posX, target.posY, target.posZ, new ItemStack(ModItems.SOUL_STAR)));
                        }
                    }
                }
            }
        }


    }


    @SubscribeEvent
    public static void createNovikGolem(BlockEvent.EntityPlaceEvent event) {

        if(event.getEntity() != null) {
            if(event.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = ((EntityPlayer) event.getEntity());
                if(player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
                    World world = event.getWorld();
                    BlockPos pos = event.getPos();
                     BlockPattern golemPattern = null;
                    BlockPattern.PatternHelper patternHelper = getGolemPattern(golemPattern).match(world, pos);
                    if(patternHelper != null) {
                        for (int j = 0; j < getGolemPattern(golemPattern).getPalmLength(); ++j)
                        {
                            for (int k = 0; k < getGolemPattern(golemPattern).getThumbLength(); ++k)
                            {
                                world.setBlockState(patternHelper.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                            }
                        }
                        BlockPos blockpos = patternHelper.translateOffset(1, 2, 0).getPos();
                        EntityFriendlyCursedGolem golem = new EntityFriendlyCursedGolem(world);
                        golem.setLocationAndAngles((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.05D, (double)blockpos.getZ() + 0.5D, 0.0F, 0.0F);
                        golem.setSetSpawnLoc(true);
                        golem.setSpawnLocation(new BlockPos(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ()));
                        world.spawnEntity(golem);
                    }
                }
            }
        }
    }

    private static final Predicate<IBlockState> IS_PUMPKIN = new Predicate<IBlockState>()
    {
        public boolean apply(@Nullable IBlockState p_apply_1_)
        {
            return p_apply_1_ != null && (p_apply_1_.getBlock() == Blocks.PUMPKIN || p_apply_1_.getBlock() == Blocks.LIT_PUMPKIN);
        }
    };

    protected static BlockPattern getGolemPattern(BlockPattern golemPattern)
    {
        if (golemPattern == null)
        {
            golemPattern = FactoryBlockPattern.start().aisle(" ", "#", "#").where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(ModBlocks.NOVIK_BLOCK))).build();
        }

        return golemPattern;
    }


    //Gaelon Ingot Anvil List

    @SubscribeEvent
    public static void addGaelonRepairRecipes(AnvilUpdateEvent event) {

        ItemStack leftInput = event.getLeft();
        ItemStack rightInput = event.getRight();
        ItemStack output = event.getOutput();

        if(ModUtils.canItemBeRepaired(leftInput.getItem()) && rightInput.getItem() == ModItems.GAELON_INGOT) {
            NBTTagCompound tags = leftInput.getTagCompound();
            output = new ItemStack(leftInput.getItem());
            int itemDamage = (int) ModUtils.getPercentageOf(leftInput.getMaxDamage(), leftInput.getItemDamage());
            int calculatedDamage = (int) ModUtils.calculateValueWithPrecentage(output.getMaxDamage(), (itemDamage - 30));
            output.setItemDamage((calculatedDamage));
            output.setTagCompound(tags);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(5);
        }
    }

}
