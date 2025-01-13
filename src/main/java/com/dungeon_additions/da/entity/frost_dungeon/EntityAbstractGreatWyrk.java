package com.dungeon_additions.da.entity.frost_dungeon;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.blossom.EntityAbstractVoidBlossom;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityAbstractGreatWyrk extends EntityFrostBase implements IEntityMultiPart, IPitch {

    private final MultiPartEntityPart[] hitboxParts;

    private final MultiPartEntityPart model = new MultiPartEntityPart(this, "model", 0.0f, 0.0f);

    private final MultiPartEntityPart corePart = new MultiPartEntityPart(this, "core", 2.5f, 1.5f);
    private final MultiPartEntityPart foward_left_leg = new MultiPartEntityPart(this, "left_leg", 1.0F, 3.0f);
    private final MultiPartEntityPart foward_right_leg = new MultiPartEntityPart(this, "left_leg", 1.0F, 3.0f);
    private final MultiPartEntityPart back_left_leg = new MultiPartEntityPart(this, "left_leg", 1.0F, 3.0f);
    private final MultiPartEntityPart back_right_leg = new MultiPartEntityPart(this, "left_leg", 1.0F, 3.0f);

    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityAbstractGreatWyrk.class, DataSerializers.FLOAT);

    private static final DataParameter<Boolean> MEGA_STOMP = EntityDataManager.createKey(EntityAbstractGreatWyrk.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ROLL = EntityDataManager.createKey(EntityAbstractGreatWyrk.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEE_STRIKE = EntityDataManager.createKey(EntityAbstractGreatWyrk.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHAKE = EntityDataManager.createKey(EntityAbstractGreatWyrk.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SMALL_STOMPS = EntityDataManager.createKey(EntityAbstractGreatWyrk.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DROP_ATTACK = EntityDataManager.createKey(EntityAbstractGreatWyrk.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LAZER_ATTACK = EntityDataManager.createKey(EntityAbstractGreatWyrk.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_AID = EntityDataManager.createKey(EntityAbstractGreatWyrk.class, DataSerializers.BOOLEAN);

    public void setMegaStomp(boolean value) {this.dataManager.set(MEGA_STOMP, Boolean.valueOf(value));}
    public void setRoll(boolean value) {this.dataManager.set(ROLL, Boolean.valueOf(value));}
    public void setMeleeStrike(boolean value) {this.dataManager.set(MELEE_STRIKE, Boolean.valueOf(value));}
    public void setShake(boolean value) {this.dataManager.set(SHAKE, Boolean.valueOf(value));}
    public void setSmallStomps(boolean value) {this.dataManager.set(SMALL_STOMPS, Boolean.valueOf(value));}
    public void setDropAttack(boolean value) {this.dataManager.set(DROP_ATTACK, Boolean.valueOf(value));}
    public void setLazerAttack(boolean value) {this.dataManager.set(LAZER_ATTACK, Boolean.valueOf(value));}
    public void setSummonAid(boolean value) {this.dataManager.set(SUMMON_AID, Boolean.valueOf(value));}

    public boolean isMegaStomp() {return this.dataManager.get(MEGA_STOMP);}
    public boolean isRoll() {return this.dataManager.get(ROLL);}
    public boolean isMeleeStrike() {return this.dataManager.get(MELEE_STRIKE);}
    public boolean isShake() {return this.dataManager.get(SHAKE);}
    public boolean isSmallStomps() {return this.dataManager.get(SMALL_STOMPS);}
    public boolean isDropAttack() {return this.dataManager.get(DROP_ATTACK);}
    public boolean isLazerAttack() {return this.dataManager.get(LAZER_ATTACK);}
    public boolean isSummonAid() {return this.dataManager.get(SUMMON_AID);}

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Mega_Stomp", this.isMegaStomp());
        nbt.setBoolean("Roll", this.isRoll());
        nbt.setBoolean("Melee_Strike", this.isMeleeStrike());
        nbt.setBoolean("Shake", this.isShake());
        nbt.setBoolean("Small_Stomps", this.isSmallStomps());
        nbt.setBoolean("Drop_Attack", this.isDropAttack());
        nbt.setBoolean("Lazer_Attack", this.isLazerAttack());
        nbt.setBoolean("Summon_Aid", this.isSummonAid());
        nbt.setFloat("Look", this.getPitch());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setMegaStomp(nbt.getBoolean("Mega_Stomp"));
        this.setRoll(nbt.getBoolean("Roll"));
        this.setMeleeStrike(nbt.getBoolean("Melee_Strike"));
        this.setShake(nbt.getBoolean("Shake"));
        this.setSmallStomps(nbt.getBoolean("Small_Stomps"));
        this.setDropAttack(nbt.getBoolean("Drop_Attack"));
        this.setLazerAttack(nbt.getBoolean("Lazer_Attack"));
        this.setSummonAid(nbt.getBoolean("Summon_Aid"));
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
        super.readEntityFromNBT(nbt);
    }
    public EntityAbstractGreatWyrk(World worldIn) {
        super(worldIn);
        this.hitboxParts = new MultiPartEntityPart[]{model, corePart, foward_left_leg, foward_right_leg, back_left_leg, back_right_leg};
        this.setSize(4.0F, 3.5F);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(MEGA_STOMP, Boolean.valueOf(false));
        this.dataManager.register(ROLL, Boolean.valueOf(false));
        this.dataManager.register(MELEE_STRIKE, Boolean.valueOf(false));
        this.dataManager.register(SHAKE, Boolean.valueOf(false));
        this.dataManager.register(SMALL_STOMPS, Boolean.valueOf(false));
        this.dataManager.register(DROP_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(LAZER_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_AID, Boolean.valueOf(false));
        super.entityInit();
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(45D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(350);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(18);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        Vec3d[] avec3d = new Vec3d[this.hitboxParts.length];
        for (int j = 0; j < this.hitboxParts.length; ++j) {
            avec3d[j] = new Vec3d(this.hitboxParts[j].posX, this.hitboxParts[j].posY, this.hitboxParts[j].posZ);
        }

        this.setHitBoxPos(corePart, new Vec3d(0, 1, 0));
        this.setHitBoxPos(foward_left_leg, new Vec3d(1.75, 0, 1.75));
        this.setHitBoxPos(foward_right_leg, new Vec3d(-1.75, 0, 1.75));
        this.setHitBoxPos(back_left_leg, new Vec3d(1.75, 0, -1.75));
        this.setHitBoxPos(back_right_leg, new Vec3d(-1.75, 0, -1.75));
        //HitBox Positions

        Vec3d knightPos = this.getPositionVector();
        ModUtils.setEntityPosition(model, knightPos);

        for (int l = 0; l < this.hitboxParts.length; ++l) {
            this.hitboxParts[l].prevPosX = avec3d[l].x;
            this.hitboxParts[l].prevPosY = avec3d[l].y;
            this.hitboxParts[l].prevPosZ = avec3d[l].z;
        }
    }

    private void setHitBoxPos(Entity entity, Vec3d offset) {
        Vec3d lookVel = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
        Vec3d center = this.getPositionVector().add(ModUtils.yVec(1.2));

        Vec3d position = center.subtract(ModUtils.Y_AXIS.add(ModUtils.getAxisOffset(lookVel, offset)));
        ModUtils.setEntityPosition(entity, position);

    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Entity[] getParts() {
        return this.hitboxParts;
    }


    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        this.dataManager.set(LOOK, clampedLook);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    //Damage Handling
    @Override
    public boolean attackEntityFromPart(@Nonnull MultiPartEntityPart part, @Nonnull DamageSource source, float damage) {
        if(part == corePart) {
            return this.attackEntityFrom(source, damage);
        } else {
            return false;
        }
        
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.isProjectile()) {
            return super.attackEntityFrom(source, amount * 0.75F);
        }
        return super.attackEntityFrom(source, amount);
    }
}
