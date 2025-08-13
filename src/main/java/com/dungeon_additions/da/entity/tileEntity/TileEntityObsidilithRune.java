package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.void_dungeon.EntityObsidilith;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class TileEntityObsidilithRune extends TileEntity implements ITickable {
    @Override
    public void update() {
        if(!this.world.isRemote) {

            AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            List<EntityObsidilith> nearbyBoss = this.world.getEntitiesWithinAABB(EntityObsidilith.class, box.grow(25D), e -> !e.getIsInvulnerable());
            if (!nearbyBoss.isEmpty()) {
                for (EntityObsidilith blossom : nearbyBoss) {
                    double health = blossom.getHealth() / blossom.getMaxHealth();
                    EntityLivingBase target = blossom.getAttackTarget();
                    if (health < blossom.getStatLine() && target != null) {
                        blossom.heal(1.0f);
                    }

                    if(blossom.ticksExisted % 100 == 0) {
                        Vec3d currPos = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                        Vec3d blossomPos = blossom.getPositionVector().add(0, 2, 0);
                        ModUtils.lineCallback(currPos, blossomPos,(int) (blossom.getDistance(currPos.x, currPos.y, currPos.z) * 1.5), (particlePos, j) -> {
                            blossom.addEvent(()-> {
                                Main.proxy.spawnParticle(7, particlePos.x, particlePos.y + (j * 0.1), particlePos.z, 0, 0, 0);
                            }, j * 3);
                        });
                    }

                }
            }
        }
    }
}
