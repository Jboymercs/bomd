package com.dungeon_additions.da.entity.frost_dungeon.draugr;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.EntityDraugrRangedAI;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.frost_dungeon.EntityFrostBase;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import mod.azure.azurelib.util.MoveAnalysis;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class EntityDraugrRanger extends EntityFrostBase implements IAttack {

    private static final DataParameter<ItemStack> ITEM_HAND = EntityDataManager.<ItemStack>createKey(EntityDraugrRanger.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Integer> SKIN_TYPE = EntityDataManager.<Integer>createKey(EntityDraugrRanger.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> RANGED_ATTACK = EntityDataManager.createKey(EntityDraugrRanger.class, DataSerializers.BOOLEAN);

    private final String ANIM_IDLE = "idle";
    private final String ANIM_MODEL_ADJUST = "model_adjustments";
    private final String ANIM_WALK = "walk";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_THROW_BOMB = "throw_bomb";
    private Consumer<EntityLivingBase> prevAttack;

    public boolean isRangedAttack() {return this.dataManager.get(RANGED_ATTACK);}
    private void setRangedAttack(boolean value) {this.dataManager.set(RANGED_ATTACK, Boolean.valueOf(value));}

    public void setSkin(int skinType)
    {
        this.dataManager.set(SKIN_TYPE, Integer.valueOf(skinType));
    }

    public int getSkin()
    {
        return this.dataManager.get(SKIN_TYPE).intValue();
    }
    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, true);
    private final MoveAnalysis moveAnalysis;

    public EntityDraugrRanger(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 1.95F);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        this.equipBlock(RANGER_HAND.HAND, this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND));
        this.experienceValue = 8;
        this.setSkin(rand.nextInt(5));
        this.hemorrhage_resistance = 0.5F;
        this.falter_resistance = 0.8F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public  boolean draugrHasLoaded = false;

    public EntityDraugrRanger(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 1.95F);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        this.equipBlock(RANGER_HAND.HAND, this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND));
        this.experienceValue = 8;
        this.setSkin(rand.nextInt(5));
        this.hemorrhage_resistance = 0.5F;
        this.falter_resistance = 0.8F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Ranged_Attack", this.isRangedAttack());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setRangedAttack(nbt.getBoolean("Ranged_Attack"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(ITEM_HAND, ItemStack.EMPTY);
        this.getDataManager().register(SKIN_TYPE, Integer.valueOf(0));
        this.dataManager.register(RANGED_ATTACK, Boolean.valueOf(false));
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.draugr_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.draugr_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(14D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityDraugrRangedAI<>(this, 1, 60, 16, 0.3F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData) {
        this.setSkin(rand.nextInt(5));
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        return super.onInitialSpawn(difficulty, entityLivingData);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.moveAnalysis.update();
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            int randI = ModRand.range(1, 10);
            if(distance <= 5 && randI >= 7 && prevAttack == ranged_attack) {
                prevAttack = throw_bomb;
            } else {
                prevAttack = ranged_attack;
            }
            prevAttack.accept(target);
        }
        return prevAttack == throw_bomb ? 140 : 120;
    }

    private final Consumer<EntityLivingBase> ranged_attack = (target) -> {
        this.setFightMode(true);
        this.setRangedAttack(true);
        ItemStack stack = getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        addEvent(()-> {
            draugrHasLoaded = true;
            this.equipBlock(RANGER_HAND.HAND, new ItemStack(ModItems.FAKE_BOW));
        }, 20);

        addEvent(()-> {
            draugrHasLoaded = false;
            this.equipBlock(RANGER_HAND.HAND, new ItemStack(Items.BOW));
            EntityArrow arrow =new EntityTippedArrow(world, this);
            double d0 = target.posX - posX;
            double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3f) - arrow.posY;
            double d2 = target.posZ - posZ;
            double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            arrow.shoot(d0, d1 + d3 * 0.20000000298023224, d2, 1.6f, (float)(14 - world.getDifficulty().getId() * 4));
            playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1, 1f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
            arrow.setDamage(this.getAttack() * 0.5);
            world.spawnEntity(arrow);
        }, 50);

        addEvent(()-> {
            this.setFightMode(false);
            this.setRangedAttack(false);
        }, 60);
    };

    private final Consumer<EntityLivingBase> throw_bomb = (target) -> {
      animator.performAttack(ANIM_THROW_BOMB);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        //teleport
      addEvent(()-> {
          world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
          this.setImmovable(false);
          Vec3d randPos = new Vec3d(this.posX + (ModRand.range(-4, 4) + 8), this.posY + 1, this.posZ + (ModRand.range(-4, 4) + 8));
          ModUtils.attemptTeleport(randPos, this);
          playSound(SoundEvents.ITEM_FIRECHARGE_USE, 0.7F, 1f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
          List<EntityPlayer> nearbyEntities = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(4.0D), e -> !e.getIsInvulnerable());
          if(!nearbyEntities.isEmpty()) {
              for(EntityPlayer player : nearbyEntities) {
                  if(!player.isSpectator() && !player.isCreative()) {
                      if(!world.isRemote) {
                          player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0));
                      }
                  }
              }
          }
      }, 45);

      addEvent(()-> {
          this.setImmovable(true);
      }, 55);
      addEvent(()-> {
          this.setImmovable(false);
          this.setFullBodyUsage(false);
          this.setFightMode(false);
      }, 80);
    };

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);

        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(1, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(0.5)), ModColors.AZURE, pos.normalize().scale(0.1), ModRand.range(10, 15));
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1.0)), ModColors.AZURE, pos.normalize().scale(0.1), ModRand.range(10, 15));
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1.5)), ModColors.AZURE, pos.normalize().scale(0.1), ModRand.range(10, 15));
            });
        }
    }

    @Override
    public void updateAnimations() {
            if(!this.isFullBodyUsage()) {
                boolean isMovingOnGround = moveAnalysis.isMovingHorizontally() && this.onGround;
                if(isMovingOnGround) {
                    if(!this.isFightMode()) {
                        animator.setCustomUpperWalk(ANIM_WALK_UPPER);
                    }
                    animator.setCustomLowerWalk(ANIM_WALK);
                } else {
                    if(!this.isFightMode()) {
                        animator.setCustomUpperIdle(ANIM_MODEL_ADJUST);
                    }
                    animator.setCustomLowerIdle(ANIM_IDLE);
                }
            }
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.DRAUGR_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.DRAUGR_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.DRAUGR_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.DRAUGR_STEP, 0.5F, 0.4f + ModRand.getFloat(0.3F));
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "draugr_ranger");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }


    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    public enum RANGER_HAND {
        HAND("RHoldJoint");

        public String getBoneName() {
            return this.boneName;
        }

        private String boneName;

        RANGER_HAND(String bone) {
            this.boneName = bone;
        }

        @Nullable
        public static EntityDraugrRanger.RANGER_HAND getFromBoneName(String boneName) {
            if ("RHoldJoint".equals(boneName)) {
                return HAND;
            }
            return null;
        }

        public int getIndex() {
            if (this == RANGER_HAND.HAND) {
                return 1;
            }
            return 0;
        }
    }

    public void equipBlock(EntityDraugrRanger.RANGER_HAND hand, ItemStack state) {
        if(world.isRemote) {
            return;
        }
        if (hand == RANGER_HAND.HAND) {
            this.dataManager.set(ITEM_HAND, state);
        }
    }


    public ItemStack getItemFromKnightHand(EntityDraugrRanger.RANGER_HAND hand) {
        if (hand == RANGER_HAND.HAND) {
            return this.dataManager.get(ITEM_HAND);
        }
        return null;
    }
}
