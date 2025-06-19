package com.dungeon_additions.da.entity.flame_knight;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityEliteDraugr;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
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

public class EntityPyre extends EntityAbstractBase implements IAnimatable, IAnimationTickable, IScreenShake {

    private AnimationFactory factory = new AnimationFactory(this);

    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityPyre.class, DataSerializers.BOOLEAN);
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    public boolean isCracked = false;

    public boolean isParticleSet = false;
    private int shakeTime = 0;

    public EntityPyre(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public EntityPyre(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.setNoAI(true);
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

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.shakeTime--;
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;
    }


    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(stack.getItem() == ModItems.FLAME_OF_AMBITION && !world.isRemote) {
            stack.shrink(1);
            world.setBlockToAir(this.getPosition().add(0, 1, 0));
            world.setBlockToAir(this.getPosition().add(0, 2, 0));
            this.beginBossCreation();
        }

        return super.processInteract(player, hand);
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }


    private void beginBossCreation() {
        this.setFightMode(true);
        this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 0.9f);

        addEvent(()-> this.isCracked = true, 126);
        addEvent(()-> {
            this.setShaking(true);
            this.shakeTime = 40;
        }, 60);
        addEvent(()-> {
            this.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 0.9f, 0.5f);
        },76 );
        addEvent(()-> {
            this.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 0.9f, 0.5f);
        },84 );
        addEvent(()-> {
            this.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 0.9f, 0.5f);
        },96 );
        addEvent(()-> {
            this.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 0.9f, 0.5f);
        },111 );

        addEvent(()-> {
            this.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1.3f, 0.5f);
        },126 );
        addEvent(()-> this.isParticleSet = true, 60);

        addEvent(()-> {
        this.setFightMode(false);
        this.world.newExplosion(this, this.posX, this.posY + 1.5, this.posZ, 2, false, false);
        this.setShaking(false);
        new EntityFlameKnight(this.world).onSummon(this);
        }, 152);
    }


    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public int getBrightnessForRender() {
        if(this.isCracked) {
            return Math.min(super.getBrightnessForRender() + 30, 200);
        }
        return super.getBrightnessForRender();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "start_controller", 0, this::predicateStart));
    }

    private<E extends IAnimatable>PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    private<E extends IAnimatable>PlayState predicateStart(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("begin", false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(this.isParticleSet) {
            if(world.rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            }
        }
    }


    @Override
    public void handleStatusUpdate(byte id) {
        if(id == ModUtils.PARTICLE_BYTE) {
            Vec3d pos = this.getPositionVector().add(new Vec3d(ModRand.range(-6, 6), 0, ModRand.range(-6, 6)));
            Vec3d pos2 = this.getPositionVector().add(new Vec3d(ModRand.range(-6, 6), 0, ModRand.range(-6, 6)));
            Vec3d pos3 = this.getPositionVector().add(new Vec3d(ModRand.range(-6, 6), 0, ModRand.range(-6, 6)));
            Vec3d vel = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat())/3,  0.1, (world.rand.nextFloat() - world.rand.nextFloat())/3);
            world.spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, ModRand.range(10, 15));
            world.spawnParticle(EnumParticleTypes.FLAME, pos2.x, pos2.y, pos2.z, vel.x, vel.y, vel.z, ModRand.range(10, 15));
            world.spawnParticle(EnumParticleTypes.FLAME, pos3.x, pos3.y, pos3.z, vel.x, vel.y, vel.z, ModRand.range(10, 15));
        }
        super.handleStatusUpdate(id);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 16.0F);
            if (dist >= 16.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.75F * screamMult);
        }
        return 0;
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
