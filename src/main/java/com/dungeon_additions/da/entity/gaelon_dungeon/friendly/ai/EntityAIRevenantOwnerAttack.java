package com.dungeon_additions.da.entity.gaelon_dungeon.friendly.ai;

import com.dungeon_additions.da.entity.gaelon_dungeon.friendly.EntityFriendlyCursedRevenant;
import com.dungeon_additions.da.entity.sky_dungeon.friendly.EntityFriendlyHalberd;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class EntityAIRevenantOwnerAttack  extends EntityAITarget {
    EntityFriendlyCursedRevenant tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public  EntityAIRevenantOwnerAttack(EntityFriendlyCursedRevenant theEntityTameableIn)
    {
        super(theEntityTameableIn, false);
        this.tameable = theEntityTameableIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.tameable.getOwner();

        if (entitylivingbase == null)
        {
            return false;
        }
        else
        {
            this.attacker = entitylivingbase.getLastAttackedEntity();
            int i = entitylivingbase.getLastAttackedEntityTime();
            return i != this.timestamp && this.isSuitableTarget(this.attacker, false);
        }
    }

    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.attacker);
        EntityLivingBase entitylivingbase = this.tameable.getOwner();

        if (entitylivingbase != null)
        {
            this.timestamp = entitylivingbase.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }
}
