package com.dungeon_additions.da.entity.dark_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.EntityFrostBase;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
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

import java.util.List;

public class EntityShadowHand extends EntityDarkBase implements IAnimatable, IAnimationTickable {

    private final String ANIM_IDLE = "idle";
    private final String ANIM_HAND_STRIKE = "hand_attack";
    private final String ANIM_HIDE_HAND = "hide_hand";

    private int randomRot = ModRand.range(1, 360);

    private static final DataParameter<Boolean> SWING_MELEE = EntityDataManager.createKey(EntityShadowHand.class, DataSerializers.BOOLEAN);

    private boolean isSwingMelee() {
        return this.dataManager.get(SWING_MELEE);
    }

    private void setSwingMelee(boolean value) {
        this.dataManager.set(SWING_MELEE, Boolean.valueOf(value));
    }
    private boolean hasIniatedAttack = false;
    private AnimationFactory factory = new AnimationFactory(this);

    public EntityShadowHand(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.0F, 1.5F);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public EntityShadowHand(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.5F);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Swing_Melee", this.isSwingMelee());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSwingMelee(nbt.getBoolean("Swing_Melee"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SWING_MELEE, Boolean.valueOf(false));

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
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = randomRot;
        this.rotationPitch = 0;
        this.rotationYawHead = randomRot;
        this.renderYawOffset = 0;

        if(!world.isRemote) {

            if(ticksExisted == 2) {
                this.playSound(SoundsHandler.SHADOW_HAND_SUMMON, 1.0f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
            }
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityDarkBase)));

            if(ticksExisted < 1150 && !targets.isEmpty()) {
                for(EntityLivingBase target : targets) {
                    if(!(target instanceof EntityDarkBase) && !hasIniatedAttack) {
                        this.initiateAttack();
                        this.hasIniatedAttack = true;
                    }
                }
            }

            if(ticksExisted == 1200) {
                this.setDead();
            }
        }
    }


    private void initiateAttack() {
        this.setSwingMelee(true);
        this.playSound(SoundsHandler.SHADOW_HAND_ATTACK, 1.0f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
        addEvent(()-> {
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityDarkBase)));

            if(!targets.isEmpty()) {
                for(EntityLivingBase base : targets) {
                    if(!(base instanceof EntityDarkBase)) {
                        Vec3d offset = base.getPositionVector().add(ModUtils.yVec(0.5D));
                        DamageSource source;
                        source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
                        float damage = this.getAttack();
                        ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, offset, source, 0.15f, 0, false);
                        base.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 200, 0, false, false));
                    }
                }
            }
        }, 20);

        addEvent(()-> {
        this.setSwingMelee(false);
        addEvent(this::setDead, 20);
        }, 40);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "hide_hand_controller", 0, this::predicateHand));
        animationData.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttack));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateHand(AnimationEvent<E> event) {
        if(!this.isSwingMelee()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HIDE_HAND, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isSwingMelee()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HAND_STRIKE, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
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
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    /**
     * Add a bit of brightness to the entity for it's texture
     */
    @Override
    public int getBrightnessForRender() {
        return Math.min(super.getBrightnessForRender() + 30, 200);
    }
}
