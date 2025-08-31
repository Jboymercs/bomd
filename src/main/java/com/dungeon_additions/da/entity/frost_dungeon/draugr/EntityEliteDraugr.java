package com.dungeon_additions.da.entity.frost_dungeon.draugr;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.EntityDraugrEliteAttackAI;
import com.dungeon_additions.da.entity.ai.EntityDraugrRangedAI;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.frost_dungeon.EntityAbstractGreatWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.EntityFrostBase;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.draugr_elite.*;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicGround;
import com.dungeon_additions.da.entity.night_lich.action.ActionLichTeleport;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.*;
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
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
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

public class EntityEliteDraugr extends EntityFrostBase implements IAnimatable, IAttack, IAnimationTickable, IScreenShake {

    private final String ANIM_IDLE = "idle";
    private final String ANIM_IDLE_UPPER = "idle_upper";
    private final String ANIM_WALK = "walk";
    private final String ANIM_WALK_UPPER = "walk_upper";

    private final String ANIM_DEATH = "death";
    private final String ANIM_SWING = "swing";
    private final String ANIM_DOUBLE_SWING = "double_swing";
    private final String ANIM_STOMP = "stomp";
    private final String ANIM_DISTANCE_CLOSER = "distance_closer";
    private final String ANIM_MAGIC_WAVE = "magic_wave";
    private final String ANIM_BEZERK = "bezerk";
    private final String ANIM_ICE_WAVE = "spike_arise";

