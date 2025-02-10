package com.dungeon_additions.da.entity.sky_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.frost_dungeon.IDirectionalRender;
import com.dungeon_additions.da.entity.night_lich.EntityLichStaffAOE;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.packets.MessageDirectionForRender;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntitySkyBolt extends EntitySkyBase implements IAnimatable, IAnimationTickable, IDirectionalRender {
    private Vec3d renderLazerPos;
    private AnimationFactory factory = new AnimationFactory(this);

    public EntitySkyBolt(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(0.9F, 7.0F);
    }

    private EntityPlayer player;
    public int delay = 0;

    public EntitySkyBolt(World worldIn, Vec3d renderLazerPos) {
        super(worldIn);
        this.renderLazerPos = renderLazerPos;
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(0.9F, 7.0F);
    }

    public EntitySkyBolt(World worldIn, Vec3d renderLazerPos, EntityPlayer summoner, int delay) {
        super(worldIn);
        this.renderLazerPos = renderLazerPos;
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(0.9F, 7.0F);
        this.player = summoner;
        this.delay = delay;
    }

    public EntitySkyBolt(World worldIn) {
        super(worldIn);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(0.9F, 7.0F);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(26D);
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

        if (player == null) {
            if (this.ticksExisted > 1 && !this.world.isRemote) {
                Main.network.sendToAllTracking(new MessageDirectionForRender(this, renderLazerPos), this);
            }

            if (ticksExisted < 17) {
                this.world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            } else {
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            }

            if (ticksExisted == 17) {
                this.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 0.9f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            }

            if (ticksExisted == 20) {
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntitySkyBase)));
                if (!targets.isEmpty()) {
                    for (EntityLivingBase target : targets) {
                        if (!(target instanceof EntitySkyBase)) {
                            Vec3d offset = target.getPositionVector().add(ModUtils.yVec(1.0D));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                            float damage = this.getAttack();
                            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                        }
                    }
                }
            }

            if (ticksExisted == 30) {
                this.setDead();
            }
        } else {
            if (this.ticksExisted > 1 && !this.world.isRemote) {
                Main.network.sendToAllTracking(new MessageDirectionForRender(this, renderLazerPos), this);
            }

            if (ticksExisted < 17 + delay) {
                this.world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            } else {
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            }

            if (ticksExisted == 17 + delay) {
                this.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 0.9f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
            }

            if (ticksExisted == 20 + delay) {
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityPlayer)));
                if (!targets.isEmpty()) {
                    this.damageOverride = true;
                    for (EntityLivingBase target : targets) {
                        if (!(target instanceof EntityPlayer)) {
                            Vec3d offset = target.getPositionVector().add(ModUtils.yVec(1.0D));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                            float damage = (float) (this.getAttack() * 0.7);
                            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.1f, 0, false);
                        }
                    }
                }
            }

            if (ticksExisted == 30 + delay) {
                this.setDead();
            }
        }
    }


    @Override
    public void handleStatusUpdate(byte id) {
        if (id == ModUtils.PARTICLE_BYTE && this.getRenderDirection() != null) {
            ModUtils.lineCallback(this.getPositionVector(), this.getRenderDirection(), 10, (pos, i) -> {
                ParticleManager.spawnDust(world, pos, ModColors.WHITE, Vec3d.ZERO, ModRand.range(10, 15));
            });
        }
        if (id == ModUtils.SECOND_PARTICLE_BYTE) {
            ParticleManager.spawnDust(world, this.getPositionVector().add(0, 0.5, 0), ModColors.RANDOM_GREY, new Vec3d(0,0.05, 0), ModRand.range(10, 15));
        }
        super.handleStatusUpdate(id);
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
    public void registerControllers(AnimationData data) {

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
    public void setRenderDirection(Vec3d dir) {
        this.renderLazerPos = dir;
    }

    public Vec3d getRenderDirection() {
        return this.renderLazerPos;
    }
}
