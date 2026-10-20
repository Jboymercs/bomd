package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityAIAttackBasic;
import com.dungeon_additions.da.entity.dark_dungeon.boss.action.ActionHorizontalSlash;
import com.dungeon_additions.da.entity.dark_dungeon.boss.action.ActionVerticalSlash;
import com.dungeon_additions.da.entity.dark_dungeon.minion_action.ActionCastRedProjectiles;
import com.dungeon_additions.da.entity.dark_dungeon.minion_action.ActionWaveLightningGround;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
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

public class EntityEverchosen extends EntityDarkBase implements IAnimatable, IAnimationTickable, IAttack {
    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;
    private boolean hasShieldLowered = false;
    private int comboAmount = 0;

    private final String ANIM_WALK_UPPER = "walk_sword_upper";
    private final String ANIM_WALK_LOWER = "walk_sword_lower";
    private final String ANIM_IDLE_UPPER = "idle_sword_upper";

    //Guarded
    private final String ANIM_WALK_UPPER_GUARDED = "walk_sword_upper_guarded";
    private final String ANIM_IDLE_UPPER_GUARDED = "idle_sword_upper_guarded";

    //Attacks
    private final String ANIM_SWING_SWORD = "sword_swing";
    private final String ANIM_SWING_SWORD_TWO = "sword_swing_two";
    private final String ANIM_SWING_SWORD_ULTIMATE = "sword_swing_ultimate";
    private final String ANIM_SWORD_SLASH = "sword_slash";
    private final String ANIM_THRUST_SWORD = "thrust_sword";
    private final String ANIM_CAST_PROJECTILES = "cast_projectiles";
    private final String ANIM_PARRY = "parry";