    private static final DataParameter<Boolean> DEATH_STATE = EntityDataManager.createKey(EntityEliteDraugr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING = EntityDataManager.createKey(EntityEliteDraugr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DOUBLE_SWING = EntityDataManager.createKey(EntityEliteDraugr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STOMP = EntityDataManager.createKey(EntityEliteDraugr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DISTANCE_CLOSER = EntityDataManager.createKey(EntityEliteDraugr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MAGIC_WAVE = EntityDataManager.createKey(EntityEliteDraugr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BEZERK = EntityDataManager.createKey(EntityEliteDraugr.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ICE_WAVE = EntityDataManager.createKey(EntityEliteDraugr.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityEliteDraugr.class, DataSerializers.BOOLEAN);

    protected int playersNearbyAmount = 0;
    public boolean isDeathState() {return this.dataManager.get(DEATH_STATE);}
    private void setDeathState(boolean value) {this.dataManager.set(DEATH_STATE, Boolean.valueOf(value));}
    public boolean isSwingSimple() {return this.dataManager.get(SWING);}
    private void setSwingSimple(boolean value) {this.dataManager.set(SWING, Boolean.valueOf(value));}
    public boolean isDoubleSwing() {return this.dataManager.get(DOUBLE_SWING);}
    private void setDoubleSwing(boolean value) {this.dataManager.set(DOUBLE_SWING, Boolean.valueOf(value));}
    public boolean isStomp() {return this.dataManager.get(STOMP);}
    private void setStomp(boolean value) {this.dataManager.set(STOMP, Boolean.valueOf(value));}
    public boolean isDistanceCloser() {return this.dataManager.get(DISTANCE_CLOSER);}
    private void setDistanceCloser(boolean value) {this.dataManager.set(DISTANCE_CLOSER, Boolean.valueOf(value));}
    public boolean isMagicWave() {return this.dataManager.get(MAGIC_WAVE);}
    private void setMagicWave(boolean value) {this.dataManager.set(MAGIC_WAVE, Boolean.valueOf(value));}
    public boolean isBezerk() {return this.dataManager.get(BEZERK);}
    private void setBezerk(boolean value) {this.dataManager.set(BEZERK, Boolean.valueOf(value));}
    public boolean isIceWave() {return this.dataManager.get(ICE_WAVE);}
    private void setIceWave(boolean value) {this.dataManager.set(ICE_WAVE, Boolean.valueOf(value));}

    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;
    private int shakeTime = 0;
    Supplier<Projectile> ground_projectiles = () -> new ProjectileMagicGround(world, this, (float) 16, null);

    private boolean destroyCloseBlocks = false;

    public EntityEliteDraugr(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.75F, 3.95F);
        this.iAmBossMob = true;
        this.experienceValue = 65;
    }

    public EntityEliteDraugr(World worldIn) {
        super(worldIn);
        this.setSize(1.75F, 3.95F);
        this.iAmBossMob = true;
        this.experienceValue = 65;
    }

    private int longDistanceCooldown = 150;

    @Override
    public void onUpdate() {
        super.onUpdate();

        EntityLivingBase target = this.getAttackTarget();
        this.shakeTime--;
        if(!world.isRemote && target != null) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);

            bezerkActionCooldown--;
            playersNearbyAmount = ServerScaleUtil.getPlayers(this, world);

            if(distance >= 6 && !this.isFightMode()) {
                if(longDistanceCooldown > 1) {
                    longDistanceCooldown--;
                }
                if (longDistanceCooldown < 3) {
                    this.selectLongRangeAttack(target);
                }
            }
        }

        //This destroys blocks to ensure that any terrain and what not doesn't get in the way
        if(this.destroyCloseBlocks) {
            AxisAlignedBB box = getEntityBoundingBox().grow(1.25, 0.1, 1.25).offset(0, 0.1, 0);
            ModUtils.destroyBlocksInAABB(box, world, this);
        }
    }

    private int magicWaveCounter = 0;
    private void selectLongRangeAttack(EntityLivingBase target) {
        int randI = ModRand.range(1, 10);
        int cooldown_degradation = 20 * playersNearbyAmount;
        if(!this.isFightMode()) {
            if (randI < 8 && prevAttack != magic_wave && magicWaveCounter <= 2 || magicWaveCounter > 0 && randI <= 3 && prevAttack != magic_wave) {
                    prevAttack = magic_wave;
                    prevAttack.accept(target);
                    magicWaveCounter++;
                    longDistanceCooldown = 200 - cooldown_degradation;
            } else if (prevAttack != distance_closer) {
                prevAttack = distance_closer;
                prevAttack.accept(target);
                magicWaveCounter = 0;
                longDistanceCooldown = 200 - cooldown_degradation;
            } else {
                longDistanceCooldown = 100;
                prevAttack = null;
            }
        }
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Death_State", this.isDeathState());
        nbt.setBoolean("Swing", this.isSwingSimple());
        nbt.setBoolean("Double_Swing", this.isDoubleSwing());
        nbt.setBoolean("Stomp", this.isStomp());
        nbt.setBoolean("Distance_Closer", this.isDistanceCloser());
        nbt.setBoolean("Magic_Wave", this.isMagicWave());
        nbt.setBoolean("Bezerk", this.isBezerk());
        nbt.setBoolean("Ice_Wave", this.isIceWave());
        nbt.setBoolean("Shaking", this.isShaking());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setDeathState(nbt.getBoolean("Death_State"));
        this.setSwingSimple(nbt.getBoolean("Swing"));
        this.setDoubleSwing(nbt.getBoolean("Double_Swing"));
        this.setStomp(nbt.getBoolean("Stomp"));
        this.setDistanceCloser(nbt.getBoolean("Distance_Closer"));
        this.setMagicWave(nbt.getBoolean("Magic_Wave"));
        this.setBezerk(nbt.getBoolean("Bezerk"));
        this.setIceWave(nbt.getBoolean("Ice_Wave"));
        this.setShaking(nbt.getBoolean("Shaking"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(DEATH_STATE, Boolean.valueOf(false));
        this.dataManager.register(SWING, Boolean.valueOf(false));
        this.dataManager.register(DOUBLE_SWING, Boolean.valueOf(false));
        this.dataManager.register(STOMP, Boolean.valueOf(false));
        this.dataManager.register(DISTANCE_CLOSER, Boolean.valueOf(false));
        this.dataManager.register(MAGIC_WAVE, Boolean.valueOf(false));
        this.dataManager.register(BEZERK, Boolean.valueOf(false));
        this.dataManager.register(ICE_WAVE, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.champion_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.champion_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.champion_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.champion_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityDraugrEliteAttackAI<>(this, 1, 20, 6, 0F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double healthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && !this.isDeathState()) {
            List<Consumer<EntityLivingBase>> close_attacks = new ArrayList<>(Arrays.asList(swing_action, stomp_action, spike_arise, magic_wave, bezerk_action));
            double[] weights = {
                distance <= 6 && prevAttack != swing_action ? 1/distance : 0, //Swing/DoubleSwing
                distance <= 7 && prevAttack != stomp_action ? 1/distance : 0, //Stomp
                distance <= 7 && prevAttack != spike_arise ? 1/distance : 0, //Ice Spike Wave
                distance <= 7 && distance > 4 && prevAttack != magic_wave ? 1/distance : 0, //Magic Wave
                distance <= 6 && prevAttack != bezerk_action && bezerkActionCooldown < 3 && healthChange <= 0.5 ? 1/distance : 0 // Bezerk Action

            };
            prevAttack = ModRand.choice(close_attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        return 10;
    }

    private int bezerkActionCooldown = 400;

    private final Consumer<EntityLivingBase> bezerk_action = (target) -> {
        this.setFightMode(true);
        this.setBezerk(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);

        addEvent(() -> {
            this.setShaking(true);
            this.shakeTime = 70;
        }, 20);

        addEvent(()-> {
            this.setShaking(false);
        }, 55);
        addEvent(()-> this.playSound(SoundsHandler.DRAUGR_ELITE_WAR_CRY, 1.5f, 0.8f), 25);
        addEvent(()-> {
            this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 400, 0));
            List<EntityPlayer> nearbyEntities = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(6.0D), e -> !e.getIsInvulnerable());
            if(!nearbyEntities.isEmpty()) {
                for(EntityPlayer player : nearbyEntities) {
                    if(!player.isCreative() && !player.isSpectator()) {
                        player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 300, 0));
                    }
                }
            }
        }, 30);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                if(!this.isDeathState()) {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18), 0.1F);
                }
            }, 8);
        }, 62);

        addEvent(()-> {
            if(!this.isDeathState()) {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.3, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f));
            }
        }, 76);

        addEvent(()-> {
            this.lockLook = false;
        }, 80);

        addEvent(()-> {
            this.setImmovable(true);
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                if(!this.isDeathState()) {
                    this.lockLook = true;
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18), 0.1F);
                }
            }, 8);
        }, 86);

        addEvent(()-> {
            if(!this.isDeathState()) {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.3, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f));
            }
        }, 97);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(true);
        }, 110);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                if(!this.isDeathState()) {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18), 0.1F);
                }
            }, 8);
        }, 125);

        addEvent(()-> {
            if(!this.isDeathState()) {
                this.setImmovable(true);
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.3, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (this.getAttack());
                ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f));
                new ActionSplitMagicWave(ground_projectiles, 0.55F).performAction(this, target);
            }
        }, 140);

        addEvent(()-> this.lockLook = false, 160);

        addEvent(()-> {
            this.bezerkActionCooldown = 400;
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setBezerk(false);
        }, 170);
    };
    private final Consumer<EntityLivingBase> distance_closer = (target) -> {
        this.setDistanceCloser(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);

        addEvent(()-> this.lockLook = true, 25);
        addEvent(()-> {
            this.playSound(SoundsHandler.DRAUGR_ELITE_SWING_IMPACT, 1.5f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 27);
        addEvent(()-> new ActionQuickMagicWave(ground_projectiles, 0.65F).performAction(this, target), 33);
        addEvent(()-> this.lockLook = false, 50);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            addEvent(()-> {
                this.setImmovable(false);
                Vec3d adjusted = target.getPositionVector().add(posSet.scale(-6));
                int y = ModUtils.getSurfaceHeightLich(world, new BlockPos(adjusted.x, 0, adjusted.z), (int) target.posY - 3, (int) target.posY + 3);
                if(!this.isDeathState()) {
                if(y != 0) {
                    // ModUtils.attemptTeleport(new Vec3d(adjusted.x, y + 1, adjusted.z), this);
                    new ActionDraugrTeleport(new Vec3d(adjusted.x, y + 1, adjusted.z)).performAction(this, target);
                    this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
                    Vec3d targetPos = target.getPositionVector();
                    destroyCloseBlocks = true;
                    addEvent(()-> {
                        this.lockLook = true;
                        double distance = this.getPositionVector().distanceTo(targetPos);
                        ModUtils.leapTowards(this, targetPos, (float) (distance * 0.18),0.1F);
                        this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                    }, 10);
                } else {
                    //ModUtils.attemptTeleport(new Vec3d(adjusted.x, target.posY, adjusted.z), this);
                    new ActionDraugrTeleport(new Vec3d(adjusted.x, target.posY, adjusted.z)).performAction(this, target);
                    this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 0.8F / (rand.nextFloat() * 0.4F + 0.6F));
                    Vec3d targetPos = target.getPositionVector();
                    destroyCloseBlocks = true;
                    addEvent(() -> {
                        this.lockLook = true;
                        double distance = this.getPositionVector().distanceTo(targetPos);
                        ModUtils.leapTowards(this, targetPos, (float) (distance * 0.18), 0.1F);
                        this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                    }, 10);
                }
                }
            }, 10);
        }, 55);

        addEvent(()-> {
            for(int b = 0; b <= 10; b += 2) {
                addEvent(()-> {
                    if(!this.isDeathState()) {
                        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.3, 0)));
                        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                        float damage = (float) (this.getAttack());
                        ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                    }
                }, b);
            }
        }, 75);

        addEvent(()-> this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f)), 77);

        addEvent(()-> {this.setImmovable(true);
            this.destroyCloseBlocks = false;}, 95);
        addEvent(()-> this.lockLook = false, 100);
        addEvent(()-> {
        this.setImmovable(false);
        this.setDistanceCloser(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        }, 120);
    };
    private final Consumer<EntityLivingBase> magic_wave = (target) -> {
      this.setMagicWave(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);

      addEvent(()-> this.lockLook = true, 25);

      addEvent(()-> {
          this.playSound(SoundsHandler.DRAUGR_ELITE_SWING_IMPACT, 1.5f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f));
          this.setShaking(true);
          this.shakeTime = 20;
      }, 35);

      addEvent(()-> {
          this.setShaking(false);
      }, 55);
      addEvent(()-> {
          if(!this.isDeathState()) {
              new ActionMagicWave(ground_projectiles, 0.6F).performAction(this, target);
              this.setUpdateIcicles = true;
          }
          }, 40);

      addEvent(()-> {
          this.setUpdateIcicles = false;
      }, 50);

      addEvent(()-> this.lockLook = false, 60);
      addEvent(()-> {
        this.setMagicWave(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 70);
    };

    private final Consumer<EntityLivingBase> spike_arise = (target) -> {
      this.setIceWave(true);
      this.setFightMode(true);

      addEvent(()-> {
        this.lockLook = true;
        this.setImmovable(true);
      }, 40);

        addEvent(()-> {
            if(!this.isDeathState()) {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.3, 0)));
                DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f));
            }
        }, 47);

      addEvent(()-> new ActionIceWave().performAction(this, target), 50);

      addEvent(()-> {
          this.setImmovable(false);
          this.lockLook = false;
      }, 70);

      addEvent(()-> {
        this.setIceWave(false);
        this.setFightMode(false);
      }, 75);
    };

    private final Consumer<EntityLivingBase> stomp_action = (target) -> {
      this.setFightMode(true);
      this.setStomp(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);

      addEvent(()-> {
          this.setShaking(true);
          this.shakeTime = 15;
      }, 33);

      addEvent(()-> this.setShaking(false), 43);
      addEvent(()-> {
        //Do AOE Action
          if(!this.isDeathState()) {
              int distanceToo = (int) (this.getDistance(target));
              new ActionDraugrStomp(distanceToo + 3).performAction(this, target);
              this.playSound(SoundsHandler.DRAUGR_ELITE_STOMP, 2.0f, 0.75f / (rand.nextFloat() * 0.4F + 0.4f));
              this.setUpdateIcicles = true;
          }
      }, 35);

        addEvent(()-> {
            this.setUpdateIcicles = false;
        }, 45);

      addEvent(()-> {
        this.setFightMode(false);
        this.setStomp(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
      }, 75);
    };
    private final Consumer<EntityLivingBase> swing_action = (target) -> {
      this.setFightMode(true);
      int randI = ModRand.range(1, 10);
      double healthChange = this.getHealth() / this.getMaxHealth();
      if(randI >= 6) {
          //Double Swing
          this.setDoubleSwing(true);
          addEvent(()-> this.lockLook = true, 18);
          addEvent(()-> {
              if(!this.isDeathState()) {
                  Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.3, 0)));
                  DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
                  float damage = this.getAttack();
                  ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                  this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f));
                  if (healthChange <= 0.5) {
                      new ActionQuickSling(ground_projectiles, 0.55F).performAction(this, target);
                  }
              }
          }, 28);

          addEvent(()-> this.lockLook = false, 40);
          addEvent(()-> this.lockLook = true, 55);
          addEvent(()-> {
              if(!this.isDeathState()) {
                  Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.3, 0)));
                  DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
                  float damage = this.getAttack();
                  ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                  this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f));
                  if (healthChange <= 0.5) {
                      new ActionDraugrFullSling(ground_projectiles, 0.55F).performAction(this, target);
                  }
              }
          }, 65);

          addEvent(()-> this.lockLook = false, 80);
          addEvent(()-> {
            this.setFightMode(false);
            this.setDoubleSwing(false);
          }, 90);
      } else {
          //Single Swing
          this.setSwingSimple(true);
          addEvent(()-> this.lockLook = true, 18);
          addEvent(()-> {
              if(!this.isDeathState()) {
                  Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.3, 0)));
                  DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
                  float damage = this.getAttack();
                  ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                  this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f));
                  if (healthChange <= 0.5) {
                      new ActionQuickSling(ground_projectiles, 0.55F).performAction(this, target);
                  }
              }
          }, 28);

          addEvent(()-> this.lockLook = false, 40);
          addEvent(()-> {
            this.setSwingSimple(false);
            this.setFightMode(false);
          }, 60);
      }
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "model_adjustments_from", 0, this::predicateModelAdjustments));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "death_controller", 0, this::predicateDeathState));
    }

    private <E extends IAnimatable> PlayState predicateDeathState(AnimationEvent<E> event) {
        if(this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DEATH, false));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isDeathState()) {
            if(this.isSwingSimple()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isDoubleSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DOUBLE_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isStomp()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STOMP, false));
                return PlayState.CONTINUE;
            }
            if(this.isDistanceCloser()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DISTANCE_CLOSER, false));
                return PlayState.CONTINUE;
            }
            if(this.isMagicWave()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MAGIC_WAVE, false));
                return PlayState.CONTINUE;
            }
            if(this.isBezerk()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BEZERK, false));
                return PlayState.CONTINUE;
            }
            if(this.isIceWave()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ICE_WAVE, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }


    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage() && !this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateModelAdjustments(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode() && !this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage() && !this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode() && !this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.DRAUGR_ELITE_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.DRAUGR_ELITE_IDLE;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.DRAUGR_ELITE_STEP, 0.7F, 0.4f + ModRand.getFloat(0.3F));
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.00001F);
        this.setDeathState(true);
        this.setFullBodyUsage(true);
        this.lockLook = true;
        this.setImmovable(true);
        this.playSound(SoundsHandler.DRAUGR_ELITE_DEATH, 0.9f, 1.0f);
        addEvent(()-> {
            if(!world.isRemote) {
                for(int i = 0; i <= 45; i+=5) {
                    if(!world.isRemote) {
                        addEvent(() -> {
                            EntityXPOrb orb = new EntityXPOrb(world, this.posX, this.posY, this.posZ, 8);
                            orb.setPosition(this.posX, this.posY + 1, this.posZ);
                            world.spawnEntity(orb);
                        }, i);
                    }
                }
            }
        }, 50);

        addEvent(()-> {
            this.setDropItemsWhenDead(true);
            this.setDead();
        }, 95);
        super.onDeath(cause);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.isProjectile()) {
            return super.attackEntityFrom(source, amount * 0.5F);
        }

        if(ModConfig.boss_cap_damage_enabled && amount > MobConfig.champion_damage_cap) {
            return super.attackEntityFrom(source, MobConfig.champion_damage_cap);
        }

        return super.attackEntityFrom(source, amount);
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "draugr_elite");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 16.0F);
            if (dist >= 16.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.75F * screamMult);
        }
        return 0;
    }
}
