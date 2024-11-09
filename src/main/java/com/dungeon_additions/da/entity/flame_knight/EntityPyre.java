package com.dungeon_additions.da.entity.flame_knight;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
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

public class EntityPyre extends EntityAbstractBase implements IAnimatable, IAnimationTickable {

    private AnimationFactory factory = new AnimationFactory(this);

    public boolean isCracked = false;

    public boolean isParticleSet = false;

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
    public void onUpdate() {
        super.onUpdate();
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
    public int tickTimer() {
        return this.ticksExisted;
    }
}
