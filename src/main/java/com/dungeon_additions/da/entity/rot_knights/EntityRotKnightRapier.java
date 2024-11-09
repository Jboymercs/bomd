package com.dungeon_additions.da.entity.rot_knights;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.EntityAIAttackRotKnightRapier;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityRotKnightRapier extends EntityAbstractBase implements IAttack, IAnimatable, IAnimationTickable {

    private final String ANIM_IDLE = "idle";
    private final String ANIM_IDLE_SHIELD = "idle_shield";
    private final String ANIM_WALK_LOWER = "walk";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_WALK_UPPER_SHIELD = "walk_upper_shield";

    private final String ANIM_PIERCE_ATTACK = "pierce";
    private final String ANIM_SWING_ATTACK = "swing";
    private final String ANIM_PIERCE_COMBO = "pierce_extra";
    private final String ANIM_DRINK_POTION = "drink_potion";

    private static final DataParameter<Boolean> PIERCE = EntityDataManager.createKey(EntityRotKnightRapier.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING = EntityDataManager.createKey(EntityRotKnightRapier.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_COMBO = EntityDataManager.createKey(EntityRotKnightRapier.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DRINK_POTION = EntityDataManager.createKey(EntityRotKnightRapier.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> ITEM_HAND = EntityDataManager.<ItemStack>createKey(EntityRotKnightRapier.class, DataSerializers.ITEM_STACK);

    private static final DataParameter<Boolean> IDLE_MODE = EntityDataManager.createKey(EntityRotKnightRapier.class, DataSerializers.BOOLEAN);
    private boolean isPierce() {return this.dataManager.get(PIERCE);}
    private boolean isSwing() {return this.dataManager.get(SWING);}
    private boolean isPierceCombo() {return this.dataManager.get(PIERCE_COMBO);}
    public boolean isDrinkPotion() {return this.dataManager.get(DRINK_POTION);}
    public boolean isIdleMode() {return this.dataManager.get(IDLE_MODE);}

    private void setPierce(boolean value) {this.dataManager.set(PIERCE, Boolean.valueOf(value));}
    private void setSwing(boolean value) {this.dataManager.set(SWING, Boolean.valueOf(value));}
    private void setPierceCombo(boolean value) {this.dataManager.set(PIERCE_COMBO, Boolean.valueOf(value));}
    public void setDrinkPotion(boolean value) {this.dataManager.set(DRINK_POTION, Boolean.valueOf(value));}
    public void setIdleMode(boolean value) {this.dataManager.set(IDLE_MODE, Boolean.valueOf(value));}

    public EntityRotKnightRapier(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.75F, 1.95F);
        selectAnimationTooPlay();
        this.setIdleMode(true);
    }

    public EntityRotKnightRapier(World worldIn) {
        super(worldIn);
        this.setSize(0.75F, 1.95F);
        selectAnimationTooPlay();
        this.setIdleMode(true);
    }

    protected int selection = ModRand.range(1, 5);
    private String ANIM_SELECTION_STRING;
    private final String ANIM_IDLE_1 = "idle_1";
    private final String ANIM_IDLE_2 = "idle_2";
    private final String ANIM_IDLE_3 = "idle_3";
    private final String ANIM_IDLE_4 = "idle_4";
    private float randomTurn = ModRand.range(1, 360);

    private boolean hasEndedState = false;
    public void selectAnimationTooPlay() {
        if(selection == 1) {
            ANIM_SELECTION_STRING = ANIM_IDLE_1;
        } else if (selection == 2) {
            ANIM_SELECTION_STRING = ANIM_IDLE_2;
        }else if(selection == 3) {
            ANIM_SELECTION_STRING = ANIM_IDLE_3;
        }else if(selection == 4) {
            ANIM_SELECTION_STRING = ANIM_IDLE_4;
        } else {
            ANIM_SELECTION_STRING = ANIM_IDLE_1;
        }
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Pierce", this.isPierce());
        nbt.setBoolean("Swing", this.isSwing());
        nbt.setBoolean("Pierce_Combo", this.isPierceCombo());
        nbt.setBoolean("Drink_Potion", this.isDrinkPotion());
        nbt.setBoolean("Idle_State", this.isIdleMode());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setPierce(nbt.getBoolean("Pierce"));
        this.setSwing(nbt.getBoolean("Swing"));
        this.setPierceCombo(nbt.getBoolean("Pierce_Combo"));
        this.setDrinkPotion(nbt.getBoolean("Drink_Potion"));
        this.setIdleMode(nbt.getBoolean("Idle_State"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(ITEM_HAND, ItemStack.EMPTY);
        this.dataManager.register(PIERCE, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_COMBO, Boolean.valueOf(false));
        this.dataManager.register(DRINK_POTION, Boolean.valueOf(false));
        this.dataManager.register(SWING, Boolean.valueOf(false));
        this.dataManager.register(IDLE_MODE, Boolean.valueOf(false));
    }

    private final AnimationFactory factory = new AnimationFactory(this);
    private boolean hasDrankPotion = false;
    private Consumer<EntityLivingBase> prevAttack;
    public boolean isRandomGetAway = false;


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.rot_knights_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.rot_knights_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.rot_knights_armor);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.isIdleMode() && !hasEndedState && !world.isRemote) {
            this.rotationYaw = randomTurn;
            this.rotationYawHead = randomTurn;
            this.setImmovable(true);
        }

        EntityLivingBase target = this.getAttackTarget();
        if(target != null && !world.isRemote) {

            if(!hasEndedState && this.getDistanceSq(target) <= 24 && this.isIdleMode()) {
                this.endIdleState();
            }
            if(this.isRandomGetAway) {
                double d0 = (this.posX - target.posX) * 0.009;
                double d1 = (this.posY - target.posY) * 0.005;
                double d2 = (this.posZ - target.posZ) * 0.009;
                this.addVelocity(d0, d1, d2);
                this.faceEntity(target, 35, 35);
                this.getLookHelper().setLookPositionWithEntity(target, 35, 35);
            }
        }

    }

    private void endIdleState() {
        this.hasEndedState = true;

        addEvent(()-> {
            this.setIdleMode(false);
            this.setImmovable(false);
        }, 10);

    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIAttackRotKnightRapier<>(this, 1.1, 20, 5, 0F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityZombie>(this, EntityZombie.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityZombieVillager>(this, EntityZombieVillager.class, 1, true, false, null));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget<EntityHusk>(this, EntityHusk.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double healthF = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && !isRandomGetAway && !this.isIdleMode()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(randomGetAway, basic_swing, pierce, pierce_combo, drink_potion));
            double[] weights = {
                    (distance <= 5 && prevAttack != randomGetAway) ? 1/distance : 0,
                    (distance <= 3 && prevAttack != basic_swing) ? 1/distance : 0,
                    (distance <= 5 && prevAttack != pierce && prevAttack != pierce_combo) ? 1/distance : 0,
                    (distance <= 5 && prevAttack != pierce_combo) ? 1/distance : 0,
                    (healthF <= 0.5 && !hasDrankPotion) ? 1/distance : 0
            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        return 30;
    }

    private final Consumer<EntityLivingBase> drink_potion = (target) -> {
      this.setFightMode(true);
      this.setDrinkPotion(true);
      this.isRandomGetAway = true;
      this.equipItemInHand(ROT_KNIGHT_HAND.HAND, new ItemStack(ModItems.FAKE_HEALING_POTION));

      addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 15);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 20);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 25);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 30);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 35);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 40);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 45);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 50);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0f, 0.9f), 55);
      addEvent(()-> {
          //heal
        double healthTooHeal = this.getMaxHealth() * 0.25;
        this.heal((float) healthTooHeal);
          this.equipItemInHand(ROT_KNIGHT_HAND.HAND, new ItemStack(ModItems.INVISISBLE_ITEM));
      }, 55);


      addEvent(()-> {
          this.isRandomGetAway = false;
        this.setFightMode(false);
        this.setDrinkPotion(false);
        this.hasDrankPotion = true;
      }, 70);
    };

    private final Consumer<EntityLivingBase> pierce_combo = (target) -> {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setPierceCombo(true);
        this.setImmovable(true);

        addEvent(()-> {
            Vec3d targetedpos = target.getPositionVector();
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedpos);
                ModUtils.leapTowards(this, targetedpos, (float) (distance * 0.18),0.25F);
            }, 10);
        }, 12);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 30);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 35);

        addEvent(()-> {
            this.setImmovable(false);
            Vec3d targetedpos = target.getPositionVector();
            double distance = this.getPositionVector().distanceTo(targetedpos);
            ModUtils.leapTowards(this, targetedpos, (float) (distance * 0.08),0.25F);
        }, 40);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.8, 1.3, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 45);


        addEvent(()-> this.setImmovable(true), 50);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedpos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedpos);
                ModUtils.leapTowards(this, targetedpos, (float) (distance * 0.22),0.25F);
            }, 10);
        }, 60);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 1.3, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 77);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 83);

        addEvent(()-> {
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            this.setFightMode(false);
            this.setPierceCombo(false);
        }, 100);
    };

    private final Consumer<EntityLivingBase> pierce = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setPierce(true);
      this.setImmovable(true);

      addEvent(()-> {
        Vec3d targetedpos = target.getPositionVector();
        this.lockLook = true;
        addEvent(()-> {
            this.setImmovable(false);
            double distance = this.getPositionVector().distanceTo(targetedpos);
            ModUtils.leapTowards(this, targetedpos, (float) (distance * 0.18),0.25F);
        }, 10);
      }, 12);


      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.0, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 30);

      addEvent(()-> {
          this.setImmovable(true);
          this.lockLook = false;
      }, 35);

      addEvent(()-> {
          this.setImmovable(false);
          Vec3d targetedpos = target.getPositionVector();
          double distance = this.getPositionVector().distanceTo(targetedpos);
          ModUtils.leapTowards(this, targetedpos, (float) (distance * 0.08),0.25F);
      }, 40);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.8, 1.3, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 45);


      addEvent(()-> this.setImmovable(true), 50);

      addEvent(()-> {
          this.setImmovable(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setPierce(false);
      }, 65);
    };
    private final Consumer<EntityLivingBase> basic_swing = (target) -> {
      this.setSwing(true);
      this.setFightMode(true);
        addEvent(()-> this.lockLook = true, 9);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.9, 1.3, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            this.setImmovable(true);
        }, 19);

        addEvent(()-> this.lockLook = false, 30);

      addEvent(()-> {
          this.setImmovable(false);
        this.setSwing(false);
        this.setFightMode(false);
      }, 35);
    };


    private final Consumer<EntityLivingBase> randomGetAway = (target) -> {
        this.isRandomGetAway = true;
        addEvent(()-> this.isRandomGetAway = false, 40);
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "statue_controller", 0, this::predicateIdleStatues));
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if (!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode() && !this.isIdleMode()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage() && !this.isIdleMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdleStatues(AnimationEvent<E> event) {
        if(this.isIdleMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SELECTION_STRING, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
     if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode() && !this.isIdleMode()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isIdleMode()) {
            if(this.isPierce()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PIERCE_ATTACK, false));
            }
            if(this.isSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_ATTACK, false));
            }
            if(this.isPierceCombo()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PIERCE_COMBO, false));
            }
            if(this.isDrinkPotion()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DRINK_POTION, false));
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if(source.getImmediateSource() == this || source.getImmediateSource() instanceof EntityRotKnight || this.isIdleMode()) {
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "rot_knight");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public enum ROT_KNIGHT_HAND {
        HAND("HandEquip");

        public String getBoneName() {
            return this.boneName;
        }

        private String boneName;

        ROT_KNIGHT_HAND(String bone) {
            this.boneName = bone;
        }

        @Nullable
        public static EntityRotKnightRapier.ROT_KNIGHT_HAND getFromBoneName(String boneName) {
            if ("HandEquip".equals(boneName)) {
                return HAND;
            }
            return null;
        }

        public int getIndex() {
            if (this == EntityRotKnightRapier.ROT_KNIGHT_HAND.HAND) {
                return 1;
            }
            return 0;
        }
    }

    public void equipItemInHand(ROT_KNIGHT_HAND head, ItemStack state) {
        if(world.isRemote) {
            return;
        }
        if (head == ROT_KNIGHT_HAND.HAND) {
            this.dataManager.set(ITEM_HAND, state);
        }
    }


    public ItemStack getItemFromKnightHand(ROT_KNIGHT_HAND head) {
        if (head == ROT_KNIGHT_HAND.HAND) {
            return this.dataManager.get(ITEM_HAND);
        }
        return null;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }


    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
