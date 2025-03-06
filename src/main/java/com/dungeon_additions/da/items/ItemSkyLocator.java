package com.dungeon_additions.da.items;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.projectiles.EntityFrozenCastleLocator;
import com.dungeon_additions.da.entity.projectiles.EntitySkyDungeonLocator;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.DALogger;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
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

public class ItemSkyLocator extends ItemBase{

    private String info_loc;

    public ItemSkyLocator(String name, String info_loc) {
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
                particlePos = findNearestPosHighCity(playerIn.getPosition(), playerIn);
            }

            if(isWithinRadius(particlePos, playerIn.getPosition())) {
                particlePos = null;
                playerIn.sendStatusMessage(new TextComponentTranslation("da.within_bounds", new Object[0]), true);
            }

            if (particlePos != null) {
                EntitySkyDungeonLocator entityendereye = new EntitySkyDungeonLocator(worldIn, playerIn.posX, playerIn.posY + (double) (playerIn.height / 2.0F), playerIn.posZ);
                entityendereye.moveTowards(particlePos);
                worldIn.spawnEntity(entityendereye);
                worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDEREYE_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                worldIn.playEvent((EntityPlayer) null, 1003, new BlockPos(playerIn), 0);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            }
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }


    public static boolean isWithinRadius(BlockPos setPos, BlockPos pos) {
        if(setPos != null) {
            if(setPos.getDistance(pos.getX(), pos.getY(), pos.getZ()) < ModConfig.locator_reset_pos) {
                System.out.println("Within Radius of structure, resetting particlePos");
                return true;
            }
        }
        return false;
    }

    public static BlockPos findNearestPosHighCity(BlockPos pos, EntityPlayer player) {
        BlockPos resultpos = null;
        World world = player.getEntityWorld();
        Chunk chunk = world.getChunk(pos);
        //probably laggy as hell but hey it works
        for (int i = -ModConfig.high_court_city_search_distance; i < ModConfig.high_court_city_search_distance + 1; i++) {
            for (int j = -ModConfig.high_court_city_search_distance; j < ModConfig.high_court_city_search_distance + 1; j++) {
                boolean c = IsHighCityAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, WorldConfig.high_city_y_height, (chunk.z + j) << 4);
                    break;
                }
            }
        }
        return resultpos;
    }

    protected static boolean IsHighCityAtPos(World world, int chunkX, int chunkZ) {
        int spacing = WorldConfig.high_city_spacing;
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
        Random random = world.setRandomSeed(k, l, 10387672);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l && isAllowedDimensionTooSpawnInHighCastle(world.provider.getDimension())) {
            BlockPos pos = new BlockPos((i << 4), 0, (j << 4));
            return isAbleToSpawnHereHighCity(pos, world);
        } else {

            return false;
        }
    }

    public static boolean isAbleToSpawnHereHighCity(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypesHighCity()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return false;
            }
        }
        return true;
    }

    private static List<BiomeDictionary.Type> highCityBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypesHighCity() {
        if(highCityBiomeTypes == null) {
            highCityBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.biome_types_blacklist_high_city) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) highCityBiomeTypes.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return highCityBiomeTypes;
    }

    public static boolean isAllowedDimensionTooSpawnInHighCastle(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_high_court_city) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }
}
