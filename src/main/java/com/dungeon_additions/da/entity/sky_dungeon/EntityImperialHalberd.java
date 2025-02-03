package com.dungeon_additions.da.entity.sky_dungeon;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.EntityDraugrMeleeAI;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.sky_dungeon.EntityImperialHalberdAI;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.sky_dungeon.city_knights.ActionHalberdSpecial;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.sun.jna.platform.win32.WinBase;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
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
import org.lwjgl.Sys;
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

public class EntityImperialHalberd extends EntitySkyBase implements IAnimatable, IAnimationTickable, IAttack {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;

    private static final DataParameter<Boolean> SWING_MELEE = EntityDataManager.createKey(EntityImperialHalberd.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STORM_STOMP = EntityDataManager.createKey(EntityImperialHalberd.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> AOE_SWING = EntityDataManager.createKey(EntityImperialHalberd.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_LIGHTNING = EntityDataManager.createKey(EntityImperialHalberd.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> PIERCE_SWING = EntityDataManager.createKey(EntityImperialHalberd.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ROLL_AROUND = EntityDataManager.createKey(EntityImperialHalberd.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLOCK_ACTION = EntityDataManager.createKey(EntityImperialHalberd.class, DataSerializers.BOOLEAN);

    private final String ANIM_SWING_MELEE = "regular_swing";
    private final String ANIM_AOE_SWING = "swing_around";
    private final String ANIM_STORM_STOMP = "storm_stomp";
    private final String ANIM_SUMMON_LIGHTNING = "lightning_strike";
    private final String ANIM_PIERCE_SWING = "combo";
    private final String ANIM_ROLL_AROUND = "roll_around";
    private final String ANIM_BLOCK = "block";
    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_WALK_LOWER = "walk";
    private boolean isSwingMelee() {return this.dataManager.get(SWING_MELEE);}
    private void setSwingMelee(boolean value) {this.dataManager.set(SWING_MELEE, Boolean.valueOf(value));}
    private boolean isStormStomp() {return this.dataManager.get(STORM_STOMP);}
    private void setStormStomp(boolean value) {this.dataManager.set(STORM_STOMP, Boolean.valueOf(value));}
    private boolean isAoeSwing() {return this.dataManager.get(AOE_SWING);}
    private void setAoeSwing(boolean value) {this.dataManager.set(AOE_SWING, Boolean.valueOf(value));}
    private boolean isSummonLightning() {return this.dataManager.get(SUMMON_LIGHTNING);}
    private void setSummonLightning(boolean value) {this.dataManager.set(SUMMON_LIGHTNING, Boolean.valueOf(value));}
    private boolean isPierceSwing() {return this.dataManager.get(PIERCE_SWING);}
    private void setPierceSwing(boolean value) {this.dataManager.set(PIERCE_SWING, Boolean.valueOf(value));}
    private boolean isRollAround() {return this.dataManager.get(ROLL_AROUND);}
    private void setRollAround(boolean value) {this.dataManager.set(ROLL_AROUND, Boolean.valueOf(value));}
    private boolean isBlock() {return this.dataManager.get(BLOCK_ACTION);}
    private void setBlockAction(boolean value) {this.dataManager.set(BLOCK_ACTION, Boolean.valueOf(value));}


    public EntityImperialHalberd(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 2.40F);
    }

    public EntityImperialHalberd(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 2.40F);
    }

    private boolean isInEasySight = false;
    private boolean checkTargetPosition = false;
    private int storm_stomp_cooldown = 250;

    private int useLongRangeSpell = 300;

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Swing_Melee", this.isSwingMelee());
        nbt.setBoolean("Storm_Stomp", this.isStormStomp());
        nbt.setBoolean("Aoe_Swing", this.isAoeSwing());
        nbt.setBoolean("Summon_Lightning", this.isSummonLightning());
        nbt.setBoolean("Pierce_Swing", this.isPierceSwing());
        nbt.setBoolean("Roll_Around", this.isRollAround());
        nbt.setBoolean("Block_Action", this.isBlock());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSwingMelee(nbt.getBoolean("Swing_Melee"));
        this.setStormStomp(nbt.getBoolean("Storm_Stomp"));
        this.setAoeSwing(nbt.getBoolean("Aoe_Swing"));
        this.setSummonLightning(nbt.getBoolean("Summon_Lightning"));
        this.setPierceSwing(nbt.getBoolean("Pierce_Swing"));
        this.setRollAround(nbt.getBoolean("Roll_Around"));
        this.setBlockAction(nbt.getBoolean("Block_Action"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SWING_MELEE, Boolean.valueOf(false));
        this.dataManager.register(STORM_STOMP, Boolean.valueOf(false));
        this.dataManager.register(AOE_SWING, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_LIGHTNING, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_SWING, Boolean.valueOf(false));
        this.dataManager.register(ROLL_AROUND, Boolean.valueOf(false));
        this.dataManager.register(BLOCK_ACTION, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityImperialHalberdAI<>(this, 1, 30, 6, 0.15F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        storm_stomp_cooldown--;
        useLongRangeSpell--;

        EntityLivingBase target = this.getAttackTarget();
        if(target != null && !world.isRemote) {
            if(this.checkTargetPosition) {
                if(checkSightLine(target)) {
                    this.isInEasySight = true;
                } else {
                    this.isInEasySight = false;
                }
                this.checkTargetPosition = false;
            }

            if (useLongRangeSpell < 0) {
                double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
                double distance = Math.sqrt(distSq);

                if (distance >= 7 && !this.isFightMode()) {
                    summon_lightning.accept(target);
                    useLongRangeSpell = 340;
                }
            }
        }
    }


    private boolean checkSightLine(EntityLivingBase target) {
        List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(10.0D, 5.0D, 5.0D).offset(4.9, 0, 0), e -> !e.getIsInvulnerable());
        if(!nearbyEntities.isEmpty()) {
            for(EntityLivingBase base : nearbyEntities) {
                if (base == target) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);

        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(regular_swing, aoe_swing, storm_stomp, summon_lightning, pierce_swing, roll_around_action));
            double[] weights = {
                    (prevAttacks != regular_swing) ? 1/distance : 0,
                    (distance <= 5 && prevAttacks != aoe_swing && !this.isInEasySight) ? 2/distance : (distance <= 5 && prevAttacks != aoe_swing && prevAttacks != storm_stomp) ? 1/distance : 0,
                    (distance <= 4 && prevAttacks != aoe_swing && prevAttacks != storm_stomp && storm_stomp_cooldown < 0) ? 1/distance : 0,
                    (prevAttacks != summon_lightning) ? 1/distance : 0,
                    (prevAttacks != pierce_swing) ? 1/distance : 0,
                    (prevAttacks != roll_around_action) ? 1/distance : 0
            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return prevAttacks == roll_around_action ? 60 : 5;
    }

    private final Consumer<EntityLivingBase> roll_around_action = (target) -> {
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.setRollAround(true);
      this.setFightMode(true);

      addEvent(()-> {
          Vec3d targetedPos = target.getPositionVector();
          this.lockLook = true;
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18),0.2F);
          }, 5);
      }, 10);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.2f, 0, false);
          this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
          this.lockLook = false;
      }, 20);

