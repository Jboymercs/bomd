package com.dungeon_additions.da.entity.blossom;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityGenericWave extends EntityAbstractBase  {
    /**
     * A simple low AI that just damages the player and disappears following, creates a nice effect for waves or AOE
     */
    private final String ANIM_SHOOT = "shoot";
    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);

    public EntityGenericWave(World worldIn) {
        super(worldIn);
        this.setImmovable(true);

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

        if(ticksExisted > 20 && ticksExisted < 27) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
        if(ticksExisted == 20) {
            this.playSound(SoundsHandler.SPORE_IMPACT, 0.75f, 1.0f);
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityVoidBlossom || e instanceof EntityVoidSpike || e instanceof EntityGenericWave)));

            if(!targets.isEmpty()) {
                for(EntityLivingBase base : targets) {
                    if(base != this && !(base instanceof EntityVoidBlossom || base instanceof EntityVoidSpike) && !(base instanceof EntityMiniBlossom)) {
                        Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.0D));
                        DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
                        float damage = this.getAttack();
                        ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 0.2f, 0, false, 0.3F);
                        addPotionEffect(new PotionEffect(MobEffects.POISON, 9, 3));
                        if(base != null && !world.isRemote) {
                            double distSq = this.getDistanceSq(base.posX, base.getEntityBoundingBox().minY, base.posZ);
                            double distance = Math.sqrt(distSq);
                            if (base.getActivePotionEffect(MobEffects.POISON) == null && distance < 1) {
                                base.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 2));
                            }
                        }
                    }
                }
            }

        }
        if(ticksExisted == 30) {
            this.setDead();
        }
    }


    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(0.5D)), ModColors.GREEN, new Vec3d(0,0.1,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(1.5D)), ModColors.GREEN, new Vec3d(0,0.1,0));
            ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.yVec(3.0D)), ModColors.GREEN, new Vec3d(0,0.1,0));
        }
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.blossom_attack_damange * 0.5);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

  @Override
  public void updateAnimations() {
        animator.setStateAnim(ANIM_SHOOT);
  }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
}
