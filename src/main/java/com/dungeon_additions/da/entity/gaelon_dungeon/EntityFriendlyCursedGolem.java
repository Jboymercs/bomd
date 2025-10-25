package com.dungeon_additions.da.entity.gaelon_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.ai.gaelon_dungeon.EntityAICursedGolemAttack;
import com.dungeon_additions.da.entity.ai.gaelon_dungeon.EntityFriendlyCursedGolemAttack;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.frost_dungeon.wyrk.EntityFriendWyrk;
import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.ProjectileFastGhostCrystal;
import com.dungeon_additions.da.entity.gaelon_dungeon.friendly.EntityFriendlyCursedRevenant;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.sky_dungeon.friendly.EntityFriendlyHalberd;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
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

public class EntityFriendlyCursedGolem extends EntityGaelonBase implements IAnimatable, IAnimationTickable, IAttack, IScreenShake {
    private int shakeTime = 0;
    private final String ANIM_SWING = "swing";
    private final String ANIM_DOUBLE_SWING = "swing_two";
    private final String ANIM_OVERHEAD_SWING = "overhead_swing";
    private final String ANIM_SHOOT_CANNON = "shoot_cannon";
    private final String ANIM_MORTAR_FIRE = "mortar_fire";

    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_WALK_LOWER = "walk_lower";

    private final String ANIM_INANIMATE = "inanimate";
    private final String ANIM_AWAKEN = "awake";

