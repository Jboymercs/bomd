package com.dungeon_additions.da.entity.gaelon_dungeon;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

public class EntityGaelonBase extends EntityAbstractBase {

    private boolean playerInCreative = false;

    protected boolean setImmediateChange = false;
    private static final DataParameter<Boolean> PLAYER_CHECKED = EntityDataManager.createKey(EntityGaelonBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DROPS_LOOT = EntityDataManager.createKey(EntityGaelonBase.class, DataSerializers.BOOLEAN);
    protected void setPlayerChecked(boolean value) {this.dataManager.set(PLAYER_CHECKED, Boolean.valueOf(value));}
    private boolean isPlayerChecked() {return this.dataManager.get(PLAYER_CHECKED);}
    public void setDropsLoot(boolean value) {this.dataManager.set(DROPS_LOOT, Boolean.valueOf(value));}
    protected boolean isDropsLoot() {return this.dataManager.get(DROPS_LOOT);}
    public EntityGaelonBase (World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityGaelonBase(World worldIn) {
        super(worldIn);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Player_Checked", this.isPlayerChecked());
        nbt.setBoolean("Drops_Loot", this.isDropsLoot());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setPlayerChecked(nbt.getBoolean("Player_Checked"));
        this.setDropsLoot(nbt.getBoolean("Drops_Loot"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
    this.dataManager.register(PLAYER_CHECKED, Boolean.valueOf(false));
    this.dataManager.register(DROPS_LOOT, Boolean.valueOf(true));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!world.isRemote) {
            if(!this.iAmBossMob && ticksExisted % 100 == 0 && !this.isPlayerChecked() || !this.iAmBossMob && !this.isPlayerChecked() && this.setImmediateChange) {
                List<EntityPlayer> nearbyMonsters = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(40D), e -> !e.getIsInvulnerable());
                int highestAchievement = 0;
                if(!nearbyMonsters.isEmpty()) {
                    for(EntityPlayer player : nearbyMonsters) {
                        if(!player.isCreative() && !player.isSpectator()) {

                            if(ModUtils.getAdvancementCompletionAsListOnePass(player, ModConfig.gaelon_boost_one) && highestAchievement == 0) {
                                highestAchievement = 1;
                            }
                            if(ModUtils.getAdvancementCompletionAsListOnePass(player, ModConfig.gaelon_boost_two) && highestAchievement != 1 && highestAchievement != 3) {
                                highestAchievement = 2;
                            }
                            if(ModUtils.getAdvancementCompletionAsListOnePass(player, ModConfig.gaelon_boost_three)) {
                                highestAchievement = 3;
                            }

                        }

                        if(player.isSpectator() || player.isCreative()) {
                            this.playerInCreative = true;
                        }
                    }

                    if(!playerInCreative) {
                        this.adjustStats(highestAchievement);
                    }
                }
            }
        }
    }


    /**
     * adjusts health and attack damage to new values with player progression
     * @param number
     */
    private void adjustStats(int number) {
        double current_health = this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
        double current_AD = this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
        double current_AT = this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getBaseValue();
        double current_AA = this.getEntityAttribute(SharedMonsterAttributes.ARMOR).getBaseValue();
        double additive_health = number * ModConfig.gaelon_mob_health_boost;
        double additive_attack_damage = number * ModConfig.gaelon_attack_damage_boost;
        double additive_armor_amount = number * 2;
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(current_AD + additive_attack_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(current_health + additive_health);
        this.setHealth((float) (current_health + additive_health));
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(current_AA + additive_armor_amount);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(current_AT + (double) number);
        this.setPlayerChecked(true);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getImmediateSource() instanceof EntityGaelonBase) {
            return false;
        }


        return super.attackEntityFrom(source, amount);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
