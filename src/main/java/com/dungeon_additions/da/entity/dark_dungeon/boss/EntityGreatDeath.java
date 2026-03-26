package com.dungeon_additions.da.entity.dark_dungeon.boss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.packets.MessageDirectionForRender;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
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

public class EntityGreatDeath extends EntityDarkBase implements IAnimationTickable, IAnimatable, IScreenShake {

    private final String ANIM_SUMMON = "summon";
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityGreatDeath.class, DataSerializers.BOOLEAN);
    private void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    private final AnimationFactory factory = new AnimationFactory(this);


    public EntityGreatDeath(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.setImmovable(true);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setNoAI(true);
        this.setSize(1.5F, 0.4F);
    }

    public EntityGreatDeath(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setImmovable(true);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setNoAI(true);
        this.setSize(1.5F, 0.3F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Shaking", this.isShaking());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setShaking(nbt.getBoolean("Shaking"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
    }

    private int shakeTime = 0;


    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;
        this.shakeTime--;

        if(ticksExisted == 15) {
            this.setShaking(true);
            this.shakeTime = 40;
        }

        if(ticksExisted == 2) {
            this.playSound(SoundsHandler.GREAT_DEATH_SUMMON, 0.5f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        }

        if(ticksExisted == 19) {
            this.playSound(SoundsHandler.GREAT_DEATH_IMPACT, 0.7f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        }

        if(this.ticksExisted > 15 && world.rand.nextInt(2) == 0) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }

        if(this.ticksExisted > 19) {
            if (!this.world.isRemote) {

                if(this.ticksExisted == 20 || this.ticksExisted == 30 || this.ticksExisted == 40) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1, 0)));
                        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(this).build();
                        float damage = 10;
                        ModUtils.handleAreaImpact(1.4f, (e) -> damage, this, offset, source, 0.4f, 0, false);
                }
            }
        }

        if(ticksExisted >= 41) {
            this.setDead();
        }

    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.performNTimes(1, (i) -> {
                Main.proxy.spawnParticle(30, this.posX + ModRand.getFloat(1.5F), this.posY + ModRand.getFloat(2F) + 2, this.posZ + ModRand.getFloat(1.5F), 0, -0.06, 0, 7340032);
            });
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON));
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
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 8.0F);
            if (dist >= 8.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.4F) * 0.25F * screamMult);
        }
        return 0;
    }
}
