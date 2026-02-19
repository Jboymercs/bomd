package com.dungeon_additions.da.items.shield;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.entity.void_dungeon.EntityBlueWave;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.proxy.ClientProxy;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemObsidianShield extends BOMDShieldItem implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);
    public int hitCounter = 0;
    private String info_loc;
    public ItemObsidianShield(String name, String info_loc, CreativeTabs tabs) {
        super(name);
        setCreativeTab(tabs);
        this.info_loc = info_loc;
        this.maxStackSize = 1;
        this.setMaxDamage(1032);
        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.getSubCompound("BlockEntityTag") != null)
        {
            EnumDyeColor enumdyecolor = TileEntityBanner.getColor(stack);
            return I18n.translateToLocal("da:item.obsidian_shield." + enumdyecolor.getTranslationKey() + ".name");
        }
        else
        {
            return I18n.translateToLocal("da.desc.obsidian_shield_name");
        }
    }

    private boolean initiatedAttack;
    private Vec3d targetOriginalPos;
    private int waitTime = 4;
    private Entity entityFound = null;

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if (entityFound != null) {

                //Do Quick AOE
                float damage = (ModConfig.obsidilith_shield_damage + ModUtils.addShieldBonusDamage(player.getHeldItemOffhand(), 1.25F)) * ModUtils.addDarkArmorBonusMultiplier(player, 1);

                    if(entityFound instanceof EntityLivingBase) {
                        player.world.playSound(player.posX + 0.5D, player.posY, player.posZ + 0.5D, SoundsHandler.OBSIDILITH_CAST, SoundCategory.BLOCKS,(float) 1.0F, 1.0F, false);
                        Vec3d particleStart = player.getPositionEyes(1.0F);
                        Vec3d particleEnd = entityFound.getPositionVector().add(0, 1.5, 0);
                        ModUtils.lineCallback(particleStart, particleEnd, (int) player.getDistance(entityIn), (pos, i) -> {
                            Main.proxy.spawnParticle(7,worldIn, pos.x, pos.y, pos.z, 0,0,0);
                        });
                        if(!initiatedAttack) {
                            //old position
                            targetOriginalPos = particleEnd;
                            initiatedAttack = true;
                        }
                        if (!worldIn.isRemote) {
                            if(waitTime < 0) {
                                //send attack and reset everything
                                Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOriginalPos, entityFound.getPositionVector(), 3);
                                this.summonSpikesOnEnemy(player, damage, ((EntityLivingBase) entityFound), worldIn, predictedPosition);
                                player.resetActiveHand();
                                player.disableShield(true);
                                player.getCooldownTracker().setCooldown(stack.getItem(), ModConfig.obsidilith_shield_ability * 20);
                                this.waitTime = 4;
                                this.initiatedAttack = false;
                                targetOriginalPos = null;
                                entityFound = null;
                                this.hitCounter = 0;
                            } else {
                                waitTime--;
                            }
                        }
                    }


            }
        }
    }

    @Override
    public boolean onApplyButtonPressed(EntityPlayer player, World world, ItemStack stack){
        if (stack == player.getActiveItemStack() && hitCounter > 4) {
            if(player.canBePushed()) {
                player.motionX = 0;
                player.motionY = 0;
                player.motionZ = 0;
            }
            Entity entityToo = findClosestEntity(player, world);
            if(entityToo != null) {
                this.entityFound = entityToo;
                //then goes to onUpdate
                return true;
            }
        }
        return false;
    }


    private Entity findClosestEntity(EntityPlayer player, World world) {
        Vec3d lazerEnd = player.getPositionEyes(1).add(player.getLookVec().scale(16));
        RayTraceResult raytraceresult = world.rayTraceBlocks(player.getPositionEyes(1), lazerEnd, false, true, false);
        if (raytraceresult != null) {
            // If we hit a block, make sure that any collisions with entities are detected up to the hit block
            lazerEnd = raytraceresult.hitVec;
        }

        Entity closestEntity = null;
        for (Entity entity : ModUtils.findEntitiesInLine(player.getPositionEyes(1), lazerEnd, world, player)) {
            if (entity.canBeCollidedWith() && (closestEntity == null || entity.getDistanceSq(player) < closestEntity.getDistanceSq(player))) {
                closestEntity = entity;
            }
        }

        if (closestEntity != null) {
            if(closestEntity instanceof EntityLivingBase) {
                if (closestEntity instanceof IEntityMultiPart) {
                    if (closestEntity.getParts() != null) {
                        MultiPartEntityPart closestPart = null;
                        for (Entity entity : closestEntity.getParts()) {
                            RayTraceResult result = entity.getEntityBoundingBox().calculateIntercept(player.getPositionEyes(1), lazerEnd);
                            if (result != null) {
                                if (entity instanceof MultiPartEntityPart && (closestPart == null || entity.getDistanceSq(player) < closestPart.getDistanceSq(player))) {
                                    closestPart = (MultiPartEntityPart) entity;
                                }
                            }
                        }
                        if (closestPart != null) {
                            lazerEnd = closestPart.getEntityBoundingBox().calculateIntercept(player.getPositionEyes(1), lazerEnd).hitVec;
                            return closestEntity;
                        }
                    }
                } else {
                    lazerEnd = closestEntity.getEntityBoundingBox().calculateIntercept(player.getPositionEyes(1), lazerEnd).hitVec;
                    return closestEntity;
                }
            }
        }
        return null;
    }

    private void summonSpikesOnEnemy(EntityPlayer player, float damage, EntityLivingBase target, World world, Vec3d predictedPos) {
        if(target != null) {
            EntityBlueWave spike = new EntityBlueWave(world, player, damage);
            BlockPos area = new BlockPos(predictedPos.x, predictedPos.y, predictedPos.z);
            int y = getSurfaceHeight(world, new BlockPos(target.posX, 0, target.posZ), (int) target.posY - 3, (int) target.posY + 3);
            spike.setPosition(area.getX(), y + 1, area.getZ());
            world.spawnEntity(spike);
            spike.playSound(SoundsHandler.APPEARING_WAVE, 0.75f, 1.0f / new Random().nextFloat() * 0.04F + 1.2F);;
            //2
            EntityBlueWave spike2 = new EntityBlueWave(world, player, damage);
            BlockPos area2 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z);
            int y2 = getSurfaceHeight(world, new BlockPos(area2.getX(), 0, area2.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike2.setPosition(area2.getX(), y2 + 1, area2.getZ());
            world.spawnEntity(spike2);
            //3
            EntityBlueWave spike3 = new EntityBlueWave(world, player, damage);
            BlockPos area3 = new BlockPos(predictedPos.x - 1, predictedPos.y, predictedPos.z);
            int y3 = getSurfaceHeight(world, new BlockPos(area3.getX(), 0, area3.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike3.setPosition(area3.getX(), y3 + 1, area3.getZ());
            world.spawnEntity(spike3);
            //4
            EntityBlueWave spike4 = new EntityBlueWave(world, player, damage);
            BlockPos area4 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 1);
            int y4 = getSurfaceHeight(world, new BlockPos(area4.getX(), 0, area4.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike4.setPosition(area4.getX(),y4 + 1, area4.getZ());
            world.spawnEntity(spike4);
            //5
            EntityBlueWave spike5 = new EntityBlueWave(world, player, damage);
            BlockPos area5 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z - 1);
            int y5 = getSurfaceHeight(world, new BlockPos(area5.getX(), 0, area5.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike5.setPosition(area5.getX(), y5 + 1, area5.getZ());
            world.spawnEntity(spike5);
            //6
            EntityBlueWave spike6 = new EntityBlueWave(world, player, damage);
            BlockPos area6 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z + 1);
            int y6 = getSurfaceHeight(world, new BlockPos(area6.getX(), 0, area6.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike6.setPosition(area6.getX(), y6 + 1, area6.getZ());
            world.spawnEntity(spike6);
            //7
            EntityBlueWave spike7 = new EntityBlueWave(world, player, damage);
            BlockPos area7 = new BlockPos(predictedPos.x +1 , predictedPos.y, predictedPos.z - 1);
            int y7 = getSurfaceHeight(world, new BlockPos(area7.getX(), 0, area7.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike7.setPosition(area7.getX(), y7 + 1, area7.getZ());
            world.spawnEntity(spike7);
            //8
            EntityBlueWave spike8 = new EntityBlueWave(world, player, damage);
            BlockPos area8 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z - 1);
            int y8 = getSurfaceHeight(world, new BlockPos(area8.getX(), 0, area8.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike8.setPosition(area8.getX(), y8 + 1, area8.getZ());
            world.spawnEntity(spike8);
            //9
            EntityBlueWave spike9 = new EntityBlueWave(world, player, damage);
            BlockPos area9 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z + 1);
            int y9 = getSurfaceHeight(world, new BlockPos(area9.getX(), 0, area9.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike9.setPosition(area9.getX(), y9 + 1, area9.getZ());
            world.spawnEntity(spike9);
            //10
            EntityBlueWave spike10 = new EntityBlueWave(world, player, damage);
            BlockPos area10 = new BlockPos(predictedPos.x +2, predictedPos.y, predictedPos.z );
            int y10 = getSurfaceHeight(world, new BlockPos(area10.getX(), 0, area10.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike10.setPosition(area10.getX(), y10 + 1, area10.getZ());
            world.spawnEntity(spike10);
            //11
            EntityBlueWave spike11 = new EntityBlueWave(world, player, damage);
            BlockPos area11 = new BlockPos(predictedPos.x - 2, predictedPos.y, predictedPos.z );
            int y11 = getSurfaceHeight(world, new BlockPos(area11.getX(), 0, area11.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike11.setPosition(area11.getX(), y11 + 1, area11.getZ());
            world.spawnEntity(spike11);
            //12
            EntityBlueWave spike12 = new EntityBlueWave(world, player, damage);
            BlockPos area12 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 2);
            int y12 = getSurfaceHeight(world, new BlockPos(area12.getX(), 0, area12.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike12.setPosition(area12.getX(), y12 + 1, area12.getZ());
            world.spawnEntity(spike12);
            //13
            EntityBlueWave spike13 = new EntityBlueWave(world, player, damage);
            BlockPos area13 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z -2);
            int y13 = getSurfaceHeight(world, new BlockPos(area13.getX(), 0, area13.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike13.setPosition(area13.getX(), y13 + 1, area13.getZ());
            world.spawnEntity(spike13);
        }
    }

    public int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock()) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }


    @Override
    public void onBlockingDamage(ItemStack shield, EntityPlayer player) {
        hitCounter++;
        super.onBlockingDamage(shield, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
        tooltip.add(TextFormatting.GOLD + I18n.translateToLocal("description.dungeon_additions.shield_pre.name") + TextFormatting.AQUA + I18n.translateToLocal(ClientProxy.SHIELD_ABILITY.getDisplayName()));
        ItemBanner.appendHoverTextFromTileEntityTag(stack, tooltip);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
