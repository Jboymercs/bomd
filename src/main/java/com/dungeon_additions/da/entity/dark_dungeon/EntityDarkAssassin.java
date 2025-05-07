package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.animation.IAnimatedEntity;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.dark_dungeon.EntityAIAssassinAttack;
import com.dungeon_additions.da.entity.ai.sky_dungeon.EntityAIMageGargoyle;
import com.dungeon_additions.da.entity.sky_dungeon.EntityMageGargoyle;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityDarkAssassin extends EntityDarkBase implements IAnimatable, IAnimationTickable, IAttack {

    private static final DataParameter<Boolean> SWING_MELEE = EntityDataManager.createKey(EntityDarkAssassin.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_MELEE_ALT = EntityDataManager.createKey(EntityDarkAssassin.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_ATTACK = EntityDataManager.createKey(EntityDarkAssassin.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_INK = EntityDataManager.createKey(EntityDarkAssassin.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> HIDDEN = EntityDataManager.createKey(EntityDarkAssassin.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DODGE = EntityDataManager.createKey(EntityDarkAssassin.class, DataSerializers.BOOLEAN);

    private final String ANIM_ATTACK = "attack";
    private final String ANIM_ATTACK_ALTERNATIVE = "attack_alternative";
    private final String ANIM_JUMP_ATTACK = "attack_jump";
    private final String ANIM_SUMMON_INK = "summon_ink";
    private final String ANIM_DODGE = "dodge";

    private final String ANIM_IDLE_LOWER = "idle";
    private final String ANIM_IDLE_UPPER = "idle_upper";
    private final String ANIM_WALK_LOWER = "walk";
    private final String ANIM_WALK_UPPER = "walk_upper";

    private final String ANIM_HIDDEN = "hidden";

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;

    private boolean firstStrike = true;
    public boolean setMaskReveal = false;

    private boolean isSwingMelee() {
        return this.dataManager.get(SWING_MELEE);
    }

    private void setSwingMelee(boolean value) {
        this.dataManager.set(SWING_MELEE, Boolean.valueOf(value));
    }

    private boolean isSwingMeleeAlt() {
        return this.dataManager.get(SWING_MELEE_ALT);
    }

    private void setSwingMeleeAlt(boolean value) {
        this.dataManager.set(SWING_MELEE_ALT, Boolean.valueOf(value));
    }

    private boolean isJumpAttack() {
        return this.dataManager.get(JUMP_ATTACK);
    }

    private void setJumpAttack(boolean value) {
        this.dataManager.set(JUMP_ATTACK, Boolean.valueOf(value));
    }

    public boolean isSummonInk() {
        return this.dataManager.get(SUMMON_INK);
    }

    private void setSummonInk(boolean value) {
        this.dataManager.set(SUMMON_INK, Boolean.valueOf(value));
    }

    private boolean isDodge() {
        return this.dataManager.get(DODGE);
    }

    private void setDodge(boolean value) {
        this.dataManager.set(DODGE, Boolean.valueOf(value));
    }

    private boolean isHiddenFrom() {
        return this.dataManager.get(HIDDEN);
    }

    private void setHiddenFrom(boolean value) {
        this.dataManager.set(HIDDEN, Boolean.valueOf(value));
    }

    public EntityDarkAssassin(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 10;
        this.setSize(0.6F, 1.95F);
    }

    public EntityDarkAssassin(World worldIn) {
        super(worldIn);
        this.experienceValue = 10;
        this.setSize(0.6F, 1.95F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Swing_Melee", this.isSwingMelee());
        nbt.setBoolean("Swing_Melee_Alt", this.isSwingMeleeAlt());
        nbt.setBoolean("Jump_Attack", this.isJumpAttack());
        nbt.setBoolean("Summon_Ink", this.isSummonInk());
        nbt.setBoolean("Dodge", this.isDodge());
        nbt.setBoolean("Hidden", this.isHiddenFrom());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSwingMelee(nbt.getBoolean("Swing_Melee"));
        this.setSwingMeleeAlt(nbt.getBoolean("Swing_Melee_Alt"));
        this.setJumpAttack(nbt.getBoolean("Jump_Attack"));
        this.setSummonInk(nbt.getBoolean("Summon_Ink"));
        this.setDodge(nbt.getBoolean("Dodge"));
        this.setHiddenFrom(nbt.getBoolean("Hidden"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SWING_MELEE, Boolean.valueOf(false));
        this.dataManager.register(SWING_MELEE_ALT, Boolean.valueOf(false));
        this.dataManager.register(JUMP_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_INK, Boolean.valueOf(false));
        this.dataManager.register(HIDDEN, Boolean.valueOf(false));
        this.dataManager.register(DODGE, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.assassin_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.31D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.assassin_healht);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.assassin_armor);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.6D);
    }


    @Override
    public void initEntityAI() {
        super.initEntityAI();
          this.tasks.addTask(4, new EntityAIAssassinAttack<EntityDarkAssassin>(this, 1.0D, 20, 6, 0.25F));
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

    private int dodgeCooldown = 60;
    private int invisibilityCooldown = 80;
    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!world.isRemote) {
            dodgeCooldown--;
            invisibilityCooldown--;

            EntityLivingBase target = this.getAttackTarget();

            if(target != null) {
                double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
                double distance = Math.sqrt(distSq);

                if(distance >= 8 && invisibilityCooldown < 0 && !this.isHiddenFrom()) {
                    this.setHiddenFrom(true);
                    invisibilityCooldown = 100;
                }
            }

            if(target == null) {
                this.firstStrike = true;
            }
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(1)), ModColors.BLACK, new Vec3d(0, 0.01, 0));
        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0))), ModColors.BLACK, new Vec3d((world.rand.nextFloat() - world.rand.nextFloat())/5,0.1,(world.rand.nextFloat() - world.rand.nextFloat())/5));
        }
    }


    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(this.isHiddenFrom() && world.rand.nextInt(2) == 0) {
            this.world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        } else if (world.rand.nextInt(7) == 0) {
            this.world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        if(!this.isFightMode()) {
            double distance = Math.sqrt(distanceSq);
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(strike, strike_alternative, jump_strike, summon_ink));
            double[] weights = {
                    (prevAttacks != strike) ? 1/distance : 0,
                    (prevAttacks != strike_alternative) ? 1/distance : 0,
                    (prevAttacks != jump_strike) ? 1/distance : 0,
                    (prevAttacks != summon_ink && distance >= 4) ? 1/distance : 0

            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return 30;
    }

    private final Consumer<EntityLivingBase> summon_ink = (target) -> {
      this.setSummonInk(true);
      this.setFullBodyUsage(true);
      this.setFightMode(true);
      this.setImmovable(true);
      addEvent(()-> {
        this.setMaskReveal = true;
      },  10);
        this.playSound(SoundsHandler.DARK_SUMMON_INK, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.7f));
      addEvent(()-> {
          this.lockLook = true;
      }, 30);

      addEvent(() -> {
          this.firstStrike = false;
          EntityShadowHand hand = new EntityShadowHand(world);
          hand.setPosition(target.posX, target.posY, target.posZ);
          world.spawnEntity(hand);
      }, 15);


      addEvent(()-> {
          this.lockLook = false;
          this.setMaskReveal = false;
      }, 45);
      addEvent(()-> {
        this.setSummonInk(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 50);
    };

    private final Consumer<EntityLivingBase> jump_strike = (target) -> {
      this.setJumpAttack(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      if(this.isHiddenFrom()) {
          this.setHiddenFrom(false);
          this.playSound(SoundsHandler.DARK_ASSASSIN_APPEAR, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.7f));
      }
        Vec3d targetedPos = target.getPositionVector();
        double distance = this.getPositionVector().distanceTo(targetedPos);
        ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.16),0.7F);
        this.lockLook = true;

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.firstStrike ? (float) (this.getAttack() * 2) : (float) (this.getAttack());
            this.firstStrike = false;
            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.7f));
        }, 18);

        addEvent(()-> this.setImmovable(true), 25);
        addEvent(()-> this.lockLook = false, 33);
      addEvent(()-> {
        this.setJumpAttack(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
      }, 40);
    };

    private final Consumer<EntityLivingBase> strike_alternative = (target) -> {
      this.setSwingMeleeAlt(true);
      this.setFightMode(true);
        if(this.isHiddenFrom()) {
            this.setHiddenFrom(false);
            this.playSound(SoundsHandler.DARK_ASSASSIN_APPEAR, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.7f));
        }
      addEvent(()-> {
          this.lockLook = true;
        Vec3d targetedPos = target.getPositionVector();
        addEvent(()-> {
            double distance = this.getPositionVector().distanceTo(targetedPos);
            ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.24),0.2F);
        }, 6);
      },8 );

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.firstStrike ? (float) (this.getAttack() * 2) : (float) (this.getAttack());
          this.firstStrike = false;
          ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.2f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.7f));
      }, 20);

      addEvent(()-> this.lockLook = false, 35);

      addEvent(()-> {
        this.setSwingMeleeAlt(false);
        this.setFightMode(false);
      }, 45);
    };
    private final Consumer<EntityLivingBase> strike = (target) -> {
        this.setSwingMelee(true);
        this.setFightMode(true);
        if(this.isHiddenFrom()) {
            this.setHiddenFrom(false);
            this.playSound(SoundsHandler.DARK_ASSASSIN_APPEAR, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.7f));
        }
        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.24),0.2F);
            }, 6);
        }, 6);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.firstStrike ? (float) (this.getAttack() * 2) : (float) (this.getAttack());
            this.firstStrike = false;
            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.7f));
        }, 18);

        addEvent(()-> this.lockLook = false, 30);

        addEvent(()-> {
            this.setSwingMelee(false);
            this.setFightMode(false);
        }, 40);
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateIdleArms));
        data.addAnimationController(new AnimationController(this, "arms_legs_controller", 0, this::predicateLegsUpper));
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "hidden_controller", 0, this::predicateHiddenTact));
    }

    private <E extends IAnimatable> PlayState predicateHiddenTact(AnimationEvent<E> event) {
        if(this.isHiddenFrom()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HIDDEN, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isDodge()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DODGE, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingMelee()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingMeleeAlt()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK_ALTERNATIVE, false));
                return PlayState.CONTINUE;
            }
            if(this.isJumpAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_JUMP_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isSummonInk()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_INK, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if (event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage() && !this.isHiddenFrom()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdleArms(AnimationEvent<E> event) {
        if (event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode() && !this.isHiddenFrom()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if (!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage() && !this.isHiddenFrom()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegsUpper(AnimationEvent<E> event) {
        if (!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode() && !this.isHiddenFrom()) {
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
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.isDodge()) {
            return false;
        }

        if(!this.isFightMode() && dodgeCooldown < 0 && source.getImmediateSource() instanceof EntityLivingBase) {
            if(source.getImmediateSource() instanceof EntityPlayer) {
                EntityPlayer player = ((EntityPlayer) source.getImmediateSource());
                if(player.isAirBorne) {
                    return super.attackEntityFrom(source, amount * 0.5F);
                } else {
                    this.DodgeAction((EntityLivingBase) source.getImmediateSource());
                    return false;
                }
            } else {
                this.DodgeAction((EntityLivingBase) source.getImmediateSource());
                return false;
            }

        }


        return super.attackEntityFrom(source, amount);
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }


    @Override
    public boolean getCanSpawnHere()
    {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "dark_assassin");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
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

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.DARK_ASSASSIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.DARK_ASSASSIN_HURT;
    }

    private void DodgeAction (EntityLivingBase source) {
        this.setDodge(true);
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.playSound(SoundEvents.BLOCK_ANVIL_HIT, 0.5f, 1.6f / (rand.nextFloat() * 0.4F + 0.7f));
        this.playSound(SoundsHandler.DARK_ASSASSIN_DASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.7f));
        this.lockLook = true;
        if(this.isHiddenFrom()) {
            this.setHiddenFrom(false);
        }
        if(source != null) {
            Vec3d targetPos = source.getPositionVector();
            Vec3d dirToo = this.getPositionVector().subtract(targetPos).normalize();
            Vec3d jumpTooPos = this.getPositionVector().add(dirToo.scale(47));
            ModUtils.leapTowards(this, jumpTooPos, 1.4F, 0.5F);
        }

        addEvent(()-> {
        this.setDodge(false);
        this.lockLook = false;
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.dodgeCooldown = 65;
        }, 25);
    }
}
