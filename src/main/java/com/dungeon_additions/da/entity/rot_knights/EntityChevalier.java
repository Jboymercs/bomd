package com.dungeon_additions.da.entity.rot_knights;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.EntityAIChevalierAttack;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.rot_knights.actions.ActionChevCloud;
import com.dungeon_additions.da.entity.rot_knights.actions.ActionChevStomp;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import mod.azure.azurelib.util.MoveAnalysis;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityChevalier extends EntityRotBase implements IAttack {
    public boolean isRandomGetAway = false;
    private int swirlTime = 100;
    private float randomTurn = ModRand.range(1, 360);
    private int focusTime = 0;
    public boolean focusMode = false;
    private Vec3d aimedPos = null;
    private int blockTimer = 0;
    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_IDLE_POS = "idle_pos";
    private final String ANIM_END_IDLE_POS = "end_idle_pos";
    //Attacks
    private final String ANIM_STOMP = "stomp";
    private final String ANIM_SWING = "swing";
    private final String ANIM_SWING_COMBO = "swing_combo";
    private final String ANIM_PARRY = "parry";
    private final String ANIM_HEAVY_SWING = "heavy_swing";
    private final String ANIM_HEAVY_SWING_COMBO = "heavy_swing_combo";
    private final String  ANIM_BEGIN_SWIRL = "begin_swinl";
    private final String ANIM_SWIRLD = "swirl";
    private final String ANIM_END_SWIRL = "end_swirl";
    private final String ANIM_ENTER_FOCUS = "enter_focus";
    private final String ANIM_FOCUS_ATTACK = "focus_attack";
    private final String ANIM_SUMMON_PROJECTILE = "summon_projectile";
    private final String ANIM_END_FOCUS = "end_focus";
    private final String ANIM_FOCUS_LOOP = "focus_loop";

    private Consumer<EntityLivingBase> prevAttack;

    private static final DataParameter<Boolean> PARRY = EntityDataManager.createKey(EntityChevalier.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWIRL = EntityDataManager.createKey(EntityChevalier.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HYPER_FOCUS_LOOP = EntityDataManager.createKey(EntityChevalier.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IDLE_POS = EntityDataManager.createKey(EntityChevalier.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DUNGEON_SPAWN = EntityDataManager.createKey(EntityChevalier.class, DataSerializers.BOOLEAN);

    private boolean isParry() {return this.dataManager.get(PARRY);}
    private void setParry(boolean value) {this.dataManager.set(PARRY, Boolean.valueOf(value));}
    private boolean isSwirl() {return this.dataManager.get(SWIRL);}
    private void setSwirl(boolean value) {this.dataManager.set(SWIRL, Boolean.valueOf(value));}
    private boolean isFocusLoop() {return this.dataManager.get(HYPER_FOCUS_LOOP);}
    private void setFocusLoop(boolean value) {this.dataManager.set(HYPER_FOCUS_LOOP, Boolean.valueOf(value));}
    private boolean isIdlePos() {return this.dataManager.get(IDLE_POS);}
    private void setIdlePos(boolean value) {this.dataManager.set(IDLE_POS, Boolean.valueOf(value));}
    private boolean isDungeonSpawn() {return this.dataManager.get(DUNGEON_SPAWN);}
    public void setDungeonSpawn(boolean value) {this.dataManager.set(DUNGEON_SPAWN, Boolean.valueOf(value));}

    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, true);
    private final MoveAnalysis moveAnalysis;

    public EntityChevalier(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.75F, 2.15F);
        this.hemorrhage_resistance = 0.6F;
        this.falter_resistance = 1.2F;
        this.setIdlePos(true);
        this.setImmovable(true);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public EntityChevalier(World worldIn) {
        super(worldIn);
        this.setSize(0.75F, 2.15F);
        this.hemorrhage_resistance = 0.6F;
        this.falter_resistance = 1.2F;
        this.setIdlePos(true);
        this.setImmovable(true);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Parry", this.isParry());
        nbt.setBoolean("Swirl", this.isSwirl());
        nbt.setBoolean("Hyper_Focus_Loop", this.isFocusLoop());
        nbt.setBoolean("Idle_Pos", this.isIdlePos());
        nbt.setBoolean("Dungeon_Spawn", this.isDungeonSpawn());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setParry(nbt.getBoolean("Parry"));
        this.setSwirl(nbt.getBoolean("Swirl"));
        this.setFocusLoop(nbt.getBoolean("Hyper_Focus_Loop"));
        this.setIdlePos(nbt.getBoolean("Idle_Pos"));
        this.setDungeonSpawn(nbt.getBoolean("Dungeon_Spawn"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(PARRY, Boolean.valueOf(false));
        this.dataManager.register(SWIRL, Boolean.valueOf(false));
        this.dataManager.register(HYPER_FOCUS_LOOP, Boolean.valueOf(false));
        this.dataManager.register(IDLE_POS, Boolean.valueOf(false));
        this.dataManager.register(DUNGEON_SPAWN, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.chevalier_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.chevalier_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.chevalier_armor);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.moveAnalysis.update();
        this.blockTimer--;
        if(!world.isRemote) {

            if(this.isIdlePos()) {
                this.rotationYaw = randomTurn;
                this.rotationYawHead = randomTurn;
            }

            if(this.getAttackTarget() != null) {
                EntityLivingBase target = this.getAttackTarget();

                if(this.isIdlePos() && this.getDistance(target) <= 25) {
                    this.endIdleState();
                }
                //Random Getaway
                if(this.isRandomGetAway) {
                    double d0 = (this.posX - target.posX) * 0.012;
                    double d1 = (this.posY - target.posY) * 0.005;
                    double d2 = (this.posZ - target.posZ) * 0.012;
                    this.addVelocity(d0, d1, d2);
                    this.faceEntity(target, 35, 35);
                    this.getLookHelper().setLookPositionWithEntity(target, 35, 35);
                }

                //Handler for Focus Mode
                if(this.focusMode) {
                    //do the attack
                    if(this.focusTime <= 0 || this.getDistance(target) <= 3 || this.hurtTime > 0) {
                        this.doFocusAttack(target);
                    }
                    this.focusTime--;
                }

                //Handler for Swirling
                if(this.isSwirl()) {
                    if(this.swirlTime <= 0) {
                        this.endSwirlAttack();
                        this.aimedPos = null;
                    }

                    if(this.ticksExisted % 10 == 0) {
                        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0)));
                        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                        float damage = this.getAttack();
                        ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.4f, 0, false, 0.4F);
                        this.playSound(SoundsHandler.SWING_HEAVY, 0.6f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
                    }

                    if(aimedPos != null) {
                        double d0 = (aimedPos.x - this.posX) * 0.004;
                        double d2 = (aimedPos.z - this.posZ) * 0.004;
                        this.addVelocity(d0, 0, d2);
                       // this.faceEntity(target, 35, 35);
                       // this.getLookHelper().setLookPositionWithEntity(target, 35, 35);

                        AxisAlignedBB box = this.getEntityBoundingBox().grow(0.75D, 0.5D, 0.75D).offset(0, 1, 0);

                        //reset and recalculate if running into blocks
                        if(this.getDistance(aimedPos.x, this.posY, aimedPos.z) <= 1 || ModUtils.collisionNearby(box, world, this)) {
                            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                            Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(47));
                            aimedPos = new Vec3d(softTargetPos.x, this.posY, softTargetPos.z);
                            //do some particle effects and sound queue to show a difference
                        }
                    }
                    this.swirlTime--;
                }
            }

            if(this.getAttackTarget() == null && this.focusMode) {
                this.endFocusMode();
            }
        }
    }

    private void endIdleState() {
        this.setIdlePos(false);
        animator.setStateAnim(ANIM_END_IDLE_POS);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.lockLook = true;

        addEvent(()-> this.lockLook = false, 15);
        addEvent(()-> {
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
        }, 20);
    }

    private void endFocusMode() {
        this.focusTime = 0;
        this.focusMode = false;
        this.setImmovable(true);
        this.setFocusLoop(false);
        animator.performAttack(ANIM_END_FOCUS);

        addEvent(()-> {
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
        }, 15);
    }

    private void endSwirlAttack() {
        this.setSwirl(false);
        animator.performAttack(ANIM_END_SWIRL);
        this.setImmovable(true);

        addEvent(() -> {
            this.lockLook = false;
        }, 30);

        addEvent(()-> {
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
        }, 40);
    }

    private void doFocusAttack(EntityLivingBase target) {
        this.focusMode = false;
        this.focusTime = 0;
        this.setFocusLoop(false);
        animator.performAttack(ANIM_FOCUS_ATTACK);
        this.setImmovable(true);
        this.setFullBodyUsage(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(softTargetPos);
                ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.28),0F);
            }, 2);
        }, 7);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 0.75, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.9F);
            this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 14);

        addEvent(()-> {
            this.setImmovable(true);
        }, 25);

        addEvent(()-> {
            this.lockLook = false;
        }, 30);

        addEvent(()-> {
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setImmovable(false);
        }, 40);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIChevalierAttack<>(this, 1.1, 20, 7, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityZombie>(this, EntityZombie.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityZombieVillager>(this, EntityZombieVillager.class, 1, true, false, null));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget<EntityHusk>(this, EntityHusk.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode() && !this.isIdlePos()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(swing, heavy_swing, stomp, begin_swirl, summon_projectiles, enter_focus));
            double[] weights = {
                    (distance <= 8 && prevAttack != swing) ? 1/distance : 0, // Swing
                    (distance <= 8 && prevAttack != heavy_swing) ? 1/distance : 0, //Heavy Swing
                    (distance <= 7 && prevAttack != stomp) ? 1/distance : 0, //Stomp
                    (distance <= 8 && prevAttack != begin_swirl) ? 1/distance : 0, //Swirl
                    (distance <= 6 && prevAttack != summon_projectiles) ? 1/distance : 0, //Summon Projectiles
                    (distance <= 8 && prevAttack != enter_focus) ? 1/distance : 0 //Focus Mode
            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        return 20;
    }

    private final Consumer<EntityLivingBase> enter_focus = (target) -> {
        animator.performAttack(ANIM_ENTER_FOCUS);
        this.setFightMode(true);
        this.setImmovable(true);
        addEvent(()-> {
            this.setImmovable(false);
            this.setFocusLoop(true);
            this.focusMode = true;
            this.focusTime = 40 + ModRand.range(30, 80);
        }, 15);
    };

    private final Consumer<EntityLivingBase> summon_projectiles = (target) -> {
      this.setFightMode(true);
      this.isRandomGetAway = true;
      animator.performAttack(ANIM_SUMMON_PROJECTILE);

      addEvent(()-> {
        new ActionChevCloud().performAction(this, target);
      }, 23);

      addEvent(()-> {
          this.isRandomGetAway = false;
            this.setFightMode(false);
      }, 40);
    };

    private final Consumer<EntityLivingBase> begin_swirl = (target) -> {
    this.setFightMode(true);
    this.setFullBodyUsage(true);
    this.setImmovable(true);
    animator.performAttack(ANIM_BEGIN_SWIRL);

    addEvent(()-> {
        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0)));
        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
        float damage = this.getAttack();
        ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.4f, 0, false, 0.4F);
        this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
        this.lockLook = true;
        //starts swirl attack
        Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
        Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(36));
        this.setImmovable(false);
        aimedPos = new Vec3d(softTargetPos.x, this.posY, softTargetPos.z);
        this.swirlTime = ModRand.range(3, 8) * 20;
        this.setSwirl(true);
    }, 20);
    };


    private final Consumer<EntityLivingBase> stomp = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
      animator.performAttack(ANIM_STOMP);

      addEvent(()-> this.lockLook = true, 10);
      addEvent(()-> {
            //do stomp
          this.playSound(SoundsHandler.B_KNIGHT_STOMP, 1.3f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
          new ActionChevStomp().performAction(this, target);
      }, 18);

      addEvent(()-> {
          this.lockLook = false;
      }, 25);

      addEvent(()-> {
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setImmovable(false);
      }, 30);
    };

    private final Consumer<EntityLivingBase> heavy_swing = (target) -> {
        boolean swingRand = rand.nextBoolean();
        this.setFightMode(true);
        this.setImmovable(true);
        this.setFullBodyUsage(true);

        if(swingRand) {
            animator.performAttack(ANIM_HEAVY_SWING);
                addEvent(()-> {
                    this.lockLook = true;
                    Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                    Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(4));
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.07),0F);
                }, 22);

                addEvent(()-> this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 24);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 0.1, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.7f, 0, false, 0.6F);
                this.setImmovable(true);
            }, 30);

            addEvent(()-> {
                this.lockLook = false;
            }, 40);

            addEvent(()-> {
                this.setImmovable(false);
                this.setFullBodyUsage(false);
                this.setFightMode(false);
            }, 50);
        } else {
            animator.performAttack(ANIM_HEAVY_SWING_COMBO);
            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(4));
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(softTargetPos);
                ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.07),0F);
            }, 22);

            addEvent(()-> this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 24);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 0.1, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.7f, 0, false, 0.6F);
                this.setImmovable(true);
            }, 30);

            addEvent(()-> {
                this.lockLook = false;
            }, 40);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-1));
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.23),0F);
                }, 2);
            }, 59);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 0.1, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.6F);
                this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
                this.setImmovable(true);
            }, 66);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 80);

            addEvent(()-> {
                this.setFightMode(false);
                this.setFullBodyUsage(false);
                this.setImmovable(false);
            }, 90);

        }
    };

    private final Consumer<EntityLivingBase> swing = (target) -> {
        boolean swingRand = rand.nextBoolean();
        this.setFightMode(true);
        this.setImmovable(true);
        this.setFullBodyUsage(true);

        if(swingRand) {
            animator.performAttack(ANIM_SWING);
            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-1));
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.21),0F);
                }, 5);
            }, 10);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.4F);
                this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
            }, 22);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 30);

            addEvent(()-> {
                this.setFightMode(false);
                this.setFullBodyUsage(false);
                this.setImmovable(false);
            }, 40);
        } else {
            animator.performAttack(ANIM_SWING_COMBO);
            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-1));
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.21),0F);
                }, 5);
            }, 10);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.4F);
                this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
            }, 22);


            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 25);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.31),0F);
                }, 5);
            }, 36);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.4F);
                this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f));
            }, 47);

            addEvent(()-> this.setImmovable(true), 55);
            addEvent(()-> this.lockLook = false, 60);
            addEvent(()-> {
                    this.setImmovable(false);
                    this.setFightMode(false);
                    this.setFullBodyUsage(false);
            }, 70);
        }
    };

    @Override
    public void updateAnimations() {
        if(this.isIdlePos()) {
            animator.setStateAnim(ANIM_IDLE_POS, true);
        }
        if(this.isFocusLoop()) {
            animator.performAttack(ANIM_FOCUS_LOOP, true);
        }
        if(this.isSwirl()) {
            animator.performAttack(ANIM_SWIRLD, true);
        }

        if(!this.isFullBodyUsage() && !this.isIdlePos()) {
            boolean isMovingOnGround = moveAnalysis.isMovingHorizontally() && this.onGround;
            if(isMovingOnGround) {
                if(!this.isFightMode()) {
                    animator.setCustomUpperWalk(ANIM_WALK_UPPER);
                }
                animator.setCustomLowerWalk(ANIM_WALK_LOWER);
            } else if(!this.isFightMode()) {
                animator.setCustomUpperIdle(ANIM_IDLE);
            }
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.ROT_KNIGHT_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.ROT_KNIGHT_WALK, 0.2F, 0.6f + ModRand.getFloat(0.3F));
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ROT_KNIGHT_DEATH;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if(source.getImmediateSource() instanceof EntityRotKnight || source.getImmediateSource() instanceof EntityRotKnightRapier || this.isIdlePos() || this.isParry()) {
            return false;
        }

        if(amount > 0.0F && this.canBlockDamageSource(source) && !this.isFightMode()) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
                return false;
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if (!damageSourceIn.isUnblockable() && this.blockTimer < 0 && !this.isParry() && !this.isFightMode()) {
            Vec3d vec3d = damageSourceIn.getDamageLocation();
            //Handler for other
            if (vec3d != null) {
                Vec3d vec3d1 = this.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);
                Vec3d targetPos = null;
                if(damageSourceIn.getTrueSource() instanceof EntityLivingBase) {
                    targetPos = damageSourceIn.getTrueSource().getPositionVector();
                }
                this.doBlockAction(targetPos);
                return vec3d2.dotProduct(vec3d1) < 0.5D;
            }
        }

        return false;
    }

    private void doBlockAction(Vec3d targetPos) {
        this.setParry(true);
        animator.performCombo(ANIM_PARRY);
        this.setFightMode(true);
        this.setImmovable(true);
        this.blockTimer = 100;
        this.playSound(SoundsHandler.VOLACTILE_SHIELD_BLOCK, 0.7f, 0.3f / (rand.nextFloat() * 0.4F + 0.4f));
        addEvent(()-> {
            if(targetPos != null) {
                Vec3d offset = targetPos.add(0, 0.25, 0);
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = 1F;
                ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.7F);
            }
        }, 5);

        addEvent(()-> {
            this.setParry(false);
            this.setFightMode(false);
            this.setImmovable(false);
            this.isRandomGetAway = false;
        }, 25);
    }

    private static final ResourceLocation LOOT_DUNGEON = new ResourceLocation(ModReference.MOD_ID, "chevalier_dungeon");
    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "rot_knight");
    @Override
    protected ResourceLocation getLootTable() {
        if(this.isDungeonSpawn()) {
            return LOOT_DUNGEON;
        }
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }
}
