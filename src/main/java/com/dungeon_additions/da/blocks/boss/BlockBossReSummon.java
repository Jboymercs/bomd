package com.dungeon_additions.da.blocks.boss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.blocks.BlockBase;
import com.dungeon_additions.da.blocks.base.IBlockUpdater;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntityAegyptianWarlord;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.frost_dungeon.EntityGreatWyrk;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightBoss;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.entity.tileEntity.TileEntityBossReSummon;
import com.dungeon_additions.da.entity.void_dungeon.EntityObsidilith;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.gui.ForgeGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockBossReSummon extends BlockBase implements ITileEntityProvider, IBlockUpdater {

    public static final PropertyEnum<BlockEnumBossSummonState> STATE = PropertyEnum.create("state", BlockEnumBossSummonState.class);
    private Item activationItem;
    int counter = 0;

    public BlockBossReSummon(String name, Material material, Item activationItem) {
        super(name, material);
        this.setBlockUnbreakable();
        this.activationItem = activationItem;
    }

    @Override
    public void update(World world, BlockPos pos) {
        counter++;
        if (counter % 5 == 0) {
            List<EntityPlayerSP> list = world.<EntityPlayerSP>getPlayers(EntityPlayerSP.class, new Predicate<EntityPlayerSP>() {
                @Override
                public boolean apply(@Nullable EntityPlayerSP player) {
                    return player.getHeldItem(EnumHand.MAIN_HAND).getItem() == activationItem;
                }
            });

        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityBossReSummon) {
            TileEntityBossReSummon boss_spawner = ((TileEntityBossReSummon) te);
            if(boss_spawner.getState() != BlockEnumBossSummonState.INACTIVE) {
                if(!world.isRemote && player.getHeldItemMainhand().getItem() == ModItems.SOUL_KEY) {
                    //summon new boss upon inserting the key
                    int timesUsed = boss_spawner.getTimesUsed();

                    if(boss_spawner.getState() == BlockEnumBossSummonState.FALLEN_STORMVIER) {
                        EntityRotKnightBoss boss = new EntityRotKnightBoss(world, timesUsed, pos);
                        boss.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        world.spawnEntity(boss);
                        world.setBlockToAir(pos);
                        player.getHeldItem(hand).shrink(1);
                    } else if (boss_spawner.getState() == BlockEnumBossSummonState.VOID_BLOSSOM) {
                        EntityVoidBlossom blossom = new EntityVoidBlossom(world, timesUsed, pos);
                        blossom.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        world.setBlockToAir(pos);
                        blossom.onSummonBoss(pos);
                        world.spawnEntity(blossom);
                        player.getHeldItem(hand).shrink(1);
                    } else if (boss_spawner.getState() == BlockEnumBossSummonState.ANCIENT_WYRK) {
                        EntityGreatWyrk wyrk = new EntityGreatWyrk(world, timesUsed, pos);
                        wyrk.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        world.setBlockToAir(pos);
                        world.spawnEntity(wyrk);
                        player.getHeldItem(hand).shrink(1);
                    } else if (boss_spawner.getState() == BlockEnumBossSummonState.FLAME_KNIGHT) {
                        EntityFlameKnight knight = new EntityFlameKnight(world, timesUsed, pos);
                        knight.onSummonViaBlock(pos);
                        world.spawnEntity(knight);
                        world.setBlockToAir(pos);
                        player.getHeldItem(hand).shrink(1);
                    }  else if (boss_spawner.getState() == BlockEnumBossSummonState.OBSIDILITH) {
                        EntityObsidilith knight = new EntityObsidilith(world, timesUsed, pos);
                        world.spawnEntity(knight);
                        world.setBlockToAir(pos);
                        player.getHeldItem(hand).shrink(1);
                    }  else if (boss_spawner.getState() == BlockEnumBossSummonState.APATHYR) {
                        EntityApathyr knight = new EntityApathyr(world, timesUsed, pos);
                        world.setBlockToAir(pos);
                        knight.onSummonViaBlock(pos);
                        world.spawnEntity(knight);
                        player.getHeldItem(hand).shrink(1);
                    }  else if (boss_spawner.getState() == BlockEnumBossSummonState.AEGYPTIAN) {
                        EntityAegyptianWarlord warlord = new EntityAegyptianWarlord(world, timesUsed, pos);
                        warlord.setSetSpawnLoc(true);
                        warlord.setSpawnLocation(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
                        warlord.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        world.setBlockToAir(pos);
                        world.spawnEntity(warlord);
                        player.getHeldItem(hand).shrink(1);
                    } else if (boss_spawner.getState() == BlockEnumBossSummonState.NIGHT_LICH) {
                        if(MobConfig.lich_enable_daylight && world.getWorldTime() < MobConfig.lich_summon_time) {
                            player.sendStatusMessage(new TextComponentTranslation("da.lich_wrong_time", new Object[0]), true);
                            return false;
                        } else {
                            EntityNightLich lich = new EntityNightLich(world, timesUsed, pos);
                            lich.setPosition(pos.getX(), pos.getY() + 10, pos.getZ());
                            world.spawnEntity(lich);
                            world.setBlockToAir(pos);
                            player.getHeldItem(hand).shrink(1);
                        }
                    } else if (boss_spawner.getState() == BlockEnumBossSummonState.HIGH_KING) {
                        if(MobConfig.dragon_starts_first) {
                            EntityHighKingDrake dragon = new EntityHighKingDrake(world, timesUsed, pos.add(0, 25, 0));
                            dragon.setPosition(pos.getX(), pos.getY() + 25, pos.getZ());
                            world.spawnEntity(dragon);
                        } else {
                            EntityHighKing king = new EntityHighKing(world, timesUsed, pos);
                            king.setPosition(pos.getX(), pos.getY(), pos.getZ());
                            world.spawnEntity(king);
                        }
                        world.setBlockToAir(pos);
                        player.getHeldItem(hand).shrink(1);
                    }
                }
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBossReSummon();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityBossReSummon)) return state;
        TileEntityBossReSummon spawner = (TileEntityBossReSummon) te;
        return state.withProperty(STATE, spawner.getState());
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityBossReSummon && worldIn.rand.nextInt(3) == 0) {
            TileEntityBossReSummon spawner = ((TileEntityBossReSummon) te);
            if(spawner.getState() == BlockEnumBossSummonState.INACTIVE) {
                Vec3d particlePos = new Vec3d(pos.getX() -0.1 + ModRand.getFloat(1.1F), pos.getY() + 0.9 + ModRand.getFloat(1F), pos.getZ() -0.1+ ModRand.getFloat(1.1F));
                ParticleManager.spawnDust(worldIn, particlePos, ModColors.BLACK, new Vec3d(0, 0.05, 0), ModRand.range(10, 15));
                //Fallen Stormvier
            } else if (spawner.getState() == BlockEnumBossSummonState.FALLEN_STORMVIER) {
                Vec3d particlePos = new Vec3d(pos.getX() -0.1 + ModRand.getFloat(1.1F), pos.getY() + 0.9 + ModRand.getFloat(1F), pos.getZ() -0.1 + ModRand.getFloat(1.1F));
                ParticleManager.spawnDust(worldIn, particlePos, ModColors.GREEN, new Vec3d(0, 0.05, 0), ModRand.range(10, 15));
                //Void Blossom
            } else if (spawner.getState() == BlockEnumBossSummonState.VOID_BLOSSOM) {
                Vec3d particlePos = new Vec3d(pos.getX() -0.1 + ModRand.getFloat(1.1F), pos.getY() + 0.9 + ModRand.getFloat(1F), pos.getZ() -0.1 + ModRand.getFloat(1.1F));
                ParticleManager.spawnDust(worldIn, particlePos, ModColors.PINK, new Vec3d(0, 0.05, 0), ModRand.range(10, 15));
                //Ancient Wyrk
            }  else if (spawner.getState() == BlockEnumBossSummonState.ANCIENT_WYRK) {
                Vec3d particlePos = new Vec3d(pos.getX() -0.1 + ModRand.getFloat(1.1F), pos.getY() + 0.9 + ModRand.getFloat(1F), pos.getZ() -0.1 + ModRand.getFloat(1.1F));
                ParticleManager.spawnDust(worldIn, particlePos, ModColors.WHITE, new Vec3d(0, 0.05, 0), ModRand.range(10, 15));
                //Knight of Burning Flame
            }  else if (spawner.getState() == BlockEnumBossSummonState.FLAME_KNIGHT) {
                Vec3d particlePos = new Vec3d(pos.getX() -0.1 + ModRand.getFloat(1.1F), pos.getY() + 0.9 + ModRand.getFloat(1F), pos.getZ() -0.1 + ModRand.getFloat(1.1F));
                ParticleManager.spawnDust(worldIn, particlePos, ModColors.FIREBALL_ORANGE, new Vec3d(0, 0.05, 0), ModRand.range(10, 15));
                //Night Lich
            }  else if (spawner.getState() == BlockEnumBossSummonState.NIGHT_LICH) {
                Vec3d particlePos = new Vec3d(pos.getX() -0.1 + ModRand.getFloat(1.1F), pos.getY() + 0.9 + ModRand.getFloat(1F), pos.getZ() -0.1 + ModRand.getFloat(1.1F));
                ParticleManager.spawnDust(worldIn, particlePos, ModColors.AZURE, new Vec3d(0, 0.05, 0), ModRand.range(10, 15));
                //The High King
            }  else if (spawner.getState() == BlockEnumBossSummonState.HIGH_KING) {
                Vec3d particlePos = new Vec3d(pos.getX() -0.1 + ModRand.getFloat(1.1F), pos.getY() + 0.9 + ModRand.getFloat(1F), pos.getZ() -0.1 + ModRand.getFloat(1.1F));
                ParticleManager.spawnDust(worldIn, particlePos, ModColors.YELLOW, new Vec3d(0, 0.05, 0), ModRand.range(10, 15));
                //Obsidilith
            }  else if (spawner.getState() == BlockEnumBossSummonState.OBSIDILITH) {
                Vec3d particlePos = new Vec3d(pos.getX() -0.1 + ModRand.getFloat(1.1F), pos.getY() + 0.9 + ModRand.getFloat(1F), pos.getZ() -0.1 + ModRand.getFloat(1.1F));
                ParticleManager.spawnDust(worldIn, particlePos, ModColors.MAELSTROM, new Vec3d(0, 0.05, 0), ModRand.range(10, 15));
            }   else if (spawner.getState() == BlockEnumBossSummonState.APATHYR) {
                Vec3d particlePos = new Vec3d(pos.getX() -0.1 + ModRand.getFloat(1.1F), pos.getY() + 0.9 + ModRand.getFloat(1F), pos.getZ() -0.1 + ModRand.getFloat(1.1F));
                Main.proxy.spawnParticle(15, worldIn, particlePos.x, particlePos.y, particlePos.z, worldIn.rand.nextFloat()/6 - worldIn.rand.nextFloat()/6, 0.04, worldIn.rand.nextFloat()/6 - worldIn.rand.nextFloat()/6, 100);
            }    else if (spawner.getState() == BlockEnumBossSummonState.AEGYPTIAN) {
                Vec3d particlePos = new Vec3d(pos.getX() -0.1 + ModRand.getFloat(1.1F), pos.getY() + 0.9 + ModRand.getFloat(1F), pos.getZ() -0.1 + ModRand.getFloat(1.1F));
                Main.proxy.spawnParticle(23, worldIn, particlePos.x, particlePos.y, particlePos.z, 0, 0.03, 0, 15128888);
            }
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STATE);
    }

    public String byState(IBlockState state) {
        return  "boss_resummon";
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}
