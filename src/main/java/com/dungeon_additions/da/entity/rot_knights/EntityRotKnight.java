package com.dungeon_additions.da.entity.rot_knights;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.EntityAIAttackRotKnight;
import com.dungeon_additions.da.entity.ai.IAttack;
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
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
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
import software.bernie.geckolib3.core.IAnimatable;
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

public class EntityRotKnight extends EntityAbstractBase implements IAnimatable, IAttack {

    private final String ANIM_IDLE = "idle";
    private final String ANIM_IDLE_SHIELD = "idle_shield";
    private final String ANIM_WALK_LOWER = "walk";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_WALK_UPPER_SHIELD = "walk_upper_shield";

    //Attacks
    private final String ANIM_PIERCE_ATTACK = "pierce_attack";
    private final String ANIM_SWING_ATTACK = "swing_attack";
    private final String ANIM_SWING_COMBO = "swing_attack_combo";
    private final String ANIM_SHIELD_DASH = "shield_dash";

    private static final DataParameter<Boolean> PIERCE = EntityDataManager.createKey(EntityRotKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING = EntityDataManager.createKey(EntityRotKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_COMBO = EntityDataManager.createKey(EntityRotKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHIELDED = EntityDataManager.createKey(EntityRotKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHIELD_DASH = EntityDataManager.createKey(EntityRotKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IDLE_MODE = EntityDataManager.createKey(EntityRotKnight.class, DataSerializers.BOOLEAN);
    private boolean isPierce() {return this.dataManager.get(PIERCE);}
    private boolean isSwing() {return this.dataManager.get(SWING);}
    private boolean isSwingCombo() {return this.dataManager.get(SWING_COMBO);}
    public boolean isShielded() {return this.dataManager.get(SHIELDED);}
    private boolean isShieldDash() {return this.dataManager.get(SHIELD_DASH);}
    public boolean isIdleMode() {return this.dataManager.get(IDLE_MODE);}
    private void setPierce(boolean value) {this.dataManager.set(PIERCE, Boolean.valueOf(value));}
    private void setSwing(boolean value) {this.dataManager.set(SWING, Boolean.valueOf(value));}
    private void setSwingCombo(boolean value) {this.dataManager.set(SWING_COMBO, Boolean.valueOf(value));}
    public void setShielded(boolean value) {this.dataManager.set(SHIELDED, Boolean.valueOf(value));}
    private void setShieldDash(boolean value) {this.dataManager.set(SHIELD_DASH, Boolean.valueOf(value));}
    public void setIdleMode(boolean value) {this.dataManager.set(IDLE_MODE, Boolean.valueOf(value));}

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Pierce", this.isPierce());
        nbt.setBoolean("Swing", this.isSwing());
        nbt.setBoolean("Swing_Combo", this.isSwingCombo());
        nbt.setBoolean("Shielded", this.isShielded());
        nbt.setBoolean("Shield_Dash", this.isShieldDash());
        nbt.setBoolean("Idle_State", this.isIdleMode());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setPierce(nbt.getBoolean("Pierce"));
        this.setSwing(nbt.getBoolean("Swing"));
        this.setSwingCombo(nbt.getBoolean("Swing_Combo"));
        this.setShielded(nbt.getBoolean("Shielded"));
        this.setShieldDash(nbt.getBoolean("Shield_Dash"));
        this.setIdleMode(nbt.getBoolean("Idle_State"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(PIERCE, Boolean.valueOf(false));
        this.dataManager.register(SHIELDED, Boolean.valueOf(false));
        this.dataManager.register(SHIELD_DASH, Boolean.valueOf(false));
        this.dataManager.register(SWING, Boolean.valueOf(false));
        this.dataManager.register(SWING_COMBO, Boolean.valueOf(false));
        this.dataManager.register(IDLE_MODE, Boolean.valueOf(false));
    }
    private final AnimationFactory factory = new AnimationFactory(this);
    private boolean shieldLowered = false;
    //To avoid weird movements of the shield
    public int standbyTimer = 40;
    private Consumer<EntityLivingBase> prevAttack;
    public boolean isRandomGetAway = false;
    private boolean doingDash = false;

    public EntityRotKnight(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.75F, 1.95F);
        selectAnimationTooPlay();
        this.setIdleMode(true);
    }

    public EntityRotKnight(World worldIn) {
        super(worldIn);
        this.setSize(0.75F, 1.95F);
        selectAnimationTooPlay();
        this.setIdleMode(true);
    }

    protected int selection = ModRand.range(1, 5);
    private String ANIM_SELECTION_STRING;
    private final String ANIM_IDLE_1 = "idle_1";
    private final String ANIM_IDLE_2 = "idle_2";
    private final String ANIM_IDLE_3 = "idle_3";
    private final String ANIM_IDLE_4 = "idle_4";
    private float randomTurn = ModRand.range(1, 360);

    private boolean hasEndedState = false;
    public void selectAnimationTooPlay() {
        if(selection == 1) {
            ANIM_SELECTION_STRING = ANIM_IDLE_1;
        } else if (selection == 2) {
            ANIM_SELECTION_STRING = ANIM_IDLE_2;
        }else if(selection == 3) {
            ANIM_SELECTION_STRING = ANIM_IDLE_3;
        }else if(selection == 4) {
            ANIM_SELECTION_STRING = ANIM_IDLE_4;
        } else {
            ANIM_SELECTION_STRING = ANIM_IDLE_1;
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.rot_knights_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.rot_knights_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.rot_knights_armor);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    private int damageTime = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.isIdleMode() && !hasEndedState && !world.isRemote) {
            this.rotationYaw = randomTurn;
            this.rotationYawHead = randomTurn;
            this.setImmovable(true);
        }

        EntityLivingBase target = this.getAttackTarget();
        if(target != null && !world.isRemote) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);

            if(!hasEndedState && this.getDistanceSq(target) <= 24 && this.isIdleMode()) {
                this.endIdleState();
            }

            if(distance < 8) {
                this.setShielded(true);
                standbyTimer = 40;
            } else if(distance > 8 && standbyTimer < 0){
                this.setShielded(false);
            }

            if(this.isRandomGetAway) {
                double d0 = (this.posX - target.posX) * 0.009;
                double d1 = (this.posY - target.posY) * 0.005;
                double d2 = (this.posZ - target.posZ) * 0.009;
                this.addVelocity(d0, d1, d2);
                this.faceEntity(target, 35, 35);
                this.getLookHelper().setLookPositionWithEntity(target, 35, 35);
            }
            standbyTimer--;

            if(this.doingDash) {
                if(damageTime >= 7) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.2, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                    float damage = this.getAttack();
                    ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.9f, 0, false);
                    damageTime =0;
                }
                damageTime++;
            } else {
                damageTime = 0;
            }
        }
    }

