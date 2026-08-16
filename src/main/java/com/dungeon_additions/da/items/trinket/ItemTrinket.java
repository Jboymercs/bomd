package com.dungeon_additions.da.items.trinket;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.capabilities.CapabilityItemAnimations;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.PotionTrinketConfig;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDelayedLazer;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntityColossusSigil;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntitySummonedMace;
import com.dungeon_additions.da.entity.generic.EntityDelayedExplosion;
import com.dungeon_additions.da.entity.generic.EntityRallyFlag;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicMissile;
import com.dungeon_additions.da.entity.player.ActionCastPoisonMist;
import com.dungeon_additions.da.entity.player.ActionPlayerPetalWave;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyTornado;
import com.dungeon_additions.da.integration.BaublesIntegration;
import com.dungeon_additions.da.items.ItemBase;
import com.dungeon_additions.da.packets.PacketParryAnimationItem;
import com.dungeon_additions.da.proxy.ClientProxy;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.PlayerFalterUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Vector;


@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemTrinket extends ItemBase implements IBauble{
    private String info_loc;


    private int slotId;
    public boolean hasKeyAbility = false;
    private boolean isParrying = false;
    private int currentLife = 0;
    private double setPlayerLife = 0;
    private boolean dealtDamage = false;

    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        if (slotId == 1) {
            return BaubleType.CHARM;
        } else if (slotId == 2) {
            return BaubleType.AMULET;
        } else if (slotId == 3) {
            return BaubleType.BELT;
        } else if (slotId == 4) {
            return BaubleType.TRINKET;
        }
        return BaubleType.RING;
    }

    @Optional.Method(modid = "baubles")
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        int max = 0;
        if(player instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)player;
            for (int i = 0; i < BaublesApi.getBaublesHandler(entityplayer).getSlots(); i++) {
                ItemStack stack = BaublesApi.getBaublesHandler(entityplayer).getStackInSlot(i);
                if(stack.getItem() instanceof ItemTrinket) {
                    max = max + 1;
                }
            }
        }
        return max <= PotionTrinketConfig.max_trinkets_allowed;
    }

    public ItemTrinket(String name, String info_loc, int maxDamage, int slotID) {
        super(name);
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        this.info_loc = info_loc;
        this.maxStackSize = 1;
        if(PotionTrinketConfig.trinkets_unbreakable) {
            this.setMaxDamage(-1);
        } else {
            this.setMaxDamage(maxDamage);
        }
        this.slotId = slotID;
    }

    public ItemTrinket(String name, String info_loc, int maxDamage, int slotID, boolean hasKeyAbility) {
        super(name);
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        this.info_loc = info_loc;
        this.maxStackSize = 1;
        this.slotId = slotID;
        if(PotionTrinketConfig.trinkets_unbreakable) {
            this.setMaxDamage(-1);
        } else {
            this.setMaxDamage(maxDamage);
        }
        this.hasKeyAbility = hasKeyAbility;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
            tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
            if(!BaublesIntegration.isEnabled()) {
                tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.trinket_item.name"));
            }
            if(hasKeyAbility) {
                tooltip.add(TextFormatting.GOLD + I18n.translateToLocal("description.dungeon_additions.trinket_pre.name") + TextFormatting.AQUA + I18n.translateToLocal(ClientProxy.SHIELD_ABILITY.getDisplayName()));
            }
    }

    //allows for access to perform events tied to keyed ability trinkets
    public boolean onApplyButtonPressed(EntityPlayer player, World world, ItemStack stack, int type) {
        //Wind Trinket
        if (!world.isRemote) {
            if (type == 1 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                EntitySkyTornado tornado = new EntitySkyTornado(player.world, true, player);
                tornado.setPosition(player.posX, player.posY, player.posZ);
                player.world.spawnEntity(tornado);
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.CAST_GENERIC_SPELL, SoundCategory.NEUTRAL, 1f, 0.8f / (world.rand.nextFloat() * 0.4F + 0.4f));
                stack.damageItem(1, player);
                player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.storm_calling_cooldown);
                return true;
                //Sigil Trinket
            } else if (type == 2 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                EntityColossusSigil sigil = new EntityColossusSigil(player.world, player, PotionTrinketConfig.golden_ritual_damage + ModUtils.addMageSetBonus(player, 0), true);
                sigil.setPosition(player.posX, player.posY + 2, player.posZ);
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.CAST_GENERIC_SPELL, SoundCategory.NEUTRAL, 1f, 0.8f / (world.rand.nextFloat() * 0.4F + 0.4f));
                player.world.spawnEntity(sigil);
                stack.damageItem(1, player);
                player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.golden_ritual_cooldown);
                return true;
                //teleport trinket
            } else if (type == 3 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                Vec3d lookPos = player.getLookVec();
                int distanceTeleport = (int) (6 + ModUtils.addMageSetBonus(player, 0));
                for (int i = distanceTeleport; i >= 1; i--) {
                    Vec3d setPos = new Vec3d(player.posX + lookPos.x * i, player.posY, player.posZ + lookPos.z * i);
                    BlockPos posToo = new BlockPos(setPos.x, setPos.y, setPos.z);
                    int y = ModUtils.getSurfaceHeightZeroReturn(world, new BlockPos(posToo.getX(), 0, posToo.getZ()), posToo.getY() - 2, posToo.getY() + 3);
                    if (y != 0) {
                        BlockPos posAttempt = new BlockPos(setPos.x, y + 1, setPos.z);
                        if (player.canBePushed() && !player.isBeingRidden()) {
                            ModUtils.performNTimes(15, (b) -> {
                                Main.proxy.spawnParticle(30, player.posX + ModRand.getFloat(1F), player.posY + ModRand.getFloat(2) + 2, player.posZ + ModRand.getFloat(1F), 0, 0.06, 0, 15423215);
                            });
                            player.setPosition(setPos.x, posAttempt.getY(), setPos.z);
                            stack.damageItem(1, player);
                            player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 1f, 1f);
                            player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.blink_bolt_cooldown);
                            break;
                        }
                    }
                }
                //dodge trinket
            } else if (type == 4 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                double playerMovementSpeed = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() / player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
                if (player.isSneaking()) {
                    Vec3d lookPos = player.getLookVec().scale(((-1.4 * (playerMovementSpeed - 0.4D))));
                    if (player.canBePushed() && player.onGround) {
                        player.motionX = lookPos.x;
                        player.motionY = lookPos.y * 0.75;
                        player.motionZ = lookPos.z;
                        player.velocityChanged = true;
                        stack.damageItem(1, player);
                        player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.dodge_trinket_cooldown);
                        world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.APATHYR_SLIGHT_DASH, SoundCategory.NEUTRAL, 0.7f, 0.3f / (world.rand.nextFloat() * 0.4F + 0.4f));
                    }
                } else {
                    Vec3d lookPos = player.getLookVec().scale(((1.4 * (playerMovementSpeed - 0.3D))));
                    if (player.canBePushed() && player.onGround) {
                        player.motionX = lookPos.x;
                        player.motionY = lookPos.y * 0.3;
                        player.motionZ = lookPos.z;
                        player.velocityChanged = true;
                        stack.damageItem(1, player);
                        player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.dodge_trinket_cooldown);
                        world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.APATHYR_SLIGHT_DASH, SoundCategory.NEUTRAL, 0.7f, 0.3f / (world.rand.nextFloat() * 0.4F + 0.4f));
                    }

                }
                //Rotten Trinket
            } else if (type == 5 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                Vec3d offset = player.getPositionVector().add(0, 1, 0);
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(player).disablesShields().build();
                float damage = PotionTrinketConfig.rotten_ring_damage + ModUtils.addMageSetBonus(player, 0, 2F);
                ModUtils.handleAreaImpact(3.0f, (e) -> damage, player, offset, source, 0.8f, 0, false, MobEffects.POISON, 0, 200);
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.ROT_SELF_AOE, SoundCategory.NEUTRAL, 1.25f, 0.8f / (world.rand.nextFloat() * 0.4F + 0.4f));
                stack.damageItem(1, player);
                player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.rotten_ring_cooldown);
                ModUtils.circleCallback(3, 35, (pos)-> {
                    pos = new Vec3d(pos.x, 0, pos.y);
                    Vec3d posToo = player.getPositionVector().add(0, 1, 0);
                    Vec3d vel = pos.normalize().scale(0.25F).add(ModUtils.yVec(0));
                    Main.proxy.spawnParticle(30, world, posToo.x, posToo.y, posToo.z, vel.x, vel.y, vel.z,45824 );
                });
                //Blue Trinket
            } else if (type == 6 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                //spawn 3 Projectiles
                this.spawnBlueProjectile(player, world, stack, new Vec3d(0,0,0));
                this.spawnBlueProjectile(player, world, stack, new Vec3d(0,0,1));
                this.spawnBlueProjectile(player, world, stack, new Vec3d(0,0,-1));
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.COLOSSUS_HILT_SLAM, SoundCategory.NEUTRAL, 1f, 0.8f / (world.rand.nextFloat() * 0.4F + 0.4f));
                stack.damageItem(1, player);
                player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.cosmic_prowess_cooldown);
                //flame explosion trinket
            } else if (type == 7 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                Vec3d playerLookVec = player.getLookVec();
                Vec3d setPos = new Vec3d(player.posX + playerLookVec.x * 2, (player.posY + playerLookVec.y * 2) + player.getEyeHeight(), player.posZ + playerLookVec.z * 2);
                Vec3d setPos2 = new Vec3d(player.posX + playerLookVec.x * 6, (player.posY + playerLookVec.y * 6) + player.getEyeHeight(), player.posZ + playerLookVec.z * 6);
                EntityDelayedExplosion explosion = new EntityDelayedExplosion(world, player, PotionTrinketConfig.flame_explosion_trinket_damage + ModUtils.addMageSetBonus(player, 0, 1.5F), true, false);
                EntityDelayedExplosion explosion2 = new EntityDelayedExplosion(world, player, PotionTrinketConfig.flame_explosion_trinket_damage + ModUtils.addMageSetBonus(player, 0, 1.5F), true, false);
                explosion.setPosition(setPos.x, setPos.y, setPos.z);
                explosion2.setPosition(setPos2.x, setPos2.y, setPos2.z);
                world.spawnEntity(explosion2);
                world.spawnEntity(explosion);
                stack.damageItem(1, player);
                player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.flame_explosion_cooldown);
                //Mace Trinket
            } else if (type == 8 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                Vec3d playerLookVec = player.getLookVec();
                Vec3d setPos = new Vec3d(player.posX + playerLookVec.x * 5, player.posY + 0.2, player.posZ + playerLookVec.z * 5);
                EntitySummonedMace mace = new EntitySummonedMace(world, player, true, PotionTrinketConfig.mace_trinket_damage + ModUtils.addMageSetBonus(player, 0, 1.4F));
                mace.setPosition(setPos.x, setPos.y, setPos.z);
                world.spawnEntity(mace);
                stack.damageItem(1, player);
                player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.metal_tornado_cooldown);
                //rally trinket
            } else if (type == 9 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                Vec3d playerLookVec = player.getLookVec();
                Vec3d setPos = new Vec3d(player.posX + playerLookVec.x * 1.5, player.posY, player.posZ + playerLookVec.z * 1.5);
                int y = ModUtils.getSurfaceHeightZeroReturn(world, new BlockPos(setPos.x, 0, setPos.z), (int) player.posY - 2, (int) player.posY + 2);
                EntityRallyFlag flag = new EntityRallyFlag(world, player);
                if(y != 0) {
                    flag.setPosition(setPos.x, y + 1, setPos.z);
                } else {
                    flag.setPosition(setPos.x, player.posY, setPos.z);
                }
                world.spawnEntity(flag);
                stack.damageItem(1, player);
                player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.last_stand_cooldown);
                //Petal Trinket
            }  else if (type == 10 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                new ActionPlayerPetalWave().performAction(player);
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.BLOSSOM_PETAL_WAVE, SoundCategory.NEUTRAL, 1f, 0.8f / (world.rand.nextFloat() * 0.4F + 0.4f));
                stack.damageItem(1, player);
                player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.thorn_ring_cooldown);
                //lazer pistol
            } else if (type == 11 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.DAUNTLESS_CAST_PROJECTILE, SoundCategory.NEUTRAL, 0.8f, 0.6f / (world.rand.nextFloat() * 0.4F + 0.4f));
                Vec3d lookVec = player.getLookVec();
                float damage = PotionTrinketConfig.pocket_pistol_damage + ModUtils.addMageSetBonus(player, 0, 3F);
                Vec3d relPos = new Vec3d(player.posX + lookVec.x * 1.4D,player.posY + lookVec.y + player.getEyeHeight(), player.posZ + lookVec.z * 1.4D);
                Vec3d targetPos = new Vec3d(player.posX + lookVec.x * 16D,(player.posY + lookVec.y * 16D) + player.getEyeHeight(), player.posZ + lookVec.z * 16D);
                //We want to ensure the offset is in place. so target will remain null
                EntityDelayedLazer lazer = new EntityDelayedLazer(player.world, 15, targetPos, player, (float) (damage), null);
                lazer.setPosition(relPos.x, relPos.y, relPos.z);
                player.world.spawnEntity(lazer);
                player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.pocket_pistol_cooldown);
            } else if (type == 12 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.VOLACTILE_SHOOT_CANNON, SoundCategory.NEUTRAL, 0.7f, 1.4f / (world.rand.nextFloat() * 0.4F + 0.4f));
                new ActionCastPoisonMist().performAction(player);
                player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.pocket_poison_cooldown);
            } else if (type == 13 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.CAST_GENERIC_SPELL, SoundCategory.NEUTRAL, 0.7f, 1.4f / (world.rand.nextFloat() * 0.4F + 0.4f));
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 20 * PotionTrinketConfig.endure_pain_amount, 0, false, true));
                player.getCooldownTracker().setCooldown(stack.getItem(), 20 * PotionTrinketConfig.endure_pain_cooldown);
                //Great Heal Trinket
            } else if (type == 14 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
                world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.CAST_GENERIC_SPELL, SoundCategory.NEUTRAL, 0.7f, 1.4f / (world.rand.nextFloat() * 0.4F + 0.4f));
                ModUtils.circleCallback(5, 35, (pos)-> {
                    pos = new Vec3d(pos.x, 0, pos.y);
                    Vec3d posToo = player.getPositionVector().add(0, 1, 0);
                    Vec3d vel = pos.normalize().scale(0.15F).add(ModUtils.yVec(0));
                    Main.proxy.spawnParticle(30, world, posToo.x, posToo.y, posToo.z, vel.x, vel.y + 0.25, vel.z,16187155 );
                });
                List<EntityPlayer> nearbyPlayers = world.getEntitiesWithinAABB(EntityPlayer.class, player.getEntityBoundingBox().grow(4.5D), e -> !e.getIsInvulnerable());
                if(!nearbyPlayers.isEmpty()) {
                    for(EntityPlayer players : nearbyPlayers) {
                        if(!players.isCreative() && !players.isSpectator()) {
                            players.heal(PotionTrinketConfig.great_heal_heal_amount + ModUtils.addMageSetBonus(player, 0, 2F));
                        }
                    }
                }
                player.getCooldownTracker().setCooldown(stack.getItem(), 20 * 45);
            }
        }
        return false;
    }

    private void spawnBlueProjectile(EntityPlayer player, World world, ItemStack stack, Vec3d offset) {
        float mageBonus = ModUtils.addMageSetBonus(player, 0, 1.5F);
        float mageBonus_raw = ModUtils.addMageSetBonus(player, 0);
        Vec3d playerLookVec = player.getLookVec();
        Vec3d offsetPos = player.getPositionVector().add(ModUtils.getRelativeOffset(player, offset));
        Vec3d setPos = new Vec3d(offsetPos.x + playerLookVec.x * 1.4D, offsetPos.y + playerLookVec.y + player.getEyeHeight(), offsetPos.z + playerLookVec.z * 1.4);
        Vec3d scaledtPos = new Vec3d(offsetPos.x + playerLookVec.x * 10D,( offsetPos.y + playerLookVec.y * 10)+ player.getEyeHeight(), offsetPos.z + playerLookVec.z * 10);
        ProjectileMagicMissile missile = new ProjectileMagicMissile(world, player, PotionTrinketConfig.blue_trinket_damage + mageBonus, setPos, scaledtPos, (mageBonus_raw * 3));
        missile.setPosition(setPos.x, setPos.y, setPos.z);
        world.spawnEntity(missile);
        missile.setTravelRange(20);
    }
}
