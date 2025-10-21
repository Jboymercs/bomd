package com.dungeon_additions.da.init;

import com.dungeon_additions.da.blocks.*;
import com.dungeon_additions.da.blocks.base.BlockPillarBase;
import com.dungeon_additions.da.blocks.base.BlockStairBase;
import com.dungeon_additions.da.blocks.base.BlockWallBase;
import com.dungeon_additions.da.blocks.base.slab.BlockDoubleSlab;
import com.dungeon_additions.da.blocks.base.slab.BlockHalfSlab;
import com.dungeon_additions.da.blocks.blossom.BlockVoidLily;
import com.dungeon_additions.da.blocks.boss.BlockBossReSummon;
import com.dungeon_additions.da.blocks.desert_dungeon.BlockGrumBlocker;
import com.dungeon_additions.da.blocks.desert_dungeon.BlockGrumDispenser;
import com.dungeon_additions.da.blocks.desert_dungeon.BlockPuzzleMirror;
import com.dungeon_additions.da.blocks.faurm.BlockFarumRope;
import com.dungeon_additions.da.blocks.faurm.BlockFaurmLantern;
import com.dungeon_additions.da.blocks.faurm.BlockLightningKeyBlock;
import com.dungeon_additions.da.blocks.gaelon.BlockEyePillar;
import com.dungeon_additions.da.blocks.ice_generator.BlockFrostBrick;
import com.dungeon_additions.da.blocks.lich.BlockSoulStar;
import com.dungeon_additions.da.blocks.vine.BlockAzealaVines;
import com.dungeon_additions.da.blocks.void_blocks.BlockObsdilithRune;
import com.dungeon_additions.da.blocks.void_blocks.BlockObsidianKeyBlock;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.frost_dungeon.EntityGreatWyrk;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightBoss;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.entity.void_dungeon.EntityObsidilith;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.handlers.BOMDSoundTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<Block>();
    public static final float STONE_HARDNESS = 1.7f;
    public static final float GLOOM_STONE_HARDNESS = 3.0F;
    public static final float STONE_RESISTANCE = 10f;
    public static final float GLOOM_STONE_RESITANCE = 6.0F;
    public static final float BRICK_HARDNESS = 2.0f;
    public static final float WOOD_HARDNESS = 1.5f;
    public static final float WOOD_RESISTANCE = 5.0f;
    public static final float PLANTS_HARDNESS = 0.2f;
    public static final float PLANTS_RESISTANCE = 2.0f;
    public static final float ORE_HARDNESS = 3.0F;
    public static final float OBSIDIAN_HARDNESS = 50;
    public static final float OBSIDIAN_RESISTANCE = 2000;
    //public static final Block ASH_BRICK = new BlockBase("ash_brick", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(ModCreativeTabs.ITEMS);

    public static final Block SPORE_BLOSSOM = new BlockSporeBlossom("spore_blossom", PLANTS_HARDNESS, PLANTS_RESISTANCE, BOMDSoundTypes.MOSS).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block MOSS_BLOCK = new BlockMossBlock("moss_block", Material.GRASS, PLANTS_HARDNESS, PLANTS_RESISTANCE, BOMDSoundTypes.MOSS).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DUNGEON_BRICK = new BlockBase("dungeon_brick", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block MOSSY_DUNGEON_BRICK = new BlockBase("mossy_dungeon_brick", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block MOSS_CARPET = new BlockMossCarpet("moss_carpet").setCreativeTab(DungeonAdditionsTab.BLOCKS);

    public static final Block AZAELA_VINES = new BlockAzealaVines("aza_vines", Material.PLANTS).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block AZAELA_BERRY_VINES = new BlockAzealaVines("aza_vines_berries", Material.PLANTS).setLightLevel(0.9375F);

    public static final Block AZAELA_LEAVES = new BlockAzealaLeaves("azaela_leaf", PLANTS_HARDNESS, PLANTS_RESISTANCE, BOMDSoundTypes.AZA_LEAVES).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block INDESTRUCTABLE_BLOCK = new BlockSpawnFlower("strong_block", Material.GRASS, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.PLANT);
    public static final Block MEGA_STRUCTURE_BLOCK = new BlockMegaStructure("mega_structure");
    public static final Block VINE_WALL = new BlockAzealaFence(Material.GRASS, false, "az_fence").setHardness(STONE_HARDNESS).setResistance(STONE_RESISTANCE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARKGLOW_BARS = new BlockAzealaFence(Material.ROCK, false, "darkglow_bars").setHardness(GLOOM_STONE_HARDNESS).setResistance(GLOOM_STONE_RESITANCE).setCreativeTab(DungeonAdditionsTab.BLOCKS);

    //Fire Update
    public static final Block FIERY_BLOCK = new BlockBase("fire_stone", Material.ROCK, STONE_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CHISLED_FIERY_STONE = new BlockBase("chisled_fire_stone", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block FIERY_LIT_BLOCK = new BlockFireStoneLit("fire_stone_lit", Material.ROCK, STONE_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE, "fire_stone_desc").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setLightLevel(0.7F).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block FIERY_STONE_STAIRS = new BlockStairBase("fire_stone_stairs", FIERY_BLOCK.getDefaultState(), STONE_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block FIERY_STONE_WALL = new BlockWallBase("fire_stone_wall", Material.ROCK, STONE_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.DECORATIONS).setCreativeTab(DungeonAdditionsTab.BLOCKS);

    //Overworld DUngeon Update
    public static final Block MOSSY_SPRUCE_WOOD = new BlockMossySpruce("mossy_spruce", Material.WOOD, WOOD_HARDNESS, WOOD_RESISTANCE, SoundType.WOOD).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block KNIGHT_STONE = new BlockBase("knight_stone", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block KNIGHT_KEY_BLOCK = new BlockKnightBossKeyBlock("knight_key_block", ModItems.ROT_KNIGHT_KEY, ((world, pos) -> new EntityRotKnightBoss(world, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F))).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block FROZEN_CASTLE_KEY_BLOCK = new BlockFrostKeyBlock("frost_key_block", ModItems.FROST_KEY, ((worldv, posr) -> new EntityGreatWyrk(worldv, posr.getX(), posr.getY(), posr.getZ()))).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block OBSIDIAN_KEY_BLOCK = new BlockObsidianKeyBlock("obsidian_end_frame", Material.ROCK, ((world, pos) -> new EntityObsidilith(world, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F)));
    public static final Block SKY_KEY_BLOCK = new BlockLightningKeyBlock("sky_key_block", MobConfig.dragon_starts_first ? ((worldv, posr) -> new EntityHighKingDrake(worldv, posr.getX(), posr.getY() + 25, posr.getZ())) : ((worldv, posr) -> new EntityHighKing(worldv, posr.getX(), posr.getY(), posr.getZ()))).setCreativeTab(DungeonAdditionsTab.BLOCKS);

    public static final Block BOSS_RESUMMON_BLOCK = new BlockBossReSummon("boss_resummon", Material.ROCK, ModItems.LIGHTNING_KEY).setLightLevel(0.9F).setCreativeTab(DungeonAdditionsTab.BLOCKS);

    // LICH UPDATE
    public static final Block PETROGLOOM = new BlockBase("gloom_stone", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM = new BlockBase("cold_stone", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block PETROGLOOM_BRICKS = new BlockBase("gloom_brick", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM_BRICKS = new BlockBase("cold_brick", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_BRICK_LAMP = new BlockBase("cold_brick_lamp", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setLightLevel(1.0F).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block SNOW_COLD_PETROGLOOM_BRICKS = new BlockFrostBrick("snow_cold_brick", "snow_cold_brick_desc", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_BRICK = new BlockBase("city_brick", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block FARUM_STONE = new BlockBase("farum_stone", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_BRICK_ALT = new BlockBase("city_brick_alt", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_BRICK_DECOR = new BlockBase("city_decoration", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_DECORATION = new BlockBase("city_chisled", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_BRICK_TILE = new BlockBase("city_tile", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_SMOOTH = new BlockBase("city_smooth", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_PILLAR = new BlockPillarBase("city_pillar", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_PILLAR_CHISLED = new BlockPillarBase("city_pillar_chisled", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GOLD_CITY_BRICK = new BlockBase("gold_city_brick", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GRUM_BRICKS = new BlockBase("grum_brick", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GRUM_CHISLED = new BlockBase("grum_chisled", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GRUM_STONE = new BlockBase("grum_stone", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GRUM_SMOOTH = new BlockBase("grum_smooth", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GRUM_TILE = new BlockBase("grum_tile", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GRUM_CHISLED_TILE = new BlockBase("grum_chisled_tile", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARK_GLOW_STONEBRICKS = new BlockBase("darkglow_stonebricks", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARK_GLOW_BRICKS = new BlockBase("darkglow_bricks", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARK_GLOW_DECORATION_BRICKS = new BlockBase("darkglow_decoration_bricks", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARK_GLOW_TILE = new BlockBase("darkglow_tile", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARK_GLOW_LIT_BRICKS = new BlockBase("darkglow_lit_bricks", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setLightLevel(0.6F).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARK_GLOW_LIT_DECORATION_BRICKS = new BlockBase("darkglow_lit_decoration_bricks", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setLightLevel(0.8F).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GAELON_CRYSTAL = new BlockGaelonCrystalBlock("gaelon_crystal", Material.GLASS, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, BOMDSoundTypes.GAELON_CRYSTAL).setLightLevel(1.0F).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GAELON_CHUNK = new BlockGaelonCrystal("gaelon_chunk", Material.ROCK, PLANTS_HARDNESS, PLANTS_RESISTANCE, BOMDSoundTypes.GAELON_CRYSTAL).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block ALFERA_PLANKS = new BlockBase("alfera_planks", Material.WOOD, WOOD_HARDNESS, WOOD_RESISTANCE, SoundType.WOOD).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block ALFERA_PLANKS_ROD = new BlockBase("alfera_planks_rod", Material.WOOD, WOOD_HARDNESS, WOOD_RESISTANCE, SoundType.WOOD).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block ALFERA_CRATE = new BlockBase("alfera_crate", Material.WOOD, WOOD_HARDNESS, WOOD_RESISTANCE, SoundType.WOOD).setCreativeTab(DungeonAdditionsTab.BLOCKS);



    public static final Block OBSIDILITH_RUNE = new BlockObsdilithRune("obsidilith_rune", Material.ROCK, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.GLASS).setCreativeTab(DungeonAdditionsTab.BLOCKS).setLightLevel(0.5F);
    public static final Block FAKE_OBSIDIAN = new BlockBase("fake_obsidian", Material.ROCK, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setBlockUnbreakable();

    public static final Block PETROGLOOM_SMOOTH = new BlockBase("gloom_smooth", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM_SMOOTH = new BlockBase("cold_smooth", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block PETROGLOOM_CARVED = new BlockPillarBase("gloom_carved", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM_CARVED = new BlockPillarBase("cold_carved", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block PETROGLOOM_PILLAR = new BlockPillarBase("gloom_pillar", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM_PILLAR = new BlockPillarBase("cold_pillar", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GRUM_PILLAR = new BlockPillarBase("grum_pillar", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARK_GLOW_PILLAR = new BlockPillarBase("darkglow_pillar", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARK_GLOW_LIT_PILLAR = new BlockPillarBase("darkglow_pillar_lit", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setBlockUnbreakable().setLightLevel(1.0F).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARK_GLOW_EYE_PILLAR = new BlockEyePillar("darkglow_pillar_eye", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setLightLevel(1.0F);
    public static final Block DARK_BRICK = new BlockBase("dark_brick", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);

    public static final Block PETROGLOOM_ROUGH = new BlockPetroGloom("gloom_rough", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM_ROUGH = new BlockPetroGloom("cold_rough", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block PETROGLOOM_TRIANGLE = new BlockBase("gloom_triangle", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM_TRIANGLE = new BlockBase("cold_triangle", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block PETROGLOOM_STAIRS = new BlockStairBase("gloom_stone_stairs", PETROGLOOM.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM_STAIRS = new BlockStairBase("cold_stone_stairs", PETROGLOOM.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block PETROGLOOM_BRICK_STAIRS = new BlockStairBase("gloom_brick_stairs", PETROGLOOM_BRICKS.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM_BRICK_STAIRS = new BlockStairBase("cold_brick_stairs", PETROGLOOM_BRICKS.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block PETROGLOOM_SMOOTH_STAIRS = new BlockStairBase("gloom_smooth_stairs", PETROGLOOM_SMOOTH.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM_SMOOTH_STAIRS = new BlockStairBase("cold_smooth_stairs", PETROGLOOM_SMOOTH.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_BRICK_STAIRS = new BlockStairBase("city_brick_stairs", CITY_BRICK.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_BRICK_STAIRS_ALT = new BlockStairBase("city_brick_stairs_alt", CITY_BRICK_ALT.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_TILE_STAIRS = new BlockStairBase("city_tile_stairs", CITY_BRICK_TILE.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GOLD_CITY_BRICK_STAIRS = new BlockStairBase("gold_city_brick_stairs", GOLD_CITY_BRICK.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GRUM_BRICK_STAIRS = new BlockStairBase("grum_brick_stairs", GRUM_BRICKS.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DUNGEON_BRICK_STAIRS = new BlockStairBase("dungeon_brick_stairs", DUNGEON_BRICK.getDefaultState(), STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARKGLOW_STONEBRICK_STAIRS = new BlockStairBase("darkglow_stonebrick_stairs", DARK_GLOW_STONEBRICKS.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARKGLOW_BRICK_STAIRS = new BlockStairBase("darkglow_brick_stairs", DARK_GLOW_BRICKS.getDefaultState(), GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);



    public static final BlockSlab CITY_BRICK_SLAB_DOUBLE = new BlockDoubleSlab("city_brick_double", Material.ROCK, CreativeTabs.SEARCH, ModBlocks.CITY_BRICK_SLAB_HALF, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab CITY_BRICK_SLAB_HALF = new BlockHalfSlab("city_brick_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.CITY_BRICK_SLAB_HALF, ModBlocks.CITY_BRICK_SLAB_DOUBLE, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab CITY_BRICK_SLAB_DOUBLE_ALT = new BlockDoubleSlab("city_brick_alt_double", Material.ROCK, CreativeTabs.SEARCH, ModBlocks.CITY_BRICK_SLAB_HALF_ALT, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab CITY_BRICK_SLAB_HALF_ALT = new BlockHalfSlab("city_brick_alt_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.CITY_BRICK_SLAB_HALF_ALT, ModBlocks.CITY_BRICK_SLAB_DOUBLE_ALT, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab CITY_TILE_SLAB_DOUBLE = new BlockDoubleSlab("city_tile_double", Material.ROCK, CreativeTabs.SEARCH, ModBlocks.CITY_TILE_SLAB_HALF, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab CITY_TILE_SLAB_HALF = new BlockHalfSlab("city_tile_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.CITY_TILE_SLAB_HALF, ModBlocks.CITY_TILE_SLAB_DOUBLE, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);

    public static final BlockSlab GOLD_CITY_BRICK_SLAB_DOUBLE = new BlockDoubleSlab("gold_city_brick_double", Material.ROCK, CreativeTabs.SEARCH, ModBlocks.GOLD_CITY_BRICK_SlAB_HALF, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab GOLD_CITY_BRICK_SlAB_HALF = new BlockHalfSlab("gold_city_brick_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.GOLD_CITY_BRICK_SlAB_HALF, ModBlocks.GOLD_CITY_BRICK_SLAB_DOUBLE, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);

    public static final BlockSlab PETROGLOOM_STONE_SLAB_DOUBLE = new BlockDoubleSlab("gloom_stone_double", Material.ROCK, CreativeTabs.SEARCH, ModBlocks.PETROGLOOM_STONE_SlAB_HALF, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab PETROGLOOM_STONE_SlAB_HALF = new BlockHalfSlab("gloom_stone_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.PETROGLOOM_STONE_SlAB_HALF, ModBlocks.PETROGLOOM_STONE_SLAB_DOUBLE, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab COLD_PETROGLOOM_STONE_SLAB_DOUBLE = new BlockDoubleSlab("cold_stone_double", Material.ROCK, CreativeTabs.SEARCH, ModBlocks.COLD_PETROGLOOM_STONE_SlAB_HALF, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab COLD_PETROGLOOM_STONE_SlAB_HALF = new BlockHalfSlab("cold_stone_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.COLD_PETROGLOOM_STONE_SlAB_HALF, ModBlocks.COLD_PETROGLOOM_STONE_SLAB_DOUBLE, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab PETROGLOOM_SMOOTH_SLAB_DOUBLE = new BlockDoubleSlab("gloom_smooth_double", Material.ROCK, CreativeTabs.SEARCH, ModBlocks.PETROGLOOM_SMOOTH_SLAB_HALF, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab PETROGLOOM_SMOOTH_SLAB_HALF = new BlockHalfSlab("gloom_smooth_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.PETROGLOOM_SMOOTH_SLAB_HALF, ModBlocks.PETROGLOOM_SMOOTH_SLAB_DOUBLE, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab COLD_PETROGLOOM_SMOOTH_SLAB_DOUBLE = new BlockDoubleSlab("cold_smooth_double", Material.ROCK, CreativeTabs.SEARCH, ModBlocks.COLD_PETROGLOOM_SMOOTH_SLAB_HALF, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab COLD_PETROGLOOM_SMOOTH_SLAB_HALF = new BlockHalfSlab("cold_smooth_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.COLD_PETROGLOOM_SMOOTH_SLAB_HALF, ModBlocks.COLD_PETROGLOOM_SMOOTH_SLAB_DOUBLE, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab PETROGLOOM_BRICK_SLAB_DOUBLE = new BlockDoubleSlab("gloom_brick_double", Material.ROCK, CreativeTabs.SEARCH, ModBlocks.PETROGLOOM_BRICK_SLAB_HALF, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab PETROGLOOM_BRICK_SLAB_HALF = new BlockHalfSlab("gloom_brick_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.PETROGLOOM_BRICK_SLAB_HALF, ModBlocks.PETROGLOOM_BRICK_SLAB_DOUBLE, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab FIRE_STONE_SLAB_DOUBLE = new BlockDoubleSlab("fire_stone_double", Material.ROCK, CreativeTabs.SEARCH, ModBlocks.FIRE_STONE_SLAB_HALF, STONE_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE);
    public static final BlockSlab FIRE_STONE_SLAB_HALF = new BlockHalfSlab("fire_stone_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.FIRE_STONE_SLAB_HALF, ModBlocks.FIRE_STONE_SLAB_DOUBLE, STONE_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE);
    public static final BlockSlab COLD_PETROGLOOM_BRICK_SLAB_DOUBLE = new BlockDoubleSlab("cold_brick_double", Material.ROCK, CreativeTabs.SEARCH, ModBlocks.COLD_PETROGLOOM_BRICK_SLAB_HALF, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab COLD_PETROGLOOM_BRICK_SLAB_HALF = new BlockHalfSlab("cold_brick_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.COLD_PETROGLOOM_BRICK_SLAB_HALF, ModBlocks.COLD_PETROGLOOM_BRICK_SLAB_DOUBLE, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab GRUM_BRICK_DOUBLE = new BlockDoubleSlab("grum_brick_double", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.GRUM_BRICK_HALF, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE);
    public static final BlockSlab GRUM_BRICK_HALF = new BlockHalfSlab("grum_brick_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.GRUM_BRICK_HALF, ModBlocks.GRUM_BRICK_DOUBLE, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE);
    public static final BlockSlab DUNGEON_BRICK_DOUBLE = new BlockDoubleSlab("dungeon_brick_double", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.DUNGEON_BRICK_HALF, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE);
    public static final BlockSlab DUNGEON_BRICK_HALF = new BlockHalfSlab("dungeon_brick_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.DUNGEON_BRICK_HALF, ModBlocks.DUNGEON_BRICK_DOUBLE, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE);
    public static final BlockSlab DARKGLOW_STONEBRICK_DOUBLE = new BlockDoubleSlab("darkglow_stonebrick_double", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.DARKGLOW_STONEBRICK_HALF, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab DARKGLOW_STONEBRICK_HALF = new BlockHalfSlab("darkglow_stonebrick_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.DARKGLOW_STONEBRICK_HALF, ModBlocks.DARKGLOW_STONEBRICK_DOUBLE, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab DARKGLOW_BRICK_DOUBLE = new BlockDoubleSlab("darkglow_brick_double", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.DARKGLOW_BRICK_HALF, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);
    public static final BlockSlab DARKGLOW_BRICK_HALF = new BlockHalfSlab("darkglow_brick_half", Material.ROCK, DungeonAdditionsTab.BLOCKS, ModBlocks.DARKGLOW_BRICK_HALF, ModBlocks.DARKGLOW_BRICK_DOUBLE, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM);

    public static final Block PETROGLOOM_STONE_WALL = new BlockWallBase("gloom_stone_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM_STONE_WALL = new BlockWallBase("cold_stone_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block PETROGLOOM_SMOOTH_WALL = new BlockWallBase("gloom_smooth_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM_SMOOTH_WALL = new BlockWallBase("cold_smooth_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block PETROGLOOM_BRICK_WALL = new BlockWallBase("gloom_brick_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block COLD_PETROGLOOM_BRICK_WALL = new BlockWallBase("cold_brick_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_BRICK_WALL = new BlockWallBase("city_brick_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_BRICK_WALL_ALT = new BlockWallBase("city_brick_wall_alt", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_TILE_WALL = new BlockWallBase("city_tile_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block CITY_SMOOTH_WALL = new BlockWallBase("city_smooth_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GOLD_CITY_WALL = new BlockWallBase("gold_city_brick_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block GRUM_BRICK_WALL = new BlockWallBase("grum_brick_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DUNGEON_BRICK_WALL = new BlockWallBase("dungeon_brick_wall", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARKGLOW_BRICK_WALL = new BlockWallBase("darkglow_brick_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DARKGLOW_STONEBRICK_WALL = new BlockWallBase("darkglow_stonebrick_wall", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS);

    public static final Block FARUM_ROPE = new BlockFarumRope("farum_rope", Material.GROUND, PLANTS_HARDNESS, PLANTS_RESISTANCE, SoundType.CLOTH).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block FARUM_LANTERN = new BlockFaurmLantern("farum_lantern", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.PETRO_GLOOM).setCreativeTab(DungeonAdditionsTab.BLOCKS).setLightLevel(1.0F);

    public static final Block ICICLE_BLOCK = new BlockIcicle("frost_icicle", PLANTS_HARDNESS, PLANTS_RESISTANCE, SoundType.GLASS).setCreativeTab(DungeonAdditionsTab.BLOCKS).setLightLevel(0.5F);
    public static final Block LICH_SOUL_STAR_BLOCK = new BlockSoulStar("soul_star_block", ModItems.SOUL_STAR).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block VOID_LILY_BLOCK = new BlockVoidLily("void_lily", "void_lily_desc").setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block PUZZLE_MIRROR = new BlockPuzzleMirror("puzzle_mirror", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE);
    public static final Block PUZZLE_DISPLACER = new BlockBase("grum_displacer", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS).setBlockUnbreakable();
    public static final Block PUZZLE_DISPENCER = new BlockGrumDispenser("grum_dispenser", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block PUZZLE_BLOCKER = new BlockGrumBlocker("grum_blocker", Material.ROCK, GLOOM_STONE_HARDNESS, GLOOM_STONE_RESITANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block PUZZLE_DOOR = new BlockBase("grum_tile_r", Material.ROCK, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, BOMDSoundTypes.GRUM_STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);

    public static final Block CHANGE_BLOCK = new BlockChangingDoor("change_block", Material.ROCK, OBSIDIAN_HARDNESS, OBSIDIAN_RESISTANCE, SoundType.STONE).setCreativeTab(DungeonAdditionsTab.BLOCKS);

    public static final Block LEVITATION_BLOCK = new BlockLevitationAltar("levitation_block", Material.ROCK, STONE_HARDNESS, OBSIDIAN_RESISTANCE, BOMDSoundTypes.PETRO_GLOOM, "levitation_block_desc").setCreativeTab(DungeonAdditionsTab.BLOCKS);
    public static final Block DISAPPEARING_SPAWNER = new BlockDisappearingSpawner("cave_spawner", Material.ROCK);
    public static final Block DISAPPEARING_SPAWNER_MOSS = new BlockDisappearingSpawner("moss_spawner", Material.ROCK);
    public static final Block DISAPPEARING_SPAWNER_HIGH_CITY = new BlockDisappearingSpawner("high_spawner", Material.ROCK);
    public static final Block DISAPPEARING_SPAWNER_FORGOTTEN_TEMPLE = new BlockDisappearingSpawner("temple_spawner", Material.ROCK);
    public static final Block DISAPPEARING_SPAWNER_END = new BlockDisappearingSpawner("end_spawner", Material.ROCK);
}
