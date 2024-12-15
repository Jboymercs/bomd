package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.blossom.EntityAbstractVoidBlossom;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityLevitationBlock extends TileEntity implements ITickable {

    private boolean hasPlayedSound;
    private boolean hasPlayedSoundTwo;
    @Override
    public void update() {

        if(world.isRemote) {
            boolean triggered = (this.getBlockMetadata() & 8) > 0;
            if(triggered) {
                if(!hasPlayedSound) {
                    world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundsHandler.LICH_PREPARE_COMBO, SoundCategory.AMBIENT, 1.0F, world.rand.nextFloat() * 0.4F + 0.2F, false);
                    hasPlayedSound = true;
                    hasPlayedSoundTwo = false;
                }
            } else {
                if(!hasPlayedSoundTwo) {
                    world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundsHandler.LICH_PREPARE_SPELL, SoundCategory.AMBIENT, 1.0F, world.rand.nextFloat() * 0.7F + 0.4F, false);
                    hasPlayedSoundTwo = true;
                    hasPlayedSound = false;
                }
            }
        }
        if(!world.isRemote) {

            AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).grow(4.0, 20.0, 4.0);
            boolean triggered = (this.getBlockMetadata() & 8) > 0;

            if(triggered) {
                //adds to players if redstone is applied
                List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, box, e -> !e.getIsInvulnerable());
                if(!nearbyPlayers.isEmpty()) {
                    for(EntityPlayer base : nearbyPlayers) {
                        if(base.ticksExisted % 40 == 0) {
                            base.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 60, 1, true, false));
                        }
                    }
                }
            } else {
                //adds to mobs instead
                List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, box, e -> !e.getIsInvulnerable());
                if(!nearbyEntities.isEmpty()) {
                    for(EntityLivingBase base : nearbyEntities) {
                        if(!(base instanceof EntityPlayer) && !(base instanceof EntityArmorStand)) {
                            if(base instanceof EntityAbstractBase) {
                                if(!((EntityAbstractBase) base).iAmBossMob) {
                                    if(base.ticksExisted % 40 == 0) {
                                        base.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 60, 1, true, false));
                                    }
                                }
                            } else {
                                if(base.ticksExisted % 40 == 0) {
                                    base.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 60, 1, true, false));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