    private void endIdleState() {
        this.hasEndedState = true;

        addEvent(()-> {
            this.setIdleMode(false);
            this.setImmovable(false);
        }, 10);

    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIAttackRotKnight<>(this, 1.1, 20, 9, 0F));
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
        if(!this.isFightMode() && !isRandomGetAway && !this.isIdleMode()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(swing_basic, swing_combo, pierce_attack, shieldDash, randomGetAway));
            double[] weights = {
                    (distance <= 5 && prevAttack != swing_basic) ? 1/distance : 0,
                    (distance <= 5 && prevAttack != swing_combo) ? 1/distance : 0,
                    (distance <= 3 && prevAttack != pierce_attack) ? 1.1/distance : 0,
                    (distance <= 10 && distance > 4) ? 1/distance : 0,
                    (distance <= 3 && prevAttack != randomGetAway) ? 1/distance : 0
            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        return prevAttack == shieldDash ? 60 : 30;
    }


    private final Consumer<EntityLivingBase> randomGetAway = (target) -> {
      this.isRandomGetAway = true;
      addEvent(()-> this.isRandomGetAway = false, 40);
    };

    private final Consumer<EntityLivingBase> shieldDash = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setShieldDash(true);
      this.setImmovable(true);

      addEvent(()-> {
          Main.proxy.spawnParticle(18, this.posX, this.posY, this.posZ, 0, 0, 0);
      }, 18);
      addEvent(()-> {
        Vec3d targetSaved = target.getPositionVector();
        this.lockLook = true;
        addEvent(()-> {
            this.setImmovable(false);
            double distance = this.getPositionVector().distanceTo(targetSaved);
            ModUtils.leapTowards(this, targetSaved, (float) (distance * 0.30),0.25F);
            this.doingDash = true;
        }, 5);
      }, 17);

