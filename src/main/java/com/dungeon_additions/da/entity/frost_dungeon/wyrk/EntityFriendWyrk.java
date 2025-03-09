package com.dungeon_additions.da.entity.frost_dungeon.wyrk;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.EntityWyrkAttackAI;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.friendly_wyrk.EntityAIWyrkFollow;
import com.dungeon_additions.da.entity.frost_dungeon.friendly_wyrk.EntityAIWyrkHurtByTarget;
import com.dungeon_additions.da.entity.frost_dungeon.friendly_wyrk.EntityWyrkOwnerAttack;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.base.Optional;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityWolf;
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
import net.minecraft.util.text.TextComponentTranslation;
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

public class EntityFriendWyrk extends EntityAbstractBase implements IAnimatable, IAnimationTickable, IAttack {

    private final String ANIM_IDLE = "idle";

    private final String ANIM_WALK = "walk";

    private final String ANIM_ATTACK = "stomp";
    private final AnimationFactory factory = new AnimationFactory(this);

    private boolean doSummonParticles = false;
    private Consumer<EntityLivingBase> prevAttack;
    private static final DataParameter<Boolean> STOMP = EntityDataManager.createKey(EntityFriendWyrk.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> SUMMON_COUNT = EntityDataManager.createKey(EntityFriendWyrk.class, DataSerializers.VARINT);

    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntityFriendWyrk.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private boolean isStomp() {return this.dataManager.get(STOMP);}

    private void setStomp(boolean value) {this.dataManager.set(STOMP, Boolean.valueOf(value));}

    public int getSummonCount() {return this.dataManager.get(SUMMON_COUNT);}

    public void setSummonCount(int value) {this.dataManager.set(SUMMON_COUNT, value);}


    public EntityFriendWyrk(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.8F, 1.95F);
        this.experienceValue = 2;
        this.isFriendlyCreature = true;

    }

    public EntityFriendWyrk(World worldIn) {
        super(worldIn);
        this.setSize(0.8F, 1.95F);
        this.experienceValue = 2;
        this.isFriendlyCreature = true;
    }

    public EntityFriendWyrk(World world, EntityPlayer player) {
        super(world);
        this.setSize(0.8F, 1.95F);
        this.experienceValue = 2;
    }

    public void onSummonViaPlayer(BlockPos pos, EntityPlayer owner) {
        BlockPos offset = pos.add(new BlockPos(0,0,0));
        this.setPosition(offset.getX(), offset.getY(), offset.getZ());
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


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Summon_Count", this.getSummonCount());
        nbt.setBoolean("Stomp", this.isStomp());
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
        this.setSummonCount(nbt.getInteger("Summon_Count"));
        this.setStomp(nbt.getBoolean("Stomp"));
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
        this.dataManager.register(SUMMON_COUNT, MobConfig.wyrk_starter_souls);
        this.dataManager.register(STOMP, Boolean.valueOf(false));
    }

