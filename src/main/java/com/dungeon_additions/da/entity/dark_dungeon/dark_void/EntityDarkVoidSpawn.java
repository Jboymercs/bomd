package com.dungeon_additions.da.entity.dark_dungeon.dark_void;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkAssassin;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDauntlessAOE;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class EntityDarkVoidSpawn extends EntityDarkBase implements IAnimatable, IAnimationTickable {

    protected static final DataParameter<Integer> TIME_ALIVE = EntityDataManager.createKey(EntityDarkVoidSpawn.class, DataSerializers.VARINT);
    private final String ANIM_IDLE = "idle";
    private AnimationFactory factory = new AnimationFactory(this);
    public void setTimeAlive(int skinType)
    {
        this.dataManager.set(TIME_ALIVE, Integer.valueOf(skinType));
    }
    public int getTimeAlive()
    {
        return this.dataManager.get(TIME_ALIVE).intValue();
    }


    public EntityDarkVoidSpawn(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.0F, 1F);
        this.setTimeAlive(40);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public EntityDarkVoidSpawn(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1F);
        this.setTimeAlive(40);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }
    private int mobType;
    private EntityDarkVoid owner;

    public EntityDarkVoidSpawn(World worldIn, EntityDarkVoid owner, int mobType) {
        super(worldIn);
        this.setSize(1.0F, 1F);
        this.setTimeAlive(40);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.mobType = mobType;
        this.owner = owner;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(ticksExisted == 2) {
            this.playSound(SoundsHandler.LICH_MINION_RUNE, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        }

        if(this.getTimeAlive() == 4) {
            //spawn custom particles
            ModUtils.performNTimes(15, (i) -> {
                Main.proxy.spawnParticle(37, world, this.posX + ModRand.getFloat(1), this.posY + ModRand.getFloat(1.5F) + 0.5, this.posZ + ModRand.getFloat(1), 0, 0.07, 0, 0);
            });
        }
        if(this.getTimeAlive() > 0) {
            this.setTimeAlive(this.getTimeAlive() - 1);
        } else {
            //summon respective mob
            if(owner != null && this.mobType != 0) {
                Entity entityToo = null;
                if(this.mobType == 1) {
                    entityToo = Objects.requireNonNull(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(ModRand.choice(MobConfig.dark_void_tier_one)))).newInstance(world);
                }
                if(this.mobType == 2) {
                    entityToo = Objects.requireNonNull(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(ModRand.choice(MobConfig.dark_void_tier_two)))).newInstance(world);
                }
                //prep mob spawning
                if(entityToo instanceof EntityLivingBase) {
                    EntityLivingBase spawn = ((EntityLivingBase) entityToo);

                    if(spawn instanceof EntityDarkBase) {
                        EntityDarkBase base = ((EntityDarkBase) spawn);
                        base.setSpawnOverride(true);
                        if(base instanceof EntityDarkAssassin) {
                            //rattle up the attack speed!
                            base.setAnimationAttackSpeed(1.25F);
                        }
                    }

                    if(owner != null) {
                        owner.current_mobs.add(new WeakReference<>(spawn));
                    }
                    spawn.setPosition(this.posX, this.posY, this.posZ);
                    world.spawnEntity(spawn);
                    this.playSound(SoundsHandler.LICH_SUMMON_MINION, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
                }
            }

            this.setDead();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Time_Alive", this.getTimeAlive());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setTimeAlive(nbt.getInteger("Time_Alive"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(TIME_ALIVE, 40);
        super.entityInit();
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(14D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
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
}
