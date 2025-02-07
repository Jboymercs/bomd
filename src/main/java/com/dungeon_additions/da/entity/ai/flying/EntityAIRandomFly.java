package com.dungeon_additions.da.entity.ai.flying;

import com.dungeon_additions.da.entity.ai.sky_dungeon.EntityGargoyleTridentAI;
import com.dungeon_additions.da.entity.sky_dungeon.EntityTridentGargoyle;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Consumer;

public class EntityAIRandomFly extends EntityAIBase {
    protected final EntityTridentGargoyle entity;
    protected int cooldown;
    protected float heightAboveGround;

    protected Vec3d posTo;

    public EntityAIRandomFly(EntityTridentGargoyle entityIn, int cooldown, float heightAboveGround) {
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

        if(this.entity.getDistanceSq(this.entity.getSpawnLocation()) > 625) {
            BlockPos posFrom = this.entity.getSpawnLocation();
            return new Vec3d(posFrom.getX(), posFrom.getY() + 1, posFrom.getZ());
        }
        for (int i = 0; i < 10; i++) {
            int minRange = 5;
            int maxRange = 15;
            Vec3d pos = groupCenter.add(new Vec3d(ModRand.range(minRange, maxRange) * ModRand.randSign(), 0, ModRand.range(minRange, maxRange) * ModRand.randSign()));
            pos = new Vec3d(pos.x, ModUtils.getSurfaceHeightGeneral(entity.world, new BlockPos(pos), (int) entity.posY - 10, (int) entity.posY + 30), pos.z);


                return posTo = pos.add(0, heightAboveGround + ModRand.range(-3, 3), 0);

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
            ModUtils.addEntityVelocity(entity, direction.scale(0.04f * speed));
            ModUtils.facePosition(vec3d, entity, 35, 35);
            //move towards
        }
    }
}
