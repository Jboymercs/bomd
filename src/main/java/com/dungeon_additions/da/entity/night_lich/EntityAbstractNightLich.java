package com.dungeon_additions.da.entity.night_lich;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.util.ModUtils;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.stream.Collectors;

public class EntityAbstractNightLich extends EntityAbstractBase implements IPitch {

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
    private static final DataParameter<Boolean> THROW_STAFF = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.BOOLEAN);

    public List<WeakReference<Entity>> current_mobs = Lists.newArrayList();

    private int mob_count = MobConfig.lich_active_mob_count;
    private static final DataParameter<Boolean> ANGERED_STATE = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityAbstractNightLich.class, DataSerializers.FLOAT);
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
    public void setAngeredState(boolean value) {this.dataManager.set(ANGERED_STATE, Boolean.valueOf(value));}
    public void setThrowStaff(boolean value) {this.dataManager.set(THROW_STAFF, Boolean.valueOf(value));}

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
    public boolean isAngeredState() {return this.dataManager.get(ANGERED_STATE);}
    public boolean isThrowStaff() {return this.dataManager.get(THROW_STAFF);}

    public EntityAbstractNightLich(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.iAmBossMob = true;
    }

    public EntityAbstractNightLich(World worldIn) {
        super(worldIn);
        this.iAmBossMob = true;
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        //updates the mobs currently summoned by the lich
            this.clearInvalidEntities();



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
        nbt.setBoolean("Angry_State", this.isAngeredState());
        nbt.setBoolean("Throw_Staff", this.isThrowStaff());
        nbt.setFloat("Look", this.getPitch());
        NBTTagList mobs = new NBTTagList();
        for (WeakReference<Entity> ref : current_mobs) {
            if (ref.get() == null) continue;
            mobs.appendTag(NBTUtil.createUUIDTag(ref.get().getUniqueID()));
        }
        nbt.setTag("current_mobs", mobs);
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
        this.setAngeredState(nbt.getBoolean("Angry_State"));
        this.setThrowStaff(nbt.getBoolean("Throw_Staff"));
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(LOOK, 0f);
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
        this.dataManager.register(ANGERED_STATE, Boolean.valueOf(false));
        this.dataManager.register(THROW_STAFF, Boolean.valueOf(false));
    }


    /**
     * This ensures that active mobs are still within a distance and are still alive to be accounted for
     */
    private void clearInvalidEntities() {
        current_mobs = current_mobs.stream().filter(ref -> ref.get() != null && ref.get().getDistance(this) <= MobConfig.lich_active_mob_distance && ref.get().isEntityAlive()).collect(Collectors.toList());
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.20590D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.night_lich_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.night_lich_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.night_lich_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10D);
    }

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        //we want our boy to be stable while he's slinging melee attack or in a angered State
        if(this.isAngeredState() || this.isRedAttack() || this.isGreenAttack() || this.isComboAOEAttack()) {
            this.dataManager.set(LOOK, 0F);
        } else {
            this.dataManager.set(LOOK, clampedLook);
        }

    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }
}
