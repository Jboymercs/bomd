package com.dungeon_additions.da.entity.void_dungeon;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.void_dungeon.voidclysm_action.ActionVoidBomb;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;

public class EntityVoidBomb extends EntityEndBase implements IAnimatable, IAnimationTickable {

    private AnimationFactory factory = new AnimationFactory(this);
    private static final DataParameter<Boolean> EXPLODE_BOMB = EntityDataManager.createKey(EntityVoidBomb.class, DataSerializers.BOOLEAN);
    public void setExplodeBomb(boolean value) {this.dataManager.set(EXPLODE_BOMB, Boolean.valueOf(value));}
    public boolean isExplodeBomb() {return this.dataManager.get(EXPLODE_BOMB);}
    private final String ANIM_EXPLODE = "explode";
    private final String ANIM_IDLE = "idle";

    public EntityVoidBomb(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.0F, 1.0F);
    }

    public EntityVoidBomb(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Explode_Bomb", this.isExplodeBomb());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setExplodeBomb(nbt.getBoolean("Explode_Bomb"));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EXPLODE_BOMB, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    protected void initEntityAI() {

    }

    private boolean setTrigger = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.rotationPitch = 0;
        this.rotationYaw = 0;
        this.motionX = 0;
        this.motionZ = 0;
        //summons the small AOE effect of this bomb
        if(this.onGround && !setTrigger || this.ticksExisted > 400 && !setTrigger) {
            this.explodeVoidBomb();
            this.setTrigger = true;
        }
    }


    private void explodeVoidBomb() {
        this.setExplodeBomb(true);
        this.playSound(SoundsHandler.VOIDCLYSM_BOMB_EXPLODE, 0.75f, 1.0f);
        addEvent(()-> {
            new ActionVoidBomb().performAction(this, null);
        }, 10);
     //play sound and explode bomb
        //summon AOE
     addEvent(this::setDead, 30);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "explode_controller", 0, this::predicateExplode));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isExplodeBomb()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateExplode(AnimationEvent<E> event) {
        if(this.isExplodeBomb()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_EXPLODE, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
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
    public boolean canBeCollidedWith() {
        return false;
    }

}
