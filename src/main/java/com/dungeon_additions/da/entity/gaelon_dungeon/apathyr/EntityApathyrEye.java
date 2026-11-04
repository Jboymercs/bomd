package com.dungeon_additions.da.entity.gaelon_dungeon.apathyr;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityGaelonBase;
import com.dungeon_additions.da.init.ModBlocks;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityApathyrEye extends EntityGaelonBase {

    private EntityApathyr parent;
    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);
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
    public void updateAnimations() {
        animator.setStateAnim(ANIM_IDLE, true);
    }
}
