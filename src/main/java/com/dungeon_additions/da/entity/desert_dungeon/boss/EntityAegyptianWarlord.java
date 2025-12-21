package com.dungeon_additions.da.entity.desert_dungeon.boss;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.desert_dungeon.EntityAIAegyptiaWarlord;
import com.dungeon_additions.da.entity.ai.desert_dungeon.EntityScutterBeetleAI;
import com.dungeon_additions.da.entity.desert_dungeon.boss.warlord.ActionWarlordSummonStorms;
import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.ActionApathyrSwing;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityAegyptianWarlord extends EntitySharedDesertBoss implements IAnimatable, IAttack, IAnimationTickable {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;
    public boolean isBeingPushed = false;
    private int spamAttackCounter = 0;
    private int checkSpamDelay = 80;
    public boolean isJumping = false;
    private BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.NOTCHED_6));

    private final String ANIM_WALK = "walk_upper";
    private final String ANIM_IDLE = "idle_upper";
    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_IDLE_LOWER = "idle_lower";
    private final String ANIM_TAIL_IDLE = "idle_tail";
    private final String ANIM_TAIL_WALK = "walk_tail";

    //attacks
    private final String ANIM_DOUBLE_SWING = "double_swing";
    private final String ANIM_DASH = "dash";
    private final String ANIM_SUMMON_STORM = "summon_storm";
    private final String ANIM_JUMP = "jump";

    //states
    private final String ANIM_SUMMON = "summon";


    private static final DataParameter<Boolean> DOUBLE_SWING = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DASH = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_STORM = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_AWAY = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TAIL_USAGE = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);

    public boolean isDoubleSwing() {return this.dataManager.get(DOUBLE_SWING);}
    private void setDoubleSwing(boolean value) {this.dataManager.set(DOUBLE_SWING, Boolean.valueOf(value));}
    public boolean isDashAttack() {return this.dataManager.get(DASH);}
    private void setDashAttack(boolean value) {this.dataManager.set(DASH, Boolean.valueOf(value));}
    public boolean isSummonStorm() {return this.dataManager.get(SUMMON_STORM);}
    private void setSummonStorm(boolean value) {this.dataManager.set(SUMMON_STORM, Boolean.valueOf(value));}
    public boolean isJumpAway() {return this.dataManager.get(JUMP_AWAY);}
    private void setJumpAway(boolean value) {this.dataManager.set(JUMP_AWAY, Boolean.valueOf(value));}
    public boolean isTailUsage() {return this.dataManager.get(TAIL_USAGE);}
    private void setTailUsage(boolean value) {this.dataManager.set(TAIL_USAGE, Boolean.valueOf(value));}
    public boolean isSummon() {return this.dataManager.get(SUMMON);}
    private void setSummon(boolean value) {this.dataManager.set(SUMMON, Boolean.valueOf(value));}

    public EntityAegyptianWarlord(World world, int timesUsed, BlockPos pos) {
        super(world, timesUsed, pos);
        this.setSize(0.7F, 2.45F);
        this.startSummonSetUp();
        this.bossInfo.setVisible(false);
    }

    public EntityAegyptianWarlord(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 2.45F);
        this.startSummonSetUp();
        this.bossInfo.setVisible(false);
    }

    public EntityAegyptianWarlord(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 2.45F);
        this.startSummonSetUp();
        this.bossInfo.setVisible(false);
    }


    private void startSummonSetUp() {
        this.setSummon(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        addEvent(()-> {
            EntityAegyptianColossus colossus = new EntityAegyptianColossus(world);
            Vec3d pos = this.getPositionVector();
            colossus.setPosition(pos.x + 3, pos.y, pos.z);
            //other boss states
            if(this.getOtherBoss() == null && !world.isRemote) {
                colossus.setOtherBossId(this.getUniqueID());
                world.spawnEntity(colossus);
                this.setOtherBossId(colossus.getUniqueID());
            }
        }, 5);

        addEvent(()-> {
            this.bossInfo.setVisible(true);
            this.bossInfo.setName(new TextComponentString("Aegyptian Royalty"));
            this.setSummon(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
        }, 80);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Double_Swing",this.isDoubleSwing());
        nbt.setBoolean("Dash_Attack", this.isDashAttack());
        nbt.setBoolean("Summon_Storm", this.isSummonStorm());
        nbt.setBoolean("Jump_Away", this.isJumpAway());
        nbt.setBoolean("Tail_Usage", this.isTailUsage());
        nbt.setBoolean("Summon", this.isSummon());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setDoubleSwing(nbt.getBoolean("Double_Swing"));
        this.setDashAttack(nbt.getBoolean("Dash_Attack"));
        this.setSummonStorm(nbt.getBoolean("Summon_Storm"));
        this.setJumpAway(nbt.getBoolean("Jump_Away"));
        this.setTailUsage(nbt.getBoolean("Tail_Usage"));
        this.setSummon(nbt.getBoolean("Summon"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        super.readEntityFromNBT(nbt);
    }


    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(DOUBLE_SWING, Boolean.valueOf(false));
        this.dataManager.register(DASH, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_STORM, Boolean.valueOf(false));
        this.dataManager.register(JUMP_AWAY, Boolean.valueOf(false));
        this.dataManager.register(TAIL_USAGE, Boolean.valueOf(false));
        this.dataManager.register(SUMMON, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(17);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.38D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(16);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(4);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.75D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIAegyptiaWarlord<EntityAegyptianWarlord>(this, 1.0D, 20, 20, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!world.isRemote) {

            if(checkSpamDelay > 0) {
                if(this.hurtTime > 0) {
                    this.spamAttackCounter++;
                }

                if(this.spamAttackCounter > 20) {
                    this.isBeingPushed = true;
                }
            } else if (checkSpamDelay < 1){
                this.checkSpamDelay = 80;
                this.spamAttackCounter = 0;
                if(this.getOtherBoss() == null) {
                    this.isBeingPushed = false;
                }
            }
            //other boss stuff
            if(this.getOtherBoss() != null) {
                if(this.getOtherBoss() instanceof EntityAegyptianColossus) {
                    EntityAegyptianColossus colossus = ((EntityAegyptianColossus) this.getOtherBoss());

                    //shared Health
                    if(this.bossInfo != null) {
                        double combinedMaxHealth = this.getMaxHealth() + colossus.getMaxHealth();
                        double combinedCurrentHealth = this.getHealth() + colossus.getHealth();
                        double healthFac = combinedCurrentHealth / combinedMaxHealth;
                        this.bossInfo.setPercent((float) healthFac);
                    }


                }
            }

            if(this.onGround && this.isJumping) {
                this.isJumping = false;
            }
        }
    }


    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double healtFac = this.getHealth()/this.getMaxHealth();

        if(!this.isFightMode() && !this.isSummon()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(double_swing, jump_attack, summon_storm, dash_attack));
            double[] weights = {
                    (prevAttack != double_swing && distance < 6) ? 1/distance : 0, //Double Swing Attack
                    (prevAttack != jump_attack && distance < 12) ? 1/distance : 0, //Jump Attack
                    (prevAttack != summon_storm) ? distance * 0.02 : 0, //Summon Storms Attack
                    (prevAttack != dash_attack && distance > 5) ? distance * 0.02 : 0 //Dash Attack
            };
            prevAttack = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttack.accept(target);
        }
        return 20;
    }

    private final Consumer<EntityLivingBase> dash_attack = (target) -> {
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setDashAttack(true);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-5));
            addEvent(()-> {
                this.setImmovable(false);
                ModUtils.attemptTeleport(targetedPos, this);
                this.playSound(SoundsHandler.B_KNIGHT_PREPARE, 1.75f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 2);
        }, 17);

        addEvent(()-> {
            this.setImmovable(true);
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(3));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.35),0.1F);
            }, 5);
        }, 20);

        addEvent(()-> {
            //do stoms action
            for(int i = 0; i < 15; i += 5) {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage =(float) (this.getAttack() * 1.25);
                ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.9f, 0, false);
            }
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 30);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 45);

      addEvent(()-> {
          this.setImmovable(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setDashAttack(false);
      }, 60);
    };

    private final Consumer<EntityLivingBase> jump_attack = (target) -> {
        this.setFightMode(true);
        this.setImmovable(true);
        this.setFullBodyUsage(true);
        this.setJumpAway(true);

        addEvent(()-> {
            this.setImmovable(false);
            this.lockLook = true;
            this.isJumping = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d randPosToo = target.getPositionVector().add(ModRand.randVec()).scale(14);
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-3));
            ModUtils.leapTowards(this, targetedPos.add(randPosToo), (float) 1.5F,1.9F);
        }, 10);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setJumpAway(false);
        }, 35);
    };

    private final Consumer<EntityLivingBase> summon_storm = (target) -> {
      this.setFightMode(true);
      this.setSummonStorm(true);

      addEvent(()-> {
          this.lockLook = true;
      }, 35);

      addEvent(()-> {
        //do stoms action
          new ActionWarlordSummonStorms().performAction(this, target);
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage =(float) (this.getAttack());
          ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.6f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          this.setImmovable(true);
      }, 40);

      addEvent(()-> {
          this.lockLook = false;
      }, 45);

      addEvent(()-> {
            this.setFightMode(false);
            this.setSummonStorm(false);
      }, 50);
    };

    private final Consumer<EntityLivingBase> double_swing = (target) -> {
      this.setFightMode(true);
      this.setDoubleSwing(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(0));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.25),0.1F);
            }, 5);
        }, 11);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.6f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            this.setImmovable(true);
        }, 21);

        addEvent(()-> this.lockLook = false, 25);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(0));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.25),0.1F);
            }, 5);
        }, 25);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.6f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            this.setImmovable(true);
        }, 35);

        addEvent(()-> {
            this.setImmovable(false);
            this.lockLook = false;
        }, 45);

        addEvent(()-> {
            this.setFightMode(false);
            this.setDoubleSwing(false);
        }, 50);
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "arms_controller_idle", 0, this::predicateArmsIdle));
        data.addAnimationController(new AnimationController(this, "tail_controller", 0, this::predicateIdleTail));
        data.addAnimationController(new AnimationController(this, "attacks_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "states_controller", 0, this::predicateStates));
    }

    private<E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isDoubleSwing()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_DOUBLE_SWING));
                return PlayState.CONTINUE;
            }
            if(this.isDashAttack()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_DASH));
                return PlayState.CONTINUE;
            }
            if(this.isJumpAway()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_JUMP));
                return PlayState.CONTINUE;
            }
            if(this.isSummonStorm()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SUMMON_STORM));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateStates(AnimationEvent<E> event) {
        if(this.isSummon()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SUMMON));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }


    private<E extends IAnimatable> PlayState predicateIdleTail(AnimationEvent<E> event) {
        if(!this.isTailUsage())  {
            if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_TAIL_WALK, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_TAIL_IDLE, true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArmsIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode() && !this.isSummon()) {
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
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode() && !this.isSummon()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
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
