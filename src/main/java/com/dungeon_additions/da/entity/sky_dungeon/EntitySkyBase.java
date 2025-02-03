package com.dungeon_additions.da.entity.sky_dungeon;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntitySkyBase extends EntityAbstractBase {
    public EntitySkyBase(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntitySkyBase(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getImmediateSource() instanceof EntitySkyBase) {
            return false;
        }


        return super.attackEntityFrom(source, amount);
    }


    @Override
    protected boolean canDespawn() {
        return false;
    }
}
