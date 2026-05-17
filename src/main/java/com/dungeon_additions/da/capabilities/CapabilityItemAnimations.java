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

public class CapabilityItemAnimations {
    @CapabilityInject(ICapabilityItemAnimations.class)
    public static Capability<ICapabilityItemAnimations> ANIM_CAP;
    public static final ResourceLocation ID = new ResourceLocation(ModReference.MOD_ID, "animations");

    /** Timings for the Custom Swing Animations. */
    private static final String CUSTOM_SWING_STARTTICK_TAG = "customItemSwingStartTime";
    private static final String CUSTOM_SWING_ENDTICK_TAG = "customItemSwingEndTime";
    private static final String PARRY_SWING_STARTTICK_TAG = "parryItemStartTime";
    private static final String PARRY_SWING_ENDTICK_TAG = "parryItemEndTime";

    public interface ICapabilityItemAnimations
    {
        int getCustomSwingStartTime();
        void setCustomSwingStartTime(int value);

        int getCustomSwingEndTime();
        void setCustomSwingEndTime(int value);

        int getParryStartTime();
        void setParryStartTime(int value);

        int getParryEndTime();
        void setParryEndTime(int value);
    }

    public static class AnimationMethods implements ICapabilityItemAnimations
    {
        private int customSwingStartTick = 0;
        private int customSwingEndTick = 0;
        private int parryStartTick = 0;
        private int parryEndTick = 0;

        @Override
        public int getCustomSwingStartTime() { return customSwingStartTick; }
        @Override
        public void setCustomSwingStartTime(int value) { customSwingStartTick = value; }

        @Override
        public int getCustomSwingEndTime() { return customSwingEndTick; }
        @Override
        public void setCustomSwingEndTime(int value) { customSwingEndTick = value; }

        @Override
        public int getParryStartTime() {
            return parryStartTick;
        }

        @Override
        public void setParryStartTime(int value) {
                parryStartTick = value;
        }

        @Override
        public int getParryEndTime() {
            return parryEndTick;
        }

        @Override
        public void setParryEndTime(int value) {
            parryEndTick = value;
        }
    }

    public static class Storage implements Capability.IStorage<ICapabilityItemAnimations>
    {
        @Override
        public NBTBase writeNBT(Capability<ICapabilityItemAnimations> capability, ICapabilityItemAnimations instance, EnumFacing side)
        {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger(CUSTOM_SWING_STARTTICK_TAG, instance.getCustomSwingStartTime());
            compound.setInteger(CUSTOM_SWING_ENDTICK_TAG, instance.getCustomSwingEndTime());
            compound.setInteger(PARRY_SWING_STARTTICK_TAG, instance.getParryStartTime());
            compound.setInteger(PARRY_SWING_ENDTICK_TAG, instance.getParryEndTime());
            return compound;
        }

        @Override
        public void readNBT(Capability<ICapabilityItemAnimations> capability, ICapabilityItemAnimations instance, EnumFacing side, NBTBase nbt)
        {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            instance.setCustomSwingStartTime(compound.getInteger(CUSTOM_SWING_STARTTICK_TAG));
            instance.setCustomSwingEndTime(compound.getInteger(CUSTOM_SWING_ENDTICK_TAG));
            instance.setParryStartTime(compound.getInteger(PARRY_SWING_STARTTICK_TAG));
            instance.setParryEndTime(compound.getInteger(PARRY_SWING_ENDTICK_TAG));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTBase>
    {
        final Capability<ICapabilityItemAnimations> capability;
        final EnumFacing facing;
        final ICapabilityItemAnimations instance;

        public Provider(final ICapabilityItemAnimations instance, final Capability<ICapabilityItemAnimations> capability, @Nullable final EnumFacing facing)
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

        final Capability<ICapabilityItemAnimations> getCapability()
        { return capability; }

        EnumFacing getFacing()
        { return facing; }

        final ICapabilityItemAnimations getInstance()
        { return instance; }

        @Override
        public NBTBase serializeNBT()
        { return getCapability().writeNBT(getInstance(), getFacing()); }

        @Override
        public void deserializeNBT(NBTBase nbt)
        { getCapability().readNBT(getInstance(), getFacing(), nbt); }
    }
}
