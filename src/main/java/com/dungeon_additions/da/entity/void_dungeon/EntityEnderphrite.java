package com.dungeon_additions.da.entity.void_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.void_dungeon.EntityAIEnderphrite;
import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.ProjectileCrystalWave;
import com.dungeon_additions.da.entity.void_dungeon.enderphrite_action.ActionEnderphriteSplay;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import mod.azure.azurelib.util.MoveAnalysis;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityEnderphrite extends EntityEndBase implements IAttack {

    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);
    private final MoveAnalysis moveAnalysis;
    private Consumer<EntityLivingBase> prevAttacks;
    private int teleport_cooldown = 100;

    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK = "walk";

    private final String ANIM_STRIKE = "strike";
    private final String ANIM_STRIKE_CONTINUE = "strike_continue";
    private final String ANIM_STRIKE_FINISH = "strike_finish";
    private final String ANIM_UPPER_STRIKE = "upper_strike";
    private final String ANIM_TELEPORT = "teleport";
    private final String ANIM_SIDE_STRIKE = "side_strike";

    private static final DataParameter<Boolean> TELEPORT = EntityDataManager.createKey(EntityEnderphrite.class, DataSerializers.BOOLEAN);

    public boolean isTeleportAttack() {
        return this.dataManager.get(TELEPORT);
    }
    private void setTeleportAttack(boolean value) {
        this.dataManager.set(TELEPORT, Boolean.valueOf(value));
    }

    public EntityEnderphrite(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 2.9F);
        this.experienceValue = 38;
        this.falter_resistance = 1.7F;
        this.hemorrhage_resistance = 0.3F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public EntityEnderphrite(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 2.9F);
        this.experienceValue = 38;
        this.falter_resistance = 1.7F;
        this.hemorrhage_resistance = 0.3F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Teleport_Attack", this.isTeleportAttack());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setTeleportAttack(nbt.getBoolean("Teleport_Attack"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(TELEPORT, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.enderphrite_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.34D);
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
    public void onUpdate() {
        super.onUpdate();
        this.moveAnalysis.update();
        if(!world.isRemote) {
            if(teleport_cooldown >= 0) {
                teleport_cooldown--;
            }
        }
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
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(strike_attack, upper_swing, side_strike));
            double[] weights = {
                    (prevAttacks != strike_attack) ? 1/distance : 0.001,
                    (prevAttacks != upper_swing) ? 1/distance : 0.001,
                    (prevAttacks != side_strike) ? 1/distance : 0.001

            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return 30;
    }

    private Consumer<EntityLivingBase> side_strike = target -> {
      animator.performAttack(ANIM_SIDE_STRIKE);
      this.setFightMode(true);
      this.setImmovable(true);

      addEvent(()-> {
          this.lockLook = true;
      }, 28);

      addEvent(()-> {
          //do projectiles
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = (this.getAttack());
          ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.4f, 0, false, 0.3F);
          this.playSound(SoundsHandler.APATHYR_CAST_MAGIC, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
          new ActionEnderphriteSplay().performAction(this, target);
      }, 34);

      addEvent(()-> {
            this.lockLook = false;
      }, 45);

      addEvent(()-> {
        this.setFightMode(false);
        this.setImmovable(false);
      }, 60);
    };

    private Consumer<EntityLivingBase> upper_swing = target -> {
      animator.performAttack(ANIM_UPPER_STRIKE);
      this.setFightMode(true);
      this.setImmovable(true);

        addEvent(()-> {
            if(this.getDistance(target) >= 4) {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-16));
                addEvent(() -> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23), 0.1F);
                }, 6);
            }
        }, 14);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.4f, 0, false, 1.0F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.2f));
            ProjectileCrystalWave wave = new ProjectileCrystalWave(world, this, (float) this.getAttack(), null,false);
            wave.setPosition(offset.x, offset.y, offset.z);
            wave.shoot(this, 0, this.rotationYaw, 0.0F, 0.6F, 0);
            wave.setTravelRange(12);
            world.spawnEntity(wave);
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 0.1, 0)));
            this.playSound(SoundsHandler.COLOSSUS_HILT_SLAM, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            Main.proxy.spawnParticle(21,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
        }, 30);

        addEvent(()-> this.lockLook = false, 40);

      addEvent(()-> {
          this.setFightMode(false);
          this.setImmovable(false);
      }, 50);
    };

    private Consumer<EntityLivingBase> strike_attack = target -> {
        this.setFightMode(true);
        this.setImmovable(true);
        animator.performAttack(ANIM_STRIKE);
        addEvent(()-> this.playSound(SoundsHandler.ENDERPHRITE_SCREAM, 0.7f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f)), 10);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 5);
        }, 15);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.4f, 0, false, 0.6F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 25);

        addEvent(()-> this.lockLook = false, 26);

        addEvent(()-> this.setImmovable(true), 30);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
            }, 5);
        }, 33);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (this.getAttack());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.4f, 0, false, 0.6F);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 43);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(true);
        }, 50);

        addEvent(()-> {
            if(this.getDistance(target) <= 8 && rand.nextBoolean()) {
                this.setSTrikeTOOContinue(target);
            } else {
                animator.performAttack(ANIM_STRIKE_FINISH);

                addEvent(()-> {
                this.setImmovable(false);
                this.setFightMode(false);
                }, 15);
            }
        }, 60);
    };

    private void setSTrikeTOOContinue(EntityLivingBase target) {
        strike_continue.accept(target);
    }

    private Consumer<EntityLivingBase> strike_continue = target -> {
        animator.performAttack(ANIM_STRIKE_CONTINUE);
        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-3));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
                this.playSound(SoundsHandler.APATHYR_SLIGHT_DASH, 1.0f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 5);
        }, 13);

        addEvent(()-> {
            for(int b = 0; b <= 9; b++) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 1.0, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = (this.getAttack());
                    ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.4f, 0, false, 0.3F);
                }, b);
            }
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 18);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 30);

        addEvent(()-> {
            this.setImmovable(false);
            this.setFightMode(false);
        }, 40);
    };

    @Override
    public void updateAnimations() {
        if(!this.isFightMode()) {
            boolean isMovingOnGround = moveAnalysis.isMovingHorizontally() && this.onGround;
            animator.handleIdleStates(isMovingOnGround);
        }
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
        animator.performAttack(ANIM_TELEPORT);
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
}