      addEvent(()-> {
        this.setImmovable(true);
        this.doingDash = false;
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.8f, 0, false);
          this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 36);

      addEvent(()-> this.shieldLowered = true, 36);
      addEvent(()-> {
          this.setImmovable(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setShieldDash(false);
        this.shieldLowered = false;
        this.lockLook = false;
      }, 50);
    };

    private final Consumer<EntityLivingBase> swing_basic = (target) -> {
      this.setSwing(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);

      addEvent(()-> {
          Vec3d targetSaved = target.getPositionVector();
          this.lockLook = true;
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetSaved);
              ModUtils.leapTowards(this, targetSaved, (float) (distance * 0.18),0.25F);
          }, 10);
      }, 5);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 20);

      addEvent(()-> this.setImmovable(true), 25);

      addEvent(()-> this.lockLook = false, 36);
      addEvent(()-> this.shieldLowered = true, 10);
      addEvent(()-> this.shieldLowered = false, 40);
      addEvent(()-> {
          this.setImmovable(false);
        this.setSwing(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
      }, 44);
    };

    private final Consumer<EntityLivingBase> swing_combo = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setSwingCombo(true);
        this.setImmovable(true);

        addEvent(()-> {
            Vec3d targetSaved = target.getPositionVector();
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetSaved);
                ModUtils.leapTowards(this, targetSaved, (float) (distance * 0.18),0.25F);
            }, 10);
        }, 5);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            this.lockLook = false;
        }, 20);

        addEvent(()-> this.setImmovable(true), 25);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetSaved = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetSaved);
                ModUtils.leapTowards(this, targetSaved, (float) (distance * 0.18),0.25F);
            }, 10);
        }, 30);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.6, 1.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 46);

        addEvent(()-> this.setImmovable(true), 50);

      addEvent(()-> this.shieldLowered = true, 10);
      addEvent(()-> {
          this.setImmovable(false);
          this.lockLook = false;
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setSwingCombo(false);
        this.shieldLowered = false;
      }, 70);
    };

    private final Consumer<EntityLivingBase> pierce_attack = (target) -> {
      this.setFightMode(true);
      this.setPierce(true);

    addEvent(()-> this.lockLook = true, 10);

    addEvent(()-> {
        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 1.5, 0)));
        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
        float damage = this.getAttack();
        ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
        this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
    }, 18);

      addEvent(()-> this.shieldLowered = true, 20);

      addEvent(()-> this.lockLook = false, 28);

      addEvent(()-> {
        this.setFightMode(false);
        this.setPierce(false);
        this.shieldLowered = false;
      }, 36);
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "statue_controller", 0, this::predicateIdleStatues));
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if (!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode() && !this.isIdleMode()) {
            if(this.isShielded()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER_SHIELD, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
            }
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdleStatues(AnimationEvent<E> event) {
        if(this.isIdleMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SELECTION_STRING, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage() && !this.isIdleMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {

        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode() && !this.isIdleMode()) {
            if(this.isShielded()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_SHIELD, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            }

            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isIdleMode()) {
            if(this.isPierce()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PIERCE_ATTACK, false));
            }
            if(this.isSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_ATTACK, false));
            }
            if(this.isSwingCombo()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_COMBO, false));
            }
            if(this.isShieldDash()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHIELD_DASH, false));
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if(source.getImmediateSource() instanceof EntityRotKnight || source.getImmediateSource() instanceof EntityRotKnightRapier || this.isIdleMode()) {
            return false;
        }

        if(amount > 0.0F && this.canBlockDamageSource(source)) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }
            this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0f, 0.6f + ModRand.getFloat(0.2f));

            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if (!damageSourceIn.isUnblockable() && !this.shieldLowered && this.isShielded()) {
            Vec3d vec3d = damageSourceIn.getDamageLocation();
            //Handler for other
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
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.ROT_KNIGHT_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.ROT_KNIGHT_WALK, 0.4F, 0.4f + ModRand.getFloat(0.3F));
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ROT_KNIGHT_DEATH;
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "rot_knight");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
