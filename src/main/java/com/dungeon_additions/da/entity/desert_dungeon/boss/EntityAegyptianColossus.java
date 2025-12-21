package com.dungeon_additions.da.entity.desert_dungeon.boss;

import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.ai.desert_dungeon.EntityAIAegyptiaWarlord;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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

import java.util.function.Consumer;

public class EntityAegyptianColossus extends EntitySharedDesertBoss implements IAnimatable, IAnimationTickable, IAttack, IScreenShake {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;
    private int shakeTime = 0;

    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STEP_SHAKE = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_FINISH = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_CONTINUE = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CALL_MACE = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MACE_SMASH = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MACE_SMASH_TWO = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);

    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    public boolean isStepShake() {return this.dataManager.get(STEP_SHAKE);}
    private void setStepShake(boolean value) {this.dataManager.set(STEP_SHAKE, Boolean.valueOf(value));}
    public boolean isSummon() {return this.dataManager.get(SUMMON);}
    private void setSummon(boolean value) {this.dataManager.set(SUMMON, Boolean.valueOf(value));}
    public boolean isSwingAttack() {return this.dataManager.get(SWING);}
    private void setSwingAttack(boolean value) {this.dataManager.set(SWING, Boolean.valueOf(value));}
    public boolean isSwingContinue() {return this.dataManager.get(SWING_CONTINUE);}
    private void setSwingContinue(boolean value) {this.dataManager.set(SWING_CONTINUE, Boolean.valueOf(value));}
    public boolean isSwingFinish() {return this.dataManager.get(SWING_FINISH);}
    private void setSwingFinish(boolean value) {this.dataManager.set(SWING_FINISH, Boolean.valueOf(value));}
    public boolean isCallMace() {return this.dataManager.get(CALL_MACE);}
    private void setCallMace(boolean value) {this.dataManager.set(CALL_MACE, Boolean.valueOf(value));}
    public boolean isMaceSmash() {return this.dataManager.get(MACE_SMASH);}
    private void setMaceSmash(boolean value) {this.dataManager.set(MACE_SMASH, Boolean.valueOf(value));}
    public boolean isMaceSmashTwo() {return this.dataManager.get(MACE_SMASH_TWO);}
    private void setMaceSmashTwo(boolean value) {this.dataManager.set(MACE_SMASH_TWO, Boolean.valueOf(value));}

    private final String ANIM_IDLE_LOWER = "idle_lower";
    private final String ANIM_IDLE_UPPER = "idle_upper";
    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_SUMMON = "summon";

    //attacks
    private final String ANIM_SWING = "swing";
    private final String ANIM_SWING_CONTINUE = "swing_continue";
    private final String ANIM_SWING_FINISH = "swing_finish";
    private final String ANIM_MACE_SMASH = "mace_smash";
    private final String ANIM_MACE_SMASH_TWO = "mace_smash_two";
    private final String ANIM_CALL_MACE = "call_mace";

    public EntityAegyptianColossus(World world, int timesUsed, BlockPos pos) {
        super(world, timesUsed, pos);
        this.setSize(2.5F, 4.95F);
        this.startBossSetup();
    }

    public EntityAegyptianColossus(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(2.5F, 4.95F);
        this.startBossSetup();
    }

    public EntityAegyptianColossus(World worldIn) {
        super(worldIn);
        this.setSize(2.5F, 4.95F);
        this.startBossSetup();
    }

    private void startBossSetup() {
        this.setSummon(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.lockLook = true;
        this.shakeTime = 80;
        addEvent(()-> this.setShaking(true), 10);

        addEvent(()-> this.setShaking(false), 80);

        addEvent(()-> {
            this.setSummon(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            this.lockLook = false;
        }, 100);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Step_Shake", this.isStepShake());
        nbt.setBoolean("Shaking", this.isShaking());
        nbt.setBoolean("Summon", this.isSummon());
        nbt.setBoolean("Swing_Attack", this.isSwingAttack());
        nbt.setBoolean("Swing_Continue", this.isSwingContinue());
        nbt.setBoolean("Swing_Finish", this.isSwingFinish());
        nbt.setBoolean("Call_Mace", this.isCallMace());
        nbt.setBoolean("Mace_Smash", this.isMaceSmash());
        nbt.setBoolean("Mace_Smash_Two", this.isMaceSmashTwo());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setStepShake(nbt.getBoolean("Step_Shake"));
        this.setShaking(nbt.getBoolean("Shaking"));
        this.setSummon(nbt.getBoolean("Summon"));
        this.setSwingAttack(nbt.getBoolean("Swing_Attack"));
        this.setSwingContinue(nbt.getBoolean("Swing_Continue"));
        this.setSwingFinish(nbt.getBoolean("Swing_Finish"));
        this.setCallMace(nbt.getBoolean("Call_Mace"));
        this.setMaceSmash(nbt.getBoolean("Mace_Smash"));
        this.setMaceSmashTwo(nbt.getBoolean("Mace_Smash_Two"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        this.dataManager.register(STEP_SHAKE, Boolean.valueOf(false));
        this.dataManager.register(SUMMON, Boolean.valueOf(false));
        this.dataManager.register(SWING, Boolean.valueOf(false));
        this.dataManager.register(SWING_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(SWING_FINISH, Boolean.valueOf(false));
        this.dataManager.register(CALL_MACE, Boolean.valueOf(false));
        this.dataManager.register(MACE_SMASH, Boolean.valueOf(false));
        this.dataManager.register(MACE_SMASH_TWO, Boolean.valueOf(false));
        super.entityInit();

    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(25);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(6);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
     //   this.tasks.addTask(4, new EntityAIAegyptiaWarlord<EntityAegyptianWarlord>(this, 1.0D, 20, 20, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.shakeTime--;
        if(!world.isRemote) {

            if(this.getOtherBoss() != null) {
                if(this.getOtherBoss() instanceof EntityAegyptianWarlord) {
                    EntityAegyptianWarlord warlord = ((EntityAegyptianWarlord) this.getOtherBoss());


                    //Specific state for checking if the warlord is being targeted, it is calculated if the Warlord has been damaged a lot
                    //within a four second period of checking
                    if(warlord.isBeingPushed) {

                    }

                }
            }

        }
    }


    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double healtFac = this.getHealth()/this.getMaxHealth();
        if(!this.isFightMode()) {

        }
        return 0;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "arms_controller_idle", 0, this::predicateArmsIdle));
        data.addAnimationController(new AnimationController(this, "attacks_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "states_controller", 0, this::predicateStates));
    }

    private<E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSwingAttack()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING));
                return PlayState.CONTINUE;
            }
            if(this.isSwingContinue()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_CONTINUE));
                return PlayState.CONTINUE;
            }
            if(this.isSwingFinish()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_FINISH));
                return PlayState.CONTINUE;
            }
            if(this.isMaceSmash()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_MACE_SMASH));
                return PlayState.CONTINUE;
            }
            if(this.isMaceSmashTwo()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_MACE_SMASH_TWO));
                return PlayState.CONTINUE;
            }
            if(this.isCallMace()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CALL_MACE));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateStates(AnimationEvent<E> event) {
        if(this.isSummon()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SUMMON));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.07F && event.getLimbSwingAmount() <= 0.07F && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArmsIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.07F && event.getLimbSwingAmount() <= 0.07F && !this.isFightMode() && !this.isSummon()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.08F && event.getLimbSwingAmount() <= 0.08F) && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.08F && event.getLimbSwingAmount() <= 0.08F) && !this.isFightMode() && !this.isSummon()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
            return PlayState.CONTINUE;
        }
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
    public int tickTimer() {
        return this.ticksExisted;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.APATHYR_STEP, 0.35F, 0.8f + ModRand.getFloat(0.5F));
        this.setStepShake(true);
        this.shakeTime = 5;
        addEvent(() -> {
            this.setStepShake(false);
        }, 5);

    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 34.0F);
            if (dist >= 34.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.4F) * 1.1F * screamMult);
        } else if(this.isStepShake()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 10.0F);
            if (dist >= 10.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 0.22F * screamMult);
        }
        return 0;
    }
}
