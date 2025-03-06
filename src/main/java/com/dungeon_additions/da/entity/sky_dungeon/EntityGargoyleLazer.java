package com.dungeon_additions.da.entity.sky_dungeon;

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
import java.util.function.Consumer;

public class EntityGargoyleLazer extends EntitySkyBase implements IAnimatable, IAnimationTickable {

    private final AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_IDLE = "summon";
    private EntityLivingBase target;

    private int lazerFastCooldown = 0;

    public EntityGargoyleLazer(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(1.25F, 7.0F);
    }

    public EntityGargoyleLazer(World worldIn) {
        super(worldIn);
        this.noClip = true;
        this.setNoGravity(true);
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(1.25F, 7.0F);
    }


    public EntityGargoyleLazer(World worldIn, EntityLivingBase target) {
        super(worldIn);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoGravity(true);
        this.setNoAI(true);
        this.setSize(1.25F, 7.0F);
        this.target = target;
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
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


        if(!world.isRemote) {

            lazerFastCooldown--;

            if(ticksExisted == 2) {
                this.playSound(SoundsHandler.GARGOYLE_SUMMON_LAZER, 1.5f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
            }

            //tracks target
            if(target != null) {
                if(ticksExisted < 53) {
                    int yVar = ModUtils.getSurfaceHeightGeneral(world, new BlockPos(target.posX, target.posY, target.posZ), (int) target.posY - 3, (int) target.posY + 1);
                    Vec3d targetPos;
                    if(yVar != target.posY) {
                        targetPos = new Vec3d(target.posX, yVar, target.posZ);
                    } else {
                        targetPos = target.getPositionVector();
                    }
                    ModUtils.setEntityPosition(this, targetPos.add(0, 0.1, 0));
                }
            }

            if(ticksExisted == 55) {
                this.playSound(SoundsHandler.GARGOYLE_LAZER, 1.5f, 0.8f / (rand.nextFloat() * 0.4F + 0.4f));
            }
            //deals damage with the lazer
            if (this.ticksExisted > 57 && this.ticksExisted < 85 && lazerFastCooldown < 0) {
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntitySkyBase)));
                if(!targets.isEmpty()) {
                    for(EntityLivingBase targetfrom : targets) {
                        if(!(targetfrom instanceof EntitySkyBase)) {
                            Vec3d offset = targetfrom.getPositionVector().add(ModUtils.yVec(1.0D));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                            float damage = this.getAttack();
                            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                            lazerFastCooldown = 10;
                        }
                    }
                }
            }

            if(ticksExisted == 90) {
                this.setDead();
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
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
