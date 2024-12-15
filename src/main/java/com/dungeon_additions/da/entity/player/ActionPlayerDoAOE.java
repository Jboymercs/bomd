package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.night_lich.EntityLichStaffAOE;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionPlayerDoAOE implements IActionPlayer{

    private final int lengthOfAOE;

    public ActionPlayerDoAOE(int lengthOfAOE) {
        this.lengthOfAOE = lengthOfAOE;
    }
    @Override
    public void performAction(EntityPlayer actor) {
        Vec3d savedPos = actor.getPositionVector();
        //Now we need to make this in a loop relative
        double additionalDamage = 0.45;
        if(actor.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NIGHT_LICH_HELMET) {
            additionalDamage = 0.7;
        }

        for(int t = 1; t < lengthOfAOE; t++ ) {

            double finalAdditionalDamage = additionalDamage;
            ModUtils.circleCallback(t, (4 * t), (pos) -> {
                pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                EntityLichStaffAOE tile = new EntityLichStaffAOE(actor.world, actor, (float) (MobConfig.night_lich_attack_damage * finalAdditionalDamage));
                tile.setPosition(pos.x, pos.y, pos.z);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 4);
                BlockPos posToo = new BlockPos(pos.x, y + 1, pos.z);
                tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                actor.world.spawnEntity(tile);
            });

        }
    }

    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
