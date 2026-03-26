package com.dungeon_additions.da.entity.dark_dungeon.boss;

import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.dark_dungeon.ProjectileBloodSigil;
import com.dungeon_additions.da.entity.dark_dungeon.boss.action.ActionDemonSigilLazer;
import com.dungeon_additions.da.entity.frost_dungeon.IDirectionalRender;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.IMultiAction;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.ITarget;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
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

import java.util.Optional;

public class EntityDemonSigil extends EntityDarkBase  implements IAnimationTickable, IAnimatable, ITarget, IDirectionalRender {

    private final String ANIM_STATIC = "static";
    private final String ANIM_ROTATING = "moving";
    private float damageIn;
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final DataParameter<Boolean> ROTATING_SIGIL = EntityDataManager.createKey(EntityDemonSigil.class, DataSerializers.BOOLEAN);
    private void setRotatingSigil(boolean value) {this.dataManager.set(ROTATING_SIGIL, Boolean.valueOf(value));}
    public boolean isRotatingSigil() {return this.dataManager.get(ROTATING_SIGIL);}

    public IMultiAction lazerAttack =  new ActionDemonSigilLazer(this, stopLazerByte, (vec3d) -> {}, 5);
    public Vec3d renderLazerPos;
    public Vec3d prevRenderLazerPos;
    protected boolean performLazerAttack = false;
    protected static final byte stopLazerByte = 39;

    private float yawValue;

    public EntityDemonSigil(World world, float yawVal, boolean isRotating, float damageIn) {
        super(world);
        this.yawValue = yawVal;
        this.setRotatingSigil(isRotating);
        this.rotationYaw = yawVal;
        this.rotationYawHead = yawVal;
        this.damageIn = damageIn;
        this.setNoGravity(true);
        this.setImmovable(true);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setNoAI(true);
        this.setSize(0.5F, 1F);
    }

    public EntityDemonSigil(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.setImmovable(true);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.damageIn = 5;
        this.setNoAI(true);
        this.setSize(0.5F, 1F);
        this.setRotatingSigil(false);
    }

    public EntityDemonSigil(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setImmovable(true);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.damageIn = 5;
        this.setNoAI(true);
        this.setSize(0.5F, 1F);
        this.setRotatingSigil(false);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationPitch = 0;

        if(!world.isRemote) {

            //rotating
            if(this.isRotatingSigil()) {

                if(ticksExisted == 2) {
                    this.doLazerAttackCurrently();
                }

                    if(this.ticksExisted >= 10 && this.ticksExisted <= 190) {
                        this.rotationYaw += 1.5F;
                        this.rotationYawHead += 1.5F;
                    }

                    if(this.ticksExisted == 199) {
                        this.setDead();
                    }
            } else {
                if(yawValue != 0) {
                    this.rotationYawHead = yawValue;
                    this.rotationYaw = yawValue;
                } else {
                    this.rotationYawHead = 0;
                    this.rotationYaw = 0;
                }

                if(this.ticksExisted % 10 == 0 && this.ticksExisted <= 130 && this.ticksExisted >= 15) {
                    this.playSound(SoundsHandler.COLOSSUS_HILT_SLAM, 0.75f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
                    ProjectileBloodSigil sigil = new ProjectileBloodSigil(world, this, damageIn);
                    Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75, 0.5, 0)));
                    sigil.setPosition(relPos.x, relPos.y, relPos.z);
                    sigil.shoot(this, 0, this.rotationYaw, 0.0F, 1F, 0F);
                    sigil.setTravelRange(20F);
                    world.spawnEntity(sigil);
                }

                if(this.ticksExisted == 140) {
                    this.setDead();
                }
            }
        }

    }

    public void doLazerAttackCurrently() {
        lazerAttack.doAction();
        this.performLazerAttack = true;

        addEvent(()-> this.performLazerAttack = false, 188);
    }

    @Override
    public float getEyeHeight() {
        return 0.5F;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!world.isRemote && performLazerAttack) {
            lazerAttack.update();
        }
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Rotating_Sigil", this.isRotatingSigil());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setRotatingSigil(nbt.getBoolean("Rotating_Sigil"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(ROTATING_SIGIL, Boolean.valueOf(false));
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(this.isRotatingSigil()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ROTATING));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STATIC));
        }
        return PlayState.CONTINUE;
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

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public Optional<Vec3d> getTarget() {
        return Optional.ofNullable(renderLazerPos);
    }

    @Override
    public void setRenderDirection(Vec3d dir) {
        if (this.renderLazerPos != null) {
            this.prevRenderLazerPos = this.renderLazerPos;
        } else {
            this.prevRenderLazerPos = dir;
        }
        this.renderLazerPos = dir;
    }
}
