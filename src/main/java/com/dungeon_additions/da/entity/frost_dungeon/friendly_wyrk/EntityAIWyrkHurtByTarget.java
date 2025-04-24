package com.dungeon_additions.da.entity.frost_dungeon.friendly_wyrk;

import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.wyrk.EntityFriendWyrk;
import com.dungeon_additions.da.entity.sky_dungeon.friendly.EntityFriendlyHalberd;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class EntityAIWyrkHurtByTarget extends EntityAITarget {
    EntityFriendWyrk tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public EntityAIWyrkHurtByTarget(EntityFriendWyrk theDefendingTameableIn)
    {
        super(theDefendingTameableIn, false);
        this.tameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.tameable.getOwner();

        if(entitylivingbase instanceof EntityFriendlyHalberd) {
            if(((EntityFriendlyHalberd) entitylivingbase).getOwner() == this.tameable.getOwner()) {
                return false;
            }
        }

        if (entitylivingbase == null)
        {
            return false;
        }
        else
        {
            this.attacker = entitylivingbase.getRevengeTarget();
            int i = entitylivingbase.getRevengeTimer();
            return i != this.timestamp && this.isSuitableTarget(this.attacker, false);
        }

    }

    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.attacker);
        EntityLivingBase entitylivingbase = this.tameable.getOwner();

        if (entitylivingbase != null)
        {
            this.timestamp = entitylivingbase.getRevengeTimer();
        }

        super.startExecuting();
    }
}
