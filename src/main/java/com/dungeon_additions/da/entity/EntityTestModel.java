package com.dungeon_additions.da.entity;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.util.ModRand;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.keyframe.EventKeyFrame;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class EntityTestModel extends EntityAbstractBase implements IAnimatable, IAnimationTickable {
    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_SWING = "swing";
    private final String ANIM_SWING_RIGHT = "swing_r";
    private final String ANIM_SWING_PIERCE = "swing_pierce";
    private static DataParameter<Boolean> SWING_ATTACK = EntityDataManager.createKey(EntityTestModel.class, DataSerializers.BOOLEAN);
    private static DataParameter<Boolean> SWING_RIGHT = EntityDataManager.createKey(EntityTestModel.class, DataSerializers.BOOLEAN);
    private static DataParameter<Boolean> SWING_PIERCE = EntityDataManager.createKey(EntityTestModel.class, DataSerializers.BOOLEAN);
    private static DataParameter<Boolean> PERFORMING_COMBO = EntityDataManager.createKey(EntityTestModel.class, DataSerializers.BOOLEAN);

    public void setSwingAttacks(boolean value) {
        this.dataManager.set(SWING_ATTACK, Boolean.valueOf(value));
    }
    public void setSwingRight(boolean value) {
        this.dataManager.set(SWING_RIGHT, Boolean.valueOf(value));
    }
    public void setSwingPierce(boolean value) {
        this.dataManager.set(SWING_PIERCE, Boolean.valueOf(value));
    }
    public void setPerformingCombo(boolean value) {this.dataManager.set(PERFORMING_COMBO, Boolean.valueOf(value));}
    public boolean isSwingAttacks() {
        return this.dataManager.get(SWING_ATTACK);
    }
    public boolean isSwingRight() {
        return this.dataManager.get(SWING_RIGHT);
    }
    public boolean isSwingPierce() {
        return this.dataManager.get(SWING_PIERCE);
    }
    public boolean isPerformingCombo() {return this.dataManager.get(PERFORMING_COMBO);}


    public EntityTestModel(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 1.85F);
        this.setNoAI(true);
    }

    public EntityTestModel(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 1.85F);
        this.setNoAI(true);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setSwingRight(nbt.getBoolean("Swing_R"));
        this.setSwingAttacks(nbt.getBoolean("Swing_A"));
        this.setSwingPierce(nbt.getBoolean("Swing_P"));
        this.setPerformingCombo(nbt.getBoolean("Performing_Combo"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Swing_R", this.isSwingRight());
        nbt.setBoolean("Swing_A", this.isSwingAttacks());
        nbt.setBoolean("Swing_P", this.isSwingPierce());
        nbt.setBoolean("Performing_Combo", this.isPerformingCombo());
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SWING_PIERCE, Boolean.valueOf(false));
        this.dataManager.register(SWING_RIGHT, Boolean.valueOf(false));
        this.dataManager.register(SWING_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(PERFORMING_COMBO, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(10D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!this.world.isRemote) {

            if(ticksExisted % 100 == 0 && !this.isFightMode()) {
                if(rand.nextInt(2) == 0) {
                    swing_start.accept(this);
                } else {
                    swing_two_start.accept(this);
                }
                this.setPerformingCombo(false);
            }
        }
    }

    private final Consumer<EntityLivingBase> swing_start = (target) -> {
        this.setFightMode(true);
        this.setSwingAttacks(true);

        addEvent(()-> {
            boolean rand = ModRand.percentageOf(70);
            if(rand) {
                this.setPerformingCombo(true);
                this.setSwingTwoStart(target);
                this.setSwingAttacks(false);
            } else {
                addEvent(() -> {
                    this.setSwingAttacks(false);
                    if (!rand) {
                        this.setFightMode(false);
                    }
                }, 10);
            }
        }, 30);
    };

    private void setSwingTwoStart(EntityLivingBase target) {
        swing_two_start.accept(target);
    }

    private final Consumer<EntityLivingBase> swing_two_start = (target) -> {
        this.setSwingRight(true);
        this.setFightMode(true);

        addEvent(()-> {
            boolean rand = ModRand.percentageOf(70);
            if(rand) {
                this.setSwingThreeStart(target);
                this.setSwingRight(false);
            } else {
                addEvent(() -> {
                    this.setSwingRight(false);
                    this.setFightMode(false);
                }, 10);
            }
        }, 40);
    };

    private void setSwingThreeStart(EntityLivingBase target) {
        swing_three.accept(target);
    }

    private final Consumer<EntityLivingBase> swing_three = (target) -> {
        this.setSwingPierce(true);
        this.setFightMode(true);

        addEvent(()-> {
            this.setSwingPierce(false);
            this.setFightMode(false);
        }, 40);
    };


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "test_attacks", 3, this::predicateAttack));
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSwingAttacks()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING));
            }
            if(this.isSwingRight()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_RIGHT + (this.isPerformingCombo() ? "t" : "")));
            }
            if(this.isSwingPierce()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_PIERCE + (this.isPerformingCombo() ? "t" : "")));
            }
            if(event.getController().getAnimationState() == AnimationState.Transitioning) {
              //  event.getController().tickOffset = comboDelay;
              //  event.getController().getCurrentAnimation().customInstructionKeyframes.stream().count();
              //  this.comboDelay = 0;
            }
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
        super.onUpdate();
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
