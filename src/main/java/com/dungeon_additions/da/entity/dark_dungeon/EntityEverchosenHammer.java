package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityAIAttackBasic;
import com.dungeon_additions.da.entity.dark_dungeon.minion_action.ActionCastDeathProjectile;
import com.dungeon_additions.da.entity.dark_dungeon.minion_action.ActionRedLightningGround;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
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
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemShield;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityEverchosenHammer extends EntityDarkBase implements IAttack, IScreenShake {
    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);
    private final MoveAnalysis moveAnalysis;
    private Consumer<EntityLivingBase> prevAttacks;
    private final String ANIM_WALK_UPPER = "walk_hammer_upper";
    private final String ANIM_WALK_LOWER = "walk_hammer_lower";
    private final String ANIM_IDLE_UPPER = "idle_hammer_upper";

    private final String ANIM_SWING_HAMMER = "hammer_swing";
    private final String ANIM_HAMMER_JUMP = "hammer_jump";
    private final String ANIM_HAMMER_JUMP_LOOP = "hammer_jump_continue";
    private final String ANIM_HAMMER_JUMP_END = "hammer_jump_end";
    private final String ANIM_CAST_PROJECTILE = "cast_projectile";
    private final String ANIM_SWING_HAMMER_TWO = "hammer_swing_two";
    private final String ANIM_HEAVY_SWING = "heavy_swing";
    private final String ANIM_OVERHEAD_SWING = "overhead_swing";
    private int hammer_jump_amount = 0;

    private static final DataParameter<Boolean> HAMMER_LOOP = EntityDataManager.createKey(EntityEverchosenHammer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityEverchosenHammer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_PROJECTILE = EntityDataManager.createKey(EntityEverchosenHammer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PERFORMING_COMBO = EntityDataManager.createKey(EntityEverchosenHammer.class, DataSerializers.BOOLEAN);

    public boolean isPerformingCombo() {
        return this.dataManager.get(PERFORMING_COMBO);
    }
    private void setPerformingCombo(boolean value) {
        this.dataManager.set(PERFORMING_COMBO, Boolean.valueOf(value));
    }
    public boolean isCastProjectile() {
        return this.dataManager.get(CAST_PROJECTILE);
    }
    private void setCastProjectile(boolean value) {
        this.dataManager.set(CAST_PROJECTILE, Boolean.valueOf(value));
    }
    public boolean isHammerLoop() {
        return this.dataManager.get(HAMMER_LOOP);
    }
    private void setHammerLoop(boolean value) {
        this.dataManager.set(HAMMER_LOOP, Boolean.valueOf(value));
    }
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}

    private int shakeTime = 0;
    private int comboAmount = 0;

    public EntityEverchosenHammer(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 35;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(0.7F, 2.2F);
        this.falter_resistance = 1.9F;
        this.hemorrhage_resistance = 0.9F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public EntityEverchosenHammer(World worldIn) {
        super(worldIn);
        this.experienceValue = 35;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(0.7F, 2.2F);
        this.falter_resistance = 1.9F;
        this.hemorrhage_resistance = 0.9F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Hammer_Loop", this.isHammerLoop());
        nbt.setBoolean("Shaking", this.isShaking());
        nbt.setBoolean("Cast_Projectile", this.isCastProjectile());
        nbt.setBoolean("Performing_Combo", this.isPerformingCombo());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setHammerLoop(nbt.getBoolean("Hammer_Loop"));
        this.setShaking(nbt.getBoolean("Shaking"));
        this.setCastProjectile(nbt.getBoolean("Cast_Projectile"));
        this.setPerformingCombo(nbt.getBoolean("Performing_Combo"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(HAMMER_LOOP, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        this.dataManager.register(CAST_PROJECTILE, Boolean.valueOf(false));
        this.dataManager.register(PERFORMING_COMBO, Boolean.valueOf(false));
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(30);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.31D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(70);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(6);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIAttackBasic<>(this, 1.0D, 40, 10, 0.25F, 12));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.shakeTime--;

        EntityLivingBase target = this.getAttackTarget();

        if(target != null) {
            if(this.getDistance(target) > 11 && !this.isFightMode()) {
                if(this.ticksExisted % 200 == 0) {
                    cast_projectile.accept(target);
                }
            }
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        if(!this.isFightMode()) {
            double distance = Math.sqrt(distanceSq);
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(swing_hammer, hammer_jump_start, swing_hammer_two, overhead_swing));
            double[] weights = {
                prevAttacks != swing_hammer ? 1/distance  : 0.0001, //Swing Hammer
                prevAttacks != hammer_jump_start && prevAttacks != hammer_jump_loop ? distance * 0.02 : 0, //Hammer Jump Loop
                prevAttacks != swing_hammer_two ? 1/distance : 0.0001, //Swing Hammer two
                prevAttacks != overhead_swing ? distance * 0.02 : 0 //Overhead Swing
            };
            this.setPerformingCombo(false);
            this.comboAmount = 0;
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return 50;
    }

    private final Consumer<EntityLivingBase> hammer_jump_start = (target) -> {
      animator.performAttack(ANIM_HAMMER_JUMP);
      this.setImmovable(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);

      addEvent(()-> {
            this.setImmovable(false);
            this.lockLook = true;
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.7, 0, 0)));
          Main.proxy.spawnParticle(18,world, relPos.x, this.posY + 0.08, relPos.z, 0, 0, 0);
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(0));
          addEvent(()-> {
              this.motionY = 0;
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18),0.1F);
              this.playSound(SoundsHandler.APATHYR_SLIGHT_DASH, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
              this.motionY += 0.57;
          }, 1);
      }, 20);

      addEvent(()-> {
          this.setImmovable(true);
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 0.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (this.getAttack());
          ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.6F);
          this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.7, 0, 0)));
          Main.proxy.spawnParticle(18,world, relPos.x, this.posY + 0.08, relPos.z, 0, 0, 0);
          this.setShaking(true);
          this.shakeTime = 10;
          addEvent(()-> this.setShaking(false), 10);
      }, 43);

      addEvent(()-> {
          //continue the jumping!
          if(this.getDistance(target) > 3 && ModRand.percentageOf(80 - (hammer_jump_amount * 10))) {
            this.setHammerJumpContinue(target);
          } else {
              this.lockLook = false;
              animator.performAttack(ANIM_HAMMER_JUMP_END);
              addEvent(()-> {
                  this.setFightMode(false);
                  this.setFullBodyUsage(false);
                  this.setImmovable(false);
                  this.motionY = 0;
                  this.hammer_jump_amount = 0;
              }, 20);
          }
      }, 50);
    };

    private void setHammerJumpContinue(EntityLivingBase target) {
        hammer_jump_loop.accept(target);
    }

    private final Consumer<EntityLivingBase> hammer_jump_loop = (target) -> {
      this.setFightMode(true);
      this.setHammerLoop(true);
      this.hammer_jump_amount++;

      addEvent(()-> {
          this.setImmovable(false);
          this.lockLook = false;
          this.motionY = 0;
          double distance = this.getPositionVector().distanceTo(target.getPositionVector());
          ModUtils.leapTowards(this, target.getPositionVector(), (float) (distance * 0.18),0.1F);
          this.playSound(SoundsHandler.APATHYR_SLIGHT_DASH, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
          this.motionY += 0.57;
      }, 5);

      addEvent(()-> {
          this.lockLook = true;
      }, 24);

      addEvent(()-> {
          this.setImmovable(true);
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 0.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (this.getAttack());
          ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.3f, 0, false, 0.6F);
          this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.7, 0, 0)));
          Main.proxy.spawnParticle(18,world, relPos.x, this.posY + 0.08, relPos.z, 0, 0, 0);
          this.setShaking(true);
          this.shakeTime = 10;
          addEvent(()-> this.setShaking(false), 10);
      }, 28);

      addEvent(()-> {
            this.setHammerLoop(false);
          if(this.getDistance(target) > 3 && ModRand.percentageOf(80 - (hammer_jump_amount * 10))) {
              this.setHammerJumpContinue(target);
          } else {
              this.lockLook = false;
              animator.performAttack(ANIM_HAMMER_JUMP_END);
              addEvent(()-> {
                  this.setFightMode(false);
                  this.setFullBodyUsage(false);
                  this.setImmovable(false);
                  this.motionY = 0;
                  this.hammer_jump_amount = 0;
              }, 20);
          }
      }, 35);
    };

    private final Consumer<EntityLivingBase> swing_hammer = (target) -> {
        this.setFightMode(true);
        if(this.isPerformingCombo()) {
            animator.performCombo(ANIM_SWING_HAMMER, 5 ,5);
        } else {
            animator.performAttack(ANIM_SWING_HAMMER);
        }

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-2));
            this.lockLook = true;
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 3);
        }, 11);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.7f, 0, false, 0.6F);
            this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 20);

        addEvent(()-> {
            this.lockLook = false;
        }, 30);

        addEvent(()-> {
            if(ModRand.percentageOf(60 - (comboAmount * 10))) {
                this.selectComboFollowUp(target);
            } else {
                addEvent(()-> {
                    this.setFightMode(false);
                }, 5);
            }
        }, 35);
    };

    private final Consumer<EntityLivingBase> swing_hammer_two = (target) -> {
      this.setFightMode(true);
      if(this.isPerformingCombo()) {
           animator.performCombo(ANIM_SWING_HAMMER_TWO, 5, 5);
      } else {
          animator.performAttack(ANIM_SWING_HAMMER_TWO);
      }

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-2));
            this.lockLook = true;
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 3);
        }, 11);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.7f, 0, false, 0.6F);
            this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 20);

        addEvent(()-> {
            this.lockLook = false;
        }, 30);

      addEvent(()-> {
          if(ModRand.percentageOf(60 - (comboAmount * 10))) {
              this.selectComboFollowUp(target);
          } else {
              addEvent(()-> {
                  this.setFightMode(false);
              }, 5);
          }
      }, 35);
    };

    private final Consumer<EntityLivingBase> heavy_swing = (target) -> {
      this.setFightMode(true);
      if(this.isPerformingCombo()) {
          animator.performCombo(ANIM_HEAVY_SWING, 5, 5);
      } else {
          animator.performAttack(ANIM_HEAVY_SWING);
      }
      this.setFullBodyUsage(true);
      this.setImmovable(true);

        addEvent(()-> {
            this.setImmovable(false);
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-2));
            this.lockLook = true;
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                this.motionY = 0;
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
                this.motionY += 0.25F;
            }, 3);
        }, 12);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(2.75f, (e) -> damage, this, offset, source, 0.7f, 0, false, 0.8F);
            this.playSound(SoundsHandler.SWING_HEAVY, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 27);

        addEvent(()-> {
            this.setImmovable(true);
            addEvent(()-> this.lockLook = false, 7);
        }, 35);

      addEvent(()-> {
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
      }, 50);
    };

    Supplier<Projectile> ground_projectiles = () -> new ProjectileRedLightningGround(world, this, (float) this.getAttack() * 0.75F, null);

    private final Consumer<EntityLivingBase> overhead_swing = (target) -> {
      this.setFightMode(true);
      if(this.isPerformingCombo()) {
          animator.performCombo(ANIM_OVERHEAD_SWING, 5, 5);
      } else {
          animator.performAttack(ANIM_OVERHEAD_SWING);
      }

      addEvent(()-> {
          this.lockLook = true;
          this.setImmovable(true);
      }, 25);

      addEvent(()-> {
        //do overhead Action
          new ActionRedLightningGround(ground_projectiles, 0.55F, 15, 20,
                  this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5,0,0)))).performAction(this, target);
      }, 33);

      addEvent(()-> {
          this.setImmovable(false);
          this.lockLook = false;
      }, 45);

      addEvent(()-> {
            this.setFightMode(false);
      }, 50);
    };

    private final Consumer<EntityLivingBase> cast_projectile = (target) -> {
      if(this.isPerformingCombo()) {
       animator.performCombo(ANIM_CAST_PROJECTILE, 5, 5);
      } else {
          animator.performAttack(ANIM_CAST_PROJECTILE);
      }
      this.setFightMode(true);

      addEvent(()-> {
        //start cast
          new ActionCastDeathProjectile().performAction(this, target);
      }, 15);

      addEvent(()-> this.setImmovable(true), 25);

      addEvent(()-> this.setImmovable(false), 45);

      addEvent(()-> {
          this.setFightMode(false);
      }, 55);
    };

    private void selectComboFollowUp(EntityLivingBase target) {
        float distance = this.getDistance(target);
        this.setPerformingCombo(true);
        this.comboAmount++;
        if(target instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) target);
            if(distance < 10) {
                if(player.getActiveItemStack().getItem() instanceof ItemShield) {
                    heavy_swing.accept(target);
                    return;
                }
                if(player.getActiveItemStack().getItem() instanceof ItemBow || player.getActiveItemStack().getItem() instanceof ItemPotion) {
                    overhead_swing.accept(target);
                    return;
                }
            }
        }

        if(distance < 12) {
            if(distance < 6 && comboAmount <= 1) {
                if(prevAttacks != swing_hammer) {
                    swing_hammer.accept(target);
                    return;
                } else if (prevAttacks != swing_hammer_two) {
                    swing_hammer_two.accept(target);
                    return;
                }
            }

            if(ModRand.percentageOf(75)) {
                heavy_swing.accept(target);
            } else {
                overhead_swing.accept(target);
            }

        } else {
            cast_projectile.accept(target);
        }
    }

    @Override
    public void updateAnimations() {
        if(this.isHammerLoop()) {
            animator.performAttack(ANIM_HAMMER_JUMP_LOOP);
        }
        if(!this.isFullBodyUsage()) {
            boolean isMovingOnGround = moveAnalysis.isMovingHorizontally() && this.onGround;
            if(isMovingOnGround) {
                if(!this.isFightMode()) {
                    animator.setCustomUpperWalk(ANIM_WALK_UPPER);
                }
                animator.setCustomLowerWalk(ANIM_WALK_LOWER);
            } else if(!this.isFightMode()) {
                animator.setCustomUpperIdle(ANIM_IDLE_UPPER);
            }
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
            this.playSound(SoundsHandler.REANIMATE_STEP, 0.4F, 0.8f + ModRand.getFloat(0.5F));
            if(world.rand.nextInt(2) == 0) {
                Main.proxy.spawnParticle(50, world, this.posX, this.posY, this.posZ, 0, 0.03, 0);
            }
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 10.0F);
            if (dist >= 10.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.5F * screamMult);
        }
        return 0;
    }
}
