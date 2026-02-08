package com.dungeon_additions.da.entity.desert_dungeon.boss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.desert_dungeon.EntityDesertBase;
import com.dungeon_additions.da.entity.desert_dungeon.boss.colossus.ActionColossusMaceSlam;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityGaelonBase;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
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

public class EntitySummonedMace extends EntityDesertBase implements IAnimatable, IAnimationTickable {

    private AnimationFactory factory = new AnimationFactory(this);
    private static final DataParameter<Boolean> MELEE_VARIATION = EntityDataManager.createKey(EntitySummonedMace.class, DataSerializers.BOOLEAN);
    private EntityPlayer player;
    private float damageIn;
    private EntityAbstractBase ownerIn;

    public void setMeleeVariation(boolean value) {this.dataManager.set(MELEE_VARIATION, Boolean.valueOf(value));}
    public boolean isMeleeVariation() {return this.dataManager.get(MELEE_VARIATION);}
    private final String ANIM_MACE_SLAM = "mace_impact";
    private final String ANIM_MACE_SWING = "mace_swing";

    public EntitySummonedMace(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1f, 4.5f);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public EntitySummonedMace(World worldIn) {
        super(worldIn);
        this.setSize(1f, 4.5f);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.damageIn = 10;
    }

    public EntitySummonedMace(World worldIn, boolean isMeleeVariant, float damageIn, EntityAbstractBase ownerInIn) {
        super(worldIn);
        if(isMeleeVariant) {
            this.setSize(1f, 1.75f);
        } else {
            this.setSize(1f, 4.5f);
        }
        this.noClip = true;
        this.setImmovable(true);
        this.setMeleeVariation(isMeleeVariant);
        this.setNoAI(true);
        this.damageIn = damageIn;
        this.ownerIn = ownerInIn;
    }

    public EntitySummonedMace(World worldIn, EntityPlayer player, boolean isMeleeVariant, float damageIn) {
        super(worldIn);
        if(isMeleeVariant) {
            this.setSize(3.5f, 1.75f);
        } else {
            this.setSize(1f, 4.5f);
        }
        this.setMeleeVariation(isMeleeVariant);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.player = player;
        this.damageIn = damageIn;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Melee_Variation", this.isMeleeVariation());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setMeleeVariation(nbt.getBoolean("Melee_Variation"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(MELEE_VARIATION, Boolean.valueOf(false));
        super.entityInit();
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(damageIn);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    protected void initEntityAI() {

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
            this.playSound(SoundsHandler.COLOSSUS_SUMMON_MACE, 1f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }

        if(!world.isRemote) {
            if(this.isMeleeVariation()) {

                if(ticksExisted == 27) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 2)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage =(float) (damageIn);
                    this.playSound(SoundsHandler.COLOSSUS_SWING, 0.7f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                    ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 1.3f, 0, false);
                }

                if(ticksExisted == 30) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(-2, 1.2, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage =(float) (damageIn);
                    this.playSound(SoundsHandler.COLOSSUS_SWING, 0.7f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                    ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 1.3f, 0, false);
                }

                if(ticksExisted == 32) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, -2)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage =(float) (damageIn);
                    this.playSound(SoundsHandler.COLOSSUS_SWING, 0.7f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                    ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 1.3f, 0, false);
                }

                if(ticksExisted == 34) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.2, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage =(float) (damageIn);
                    this.playSound(SoundsHandler.COLOSSUS_SWING, 0.7f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                    ModUtils.handleAreaImpact(1.75f, (e) -> damage, this, offset, source, 1.3f, 0, false);
                }

                if(ticksExisted > 49) {
                    this.setDead();
                }

            } else {

                if(ticksExisted == 29) {
                    this.playSound(SoundsHandler.COLOSSUS_SWING_SLAM, 0.7f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
                }

                if(ticksExisted == 33) {
                    if(this.ownerIn != null) {
                        new ActionColossusMaceSlam(6).performAction(this, null);
                    } else {
                        new ActionColossusMaceSlam(6).performAction(this, null);
                    }
                    Main.proxy.spawnParticle(20,world, this.posX, this.posY + 0.5, this.posZ, 0, 0, 0);
                }

                if(ticksExisted > 54) {
                    this.setDead();
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
    }
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(this.isMeleeVariation()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MACE_SWING, false));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MACE_SLAM, false));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
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
}
