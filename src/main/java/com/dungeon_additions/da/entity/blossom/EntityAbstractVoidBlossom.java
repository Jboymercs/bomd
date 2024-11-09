package com.dungeon_additions.da.entity.blossom;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.Sys;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class EntityAbstractVoidBlossom extends EntityAbstractBase implements IEntityMultiPart, IPitch {

    private static final DataParameter<Boolean> SPIKE_ATTACK = EntityDataManager.createKey(EntityAbstractVoidBlossom.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPIKE_WAVE = EntityDataManager.createKey(EntityAbstractVoidBlossom.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LEAF_ATTACK = EntityDataManager.createKey(EntityAbstractVoidBlossom.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPORE_ATTACK = EntityDataManager.createKey(EntityAbstractVoidBlossom.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> DEATH_STATE = EntityDataManager.createKey(EntityAbstractVoidBlossom.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Float> STAT_LINE = EntityDataManager.createKey(EntityAbstractVoidBlossom.class, DataSerializers.FLOAT);

    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityAbstractVoidBlossom.class, DataSerializers.FLOAT);

    private final MultiPartEntityPart[] hitboxParts;

    private final MultiPartEntityPart model = new MultiPartEntityPart(this, "model", 0.0f, 0.0f);
    private final MultiPartEntityPart baseStem = new MultiPartEntityPart(this, "stem", 1.5f, 7.0f);
    private final MultiPartEntityPart flowerTopStem = new MultiPartEntityPart(this, "topStem", 1.5f, 1.5f);

    private final MultiPartEntityPart flowerMiddle = new MultiPartEntityPart(this, "middleHead", 1.0f, 3.5f);
    private final MultiPartEntityPart flowerLeft = new MultiPartEntityPart(this, "leftHead", 1.0f, 3.0f);
    private final MultiPartEntityPart flowerRight = new MultiPartEntityPart(this, "rightHead", 1.0f, 3.0f);

    public EntityAbstractVoidBlossom(World worldIn) {
        super(worldIn);
        this.setSize(5.0f, 10.0f);
        this.experienceValue = 1000;
        this.hitboxParts = new MultiPartEntityPart[]{model, baseStem, flowerTopStem, flowerMiddle, flowerLeft, flowerRight};

    }


    @Override
    public void entityInit() {
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(SPIKE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SPIKE_WAVE, Boolean.valueOf(false));
        this.dataManager.register(LEAF_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SPORE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(DEATH_STATE, Boolean.valueOf(false));
        this.dataManager.register(STAT_LINE,  0.75F);
        super.entityInit();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Spike_Attack", this.isSpikeAttack());
        nbt.setBoolean("Spike_Wave", this.isSpikeWave());
        nbt.setBoolean("Leaf_Attack", this.isLeafAttack());
        nbt.setBoolean("Spore_Attack", this.isSporeAttack());
        nbt.setBoolean("Death_State", this.isDeathState());
        nbt.setFloat("Look", this.getPitch());
        nbt.setFloat("Stat_Line", this.getStatLine());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSpikeAttack(nbt.getBoolean("Spike_Attack"));
        this.setSpikeWave(nbt.getBoolean("Spike_Wave"));
        this.setLeafAttack(nbt.getBoolean("Leaf_Attack"));
        this.setSporeAttack(nbt.getBoolean("Spore_Attack"));
        this.setDeathState(nbt.getBoolean("Death_State"));
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
        this.setStateLine(nbt.getFloat("Stat_Line"));
        super.readEntityFromNBT(nbt);
    }
    protected boolean hasLaunchedFlowersOne = false;
    protected boolean hasLaunchedFlowersTwice = false;
    protected boolean hasLaunchedFlowersThree = false;


    public int minionCooldown = 400;
    @Override
    public void onUpdate() {
        super.onUpdate();

        double currentHealth = this.getHealth() / this.getMaxHealth();

        //Cool this works!
        if(!world.isRemote) {
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable());
            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase base : nearbyEntities) {
                    if(base != this && !(base instanceof EntityVoidSpike) && !(base instanceof EntityGenericWave) && !(base instanceof EntityMiniBlossom)) {
                            Vec3d pos = base.getPositionVector().add(ModUtils.yVec(0.5));
                            DamageSource source = ModDamageSource.builder()
                                    .type(ModDamageSource.MOB)
                                    .directEntity(this)
                                    .build();
                            float damage = (float) (this.getAttack());
                            ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, pos, source, 0.2F, 0, false);
                            this.playSound(SoundEvents.ENCHANT_THORNS_HIT, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);

                    }
                }
            }


            minionCooldown--;

            if (currentHealth < 0.75 && !hasLaunchedFlowersOne) {
                for (int i = 0; i < 5; i++) {
                    AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
                    BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.INDESTRUCTABLE_BLOCK.getDefaultState());
                    if (setTooPos != null) {
                        world.setBlockState(setTooPos.add(0, 1, 0), ModBlocks.SPORE_BLOSSOM.getDefaultState());

                        hasLaunchedFlowersOne = true;
                    }

                }
            }
            if (hasLaunchedFlowersOne) {
                AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
                BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.SPORE_BLOSSOM.getDefaultState());
                if (setTooPos == null) {
                    this.setStateLine((float) 0.5);
                }
            }

            if (currentHealth < 0.50 && !hasLaunchedFlowersTwice) {
                for (int i = 0; i < 7; i++) {
                    AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
                    BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.INDESTRUCTABLE_BLOCK.getDefaultState());
                    if (setTooPos != null) {
                        world.setBlockState(setTooPos.add(0, 1, 0), ModBlocks.SPORE_BLOSSOM.getDefaultState());

                        hasLaunchedFlowersTwice = true;
                    }

                }
            }
            if (hasLaunchedFlowersTwice) {
                AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
                BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.SPORE_BLOSSOM.getDefaultState());
                if (setTooPos == null) {
                    this.setStateLine((float) 0.25);
                }
            }

            if (currentHealth < 0.25 && !hasLaunchedFlowersThree) {
                for (int i = 0; i < 8; i++) {
                    AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
                    BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.INDESTRUCTABLE_BLOCK.getDefaultState());
                    if (setTooPos != null) {
                        world.setBlockState(setTooPos.add(0, 1, 0), ModBlocks.SPORE_BLOSSOM.getDefaultState());

                        hasLaunchedFlowersThree = true;
                    }

                }
            }
            if (hasLaunchedFlowersThree) {
                AxisAlignedBB box = getEntityBoundingBox().grow(20, 8, 20);
                BlockPos setTooPos = ModUtils.searchForBlocks(box, world, this, ModBlocks.SPORE_BLOSSOM.getDefaultState());
                if (setTooPos == null) {
                    this.setStateLine(0);
                }
            }
        }
    }


    public void setSpikeAttack(boolean value) {this.dataManager.set(SPIKE_ATTACK, Boolean.valueOf(value));}
    public void setSpikeWave(boolean value) {this.dataManager.set(SPIKE_WAVE, Boolean.valueOf(value));}
    public void setLeafAttack(boolean value) {this.dataManager.set(LEAF_ATTACK, Boolean.valueOf(value));}
    public void setSporeAttack(boolean value) {this.dataManager.set(SPORE_ATTACK, Boolean.valueOf(value));}
    public void setDeathState(boolean value) {this.dataManager.set(DEATH_STATE, Boolean.valueOf(value));}

    public boolean isSpikeAttack() {return this.dataManager.get(SPIKE_ATTACK);}
    public boolean isSpikeWave() {return this.dataManager.get(SPIKE_WAVE);}
    public boolean isLeafAttack() {return this.dataManager.get(LEAF_ATTACK);}
    public boolean isSporeAttack() {return this.dataManager.get(SPORE_ATTACK);}
    public boolean isDeathState() {return this.dataManager.get(DEATH_STATE);}
    public void setStateLine(float value) {
        this.dataManager.set(STAT_LINE, Float.valueOf(value));
    }
    public float getStatLine() {
        return this.dataManager.get(STAT_LINE);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(27D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.blossom_attack_damange);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.blossom_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.blossom_armor);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        Vec3d[] avec3d = new Vec3d[this.hitboxParts.length];
        for (int j = 0; j < this.hitboxParts.length; ++j) {
            avec3d[j] = new Vec3d(this.hitboxParts[j].posX, this.hitboxParts[j].posY, this.hitboxParts[j].posZ);
        }

        if(this.isSpikeAttack()) {
            this.setHitBoxPos(baseStem, new Vec3d(0, -3, 0));
            this.setHitBoxPos(flowerTopStem, new Vec3d(-1.5, 3.0, 0));
            this.setHitBoxPos(flowerMiddle, new Vec3d(-2.8, 1.75, 0));
            this.setHitBoxPos(flowerLeft, new Vec3d(-2.8, 2, -1.0));
            this.setHitBoxPos(flowerRight, new Vec3d(-2.8, 2, 1.0));
        } else if (this.isSporeAttack() || this.isLeafAttack()) {
            this.setHitBoxPos(baseStem, new Vec3d(0, 0, 0));
            this.setHitBoxPos(flowerTopStem, new Vec3d(0, 4.0, 0));
            this.setHitBoxPos(flowerMiddle, new Vec3d(0, -6.5, 0));
            this.setHitBoxPos(flowerLeft, new Vec3d(0, -6.75, -1.0));
            this.setHitBoxPos(flowerRight, new Vec3d(0, -6.75, 1.0));
        } else {
            this.setHitBoxPos(baseStem, new Vec3d(0, 0, 0));
            this.setHitBoxPos(flowerTopStem, new Vec3d(-1.5, 6.0, 0));
            this.setHitBoxPos(flowerMiddle, new Vec3d(-2.8, 4.5, 0));
            this.setHitBoxPos(flowerLeft, new Vec3d(-2.8, 4.75, -1.0));
            this.setHitBoxPos(flowerRight, new Vec3d(-2.8, 4.75, 1.0));
        }


        Vec3d knightPos = this.getPositionVector();
        ModUtils.setEntityPosition(model, knightPos);

        for (int l = 0; l < this.hitboxParts.length; ++l) {
            this.hitboxParts[l].prevPosX = avec3d[l].x;
            this.hitboxParts[l].prevPosY = avec3d[l].y;
            this.hitboxParts[l].prevPosZ = avec3d[l].z;
        }
    }

    private void setHitBoxPos(Entity entity, Vec3d offset) {
        Vec3d lookVel = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
        Vec3d center = this.getPositionVector().add(ModUtils.yVec(1.2));

        Vec3d position = center.subtract(ModUtils.Y_AXIS.add(ModUtils.getAxisOffset(lookVel, offset)));
        ModUtils.setEntityPosition(entity, position);

    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Entity[] getParts() {
        return this.hitboxParts;
    }

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        this.dataManager.set(LOOK, clampedLook);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    //Damage Handling
    @Override
    public boolean attackEntityFromPart(@Nonnull MultiPartEntityPart part, @Nonnull DamageSource source, float damage) {
        if(part == flowerLeft || part == flowerMiddle || part== flowerRight) {
            if(!this.isSporeAttack() && !this.isSpikeWave() && !this.isLeafAttack()) {
                return this.attackEntityFrom(source, damage);
            } else {
                return false;
            }
        }
        if(part == baseStem || part == flowerTopStem) {
            return this.attackEntityFrom(source, ( damage * (float) 0.75));
        }
        return false;
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount);
    }
}
