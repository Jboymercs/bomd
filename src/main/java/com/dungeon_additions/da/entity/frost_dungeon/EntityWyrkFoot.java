package com.dungeon_additions.da.entity.frost_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.ActionWyrkFootAOE;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;


public class EntityWyrkFoot extends EntityFrostBase {

    private final String ANIM_SUMMON = "summon";
    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);

    public EntityWyrkFoot(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(0.8F, 1.8F);
    }

    public EntityWyrkFoot(World worldIn) {
        super(worldIn);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(0.8F, 1.8F);
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

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(this.ticksExisted == 2) {
            //play sounds
            this.playSound(SoundsHandler.BIG_WYRK_FOOT_IMPACT, 1.5f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }

        if(this.ticksExisted == 25) {
            new ActionWyrkFootAOE().performAction(this, this);
        }

        if(this.ticksExisted == 40) {
            this.setDead();
        }
    }

    @Override
    public void updateAnimations() {
        animator.setStateAnim(ANIM_SUMMON);
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
