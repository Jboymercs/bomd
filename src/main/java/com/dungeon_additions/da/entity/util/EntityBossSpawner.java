package com.dungeon_additions.da.entity.util;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.blossom.EntityAbstractVoidBlossom;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.mini_blossom.EntityBlossomDart;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

public class EntityBossSpawner extends EntityAbstractBase {


    protected static final DataParameter<Float> ID_FOR = EntityDataManager.createKey(EntityBossSpawner.class, DataSerializers.FLOAT);

    public void setStateLine(float value) {
        this.dataManager.set(ID_FOR, Float.valueOf(value));
    }
    public float getStatLine() {
        return this.dataManager.get(ID_FOR);
    }

    public EntityBossSpawner(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setNoAI(true);
        this.setImmovable(true);
    }

    public EntityBossSpawner(World worldIn) {
        super(worldIn);
        this.setNoAI(true);
        this.setImmovable(true);
    }

    public EntityBossSpawner(World worldIn, int idOfBoss) {
        super(worldIn);
        this.setNoAI(true);
        this.setImmovable(true);

        this.setStateLine(idOfBoss);
    }

    public void onUpdate() {
        super.onUpdate();
        List<EntityPlayer> nearbyEntities = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable());
        if(!nearbyEntities.isEmpty() && !world.isRemote) {
            for(EntityPlayer player : nearbyEntities) {
                if(!player.isCreative() && !player.isSpectator()) {
                    //spawns the id of the respective Boss
                    if(this.getStatLine() == 1) {
                        //kills nearby mobs in the arena
                        List<EntityMob> nearbyMonsters = this.world.getEntitiesWithinAABB(EntityMob.class, this.getEntityBoundingBox().grow(20D), e -> !e.getIsInvulnerable());
                        for(EntityMob monster : nearbyMonsters) {
                            monster.setDead();
                        }
                        //spawn
                        EntityVoidBlossom blossom = new EntityVoidBlossom(world);
                        blossom.setPosition(this.posX, this.posY, this.posZ);
                        world.spawnEntity(blossom);
                        this.setDead();
                    }
                }
            }
        }
    }

    @Override
    public void entityInit() {
        this.dataManager.register(ID_FOR, 0f);
        super.entityInit();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setFloat("ID_FOR", this.getStatLine());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setStateLine(nbt.getFloat("ID_FOR"));
        super.readEntityFromNBT(nbt);
    }
    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
