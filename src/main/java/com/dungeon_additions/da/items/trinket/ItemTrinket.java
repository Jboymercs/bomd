package com.dungeon_additions.da.items.trinket;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.integration.BaublesIntegration;
import com.dungeon_additions.da.items.ItemBase;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTrinket extends ItemBase {
    private String info_loc;

    private final baubleSlot baubleSlot;

    public enum baubleSlot {
        CHARM(1),
        TRINKET(1),
        AMULET(1);
        baubleSlot(int max){

        }
    }

    public ItemTrinket(String name, String info_loc, int maxDamage, baubleSlot slot) {
        super(name);
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        this.info_loc = info_loc;
        this.maxStackSize = 1;
        this.setMaxDamage(maxDamage);
        baubleSlot = slot;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt){
        return BaublesIntegration.isEnabled() ? new BaublesIntegration.BaubleProvider(baubleSlot) : null;
    }



    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
            tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
            if(!BaublesIntegration.isEnabled()) {
                tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.trinket_item.name"));
            }
    }
}
