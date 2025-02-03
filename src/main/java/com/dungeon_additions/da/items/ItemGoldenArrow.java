package com.dungeon_additions.da.items;

import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyArrow;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemGoldenArrow extends ItemBase{

    private String info_loc;

    public ItemGoldenArrow(String name, String info_loc, CreativeTabs tab) {
        super(name, tab);
        this.setHasSubtypes(true);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DISPENSER_BEHAVIOR);
        this.info_loc = info_loc;
    }

    public ItemGoldenArrow(String name) {
        super(name);
        this.setHasSubtypes(true);
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DISPENSER_BEHAVIOR);
    }

    public static final IBehaviorDispenseItem DISPENSER_BEHAVIOR = new BehaviorProjectileDispense()
    {
        @Override
        protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
        {
            EntitySkyArrow dart = new EntitySkyArrow(worldIn);

            if(stackIn.getItem() == ModItems.SKY_ARROW)
            {
                dart = new EntitySkyArrow(worldIn);
            }


            dart.setNoGravity(true);
            dart.pickupStatus = EntitySkyArrow.PickupStatus.DISALLOWED;
            dart.setPosition(position.getX(), position.getY(), position.getZ());

            return dart;
        }
    };

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return stack.getMetadata() == 2 ? EnumRarity.UNCOMMON : super.getRarity(stack);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

}
