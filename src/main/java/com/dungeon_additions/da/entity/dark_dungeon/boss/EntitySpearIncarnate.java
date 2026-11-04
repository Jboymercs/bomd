package com.dungeon_additions.da.entity.dark_dungeon.boss;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.dark_dungeon.boss.util.RohUtils;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntitySpearIncarnate extends EntityDarkBase {

    private final String ANIM_SUMMON = "shoot";
    private final String ANIM_HOLD = "hold";
    private final String ANIM_END = "end";
    private float damage = 10;
    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);
    private static final DataParameter<Boolean> HAS_PLAYER = EntityDataManager.createKey(EntitySpearIncarnate.class, DataSerializers.BOOLEAN);
    private void setHasPlayer(boolean value) {this.dataManager.set(HAS_PLAYER, Boolean.valueOf(value));}
    public boolean isHasPlayer() {return this.dataManager.get(HAS_PLAYER);}

    public EntitySpearIncarnate(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.6F, 3.0F);
        this.setNoGravity(true);
        this.setImmovable(true);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setNoAI(true);
    }

    public EntitySpearIncarnate(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 3.0F);
        this.setNoGravity(true);
        this.setImmovable(true);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setNoAI(true);
    }

    public EntitySpearIncarnate(World worldIn, float damage) {
        super(worldIn);
        this.setSize(0.6F, 3.0F);
        this.setNoGravity(true);
        this.setImmovable(true);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setNoAI(true);
        this.damage = damage;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Has_Player", this.isHasPlayer());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setHasPlayer(nbt.getBoolean("Has_Player"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(HAS_PLAYER, Boolean.valueOf(false));
    }

    private EntityLivingBase grabbedEntity;
    private boolean grabDetection = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(!world.isRemote) {

            if(ticksExisted == 1) {
                this.playSound(SoundsHandler.ROH_CAST_GENERIC, 1.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
                animator.setStateAnim(ANIM_SUMMON);
            }

            if(this.ticksExisted == 14) {
                this.grabDetection = true;
            }

            if(grabDetection && grabbedEntity == null) {
                List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                        this.getEntityBoundingBox().grow(1.1D, 1D, 1.1D),
                        e -> !e.getIsInvulnerable());

                if (!nearbyEntities.isEmpty()) {
                    for (EntityLivingBase base : nearbyEntities) {
                        if (!(base instanceof EntityDarkBase)) {
                            grabbedEntity = base;
                            this.playSound(SoundsHandler.KING_GRAB_SUCCESS, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                        }
                    }
                }
            }

            if(grabbedEntity != null) {
                Vec3d offset = this.getPositionVector().add(new Vec3d(0, 2.5, 0));
                grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);
            }

            //either end it or continue
            if(ticksExisted == 20) {
                this.grabDetection = false;
                if(grabbedEntity != null) {
                    this.setHasPlayer(true);
                    animator.setStateAnim(ANIM_HOLD);
                } else {
                    animator.setStateAnim(ANIM_END);
                    this.playSound(SoundsHandler.VOIDCLYSM_IMPACT, 0.3f, 0.5f / (rand.nextFloat() * 0.4f + 0.2f));
                }
            }

            //initial end it if not grabbed entity
            if(ticksExisted == 55 && !this.isHasPlayer()) {
                this.setDead();
            }

            if(this.ticksExisted == 25 && this.isHasPlayer()) {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 3, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(this).build();
                RohUtils.handleAreaImpactDarkdrift(0.5f, (e) -> damage, this, offset, source, 0f, 0, false,0.4F, 3, 600);
                this.playSound(SoundsHandler.COLOSSUS_HILT_SLAM, 1f, 0.5f / (rand.nextFloat() * 0.4f + 0.2f));
            }

            if(this.ticksExisted == 60 && this.isHasPlayer()) {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 3, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(this).build();
                RohUtils.handleAreaImpactDarkdrift(0.5f, (e) -> damage, this, offset, source, 0f, 0, false,0.4F, 3, 600);
                this.playSound(SoundsHandler.COLOSSUS_HILT_SLAM, 1f, 0.5f / (rand.nextFloat() * 0.4f + 0.2f));
            }

            if(this.ticksExisted == 80) {
                this.setHasPlayer(false);
                animator.setStateAnim(ANIM_END);
                this.grabbedEntity = null;
                this.playSound(SoundsHandler.VOIDCLYSM_IMPACT, 0.3f, 0.5f / (rand.nextFloat() * 0.4f + 0.2f));
            }

            if(this.ticksExisted == 115) {
                this.setDead();
            }
        }

    }

    @Override
    public void updateAnimations() {

    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }
}
