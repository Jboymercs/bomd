package com.dungeon_additions.da.entity.generic;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntitySharedDesertBoss;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameBase;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;
import java.util.List;

public class EntityBlastTile extends Entity implements IEntityAdditionalSpawnData {
    public Block block;
    public int blockMeta;
    public int jumpDelay;
    public BlockPos origin;
    private double waveStartX, waveStartZ;

    private Entity ownerType;


    public EntityBlastTile(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setBlock(Blocks.STONE, 0);
        this.noClip = true;

    }

    public EntityBlastTile(World worldIn, Entity ownerType) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setBlock(Blocks.STONE, 0);
        this.noClip = true;
        this.ownerType = ownerType;
    }

    private boolean reduced;

    public EntityBlastTile(World worldIn, Entity ownerType, boolean reduced) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setBlock(Blocks.STONE, 0);
        this.noClip = true;
        this.ownerType = ownerType;
        this.reduced = reduced;
    }

    public EntityBlastTile(World worldIn, Entity ownerType, boolean reduced, float damage) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setBlock(Blocks.STONE, 0);
        this.noClip = true;
        this.ownerType = ownerType;
        this.reduced = reduced;
        this.damage = damage;
    }

    private float damage = 0;

    public EntityBlastTile(World worldIn, Entity ownerType, float damage) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setBlock(Blocks.STONE, 0);
        this.noClip = true;
        this.ownerType = ownerType;
        this.damage = damage;
    }

    private Vec3d center;

    public Entity getOwner() {
        return this.ownerType;
    }

    public void setBlock(Block blockID, int blockMeta) {
        this.block = blockID;
        this.blockMeta = blockMeta;
    }

    private int damageTime = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();

        this.world.profiler.startSection("entityBaseTick");
        damageTime--;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        if(this.ticksExisted >= this.jumpDelay) {
            if(this.ticksExisted == this.jumpDelay && this.motionY <= 0.0D) {
                this.motionY += ModRand.getFloatPositive(0.7F) + 0.25F;
                //move the tile away from center
                if(center != null) {
                    double amount = reduced ? ModRand.getFloatPositive(0.05F) + 0.05 : ModRand.getFloatPositive(0.13F) + 0.05F;
                    double d0 = (this.posX - center.x) * amount;
                    double d1 = (this.posZ - center.z) * amount;
                    this.motionX += d0;
                    this.motionZ += d1;
                }
            } else {
                this.motionY -= 0.05D;

                if(!this.world.isRemote && (this.posY <= this.origin.getY() || this.onGround || this.ticksExisted >= this.jumpDelay + 100)) {
                    this.setDead();
                }
            }
        } else {
            this.motionY = 0.0D;
            this.motionX = 0;
            this.motionZ = 0;
        }

        if(this.posY < -64.0D) {
            this.setDead();
        }

        if(this.posY + this.motionY <= this.origin.getY()) {
            this.motionY = 0.0D;
            this.setLocationAndAngles(this.posX, this.origin.getY(), this.posZ, 0, 0);
        } else {
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        }

        if(this.motionY > 0.1D && !this.world.isRemote && damageTime <= 0) {
            DamageSource damageSource;
            Entity owner = getOwner();
            if(owner instanceof EntityLivingBase) {
                if(owner instanceof EntityPlayer) {
                    damageSource = new EntityDamageSourceIndirect("player", this, owner);
                } else {
                    damageSource = DamageSource.causeIndirectDamage(this, (EntityLivingBase) owner);
                }
            } else {
                damageSource = new EntityDamageSource("da.magma_stomp", this);
            }
            List<EntityLivingBase> entities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(0.1D, 0.1D, 0.1D));
            for(EntityLivingBase entity : entities) {
                if (entity != null) {
                    if (entity instanceof EntityLivingBase && entity != getOwner() && !(entity instanceof EntityFlameBase) && !(entity instanceof EntitySharedDesertBoss) || getOwner() instanceof EntityPlayer && entity != getOwner() && entity instanceof EntityLivingBase) { // needs null check on owner?
                        if(entity.attackEntityFrom(damageSource, (damage != 0) ? damage : MobConfig.aoe_block_damage)) {
                            float knockback = 1.5F;
                            ModUtils.addFalterTooEnemies(entity, 0.25F, 8);
                            Vec3d dir = new Vec3d(this.posX - this.waveStartX, 0, this.posZ - this.waveStartZ);
                            dir = dir.normalize();
                            entity.motionX = dir.x * knockback;
                            entity.motionY = 0.5D;
                            entity.motionZ = dir.z * knockback;
                            damageTime = 20;
                        }
                    }
                }
            }
        }

        this.firstUpdate = false;
        this.world.profiler.endSection();
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return entityIn.getEntityBoundingBox();
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }


    @Override
    protected void entityInit() {

    }

    public void setOrigin(BlockPos pos, int delay, double waveStartX, double waveStartZ) {
        this.origin = pos;
        this.jumpDelay = delay;
        this.waveStartX = waveStartX;
        this.waveStartZ = waveStartZ;
    }

    public void setOriginCenter(BlockPos pos, int delay, double waveStartX, double waveStartZ, Vec3d center) {
        this.origin = pos;
        this.jumpDelay = delay;
        this.waveStartX = waveStartX;
        this.waveStartZ = waveStartZ;
        //we want a center to base the velocity on
        this.center = center;
    }


    @Override
    protected void readEntityFromNBT(NBTTagCompound data) {
        this.block = Block.getBlockById(data.getInteger("blockID"));
        if(this.block == null)
            this.block = Blocks.STONE;
        this.blockMeta = data.getInteger("blockMeta");
        this.origin = new BlockPos(data.getInteger("originX"), data.getInteger("originY"), data.getInteger("originZ"));
        this.waveStartX = data.getDouble("waveStartX");
        this.waveStartZ = data.getDouble("waveStartZ");
        this.jumpDelay = data.getInteger("jumpDelay");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound data) {
        data.setInteger("blockID", Block.getIdFromBlock(this.block));
        data.setInteger("blockMeta", this.blockMeta);
        data.setInteger("originX", this.origin.getX());
        data.setInteger("originY", this.origin.getY());
        data.setInteger("originZ", this.origin.getZ());
        data.setDouble("waveStartX", this.waveStartX);
        data.setDouble("waveStartZ", this.waveStartZ);
        data.setInteger("jumpDelay", this.jumpDelay);
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        PacketBuffer buffer = new PacketBuffer(data);
        buffer.writeInt(Block.getIdFromBlock(this.block));
        buffer.writeInt(this.blockMeta);
        buffer.writeBlockPos(this.origin);
        buffer.writeInt(this.jumpDelay);
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        PacketBuffer buffer = new PacketBuffer(data);
        this.block = Block.getBlockById(buffer.readInt());
        this.blockMeta = buffer.readInt();
        this.origin = buffer.readBlockPos();
        this.jumpDelay = buffer.readInt();
    }
}
