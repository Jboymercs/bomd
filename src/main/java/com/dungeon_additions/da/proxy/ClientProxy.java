package com.dungeon_additions.da.proxy;

import com.dungeon_additions.da.animation.IAnimatedEntity;
import com.dungeon_additions.da.blocks.BlockLeaveBase;
import com.dungeon_additions.da.entity.util.EntityMusicPlayer;
import com.dungeon_additions.da.event.EventBossMusic;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.model.*;
import com.dungeon_additions.da.util.glowLayer.GlowingMetadataSection;
import com.dungeon_additions.da.util.glowLayer.GlowingMetadataSectionSerializer;
import com.dungeon_additions.da.util.handlers.CameraPositionHandler;
import com.dungeon_additions.da.util.handlers.RenderHandler;
import com.dungeon_additions.da.util.particle.*;
import com.dungeon_additions.da.util.particle.confetti.ParticleConfetti;
import com.dungeon_additions.da.util.particle.cuts.ParticleColossusSigil;
import com.dungeon_additions.da.util.particle.cuts.ParticleLeftCut;
import com.dungeon_additions.da.util.particle.cuts.ParticleRightCut;
import com.dungeon_additions.da.util.particle.impact.*;
import com.dungeon_additions.da.util.particle.obsidilith.*;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ClientProxy extends CommonProxy{

    private final ModelBiped MODEL_LICH_HELMET = new ModelLichHelmet(0F);
    private final ModelBiped MODEL_DRAUGR_HELMET = new ModelDraugrHelmet(0F);

    private final ModelBiped MODEL_IMPERIAL_CHESTPLATE = new ModelImperialChestplate(0F);
    private final ModelBiped MODEL_VOIDIANT_CHESTPLATE = new ModelVoidiantChestplate(0F);
    private final ModelBiped MODEL_IMPERIAL_HELMET = new ModelImperialHelmet(0F);

    private final ModelBiped MODEL_WYRK_HELMET = new ModelWyrkHelmet(0F);
    private final ModelBiped MODEL_KING_HELMET = new ModelKingHelmet(0F);

    private final ModelBiped MODEL_INCENDIUM_HELMET = new ModelIncendiumHelmet(0F);

    private final ModelBiped MODEL_DARK_METAL_HELMET = new ModelDarkHelmet(0F);
    private final ModelBiped MODEL_OBSIDIAN_HELMET = new ModelObsidianHelmet(0F);
    private final ModelBiped MODEL_FLOWER_CROWN = new ModelVoidCrown(0F);
    private final ModelBiped MODEL_NOVIK_HELMET = new ModelNovikHelmet(0f);
    private final ModelBiped MODEL_NOVIK_CHESTPLATE = new ModelNovikChestplate(0f);
    private final ModelBiped MODEL_APATHYR_HELMET = new ModelApathyrHelmet(0f);
    private final ModelBiped MODEL_ADVENTURIC_HELMET = new ModelAdventuricHelmet(0f);
    private final ModelBiped MODEL_MAGE_HAT = new ModelMageHat(0F);

    @Override
    public void init() {

        RenderHandler.registerProjectileRenderers();
        RenderHandler.registerGeoEntityRenderers();
        RenderHandler.registerEntityRenderers();
        // Add custom metadataserializers
        Minecraft mc = Minecraft.getMinecraft();
        mc.metadataSerializer.registerMetadataSectionType(new GlowingMetadataSectionSerializer(), GlowingMetadataSection.class);
        super.init();
    }



    @Override
    public void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(CameraPositionHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(EventBossMusic.INSTANCE);
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
        if(item == ModItems.DRAUGR_HELMET) {
            return MODEL_DRAUGR_HELMET;
        }
        if(item == ModItems.WYRK_HELMET) {
            return MODEL_WYRK_HELMET;
        }
        if(item == ModItems.IMPERIAL_CHESTPLATE) {
            return MODEL_IMPERIAL_CHESTPLATE;
        }
        if(item == ModItems.IMPERIAL_HELMET) {
            return MODEL_IMPERIAL_HELMET;
        }
        if(item == ModItems.KINGS_HELMET) {
            return MODEL_KING_HELMET;
        }
        if(item == ModItems.INCENDIUM_HELMET) {
            return MODEL_INCENDIUM_HELMET;
        }
        if(item == ModItems.DARK_METAL_HELMET) {
            return MODEL_DARK_METAL_HELMET;
        }
        if(item == ModItems.VOIDIANT_CHESTPLATE) {
            return MODEL_VOIDIANT_CHESTPLATE;
        }
        if(item == ModItems.OBSIDIAN_HELMET) {
            return MODEL_OBSIDIAN_HELMET;
        }
        if(item == ModItems.VOID_FLOWER_CROWN) {
            return MODEL_FLOWER_CROWN;
        }
        if(item == ModItems.NOVIK_HELMET) {
            return MODEL_NOVIK_HELMET;
        }
        if(item == ModItems.NOVIK_CHESTPLATE) {
            return MODEL_NOVIK_CHESTPLATE;
        }
        if(item == ModItems.APATHYR_HELMET) {
            return MODEL_APATHYR_HELMET;
        }
        if(item == ModItems.ADVENTUIC_HELMET) {
            return MODEL_ADVENTURIC_HELMET;
        }
        if(item == ModItems.MAGE_HELMET) {
            return MODEL_MAGE_HAT;
        }
        return null;
    }

    /**
     * This is used by the Particle Spawning as an ID system for out Particles.
     * We do not require Ids for Particles, it's just more convenient for sending over packets!
     * */

    @Override
    public void spawnParticle(int particle, double posX, double posY, double posZ, double speedX, double speedY, double speedZ, int... parameters)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        World world = minecraft.world;
        minecraft.effectRenderer.addEffect(getFactory(particle).createParticle(0, world, posX, posY, posZ, speedX, speedY, speedZ, parameters));
    }

    @SideOnly(Side.CLIENT)
    public static IParticleFactory getFactory(int particleId)
    {
        switch(particleId)
        {
            default:
            case 1:
                return new ParticlePixel.Factory();
            case 2:
                return new ParticleDash.Factory();
            case 3:
                return new ParticleFluff.Factory();
            case 4:
                return new ParticlePuzzle.Factory();
            case 5:
                return new ParticleLargeFlame.Factory();
            case 6:
                return new ParticleStar.Factory();
            case 7:
                return new ParticleEnchantment.Factory();
            case 8:
                return new ParticleLevitation.Factory();
            case 9:
                return new ParticleVoid.Factory();
            case 10:
                return new ParticleRed.Factory();
            case 11:
                return new ParticlePurple.Factory();
            case 12:
                return new ParticleOrange.Factory();
            case 13:
                return new ParticleBlue.Factory();
            case 14:
                return new ParticleYellow.Factory();
            case 15:
                return new ParticleGaelonGleam.Factory();
            case 16:
                return new ParticleApathyrTarget.Factory();
            case 17:
                return new ParticleCrystalPixel.Factory();
            case 18:
                return new ParticleImpact.Factory();
            case 19:
                return new PoisonImpact.Factory();
            case 20:
                return new ParticleBigImpact.Factory();
            case 21:
                //Blue Impact
                return new ParticleBlueImpact.Factory();
            case 22:
                //Purple Impact
                return new ParticlePurpleImpact.Factory();
            case 23:
                return new ParticleConfetti.Factory();
            case 24:
                return new ParticleLeftCut.Factory();
            case 25:
                return new ParticleRightCut.Factory();
            case 26:
                return new ParticleColossusSigil.Factory();
        }
    }

    @Override
    public boolean doesPlayerHaveXAdvancement(EntityPlayer player, ResourceLocation Id) {
        if(player instanceof EntityPlayerSP) {
            ClientAdvancementManager manager = ((EntityPlayerSP) player).connection.getAdvancementManager();
            Advancement advancement = manager.getAdvancementList().getAdvancement(Id);
            if(advancement == null) {
                return false;
            }
            AdvancementProgress progress = manager.advancementToProgress.get(advancement);
            return progress != null && progress.isDone();
        }

        return super.doesPlayerHaveXAdvancement(player, Id);
    }

    @Override
    public void setFancyGraphics(BlockLeaveBase block, boolean isFancy) {
        block.setFancyGraphics(isFancy);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override
    public void playMusic(SoundEvent soundEvent, @Nullable Entity linkedEntity) {
        if (Minecraft.getMinecraft().world != null) {
            SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();

            if (!EntityMusicPlayer.isPlaying(soundEvent, linkedEntity)) {
                stopMusic();
                soundHandler.playSound(new EntityMusicPlayer(linkedEntity, soundEvent));
            }
        }
    }


    @Override
    public void stopMusic() {
        Minecraft.getMinecraft().getSoundHandler().stopSounds();
    }
}
