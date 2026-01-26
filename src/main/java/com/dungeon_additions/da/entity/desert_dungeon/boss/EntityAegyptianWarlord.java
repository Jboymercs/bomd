package com.dungeon_additions.da.entity.desert_dungeon.boss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.desert_dungeon.EntityAIAegyptiaWarlord;
import com.dungeon_additions.da.entity.desert_dungeon.EntityDesertBase;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileThousandCuts;
import com.dungeon_additions.da.entity.desert_dungeon.boss.warlord.*;
import com.dungeon_additions.da.entity.desert_dungeon.miniboss.ProjectileYellowWave;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.projectiles.puzzle.ProjectilePuzzleBall;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
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
import java.util.function.Supplier;

public class EntityAegyptianWarlord extends EntitySharedDesertBoss implements IAnimatable, IAttack, IAnimationTickable {

    //Abilities to Add
    // Parry Attack - Warlord will parry the player if they attack with a melee weapon

    //Post Phase Transition Attacks
    // Thousand Blades - Warlord jumps towards the player for a slam then swings twice causing nearby thousand cuts to spawn
    // Thousand lines - Warlord stops and does a mega swing summoning a line of thousand cuts then a secondary line of thousand cuts in nearby positions
    // Fly Swing - Warlord flys in the air then shoots a barrage of projectiles at the player
    // Quick Circle Swing - Warlord dashes to the player and upon doing the attack a wave of 4 yellow waves spawn

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;
    public boolean isBeingPushed = false;
    private int spamAttackCounter = 0;
    private int checkSpamDelay = 80;
    private int parryCooldown = 100;
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
    private final String ANIM_PARRY = "parry";

    //second phase
    private final String ANIM_CIRCLE_SWING = "circle_swing";
    private final String ANIM_THOUSAND_SLICES = "thousand_slices";
    private final String ANIM_THOUSAND_SLICES_JUMP = "thousand_slices_jump";
    private final String ANIM_FLY_SWING = "fly_swing";

    //states
    private final String ANIM_SUMMON = "summon";


    private static final DataParameter<Boolean> DOUBLE_SWING = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DASH = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_STORM = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_AWAY = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TAIL_USAGE = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CIRCLE_SWING = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> THOUSAND_SLICES = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> THOUSAND_JUMP = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FLY_SWING = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PARRY = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PHASE_ANIM = EntityDataManager.createKey(EntityAegyptianWarlord.class, DataSerializers.BOOLEAN);

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
    public boolean isCircleSwing() {return this.dataManager.get(CIRCLE_SWING);}
    private void setCircleSwing(boolean value) {this.dataManager.set(CIRCLE_SWING, Boolean.valueOf(value));}
    public boolean isThousandSlices() {return this.dataManager.get(THOUSAND_SLICES);}
    private void setThousandSlices(boolean value) {this.dataManager.set(THOUSAND_SLICES, Boolean.valueOf(value));}
    public boolean isThousandJump() {return this.dataManager.get(THOUSAND_JUMP);}
    private void setThousandJump(boolean value) {this.dataManager.set(THOUSAND_JUMP, Boolean.valueOf(value));}
    public boolean isFlySwing() {return this.dataManager.get(FLY_SWING);}
    private void setFlySwing(boolean value) {this.dataManager.set(FLY_SWING, Boolean.valueOf(value));}
    public boolean isParry() {return this.dataManager.get(PARRY);}
    private void setParry(boolean value) {this.dataManager.set(PARRY, Boolean.valueOf(value));}
    public boolean isPhaseAnim() {return this.dataManager.get(PHASE_ANIM);}
    private void setPhaseAnim(boolean value) {this.dataManager.set(PHASE_ANIM, Boolean.valueOf(value));}

    Supplier<Projectile> yellow_wave_projectiles = () -> new ProjectileYellowWave(world, this, (float) this.getAttack(), null);

