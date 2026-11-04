package com.dungeon_additions.da.entity.flame_knight;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.ai.flame_dungeon.EntityBareantAI;
import com.dungeon_additions.da.entity.ai.flying.TimedAttackInitiator;
import com.dungeon_additions.da.entity.flame_knight.bareant.ActionSummonBareantFlame;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import mod.azure.azurelib.util.MoveAnalysis;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class EntityBareant extends EntityFlameBase implements IAttack, IPitch {
    private Consumer<EntityLivingBase> prevAttacks;
    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK = "move";
    private final String ANIM_SUMMON_FLAME = "summon_flame";
    private final String ANIM_MELEE_ATTACK = "melee_attack";

    public boolean standbyOnVel = false;
    public boolean clearCurrentVelocity = false;
    private boolean summonFlames = false;
    private static final DataParameter<Boolean> SUMMON_FLAME = EntityDataManager.createKey(EntityBareant.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEE_ATTACK = EntityDataManager.createKey(EntityBareant.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityBareant.class, DataSerializers.FLOAT);
    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);
    private final MoveAnalysis moveAnalysis;

    public EntityBareant(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 8;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(0.9F, 0.9F);
        this.setNoGravity(true);
        this.falter_resistance = 0.5F;
        this.hemorrhage_resistance = 0.4F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public EntityBareant(World worldIn) {
        super(worldIn);
        this.experienceValue = 8;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(0.9F, 0.9F);
        this.setNoGravity(true);
        this.falter_resistance = 0.5F;
        this.hemorrhage_resistance = 0.4F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(MELEE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_FLAME, Boolean.valueOf(false));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setFloat("Look", this.getPitch());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.bareant_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.bareant_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.bareant_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityBareantAI(this, 16, 8, 35, new TimedAttackInitiator<>(this, 20)));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPigZombie>(this, EntityPigZombie.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.moveAnalysis.update();
        if(!world.isRemote) {
            if(clearCurrentVelocity) {
                this.motionX = 0;
                this.motionY = 0;
                this.motionZ = 0;
                standbyOnVel = true;
                clearCurrentVelocity = false;
            }
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        if(!this.isFightMode()) {
            if(prevAttacks == summon_flame) {
                prevAttacks = melee_dash;
            } else {
                prevAttacks = summon_flame;
            }
            prevAttacks.accept(target);
        }
        return (prevAttacks == melee_dash) ? 160 : 100;
    }

    private final Consumer<EntityLivingBase> melee_dash = (target) -> {
      this.setFightMode(true);
      animator.performAttack(ANIM_MELEE_ATTACK);
      this.clearCurrentVelocity = true;
      this.setImmovable(true);

      addEvent(()-> {
        //Do dash
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-0.5));
          this.lockLook = true;
          this.summonFlames = true;
          addEvent(()-> {
              this.setImmovable(false);
              ModUtils.attemptTeleport(targetedPos, this);
              this.playSound(SoundsHandler.BAREANT_DASH, 1.5f, 1.0f / (rand.nextFloat() * 0.8F + 0.4f));
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
              float damage = this.getAttack();
              ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.7f, 4, false, 1.3F);
          }, 8);
      }, 32);

      addEvent(()-> {
          this.lockLook = false;
        this.standbyOnVel = false;
        this.summonFlames = false;
      }, 60);

      addEvent(()-> {
        this.setFightMode(false);
      }, 70);
    };

    private final Consumer<EntityLivingBase> summon_flame = (target) -> {
        this.setFightMode(true);
        animator.performAttack(ANIM_SUMMON_FLAME);

        addEvent(()-> {
        this.setImmovable(true);
        this.clearCurrentVelocity = true;
        }, 20);

        addEvent(()-> {
            //summon Flame
            new ActionSummonBareantFlame().performAction(this, target);
        }, 25);

        addEvent(()-> {
            this.setImmovable(false);
            this.standbyOnVel = false;
            this.setFightMode(false);
        }, 55);
    };

    @Override
    public void handleStatusUpdate(byte id) {
        if(id == ModUtils.PARTICLE_BYTE) {
            world.spawnParticle(EnumParticleTypes.FLAME, this.posX, this.posY + 0.5, this.posZ, 0, 0.03, 0, ModRand.range(10, 15));
        }
        super.handleStatusUpdate(id);
    }

    public void onEntityUpdate() {
        if(this.summonFlames && world.rand.nextInt(2) == 0) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
        super.onEntityUpdate();
    }

    @Override
    public void updateAnimations() {
        boolean isMovingOnGround = moveAnalysis.isMoving();
        if(!this.isFightMode()) {
            if(isMovingOnGround) {
                animator.setCustomUpperWalk(ANIM_WALK);
            } else {
                animator.setCustomUpperIdle(ANIM_IDLE);
            }
        }
    }

    @Override
    protected boolean canDespawn() {

        // Edit this to restricting them not despawning in Dungeons
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.BAREANT_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.BAREANT_HURT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.BAREANT_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "bareant");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        //we want our boy to be stable while he's slinging melee attack or in a angered State

            this.dataManager.set(LOOK, clampedLook);
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }
}
