package com.dungeon_additions.da.entity.void_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.ai.desert_dungeon.EntityScutterBeetleAI;
import com.dungeon_additions.da.entity.ai.void_dungeon.EntityAIVoidiant;
import com.dungeon_additions.da.entity.desert_dungeon.EntityScutterBeetle;
import com.dungeon_additions.da.entity.frost_dungeon.EntityAbstractGreatWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.IDirectionalRender;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.ActionWyrkLazer;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.IMultiAction;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.entity.void_dungeon.voidiant_action.VoidiantFlameCircle;
import com.dungeon_additions.da.entity.void_dungeon.voidiant_action.VoidiantLazerAction;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
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

import java.util.List;
import java.util.function.Consumer;

public class EntityVoidiant extends EntityEndBase implements IAnimatable, IAnimationTickable, IAttack, IDirectionalRender, IPitch {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;

    public Vec3d renderLazerPos;
    public Vec3d prevRenderLazerPos;
    protected static final byte stopLazerByte = 20;
    protected boolean performLazerAttack = false;

    public IMultiAction lazerAttack =  new VoidiantLazerAction(this, stopLazerByte, (vec3d) -> {});
    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK = "walk";

    private final String ANIM_IDLE_STATE = "idle_state";
    private final String ANIM_IDLE_AWAKE = "idle_awake";
    private final String ANIM_IDLE_SHIELD = "idle_shield";

    private final String ANIM_SHOOT_LAZER = "shoot_lazer";
    private final String ANIM_STOMP = "stomp";

