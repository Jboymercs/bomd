package com.dungeon_additions.da.entity.gaelon_dungeon.friendly.ai;

import com.dungeon_additions.da.entity.frost_dungeon.wyrk.EntityFriendWyrk;
import com.dungeon_additions.da.entity.gaelon_dungeon.friendly.EntityFriendlyCursedRevenant;
import com.dungeon_additions.da.entity.sky_dungeon.friendly.EntityFriendlyHalberd;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class EntityAIRevenantHurtByTarget  extends EntityAITarget {
    EntityFriendlyCursedRevenant tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public EntityAIRevenantHurtByTarget(EntityFriendlyCursedRevenant theDefendingTameableIn)
    {
        super(theDefendingTameableIn, false);
        this.tameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.tameable.getOwner();

        if(entitylivingbase instanceof EntityFriendWyrk) {
            if(((EntityFriendWyrk) entitylivingbase).getOwner() == this.tameable.getOwner()) {
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
