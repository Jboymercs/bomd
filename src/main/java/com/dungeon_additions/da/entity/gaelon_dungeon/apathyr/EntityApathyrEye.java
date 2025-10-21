package com.dungeon_additions.da.entity.gaelon_dungeon.apathyr;

import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityGaelonBase;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityApathyrEye extends EntityGaelonBase implements IAnimatable, IAnimationTickable {

    private EntityApathyr parent;
    private final AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_IDLE = "idle";

    public EntityApathyrEye(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.9F, 0.9F);
        this.setImmovable(true);
        this.setNoGravity(true);
    }

    public EntityApathyrEye(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 0.9F);
        this.setImmovable(true);
        this.setNoGravity(true);
    }

    public EntityApathyrEye(World worldIn, EntityApathyr parent) {
        super(worldIn);
        this.parent = parent;
        this.setSize(0.9F, 0.9F);
        this.setImmovable(true);
        this.setNoGravity(true);
    }

    boolean fail_look = false;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.hurtTime == 1) {
            this.setDead();
            world.setBlockState(this.getPosition(), ModBlocks.DARK_GLOW_LIT_PILLAR.getDefaultState());
        }

        //ensure this mob looks at the boss while supporting it
        if(parent != null) {
            //heals Apathyr if below Statline
            if(parent.getHealth()/parent.getMaxHealth() < parent.getStatLine()) {
                parent.heal(1.0f);
            }
            if(this.ticksExisted % 100 == 0 && this.getDistance(parent) > 38) {
                this.fail_look = true;
            } else {
                this.fail_look = false;
            }

            if(!this.fail_look) {
                this.faceEntity(parent, 30, 30);
            }
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(38D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
    }



    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller_idle", 0, this::predicateIDLE));
    }

    private <E extends IAnimatable> PlayState predicateIDLE(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
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
