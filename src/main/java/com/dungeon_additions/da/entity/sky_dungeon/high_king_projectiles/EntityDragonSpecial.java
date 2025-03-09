package com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
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

import java.util.List;

public class EntityDragonSpecial extends EntitySkyBase implements IAnimatable, IAnimationTickable {

    private final String ANIM_SUMMON = "summon";

    private boolean didDamage = false;
    private AnimationFactory factory = new AnimationFactory(this);
    private EntityLivingBase targetedEntity;
    private EntityLivingBase summonerEntity;

    public EntityDragonSpecial(World worldIn) {
        super(worldIn);
        this.setNoAI(true);
        this.setSize(2.6F, 0.65F);
        this.noClip = true;
        this.setNoGravity(true);
    }

    public EntityDragonSpecial(World worldIn, Vec3d targetCurrentPos, EntityLivingBase target, EntityLivingBase summoner) {
        super(worldIn);
        this.setPosition(targetCurrentPos.x, targetCurrentPos.y, targetCurrentPos.z);
        this.targetedEntity = target;
        this.summonerEntity = summoner;
        this.setNoAI(true);
        this.setSize(2.6F, 0.65F);
        this.noClip = true;
        this.setNoGravity(true);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionY = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(!world.isRemote) {
            if(targetedEntity != null) {
                if(ticksExisted == 1) {
                    this.playSound(SoundsHandler.HIGH_DRAKE_SUMMON_SPECIAL, 1.0f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
                }

                if(ticksExisted == 70) {
                    this.playSound(SoundsHandler.HIGH_DRAKE_SPECIAL_DAMAGE_ENTITY, 1.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
                }

                if(ticksExisted <= 73) {
                    BlockPos targetPos = new BlockPos(targetedEntity.posX, Math.round(targetedEntity.posY), targetedEntity.posZ);
                    int y = getSurfaceHeight(world, targetPos, (int) targetPos.getY() - 10, (int) targetPos.getY() + 3);

                    if(y == 0) {
                        this.setPosition(targetedEntity.posX, targetPos.getY(), targetedEntity.posZ);
                    } else {
                        this.setPosition(targetedEntity.posX, y + 1, targetedEntity.posZ);
                    }

                }

                if(ticksExisted == 74) {
                    BlockPos targetPos = new BlockPos(targetedEntity.posX, Math.round(targetedEntity.posY), targetedEntity.posZ);
                    int y = getSurfaceHeight(world, targetPos, (int) targetPos.getY() - 10, (int) targetPos.getY() + 3);

                        this.setPosition(targetedEntity.posX, y + 1, targetedEntity.posZ);

                    if(y == 0) {
                        this.setPosition(targetedEntity.posX, targetPos.getY(), targetedEntity.posZ);
                    } else {
                        this.setPosition(targetedEntity.posX, y + 1, targetedEntity.posZ);
                    }

                    this.setImmovable(true);
                }

                if(didDamage) {
                    if(summonerEntity != null) {
                        //Life steal occurs
                        double summonerMaxHealth = summonerEntity.getMaxHealth() * MobConfig.divine_ring_life_steal;
                        summonerEntity.heal( (float) summonerMaxHealth);
                    }
                    this.didDamage = false;
                }

                if(ticksExisted == 81) {
                    List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntitySkyBase)));
                    if(!targets.isEmpty()) {
                        for(EntityLivingBase target : targets) {
                            if(!(target instanceof EntitySkyBase)) {
                                double healthSteal = target.getMaxHealth() * MobConfig.divine_ring_damage_percentage;
                                target.setHealth((float) (target.getHealth() - healthSteal));

                                Vec3d offset = target.getPositionVector().add(ModUtils.yVec(1.0D));
                                DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
                                float damage = 1F;
                                ModUtils.handleAreaImpact(0.3f, (e) -> damage, this, offset, source, 0.4f, 0, false);
                                if(target == targetedEntity) {
                                    this.didDamage = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        if(this.ticksExisted >= 120) {
            this.setDead();
        }
    }


    public int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0))) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
        return PlayState.CONTINUE;
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
    protected void playStepSound(BlockPos pos, Block blockIn)
    {

    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
