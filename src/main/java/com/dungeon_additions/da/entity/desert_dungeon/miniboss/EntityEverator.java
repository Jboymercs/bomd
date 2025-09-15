package com.dungeon_additions.da.entity.desert_dungeon.miniboss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.ai.desert_dungeon.EntityAegyptiaAttackAI;
import com.dungeon_additions.da.entity.ai.desert_dungeon.EntityEveratorAttackAI;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.desert_dungeon.EntityDesertBase;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileDesertStorm;
import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityAegyptia;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicGround;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityEverator extends EntityDesertBase implements IAnimatable, IAnimationTickable, IAttack, IScreenShake {

    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_IDLE_LOWER = "idle_lower";

    private final String ANIM_IDLE_UPPER = "idle_upper";

    private final String ANIM_WALK_UPPER = "walk_upper";

    //states
    private final String ANIM_INACTIVE = "inactive";
    private final String ANIM_AWAKE = "awake";

    //Attack Animations
    private final String ANIM_SWING_ONE = "swing_one";
    private final String ANIM_SWING_TWO = "swing_two";
    private final String ANIM_SWING_THREE = "swing_three";
    private final String ANIM_SUMMON_STORMS = "summon_storms";
    private final String ANIM_STOMP = "stomp";
    private final String ANIM_OVERHEAD_SWING = "overhead_swing";
    //50% attacks
    private final String ANIM_CIRCLE_SWING = "circle_swing";
    private final String ANIM_TRIBUTE = "tribute";


    private static final DataParameter<Boolean> SWING_ONE = EntityDataManager.createKey(EntityEverator.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_TWO = EntityDataManager.createKey(EntityEverator.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_THREE = EntityDataManager.createKey(EntityEverator.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_STORMS = EntityDataManager.createKey(EntityEverator.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STOMP = EntityDataManager.createKey(EntityEverator.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> OVERHEAD_SWING = EntityDataManager.createKey(EntityEverator.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CIRCLE_SWING = EntityDataManager.createKey(EntityEverator.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TRIBUTE = EntityDataManager.createKey(EntityEverator.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> INACTIVE = EntityDataManager.createKey(EntityEverator.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> AWAKE = EntityDataManager.createKey(EntityEverator.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityEverator.class, DataSerializers.BOOLEAN);

    public boolean byPassStrafing = false;

    public boolean isSwingOne() {return this.dataManager.get(SWING_ONE);}
    private void setSwingOne(boolean value) {this.dataManager.set(SWING_ONE, Boolean.valueOf(value));}
    public boolean isSwingTwo() {return this.dataManager.get(SWING_TWO);}
    private void setSwingTwo(boolean value) {this.dataManager.set(SWING_TWO, Boolean.valueOf(value));}
    public boolean isSwingThree() {return this.dataManager.get(SWING_THREE);}
    private void setSwingThree(boolean value) {this.dataManager.set(SWING_THREE, Boolean.valueOf(value));}
    public boolean isSummonStorms() {return this.dataManager.get(SUMMON_STORMS);}
    private void setSummonStorms(boolean value) {this.dataManager.set(SUMMON_STORMS, Boolean.valueOf(value));}
    public boolean isStompAttack() {return this.dataManager.get(STOMP);}
    private void setStompAttack(boolean value) {this.dataManager.set(STOMP, Boolean.valueOf(value));}
    public boolean isOverheadSwing() {return this.dataManager.get(OVERHEAD_SWING);}
    private void setOverheadSwing(boolean value) {this.dataManager.set(OVERHEAD_SWING, Boolean.valueOf(value));}
    public boolean isCircleSwing() {return this.dataManager.get(CIRCLE_SWING);}
    private void setCircleSwing(boolean value) {this.dataManager.set(CIRCLE_SWING, Boolean.valueOf(value));}
    public boolean isTribute() {return this.dataManager.get(TRIBUTE);}
    private void setTribute(boolean value) {this.dataManager.set(TRIBUTE, Boolean.valueOf(value));}
    public boolean isAwake() {return this.dataManager.get(AWAKE);}
    private void setAwake(boolean value) {this.dataManager.set(AWAKE, Boolean.valueOf(value));}
    public boolean isInactiveMode() {return this.dataManager.get(INACTIVE);}
    private void setInactive(boolean value) {this.dataManager.set(INACTIVE, Boolean.valueOf(value));}

    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;
    private int shakeTime = 0;


    public EntityEverator(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.3F, 3.2F);
        this.experienceValue = 100;
        this.iAmBossMob = true;
        this.setInactive(true);
        this.setImmovable(true);
    }

    public EntityEverator(World worldIn) {
        super(worldIn);
        this.setSize(1.3F, 3.2F);
        this.experienceValue = 100;
        this.iAmBossMob = true;
        this.setImmovable(true);
        this.setInactive(true);
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Swing_One", this.isSwingOne());
        nbt.setBoolean("Swing_Two", this.isSwingTwo());
        nbt.setBoolean("Swing_Three", this.isSwingThree());
        nbt.setBoolean("Summon_Storms", this.isSummonStorms());
        nbt.setBoolean("Stomp", this.isStompAttack());
        nbt.setBoolean("Overhead_Swing", this.isOverheadSwing());
        nbt.setBoolean("Circle_Swing", this.isCircleSwing());
        nbt.setBoolean("Tribute", this.isTribute());
        nbt.setBoolean("Awake", this.isAwake());
        nbt.setBoolean("Inactive", this.isInactiveMode());
        nbt.setBoolean("Shaking", this.isShaking());
        super.writeEntityToNBT(nbt);
    }
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSwingOne(nbt.getBoolean("Swing_One"));
        this.setSwingTwo(nbt.getBoolean("Swing_Two"));
        this.setSwingThree(nbt.getBoolean("Swing_Three"));
        this.setSummonStorms(nbt.getBoolean("Summon_Storms"));
        this.setStompAttack(nbt.getBoolean("Stomp"));
        this.setOverheadSwing(nbt.getBoolean("Overhead_Swing"));
        this.setCircleSwing(nbt.getBoolean("Circle_Swing"));
        this.setTribute(nbt.getBoolean("Tribute"));
        this.setAwake(nbt.getBoolean("Awake"));
        this.setInactive(nbt.getBoolean("Inactive"));
        this.setShaking(nbt.getBoolean("Shaking"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(SWING_ONE, Boolean.valueOf(false));
        this.dataManager.register(SWING_TWO, Boolean.valueOf(false));
        this.dataManager.register(SWING_THREE, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_STORMS, Boolean.valueOf(false));
        this.dataManager.register(STOMP, Boolean.valueOf(false));
        this.dataManager.register(OVERHEAD_SWING, Boolean.valueOf(false));
        this.dataManager.register(CIRCLE_SWING, Boolean.valueOf(false));
        this.dataManager.register(TRIBUTE, Boolean.valueOf(false));
        this.dataManager.register(INACTIVE, Boolean.valueOf(false));
        this.dataManager.register(AWAKE, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        super.entityInit();
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.everator_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.everator_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.everator_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.everator_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }


    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityEveratorAttackAI<>(this, 1.0D, 10, 10, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityDarkBase>(this, EntityDarkBase.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    private float randomTurn = ModRand.range(1, 360);

    private boolean isTributed = false;
    private int tributeTimer = 20 * 15;

    private int attackCooldown = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.shakeTime--;
        this.attackCooldown--;
        if(this.isInactiveMode() || this.isAwake()) {
            this.rotationYaw = randomTurn;
            this.rotationYawHead = randomTurn;
            this.setImmovable(true);
        }
        if(!world.isRemote) {
            EntityLivingBase target = this.getAttackTarget();

            if(target != null) {
                double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
                double distance = Math.sqrt(distSq);

                if(distance < 2 && this.isInactiveMode() && !this.isAwake()) {
                        this.setActiveState();
                }

                //does the overhead swing if the target is far
                if(distance > 10 && !this.isFightMode() && !this.isInactiveMode() && !this.isAwake()) {
                    if(this.ticksExisted % 160 == 0) {
                        prevAttack = overhead_swing;
                        prevAttack.accept(target);
                    }
                }
            }

            if(this.isTributed) {
                if(ticksExisted % 20 == 0) {
                    this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 0, 60, false, false));
                }
                if(tributeTimer < 0) {
                    this.isTributed = false;
                    tributeTimer = 20 * 15;
                } else {
                    tributeTimer--;
                }
            }
        }
    }

    private void setActiveState() {
        this.setInactive(false);
        this.setAwake(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);

        addEvent(()-> {
            this.setImmovable(false);
            this.setAwake(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
        }, 35);
    }

    //start Attacks
    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double healthFac = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && !this.isInactiveMode() && !this.isAwake() && attackCooldown <= 0) {
            List<Consumer<EntityLivingBase>> attacks= new ArrayList<>(Arrays.asList(swing_attack, long_swing, summon_storms, stomp_attack, send_tribute, circleSwing, overhead_swing));
            double[] weights = {
                    (distance < 6 && prevAttack != swing_attack) ? 1/distance : 0,
                    (distance < 6) && prevAttack != long_swing ? 1/distance : 0,
                    (distance > 3 && prevAttack != summon_storms) ? 1/distance : 0,
                    (prevAttack != stomp_attack) ? 1/distance : 0,
                    (healthFac < 0.5 && !this.isTributed && prevAttack != send_tribute) ? 1/distance : 0,
                    (healthFac < 0.5 && prevAttack != circleSwing && distance < 6) ? 1/distance : 0,
                    (distance > 3 && prevAttack != overhead_swing) ? distance * 0.02 : 0
            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        return 5;
    }

    Supplier<Projectile> yellow_wave_projectiles = () -> new ProjectileYellowWave(world, this, (float) this.getAttack(), null);

    private final Consumer<EntityLivingBase> overhead_swing = (target) -> {
      this.setFightMode(true);
      this.setOverheadSwing(true);

      addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = true;
      }, 25);

      addEvent(()-> {
            //do long attack
          new ActionEveratorProjectileYellow(yellow_wave_projectiles).performAction(this, target);
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.6, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage =(float) (this.getAttack());
          int fireFac = 0;
          if(this.isTributed) {
              fireFac = 4;
          }
          ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.5f, fireFac, false);
          this.destroyBlocksInSwing(new Vec3d(1.6, 1.2, 0), 2.0);
          this.playSound(SoundsHandler.EVERATOR_SWING, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          this.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          world.setEntityState(this, ModUtils.PARTICLE_BYTE);
      }, 37);

      addEvent(()-> {
        this.setImmovable(false);
        this.lockLook = false;
      }, 55);

      addEvent(()-> {
        this.setFightMode(false);
        this.setOverheadSwing(false);
      }, 65);
    };

    private final Consumer<EntityLivingBase> circleSwing = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setCircleSwing(true);
      this.setImmovable(true);

      addEvent(()-> {
        this.lockLook = true;
      }, 25);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage =(float) (this.getAttack());
          int fireFac = 0;
          if(this.isTributed) {
              fireFac = 4;
          }
          ModUtils.handleAreaImpact(4.5f, (e) -> damage, this, offset, source, 0.9f, fireFac, false);
          this.playSound(SoundsHandler.EVERATOR_SWING, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 40);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack() * 1.75);
            int fireFac = 0;
            if(this.isTributed) {
                fireFac = 4;
            }
            ModUtils.handleAreaImpact(4.5f, (e) -> damage, this, offset, source, 0.9f, fireFac, false);
            this.destroyBlocksInSwing(new Vec3d(0, 1.2, 0), 4.5);
            this.playSound(SoundsHandler.EVERATOR_SWING, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 50);

        addEvent(()-> {
            this.lockLook = false;
        }, 85);

      addEvent(()-> {
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setCircleSwing(false);
        this.setImmovable(false);
        attackCooldown = 40;
      }, 90);
    };

    private final Consumer<EntityLivingBase> send_tribute = (target) -> {
      this.setTribute(true);
      this.setFightMode(true);

      addEvent(()-> {
        this.setImmovable(true);
        this.lockLook = true;
      }, 20);

      addEvent(()-> this.playSound(SoundsHandler.EVERATOR_TRIBUTE, 1.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f)), 35);
      addEvent(()-> {
          world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        this.isTributed = true;
      }, 45);

      addEvent(()-> {
        this.lockLook = false;
        this.setImmovable(false);
      }, 50);

      addEvent(()-> {
          this.setTribute(false);
          this.setFightMode(false);
      }, 55);
    };

    private final Consumer<EntityLivingBase> stomp_attack = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setStompAttack(true);
      this.setImmovable(true);
      addEvent(()-> {
        this.lockLook = true;
      }, 25);

      addEvent(()-> {
        //do stop attack
          this.playSound(SoundsHandler.B_KNIGHT_STOMP, 1.5f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
        new ActionProgressiveStomp((int) (this.getDistanceSq(target) + 2)).performAction(this, target);
        this.setShaking(true);
        this.shakeTime = 30;
      }, 43);

      addEvent(()-> {
            this.lockLook = false;
      }, 60);

      addEvent(()-> {
          this.setShaking(false);
      }, 73);

      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setStompAttack(false);
        this.setImmovable(false);
          attackCooldown = 40;
      }, 65);
    };

    Supplier<Projectile> ground_projectiles = () -> new ProjectileDesertStorm(world, this, (float) this.getAttack(), null);

    private final Consumer<EntityLivingBase> summon_storms = (target) -> {
      this.setFightMode(true);
      this.setSummonStorms(true);

      addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = true;
      }, 35);

      addEvent(()-> this.playSound(SoundsHandler.AEGYPTIA_SWING_STORM, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 40);

      addEvent(()-> {
        //summon tornadoes
          new ActionEveratorSummonStorms(ground_projectiles, 0.6F).performAction(this, target);
      }, 46);

      addEvent(()-> {
          this.setImmovable(false);
          this.lockLook = false;
      }, 70);

      addEvent(()-> {
          this.setSummonStorms(false);
          this.setFightMode(false);
          attackCooldown = 40;
      }, 80);
    };

    private final Consumer<EntityLivingBase> long_swing = (target) -> {
      this.setFightMode(true);
      this.setSwingThree(true);

        addEvent(()-> {
            this.lockLook = true;
            this.byPassStrafing = true;
        }, 20);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (this.getAttack());
            int fireFac = 0;
            if(this.isTributed) {
                fireFac = 4;
            }
            ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.5f, fireFac, false);
            this.destroyBlocksInSwing(new Vec3d(1.3, 1.2, 0), 2.0);
            this.playSound(SoundsHandler.EVERATOR_SWING, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 31);

        addEvent(()-> {
            this.lockLook = false;
            this.byPassStrafing = false;
        }, 40);

        addEvent(()-> {
            this.lockLook = true;
            this.byPassStrafing = true;
        }, 54);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack());
            int fireFac = 0;
            if(this.isTributed) {
                fireFac = 4;
            }
            ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.5f, fireFac, false);
            this.destroyBlocksInSwing(new Vec3d(1.3, 1.2, 0), 2.0);
            this.playSound(SoundsHandler.EVERATOR_SWING, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 64);

        addEvent(()-> {
            this.lockLook = false;
            this.byPassStrafing = false;
        }, 80);

        addEvent(()-> {
            this.lockLook = true;
            this.byPassStrafing = true;
        }, 110);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75, 0.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack() * 1.5);
            int fireFac = 0;
            if(this.isTributed) {
                fireFac = 4;
            }
            ModUtils.handleAreaImpact(3.0f, (e) -> damage, this, offset, source, 0.9f, fireFac, false);
            this.destroyBlocksInSwing(new Vec3d(0.75, 1.2, 0), 3.0);
            this.playSound(SoundsHandler.EVERATOR_SWING, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            this.setImmovable(true);
            //do arena Ground Slam
            new ActionEveratorLongSwing().performAction(this, target);
            this.setShaking(true);
            this.shakeTime = 30;
        }, 123);

        addEvent(()-> {
            this.setShaking(false);
        }, 153);


        addEvent(()-> {
            this.lockLook = false;
            this.byPassStrafing = false;
            this.setImmovable(false);
        }, 145);

        addEvent(()-> {
            this.setFightMode(false);
            this.setSwingThree(false);
            attackCooldown = 30;
        }, 155);
    };

    private final Consumer<EntityLivingBase> swing_attack = (target) -> {
      this.setFightMode(true);
      boolean randBoolean = rand.nextBoolean();

      if(randBoolean) {
        this.setSwingTwo(true);

          addEvent(()-> {
              this.lockLook = true;
              this.byPassStrafing = true;
          }, 20);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.2, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
              float damage =(float) (this.getAttack());
              int fireFac = 0;
              if(this.isTributed) {
                  fireFac = 4;
              }
              ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.5f, fireFac, false);
              this.destroyBlocksInSwing(new Vec3d(1.3, 1.2, 0), 2.0);
              this.playSound(SoundsHandler.EVERATOR_SWING, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          }, 31);

          addEvent(()-> {
              this.lockLook = false;
              this.byPassStrafing = false;
          }, 40);

          addEvent(()-> {
            this.lockLook = true;
            this.byPassStrafing = true;
          }, 54);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.2, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
              float damage =(float) (this.getAttack());
              int fireFac = 0;
              if(this.isTributed) {
                  fireFac = 4;
              }
              ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.5f, fireFac, false);
              this.destroyBlocksInSwing(new Vec3d(1.3, 1.2, 0), 2.0);
              this.playSound(SoundsHandler.EVERATOR_SWING, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          }, 64);

          addEvent(()-> {
              this.lockLook = false;
              this.byPassStrafing = false;
          }, 80);

          addEvent(()-> {
            this.setFightMode(false);
            this.setSwingTwo(false);
          }, 95);
      } else {
          this.setSwingOne(true);

          addEvent(()-> {
            this.lockLook = true;
            this.byPassStrafing = true;
          }, 20);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.2, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
              float damage =(float) (this.getAttack());
              int fireFac = 0;
              if(this.isTributed) {
                  fireFac = 4;
              }
              ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.5f, fireFac, false);
              this.destroyBlocksInSwing(new Vec3d(1.3, 1.2, 0), 2.0);
              this.playSound(SoundsHandler.EVERATOR_SWING, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          }, 31);

          addEvent(()-> {
              this.lockLook = false;
              this.byPassStrafing = false;
          }, 50);

          addEvent(()-> {
            this.setSwingOne(false);
            this.setFightMode(false);
          }, 60);
      }
    };


    private void destroyBlocksInSwing(Vec3d offset, double size) {
        AxisAlignedBB box = getEntityBoundingBox().grow(size, 0.1, size).offset(ModUtils.getRelativeOffset(this, new Vec3d(offset.x, 0.1, offset.z)));
        ModUtils.destroyBlocksInAABB(box, world, this);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "arms_controller_idle", 0, this::predicateArmsIdle));
        data.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "inactive_controller", 0, this::predicateInactive));
    }

    private <E extends IAnimatable> PlayState predicateInactive(AnimationEvent<E> event) {
        if(this.isInactiveMode() && !this.isAwake()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_INACTIVE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSwingOne()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_ONE, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingTwo()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_TWO, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingThree()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_THREE, false));
                return PlayState.CONTINUE;
            }
            if(this.isSummonStorms()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_STORMS, false));
                return PlayState.CONTINUE;
            }
            if(this.isCircleSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CIRCLE_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isStompAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STOMP, false));
                return PlayState.CONTINUE;
            }
            if(this.isTribute()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_TRIBUTE, false));
                return PlayState.CONTINUE;
            }
            if(this.isOverheadSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_OVERHEAD_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isAwake()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_AWAKE, false));
                return PlayState.CONTINUE;
            }
        }
         event.getController().markNeedsReload();
        return PlayState.STOP;
        }


    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage() && !this.isInactiveMode() && !this.isAwake()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArmsIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode() && !this.isInactiveMode() && !this.isAwake()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage() && !this.isInactiveMode() && !this.isAwake()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
           // event.getController().setAnimationSpeed(1.0 + (0.003 * event.getLimbSwing()));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode() && !this.isInactiveMode() && !this.isAwake()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
                event.getController().setAnimationSpeed(1.0 + (0.003 * event.getLimbSwing()));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
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
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if(this.isInactiveMode() || this.isAwake()) {
            return false;
        }

        if(this.isTributed) {
            return super.attackEntityFrom(source, amount * 0.5F);
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Main.proxy.spawnParticle(14, this.posX + pos.x, this.posY + 2.5, this.posZ + pos.z, 0,0.025,0);
            });
        }
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 24.0F);
            if (dist >= 24.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.9F * screamMult);
        }
        return 0;
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "everator");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.EVERATOR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.EVERATOR_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.EVERATOR_STEP, 0.55F, 0.4f + ModRand.getFloat(0.3F));
    }
}
