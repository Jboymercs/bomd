package com.dungeon_additions.da.proxy;

import com.dungeon_additions.da.animation.IAnimatedEntity;
import com.dungeon_additions.da.blocks.BlockLeaveBase;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.model.ModelLichHelmet;
import com.dungeon_additions.da.util.glowLayer.GlowingMetadataSection;
import com.dungeon_additions.da.util.glowLayer.GlowingMetadataSectionSerializer;
import com.dungeon_additions.da.util.handlers.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy{

    private final ModelBiped MODEL_LICH_HELMET = new ModelLichHelmet(0F);

    @Override
    public void init() {
        RenderHandler.registerProjectileRenderers();
        RenderHandler.registerGeoEntityRenderers();
        RenderHandler.registerEntityRenderers();
        // Add custom metadataserializers
        Minecraft mc = Minecraft.getMinecraft();
        mc.metadataSerializer.registerMetadataSectionType(new GlowingMetadataSectionSerializer(), GlowingMetadataSection.class);

    }

    @Override
    public void registerEventHandlers() {

    }


    @Override
    public void handleAnimationPacket(int entityId, int index) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null) {
            IAnimatedEntity entity = (IAnimatedEntity) player.world.getEntityByID(entityId);
            if (entity != null) {
                if (index == -1) {
                    entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
                } else {
                    entity.setAnimation(entity.getAnimations()[index]);
                }
                entity.setAnimationTick(0);
            }
        }
    }

    @Override
    public Object getArmorModel(Item item, EntityLivingBase entity) {
        if(item == ModItems.NIGHT_LICH_HELMET){
            return MODEL_LICH_HELMET;
        }
        return null;
    }

    @Override
    public void setFancyGraphics(BlockLeaveBase block, boolean isFancy) {
        block.setFancyGraphics(isFancy);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
}
