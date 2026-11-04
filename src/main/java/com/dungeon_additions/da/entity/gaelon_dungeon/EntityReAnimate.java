package com.dungeon_additions.da.entity.gaelon_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.gaelon_dungeon.EntityReAnimateAttackAI;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityReAnimate extends EntityGaelonBase implements IAttack {

    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_IDLE_LOWER = "idle_lower";

    private final String ANIM_IDLE_UPPER = "idle_upper";

    private final String ANIM_WALK_UPPER = "walk_upper";

    private final String ANIM_ORB_IDLE = "orb_idle";

    private final String ANIM_SWING = "swing";
    private final String ANIM_DOUBLE_SWING = "swing_double";
    private final String ANIM_USE_SHIELD = "use_shield";
    private final String ANIM_CAST = "cast";
    private final String ANIM_TELEPORT_PIERCE = "teleportPierce";




    private static final DataParameter<Boolean> ORB_MODE = EntityDataManager.createKey(EntityReAnimate.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> USE_SHIELD = EntityDataManager.createKey(EntityReAnimate.class, DataSerializers.BOOLEAN);
    private Consumer<EntityLivingBase> prevAttack;
    public boolean byPassStrafing = false;

    private boolean usingShieldOff = false;

    private void setOrbMode(boolean value) {this.dataManager.set(ORB_MODE, Boolean.valueOf(value));}
    public boolean isOrbMode() {return this.dataManager.get(ORB_MODE);}
    private void setUseShield(boolean value) {this.dataManager.set(USE_SHIELD, Boolean.valueOf(value));}
    public boolean isUseShield() {return this.dataManager.get(USE_SHIELD);}

    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, true);
    private final MoveAnalysis moveAnalysis;

    public EntityReAnimate(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.6F, 2.1F);
        this.experienceValue = 15;
        this.setOrbMode(true);
        this.hemorrhage_resistance = 0.25F;
        this.falter_resistance = 1F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public EntityReAnimate(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 2.1F);
        this.experienceValue = 15;
        this.setOrbMode(true);
        this.hemorrhage_resistance = 0.25F;
        this.falter_resistance = 1F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public EntityReAnimate(World worldIn, boolean setImmediateChange) {
        super(worldIn);
        this.setSize(0.6F, 2.1F);
        this.experienceValue = 15;
        this.setImmediateChange = setImmediateChange;
        this.setOrbMode(true);
        this.hemorrhage_resistance = 0.25F;
        this.falter_resistance = 1F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Orb_Mode", this.isOrbMode());
        nbt.setBoolean("Use_Shield", this.isUseShield());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setOrbMode(nbt.getBoolean("Orb_Mode"));
        this.setUseShield(nbt.getBoolean("Use_Shield"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(ORB_MODE, Boolean.valueOf(false));
        this.dataManager.register(USE_SHIELD, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.reanimate_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.reanimate_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.reanimate_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityReAnimateAttackAI<>(this, 1.0D, 10, 7, 0.3F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityDarkBase>(this, EntityDarkBase.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    private boolean speedDiff = false;
    private int idleTimer = 200;
    public boolean wantsToGetAway = false;
    private int getAwayTime = 100;
    private int hurtCounter = 0;
    private int coolDownTime = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.moveAnalysis.update();
        getAwayTime--;
        coolDownTime--;
        shieldCooldown--;
        if(!world.isRemote) {
            EntityLivingBase target = this.getAttackTarget();

            if(target != null) {
                //handles movement difference
                if(this.isOrbMode()) {
                    if(!speedDiff) {
                        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.36);
                        this.speedDiff = true;
                    }
                } else if(speedDiff) {
                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
                    this.speedDiff = false;
                }

                if(this.getDistance(target) <= 7 && this.isOrbMode() && !wantsToGetAway) {
                    this.setOrbMode(false);
                    //do particles
                    world.setEntityState(this, ModUtils.PARTICLE_BYTE);
                    this.playSound(SoundsHandler.REANIMATE_APPEAR, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                    idleTimer = 200;
                }

                // enter flee mode
                if(hurtCounter > 4 && !this.isFightMode()) {
                    this.wantsToGetAway = true;
                    this.setOrbMode(true);
                    this.clearEvents();
                    world.setEntityState(this, ModUtils.PARTICLE_BYTE);
                    this.playSound(SoundsHandler.REANIMATE_DISAPPEAR, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                    getAwayTime = 40 + ModRand.range(1, 70);
                    this.hurtCounter = 0;
                }

                //come out of orb mode from fleeing
                if(wantsToGetAway) {
                    if(getAwayTime < 0) {
                        this.setOrbMode(false);
                        world.setEntityState(this, ModUtils.PARTICLE_BYTE);
                        this.playSound(SoundsHandler.REANIMATE_APPEAR, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                        this.wantsToGetAway = false;
                    }
                }
            }

            if(target == null && !this.isOrbMode()) {
                if(idleTimer > 0) {
                    idleTimer--;
                } else {
                    this.setOrbMode(true);
                    world.setEntityState(this, ModUtils.PARTICLE_BYTE);
                    this.playSound(SoundsHandler.REANIMATE_DISAPPEAR, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                    idleTimer = 200;
                }
            }
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isOrbMode() && !this.isFightMode() && coolDownTime <= 0) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(swing_attack, double_swing_attack, cast_spell, teleportPierce));
            double[] weights = {
                    (distance <= 5 && prevAttack != swing_attack) ? 1/distance : 0,
                    (distance <= 5 && prevAttack != double_swing_attack) ? 1/distance : 0,
                    distance * 0.02,
                    (prevAttack != teleportPierce && distance > 2) ? distance * 0.02 : 0
            };
            prevAttack = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttack.accept(target);
        }
        return 20;
    }

    private final Consumer<EntityLivingBase> teleportPierce = (target) -> {
        animator.performAttack(ANIM_TELEPORT_PIERCE);
        this.setFightMode(true);
        this.setImmovable(true);
        this.setFullBodyUsage(true);
        this.usingShieldOff = true;

        addEvent(() -> this.playSound(SoundsHandler.REANIMATE_SHIELD, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 5);
        addEvent(()-> {
            Vec3d targetOldPos = target.getPositionVector();
            addEvent(()-> {
                this.usingShieldOff = false;
                Vec3d targetedPosNew = target.getPositionVector();
                Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPosNew, 3);
                Vec3d posSet = predictedPosition.subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = predictedPosition.add(posSet.scale(-2));
                addEvent(()-> {
                    this.setImmovable(false);
                    ModUtils.attemptTeleport(targetedPos, this);
                    world.setEntityState(this, ModUtils.PARTICLE_BYTE);
                    this.lockLook = true;
                    this.setImmovable(true);
                    this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.75f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
                }, 3);
            }, 4);
        }, 21);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack() * 1.5);
            ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.9f, 0, false, 0.7F);
            this.playSound(SoundsHandler.REANIMATE_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 40);

        addEvent(()-> {
            if(hurtCounter > 4) {
                this.coolDownTime = 30;
            }
            this.lockLook = false;
            this.setFightMode(false);
            this.setImmovable(false);
            this.setFullBodyUsage(false);
        }, 60);
    };

    private final Consumer<EntityLivingBase> cast_spell = (target) -> {
        animator.performAttack(ANIM_CAST);
        this.setFightMode(true);

        addEvent(()-> {
            //do melee attack
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.5f, 0, false);
             this.playSound(SoundsHandler.REANIMATE_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            //spell
            ProjectileGhost bolt = new ProjectileGhost(world, this, this.getAttack());
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1,1.4,0)));
            bolt.setPosition(relPos.x, relPos.y, relPos.z);
            bolt.setTravelRange(20);
            float speed = (float) 0.5;
            bolt.shoot(this, 0, this.rotationYaw, 0.0F, speed, 0);
            world.spawnEntity(bolt);
            this.playSound(SoundsHandler.REANIMATE_CAST, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));

        }, 30);

        addEvent(()-> {
        this.setFightMode(false);
        this.coolDownTime = 45;
        }, 60);
    };

    private final Consumer<EntityLivingBase> double_swing_attack = (target) -> {
      animator.performAttack(ANIM_DOUBLE_SWING);
      this.setFightMode(true);

      addEvent(()-> {
          this.lockLook = true;
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
          }, 4);
      }, 10);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage =(float) (this.getAttack());
          ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.5f, 0, false, 0.3F);
          this.playSound(SoundsHandler.REANIMATE_SWING, 0.8f, 0.5f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 20);

      addEvent(()-> {
          this.setImmovable(true);
          this.lockLook = false;
      }, 25);

      addEvent(()-> {
        this.byPassStrafing = true;
        this.setImmovable(false);
      }, 35);

      addEvent(()-> this.lockLook = true, 50);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage =(float) (this.getAttack());
          ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.3F);
          this.playSound(SoundsHandler.REANIMATE_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 60);

      addEvent(()-> {
          this.lockLook = false;
          this.byPassStrafing = false;
      }, 70);

      addEvent(()-> {
          if(hurtCounter > 4) {
              this.coolDownTime = 30;
          }
        this.setFightMode(false);
      }, 85);
    };


    private final Consumer<EntityLivingBase> swing_attack = (target) -> {
        animator.performAttack(ANIM_SWING);
        this.setFightMode(true);
        this.byPassStrafing = true;

        addEvent(()-> {
            this.lockLook = true;
        }, 10);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.3F);
            this.playSound(SoundsHandler.REANIMATE_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 20);

        addEvent(()-> {
            this.lockLook = false;
            this.byPassStrafing = false;
        }, 30);

        addEvent(()-> {
            if(hurtCounter > 4) {
                this.coolDownTime = 30;
            }
            if(rand.nextBoolean()) {
                this.hurtCounter++;
            }
        this.setFightMode(false);
        }, 45);
    };


    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.performNTimes(10, (i) -> {
                Main.proxy.spawnParticle(15, this.posX, this.posY + ModRand.range(1, 3), this.posZ, world.rand.nextFloat()/6 - world.rand.nextFloat()/6, 0.04, world.rand.nextFloat()/6 - world.rand.nextFloat()/6, 100);
            });
        }
    }

    @Override
    public void updateAnimations() {
        if(!this.isOrbMode()) {
            boolean isMovingOnGround = moveAnalysis.isMovingHorizontally() && this.onGround;
            animator.handleIdleStates(isMovingOnGround);
        } else if (this.isOrbMode()) {
            animator.setStateAnim(ANIM_ORB_IDLE, true);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.isOrbMode() || this.isUseShield() || this.usingShieldOff) {
            if(this.usingShieldOff || this.isUseShield()) {
                this.playSound(SoundsHandler.REANIMATE_SHIELD, 0.6f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            }
            return false;
        }  else if (!this.isFightMode() && amount > 0.0F && this.canBlockDamageSource(source)) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }
            this.playSound(SoundsHandler.REANIMATE_SHIELD, 0.6f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));

            return false;
        }

        //a quick o shit run away moment
        if(amount > (this.getMaxHealth()) * 0.4) {
            this.hurtCounter = 16;
            this.coolDownTime = 20;
        }



        hurtCounter++;
        return super.attackEntityFrom(source, amount);
    }

    private int shieldCooldown = 60;

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if (!this.isFightMode()) {
            if (!damageSourceIn.isUnblockable() && !this.isFightMode() && shieldCooldown <= 0) {
                if (!this.isUseShield()) {
                    //We want a certain distance where the attack part if variable, greater than 6 and less than 14
                    this.useShieldAction();
                    Vec3d vec3d = damageSourceIn.getDamageLocation();

                    if (vec3d != null) {
                        Vec3d vec3d1 = this.getLook(1.0F);
                        Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                        vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

                        return vec3d2.dotProduct(vec3d1) < 0.0D;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.REANIMATE_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.REANIMATE_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.REANIMATE_HURT;
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "reanimate");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return this.isDropsLoot();
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        if(!this.isOrbMode()) {
            this.playSound(SoundsHandler.REANIMATE_STEP, 0.4F, 0.8f + ModRand.getFloat(0.5F));
        }
    }


    private void useShieldAction() {
        this.setFightMode(true);
        this.setUseShield(true);
        animator.performAttack(ANIM_USE_SHIELD);

        addEvent(()-> {
        this.setFightMode(false);
        this.setUseShield(false);
        this.shieldCooldown = 60;
        }, 50);
    }
}
