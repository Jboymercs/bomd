package com.dungeon_additions.da.entity.sky_dungeon;

import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.Sys;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntitySkyTornado extends EntitySkyBase implements IAnimatable, IAnimationTickable {

    private static final DataParameter<Boolean> IS_HOSTILE = EntityDataManager.createKey(EntitySkyTornado.class, DataSerializers.BOOLEAN);

    private final String ANIM_IDLE = "storm_idle";
    private final String ANIM_ATTACK = "is_hostile";
    private int tornadoTimer = 0;
    private boolean isHostileTornado() {return this.dataManager.get(IS_HOSTILE);}
    private void setHostileTornado(boolean value) {this.dataManager.set(IS_HOSTILE, Boolean.valueOf(value));}

    private AnimationFactory factory = new AnimationFactory(this);

    public EntitySkyTornado(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(1.25F, 1.0F);
    }

    public EntitySkyTornado(World worldIn, boolean isHostile) {
        super(worldIn);
        this.setHostileTornado(isHostile);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(1.25F, 1.0F);
    }

    public EntitySkyTornado(World worldIn) {
        super(worldIn);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(1.25F, 1.0F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Hostile_Tornado", this.isHostileTornado());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setHostileTornado(nbt.getBoolean("Hostile_Tornado"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(IS_HOSTILE, Boolean.valueOf(false));
    }

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
            tornadoTimer--;
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            if(!this.isHostileTornado()) {
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(0.5, 1.5, 0.5), e -> !e.getIsInvulnerable() && (!(e instanceof EntitySkyBase)));

                if(!targets.isEmpty()) {
                    for(EntityLivingBase base : targets) {
                        if(!(base instanceof EntitySkyBase)) {
                            if(base.canBePushed() && tornadoTimer < 0) {
                                base.motionY += 1.7;
                                base.velocityChanged = true;
                                for(int i = 0; i < 55; i += 5) {
                                    addEvent(()-> base.fallDistance = 0, i);
                                }
                                playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.4F, 1.5f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
                                base.fallDistance = 0;
                                tornadoTimer = 38;
                            }
                        }
                    }
                }
            } else {

                if (this.ticksExisted == 2) {
                    this.playSound(SoundsHandler.IMPERIAL_STORM_SHORT, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.6f));
                }

                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(2.2), e -> !e.getIsInvulnerable() && (!(e instanceof EntitySkyBase)));
                if(!targets.isEmpty()) {
                    for(EntityLivingBase base : targets) {
                        if(!(base instanceof EntitySkyBase)) {
                            if(base.canBePushed() && tornadoTimer < 0 && this.ticksExisted <= 20) {
                                base.motionY += 1.4;
                                base.velocityChanged = true;
                                tornadoTimer = 10;
                                playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.4F, 1.5f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
                            }
                        }
                    }
                }
            }

        }


        if(this.isHostileTornado() && ticksExisted == 24) {
            this.setDead();
        }
    }


    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            if(world.rand.nextInt(2) == 0) {
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(0.2F - ModRand.getFloat(0.4F),0.25,0.2F - ModRand.getFloat(0.4F)), ModColors.WHITE, new Vec3d(0, 0.08, 0));
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(0.2F - ModRand.getFloat(0.4F),0.25,0.2F - ModRand.getFloat(0.4F)), ModColors.WHITE, new Vec3d(ModRand.getFloat(0.15F) - 0.1F, 0.08, ModRand.getFloat(0.15F) - 0.1F));
            }

        }
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
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateATtack));
    }

    private <E extends IAnimatable>PlayState predicateATtack(AnimationEvent<E> event) {
        if(this.isHostileTornado()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK, false));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isHostileTornado()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if(!this.isHostileTornado()) {
            return SoundsHandler.IMPERIAL_STORM_LONG;
        }
        return null;
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
