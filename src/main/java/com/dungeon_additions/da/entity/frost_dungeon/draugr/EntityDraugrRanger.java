package com.dungeon_additions.da.entity.frost_dungeon.draugr;

import com.dungeon_additions.da.entity.ai.EntityDraugrMeleeAI;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.frost_dungeon.EntityFrostBase;
import com.dungeon_additions.da.util.ModRand;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class EntityDraugrRanger extends EntityFrostBase implements IAttack, IAnimatable, IAnimationTickable {

    private static final DataParameter<ItemStack> ITEM_HAND = EntityDataManager.<ItemStack>createKey(EntityDraugrRanger.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Integer> SKIN_TYPE = EntityDataManager.<Integer>createKey(EntityDraugrRanger.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> RANGED_ATTACK = EntityDataManager.createKey(EntityDraugrRanger.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> THROW_BOMB = EntityDataManager.createKey(EntityDraugrRanger.class, DataSerializers.BOOLEAN);

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;

    private boolean isRangedAttack() {return this.dataManager.get(RANGED_ATTACK);}
    private void setRangedAttack(boolean value) {this.dataManager.set(RANGED_ATTACK, Boolean.valueOf(value));}
    private boolean isThrowBomb() {return this.dataManager.get(THROW_BOMB);}
    private void setThrowBomb(boolean value) {this.dataManager.set(THROW_BOMB, Boolean.valueOf(value));}

    public void setSkin(int skinType)
    {
        this.dataManager.set(SKIN_TYPE, Integer.valueOf(skinType));
    }

    public int getSkin()
    {
        return this.dataManager.get(SKIN_TYPE).intValue();
    }

    public EntityDraugrRanger(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 1.95F);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        this.experienceValue = 8;
    }

    public EntityDraugrRanger(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 1.95F);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        this.experienceValue = 8;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Throw_Bomb", this.isThrowBomb());
        nbt.setBoolean("Ranged_Attack", this.isRangedAttack());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setRangedAttack(nbt.getBoolean("Ranged_Attack"));
        this.setThrowBomb(nbt.getBoolean("Throw_Bomb"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(ITEM_HAND, new ItemStack(this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem()));
        this.getDataManager().register(SKIN_TYPE, Integer.valueOf(0));
        this.dataManager.register(THROW_BOMB, Boolean.valueOf(false));
        this.dataManager.register(RANGED_ATTACK, Boolean.valueOf(false));
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(12D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(14D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
      //  this.tasks.addTask(4, new EntityDraugrMeleeAI<>(this, 1, 30, 4, 0.15F));
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
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        return 0;
    }

    @Override
    public void registerControllers(AnimationData data) {

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
