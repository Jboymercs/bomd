package com.dungeon_additions.da.capabilities;

import com.dungeon_additions.da.util.ModReference;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class CapabilityRohsCorruption {

    @CapabilityInject(IRohsCorruptionCapability.class)
    public static Capability<IRohsCorruptionCapability> PLAYER_ROH_CORRUPTION_CAP;
    public static final ResourceLocation ID = new ResourceLocation(ModReference.MOD_ID, "daRohsCorrupt");

    private static final String ROHS_CORRUPTION = "daRohsCorruptProg";
    private static final String ROHS_CORRUPTION_BOOL = "daRohsCorruptBool";

    public interface IRohsCorruptionCapability
    {
        boolean isCorruptionEnabled();
        void setCorruptionEnabled(boolean val);
        int getRohsCorruption();
        void setRohsCorruption(int val);
    }

    public static class DARohsCorruptionMethods implements IRohsCorruptionCapability {

        private int rohsCorruptionProgress;
        private boolean rohsCorruptionEnabled;

        @Override
        public boolean isCorruptionEnabled() {
            return rohsCorruptionEnabled;
        }

        @Override
        public void setCorruptionEnabled(boolean val) {
            rohsCorruptionEnabled = val;
        }

        @Override
        public int getRohsCorruption() {
            return rohsCorruptionProgress;
        }

        @Override
        public void setRohsCorruption(int val) {
            rohsCorruptionProgress = val;
        }
    }

    public static class Storage implements Capability.IStorage<IRohsCorruptionCapability>
    {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IRohsCorruptionCapability> capability, IRohsCorruptionCapability instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger(ROHS_CORRUPTION, instance.getRohsCorruption());
            compound.setBoolean(ROHS_CORRUPTION_BOOL, instance.isCorruptionEnabled());
            return compound;
        }

        @Override
        public void readNBT(Capability<IRohsCorruptionCapability> capability, IRohsCorruptionCapability instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            instance.setRohsCorruption(compound.getInteger(ROHS_CORRUPTION));
            instance.setCorruptionEnabled(compound.getBoolean(ROHS_CORRUPTION_BOOL));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTBase>
    {
        final Capability<IRohsCorruptionCapability> capability;
        final EnumFacing facing;
        final IRohsCorruptionCapability instance;

        public Provider(final IRohsCorruptionCapability instance, final Capability<IRohsCorruptionCapability> capability, @Nullable final EnumFacing facing)
        {
            this.instance = instance;
            this.capability = capability;
            this.facing = facing;
        }

        @Override
        public boolean hasCapability(@Nullable final Capability<?> capability, final EnumFacing facing)
        { return capability == getCapability(); }

        @Override
        public <T> T getCapability(@Nullable Capability<T> capability, EnumFacing facing)
        { return capability == getCapability() ? getCapability().cast(this.instance) : null; }

        final Capability<IRohsCorruptionCapability> getCapability()
        { return capability; }

        EnumFacing getFacing()
        { return facing; }

        final IRohsCorruptionCapability getInstance()
        { return instance; }

        @Override
        public NBTBase serializeNBT()
        { return getCapability().writeNBT(getInstance(), getFacing()); }

        @Override
        public void deserializeNBT(NBTBase nbt)
        { getCapability().readNBT(getInstance(), getFacing(), nbt); }
    }
}
