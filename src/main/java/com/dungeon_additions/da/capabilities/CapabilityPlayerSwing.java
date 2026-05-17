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

public class CapabilityPlayerSwing {

    @CapabilityInject(CapabilityPlayerSwing.IPlayerSwingCapability.class)
    public static Capability<CapabilityPlayerSwing.IPlayerSwingCapability> PLAYER_SWING_CAP;

    public static final ResourceLocation ID = new ResourceLocation(ModReference.MOD_ID, "daPlayerSwingCustom");
    private static final String PLAYER_SWING_CUSTOM = "daPlayerSwingProgress";
    private static final String PLAYER_SWING_CUSTOM_BOOL = "daPlayerSwingBoolean";

    public interface IPlayerSwingCapability
    {
        boolean isSwingEnabled();
        void setSwingEnabled(boolean val);
        int getPlayerSwingProgress();
        void setPlayerSwingProgress(int val);
    }

    public static class DAPlayerSwingMethods implements CapabilityPlayerSwing.IPlayerSwingCapability {

        private int playerSwingCurrent;
        private boolean playerSwingEnabled;

        @Override
        public boolean isSwingEnabled() {
            return this.playerSwingEnabled;
        }

        @Override
        public void setSwingEnabled(boolean val) {
            this.playerSwingEnabled = val;
        }

        @Override
        public int getPlayerSwingProgress() {
            return this.playerSwingCurrent;
        }

        @Override
        public void setPlayerSwingProgress(int val) {
            this.playerSwingCurrent = val;
        }
    }

    public static class Storage implements Capability.IStorage<CapabilityPlayerSwing.IPlayerSwingCapability> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<CapabilityPlayerSwing.IPlayerSwingCapability> capability, CapabilityPlayerSwing.IPlayerSwingCapability instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger(PLAYER_SWING_CUSTOM, instance.getPlayerSwingProgress());
            compound.setBoolean(PLAYER_SWING_CUSTOM_BOOL, instance.isSwingEnabled());
            return compound;
        }

        @Override
        public void readNBT(Capability<CapabilityPlayerSwing.IPlayerSwingCapability> capability, CapabilityPlayerSwing.IPlayerSwingCapability instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            instance.setPlayerSwingProgress(compound.getInteger(PLAYER_SWING_CUSTOM));
            instance.setSwingEnabled(compound.getBoolean(PLAYER_SWING_CUSTOM_BOOL));
        }
    }

        public static class Provider implements ICapabilitySerializable<NBTBase>
        {
            final Capability<CapabilityPlayerSwing.IPlayerSwingCapability> capability;
            final EnumFacing facing;
            final CapabilityPlayerSwing.IPlayerSwingCapability instance;

            public Provider(final CapabilityPlayerSwing.IPlayerSwingCapability instance, final Capability<CapabilityPlayerSwing.IPlayerSwingCapability> capability, @Nullable final EnumFacing facing)
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

            final Capability<CapabilityPlayerSwing.IPlayerSwingCapability> getCapability()
            { return capability; }

            EnumFacing getFacing()
            { return facing; }

            final CapabilityPlayerSwing.IPlayerSwingCapability getInstance()
            { return instance; }

            @Override
            public NBTBase serializeNBT()
            { return getCapability().writeNBT(getInstance(), getFacing()); }

            @Override
            public void deserializeNBT(NBTBase nbt)
            { getCapability().readNBT(getInstance(), getFacing(), nbt); }
        }

}
