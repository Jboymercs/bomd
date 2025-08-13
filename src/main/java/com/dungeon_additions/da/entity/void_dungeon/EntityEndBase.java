package com.dungeon_additions.da.entity.void_dungeon;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.desert_dungeon.EntityDesertBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityEndBase extends EntityAbstractBase {
    public EntityEndBase(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityEndBase(World world, int timesUsed, BlockPos pos) {
        super(world, timesUsed, pos);
    }

    public EntityEndBase(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getImmediateSource() instanceof EntityEndBase) {
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