    public EntityAegyptianWarlord(World world, int timesUsed, BlockPos pos) {
        super(world, timesUsed, pos);
        this.setSize(0.7F, 2.45F);
        this.startSummonSetUp();
        this.bossInfo.setVisible(false);
        this.timesUsed = timesUsed;
        this.timesUsed++;
        this.iAmBossMob = true;
    }

    public EntityAegyptianWarlord(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 2.45F);
        this.startSummonSetUp();
        this.bossInfo.setVisible(false);
        this.iAmBossMob = true;
    }

    public EntityAegyptianWarlord(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 2.45F);
        this.startSummonSetUp();
        this.bossInfo.setVisible(false);
        this.iAmBossMob = true;
    }


    private void startSummonSetUp() {
        this.setSummon(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        addEvent(()-> {
            EntityAegyptianColossus colossus;
            if(this.timesUsed != 0) {
                colossus = new EntityAegyptianColossus(world, this.timesUsed, this.getPosition());
            } else {
                colossus = new EntityAegyptianColossus(world);
            }
            Vec3d pos = this.getPositionVector();
            colossus.setPosition(pos.x + 3, pos.y, pos.z);
            if(this.getSpawnLocation() != null && !this.isSetSpawnLoc()) {
                this.setSpawnLocation(new BlockPos(this.posX, this.posY, this.posZ));
                this.setSetSpawnLoc(true);
            }
            boolean flag = false;
            //other boss states
            if(!this.isHasPhaseTransitioned()) {
                if (this.getOtherBoss() == null && !world.isRemote && !this.findAndSetOtherBoss()) {
                    colossus.setOtherBossId(this.getUniqueID());
                    world.spawnEntity(colossus);
                    colossus.setSetSpawnLoc(true);
                    colossus.setSpawnLocation(new BlockPos(this.posX, this.posY, this.posZ));
                    this.setOtherBossId(colossus.getUniqueID());
                    flag = true;
                } else if (this.setOtherBossFromFinding() != null && !flag) {
                    EntityAegyptianColossus boss = this.setOtherBossFromFinding();
                    boss.setOtherBossId(null);
                    this.setOtherBossId(null);
                    boss.setOtherBossId(this.getUniqueID());
                    this.setOtherBossId(boss.getUniqueID());
                    if (!boss.isSetSpawnLoc() && boss.getSpawnLocation() != null) {
                        boss.setSpawnLocation(new BlockPos(this.posX, this.posY, this.posZ));
                        boss.setSetSpawnLoc(true);
                    }
                }
            }
        }, 5);

        addEvent(()-> {
            this.bossInfo.setVisible(true);
            if(!this.isHasPhaseTransitioned()) {
                this.bossInfo.setName(new TextComponentString("Aegyptian Royalty"));
            }
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
        nbt.setBoolean("Circle_Swing", this.isCircleSwing());
        nbt.setBoolean("Thousand_Slices", this.isThousandSlices());
        nbt.setBoolean("Thousand_Jump", this.isThousandJump());
        nbt.setBoolean("Fly_Swing", this.isFlySwing());
        nbt.setBoolean("Parry", this.isParry());
        nbt.setBoolean("Phase_Anim", this.isPhaseAnim());
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
        this.setCircleSwing(nbt.getBoolean("Circle_Swing"));
        this.setThousandSlices(nbt.getBoolean("Thousand_Slices"));
        this.setThousandJump(nbt.getBoolean("Thousand_Jump"));
        this.setFlySwing(nbt.getBoolean("Fly_Swing"));
        this.setParry(nbt.getBoolean("Parry"));
        this.setPhaseAnim(nbt.getBoolean("Phase_Anim"));
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
        this.dataManager.register(CIRCLE_SWING, Boolean.valueOf(false));
        this.dataManager.register(THOUSAND_JUMP, Boolean.valueOf(false));
        this.dataManager.register(THOUSAND_SLICES, Boolean.valueOf(false));
        this.dataManager.register(PHASE_ANIM, Boolean.valueOf(false));
        this.dataManager.register(PARRY, Boolean.valueOf(false));
        this.dataManager.register(FLY_SWING, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.warlord_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.38D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.warlord_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.warlord_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.warlord_armor_toughness);
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

        this.parryCooldown--;
        if(!world.isRemote) {

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

            if(this.getOtherBoss() == null && this.bossInfo != null) {
                double healthFac = this.getHealth() / this.getMaxHealth();
                this.bossInfo.setPercent((float) healthFac);
            }

            if(this.onGround && this.isJumping) {
                this.isJumping = false;
            }

            //preps for Phase Transition
            if(this.getOtherBoss() == null && this.isEnraged() && !this.isFightMode() && !this.isHasPhaseTransitioned() && !this.isPhaseAnim()) {
                this.doPhaseTransition();
            }
        }
    }

    private void doPhaseTransition() {
        this.setPhaseAnim(true);
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.setImmovable(true);

        addEvent(()-> {
            this.setHasPhaseTransitioned(true);
            this.heal((float) (this.getMaxHealth() * MobConfig.desert_bosses_second_phase_healing));
            this.bossInfo.setName(this.getDisplayName());
        }, 45);

        addEvent(()-> {
            this.setPhaseAnim(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setImmovable(false);
        }, 70);
    }


    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);

        if(!this.isFightMode() && !this.isSummon() && !this.isShielded()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(double_swing, jump_attack, summon_storm, dash_attack, circle_swing, thousand_slices, thousand_slices_jump, fly_swing));
            double[] weights = {
                    (prevAttack != double_swing && distance < 6) ? 1/distance : 0, //Double Swing Attack
                    (prevAttack != jump_attack && distance < 12) ? 1/distance : 0, //Jump Attack
                    (prevAttack != summon_storm) ? distance * 0.02 : 0, //Summon Storms Attack
                    (prevAttack != dash_attack && distance > 5) ? distance * 0.02 : 0, //Dash Attack
                    (this.isHasPhaseTransitioned() && distance < 14 && prevAttack != circle_swing) ? 1/distance : 0, //Circle Swing
                    (this.isHasPhaseTransitioned() && prevAttack != thousand_slices) ? distance * 0.02 : 0, //Thousand Slices
                    (this.isHasPhaseTransitioned() && prevAttack != thousand_slices_jump) ? 1/distance : 0, //Thousand Slices Jump
                    (this.isHasPhaseTransitioned() && prevAttack != fly_swing) ? distance * 0.02 : 0 //Fly Swing Attack
            };
            prevAttack = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttack.accept(target);
        }
        return this.isHasPhaseTransitioned() ? 15 : this.isEnraged() ? 30 : 60;
    }

    private final Consumer<EntityLivingBase> fly_swing = (target) -> {
      this.setFlySwing(true);
      this.setImmovable(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);

      addEvent(()-> {
        this.setNoGravity(true);
        this.setImmovable(false);
        if(this.getSpawnLocation() != null) {
            this.setPosition(this.getSpawnLocation().getX(), this.getSpawnLocation().getY() + 4, this.getSpawnLocation().getZ());
            this.setImmovable(true);
        } else {
            this.setPosition(this.posX, this.posY + 4, this.posZ);
            this.setImmovable(true);
        }
      }, 23);

      addEvent(()-> {
          //wave projectiles one
          new ActionWarlordProjectileSplay(true).performAction(this, target);
      }, 35);

      addEvent(()-> {
          //wave projectile two
          new ActionWarlordProjectileSplay(false).performAction(this, target);
      }, 62);

      addEvent(()-> {
         //wave projectile three
          ProjectileThousandCuts cuts_projectile = new ProjectileThousandCuts(world, this, this.getAttack());
          cuts_projectile.setPosition(this.posX, this.posY - 3, this.posZ);
          world.spawnEntity(cuts_projectile);
          new ActionWarlordCutsProjectileSplay().performAction(this, target);
      }, 110);

      addEvent(()-> {
          this.setNoGravity(false);
          this.setImmovable(false);
      }, 130);

      addEvent(()-> {
        this.setFlySwing(false);
        this.setImmovable(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
      }, 135);
    };

    private final Consumer<EntityLivingBase> thousand_slices_jump = (target) -> {
      this.setThousandJump(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);

      addEvent(()-> {
          this.setImmovable(false);
          this.setPosition(this.posX, this.posY + 4, this.posZ);
          this.setImmovable(true);
      }, 25);

      addEvent(()-> {
            this.setImmovable(false);
            this.setPosition(target.posX, target.posY, target.posZ);
            this.setImmovable(true);
            this.lockLook = true;
      }, 60);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage =(float) (this.getAttack() * 1.25);
          ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 0.4f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          addEvent(() -> {
                //Do Cuts Projectile in a circle like manner
              Vec3d currPos = this.getPositionVector();
              ModUtils.circleCallback(5, 6, (pos)-> {
                  pos = new Vec3d(pos.x, 0, pos.y);
                  ProjectileThousandCuts cut_projectile = new ProjectileThousandCuts(world, this, this.getAttack());
                  cut_projectile.setPosition(currPos.x + pos.x, currPos.y + 2, currPos.z + pos.z);
                  world.spawnEntity(cut_projectile);
              });
          }, 10);
      }, 85);

      addEvent(()-> this.lockLook = false, 105);

      addEvent(()-> {
        this.setThousandJump(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 120);
    };

    private final Consumer<EntityLivingBase> thousand_slices = (target) -> {
      this.setThousandSlices(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);

      addEvent(()-> {
            this.lockLook = true;
      }, 30);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage =(float) (this.getAttack() * 1.25);
          ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.7f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          //do cuts in a line like manner
          new ActionWarlordCutsLine().performAction(this, target);
          addEvent(()-> {
              //do secondary lines
          }, 7);
      }, 40);

      addEvent(()-> this.lockLook = false, 70);

      addEvent(()-> {
        this.setThousandSlices(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 95);
    };

    private final Consumer<EntityLivingBase> circle_swing = (target) -> {
      this.setCircleSwing(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);

      addEvent(()-> {
          this.setImmovable(true);
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
          this.lockLook = true;
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.35),0.1F);
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
              float damage =(float) (this.getAttack() * 1.25);
              ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.9f, 0, false);
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          }, 5);
      }, 15);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage =(float) (this.getAttack() * 1.25);
          ModUtils.handleAreaImpact(3f, (e) -> damage, this, offset, source, 0.9f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          this.setImmovable(true);
          //do wave action
          new ActionWarlordCircleSwing(yellow_wave_projectiles).performAction(this, target);
      }, 35);

      addEvent(()-> this.lockLook = false, 60);

      addEvent(() -> {
        this.setCircleSwing(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 75);
    };

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

    protected boolean findAndSetOtherBoss() {
        List<EntityAegyptianColossus> nearbyBoss = this.world.getEntitiesWithinAABB(EntityAegyptianColossus.class, this.getEntityBoundingBox().grow(50D), e -> !e.getIsInvulnerable());
        if(!nearbyBoss.isEmpty()) {
                return true;
        }
        return false;
    }

    private EntityAegyptianColossus setOtherBossFromFinding() {
        List<EntityAegyptianColossus> nearbyBoss = this.world.getEntitiesWithinAABB(EntityAegyptianColossus.class, this.getEntityBoundingBox().grow(50D), e -> !e.getIsInvulnerable());
        if(!nearbyBoss.isEmpty()) {
            for(EntityAegyptianColossus boss : nearbyBoss) {
                return boss;
            }
        }
        return null;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "arms_controller_idle", 0, this::predicateArmsIdle));
        data.addAnimationController(new AnimationController(this, "tail_controller", 0, this::predicateIdleTail));
        data.addAnimationController(new AnimationController(this, "attacks_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "states_controller", 0, this::predicateStates));
        data.addAnimationController(new AnimationController(this, "shielded_controller", 0, this::predicateShielded));
    }

    private <E extends IAnimatable> PlayState predicateShielded(AnimationEvent<E> event) {
        if(this.isShielded()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHIELDED, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
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
            if(this.isParry()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PARRY));
                return PlayState.CONTINUE;
            }
            if(this.isFlySwing()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_FLY_SWING));
                return PlayState.CONTINUE;
            }
            if(this.isThousandJump()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_THOUSAND_SLICES_JUMP));
                return PlayState.CONTINUE;
            }
            if(this.isThousandSlices()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_THOUSAND_SLICES));
                return PlayState.CONTINUE;
            }
            if(this.isCircleSwing()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CIRCLE_SWING));
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
        if(this.isStartShielded()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SET_SHIELDED));
            return PlayState.CONTINUE;
        }
        if(this.isEndShielded()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_END_SHIELDED));
            return PlayState.CONTINUE;
        }
        if(this.isPhaseAnim()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PHASE_TRANSITION));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }


    private<E extends IAnimatable> PlayState predicateIdleTail(AnimationEvent<E> event) {
        if(!this.isTailUsage() && !this.isShielded() && !this.isThousandSlices() && !this.isThousandJump() && !this.isFlySwing() && !this.isPhaseAnim())  {
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
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode() && !this.isSummon() && !this.isShielded() && !this.isPhaseAnim()) {
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
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode() && !this.isSummon() && !this.isShielded() && !this.isPhaseAnim()) {
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

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if(this.isPhaseAnim() || this.isParry()) {
            return false;
        }

        if(!source.isProjectile() && !source.isUnblockable && parryCooldown <= 0 && !this.isParry() && !this.isFightMode()) {
            if(source.getImmediateSource() instanceof EntityLivingBase) {
                EntityLivingBase base = ((EntityLivingBase) source.getImmediateSource());
                if(!(base instanceof EntityDesertBase)) {
                    this.doParryAttack(base);
                    return false;
                }
            }
        }

        if(this.isShielded() || this.isStartShielded()) {
            if(source.getImmediateSource() instanceof ProjectilePuzzleBall && this.getOtherBoss() != null) {
                return super.attackEntityFrom(source, 100);
            } else {
                this.playSound(SoundsHandler.OBSIDILITH_SHIELD, 0.75f, 0.8f + ModRand.getFloat(0.4f));
                return false;
            }
        }

        if(ModConfig.boss_cap_damage_enabled && amount > MobConfig.warlord_damage_cap) {
            return super.attackEntityFrom(source, MobConfig.warlord_damage_cap);
        }

        return super.attackEntityFrom(source, amount);
    }

    private void doParryAttack(EntityLivingBase target) {
        this.setParry(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.playSound(SoundsHandler.IMPERIAL_SWORD_PARRY, 1f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 50, 2, false, false));
        //incase theres multiple players the entity will target the entity that activated the parry during this attack
        this.setAttackTarget(target);

        addEvent(()-> {
            this.lockLook = true;
            this.setImmovable(false);
            Vec3d targetedPos = target.getPositionVector();
            double distance = this.getPositionVector().distanceTo(targetedPos);
            ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.2),0.1F);
        }, 36);
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.9f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            this.setImmovable(true);
        }, 41);

        addEvent(()-> this.lockLook = false, 55);

        addEvent(()-> {
            this.setParry(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            this.parryCooldown = 100;
        }, 60);
    }

    @Override
    public void onDeath(DamageSource cause) {
        if(this.getOtherBoss() != null && !this.inLowHealthState) {
            this.setHealth(0.00001F);
            this.inLowHealthState = true;
            this.setLowHealthState();
        } else if (this.inLowHealthState && this.isShielded() || this.getOtherBoss() == null) {
            if(this.getSpawnLocation() != null && this.getOtherBoss() == null) {
                this.turnBossIntoSummonSpawner(this.getSpawnLocation());
            }
            super.onDeath(cause);
        }
    }
}
