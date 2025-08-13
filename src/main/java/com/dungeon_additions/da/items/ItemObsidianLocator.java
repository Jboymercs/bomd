package com.dungeon_additions.da.items;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.projectiles.EntityBurningFlameArenaLocator;
import com.dungeon_additions.da.entity.projectiles.EntityObsidianLocator;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.DALogger;
import com.dungeon_additions.da.util.ModUtils;
import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
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

public class ItemObsidianLocator extends ItemBase{
    private String info_loc;

    public ItemObsidianLocator(String name, String info_loc) {
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
                particlePos = findNearestPosObsdilithArena(playerIn.getPosition(), playerIn);
                if(particlePos == null) {
                    playerIn.sendStatusMessage(new TextComponentTranslation("da.no_structure", new Object[0]), true);
                }
            }

            if(isWithinRadius(particlePos, playerIn.getPosition())) {
                particlePos = null;
                playerIn.sendStatusMessage(new TextComponentTranslation("da.within_bounds", new Object[0]), true);
            }

            if (particlePos != null) {
                EntityObsidianLocator entityendereye = new EntityObsidianLocator(worldIn, playerIn.posX, playerIn.posY + (double) (playerIn.height / 2.0F), playerIn.posZ);
                entityendereye.moveTowards(particlePos);
                worldIn.spawnEntity(entityendereye);
                worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                worldIn.playEvent((EntityPlayer) null, 1003, new BlockPos(playerIn), 0);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            }
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }



    public static BlockPos findNearestPosObsdilithArena( BlockPos pos, EntityPlayer player) {
        BlockPos resultpos = null;
        World world = player.getEntityWorld();
        Chunk chunk = world.getChunk(pos);

        //probably laggy as hell but hey it works
        for (int i = -ModConfig.obsidilith_arena_search_distance; i < ModConfig.obsidilith_arena_search_distance + 1; i++) {
            for (int j = -ModConfig.obsidilith_arena_search_distance; j < ModConfig.obsidilith_arena_search_distance + 1; j++) {
                boolean c = IsObsidilithArenaAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, 100, (chunk.z + j) << 4);
                    break;
                }
            }
        }
        return resultpos;
    }

    protected static boolean IsObsidilithArenaAtPos(World world, int chunkX, int chunkZ) {
        int spacing = WorldConfig.obsidilith_arena_spacing;
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
        Random random = world.setRandomSeed(k, l, 20387994);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l && isAllowedDimensionTooSpawnInObsidilithArena(world.provider.getDimension())) {
            BlockPos pos = new BlockPos((i << 4), WorldConfig.obsidilith_y_height, (j << 4));
            return isAbleToSpawnHereObsidilithArena(pos, world);
        } else {

            return false;
        }
    }

    public static boolean isAbleToSpawnHereObsidilithArena(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypesObsdilith_Arena()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return true;
            }
        }
        return false;
    }

    private static List<BiomeDictionary.Type> obsdilith_arena_types;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypesObsdilith_Arena() {
        if(obsdilith_arena_types == null) {
            obsdilith_arena_types = Lists.newArrayList();

            for(String str : WorldConfig.obsdilith_arena_whitelist) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) obsdilith_arena_types.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return obsdilith_arena_types;
    }

    public static boolean isAllowedDimensionTooSpawnInObsidilithArena(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_obsidilith_arena) {
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
