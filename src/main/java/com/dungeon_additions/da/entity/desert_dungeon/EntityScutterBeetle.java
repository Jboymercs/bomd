package com.dungeon_additions.da.entity.desert_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityAIRoyalAttack;
import com.dungeon_additions.da.entity.ai.desert_dungeon.EntityScutterBeetleAI;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkRoyal;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityVillager;
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

import java.util.List;
import java.util.function.Consumer;

public class EntityScutterBeetle extends EntityDesertBase implements IAnimatable, IAttack, IAnimationTickable {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;
    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK = "walk";

    private final String ANIM_BITE = "bite";
    private final String ANIM_BEGIN_SCUTTLE = "begin_scuttle";
    private final String ANIM_SCUTTLE = "scuttle";
    private final String ANIM_END_SCUTTLE = "end_scuttle";

    private boolean setHeadState;

    private static final DataParameter<Boolean> BITE = EntityDataManager.createKey(EntityScutterBeetle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BEGIN_SCUTTLE = EntityDataManager.createKey(EntityScutterBeetle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SCUTTLE = EntityDataManager.createKey(EntityScutterBeetle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_SCUTTLE = EntityDataManager.createKey(EntityScutterBeetle.class, DataSerializers.BOOLEAN);

    public boolean isBite() {
        return this.dataManager.get(BITE);
    }
    private void setBite(boolean value) {
        this.dataManager.set(BITE, Boolean.valueOf(value));
    }
    public boolean isBeginScuttle() {
        return this.dataManager.get(BEGIN_SCUTTLE);
    }
    private void setBeginScuttle(boolean value) {
        this.dataManager.set(BEGIN_SCUTTLE, Boolean.valueOf(value));
    }
    public boolean isScuttle() {
        return this.dataManager.get(SCUTTLE);
    }
    private void setScuttle(boolean value) {
        this.dataManager.set(SCUTTLE, Boolean.valueOf(value));
    }
    public boolean isEndScuttle() {
        return this.dataManager.get(END_SCUTTLE);
    }
    private void setEndScuttle(boolean value) {
        this.dataManager.set(END_SCUTTLE, Boolean.valueOf(value));
    }

    public EntityScutterBeetle(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.2F, 0.9F);
        this.experienceValue = 7;
        this.setHeadState = false;
        this.setScuttle(false);
    }

    public EntityScutterBeetle(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 0.9F);
        this.experienceValue = 7;
        this.setHeadState =false;
        this.setScuttle(false);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Bite", this.isBite());
        nbt.setBoolean("Begin_Scuttle", this.isBeginScuttle());
        nbt.setBoolean("Scuttle", this.isScuttle());
        nbt.setBoolean("End_Scuttle", this.isEndScuttle());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setBite(nbt.getBoolean("Bite"));
        this.setBeginScuttle(nbt.getBoolean("Begin_Scuttle"));
        this.setScuttle(nbt.getBoolean("Scuttle"));
        this.setEndScuttle(nbt.getBoolean("End_Scuttle"));
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(BITE, Boolean.valueOf(false));
        this.dataManager.register(BEGIN_SCUTTLE, Boolean.valueOf(false));
        this.dataManager.register(SCUTTLE, Boolean.valueOf(false));
        this.dataManager.register(END_SCUTTLE, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(14D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.32D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityScutterBeetleAI<>(this, 1.0D, 20, 10, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityRabbit>(this, EntityRabbit.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    private int scuttleTimer = 40;


    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.setHeadState && !world.isRemote) {
            EntityLivingBase target = this.getAttackTarget();
            if(target != null) {
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable());
            if(!nearbyEntities.isEmpty()) {
                for (EntityLivingBase base : nearbyEntities) {
                    if (!(base instanceof EntityDesertBase)) {
                        if(base == target || base instanceof EntityPlayer) {
                            Vec3d offset = base.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.75, 0)));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                            float damage =(float) (this.getAttack() * 1.5);
                            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 1.4f, 0, false);
                            this.playSound(SoundsHandler.BEETLE_ATTACK, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
                            this.endScuttleAttack();
                        }
                    }
                }
            }
            }

            scuttleTimer--;
            if(this.ticksExisted % 2 == 0) {
                Main.proxy.spawnParticle(2, this.posX, this.posY + 0.6, this.posZ, 0,0,0);
            }
            if(scuttleTimer < 0 || Math.abs(this.motionX) + Math.abs(this.motionZ) < 0.01f) {
                this.endScuttleAttack();
            }
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            if(distance <= 5) {
                prevAttacks = do_bite_attack;
            } else {
                prevAttacks = do_scuttle_attack;
            }

            prevAttacks.accept(target);
        }
        return distance <= 5 ? 20 : 70;
    }

    private void endScuttleAttack() {
      this.setScuttle(false);
      this.setEndScuttle(true);
      this.setHeadState = false;
      this.scuttleTimer = 40;
      this.lockLook =false;

      addEvent(()-> {
          this.setEndScuttle(false);
          this.setFightMode(false);
      }, 10);
    }

    private final Consumer<EntityLivingBase> do_scuttle_attack = (target) -> {
        this.setImmovable(true);
        this.setFullBodyUsage(true);
        this.setBeginScuttle(true);
        this.setFightMode(true);

        addEvent(()-> {
            this.playSound(SoundsHandler.BEETLE_WAR_CRY, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 25);
        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(3));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.31),0.1F);
                this.setHeadState = true;
                this.setFullBodyUsage(false);
                this.setBeginScuttle(false);
                this.setScuttle(true);
            }, 5);
        }, 30);

    };

    private final Consumer<EntityLivingBase> do_bite_attack = (target) -> {
        this.setFightMode(true);
        this.setBite(true);
        this.setImmovable(true);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.15),0.1F);
            }, 5);
        }, 15);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 0.75, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
            this.playSound(SoundsHandler.BEETLE_ATTACK, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 27);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 40);

        addEvent(()-> {
        this.setBite(false);
        this.setImmovable(false);
        this.setFightMode(false);
        }, 50);

    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "walk_controller", 0, this::predicateWalk));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttackStates));
        data.addAnimationController(new AnimationController(this, "head_controller", 0, this::predicateHoldHead));
    }

    private <E extends IAnimatable> PlayState predicateHoldHead(AnimationEvent<E> event) {
        if(this.isScuttle() && this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SCUTTLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateAttackStates(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isBite()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BITE, false));
                return PlayState.CONTINUE;
            }
            if(this.isBeginScuttle()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BEGIN_SCUTTLE, false));
                return PlayState.CONTINUE;
            }
            if(this.isEndScuttle()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_END_SCUTTLE, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateWalk(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage()) {
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
    public void tick() {

    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "scutter_beetle");

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
        return SoundsHandler.BEETLE_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.BEETLE_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.BEETLE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.BEETLE_WALK, 0.3F, 0.8f + ModRand.getFloat(0.3F));
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
