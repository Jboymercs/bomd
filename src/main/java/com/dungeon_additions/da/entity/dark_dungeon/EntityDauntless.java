package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.EntityDraugrMeleeAI;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityDauntlessAttackAI;
import com.dungeon_additions.da.entity.ai.flying.FlyingMoveHelper;
import com.dungeon_additions.da.entity.ai.flying.TimedAttackInitiator;
import com.dungeon_additions.da.entity.ai.void_dungeon.EntityVoidclysmAttackAI;
import com.dungeon_additions.da.entity.blossom.EntityAbstractVoidBlossom;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.ActionDauntlessSelfAOE;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.ActionDauntlessThrowSword;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDauntlessAOE;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDauntlessSword;
import com.dungeon_additions.da.entity.void_dungeon.EntityEndBase;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiclysm;
import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.util.DauntlessUtils;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import org.lwjgl.Sys;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityDauntless  extends EntityDarkBase implements IAnimatable, IAnimationTickable, IPitch, IAttack {

    private final String ANIM_SWING_ONE = "swing";
    private final String ANIM_SWING_TWO = "swing_two";
    private final String ANIM_SWING_THREE = "swing_three";
    private final String ANIM_GROUND_STRIKE = "ground_strike";
    private final String ANIM_SWORD_SLASH = "sword_slash";
    private final String ANIM_TELEPORT = "teleport";
    private final String ANIM_SUMMON_PROJECTILES = "summon_projectiles";
    private final String ANIM_SELF_AOE = "self_aoe";
    private final String ANIM_USE_SWORD = "use_sword";
    private final String ANIM_DASH_SWEEP = "dash_sweep";
    private final String ANIM_THROW_SWORD = "throw_sword";
    private final String ANIM_PIERCE_START = "pierce_start";
    private final String ANIM_PIERCE_FAIL = "pierce_fail";
    private final String ANIM_PIERCE_FINISH = "pierce_finish";
    private int shakeTime = 0;
    public int wantedDistance = 18;
    protected int teleportCooldownTimer = 10 * 20;
    public boolean doesBossSlowDown = false;
    public boolean standbyOnVel = false;
    private int general_use_sword_cooldown = 30 * 20;
    public int sword_charge_one = 0;
    public int sword_charge_two = 0;
    public int sword_charge_3 = 0;
    private EntityLivingBase grabbedEntity;
    public boolean clearCurrentVelocity = false;
    private Consumer<EntityLivingBase> prevAttack;
    private boolean setBossToFlyHigh = false;
    private boolean grabDetection = false;
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.NOTCHED_12));
    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.FLOAT);
    protected static final DataParameter<Integer> SWORD_CHARGE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_ONE= EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_TWO = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_THREE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GROUND_STRIKE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWORD_SLASH = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_PROJECTILES = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SELF_AOE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> USE_SWORD = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DASH_SWEEP = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_START = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_FAIL = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_FINISH = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RANGED_MODE = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TELEPORT_ATTACK = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> THROW_SWORD = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPAWNED_NATURALLY = EntityDataManager.createKey(EntityDauntless.class, DataSerializers.BOOLEAN);

    public void setSwordCharge(int skinType)
    {
        this.dataManager.set(SWORD_CHARGE, Integer.valueOf(skinType));
    }

    public int getSwordCharge()
    {
        return this.dataManager.get(SWORD_CHARGE).intValue();
    }
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    public void setSwingOne(boolean value) {this.dataManager.set(SWING_ONE, Boolean.valueOf(value));}
    public boolean isSwingOne() {return this.dataManager.get(SWING_ONE);}
    public void setSwingTwo(boolean value) {this.dataManager.set(SWING_TWO, Boolean.valueOf(value));}
    public boolean isSwingTwo() {return this.dataManager.get(SWING_TWO);}
    public void setSwingThree(boolean value) {this.dataManager.set(SWING_THREE, Boolean.valueOf(value));}
    public boolean isSwingThree() {return this.dataManager.get(SWING_THREE);}
    public void setGroundStrike(boolean value) {this.dataManager.set(GROUND_STRIKE, Boolean.valueOf(value));}
    public boolean isGroundStrike() {return this.dataManager.get(GROUND_STRIKE);}
    public void setSwordSlash(boolean value) {this.dataManager.set(SWORD_SLASH, Boolean.valueOf(value));}
    public boolean isSwordSlash() {return this.dataManager.get(SWORD_SLASH);}
    public void setSummonProjectiles(boolean value) {this.dataManager.set(SUMMON_PROJECTILES, Boolean.valueOf(value));}
    public boolean isSummonProjectiles() {return this.dataManager.get(SUMMON_PROJECTILES);}
    public void setSelfAoe(boolean value) {this.dataManager.set(SELF_AOE, Boolean.valueOf(value));}
    public boolean isSelfAoe() {return this.dataManager.get(SELF_AOE);}
    public void setUseSword(boolean value) {this.dataManager.set(USE_SWORD, Boolean.valueOf(value));}
    public boolean isUseSword() {return this.dataManager.get(USE_SWORD);}
    public void setDashSweep(boolean value) {this.dataManager.set(DASH_SWEEP, Boolean.valueOf(value));}
    public boolean isDashSweep() {return this.dataManager.get(DASH_SWEEP);}
    public void setPierceStart(boolean value) {this.dataManager.set(PIERCE_START, Boolean.valueOf(value));}
    public boolean isPierceStart() {return this.dataManager.get(PIERCE_START);}
    public void setPierceFail(boolean value) {this.dataManager.set(PIERCE_FAIL, Boolean.valueOf(value));}
    public boolean isPierceFail() {return this.dataManager.get(PIERCE_FAIL);}
    public void setPierceFinish(boolean value) {this.dataManager.set(PIERCE_FINISH, Boolean.valueOf(value));}
    public boolean isPierceFinish() {return this.dataManager.get(PIERCE_FINISH);}
    public void setRangedMode(boolean value) {this.dataManager.set(RANGED_MODE, Boolean.valueOf(value));}
    public boolean isRangedMode() {return this.dataManager.get(RANGED_MODE);}
    public void setTeleportAttack(boolean value) {this.dataManager.set(TELEPORT_ATTACK, Boolean.valueOf(value));}
    public boolean isTeleportAttack() {return this.dataManager.get(TELEPORT_ATTACK);}
    public void setThrowSword(boolean value) {this.dataManager.set(THROW_SWORD, Boolean.valueOf(value));}
    public boolean isThrowSword() {return this.dataManager.get(THROW_SWORD);}
    public void setSpawnedNaturally(boolean value) {this.dataManager.set(SPAWNED_NATURALLY, Boolean.valueOf(value));}
    public boolean isSpawnedNaturally() {return this.dataManager.get(SPAWNED_NATURALLY);}

    private final AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_IDLE = "idle";

    public EntityDauntless(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 100;
        this.setSize(0.8F, 2.45F);
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.hemorrhage_resistance = 0.93F;
        this.falter_resistance = 1.7F;
        this.bossInfo.setDarkenSky(true);
        if(!world.isRemote) {
            initDauntlessAI();
        }
        this.setRangedMode(true);
    }

    public EntityDauntless(World worldIn) {
        super(worldIn);
        this.experienceValue = 100;
        this.setSize(0.8F, 2.45F);
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.hemorrhage_resistance = 0.93F;
        this.falter_resistance = 1.7F;
        this.bossInfo.setDarkenSky(true);
        if(!world.isRemote) {
            initDauntlessAI();
        }
        this.setRangedMode(true);
    }

    private void initDauntlessAI() {
        float attackDistance = 12;
        float attackDistanceFar = (float) (this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()) - 10;
        this.tasks.addTask(4, new EntityDauntlessAttackAI(this, attackDistanceFar, attackDistance, 35, new TimedAttackInitiator<>(this, 30)));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setFloat("Look", this.getPitch());
        nbt.setInteger("Sword_Charge", this.getSwordCharge());
        nbt.setBoolean("Swing_One", this.isSwingOne());
        nbt.setBoolean("Swing_Two", this.isSwingTwo());
        nbt.setBoolean("Swing_Three", this.isSwingThree());
        nbt.setBoolean("Ground_Strike", this.isGroundStrike());
        nbt.setBoolean("Sword_Slash", this.isSwordSlash());
        nbt.setBoolean("Summon_Projectiles", this.isSummonProjectiles());
        nbt.setBoolean("Self_Aoe", this.isSelfAoe());
        nbt.setBoolean("Use_Sword", this.isUseSword());
        nbt.setBoolean("Dash_Sweep", this.isDashSweep());
        nbt.setBoolean("Pierce_Start", this.isPierceStart());
        nbt.setBoolean("Pierce_Fail", this.isPierceFail());
        nbt.setBoolean("Pierce_Finish", this.isPierceFinish());
        nbt.setBoolean("Shaking", this.isShaking());
        nbt.setBoolean("Ranged_Mode", this.isRangedMode());
        nbt.setBoolean("Teleport_Attack", this.isTeleportAttack());
        nbt.setBoolean("Throw_Sword", this.isThrowSword());
        nbt.setBoolean("Spawned_Naturally", this.isSpawnedNaturally());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
        this.setSwordCharge(nbt.getInteger("Sword_Charge"));
        this.setSwingOne(nbt.getBoolean("Swing_One"));
        this.setSwingTwo(nbt.getBoolean("Swing_Two"));
        this.setSwingThree(nbt.getBoolean("Swing_Three"));
        this.setGroundStrike(nbt.getBoolean("Ground_Strike"));
        this.setSwordSlash(nbt.getBoolean("Sword_Slash"));
        this.setSummonProjectiles(nbt.getBoolean("Summon_Projectiles"));
        this.setSelfAoe(nbt.getBoolean("Self_Aoe"));
        this.setUseSword(nbt.getBoolean("Use_Sword"));
        this.setDashSweep(nbt.getBoolean("Dash_Sweep"));
        this.setPierceStart(nbt.getBoolean("Pierce_Start"));
        this.setPierceFail(nbt.getBoolean("Pierce_Fail"));
        this.setPierceFinish(nbt.getBoolean("Pierce_Finish"));
        this.setShaking(nbt.getBoolean("Shaking"));
        this.setRangedMode(nbt.getBoolean("Ranged_Mode"));
        this.setTeleportAttack(nbt.getBoolean("Teleport_Attack"));
        this.setThrowSword(nbt.getBoolean("Throw_Sword"));
        this.setSpawnedNaturally(nbt.getBoolean("Spawned_Naturally"));
        super.readEntityFromNBT(nbt);
    }

    private int attack_differential = 0;
    private int HoverTimeIncrease = 0;
    private boolean hasHoverMovement = false;
    private int attack_cooldown = 0;

    @Override
    public void entityInit() {
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(SWORD_CHARGE, 0);
        this.dataManager.register(SWING_ONE, Boolean.valueOf(false));
        this.dataManager.register(SWING_TWO, Boolean.valueOf(false));
        this.dataManager.register(SWING_THREE, Boolean.valueOf(false));
        this.dataManager.register(GROUND_STRIKE, Boolean.valueOf(false));
        this.dataManager.register(SWORD_SLASH, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_PROJECTILES, Boolean.valueOf(false));
        this.dataManager.register(SELF_AOE, Boolean.valueOf(false));
        this.dataManager.register(USE_SWORD, Boolean.valueOf(false));
        this.dataManager.register(DASH_SWEEP, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_START, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_FAIL, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_FINISH, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        this.dataManager.register(RANGED_MODE, Boolean.valueOf(false));
        this.dataManager.register(THROW_SWORD, Boolean.valueOf(false));
        this.dataManager.register(TELEPORT_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SPAWNED_NATURALLY, Boolean.valueOf(false));
        super.entityInit();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(getHealth() / getMaxHealth());
        this.shakeTime--;
        this.teleportCooldownTimer--;
        this.attack_cooldown--;
        if(!world.isRemote) {
            if(general_use_sword_cooldown > -1) {
                general_use_sword_cooldown--;
            }
            if(sword_charge_one > 0) {
                sword_charge_one--;
            }
            if(sword_charge_two > 0) {
                sword_charge_two--;
            }
            if(sword_charge_3 > 0) {
                sword_charge_3--;
            }

            EntityLivingBase target = this.getAttackTarget();
            if(clearCurrentVelocity) {
                this.motionX = 0;
                this.motionY = 0;
                this.motionZ = 0;
                standbyOnVel = true;
                clearCurrentVelocity = false;
            }

            //switches the ai between distance and melee
                if(this.isRangedMode()) {
                    if(attack_differential > 6) {
                        this.setRangedMode(false);
                        this.attack_differential = 0;
                        System.out.println("Changed Ranged Mode");
                    }
                } else if (attack_differential > 15){
                        this.setRangedMode(true);
                        this.attack_differential = 0;
                }

            //target specific stuff
            if(target != null) {
                if (HoverTimeIncrease > 0) {
                    this.motionY = 0.25;
                    HoverTimeIncrease--;
                }

                if (this.hasHoverMovement) {
                    double d0 = (target.posX - this.posX) * 0.016;
                    double d2 = (target.posZ - this.posZ) * 0.016;
                    this.addVelocity(d0, 0, d2);
                    this.faceEntity(target, 30F, 30F);
                }


                if(grabDetection && grabbedEntity == null) {
                    List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                            this.getEntityBoundingBox().offset(ModUtils.getRelativeOffset(this, new Vec3d(1.5, -0.5, 0))).grow(1.5D, 3.5D, 1.5D),
                            e -> !e.getIsInvulnerable());

                    if(!nearbyEntities.isEmpty()) {
                        for(EntityLivingBase base : nearbyEntities) {
                            if(!(base instanceof EntityDarkBase)) {
                                grabbedEntity = base;
                                this.playSound(SoundsHandler.KING_GRAB_SUCCESS, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
                            }
                        }
                    }
                } else if (grabbedEntity != null) {
                    if(setBossToFlyHigh) {
                        this.motionY = 0.36;
                        this.motionX = 0;
                        this.motionZ = 0;
                        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.8, 1.2, 0)));
                        grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                        grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);
                    } else {
                        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.8, 0.4, 0)));
                        grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                        grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);
                    }

                }
            }
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(24);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.20590D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double HealthChange = this.getHealth() / this.getMaxHealth();
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode() && this.attack_cooldown < 1) {
            List<Consumer<EntityLivingBase>> close_attacks = new ArrayList<>(Arrays.asList(swing_attack, ground_strike, dash_sweep, self_aoe, pierce_start, use_sword_ability, throw_sword, summon_projectiles, sword_slash, use_teleport_ranged));
            double[] weights = {
                    (prevAttack != swing_attack && distance < 9 && !this.isRangedMode()) ? 1/distance : 0, //Swing attack
                    (prevAttack != ground_strike && distance < 15 && !this.isRangedMode()) ? 1/distance : 0, //Ground Strike
                    (prevAttack != dash_sweep && distance < 15 && !this.isRangedMode()) ? 1/distance : 0, //Dash Sweep Attack
                    (prevAttack != self_aoe && distance < 8 && !this.isRangedMode()) ? 1.1/distance : 0, //Self AOE
                    (prevAttack != pierce_start && distance < 24 && !this.isRangedMode()) ? 1/distance : 0, //Pierce Teleport Grab Attack
                    (prevAttack != use_sword_ability && this.getSwordCharge() > 0 && general_use_sword_cooldown < 1 && !this.isRangedMode()) ? 2/distance : 0, //Use Sword Ability
                    (prevAttack != throw_sword && this.isRangedMode()) ? distance * 0.02 : (prevAttack != throw_sword && distance > 13) ? distance * 0.02 : 0, //Throw Sword Attack
                    (prevAttack != summon_projectiles && this.isRangedMode()) ? distance * 0.02 : (prevAttack != summon_projectiles && distance > 13) ? distance * 0.02 : 0, //Summon Projectiles
                    (prevAttack != sword_slash && distance > 6) ? distance * 0.02 : 0, //Sword Slash
                    (prevAttack != use_teleport_ranged && this.isRangedMode()) ? distance * 0.01 : 0 //Use Teleport ability as back up
                };
            prevAttack = ModRand.choice(close_attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        return 0;
    }

    private final Consumer<EntityLivingBase> use_teleport_ranged = (target) -> {
      this.teleportAbility(target, true);
    };

    private final Consumer<EntityLivingBase> use_sword_ability = (target) -> {
        this.setUseSword(true);
        this.setFightMode(true);
        this.setImmovable(true);

        addEvent(()-> {
            //allows the boss to use various different buffs to it's aid
            if(this.getSwordCharge() == 1) {
                this.sword_charge_one = 30 * 20;
            } else if (this.getSwordCharge() == 2) {
                this.sword_charge_two = 60 * 20;
            } else if (this.getSwordCharge() >= 3) {
                this.sword_charge_3 = 30 * 20;
                //summons giant sword on the player's position
                addEvent(()-> {
                    Vec3d targetPos = target.getPositionVector();
                    addEvent(() -> {
                        Vec3d targetedPos = target.getPositionVector();
                        Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetPos, targetedPos, 4);
                        EntityDauntlessSword sword = new EntityDauntlessSword(world, true);
                        sword.setPosition(predictedPosition.x, target.posY + 16, predictedPosition.z);
                        world.spawnEntity(sword);
                    }, 3);
                }, 1);
            }
            this.setSwordCharge(0);
        }, 35);

        addEvent(()-> {
            this.setUseSword(false);
            this.setFightMode(false);
            this.setImmovable(false);
            this.attack_cooldown = 10;
        }, 60);
    };

    private final Consumer<EntityLivingBase> throw_sword = (target) -> {
        this.setThrowSword(true);
        this.setFightMode(true);

        addEvent(() -> new ActionDauntlessThrowSword().performAction(this, target), 35);

        addEvent(()-> {
            this.setThrowSword(false);
            this.setFightMode(false);
            if(this.isRangedMode()) {
                this.attack_differential += 2;
                this.attack_cooldown = 70;
            } else {
                this.attack_differential += 1;
                this.attack_cooldown = 40;
            }
        }, 80);
    };

    private final Consumer<EntityLivingBase> summon_projectiles = (target) -> {
      this.setSummonProjectiles(true);
      this.setFightMode(true);

      addEvent(()-> {
        this.setSummonProjectiles(false);
        this.setFightMode(false);
          if(this.isRangedMode()) {
              this.attack_differential += 1;
          }
      }, 30);
    };

    private final Consumer<EntityLivingBase> sword_slash = (target) -> {
      this.setSwordSlash(true);
      this.setFightMode(true);

      addEvent(()-> {
            this.setSwordSlash(false);
            this.setFightMode(false);
          if(this.isRangedMode()) {
              this.attack_differential += 1;
          }
      }, 65);
    };

    private final Consumer<EntityLivingBase> pierce_start = (target) -> {
      this.setPierceStart(true);
      this.setFightMode(true);
      this.setImmovable(true);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-6));
            addEvent(()-> {
                this.setImmovable(false);
                ModUtils.attemptTeleport(targetedPos.add(0, 0.25, 0), this);
                this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.75f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 3);
        }, 20);

        addEvent(()-> {
            this.setImmovable(true);
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(4));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                this.grabDetection = true;
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.13),0.1F);
            }, 8);
        }, 20);

      addEvent(() -> {
            this.grabDetection = false;
            this.setImmovable(true);
            this.setPierceStart(false);
            if(this.grabbedEntity != null) {
                this.setGrabTooContinue(target);
            } else {
                this.setPierceFail(true);

                addEvent(()-> this.lockLook = false, 5);
                addEvent(()-> {
                this.setPierceFail(false);
                this.setFightMode(false);
                this.setImmovable(false);
                this.attack_differential += 1;
                this.attack_cooldown = 10;
                }, 20);
            }
      }, 55);
    };

    private void setGrabTooContinue(EntityLivingBase target) {
        this.pierce_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> pierce_continue = (target) -> {
        this.setPierceFinish(true);
        addEvent(()-> {
            this.setBossToFlyHigh = true;
            this.setImmovable(false);
            this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (0);
            ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.1f, 0);
        }, 3);

        addEvent(()-> {
            this.setImmovable(true);
            addEvent(()-> {
                this.setBossToFlyHigh = false;
                this.grabbedEntity = null;
                this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.0, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.1f, 0, false, ModPotions.HEMORRHAGE, 1, 300, 2F);
            }, 2);
        }, 30);

        addEvent(()-> {
            this.lockLook = false;
        }, 55);

        addEvent(()-> {
            this.setImmovable(false);
            this.setPierceFinish(false);
            this.setFightMode(false);
            this.attack_differential += 2;
            this.attack_cooldown = 30;
        }, 60);
    };

    private final Consumer<EntityLivingBase> self_aoe = (target) -> {
      this.setFightMode(true);
      this.setSelfAoe(true);
      this.setImmovable(true);

      addEvent(()-> {
        this.lockLook = true;
      }, 20);

        addEvent(()-> {
            Main.proxy.spawnParticle(22,world, this.posX, this.posY + 0.1, this.posZ, 0, 0, 0);
            Main.proxy.spawnParticle(22,world, this.posX, this.posY + 3.1, this.posZ, 0, 0, 0);
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.25, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (this.getAttack());
            DauntlessUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.9F);
            //do AOE action
            new ActionDauntlessSelfAOE().performAction(this, target);
        }, 27);

        addEvent(()-> {
            this.lockLook = false;
        }, 40);

      addEvent(()-> {
        this.setFightMode(false);
        this.setSelfAoe(false);
        this.setImmovable(false);
        this.attack_differential += 2;
        this.attack_cooldown = 40;
      }, 45);
    };

    private final Consumer<EntityLivingBase> dash_sweep = (target) -> {
        this.setFightMode(true);
        this.setDashSweep(true);
        this.setImmovable(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                //this.holdPosition = false;
                double distance = this.getPositionVector().distanceTo(softTargetPos);
                ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
            }, 5);
        }, 25);

        addEvent(()-> {
            this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.6f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 0.75, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (this.getAttack());
            DauntlessUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.9F);
        }, 38);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 50);

        addEvent(()-> {
            this.teleportAbility(target, false);
        }, 58);

        addEvent(()-> {
            this.setDashSweep(false);
            this.setFightMode(false);
            this.setImmovable(false);
            this.attack_cooldown = 10;
        }, 65);
    };


    private final Consumer<EntityLivingBase> ground_strike = (target) -> {
      this.setFightMode(true);
      this.setGroundStrike(true);
      this.HoverTimeIncrease = 3;
      //we basically want dauntless to glide up then track the player
        addEvent(()-> {
            this.hasHoverMovement = true;
        }, 12);

        addEvent(()-> {
            //teleport boss to the ground
            int y_value = ModUtils.getSurfaceHeightGeneral(world, new BlockPos(this.posX, 0, this.posZ), (int) this.posY - 20, (int) this.posY + 2);
            if(y_value != this.posY) {
                this.setPosition(this.posX, y_value + 1, this.posZ);
            } else {
                //destroy blocks just incase
                this.setPosition(this.posX, target.posY, this.posZ);
            }
            this.destroyBlocksInSwing(new Vec3d(this.posX, this.posY, this.posZ), 1.0F);
            this.hasHoverMovement = false;
            this.setImmovable(true);
            this.lockLook = true;
        }, 30);

        addEvent(()-> {
            //do explosion attack
            this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.6f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0.25, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack() * 1.5F);
            DauntlessUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 0.9f, 0, false, 1.2F);
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0, 0)));
            Main.proxy.spawnParticle(22,world, relPos.x, this.posY + 0.1, relPos.z, 0, 0, 0);
            this.destroyBlocksInSwing(offset, 3.5F);
        }, 35);

        addEvent(()-> {
            this.lockLook = false;
        }, 62);


      addEvent(()-> {
        this.setFightMode(false);
        this.setGroundStrike(false);
        this.setImmovable(false);
        this.attack_differential += 1;
        this.attack_cooldown = 20;
      }, 75);
    };

    private final Consumer<EntityLivingBase> swing_attack = (target) -> {
        this.setFightMode(true);
        this.setImmovable(true);
        int randB;
        if(this.isSpawnedNaturally()) {
            randB = ModRand.range(1, 3);
        } else {
           randB = ModRand.range(1, 4);
        }

        //Swings sword once
        if(randB == 1) {
            this.setSwingOne(true);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
                }, 5);
            }, 15);

            addEvent(()-> {
                this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.6F);
            }, 26);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 40);

            addEvent(()-> {
                this.setImmovable(false);
                this.setSwingOne(false);
                this.setFightMode(false);
                this.attack_cooldown = 10;
            }, 60);
        }
        //swings sword Twice
        if(randB == 2) {
            this.setSwingTwo(true);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
                }, 5);
            }, 15);

            addEvent(()-> {
                this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.6F);
            }, 26);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 40);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
                }, 5);
            }, 50);

            addEvent(()-> {
                this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.6F);
            }, 61);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 73);

            addEvent(()-> {
                this.setImmovable(false);
                this.setSwingTwo(false);
                this.setFightMode(false);
                this.attack_differential += 1;
                this.attack_cooldown = 20;
            }, 85);
        }

        //Swing sword three
        if(randB == 3) {
            this.setSwingThree(true);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
                }, 5);
            }, 15);

            addEvent(()-> {
                this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.6F);
            }, 26);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 40);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
                }, 5);
            }, 50);

            addEvent(()-> {
                this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.6F);
            }, 61);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 80);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    //this.holdPosition = false;
                    double distance = this.getPositionVector().distanceTo(softTargetPos);
                    ModUtils.leapTowards(this, softTargetPos, (float) (distance * 0.13),0F);
                }, 5);
            }, 86);

            addEvent(()-> {
                this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.6f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                DauntlessUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.6F);
            }, 97);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 110);

            addEvent(()-> {
                this.setSwingThree(false);
                this.setFightMode(false);
                this.setImmovable(false);
                this.attack_differential += 2;
                this.attack_cooldown = 30;
            }, 130);
        }
    };

    private void teleportAbility(EntityLivingBase target, boolean needsAnimation) {
        if(needsAnimation) {
            this.setFightMode(true);
            this.setTeleportAttack(true);
        }
        addEvent(()-> {
            if(!this.isRangedMode()) {
                if (target != null) {
                    //calculated teleport attack
                    boolean backwards = rand.nextBoolean();
                    Vec3d lookPos = target.getLookVec();
                    Vec3d targetPos = new Vec3d(target.posX + lookPos.x * 0.5D, target.posY, target.posZ + lookPos.z * 0.5);
                    Vec3d teleportPos = null;
                    for (int i = 1; i <= 256; i++) {
                        Vec3d posSet = targetPos.subtract(this.getPositionVector()).normalize();
                        Vec3d targetedPos = targetPos.add(posSet.scale(backwards ? i : -i));
                        int y = ModUtils.getSurfaceHeightZeroReturn(world, new BlockPos(targetedPos.x, 0, targetedPos.z), (int) target.posY - 4, (int) target.posY + 3);
                        if (y != 0 && (backwards && target.getDistance(targetedPos.x, y + 1, targetedPos.z) > 5 || !backwards && target.getDistance(targetedPos.x, y + 1, targetedPos.z) > 7)) {
                            teleportPos = new Vec3d(targetedPos.x, y + 1, targetedPos.z);
                            break;
                        }
                    }

                    if (teleportPos != null) {
                        this.setPosition(teleportPos.x, teleportPos.y, teleportPos.z);
                    } else {
                        //random teleport for ground mode
                        for (int i = 4; i <= 25; i++) {
                            Vec3d idealPos = new Vec3d(this.posX + ModRand.getFloat(i), this.posY, this.posZ + ModRand.getFloat(i));
                            int y = ModUtils.getSurfaceHeightZeroReturn(world, new BlockPos(idealPos.x, 0, idealPos.z), (int) this.posY - 12, (int) this.posY + 5);
                            if (y != 0 && target.getDistance(idealPos.x, y + 1, idealPos.z) > 6) {
                                this.setPosition(idealPos.x, y + 1, idealPos.z);
                            }
                        }
                    }
                    this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.8F, 1.0F / (rand.nextFloat() * 0.4F + 0.6F));
                    this.setImmovable(true);
                }
            } else {
                //random teleport for ranged mode
                for(int i = 0; i < 20; i++) {
                    Vec3d pos = ModRand.randVec().normalize().scale(7)
                            .add(this.getPositionVector());
                    boolean canSee = this.world.rayTraceBlocks(target.getPositionEyes(1), pos, false, true, false) == null;
                    Vec3d prevPos = this.getPositionVector();
                    this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 2.3F, 1.0F / (rand.nextFloat() * 0.4F + 0.6F));
                    if(canSee && ModUtils.attemptTeleport(pos, this)){
                        // ModUtils.lineCallback(prevPos, pos, 20, (particlePos, j) ->
                        //   actor.world.setEntityState(actor, ModUtils.SECOND_PARTICLE_BYTE));
                        break;
                    }

                }
            }
            this.destroyBlocksInSwing(new Vec3d(this.posX, this.posY, this.posZ), 1.0F);
        }, 3);

        if(needsAnimation) {
            addEvent(() -> {
                this.setFightMode(false);
                this.setTeleportAttack(false);
                this.setImmovable(false);
                if(this.isRangedMode()) {
                    this.attack_cooldown = 50;
                } else {
                    this.attack_cooldown = 10;
                }
            }, 5);
        }
    }


    private void destroyBlocksInSwing(Vec3d offset, double size) {
        AxisAlignedBB box = getEntityBoundingBox().grow(size, 0.1, size).offset(ModUtils.getRelativeOffset(this, new Vec3d(offset.x, 0.1, offset.z)));
        ModUtils.destroyBlocksInAABB(box, world, this);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateFight));
    }

    private <E extends IAnimatable> PlayState predicateFight(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSwingOne()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_ONE));
                return PlayState.CONTINUE;
            }
            if(this.isSwingTwo()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_TWO));
                return PlayState.CONTINUE;
            }
            if(this.isSwingThree()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_THREE));
                return PlayState.CONTINUE;
            }
            if(this.isSwingOne()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_ONE));
                return PlayState.CONTINUE;
            }
            if(this.isGroundStrike()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_GROUND_STRIKE));
                return PlayState.CONTINUE;
            }
            if(this.isSwordSlash()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWORD_SLASH));
                return PlayState.CONTINUE;
            }
            if(this.isTeleportAttack()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_TELEPORT));
                return PlayState.CONTINUE;
            }
            if(this.isSummonProjectiles()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SUMMON_PROJECTILES));
                return PlayState.CONTINUE;
            }
            if(this.isSelfAoe()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SELF_AOE));
                return PlayState.CONTINUE;
            }
            if(this.isUseSword()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_USE_SWORD));
                return PlayState.CONTINUE;
            }
            if(this.isDashSweep()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_DASH_SWEEP));
                return PlayState.CONTINUE;
            }
            if(this.isThrowSword()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_THROW_SWORD));
                return PlayState.CONTINUE;
            }
            if(this.isPierceStart()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PIERCE_START));
                return PlayState.CONTINUE;
            }
            if(this.isPierceFail()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PIERCE_FAIL));
                return PlayState.CONTINUE;
            }
            if(this.isPierceFinish()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PIERCE_FINISH));
                return PlayState.CONTINUE;
            }
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.performNTimes(25, (i) -> {
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + ModRand.getFloat(3.5F), this.posY + ModRand.getFloat(3.5F),
                        this.posZ + ModRand.getFloat(3.5F), 0, 0, 0);
            });
        }
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        //teleports the boss to avoid attacks, differs both modes of the boss
        if(!this.isFightMode() && teleportCooldownTimer < 1) {
            if(this.getAttackTarget() != null) {
                this.teleportAbility(this.getAttackTarget(), true);
                this.teleportCooldownTimer = 10 * 20;
                return false;
            }
        }

        if(this.grabbedEntity != null) {
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected boolean canDropLoot() {
        return true;
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
    public void setCustomNameTag(@Nonnull String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void addTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if(!this.isImmovable()) {
            ModUtils.aerialTravel(this, strafe, vertical, forward);
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    @Override
    public void removeTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        if(!this.isRangedMode()) {
            this.dataManager.set(LOOK, 0F);
        } else {
            this.dataManager.set(LOOK, clampedLook);
        }
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }

}
