package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.desert_dungeon.EntityDesertBase;
import com.dungeon_additions.da.entity.frost_dungeon.IDirectionalRender;
import com.dungeon_additions.da.packets.MessageDirectionForRender;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityDelayedLazer extends Entity implements IDirectionalRender {

    public int delayedLazer;
    private Vec3d renderLazerPos;
    private float damage;
    private EntityLivingBase owner;
    public static final int TICK_LIFE = 20;
    private EntityLivingBase targetIn;

    public EntityDelayedLazer(World worldIn) {
        super(worldIn);
        this.noClip = true;
        this.setNoGravity(true);
        this.setSize(0.5F, 0.5F);
    }

    public EntityDelayedLazer(World worldIn, int delayedLazer, Vec3d renderLazerPos, EntityLivingBase owner, float damage, EntityLivingBase targetIn) {
        super(worldIn);
        this.delayedLazer = delayedLazer;
        this.renderLazerPos = renderLazerPos;
        this.setSize(0.5F, 0.5F);
        this.owner = owner;
        this.damage = damage;
        this.noClip = true;
        this.setNoGravity(true);
        this.targetIn = targetIn;
    }

    @Override
    public void onUpdate() {
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;

        if(!world.isRemote && renderLazerPos != null) {

            if(ticksExisted == 1) {
                //spawn the signal particle
                Main.proxy.spawnParticle(38, world, this.posX, this.posY, this.posZ, 0,0,0, this.getEntityId());
                this.playSound(SoundsHandler.DAUNTLESS_DELAYED_LAZER, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.6f));
            }
            if(ticksExisted == delayedLazer - 5 && targetIn != null) {
               renderLazerPos = targetIn.getPositionVector().add(0, 0.5, 0);
            }
            if(ticksExisted > delayedLazer) {
                Main.network.sendToAllTracking(new MessageDirectionForRender(this, renderLazerPos), this);
            }

            if(ticksExisted == delayedLazer && owner != null && this.damage != 0) {
                Main.proxy.spawnParticle(35, world, renderLazerPos.x, renderLazerPos.y + 0.1F, renderLazerPos.z, 0,0,0, 16);
                if(owner != null) {
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(owner).disablesShields().build();
                    float damage = (float) (this.damage);
                    ModUtils.handleAreaImpact(0.25f, (e) -> damage, owner, this.renderLazerPos, source, 0.1f, 0);
                    world.playSound((EntityPlayer) null, renderLazerPos.x, renderLazerPos.y, renderLazerPos.z, SoundsHandler.DAUNTLESS_LESS_AOE_EXPLODE, SoundCategory.NEUTRAL, 0.7f, 0.7f / (world.rand.nextFloat() * 0.4F + 0.2f));
                    //detect entities in a line and damage them too
                    Vec3d posSet = renderLazerPos.subtract(this.getPositionVector()).normalize();
                    Vec3d targetedPos = this.renderLazerPos.add(posSet.scale(10));
                    this.detectEntitiesInline(this.getPositionVector(), targetedPos);
                }
            }
        }

        if (this.ticksExisted > TICK_LIFE + delayedLazer) {
            this.setDead();
        }
        super.onUpdate();
    }

    private void detectEntitiesInline(Vec3d start, Vec3d end) {
        Vec3d lazerEnd = end;
        RayTraceResult raytraceresult = world.rayTraceBlocks(start, lazerEnd, false, true, false);

        if (raytraceresult != null) {
            // If we hit a block, make sure that any collisions with entities are detected up to the hit block
            lazerEnd = raytraceresult.hitVec;
        }

        Entity closestEntity = null;
        for (Entity entity : ModUtils.findEntitiesInLine(start, lazerEnd, world, this)) {
            if (entity.canBeCollidedWith() && (closestEntity == null || entity.getDistanceSq(this) < closestEntity.getDistanceSq(this))) {
                if(!(entity instanceof EntityDarkBase)) {
                    closestEntity = entity;
                }
            }

        }

        if (closestEntity != null) {
            if (closestEntity instanceof IEntityMultiPart) {
                if(closestEntity.getParts() != null) {
                    MultiPartEntityPart closestPart = null;
                    for (Entity entity : closestEntity.getParts()) {
                        RayTraceResult result = entity.getEntityBoundingBox().calculateIntercept(this.getPositionEyes(1), lazerEnd);
                        if (result != null) {
                            if (entity instanceof MultiPartEntityPart && (closestPart == null || entity.getDistanceSq(this) < closestPart.getDistanceSq(this))) {
                                closestPart = (MultiPartEntityPart) entity;
                            }
                        }
                    }
                    if (closestPart != null) {
                        ((IEntityMultiPart) closestEntity).attackEntityFromPart(closestPart, ModUtils.causeStaffDamage(this), (float) (this.damage));
                    }
                }
            } else {
                closestEntity.attackEntityFrom(ModUtils.causeStaffDamage(this), (float) (this.damage));
            }
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    public float getEyeHeight() {
        return 0;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public void setRenderDirection(Vec3d dir) {
        this.renderLazerPos = dir;
    }

    public Vec3d getRenderDirection() {
        return this.renderLazerPos;
    }
}