    private static final DataParameter<Boolean> SWING = EntityDataManager.createKey(EntityFriendlyCursedGolem.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_TWO = EntityDataManager.createKey(EntityFriendlyCursedGolem.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> OVERHEAD_SWING = EntityDataManager.createKey(EntityFriendlyCursedGolem.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHOOT_CANNON = EntityDataManager.createKey(EntityFriendlyCursedGolem.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MORTAR_FIRE = EntityDataManager.createKey(EntityFriendlyCursedGolem.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> AWAKE_STATE = EntityDataManager.createKey(EntityFriendlyCursedGolem.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> INANIMATE_STATE = EntityDataManager.createKey(EntityFriendlyCursedGolem.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityFriendlyCursedGolem.class, DataSerializers.BOOLEAN);
    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityFriendlyCursedGolem.class, DataSerializers.BLOCK_POS);
    public static DataParameter<Boolean> SET_SPAWN_LOC = EntityDataManager.createKey(EntityFriendlyCursedGolem.class, DataSerializers.BOOLEAN);

    private void setSwingAttack(boolean value) {this.dataManager.set(SWING, Boolean.valueOf(value));}
    private boolean isSwingAttack() {return this.dataManager.get(SWING);}
    private void setSwingTwo(boolean value) {this.dataManager.set(SWING_TWO, Boolean.valueOf(value));}
    private boolean isSwingTwo() {return this.dataManager.get(SWING_TWO);}
    private void setOverheadSwing(boolean value) {this.dataManager.set(OVERHEAD_SWING, Boolean.valueOf(value));}
    private boolean isOverheadSwing() {return this.dataManager.get(OVERHEAD_SWING);}
    private void setShootCannon(boolean value) {this.dataManager.set(SHOOT_CANNON, Boolean.valueOf(value));}
    private boolean isShootCannon() {return this.dataManager.get(SHOOT_CANNON);}
    private void setMortarFire(boolean value) {this.dataManager.set(MORTAR_FIRE, Boolean.valueOf(value));}
    private boolean isMortarFire() {return this.dataManager.get(MORTAR_FIRE);}
    private void setAwakeState(boolean value) {this.dataManager.set(AWAKE_STATE, Boolean.valueOf(value));}
    public boolean isAwakeState() {return this.dataManager.get(AWAKE_STATE);}
    private void setInanimateState(boolean value) {this.dataManager.set(INANIMATE_STATE, Boolean.valueOf(value));}
    public boolean isInAnimateState() {return this.dataManager.get(INANIMATE_STATE);}
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    public void setSpawnLocation(BlockPos pos) {
        this.dataManager.set(SPAWN_LOCATION, pos);
    }
    public boolean isSetSpawnLoc() {
        return this.dataManager.get(SET_SPAWN_LOC);
    }
    public void setSetSpawnLoc(boolean value) {
        this.dataManager.set(SET_SPAWN_LOC, Boolean.valueOf(value));
    }

    public BlockPos getSpawnLocation() {
        return this.dataManager.get(SPAWN_LOCATION);
    }
    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;
    private float randomTurn = ModRand.range(1, 360);

    public EntityFriendlyCursedGolem(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 10;
        this.setSize(1.2F, 2.8F);
        this.setInanimateState(true);
        this.setPlayerChecked(true);
        this.isFriendlyCreature = true;
    }

    public EntityFriendlyCursedGolem(World worldIn) {
        super(worldIn);
        this.experienceValue = 10;
        this.setSize(1.2F, 2.8F);
        this.setInanimateState(true);
        this.setPlayerChecked(true);
        this.isFriendlyCreature = true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Swing", this.isSwingAttack());
        nbt.setBoolean("Swing_Two", this.isSwingTwo());
        nbt.setBoolean("Overhead_Swing", this.isOverheadSwing());
        nbt.setBoolean("Mortar_Fire", this.isMortarFire());
        nbt.setBoolean("Shoot_Cannon", this.isShootCannon());
        nbt.setBoolean("Awake_State", this.isAwakeState());
        nbt.setBoolean("Inanimate_State",this.isInAnimateState());
        nbt.setBoolean("Shaking", this.isShaking());
        nbt.setBoolean("Set_Spawn_Loc", this.dataManager.get(SET_SPAWN_LOC));
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSwingAttack(nbt.getBoolean("Swing"));
        this.setSwingTwo(nbt.getBoolean("Swing_Two"));
        this.setOverheadSwing(nbt.getBoolean("Overhead_Swing"));
        this.setMortarFire(nbt.getBoolean("Mortar_Fire"));
        this.setShootCannon(nbt.getBoolean("Shoot_Cannon"));
        this.setAwakeState(nbt.getBoolean("Awake_State"));
        this.setInanimateState(nbt.getBoolean("Inanimate_State"));
        this.setShaking(nbt.getBoolean("Shaking"));
        this.dataManager.set(SET_SPAWN_LOC, nbt.getBoolean("Set_Spawn_Loc"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(SWING, Boolean.valueOf(false));
        this.dataManager.register(SWING_TWO, Boolean.valueOf(false));
        this.dataManager.register(OVERHEAD_SWING, Boolean.valueOf(false));
        this.dataManager.register(MORTAR_FIRE, Boolean.valueOf(false));
        this.dataManager.register(SHOOT_CANNON, Boolean.valueOf(false));
        this.dataManager.register(AWAKE_STATE, Boolean.valueOf(false));
        this.dataManager.register(INANIMATE_STATE, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        this.dataManager.register(SET_SPAWN_LOC, Boolean.valueOf(false));
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
        super.entityInit();
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.cursed_sentinel_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.cursed_sentinel_health - 10);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.cursed_sentinel_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityFriendlyCursedGolemAttack<>(this, 1.0D, 10, 6, 0.3F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.75D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityMob>(this, EntityMob.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityAbstractBase>(this, EntityAbstractBase.class, 1, true, false, e -> (!(e instanceof EntityFriendlyCursedGolem) && !(e instanceof EntityFriendlyCursedRevenant) && !(e instanceof EntityFriendlyHalberd) && !(e instanceof EntityFriendWyrk))));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.shakeTime--;
        if(this.isInAnimateState()) {
            this.setImmovable(true);
            this.rotationYaw = randomTurn;
            this.rotationYawHead = randomTurn;

            EntityLivingBase target = this.getAttackTarget();
            if(target != null) {
                if(this.getDistance(target) <= 12 && !this.isAwakeState()) {
                    this.setTooAwake();
                }
            }
        } else {
            EntityLivingBase target = this.getAttackTarget();

            if(target != null && !this.isFightMode()) {
                if(this.getDistance(target) > 10 && ticksExisted % 300 == 0) {
                    shoot_cannon.accept(target);
                }
            }
        }

        if(!world.isRemote && this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
            BlockPos pos = this.getSpawnLocation();
            if(this.getDistance(pos.getX(), pos.getY(), pos.getZ()) > 16) {
                this.setPosition(pos.getX() + 0.5, pos.getY(), this.posZ + 0.5);
            }
        }

    }

    private void setTooAwake() {
        this.setInanimateState(false);
        this.setAwakeState(true);
        this.playSound(SoundsHandler.CURSED_SENTINEL_AWAKE, 1.5f, 1.0F);

        addEvent(()-> {
            this.setImmovable(false);
            this.setAwakeState(false);
        }, 50);
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isAwakeState() && !this.isInAnimateState() && !this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(swing_attack, overhead_swing, mortar_fire, shoot_cannon));
            double[] weights = {
                    (prevAttack != swing_attack) ? 1/distance : 0, //swing_Attack
                    (prevAttack != overhead_swing) ? 1/distance : 0, //overhead swing
                    (prevAttack != mortar_fire) ? distance * 0.02 : 0, //mortar fire
                    (prevAttack != shoot_cannon) ? distance * 0.02 : 0 //shoot cannon
            };
            prevAttack = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttack.accept(target);
        }
        return 30;
    }

    private Consumer<EntityLivingBase> shoot_cannon = (target) -> {
        this.setShootCannon(true);
        this.setFightMode(true);

        addEvent(()-> this.lockLook = true, 15);
        addEvent(()-> {
            ProjectileFastGhostCrystal ghost_proj = new ProjectileFastGhostCrystal(world, this, this.getAttack());
            this.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 1.5f, 0.4f / (world.rand.nextFloat() * 0.4f + 0.2f));
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3,2,0)));
            ghost_proj.setPosition(relPos.x, relPos.y, relPos.z);
            float inaccuracy = 0.0f;
            float speed = 0.7f;
            ghost_proj.shoot(this, 2, this.rotationYaw, 0.0F, speed, inaccuracy);
            ghost_proj.rotationYaw = this.rotationYaw;
            ghost_proj.setTravelRange(24);
            world.spawnEntity(ghost_proj);
            this.setImmovable(true);
        }, 25);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(false);
        }, 40);

        addEvent(()-> {
            this.setShootCannon(false);
            this.setFightMode(false);
        }, 50);
    };

