package com.dungeon_additions.da.entity.night_lich;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.ai.flying.EntityAIAerialAttack;
import com.dungeon_additions.da.entity.ai.flying.FlyingMoveHelper;
import com.dungeon_additions.da.entity.ai.flying.TimedAttackInitiator;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileFlameSling;
import com.dungeon_additions.da.entity.night_lich.action.*;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityNightLich extends EntityAbstractNightLich implements IAnimatable, IAttack, IAnimationTickable, IScreenShake {


    private Consumer<EntityLivingBase> prevAttack;

    Supplier<Projectile> ground_projectiles = () -> new ProjectileMagicGround(world, this, (float) MobConfig.night_lich_attack_damage, null);
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
    private final String ANIM_RAGE_MODE = "rage_mode";
    //Non Colored
    private final String ANIM_SWING = "swing";
    private final String ANIM_DOUBLE_SWING = "double_swing";
    private final String ANIM_MELEE_COMBO = "melee_combo";


    private AnimationFactory factory = new AnimationFactory(this);

    //it either equals 30 or 3
    public int wantedDistance;
    private boolean enableRedParticles = false;
    private boolean enableFireballParticles = false;

    private boolean destroyCloseBlocks = false;
    public boolean doesBossSlowDown = false;
    private int shakeTime = 0;

    public EntityNightLich(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.1F, 2.5F);
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.wantedDistance = 30;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.experienceValue = MobConfig.lich_experience_value;
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
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.experienceValue = MobConfig.lich_experience_value;
        if(!world.isRemote) {
            initLichAI();
        }

    }

    public EntityNightLich(World worldIn, int timesUsed, BlockPos pos) {
        super(worldIn);
        this.timesUsed = timesUsed;
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.setSize(1.1F, 2.5F);
        this.wantedDistance = 30;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.timesUsed++;
        this.doBossReSummonScaling();
        this.experienceValue = MobConfig.lich_experience_value;
        if(!world.isRemote) {
            initLichAI();
        }
    }


    private void initLichAI() {
        float attackDistance = this.isAngeredState() ? 3F : 30F;
        float attackDistanceFar = (float) (this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()) - 10;
        this.tasks.addTask(4, new EntityAIAerialAttack(this, attackDistanceFar, attackDistance, 35, new TimedAttackInitiator<>(this, 60)));

    }

    private int damageTaken = 80;

    public boolean clearCurrentVelocity = false;
    public boolean standbyOnVel = false;

    private int counterStateTimer = 1200;
    private int meleeCooldown = 400;
    private int animationLength = 0;

    private int resetWorldTimer = 60;
    private int timeTooDieTimer = 60;

    private int rageModeTimer = 0;
    private boolean hasRagedRecently = false;


    @Override
    public void onUpdate() {
        super.onUpdate();

        this.bossInfo.setPercent(getHealth() / getMaxHealth());
        EntityLivingBase target = this.getAttackTarget();
        this.shakeTime--;
        if(MobConfig.lich_enable_daylight && world.getWorldTime() < MobConfig.lich_summon_time && !world.isRemote) {
            if(timeTooDieTimer > 0) {
                timeTooDieTimer--;
            }

            if(timeTooDieTimer < 5) {
                this.setDead();
            }
        }

        if(target != null && !world.isRemote) {
            if(world.getWorldTime() > 17000 && MobConfig.lich_enable_daylight) {
                if(world.provider.getDimension() == 0) {
                    if(resetWorldTimer > 0) {
                        resetWorldTimer--;
                    }

                    if(resetWorldTimer < 1) {
                        world.setWorldTime(17000);
                        resetWorldTimer = 60;
                    }
                }
            }
            double healthCurr = this.getHealth() / this.getMaxHealth();
            //Performs Rage Mode and handles timer

            if(healthCurr <= 0.25  && hasRagedRecently) {
                if(rageModeTimer > 0) {
                    rageModeTimer--;
                }

                if(rageModeTimer <= 1) {
                    hasRagedRecently = false;
                    rageModeTimer = 1400 + ModRand.range(5, 200);
                }
            }

            if(healthCurr > 0.75) {
                this.setAngeredState(false);
                this.wantedDistance = 30;
                this.damageTaken = 80;
                this.hasRagedRecently = false;
            }

            if(meleeCooldown > 0 && !this.isAngeredState()) {
                meleeCooldown--;
            }

            if(healthCurr <= 0.75) {

                //allows for a different way of switching between melee and ranged mode with less things
                if(counterStateTimer > 0) {
                    counterStateTimer--;
                }

                if(wantedDistance == 30) {
                    if(this.hurtTime > 0 || counterStateTimer <= 0) {
                        this.damageTaken++;
                    }

                    if(damageTaken >= 100 && !this.isFightMode()) {
                        this.wantedDistance = 3;
                        this.counterStateTimer = 1200 + ModRand.range(5, 60);
                        if(!this.isAngeredState()) {
                            this.setAngeredState(true);
                            this.setStateChange = true;
                        }
                    }
                }
                if(wantedDistance == 3) {
                    if(this.hurtTime > 0 || counterStateTimer <= 0) {
                        this.damageTaken--;
                    }
                    if(damageTaken <= 0 && !this.isFightMode()) {
                        this.wantedDistance = 30;
                        this.counterStateTimer = 1200 + ModRand.range(5, 200);
                        if(this.isAngeredState()) {
                            this.setAngeredState(false);
                            this.setStateChange = true;
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

    private boolean setStateChange = false;
    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);

        int cooldown;
        int additive = 0;
        //this allows it so that the more mobs that are active, the slower the cooldown, but to a cap of the current mob limit

        if(current_mobs.size() >= MobConfig.lich_active_mob_count) {
            cooldown = (MobConfig.lich_active_mob_count * MobConfig.lich_progressive_cooldown) + MobConfig.lich_static_cooldown;
        } else {
            cooldown = (current_mobs.size() * MobConfig.lich_progressive_cooldown) + MobConfig.lich_static_cooldown;
        }
        double HealthChange = this.getHealth() / this.getMaxHealth();

        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> close_attacks = new ArrayList<>(Arrays.asList(shoot_magic_projectiles, shoot_magic_fireball, summon_mobs, combo_magic, throw_staff, dash_attack_red, green_attack, regular_swing, double_swing, combo_aoe_dash, track_projectiles, rage_mode_attack, melee_combo));
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
                    (dash_attack_red != prevAttack && this.isAngeredState() && distance > 5) ? distance * 0.02 : (dash_attack_red != prevAttack && !this.isAngeredState() && distance >= 15 && meleeCooldown <= 0) ? distance * 0.02 : 0,
                    //PHASE TWO 0-75% HP MELEE STUFF
                    (green_attack != prevAttack && this.isAngeredState() && distance > 4 && HealthChange <= 0.75) ? 1/distance : (green_attack != prevAttack && !this.isAngeredState() && HealthChange <= 0.75 && HealthChange >= 0.5 && meleeCooldown <= 0) ? distance * 0.01 : 0,
                    (regular_swing != prevAttack && this.isAngeredState() && distance <= 5 && HealthChange <= 0.75) ? 1/distance : 0,
                    (double_swing != prevAttack && this.isAngeredState() && distance <= 6 && HealthChange <= 0.75) ? 1/distance : 0,
                    //PHASE THREE 0-50% HP
                    (combo_aoe_dash != prevAttack && green_attack != prevAttack && this.isAngeredState() && HealthChange <= 0.5) ? 1/distance : (combo_aoe_dash != prevAttack && green_attack != prevAttack && !this.isAngeredState() && HealthChange <= 0.5 && meleeCooldown <= 0) ? distance * 0.02 : 0,
                    (track_projectiles != prevAttack && !this.isAngeredState() && HealthChange <= 0.5) ? distance * 0.02 : 0,
                    //BIG ATTACK
                    (rage_mode_attack != prevAttack && !this.isAngeredState() && !this.hasRagedRecently && HealthChange <= 0.5 && distance >= 25) ? distance * 0.05 : (rage_mode_attack != prevAttack && !this.isAngeredState() && !hasRagedRecently && HealthChange <= 0.25 && distance >= 25) ? distance * 0.03 : 0,
                    //MELEE COMBO
                    (melee_combo != prevAttack && this.isAngeredState() && HealthChange <= 0.5 && distance >= 7) ? 1/distance : 0

            };

            prevAttack = ModRand.choice(close_attacks, rand, weights).next();
            prevAttack.accept(target);

        }
        if(setStateChange) {
            additive = MobConfig.lich_additive_cooldown * 20;
            setStateChange = false;
        }
        return (this.isAngeredState()) ? (MobConfig.lich_static_cooldown/2) + animationLength : cooldown + additive + animationLength;
    }

    private final Consumer<EntityLivingBase> melee_combo = (target) -> {
        this.setFightMode(true);
        this.setMeleeCombo(true);
        this.animationLength = 145;
        this.playSound(SoundsHandler.LICH_PREPARE_COMBO, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));

        addEvent(()-> this.setImmovable(true), 20);

        addEvent(()-> {
            //start projectile combo
            new ActionMeleeCombo(ground_projectiles, 0.55F).performAction(this, target);
            addEvent(()-> this.setImmovable(false), 10);
            addEvent(()-> this.setImmovable(true), 20);
            addEvent(()-> this.setImmovable(false), 35);
            addEvent(()-> this.setImmovable(true), 45);
        }, 27);

        //teleport and dash action
        addEvent(() -> {
        this.setImmovable(false);
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-6));
            int y = ModUtils.getSurfaceHeightLich(world, new BlockPos(softTargetPos.x, 0, softTargetPos.z),(int) target.posY - 8, (int)target.posY + 5);
            if(y != 0) {
                new ActionLichTeleport(ModColors.AZURE, new Vec3d(softTargetPos.x, y + 1, softTargetPos.z)).performAction(this, target);
                this.destroyCloseBlocks = true;
                Vec3d targetPos = target.getPositionVector();
                addEvent(()-> {
                    this.enableRedParticles = true;
                    this.lockLook = true;
                    double distance = this.getPositionVector().distanceTo(targetPos);
                    ModUtils.leapTowards(this, targetPos, (float) (distance * 0.11),0F);
                    this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                }, 8);
            } else {
                new ActionLichTeleport(ModColors.AZURE, new Vec3d(softTargetPos.x, target.posY + 1, softTargetPos.z)).performAction(this, target);
                this.destroyCloseBlocks = true;
                Vec3d targetPos = target.getPositionVector();
                addEvent(()-> {
                    this.enableRedParticles = true;
                    this.lockLook = true;
                    double distance = this.getPositionVector().distanceTo(targetPos);
                    ModUtils.leapTowards(this, targetPos, (float) (distance * 0.13),0F);
                    this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                }, 8);
            }
        }, 104);

        addEvent(()-> this.playSound(SoundsHandler.LICH_USE_SPEAR, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f)), 95);
        //damage
        addEvent(()-> {
            this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack() * MobConfig.night_lich_dash_multiplier);
            ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.5f, 0, false);
        }, 118);

        addEvent(()-> {
            this.setImmovable(true);
            this.enableRedParticles = false;
            this.destroyCloseBlocks = false;
            this.lockLook = false;
        }, 130);

        addEvent(()-> {
        this.setImmovable(false);
        this.setFightMode(false);
        this.setMeleeCombo(false);
        }, 145);
    };
    private final Consumer<EntityLivingBase> rage_mode_attack = (target) -> {
        double HealthCurr = this.getHealth() / this.getMaxHealth();
      this.setRageMode(true);
      this.hasRagedRecently = true;
      this.animationLength = 130;
      this.setFightMode(true);
        List<EntityPlayer> soundSender = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()), e -> !e.getIsInvulnerable());
        if(!soundSender.isEmpty()) {
            for(EntityPlayer player : soundSender) {
                //play world sound
                if(!player.isSpectator() && !player.isCreative()) {
                    BlockPos pos = player.getPosition();
                    world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundsHandler.LICH_RAGE_PREPARE, SoundCategory.HOSTILE, 1.5F, world.rand.nextFloat() * 0.7F + 0.4F, false);
                }}
        }
      //some silly sounds and a crazy attack
        addEvent(()-> {
            List<EntityPlayer> targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()), e -> !e.getIsInvulnerable());

            if(!targets.isEmpty()) {
                int maxPlayersTooUse = 0;
                for(EntityPlayer player : targets) {
                    if(maxPlayersTooUse <= 3 && !player.isCreative() && !player.isSpectator()) {
                        new ActionLichRageMode().performAction(this, player);
                        maxPlayersTooUse++;
                    }
                }
            }

            if(!(target instanceof EntityPlayer)) {
                new ActionLichRageMode().performAction(this, target);
            }

        }, 10);

      addEvent(()-> {
          this.setRageMode(false);
          this.setFightMode(false);
          if(HealthCurr <= 0.5 && HealthCurr >= 0.3) {
              rageModeTimer = 60;
          }
      }, 130);
    };

    private final Consumer<EntityLivingBase> track_projectiles = (target) -> {
        this.animationLength = 60;
        this.playSound(SoundsHandler.LICH_PREPARE_COMBO, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
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
        this.animationLength = 125;
        this.playSound(SoundsHandler.LICH_PREPARE_COMBO, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
      this.setComboAoeAttack(true);
      this.setFightMode(true);

        addEvent(()-> {
            this.lockLook = true;
            this.clearCurrentVelocity = true;
        }, 20);

        addEvent(()-> this.playSound(SoundsHandler.LICH_USE_SPEAR, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f)), 25);
        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-4));
            int y = ModUtils.getSurfaceHeightLich(world, new BlockPos(softTargetPos.x, 0, softTargetPos.z),(int) target.posY - 8, (int)target.posY + 5);

            addEvent(()-> {
                if(y != 0) {
                    new ActionLichTeleport(ModColors.AZURE, new Vec3d(softTargetPos.x, y + 1, softTargetPos.z)).performAction(this, target);
                    this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
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
                                this.playSound(SoundsHandler.LICH_STAFF_IMPACT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
                                new ActionSelfAOE((int) distance + 3).performAction(this, target);
                            }, 5);
                        }, 5);
                    }, 5);

                } else {
                    new ActionLichTeleport(ModColors.AZURE, new Vec3d(softTargetPos.x, target.posY, softTargetPos.z)).performAction(this, target);
                    this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
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
                                this.playSound(SoundsHandler.LICH_STAFF_IMPACT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
                                new ActionSelfAOE((int) distance + 3).performAction(this, target);
                            }, 5);
                        }, 5);
                    }, 5);
                }
            }, 5);
        }, 35);

        addEvent(()-> {
            this.setShaking(true);
            this.shakeTime = 20;
        }, 65);

        addEvent(()-> {
            this.setShaking(false);
        }, 80);

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
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.0, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = (float) (this.getAttack() * MobConfig.night_lich_dash_multiplier);
                    ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.6f, 0, false);
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
        if(!this.isAngeredState()) {
            this.meleeCooldown = 400;
        }
      }, 125);
    };
    private final Consumer<EntityLivingBase> double_swing = (target) -> {
        this.animationLength = 95;
      this.setDoubleSwing(true);
      this.setFightMode(true);

        addEvent(()-> this.playSound(SoundsHandler.LICH_USE_SPEAR, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f)), 13);
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
          this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.4f, 0, false);
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
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.13),0F);
            }, 8);
        }, 7);
      }, 40);

      addEvent(()-> {
          this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.8f, 0, false);
      }, 61);

      addEvent(()-> new ActionShootGroundProjectiles(ground_projectiles).performAction(this, target), 65);

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
        this.animationLength = 55;
      this.setSwingAttack(true);
      this.setFightMode(true);

        addEvent(()-> this.playSound(SoundsHandler.LICH_USE_SPEAR, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f)), 15);
      addEvent(()-> {
          this.setImmovable(true);
          Vec3d targetedPos = target.getPositionVector();
          addEvent(()-> {
              this.setImmovable(false);
              this.lockLook = true;
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.13),0F);
          }, 7);
      }, 20);

      addEvent(()-> {
          this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
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
        double healthCurr = this.getHealth() / this.getMaxHealth();
        this.playSound(SoundsHandler.LICH_PREPARE_SPELL, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        if(!this.isAngeredState() && healthCurr >= 0.5) {
            this.setStateChange = true;
        }
        this.animationLength = 90;
      this.setGreenAttack(true);
      this.setFightMode(true);

      addEvent(()-> {
          this.lockLook = true;
          this.clearCurrentVelocity = true;
      }, 20);
        addEvent(()-> this.playSound(SoundsHandler.LICH_USE_SPEAR, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f)), 25);
      addEvent(()-> {
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
        Vec3d softTargetPos = target.getPositionVector().add(posSet.scale(-4));
        int y = ModUtils.getSurfaceHeightLich(world, new BlockPos(softTargetPos.x, 0, softTargetPos.z),(int) target.posY - 8, (int)target.posY + 5);

        addEvent(()-> {
        if(y != 0) {
            new ActionLichTeleport(ModColors.AZURE, new Vec3d(softTargetPos.x, y + 1, softTargetPos.z)).performAction(this, target);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
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
                        this.playSound(SoundsHandler.LICH_STAFF_IMPACT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
                        new ActionSelfAOE((int) distance + 3).performAction(this, target);
                    }, 5);
                }, 5);
            }, 5);

        } else {
            new ActionLichTeleport(ModColors.AZURE, new Vec3d(softTargetPos.x, target.posY, softTargetPos.z)).performAction(this, target);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
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
                        new ActionSelfAOE((int) distance + 3).performAction(this, target);
                    }, 5);
                }, 5);
            }, 5);
        }
        }, 5);
      }, 35);

      addEvent(()-> {
          this.setShaking(true);
          this.shakeTime = 20;
      }, 65);

      addEvent(()-> {
          this.setShaking(false);
      }, 80);
      addEvent(()-> {
          this.setImmovable(false);
          this.lockLook = false;
          this.standbyOnVel = false;
          this.destroyCloseBlocks = false;
          if(!this.isAngeredState()) {
              this.setStateChange = true;
              this.meleeCooldown = 400;
          }
      }, 80);


      addEvent(()-> {
        this.setGreenAttack(false);
        this.setFightMode(false);
      }, 90);
    };
    private final Consumer<EntityLivingBase> dash_attack_red = (target) -> {
        this.animationLength = 95;
        this.playSound(SoundsHandler.LICH_PREPARE_SPELL, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        double healthCurr = this.getHealth() / this.getMaxHealth();
        if(!this.isAngeredState() && healthCurr >= 0.75) {
            this.setStateChange = true;
        }
      this.setFightMode(true);
      this.setRedAttack(true);

      addEvent(()-> {
        this.setImmovable(true);
        this.clearCurrentVelocity = true;
      }, 15);

        addEvent(()-> this.playSound(SoundsHandler.LICH_USE_SPEAR, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f)), 23);
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
                  float damage = (float) (this.getAttack() * MobConfig.night_lich_dash_multiplier);
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
          if(!this.isAngeredState()) {
              this.meleeCooldown = 400;
          }
      }, 95);
    };
    private final Consumer<EntityLivingBase> throw_staff = (target) -> {
        this.animationLength = 110;
        this.playSound(SoundsHandler.LICH_PREPARE_COMBO, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
      this.setFightMode(true);
      this.setThrowStaff(true);

      addEvent(()-> {
          doesBossSlowDown = true;
      }, 20);

      addEvent(()-> this.playSound(SoundsHandler.LICH_USE_SPEAR, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f)), 40);
      addEvent(()-> {
          List<EntityPlayer> targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()), e -> !e.getIsInvulnerable());

          if(!targets.isEmpty()) {
            for(EntityPlayer player : targets) {
                    new ActionBeginStaffThrow().performAction(this, player);
            }
          }

          if(!(target instanceof EntityPlayer)) {
              new ActionBeginStaffThrow().performAction(this, target);
          }
      }, 70);

      addEvent(()-> {
        doesBossSlowDown = false;
      }, 100);

      addEvent(()-> {
        this.setThrowStaff(false);
        this.setFightMode(false);
      }, 110);
    };
    private final Consumer<EntityLivingBase> combo_magic = (target) -> {
        this.animationLength = 105;
        this.doesBossSlowDown = true;
        this.playSound(SoundsHandler.LICH_PREPARE_COMBO, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
      this.setFightMode(true);
      this.setComboShootProjectiles(true);

      addEvent(()-> {
        //start combo event
        new ActionComboMagic().performAction(this, target);
      }, 25);

      addEvent(()-> {
          this.doesBossSlowDown = false;
        this.setFightMode(false);
        this.setComboShootProjectiles(false);
      }, 105);
    };
    private final Consumer<EntityLivingBase> summon_mobs = (target) -> {
        this.setStateChange = true;
        this.animationLength = 85;
        this.doesBossSlowDown = true;
        this.playSound(SoundsHandler.LICH_PREPARE_SPELL, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
      this.setFightMode(true);
      this.setPurpleAttack(true);

        //Mob Summoning
        addEvent(()-> {
            new ActionSummonMobs().performAction(this, target);
        }, 30);

      addEvent(()-> {
        this.setFightMode(false);
        this.setPurpleAttack(false);
        this.doesBossSlowDown = false;
      }, 85);
    };
    private final Consumer<EntityLivingBase> shoot_magic_fireball = (target) -> {
        this.animationLength = 85;
        this.playSound(SoundsHandler.LICH_PREPARE_SPELL, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
      this.setFightMode(true);
      this.setYellowAttack(true);

      addEvent(()-> this.enableFireballParticles = true, 20);
      addEvent(()-> this.enableFireballParticles = false, 50);
      addEvent(()-> {
          this.setImmovable(true);
          this.clearCurrentVelocity = true;
      }, 25);
      addEvent(()-> this.playSound(SoundsHandler.LICH_PREPARE_FIREBALL, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f)), 30);
      addEvent(()-> new ActionShootFireball().performAction(this, target), 60);
      addEvent(()-> {
          this.setImmovable(false);
          this.standbyOnVel = false;
      }, 80);

      addEvent(()-> {
          this.setFightMode(false);
          this.setYellowAttack(false);
      }, 85);
    };

    private final Consumer<EntityLivingBase> shoot_magic_projectiles = (target) -> {
        this.animationLength = 85;
        this.playSound(SoundsHandler.LICH_PREPARE_SPELL, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
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
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1)), ModColors.AZURE, Vec3d.ZERO, ModRand.range(10, 15));
            ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(2)), ModColors.AZURE, Vec3d.ZERO, ModRand.range(10, 15));
            ModUtils.circleCallback(1, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1)), ModColors.AZURE, pos.normalize().scale(0.1), ModRand.range(10, 15));
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(2)), ModColors.AZURE, pos.normalize().scale(0.1), ModRand.range(10, 15));
            });
        }

        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(4)), ModColors.AZURE, Vec3d.ZERO, ModRand.range(10, 15));
        }

        if(id == ModUtils.FOURTH_PARTICLE_BYTE) {
            ModUtils.circleCallback(1, 30, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.z);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0))), ModColors.WHITE, pos.normalize().scale(0.1), ModRand.range(8, 10));
            });
        }
    }

    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(this.enableRedParticles && world.rand.nextInt(2) == 0) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
        if(this.enableFireballParticles && world.rand.nextInt(2) == 0) {
            world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
        }
        if(this.isRageMode() && world.rand.nextInt(2) == 0) {
            world.setEntityState(this, ModUtils.FOURTH_PARTICLE_BYTE);
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
            if(this.isRageMode()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_RAGE_MODE, false));
                return PlayState.CONTINUE;
            }
            if(this.isMeleeCombo()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MELEE_COMBO, false));
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

    /**
     * Add a bit of brightness to the entity, because otherwise it looks pretty black
     */
    @Override
    public int getBrightnessForRender() {
        return Math.min(super.getBrightnessForRender() + 20, 150);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.LICH_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {

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


    /**
     * Teleports the Night Lich if its cooldown is done to try and dodge attacks
     * @return
     */
    private boolean attemptTeleportFrom() {
        EntityLivingBase target = this.getAttackTarget();
        if(target != null && !world.isRemote) {
            new ActionRandomLichTeleport(ModColors.AZURE).performAction(this, target);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.6F));
            return true;
          //  Vec3d pos = ModRand.randVec().normalize().scale(12)
         //           .add(target.getPositionVector());
         //   boolean canSee = this.world.rayTraceBlocks(target.getPositionEyes(1), pos, false, true, false) == null;
         //   if(canSee) {
         //       ModUtils.attemptTeleport(pos, this);
         //       this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.6F));
         //       return true;
        //    }
        }
        return false;
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(teleportCooldownTimer < 1 && !this.isAngeredState() && !this.isFightMode()) {
            if(attemptTeleportFrom()) {
                teleportCooldownTimer = MobConfig.lich_teleport_timer * 20;
                return false;
            }
        }

        if (this.isAngeredState()) {
            return super.attackEntityFrom(source, (float) (amount * MobConfig.lich_melee_resistance));
        }
        return super.attackEntityFrom(source, amount);
    }

    private boolean initiateDeathText = false;

    @Override
    public void onDeath(DamageSource cause) {

        if(cause.getTrueSource() instanceof EntityPlayer || cause.getImmediateSource() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) cause.getTrueSource());
            if(!ModUtils.getAdvancementCompletionAsList(player, ModConfig.sorcerers_spawn_progress) && !initiateDeathText) {
                assert player != null;
                player.sendMessage(new TextComponentString(TextFormatting.RED + "They giggle at your progress..."));
                initiateDeathText = true;
            }
        }
        if(ModConfig.boss_resummon_enabled && this.timesUsed <= ModConfig.boss_resummon_max_uses) {
            BlockPos pos = this.getPosition();
            int y = getSurfaceHeight(world, new BlockPos(pos.getX(), 0, pos.getZ()), (int) this.posY - 45, (int) this.posY + 10);
            BlockPos posModified = new BlockPos(pos.getX(), y + 2, pos.getZ());
            world.setBlockState(posModified.down(), Blocks.STONEBRICK.getDefaultState());
            turnBossIntoSummonSpawner(posModified);
        }
        super.onDeath(cause);
    }

    private static final ResourceLocation LOOT_BOSS = new ResourceLocation(ModReference.MOD_ID, "night_lich");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_BOSS;
    }


    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 25.0F);
            if (dist >= 25.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.5F * screamMult);
        }
        return 0;
    }
}
