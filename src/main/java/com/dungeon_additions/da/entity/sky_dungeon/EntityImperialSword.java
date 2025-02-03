package com.dungeon_additions.da.entity.sky_dungeon;

import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.sky_dungeon.EntityImperialHalberdAI;
import com.dungeon_additions.da.entity.ai.sky_dungeon.EntityImperialSwordAI;
import com.dungeon_additions.da.entity.sky_dungeon.city_knights.ActionSwordSpecial;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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

public class EntityImperialSword extends EntitySkyBase implements IAnimatable, IAnimationTickable, IAttack {
    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;

    private final String ANIM_IDLE = "idle";
    private final String ANIM_IDLE_SHIELD = "idle_shield";
    private final String ANIM_IDLE_PLANTED = "idle_feet";

    private final String ANIM_WALK_LOWER = "walk";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_WALK_UPPER_SHIELD = "walk_upper_shield";

    private final String ANIM_PLANT_FEET = "plant_feet";
    private final String ANIM_REMOVE_PLANTED_FEET = "remove_planted";

    private final String ANIM_HIDE_BOW = "hide_bow";
    private final String ANIM_HIDE_SWORD = "hide_sword";

    private final String ANIM_SHOOT_ARROW = "shoot_arrow";

    private final String ANIM_STRIKE = "strike";
    private final String ANIM_STRIKE_COMBO = "strike_combo";
    private final String ANIM_SHIELD_PARRY = "shield_parry";
    private final String ANIM_UPPER_STRIKE = "upper_strike";
    private final String ANIM_CIRCLE_STRIKE = "circle_strike";
    private static final DataParameter<Boolean> STRIKE = EntityDataManager.createKey(EntityImperialSword.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STRIKE_COMBO = EntityDataManager.createKey(EntityImperialSword.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHIELDED = EntityDataManager.createKey(EntityImperialSword.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PLANTED = EntityDataManager.createKey(EntityImperialSword.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHIELD_PARRY = EntityDataManager.createKey(EntityImperialSword.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> UPPER_STRIKE = EntityDataManager.createKey(EntityImperialSword.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CIRCLE_STRIKE = EntityDataManager.createKey(EntityImperialSword.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHOOT_ARROW = EntityDataManager.createKey(EntityImperialSword.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BEGIN_PLANTED = EntityDataManager.createKey(EntityImperialSword.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_PLANTED = EntityDataManager.createKey(EntityImperialSword.class, DataSerializers.BOOLEAN);

    private boolean isStrike() {return this.dataManager.get(STRIKE);}
    private void setStrike(boolean value) {this.dataManager.set(STRIKE, Boolean.valueOf(value));}
    private boolean isStrikeCombo() {return this.dataManager.get(STRIKE_COMBO);}
    private void setStrikeCombo(boolean value) {this.dataManager.set(STRIKE_COMBO, Boolean.valueOf(value));}
    private boolean isShielded() {return this.dataManager.get(SHIELDED);}
    private void setShielded(boolean value) {this.dataManager.set(SHIELDED, Boolean.valueOf(value));}
    private boolean isPlanted() {return this.dataManager.get(PLANTED);}
    private void setPlanted(boolean value) {this.dataManager.set(PLANTED, Boolean.valueOf(value));}
    private boolean isShieldParry() {return this.dataManager.get(SHIELD_PARRY);}
    private void setShieldParry(boolean value) {this.dataManager.set(SHIELD_PARRY, Boolean.valueOf(value));}
    private boolean isUpperStrike() {return this.dataManager.get(UPPER_STRIKE);}
    private void setUpperStrike(boolean value) {this.dataManager.set(UPPER_STRIKE, Boolean.valueOf(value));}
    private boolean isCircleStrike() {return this.dataManager.get(CIRCLE_STRIKE);}
    private void setCircleStrike(boolean value) {this.dataManager.set(CIRCLE_STRIKE, Boolean.valueOf(value));}
    private boolean isShootArrow() {return this.dataManager.get(SHOOT_ARROW);}
    private void setShootArrow(boolean value) {this.dataManager.set(SHOOT_ARROW, Boolean.valueOf(value));}
    private boolean isBeginPlanted() {return this.dataManager.get(BEGIN_PLANTED);}
    private void setBeginPlanted(boolean value) {this.dataManager.set(BEGIN_PLANTED, Boolean.valueOf(value));}
    private boolean isEndPlanted() {return this.dataManager.get(END_PLANTED);}
    private void setEndPlanted(boolean value) {this.dataManager.set(END_PLANTED, Boolean.valueOf(value));}

    private int rangedCooldownTimer = 100;

    private int shield_switch_timer = 20;

    private int shootArrowCooldown = 0;
    private int storm_stomp_cooldown = 250;
    private int shield_parry_cooldown = 250;

    private boolean exhaustionFlag = false;
    public float exhaustionLevel = 0F;

    public EntityImperialSword(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 2.40F);
    }

    public EntityImperialSword(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 2.40F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Strike", this.isStrike());
        nbt.setBoolean("Strike_Combo", this.isStrikeCombo());
        nbt.setBoolean("Shielded", this.isShielded());
        nbt.setBoolean("Planted", this.isPlanted());
        nbt.setBoolean("Shield_Parry", this.isShieldParry());
        nbt.setBoolean("Upper_Strike", this.isUpperStrike());
        nbt.setBoolean("Circle_Strike", this.isCircleStrike());
        nbt.setBoolean("Shoot_Arrow", this.isShootArrow());
        nbt.setBoolean("Begin_Planted", this.isBeginPlanted());
        nbt.setBoolean("End_Planted", this.isEndPlanted());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setStrike(nbt.getBoolean("Strike"));
        this.setStrikeCombo(nbt.getBoolean("Strike_Combo"));
        this.setShielded(nbt.getBoolean("Shielded"));
        this.setPlanted(nbt.getBoolean("Planted"));
        this.setShieldParry(nbt.getBoolean("Shield_Parry"));
        this.setUpperStrike(nbt.getBoolean("Upper_Strike"));
        this.setCircleStrike(nbt.getBoolean("Circle_Strike"));
        this.setShootArrow(nbt.getBoolean("Shoot_Arrow"));
        this.setBeginPlanted(nbt.getBoolean("Begin_Planted"));
        this.setEndPlanted(nbt.getBoolean("End_Planted"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(STRIKE, Boolean.valueOf(false));
        this.dataManager.register(STRIKE_COMBO, Boolean.valueOf(false));
        this.dataManager.register(SHIELDED, Boolean.valueOf(false));
        this.dataManager.register(SHIELD_PARRY, Boolean.valueOf(false));
        this.dataManager.register(PLANTED, Boolean.valueOf(false));
        this.dataManager.register(UPPER_STRIKE, Boolean.valueOf(false));
        this.dataManager.register(CIRCLE_STRIKE, Boolean.valueOf(false));
        this.dataManager.register(SHOOT_ARROW, Boolean.valueOf(false));
        this.dataManager.register(BEGIN_PLANTED, Boolean.valueOf(false));
        this.dataManager.register(END_PLANTED, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(70);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityImperialSwordAI<>(this, 1, 30, 5, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        EntityLivingBase target = this.getAttackTarget();
        shield_switch_timer--;
        rangedCooldownTimer--;
        shootArrowCooldown--;
        shield_parry_cooldown--;
        storm_stomp_cooldown--;

        if(exhaustionFlag) {
            exhaustionLevel = 0.0F;
        }
        if(target != null && !world.isRemote) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            //go into ranged mode
            if (!this.isFightMode()) {

                if(this.isPlanted() && shootArrowCooldown < 0) {
                    shoot_arrow_action.accept(target);
                }

                if (distance >= 14 && !this.isPlanted() && rangedCooldownTimer < 0 && !this.isShielded()) {
                    this.beginRangedMode();
                    //end Planted
                } else if (distance < 14 && this.isPlanted() && !this.isEndPlanted() && !this.isShielded()) {
                    this.endRangedMode();
                    this.rangedCooldownTimer = 300;
                    //turn on shield
                } else if (distance <= 8 && shield_switch_timer < 0 && !this.isPlanted()) {
                    shield_switch_timer = 20;
                    this.setShielded(true);
                } else if (distance > 8 && this.isShielded() && shield_switch_timer < 0) {
                    shield_switch_timer = 20;
                    this.setShielded(false);
                }
            }
        }

        if (!world.isRemote && target == null) {
            if(this.isPlanted() && !this.isEndPlanted()) {
                endRangedMode();
                this.rangedCooldownTimer = 300;
            }

            if(this.isShielded()) {
                shield_switch_timer = 20;
                this.setShielded(false);
            }
        }
    }


    private void beginRangedMode() {
        this.setBeginPlanted(true);
        this.setPlanted(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.shootArrowCooldown = 60;
        addEvent(()-> {
        this.setFightMode(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setBeginPlanted(false);
        }, 35);
    }

    private void endRangedMode() {
        this.setEndPlanted(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);

        addEvent(()-> {
        this.setEndPlanted(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
            this.setPlanted(false);
        }, 30);
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);

        if(!this.isPlanted() && !this.isFightMode() && this.isShielded()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(strike, upper_strike, circle_attack));
            double[] weights = {
                    (prevAttacks != strike) ? 1/distance : 0,
                    (prevAttacks != upper_strike) ? 1/distance : 0,
                    (prevAttacks != circle_attack) ? 1/distance : 0
            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return exhaustionLevel > 1 ? 200 : 10;
    }

    private final Consumer<EntityLivingBase> circle_attack = (target) -> {
      this.setFightMode(true);
      this.setCircleStrike(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      if(exhaustionLevel <= 1) {
          exhaustionLevel += 0.1 + ModRand.getFloat(0.25F);
      } else {
          exhaustionFlag = true;
      }
      addEvent(()-> {
          Vec3d targetedPos = target.getPositionVector();
          this.lockLook = true;
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18),0.2F);
          }, 5);
      }, 20);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.1, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (float) (this.getAttack() * 1.25);
          ModUtils.handleAreaImpact(3.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
          this.playSound(SoundsHandler.IMPERIAL_START_MAGIC, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 35);

      addEvent(()-> new ActionSwordSpecial().performAction(this, target), 36);
      addEvent(()-> this.setImmovable(true), 40);
      addEvent(()-> this.lockLook = false, 60);

      addEvent(()-> {
        this.setFightMode(false);
        this.setCircleStrike(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
      }, 75);
    };

    private final Consumer<EntityLivingBase> upper_strike = (target) -> {
      this.setFightMode(true);
      this.setUpperStrike(true);
        if(exhaustionLevel <= 1) {
            exhaustionLevel += 0.1 + ModRand.getFloat(0.1F);
        } else {
            exhaustionFlag = true;
        }
      addEvent(()-> this.lockLook = true, 33);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.1, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.1f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 45);

      addEvent(()-> this.lockLook = false, 60);

      addEvent(()-> {
          this.setFightMode(false);
          this.setUpperStrike(false);
      }, 70);
    };

    private final Consumer<EntityLivingBase> strike = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        if(exhaustionLevel <= 1) {
            exhaustionLevel += 0.1 + ModRand.getFloat(0.2F);
        } else {
            exhaustionFlag = true;
        }
      if(storm_stomp_cooldown < 0) {
          this.setStrikeCombo(true);
          addEvent(()-> {
              Vec3d targetedPos = target.getPositionVector();
              addEvent(()-> {
                  this.lockLook = true;
                  this.setImmovable(false);
                  double distance = this.getPositionVector().distanceTo(targetedPos);
                  ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18),0.2F);
              }, 7);
          }, 18);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.1, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
              float damage = this.getAttack();
              ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.1f, 0, false);
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          }, 30);

          addEvent(()-> {
              this.setImmovable(true);
              this.lockLook = false;
          }, 40);

          addEvent(()-> {
              Vec3d targetedPos = target.getPositionVector();
              addEvent(()-> {
                  this.lockLook = true;
                  this.setImmovable(false);
                  double distance = this.getPositionVector().distanceTo(targetedPos);
                  ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.12),0.2F);
              }, 7);
          }, 43);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.1, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
              float damage = this.getAttack();
              ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.1f, 0, false);
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          }, 62);

          addEvent(()-> this.setImmovable(true), 70);

          addEvent(() -> {
              playSound(SoundsHandler.IMPERIAL_STORM_STOMP, 1, 0.7f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
              EntitySkyTornado tornado = new EntitySkyTornado(world, true);
              tornado.setPosition(this.posX, this.posY, this.posZ);
              world.spawnEntity(tornado);
          }, 100);

          addEvent(()-> this.lockLook = false, 115);

          addEvent(()-> {
              this.setStrikeCombo(false);
              this.setFightMode(false);
              this.setFullBodyUsage(false);
              this.setImmovable(false);
              this.storm_stomp_cooldown = 250;
          }, 125);
      } else {
          this.setStrike(true);
          addEvent(()-> {
              Vec3d targetedPos = target.getPositionVector();
              addEvent(()-> {
                  this.lockLook = true;
                  this.setImmovable(false);
                  double distance = this.getPositionVector().distanceTo(targetedPos);
                  ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18),0.2F);
              }, 7);
          }, 18);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.1, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
              float damage = this.getAttack();
              ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.1f, 0, false);
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          }, 30);

          addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
          }, 40);

          addEvent(()-> {
              Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.lockLook = true;
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.12),0.2F);
            }, 7);
          }, 43);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.1, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
              float damage = this.getAttack();
              ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.1f, 0, false);
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          }, 62);

          addEvent(()-> this.setImmovable(true), 70);

          addEvent(()-> this.lockLook = false, 80);

          addEvent(()-> {
            this.setStrike(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
          }, 90);
      }
    };


    private final Consumer<EntityLivingBase> shield_parry = (target) -> {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setShieldParry(true);
        this.setImmovable(true);
        if(exhaustionLevel <= 1) {
            exhaustionLevel += 0.1 + ModRand.getFloat(0.2F);
        } else {
            exhaustionFlag = true;
        }
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = 0F;
            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.1f, 0, false);
            this.playSound(SoundsHandler.IMPERIAL_COUNTER, 0.7f, 0.8f / (rand.nextFloat() * 0.4f + 0.8f));
        }, 5);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.12),0.2F);
            }, 5);
        }, 18);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 30);

        addEvent(()-> {
            this.setImmovable(false);
            this.lockLook = false;
        }, 50);

        addEvent(()-> {
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setShieldParry(false);
            this.shield_parry_cooldown = 200;
        }, 55);
    };

    private final Consumer<EntityLivingBase> shoot_arrow_action = (target) -> {
        this.setShootArrow(true);
        this.setFightMode(true);

        addEvent(()-> {
            EntitySkyArrow arrow =new EntitySkyArrow(world, this, target);
            double d0 = target.posX - posX;
            double d1 = (target.getEntityBoundingBox().minY + 1.1) - arrow.posY;
            double d2 = target.posZ - posZ;
            double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            arrow.shoot(d0, d1, d2, 1.45f, (float)(14 - world.getDifficulty().getId() * 4));
            arrow.setDamage(this.getAttack() * 0.3);
            playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1, 1f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
            addEvent(()-> playSound(SoundsHandler.IMPERIAL_SHOOT_ARROW, 2, 0.9f / (this.getRNG().nextFloat() * 0.4f + 0.8f)), 3);
            world.spawnEntity(arrow);
        }, 52);

        addEvent(()-> {
        this.setShootArrow(false);
        this.setFightMode(false);
            shootArrowCooldown = 85;
        }, 85);
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "state_side_controller", 0, this::predicateSideStates));
    //    data.addAnimationController(new AnimationController(this, "planted_state_controller", 0, this::predicatePlantedState));
    }

    private <E extends IAnimatable> PlayState predicateSideStates(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            if(this.isPlanted()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HIDE_SWORD, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HIDE_BOW, true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isBeginPlanted()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PLANT_FEET, false));
                return PlayState.CONTINUE;
            }
            if(this.isEndPlanted()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_REMOVE_PLANTED_FEET, false));
                return PlayState.CONTINUE;
            }
            if(this.isShootArrow()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT_ARROW, false));
                return PlayState.CONTINUE;
            }
            if(this.isShieldParry()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHIELD_PARRY, false));
                return PlayState.CONTINUE;
            }
            if(this.isStrike()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRIKE, false));
                return PlayState.CONTINUE;
            }
            if(this.isStrikeCombo()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRIKE_COMBO, false));
                return PlayState.CONTINUE;
            }
            if(this.isCircleStrike()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CIRCLE_STRIKE, false));
                return PlayState.CONTINUE;
            }
            if(this.isUpperStrike()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_UPPER_STRIKE, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicatePlantedState(AnimationEvent<E> event) {
        if(!this.isFightMode() && this.isPlanted()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_PLANTED, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode()) {
            if(this.isPlanted()) {
                System.out.println("Playing Idle Animation for Planted");
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_PLANTED, true));
                return PlayState.CONTINUE;
            }
            if(this.isShielded() && !this.isPlanted()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_SHIELD, true));
            }  else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage() && !this.isPlanted()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode() && !this.isPlanted()) {
            if (this.isShielded()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER_SHIELD, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
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
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(amount > 0.0F && this.canBlockDamageSource(source)) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }
            this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0f, 0.4f + ModRand.getFloat(0.2f));

            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if (!damageSourceIn.isUnblockable() && !this.isPlanted() && this.isShielded() && !this.isFightMode()) {
            Vec3d vec3d = damageSourceIn.getDamageLocation();
            //Handler for other
            //Quick Action Counter Attack
            EntityLivingBase target = this.getAttackTarget();
            if(shield_parry_cooldown < 0 && target != null && this.getDistanceSq(target) <= 5) {
                shield_parry.accept(target);
            }

            if (vec3d != null) {
                Vec3d vec3d1 = this.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

                return vec3d2.dotProduct(vec3d1) < 0.0D;
            }
        }

        return false;
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "imperial_sword");

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.IMPERIAL_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.IMPERIAL_HURT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.IMPERIAL_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.DRAUGR_STEP, 0.6F, 0.9f + ModRand.getFloat(0.3F));
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }
}
