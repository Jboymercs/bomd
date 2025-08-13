package com.dungeon_additions.da.items;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.projectiles.EntityForgottenTempleLocator;
import com.dungeon_additions.da.entity.projectiles.EntityFrozenCastleLocator;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.DALogger;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Random;

public class ItemForgottenTempleLocator extends ItemBase{
    private String info_loc;

    public ItemForgottenTempleLocator(String name, String info_loc) {
        super(name);
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    public BlockPos particlePos = null;

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {

        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, false);

        if(!worldIn.isRemote) {

            if(particlePos == null) {
                particlePos = findNearestPosFrozenCastle(playerIn.getPosition(), playerIn);
                if(particlePos == null) {
                    playerIn.sendStatusMessage(new TextComponentTranslation("da.no_structure", new Object[0]), true);
                }
            }

            if(isWithinRadius(particlePos, playerIn.getPosition())) {
                particlePos = null;
                playerIn.sendStatusMessage(new TextComponentTranslation("da.within_bounds", new Object[0]), true);

            }

            if (particlePos != null) {
                EntityForgottenTempleLocator entityendereye = new EntityForgottenTempleLocator(worldIn, playerIn.posX, playerIn.posY + (double) (playerIn.height / 2.0F), playerIn.posZ);
                entityendereye.moveTowards(particlePos);
                worldIn.spawnEntity(entityendereye);
                worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundsHandler.MIRROR_DISPENSE, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                worldIn.playEvent((EntityPlayer) null, 1003, new BlockPos(playerIn), 0);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            }
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }



    public static BlockPos findNearestPosFrozenCastle(BlockPos pos, EntityPlayer player) {
        BlockPos resultpos = null;
        World world = player.getEntityWorld();
        Chunk chunk = world.getChunk(pos);
        //probably laggy as hell but hey it works
        for (int i = -ModConfig.forgotten_temple_distance; i < ModConfig.forgotten_temple_distance + 1; i++) {
            for (int j = -ModConfig.forgotten_temple_distance; j < ModConfig.forgotten_temple_distance + 1; j++) {
                boolean c = IsFrozenCastleAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, WorldConfig.temple_y_height, (chunk.z + j) << 4);
                    break;
                }
                // }
            }
        }
        return resultpos;
    }

    protected static boolean IsFrozenCastleAtPos(World world, int chunkX, int chunkZ) {
        int spacing = WorldConfig.temple_spacing;
        int separation = 16;
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0) {
            chunkX -= spacing - 1;
        }

        if (chunkZ < 0) {
            chunkZ -= spacing - 1;
        }

        int k = chunkX / spacing;
        int l = chunkZ / spacing;
        Random random = world.setRandomSeed(k, l, 10002973);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l && isAllowedDimensionTooSpawnInFrozenCastle(world.provider.getDimension()) && !world.isRemote && !world.isChunkGeneratedAt(i, j)) {
            BlockPos pos = new BlockPos((i << 4), 0, (j << 4));
            return isAbleToSpawnHereFrozenCastle(pos, world);
        } else {

            return false;
        }
    }

    public static boolean isAbleToSpawnHereFrozenCastle(BlockPos pos, World world) {
        int staggerCounter = 0;
        for(BiomeDictionary.Type types : getSpawnBiomeTypesFrozenCastle()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(staggerCounter > 1) {
                return true;
            }
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                staggerCounter++;
            }
        }
        return false;
    }

    private static List<BiomeDictionary.Type> frozenCastleBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypesFrozenCastle() {
        if(frozenCastleBiomeTypes == null) {
            frozenCastleBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.forgotten_temple_whitelist) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) frozenCastleBiomeTypes.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return frozenCastleBiomeTypes;
    }

    public static boolean isAllowedDimensionTooSpawnInFrozenCastle(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_forgotten_temple) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }




    public static boolean isWithinRadius(BlockPos setPos, BlockPos pos) {
        if(setPos != null) {
            if(setPos.getDistance(pos.getX(), pos.getY(), pos.getZ()) < ModConfig.locator_reset_pos) {
                return true;
            }
        }
        return false;
    }

}