    private Consumer<EntityLivingBase> mortar_fire = (target) -> {
        this.setFightMode(true);
        this.setMortarFire(true);
        this.setImmovable(true);

        addEvent(()-> {
            this.lockLook = true;
            ProjectileFastGhostCrystal ghost_proj = new ProjectileFastGhostCrystal(world, this, this.getAttack(), false);
            this.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 1.0f, 0.8f / (world.rand.nextFloat() * 0.4f + 0.2f));
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,3.5,0)));
            ghost_proj.setPosition(relPos.x, relPos.y, relPos.z);
            float inaccuracy = 0.0f;
            float speed = 0.6f;
            ghost_proj.shoot(this, -75, this.rotationYaw, 0.0F, speed, inaccuracy);
            ghost_proj.rotationYaw = this.rotationYaw;
            ghost_proj.setTravelRange(16);
            world.spawnEntity(ghost_proj);
        }, 23);

        addEvent(()-> {
            ProjectileFastGhostCrystal ghost_proj = new ProjectileFastGhostCrystal(world, this, this.getAttack(), false);
            this.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 1.0f, 0.8f / (world.rand.nextFloat() * 0.4f + 0.2f));
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,3.5,0)));
            ghost_proj.setPosition(relPos.x, relPos.y, relPos.z);
            float inaccuracy = 0.0f;
            float speed = 0.6f;
            ghost_proj.shoot(this, -75, this.rotationYaw - 144, 0.0F, speed, inaccuracy);
            ghost_proj.rotationYaw = this.rotationYaw - 144;
            ghost_proj.setTravelRange(16);
            world.spawnEntity(ghost_proj);
        }, 37);

        addEvent(()-> {
            ProjectileFastGhostCrystal ghost_proj = new ProjectileFastGhostCrystal(world, this, this.getAttack(), false);
            this.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 1.0f, 0.8f / (world.rand.nextFloat() * 0.4f + 0.2f));
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,3.5,0)));
            ghost_proj.setPosition(relPos.x, relPos.y, relPos.z);
            float inaccuracy = 0.0f;
            float speed = 0.6f;
            ghost_proj.shoot(this, -75, this.rotationYaw + 144, 0.0F, speed, inaccuracy);
            ghost_proj.rotationYaw = this.rotationYaw + 144;
            ghost_proj.setTravelRange(16);
            world.spawnEntity(ghost_proj);
        }, 53);

        addEvent(()-> this.lockLook = false, 70);

        addEvent(()-> {
            this.setImmovable(false);
            this.setFightMode(false);
            this.setMortarFire(false);
        }, 80);
    };

    private Consumer<EntityLivingBase> overhead_swing = (target) -> {
        this.setFightMode(true);
        this.setOverheadSwing(true);
        this.setImmovable(true);

        addEvent(()-> {
            for(int i = 0; i <= 30; i+= 5) {
                addEvent(()-> {
                    world.setEntityState(this, ModUtils.PARTICLE_BYTE);
                }, i);
            }
        }, 10);

        addEvent(()-> {
            this.lockLook = true;
        }, 30);

        addEvent(()-> {
            this.playSound(SoundsHandler.REANIMATE_SWING, 0.8f, 0.2f / (rand.nextFloat() * 0.4f + 0.2f));
            this.setShaking(true);
            this.shakeTime = 25;
        }, 35);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(4f, (e) -> damage, this, offset, source, 0.5f, 0, false);
            world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            this.playSound(SoundsHandler.VOIDCLYSM_IMPACT, 0.8f, 0.2f / (rand.nextFloat() * 0.4f + 0.2f));
            Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75, 1.2, 0)));
            Main.proxy.spawnParticle(18, relPos.x, this.posY, relPos.z, 0, 0, 0);
        }, 40);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(false);
        }, 55);

        addEvent(()-> {
            this.setShaking(false);
            this.setImmovable(false);
            this.setOverheadSwing(false);
            this.setFightMode(false);
        }, 65);
    };

    private Consumer<EntityLivingBase> swing_attack = (target) -> {
        this.setFightMode(true);
        boolean randB = rand.nextBoolean();

        if(randB) {
            this.setSwingTwo(true);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.13),0.1F);
                }, 4);
            }, 15);

            addEvent(()-> this.playSound(SoundsHandler.REANIMATE_SWING, 0.8f, 0.2f / (rand.nextFloat() * 0.4f + 0.2f)), 22);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false);
            }, 26);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 35);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.13),0.1F);
                }, 4);
            }, 49);

            addEvent(()-> this.playSound(SoundsHandler.REANIMATE_SWING, 0.8f, 0.2f / (rand.nextFloat() * 0.4f + 0.2f)), 54);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false);
            }, 59);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 70);

            addEvent(()-> {
                this.setSwingTwo(false);
                this.setImmovable(false);
                this.setFightMode(false);
            }, 85);

        } else {
            this.setSwingAttack(true);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
                addEvent(()-> {
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.13),0.1F);
                }, 4);
            }, 15);
            addEvent(()-> this.playSound(SoundsHandler.REANIMATE_SWING, 0.8f, 0.2f / (rand.nextFloat() * 0.4f + 0.2f)), 22);
            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage =(float) (this.getAttack());
                ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.5f, 0, false);
            }, 26);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 35);

            addEvent(()-> {
                this.setSwingAttack(false);
                this.setImmovable(false);
                this.setFightMode(false);
            }, 50);
        }
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        data.addAnimationController(new AnimationController(this, "arms_controller_idle", 0, this::predicateArmsIdle));
        data.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateAttacks));
        data.addAnimationController(new AnimationController(this, "orb_controller", 0, this::predicateOrbMode));
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(4, ModRand.range(6, 20), (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Vec3d relPos = ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0));
                Main.proxy.spawnParticle(17, this.posX + pos.x, this.posY + 0.5, this.posZ + pos.z, 0,0,0);
            });
        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(4, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Vec3d relPos = ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0));
                Main.proxy.spawnParticle(15, this.posX + pos.x, this.posY + 1, this.posZ + pos.z, 0,0.1,0);
            });
            ModUtils.circleCallback(4, 15, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Vec3d relPos = ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0));
                Main.proxy.spawnParticle(15, this.posX + pos.x, this.posY + 2.5, this.posZ + pos.z, 0,0.1,0);
            });
        }
    }

    private <E extends IAnimatable> PlayState predicateOrbMode(AnimationEvent<E> event) {
        if(this.isInAnimateState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_INANIMATE, true));
            return PlayState.CONTINUE;
        }
        if(this.isAwakeState()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_AWAKEN));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()  && !this.isAwakeState() && !this.isInAnimateState()) {
            if(this.isMortarFire()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_MORTAR_FIRE));
                return PlayState.CONTINUE;
            }
            if(this.isSwingAttack()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SWING));
                return PlayState.CONTINUE;
            }
            if(this.isSwingTwo()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_DOUBLE_SWING));
                return PlayState.CONTINUE;
            }
            if(this.isOverheadSwing()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_OVERHEAD_SWING));
                return PlayState.CONTINUE;
            }
            if(this.isShootCannon()) {
                event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SHOOT_CANNON));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArmsIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode() && !this.isAwakeState() && !this.isInAnimateState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage()  && !this.isAwakeState() && !this.isInAnimateState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            event.getController().setAnimationSpeed(1.0 + (0.003 * event.getLimbSwing()));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode()  && !this.isAwakeState() && !this.isInAnimateState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
            event.getController().setAnimationSpeed(1.0 + (0.003 * event.getLimbSwing()));
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
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.CURSED_SENTINEL_STEP, 0.35F, 0.5f + ModRand.getFloat(0.5F));

    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.CURSED_SENTINEL_HURT;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.isAwakeState() || this.isInAnimateState() || source.isProjectile() || source.getImmediateSource() instanceof Projectile) {
            this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.3f, 1.7f + ModRand.getFloat(0.2f));
            return false;
        }

        Entity sourceAt = source.getImmediateSource();

        if(sourceAt != null && sourceAt instanceof EntityPlayer) {
            ItemStack stack =  ((EntityPlayer) sourceAt).inventory.getCurrentItem();
            if(stack.getItem().canHarvestBlock(Blocks.STONE.getDefaultState())) {
                return super.attackEntityFrom(source, (float) (amount * MobConfig.cursed_sentinel_pickaxe_multiplier));
            }
        }

        return super.attackEntityFrom(source, (float) (amount * MobConfig.cursed_sentinel_regular_multiplier));
    }

    private static final ResourceLocation LOOT_MOB = new ResourceLocation(ModReference.MOD_ID, "cursed_sentinel");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_MOB;
    }

    @Override
    protected boolean canDropLoot() {
        return this.isDropsLoot();
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 20.0F);
            if (dist >= 20.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.3F) * 0.9F * screamMult);
        }
        return 0;
    }
}
