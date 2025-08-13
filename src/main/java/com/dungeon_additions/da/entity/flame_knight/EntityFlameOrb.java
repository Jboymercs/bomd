package com.dungeon_additions.da.entity.flame_knight;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModParticle;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
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

public class EntityFlameOrb extends EntityFlameBase implements IAnimatable, IAnimationTickable {

    private final String ANIM_IDLE = "idle";
    private final String ANIM_SUMMON = "summon";
    private AnimationFactory factory = new AnimationFactory(this);

    public EntityFlameOrb(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.5F, 0.5F);
        this.setImmovable(true);
        this.setNoGravity(true);
        this.noClip = true;
        this.setNoAI(true);
    }

    public EntityFlameOrb(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
        this.setImmovable(true);
        this.setNoGravity(true);
        this.noClip = true;
        this.setNoAI(true);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.blossom_attack_damange * 0.75);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    private int byRateCheck = 300;

    private int failTimer = 0;
    private boolean metConditions = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;
        byRateCheck--;
        failTimer--;


        if(byRateCheck < 0 && !metConditions && !world.isRemote) {
            List<EntityPlayer> targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(5.0, 4.0, 5.0), e -> !e.getIsInvulnerable());

            if(!targets.isEmpty()) {
                boolean flag = false;

                for(EntityPlayer player : targets) {
                    if(player.isCreative() || player.isSpectator()) {
                        flag = true;
                    }
                }

                if(!flag) {
                    boolean flag2 = false;
                    List<EntityFlameBase> nearby_mobs = this.world.getEntitiesWithinAABB(EntityFlameBase.class, this.getEntityBoundingBox().grow(MobConfig.volactile_ritual_radius), e -> !e.getIsInvulnerable());
                    if(!nearby_mobs.isEmpty()) {
                        for(EntityLivingBase base : nearby_mobs) {
                            if(base instanceof EntityFlameBase && !(base instanceof EntityFlameOrb)) {
                                flag2 = true;
                            }
                        }
                    }

                    if(flag2) {
                         //there is still mobs nearby, give some kind of warning message
                        this.failTimer = 80;
                        for(EntityPlayer player : targets) {
                            player.sendStatusMessage(new TextComponentTranslation("da.volatile_orb", new Object[0]), true);
                        }
                        this.playSound(SoundsHandler.VOLATILE_ORB_FAIL, 1.25f, 1.0f);
                    } else {
                        //the area is clear and can summon the mini-boss
                        this.metConditions = true;
                        this.summonMiniBoss();
                    }
                }

            }
            byRateCheck = 300;
        }

    }

    public void onEntityUpdate() {
        super.onEntityUpdate();
            if(this.metConditions && world.rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            }

            if(this.failTimer > 0 && world.rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            }
        }


    @Override
    public void handleStatusUpdate(byte id) {
        if (id == ModUtils.PARTICLE_BYTE) {
            Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(ModRand.range(-2, 2), ModRand.range(1, 2), ModRand.range(-2, 2))));
            Vec3d vel = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) / 3, 0.1, (world.rand.nextFloat() - world.rand.nextFloat()) / 3);
            world.spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z, ModRand.range(40, 50));
        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(ModRand.range(-2, 2), ModRand.range(1, 2), ModRand.range(-2, 2))));
            Vec3d vel = new Vec3d((world.rand.nextFloat() - world.rand.nextFloat()) / 3, -0.15, (world.rand.nextFloat() - world.rand.nextFloat()) / 3);
            ParticleManager.spawnColoredSmoke(world, pos, ModColors.BLACK, vel);
        }
        super.handleStatusUpdate(id);
    }

    private void summonMiniBoss() {
        this.setFightMode(true);
        this.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 0.9f);

        addEvent(()-> {
            this.setFightMode(false);
            this.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1.3f, 0.5f);
            //summon Miniboss
            EntityVolatileSpirit spirit = new EntityVolatileSpirit(world);
            spirit.setPosition(this.posX, this.posY - 1.5, this.posZ);
            spirit.onSummonBoss(this.posX, this.posY, this.posZ);
            world.spawnEntity(spirit);
            this.setDead();
        }, 60);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "summon_controller", 0, this::predicateSummon));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateSummon(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
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
    protected boolean canDespawn() {

        // Edit this to restricting them not despawning in Dungeons
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
