package com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityDragonAOE extends EntitySkyBase {


    private int hitDelayTimer = 0;

    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);

    private final String ANIM_SHOOT = "summon";

    private final String ANIM_SHOOT_2 = "summon_1";
    private final String ANIM_SHOOT_3 = "summon_2";
    private final String ANIM_SHOOT_4 = "summon_3";
    private final String ANIM_SHOOT_5 = "summon_4";

    protected int selection = ModRand.range(1, 5);

    private String ANIM_SELECTION_STRING;

    public EntityDragonAOE(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 1.95F);
        this.noClip = true;
        selectAnimationTooPlay();
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public void selectAnimationTooPlay() {
        if(selection == 1) {
            ANIM_SELECTION_STRING = ANIM_SHOOT_2;
        } else if (selection == 2) {
            ANIM_SELECTION_STRING = ANIM_SHOOT_3;
        }else if(selection == 3) {
            ANIM_SELECTION_STRING = ANIM_SHOOT_4;
        }else if(selection == 4) {
            ANIM_SELECTION_STRING = ANIM_SHOOT_5;
        } else {
            ANIM_SELECTION_STRING = ANIM_SHOOT;
        }
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
            hitDelayTimer--;
            if(this.ticksExisted > 15 && this.hitDelayTimer < 0 && this.ticksExisted <= 75) {
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntitySkyBase)));

                if(!targets.isEmpty()) {
                    for(EntityLivingBase base : targets) {
                        if(!(base instanceof EntitySkyBase)) {
                            Vec3d offset = base.getPositionVector().add(ModUtils.yVec(1.0D));
                            DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
                            float damage = this.getAttack();
                            ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
                            this.hitDelayTimer = 7;
                        }
                    }

                }
            }
        }

        if (this.ticksExisted >= 80) {
            this.setDead();
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.high_dragon_king_damage * 0.75);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void updateAnimations() {
        animator.setStateAnim(ANIM_SELECTION_STRING);
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
