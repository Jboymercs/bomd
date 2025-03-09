package com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntityBloodPile extends EntitySkyBase implements IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_SUMMON = "summon";
    private boolean playerProtection;

    public EntityBloodPile(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setImmovable(true);

        this.setSize(0.6f, 2.0f);
        this.noClip = true;
    }

    public EntityBloodPile(World worldIn) {
        super(worldIn);
        this.setImmovable(true);

        this.setSize(0.6f, 2.0f);
        this.noClip = true;
    }

    private EntityPlayer ownerFrom;
    public EntityBloodPile(World worldIn, boolean playerProtection, EntityPlayer ownerFrom) {
        super(worldIn);
        this.setImmovable(true);
        this.playerProtection = playerProtection;
        this.ownerFrom = ownerFrom;
        this.setSize(0.6f, 2.0f);
        this.noClip = true;
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

        if(ticksExisted > 14 && ticksExisted < 21) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }

        if(ticksExisted == 15) {
            if(playerProtection && ownerFrom != null) {
                this.playSound(SoundsHandler.SPORE_IMPACT, 0.75f, 1.0f);
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.4, 1.0, 1.4), e -> !e.getIsInvulnerable() && (!(e instanceof EntityPlayer)));

                if(!targets.isEmpty()) {
                    for(EntityLivingBase base : targets) {
                        if(base != this && base != ownerFrom && !(base instanceof EntityBloodPile)) {
                            boolean hasHelmet = ownerFrom.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.KINGS_HELMET;
                            Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.0D));
                            DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.PLAYER).directEntity(ownerFrom).build();
                            float damage = hasHelmet ? 20 : 13;
                            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
                            //  addPotionEffect(new PotionEffect(MobEffects.POISON, 9, 3));
                            if(base != null && !world.isRemote) {
                                double distSq = this.getDistanceSq(base.posX, base.getEntityBoundingBox().minY, base.posZ);
                                double distance = Math.sqrt(distSq);
                                if (base.getActivePotionEffect(MobEffects.POISON) == null && distance < 1) {
                                    base.addPotionEffect(new PotionEffect(MobEffects.WITHER, 200, 0));
                                }
                            }
                        }
                    }
                }
            } else {
                this.playSound(SoundsHandler.SPORE_IMPACT, 0.75f, 1.0f);
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityVoidSpike || e instanceof EntityGenericWave)));

                if(!targets.isEmpty()) {
                    for(EntityLivingBase base : targets) {
                        if(base != this && !(base instanceof EntitySkyBase)) {
                            Vec3d offset = base.getPositionVector().add(ModUtils.yVec(0.5D));
                            DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
                            float damage = this.getAttack();
                            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
                            //  addPotionEffect(new PotionEffect(MobEffects.POISON, 9, 3));
                            if(base != null && !world.isRemote) {
                                double distSq = this.getDistanceSq(base.posX, base.getEntityBoundingBox().minY, base.posZ);
                                double distance = Math.sqrt(distSq);
                                if (base.getActivePotionEffect(MobEffects.POISON) == null && distance < 1) {
                                    base.addPotionEffect(new PotionEffect(MobEffects.WITHER, 200, 0));
                                }
                            }
                        }
                    }
                }
            }

        }


        if(ticksExisted == 20) {
            this.setDead();
        }

    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(0.5D)), ModColors.RED, new Vec3d(0,-0.05,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(1.5D)), ModColors.RED, new Vec3d(0,-0.05,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(3.0D)), ModColors.RED, new Vec3d(0,-0.05,0));
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.high_dragon_king_damage * 0.5);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "wave_controller", 0, this::predicateIdle));
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
