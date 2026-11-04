package com.dungeon_additions.da.entity.dark_dungeon.boss;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.PlayerRohCorruptionUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

public class EntityDemonRitual extends EntityDarkBase  {

    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);

    public EntityDemonRitual(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
        this.setSize(0.8F, 1.0F);
        this.setImmovable(true);
        this.experienceValue = 0;
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setNoAI(true);
    }

    public EntityDemonRitual(World worldIn) {
        super(worldIn);
        this.setNoGravity(true);
        this.setSize(0.8F, 1.0F);
        this.setImmovable(true);
        this.experienceValue = 0;
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setNoAI(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!world.isRemote) {
            if(this.ticksExisted % 30 == 0 || this.ticksExisted == 1) {
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
                if(this.ticksExisted == 1) {
                    this.playSound(SoundsHandler.CAST_GENERIC_SPELL, 0.5f, 0.3f / (rand.nextFloat() * 0.4F + 0.4f));
                }
            }
            List<EntityLivingBase> collision_box = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable());
            if(!collision_box.isEmpty()) {
                for(EntityLivingBase base : collision_box) {
                    if(!(base instanceof EntityDarkBase)) {
                        this.playSound(SoundsHandler.ROH_SIGIL_BREAK, 0.4F, 1.4f + ModRand.getFloat(0.1f));
                        this.setDead();
                        if(base instanceof EntityPlayer) {
                            EntityPlayer player = ((EntityPlayer) base);
                            if(PlayerRohCorruptionUtils.getRohsCorruption(player) > 0) {
                                PlayerRohCorruptionUtils.setRohsCorruption(player, PlayerRohCorruptionUtils.getRohsCorruption(player) - 30);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            Main.proxy.spawnParticle(31,world, this.posX, this.posY + 0.5, this.posZ, 0, 0, 0);
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        this.playSound(SoundsHandler.ROH_SIGIL_BREAK, 0.4F, 1.4f + ModRand.getFloat(0.1f));
        if(source.getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) source.getTrueSource());
            if(PlayerRohCorruptionUtils.getRohsCorruption(player) > 0) {
                PlayerRohCorruptionUtils.setRohsCorruption(player, PlayerRohCorruptionUtils.getRohsCorruption(player) - 15);
            }
        }
        return super.attackEntityFrom(source, amount);
    }
}
