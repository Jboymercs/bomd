package com.dungeon_additions.da.entity.desert_dungeon.aegyptia;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.desert_dungeon.EntityAegyptiaAttackAI;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.desert_dungeon.EntityDesertBase;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileDesertStorm;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.player.EntityWyrkLazer;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class EntityAegyptia extends EntityDesertBase implements IAnimatable, IAnimationTickable, IAttack {
    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;

    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_IDLE_LOWER = "idle_lower";

    private final String ANIM_IDLE_UPPER = "idle_upper";
    private final String ANIM_IDLE_UPPER_NK = "idle_upper_nk";

    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_WALK_UPPER_NK = "walk_upper_nk";

    //idle jaw movement
    private String ANIM_JAW_MOVEMENT = "jaw_movement";
    public boolean byPassStrafing = false;

    //one kopis attack animations
    private final String ANIM_SWING = "swing";
    private final String ANIM_SWING_TWO = "swing_two";
    private final String ANIM_PIERCE = "pierce";
    private final String ANIM_MEGA_PIERCE = "mega_pierce";
    private final String ANIM_THROW_POTION = "throw_potion";

    //two kopis attacks
    private final String ANIM_DOUBLE_SWING = "double_swing";
    private final String ANIM_DOUBLE_SWING_CONT = "double_swing_cont";
    private final String ANIM_SIDE_SWING = "side_swing";
    private final String ANIM_JUMP_ATTACK = "jump_attack";
    private final String ANIM_CIRCLE_SWING = "circle_spin";

    private static final DataParameter<Boolean> HAS_KOPIS = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_POTION = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MOVE_JAW = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_TWO = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MEGA_PIERCE = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> THROW_POTION = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DOUBLE_SWING = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DOUBLE_SWING_CONT = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SIDE_SWING = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_ATTACK = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CIRCLE_SPIN = EntityDataManager.createKey(EntityAegyptia.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> SKIN_TYPE = EntityDataManager.<Integer>createKey(EntityAegyptia.class, DataSerializers.VARINT);

    public boolean isHasKopis() {return this.dataManager.get(HAS_KOPIS);}
    private void setHasKopis(boolean value) {this.dataManager.set(HAS_KOPIS, Boolean.valueOf(value));}
    private boolean isHasPotion() {return this.dataManager.get(HAS_POTION);}
    private void setSwingAttack(boolean value) {this.dataManager.set(SWING, Boolean.valueOf(value));}
    private boolean isSwingAttack() {return this.dataManager.get(SWING);}
    private void setSwingTwo(boolean value) {this.dataManager.set(SWING_TWO, Boolean.valueOf(value));}
    private boolean isSwingTwo() {return this.dataManager.get(SWING_TWO);}
    private void setPierce(boolean value) {this.dataManager.set(PIERCE, Boolean.valueOf(value));}
    private boolean isPierce() {return this.dataManager.get(PIERCE);}
    private void setMegaPierce(boolean value) {this.dataManager.set(MEGA_PIERCE, Boolean.valueOf(value));}
    private boolean isMegaPierce() {return this.dataManager.get(MEGA_PIERCE);}
    private void setThrowPotion(boolean value) {this.dataManager.set(THROW_POTION, Boolean.valueOf(value));}
    private boolean isThrowPotion() {return this.dataManager.get(THROW_POTION);}
    private void setDoubleSwing(boolean value) {this.dataManager.set(DOUBLE_SWING, Boolean.valueOf(value));}
    private boolean isDoubleSwing() {return this.dataManager.get(DOUBLE_SWING);}
    private void setDoubleSwingCont(boolean value) {this.dataManager.set(DOUBLE_SWING_CONT, Boolean.valueOf(value));}
    private boolean isDoubleSwingCont() {return this.dataManager.get(DOUBLE_SWING_CONT);}
    private void setSideSwing(boolean value) {this.dataManager.set(SIDE_SWING, Boolean.valueOf(value));}
    private boolean isSideSwing() {return this.dataManager.get(SIDE_SWING);}
    private void setJumpAttack(boolean value) {this.dataManager.set(JUMP_ATTACK, Boolean.valueOf(value));}
    private boolean isJumpAttack() {return this.dataManager.get(JUMP_ATTACK);}
    private void setCircleSpin(boolean value) {this.dataManager.set(CIRCLE_SPIN, Boolean.valueOf(value));}
    private boolean isCircleSpin() {return this.dataManager.get(CIRCLE_SPIN);}
    private void setMoveJaw(boolean value) {this.dataManager.set(MOVE_JAW, Boolean.valueOf(value));}
    private boolean isMoveJaw() {return this.dataManager.get(MOVE_JAW);}
    private void setHasPotion(boolean value) {this.dataManager.set(HAS_POTION, Boolean.valueOf(value));}
    public void setSkin(int skinType)
    {
        this.dataManager.set(SKIN_TYPE, Integer.valueOf(skinType));
    }

    public int getSkin()
    {
        return this.dataManager.get(SKIN_TYPE).intValue();
    }

    public EntityAegyptia(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 2.45F);
        this.experienceValue = 12;
        this.setSkin(rand.nextInt(4));
        int randI = ModRand.range(1, 10);
        this.setMoveJaw(false);
        if(randI >= 8) {
            this.setHasKopis(true);
        } else {
            this.setHasKopis(false);
        }
    }

    public EntityAegyptia(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 2.45F);
        this.experienceValue = 12;
        this.setSkin(rand.nextInt(4));
        int randI = ModRand.range(1, 11);
        this.setMoveJaw(false);
        if(randI >= 8) {
            this.setHasKopis(true);
        } else {
            this.setHasKopis(false);
        }
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData) {
        this.setSkin(rand.nextInt(4));
        int randI = ModRand.range(1, 11);
        if(randI >= 8) {
            this.setHasKopis(true);
        }
        return super.onInitialSpawn(difficulty, entityLivingData);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Has_Kopis", this.isHasKopis());
        nbt.setBoolean("Has_Potion", this.isHasPotion());
        nbt.setBoolean("Swing", this.isSwingAttack());
        nbt.setBoolean("Swing_Two", this.isSwingTwo());
        nbt.setBoolean("Piece", this.isPierce());
        nbt.setBoolean("Mega_Pierce", this.isMegaPierce());
        nbt.setBoolean("Throw_Potion", this.isThrowPotion());
        nbt.setBoolean("Double_Swing", this.isDoubleSwing());
        nbt.setBoolean("Double_Swing_Cont", this.isDoubleSwingCont());
        nbt.setBoolean("Jump_Attack", this.isJumpAttack());
        nbt.setBoolean("Circle_Spin", this.isCircleSpin());
        nbt.setBoolean("Side_Swing", this.isSideSwing());
        nbt.setBoolean("Move_Jaw", this.isMoveJaw());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setHasKopis(nbt.getBoolean("Has_Kopis"));
        this.setHasPotion(nbt.getBoolean("Has_Potion"));
        this.setSwingAttack(nbt.getBoolean("Swing"));
        this.setSwingTwo(nbt.getBoolean("Swing_Two"));
        this.setPierce(nbt.getBoolean("Pierce"));
        this.setMegaPierce(nbt.getBoolean("Mega_Pierce"));
        this.setThrowPotion(nbt.getBoolean("Throw_Potion"));
        this.setDoubleSwing(nbt.getBoolean("Double_Swing"));
        this.setDoubleSwingCont(nbt.getBoolean("Double_Swing_Cont"));
        this.setSideSwing(nbt.getBoolean("Side_Swing"));
        this.setJumpAttack(nbt.getBoolean("Jump_Attack"));
        this.setCircleSpin(nbt.getBoolean("Circle_Spin"));
        this.setMoveJaw(nbt.getBoolean("Move_Jaw"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.getDataManager().register(SKIN_TYPE, Integer.valueOf(0));
        this.dataManager.register(HAS_KOPIS, Boolean.valueOf(false));
        this.dataManager.register(HAS_POTION, Boolean.valueOf(false));
        this.dataManager.register(SWING, Boolean.valueOf(false));
        this.dataManager.register(SWING_TWO, Boolean.valueOf(false));
        this.dataManager.register(PIERCE, Boolean.valueOf(false));
        this.dataManager.register(MEGA_PIERCE, Boolean.valueOf(false));
        this.dataManager.register(THROW_POTION, Boolean.valueOf(false));
        this.dataManager.register(SIDE_SWING, Boolean.valueOf(false));
        this.dataManager.register(DOUBLE_SWING, Boolean.valueOf(false));
        this.dataManager.register(DOUBLE_SWING_CONT, Boolean.valueOf(false));
        this.dataManager.register(CIRCLE_SPIN, Boolean.valueOf(false));
        this.dataManager.register(JUMP_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(MOVE_JAW, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.aegyptia_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.aegyptia_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.aegyptia_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAegyptiaAttackAI<>(this, 1.0D, 10, 5, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityDarkBase>(this, EntityDarkBase.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    public boolean enableCircleSpin = false;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.ticksExisted % 180 == 0 && !this.isMoveJaw()) {
            this.handleJawMovement();
        }

        if(!world.isRemote) {
            EntityLivingBase target = this.getAttackTarget();
            //far distance fight manager
            if(target != null && !this.isFightMode()) {
                if(this.isHasKopis()) {


                    if(this.getDistance(target) > 5 && this.ticksExisted % 300 == 0) {
                        if(this.getDistance(target) < 9 && prevAttack != do_jump_attack) {
                            prevAttack = do_jump_attack;
                            prevAttack.accept(target);
                        } else if (this.getDistance(target) < 16) {
                            prevAttack = do_side_swing;
                            prevAttack.accept(target);
                        }
                    }
                } else {
                    //do the mega pierce if target is far away
                    if(this.getDistance(target) > 5 && this.getDistance(target) < 11 && this.ticksExisted % 300 == 0) {
                        prevAttack = do_mega_pierce;
                        prevAttack.accept(target);
                    }
                }
            }

            //circle Spin
            if(this.enableCircleSpin && target != null) {
                double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
                double distance = Math.sqrt(distSq);
                double d0 = (target.posX - this.posX) * 0.007;
                double d1 = (target.posY - this.posY) * 0.009;
                double d2 = (target.posZ - this.posZ) * 0.007;
                this.addVelocity(d0, d1, d2);
            }
        }
    }


    private void handleJawMovement() {
        int randI = ModRand.range(1, 4);
        if(randI == 1) {
            ANIM_JAW_MOVEMENT = "jaw_movement_2";
        } else if (randI == 2) {
            ANIM_JAW_MOVEMENT = "jaw_movement_3";
        } else {
            ANIM_JAW_MOVEMENT = "jaw_movement";
        }
        this.setMoveJaw(true);
        this.playSound(SoundsHandler.AEGYPTIA_IDLE, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));

        if(randI == 1) {
            addEvent(()-> this.setMoveJaw(false), 25);
        } else if (randI == 2) {
            addEvent(()-> this.setMoveJaw(false), 50);
        } else {
            addEvent(()-> this.setMoveJaw(false), 40);
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            if(this.isHasKopis()) {
                List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(do_double_swing, do_circle_spin, do_side_swing, do_jump_attack));
                double[] weights = {
                        (prevAttack != do_double_swing) ? 1/distance : 0,
                        (prevAttack != do_circle_spin) ? 1/distance : 0,
                        (prevAttack != do_side_swing) ? 1/distance : 0,
                        (prevAttack != do_jump_attack) ? 1/distance : 0

                };
                prevAttack = ModRand.choice(attacksMelee, rand, weights).next();
            } else {
                //basic attacks
                if(distance <= 6) {
                    List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(do_swing_attack, do_pierce_attack, do_mega_pierce, do_throw_potion));
                    double[] weights = {
                            (prevAttack != do_swing_attack) ? 1/distance : 0,
                            (prevAttack != do_pierce_attack) ? 1/distance : 0,
                            (prevAttack != do_mega_pierce) ? 1/distance : 0,
                            (this.isHasPotion()) ? 1.4/distance : 0
                    };
                    prevAttack = ModRand.choice(attacksMelee, rand, weights).next();
                }
            }

            prevAttack.accept(target);
        }
        return this.isHasKopis() ? 10 : 20;
    }

    private final Consumer<EntityLivingBase> do_jump_attack = (target) -> {
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.setFightMode(true);
      this.setJumpAttack(true);

      addEvent(()-> {
          Vec3d targetOldPos = target.getPositionVector();
          addEvent(()-> {
              Vec3d targetedPos = target.getPositionVector();
              Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 3);
              this.lockLook = true;
              addEvent(()-> {
                  this.setImmovable(false);
                  double distance = this.getPositionVector().distanceTo(predictedPosition);
                  ModUtils.leapTowards(this, predictedPosition, (float) (distance * 0.25),0.3F);
                  this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.5f / (rand.nextFloat() * 0.4f + 0.2f));
              }, 5);
          }, 3);
      }, 25);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.7f, 0, false);
            this.playSound(SoundsHandler.B_KNIGHT_STOMP, 0.7f, 1.1f / (rand.nextFloat() * 0.4f + 0.2f));
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            this.setImmovable(true);
        }, 50);

        addEvent(()-> {
            this.lockLook =false;
        }, 70);

      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setImmovable(false);
        this.setFightMode(false);
        this.setJumpAttack(false);
      }, 80);
    };

    private final Consumer<EntityLivingBase> do_side_swing = (target) -> {
      this.setSideSwing(true);
      this.setFightMode(true);
      this.byPassStrafing = true;

      addEvent(()-> {
        this.setImmovable(true);
      }, 25);

      addEvent(()-> {
          this.lockLook = true;
          this.playSound(SoundsHandler.AEGYPTIA_SWING_STORM, 0.8f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 35);
      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage =(float) (this.getAttack());
          ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.7f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.4f / (rand.nextFloat() * 0.4f + 0.2f));
          float inaccuracy = 0.0f;
          float speed = 0.6f;
          float pitch = 0;
          ProjectileDesertStorm storm = new ProjectileDesertStorm(world, this, this.getAttack(), null);
          Vec3d relPos3 = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0, 0)));
          storm.setPosition(relPos3.x, relPos3.y, relPos3.z);
          storm.shoot(this, pitch, this.rotationYaw, 0.0F, speed, inaccuracy);
          storm.setTravelRange(16);
          world.spawnEntity(storm);

          //spawn Tornado
      }, 42);

      addEvent(()-> {
        this.setImmovable(false);
        this.lockLook = false;
        this.byPassStrafing = false;
      }, 55);

      addEvent(()-> {
        this.setSideSwing(false);
        this.setFightMode(false);
      }, 70);
    };
    private final Consumer<EntityLivingBase> do_circle_spin = (target) -> {
      this.setCircleSpin(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.setFightMode(true);

      addEvent(() -> {
          this.setImmovable(false);
          this.byPassStrafing = true;
          this.lockLook = true;
          this.enableCircleSpin = true;
          for(int i = 0; i <= 45; i += 5) {
              addEvent(()-> {
                  Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0)));
                  DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                  float damage =(float) (this.getAttack() * 0.75);
                  ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.9f, 0, false);
                  this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.4f / (rand.nextFloat() * 0.4f + 0.2f));
              }, i);
          }
      }, 35);

      addEvent(()-> {
        this.setImmovable(true);
        this.enableCircleSpin = false;
      }, 80);

      addEvent(()-> {
          this.lockLook = false;
        this.setCircleSpin(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
        this.setFightMode(false);
      }, 110);
    };
    private final Consumer<EntityLivingBase> do_double_swing = (target) -> {
      this.setFightMode(true);
      this.byPassStrafing = true;
        boolean randI = new Random().nextBoolean();

        if(randI) {
            this.setDoubleSwingCont(true);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.4f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 33);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.4f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 54);

            addEvent(()-> this.setImmovable(true), 65);

            addEvent(()-> {
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
                this.lockLook = true;
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
                }, 5);
            }, 74);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.4f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 85);

            addEvent(()-> {
                this.lockLook = false;
                this.byPassStrafing = false;
            }, 100);

            addEvent(() -> {
                this.setDoubleSwingCont(false);
                this.setFightMode(false);
            }, 110);
        } else {
            this.setDoubleSwing(true);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.4f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 33);


            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.4f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 54);

            addEvent(()-> this.byPassStrafing = false, 60);

            addEvent(()-> {
                this.setFightMode(false);
                this.setDoubleSwing(false);
            }, 70);
        }
    };

    private final Consumer<EntityLivingBase> do_throw_potion = (target) -> {
      this.setFightMode(true);
      this.setThrowPotion(true);

      addEvent(()-> {
        //throw Potion
          this.setHasPotion(false);
      }, 30);

      addEvent(()-> {
        this.setFightMode(false);
        this.setThrowPotion(false);
      }, 50);
    };

    private final Consumer<EntityLivingBase> do_mega_pierce = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setMegaPierce(true);
      this.setImmovable(true);
        this.playSound(SoundsHandler.AEGYPTIA_PIERCE, 0.8f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.4f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 45);

        addEvent(()-> {
            Vec3d lazerEnd = this.getPositionEyes(1).add(this.getLookVec().scale(12));
            RayTraceResult raytraceresult = world.rayTraceBlocks(this.getPositionEyes(1).subtract(0, 0.5, 0), lazerEnd.subtract(0, 0.5, 0), false, true, false);

            if (raytraceresult != null) {
                // If we hit a block, make sure that any collisions with entities are detected up to the hit block
                lazerEnd = raytraceresult.hitVec;
            }

            Entity closestEntity = null;
            for (Entity entity : ModUtils.findEntitiesInLine(this.getPositionEyes(1).subtract(0, 0.5, 0), lazerEnd.subtract(0, 0.5, 0), world, this)) {
                if (entity.canBeCollidedWith() && (closestEntity == null || entity.getDistanceSq(this) < closestEntity.getDistanceSq(this))) {
                    if(!(entity instanceof EntityDesertBase)) {
                        closestEntity = entity;
                    }
                }

            }

            if (closestEntity != null) {
                if (closestEntity instanceof IEntityMultiPart) {
                    if(closestEntity.getParts() != null) {
                        MultiPartEntityPart closestPart = null;
                        for (Entity entity : closestEntity.getParts()) {
                            RayTraceResult result = entity.getEntityBoundingBox().calculateIntercept(this.getPositionEyes(1), lazerEnd);
                            if (result != null) {
                                if (entity instanceof MultiPartEntityPart && (closestPart == null || entity.getDistanceSq(this) < closestPart.getDistanceSq(this))) {
                                    closestPart = (MultiPartEntityPart) entity;
                                }
                            }
                        }
                        if (closestPart != null) {
                            ((IEntityMultiPart) closestEntity).attackEntityFromPart(closestPart, ModUtils.causeStaffDamage(this).setDamageBypassesArmor(), (float) (this.getAttack() * 0.25));
                        }
                    }
                } else {
                    closestEntity.attackEntityFrom(ModUtils.causeStaffDamage(this).setDamageBypassesArmor(), (float) (this.getAttack() * 0.25));
                }
            }

            EntityDesertBeam renderer = new EntityDesertBeam(world, this.getPositionEyes(1).subtract(0, 0.5, 0));
            renderer.setPosition(lazerEnd.x, lazerEnd.y, lazerEnd.z);
            world.spawnEntity(renderer);
        }, 50);

      addEvent(()-> {
        this.setImmovable(false);
        this.setMegaPierce(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
      }, 70);
    };

    private final Consumer<EntityLivingBase> do_pierce_attack = (target) -> {
      this.setPierce(true);
      this.setFightMode(true);
      this.byPassStrafing = true;

      addEvent(()-> this.lockLook = true, 20);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage =(float) (this.getAttack());
          ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.4f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 35);

      addEvent(()-> {
        this.lockLook = false;
        this.byPassStrafing = false;
      }, 45);

      addEvent(()-> {
          this.setPierce(false);
          this.setFightMode(false);
      }, 55);
    };

    private final Consumer<EntityLivingBase> do_swing_attack = (target) -> {
        this.setFightMode(true);
        this.byPassStrafing = true;
        boolean randI = new Random().nextBoolean();

        //two swings
        if(randI) {
            this.setSwingTwo(true);

            addEvent(()-> {
                this.lockLook = true;
            }, 16);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.4f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 25);

            addEvent(() -> {
                this.lockLook = false;
            }, 38);

            addEvent(()-> {
                this.lockLook = true;
            }, 60);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.4f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 75);

            addEvent(()-> {
                this.lockLook = false;
                this.byPassStrafing = false;
            }, 95);

            addEvent(()-> {
                this.setSwingTwo(false);
                this.setFightMode(false);
            }, 110);
        } else {
            //one swing
            this.setSwingAttack(true);

            addEvent(()-> {
            this.lockLook = true;
            }, 16);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.4f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 25);

            addEvent(() -> {
                this.lockLook = false;
                this.byPassStrafing = false;
            }, 38);

            addEvent(()-> {
                this.setSwingAttack(false);
                this.setFightMode(false);
            }, 55);
        }
    };


    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.performNTimes(9, (i) -> {
            int randI = ModRand.range(-3, 4);
            int randB = ModRand.range(-3, 4);
            IBlockState block = world.getBlockState(new BlockPos(this.posX + randI, this.posY - 1, this.posZ + randB));
            if (block.isFullCube()) {
                for (int c = 0; c < 5; c++) {
                    ParticleManager.spawnBreak(world, this.getPositionVector().add(ModRand.randVec().scale(1.0f).add(randI, 0.75F, randB)), Item.getItemFromBlock(block.getBlock()), ModRand.randVec().scale(0.1).add(ModUtils.yVec(0.1f)));
                }
            }
            });
        }

        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.25, 0))).add(pos), ModColors.YELLOW, new Vec3d(0, 0.04, 0), ModRand.range(50, 80));
            });
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "arms_controller_idle", 0, this::predicateArmsIdle));
        data.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "jaw_movement_controller", 0, this::predicateMoveJaw));
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateMoveJaw(AnimationEvent<E> event) {
        if(this.isMoveJaw()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_JAW_MOVEMENT, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSwingAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingTwo()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_TWO, false));
                return PlayState.CONTINUE;
            }
            if(this.isPierce()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PIERCE, false));
                return PlayState.CONTINUE;
            }
            if(this.isMegaPierce()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MEGA_PIERCE, false));
                return PlayState.CONTINUE;
            }
            if(this.isThrowPotion()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_THROW_POTION, false));
                return PlayState.CONTINUE;
            }

            if(this.isHasKopis()) {
                if(this.isSideSwing()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SIDE_SWING, false));
                    return PlayState.CONTINUE;
                }
                if(this.isDoubleSwing()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DOUBLE_SWING, false));
                    return PlayState.CONTINUE;
                }
                if(this.isDoubleSwingCont()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DOUBLE_SWING_CONT, false));
                    return PlayState.CONTINUE;
                }
                if(this.isCircleSpin()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CIRCLE_SWING, false));
                    return PlayState.CONTINUE;
                }
                if(this.isJumpAttack()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_JUMP_ATTACK, false));
                    return PlayState.CONTINUE;
                }
            }

        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArmsIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode()) {
            if(this.isHasKopis()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER_NK, true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            event.getController().setAnimationSpeed(1.0 + (0.003 * event.getLimbSwing()));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode()) {
            if(this.isHasKopis()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
                event.getController().setAnimationSpeed(1.0 + (0.003 * event.getLimbSwing()));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER_NK, true));
                event.getController().setAnimationSpeed(1.0 + (0.003 * event.getLimbSwing()));
            }
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
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "aegyptia");

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
        return SoundsHandler.AEGYPTIA_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.AEGYPTIA_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.AEGYPTIA_STEP, 0.35F, 0.4f + ModRand.getFloat(0.3F));
    }
}