    private static final DataParameter<Boolean> IDLE_STATE = EntityDataManager.createKey(EntityVoidiant.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IDLE_SEND = EntityDataManager.createKey(EntityVoidiant.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IDLE_RETURN = EntityDataManager.createKey(EntityVoidiant.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STOMP = EntityDataManager.createKey(EntityVoidiant.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHOOT_LAZER = EntityDataManager.createKey(EntityVoidiant.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityVoidiant.class, DataSerializers.FLOAT);

    public boolean isIdleState() {
        return this.dataManager.get(IDLE_STATE);
    }
    private void setIdleState(boolean value) {
        this.dataManager.set(IDLE_STATE, Boolean.valueOf(value));
    }
    public boolean isIdleSend() {
        return this.dataManager.get(IDLE_SEND);
    }
    private void setIdleSend(boolean value) {
        this.dataManager.set(IDLE_SEND, Boolean.valueOf(value));
    }
    public boolean isIdleReturn() {
        return this.dataManager.get(IDLE_RETURN);
    }
    private void setIdleReturn(boolean value) {
        this.dataManager.set(IDLE_RETURN, Boolean.valueOf(value));
    }
    public boolean isStompAttack() {
        return this.dataManager.get(STOMP);
    }
    private void setStompAttack(boolean value) {
        this.dataManager.set(STOMP, Boolean.valueOf(value));
    }
    public boolean isShootLazer() {
        return this.dataManager.get(SHOOT_LAZER);
    }
    private void setShootLazer(boolean value) {
        this.dataManager.set(SHOOT_LAZER, Boolean.valueOf(value));
    }


    public EntityVoidiant(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.1F, 2F);
        this.experienceValue = 15;
    }

    public EntityVoidiant(World worldIn) {
        super(worldIn);
        this.setSize(1.1F, 2F);
        this.experienceValue = 15;
    }

    private boolean sentIdleFlag;
    private boolean sentAttackFlag;
    private int closeIndicator = 0;
    private int checkNearbyEntities = 10;

    private int flameCircleAttackCooldown = 20;
    @Override
    public void onUpdate() {
        super.onUpdate();
        flameCircleAttackCooldown--;

        if(!world.isRemote && this.isIdleState()) {
            this.motionX = 0;
            this.motionZ = 0;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.setRotationYawHead(0);
        }

        if(!this.world.isRemote) {
            EntityLivingBase target = this.getAttackTarget();

            //gives nearby players Blindness and Levitation
            if(checkNearbyEntities < 0 && !this.isIdleState()) {
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.25D), e -> !e.getIsInvulnerable() && (!(e instanceof EntityEndBase)));
                if(!targets.isEmpty()) {
                    for(EntityLivingBase base : targets) {
                        if(!(base instanceof EntityEndBase)) {
                                    base.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 100, 2, false, false));
                                  }
                             }
                        }
                checkNearbyEntities = 10;
            } else {
                checkNearbyEntities--;
            }
            if(target != null) {
                if(closeIndicator == 0 && this.isIdleState() && !this.sentAttackFlag) {
                    this.sendAttackMode();
                    this.sentAttackFlag = true;
                }

                //does flame attack if the player lingers
                if(this.isIdleState() && this.getDistance(target) < 6) {
                    if(flameCircleAttackCooldown < 0 && !this.isFightMode()) {
                        flame_circle.accept(target);
                        flameCircleAttackCooldown = 80;
                    }
                }

                if(closeIndicator > 1 && !this.isFightMode()) {
                    if(this.getDistance(target) < 8 && !this.isIdleState()) {
                        this.sendDefensive();
                    }
                }

                if(this.isIdleState() && this.getDistance(target) > 8) {
                    if(closeIndicator != 0){
                        closeIndicator--;
                    }
                }
            } else if(!this.isIdleState() && !this.sentIdleFlag && !this.isFightMode()) {
                this.sendDefensive();
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

    private void sendDefensive() {
        this.sentIdleFlag = true;
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.setIdleSend(true);
        this.playSound(SoundsHandler.VOIDIANT_OPEN, 1.0f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));


        addEvent(()-> {
            this.setFullBodyUsage(false);
            this.setIdleSend(false);
            this.setIdleState(true);
            this.sentIdleFlag = false;
        }, 20);
    }

    private void sendAttackMode() {
        this.setIdleState(false);
        this.setIdleReturn(true);
        this.setFullBodyUsage(true);
        this.playSound(SoundsHandler.VOIDIANT_OPEN, 1.0f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
        addEvent(()-> {
            this.setIdleReturn(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            this.sentAttackFlag = false;
        }, 20);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Idle_State", this.isIdleState());
        nbt.setBoolean("Idle_Return", this.isIdleReturn());
        nbt.setBoolean("Idle_Send", this.isIdleSend());
        nbt.setBoolean("Stomp", this.isStompAttack());
        nbt.setBoolean("Shoot_Lazer", this.isShootLazer());
        nbt.setFloat("Look", this.getPitch());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setIdleState(nbt.getBoolean("Idle_State"));
        this.setIdleReturn(nbt.getBoolean("Idle_Return"));
        this.setIdleSend(nbt.getBoolean("Idle_Send"));
        this.setStompAttack(nbt.getBoolean("Stomp"));
        this.setShootLazer(nbt.getBoolean("Shoot_Lazer"));
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(IDLE_STATE, Boolean.valueOf(false));
        this.dataManager.register(IDLE_RETURN, Boolean.valueOf(false));
        this.dataManager.register(IDLE_SEND, Boolean.valueOf(false));
        this.dataManager.register(STOMP, Boolean.valueOf(false));
        this.dataManager.register(SHOOT_LAZER, Boolean.valueOf(false));
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.voidiant_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.29D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.voidiant_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.voidiant_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIVoidiant<>(this, 1.0D, 20, 20, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityEnderman>(this, EntityEnderman.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode() && !this.isFullBodyUsage() && !this.isIdleState()) {
            if(distance < 6) {
                prevAttacks = stomp_attack;
            } else {
                if(prevAttacks == shoot_lazer_attack) {
                    prevAttacks = cast_spell;
                } else {
                    prevAttacks = shoot_lazer_attack;
                }
            }
            prevAttacks.accept(target);
        }
        return 40;
    }

    private Consumer<EntityLivingBase> shoot_lazer_attack = target -> {
      this.setFightMode(true);
      addEvent(()-> {
          this.setImmovable(true);
          this.setFullBodyUsage(true);
          this.setShootLazer(true);

          addEvent(()-> {
              this.performLazerAttack = true;
              lazerAttack.doAction();
              this.playSound(SoundsHandler.VOIDIANT_CHARGE_LAZER, 2.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
          }, 15);

          addEvent(()-> {
              this.playSound(SoundsHandler.VOIDIANT_SHOOT_LAZER, 2.0f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
          }, 35);

          addEvent(()-> {
              this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.7f, 0.6f + ModRand.getFloat(0.2f));
              this.performLazerAttack = false;
          }, 45);

          addEvent(()-> {
              this.setImmovable(false);
              this.setFightMode(false);
              this.setFullBodyUsage(false);
              this.setShootLazer(false);
          }, 55);
      }, 30);
         };

    private Consumer<EntityLivingBase> flame_circle = target -> {
      this.setFightMode(true);
        this.playSound(SoundsHandler.OBSIDILITH_WAVE_DING, 1.0f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
        world.setEntityState(this, ModUtils.FOURTH_PARTICLE_BYTE);
      addEvent(()-> new VoidiantFlameCircle().performAction(this, target), 5);
      addEvent(() -> {
          this.setFightMode(false);
      }, 20);
    };

    private boolean enableBlueParticles = false;

    private Consumer<EntityLivingBase> cast_spell = target -> {
      this.setFightMode(true);
      this.enableBlueParticles = true;

      addEvent(()-> {
          //spawn small wave
          this.playSound(SoundsHandler.OBSIDILITH_WAVE_DING, 1.0f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
          this.enableBlueParticles = false;
              Vec3d targetOldPos = target.getPositionVector();
              addEvent(()-> {
                  Vec3d targetedPos = target.getPositionVector();
                  Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 3);
                  this.spawnSpikeAction(predictedPosition);
              }, 3);

          this.setFightMode(false);
      }, 40);
    };

    private Consumer<EntityLivingBase> stomp_attack = target -> {
    this.setImmovable(true);
    this.setFightMode(true);
    this.setFullBodyUsage(true);
    this.setStompAttack(true);

    addEvent(()-> {
        this.playSound(SoundsHandler.VOIDIANT_JUMP, 1.0f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
    }, 15);

    addEvent(()-> {
        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0)));
        DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
        float damage = this.getAttack();
        ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 0.8f, 0, false);
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            Main.proxy.spawnParticle(18,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.9f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
    }, 30);

    addEvent(()-> {
        this.closeIndicator++;
        this.setImmovable(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setStompAttack(false);
    }, 55);
    };

    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(world.rand.nextInt(4) == 0 && !this.isIdleState()) {
            world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
        }

        if(this.enableBlueParticles) {
            world.setEntityState(this, ModUtils.FIFTH_PARTICLE_BYTE);
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);

        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            Main.proxy.spawnParticle(8, this.posX + ModRand.range(-2, 2), this.posY + 0.25 + ModRand.getFloat(2.5F), this.posZ + ModRand.range(-2, 2), 0,0,0);
        }
        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 18, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1)), ModColors.WHITE, pos.normalize().scale(0.1), ModRand.range(55, 65));
            });
        }

        if(id == ModUtils.FOURTH_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 18, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1)), ModColors.FIREBALL_ORANGE, pos.normalize(), ModRand.range(55, 65));
            });
        }

        if(id == ModUtils.FIFTH_PARTICLE_BYTE) {
            for (int i = 0; i < 1; i++) {
                Vec3d lookVec = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
                Vec3d particlePos = this.getPositionEyes(1).add(ModUtils.getAxisOffset(lookVec, new Vec3d(0.7, 0, 0)));
                ParticleManager.spawnColoredSmoke(world, particlePos, ModColors.AZURE, Vec3d.ZERO);
            }
        }

        if (id == stopLazerByte) {
            this.renderLazerPos = null;
        }
        else if(id == ModUtils.PARTICLE_BYTE) {
            for (int i = 0; i < 1; i++) {
                Vec3d lookVec = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
                Vec3d particlePos = this.getPositionEyes(1).add(ModUtils.getAxisOffset(lookVec, new Vec3d(0.7, 0, 0)));
                ParticleManager.spawnColoredSmoke(world, particlePos, ModColors.WHITE, Vec3d.ZERO);
            }
        }
    }

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        this.dataManager.set(LOOK, clampedLook);
    }


    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "walk_controller", 0, this::predicateWalk));
        data.addAnimationController(new AnimationController(this, "attacks_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "state_controller", 0, this::predicateStateChange));
    }

    private<E extends IAnimatable> PlayState predicateStateChange(AnimationEvent<E> event) {
        if(this.isFullBodyUsage()) {
            if(this.isIdleReturn()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_AWAKE, false));
                return PlayState.CONTINUE;
            }
            if(this.isIdleSend()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_SHIELD, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private<E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isStompAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STOMP, false));
                return PlayState.CONTINUE;
            }
            if(this.isShootLazer()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT_LAZER, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage()) {
            if(this.isIdleState()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_STATE, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateWalk(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage() && !this.isIdleState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
            event.getController().setAnimationSpeed(1.0 + (0.004 * event.getLimbSwing()));
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
        return SoundsHandler.VOIDIANT_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.VOIDIANT_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.VOIDIANT_WALK, 0.6F, 0.9f + ModRand.getFloat(0.3F));
    }

    @Override
    public void setRenderDirection(Vec3d dir) {
        if (this.renderLazerPos != null) {
            this.prevRenderLazerPos = this.renderLazerPos;
        } else {
            this.prevRenderLazerPos = dir;
        }
        this.renderLazerPos = dir;
    }

    public void spawnSpikeAction(Vec3d predictedPos) {
        EntityLivingBase target = this.getAttackTarget();
        //1
        if(target != null) {
            EntityBlueWave spike = new EntityBlueWave(this.world);
            BlockPos area = new BlockPos(predictedPos.x, predictedPos.y, predictedPos.z);
            int y = getSurfaceHeight(this.world, new BlockPos(target.posX, 0, target.posZ), (int) target.posY - 3, (int) target.posY + 3);
            spike.setPosition(area.getX(), y + 1, area.getZ());
            world.spawnEntity(spike);
            spike.playSound(SoundsHandler.APPEARING_WAVE, 0.75f, 1.0f / getRNG().nextFloat() * 0.04F + 1.2F);;
            //2
            EntityBlueWave spike2 = new EntityBlueWave(this.world);
            BlockPos area2 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z);
            int y2 = getSurfaceHeight(this.world, new BlockPos(area2.getX(), 0, area2.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike2.setPosition(area2.getX(), y2 + 1, area2.getZ());
            world.spawnEntity(spike2);
            //3
            EntityBlueWave spike3 = new EntityBlueWave(this.world);
            BlockPos area3 = new BlockPos(predictedPos.x - 1, predictedPos.y, predictedPos.z);
            int y3 = getSurfaceHeight(this.world, new BlockPos(area3.getX(), 0, area3.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike3.setPosition(area3.getX(), y3 + 1, area3.getZ());
            world.spawnEntity(spike3);
            //4
            EntityBlueWave spike4 = new EntityBlueWave(this.world);
            BlockPos area4 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 1);
            int y4 = getSurfaceHeight(this.world, new BlockPos(area4.getX(), 0, area4.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike4.setPosition(area4.getX(),y4 + 1, area4.getZ());
            world.spawnEntity(spike4);
            //5
            EntityBlueWave spike5 = new EntityBlueWave(this.world);
            BlockPos area5 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z - 1);
            int y5 = getSurfaceHeight(this.world, new BlockPos(area5.getX(), 0, area5.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike5.setPosition(area5.getX(), y5 + 1, area5.getZ());
            world.spawnEntity(spike5);
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
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.isIdleState()) {
            this.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 0.75f, 0.8f + ModRand.getFloat(0.4f));
            return false;
        }
        if(source.isProjectile()) {
            return super.attackEntityFrom(source, amount * 0.5F);
        }
        return super.attackEntityFrom(source, amount);
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "voidiant");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
