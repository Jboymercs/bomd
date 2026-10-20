package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityDarkdriftDevil;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityPyre;
import com.dungeon_additions.da.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityRohsEye extends EntityDarkBase implements IAnimatable, IAnimationTickable, IScreenShake {

    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityRohsEye.class, DataSerializers.BOOLEAN);
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    private int shakeTime = 0;
    private AnimationFactory factory = new AnimationFactory(this);
    public EntityRohsEye(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setImmovable(true);
        this.falter_immune = true;
        this.isImmuneToFire = true;
    }

    public EntityRohsEye(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.falter_immune = true;
        this.isImmuneToFire = true;
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

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 7));
    }


    private void beginBossCreation() {
        this.setFightMode(true);
        this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 0.9f);
        addEvent(()-> {
            this.setShaking(true);
            this.shakeTime = 40;
        }, 40);

        addEvent(()-> {
            this.setFightMode(false);
            this.world.newExplosion(this, this.posX, this.posY + 1.5, this.posZ, 2, false, false);
            this.setShaking(false);
            new EntityDarkdriftDevil(this.world).onSummon(this);
        }, 80);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "start_controller", 0, this::predicateStart));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().loop("idle"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    private<E extends IAnimatable>PlayState predicateStart(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce("summon"));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
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
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