      addEvent(()-> {
        this.setImmovable(true);
      }, 34);

      addEvent(()-> {
          Vec3d targetedPos = target.getPositionVector();
          addEvent(()-> {
              this.lockLook = true;
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.14),0.2F);
          }, 5);
      }, 45);

      addEvent(()-> {
          this.setImmovable(true);
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
          float damage = (float) (this.getAttack() * 1.25);
          ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 0.2f, 0, false);
          this.playSound(SoundsHandler.IMPERIAL_START_MAGIC, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
          new ActionHalberdSpecial().performAction(this, target);
      }, 60);

      addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(false);
            this.setFightMode(false);
            this.setRollAround(false);
            this.setFullBodyUsage(false);
      },   82);
    };
    private final Consumer<EntityLivingBase> pierce_swing = (target) -> {
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.setPierceSwing(true);
        this.setFightMode(true);

        addEvent(()-> {
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                this.lockLook = true;
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18),0.2F);
            }, 5);
        }, 19);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 29);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 45);

        addEvent(()-> {
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.12),0.2F);
            }, 5);
        }, 47);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 57);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(true);
        }, 65);

        addEvent(()-> {
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            this.setPierceSwing(false);
        }, 75);
    };
    private final Consumer<EntityLivingBase> summon_lightning = (target) -> {
      this.setSummonLightning(true);
      this.setFightMode(true);
      this.lockLook = true;

      addEvent(()-> this.setImmovable(true), 20);

      addEvent(()-> {
          Vec3d targetOldPos = target.getPositionVector();
          addEvent(()-> {
              Vec3d targetedPos = target.getPositionVector();
              Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 4);
              BlockPos yCord = new BlockPos(predictedPosition.x, predictedPosition.y, predictedPosition.z);
              int y = ModUtils.getSurfaceHeightGeneral(world, yCord, yCord.getY() - 6, yCord.getY() + 4);
              EntitySkyBolt bolt = new EntitySkyBolt(world, predictedPosition.add(0, 10, 0));
              bolt.setPosition(predictedPosition.x, y, predictedPosition.z);
              world.spawnEntity(bolt);
          }, 3);
          this.playSound(SoundsHandler.IMPERIAL_HALBERD_CAST, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 17);

        addEvent(()-> {
            Vec3d targetOldPos = target.getPositionVector();
            addEvent(()-> {
                Vec3d targetedPos = target.getPositionVector();
                Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 4);
                BlockPos yCord = new BlockPos(predictedPosition.x, predictedPosition.y, predictedPosition.z);
                int y = ModUtils.getSurfaceHeightGeneral(world, yCord, yCord.getY() - 6, yCord.getY() + 4);
                EntitySkyBolt bolt = new EntitySkyBolt(world, predictedPosition.add(0, 10, 0));
                bolt.setPosition(predictedPosition.x, y, predictedPosition.z);
                world.spawnEntity(bolt);
            }, 3);
        }, 23);

        addEvent(()-> {
            Vec3d targetOldPos = target.getPositionVector();
            addEvent(()-> {
                Vec3d targetedPos = target.getPositionVector();
                Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 4);
                BlockPos yCord = new BlockPos(predictedPosition.x, predictedPosition.y, predictedPosition.z);
                int y = ModUtils.getSurfaceHeightGeneral(world, yCord, yCord.getY() - 6, yCord.getY() + 4);
                EntitySkyBolt bolt = new EntitySkyBolt(world, predictedPosition.add(0, 10, 0));
                bolt.setPosition(predictedPosition.x, y, predictedPosition.z);
                world.spawnEntity(bolt);
            }, 3);
        }, 28);

      addEvent(()-> {
          this.setImmovable(false);
          this.lockLook = false;
          this.setSummonLightning(false);
          this.setFightMode(false);
          this.checkTargetPosition = true;
      }, 40);
    };

    private final Consumer<EntityLivingBase> storm_stomp = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setStormStomp(true);
      this.setImmovable(true);

      addEvent(() -> {
          playSound(SoundsHandler.IMPERIAL_STORM_STOMP, 1, 0.7f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
            EntitySkyTornado tornado = new EntitySkyTornado(world, true);
            tornado.setPosition(this.posX, this.posY, this.posZ);
            world.spawnEntity(tornado);
      }, 25);

      addEvent(()-> {
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setStormStomp(false);
        this.setImmovable(false);
        this.storm_stomp_cooldown = 250;
      }, 50);
    };

    private final Consumer<EntityLivingBase> aoe_swing = (target) -> {
        this.setAoeSwing(true);
        this.setImmovable(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.lockLook = true;
        addEvent(()-> this.lockLook = false, 23);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 0.8f, 0, false);
            this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 27);

        addEvent(()-> this.lockLook  =true, 40);
        addEvent(()-> this.lockLook = false, 50);

        addEvent(()-> {
            this.setAoeSwing(false);
            this.setImmovable(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.checkTargetPosition = true;
        }, 55);
    };

    private final Consumer<EntityLivingBase> regular_swing = (target) -> {
        this.setSwingMelee(true);
        this.setFightMode(true);

        addEvent(()-> {
            this.setImmovable(true);
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> this.lockLook  =true, 5);
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18),0.2F);
            }, 5);
        }, 20);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.25, 1.1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.1f, 0, false);
            this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 31);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(true);
        }, 37);

        addEvent(()-> {
            this.setImmovable(false);
        this.setSwingMelee(false);
        this.setFightMode(false);
            this.checkTargetPosition = true;
        }, 40);
    };
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSwingMelee()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_MELEE, false));
                return PlayState.CONTINUE;
            }
            if(this.isStormStomp()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STORM_STOMP, false));
                return PlayState.CONTINUE;
            }
            if(this.isAoeSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_AOE_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isSummonLightning()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_LIGHTNING, false));
                return PlayState.CONTINUE;
            }
            if(this.isRollAround()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ROLL_AROUND, false));
                return PlayState.CONTINUE;
            }
            if(this.isPierceSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PIERCE_SWING, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode()) {
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
    public void tick() {

    }

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
    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "imperial_halberd");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
