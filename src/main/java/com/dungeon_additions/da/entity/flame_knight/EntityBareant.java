package com.dungeon_additions.da.entity.flame_knight;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.EntityTimedAttackIncendium;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.ai.flame_dungeon.EntityBareantAI;
import com.dungeon_additions.da.entity.ai.flying.TimedAttackInitiator;
import com.dungeon_additions.da.entity.flame_knight.bareant.ActionSummonBareantFlame;
import com.dungeon_additions.da.entity.flame_knight.misc.ActionSummonTrackFlame;
import com.dungeon_additions.da.entity.night_lich.EntityAbstractNightLich;
import com.dungeon_additions.da.packets.EnumModParticles;
import com.dungeon_additions.da.packets.MessageModParticles;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
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
import net.minecraftforge.client.gui.ForgeGuiFactory;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.function.Consumer;

public class EntityBareant extends EntityFlameBase implements IAnimatable, IAnimationTickable, IAttack, IPitch {
    private Consumer<EntityLivingBase> prevAttacks;
    private AnimationFactory factory = new AnimationFactory(this);
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

    public boolean isSummonFlame() {
        return this.dataManager.get(SUMMON_FLAME);
    }

    private void setSummonFlame(boolean value) {
        this.dataManager.set(SUMMON_FLAME, Boolean.valueOf(value));
    }
    public boolean isMeleeAttack() {
        return this.dataManager.get(MELEE_ATTACK);
    }

    private void setMeleeAttack(boolean value) {
        this.dataManager.set(MELEE_ATTACK, Boolean.valueOf(value));
    }

    public EntityBareant(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 8;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(0.9F, 0.9F);
        this.setNoGravity(true);
    }

    public EntityBareant(World worldIn) {
        super(worldIn);
        this.experienceValue = 8;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(0.9F, 0.9F);
        this.setNoGravity(true);
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
        nbt.setBoolean("Summon_Flame", this.isSummonFlame());
        nbt.setBoolean("Melee_Attack", this.isMeleeAttack());
        nbt.setFloat("Look", this.getPitch());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSummonFlame(nbt.getBoolean("Summon_Flame"));
        this.setMeleeAttack(nbt.getBoolean("Melee_Attack"));
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
      this.setMeleeAttack(true);
      this.setFightMode(true);
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
              ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.7f, 4, false);
          }, 8);
      }, 32);

      addEvent(()-> {
          this.lockLook = false;
        this.standbyOnVel = false;
        this.summonFlames = false;
      }, 60);

      addEvent(()-> {
        this.setMeleeAttack(false);
        this.setFightMode(false);
      }, 70);
    };

    private final Consumer<EntityLivingBase> summon_flame = (target) -> {
        this.setFightMode(true);
        this.setSummonFlame(true);

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
            this.setSummonFlame(false);
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
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "walk_controller", 0, this::predicateWalk));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateFight));
    }

    private <E extends IAnimatable> PlayState predicateFight(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSummonFlame()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_FLAME, false));
                return PlayState.CONTINUE;
            }
            if(this.isMeleeAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MELEE_ATTACK, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
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
    protected boolean canDespawn() {

        // Edit this to restricting them not despawning in Dungeons
        return false;
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
