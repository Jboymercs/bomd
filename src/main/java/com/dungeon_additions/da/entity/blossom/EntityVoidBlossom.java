package com.dungeon_additions.da.entity.blossom;

import com.dungeon_additions.da.blocks.BlockBase;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.EntityAIBlossom;
import com.dungeon_additions.da.entity.ai.EntityAiTimedAttack;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.blossom.action.*;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.model.ModelVoidBlossom;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.processor.IBone;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityVoidBlossom extends EntityAbstractVoidBlossom implements IAnimatable, IAttack, IAnimationTickable {

    //TODO
    //Finish Azeala's with Particle Effects
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.NOTCHED_6));


    private final String ANIM_IDLE_TARGET = "idle";
    private final String ANIM_IDLE_NONTARGET = ""; // Might need one more to hold on frame for this to stay in state
    private final String ANIM_SPIKE_SMALL = "spike";
    private final String ANIM_SPIKE_AOE = "spike_wave";
    private final String ANIM_LAUNCH_SPORE = "spore";
    private final String ANIM_LEAF_STRIKE = "leaf_blade";
    private final String ANIM_DEATH = "death";
    private final String ANIM_SUMMON = "spawn";

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;


    public EntityVoidBlossom(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.setNoGravity(true);
        this.iAmBossMob = true;
    }

    public EntityVoidBlossom(World worldIn, int timesUsed, BlockPos pos) {
        super(worldIn);
        this.setImmovable(true);
        this.setNoGravity(true);
        this.iAmBossMob = true;
        this.timesUsed = timesUsed;
        this.timesUsed++;
        this.doBossReSummonScaling();
    }

    public void onSummonBoss(BlockPos offset) {
        this.setSpawnLocation(offset);
        this.setSetSpawnLoc(true);
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "blossom_idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "blossom_attack", 0, this::predicateAttack));
        animationData.addAnimationController(new AnimationController(this, "blossom_summon_death", 0, this::predicateDeath));
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIBlossom<>(this, 0, 100, 24F, 0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    private<E extends IAnimatable>PlayState predicateDeath(AnimationEvent<E> event) {
        if(this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DEATH, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isDeathState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_TARGET, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(!this.isDeathState()) {
            if (this.isSpikeAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SPIKE_SMALL, false));
                return PlayState.CONTINUE;
            }
            if (this.isSpikeWave()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SPIKE_AOE, false));
                return PlayState.CONTINUE;
            }
            if (this.isLeafAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LEAF_STRIKE, false));
                return PlayState.CONTINUE;
            }
            if (this.isSporeAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LAUNCH_SPORE, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }


    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.BLOSSOM_HURT ;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        this.motionY = 0;
        this.motionX = 0;
        this.motionZ = 0;
        this.bossInfo.setPercent(getHealth() / getMaxHealth());

    }



    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        int cooldown_degradation = MobConfig.blossom_degradation_cooldown * playersNearbyAmount;
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(spikeAttack, spikeWave, leafAttack, sporeBomb, summonMinion));
            double[] weights = {
                    (distance < 25 && prevAttacks != spikeAttack) ? distance * 0.02 : 0, // Spike Attack Simple
                    (distance < 25 && prevAttacks != spikeWave) ? distance * 0.02 : 0, //Spike Wave
                    (distance < 25 && prevAttacks != leafAttack && HealthChange < 0.5) ? distance * 0.02 : 0, // Leaf Attack only active at 50% below Health
                    (distance < 25 && prevAttacks != sporeBomb && HealthChange < 0.75) ? distance * 0.02 : 0, //Spore Bomb Attack active only at 75% below Health
                    (distance < 25 && prevAttacks != summonMinion && HealthChange < 0.5 && minionCooldown < 0 && MobConfig.enable_minions && this.current_mobs.size() <= MobConfig.blossom_max_minion_count) ? distance * 0.02 : 0 //Minion Spawning
                                //maybe a minion summon attack
            };
            prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
            prevAttacks.accept(target);
        }
        return (HealthChange < 0.5) ? (MobConfig.blossom_cooldown_one * 20) - cooldown_degradation : (MobConfig.blossom_cooldown_two * 20) - cooldown_degradation;
    }

    //Summon Minion
    private Consumer<EntityLivingBase> summonMinion = (target) -> {
        this.setFightMode(true);
        addEvent(()-> {
            playSound(SoundsHandler.BLOSSOM_SUMMON_MINION, 1.0f, 1.0f);
            Vec3d pos = this.getPositionVector().add((rand.nextInt(2) == 0) ?ModRand.range(3, 7) : ModRand.range(-7, -3),0,
                    (rand.nextInt(2) ==0) ? ModRand.range(3, 7) : ModRand.range(-7, -3));
            EntityMiniBlossom blossomMinion = new EntityMiniBlossom(this.world);
            blossomMinion.setPosition(pos.x, pos.y, pos.z);
            world.spawnEntity(blossomMinion);
            this.current_mobs.add(new WeakReference<>(blossomMinion));
            minionCooldown = 400;
        }, 20);
        addEvent(()-> this.setFightMode(false), 40);
    };

    //Spike Attack Simple
    private Consumer<EntityLivingBase> spikeAttack = (target)-> {
        this.setFightMode(true);
        this.setSpikeAttack(true);

            addEvent(()-> {
                if(!world.isRemote) {
                    addEvent(()-> {
                        Vec3d targetOldPos = target.getPositionVector();
                        addEvent(()-> {
                            Vec3d targetedPos = target.getPositionVector();
                            Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 4);
                            this.spawnSpikeAction(predictedPosition);
                        }, 3);
                    }, 1);

                    addEvent(()-> {
                        Vec3d targetOldPos = target.getPositionVector();
                        addEvent(()-> {
                            Vec3d targetedPos = target.getPositionVector();
                            Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 4);
                            this.spawnSpikeAction(predictedPosition);
                        }, 3);
                    }, 37);

                    addEvent(()-> {
                        Vec3d targetOldPos = target.getPositionVector();
                        addEvent(()-> {
                            Vec3d targetedPos = target.getPositionVector();
                            Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 4);
                            this.spawnSpikeAction(predictedPosition);
                        }, 3);
                    }, 77);
                }
            }, 21);
        addEvent(()-> {
            this.setFightMode(false);
            this.setSpikeAttack(false);
        }, 120);
    };

    /**
     * Add a bit of brightness to the entity for it's Arena
     */
    @Override
    public int getBrightnessForRender() {
        return Math.min(super.getBrightnessForRender() + 30, 200);
    }

    //Spike Wave Initiator
    private Consumer<EntityLivingBase> spikeWave = (target)-> {
        this.setFightMode(true);
        this.setSpikeWave(true);
        addEvent(()-> playSound(SoundsHandler.BLOSSOM_BURROW, 2.0f, 1.0f), 25);
        addEvent(()-> new ActionShortRangeWave().performAction(this, target), 30);
        addEvent(()-> new ActionMediumRangeWave().performAction(this, target), 60);
        addEvent(()-> new ActionLongRangeWave().performAction(this, target), 90);
        addEvent(()-> {
            this.setFightMode(false);
            this.setSpikeWave(false);
        }, 104);
    };


    Supplier<Projectile> leafProjectileSupplier = () -> new ProjectileVoidLeaf(this.world, this, (float) (this.getAttack() * 0.5));
    //Leaf Attack
    private Consumer<EntityLivingBase> leafAttack = (target) -> {
        this.setFightMode(true);
        this.setLeafAttack(true);

        addEvent(()-> new ActionLeafAttack(leafProjectileSupplier, 0.8f).performAction(this, target), 28);
        addEvent(()-> new ActionLeafAttack(leafProjectileSupplier, 0.8f).performAction(this, target), 50);
        addEvent(()-> new ActionLeafAttack(leafProjectileSupplier, 0.8f).performAction(this, target), 74);
        addEvent(()-> {
            this.setFightMode(false);
            this.setLeafAttack(false);
        }, 106);
    };
    //Spore bomb
    private Consumer<EntityLivingBase> sporeBomb = (target) -> {
        this.setFightMode(true);
        this.setSporeAttack(true);
        addEvent(()-> playSound(SoundsHandler.SPORE_PREPARE, 1.0f, 1.0f), 30);
        addEvent(()-> {
            if(target.isEntityAlive()) {
                ProjectileSporeBomb projectile = new ProjectileSporeBomb(this.world);
                Vec3d pos = this.getPositionVector().add(ModUtils.yVec(12.0D));
                Vec3d targetPos = target.getPositionVector().add(ModUtils.yVec(14));
                Vec3d velocity = targetPos.subtract(pos).normalize().scale(0.7);
                projectile.setPosition(pos.x, pos.y, pos.z);
                ModUtils.setEntityVelocity(projectile, new Vec3d(velocity.x, velocity.y - 0.5D, velocity.z));
                projectile.setTravelRange(40F);
                world.spawnEntity(projectile);
            }
        }, 46);

        addEvent(()-> {
            this.setFightMode(false);
            this.setSporeAttack(false);
        }, 60);
    };

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

    public void spawnSpikeAction(Vec3d predictedPos) {
        EntityLivingBase target = this.getAttackTarget();
        //1
        if(target != null) {
            EntityVoidSpike spike = new EntityVoidSpike(this.world);
            BlockPos area = new BlockPos(predictedPos.x, predictedPos.y, predictedPos.z);
            int y = getSurfaceHeight(this.world, new BlockPos(target.posX, 0, target.posZ), (int) this.posY - 5, (int) this.posY + 7);
            spike.setPosition(area.getX(), y + 1, area.getZ());
            world.spawnEntity(spike);
            //2
            EntityVoidSpike spike2 = new EntityVoidSpike(this.world);
            BlockPos area2 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z);
            int y2 = getSurfaceHeight(this.world, new BlockPos(area2.getX(), 0, area2.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike2.setPosition(area2.getX(), y2 + 1, area2.getZ());
            world.spawnEntity(spike2);
            //3
            EntityVoidSpike spike3 = new EntityVoidSpike(this.world);
            BlockPos area3 = new BlockPos(predictedPos.x - 1, predictedPos.y, predictedPos.z);
            int y3 = getSurfaceHeight(this.world, new BlockPos(area3.getX(), 0, area3.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike3.setPosition(area3.getX(), y3 + 1, area3.getZ());
            world.spawnEntity(spike3);
            //4
            EntityVoidSpike spike4 = new EntityVoidSpike(this.world);
            BlockPos area4 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 1);
            int y4 = getSurfaceHeight(this.world, new BlockPos(area4.getX(), 0, area4.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike4.setPosition(area4.getX(),y4 + 1, area4.getZ());
            world.spawnEntity(spike4);
            //5
            EntityVoidSpike spike5 = new EntityVoidSpike(this.world);
            BlockPos area5 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z - 1);
            int y5 = getSurfaceHeight(this.world, new BlockPos(area5.getX(), 0, area5.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike5.setPosition(area5.getX(), y5 + 1, area5.getZ());
            world.spawnEntity(spike5);
            //6
            EntityVoidSpike spike6 = new EntityVoidSpike(this.world);
            BlockPos area6 = new BlockPos(predictedPos.x + 1, predictedPos.y, predictedPos.z + 1);
            int y6 = getSurfaceHeight(this.world, new BlockPos(area6.getX(), 0, area6.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike6.setPosition(area6.getX(), y6 + 1, area6.getZ());
            world.spawnEntity(spike6);
            //7
            EntityVoidSpike spike7 = new EntityVoidSpike(this.world);
            BlockPos area7 = new BlockPos(predictedPos.x +1 , predictedPos.y, predictedPos.z - 1);
            int y7 = getSurfaceHeight(this.world, new BlockPos(area7.getX(), 0, area7.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike7.setPosition(area7.getX(), y7 + 1, area7.getZ());
            world.spawnEntity(spike7);
            //8
            EntityVoidSpike spike8 = new EntityVoidSpike(this.world);
            BlockPos area8 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z - 1);
            int y8 = getSurfaceHeight(this.world, new BlockPos(area8.getX(), 0, area8.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike8.setPosition(area8.getX(), y8 + 1, area8.getZ());
            world.spawnEntity(spike8);
            //9
            EntityVoidSpike spike9 = new EntityVoidSpike(this.world);
            BlockPos area9 = new BlockPos(predictedPos.x -1, predictedPos.y, predictedPos.z + 1);
            int y9 = getSurfaceHeight(this.world, new BlockPos(area9.getX(), 0, area9.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike9.setPosition(area9.getX(), y9 + 1, area9.getZ());
            world.spawnEntity(spike9);
            //10
            EntityVoidSpike spike10 = new EntityVoidSpike(this.world);
            BlockPos area10 = new BlockPos(predictedPos.x +2, predictedPos.y, predictedPos.z );
            int y10 = getSurfaceHeight(this.world, new BlockPos(area10.getX(), 0, area10.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike10.setPosition(area10.getX(), y10 + 1, area10.getZ());
            world.spawnEntity(spike10);
            //11
            EntityVoidSpike spike11 = new EntityVoidSpike(this.world);
            BlockPos area11 = new BlockPos(predictedPos.x - 2, predictedPos.y, predictedPos.z );
            int y11 = getSurfaceHeight(this.world, new BlockPos(area11.getX(), 0, area11.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike11.setPosition(area11.getX(), y11 + 1, area11.getZ());
            world.spawnEntity(spike11);
            //12
            EntityVoidSpike spike12 = new EntityVoidSpike(this.world);
            BlockPos area12 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z + 2);
            int y12 = getSurfaceHeight(this.world, new BlockPos(area12.getX(), 0, area12.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike12.setPosition(area12.getX(), y12 + 1, area12.getZ());
            world.spawnEntity(spike12);
            //13
            EntityVoidSpike spike13 = new EntityVoidSpike(this.world);
            BlockPos area13 = new BlockPos(predictedPos.x , predictedPos.y, predictedPos.z -2);
            int y13 = getSurfaceHeight(this.world, new BlockPos(area13.getX(), 0, area13.getZ()), (int) this.posY - 5, (int) this.posY + 7);
            spike13.setPosition(area13.getX(), y13 + 1, area13.getZ());
            world.spawnEntity(spike13);
        }
    }

    @Override
    public void setCustomNameTag(@Nonnull String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void addTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        super.readEntityFromNBT(nbt);
    }

    private static final ResourceLocation LOOT_BOSS = new ResourceLocation(ModReference.MOD_ID, "void_blossom");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_BOSS;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.0001f);
        this.setDeathState(true);
        addEvent(()-> {
            for(int i = 0; i <= 70; i+=2) {
                if(!world.isRemote) {
                    addEvent(() -> {
                        EntityXPOrb orb = new EntityXPOrb(world, this.posX, this.posY, this.posZ, 5);
                        orb.setPosition(this.posX, this.posY + 1, this.posZ);
                        world.spawnEntity(orb);
                    }, i);
                    List<EntityMiniBlossom> nearbyEntities = this.world.getEntitiesWithinAABB(EntityMiniBlossom.class, this.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable());
                    if (!nearbyEntities.isEmpty()) {
                        for (EntityMiniBlossom smol_blossom : nearbyEntities) {
                            smol_blossom.setDead();
                        }
                    }
                }
            }
        }, 1);
        addEvent(()-> this.setDeathState(false), 71);
        addEvent(()-> this.setDropItemsWhenDead(true), 65);
        addEvent(()-> this.playSound(SoundsHandler.BLOSSOM_DEATH, 2.0f, 1.0f), 50);
        addEvent(()-> {
            if(this.getSpawnLocation() != null) {
                this.turnBossIntoSummonSpawner(this.getSpawnLocation());
            }
            this.setDead();
            }, 71);
        super.onDeath(cause);
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
