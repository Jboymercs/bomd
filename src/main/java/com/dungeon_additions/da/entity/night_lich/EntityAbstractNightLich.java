package com.dungeon_additions.da.entity.night_lich;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityAbstractNightLich extends EntityAbstractBase {

    private static final DataParameter<Boolean> GREEN_ATTACK = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RED_ATTACK = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLUE_ATTACK = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PURPLE_ATTACK = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> YELLOW_ATTACK = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> SWING_ATTACK = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DOUBLE_SWING = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> COMBO_TRACK_PROJECTILES = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_AOE_ATTACK = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_SHOOT_PROJECTILES = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.BOOLEAN);


    public void setGreenAttack(boolean value) {this.dataManager.set(GREEN_ATTACK, Boolean.valueOf(value));}
    public void setRedAttack(boolean value) {this.dataManager.set(RED_ATTACK, Boolean.valueOf(value));}
    public void setBlueAttack(boolean value) {this.dataManager.set(BLUE_ATTACK, Boolean.valueOf(value));}
    public void setYellowAttack(boolean value) {this.dataManager.set(YELLOW_ATTACK, Boolean.valueOf(value));}
    public void setPurpleAttack(boolean value) {this.dataManager.set(PURPLE_ATTACK, Boolean.valueOf(value));}
    public void setSwingAttack(boolean value) {this.dataManager.set(SWING_ATTACK, Boolean.valueOf(value));}
    public void setDoubleSwing(boolean value) {this.dataManager.set(DOUBLE_SWING, Boolean.valueOf(value));}
    public void setComboTrackProjectiles(boolean value) {this.dataManager.set(COMBO_TRACK_PROJECTILES, Boolean.valueOf(value));}
    public void setComboAoeAttack(boolean value) {this.dataManager.set(COMBO_AOE_ATTACK, Boolean.valueOf(value));}
    public void setComboShootProjectiles(boolean value) {this.dataManager.set(COMBO_SHOOT_PROJECTILES, Boolean.valueOf(value));}

    public boolean isGreenAttack() {return this.dataManager.get(GREEN_ATTACK);}
    public boolean isRedAttack() {return this.dataManager.get(RED_ATTACK);}
    public boolean isBlueAttack() {return this.dataManager.get(BLUE_ATTACK);}
    public boolean isYellowAttack() {return this.dataManager.get(YELLOW_ATTACK);}
    public boolean isPurpleAttack() {return this.dataManager.get(PURPLE_ATTACK);}
    public boolean isSwingAttack() {return this.dataManager.get(SWING_ATTACK);}
    public boolean isDoubleSwing() {return this.dataManager.get(DOUBLE_SWING);}
    public boolean isComboTrackProjectiles() {return this.dataManager.get(COMBO_TRACK_PROJECTILES);}
    public boolean isComboShootProjectiles() {return this.dataManager.get(COMBO_SHOOT_PROJECTILES);}
    public boolean isComboAOEAttack() {return this.dataManager.get(COMBO_AOE_ATTACK);}

    public EntityAbstractNightLich(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.iAmBossMob = true;
    }

    public EntityAbstractNightLich(World worldIn) {
        super(worldIn);
        this.iAmBossMob = true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Green_Attack", this.isGreenAttack());
        nbt.setBoolean("Red_Attack", this.isRedAttack());
        nbt.setBoolean("Blue_Attack", this.isBlueAttack());
        nbt.setBoolean("Yellow_Attack", this.isYellowAttack());
        nbt.setBoolean("Purple_Attack", this.isPurpleAttack());
        nbt.setBoolean("Swing_Attack", this.isSwingAttack());
        nbt.setBoolean("Double_Swing", this.isDoubleSwing());
        nbt.setBoolean("Combo_Track", this.isComboTrackProjectiles());
        nbt.setBoolean("Combo_Shoot", this.isComboShootProjectiles());
        nbt.setBoolean("Combo_Aoe", this.isComboAOEAttack());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setGreenAttack(nbt.getBoolean("Green_Attack"));
        this.setRedAttack(nbt.getBoolean("Red_Attack"));
        this.setBlueAttack(nbt.getBoolean("Blue_Attack"));
        this.setYellowAttack(nbt.getBoolean("Yellow_Attack"));
        this.setPurpleAttack(nbt.getBoolean("Purple_Attack"));
        this.setSwingAttack(nbt.getBoolean("Swing_Attack"));
        this.setDoubleSwing(nbt.getBoolean("Double_Swing"));
        this.setComboTrackProjectiles(nbt.getBoolean("Combo_Track"));
        this.setComboShootProjectiles(nbt.getBoolean("Combo_Shoot"));
        this.setComboAoeAttack(nbt.getBoolean("Combo_Aoe"));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(GREEN_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(RED_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(BLUE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(YELLOW_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(PURPLE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SWING_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(DOUBLE_SWING, Boolean.valueOf(false));
        this.dataManager.register(COMBO_TRACK_PROJECTILES, Boolean.valueOf(false));
        this.dataManager.register(COMBO_SHOOT_PROJECTILES, Boolean.valueOf(false));
        this.dataManager.register(COMBO_AOE_ATTACK, Boolean.valueOf(false));
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10D);
    }
}
