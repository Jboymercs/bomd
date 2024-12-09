package com.dungeon_additions.da.entity.night_lich;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.flying.EntityAIAerialAttack;
import com.dungeon_additions.da.entity.ai.flying.FlyingMoveHelper;
import com.dungeon_additions.da.entity.ai.flying.TimedAttackInitiator;
import com.dungeon_additions.da.entity.night_lich.action.*;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityNightLich extends EntityAbstractNightLich implements IAnimatable, IAttack, IAnimationTickable {


    private Consumer<EntityLivingBase> prevAttack;


    //Boss info Stuff
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_6));

    //Idle Stuff
    private final String ANIM_MODEL_IDLE = "model_idle";
    private final String ANIM_BOOK_IDLE = "book_idle";
    private final String ANIM_FLY_IDLE = "fly_idle";
    //Attack Anims
    private final String ANIM_BLUE = "shoot_projectiles";
    private final String ANIM_YELLOW = "shoot_fireball";
    private final String ANIM_RED = "red_dash";
    private final String ANIM_PURPLE = "summon_mobs";
    private final String ANIM_GREEN = "green_aoe";
    //Combos
    private final String ANIM_TRACK_PROJECTILES = "shoot_track_projectiles";
    private final String ANIM_SHOOT_PATTERN = "shoot_projectiles_pattern";
    private final String ANIM_COMBO_AOE = "combo_aoe";
    private final String ANIM_THROW_STAFF = "throw_staff";
    //Non Colored
    private final String ANIM_SWING = "swing";
    private final String ANIM_DOUBLE_SWING = "double_swing";

    private AnimationFactory factory = new AnimationFactory(this);

    //it either equals 30 or 3
    public int wantedDistance;

    private boolean enableRedParticles = false;

    private boolean destroyCloseBlocks = false;

    public EntityNightLich(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.1F, 2.5F);
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.wantedDistance = 30;
        if(!world.isRemote) {
            initLichAI();
        }
    }

    public EntityNightLich(World worldIn) {
        super(worldIn);
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.setSize(1.1F, 2.5F);
        this.wantedDistance = 30;
        if(!world.isRemote) {
            initLichAI();
        }

    }


    private void initLichAI() {
        float attackDistance = this.isAngeredState() ? 3F : 30F;
        float attackDistanceFar = (float) (this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()) - 5;
        this.tasks.addTask(4, new EntityAIAerialAttack(this, attackDistanceFar, attackDistance, 35, new TimedAttackInitiator<>(this, 60)));

    }

    private int damageTaken = 50;

    public boolean clearCurrentVelocity = false;
    public boolean standbyOnVel = false;

    @Override
    public void onUpdate() {
        super.onUpdate();

        this.bossInfo.setPercent(getHealth() / getMaxHealth());
        EntityLivingBase target = this.getAttackTarget();

        if(target != null) {
            double healthCurr = this.getHealth() / this.getMaxHealth();
            //change me later after testing
            if(healthCurr <= 0.01) {
                if(wantedDistance == 30) {
                    if(world.rand.nextInt(40) == 0 || this.hurtTime > 0) {
                        this.damageTaken++;
                    }

                    if(damageTaken >= 60) {
                        this.wantedDistance = 3;
                        if(!this.isAngeredState()) {
                            this.setAngeredState(true);
                        }
                    }
                }
                if(wantedDistance == 3) {
                    if(this.hurtTime > 0 || world.rand.nextInt(20) == 0) {
                        this.damageTaken -=2;
                    }
                    if(damageTaken <= 0) {
                        this.wantedDistance = 30;
                        if(this.isAngeredState()) {
                            this.setAngeredState(false);
                        }
                    }
                }
            }
        }

        //this clears current motion and enables a boolean to keep it still for the time being
        // typically only used in red attack
        if(clearCurrentVelocity) {
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
            standbyOnVel = true;
            clearCurrentVelocity = false;
        }

        //This destroys blocks to ensure that any terrain and what not doesn't get in the way
        if(this.destroyCloseBlocks) {
            AxisAlignedBB box = getEntityBoundingBox().grow(1.25, 0.1, 1.25).offset(0, 0.1, 0);
            ModUtils.destroyBlocksInAABB(box, world, this);
        }
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        int cooldown;

        //this allows it so that the more mobs that are active, the slower the cooldown, but to a cap of the current mob limit

        if(current_mobs.size() >= MobConfig.lich_active_mob_count) {
            cooldown = (MobConfig.lich_active_mob_count * MobConfig.lich_progressive_cooldown) + MobConfig.lich_static_cooldown;
        } else {
            cooldown = (current_mobs.size() * MobConfig.lich_progressive_cooldown) + MobConfig.lich_static_cooldown;
        }
        double HealthChange = this.getHealth() / this.getMaxHealth();

        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> close_attacks = new ArrayList<>(Arrays.asList(shoot_magic_projectiles, shoot_magic_fireball, summon_mobs, combo_magic, throw_staff, dash_attack_red, green_attack, regular_swing, double_swing, combo_aoe_dash, track_projectiles));
            double[] weights = {
                    //PHASE ONE 0-100% HP
                    (shoot_magic_projectiles != prevAttack && !this.isAngeredState()) ? distance * 0.02 : 0,
                    (shoot_magic_fireball != prevAttack && !this.isAngeredState()) ? distance * 0.02 : 0,
                    (summon_mobs != prevAttack && !this.isAngeredState() && current_mobs.size() <= MobConfig.lich_active_mob_count + (playersNearbyAmount * MobConfig.lich_active_mob_count_multiplayer)) ? distance * 0.02 : 0,
                    //PHASE TWO 0-75% HP
                    (combo_magic != prevAttack && !this.isAngeredState() && HealthChange <= 0.75) ? distance * 0.02 : 0,
                    //PHASE THREE 0-50% HP
                    (throw_staff != prevAttack && !this.isAngeredState() && HealthChange <= 0.5) ? distance * 0.02 : 0,
                    //PHASE ONE 0-100% HP
                    (dash_attack_red != prevAttack && this.isAngeredState() && distance > 5) ? distance * 0.02 : (dash_attack_red != prevAttack && !this.isAngeredState()) ? distance * 0.02 : 0,
                    //PHASE TWO 0-75% HP MELEE STUFF
                    (green_attack != prevAttack && this.isAngeredState() && distance > 4 && HealthChange <= 0.75) ? 1/distance : (green_attack != prevAttack && !this.isAngeredState() && HealthChange <= 0.75) ? distance * 0.01 : 0,
                    (regular_swing != prevAttack && this.isAngeredState() && distance <= 5 && HealthChange <= 0.75) ? 1/distance : 0,
                    (double_swing != prevAttack && this.isAngeredState() && distance <= 6 && HealthChange <= 0.75) ? 1/distance : 0,
                    //PHASE THREE 0-50% HP
                    (combo_aoe_dash != prevAttack && green_attack != prevAttack && this.isAngeredState() && HealthChange <= 0.5) ? 1/distance : (combo_aoe_dash != prevAttack && green_attack != prevAttack && !this.isAngeredState() && HealthChange <= 0.5) ? distance * 0.02 : 0,
                    (track_projectiles != prevAttack && !this.isAngeredState() && HealthChange <= 0.5) ? distance * 0.02 : 0

            };
            prevAttack = ModRand.choice(close_attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        return (this.isAngeredState()) ? MobConfig.lich_static_cooldown : cooldown;
    }


    private final Consumer<EntityLivingBase> track_projectiles = (target) -> {
      this.setComboTrackProjectiles(true);
      this.setFightMode(true);

      addEvent(()-> this.setImmovable(true), 10);
      addEvent(()-> new ActionShootTrackingProjectiles().performAction(this, target), 25);

      addEvent(()-> this.setImmovable(false), 50);

      addEvent(()-> {
        this.setComboTrackProjectiles(false);
        this.setFightMode(false);
      }, 60);
    };

    private final Consumer<EntityLivingBase> combo_aoe_dash = (target) -> {
      this.setComboAoeAttack(true);
      this.setFightMode(true);

        addEvent(()-> {
            this.lockLook = true;
            this.clearCurrentVelocity = true;
        }, 20);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-4));
            int y = ModUtils.getSurfaceHeightLich(world, new BlockPos(softTargetPos.x, 0, softTargetPos.z),(int) target.posY - 8, (int)target.posY + 5);

            addEvent(()-> {
                if(y != 0) {
                    new ActionLichTeleport(ModColors.AZURE, new Vec3d(softTargetPos.x, y + 1, softTargetPos.z)).performAction(this, target);
                    this.destroyCloseBlocks = true;
                    addEvent(()-> {
                        this.setImmovable(true);
                        addEvent(()-> {
                            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.5, 0)));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                            float damage = this.getAttack();
                            ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
                            addEvent(()-> {
                                //AOE ACTION
                                float distance = this.getDistance(target);
                                new ActionSelfAOE((int) distance + 4).performAction(this, target);
                            }, 5);
                        }, 5);
                    }, 5);

                } else {
                    new ActionLichTeleport(ModColors.AZURE, new Vec3d(softTargetPos.x, target.posY, softTargetPos.z)).performAction(this, target);
                    this.destroyCloseBlocks = true;
                    addEvent(()-> {
                        this.setImmovable(true);
                        addEvent(()-> {
                            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.5, 0)));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                            float damage = this.getAttack();
                            ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
                            addEvent(()-> {
                                //AOE ACTION
                                float distance = this.getDistance(target);
                                new ActionSelfAOE((int) distance + 4).performAction(this, target);
                            }, 5);
                        }, 5);
                    }, 5);
                }
            }, 5);
        }, 35);

        addEvent(()-> {
            this.lockLook = false;
            this.destroyCloseBlocks = false;
        }, 65);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d adjusted = target.getPositionVector().add(posSet.scale(-9));
            int y = ModUtils.getSurfaceHeightLich(world, new BlockPos(adjusted.x, 0, adjusted.z), (int) target.posY - 3, (int) target.posY + 3);
            this.setImmovable(false);
            if(y != 0) {
                new ActionLichTeleport(ModColors.AZURE, new Vec3d(adjusted.x, y + 1, adjusted.z)).performAction(this, target);
                this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
                Vec3d targetPos = target.getPositionVector();
                destroyCloseBlocks = true;
                addEvent(()-> {
                    this.enableRedParticles = true;
                    this.lockLook = true;
                    double distance = this.getPositionVector().distanceTo(targetPos);
                    ModUtils.leapTowards(this, targetPos, (float) (distance * 0.13),0.1F);
                    this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                }, 10);
            } else {
                new ActionLichTeleport(ModColors.AZURE, new Vec3d(adjusted.x, target.posY, adjusted.z)).performAction(this, target);
                this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
                Vec3d targetPos = target.getPositionVector();
                destroyCloseBlocks = true;
                addEvent(()-> {
                    this.enableRedParticles = true;
                    this.lockLook = true;
                    double distance = this.getPositionVector().distanceTo(targetPos);
                    ModUtils.leapTowards(this, targetPos, (float) (distance * 0.13),0.1F);
                    this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                }, 10);
            }
        }, 85);

        addEvent(()-> {
            for(int b = 0; b <= 14; b += 2) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.5, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = this.getAttack();
                    ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                }, b);
            }
        }, 95);

        addEvent(()-> {
            this.setImmovable(true);
            this.standbyOnVel = false;
            this.lockLook = false;
            this.enableRedParticles = false;
            this.destroyCloseBlocks = false;
        }, 108);

      addEvent(()-> {
          this.setImmovable(false);
        this.setComboAoeAttack(false);
        this.setFightMode(false);
      }, 125);
    };
    private final Consumer<EntityLivingBase> double_swing = (target) -> {
      this.setDoubleSwing(true);
      this.setFightMode(true);

      addEvent(()-> {
          this.setImmovable(true);
          Vec3d targetedPos = target.getPositionVector();
          addEvent(()-> {
              this.setImmovable(false);
              this.lockLook = true;
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.13),0F);
          }, 8);
      }, 17);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.4f, 0, false);
      }, 30);

      addEvent(()-> {
        this.lockLook =false;
        addEvent(()-> {
            this.setImmovable(true);
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                this.lockLook = true;
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18),0F);
            }, 8);
        }, 7);
      }, 40);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.8f, 0, false);
      }, 61);

      addEvent(()-> {
        this.setImmovable(true);
        this.lockLook =false;
      }, 70);

      addEvent(()-> {
          this.setImmovable(false);
        this.setDoubleSwing(false);
        this.setFightMode(false);
      }, 95);
    };
    private final Consumer<EntityLivingBase> regular_swing = (target) -> {
      this.setSwingAttack(true);
      this.setFightMode(true);

      addEvent(()-> {
          this.setImmovable(true);
          Vec3d targetedPos = target.getPositionVector();
          addEvent(()-> {
              this.setImmovable(false);
              this.lockLook = true;
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18),0F);
          }, 7);
      }, 20);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.4f, 0, false);
      },33);

      addEvent(()-> {
        this.setImmovable(true);
      }, 40);

      addEvent(()-> {
          this.lockLook =false;
      }, 48);

      addEvent(()-> {
          this.setImmovable(false);
        this.setSwingAttack(false);
        this.setFightMode(false);
      }, 55);
    };

    private final Consumer<EntityLivingBase> green_attack = (target) -> {
      this.setGreenAttack(true);
      this.setFightMode(true);

      addEvent(()-> {
          this.lockLook = true;
          this.clearCurrentVelocity = true;
      }, 20);

      addEvent(()-> {
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
        Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-4));
        int y = ModUtils.getSurfaceHeightLich(world, new BlockPos(softTargetPos.x, 0, softTargetPos.z),(int) target.posY - 8, (int)target.posY + 5);

        addEvent(()-> {
        if(y != 0) {
            new ActionLichTeleport(ModColors.AZURE, new Vec3d(softTargetPos.x, y + 1, softTargetPos.z)).performAction(this, target);
            this.destroyCloseBlocks = true;
            addEvent(()-> {
                this.setImmovable(true);
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.5, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = this.getAttack();
                    ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
                    addEvent(()-> {
                        //AOE ACTION
                        float distance = this.getDistance(target);
                        new ActionSelfAOE((int) distance + 4).performAction(this, target);
                    }, 5);
                }, 5);
            }, 5);

        } else {
            new ActionLichTeleport(ModColors.AZURE, new Vec3d(softTargetPos.x, target.posY, softTargetPos.z)).performAction(this, target);
            this.destroyCloseBlocks = true;
            addEvent(()-> {
                this.setImmovable(true);
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.5, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = this.getAttack();
                    ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
                    addEvent(()-> {
                        //AOE ACTION
                        float distance = this.getDistance(target);
                        new ActionSelfAOE((int) distance + 4).performAction(this, target);
                    }, 5);
                }, 5);
            }, 5);
        }
        }, 5);
      }, 35);

      addEvent(()-> {
          this.setImmovable(false);
          this.lockLook = false;
          this.standbyOnVel = false;
          this.destroyCloseBlocks = false;
      }, 80);


      addEvent(()-> {
        this.setGreenAttack(false);
        this.setFightMode(false);
      }, 90);
    };
    private final Consumer<EntityLivingBase> dash_attack_red = (target) -> {
      this.setFightMode(true);
      this.setRedAttack(true);

      addEvent(()-> {
        this.setImmovable(true);
        this.clearCurrentVelocity = true;
      }, 15);

      addEvent(()-> {
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          addEvent(()-> {
              this.setImmovable(false);
              Vec3d adjusted = target.getPositionVector().add(posSet.scale(-9));
              int y = ModUtils.getSurfaceHeightLich(world, new BlockPos(adjusted.x, 0, adjusted.z), (int) target.posY - 3, (int) target.posY + 3);

              if(y != 0) {
                 // ModUtils.attemptTeleport(new Vec3d(adjusted.x, y + 1, adjusted.z), this);
                  new ActionLichTeleport(ModColors.AZURE, new Vec3d(adjusted.x, y + 1, adjusted.z)).performAction(this, target);
                  this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
                  Vec3d targetPos = target.getPositionVector();
                  destroyCloseBlocks = true;
                  addEvent(()-> {
                  this.enableRedParticles = true;
                  this.lockLook = true;
                      double distance = this.getPositionVector().distanceTo(targetPos);
                      ModUtils.leapTowards(this, targetPos, (float) (distance * 0.13),0.1F);
                      this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                  }, 10);
              } else {
                  //ModUtils.attemptTeleport(new Vec3d(adjusted.x, target.posY, adjusted.z), this);
                  new ActionLichTeleport(ModColors.AZURE, new Vec3d(adjusted.x, target.posY, adjusted.z)).performAction(this, target);
                  this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
                  Vec3d targetPos = target.getPositionVector();
                  destroyCloseBlocks = true;
                  addEvent(()-> {
                      this.enableRedParticles = true;
                      this.lockLook = true;
                      double distance = this.getPositionVector().distanceTo(targetPos);
                      ModUtils.leapTowards(this, targetPos, (float) (distance * 0.13),0.1F);
                      this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                  }, 10);
              }
          }, 7);
      }, 38);


      addEvent(()-> {
          for(int b = 0; b <= 14; b += 2) {
              addEvent(()-> {
                  Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.5, 0)));
                  DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                  float damage = this.getAttack();
                  ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.6f, 0, false);
              }, b);
          }

      }, 55);

      addEvent(()-> {
          this.enableRedParticles = false;
          this.lockLook =false;
          this.destroyCloseBlocks = false;
          this.setImmovable(true);
      }, 70);

      addEvent(()-> {
          this.setImmovable(false);
      }, 90);

      addEvent(()-> {
          standbyOnVel = false;
        this.setFightMode(false);
        this.setRedAttack(false);
      }, 95);
    };
    private final Consumer<EntityLivingBase> throw_staff = (target) -> {
      this.setFightMode(true);
      this.setThrowStaff(true);

      addEvent(()-> {
          List<EntityPlayer> targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()), e -> !e.getIsInvulnerable());

          if(!targets.isEmpty()) {
            for(EntityPlayer player : targets) {
                new ActionBeginStaffThrow().performAction(this, player);
            }
          }
      }, 70);


      addEvent(()-> {
        this.setThrowStaff(false);
        this.setFightMode(false);
      }, 110);
    };
    private final Consumer<EntityLivingBase> combo_magic = (target) -> {
      this.setFightMode(true);
      this.setComboShootProjectiles(true);

      addEvent(()-> {
        //start combo event
        new ActionComboMagic().performAction(this, target);
      }, 25);

      addEvent(()-> {
        this.setFightMode(false);
        this.setComboShootProjectiles(false);
      }, 105);
    };
    private final Consumer<EntityLivingBase> summon_mobs = (target) -> {
      this.setFightMode(true);
      this.setPurpleAttack(true);

        //Mob Summoning
        addEvent(()-> {
            new ActionSummonMobs().performAction(this, target);
        }, 30);

      addEvent(()-> {
        this.setFightMode(false);
        this.setPurpleAttack(false);
      }, 85);
    };
    private final Consumer<EntityLivingBase> shoot_magic_fireball = (target) -> {
      this.setFightMode(true);
      this.setYellowAttack(true);

      addEvent(()-> this.setImmovable(true), 25);
      addEvent(()-> this.playSound(SoundsHandler.LICH_PREPARE_FIREBALL, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f)), 30);
      addEvent(()-> new ActionShootFireball().performAction(this, target), 60);
      addEvent(()-> this.setImmovable(false), 80);

      addEvent(()-> {
          this.setFightMode(false);
          this.setYellowAttack(false);
      }, 85);
    };

    private final Consumer<EntityLivingBase> shoot_magic_projectiles = (target) -> {
        this.setFightMode(true);
        this.setBlueAttack(true);

        addEvent(()-> {
            this.playSound(SoundsHandler.LICH_PREPARE_MISSILE, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            new ActionShootMagic().performAction(this, target);
            }, 30);

        addEvent(()-> {
        this.setFightMode(false);
        this.setBlueAttack(false);
        }, 85);
    };

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1)), ModColors.RED, Vec3d.ZERO, ModRand.range(10, 15));
            ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(2)), ModColors.RED, Vec3d.ZERO, ModRand.range(10, 15));
        }
    }

    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(this.enableRedParticles && world.rand.nextInt(2) == 0) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "book_controller", 0, this::predicateIdleBook));
        data.addAnimationController(new AnimationController(this, "model_idle", 0, this::predicateModelIdle));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttack));
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isGreenAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_GREEN, false));
                return PlayState.CONTINUE;
            }
            if(this.isBlueAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLUE, false));
                return PlayState.CONTINUE;
            }
            if(this.isRedAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_RED, false));
                return PlayState.CONTINUE;
            }
            if(this.isYellowAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_YELLOW, false));
                return PlayState.CONTINUE;
            }
            if(this.isPurpleAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PURPLE, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isDoubleSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DOUBLE_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isComboAOEAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_AOE, false));
                return PlayState.CONTINUE;
            }
            if(this.isComboShootProjectiles()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT_PATTERN, false));
                return PlayState.CONTINUE;
            }
            if(this.isComboTrackProjectiles()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_TRACK_PROJECTILES, false));
                return PlayState.CONTINUE;
            }
            if(this.isThrowStaff()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_THROW_STAFF, false));
                return PlayState.CONTINUE;
            }

        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateModelIdle(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MODEL_IDLE, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLY_IDLE, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateIdleBook(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BOOK_IDLE, true));
        return PlayState.CONTINUE;
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
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    //CHANGE THIS TO THAT WHEN ITS DAY IT DESPAWNS
    @Override
    protected boolean canDespawn() {
        return false;
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
    public void removeTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }
}
