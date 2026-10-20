package com.dungeon_additions.da.entity.ai.dark_dungeon;

import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.dark_dungeon.EntityBrineCreep;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class BrineCreepPassiveCircle extends EntityAIBase {

    protected final EntityBrineCreep entity;
    protected int cooldown;
    protected float heightAboveGround;

    protected Vec3d posTo;

    public BrineCreepPassiveCircle(EntityBrineCreep entityIn, int cooldown, float heightAboveGround) {
        this.entity = entityIn;
        this.cooldown = cooldown;
        this.heightAboveGround = heightAboveGround;
    }

    @Override
    public boolean shouldExecute() {
        return entity.getAttackTarget() != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if(entity.getAttackTarget() == null) {
            return false;
        }
        return true;
    }

    @Nullable
    protected Vec3d getPosition(EntityLivingBase target) {
        Vec3d groupCenter = target.getPositionVector();

        if(this.entity.getDistance(target) > 20) {
            BlockPos posFrom = target.getPosition().add(ModRand.range(1, 5) * ModRand.randSign(), ModRand.range(1, 3), ModRand.range(1, 5) * ModRand.randSign());
            return new Vec3d(posFrom.getX(), posFrom.getY() + 1, posFrom.getZ());
        }
        for (int i = 0; i < 10; i++) {
            int minRange = 10;
            int maxRange = 20;
            Vec3d pos = groupCenter.add(new Vec3d(ModRand.range(minRange, maxRange) * ModRand.randSign(), 0, ModRand.range(minRange, maxRange) * ModRand.randSign()));
            pos = new Vec3d(pos.x, ModUtils.getSurfaceHeightGeneral(entity.world, new BlockPos(pos), (int) entity.posY - 40, (int) entity.posY + 30), pos.z);
            pos.add(0, heightAboveGround + ModRand.range(-10, 4), 0);
            RayTraceResult raytraceresult = entity.world.rayTraceBlocks(this.entity.getPositionVector(), pos, true, true, false);
            if(raytraceresult != null && i == 9) {
                posTo = raytraceresult.hitVec;
            } else {
                return posTo = pos;
            }

        }

        return posTo = null;

    }

    @Override
    public void updateTask() {
        if(entity.getAttackTarget() == null) {
            return;
        }
        EntityLivingBase target = this.entity.getAttackTarget();
        Vec3d vec3d;
        if(posTo == null) {
            posTo = getPosition(target);
        } else if (this.entity.getDistance(target) < 10 || this.entity.getDistance(target) > 30) {
            posTo = getPosition(target);
        } else if (this.entity.getDistance(posTo.x, posTo.y, posTo.z) <= 3) {
            posTo = getPosition(target);
            ModUtils.setEntityVelocity(this.entity, new Vec3d(0,0,0));
        }

        vec3d = posTo;

        if (vec3d != null) {
            double speed = entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue();
            Vec3d direction = vec3d.subtract(entity.getPositionVector()).normalize();
            if(this.entity.facingTarget) {
                ModUtils.facePosition(target.getPositionEyes(1F), entity, 35, 35);
                Vec3d targetPos = target.getPositionEyes(1);
                Vec3d entityPos = this.entity.getPositionEyes(1);
                Vec3d forwardVec = ModUtils.direction(entityPos, targetPos);
                ((IPitch) this.entity).setPitch(forwardVec);
            } else {
                ModUtils.facePosition(vec3d, entity, 35, 35);
                Vec3d entityPos = this.entity.getPositionEyes(1);
                Vec3d forwardVec = ModUtils.direction(entityPos, vec3d);
                ((IPitch) this.entity).setPitch(forwardVec);
            }
            ModUtils.addEntityVelocity(entity, direction.scale(this.entity.facingTarget ? 0.05F * speed: 0.1F * speed));
            //move towards
        }
    }

}