    public int summonDraugrTimer = 200;

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!world.isRemote) {
        if (this.getOwner() != null) {

                if(this.getSummonCount() > 5) {
                    this.setSummonCount(this.getSummonCount() - 1);
                }
                double healthFac = this.getHealth() / this.getMaxHealth();
                //instead have the Wyrk heal itself upon gaining souls, allowing for more depth with the friendly summons
                if(healthFac <= 0.95 && this.getSummonCount() > 0) {
                    if(summonDraugrTimer > 1) {
                        summonDraugrTimer--;
                    }
                    if (summonDraugrTimer < 4) {
                        this.heal(MobConfig.wyrk_heal_amount);
                        this.setSummonCount(this.getSummonCount() - 1);
                        this.summonDraugrTimer = 150;
                        world.setEntityState(this, ModUtils.FOURTH_PARTICLE_BYTE);
                        this.playSound(SoundsHandler.LICH_PREPARE_SPELL, 0.7f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                    }
                } else {
                    List<EntityPlayer> nearbyplayer = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(8.0D), e -> !e.getIsInvulnerable());

                    if(summonDraugrTimer > 1) {
                        summonDraugrTimer--;
                    }
                    if(!nearbyplayer.isEmpty() && this.getSummonCount() > 0) {
                        for(EntityPlayer player : nearbyplayer) {
                            double playerHealth = player.getHealth() / player.getMaxHealth();

                            if(playerHealth < 1 && summonDraugrTimer < 4) {
                                player.heal(MobConfig.wyrk_heal_amount * 0.5F);
                                world.setEntityState(this, ModUtils.FOURTH_PARTICLE_BYTE);
                                this.setSummonCount(this.getSummonCount() - 1);
                                this.summonDraugrTimer = 400;
                                this.playSound(SoundsHandler.LICH_PREPARE_SPELL, 1.2f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityWyrkAttackAI<>(this, 1.1, 120, 16, 0.5F));
        this.tasks.addTask(5, new EntityAIWyrkFollow(this, 1.4D, 4, 16));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 9));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityMob>(this, EntityMob.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIWyrkHurtByTarget(this));
        this.targetTasks.addTask(4, new EntityWyrkOwnerAttack(this));

    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.wyrk_health);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.wyrk_attack_damage * 0.9);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.32);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            if(distance >= 6 || prevAttack == stomp_attack) {
                //do ranged attack
                prevAttack = ranged_attack;
            } else {
                //do stomp attack
                prevAttack = stomp_attack;
            }
            prevAttack.accept(target);
        }
        return  80;
    }

    private final Consumer<EntityLivingBase> ranged_attack = (target) -> {
        this.setFightMode(true);
        this.doSummonParticles =  true;
        addEvent(()-> {
            this.doSummonParticles = false;
            world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            new ActionShootProjectileWyrk().performAction(this, target);
        }, 40);

        addEvent(()-> this.playSound(SoundsHandler.WYRK_CAST, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 50);

        addEvent(()-> this.setFightMode(false), 110);
    };

    private final Consumer<EntityLivingBase> stomp_attack = (target) -> {
        this.setFightMode(true);
        this.setStomp(true);
        this.setImmovable(true);
        addEvent(()-> {
            //do shockwave AOE
            this.playSound(SoundsHandler.DRAUGR_ELITE_STOMP, 0.75f, 1.3f / (rand.nextFloat() * 0.4F + 0.4f));
            new ActionSummonAOEWyrk(6).performAction(this, target);
        }, 40);

        addEvent(()-> {
            this.setImmovable(false);
            this.setFightMode(false);
            this.setStomp(false);

        }, 60);
    };

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(this.doSummonParticles && rand.nextInt(2) == 0) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 2.4, 0))), ModColors.WHITE, new Vec3d(ModRand.getFloat(1) * 0.02,ModRand.getFloat(1) * 0.02,ModRand.getFloat(1) * 0.02));
        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(1, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(2.4)), ModColors.WHITE, pos.normalize().scale(0.1), ModRand.range(10, 15));
            });
        }

        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            ModUtils.circleCallback(1, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(2.4)), ModColors.AZURE, pos.normalize().scale(0.1), ModRand.range(10, 15));
            });
        }

        if(id == ModUtils.FOURTH_PARTICLE_BYTE) {
            ModUtils.circleCallback(1, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(2.4)), ModColors.PINK, pos.normalize().scale(0.1), ModRand.range(10, 15));
            });
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isStomp()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    /**
     * Add a bit of brightness to the entity, because otherwise it looks pretty black
     */
    @Override
    public int getBrightnessForRender() {
        return Math.min(super.getBrightnessForRender() + 20, 150);
    }


    @Override
    public void tick() {

    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player)
    {
        return true;
    }



    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.WYRK_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.WYRK_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.WYRK_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundsHandler.WYRK_STEP, 0.25F, 0.4f + ModRand.getFloat(0.3F));
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getImmediateSource() == this || this.getOwner() != null && source.getImmediateSource() == this.getOwner()) {
            return false;
        }


        return super.attackEntityFrom(source, amount);
    }

    private boolean hasSentMessage = false;

    @Override
    public void onDeath(DamageSource cause) {
        if(this.getOwner() != null && !hasSentMessage) {
            sendMessageToOwner();
            this.hasSentMessage = true;
        }
        super.onDeath(cause);
    }

    private void sendMessageToOwner() {
            Objects.requireNonNull(this.getOwner()).sendMessage(new TextComponentString(TextFormatting.WHITE + "Your Wyrk has passed away..."));
    }

}
