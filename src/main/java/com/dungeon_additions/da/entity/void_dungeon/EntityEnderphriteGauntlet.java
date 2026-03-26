package com.dungeon_additions.da.entity.void_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.ai.void_dungeon.EntityAIEnderphrite;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class EntityEnderphriteGauntlet extends EntityEndBase implements IAnimatable, IAnimationTickable, IAttack, IScreenShake {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;
    private int teleport_cooldown = 100;
    private int shakeTime = 0;

    private final String ANIM_IDLE = "idle_gauntlet";
    private final String ANIM_WALK = "walk_gauntlet";

    private final String ANIM_TELEPORT = "teleport";
    private final String ANIM_SWING = "swing";
    private final String ANIM_JUMP_SLAM = "jump_slam";
    private final String ANIM_SUMMON_SPIKES = "summon_spikes";
    private final String ANIM_SONIC_BOOM = "sonic_boom";

    private static final DataParameter<Boolean> SWING = EntityDataManager.createKey(EntityEnderphriteGauntlet.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SONIC_BOOM = EntityDataManager.createKey(EntityEnderphriteGauntlet.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_SLAM = EntityDataManager.createKey(EntityEnderphriteGauntlet.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_SPIKES = EntityDataManager.createKey(EntityEnderphriteGauntlet.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TELEPORT = EntityDataManager.createKey(EntityEnderphriteGauntlet.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityEnderphriteGauntlet.class, DataSerializers.BOOLEAN);

    public boolean isTeleportAttack() {
        return this.dataManager.get(TELEPORT);
    }
    private void setTeleportAttack(boolean value) {
        this.dataManager.set(TELEPORT, Boolean.valueOf(value));
    }
    public boolean isSwingAttacks() {
        return this.dataManager.get(SWING);
    }
    private void setSwingAttack(boolean value) {
        this.dataManager.set(SWING, Boolean.valueOf(value));
    }
    public boolean isSonicBoom() {
        return this.dataManager.get(SONIC_BOOM);
    }
    private void setSonicBoom(boolean value) {
        this.dataManager.set(SONIC_BOOM, Boolean.valueOf(value));
    }
    public boolean isJumpSlam() {
        return this.dataManager.get(JUMP_SLAM);
    }
    private void setJumpSlam(boolean value) {
        this.dataManager.set(JUMP_SLAM, Boolean.valueOf(value));
    }
    public boolean isSummonSpikes() {
        return this.dataManager.get(SUMMON_SPIKES);
    }
    private void setSummonSpikes(boolean value) {
        this.dataManager.set(SUMMON_SPIKES, Boolean.valueOf(value));
    }
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}


    public EntityEnderphriteGauntlet(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 2.9F);
        this.experienceValue = 38;
    }

    public EntityEnderphriteGauntlet(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 2.9F);
        this.experienceValue = 38;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Teleport_Attack", this.isTeleportAttack());
        nbt.setBoolean("Swing_Attack", this.isSwingAttacks());
        nbt.setBoolean("Sonic_Boom", this.isSonicBoom());
        nbt.setBoolean("Jump_Slam", this.isJumpSlam());
        nbt.setBoolean("Summon_Spikes", this.isSummonSpikes());
        nbt.setBoolean("Shaking", this.isShaking());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setTeleportAttack(nbt.getBoolean("Teleport_Attack"));
        this.setSwingAttack(nbt.getBoolean("Swing_Attack"));
        this.setSonicBoom(nbt.getBoolean("Sonic_Boom"));
        this.setJumpSlam(nbt.getBoolean("Jump_Slam"));
        this.setSummonSpikes(nbt.getBoolean("Summon_Spikes"));
        this.setShaking(nbt.getBoolean("Shaking"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(TELEPORT, Boolean.valueOf(false));
        this.dataManager.register(SWING, Boolean.valueOf(false));
        this.dataManager.register(SONIC_BOOM, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_SPIKES, Boolean.valueOf(false));
        this.dataManager.register(JUMP_SLAM, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.enderphrite_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.32D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.enderphrite_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.enderphrite_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(4);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIEnderphrite<>(this, 1.0D, 20, 8, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.performNTimes(15, (i) -> {
                Main.proxy.spawnParticle(30, this.posX + ModRand.getFloat(1F), this.posY + ModRand.getFloat(2) + 2, this.posZ + ModRand.getFloat(1F), 0, 0.06, 0, 15423215);
            });
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.shakeTime--;
        if(!world.isRemote) {
            if(teleport_cooldown >= 0) {
                teleport_cooldown--;
            }
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(swing, sonic_boom, jump_slam, cast_spikes));
            double[] weights = {
                    (prevAttacks != swing) ? 1/distance : 0.001,
                    (prevAttacks == sonic_boom && distance <= 4 && rand.nextInt(2) == 0) ? 3 : 1/distance,
                    (prevAttacks != jump_slam) ? 1/distance : 0.001,
                    (prevAttacks != cast_spikes) ? 1/distance : 0.001

            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return 35;
    }

    private Consumer<EntityLivingBase> cast_spikes = target -> {
      this.setSummonSpikes(true);
      this.setFightMode(true);
      this.setImmovable(true);

      addEvent(()-> {
          addEvent(()-> {
              Vec3d targetOldPos = target.getPositionVector();
              addEvent(()-> {
                  Vec3d targetedPos = target.getPositionVector();
                  Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 3);
                  this.playSound(SoundsHandler.OBSIDILITH_CAST, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                  this.spawnSpikeAction(predictedPosition);
              }, 3);
          }, 1);
      }, 24);

      addEvent(()-> {
        this.setSummonSpikes(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 40);
    };

    private Consumer<EntityLivingBase> jump_slam = target -> {
      this.setJumpSlam(true);
      this.setFightMode(true);
      this.setImmovable(true);
      AtomicBoolean didDoAOE = new AtomicBoolean(false);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-2));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
                this.playSound(SoundsHandler.APATHYR_SLIGHT_DASH, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
                this.motionY = 0.7;
            }, 3);
        }, 21);

        addEvent(()->{
        for(int i = 0; i <= 16; i++) {
            addEvent(()-> {
                if(!world.isAirBlock(this.getPosition().add(0, -2, 0)) && !world.isAirBlock(this.getPosition().add(0, -1, 0)) && !didDoAOE.get()) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.0, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = (this.getAttack());
                    ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                    this.playSound(SoundsHandler.ENDERPHRITE_SONIC_BOOM, 1.1f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                    Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0.1, 0)));
                    Main.proxy.spawnParticle(21,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
                    didDoAOE.set(true);
                    this.setImmovable(true);
                    this.setShaking(true);
                    this.shakeTime = 10;
                    addEvent(()-> {
                        this.setShaking(false);
                    }, 10);
                }
                this.fallDistance = 0;
            }, i);
        }
        }, 44);

        addEvent(()-> {
            if(!didDoAOE.get()) {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.0, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (this.getAttack());
                ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                this.playSound(SoundsHandler.ENDERPHRITE_SONIC_BOOM, 1.1f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 0.1, 0)));
                Main.proxy.spawnParticle(21,world, relPos.x, relPos.y, relPos.z, 0, 0, 0);
                didDoAOE.set(true);
                this.setImmovable(true);
                this.setShaking(true);
                this.shakeTime = 10;
                addEvent(()-> {
                    this.setShaking(false);
                }, 10);
            }
        }, 61);

        addEvent(()-> this.lockLook = false, 65);

      addEvent(()-> {
        this.setJumpSlam(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 70);
    };

    private Consumer<EntityLivingBase> sonic_boom = target -> {
      this.setSonicBoom(true);
      this.setFightMode(true);
      this.setImmovable(true);

        addEvent(()-> this.playSound(SoundsHandler.ENDERPHRITE_SCREAM, 0.7f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 10);

        addEvent(()-> {
            if(this.getDistance(target) > 6) {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-2));
                addEvent(() -> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23), 0.1F);
                }, 5);
            }
            this.lockLook = true;
        }, 23);


      addEvent(()-> {
          this.setImmovable(true);
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (this.getAttack());
          ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 0.1f, 0, false);
          this.playSound(SoundsHandler.ENDERPHRITE_SONIC_BOOM, 1.1f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.2, 0)));
          Main.proxy.spawnParticle(21,world, relPos.x, relPos.y, relPos.z, 0, 0, 0);
          this.setShaking(true);
          this.shakeTime = 10;
          addEvent(()-> {
              this.setShaking(false);
          }, 10);
      }, 33);

        addEvent(()-> this.lockLook = false, 45);

      addEvent(()-> {
        this.setSonicBoom(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 55);
    };

    private Consumer<EntityLivingBase> swing = target -> {
      this.setSwingAttack(true);
      this.setFightMode(true);
      this.setImmovable(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(2));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 5);
        }, 10);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
            this.lockLook = false;
        }, 20);

        addEvent(()-> this.setImmovable(true), 30);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(2));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 5);
        }, 33);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
            this.lockLook = false;
        }, 43);

        addEvent(()-> this.setImmovable(true), 50);

      addEvent(()-> {
        this.setSwingAttack(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 60);
    };


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "walk_controller", 0, this::predicateWalk));
        data.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateAttacks));
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isTeleportAttack()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_TELEPORT));
                return PlayState.CONTINUE;
            }
            if(this.isSwingAttacks()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING));
                return PlayState.CONTINUE;
            }
            if(this.isJumpSlam()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_JUMP_SLAM));
                return PlayState.CONTINUE;
            }
            if(this.isSonicBoom()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SONIC_BOOM));
                return PlayState.CONTINUE;
            }
            if(this.isSummonSpikes()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SUMMON_SPIKES));
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

    private <E extends IAnimatable> PlayState predicateWalk(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
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
        if(!this.isFightMode() && teleport_cooldown <= 0) {
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.75f, 0.8f + ModRand.getFloat(0.4f));
            if(!world.isRemote) {
                this.doTeleportAction();
            }
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    private void doTeleportAction() {
        this.setFightMode(true);
        this.setTeleportAttack(true);
        this.setImmovable(true);
        boolean backwards = rand.nextBoolean();
        world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        addEvent(()-> {
            this.setImmovable(false);
            EntityLivingBase target = this.getAttackTarget();
            if(target != null) {
                Vec3d lookPos = target.getLookVec();
                Vec3d targetPos = new Vec3d(target.posX + lookPos.x * 0.5D, target.posY, target.posZ + lookPos.z * 0.5);
                Vec3d teleportPos = null;
                for(int i =1; i<= 86; i++) {
                    Vec3d posSet = targetPos.subtract(this.getPositionVector()).normalize();
                    Vec3d targetedPos = targetPos.add(posSet.scale(backwards ? i : -i));
                    int y = ModUtils.getSurfaceHeightZeroReturn(world, new BlockPos(targetedPos.x, 0, targetedPos.z), (int) target.posY - 4, (int) target.posY + 3);
                    if(y != 0 && (backwards && target.getDistance(targetedPos.x, y + 1, targetedPos.z) > 3 || !backwards && target.getDistance(targetedPos.x, y + 1, targetedPos.z) > 4)) {
                        teleportPos = new Vec3d(targetedPos.x, y + 1, targetedPos.z);
                        break;
                    }
                }

                if(teleportPos != null) {
                    this.setPosition(teleportPos.x, teleportPos.y, teleportPos.z);
                } else {
                    //random teleport
                    for(int i = 4; i <= 25; i++) {
                        Vec3d idealPos = new Vec3d(this.posX + ModRand.getFloat(i), this.posY, this.posZ + ModRand.getFloat(i));
                        int y = ModUtils.getSurfaceHeightZeroReturn(world, new BlockPos(idealPos.x, 0, idealPos.z), (int) target.posY - 4, (int) target.posY + 5);
                        if(y != 0 && target.getDistance(idealPos.x, y + 1, idealPos.z) > 6) {
                            this.setPosition(idealPos.x, y + 1, idealPos.z);
                        }
                    }
                }
                this.setImmovable(true);
            }
        }, 5);

        addEvent(()-> {
            this.setImmovable(false);
            this.setFightMode(false);
            this.setTeleportAttack(false);
            this.teleport_cooldown = 100;
        }, 15);
    }

    public void spawnSpikeAction(Vec3d predictedPos) {
        EntityLivingBase target = this.getAttackTarget();
        //1
        if(target != null) {
            EntityBlueWave spike = new EntityBlueWave(this.world);
            BlockPos area = new BlockPos(predictedPos.x, predictedPos.y, predictedPos.z);
            int y = getSurfaceHeight(this.world, new BlockPos(target.posX, 0, target.posZ), (int) this.posY - 5, (int) this.posY + 3);
            spike.setPosition(area.getX(), y + 1, area.getZ());
            world.spawnEntity(spike);
            spike.playSound(SoundsHandler.APPEARING_WAVE, 0.75f, 1.0f / getRNG().nextFloat() * 0.04F + 1.2F);;
            //2
            EntityBlueWave spike2 = new EntityBlueWave(this.world);
            BlockPos area2 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z);
            int y2 = getSurfaceHeight(this.world, new BlockPos(area2.getX(), 0, area2.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike2.setPosition(area2.getX(), y2 + 1, area2.getZ());
            world.spawnEntity(spike2);
            //3
            EntityBlueWave spike3 = new EntityBlueWave(this.world);
            BlockPos area3 = new BlockPos(predictedPos.x - 1, predictedPos.y, predictedPos.z);
            int y3 = getSurfaceHeight(this.world, new BlockPos(area3.getX(), 0, area3.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike3.setPosition(area3.getX(), y3 + 1, area3.getZ());
            world.spawnEntity(spike3);
            //4
            EntityBlueWave spike4 = new EntityBlueWave(this.world);
            BlockPos area4 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 1);
            int y4 = getSurfaceHeight(this.world, new BlockPos(area4.getX(), 0, area4.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike4.setPosition(area4.getX(),y4 + 1, area4.getZ());
            world.spawnEntity(spike4);
            //5
            EntityBlueWave spike5 = new EntityBlueWave(this.world);
            BlockPos area5 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z - 1);
            int y5 = getSurfaceHeight(this.world, new BlockPos(area5.getX(), 0, area5.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike5.setPosition(area5.getX(), y5 + 1, area5.getZ());
            world.spawnEntity(spike5);
            //6
            EntityBlueWave spike6 = new EntityBlueWave(this.world);
            BlockPos area6 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z + 1);
            int y6 = getSurfaceHeight(this.world, new BlockPos(area6.getX(), 0, area6.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike6.setPosition(area6.getX(), y6 + 1, area6.getZ());
            world.spawnEntity(spike6);
            //7
            EntityBlueWave spike7 = new EntityBlueWave(this.world);
            BlockPos area7 = new BlockPos(predictedPos.x +1 , predictedPos.y, predictedPos.z - 1);
            int y7 = getSurfaceHeight(this.world, new BlockPos(area7.getX(), 0, area7.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike7.setPosition(area7.getX(), y7 + 1, area7.getZ());
            world.spawnEntity(spike7);
            //8
            EntityBlueWave spike8 = new EntityBlueWave(this.world);
            BlockPos area8 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z - 1);
            int y8 = getSurfaceHeight(this.world, new BlockPos(area8.getX(), 0, area8.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike8.setPosition(area8.getX(), y8 + 1, area8.getZ());
            world.spawnEntity(spike8);
            //9
            EntityBlueWave spike9 = new EntityBlueWave(this.world);
            BlockPos area9 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z + 1);
            int y9 = getSurfaceHeight(this.world, new BlockPos(area9.getX(), 0, area9.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike9.setPosition(area9.getX(), y9 + 1, area9.getZ());
            world.spawnEntity(spike9);
            //10
            EntityBlueWave spike10 = new EntityBlueWave(this.world);
            BlockPos area10 = new BlockPos(predictedPos.x +2, predictedPos.y, predictedPos.z );
            int y10 = getSurfaceHeight(this.world, new BlockPos(area10.getX(), 0, area10.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike10.setPosition(area10.getX(), y10 + 1, area10.getZ());
            world.spawnEntity(spike10);
            //11
            EntityBlueWave spike11 = new EntityBlueWave(this.world);
            BlockPos area11 = new BlockPos(predictedPos.x - 2, predictedPos.y, predictedPos.z );
            int y11 = getSurfaceHeight(this.world, new BlockPos(area11.getX(), 0, area11.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike11.setPosition(area11.getX(), y11 + 1, area11.getZ());
            world.spawnEntity(spike11);
            //12
            EntityBlueWave spike12 = new EntityBlueWave(this.world);
            BlockPos area12 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 2);
            int y12 = getSurfaceHeight(this.world, new BlockPos(area12.getX(), 0, area12.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike12.setPosition(area12.getX(), y12 + 1, area12.getZ());
            world.spawnEntity(spike12);
            //13
            EntityBlueWave spike13 = new EntityBlueWave(this.world);
            BlockPos area13 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z -2);
            int y13 = getSurfaceHeight(this.world, new BlockPos(area13.getX(), 0, area13.getZ()), (int) this.posY - 5, (int) this.posY + 3);
            spike13.setPosition(area13.getX(), y13 + 1, area13.getZ());
            world.spawnEntity(spike13);
        }
    }

    public int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock()) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "enderphrites");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.ENDERPHRITE_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.ENDERPHRITE_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ENDERPHRITE_DEATH;
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 14.0F);
            if (dist >= 14.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 1.6F * screamMult);
        }
        return 0;
    }
}
