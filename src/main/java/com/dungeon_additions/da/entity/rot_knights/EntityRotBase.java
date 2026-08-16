package com.dungeon_additions.da.entity.rot_knights;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityRotBase extends EntityAbstractBase {

    public EntityRotBase(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityRotBase(World world, int timesUsed, BlockPos pos) {
        super(world, timesUsed, pos);
    }

    public EntityRotBase(World worldIn) {
        super(worldIn);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!world.isRemote) {
            if(ticksExisted % 10 == 0) {
                if(this.isPotionActive(MobEffects.POISON)) {
                    this.removePotionEffect(MobEffects.POISON);
                }
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getImmediateSource() instanceof EntityRotBase) {
            return false;
        }


        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
