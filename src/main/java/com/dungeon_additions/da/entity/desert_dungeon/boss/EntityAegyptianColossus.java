package com.dungeon_additions.da.entity.desert_dungeon.boss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.ai.desert_dungeon.EntityAIAegyptianColossus;
import com.dungeon_additions.da.entity.desert_dungeon.boss.colossus.ActionColossusHiltSlam;
import com.dungeon_additions.da.entity.desert_dungeon.boss.colossus.ActionColossusMaceSlam;
import com.dungeon_additions.da.entity.desert_dungeon.boss.colossus.ActionMaceWave;
import com.dungeon_additions.da.entity.desert_dungeon.miniboss.ProjectileYellowWave;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.projectiles.puzzle.ProjectilePuzzleBall;
import com.dungeon_additions.da.entity.util.IEntitySound;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityAegyptianColossus extends EntitySharedDesertBoss implements IAnimatable, IAnimationTickable, IAttack, IScreenShake, IEntitySound {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;
    private int shakeTime = 0;
    Supplier<Projectile> yellow_wave_projectiles = () -> new ProjectileYellowWave(world, this, (float) this.getAttack(), null);
    private BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.NOTCHED_6));
    private int checkBossStatus = 60;
    //Post Phase Transition Ideas
    // Summon Melee Maces - SUmmons four Maces around the Colossus that do the melee sweep
    // Bring Forth Helper - SUmmons a small entity that constantly shoots projectiles at the player
    // Jump Slam - Colossus jumps then teleports onto the player causing a slam attack in that area
    // Mace Hilt Slam - Colossus slams the hilt of the mace into the ground causing a large wave of Yellow waves to spawn

    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STEP_SHAKE = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_FINISH = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_CONTINUE = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CALL_MACE = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MACE_SMASH = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MACE_SMASH_TWO = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_SLAM = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_MELEE_MACE = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HILT_SLAM = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_HELPER = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PHASE_TRANSITION = EntityDataManager.createKey(EntityAegyptianColossus.class, DataSerializers.BOOLEAN);

    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    public boolean isStepShake() {return this.dataManager.get(STEP_SHAKE);}
    private void setStepShake(boolean value) {this.dataManager.set(STEP_SHAKE, Boolean.valueOf(value));}
    public boolean isSummon() {return this.dataManager.get(SUMMON);}
    private void setSummon(boolean value) {this.dataManager.set(SUMMON, Boolean.valueOf(value));}
    public boolean isSwingAttack() {return this.dataManager.get(SWING);}
    private void setSwingAttack(boolean value) {this.dataManager.set(SWING, Boolean.valueOf(value));}
    public boolean isSwingContinue() {return this.dataManager.get(SWING_CONTINUE);}
    private void setSwingContinue(boolean value) {this.dataManager.set(SWING_CONTINUE, Boolean.valueOf(value));}
    public boolean isSwingFinish() {return this.dataManager.get(SWING_FINISH);}
    private void setSwingFinish(boolean value) {this.dataManager.set(SWING_FINISH, Boolean.valueOf(value));}
    public boolean isCallMace() {return this.dataManager.get(CALL_MACE);}
    private void setCallMace(boolean value) {this.dataManager.set(CALL_MACE, Boolean.valueOf(value));}
    public boolean isMaceSmash() {return this.dataManager.get(MACE_SMASH);}
    private void setMaceSmash(boolean value) {this.dataManager.set(MACE_SMASH, Boolean.valueOf(value));}
    public boolean isMaceSmashTwo() {return this.dataManager.get(MACE_SMASH_TWO);}
    private void setMaceSmashTwo(boolean value) {this.dataManager.set(MACE_SMASH_TWO, Boolean.valueOf(value));}
    public boolean isJumpSlam() {return this.dataManager.get(JUMP_SLAM);}
    private void setJumpSlam(boolean value) {this.dataManager.set(JUMP_SLAM, Boolean.valueOf(value));}
    public boolean isSummonMeleeMace() {return this.dataManager.get(SUMMON_MELEE_MACE);}
    private void setSummonMeleeMace(boolean value) {this.dataManager.set(SUMMON_MELEE_MACE, Boolean.valueOf(value));}
    public boolean isHiltSlam() {return this.dataManager.get(HILT_SLAM);}
    private void setHiltSlam(boolean value) {this.dataManager.set(HILT_SLAM, Boolean.valueOf(value));}
    public boolean isSummonHelper() {return this.dataManager.get(SUMMON_HELPER);}
    private void setSummonHelper(boolean value) {this.dataManager.set(SUMMON_HELPER, Boolean.valueOf(value));}
    public boolean isPhaseTransition() {return this.dataManager.get(PHASE_TRANSITION);}
    private void setPhaseTransition(boolean value) {this.dataManager.set(PHASE_TRANSITION, Boolean.valueOf(value));}

    private final String ANIM_IDLE_LOWER = "idle_lower";
    private final String ANIM_IDLE_UPPER = "idle_upper";
    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_SUMMON = "summon";

    //attacks
    private final String ANIM_SWING = "swing";
    private final String ANIM_SWING_CONTINUE = "swing_continue";
    private final String ANIM_SWING_FINISH = "swing_finish";
    private final String ANIM_MACE_SMASH = "mace_smash";
    private final String ANIM_MACE_SMASH_TWO = "mace_smash_two";
    private final String ANIM_CALL_MACE = "call_mace";

    //second phase
    private final String ANIM_SUMMON_HELPER = "summon_helper";
    private final String ANIM_SUMMON_MELEE_MACE = "summon_melee_maces";
    private final String ANIM_HILT_SLAM = "hilt_slam";
    private final String ANIM_JUMP_SLAM = "jump_slam";

    public EntityAegyptianColossus(World world, int timesUsed, BlockPos pos) {
        super(world);
        this.setSize(2.5F, 4.95F);
        this.startBossSetup();
        this.bossInfo.setVisible(false);
        this.timesUsed = timesUsed;
        this.iAmBossMob = true;
        this.experienceValue = 225;
        this.doBossReSummonScaling();
    }

    public EntityAegyptianColossus(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(2.5F, 4.95F);
        this.startBossSetup();
        this.bossInfo.setVisible(false);
        this.iAmBossMob = true;
        this.experienceValue = 225;
    }

    public EntityAegyptianColossus(World worldIn) {
        super(worldIn);
        this.setSize(2.5F, 4.95F);
        this.startBossSetup();
        this.bossInfo.setVisible(false);
        this.iAmBossMob = true;
        this.experienceValue = 225;
    }

    private void startBossSetup() {
        this.setSummon(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);
        this.lockLook = true;
        this.shakeTime = 80;
        addEvent(()-> this.setShaking(true), 10);

        addEvent(()-> this.setShaking(false), 80);

        addEvent(()-> {
            this.setSummon(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
            this.lockLook = false;
        }, 100);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Step_Shake", this.isStepShake());
        nbt.setBoolean("Shaking", this.isShaking());
        nbt.setBoolean("Summon", this.isSummon());
        nbt.setBoolean("Swing_Attack", this.isSwingAttack());
        nbt.setBoolean("Swing_Continue", this.isSwingContinue());
        nbt.setBoolean("Swing_Finish", this.isSwingFinish());
        nbt.setBoolean("Call_Mace", this.isCallMace());
        nbt.setBoolean("Mace_Smash", this.isMaceSmash());
        nbt.setBoolean("Mace_Smash_Two", this.isMaceSmashTwo());
        nbt.setBoolean("Jump_Slam", this.isJumpSlam());
        nbt.setBoolean("Summon_Melee_Mace", this.isSummonMeleeMace());
        nbt.setBoolean("Hilt_Slam", this.isHiltSlam());
        nbt.setBoolean("Summon_Helper", this.isSummonHelper());
        nbt.setBoolean("Phase_Transition", this.isPhaseTransition());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setStepShake(nbt.getBoolean("Step_Shake"));
        this.setShaking(nbt.getBoolean("Shaking"));
        this.setSummon(nbt.getBoolean("Summon"));
        this.setSwingAttack(nbt.getBoolean("Swing_Attack"));
        this.setSwingContinue(nbt.getBoolean("Swing_Continue"));
        this.setSwingFinish(nbt.getBoolean("Swing_Finish"));
        this.setCallMace(nbt.getBoolean("Call_Mace"));
        this.setMaceSmash(nbt.getBoolean("Mace_Smash"));
        this.setMaceSmashTwo(nbt.getBoolean("Mace_Smash_Two"));
        this.setJumpSlam(nbt.getBoolean("Jump_Slam"));
        this.setSummonMeleeMace(nbt.getBoolean("Summon_Melee_Mace"));
        this.setHiltSlam(nbt.getBoolean("Hilt_Slam"));
        this.setSummonHelper(nbt.getBoolean("Summon_Helper"));
        this.setPhaseTransition(nbt.getBoolean("Phase_Transition"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        this.dataManager.register(STEP_SHAKE, Boolean.valueOf(false));
        this.dataManager.register(SUMMON, Boolean.valueOf(false));
        this.dataManager.register(SWING, Boolean.valueOf(false));
        this.dataManager.register(SWING_CONTINUE, Boolean.valueOf(false));
        this.dataManager.register(SWING_FINISH, Boolean.valueOf(false));
        this.dataManager.register(CALL_MACE, Boolean.valueOf(false));
        this.dataManager.register(MACE_SMASH, Boolean.valueOf(false));
        this.dataManager.register(MACE_SMASH_TWO, Boolean.valueOf(false));
        this.dataManager.register(JUMP_SLAM, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_MELEE_MACE, Boolean.valueOf(false));
        this.dataManager.register(HILT_SLAM, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_HELPER, Boolean.valueOf(false));
        this.dataManager.register(PHASE_TRANSITION, Boolean.valueOf(false));
        super.entityInit();

    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.colossus_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.colossus_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.colossus_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.colossus_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
     //   this.tasks.addTask(4, new EntityAIAegyptiaWarlord<EntityAegyptianWarlord>(this, 1.0D, 20, 20, 0.2F));
        this.tasks.addTask(4, new EntityAIAegyptianColossus<EntityAegyptianColossus>(this, 1.0D, 40, 11, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    private boolean deleteNearbyBlocks = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.shakeTime--;
        this.checkBossStatus--;

        if(checkBossStatus <=0 && this.getOtherBoss() == null && this.isHasPhaseTransitioned()) {
            this.bossInfo.setVisible(true);
        }

        if(this.getOtherBoss() == null && this.bossInfo != null) {
            double healthFac = this.getHealth() / this.getMaxHealth();
            this.bossInfo.setPercent((float) healthFac);
        }

        if(!world.isRemote) {

            if(deleteNearbyBlocks) {
                if(this.onGround) {
                    deleteNearbyBlocks = false;
                }
                AxisAlignedBB box = getEntityBoundingBox().grow(1, 0.8, 1).offset(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.4, 0)));
                ModUtils.destroyBlocksInAABB(box, world, this);
            }

            if(this.getOtherBoss() != null) {
                if(this.getOtherBoss() instanceof EntityAegyptianWarlord) {
                    EntityAegyptianWarlord warlord = ((EntityAegyptianWarlord) this.getOtherBoss());


                    //Specific state for checking if the warlord is being targeted, it is calculated if the Warlord has been damaged a lot
                    //within a four second period of checking
                    if(warlord.isBeingPushed) {

                    }

                }
            }

            //preps for Phase Transition
            if(this.getOtherBoss() == null && this.isEnraged() && !this.isFightMode() && !this.isHasPhaseTransitioned() && !this.isPhaseTransition()) {
                this.doPhaseTransition();
            }
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(5, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Main.proxy.spawnParticle(14, this.posX + pos.x, this.posY + 2.5, this.posZ + pos.z, 0,0.025,0);
            });
        }
    }


    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode() && !this.isShielded() && !this.isSummon() && !this.isPhaseTransition()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(swing_attack, mace_slam, call_mace, hilt_slam, summon_melee_maces, summon_helper, jump_slam));
            double[] weights = {
                    (prevAttack != swing_attack && distance < 8) ? 1/distance : 0, //Double Swing Attack
                    (prevAttack != mace_slam && distance < 12) ? 1/distance : 0, //Mace Slam
                    (prevAttack != call_mace) ? distance * 0.02 : 0, //Call Mace Spell
                    (this.isHasPhaseTransitioned() && prevAttack != hilt_slam && distance > 4) ? distance * 0.02 : 0, //Hilt Slam Attack
                    (this.isHasPhaseTransitioned() && prevAttack != summon_melee_maces && distance < 12) ? 1/distance : 0, //Summon Melee Maces
                    (this.isHasPhaseTransitioned() && prevAttack != summon_helper) ? distance * 0.02 : 0, //Summon Helper Entity
                    (this.isHasPhaseTransitioned() && prevAttack != jump_slam && distance > 5) ? distance * 0.02 : 0 //Jump Slam Attack

            };
            prevAttack = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttack.accept(target);
        }
        return this.isHasPhaseTransitioned() ? 10 : this.isEnraged() ? 60 : 120;
    }

    private final Consumer<EntityLivingBase> jump_slam = (target) -> {
      this.setJumpSlam(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
      addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 15);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 30);
        addEvent(()-> this.playSound(SoundsHandler.DESERT_BOSS_TELEPORT, 1.4f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 30);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 100);

      addEvent(()-> {
        this.setImmovable(false);
        this.setNoGravity(true);
        this.setPosition(target.posX, target.posY + 4, target.posZ);
        this.deleteNearbyBlocks = true;
        this.setImmovable(true);
      }, 43);

      addEvent(()-> {
            this.lockLook = true;
          this.playSound(SoundsHandler.DESERT_BOSS_TELEPORT, 1.4f, 0.7f / (rand.nextFloat() * 0.2f + 0.2f));
            this.setNoGravity(false);
            this.setImmovable(false);
      }, 60);

      addEvent(()-> {
          Main.proxy.spawnParticle(20,world, this.posX, this.posY + 0.5, this.posZ, 0, 0, 0);
          this.playSound(SoundsHandler.VOLACTILE_SMASH, 1.5f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          new ActionColossusMaceSlam(12).performAction(this, target);
          this.setImmovable(true);
          this.setShaking(true);
          this.shakeTime = 15;
      }, 75);

      addEvent(()-> this.setShaking(false), 90);

      addEvent(()-> this.lockLook = false, 105);

      addEvent(()-> {
        this.setJumpSlam(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 115);
    };

    private final Consumer<EntityLivingBase> summon_helper = (target) -> {
      this.setSummonHelper(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.DESERT_BOSS_SUMMON_HELPER, 1.3f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 24);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 30);
      addEvent(()-> {
          //summon helper entity
          EntityColossusSigil sigil = new EntityColossusSigil(world, this, this.getAttack(), target);
          sigil.setPosition(this.posX, this.posY + 5.75, this.posZ);
          world.spawnEntity(sigil);
      }, 28);

      addEvent(()-> {
        this.setSummonHelper(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
      }, 50);
    };

    private final Consumer<EntityLivingBase> summon_melee_maces = (target) -> {
      this.setSummonMeleeMace(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_SWING, 1.3f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 27);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_MACE_SPELL, 1.4f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 50);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 50);
      addEvent(()-> this.lockLook = true, 20);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.8, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage =(float) (this.getAttack());
          ModUtils.handleAreaImpact(4f, (e) -> damage, this, offset, source, 0.9f, 0, false);
          Vec3d currPos = this.getPositionVector();
          ModUtils.circleCallback(5, 4, (pos)-> {
              pos = new Vec3d(pos.x, 0, pos.y);
              EntitySummonedMace cut_projectile = new EntitySummonedMace(world, true, this.getAttack(), this);
              cut_projectile.setPosition(currPos.x + pos.x, currPos.y + 0.5, currPos.z + pos.z);
              world.spawnEntity(cut_projectile);
          });
      }, 30);

      addEvent(()-> this.lockLook = false, 50);

      addEvent(()-> {
            this.setSummonMeleeMace(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
          Vec3d currPos = this.getPositionVector();
          ModUtils.circleCallback(11, 8, (pos)-> {
              pos = new Vec3d(pos.x, 0, pos.y);
              EntitySummonedMace cut_projectile = new EntitySummonedMace(world, true, this.getAttack(), this);
              cut_projectile.setPosition(currPos.x + pos.x, currPos.y + 0.5, currPos.z + pos.z);
              world.spawnEntity(cut_projectile);
          });
      }, 60);
    };

    private final Consumer<EntityLivingBase> hilt_slam = (target) -> {
      this.setHiltSlam(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 3);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 45);

      addEvent(()-> this.lockLook = true, 20);

      addEvent(()-> {
            //do hilt slam attack
          this.playSound(SoundsHandler.COLOSSUS_HILT_SLAM, 1.3f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
          new ActionColossusHiltSlam(yellow_wave_projectiles).performAction(this, target);
      }, 30);

      addEvent(()-> this.lockLook = false, 50);

      addEvent(()-> {
            this.setHiltSlam(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setImmovable(false);
      }, 60);
    };

    private final Consumer<EntityLivingBase> call_mace = (target) -> {
      this.setCallMace(true);
      this.setFightMode(true);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_MACE_SPELL, 1.4f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 30);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 45);

      addEvent(()-> {
        //call mace attack
        new ActionMaceWave().performAction(this, target);
      }, 40);

      addEvent(()-> {
            this.setCallMace(false);
            this.setFightMode(false);
      }, 60);
    };

    private final Consumer<EntityLivingBase> mace_slam = (target) -> {
      this.setMaceSmash(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_SWING_SLAM, 1.3f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 40);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 65);

      addEvent(()-> {
          this.lockLook = true;
      }, 20);

      addEvent(()-> {
          //mace smash attack
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(3, 1.8, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage =(float) (this.getAttack());
          ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.9f, 0, false);
          //do barrier wave
          Main.proxy.spawnParticle(20,world, offset.x, this.posY + 0.5, offset.z, 0, 0, 0);
            new ActionColossusMaceSlam(9).performAction(this, target);
            this.setShaking(true);
            this.shakeTime = 13;
      }, 50);

      addEvent(()-> {
          this.setShaking(false);
          this.lockLook = false;
      }, 65);

      addEvent(()-> {
          boolean randB = rand.nextBoolean();
          this.setMaceSmash(false);
          if(randB) {
              setMaceSLamTwice(target);
          } else {
              this.setFullBodyUsage(false);
              this.setFightMode(false);
              this.setImmovable(false);
          }
      }, 80);
    };

    private void setMaceSLamTwice(EntityLivingBase target) {
        mace_slam_two.accept(target);
    }


    private final Consumer<EntityLivingBase> mace_slam_two = (target) -> {
        this.setMaceSmashTwo(true);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 10);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_SWING_SLAM, 1.3f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 40);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 65);
        addEvent(()-> {
            this.lockLook = true;
        }, 20);

        addEvent(()-> {
            //mace smash attack
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(3, 1.8, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.9f, 0, false);
            //do barrier wave
            Main.proxy.spawnParticle(20,world, offset.x, this.posY + 0.5, offset.z, 0, 0, 0);
            new ActionColossusMaceSlam(15).performAction(this, target);
            this.setShaking(true);
            this.shakeTime = 13;
        }, 50);

        addEvent(()-> {
            this.setShaking(false);
            this.lockLook = false;
        }, 65);

        addEvent(()-> {
            this.setMaceSmashTwo(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setImmovable(false);
        }, 80);
    };

    private final Consumer<EntityLivingBase> swing_attack = (target) -> {
      this.setSwingAttack(true);
      this.setFightMode(true);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 5);
        addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_SWING, 1.3f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 34);

      addEvent(()-> {
          this.lockLook = true;
      }, 15);
      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.8, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage =(float) (this.getAttack());
          ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 0.9f, 0, false);
          this.setImmovable(true);
      }, 37);

      addEvent(()-> this.setImmovable(false),  45);

      addEvent(()-> {
          this.setSwingAttack(false);
          this.lockLook = false;
          boolean randBoolean = rand.nextBoolean();

          //continue with follow up
          if(this.getDistance(target) < 7 || randBoolean) {
            this.setSwingContinue(true);
              addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 7);
              addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_SWING, 1.3f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 28);
              addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 63);
            addEvent(()-> {
                this.lockLook= true;
                this.setImmovable(true);
            }, 25);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.8, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 0.9f, 0, false);
            }, 35);

            addEvent(()-> {
                this.lockLook = false;
                this.setImmovable(false);
            }, 63);

            addEvent(()-> {
                this.setFightMode(false);
                this.setSwingContinue(false);
            }, 75);


          } else {
           this.setSwingFinish(true);
              addEvent(()-> this.playSound(SoundsHandler.COLOSSUS_ARMOR, 1f, 0.7f / (rand.nextFloat() * 0.6f + 0.2f)), 3);

           addEvent(()-> {
               this.setFightMode(false);
               this.setSwingFinish(false);
           }, 15);
          }

      }, 50);
    };

    private void doPhaseTransition() {
        this.setPhaseTransition(true);
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.setImmovable(true);
        this.lockLook = true;
        this.playSound(SoundsHandler.DESERT_BOSS_TRANSITION, 1.5f, 0.7f / (rand.nextFloat() * 0.5f + 0.2f));
        addEvent(()-> {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            this.setHasPhaseTransitioned(true);
            this.heal((float) (this.getMaxHealth() * MobConfig.desert_bosses_second_phase_healing));
            this.bossInfo.setVisible(true);
            if(ModConfig.experimental_features && MobConfig.aegyptian_colossus_boss_music) {
                this.playMusic(this);
            }
        }, 45);

        addEvent(()-> this.lockLook = false, 60);

        addEvent(()-> {
            this.setPhaseTransition(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setImmovable(false);
        }, 70);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "arms_controller_idle", 0, this::predicateArmsIdle));
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
            if(this.isSwingAttack()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING));
                return PlayState.CONTINUE;
            }
            if(this.isSwingContinue()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_CONTINUE));
                return PlayState.CONTINUE;
            }
            if(this.isSwingFinish()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING_FINISH));
                return PlayState.CONTINUE;
            }
            if(this.isMaceSmash()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_MACE_SMASH));
                return PlayState.CONTINUE;
            }
            if(this.isMaceSmashTwo()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_MACE_SMASH_TWO));
                return PlayState.CONTINUE;
            }
            if(this.isCallMace()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CALL_MACE));
                return PlayState.CONTINUE;
            }
            if(this.isHiltSlam()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_HILT_SLAM));
                return PlayState.CONTINUE;
            }
            if(this.isJumpSlam()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_JUMP_SLAM));
                return PlayState.CONTINUE;
            }
            if(this.isSummonHelper()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SUMMON_HELPER));
                return PlayState.CONTINUE;
            }
            if(this.isSummonMeleeMace()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SUMMON_MELEE_MACE));
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
        if(this.isPhaseTransition()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_PHASE_TRANSITION));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.07F && event.getLimbSwingAmount() <= 0.07F && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArmsIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.07F && event.getLimbSwingAmount() <= 0.07F && !this.isFightMode() && !this.isSummon() && !this.isShielded() && !this.isPhaseTransition()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.08F && event.getLimbSwingAmount() <= 0.08F) && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.08F && event.getLimbSwingAmount() <= 0.08F) && !this.isFightMode() && !this.isSummon() && !this.isShielded() && !this.isPhaseTransition()) {
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
    public int tickTimer() {
        return this.ticksExisted;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.APATHYR_STEP, 0.5F, 0.8f + ModRand.getFloat(0.5F));
        this.setStepShake(true);
        this.shakeTime = 5;
        addEvent(() -> {
            this.setStepShake(false);
        }, 5);

    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 34.0F);
            if (dist >= 34.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.4F) * 1.1F * screamMult);
        } else if(this.isStepShake()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 10.0F);
            if (dist >= 10.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 0.22F * screamMult);
        }
        return 0;
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
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if(this.isPhaseTransition()) {
            return false;
        }

        if(this.isShielded() || this.isStartShielded()) {
            if(source.getImmediateSource() instanceof ProjectilePuzzleBall && this.getOtherBoss() != null) {
                return super.attackEntityFrom(source, 100);
            } else {
                this.playSound(SoundsHandler.OBSIDILITH_SHIELD, 0.75f, 0.8f + ModRand.getFloat(0.4f));
                return false;
            }
        }

        if(ModConfig.boss_cap_damage_enabled && amount > MobConfig.colossus_damage_cap) {
            return super.attackEntityFrom(source, MobConfig.colossus_damage_cap);
        }

        return super.attackEntityFrom(source, amount);
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "aegyptian_colossus");

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.COLOSSUS_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.COLOSSUS_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.DESERT_BOSS_DOWNED_DEATH;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return this.getOtherBoss() == null;
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
                this.createCoinSpawns(this.getPositionVector(), 0, ModRand.range(3, 5), 0);
            }

            if(this.getOtherBoss() == null) {
                this.setDropItemsWhenDead(true);
            } else {
                this.setDropItemsWhenDead(false);
            }


            super.onDeath(cause);
        }
    }

    @Nullable
    @Override
    public SoundEvent getBossMusic() {
        return SoundsHandler.COLOSSUS_TRACK;
    }
}
