package com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
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

public class EntityDragonAOE extends EntitySkyBase implements IAnimatable, IAnimationTickable {


    private int hitDelayTimer = 0;

    private AnimationFactory factory = new AnimationFactory(this);

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
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SELECTION_STRING, false));
        return PlayState.CONTINUE;
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
