package com.dungeon_additions.da.animation.example;

import com.dungeon_additions.da.animation.EZAnimation;
import com.dungeon_additions.da.animation.EZAnimationHandler;
import com.dungeon_additions.da.animation.IAnimatedEntity;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import net.minecraft.world.World;

public class EntityExample extends EntityAbstractBase implements IAnimatedEntity {
    //Here we're actually calling the animations
    public static final EZAnimation ANIMATION_WAVE = EZAnimation.create(30);


    private int animationTick;
    private EZAnimation currentAnimation;

    public EntityExample(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityExample(World worldIn) {
        super(worldIn);
    }

    public int timerForAnimation = 600;

    @Override
    public void onUpdate() {
        super.onUpdate();


        if(timerForAnimation < 0) {
            this.setAnimation(ANIMATION_WAVE);
            timerForAnimation = 600;
            System.out.println("Playing Animation");
        } else {
            timerForAnimation--;
        }
        //Update the animations with the Handler
        EZAnimationHandler.INSTANCE.updateAnimations(this);
    }


    //All methods used by the Interface
    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
    animationTick = tick;
    }

    @Override
    public EZAnimation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(EZAnimation animation) {
        currentAnimation = animation;
    }

    @Override
    public EZAnimation[] getAnimations() {
        return new EZAnimation[]{ANIMATION_WAVE};
    }
}
