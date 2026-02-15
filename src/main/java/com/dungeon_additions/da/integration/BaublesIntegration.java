package com.dungeon_additions.da.integration;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import com.dungeon_additions.da.config.CompatConfig;
import com.dungeon_additions.da.items.trinket.ItemTrinket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Many thanks to UnOriginal for a baubles compatibility. This source code is taken from his project, Beast Slayer
 */
public class BaublesIntegration {

    private static boolean isBaublesLoaded;
    private static final Map<ItemTrinket.baubleSlot, BaubleType> ARTEFACT_TYPE_MAP = new EnumMap<>(ItemTrinket.baubleSlot.class);

    public static void init(){
        isBaublesLoaded = Loader.isModLoaded("baubles") && CompatConfig.baubles_compat; //I don't think baubles will change its mod ID, so Im not declaring it as a String
        if(!isEnabled()) return;
        ARTEFACT_TYPE_MAP.put(ItemTrinket.baubleSlot.CHARM, BaubleType.CHARM);
        ARTEFACT_TYPE_MAP.put(ItemTrinket.baubleSlot.TRINKET, BaubleType.BELT);
        ARTEFACT_TYPE_MAP.put(ItemTrinket.baubleSlot.AMULET, BaubleType.AMULET);

    }

    public static List<ItemTrinket> getEquippedArtifacts(EntityPlayer player, ItemTrinket.baubleSlot... types){

        List<ItemTrinket> artefacts = new ArrayList<>();
        if(isBaublesLoaded) {
            for (ItemTrinket.baubleSlot type : types) {
                for (int slot : ARTEFACT_TYPE_MAP.get(type).getValidSlots()) {
                    ItemStack stack = BaublesApi.getBaublesHandler(player).getStackInSlot(slot);
                    if (stack.getItem() instanceof ItemTrinket) artefacts.add((ItemTrinket) stack.getItem());
                }
            }
        }

        return artefacts;
    }

    public static ItemStack getArtifactItemstack(EntityPlayer player, ItemTrinket.baubleSlot... types){

        if(isBaublesLoaded) {
            for (ItemTrinket.baubleSlot type : types) {
                for (int slot : ARTEFACT_TYPE_MAP.get(type).getValidSlots()) {
                    ItemStack stack = BaublesApi.getBaublesHandler(player).getStackInSlot(slot);
                    if (stack.getItem() instanceof ItemTrinket) return stack;
                }
            }

        }

        return null;
    }

    public static boolean isEnabled(){
        return isBaublesLoaded;
    }

    @SuppressWarnings("unchecked")
    public static final class BaubleProvider implements ICapabilityProvider {

        private BaubleType type;

        public BaubleProvider(ItemTrinket.baubleSlot type){
            this.type = ARTEFACT_TYPE_MAP.get(type);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing){
            return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;
        }

        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing){
            // This lambda expression is an implementation of the entire IBauble interface
            return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE ? (T)(IBauble) itemStack -> type : null;
        }

    }


}
