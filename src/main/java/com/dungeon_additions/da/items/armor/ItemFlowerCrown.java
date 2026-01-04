package com.dungeon_additions.da.items.armor;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.player.ActionFireRuneAttack;
import com.dungeon_additions.da.entity.void_dungeon.EntityBlueWave;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ItemFlowerCrown extends ItemArmor implements IHasModel {
    private String texture;
    private String info_loc;
    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("a3578781-e4a8-4d70-9d32-cd952aeae1df"),
            UUID.fromString("e2d1f056-f539-48c7-b353-30d7a367ebd0"), UUID.fromString("db13047a-bb47-4621-a025-65ed22ce461a"),
            UUID.fromString("abb5df20-361d-420a-8ec7-4bdba33378eb")};

    private String armorBonusDesc = "";

    public ItemFlowerCrown(String name, ArmorMaterial materialIn, int renderIdx, EntityEquipmentSlot slotIn, String textureName, String info_loc) {
        super(materialIn, renderIdx, slotIn);
        setCreativeTab(DungeonAdditionsTab.ALL);
        this.info_loc = info_loc;
        setTranslationKey(name);
        setRegistryName(name);
        this.texture = textureName;
        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (equipmentSlot == this.armorType) {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", this.damageReduceAmount * ModConfig.armor_scaling, 0));
            // Override armor toughness to make is adjustable in game
            //Come back to Re-use when needed IF needed
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", 1F * ModConfig.armor_toughness_scaling, 0));

        }


        return multimap;
    }

    private int hitCounter = 0;
    private boolean setAttack = false;
    private Vec3d targetOriginalPos;
    private int waitTime = 4;
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer) {

            EntityPlayer player = (EntityPlayer) entityIn;
            if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == this) {
            if (player.hurtTime == 1 && !player.getCooldownTracker().hasCooldown(this)) {
                hitCounter++;
            }

            if (this.hitCounter > ModConfig.void_blossom_crown_hit_counter) {
                if (player.getAttackingEntity() != null) {
                    player.world.playSound(player.posX + 0.5D, player.posY, player.posZ + 0.5D, SoundsHandler.OBSIDILITH_WAVE_DING, SoundCategory.BLOCKS, (float) 1.0F, 1.0F, false);
                    EntityLivingBase attacker = player.getAttackingEntity();
                    targetOriginalPos = attacker.getPositionVector();
                    this.setAttack = true;
                    ItemStack itemstack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                    itemstack.damageItem(1, player);
                    this.hitCounter = 0;
                } else {
                    if (worldIn.rand.nextInt(8) == 0) {
                        Main.proxy.spawnParticle(1, worldIn, player.posX + ModRand.range(-2, 2), player.posY + 0.25 + ModRand.getFloat(2F), player.posZ + ModRand.range(-2, 2), 0, 0, 0);
                    }
                }

            }

            if (this.setAttack && !worldIn.isRemote) {
                //launch Fire Rune Attack
                if (player.getAttackingEntity() != null) {
                    if (waitTime < 0) {
                        EntityLivingBase attacker = player.getAttackingEntity();
                        Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOriginalPos, attacker.getPositionVector(), 3);
                        this.summonSpikesOnEnemy(player, ModConfig.void_blossom_crown_damage, attacker, worldIn, predictedPosition);
                        //summon Void THorns on
                        targetOriginalPos = null;
                        this.waitTime = 4;
                        player.getCooldownTracker().setCooldown(stack.getItem(), ModConfig.void_blossom_crown_cooldown * 20);
                        this.setAttack = false;
                    } else {
                        waitTime--;
                    }
                }
            }
            }
        }
    }

    private void summonSpikesOnEnemy(EntityPlayer player, float damage, EntityLivingBase target, World world, Vec3d predictedPos) {
        if(target != null) {
            EntityVoidSpike spike = new EntityVoidSpike(world, player, damage);
            BlockPos area = new BlockPos(predictedPos.x, predictedPos.y, predictedPos.z);
            int y = getSurfaceHeight(world, new BlockPos(predictedPos.x, 0, predictedPos.z), (int) target.posY - 3, (int) target.posY + 3);
            spike.setPosition(area.getX(), y + 1, area.getZ());
            world.spawnEntity(spike);
            spike.playSound(SoundsHandler.APPEARING_WAVE, 0.75f, 1.0f / new Random().nextFloat() * 0.04F + 1.2F);;
            //2
            EntityVoidSpike spike2 = new EntityVoidSpike(world, player, damage);
            BlockPos area2 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z);
            int y2 = getSurfaceHeight(world, new BlockPos(area2.getX(), 0, area2.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike2.setPosition(area2.getX(), y2 + 1, area2.getZ());
            world.spawnEntity(spike2);
            //3
            EntityVoidSpike spike3 = new EntityVoidSpike(world, player, damage);
            BlockPos area3 = new BlockPos(predictedPos.x - 1, predictedPos.y, predictedPos.z);
            int y3 = getSurfaceHeight(world, new BlockPos(area3.getX(), 0, area3.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike3.setPosition(area3.getX(), y3 + 1, area3.getZ());
            world.spawnEntity(spike3);
            //4
            EntityVoidSpike spike4 = new EntityVoidSpike(world, player, damage);
            BlockPos area4 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 1);
            int y4 = getSurfaceHeight(world, new BlockPos(area4.getX(), 0, area4.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike4.setPosition(area4.getX(),y4 + 1, area4.getZ());
            world.spawnEntity(spike4);
            //5
            EntityVoidSpike spike5 = new EntityVoidSpike(world, player, damage);
            BlockPos area5 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z - 1);
            int y5 = getSurfaceHeight(world, new BlockPos(area5.getX(), 0, area5.getZ()), (int) target.posY - 3, (int) target.posY + 3);
            spike5.setPosition(area5.getX(), y5 + 1, area5.getZ());
            world.spawnEntity(spike5);
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


    public ItemFlowerCrown setArmorBonusDesc(String armorBonusDesc) {
        this.armorBonusDesc = armorBonusDesc;
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        if (!this.armorBonusDesc.isEmpty()) {
            tooltip.add(ModUtils.translateDesc(this.armorBonusDesc));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped defaultModel) {
        return (ModelBiped) Main.proxy.getArmorModel(this, entityLiving);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return ModReference.MOD_ID + ":textures/models/armor/flower_crown.png";
    }
}
