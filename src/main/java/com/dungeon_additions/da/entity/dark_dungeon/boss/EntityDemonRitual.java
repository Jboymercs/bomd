package com.dungeon_additions.da.entity.dark_dungeon.boss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntityDemonRitual extends EntityDarkBase implements IAnimationTickable, IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public EntityDemonRitual(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.setSize(0.8F, 1.0F);
        this.setImmovable(true);
        this.experienceValue = 0;
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setNoAI(true);
    }

    public EntityDemonRitual(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setSize(0.8F, 1.0F);
        this.setImmovable(true);
        this.experienceValue = 0;
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setNoAI(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!world.isRemote) {
            if(this.ticksExisted % 30 == 0) {
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            }
            List<EntityLivingBase> collision_box = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable());
            if(!collision_box.isEmpty()) {
                for(EntityLivingBase base : collision_box) {
                    if(!(base instanceof EntityDarkBase)) {
                        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.4F, 1.4f + ModRand.getFloat(0.1f));
                        this.setDead();
                    }
                }
            }
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            Main.proxy.spawnParticle(31,world, this.posX, this.posY + 0.5, this.posZ, 0, 0, 0);
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.4F, 1.4f + ModRand.getFloat(0.1f));
        return super.attackEntityFrom(source, amount);
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
}
