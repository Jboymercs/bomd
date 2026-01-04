package com.dungeon_additions.da.entity.generic;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntityDelayedExplosion extends EntityAbstractBase implements IAnimatable, IAnimationTickable {
    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_SHOOT = "explode";

    private EntityPlayer player;
    private float damageIn;
    private EntityAbstractBase owner;

    private static final DataParameter<Boolean> ORANGE_STYLE = EntityDataManager.createKey(EntityDelayedExplosion.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PURPLE_STYLE = EntityDataManager.createKey(EntityDelayedExplosion.class, DataSerializers.BOOLEAN);

    public boolean isOrangeStyle() {return this.dataManager.get(ORANGE_STYLE);}
    public void setOrangeStyle(boolean value) {this.dataManager.set(ORANGE_STYLE, Boolean.valueOf(value));}

    public boolean isPurpleStyle() {return this.dataManager.get(PURPLE_STYLE);}
    public void setPurpleStyle(boolean value) {this.dataManager.set(PURPLE_STYLE, Boolean.valueOf(value));}


    public EntityDelayedExplosion(World worldIn, EntityPlayer player, float damageIn) {
        super(worldIn);
        this.player = player;
        this.damageIn = damageIn;
        this.setSize(1f, 1F);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setOrangeStyle(false);
        this.setPurpleStyle(false);
    }

    public EntityDelayedExplosion(World worldIn, EntityAbstractBase owner, float damageIn, boolean isOrange, boolean isPurple) {
        super(worldIn);
        this.owner = owner;
        this.damageIn = damageIn;
        this.setOrangeStyle(isOrange);
        this.setPurpleStyle(isPurple);
        this.setSize(1f, 1F);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public EntityDelayedExplosion(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1f, 1F);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public EntityDelayedExplosion(World worldIn) {
        super(worldIn);
        this.setSize(1f, 1F);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Flame_Style", this.isOrangeStyle());
        nbt.setBoolean("Purple_Style", this.isPurpleStyle());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setOrangeStyle(nbt.getBoolean("Flame_Style"));
        this.setPurpleStyle(nbt.getBoolean("Purple_Style"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(ORANGE_STYLE, Boolean.valueOf(false));
        this.dataManager.register(PURPLE_STYLE, Boolean.valueOf(false));
        super.entityInit();
    }

    private boolean initiatedAttack;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(ticksExisted == 26) {
            if(!world.isRemote) {
                if (owner != null) {
                    List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(2.5), e -> !e.getIsInvulnerable() && (!(e instanceof EntityAbstractBase)));
                    if(!targets.isEmpty() && !initiatedAttack) {
                        for(EntityLivingBase base : targets) {
                            if(base != this && base != owner) {
                                Vec3d offset = this.getPositionVector().add(ModUtils.yVec(0.5D));
                                DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MAGIC).directEntity(owner).build();
                                ModUtils.handleAreaImpact(2f, (e) -> damageIn, this, offset, source, 0.6f, this.isOrangeStyle() ? 5 : 0, false);
                                this.initiatedAttack = true;
                            }
                        }
                    }
                } else if (player != null) {
                    List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(2.5), e -> !e.getIsInvulnerable());
                    if(!targets.isEmpty() && !initiatedAttack) {
                        for(EntityLivingBase base : targets) {
                            if(base != this && base != player) {
                                Vec3d offset = this.getPositionVector().add(ModUtils.yVec(0.5D));
                                DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MAGIC).directEntity(player).build();
                                ModUtils.handleAreaImpact(2f, (e) -> damageIn, this, offset, source, 0.9f, 0, false);
                                this.initiatedAttack = true;
                            }
                        }
                    }
                }
            }
            this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.6f, 1.0f);
            this.playSound(SoundsHandler.VOIDCLYSM_IMPACT, 0.5F, 0.5f + ModRand.getFloat(0.6f));
            if(this.isOrangeStyle()) {
                world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
            } else if (this.isPurpleStyle()) {
                world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            } else {
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            }
        }

        if(ticksExisted > 34) {
            this.setDead();
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
            //blue
        if(id == ModUtils.PARTICLE_BYTE) {

            //purple
        } else if (id == ModUtils.SECOND_PARTICLE_BYTE) {


            //orange
        } else if (id == ModUtils.THIRD_PARTICLE_BYTE) {

        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
    }
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT, false));
        return PlayState.CONTINUE;
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
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
