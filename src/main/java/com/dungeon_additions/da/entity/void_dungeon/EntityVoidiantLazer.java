package com.dungeon_additions.da.entity.void_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.frost_dungeon.IDirectionalRender;
import com.dungeon_additions.da.packets.MessageDirectionForRender;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityVoidiantLazer extends Entity implements IDirectionalRender {
    private Vec3d renderLazerPos;
    public static final int TICK_LIFE = 30;

    public EntityVoidiantLazer(World worldIn) {
        super(worldIn);
    }

    public EntityVoidiantLazer(World worldIn, Vec3d renderLazerPos) {
        super(worldIn);
        this.renderLazerPos = renderLazerPos;
    }

    @Override
    public void onUpdate() {
        if (this.ticksExisted > 1 && !this.world.isRemote) {
            Main.network.sendToAllTracking(new MessageDirectionForRender(this, renderLazerPos), this);
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
        if (this.ticksExisted > TICK_LIFE) {
            this.setDead();
        }
        super.onUpdate();
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == ModUtils.PARTICLE_BYTE && this.getRenderDirection() != null) {
            ModUtils.lineCallback(this.getPositionVector(), this.getRenderDirection(), 10, (pos, i) -> {
                ParticleManager.spawnDust(world, pos, ModColors.AZURE, Vec3d.ZERO, ModRand.range(50, 65));
                //Main.proxy.spawnParticle(8, pos.x, pos.y, pos.z, 0,0,0);
            });
        }
        super.handleStatusUpdate(id);
    }

    @Override
    public float getEyeHeight() {
        return 0;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
    }

    @Override
    public void setRenderDirection(Vec3d dir) {
        this.renderLazerPos = dir;
    }

    public Vec3d getRenderDirection() {
        return this.renderLazerPos;
    }
}
