package com.dungeon_additions.da.entity.flame_knight;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.EntityTimedAttackIncendium;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
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
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityIncendium extends EntityFlameBase implements IAttack {

    private Consumer<EntityLivingBase> prevAttacks;
    private final String ANIM_IDLE = "idle";
    private final String ANIM_SHIELD_IDLE = "shield_idle";
    private final String ANIM_DEFENSE_IDLE = "defense_idle";

    //Attacks
    private final String ANIM_MAGMA_STOMP = "magma_slam";
    private final String ANIM_SWING_LEFT = "swingLeft";
    private final String ANIM_HEAVY_SWING = "heavy_swing";

    private final String ANIM_SWING_RIGHT = "swingRight";
    private final String ANIM_SWING_RIGHT_FINISH = "swingRight_finish";
    private final String ANIM_SWING_RIGHT_CONTINUE = "swingRight_continue";

    private final String ANIM_ENTER_DEFENSE_MODE = "go_to_defense";
    private final String ANIM_END_DEFENSE_MODE = "leave_defense";

    private static final DataParameter<Boolean> DEFENSE_MODE = EntityDataManager.createKey(EntityIncendium.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ENTER_DEFENSE = EntityDataManager.createKey(EntityIncendium.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_DEFENSE = EntityDataManager.createKey(EntityIncendium.class, DataSerializers.BOOLEAN);

    public boolean isDefenseMode() {
        return this.dataManager.get(DEFENSE_MODE);
    }

    private void setDefenseMode(boolean value) {
        this.dataManager.set(DEFENSE_MODE, Boolean.valueOf(value));
    }

    private boolean isEnterDefense() {
        return this.dataManager.get(ENTER_DEFENSE);
    }

    private void setEnterDefense(boolean value) {
        this.dataManager.set(ENTER_DEFENSE, Boolean.valueOf(value));
    }

    private boolean isEndDefense() {
        return this.dataManager.get(END_DEFENSE);
    }

    private void setEndDefense(boolean value) {
        this.dataManager.set(END_DEFENSE, Boolean.valueOf(value));
    }

    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);
    private final MoveAnalysis moveAnalysis;

    public EntityIncendium(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(0.9f, 1.95f);
        this.setImmovable(false);
        this.setDefenseMode(false);
        this.experienceValue = 25;
        this.falter_resistance = 1.0F;
        this.hemorrhage_resistance = 0.2F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public EntityIncendium(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(0.9f, 1.95f);
        this.setImmovable(false);
        this.setDefenseMode(false);
        this.experienceValue = 25;
        this.falter_resistance = 1.0F;
        this.hemorrhage_resistance = 0.2F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(DEFENSE_MODE, Boolean.valueOf(false));
        this.dataManager.register(ENTER_DEFENSE, Boolean.valueOf(false));
        this.dataManager.register(END_DEFENSE, Boolean.valueOf(false));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Defense_Mode", this.isDefenseMode());
        nbt.setBoolean("Enter_Defense", this.isEnterDefense());
        nbt.setBoolean("End_Defense", this.isEndDefense());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setDefenseMode(nbt.getBoolean("Defense_Mode"));
        this.setEnterDefense(nbt.getBoolean("Enter_Defense"));
        this.setEndDefense(nbt.getBoolean("End_Defense"));
        super.readEntityFromNBT(nbt);
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.incendium_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.incendium_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.incendium_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(4D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityTimedAttackIncendium<>(this, 1.1, 20, 5F, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPigZombie>(this, EntityPigZombie.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    private int attackCounter = ModRand.range(1, 5);
    protected boolean markForChange = false;

    private void enterDefensiveMode() {
        this.setEnterDefense(true);
        this.setFightMode(true);
        animator.performAttack(ANIM_ENTER_DEFENSE_MODE);
        this.setImmovable(true);
        this.setFullBodyUsage(true);
        this.setNoGravity(true);

        addEvent(() -> {
            this.setEnterDefense(false);
            this.setFightMode(false);
            this.setDefenseMode(true);
            this.setImmovable(false);
            this.setFullBodyUsage(false);
        }, 35);
    }

    private void endDefensiveMode() {
        this.setDefenseMode(false);
        this.setFightMode(true);
        this.setEndDefense(true);
        animator.performAttack(ANIM_END_DEFENSE_MODE);
        this.setImmovable(true);
        this.setFullBodyUsage(true);
        this.setNoGravity(false);
        this.motionY = 0;

        addEvent(() -> {
            this.setFightMode(false);
            this.setEndDefense(false);
            this.setImmovable(false);
            this.setFullBodyUsage(false);
        }, 30);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == ModUtils.PARTICLE_BYTE) {
            Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(ModRand.range(-2, 2), ModRand.range(1, 2), ModRand.range(-2, 2))));
            Vec3d vel = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) / 3, 0.1, (world.rand.nextFloat() - world.rand.nextFloat()) / 3);
            world.spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, ModRand.range(40, 50));
        }
        if (id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(3, 25, (pos) -> {
                pos = new Vec3d(pos.x, 0, pos.y);
                world.spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y, pos.z, 0, 0.05, 0, ModRand.range(40, 50));
            });
        }
        super.handleStatusUpdate(id);
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if (this.particleUpdate && world.rand.nextInt(2) == 0) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.moveAnalysis.update();
        if (!world.isRemote) {
            EntityLivingBase target = this.getAttackTarget();

            if (target != null) {

                if (markForChange && !this.isFightMode()) {
                    if (this.isDefenseMode()) {
                        this.endDefensiveMode();
                        this.attackCounter = ModRand.range(2, 5);
                    } else {
                        this.enterDefensiveMode();
                        this.attackCounter = ModRand.range(1, 3);
                    }
                    this.markForChange = false;
                }
            }

            if (this.isDefenseMode()) {
                this.fallDistance = 0;
                if (!this.world.isAirBlock(this.getPosition().add(0, -1, 0))) {
                    this.motionY = 0.1;
                } else if (this.world.isAirBlock(this.getPosition().add(0, -3, 0))) {
                    this.motionY = -0.1;
                } else {
                    this.motionY = 0;
                    if (world.isAirBlock(this.getPosition().add(0, -1, 0))) {
                        world.setBlockState(this.getPosition().add(0, -1, 0), Blocks.FIRE.getDefaultState());
                    }
                }
            }
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode() && !this.markForChange) {
            if(!this.isDefenseMode()) {
                List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(magma_stomp_attack, swing_Left, swing_Right, heavy_Swing));
                double[] weights = {
                        (prevAttacks != magma_stomp_attack) ?  1/distance : 0,
                        (prevAttacks != swing_Left) ?  1/distance : 0,
                        (prevAttacks != swing_Right) ?  1/distance : 0,
                        (prevAttacks != heavy_Swing) ?  1/distance : 0
                };
                prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
                prevAttacks.accept(target);
            } else {

                defense_attack.accept(target);
            }
        }
        return 10;
    }

    private boolean particleUpdate = false;

    private final Consumer<EntityLivingBase> defense_attack = (target) -> {
      this.setFightMode(true);
      this.setImmovable(true);
      this.particleUpdate = true;
      //setParticle Byte
        attackCounter--;
        if(attackCounter <= 0) {
            markForChange = true;
        }

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(3));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.18),0.2F);
                this.particleUpdate = false;
                world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            }, 5);
        }, 20);

        addEvent(()-> {
            for(int i = 0; i <= 16; i += 4) {
                addEvent(()-> {
                    this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.1, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = (float) (this.getAttack());
                    ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.6f, 0, false, 0.2F);
                    new ActionTileAOE(3).performAction(this, target);
                }, i);
            }
        }, 22);

        addEvent(()-> {
            this.setImmovable(true);
        }, 45);

        addEvent(()-> {
            this.lockLook = false;
        }, 70);
        addEvent(()-> {
            this.setImmovable(false);
            this.setFightMode(false);
        }, 120);
    };

    private final Consumer<EntityLivingBase> heavy_Swing = (target) -> {
        this.setFightMode(true);
        this.setImmovable(true);
        animator.performAttack(ANIM_HEAVY_SWING);
        attackCounter--;
        if(attackCounter <= 0) {
            markForChange = true;
        }
        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(3));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.19),0.2F);
                world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            }, 5);
        }, 18);

        addEvent(()-> {
            for(int i = 0; i <= 16; i += 2) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.1, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = (float) (this.getAttack());
                    ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.6f, 5, false, 0.7F);
                }, i);
            }
            this.playSound(SoundsHandler.INCENDIUM_HEAVY_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 20);

        addEvent(()-> {
            this.setImmovable(true);
        }, 40);

        addEvent(()-> {
        this.lockLook = false;
        }, 50);

        addEvent(()-> {
        this.setImmovable(false);
        this.setFightMode(false);
        }, 64);
    };

    private final Consumer<EntityLivingBase> swing_Right = (target) -> {
      animator.performAttack(ANIM_SWING_RIGHT);
      this.setFightMode(true);
        attackCounter--;
        if(attackCounter <= 0) {
            markForChange = true;
        }
      addEvent(()-> {
          Vec3d targetedPos = target.getPositionVector();
          this.lockLook = true;
          addEvent(()-> {
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.07),0.2F);
          }, 5);
      }, 14);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.1, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = (float) (this.getAttack());
          ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.4F);
          this.playSound(SoundsHandler.INCENDIUM_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 24);

      addEvent(()-> {
        this.lockLook = false;
          this.setImmovable(true);
        int randI = ModRand.range(1, 11);
        if(randI >= 6) {
            this.setSwingTooContinue(target);
        } else {
            animator.performAttack(ANIM_SWING_RIGHT_FINISH);

            addEvent(()-> {
            this.setImmovable(false);
            this.setFightMode(false);
            }, 10);
        }
      }, 30);
    };

    private void setSwingTooContinue(EntityLivingBase target) {
        swing_right_Continue.accept(target);
    }

    private final Consumer<EntityLivingBase> swing_right_Continue = (target) -> {
        animator.performAttack(ANIM_SWING_RIGHT_CONTINUE);
        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.17),0.2F);
            }, 5);
        }, 8);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.1, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.2f, 5, false, 0.4F);
            this.playSound(SoundsHandler.INCENDIUM_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 18);

        addEvent(() -> {
        this.setImmovable(true);
        this.lockLook = false;
        }, 28);
        addEvent(()-> {
        this.setFightMode(false);
        this.setImmovable(false);
        }, 36);
    };
    private final Consumer<EntityLivingBase> swing_Left = (target) -> {
      this.setFightMode(true);
      this.setImmovable(true);
      animator.performAttack(ANIM_SWING_LEFT);
        attackCounter--;
        if(attackCounter <= 0) {
            markForChange = true;
        }
      addEvent(()-> {
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1.5));
          this.lockLook = true;
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.2),0.2F);
          }, 5);
      }, 14);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.1, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (float) (this.getAttack() * 1.25);
          ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.3F);
          this.playSound(SoundsHandler.INCENDIUM_SWING, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 24);

      addEvent(()-> {
        this.setImmovable(true);
        this.lockLook = false;
      }, 30);

      addEvent(()-> {
        this.setImmovable(false);
        this.setFightMode(false);
      }, 40);
    };

    private final Consumer<EntityLivingBase> magma_stomp_attack = (target) -> {
        this.setFightMode(true);
        animator.performAttack(ANIM_MAGMA_STOMP);
        attackCounter--;
        if(attackCounter <= 0) {
            markForChange = true;
        }


        addEvent(()-> {
            this.setImmovable(true);
        }, 15);

        addEvent(()-> {
        new ActionTileAOE(7).performAction(this, target);
            this.playSound(SoundsHandler.B_KNIGHT_STOMP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
                Main.proxy.spawnParticle(18,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
        }, 25);

        addEvent(()-> {
            this.setImmovable(false);
        }, 45);

        addEvent(()-> {
        this.setFightMode(false);
        }, 50);
    };

    @Override
    public void updateAnimations() {
        animator.setCustomLowerIdle(ANIM_SHIELD_IDLE);
        if(!this.isFullBodyUsage()) {
            if(this.isDefenseMode()) {
                animator.setCustomUpperIdle(ANIM_DEFENSE_IDLE);
            } else {
                animator.setCustomUpperIdle(ANIM_IDLE);
            }
        }
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(amount > 0.0F && this.isDefenseMode() || amount > 0.0F && this.isEnterDefense()) {
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

    @Override
    protected boolean canDespawn() {

        // Edit this to restricting them not despawning in Dungeons
        return false;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.INCENDIUM_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.INCENDIUM_HURT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.INCENDIUM_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
    }


    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "incendium");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

}
