package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityAIAssassinAttack;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityAISorcererAttack;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EntityDarkSorcerer extends EntityDarkBase implements IAnimatable, IAttack, IAnimationTickable {

    private static final DataParameter<Boolean> SWING_MELEE = EntityDataManager.createKey(EntityDarkSorcerer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_FINISH = EntityDataManager.createKey(EntityDarkSorcerer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_CONTINUE = EntityDataManager.createKey(EntityDarkSorcerer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_DASH = EntityDataManager.createKey(EntityDarkSorcerer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_MINION = EntityDataManager.createKey(EntityDarkSorcerer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_SPELL = EntityDataManager.createKey(EntityDarkSorcerer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> EXCHANGE = EntityDataManager.createKey(EntityDarkSorcerer.class, DataSerializers.BOOLEAN);

    private boolean isSwingMelee() {
        return this.dataManager.get(SWING_MELEE);
    }

    private void setSwingMelee(boolean value) {
        this.dataManager.set(SWING_MELEE, Boolean.valueOf(value));
    }
    private boolean isSwingFinish() {return this.dataManager.get(SWING_FINISH);}
    private void setSwingFinish(boolean value) {this.dataManager.set(SWING_FINISH, Boolean.valueOf(value));}
    private boolean isSwingContinue() {return this.dataManager.get(SWING_CONTINUE);}
    private void setSwingContinue(boolean value) {this.dataManager.set(SWING_CONTINUE, Boolean.valueOf(value));}
    private boolean isSwingDash() {return this.dataManager.get(SWING_DASH);}
    private void setSwingDash(boolean value) {this.dataManager.set(SWING_DASH, Boolean.valueOf(value));}
    public boolean isSummonMinion() {return this.dataManager.get(SUMMON_MINION);}
    private void setSummonMinion(boolean value) {this.dataManager.set(SUMMON_MINION, Boolean.valueOf(value));}
    private boolean isCastSpell() {return this.dataManager.get(CAST_SPELL);}
    private void setCastSpell(boolean value) {this.dataManager.set(CAST_SPELL, Boolean.valueOf(value));}
    private boolean isExchange() {return this.dataManager.get(EXCHANGE);}
    private void setExchange(boolean value) {this.dataManager.set(EXCHANGE, Boolean.valueOf(value));}

    private final String ANIM_IDLE = "idle";
    private final String ANIM_BASE_MODEL = "default_pose";
    private final String ANIM_SWING = "swing";
    private final String ANIM_SWING_CONTINUE = "swing_continue";
    private final String ANIM_SWING_FINISH = "swing_finish";
    private final String ANIM_DASH_ATTACK = "dash";
    private final String ANIM_EXCHANGE = "exchange";
    private final String ANIM_SUMMON = "summon";
    private final String ANIM_CAST = "cast";

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;

    public List<WeakReference<Entity>> current_mobs = Lists.newArrayList();

    public EntityDarkSorcerer(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 15;
        this.setSize(0.6F, 1.95F);
    }

    public EntityDarkSorcerer(World worldIn) {
        super(worldIn);
        this.experienceValue = 15;
        this.setSize(0.6F, 1.95F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Swing_Melee", this.isSwingMelee());
        nbt.setBoolean("Swing_Continue", this.isSwingContinue());
        nbt.setBoolean("Swing_Finish", this.isSwingFinish());
        nbt.setBoolean("Swing_Dash", this.isSwingDash());
        nbt.setBoolean("Summon_Minion", this.isSummonMinion());
        nbt.setBoolean("Cast_Spell", this.isCastSpell());
        nbt.setBoolean("Exchange", this.isExchange());
        NBTTagList mobs = new NBTTagList();
        for (WeakReference<Entity> ref : current_mobs) {
            if (ref.get() == null) continue;
            mobs.appendTag(NBTUtil.createUUIDTag(ref.get().getUniqueID()));
        }
        nbt.setTag("current_mobs", mobs);
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSwingMelee(nbt.getBoolean("Swing_Melee"));
        this.setSwingContinue(nbt.getBoolean("Swing_Continue"));
        this.setSwingFinish(nbt.getBoolean("Swing_Finish"));
        this.setSwingDash(nbt.getBoolean("Swing_Dash"));
        this.setSummonMinion(nbt.getBoolean("Summon_Minion"));
        this.setCastSpell(nbt.getBoolean("Cast_Spell"));
        this.setExchange(nbt.getBoolean("Exchange"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SWING_MELEE, Boolean.valueOf(false));
        this.dataManager.register(SWING_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(SWING_FINISH, Boolean.valueOf(false));
        this.dataManager.register(SWING_DASH, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_MINION, Boolean.valueOf(false));
        this.dataManager.register(CAST_SPELL, Boolean.valueOf(false));
        this.dataManager.register(EXCHANGE, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.sorcerer_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.39D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.sorcerer_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.sorcerer_armor);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.6D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAISorcererAttack<>(this, 1.0D, 30, 12, 0.3F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onLivingUpdate()
    {
        if (this.world.isDaytime() && !this.world.isRemote && MobConfig.assassin_burns_daylight)
        {
            //removes these guys when its daytime
            float f = this.getBrightness();

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ)))
            {
                this.setDead();
            }
        }

        super.onLivingUpdate();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        //updates the mobs currently summoned by the sorcerer
        this.clearInvalidEntities();

    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        if(MobConfig.cult_sorcerer_status) {
            return EnumCreatureAttribute.UNDEAD;
        } else {
            return EnumCreatureAttribute.UNDEFINED;
        }
    }


    @Override
    public boolean getCanSpawnHere()
    {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
    }

    protected boolean isValidLightLevel()
    {
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

        if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32))
        {
            return false;
        }
        else
        {
            int i = this.world.getLightFromNeighbors(blockpos);

            if (this.world.isThundering())
            {
                int j = this.world.getSkylightSubtracted();
                this.world.setSkylightSubtracted(10);
                i = this.world.getLightFromNeighbors(blockpos);
                this.world.setSkylightSubtracted(j);
            }

            return i <= this.rand.nextInt(8);
        }
    }

    /**
     * This ensures that active mobs are still within a distance and are still alive to be accounted for
     */
    private void clearInvalidEntities() {
        current_mobs = current_mobs.stream().filter(ref -> ref.get() != null && ref.get().getDistance(this) <= 30 && ref.get().isEntityAlive()).collect(Collectors.toList());
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(summon_minion, cast_spell, dash_attack, swing_attack, exchange));
            double[] weights = {
                    (prevAttacks != summon_minion && current_mobs.size() < MobConfig.sorcerer_minions_cap) ? distance * 0.05 : 0,
                    (distance > 6) ? distance * 0.02 : 0,
                    (prevAttacks != dash_attack) ? 1/distance : 0,
                    (prevAttacks != swing_attack && distance <= 6) ? 1/distance : 0,
                    (prevAttacks != exchange && distance <= 6) ? 1/distance : 0
            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return distance > 6 ? 90 : 30;
    }

    private final Consumer<EntityLivingBase> swing_attack = (target) -> {
      this.setSwingMelee(true);
      this.setFightMode(true);

      addEvent(()-> {
          this.setImmovable(true);
      }, 5);

      addEvent(()-> {
          this.lockLook = true;
          Vec3d targetedPos = target.getPositionVector();
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.24),0.2F);
          }, 5);
      }, 15);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = (this.getAttack());
          ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.3f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
      }, 25);

      addEvent(()-> {
        this.setImmovable(true);
        this.lockLook = false;
      }, 40);

      addEvent(()-> {
          int randI = ModRand.range(1, 11);
          this.setSwingMelee(false);
          if(randI >= 6) {
              //continue Melee Attack
              this.setSwingTooContinue(target);
          } else {
              //finish
              this.setSwingFinish(true);
              addEvent(()-> {
                  this.setImmovable(false);
                this.setSwingFinish(false);
                this.setFightMode(false);
              }, 20);
          }
      }, 45);
    };

    private void setSwingTooContinue(EntityLivingBase target) {
        swing_continue.accept(target);
    }

    private final Consumer<EntityLivingBase> swing_continue = (target) -> {
      this.setSwingContinue(true);
      this.setFightMode(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.24),0.2F);
            }, 5);
        }, 7);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.3f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 18);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 30);

      addEvent(()-> {
          this.setImmovable(false);
        this.setSwingContinue(false);
        this.setFightMode(false);
      }, 50);
    };

    private final Consumer<EntityLivingBase> dash_attack = (target) -> {
      this.setFightMode(true);
      this.setSwingDash(true);
      this.setImmovable(true);
      addEvent(()-> {
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-4));
          ModUtils.attemptTeleport(targetedPos, this);
      }, 20);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
            //  ModUtils.attemptTeleport(targetedPos, this);
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.35),0.1F);
            }, 7);
        }, 23);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.7f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 35);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 45);

        addEvent(()-> {
            this.setImmovable(false);
        }, 70);

      addEvent(()-> {
        this.setFightMode(false);
        this.setSwingDash(false);
      }, 90);
    };

    private final Consumer<EntityLivingBase> exchange = (target) -> {
      this.setExchange(true);
      this.setFightMode(true);

      addEvent(()-> {
          this.playSound(SoundsHandler.DARK_SORCERER_EXCHANGE, 0.75f, 0.7f / (rand.nextFloat() * 0.4F + 0.7f));
      }, 30);
      addEvent(()-> {
        //exchange minion
          EntityShadowHand handI = new EntityShadowHand(world);
          handI.setPosition(this.posX, this.posY, this.posZ);
          world.spawnEntity(handI);
          for(int i = 0; i < 15; i +=5) {

              addEvent(()-> {
                  EntityShadowHand hand = new EntityShadowHand(world);
                  Vec3d randPosI = this.getPositionVector().add(ModRand.range(-6, 6) + 2, 0, ModRand.range(-6, 6) + 2);
                  int yVar = ModUtils.getSurfaceHeightGeneral(world, new BlockPos(randPosI.x, randPosI.y, randPosI.z), (int) this.posY - 5, (int) this.posY + 4);
                  if(yVar == this.posY) {
                      hand.setPosition(randPosI.x, yVar, randPosI.z);
                  } else {
                      hand.setPosition(randPosI.x, yVar + 1, randPosI.z);
                  }
                  this.world.spawnEntity(hand);
              }, i);
          }
      }, 50);

      addEvent(()-> {
        this.setExchange(false);
        this.setFightMode(false);
      }, 85);
    };

    private final Consumer<EntityLivingBase> cast_spell = (target) -> {
      this.setCastSpell(true);
      this.setFightMode(true);

      addEvent(()-> {
        this.setImmovable(true);
      }, 20);

      addEvent(()-> {
          for(int i = 0; i < 20; i += 5) {
              addEvent(()-> {
                  //shoot projectiles
                  ProjectileDarkMatter matter = new ProjectileDarkMatter(world, this, 9);
                  Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5,1.5,0)));
                  matter.setPosition(relPos.x, relPos.y, relPos.z);
                  matter.setTravelRange(20);
                  world.spawnEntity(matter);

                  Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(this, new Vec3d(0, -0.5, 0)));

                  Vec3d fromTargetTooActor = this.getPositionVector().subtract(targetPos);
                  Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                  Vec3d lineStart = targetPos.subtract(lineDir);
                  Vec3d lineEnd = targetPos.add(lineDir);

                  float speed = (float) 0.9;

                  ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, t) -> {
                      ModUtils.throwProjectileNoSpawn(pos,matter,0F, speed);
                  });

                  this.playSound(SoundsHandler.SHADOW_HAND_SUMMON, 0.75f, 0.9f / (rand.nextFloat() * 0.4f + 0.2f));

                  Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.3, 0)));
                  DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                  float damage = (this.getAttack());
                  ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.7f, 0, false);
              }, i);
          }
      }, 30);

      addEvent(()-> {
        this.setImmovable(false);
      }, 90);

      addEvent(()-> {
        this.setCastSpell(false);
        this.setFightMode(false);
      }, 110);
    };

    private final Consumer<EntityLivingBase> summon_minion = (target) -> {
        this.setSummonMinion(true);
        this.setFightMode(true);

        addEvent(()-> {
            this.playSound(SoundsHandler.DARK_SORCERER_SUMMON_MINION, 1.25f, 0.7f / (rand.nextFloat() * 0.4F + 0.7f));
            //summon minion and do particle effect
            Entity entityToo = Objects.requireNonNull(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(ModRand.choice(MobConfig.sorcerer_spawn_list)))).newInstance(world);
            if(entityToo instanceof EntityLivingBase) {
                EntityLivingBase spawn = ((EntityLivingBase) entityToo);
                Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.0, 0)));
                spawn.setPosition(relPos.x, relPos.y, relPos.z);
                if(this.isEntityAlive()) {
                    this.current_mobs.add(new WeakReference<>(spawn));
                }
                spawn.setRevengeTarget(target);
                world.spawnEntity(spawn);
            }

        }, 45);
        addEvent(()-> {
            this.setSummonMinion(false);
            this.setFightMode(false);
        }, 75);
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "model_controller", 0, this::predicateModelLocks));
        data.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateAttacks));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateModelLocks(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BASE_MODEL, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSummonMinion()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
                return PlayState.CONTINUE;
            }
            if(this.isCastSpell()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST, false));
                return PlayState.CONTINUE;
            }
            if(this.isExchange()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_EXCHANGE, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingDash()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DASH_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingMelee()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingContinue()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_CONTINUE, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingFinish()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_FINISH, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }



    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "dark_sorcerer");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.DARK_SORCERER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.DARK_SORCERER_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.DARK_SORCERER_IDLE;
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
