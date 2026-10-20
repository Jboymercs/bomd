package com.dungeon_additions.da.entity.dark_dungeon.boss.action;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionHorizontalSlash implements IAction {
    private float damage;
    private int range;
    public ActionHorizontalSlash(float damage, int range) {
        this.damage =damage;
        this.range = range;
    }
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d savedPos = actor.getPositionVector();
        //calls hit points all along the ground within a radius
        if(range > 16) {
            for (int t = 1; t < 15; t++) {
                ModUtils.circleCallback(t, (5 * t), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 3);
                    Vec3d finalPos = new Vec3d(pos.x, y + 1, pos.z);
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(actor).disablesShields().build();
                    ModUtils.handleAreaImpact(0.45f, (e) -> damage, actor, finalPos, source, 0.3f, 0, false, 1.1F);
                    Main.proxy.spawnParticle(24, finalPos.x, finalPos.y + 0.3, finalPos.z, 0, 0, 0, 11607828);
                });
            }
        } else {
            for (int t = 1; t < range; t++) {
                ModUtils.circleCallback(t, (5 * t), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 3);
                    Vec3d finalPos = new Vec3d(pos.x, y + 1, pos.z);
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(actor).disablesShields().build();
                    ModUtils.handleAreaImpact(0.45f, (e) -> damage, actor, finalPos, source, 0.3f, 0, false, 1.1F);
                    Main.proxy.spawnParticle(24, finalPos.x, finalPos.y + 0.45, finalPos.z, 0, 0, 0, 11607828);
                });
            }
        }
    }

    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote ) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
