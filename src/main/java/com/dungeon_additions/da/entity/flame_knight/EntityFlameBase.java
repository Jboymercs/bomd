package com.dungeon_additions.da.entity.flame_knight;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityFlameBase extends EntityAbstractBase {
    public EntityFlameBase(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityFlameBase(World worldIn) {
        super(worldIn);
    }
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getImmediateSource() instanceof EntityFlameBase) {
                return false;
        }


        return super.attackEntityFrom(source, amount);
    }


    @Override
    protected boolean canDespawn() {
        return false;
    }
}
