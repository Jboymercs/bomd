package com.dungeon_additions.da.util.commands;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.util.DALogger;
import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

public class CommandLocateLich implements ICommand {

    private final List<String> aliases;
    public CommandLocateLich() {
        aliases = new ArrayList<>();
        aliases.add("locateBOMD");
    }

    @Override
    public String getName() {
        return "locateBOMD";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "locateBOMD <mod location>";
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            throw new WrongUsageException("locateBOMD <mod location>");
        } else {
            String s = args[0];
            if(s.equals("NightLichTower")) {
                BlockPos blockpos = findNearestPos(sender);

                if (blockpos != null) {
                    sender.sendMessage(new TextComponentTranslation("commands.locate.success", new Object[]{s, blockpos.getX(), blockpos.getZ()}));
                } else {
                    throw new CommandException("commands.locate.failure", s);
                }
            } else if (s.equals("BlossomCave")) {
                BlockPos blockpos = findNearestPosCave(sender);

                if (blockpos != null) {
                    sender.sendMessage(new TextComponentTranslation("commands.locate.success", new Object[]{s, blockpos.getX(), blockpos.getZ()}));
                } else {
                    throw new CommandException("commands.locate.failure", s);
                }
            } else if (s.equals("FrozenCastle")) {
                BlockPos blockpos = findNearestPosFrozenCastle(sender);

                if (blockpos != null) {
                    sender.sendMessage(new TextComponentTranslation("commands.locate.success", new Object[]{s, blockpos.getX(), blockpos.getZ()}));
                } else {
                    throw new CommandException("commands.locate.failure", s);
                }
            } else if (s.equals("HighCourtCity")) {
                BlockPos blockpos = findNearestPosHighCity(sender);

                if (blockpos != null) {
                    sender.sendMessage(new TextComponentTranslation("commands.locate.success", new Object[]{s, blockpos.getX(), blockpos.getZ()}));
                } else {
                    throw new CommandException("commands.locate.failure", s);
                }
            } else if (s.equals("BurningFlameArena")) {
                BlockPos blockpos = findNearestPosBurningFlame(sender);

                if (blockpos != null) {
                    sender.sendMessage(new TextComponentTranslation("commands.locate.success", new Object[]{s, blockpos.getX(), blockpos.getZ()}));
                } else {
                    throw new CommandException("commands.locate.failure", s);
                }
            }
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "NightLichTower", "BlossomCave", "FrozenCastle", "HighCourtCity","BurningFlameArena") : Collections.emptyList();
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] args, String... possibilities) {
        return getListOfStringsMatchingLastWord(args, Arrays.asList(possibilities));
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] inputArgs, Collection<?> possibleCompletions) {
        String s = inputArgs[inputArgs.length - 1];
        List<String> list = Lists.newArrayList();

        if (!possibleCompletions.isEmpty()) {
            for (String s1 : Iterables.transform(possibleCompletions, Functions.toStringFunction())) {
                if (doesStringStartWith(s, s1)) {
                    list.add(s1);
                }
            }

            if (list.isEmpty()) {
                for (Object object : possibleCompletions) {
                    if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation) object).getPath())) {
                        list.add(String.valueOf(object));
                    }
                }
            }
        }

        return list;
    }

    public static BlockPos findNearestPos(ICommandSender sender) {
        BlockPos resultpos = null;
        BlockPos pos = sender.getPosition();
        World world = sender.getEntityWorld();
        Chunk chunk = world.getChunk(pos);
        //probably laggy as hell but hey it works
        for (int i = -ModConfig.lich_search_distance; i < ModConfig.lich_search_distance + 1; i++) {
            for (int j = -ModConfig.lich_search_distance; j < ModConfig.lich_search_distance + 1; j++) {
                boolean c = IsLichTowerAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, 100, (chunk.z + j) << 4);
                    break;
                }
            }
        }
        return resultpos;
    }

    public static BlockPos findNearestPosCave(ICommandSender sender) {
        BlockPos resultpos = null;
        BlockPos pos = sender.getPosition();
        World world = sender.getEntityWorld();
        Chunk chunk = world.getChunk(pos);
        //probably laggy as hell but hey it works
        for (int i = -ModConfig.void_blosom_search_distance; i < ModConfig.void_blosom_search_distance + 1; i++) {
            for (int j = -ModConfig.void_blosom_search_distance; j < ModConfig.void_blosom_search_distance + 1; j++) {
                boolean c = IsBlossomCaveAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, 100, (chunk.z + j) << 4);
                    break;
                }
            }
        }
        return resultpos;
    }

    public static BlockPos findNearestPosFrozenCastle(ICommandSender sender) {
        BlockPos resultpos = null;
        BlockPos pos = sender.getPosition();
        World world = sender.getEntityWorld();
        Chunk chunk = world.getChunk(pos);
        //probably laggy as hell but hey it works
        for (int i = -ModConfig.frozen_castle_search_distance; i < ModConfig.frozen_castle_search_distance + 1; i++) {
            for (int j = -ModConfig.frozen_castle_search_distance; j < ModConfig.frozen_castle_search_distance + 1; j++) {
                boolean c = IsFrozenCastleAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, 100, (chunk.z + j) << 4);
                    break;
                }
            }
        }
        return resultpos;
    }

    public static BlockPos findNearestPosHighCity(ICommandSender sender) {
        BlockPos resultpos = null;
        BlockPos pos = sender.getPosition();
        World world = sender.getEntityWorld();
        Chunk chunk = world.getChunk(pos);
        //probably laggy as hell but hey it works
        for (int i = -ModConfig.high_court_city_search_distance; i < ModConfig.high_court_city_search_distance + 1; i++) {
            for (int j = -ModConfig.high_court_city_search_distance; j < ModConfig.high_court_city_search_distance + 1; j++) {
                boolean c = IsHighCityAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, 100, (chunk.z + j) << 4);
                    break;
                }
            }
        }
        return resultpos;
    }

    public static BlockPos findNearestPosBurningFlame(ICommandSender sender) {
        BlockPos resultpos = null;
        BlockPos pos = sender.getPosition();
        World world = sender.getEntityWorld();
        Chunk chunk = world.getChunk(pos);

        for (int i = -ModConfig.burning_flame_arena_search_radius; i < ModConfig.burning_flame_arena_search_radius + 1; i++) {
            for (int j = -ModConfig.burning_flame_arena_search_radius; j < ModConfig.burning_flame_arena_search_radius + 1; j++) {
                boolean c = IsBurningFlameArenaAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, WorldConfig.burning_arena_y_level, (chunk.z + j) << 4);
                    break;
                }
            }
        }
        return resultpos;
    }

    protected static boolean IsBurningFlameArenaAtPos(World world, int chunkX, int chunkZ) {
        int spacing = WorldConfig.burning_arena_weight;
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
        Random random = world.setRandomSeed(k, l, 13259582);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l && isAllowedDimensionTooSpawnInBurningFlameArena(world.provider.getDimension())) {
            BlockPos pos = new BlockPos((i << 4), WorldConfig.burning_arena_y_level, (j << 4));
            return isAbleToSpawnHereBurningFlameArena(pos, world);
        } else {

            return false;
        }
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
            BlockPos pos = new BlockPos((i << 4), WorldConfig.high_city_y_height, (j << 4));
            return isAbleToSpawnHereHighCity(pos, world);
        } else {

            return false;
        }
    }

    protected static boolean IsFrozenCastleAtPos(World world, int chunkX, int chunkZ) {
        int spacing = WorldConfig.frozen_castle_spacing;
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
        Random random = world.setRandomSeed(k, l, 10387289);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l && isAllowedDimensionTooSpawnInFrozenCastle(world.provider.getDimension())) {
            BlockPos pos = new BlockPos((i << 4), 0, (j << 4));
            return isAbleToSpawnHereFrozenCastle(pos, world);
        } else {

            return false;
        }
    }

    protected static boolean IsBlossomCaveAtPos(World world, int chunkX, int chunkZ) {
        int spacing = WorldConfig.void_blossom_cave_weight;
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
        Random random = world.setRandomSeed(k, l, 10383709);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l && isAllowedDimensionTooSpawnIn(world.provider.getDimension())) {
            BlockPos pos = new BlockPos((i << 4), 0, (j << 4));
                return isAbleToSpawnHereBlossom(pos, world);
        } else {

            return false;
        }
    }

    protected static boolean IsLichTowerAtPos(World world, int chunkX, int chunkZ) {
        int spacing = WorldConfig.lich_tower_spacing;
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
        Random random = world.setRandomSeed(k, l, 10387312);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l && isAllowedDimensionTooSpawnInNightLich(world.provider.getDimension())) {
            BlockPos pos = new BlockPos((i << 4), 0, (j << 4));
            return isAbleToSpawnHere(pos, world);
        } else {

            return false;
        }
    }

    public static boolean isAbleToSpawnHere(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypes()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return false;
            }
        }
        return true;
    }

    private static List<BiomeDictionary.Type> lichTowerBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypes() {
        if(lichTowerBiomeTypes == null) {
            lichTowerBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.biome_types_blacklist_lich) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) lichTowerBiomeTypes.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return lichTowerBiomeTypes;
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

            for(String str : WorldConfig.frozen_castle_blacklist) {
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

    public static boolean isAbleToSpawnHereHighCity(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypesHighCity()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return false;
            }
        }
        return true;
    }


    public static boolean isAbleToSpawnHereBurningFlameArena(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypesBurningFlameArena()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return false;
            }
        }
        return true;
    }

    private static List<BiomeDictionary.Type> burningFlameArenaBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypesBurningFlameArena() {
        if(burningFlameArenaBiomeTypes == null) {
            burningFlameArenaBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.biome_types_blacklist_burning_arena) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) burningFlameArenaBiomeTypes.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return burningFlameArenaBiomeTypes;
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

    public static boolean isAbleToSpawnHereBlossom(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypesBlossom()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return false;
            }
        }
        return true;
    }

    private static List<BiomeDictionary.Type> blossomTowerBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypesBlossom() {
        if(blossomTowerBiomeTypes == null) {
            blossomTowerBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.biome_types_blossom) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) blossomTowerBiomeTypes.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return blossomTowerBiomeTypes;
    }


    public static boolean isAllowedDimensionTooSpawnInNightLich(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_lich_tower) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAllowedDimensionTooSpawnIn(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAllowedDimensionTooSpawnInFrozenCastle(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_frozen_castle) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAllowedDimensionTooSpawnInHighCastle(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_high_court_city) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAllowedDimensionTooSpawnInBurningFlameArena(int dimensionIn) {

            if(dimensionIn == -1) {
                return true;
            }
        return false;
    }

    public static boolean doesStringStartWith(String original, String region)
    {
        return region.regionMatches(true, 0, original, 0, original.length());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
