package com.dungeon_additions.da.entity.desert_dungeon;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityDesertBase extends EntityAbstractBase {
    public EntityDesertBase(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityDesertBase(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getImmediateSource() instanceof EntityDesertBase) {
            return false;
        }


        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
