package com.dungeon_additions.da.entity.tileEntity;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityMegaStructure extends TileEntityStructure {
    private String name = "";
    private String author = "";
    private String metadata = "";
    private BlockPos position = new BlockPos(0, 1, 0);
    private BlockPos size = BlockPos.ORIGIN;
    private Mirror mirror = Mirror.NONE;
    private Rotation rotation = Rotation.NONE;
    private TileEntityStructure.Mode mode = TileEntityStructure.Mode.DATA;
    private boolean ignoreEntities = true;
    private boolean powered;
    private boolean showAir;
    private boolean showBoundingBox = true;
    private float integrity = 1.0F;
    private long seed;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("name", this.name);
        compound.setString("author", this.author);
        compound.setString("metadata", this.metadata);
        compound.setInteger("posX", this.position.getX());
        compound.setInteger("posY", this.position.getY());
        compound.setInteger("posZ", this.position.getZ());
        compound.setInteger("sizeX", this.size.getX());
        compound.setInteger("sizeY", this.size.getY());
        compound.setInteger("sizeZ", this.size.getZ());
        compound.setString("rotation", this.rotation.toString());
        compound.setString("mirror", this.mirror.toString());
        compound.setString("mode", this.mode.toString());
        compound.setBoolean("ignoreEntities", this.ignoreEntities);
        compound.setBoolean("powered", this.powered);
        compound.setBoolean("showair", this.showAir);
        compound.setBoolean("showboundingbox", this.showBoundingBox);
        compound.setFloat("integrity", this.integrity);
        compound.setLong("seed", this.seed);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.setName(compound.getString("name"));
        this.author = compound.getString("author");
        this.metadata = compound.getString("metadata");
        int i = MathHelper.clamp(compound.getInteger("posX"), -1000, 1000);
        int j = MathHelper.clamp(compound.getInteger("posY"), -1000, 1000);
        int k = MathHelper.clamp(compound.getInteger("posZ"), -1000, 1000);
        this.position = new BlockPos(i, j, k);
        int l = MathHelper.clamp(compound.getInteger("sizeX"), 0, 1000);
        int i1 = MathHelper.clamp(compound.getInteger("sizeY"), 0, 1000);
        int j1 = MathHelper.clamp(compound.getInteger("sizeZ"), 0, 1000);
        this.size = new BlockPos(l, i1, j1);

        try {
            this.rotation = Rotation.valueOf(compound.getString("rotation"));
        } catch (IllegalArgumentException var11) {
            this.rotation = Rotation.NONE;
        }

        try {
            this.mirror = Mirror.valueOf(compound.getString("mirror"));
        } catch (IllegalArgumentException var10) {
            this.mirror = Mirror.NONE;
        }

        try {
            this.mode = TileEntityStructure.Mode.valueOf(compound.getString("mode"));
        } catch (IllegalArgumentException var9) {
            this.mode = TileEntityStructure.Mode.DATA;
        }

        this.ignoreEntities = compound.getBoolean("ignoreEntities");
        this.powered = compound.getBoolean("powered");
        this.showAir = compound.getBoolean("showair");
        this.showBoundingBox = compound.getBoolean("showboundingbox");

        if (compound.hasKey("integrity")) {
            this.integrity = compound.getFloat("integrity");
        } else {
            this.integrity = 1.0F;
        }

        this.seed = compound.getLong("seed");
        this.updateBlockState();
    }

    private void updateBlockState() {
        if (this.world != null) {
            BlockPos blockpos = this.getPos();
            IBlockState iblockstate = this.world.getBlockState(blockpos);

            if (iblockstate.getBlock() == Blocks.STRUCTURE_BLOCK) {
                this.world.setBlockState(blockpos, iblockstate.withProperty(BlockStructure.MODE, this.mode), 2);
            }
        }
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 7, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public boolean usedBy(EntityPlayer player) {
        if (!player.canUseCommandBlock()) {
            return false;
        } else {
            if (player.getEntityWorld().isRemote) {
                player.openEditStructure(this);
            }

            return true;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String nameIn) {
        String s = nameIn;

        for (char c0 : ChatAllowedCharacters.ILLEGAL_STRUCTURE_CHARACTERS) {
            s = s.replace(c0, '_');
        }

        this.name = s;
    }

    @Override
    public void createdBy(EntityLivingBase p_189720_1_) {
        if (!StringUtils.isNullOrEmpty(p_189720_1_.getName())) {
            this.author = p_189720_1_.getName();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockPos getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(BlockPos posIn) {
        this.position = posIn;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockPos getStructureSize() {
        return this.size;
    }

    @Override
    public void setSize(BlockPos sizeIn) {
        this.size = new BlockPos(53, 19, 53);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Mirror getMirror() {
        return this.mirror;
    }

    @Override
    public void setMirror(Mirror mirrorIn) {
        this.mirror = mirrorIn;
    }

    @Override
    public void setRotation(Rotation rotationIn) {
        this.rotation = rotationIn;
    }

    @Override
    public void setMetadata(String metadataIn) {
        this.metadata = metadataIn;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Rotation getRotation() {
        return this.rotation;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getMetadata() {
        return this.metadata;
    }

    @Override
    public TileEntityStructure.Mode getMode() {
        return this.mode;
    }

    @Override
    public void setMode(TileEntityStructure.Mode modeIn) {
        this.mode = modeIn;
        IBlockState iblockstate = this.world.getBlockState(this.getPos());

        if (iblockstate.getBlock() == Blocks.STRUCTURE_BLOCK) {
            this.world.setBlockState(this.getPos(), iblockstate.withProperty(BlockStructure.MODE, modeIn), 2);
        }
    }

    @Override
    public void setIgnoresEntities(boolean ignoreEntitiesIn) {
        this.ignoreEntities = ignoreEntitiesIn;
    }

    @Override
    public void setIntegrity(float integrityIn) {
        this.integrity = integrityIn;
    }

    @Override
    public void setSeed(long seedIn) {
        this.seed = seedIn;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void nextMode() {
        switch (this.getMode()) {
            case SAVE:
                this.setMode(TileEntityStructure.Mode.LOAD);
                break;
            case LOAD:
                this.setMode(TileEntityStructure.Mode.CORNER);
                break;
            case CORNER:
                this.setMode(TileEntityStructure.Mode.DATA);
                break;
            case DATA:
                this.setMode(TileEntityStructure.Mode.SAVE);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean ignoresEntities() {
        return this.ignoreEntities;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getIntegrity() {
        return this.integrity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public long getSeed() {
        return this.seed;
    }

    @Override
    public boolean detectSize() {
        if (this.mode != TileEntityStructure.Mode.SAVE) {
            return false;
        } else {
            BlockPos blockpos = this.getPos();
            int i = 80;
            BlockPos blockpos1 = new BlockPos(blockpos.getX() - 80, 0, blockpos.getZ() - 80);
            BlockPos blockpos2 = new BlockPos(blockpos.getX() + 80, 255, blockpos.getZ() + 80);
            List<TileEntityStructure> list = this.getNearbyCornerBlocks(blockpos1, blockpos2);
            List<TileEntityStructure> list1 = this.filterRelatedCornerBlocks(list);

            if (list1.size() < 1) {
                return false;
            } else {
                StructureBoundingBox structureboundingbox = this.calculateEnclosingBoundingBox(blockpos, list1);

                if (structureboundingbox.maxX - structureboundingbox.minX > 1 && structureboundingbox.maxY - structureboundingbox.minY > 1
                        && structureboundingbox.maxZ - structureboundingbox.minZ > 1) {
                    this.position = new BlockPos(structureboundingbox.minX - blockpos.getX() + 1, structureboundingbox.minY - blockpos.getY() + 1,
                            structureboundingbox.minZ - blockpos.getZ() + 1);
                    this.size = new BlockPos(structureboundingbox.maxX - structureboundingbox.minX - 1, structureboundingbox.maxY - structureboundingbox.minY - 1,
                            structureboundingbox.maxZ - structureboundingbox.minZ - 1);
                    this.markDirty();
                    IBlockState iblockstate = this.world.getBlockState(blockpos);
                    this.world.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private List<TileEntityStructure> filterRelatedCornerBlocks(List<TileEntityStructure> p_184415_1_) {
        Iterable<TileEntityStructure> iterable = Iterables.filter(p_184415_1_, new Predicate<TileEntityStructure>() {
            @Override
            public boolean apply(@Nullable TileEntityStructure p_apply_1_) {
                if (p_apply_1_ instanceof TileEntityMegaStructure) {
                    return ((TileEntityMegaStructure) p_apply_1_).mode == TileEntityStructure.Mode.CORNER && TileEntityMegaStructure.this.name.equals(((TileEntityMegaStructure) p_apply_1_).name);
                }
                return false;
            }
        });
        return Lists.newArrayList(iterable);
    }

    private List<TileEntityStructure> getNearbyCornerBlocks(BlockPos p_184418_1_, BlockPos p_184418_2_) {
        List<TileEntityStructure> list = Lists.<TileEntityStructure>newArrayList();

        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(p_184418_1_, p_184418_2_)) {
            IBlockState iblockstate = this.world.getBlockState(blockpos$mutableblockpos);

            if (iblockstate.getBlock() == Blocks.STRUCTURE_BLOCK) {
                TileEntity tileentity = this.world.getTileEntity(blockpos$mutableblockpos);

                if (tileentity != null && tileentity instanceof TileEntityStructure) {
                    list.add((TileEntityStructure) tileentity);
                }
            }
        }

        return list;
    }

    private StructureBoundingBox calculateEnclosingBoundingBox(BlockPos p_184416_1_, List<TileEntityStructure> p_184416_2_) {
        StructureBoundingBox structureboundingbox;

        if (p_184416_2_.size() > 1) {
            BlockPos blockpos = p_184416_2_.get(0).getPos();
            structureboundingbox = new StructureBoundingBox(blockpos, blockpos);
        } else {
            structureboundingbox = new StructureBoundingBox(p_184416_1_, p_184416_1_);
        }

        for (TileEntityStructure tileentitystructure : p_184416_2_) {
            BlockPos blockpos1 = tileentitystructure.getPos();

            if (blockpos1.getX() < structureboundingbox.minX) {
                structureboundingbox.minX = blockpos1.getX();
            } else if (blockpos1.getX() > structureboundingbox.maxX) {
                structureboundingbox.maxX = blockpos1.getX();
            }

            if (blockpos1.getY() < structureboundingbox.minY) {
                structureboundingbox.minY = blockpos1.getY();
            } else if (blockpos1.getY() > structureboundingbox.maxY) {
                structureboundingbox.maxY = blockpos1.getY();
            }

            if (blockpos1.getZ() < structureboundingbox.minZ) {
                structureboundingbox.minZ = blockpos1.getZ();
            } else if (blockpos1.getZ() > structureboundingbox.maxZ) {
                structureboundingbox.maxZ = blockpos1.getZ();
            }
        }

        return structureboundingbox;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void writeCoordinates(ByteBuf buf) {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
    }

    /**
     * Saves the template, writing it to disk.
     *
     * @return true if the template was successfully saved.
     */
    @Override
    public boolean save() {
        return this.save(true);
    }

    /**
     * Saves the template, either updating the local version or writing it to disk.
     *
     * @param writeToDisk If true, {@link TemplateManager#writeTemplate} will be called with the template, and its return value will dictate the return value of this method. If false, the template will be updated
     *                    but not written to disk.
     * @return true if the template was successfully saved.
     */
    @Override
    public boolean save(boolean writeToDisk) {
        if (this.mode == TileEntityStructure.Mode.SAVE && !this.world.isRemote && !StringUtils.isNullOrEmpty(this.name)) {
            BlockPos blockpos = this.getPos().add(this.position);
            return saveStructure(writeToDisk, blockpos, this.size, this.name);

            // Split structure code into chunks
//            for (int x = 0; x < this.size.getX(); x += 16) {
//                for (int z = 0; z < this.size.getZ(); z += 16) {
//                    BlockPos pos = blockpos.add(new BlockPos(x, 0, z));
//                    saveStructure(writeToDisk, pos, new BlockPos(16, this.size.getY(), 16), this.name + "_" + (x >> 4) + "_" + (z >> 4));
//                    System.out.println((x >> 4) + " " + (z >> 4));
//                }
//            }
//            return true;
        } else {
            return false;
        }
    }

    private boolean saveStructure(boolean writeToDisk, BlockPos startPos, BlockPos size, String structureName) {
        WorldServer worldserver = (WorldServer) this.world;
        MinecraftServer minecraftserver = this.world.getMinecraftServer();
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        Template template = templatemanager.getTemplate(minecraftserver, new ResourceLocation(structureName));
        template.takeBlocksFromWorld(this.world, startPos, size, !this.ignoreEntities, Blocks.AIR);
        NBTTagCompound nbt = new NBTTagCompound();
        template.writeToNBT(nbt);
        NBTTagList blocks = nbt.getTagList("blocks", nbt.getId());
        if (blocks.isEmpty()) {
            System.out.println("Empty chunk: not saving");
            return false;
        }
        template.setAuthor(this.author);
        return !writeToDisk || templatemanager.writeTemplate(minecraftserver, new ResourceLocation(structureName));
    }

    /**
     * Loads the given template, both into this structure block and into the world, aborting if the size of the template does not match the size in this structure block.
     *
     * @return true if the template was successfully added to the world.
     */
    @Override
    public boolean load() {
        return this.load(true);
    }

    /**
     * Loads the given template, both into this structure block and into the world.
     *
     * @param requireMatchingSize If true, and the size of the loaded template does not match the size in this structure block, the structure will not be loaded into the world and false will be returned. Regardless of
     *                            the value of this parameter, the size in the structure block will be updated after calling this method.
     * @return true if the template was successfully added to the world.
     */
    @Override
    public boolean load(boolean requireMatchingSize) {
        if (this.mode == TileEntityStructure.Mode.LOAD && !this.world.isRemote && !StringUtils.isNullOrEmpty(this.name)) {
            BlockPos blockpos = this.getPos();
            BlockPos blockpos1 = blockpos.add(this.position);
            WorldServer worldserver = (WorldServer) this.world;
            MinecraftServer minecraftserver = this.world.getMinecraftServer();
            TemplateManager templatemanager = worldserver.getStructureTemplateManager();
            Template template = templatemanager.get(minecraftserver, new ResourceLocation(this.name));

            if (template == null) {
                return false;
            } else {
                if (!StringUtils.isNullOrEmpty(template.getAuthor())) {
                    this.author = template.getAuthor();
                }

                BlockPos blockpos2 = template.getSize();
                boolean flag = this.size.equals(blockpos2);

                if (!flag) {
                    this.size = blockpos2;
                    this.markDirty();
                    IBlockState iblockstate = this.world.getBlockState(blockpos);
                    this.world.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
                }

                if (requireMatchingSize && !flag) {
                    return false;
                } else {
                    PlacementSettings placementsettings = (new PlacementSettings()).setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities)
                            .setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

                    if (this.integrity < 1.0F) {
                        placementsettings.setIntegrity(MathHelper.clamp(this.integrity, 0.0F, 1.0F)).setSeed(Long.valueOf(this.seed));
                    }

                    template.addBlocksToWorldChunk(this.world, blockpos1, placementsettings);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public void unloadStructure() {
        WorldServer worldserver = (WorldServer) this.world;
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        templatemanager.remove(new ResourceLocation(this.name));
    }

    @Override
    public boolean isStructureLoadable() {
        if (this.mode == TileEntityStructure.Mode.LOAD && !this.world.isRemote) {
            WorldServer worldserver = (WorldServer) this.world;
            MinecraftServer minecraftserver = this.world.getMinecraftServer();
            TemplateManager templatemanager = worldserver.getStructureTemplateManager();
            return templatemanager.get(minecraftserver, new ResourceLocation(this.name)) != null;
        } else {
            return false;
        }
    }

    @Override
    public boolean isPowered() {
        return this.powered;
    }

    @Override
    public void setPowered(boolean poweredIn) {
        this.powered = poweredIn;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean showsAir() {
        return this.showAir;
    }

    @Override
    public void setShowAir(boolean showAirIn) {
        this.showAir = showAirIn;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean showsBoundingBox() {
        return this.showBoundingBox;
    }

    @Override
    public void setShowBoundingBox(boolean showBoundingBoxIn) {
        this.showBoundingBox = showBoundingBoxIn;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @Override
    @Nullable
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("structure_block.hover." + this.mode.getName(),
                new Object[]{this.mode == TileEntityStructure.Mode.DATA ? this.metadata : this.name});
    }

    public static enum Mode implements IStringSerializable {
        SAVE("save", 0), LOAD("load", 1), CORNER("corner", 2), DATA("data", 3);

        private static final TileEntityMegaStructure.Mode[] MODES = new TileEntityMegaStructure.Mode[values().length];
        private final String modeName;
        private final int modeId;

        private Mode(String modeNameIn, int modeIdIn) {
            this.modeName = modeNameIn;
            this.modeId = modeIdIn;
        }

        @Override
        public String getName() {
            return this.modeName;
        }

        public int getModeId() {
            return this.modeId;
        }

        public static TileEntityMegaStructure.Mode getById(int id) {
            return id >= 0 && id < MODES.length ? MODES[id] : MODES[0];
        }

        static {
            for (TileEntityMegaStructure.Mode tileentitystructure$mode : values()) {
                MODES[tileentitystructure$mode.getModeId()] = tileentitystructure$mode;
            }
        }
    }
}
