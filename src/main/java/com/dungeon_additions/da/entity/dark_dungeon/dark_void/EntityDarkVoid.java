package com.dungeon_additions.da.entity.dark_dungeon.dark_void;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDauntlessAOE;
import com.dungeon_additions.da.entity.night_lich.EntityAbstractNightLich;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.ServerScaleUtil;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
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
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.stream.Collectors;

public class EntityDarkVoid extends EntityDarkBase implements IAnimatable, IAnimationTickable, IScreenShake {

    private final String ANIM_CUBE_IDLE = "idle_cube";
    private final String ANIM_PLANE_IDLE = "idle_plane";
    private final String ANIM_CUBE_TO_PLANE = "cube_to_plane";
    private final String ANIM_SUMMON_OBJECT = "summon_object";
    private final String ANIM_KILL_OBJECT = "kill_object";
    private final String ANIM_HIDDEN = "hidden_idle";

    private AnimationFactory factory = new AnimationFactory(this);
    public List<WeakReference<Entity>> current_mobs = Lists.newArrayList();
    private int HoverTimeIncrease = 0;
    private int difficulty = 14;
    private int shakeTime = 0;

    protected static final DataParameter<Integer> WAVE_COUNT = EntityDataManager.createKey(EntityDarkVoid.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IS_CUBE = EntityDataManager.createKey(EntityDarkVoid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> KILL_OBJECT = EntityDataManager.createKey(EntityDarkVoid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_OBJECT = EntityDataManager.createKey(EntityDarkVoid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CUBE_TO_PLANE = EntityDataManager.createKey(EntityDarkVoid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HIDDEN_IDLE = EntityDataManager.createKey(EntityDarkVoid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ONLY_BOSS = EntityDataManager.createKey(EntityDarkVoid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityDarkVoid.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CONTAINS_BOSS = EntityDataManager.createKey(EntityDarkVoid.class, DataSerializers.BOOLEAN);

    public void setIsCube(boolean value) {this.dataManager.set(IS_CUBE, Boolean.valueOf(value));}
    public void setKillObject(boolean value) {this.dataManager.set(KILL_OBJECT, Boolean.valueOf(value));}
    public void setSummonObject(boolean value) {this.dataManager.set(SUMMON_OBJECT, Boolean.valueOf(value));}
    public void setCubeToPlane(boolean value) {this.dataManager.set(CUBE_TO_PLANE, Boolean.valueOf(value));}
    public void setHiddenIdle(boolean value) {this.dataManager.set(HIDDEN_IDLE, Boolean.valueOf(value));}
    public void setOnlyBoss(boolean value) {this.dataManager.set(ONLY_BOSS, Boolean.valueOf(value));}
    public void setContainsBoss(boolean value) {this.dataManager.set(CONTAINS_BOSS, Boolean.valueOf(value));}
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public void setWaveCount(int val) {this.dataManager.set(WAVE_COUNT, val);}

    public boolean isCube() {return this.dataManager.get(IS_CUBE);}
    public boolean isKillObject() {return this.dataManager.get(KILL_OBJECT);}
    public boolean isSummonObject() {return this.dataManager.get(SUMMON_OBJECT);}
    public boolean isCubeToPlane() {return this.dataManager.get(CUBE_TO_PLANE);}
    public boolean isHiddenIdle() {return this.dataManager.get(HIDDEN_IDLE);}
    public boolean isOnlyBoss() {return this.dataManager.get(ONLY_BOSS);}
    public boolean isContainsBoss() {return this.dataManager.get(CONTAINS_BOSS);}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    public int getWaveCount() {return this.dataManager.get(WAVE_COUNT);}

    public EntityDarkVoid(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setNoAI(true);
        this.summonObject();
        this.setNoGravity(true);
        this.difficulty = 4 * this.getWaveCount();
        this.setSize(1.0F, 1.0F);
        this.setIsCube(true);
    }

    public EntityDarkVoid(World worldIn) {
        super(worldIn);
        this.setNoAI(true);
        this.summonObject();
        this.setNoGravity(true);
        this.difficulty = 4 * this.getWaveCount();
        this.setSize(1.0F, 1.0F);
        this.setIsCube(true);
    }

    public EntityDarkVoid(World worldIn, int waveCount, boolean containsBoss, boolean onlyBoss) {
        super(worldIn);
        this.setNoAI(true);
        this.setWaveCount(waveCount);
        this.setContainsBoss(containsBoss);
        this.setOnlyBoss(onlyBoss);
        this.setNoGravity(true);
        this.setSize(1.0F, 1.0F);
        this.setIsCube(true);
        this.difficulty = 4 * waveCount;
        if(!this.world.isDaytime()) {
            this.summonObject();
        } else {
            this.setHiddenIdle(true);
        }
    }

    public EntityDarkVoid(World worldIn, int waveCount, int timesUsed) {
        super(worldIn);
        this.setNoAI(true);
        this.setOnlyBoss(true);
        this.timesUsed = timesUsed;
        this.timesUsed++;
        this.setContainsBoss(false);
        this.setWaveCount(waveCount);
        this.setNoGravity(true);
        this.setSize(1.0F, 1.0F);
        this.setIsCube(true);
        if(!this.world.isDaytime()) {
            this.summonObject();
        } else {
            this.setHiddenIdle(true);
        }
    }

    //Sets the dark voids that spawn naturally in the world. Allows for some great control due to other functions
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData) {
        if(!this.isOnlyBoss() && !this.isContainsBoss() && this.getWaveCount() == 0) {
            this.setWaveCount(ModRand.range(1, 3));
            this.difficulty = 3 * this.getWaveCount();
        }
        return super.onInitialSpawn(difficulty, entityLivingData);
    }

    private void summonObject() {
        this.setSummonObject(true);
        this.setHiddenIdle(false);
        this.setShaking(true);
        this.shakeTime = 45;
        addEvent(()-> {
            this.setShaking(false);
            this.setSummonObject(false);
        }, 50);
    }

    private void killObject(boolean remove) {
        this.setKillObject(true);
        addEvent(()-> {
            this.setKillObject(false);
            if(remove) {
                this.setDead();
            } else {
                this.setHiddenIdle(true);
            }
        }, 20);
    }

    private void turnObjectTooPlane() {
        this.setCubeToPlane(true);
        this.HoverTimeIncrease = 20;
        this.playSound(SoundsHandler.DAUNTLESS_PREPARE_SPELL, 2.0f, 1.0f);
        addEvent(()-> {
            this.setCubeToPlane(false);
            this.setIsCube(false);
        }, 60);

        if(this.timesUsed != 0) {
            addEvent(this::createDauntlessSpawnRevived, 200);
        } else {
            addEvent(this::createDauntlessSpawn, 200);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Wave_Count", this.getWaveCount());
        nbt.setBoolean("Is_Cube", this.isCube());
        nbt.setBoolean("Kill_Object", this.isKillObject());
        nbt.setBoolean("Summon_Object", this.isSummonObject());
        nbt.setBoolean("Cube_To_Plane", this.isCubeToPlane());
        nbt.setBoolean("Hidden_Idle", this.isHiddenIdle());
        nbt.setBoolean("Only_Boss", this.isOnlyBoss());
        nbt.setBoolean("Shaking", this.isShaking());
        nbt.setBoolean("Contains_Boss", this.isContainsBoss());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setWaveCount(nbt.getInteger("Wave_Count"));
        this.setIsCube(nbt.getBoolean("Is_Cube"));
        this.setKillObject(nbt.getBoolean("Kill_Object"));
        this.setSummonObject(nbt.getBoolean("Summon_Object"));
        this.setCubeToPlane(nbt.getBoolean("Cube_To_Plane"));
        this.setHiddenIdle(nbt.getBoolean("Hidden_Idle"));
        this.setOnlyBoss(nbt.getBoolean("Only_Boss"));
        this.setShaking(nbt.getBoolean("Shaking"));
        this.setContainsBoss(nbt.getBoolean("Contains_Boss"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(WAVE_COUNT, 0);
        this.dataManager.register(IS_CUBE, Boolean.valueOf(false));
        this.dataManager.register(KILL_OBJECT, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_OBJECT, Boolean.valueOf(false));
        this.dataManager.register(CUBE_TO_PLANE, Boolean.valueOf(false));
        this.dataManager.register(ONLY_BOSS, Boolean.valueOf(false));
        this.dataManager.register(CONTAINS_BOSS, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        this.dataManager.register(HIDDEN_IDLE, Boolean.valueOf(false));
        super.entityInit();
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(14D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    private boolean hasCreatedWave = false;
    private int waitTime = 0;


    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationPitch = 0;
        this.waitTime--;
        this.shakeTime--;
        this.clearInvalidEntities();
        //start wave summoning
        if(!this.world.isRemote) {

            if (HoverTimeIncrease > 0) {
                this.setNoAI(false);
                this.motionY = 0.3;
                HoverTimeIncrease--;
            } else {
                this.setNoAI(true);
                this.motionY = 0;
                this.rotationYaw = 0;
                this.rotationYawHead = 0;
                this.renderYawOffset = 0;
            }

            if(!this.isHiddenIdle() && world.rand.nextInt(10) == 0) {
                ModUtils.performNTimes(3, (i) -> {
                    Main.proxy.spawnParticle(23, world, this.posX + ModRand.getFloat(4), this.posY + ModRand.getFloat(3) + 0.5, this.posZ + ModRand.getFloat(4), 0, 0.03, 0, 0);
                });

            }

            if(ticksExisted == 1 || ticksExisted % 200 == 0) {
                world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundsHandler.DAUNTLESS_DARK_VOID, SoundCategory.NEUTRAL, 0.75f, 0.4f / (world.rand.nextFloat() * 0.4F + 0.2f));
            }


            if (!this.isHiddenIdle() && !this.isSummonObject() && !this.isKillObject() && !this.isCubeToPlane() && this.isCube() && waitTime < 0) {
                double searchDistance = this.isOnlyBoss() ? 5.5D : 16D;
                List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(searchDistance), e -> !e.getIsInvulnerable());
                if(!nearbyPlayers.isEmpty()) {
                    if(this.isOnlyBoss()) {
                        //spawn a arena Dauntless
                        this.turnObjectTooPlane();
                    }else if(this.current_mobs.isEmpty() && this.getWaveCount() != 0) {

                        if(this.isContainsBoss() && this.getWaveCount() == 1) {
                            //spawn a Dauntless naturally
                            this.turnObjectTooPlane();
                        } else {
                            //spawn regular wave
                            if(!hasCreatedWave) {
                                this.createWaveGroup();
                            }
                        }
                    } else if (this.getWaveCount() == 0 && this.current_mobs.isEmpty()) {
                        //remove after defeating the final wave
                        this.killObject(true);
                    }
                }
            }
        }


    }

    private void createDauntlessSpawnRevived() {
        this.noClip = true;
        this.playSound(SoundsHandler.DAUNTLESS_USE_SWORD, 2.0f, 1.0f);
        EntityDauntless boss = new EntityDauntless(world, this.timesUsed, this.getPosition());
        boss.setPosition(this.posX, this.posY, this.posZ);
        boss.motionY -= 0.35;
        boss.setSpawnedNaturally(false);
        world.spawnEntity(boss);
        this.current_mobs.add(new WeakReference<>(boss));
        this.setWaveCount(this.getWaveCount() - 1);
        //we will remove it anyways after spawning the boss
        addEvent(()-> {
            this.killObject(true);
        }, 100);
    }

    private void createDauntlessSpawn() {
        this.noClip = true;
        this.playSound(SoundsHandler.DAUNTLESS_USE_SWORD, 2.0f, 1.0f);
        EntityDauntless boss = new EntityDauntless(world, (float) this.posX, (float) this.posY, (float) this.posZ);
      //  boss.setPosition(this.posX, this.posY, this.posZ);
        boss.motionY -= 0.35;
        if(this.isOnlyBoss()) {
            boss.setSpawnedNaturally(false);
        } else {
            boss.setSpawnedNaturally(true);
        }
        world.spawnEntity(boss);
        this.current_mobs.add(new WeakReference<>(boss));
        this.setWaveCount(this.getWaveCount() - 1);
        //we will remove it anyways after spawning the boss
        addEvent(()-> {
            this.killObject(true);
        }, 100);

    }

    private void createWaveGroup() {
        this.hasCreatedWave = true;
        playersNearbyAmount = ServerScaleUtil.getPlayers(this, world);
        int playerBonus = playersNearbyAmount > 1 ? (playersNearbyAmount - 1) * 2 : 0;
        int number_of_mobs = this.difficulty/this.getWaveCount() + playerBonus; // wave 3 is 5 mobs
        if(this.getWaveCount() > 0 && number_of_mobs != 0) {
            for(int i = 0; i <= number_of_mobs; i++) {
                //create the spawn
                this.addEvent(()-> {
                    EntityDarkVoidSpawn spawn;
                    //tier two spawn
                    if(ModRand.range(0, number_of_mobs) <= (number_of_mobs * 0.25)) {
                        spawn = new EntityDarkVoidSpawn(world, this, 2);
                    } else {
                        spawn = new EntityDarkVoidSpawn(world, this, 1);
                    }
                    Vec3d relPos = this.getPositionVector().add(ModRand.getFloat(14), 0, ModRand.getFloat(14));
                    int yFor = ModUtils.getSurfaceHeightZeroReturn(world, new BlockPos(relPos.x, 0, relPos.z), (int) relPos.y - 12, (int) relPos.y + 3);
                    if(yFor != 0) {
                        spawn.setPosition(relPos.x + 0.5, yFor + 1, relPos.z + 0.5);
                    } else {
                        spawn.setPosition(relPos.x + 0.5, this.posY - 1, relPos.z + 0.5);
                    }
                    world.spawnEntity(spawn);
                }, 30 * i);
            }
            this.playSound(SoundsHandler.DAUNTLESS_PREPARE_SPELL, 2.0f, 1.0f);
        }

        addEvent(()-> this.hasCreatedWave = false, number_of_mobs * 10 - 5);
        this.waitTime = 600;
        //with the settings of the wave complete. subtract one
        this.setWaveCount(this.getWaveCount() - 1);
    }

    @Override
    public void onLivingUpdate()
    {
        if(!world.isRemote && !this.isSummonObject() && !this.isKillObject()) {
            if (this.world.isDaytime() && !this.isHiddenIdle()) {
                //can soft remove the mechanic else it will set itself to disappear
                float f = this.getBrightness();

                if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(new BlockPos(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ))) {
                    //particles
                    ModUtils.performNTimes(15, (i) -> {
                        Main.proxy.spawnParticle(23, world, this.posX + ModRand.getFloat(1), this.posY + ModRand.getFloat(1) + 0.5, this.posZ + ModRand.getFloat(1), 0, 0.07, 0, 0);
                    });

                    current_mobs.clear();
                    if(this.getWaveCount() == 0 && !this.isOnlyBoss()) {
                     this.killObject(true);
                    } else {
                        this.killObject(false);
                    }
                }
            } else if (this.isHiddenIdle()){
                float f = this.getBrightness();
                if(f < 0.5) {
                    this.summonObject();
                }
            }
        }

        super.onLivingUpdate();
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "state_controller", 0, this::predicateStates));
    }

    /**
     * This ensures that active mobs are still within a distance and are still alive to be accounted for
     */
    private void clearInvalidEntities() {
        current_mobs = current_mobs.stream().filter(ref -> ref.get() != null && ref.get().getDistance(this) <= 70 && ref.get().isEntityAlive()).collect(Collectors.toList());
    }

    private <E extends IAnimatable> PlayState predicateStates(AnimationEvent<E> event) {
        if(this.isKillObject()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_KILL_OBJECT));
            return PlayState.CONTINUE;
        }
        if(this.isSummonObject()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SUMMON_OBJECT));
            return PlayState.CONTINUE;
        }
        if(this.isCubeToPlane()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_CUBE_TO_PLANE));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isCubeToPlane()) {
            if(this.isHiddenIdle()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HIDDEN, true));
            } else if(this.isCube()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CUBE_IDLE, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PLANE_IDLE, true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public int getBrightnessForRender() {
        return Math.min(super.getBrightnessForRender() + 20, 150);
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 20.0F + 0.2F);
            if (dist >= 20.0F) {
                return 0.0F;
            }
            float isLarge =  1.5F;
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * isLarge * screamMult);
        }
        return 0;
    }
}
