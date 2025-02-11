package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityDarkBase extends EntityAbstractBase {


    public EntityDarkBase(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityDarkBase(World worldIn) {
        super(worldIn);
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getImmediateSource() instanceof EntityDarkBase) {
            return false;
        }


        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
