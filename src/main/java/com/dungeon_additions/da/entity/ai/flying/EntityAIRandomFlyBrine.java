package com.dungeon_additions.da.entity.ai.flying;

import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.dark_dungeon.EntityBrineCreep;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class EntityAIRandomFlyBrine extends EntityAIBase {
    protected final EntityBrineCreep entity;
    protected int cooldown;
    protected float heightAboveGround;

    protected Vec3d posTo;

    public EntityAIRandomFlyBrine(EntityBrineCreep entityIn, int cooldown, float heightAboveGround) {
        this.entity = entityIn;
        this.cooldown = cooldown;
        this.heightAboveGround = heightAboveGround;
    }

    @Override
    public boolean shouldExecute() {
        return entity.getAttackTarget() == null && this.entity.ticksExisted % cooldown != 0;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if(entity.getAttackTarget() != null) {
            return false;
        }

        if(posTo == null) {
            posTo = this.getPosition();
        }
        assert posTo != null;
        if(this.entity.getDistance(posTo.x, posTo.y, posTo.z) > 3) {
            return true;
        } else {
            posTo = null;
            return false;
        }
    }

    @Nullable
    protected Vec3d getPosition() {
        Vec3d groupCenter = this.entity.getPositionVector();

        if(this.entity.getDistanceSq(this.entity.getSpawnLocation()) > 1000) {
            BlockPos posFrom = this.entity.getSpawnLocation();
            return new Vec3d(posFrom.getX(), posFrom.getY() + 1, posFrom.getZ());
        }
        for (int i = 0; i < 10; i++) {
            int minRange = 5;
            int maxRange = 20;
            Vec3d pos = groupCenter.add(new Vec3d(ModRand.range(minRange, maxRange) * ModRand.randSign(), 0, ModRand.range(minRange, maxRange) * ModRand.randSign()));
            pos = new Vec3d(pos.x, ModUtils.getSurfaceHeightGeneral(entity.world, new BlockPos(pos), (int) entity.posY - 40, (int) entity.posY + 30), pos.z);
            if(entity.world.rand.nextInt(5) == 0) {
                pos.add(0, heightAboveGround + 20, 0);
            } else {
                pos.add(0, heightAboveGround + ModRand.range(-10, 12), 0);
            }
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


        Vec3d vec3d = posTo;


        if (vec3d != null) {

            if(entity.getDistance(vec3d.x, vec3d.y, vec3d.z) <= 3) {
                ModUtils.setEntityVelocity(this.entity, new Vec3d(0,0,0));
                return;
            }

            double speed = entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue();
            Vec3d direction = vec3d.subtract(entity.getPositionVector()).normalize();
            ModUtils.addEntityVelocity(entity, direction.scale(0.07f * speed));
            ModUtils.facePosition(vec3d, entity, 35, 35);
            Vec3d entityPos = this.entity.getPositionEyes(1);
            Vec3d forwardVec = ModUtils.direction(entityPos, vec3d);
            ((IPitch) this.entity).setPitch(forwardVec);
            //move towards
        }
    }
}