    private static final DataParameter<Boolean> GUARDED = EntityDataManager.createKey(EntityEverchosen.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PERFORMING_COMBO = EntityDataManager.createKey(EntityEverchosen.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_ONE = EntityDataManager.createKey(EntityEverchosen.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_TWO = EntityDataManager.createKey(EntityEverchosen.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_ULTIMATE = EntityDataManager.createKey(EntityEverchosen.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_SLASH = EntityDataManager.createKey(EntityEverchosen.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PARRY = EntityDataManager.createKey(EntityEverchosen.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> THRUST_SWORD = EntityDataManager.createKey(EntityEverchosen.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_PROJECTILES = EntityDataManager.createKey(EntityEverchosen.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DISABLED_SHIELD = EntityDataManager.createKey(EntityEverchosen.class, DataSerializers.BOOLEAN);

    public boolean isPerformingCombo() {
        return this.dataManager.get(PERFORMING_COMBO);
    }
    private void setPerformingCombo(boolean value) {
        this.dataManager.set(PERFORMING_COMBO, Boolean.valueOf(value));
    }
    public boolean isGuarded() {
        return this.dataManager.get(GUARDED);
    }
    private void setGuarded(boolean value) {
        this.dataManager.set(GUARDED, Boolean.valueOf(value));
    }
    public boolean isDisabledShield() {
        return this.dataManager.get(DISABLED_SHIELD);
    }
    private void setDisabledShield(boolean value) {
        this.dataManager.set(DISABLED_SHIELD, Boolean.valueOf(value));
    }
    public boolean isSwingOne() {
        return this.dataManager.get(SWING_ONE);
    }
    private void setSwingOne(boolean value) {
        this.dataManager.set(SWING_ONE, Boolean.valueOf(value));
    }
    public boolean isSwingTwo() {
        return this.dataManager.get(SWING_TWO);
    }
    private void setSwingTwo(boolean value) {
        this.dataManager.set(SWING_TWO, Boolean.valueOf(value));
    }
    public boolean isSwingUltimate() {
        return this.dataManager.get(SWING_ULTIMATE);
    }
    private void setSwingUltimate(boolean value) {
        this.dataManager.set(SWING_ULTIMATE, Boolean.valueOf(value));
    }
    public boolean isSwingSlash() {
        return this.dataManager.get(SWING_SLASH);
    }
    private void setSwingSlash(boolean value) {
        this.dataManager.set(SWING_SLASH, Boolean.valueOf(value));
    }
    public boolean isParryMove() {
        return this.dataManager.get(PARRY);
    }
    private void setParryMove(boolean value) {
        this.dataManager.set(PARRY, Boolean.valueOf(value));
    }
    public boolean isThrustSword() {
        return this.dataManager.get(THRUST_SWORD);
    }
    private void setThrustSword(boolean value) {
        this.dataManager.set(THRUST_SWORD, Boolean.valueOf(value));
    }
    public boolean isCastProjectiles() {
        return this.dataManager.get(CAST_PROJECTILES);
    }
    private void setCastProjectiles(boolean value) {
        this.dataManager.set(CAST_PROJECTILES, Boolean.valueOf(value));
    }

    public EntityEverchosen(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 35;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(0.7F, 2.2F);
        this.falter_resistance = 1.9F;
        this.hemorrhage_resistance = 0.9F;
    }

    public EntityEverchosen(World worldIn) {
        super(worldIn);
        this.experienceValue = 35;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(0.7F, 2.2F);
        this.falter_resistance = 1.9F;
        this.hemorrhage_resistance = 0.9F;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Guarded", this.isGuarded());
        nbt.setBoolean("Swing_One", this.isSwingOne());
        nbt.setBoolean("Swing_Two", this.isSwingTwo());
        nbt.setBoolean("Swing_Ultimate", this.isSwingUltimate());
        nbt.setBoolean("Swing_Slash", this.isSwingSlash());
        nbt.setBoolean("Parry", this.isParryMove());
        nbt.setBoolean("Disabled_Shield", this.isDisabledShield());
        nbt.setBoolean("Cast_Projectiles", this.isCastProjectiles());
        nbt.setBoolean("Thrust_Sword", this.isThrustSword());
        nbt.setBoolean("Performing_Combo", this.isPerformingCombo());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSwingOne(nbt.getBoolean("Swing_One"));
        this.setSwingTwo(nbt.getBoolean("Swing_Two"));
        this.setSwingUltimate(nbt.getBoolean("Swing_Ultimate"));
        this.setSwingSlash(nbt.getBoolean("Swing_Slash"));
        this.setParryMove(nbt.getBoolean("Parry"));
        this.setGuarded(nbt.getBoolean("Guarded"));
        this.setThrustSword(nbt.getBoolean("Thrust_Sword"));
        this.setCastProjectiles(nbt.getBoolean("Cast_Projectile"));
        this.setDisabledShield(nbt.getBoolean("Disabled_Shield"));
        this.setPerformingCombo(nbt.getBoolean("Performing_Combo"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(PERFORMING_COMBO, Boolean.valueOf(false));
        this.dataManager.register(GUARDED, Boolean.valueOf(false));
        this.dataManager.register(SWING_ONE, Boolean.valueOf(false));
        this.dataManager.register(SWING_TWO, Boolean.valueOf(false));
        this.dataManager.register(SWING_ULTIMATE, Boolean.valueOf(false));
        this.dataManager.register(SWING_SLASH, Boolean.valueOf(false));
        this.dataManager.register(DISABLED_SHIELD, Boolean.valueOf(false));
        this.dataManager.register(CAST_PROJECTILES, Boolean.valueOf(false));
        this.dataManager.register(THRUST_SWORD, Boolean.valueOf(false));
        this.dataManager.register(PARRY, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(30);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.31D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(6);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIAttackBasic<>(this, 1.0D, 40, 8, 0.22F, 20));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    private int shieldDisableTime = 180;
    private boolean isRandomGetAway = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!world.isRemote) {

            if (this.isDisabledShield()) {
                shieldDisableTime--;
                if (shieldDisableTime < 0 && !this.isFightMode()) {
                    this.setDisabledShield(false);
                }
            }

            EntityLivingBase target = this.getAttackTarget();

            if(target != null) {
                if(this.getDistance(target) < 10) {
                    this.setGuarded(true);
                } else {
                    this.setGuarded(false);
                }


                if(this.isRandomGetAway && this.getDistance(target) < 10) {
                    double d0 = (this.posX - target.posX) * 0.009;
                    double d1 = (this.posY - target.posY) * 0.005;
                    double d2 = (this.posZ - target.posZ) * 0.009;
                    this.addVelocity(d0, d1, d2);
                    this.faceEntity(target, 35, 35);
                    this.getLookHelper().setLookPositionWithEntity(target, 35, 35);
                }
            }
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        if(!this.isFightMode()) {
            double distance = Math.sqrt(distanceSq);
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(swing_sword, swing_sword_two, sword_slash, thrust_sword, cast_projectiles));
            double[] weights = {
                    (prevAttacks != swing_sword) ? 1/distance : 0, //Swing Sword
                    (prevAttacks != swing_sword_two) ? 1/distance : 0, //Swing Sword Two
                    (prevAttacks != sword_slash) ? distance * 0.02 : 0, //Sword Slash
                    (prevAttacks != thrust_sword) ? distance * 0.02 : 0, //Thrust Sword
                    (prevAttacks != cast_projectiles) ? distance * 0.02 : 0//
            };
            this.setPerformingCombo(false);
            this.comboAmount = 0;
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return (int) (50 + (Math.sqrt(distanceSq) * 8));
    }

    private final Consumer<EntityLivingBase> cast_projectiles = (target) -> {
      this.setCastProjectiles(true);
      this.setFightMode(true);
      this.hasShieldLowered = true;
      this.isRandomGetAway = true;

      addEvent(()-> {
          this.lockLook = true;
      }, 14);
      addEvent(()-> {
        //Start creating projectiles
          new ActionCastRedProjectiles().performAction(this, target);
      }, 17);

      addEvent(()-> {
          this.lockLook = false;
      }, 25);

      addEvent(()-> {
          this.isRandomGetAway = false;
          this.hasShieldLowered = false;
        this.setCastProjectiles(false);
        this.setFightMode(false);
      }, 35);
    };

    Supplier<Projectile> ground_projectiles = () -> new ProjectileRedLightningGround(world, this, (float) this.getAttack() * 0.75F, null);

    private final Consumer<EntityLivingBase> thrust_sword = (target) -> {
      this.setThrustSword(true);
      this.setImmovable(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.hasShieldLowered = true;

      addEvent(()-> this.lockLook = true, 10);
      addEvent(()-> new ActionWaveLightningGround(ground_projectiles, 0.4F, 16, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0, 0))),false).performAction(this, target), 18);

      addEvent(()-> new ActionWaveLightningGround(ground_projectiles, 0.4F, 16, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0, 0))),true).performAction(this, target), 34);

      addEvent(()-> this.lockLook = false, 37);
      addEvent(()-> {
          this.hasShieldLowered = false;
        this.setThrustSword(false);
        this.setImmovable(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
      }, 45);
    };

    private final Consumer<EntityLivingBase> sword_slash = (target) -> {
      this.setFightMode(true);
      this.setSwingSlash(true);

      addEvent(()-> {
        this.lockLook = true;
        this.setImmovable(true);
      }, 17);

      addEvent(()-> this.playSound(SoundsHandler.ROH_SWING_SWORD_HEAVY, 2.0f, 0.7f / (rand.nextFloat() * 0.4F + 0.4f)), 21);

      addEvent(()-> {
          //Sword Slash
          new ActionVerticalSlash(this.getAttack(), 16, 1.5F).performAction(this, target);
      }, 25);

      addEvent(()-> {
          this.lockLook = false;
          this.setImmovable(false);
      }, 30);

      addEvent(()-> {
            this.setFightMode(false);
            this.setSwingSlash(false);
      }, 40);
    };

    private final Consumer<EntityLivingBase> swing_sword_two = (target) -> {
      this.setFightMode(true);
      this.setSwingTwo(true);
      this.hasShieldLowered = true;

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(2));
            this.lockLook = true;
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 3);
        }, 11);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.4, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.7f, 0, false, 0.4F);
            this.playSound(SoundsHandler.ROH_SWING_SWORD, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 19);

        addEvent(()-> {
            this.lockLook = false;
        }, 27);

      addEvent(()-> {
          if(ModRand.percentageOf(60 - (comboAmount * 10))) {
              this.selectComboFollowUp(target);
              this.setSwingTwo(false);
          } else {
              addEvent(()-> {
                  this.hasShieldLowered = false;
                  this.setFightMode(false);
                  this.setSwingTwo(false);
              }, 5);
          }
      }, 30);
    };

    private final Consumer<EntityLivingBase> swing_sword = (target) -> {
      this.setFightMode(true);
      this.setSwingOne(true);
      this.hasShieldLowered = true;

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(2));
            this.lockLook = true;
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 3);
        }, 11);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.4, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.7f, 0, false, 0.4F);
            this.playSound(SoundsHandler.ROH_SWING_SWORD, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 19);

        addEvent(()-> {
            this.lockLook = false;
        }, 27);

      addEvent(()-> {
          if(ModRand.percentageOf(60 - (comboAmount * 10))) {
              this.selectComboFollowUp(target);
              this.setSwingOne(false);
          } else {
              addEvent(()-> {
                  this.hasShieldLowered = false;
                  this.setFightMode(false);
                  this.setSwingOne(false);
              }, 5);
          }
      }, 30);
    };

    private final Consumer<EntityLivingBase> sword_swing_ultimate = (target) -> {
      this.setSwingUltimate(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
      this.hasShieldLowered = true;

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(3));
            this.lockLook = true;
            this.setImmovable(false);
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 5);
        }, 15);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.7f, 0, false, 0.9F);
            this.playSound(SoundsHandler.ROH_SWING_SWORD_HEAVY, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
            new ActionHorizontalSlash(this.getAttack() * 0.8F, 8).performAction(this, target);
        }, 25);

        addEvent(()-> {
            this.setImmovable(true);
            addEvent(()-> this.lockLook = false, 5);
        }, 30);

      addEvent(()-> {
          this.hasShieldLowered = false;
            this.setSwingUltimate(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setImmovable(false);
      }, 50);
    };

    private void selectComboFollowUp(EntityLivingBase target) {
        float distance = this.getDistance(target);
        this.setPerformingCombo(true);
        this.comboAmount++;
        if(target instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) target);
            if(distance < 10) {
                if(player.getActiveItemStack().getItem() instanceof ItemShield) {
                    sword_swing_ultimate.accept(target);
                    return;
                }
                if(player.getActiveItemStack().getItem() instanceof ItemBow || player.getActiveItemStack().getItem() instanceof ItemPotion) {
                    sword_slash.accept(target);
                    return;
                }
            }
        }

        if(distance < 12) {
            if(distance < 6 && comboAmount <= 1) {
                if(prevAttacks != swing_sword) {
                    swing_sword.accept(target);
                    return;
                } else if (prevAttacks != swing_sword_two) {
                    swing_sword_two.accept(target);
                    return;
                }
            }

                sword_swing_ultimate.accept(target);
        } else {
            sword_swing_ultimate.accept(target);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "fight_controller", 3, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSwingOne()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_SWORD + (this.isPerformingCombo() ? "t" : "")));
            }
            if(this.isSwingTwo()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_SWORD_TWO + (this.isPerformingCombo() ? "t" : "")));
            }
            if(this.isSwingUltimate()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_SWORD_ULTIMATE + (this.isPerformingCombo() ? "t" : "")));
            }
            if(this.isSwingSlash()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWORD_SLASH + (this.isPerformingCombo() ? "t" : "")));
            }
            if(this.isParryMove()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PARRY));
            }
            if(this.isThrustSword()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_THRUST_SWORD));
            }
            if(this.isCastProjectiles()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CAST_PROJECTILES));
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode()) {
            if(this.isGuarded() && !this.isDisabledShield()) {
                event.getController().setAnimation(new AnimationBuilder().loop(ANIM_IDLE_UPPER_GUARDED));
            } else {
                event.getController().setAnimation(new AnimationBuilder().loop(ANIM_IDLE_UPPER));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().loop(ANIM_WALK_LOWER));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode()) {
            if(this.isGuarded() && !this.isDisabledShield()) {
                event.getController().setAnimation(new AnimationBuilder().loop(ANIM_WALK_UPPER_GUARDED));
            } else {
                event.getController().setAnimation(new AnimationBuilder().loop(ANIM_WALK_UPPER));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if (amount > 0.0F && this.canBlockDamageSource(source)) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase && !this.hasShieldLowered && !this.isDisabledShield()) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }
            this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0f, 0.8f + ModRand.getFloat(0.2f));

            return false;
        }
        return super.attackEntityFrom(source, amount);
    }


    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if (!damageSourceIn.isUnblockable() && this.isGuarded() && !this.hasShieldLowered && !this.isDisabledShield()) {
            Vec3d vec3d = damageSourceIn.getDamageLocation();
            //Handler for Parrying specifically
            if(damageSourceIn.getImmediateSource() instanceof EntityPlayer) {
                Entity sourceAt = damageSourceIn.getImmediateSource();
                if(sourceAt != null && !damageSourceIn.isProjectile()) {
                    ItemStack stack =  ((EntityPlayer) sourceAt).inventory.getCurrentItem();
                    if(stack.getItem() instanceof ItemAxe || stack.getItem().canDisableShield(stack, null, this, ((EntityLivingBase) sourceAt))) {
                        this.setDisabledShield(true);
                        this.shieldDisableTime = 140;
                        this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 1.0f, 0.8f + ModRand.getFloat(0.2f));
                    }
                }
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
}
