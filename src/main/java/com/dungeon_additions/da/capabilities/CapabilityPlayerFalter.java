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

public class CapabilityPlayerFalter {
    @CapabilityInject(IPlayerFalterCapability.class)
    public static Capability<IPlayerFalterCapability> PLAYER_FALTER_CAP;
    public static final ResourceLocation ID = new ResourceLocation(ModReference.MOD_ID, "daPlayerFalter");

    private static final String PLAYER_FALTER = "daPlayerFalterProgress";
    private static final String PLAYER_FALTER_BOOL = "daPlayerFalterBoolean";

    public interface IPlayerFalterCapability
    {
        boolean isFalterEnabled();
        void setFalterEnabled(boolean val);
        float getPlayerFalterProgress();
        void setPlayerFalterProgress(float val);
    }

    public static class DAPlayerFalterMethods implements IPlayerFalterCapability {

        private float playerFalterCurrent;
        private boolean playerFalterEnabled;

        @Override
        public boolean isFalterEnabled() {
            return playerFalterEnabled;
        }

        @Override
        public void setFalterEnabled(boolean val) {
            playerFalterEnabled = val;
        }

        @Override
        public float getPlayerFalterProgress() {
            return playerFalterCurrent;
        }

        @Override
        public void setPlayerFalterProgress(float val) {
            playerFalterCurrent = val;
        }
    }

    public static class Storage implements Capability.IStorage<IPlayerFalterCapability>
    {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IPlayerFalterCapability> capability, IPlayerFalterCapability instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setFloat(PLAYER_FALTER, instance.getPlayerFalterProgress());
            compound.setBoolean(PLAYER_FALTER, instance.isFalterEnabled());
            return compound;
        }

        @Override
        public void readNBT(Capability<IPlayerFalterCapability> capability, IPlayerFalterCapability instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            instance.setPlayerFalterProgress(compound.getFloat(PLAYER_FALTER));
            instance.setFalterEnabled(compound.getBoolean(PLAYER_FALTER_BOOL));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTBase>
    {
        final Capability<IPlayerFalterCapability> capability;
        final EnumFacing facing;
        final IPlayerFalterCapability instance;

        public Provider(final IPlayerFalterCapability instance, final Capability<IPlayerFalterCapability> capability, @Nullable final EnumFacing facing)
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

        final Capability<IPlayerFalterCapability> getCapability()
        { return capability; }

        EnumFacing getFacing()
        { return facing; }

        final IPlayerFalterCapability getInstance()
        { return instance; }

        @Override
        public NBTBase serializeNBT()
        { return getCapability().writeNBT(getInstance(), getFacing()); }

        @Override
        public void deserializeNBT(NBTBase nbt)
        { getCapability().readNBT(getInstance(), getFacing(), nbt); }
    }
}
