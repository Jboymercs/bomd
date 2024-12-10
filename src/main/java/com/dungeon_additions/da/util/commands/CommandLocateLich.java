package com.dungeon_additions.da.util.commands;

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

import javax.annotation.Nullable;
import java.util.*;

public class CommandLocateLich implements ICommand {

    private final List<String> aliases;
    private static List<Biome> spawnBiomesLichTower;
    private static List<Biome> spawnBiomes;
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
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "NightLichTower", "BlossomCave") : Collections.emptyList();
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
        for (int i = -100; i < 101; i++) {
            for (int j = -100; j < 101; j++) {
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
        for (int i = -100; i < 101; i++) {
            for (int j = -100; j < 101; j++) {
                boolean c = IsBlossomCaveAtPos(world, chunk.x + i, chunk.z + j);
                if (c) {
                    resultpos = new BlockPos((chunk.x + i) << 4, 100, (chunk.z + j) << 4);
                    break;
                }
            }
        }
        return resultpos;
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
        Random random = world.setRandomSeed(k, l, 10387312);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l && isAllowedDimensionTooSpawnIn(world.provider.getDimension())) {
            BlockPos pos = new BlockPos((i << 4), 0, (j << 4));
                return isBiomeValidBlossom(pos, world);
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
            return isBiomeValid(pos, world);
        } else {

            return false;
        }
    }

    public static boolean isBiomeValid(BlockPos pos, World world) {
        for(Biome biome : getSpawnBiomesLichTower()) {
            if(world.provider.getBiomeForCoords(pos) == biome) {
                return true;
            }
        }

        return false;
    }



    public static List<Biome> getSpawnBiomesLichTower() {
        if (spawnBiomesLichTower == null) {
            spawnBiomesLichTower = Lists.newArrayList();
            for (String str : WorldConfig.biome_allowed_lich) {
                try {
                    Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(str));
                    if (biome != null) spawnBiomesLichTower.add(biome);
                    else DALogger.logError("Biome " + str + " is not registered", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid registry name", e);
                }
            }
        }
        return spawnBiomesLichTower;
    }

    public static boolean isBiomeValidBlossom(BlockPos pos, World world) {
        for(Biome biome : getSpawnBiomes()) {
            if(WorldConfig.isBlacklist) {
                if(world.provider.getBiomeForCoords(pos) != biome) {
                    return true;
                }
            } else {
                if(world.provider.getBiomeForCoords(pos) == biome) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Biome> getSpawnBiomes() {
        if (spawnBiomes == null) {
            spawnBiomes = Lists.newArrayList();
            for (String str : WorldConfig.biome_allowed) {
                try {
                    Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(str));
                    if (biome != null) spawnBiomes.add(biome);
                    else DALogger.logError("Biome " + str + " is not registered", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid registry name", e);
                }
            }
        }
        return spawnBiomes;
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
