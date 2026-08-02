package com.dungeon_additions.da.blocks.aspect_forge;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.item.EnumWeaponType;
import com.dungeon_additions.da.blocks.BlockBase;
import com.dungeon_additions.da.blocks.base.IBlockUpdater;
import com.dungeon_additions.da.blocks.boss.BlockEnumBossSummonState;
import com.dungeon_additions.da.entity.tileEntity.TileEntityAspectForge;
import com.dungeon_additions.da.entity.tileEntity.TileEntityBossReSummon;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.items.tools.ToolSword;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockAspectForge extends BlockBase implements ITileEntityProvider, IBlockUpdater {

    private Item activationItem;
    int counter = 0;
    public static final PropertyEnum<BlockEnumAspectForge> STATE = PropertyEnum.create("state", BlockEnumAspectForge.class);

    public BlockAspectForge(String name, Material material, Item activationItem) {
        super(name, material);
        this.setBlockUnbreakable();
        this.activationItem = activationItem;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityAspectForge && !world.isRemote) {
            //call for the forge
            TileEntityAspectForge aspect_forge = ((TileEntityAspectForge) te);
            //used for setting new aspect forges to a new permanent state
            if(aspect_forge.getState() == BlockEnumAspectForge.BASE) {
                if(player.isPotionActive(ModPotions.CLAIRVOYANCE) && !(player.getHeldItemMainhand().getItem() == ModItems.ASPECT_TEMPLATE)) {
                    if (player.getHeldItemMainhand().getItem() instanceof ItemShield) {
                        aspect_forge.setState(BlockEnumAspectForge.SHIELD, "aspect_shield");
                    }
                    if (player.getHeldItemMainhand().getItem() instanceof ItemPotion) {
                        aspect_forge.setState(BlockEnumAspectForge.COLOSSAL, "aspect_mage");
                    }
                    if (player.getHeldItemMainhand().getItem() == Items.BOW) {
                        aspect_forge.setState(BlockEnumAspectForge.BOW, "aspect_bow");
                    }
                    if (player.getHeldItemOffhand().getItem() instanceof ItemSword && player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                        aspect_forge.setState(BlockEnumAspectForge.DUELIST, "aspect_duelist");
                    }else if (player.getHeldItemMainhand().getItem() instanceof ToolSword) {
                        ToolSword type = ((ToolSword) player.getHeldItemMainhand().getItem());
                        if(type.getWeaponAnimationType() == EnumWeaponType.DAGGER) {
                            aspect_forge.setState(BlockEnumAspectForge.DAGGER, "aspect_dagger");
                        }
                        if(type.getWeaponAnimationType() == EnumWeaponType.SWORD || type.getWeaponAnimationType() == EnumWeaponType.PARRY_SWORD) {
                            aspect_forge.setState(BlockEnumAspectForge.SWORD, "aspect_sword");
                        }
                        if(type.getWeaponAnimationType() == EnumWeaponType.SPEAR) {
                            aspect_forge.setState(BlockEnumAspectForge.SPEAR, "aspect_spear");
                        }
                        if(type.getWeaponAnimationType() == EnumWeaponType.HEAVY_AXE) {
                            aspect_forge.setState(BlockEnumAspectForge.COLOSSAL, "aspect_colossal");
                        }

                    } else if (player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                        aspect_forge.setState(BlockEnumAspectForge.SWORD, "aspect_sword");
                    }
                    return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
                } else {
                    player.sendStatusMessage(new TextComponentTranslation("da.aspect_forge_base", new Object[0]), true);
                }
            } else {
                if(player.getHeldItemMainhand().getItem() == ModItems.ASPECT_TEMPLATE) {
                        if (aspect_forge.getState() == BlockEnumAspectForge.SHIELD) {
                            player.getHeldItem(hand).shrink(1);
                            this.spawnImpactParticle(world, new Vec3d(hitX + pos.getX(), hitY + pos.getY(), hitZ + pos.getZ()));
                            this.spawnAspectEntity(ModItems.ASPECT_SHIELD, pos, world);
                        } else if (aspect_forge.getState() == BlockEnumAspectForge.COLOSSAL) {
                            player.getHeldItem(hand).shrink(1);
                            this.spawnImpactParticle(world, new Vec3d(hitX + pos.getX(), hitY + pos.getY(), hitZ + pos.getZ()));
                            this.spawnAspectEntity(ModItems.ASPECT_COLOSSAL, pos, world);
                        } else if (aspect_forge.getState() == BlockEnumAspectForge.SPEAR) {
                            player.getHeldItem(hand).shrink(1);
                            this.spawnImpactParticle(world, new Vec3d(hitX + pos.getX(), hitY + pos.getY(), hitZ + pos.getZ()));
                            this.spawnAspectEntity(ModItems.ASPECT_SPEAR, pos, world);
                        } else if (aspect_forge.getState() == BlockEnumAspectForge.DAGGER) {
                            player.getHeldItem(hand).shrink(1);
                            this.spawnImpactParticle(world, new Vec3d(hitX + pos.getX(), hitY + pos.getY(), hitZ + pos.getZ()));
                            this.spawnAspectEntity(ModItems.ASPECT_DAGGER, pos, world);
                        } else if (aspect_forge.getState() == BlockEnumAspectForge.SWORD) {
                            player.getHeldItem(hand).shrink(1);
                            this.spawnImpactParticle(world, new Vec3d(hitX + pos.getX(), hitY + pos.getY(), hitZ + pos.getZ()));
                            this.spawnAspectEntity(ModItems.ASPECT_SWORD, pos, world);
                        } else if (aspect_forge.getState() == BlockEnumAspectForge.MAGE) {
                            player.getHeldItem(hand).shrink(1);
                            this.spawnImpactParticle(world, new Vec3d(hitX + pos.getX(), hitY + pos.getY(), hitZ + pos.getZ()));
                            this.spawnAspectEntity(ModItems.ASPECT_MAGE, pos, world);
                        } else if (aspect_forge.getState() == BlockEnumAspectForge.BOW) {
                            player.getHeldItem(hand).shrink(1);
                            this.spawnImpactParticle(world, new Vec3d(hitX + pos.getX(), hitY + pos.getY(), hitZ + pos.getZ()));
                            this.spawnAspectEntity(ModItems.ASPECT_BOW, pos, world);
                        } else if (aspect_forge.getState() == BlockEnumAspectForge.DUELIST) {
                            player.getHeldItem(hand).shrink(1);
                            this.spawnImpactParticle(world, new Vec3d(hitX + pos.getX(), hitY + pos.getY(), hitZ + pos.getZ()));
                            this.spawnAspectEntity(ModItems.ASPECT_DUELIST, pos, world);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if(worldIn.rand.nextInt(3) == 0) {
            this.spawnAmbientParticles(worldIn, new Vec3d(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D));
        }

        if(worldIn.rand.nextInt(30) == 0) {
            worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundsHandler.ASPECT_FORCE_IDLE, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
        }
    }

    private void spawnImpactParticle(World world, Vec3d pos) {
        ModUtils.performNTimes(30, (i) -> {
            Main.proxy.spawnParticle(42, world, pos.x, pos.y, pos.z, ModRand.getFloat(0.25F), 0.05 + ModRand.getFloat(0.15F), ModRand.getFloat(0.25F), 16777095);
        });
    }

    private void spawnAmbientParticles(World world, Vec3d pos) {
        Main.proxy.spawnParticle(43, world, pos.x, pos.y, pos.z, 0, 0, 0, 54527);
    }

    private void spawnAspectEntity(Item item, BlockPos pos, World world) {
        if(!world.isRemote) {
            EntityItem entityitem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, new ItemStack(item));
            entityitem.setDefaultPickupDelay();
            entityitem.motionY += 0.25F;
            entityitem.captureDrops = false;
            world.spawnEntity(entityitem);
            world.playSound((EntityPlayer)null, pos, SoundsHandler.ASPECT_FORGE_USE, SoundCategory.BLOCKS, 1F, 1.0F);
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        return EnumOffsetType.NONE;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if(worldIn.getBlockState(pos.up()).isFullBlock()) {
            worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
        }
        worldIn.removeTileEntity(pos);

    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityAspectForge)) return state;
        TileEntityAspectForge spawner = (TileEntityAspectForge) te;
        return state.withProperty(STATE, spawner.getState());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STATE);
    }

    public String byState(IBlockState state) {
        return  "aspect_forge";
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityAspectForge();
    }
}
