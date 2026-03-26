package com.dungeon_additions.da.entity.generic;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import scala.tools.nsc.backend.icode.Primitives;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import sun.security.provider.SHA;

import java.util.List;

public class EntityRallyFlag extends EntityAbstractBase implements IAnimatable, IAnimationTickable, IScreenShake {
    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_SUMMON = "summon";
    private final String ANIM_DISAPPEAR = "disappear";
    private final String ANIM_IDLE = "idle";

    private EntityPlayer player;

    private static final DataParameter<Boolean> SUMMON = EntityDataManager.createKey(EntityRallyFlag.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DISAPPEAR = EntityDataManager.createKey(EntityRallyFlag.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityRallyFlag.class, DataSerializers.BOOLEAN);

    public boolean isSummonState() {return this.dataManager.get(SUMMON);}
    public void setSummonState(boolean value) {this.dataManager.set(SUMMON, Boolean.valueOf(value));}
    public boolean isDisappear() {return this.dataManager.get(DISAPPEAR);}
    public void setDisappear(boolean value) {this.dataManager.set(DISAPPEAR, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}

    public EntityRallyFlag(World worldIn, EntityPlayer player) {
        super(worldIn);
        this.player = player;
        this.setSize(0.3f, 2.95F);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSummonState(true);
        this.setShaking(true);
        this.shakeTime = 20;
        addEvent(()-> {
            this.setShaking(false);
            this.setSummonState(false);
        }, 20);
    }


    public EntityRallyFlag(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.3f, 2.95F);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSummonState(true);
        this.setShaking(true);
        this.shakeTime = 20;
        addEvent(()-> {
            this.setShaking(false);
            this.setSummonState(false);
        }, 20);
    }

    public EntityRallyFlag(World worldIn) {
        super(worldIn);
        this.setSize(0.3f, 2.95F);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSummonState(true);
        this.setShaking(true);
        this.shakeTime = 20;
        addEvent(()-> {
            this.setShaking(false);
            this.setSummonState(false);
        }, 20);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Summon", this.isSummonState());
        nbt.setBoolean("Disappear", this.isDisappear());
        nbt.setBoolean("Shaking", this.isShaking());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSummonState(nbt.getBoolean("Summon"));
        this.setDisappear(nbt.getBoolean("Disappear"));
        this.setShaking(nbt.getBoolean("Shaking"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(SUMMON, Boolean.valueOf(false));
        this.dataManager.register(DISAPPEAR, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        super.entityInit();
    }

    private int shakeTime = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.shakeTime--;
        if(!world.isRemote) {
            this.motionX = 0;
            this.motionZ = 0;
            this.rotationYaw = 0;
            this.rotationPitch = 0;
            this.rotationYawHead = 0;
            this.renderYawOffset = 0;

            if(ticksExisted == 2) {
                this.playSound(SoundsHandler.BLOOD_FLY_BEGIN, 1F, 0.3f / (rand.nextFloat() * 0.4f + 0.2f));
            }

            if(ticksExisted % 20 == 0) {
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            }

            //give owner Absorption
            if(ticksExisted == 20 && player != null) {
                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 600, 0));
            }

            //buff players
            if(ticksExisted >= 20 && tickTimer() <= 620) {
                List<EntityPlayer> targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(5), e -> !e.getIsInvulnerable());

                if(!targets.isEmpty()) {
                    for(EntityPlayer playerIn : targets) {
                      if(ticksExisted % 60 == 0) {
                          double playerHealth = playerIn.getHealth() / playerIn.getMaxHealth();
                          if(playerHealth < 1) {
                              playerIn.heal(1);
                          }
                      }

                      if(ticksExisted % 40 == 0) {
                          playerIn.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 80, 0));
                          playerIn.addPotionEffect(new PotionEffect(MobEffects.HASTE, 80, 0));
                      }
                    }
                }
            }

            if(ticksExisted == 622) {
                this.removeFlag();
            }
        }
    }

    private void removeFlag() {
        this.setDisappear(true);

        addEvent(()-> {
            this.setDisappear(false);
            this.setDead();
        }, 9);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(5, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Vec3d relPos = ModUtils.getRelativeOffset(this, new Vec3d(1.1, 1.2, 0));
                Main.proxy.spawnParticle(17, this.posX + pos.x, this.posY + 0.5, this.posZ + pos.z, 0,0,0);
            });
        }
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "state_controller", 0, this::predicateStates));
    }

    private <E extends IAnimatable>PlayState predicateStates(AnimationEvent<E> event) {
        if(this.isSummonState()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_SUMMON));
            return PlayState.CONTINUE;
        }
        if(this.isDisappear()) {
            event.getController().setAnimation(new AnimationBuilder().playOnce(ANIM_DISAPPEAR));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isDisappear() && !this.isSummonState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
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
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 12.0F);
            if (dist >= 12.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.8F) * 0.25F * screamMult);
        }
        return 0;
    }
}
