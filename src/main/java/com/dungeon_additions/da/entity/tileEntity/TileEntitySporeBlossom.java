package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.blocks.BlockSporeBlossom;
import com.dungeon_additions.da.entity.blossom.EntityAbstractVoidBlossom;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntitySporeBlossom extends TileEntity implements ITickable {



    @Override
    public void update() {
        if(!this.world.isRemote) {

            AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            List<EntityAbstractVoidBlossom> nearbyBoss = this.world.getEntitiesWithinAABB(EntityAbstractVoidBlossom.class, box.grow(25D), e -> !e.getIsInvulnerable());
            if (!nearbyBoss.isEmpty()) {
                for (EntityAbstractVoidBlossom blossom : nearbyBoss) {
                    double health = blossom.getHealth() / blossom.getMaxHealth();
                    EntityLivingBase target = blossom.getAttackTarget();
                    if (health < blossom.getStatLine() && target != null) {
                        blossom.heal(1.0f);
                    }

                }
            }
        }
    }
}
