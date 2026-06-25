package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.dark_dungeon.dark_void.EntityDarkVoid;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityDarkBase extends EntityAbstractBase {

    private static final DataParameter<Boolean> SPAWN_OVERRIDE = EntityDataManager.createKey(EntityDarkBase.class, DataSerializers.BOOLEAN);

    public void setSpawnOverride(boolean value) {this.dataManager.set(SPAWN_OVERRIDE, Boolean.valueOf(value));}
    public boolean isSpawnOverride() {return this.dataManager.get(SPAWN_OVERRIDE);}

    public EntityDarkBase(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityDarkBase(World worldIn) {
        super(worldIn);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Spawn_O", this.isSpawnOverride());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSpawnOverride(nbt.getBoolean("Spawn_O"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(SPAWN_OVERRIDE, Boolean.valueOf(false));
        super.entityInit();
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
