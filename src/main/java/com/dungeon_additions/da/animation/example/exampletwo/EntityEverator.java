package com.dungeon_additions.da.animation.example.exampletwo;

import akka.japi.pf.FI;
import com.dungeon_additions.da.animation.EZAnimation;
import com.dungeon_additions.da.animation.EZAnimationHandler;
import com.dungeon_additions.da.animation.IAnimatedEntity;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.EntityAiTimedAttack;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityEverator extends EntityAbstractBase implements IAnimatedEntity, IAttack {

    //Here is where you create animations for the entity and a simple statement of how long they are in ticks
    public static final EZAnimation ANIMATION_ATTACK_MOB = EZAnimation.create(40);
    public static final EZAnimation ANIMATION_SWING_HEAD = EZAnimation.create(20);

    protected static final DataParameter<Boolean> FIGHT_MODES = EntityDataManager.createKey(EntityEverator.class, DataSerializers.BOOLEAN);

    //Used for the combat AI IGNORE
    private Consumer<EntityLivingBase> prevAttack;

    //used for animation system
    private int animationTick;
    //just a variable that holds what the current animation is
    private EZAnimation currentAnimation;

    public EntityEverator(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityEverator(World worldIn) {
        super(worldIn);
    }

    public int swingHeadTimer = 600;
    public int tick_delay = 5;
    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.isFightMode()) {
            if(this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_ATTACK_MOB);
            }
        }

        if(swingHeadTimer < 0 && !this.isFightMode()) {
            this.setAnimation(ANIMATION_SWING_HEAD);
            swingHeadTimer = 600;
        } else {
            swingHeadTimer--;
        }

        //NEVER FORGET THIS, OR ELSE ANIMATIONS WILL NOT WORK
        EZAnimationHandler.INSTANCE.updateAnimations(this);

    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(FIGHT_MODES, Boolean.valueOf(false));
    }

    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODES);}
    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODES, Boolean.valueOf(value));}

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);

    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(4, new EntityAiTimedAttack<EntityEverator>(this, 1.3D, 60, 3, 0.3F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));

    }


    //This is applying those variables from the entity class into the animation system
    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public EZAnimation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(EZAnimation animation) {
        currentAnimation = animation;
    }

    //This is where you store a collective list of all the animations this entity is capable of ONLY USING THIS SYSTEM, there is no walk or idle animations
    @Override
    public EZAnimation[] getAnimations() {
        return new EZAnimation[]{ANIMATION_ATTACK_MOB, ANIMATION_SWING_HEAD};
    }

    //Ignored, this is the attack brains AI
    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);

        if(!this.isFightMode() && this.getAnimation() == NO_ANIMATION) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(meleeAttack));
            double[] weights = {
                    (distance <= 4) ? 1/distance : 0 // melee attack

            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return 60;
    }

    public boolean setAttackAnimation = false;
    private final Consumer<EntityLivingBase> meleeAttack = (target) -> {
        this.setFightMode(true);
        //


        //below is just combat stuff
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.4, 0)));
            DamageSource damageSource = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = 6.0F;
            ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, offset, damageSource, 0.5f, 0, false);
        }, 17);

        addEvent(()-> {
            //as a side note just make sure that the attack doesn't shorten beyond the animation length may cause bugs with overlapping animations
            this.setAnimation(NO_ANIMATION);
            this.setFightMode(false);
        }, 40);
    };
}
