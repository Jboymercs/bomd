package com.dungeon_additions.da.util.handlers;

import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.util.player.IEntityCameraOffset;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CameraPositionHandler {

    public static CameraPositionHandler INSTANCE = new CameraPositionHandler();

    private float getShakeStrength(Entity renderViewEntity) {
        float screenShake = 0.0F;
        World world = renderViewEntity.world;

        if(renderViewEntity != null) {
            for(Entity entity : (List<Entity>) world.loadedEntityList) {
                if(entity instanceof IScreenShake) {
                    IScreenShake shake = (IScreenShake) entity;
                    screenShake += shake.getShakeIntensity(renderViewEntity);
                }
            }

            for(TileEntity tile : (List<TileEntity>) world.loadedTileEntityList) {
                if(tile instanceof IScreenShake) {
                    IScreenShake shake = (IScreenShake) tile;
                    screenShake += shake.getShakeIntensity(renderViewEntity);
                }
            }

    }
        return MathHelper.clamp(screenShake, 0.0F, 5.0F);
    }


    private double prevPosX;
    private double prevPosY;
    private double prevPosZ;
    private boolean didChange = false;

    private List<IEntityCameraOffset> offsetEntities = new ArrayList<>();
    private float shakeStrength = 0.0f;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity();

        if(renderViewEntity != null) {
            this.shakeStrength = this.getShakeStrength(renderViewEntity);

            this.offsetEntities.clear();

            List<IEntityCameraOffset> offsetEntities = new ArrayList<IEntityCameraOffset>();
            for(Entity entity : (List<Entity>) renderViewEntity.world.loadedEntityList) {
                if(entity instanceof IEntityCameraOffset)
                    offsetEntities.add((IEntityCameraOffset)entity);
            }
        } else {
            this.shakeStrength = 0.0f;
            this.offsetEntities.clear();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderTickStart(TickEvent.RenderTickEvent event) {
        Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity();

        if(renderViewEntity != null) {
            boolean shouldChange = this.shakeStrength > 0.0F || !this.offsetEntities.isEmpty();

            if((shouldChange && !Minecraft.getMinecraft().isGamePaused()) || this.didChange) {
                if(event.phase == TickEvent.Phase.START) {
                    this.prevPosX = renderViewEntity.posX;
                    this.prevPosY = renderViewEntity.posY;
                    this.prevPosZ = renderViewEntity.posZ;

                    Random rnd = renderViewEntity.world.rand;

                    renderViewEntity.posX += rnd.nextFloat() * this.shakeStrength;
                    renderViewEntity.posY += rnd.nextFloat() * this.shakeStrength;
                    renderViewEntity.posZ += rnd.nextFloat() * this.shakeStrength;

                    if(!this.offsetEntities.isEmpty()) {
                        for(IEntityCameraOffset offset : offsetEntities)
                            if(((Entity) offset).isEntityAlive() && offset.applyOffset(renderViewEntity, event.renderTickTime))
                                break;
                    }

                    this.didChange = true;
                } else {
                    renderViewEntity.posX = this.prevPosX;
                    renderViewEntity.posY = this.prevPosY;
                    renderViewEntity.posZ = this.prevPosZ;

                    this.didChange = false;
                }
            }
        }
    }
}
