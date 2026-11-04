package com.dungeon_additions.da.entity;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.EntityAiTimedAttack;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameBase;
import com.dungeon_additions.da.entity.projectiles.ProjectileAbberrantAttack;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityNetherAbberrant extends EntityFlameBase implements IAttack {
    /**
     * The Locals of the Nether Dungeon, A specialist of it's era.
     */

    private final String MOVE_ANIM = "move";
    private final String IDLE_ANIM = "idle";
    private final String BLINK = "blink";
    private final String RANGED_ANIM = "castSpell";
    private final String STRIKE_ANIM = "simpleStrike";
    private final String DASH_ANIM = "dashAway";
    private final String SPIN_ANIM = "spin";

    private static DataParameter<Boolean> RANGED = EntityDataManager.createKey(EntityNetherAbberrant.class, DataSerializers.BOOLEAN);


    private Consumer<EntityLivingBase> prevAttacks;

    public boolean meleeMode;
    public boolean rangedMode;

    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);
    private final MoveAnalysis moveAnalysis;


    public EntityNetherAbberrant(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setSize(0.9f, 1.95f);
        this.rangedMode = true;
        this.setImmovable(false);
        this.experienceValue = 12;
        this.falter_resistance = 0.8F;
        this.hemorrhage_resistance = 0.2F;
        this.moveAnalysis = new MoveAnalysis(this);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(RANGED, Boolean.valueOf(false));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Ranged", this.isRanged());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setRanged(nbt.getBoolean("Ranged"));
        super.readEntityFromNBT(nbt);
    }


    public void setRanged(boolean value) {
        this.dataManager.set(RANGED, Boolean.valueOf(value));
    }

    public boolean isRanged() {
        return this.dataManager.get(RANGED);
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(rangedMode) {
            if(rand.nextInt(300) == 0) {
                meleeMode = true;
                rangedMode = false;
            }
        }
        if(meleeMode) {
            if(rand.nextInt(300) == 0) {
                rangedMode = true;
                meleeMode = false;
            }
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.abberrant_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.34D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.abberrant_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.6D);
    }


    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        EntityLivingBase target = this.getAttackTarget();
        if(target != null && !world.isRemote) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            if(meleeMode) {
                if(distance > 4 && !this.isBeingRidden()) {
                    if(!this.isRanged()) {
                        //Move to melee combat
                        double d0 = (target.posX - this.posX) * 0.02;
                        double d1 = (target.posY - this.posY) * 0.03;
                        double d2 = (target.posZ - this.posZ) * 0.02;
                        this.addVelocity(d0, d1, d2);
                    }
                }
            }
        }

    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAiTimedAttack<>(this, 1.0, 20, 14F, 0.3F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPigZombie>(this, EntityPigZombie.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void updateAnimations() {
        animator.setStateAnim(BLINK, true);
        if(!this.isFightMode()) {
            boolean isMovingOnGround = moveAnalysis.isMoving();
            if(isMovingOnGround) {
                animator.setCustomUpperWalk(MOVE_ANIM);
            } else {
                animator.setCustomUpperIdle(IDLE_ANIM);
            }
        }
    }


    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {

            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(strikeMelee, RangedAttack, dashAwayMove, spinCircle));
            double[] weights = {
                    (distance < 3) ? 1/distance : 0, // SImple Strike
                    (distance > 3) ? distance * 0.02 : 0, // Ranged Attack
                    (distance < 6 && rangedMode) ? 2/distance : 0, // Dash Away - Ranged Only
                    (distance < 6 && prevAttacks != spinCircle) ? 1/distance : 0 // Spin Circle Attack
            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return (prevAttacks == RangedAttack) ? 100 : 20;
    }
    // Simple Melee Strike
    private Consumer<EntityLivingBase> strikeMelee = (target) -> {
        this.setFightMode(true);
        animator.performAttack(STRIKE_ANIM);
        addEvent(() -> {
            ModUtils.leapTowards(this, target.getPositionVector(), 0.5f, 0.2f);
        }, 10);

        addEvent(() -> {
            Vec3d offset = getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB)
                    .directEntity(this)
                    .build();
            float damage = getAttack();
            ModUtils.handleAreaImpact(0.8f, (e) -> damage, this, offset, source, 0.4f, 0, false, 0.2F);
            playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / getRNG().nextFloat() * 0.4f + 0.8f);
        }, 14);


        addEvent(() -> {
            this.setFightMode(false);
        }, 22);
    };


    @Override
    protected boolean canDespawn() {

        // Edit this to restricting them not despawning in Dungeons
        return false;

    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "nether_abberrant");

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
        return SoundsHandler.ABBERRANT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.ABBERRANT_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.ABBERRANT_IDLE;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
       // this.playSound(SoundsHandler.DRAUGR_STEP, 0.5F, 0.4f + ModRand.getFloat(0.3F));
    }

    @Override
    public boolean getCanSpawnHere() {
        return super.getCanSpawnHere();
    }
    private Consumer<EntityLivingBase> RangedAttack = (target) -> {
        this.setRanged(true);
        this.setFightMode(true);
        animator.performAttack(RANGED_ANIM);

        addEvent(() -> {

            ProjectileAbberrantAttack projectile = new ProjectileAbberrantAttack(this.world, this, this.getAttack());
            Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.8, 1.3, 0.5)));
            Vec3d targetPos = new Vec3d(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            Vec3d velocity = targetPos.subtract(pos).normalize().scale(0.4);
            projectile.setPosition(pos.x, pos.y, pos.z);
            projectile.setTravelRange(16f);
            ModUtils.setEntityVelocity(projectile, velocity);
            this.getLookVec();
            world.spawnEntity(projectile);
            playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f / getRNG().nextFloat() * 0.4f + 0.8f);


        }, 20);

        addEvent(() -> {
            this.setRanged(false);
            this.setFightMode(false);
        }, 40);
    };

    private Consumer<EntityLivingBase> dashAwayMove = (target) -> {
        this.setFightMode(true);
        animator.performAttack(DASH_ANIM);
        addEvent(()-> {
            Vec3d randomPoint = new Vec3d(ModRand.getFloat(10), ModRand.getFloat(1), ModRand.getFloat(10)).add(this.getPositionVector());
            Vec3d away = this.getPositionVector().subtract(randomPoint).normalize();
            ModUtils.leapTowards(this, away, 0.9f, 0.4f);
        }, 5);

        addEvent(() -> {
            this.setFightMode(false);
        }, 20);
    };
    // Spin Attack
    private Consumer<EntityLivingBase> spinCircle = (target) -> {
        this.setFightMode(true);
        animator.performAttack(SPIN_ANIM);

        addEvent(()-> {
            for(int i = 0; i < 20; i += 5) {
                addEvent(()-> {
                    Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0)));
                    float damage = (float) (this.getAttack() * 0.8);
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB)
                            .directEntity(this).build();
                    ModUtils.handleAreaImpact(2.8f, (e) -> damage, this, pos, source, 0.3f, 0, false, 0.3F);
                    playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / getRNG().nextFloat() * 0.4f + 0.5f);
                }, i);
            }

        }, 18);

        addEvent(()-> {
            for(int i = 0; i < 20; i += 5) {
                addEvent(()-> {
                    Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0)));
                    float damage = (float) (this.getAttack() * 0.8);
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB)
                            .directEntity(this).build();
                    ModUtils.handleAreaImpact(2.8f, (e) -> damage, this, pos, source, 0.3f, 0, false, 0.3F);
                    playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / getRNG().nextFloat() * 0.4f + 0.5f);
                }, i);
            }

        }, 50);

        addEvent(() -> {
            this.setFightMode(false);
        }, 85);

    };

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        EntityLivingBase target = this.getAttackTarget();
        if ( target instanceof EntityNetherAbberrant) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }
}
