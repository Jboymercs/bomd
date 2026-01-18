package com.dungeon_additions.da.entity.desert_dungeon.boss;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.desert_dungeon.EntityDesertBase;
import com.dungeon_additions.da.entity.gaelon_dungeon.friendly.EntityFriendlyCursedRevenant;
import com.google.common.base.Optional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EntitySharedDesertBoss extends EntityDesertBase {


 protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntitySharedDesertBoss.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Boolean> SHIELDED = EntityDataManager.createKey(EntitySharedDesertBoss.class, DataSerializers.BOOLEAN);


    public void setShielded(boolean value) {this.dataManager.set(SHIELDED, Boolean.valueOf(value));}
    public boolean isShielded() {return this.dataManager.get(SHIELDED);}

    @Nullable
    public UUID getOtherBossId()
    {
        return (UUID)((Optional)this.dataManager.get(OWNER_UNIQUE_ID)).orNull();
    }

    public void setOtherBossId(@Nullable UUID p_184754_1_)
    {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(p_184754_1_));
    }

    // We want to ensure that when this method is called it'll return exactly the other boss
    @Nullable
    public EntityLivingBase getOtherBoss()
    {
        UUID uuid = this.getOtherBossId();
        if(uuid == null) {
            return null;
        }
        else {
            List<EntitySharedDesertBoss> nearbyBoss = this.world.getEntitiesWithinAABB(EntitySharedDesertBoss.class, this.getEntityBoundingBox().grow(50D), e -> !e.getIsInvulnerable());
            if(!nearbyBoss.isEmpty()) {
                for(EntitySharedDesertBoss boss : nearbyBoss) {
                    if(boss.getUniqueID() == uuid) {
                        return boss;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Shielded", this.isShielded());
        if (this.getOtherBossId() == null)
        {
            nbt.setString("OwnerUUID", "");
        }
        else
        {
            nbt.setString("OwnerUUID", this.getOtherBossId().toString());
        }
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setShielded(nbt.getBoolean("Shielded"));
        String s;
        if (nbt.hasKey("OwnerUUID", 8))
        {
            s = nbt.getString("OwnerUUID");
        }
        else
        {
            String s1 = nbt.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(Objects.requireNonNull(this.getServer()), s1);
        }

        if (!s.isEmpty())
        {
            this.setOtherBossId(UUID.fromString(s));
        }
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SHIELDED, Boolean.valueOf(false));
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
    }


    public EntitySharedDesertBoss(World world, int timesUsed, BlockPos pos) {
        super(world);
        this.timesUsed = timesUsed;
        this.iAmBossMob = true;
    }

    public EntitySharedDesertBoss(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.iAmBossMob = true;
    }

    public EntitySharedDesertBoss(World worldIn) {
        super(worldIn);
        this.iAmBossMob = true;
    }


    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }
}
