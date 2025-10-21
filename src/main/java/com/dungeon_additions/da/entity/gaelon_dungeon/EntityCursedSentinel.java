package com.dungeon_additions.da.entity.gaelon_dungeon;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.gaelon_dungeon.EntityReAnimateAttackAI;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.function.Consumer;

public class EntityCursedSentinel extends EntityGaelonBase implements IAnimatable, IAnimationTickable, IAttack {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;


    public EntityCursedSentinel(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 45;
    }

    public EntityCursedSentinel(World worldIn) {
        super(worldIn);
        this.experienceValue = 45;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();

    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(12);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(65);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
      //  this.tasks.addTask(4, new EntityReAnimateAttackAI<>(this, 1.0D, 10, 7, 0.3F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityDarkBase>(this, EntityDarkBase.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

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

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
      return super.attackEntityFrom(source, amount);
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "reanimate");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return this.isDropsLoot();
    }
}
