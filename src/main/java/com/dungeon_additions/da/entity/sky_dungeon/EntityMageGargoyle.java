package com.dungeon_additions.da.entity.sky_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.flying.EntityAIRandomFly;
import com.dungeon_additions.da.entity.ai.flying.TimedAttackInitiator;
import com.dungeon_additions.da.entity.ai.sky_dungeon.EntityAIMageGargoyle;
import com.dungeon_additions.da.entity.ai.sky_dungeon.EntityGargoyleTridentAI;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityMageGargoyle extends EntitySkyBase implements IAnimatable, IAnimationTickable, IAttack {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;

    private int castSpecialCooldown = 50 + ModRand.range(100, 200);
    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK = "walk";
    private final String ANIM_HANGING = "hanging";

    private final String ANIM_SWING = "attack";
    private final String ANIM_SWING_ALT = "attack_alt";
    private final String ANIM_CAST_BASIC = "cast_basic";
    private final String ANIM_CAST_SPECIAL = "cast_special";

    private static final DataParameter<Boolean> SWING_MELEE = EntityDataManager.createKey(EntityMageGargoyle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_MELEE_ALT = EntityDataManager.createKey(EntityMageGargoyle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_BASIC = EntityDataManager.createKey(EntityMageGargoyle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAST_SPECIAL = EntityDataManager.createKey(EntityMageGargoyle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HANGING = EntityDataManager.createKey(EntityMageGargoyle.class, DataSerializers.BOOLEAN);

    private boolean isSwingMelee() {return this.dataManager.get(SWING_MELEE);}
    private void setSwingMelee(boolean value) {this.dataManager.set(SWING_MELEE, Boolean.valueOf(value));}
    private boolean isSwingMeleeAlt() {return this.dataManager.get(SWING_MELEE_ALT);}
    private void setSwingMeleeAlt(boolean value) {this.dataManager.set(SWING_MELEE_ALT, Boolean.valueOf(value));}
    private boolean isCastBasic() {return this.dataManager.get(CAST_BASIC);}
    private void setCastBasic(boolean value) {this.dataManager.set(CAST_BASIC, Boolean.valueOf(value));}
    private boolean isCastSpecial() {return this.dataManager.get(CAST_SPECIAL);}
    private void setCastSpecial(boolean value) {this.dataManager.set(CAST_SPECIAL, Boolean.valueOf(value));}
    private boolean isHanging() {return this.dataManager.get(HANGING);}
    private void setHanging(boolean value) {this.dataManager.set(HANGING, Boolean.valueOf(value));}

    public EntityMageGargoyle(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 12;
        this.setSize(0.7F, 1.95F);
        hasFallTpOverride = true;
    }

    public EntityMageGargoyle(World worldIn) {
        super(worldIn);
        this.experienceValue = 12;
        this.setSize(0.7F, 1.95F);
        hasFallTpOverride = true;
    }
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Swing_Melee", this.isSwingMelee());
        nbt.setBoolean("Swing_Melee_Alt", this.isSwingMeleeAlt());
        nbt.setBoolean("Cast_Basic", this.isCastBasic());
        nbt.setBoolean("Cast_Special", this.isCastSpecial());
        nbt.setBoolean("Hanging", this.isHanging());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSwingMelee(nbt.getBoolean("Swing_Melee"));
        this.setSwingMeleeAlt(nbt.getBoolean("Swing_Melee_Alt"));
        this.setCastBasic(nbt.getBoolean("Cast_Basic"));
        this.setCastSpecial(nbt.getBoolean("Cast_Special"));
        this.setHanging(nbt.getBoolean("Hanging"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
    this.dataManager.register(SWING_MELEE, Boolean.valueOf(false));
    this.dataManager.register(SWING_MELEE_ALT, Boolean.valueOf(false));
    this.dataManager.register(CAST_SPECIAL, Boolean.valueOf(false));
    this.dataManager.register(CAST_BASIC, Boolean.valueOf(false));
    this.dataManager.register(HANGING, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.elder_gargoyle_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.elder_gargoyle_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.elder_gargoyle_armor);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.6D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIMageGargoyle<EntityMageGargoyle>(this, 1.0D, 40, 20, 0.2F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!world.isRemote) {
            castSpecialCooldown--;

        }
    }


    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode() && !this.isHanging()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(swing_regular, swing_alternative, cast_basic, cast_special));
            double[] weights = {
                    (distance <= 6 && prevAttacks != swing_regular) ? 1/distance : 0,
                    (distance <= 6 && prevAttacks != swing_alternative) ? 1/distance : 0,
                    (distance > 6) ? 1/distance : (distance <= 6 && prevAttacks != cast_basic) ? 1/distance : 0,
                    (prevAttacks != cast_special && distance > 6 && castSpecialCooldown < 0 && isTargetableArea(target)) ? 1/distance : 0
            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return prevAttacks == cast_basic ? 105 : 20;
    }

    //a boolean to ensure that two of the same attacks are not initiating on the target
    private boolean isTargetableArea(EntityLivingBase target) {
        if(target == null) {
            return false;
        }
        List<EntityGargoyleLazer> nearbyEntities = this.world.getEntitiesWithinAABB(EntityGargoyleLazer.class, target.getEntityBoundingBox().grow(2, 3, 2), e -> !e.getIsInvulnerable());
        if(!nearbyEntities.isEmpty()) {
            return false;
        }
        return true;
    }

    private final Consumer<EntityLivingBase> cast_special = (target) -> {
      this.setCastSpecial(true);
      this.setFightMode(true);
      this.setImmovable(true);
      this.lockLook = true;

      addEvent(()-> this.playSound(SoundsHandler.GARGOYLE_CAST_SPECIAL, 1.0f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 33);
        addEvent(()-> {
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.1, 1.2, 0)));
            Main.proxy.spawnParticle(18,world, relPos.x, this.posY, relPos.z, 0, 0, 0);
        }, 33);
      addEvent(()-> {
        //cast special attack
          EntityGargoyleLazer lazer = new EntityGargoyleLazer(world, target);
          lazer.setPosition(target.posX, target.posY, target.posZ);
          world.spawnEntity(lazer);
      }, 55);

      addEvent(()-> {
          this.lockLook = false;
      }, 75);

      addEvent(()-> {
        this.setCastSpecial(false);
        this.setFightMode(false);
        this.setImmovable(false);
        castSpecialCooldown = 300;
      }, 85);
    };

    private final Consumer<EntityLivingBase> cast_basic = (target) -> {
      this.setCastBasic(true);
      this.setFightMode(true);
      this.setImmovable(true);

      addEvent(()-> this.playSound(SoundsHandler.GARGOYLE_CAST_BASIC, 1.0f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f)), 20);
      addEvent(()-> this.lockLook = true, 27);

      addEvent(()-> {
            //cast projectile
          ProjectileLightRing ring = new ProjectileLightRing(world, this, this.getAttack(), target);
          ring.setTravelRange(40F);
          ring.setPosition(this.posX, this.posY + 2.25, this.posZ);
          ring.shoot(this, this.rotationPitch + 5, this.rotationYaw, 0.0F, 0.65F, 0);
          world.spawnEntity(ring);
          this.lockLook = false;
      }, 35);

      addEvent(()-> {
        this.setCastBasic(false);
        this.setFightMode(false);
        this.setImmovable(false);
      }, 65);
    };

    private final Consumer<EntityLivingBase> swing_regular = (target) -> {
        this.setSwingMelee(true);
        this.setFightMode(true);
        this.setImmovable(true);

        addEvent(()-> {
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                this.lockLook = true;
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.16),0F);
            }, 6);
        }, 8);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.35f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.7f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 19);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 30);
        addEvent(()-> {
            this.setImmovable(false);
        this.setSwingMelee(false);
        this.setFightMode(false);
        }, 55);
    };

    private final Consumer<EntityLivingBase> swing_alternative = (target) -> {
      this.setSwingMeleeAlt(true);
      this.setFightMode(true);
      this.setImmovable(true);


      addEvent(()-> {
          Vec3d targetedPos = target.getPositionVector();
          addEvent(()-> {
              this.setImmovable(false);
              this.lockLook = true;
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.2),0F);
          }, 6);
      }, 9);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.35f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.7f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 22);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 30);

      addEvent(()-> {
          this.setImmovable(false);
        this.setSwingMeleeAlt(false);
        this.setFightMode(false);
      }, 55);
    };
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "hang_controlle", 40, this::predicateHanging));
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isSwingMelee()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingMeleeAlt()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_ALT, false));
                return PlayState.CONTINUE;
            }
            if(this.isCastBasic()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_BASIC, false));
                return PlayState.CONTINUE;
            }
            if(this.isCastSpecial()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_SPECIAL, false));
                return PlayState.CONTINUE;
            }
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateHanging(AnimationEvent<E> event) {
        if(this.isHanging() && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HANGING, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {

            if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isHanging() && !this.isFightMode()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
                return PlayState.CONTINUE;
            }


        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode() && !this.isHanging()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.GARGOYLE_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.GARGOYLE_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.GARGOYLE_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.GARGOYLE_STEP, 0.65F, 0.7f + ModRand.getFloat(0.3F));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }
    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "gargoyle_mage");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
