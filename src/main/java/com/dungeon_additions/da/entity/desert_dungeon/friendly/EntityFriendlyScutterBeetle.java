package com.dungeon_additions.da.entity.desert_dungeon.friendly;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.desert_dungeon.EntityScutterBeetleAI;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.desert_dungeon.EntityDesertBase;
import com.dungeon_additions.da.entity.desert_dungeon.EntityScutterBeetle;
import com.dungeon_additions.da.entity.desert_dungeon.friendly.ai.EntityAIBeetleHurtByTarget;
import com.dungeon_additions.da.entity.desert_dungeon.friendly.ai.EntityAIBeetleOwnerAttack;
import com.dungeon_additions.da.entity.desert_dungeon.friendly.ai.EntityAIFriendBeetleFollow;
import com.dungeon_additions.da.entity.desert_dungeon.friendly.ai.EntityAIFriendlyBeetleAttack;
import com.dungeon_additions.da.entity.gaelon_dungeon.friendly.EntityFriendlyCursedRevenant;
import com.dungeon_additions.da.entity.gaelon_dungeon.friendly.ai.EntityAIRevenantFollow;
import com.dungeon_additions.da.entity.gaelon_dungeon.friendly.ai.EntityAIRevenantHurtByTarget;
import com.dungeon_additions.da.entity.gaelon_dungeon.friendly.ai.EntityAIRevenantOwnerAttack;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.base.Optional;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class EntityFriendlyScutterBeetle extends EntityDesertBase implements IAnimatable, IAttack, IAnimationTickable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;
    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK = "walk";

    private final String ANIM_BITE = "bite";
    private final String ANIM_BEGIN_SCUTTLE = "begin_scuttle";
    private final String ANIM_SCUTTLE = "scuttle";
    private final String ANIM_END_SCUTTLE = "end_scuttle";

    private boolean setHeadState;

    private static final DataParameter<Boolean> BITE = EntityDataManager.createKey(EntityFriendlyScutterBeetle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BEGIN_SCUTTLE = EntityDataManager.createKey(EntityFriendlyScutterBeetle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SCUTTLE = EntityDataManager.createKey(EntityFriendlyScutterBeetle.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_SCUTTLE = EntityDataManager.createKey(EntityFriendlyScutterBeetle.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntityFriendlyScutterBeetle.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public boolean isBite() {
        return this.dataManager.get(BITE);
    }
    private void setBite(boolean value) {
        this.dataManager.set(BITE, Boolean.valueOf(value));
    }
    public boolean isBeginScuttle() {
        return this.dataManager.get(BEGIN_SCUTTLE);
    }
    private void setBeginScuttle(boolean value) {
        this.dataManager.set(BEGIN_SCUTTLE, Boolean.valueOf(value));
    }
    public boolean isScuttle() {
        return this.dataManager.get(SCUTTLE);
    }
    private void setScuttle(boolean value) {
        this.dataManager.set(SCUTTLE, Boolean.valueOf(value));
    }
    public boolean isEndScuttle() {
        return this.dataManager.get(END_SCUTTLE);
    }
    private void setEndScuttle(boolean value) {
        this.dataManager.set(END_SCUTTLE, Boolean.valueOf(value));
    }

    @Nullable
    public UUID getOwnerId()
    {
        return (UUID)((Optional)this.dataManager.get(OWNER_UNIQUE_ID)).orNull();
    }

    public void setOwnerId(@Nullable UUID p_184754_1_)
    {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(p_184754_1_));
    }

    @Nullable
    public EntityLivingBase getOwner()
    {
        UUID uuid = this.getOwnerId();
        if(uuid == null) {
            return null;
        }
        else {
            this.isFriendlyCreature = true;
            return this.world.getPlayerEntityByUUID(uuid);
        }
    }

    public EntityFriendlyScutterBeetle(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.2F, 0.9F);
        this.experienceValue = 0;
        this.setHeadState = false;
        this.setScuttle(false);
        this.isFriendlyCreature = true;
    }

    public EntityFriendlyScutterBeetle(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 0.9F);
        this.experienceValue = 0;
        this.setHeadState =false;
        this.setScuttle(false);
        this.isFriendlyCreature = true;
    }

    public void onSummonViaPlayer(BlockPos pos, EntityPlayer owner) {
        BlockPos offset = pos.add(new BlockPos(0,0,0));
        this.setPosition(offset.getX(), offset.getY(), offset.getZ());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Bite", this.isBite());
        nbt.setBoolean("Begin_Scuttle", this.isBeginScuttle());
        nbt.setBoolean("Scuttle", this.isScuttle());
        nbt.setBoolean("End_Scuttle", this.isEndScuttle());
        if (this.getOwnerId() == null)
        {
            nbt.setString("OwnerUUID", "");
        }
        else
        {
            nbt.setString("OwnerUUID", this.getOwnerId().toString());
        }
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setBite(nbt.getBoolean("Bite"));
        this.setBeginScuttle(nbt.getBoolean("Begin_Scuttle"));
        this.setScuttle(nbt.getBoolean("Scuttle"));
        this.setEndScuttle(nbt.getBoolean("End_Scuttle"));
        String s;
        if (nbt.hasKey("OwnerUUID", 8))
        {
            s = nbt.getString("OwnerUUID");
        }
        else
        {
            String s1 = nbt.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(Objects.requireNonNull(this.getServer()), s1);
        }

        if (!s.isEmpty())
        {
            this.setOwnerId(UUID.fromString(s));
        }
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
        this.dataManager.register(BITE, Boolean.valueOf(false));
        this.dataManager.register(BEGIN_SCUTTLE, Boolean.valueOf(false));
        this.dataManager.register(SCUTTLE, Boolean.valueOf(false));
        this.dataManager.register(END_SCUTTLE, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.scutter_beetle_damage * 0.6D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.32D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.scutter_beetle_health * 0.7D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.scutter_beetle_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIFriendlyBeetleAttack<>(this, 1.0D, 20, 10, 0.2F));
        this.tasks.addTask(5, new EntityAIFriendBeetleFollow(this, 1.4D, 6, 20));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 9));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityMob>(this, EntityMob.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityDarkBase>(this, EntityDarkBase.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityRabbit>(this, EntityRabbit.class, 1, true, false, null));
        this.targetTasks.addTask(4, new EntityAIBeetleHurtByTarget(this));
        this.targetTasks.addTask(5, new EntityAIBeetleOwnerAttack(this));
    }

    private int scuttleTimer = 40;


    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.setHeadState && !world.isRemote) {
            EntityLivingBase target = this.getAttackTarget();
            if(target != null) {
                List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable());
                if(!nearbyEntities.isEmpty()) {
                    for (EntityLivingBase base : nearbyEntities) {
                        if (!(base instanceof EntityDesertBase)) {
                            if(base == target || base instanceof EntityPlayer) {
                                Vec3d offset = base.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.75, 0)));
                                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                                float damage =(float) (this.getAttack() * 1.5);
                                ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, offset, source, 1.4f, 0, false);
                                this.playSound(SoundsHandler.BEETLE_ATTACK, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
                                this.endScuttleAttack();
                            }
                        }
                    }
                }
            }

            scuttleTimer--;
            if(this.ticksExisted % 2 == 0) {
                Main.proxy.spawnParticle(2,world, this.posX, this.posY + 0.6, this.posZ, 0,0,0);
            }
            if(scuttleTimer < 0 || Math.abs(this.motionX) + Math.abs(this.motionZ) < 0.01f) {
                this.endScuttleAttack();
            }
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            if(distance <= 5) {
                prevAttacks = do_bite_attack;
            } else {
                prevAttacks = do_scuttle_attack;
            }

            prevAttacks.accept(target);
        }
        return distance <= 5 ? 20 : 70;
    }

    private void endScuttleAttack() {
        this.setScuttle(false);
        this.setEndScuttle(true);
        this.setHeadState = false;
        this.scuttleTimer = 40;
        this.lockLook =false;

        addEvent(()-> {
            this.setEndScuttle(false);
            this.setFightMode(false);
        }, 10);
    }

    private final Consumer<EntityLivingBase> do_scuttle_attack = (target) -> {
        this.setImmovable(true);
        this.setFullBodyUsage(true);
        this.setBeginScuttle(true);
        this.setFightMode(true);

        addEvent(()-> {
            this.playSound(SoundsHandler.BEETLE_WAR_CRY, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 25);
        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(3));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.31),0.1F);
                this.setHeadState = true;
                this.setFullBodyUsage(false);
                this.setBeginScuttle(false);
                this.setScuttle(true);
            }, 5);
        }, 30);

    };

    private final Consumer<EntityLivingBase> do_bite_attack = (target) -> {
        this.setFightMode(true);
        this.setBite(true);
        this.setImmovable(true);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(1));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.15),0.1F);
            }, 5);
        }, 15);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.1, 0.75, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (this.getAttack());
            ModUtils.handleAreaImpact(1.25f, (e) -> damage, this, offset, source, 0.1f, 0, false);
            this.playSound(SoundsHandler.BEETLE_ATTACK, 1.0f, 0.6f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 27);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 40);

        addEvent(()-> {
            this.setBite(false);
            this.setImmovable(false);
            this.setFightMode(false);
        }, 50);

    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "walk_controller", 0, this::predicateWalk));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttackStates));
        data.addAnimationController(new AnimationController(this, "head_controller", 0, this::predicateHoldHead));
    }

    private <E extends IAnimatable> PlayState predicateHoldHead(AnimationEvent<E> event) {
        if(this.isScuttle() && this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SCUTTLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateAttackStates(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isBite()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BITE, false));
                return PlayState.CONTINUE;
            }
            if(this.isBeginScuttle()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BEGIN_SCUTTLE, false));
                return PlayState.CONTINUE;
            }
            if(this.isEndScuttle()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_END_SCUTTLE, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateWalk(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
            event.getController().setAnimationSpeed(1.0 + (0.004 * event.getLimbSwing()));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getImmediateSource() == this || this.getOwner() != null && source.getImmediateSource() == this.getOwner()) {
            return false;
        }

        if(source.getImmediateSource() instanceof EntityAbstractBase) {
            EntityAbstractBase base = ((EntityAbstractBase) source.getImmediateSource());
            if(base.isFriendlyCreature) {
                this.setAttackTarget(null);
                return false;
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    protected boolean canDropLoot() {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.BEETLE_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.BEETLE_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.BEETLE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.BEETLE_WALK, 0.3F, 0.8f + ModRand.getFloat(0.3F));
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    private boolean hasSentMessage = false;

    @Override
    public void onDeath(DamageSource cause) {
        if(this.getOwner() != null && !hasSentMessage && !world.isRemote) {
            sendMessageToOwner();
            this.hasSentMessage = true;
        }
        super.onDeath(cause);
    }

    private void sendMessageToOwner() {
        Objects.requireNonNull(this.getOwner()).sendMessage(new TextComponentString(TextFormatting.WHITE + "Your Summoned Scutter Beetle has returned to there world..."));
    }
}
