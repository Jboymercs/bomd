package com.dungeon_additions.da.entity.sky_dungeon.high_king;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.sky_dungeon.EntityImperialHalberdAI;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.sky_dungeon.EntityImperialHalberd;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.function.Consumer;

/**
 * Base Boss class, mostly to inherit common factors between both entities
 */
public class EntityHighKingBoss extends EntitySkyBase {

    private int changeWeatherTimer = 0;
    private static final DataParameter<Boolean> KING_CAST = EntityDataManager.createKey(EntityHighKingBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> KING_CAST_LIGHTNING = EntityDataManager.createKey(EntityHighKingBoss.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAD_PREVIOUS_TARGET = EntityDataManager.createKey(EntityHighKingBoss.class, DataSerializers.BOOLEAN);
    protected boolean isKingCast() {return this.dataManager.get(KING_CAST);}
    protected void setKingCast(boolean value) {this.dataManager.set(KING_CAST, Boolean.valueOf(value));}
    protected boolean isKingCastLightning() {return this.dataManager.get(KING_CAST_LIGHTNING);}
    protected void setKingCastLightning(boolean value) {this.dataManager.set(KING_CAST_LIGHTNING, Boolean.valueOf(value));}

    public boolean isHadPreviousTarget() {return this.dataManager.get(HAD_PREVIOUS_TARGET);}
    public void setHadPreviousTarget(boolean value) {this.dataManager.set(HAD_PREVIOUS_TARGET, Boolean.valueOf(value));}

    public Consumer<EntityLivingBase> prevAttacks;

    public EntityHighKingBoss(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.iAmBossMob = true;
        this.iAmBossMobWyrkNerf = true;
    }

    public EntityHighKingBoss(World worldIn) {
        super(worldIn);
        this.iAmBossMob = true;
        this.iAmBossMobWyrkNerf = true;
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("King_Cast", this.isKingCast());
        nbt.setBoolean("King_Cast_Lightning", this.isKingCastLightning());
        nbt.setBoolean("Had_Target", this.isHadPreviousTarget());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setKingCast(nbt.getBoolean("King_Cast"));
        this.setKingCastLightning(nbt.getBoolean("King_Cast_Lightning"));
        this.setHadPreviousTarget(nbt.getBoolean("Had_Target"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!world.isRemote) {
            changeWeatherTimer--;


            if(changeWeatherTimer < 0) {
                world.getWorldInfo().setThundering(true);
                world.getWorldInfo().setRaining(true);
                changeWeatherTimer = 1200;
            }


            EntityLivingBase target = this.getAttackTarget();


            if(this.getSpawnLocation() != null && this.isHasSpawn()) {
                if(target != null) {
                    if(target instanceof EntityPlayer) {
                        if(!this.isHadPreviousTarget()) {
                            this.setHadPreviousTarget(true);
                        }
                    }
                }
            }
        }
    }

    private static final ResourceLocation LOOT_RESET = new ResourceLocation(ModReference.MOD_ID, "sky_dungeon_reset");

    protected void resetBossTask() {
        this.setImmovable(true);
        BlockPos pos = this.getSpawnLocation();
        world.setBlockState(pos, ModBlocks.SKY_KEY_BLOCK.getDefaultState());
        world.setBlockState(pos.add(0, 1, 0), Blocks.CHEST.getDefaultState());
        TileEntity te = world.getTileEntity(pos.add(0, 1, 0));
        if(te instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) te;
            chest.setLootTable(LOOT_RESET, rand.nextLong());
        }
        this.experienceValue = 0;
        this.setDropItemsWhenDead(false);
        this.setDead();
    }


    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(KING_CAST_LIGHTNING, Boolean.valueOf(false));
        this.dataManager.register(KING_CAST, Boolean.valueOf(false));
        this.dataManager.register(HAD_PREVIOUS_TARGET, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(55D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.high_dragon_king_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(95);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }
}
