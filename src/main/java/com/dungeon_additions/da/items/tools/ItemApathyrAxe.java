package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyrSpear;
import com.dungeon_additions.da.entity.player.ActionApathyrWave;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ItemApathyrAxe extends ItemSword implements IAnimatable, IHasModel {
    public AnimationFactory factory = new AnimationFactory(this);

    private String info_loc;

    public ItemApathyrAxe(String name, Item.ToolMaterial material, String info_loc) {
        super(material);
        this.setMaxDamage(986);
        setTranslationKey(name);
        setRegistryName(name);
        ModItems.ITEMS.add(this);
        setCreativeTab(DungeonAdditionsTab.ALL);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
    }

    private EntityLivingBase targetedEntity;
    private int waitTime;
    private Vec3d targetOriginalPosition;

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            if(player.isSneaking()) {
                float damage;
                if(!worldIn.isDaytime()) {
                    damage = ModConfig.midnight_reign_ability_damage + 5;
                } else {
                    damage = ModConfig.midnight_reign_ability_damage + 1;
                }
                ModUtils.performNTimes(20, (i) -> Main.proxy.spawnParticle(17,worldIn, player.posX + ModRand.range(-2, 2), player.posY + ModRand.range(1, 3), player.posZ + ModRand.range(-2, 2), 0, 0.08, 0, 20));
                worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.APATHYR_CAST_HEAVY, SoundCategory.NEUTRAL, 1.5f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.4f));
                //do crystal wave
                new ActionApathyrWave(damage + ModUtils.addAbilityBonusDamage(stack, 0.75F)).performAction(player);
                player.getCooldownTracker().setCooldown(stack.getItem(), (ModConfig.midnight_reign_cooldown * 2) * 20);
                stack.damageItem(4, player);
            } else {
                Entity entityToo = findClosestEntity(player, worldIn);
                if(entityToo != null) {
                    if(entityToo instanceof EntityLivingBase) {
                        player.setActiveHand(hand);
                        EntityLivingBase entityIn = ((EntityLivingBase) entityToo);
                        targetedEntity = entityIn;
                        targetOriginalPosition = entityIn.getPositionVector();
                        stack.damageItem(2, player);
                        ModUtils.performNTimes(10, (i) -> Main.proxy.spawnParticle(15, worldIn,player.posX + ModRand.range(-2, 2), player.posY + ModRand.range(1, 3), player.posZ + ModRand.range(-2, 2), 0, 0.08, 0, 20));
                        worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.APATHYR_CAST_SPIKES, SoundCategory.NEUTRAL, 1.5f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.4f));
                    }
                }
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if (targetedEntity != null) {
                if(!worldIn.isRemote) {
                    if(waitTime < 0) {
                        Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOriginalPosition, targetedEntity.getPositionVector(), 3);
                        float damage;
                        if(!worldIn.isDaytime()) {
                            damage = ModConfig.midnight_reign_ability_damage + 4;
                        } else {
                            damage = ModConfig.midnight_reign_ability_damage;
                        }
                        this.spawnSpikeAction(player, damage + ModUtils.addAbilityBonusDamage(player.getHeldItemMainhand(), 0.75F), targetedEntity, predictedPosition, worldIn);
                        player.resetActiveHand();
                        this.waitTime = 4;
                        this.targetOriginalPosition = null;
                        player.getCooldownTracker().setCooldown(stack.getItem(), ModConfig.midnight_reign_cooldown * 20);
                        this.targetedEntity = null;
                    } else {
                        waitTime--;
                    }
                }
            }
        }
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

    public void spawnSpikeAction(EntityPlayer player, float damage, EntityLivingBase target, Vec3d predictedPos, World world) {
        //1
        if(target != null) {
            EntityApathyrSpear spike = new EntityApathyrSpear(world, player, damage);
            BlockPos area = new BlockPos(predictedPos.x, predictedPos.y, predictedPos.z);
            int y = getSurfaceHeight(world, new BlockPos(target.posX, 0, target.posZ), (int) target.posY - 3, (int) target.posY + 3);
            spike.setPosition(area.getX(), y + 1, area.getZ());
            world.spawnEntity(spike);
            //2
            EntityApathyrSpear spike2 = new EntityApathyrSpear(world, player, damage);
            BlockPos area2 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z);
            int y2 = getSurfaceHeight(world, new BlockPos(area2.getX(), 0, area2.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike2.setPosition(area2.getX(), y2 + 1, area2.getZ());
            world.spawnEntity(spike2);
            //3
            EntityApathyrSpear spike3 = new EntityApathyrSpear(world, player, damage);
            BlockPos area3 = new BlockPos(predictedPos.x - 1, predictedPos.y, predictedPos.z);
            int y3 = getSurfaceHeight(world, new BlockPos(area3.getX(), 0, area3.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike3.setPosition(area3.getX(), y3 + 1, area3.getZ());
            world.spawnEntity(spike3);
            //4
            EntityApathyrSpear spike4 = new EntityApathyrSpear(world, player, damage);
            BlockPos area4 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 1);
            int y4 = getSurfaceHeight(world, new BlockPos(area4.getX(), 0, area4.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike4.setPosition(area4.getX(),y4 + 1, area4.getZ());
            world.spawnEntity(spike4);
            //5
            EntityApathyrSpear spike5 = new EntityApathyrSpear(world, player, damage);
            BlockPos area5 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z - 1);
            int y5 = getSurfaceHeight(world, new BlockPos(area5.getX(), 0, area5.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike5.setPosition(area5.getX(), y5 + 1, area5.getZ());
            world.spawnEntity(spike5);
            //6
            EntityApathyrSpear spike6 = new EntityApathyrSpear(world, player, damage);
            BlockPos area6 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z + 1);
            int y6 = getSurfaceHeight(world, new BlockPos(area6.getX(), 0, area6.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike6.setPosition(area6.getX(), y6 + 1, area6.getZ());
            world.spawnEntity(spike6);
            //7
            EntityApathyrSpear spike7 = new EntityApathyrSpear(world, player, damage);
            BlockPos area7 = new BlockPos(predictedPos.x +1 , predictedPos.y, predictedPos.z - 1);
            int y7 = getSurfaceHeight(world, new BlockPos(area7.getX(), 0, area7.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike7.setPosition(area7.getX(), y7 + 1, area7.getZ());
            world.spawnEntity(spike7);
            //8
            EntityApathyrSpear spike8 = new EntityApathyrSpear(world, player, damage);
            BlockPos area8 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z - 1);
            int y8 = getSurfaceHeight(world, new BlockPos(area8.getX(), 0, area8.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike8.setPosition(area8.getX(), y8 + 1, area8.getZ());
            world.spawnEntity(spike8);
            //9
            EntityApathyrSpear spike9 = new EntityApathyrSpear(world, player, damage);
            BlockPos area9 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z + 1);
            int y9 = getSurfaceHeight(world, new BlockPos(area9.getX(), 0, area9.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike9.setPosition(area9.getX(), y9 + 1, area9.getZ());
            world.spawnEntity(spike9);
            //10
            EntityApathyrSpear spike10 = new EntityApathyrSpear(world, player, damage);
            BlockPos area10 = new BlockPos(predictedPos.x +2, predictedPos.y, predictedPos.z );
            int y10 = getSurfaceHeight(world, new BlockPos(area10.getX(), 0, area10.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike10.setPosition(area10.getX(), y10 + 1, area10.getZ());
            world.spawnEntity(spike10);
            //11
            EntityApathyrSpear spike11 = new EntityApathyrSpear(world, player, damage);
            BlockPos area11 = new BlockPos(predictedPos.x - 2, predictedPos.y, predictedPos.z );
            int y11 = getSurfaceHeight(world, new BlockPos(area11.getX(), 0, area11.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike11.setPosition(area11.getX(), y11 + 1, area11.getZ());
            world.spawnEntity(spike11);
            //12
            EntityApathyrSpear spike12 = new EntityApathyrSpear(world, player, damage);
            BlockPos area12 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 2);
            int y12 = getSurfaceHeight(world, new BlockPos(area12.getX(), 0, area12.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike12.setPosition(area12.getX(), y12 + 1, area12.getZ());
            world.spawnEntity(spike12);
            //13
            EntityApathyrSpear spike13 = new EntityApathyrSpear(world, player, damage);
            BlockPos area13 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z -2);
            int y13 = getSurfaceHeight(world, new BlockPos(area13.getX(), 0, area13.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike13.setPosition(area13.getX(), y13 + 1, area13.getZ());
            world.spawnEntity(spike13);
            //14
            EntityApathyrSpear spike14 = new EntityApathyrSpear(world, player, damage);
            BlockPos area14 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z -3);
            int y14 = getSurfaceHeight(world, new BlockPos(area14.getX(), 0, area14.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike14.setPosition(area14.getX(), y14 + 1, area14.getZ());
            world.spawnEntity(spike14);
            //15
            EntityApathyrSpear spike15 = new EntityApathyrSpear(world, player, damage);
            BlockPos area15 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 3);
            int y15 = getSurfaceHeight(world, new BlockPos(area15.getX(), 0, area15.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike15.setPosition(area15.getX(), y15 + 1, area15.getZ());
            world.spawnEntity(spike15);
            //16
            EntityApathyrSpear spike16 = new EntityApathyrSpear(world, player, damage);
            BlockPos area16 = new BlockPos(predictedPos.x - 3, predictedPos.y, predictedPos.z);
            int y16 = getSurfaceHeight(world, new BlockPos(area16.getX(), 0, area16.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike16.setPosition(area16.getX(), y16 + 1, area16.getZ());
            world.spawnEntity(spike16);
            //17
            EntityApathyrSpear spike17 = new EntityApathyrSpear(world, player, damage);
            BlockPos area17 = new BlockPos(predictedPos.x + 3, predictedPos.y, predictedPos.z);
            int y17 = getSurfaceHeight(world, new BlockPos(area17.getX(), 0, area17.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike17.setPosition(area17.getX(), y17 + 1, area17.getZ());
            world.spawnEntity(spike17);
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public void registerModels() {
        {
            Main.proxy.registerItemRenderer(this, 0, "inventory");
        }}

    @Override
    public void registerControllers(AnimationData data) {

    }



    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit
     * damage.
     */
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.getAttackDamage(), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", getAttackSpeed(), 0));
        }

        return multimap;
    }

    @Override
    public float getAttackDamage() {
        return super.getAttackDamage();
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected double getAttackSpeed() {
        return -3D;
    }
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player)
    { return false; }



    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker)
    {
        return true;
    }

}
