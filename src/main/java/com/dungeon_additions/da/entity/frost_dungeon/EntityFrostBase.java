package com.dungeon_additions.da.entity.frost_dungeon;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityFrostBase extends EntityAbstractBase {

    public boolean setUpdateIcicles = false;

    public EntityFrostBase(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityFrostBase(World worldIn) {
        super(worldIn);
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getImmediateSource() == this) {
            return false;
        }


        return super.attackEntityFrom(source, amount);
    }


    @Override
    protected boolean canDespawn() {
        return false;
    }
}
