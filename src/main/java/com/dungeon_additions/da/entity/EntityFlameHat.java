package com.dungeon_additions.da.entity;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityFlameHat extends EntityAbstractBase implements IAnimatable, IAnimationTickable {

    private EntityFlameKnight owner;
    private UUID ownerUuid;

    public EntityFlameHat(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.5F, 0.5F);
    }

    public EntityFlameHat(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
        this.setNoGravity(true);
    }

    public EntityFlameHat(World worldIn, EntityFlameKnight owner) {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
        this.setOwnerWithHead(owner);
        this.setNoGravity(true);
        this.setPosition(owner.posX, owner.posY + 2.2, owner.posZ);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    public void onUpdate() {
        super.onUpdate();

        if(owner != null) {
            if(owner.isLostHead()) {
                this.setPosition(owner.posX, owner.posY + 2.2, owner.posZ);

            }
            else if( !world.isRemote) {
                this.setDead();
            }
        }

    }
    public void setOwnerWithHead(@Nullable EntityFlameKnight owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUniqueID();
    }


    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasUniqueId("owner")){
            this.ownerUuid = compound.getUniqueId("owner");
        }
    }
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        if(this.ownerUuid != null) {
            compound.setUniqueId("owner", this.ownerUuid);
        }

    }


    @Override
    public AnimationFactory getFactory() {
        return null;
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
