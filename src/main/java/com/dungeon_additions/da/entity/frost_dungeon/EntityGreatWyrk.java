package com.dungeon_additions.da.entity.frost_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.EntityAIBlossom;
import com.dungeon_additions.da.entity.ai.EntityGreatWyrkAttackAI;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.*;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicGround;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.util.IEntitySound;
import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
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
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityGreatWyrk extends EntityAbstractGreatWyrk implements IAnimatable, IAnimationTickable, IAttack, IDirectionalRender, ITarget, IScreenShake, IEntitySound {

    private final String ANIM_WALK = "walk";
    private final String ANIM_IDLE = "idle";

    private final String ANIM_MEGA_STOMP = "mega_stomp";
    private final String ANIM_ROLL = "roll";
    private final String ANIM_MELEE_STRIKE = "melee_strike";
    private final String ANIM_SHAKE = "shake";
    private final String ANIM_SMALL_STOMPS = "small_stomps";
    private final String ANIM_DROP = "drop";
    private final String ANIM_LAZER_SHOOT = "lazer_shoot";
    private final String ANIM_SUMMON_AID = "summon_aid";
    private final String ANIM_SUMMON_BOSS = "summon_boss";

    protected boolean performLazerAttack = false;
    public boolean roll_state_active = false;

    Supplier<Projectile> ground_projectiles = () -> new ProjectileFrostGround(world, this, (float) this.getAttack() * 0.55F, null);
    public Vec3d chargePos;
    private boolean destroyBlocks = false;

    private boolean updateStateToMove = false;
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.NOTCHED_6));

    private int dropCounter = 0;
    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;

    private int shakeTime = 0;

    public EntityGreatWyrk(World worldIn) {
        super(worldIn);
        this.iAmBossMob = true;
        this.iAmBossMobWyrkNerf = true;
        this.experienceValue = 200;
        onSummonBoss();
    }

    public EntityGreatWyrk(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.iAmBossMob = true;
        this.iAmBossMobWyrkNerf = true;
        this.experienceValue = 200;
        BlockPos offset = new BlockPos(x, y, z);
        this.setSpawnLocation(offset);
        this.setSetSpawnLoc(true);
        onSummonBoss();
    }

    public EntityGreatWyrk(World worldIn, int timesused, BlockPos pos) {
        super(worldIn);
        this.iAmBossMob = true;
        this.timesUsed = timesused;
        this.iAmBossMobWyrkNerf = true;
        this.experienceValue = 200;
        this.setSpawnLocation(pos);
        this.setSetSpawnLoc(true);
        this.timesUsed++;
        this.doBossReSummonScaling();
        onSummonBoss();
    }

    public void onSummonBoss() {
        this.playSound(SoundsHandler.BIG_WYRK_SUMMON, 1.5f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setSummonBoss(true);
        this.setImmovable(true);
        this.lockLook = true;
        this.shakeTime = 80;
        this.setShaking(true);

        addEvent(()-> this.setShaking(false), 80);

        addEvent(()-> {
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setSummonBoss(false);
            this.setImmovable(false);
            this.lockLook = false;
        }, 100);
    }

    @Override
    public float getEyeHeight()
    {
        return this.height * 0.7F;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        this.bossInfo.setPercent(getHealth() / getMaxHealth());
        if(world.isRemote && ticksExisted == 1 && ModConfig.experimental_features && MobConfig.ancient_wyrk_boss_music) {
            this.playMusic(this);
        }
        EntityLivingBase target = this.getAttackTarget();
        shakeTime--;
        if(!world.isRemote && target != null) {
            if(this.destroyBlocks) {
                AxisAlignedBB box = getEntityBoundingBox().grow(1.25, 0.1, 1.25).offset(0, 0.1, 0);
                ModUtils.destroyBlocksInAABBWyrk(box, world, this);
            }

            if(this.updateStateToMove) {
                AxisAlignedBB box = getEntityBoundingBox().grow(1.25, 0.1, 1.25).offset(0, 0.1, 0);
                ModUtils.destroyBlocksInAABB(box, world, this);
            }

            //This Forces the Entity into that Direction at a Constant Speed
            if(chargePos != null && this.roll_state_active) {
                BlockPos asPos = new BlockPos(chargePos.x, chargePos.y, chargePos.z);
                this.getNavigator().tryMoveToXYZ(asPos.getX(), asPos.getY(), asPos.getZ(), 2.4D);
                this.getLookHelper().setLookPosition(asPos.getX(), this.getPositionVector().y + 2, asPos.getZ(), 3, 3);
                ModUtils.facePosition(chargePos, this, 10, 10);
            }

            if(this.roll_state_active) {
                List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable());
                if(!nearbyEntities.isEmpty()) {
                    for(EntityLivingBase base : nearbyEntities) {
                        Vec3d offset = base.getPositionVector().add(ModUtils.getRelativeOffset(base, new Vec3d(0.3, 0.3, 0)));
                        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                        float damage = (float) (this.getAttack());
                        ModUtils.handleAreaImpact(0.7f, (e)-> damage, this, offset, source, 1.2f, 0, false);
                    }
                }
            }
        }

    }



    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!world.isRemote && performLazerAttack) {
            lazerAttack.update();
        }
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityGreatWyrkAttackAI<>(this, 1.0, 140, 30F, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);

        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && !this.isSummonBoss()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(mega_stomp_action, roll_action, melee_attack, shake_attack, small_stomps_attack, drop_attack, lazer_action, summon_aid));
            double[] weights = {
                    (distance <= 15 && prevAttacks != mega_stomp_action) ? 1/distance : 0, //Mega Stomp
                    (distance <= 9 && prevAttacks != roll_action) ? 1/distance : (distance >= 10 && prevAttacks != roll_action) ? distance * 0.02 + (dropCounter * 0.01) : 0, // Roll Action
                    (distance <= 9 && prevAttacks != melee_attack) ? 1/distance : 0, // Melee Strike
                    (distance <= 31 && distance >= 14 && prevAttacks != shake_attack) ? distance * 0.02 : 0, //Projectile Barrage
                    (distance <= 31 && distance >= 9 && prevAttacks != small_stomps_attack) ? distance * 0.02 : 0, //Small Stomps
                    (distance <= 16 && prevAttacks != drop_attack && HealthChange <= 0.5) ? 1/distance : 0, //Drop Attack
                    (distance <= 31 && prevAttacks != lazer_action && distance >= 14 && HealthChange <= 0.5) ? distance * 0.02 : 0, //Lazer Attack
                    (distance <= 31 && prevAttacks != summon_aid && distance >= 14 && HealthChange <= 0.75) ? distance * 0.02 : (distance < 14 && prevAttacks != summon_aid && HealthChange <= 0.75) ? 1/distance : 0 // Summon Aid Attack
            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return prevAttacks == roll_action ? (MobConfig.great_wyrk_cooldown * 20) + 80 : (MobConfig.great_wyrk_cooldown * 20);
    }

    private Consumer<EntityLivingBase> summon_aid = (target) -> {
      this.setFightMode(true);
      this.setSummonAid(true);
      this.setImmovable(true);

      addEvent(()-> this.playSound(SoundsHandler.BIG_WYRK_SUMMON_FOOT, 1.5f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 5);
      addEvent(()-> new ActionSummonWyrkFoot().performAction(this, target), 20);

        addEvent(()-> this.playSound(SoundsHandler.BIG_WYRK_SUMMON_FOOT, 1.5f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 30);
        addEvent(()-> new ActionSummonWyrkFoot().performAction(this, target), 45);

      addEvent(()-> {
        this.setSummonAid(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 60);
    };

    private Consumer<EntityLivingBase> lazer_action = (target) -> {
      this.setFightMode(true);
      this.setLazerAttack(true);
      this.setImmovable(true);
        this.playSound(SoundsHandler.BIG_WYRK_LAZER, 1.5f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      addEvent(()-> {
          this.performLazerAttack = true;
        lazerAttack.doAction();
      }, 10);

      addEvent(()-> {
          this.performLazerAttack = false;
      }, 110);

      addEvent(()-> {
        this.setFightMode(false);
        this.setLazerAttack(false);
        this.setImmovable(false);
      }, 125);
    };

    private Consumer<EntityLivingBase> drop_attack = (target) -> {
      this.setDropAttack(true);
      this.setFightMode(true);
      this.lockLook = true;
      this.setImmovable(true);

      addEvent(()-> this.playSound(SoundsHandler.BIG_WYRK_DROP, 1.5f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 30);
      addEvent(()-> {
        //action star stomp
          new ActionStarStomp().performAction(this, target);
          this.setUpdateIcicles = true;
          this.setShaking(true);
          this.shakeTime = 20;
          addEvent(()-> {
              this.setUpdateIcicles = false;
          }, 5);
              Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
              Main.proxy.spawnParticle(20,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
      }, 55);

      addEvent(()-> {
        this.setShaking(false);
      }, 75);

      addEvent(()-> {
          this.lockLook = false;
        this.setDropAttack(false);
        this.setImmovable(false);
        this.setFightMode(false);
      }, 90);
    };

    private Consumer<EntityLivingBase> small_stomps_attack = (target) -> {
      this.setFightMode(true);
      this.setImmovable(true);
      this.setSmallStomps(true);
        this.dropCounter++;
      addEvent(()-> {
        this.lockLook = true;
        addEvent(()-> {
            //action
            new ActionGroundSweep(ground_projectiles, 1.0F).performAction(this, target);
            this.setUpdateIcicles = true;
            addEvent(()-> {
                this.setUpdateIcicles = false;
            }, 5);
        }, 10);
      }, 20);

      addEvent(()-> this.lockLook = false, 40);

        addEvent(()-> {
            this.lockLook = true;
            addEvent(()-> {
                //action
                new ActionGroundSweep(ground_projectiles, 1.0F).performAction(this, target);
                this.setUpdateIcicles = true;
                addEvent(()-> {
                    this.setUpdateIcicles = false;
                }, 5);
            }, 10);
        }, 55);

        addEvent(()-> this.lockLook = false, 75);

      addEvent(()-> {
        this.setFightMode(false);
        this.setImmovable(false);
        this.setSmallStomps(false);
      }, 90);
    };

    private Consumer<EntityLivingBase> shake_attack = (target) -> {
      this.setShake(true);
      this.setFightMode(true);
      this.setImmovable(true);
      this.dropCounter++;

      addEvent(()-> {
          new ActionSummonFrostBarrage().performAction(this, target);
      }, 30);

      addEvent(()-> {
        this.setShake(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 130);
    };
    private Consumer<EntityLivingBase> melee_attack = (target) -> {
      this.setFightMode(true);
      this.setMeleeStrike(true);
      this.setImmovable(true);
        this.playSound(SoundsHandler.BIG_WYRK_STRIKE, 1.5f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));

      addEvent(()-> {
          this.lockLook = true;
          Vec3d targetedPos = target.getPositionVector();
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18), 0.1F);
          }, 10);
      }, 30);

      addEvent(()-> {
          this.setShaking(true);
          this.shakeTime = 20;
      }, 45);

      addEvent(()-> this.setShaking(false), 55);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.75, 2.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (float) (this.getAttack() * 0.75);
          ModUtils.handleAreaImpact(3.0f, (e) -> damage, this, offset, source, 0.6f, 0, false, MobEffects.SLOWNESS, 0, 100);
          new ActionProgressiveRing().performAction(this, target);
              Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.2, 0)));
              Main.proxy.spawnParticle(18,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
        //  this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f));
          addEvent(()-> {
              this.setImmovable(true);
          }, 4);
      }, 46);

      addEvent(()-> this.lockLook = false, 60);

      addEvent(()-> {
        this.setFightMode(false);
        this.setImmovable(false);
        this.setMeleeStrike(false);
      }, 70);
    };

    private Consumer<EntityLivingBase> roll_action = (target) -> {
      this.setFightMode(true);
      this.setRoll(true);
      this.setImmovable(true);
      this.dropCounter = 0;

      addEvent(()-> {
          this.playSound(SoundsHandler.BIG_WYRK_RISE, 1.5f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 15);

      addEvent(()-> {
          this.setShaking(true);
          this.shakeTime = 20;
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75, 1.2, 0)));
          Main.proxy.spawnParticle(20,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
      }, 45);

      addEvent(()-> {
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
          Main.proxy.spawnParticle(20,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
      }, 58);

        addEvent(()-> {
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            Main.proxy.spawnParticle(20,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
        }, 70);

        addEvent(()-> {
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            Main.proxy.spawnParticle(20,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
        }, 90);

        addEvent(()-> {
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            Main.proxy.spawnParticle(20,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
        }, 110);

      addEvent(()-> {
          this.lockLook = true;
          Vec3d target_curr_pos = target.getPositionVector();
          addEvent(()-> {
              this.setImmovable(false);
              this.destroyBlocks = true;

              Vec3d startPos = this.getPositionVector().add(ModUtils.yVec(1.0D));
              Vec3d targetedPos = target_curr_pos.add(ModUtils.yVec(1.0D));
              Vec3d Dir = targetedPos.subtract(startPos).normalize();
              AtomicReference<Vec3d> teleportPos = new AtomicReference<>(targetedPos);
              int maxDistance = 40;
              ModUtils.lineCallback(targetedPos.add(Dir), targetedPos.add(Dir.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                  boolean safeLanding = ModUtils.cubePoints(-2, -2, -2, 2, 0, 2).stream()
                          .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                  .isSideSolid(world, new BlockPos(pos.add(off)).down(), EnumFacing.UP));
                  boolean notOpen = ModUtils.cubePoints(-3, 1, -3, 3, 4, 3).stream()
                          .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                  .causesSuffocation());
                  if(safeLanding && !notOpen) {
                      teleportPos.set(pos);
                  }
                  this.chargePos = teleportPos.get();
              });
              this.roll_state_active = true;

              addEvent(()-> {
                  for(int i = 0; i < 60; i += 10) {
                      addEvent(()-> {
                          new ActionLaunchSIdeProjectiles(ground_projectiles, 1.2F).performAction(this, target);
                      }, i);
                  }
              }, 15);
          }, 5);
      }, 35);

      addEvent(()-> this.playSound(SoundsHandler.BIG_WYRK_ROLL, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 40);
        addEvent(()-> this.playSound(SoundsHandler.BIG_WYRK_ROLL, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 60);
        addEvent(()-> this.playSound(SoundsHandler.BIG_WYRK_ROLL, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 80);
        addEvent(()-> this.playSound(SoundsHandler.BIG_WYRK_ROLL, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 100);

      addEvent(()-> {
        this.setImmovable(true);
        this.roll_state_active = false;
        this.chargePos = null;
        this.updateStateToMove = true;
        this.lockLook = false;
        this.setShaking(false);
        this.getNavigator().clearPath();
        if(target != null) {
            this.getLookHelper().setLookPosition(target.posX, this.getPositionVector().y + 2, target.posZ, 3, 3);
            ModUtils.facePosition(target.getPositionVector(), this, 10, 10);
        }
        addEvent(()-> this.updateStateToMove = false, 5);
      }, 115);


      addEvent(()-> {
        this.setImmovable(false);
        this.setRoll(false);
        this.setFightMode(false);
      }, 135);
    };

    private Consumer<EntityLivingBase> mega_stomp_action = (target) -> {
      this.setFightMode(true);
      this.setMegaStomp(true);
      this.setImmovable(true);
        addEvent(()-> {
            this.playSound(SoundsHandler.BIG_WYRK_RISE, 1.5f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 15);

      addEvent(()-> this.lockLook = true, 25);

      addEvent(()-> {
          this.setShaking(true);
          this.shakeTime = 30;
      }, 45);

      addEvent(()-> {
          this.setShaking(false);
      }, 60);

      addEvent(()-> {
            double distance = this.getDistance(target);
            if(distance > 6) {
                new ActionMegaStompAOE(true, distance).performAction(this, target);
            } else {
                new ActionMegaStompAOE(false, 0).performAction(this, target);
            }
            this.setUpdateIcicles = true;
            addEvent(()-> {
                this.setUpdateIcicles = false;
            }, 5);

          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.75, 0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (float) (this.getAttack() * 0.75);
          ModUtils.handleAreaImpact(4f, (e) -> damage, this, offset, source, 1.5f, 0, false, MobEffects.WEAKNESS, 0, 200);
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75, 1.2, 0)));
          Main.proxy.spawnParticle(20,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
      }, 45);

      addEvent(()-> this.lockLook = false, 80);
      addEvent(()-> {
        this.setFightMode(false);
        this.setMegaStomp(false);
        this.setImmovable(false);
      },90 );
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSummonBoss()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_BOSS, false));
                return PlayState.CONTINUE;
            }
            if(this.isMegaStomp()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MEGA_STOMP, false));
                return PlayState.CONTINUE;
            }
            if(this.isRoll()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ROLL, false));
                return PlayState.CONTINUE;
            }
            if(this.isMeleeStrike()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MELEE_STRIKE, false));
                return PlayState.CONTINUE;
            }
            if(this.isShake()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHAKE, false));
                return PlayState.CONTINUE;
            }
            if(this.isSmallStomps()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SMALL_STOMPS, false));
                return PlayState.CONTINUE;
            }
            if(this.isDropAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DROP, false));
                return PlayState.CONTINUE;
            }
            if(this.isLazerAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LAZER_SHOOT, false));
                return PlayState.CONTINUE;
            }
            if(this.isSummonAid()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_AID, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }


    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if (id == stopLazerByte) {
            this.renderLazerPos = null;
        }
        else if(id == ModUtils.PARTICLE_BYTE) {
            for (int i = 0; i < 5; i++) {
                Vec3d lookVec = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
                Vec3d randOffset = ModUtils.rotateVector2(lookVec, lookVec.crossProduct(ModUtils.Y_AXIS), ModRand.range(-70, 70));
                randOffset = ModUtils.rotateVector2(randOffset, lookVec, ModRand.range(0, 360)).scale(1.5f);
                Vec3d velocity = Vec3d.ZERO.subtract(randOffset).normalize().scale(0.15f).add(new Vec3d(this.motionX, this.motionY, this.motionZ));
                Vec3d particlePos = this.getPositionEyes(1).add(ModUtils.getAxisOffset(lookVec, new Vec3d(1.4, 0, 0))).add(randOffset);
                ParticleManager.spawnColoredSmoke(world, particlePos, ModColors.WHITE, velocity);
            }
        }
    }

    private boolean initiateDeathText = false;

    @Override
    public void onDeath(DamageSource cause) {

        if(cause.getTrueSource() instanceof EntityPlayer || cause.getImmediateSource() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) cause.getTrueSource());
            if(!ModUtils.getAdvancementCompletionAsListBase(player, ModConfig.assassins_spawn_progress) && !initiateDeathText) {
                assert player != null;
                player.sendMessage(new TextComponentString(TextFormatting.RED + "You have now caught the attention of something lurking in the shadows..."));
                initiateDeathText = true;
            }
        }
        if(this.getSpawnLocation() != null && !world.isRemote) {
            this.turnBossIntoSummonSpawner(this.getSpawnLocation());
        }
        super.onDeath(cause);
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

    /**
     * Add a bit of brightness to the entity for it's Arena
     */
    @Override
    public int getBrightnessForRender() {
        return Math.min(super.getBrightnessForRender() + 30, 200);
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "great_wyrk");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
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

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        super.readEntityFromNBT(nbt);
    }

    @Override
    public Optional<Vec3d> getTarget() {
        return Optional.ofNullable(renderLazerPos);
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 30.0F);
            if (dist >= 30.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.75F * screamMult);
        }
        return 0;
    }

    @Nullable
    @Override
    public SoundEvent getBossMusic() {
        return SoundsHandler.ANCIENT_WYRK_TRACK;
    }
}
