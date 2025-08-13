package com.dungeon_additions.da.entity.sky_dungeon.high_king;

import com.dungeon_additions.da.blocks.faurm.BlockLightningKeyBlock;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.ai.flying.FlyingMoveHelper;
import com.dungeon_additions.da.entity.ai.flying.TimedAttackInitiator;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyTornado;
import com.dungeon_additions.da.entity.sky_dungeon.ProjectileLightRing;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.action.*;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.drake.EntityHighKingDrakeAI;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.ProjectileStormWind;
import com.dungeon_additions.da.entity.util.IEntitySound;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.*;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class EntityHighKingDrake extends EntityHighKingBoss implements IAnimatable, IAnimationTickable, IAttack, IPitch, IEntityMultiPart, IScreenShake, IEntitySound {

    private final String ANIM_FLY_BASE = "fly";
    private final String ANIM_FLY_DRAGON = "fly_dragon";
    private final String ANIM_FLY_KING = "fly_king";

    private final String ANIM_GROUND_IDLE = "idle";

    private final String ANIM_GROUND_WALK = "walk";

    //Flying King Attacks
    private final String ANIM_KING_CAST_MAGIC = "king_cast_magic";
    private final String ANIM_KING_CAST_LIGHTNING = "king_summon_lightning";

    //Flying Dragon Attacks
    private final String ANIM_DRAGON_ROAR = "dragon_roar";
    private final String ANIM_DRAGON_STORM = "storm_breath";

    //Transistion to Ground Attack
    private final String ANIM_DRAGON_GROUND_TRANSITION = "teleport_smash";
    //Transition To Air Attack
    private final String ANIM_DRAGON_AIR_TRANSITION = "end_ground_attack";

    //Ground Animations

    //Bite Has 3 Altercations
    private final String ANIM_BITE_BEGIN = "bite";
    private final String ANIM_BITE_CAST = "bite_cast";
    private final String ANIM_BITE_TAIL_SWIPE = "bite_tail_swipe";
    private final String ANIM_BITE_END = "bite_end";

    private final String ANIM_STOMP = "stomp";

    //Storm Breath 2 Altercations
    private final String ANIM_BREATH_BEGIN = "ground_storm_breath";
    private final String ANIM_BREATH_END = "ground_storm_breath_end";
    private final String ANIM_BREATH_ATTACK = "ground_storm_breath_attack";

    private final String ANIM_GROUND_ROAR = "roar";




    private int kingAttackCooldown = 300;

    private int timeUntilTransition = 1400;

    private boolean switchAttacks = false;

    //HitBox Parts Begin
    private final MultiPartEntityPart[] hitboxParts;
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.NOTCHED_10));

    private final MultiPartEntityPart model = new MultiPartEntityPart(this, "model", 0.0f, 0.0f);

    public final MultiPartEntityPart torso = new MultiPartEntityPart(this, "torso", 3F, 2.5F);
    public final MultiPartEntityPart neckPart1 = new MultiPartEntityPart(this, "neck_part_one", 1.0F, 1.0F);
    public final MultiPartEntityPart neckPart2 = new MultiPartEntityPart(this, "neck_part_two", 0.8F, 0.8F);

    public final MultiPartEntityPart headPart = new MultiPartEntityPart(this, "head_part", 1.5F, 1.25F);

    public final MultiPartEntityPart tailPart1 = new MultiPartEntityPart(this, "tail_part_one", 1.1F, 1.2F);
    public final MultiPartEntityPart legPartLeft = new MultiPartEntityPart(this, "leg_left", 1.2F, 1.7F);
    public final MultiPartEntityPart legPartRight = new MultiPartEntityPart(this, "right_leg", 1.2F, 1.7F);

    public final MultiPartEntityPart tailPart2 = new MultiPartEntityPart(this, "tail_part_two", 1.25F, 1.0F);

    public final MultiPartEntityPart tailPart3 = new MultiPartEntityPart(this, "tail_part_3", 1.2F,  0.9F);

    public final MultiPartEntityPart tailPart4 = new MultiPartEntityPart(this, "tail_part_4", 1.0F, 0.8F);

    public final MultiPartEntityPart tailPart5 = new MultiPartEntityPart(this, "tail_part_5", 1.1F, 0.6F);

    //This Determines when the King is separately in Fight Mode from the Dragon, usually only used for when it's flying
    //Fight mode itself is only used for the dragon, and in combination when on the ground
    private static final DataParameter<Boolean> KING_FIGHT_MODE = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);
    //This is for when the Dragon is not flying and is staying on the ground, used for the idle animations regarding that
    private static final DataParameter<Boolean> GROUNDED = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);

    //Flying Dragon Attacks
    private static final DataParameter<Boolean> DRAGON_ROAR = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DRAGON_STORM = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);

    //Ground Transition Attack
    private static final DataParameter<Boolean> GROUND_TRANSITION_ATTACK = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> AIR_TRANSITION_ATTACK = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);

    //Ground Attacks
    private static final DataParameter<Boolean> BITE_BEGIN = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BITE_TAIL_SWING = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BITE_CAST = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BITE_END = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GROUND_ROAR = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> GROUND_STOMP = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BREATH_BEGIN = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BREATH_END = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BREATH_ATTACK = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.BOOLEAN);

    //Pitch
    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityHighKingDrake.class, DataSerializers.FLOAT);
    private final AnimationFactory factory = new AnimationFactory(this);

    private Consumer<EntityLivingBase> prevAttacks;
    private boolean isKingFightMode() {return this.dataManager.get(KING_FIGHT_MODE);}
    private void setKingFightMode(boolean value) {this.dataManager.set(KING_FIGHT_MODE, Boolean.valueOf(value));}

    public boolean isGrounded() {return this.dataManager.get(GROUNDED);}
    public void setGrounded(boolean value) {this.dataManager.set(GROUNDED, Boolean.valueOf(value));}
    private boolean isDragonRoar() {return this.dataManager.get(DRAGON_ROAR);}
    private void setDragonRoar(boolean value) {this.dataManager.set(DRAGON_ROAR, Boolean.valueOf(value));}
    private boolean isDragonStorm() {return this.dataManager.get(DRAGON_STORM);}
    private void setDragonStorm(boolean value) {this.dataManager.set(DRAGON_STORM, Boolean.valueOf(value));}
    public boolean isGroundTransitionAttack() {return this.dataManager.get(GROUND_TRANSITION_ATTACK);}
    private void setGroundTransitionAttack(Boolean value) {this.dataManager.set(GROUND_TRANSITION_ATTACK, Boolean.valueOf(value));}

    private boolean isBiteBegin() {return this.dataManager.get(BITE_BEGIN);}
    private void setBiteBegin(Boolean value) {this.dataManager.set(BITE_BEGIN, Boolean.valueOf(value));}
    private boolean isBiteCast() {return this.dataManager.get(BITE_CAST);}
    private void setBiteCast(Boolean value) {this.dataManager.set(BITE_CAST, Boolean.valueOf(value));}
    private boolean isBiteTailSwipe() {return this.dataManager.get(BITE_TAIL_SWING);}
    private void setBiteTailSwipe(boolean value) {this.dataManager.set(BITE_TAIL_SWING, Boolean.valueOf(value));}
    private boolean isBiteEnd() {return this.dataManager.get(BITE_END);}
    private void setBiteEnd(boolean value) {this.dataManager.set(BITE_END, Boolean.valueOf(value));}
    private boolean isGroundStomp() {return this.dataManager.get(GROUND_STOMP);}
    private void setGroundStomp(boolean value) {this.dataManager.set(GROUND_STOMP, Boolean.valueOf(value));}
    private boolean isGroundRoar() {return this.dataManager.get(GROUND_ROAR);}
    private void setGroundRoar(boolean value) {this.dataManager.set(GROUND_ROAR, Boolean.valueOf(value));}
    private boolean isBreathBegin() {return this.dataManager.get(BREATH_BEGIN);}
    private void setBreathBegin(boolean value) {this.dataManager.set(BREATH_BEGIN, Boolean.valueOf(value));}
    private boolean isBreathEnd() {return this.dataManager.get(BREATH_END);}
    private void setBreathEnd(boolean value) {this.dataManager.set(BREATH_END, Boolean.valueOf(value));}
    private boolean isBreathAttack() {return this.dataManager.get(BREATH_ATTACK);}
    private void setBreathAttack(boolean value) {this.dataManager.set(BREATH_ATTACK, Boolean.valueOf(value));}
    public boolean isAirTransitionAttack() {return this.dataManager.get(AIR_TRANSITION_ATTACK);}
    protected void setAirGroundTransitionAttack(boolean value) {this.dataManager.set(AIR_TRANSITION_ATTACK, Boolean.valueOf(value));}


    public Vec3d targetBreathPosition = null;

    private static int wingFlapTimer = 45;

    private static int lightningAmbienceTimer = 0;

    public boolean isDoingBreathAttack = false;

    private boolean isSetUpFallAttack = false;

    private boolean isSetupFlyAttack = false;

    private boolean isTeleportingAttack = false;

    public EntityHighKingDrake(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(4.0F, 5.0F);
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.setNoGravity(true);
        this.hitboxParts = new MultiPartEntityPart[]{model, torso, neckPart1, neckPart2, headPart, tailPart1, legPartLeft, legPartRight, tailPart2, tailPart3, tailPart4, tailPart5};
        this.experienceValue = MobConfig.high_dragon_experience_value;
        BlockPos pos = new BlockPos(x,y,z);
        this.setSpawnLocation(pos.add(0, -25, 0));
        this.setHasSpawn(true);
        this.playSound(SoundsHandler.HIGH_DRAKE_ROAR_AIR, 2.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
    }

    public EntityHighKingDrake(World worldIn) {
        super(worldIn);
        this.setSize(4.0F, 5.0F);
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.setNoGravity(true);
        this.hitboxParts = new MultiPartEntityPart[]{model, torso, neckPart1, neckPart2, headPart, tailPart1, legPartLeft, legPartRight, tailPart2, tailPart3, tailPart4, tailPart5};
        this.experienceValue = MobConfig.high_dragon_experience_value;
    }

    public EntityHighKingDrake(World worldIn, int timesUsed, BlockPos pos) {
        super(worldIn);
        this.timesUsed = timesUsed;
        this.timesUsed++;
        this.doBossReSummonScaling();
        this.setSize(4.0F, 5.0F);
        this.moveHelper = new FlyingMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.setNoGravity(true);
        this.hitboxParts = new MultiPartEntityPart[]{model, torso, neckPart1, neckPart2, headPart, tailPart1, legPartLeft, legPartRight, tailPart2, tailPart3, tailPart4, tailPart5};
        this.experienceValue = MobConfig.high_dragon_experience_value;
        this.setSpawnLocation(pos.add(0, -25, 0));
        this.setHasSpawn(true);
        this.playSound(SoundsHandler.HIGH_DRAKE_ROAR_AIR, 2.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("King_Fight_Mode", this.isKingFightMode());
        nbt.setBoolean("Grounded", this.isGrounded());
        nbt.setBoolean("Dragon_Roar", this.isDragonRoar());
        nbt.setBoolean("Dragon_Storm", this.isDragonStorm());
        nbt.setBoolean("Ground_Transition", this.isGroundTransitionAttack());
        nbt.setBoolean("Bite_Begin", this.isBiteBegin());
        nbt.setBoolean("Bite_Cast", this.isBiteCast());
        nbt.setBoolean("Bite_Tail_Swipe", this.isBiteTailSwipe());
        nbt.setBoolean("Bite_End", this.isBiteEnd());
        nbt.setBoolean("Ground_Roar", this.isGroundRoar());
        nbt.setBoolean("Ground_Stomp", this.isGroundStomp());
        nbt.setBoolean("Breath_Begin", this.isBreathBegin());
        nbt.setBoolean("Breath_End", this.isBreathEnd());
        nbt.setBoolean("Breath_Attack", this.isBreathAttack());
        nbt.setBoolean("Air_Transition", this.isAirTransitionAttack());
        nbt.setFloat("Look", this.getPitch());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setKingFightMode(nbt.getBoolean("King_Fight_Mode"));
        this.setGrounded(nbt.getBoolean("Grounded"));
        this.setDragonRoar(nbt.getBoolean("Dragon_Roar"));
        this.setDragonStorm(nbt.getBoolean("Dragon_Storm"));
        this.setGroundTransitionAttack(nbt.getBoolean("Ground_Transition"));
        this.setBiteBegin(nbt.getBoolean("Bite_Begin"));
        this.setBiteCast(nbt.getBoolean("Bite_Cast"));
        this.setBiteTailSwipe(nbt.getBoolean("Bite_Tail_Swipe"));
        this.setBiteEnd(nbt.getBoolean("Bite_End"));
        this.setGroundRoar(nbt.getBoolean("Ground_Roar"));
        this.setGroundStomp(nbt.getBoolean("Ground_Stomp"));
        this.setBreathAttack(nbt.getBoolean("Breath_Attack"));
        this.setBreathBegin(nbt.getBoolean("Breath_Begin"));
        this.setBreathEnd(nbt.getBoolean("Breath_End"));
        this.setAirGroundTransitionAttack(nbt.getBoolean("Air_Transition"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.dataManager.set(LOOK, nbt.getFloat("Look"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(KING_FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(GROUNDED,Boolean.valueOf(false));
        this.dataManager.register(DRAGON_STORM, Boolean.valueOf(false));
        this.dataManager.register(DRAGON_ROAR, Boolean.valueOf(false));
        this.dataManager.register(GROUND_TRANSITION_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(BITE_BEGIN, Boolean.valueOf(false));
        this.dataManager.register(BITE_CAST, Boolean.valueOf(false));
        this.dataManager.register(BITE_TAIL_SWING, Boolean.valueOf(false));
        this.dataManager.register(BITE_END, Boolean.valueOf(false));
        this.dataManager.register(GROUND_ROAR, Boolean.valueOf(false));
        this.dataManager.register(GROUND_STOMP, Boolean.valueOf(false));
        this.dataManager.register(BREATH_BEGIN, Boolean.valueOf(false));
        this.dataManager.register(BREATH_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(BREATH_END, Boolean.valueOf(false));
        this.dataManager.register(AIR_TRANSITION_ATTACK, Boolean.valueOf(false));
    }
    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.high_dragon_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.high_dragon_armor);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.high_dragon_armor_toughness);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        Vec3d[] avec3d = new Vec3d[this.hitboxParts.length];
        for (int j = 0; j < this.hitboxParts.length; ++j) {
            avec3d[j] = new Vec3d(this.hitboxParts[j].posX, this.hitboxParts[j].posY, this.hitboxParts[j].posZ);
        }

      //  this.setHitBoxPos(corePart, new Vec3d(0, 1, 0));
        if(this.isGrounded()) {
            this.setHitBoxPos(torso, new Vec3d(0, 1.5, 0));
            this.setHitBoxPos(neckPart1, new Vec3d(-2.0, 2.9, 0));
            this.setHitBoxPos(neckPart2, new Vec3d(-3.0, 2.6, 0));
            this.setHitBoxPos(headPart, new Vec3d(-4.2, 2.1, 0));
            this.setHitBoxPos(tailPart1, new Vec3d(2, 1.1, 0));
            this.setHitBoxPos(legPartLeft, new Vec3d(1, 0, -1.2));
            this.setHitBoxPos(legPartRight, new Vec3d(1, 0, 1.2));
            this.setHitBoxPos(tailPart2, new Vec3d(3.2, 1.1, 0));
            this.setHitBoxPos(tailPart3, new Vec3d(4.3, 1.15, 0));
            this.setHitBoxPos(tailPart4, new Vec3d(5.4,1.1,0));
            this.setHitBoxPos(tailPart5, new Vec3d(6.5,1, 0));
        } else {
            this.setHitBoxPos(torso, new Vec3d(0, 1.5, 0));
            this.setHitBoxPos(neckPart1, new Vec3d(-2.0, 1.5, 0));
            this.setHitBoxPos(neckPart2, new Vec3d(-3.0, 1.5, 0));
            this.setHitBoxPos(headPart, new Vec3d(-4.2, 1.35, 0));
            this.setHitBoxPos(tailPart1, new Vec3d(2, 1.4, 0));
            this.setHitBoxPos(legPartLeft, new Vec3d(2.1, 0.7, 0.6));
            this.setHitBoxPos(legPartRight, new Vec3d(2.1, 0.7, -0.6));
            this.setHitBoxPos(tailPart2, new Vec3d(3.2, 1.4, 0));
            this.setHitBoxPos(tailPart3, new Vec3d(4.3, 1.4, 0));
            this.setHitBoxPos(tailPart4, new Vec3d(5.4,1.5,0));
            this.setHitBoxPos(tailPart5, new Vec3d(6.5,1.5, 0));
        }
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
        Vec3d lookVel = ModUtils.getLookVec((float) (this.getPitch() * 0.35), this.renderYawOffset);
        Vec3d center = this.getPositionVector().add(ModUtils.yVec(1.2));

        Vec3d position = center.subtract(ModUtils.Y_AXIS.add(ModUtils.getAxisOffset(lookVel, offset)));
        ModUtils.setEntityPosition(entity, position);

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(getHealth() / getMaxHealth());

        if(world.isRemote && ticksExisted == 1 && ModConfig.experimental_features) {
            this.playMusic(this);
        }

        if(!world.isRemote) {
            EntityLivingBase target = this.getAttackTarget();

            if(ticksExisted == 10 && this.getSpawnLocation() != null) {
                List<EntitySkyTornado> nearbyEntities = this.world.getEntitiesWithinAABB(EntitySkyTornado.class, this.getEntityBoundingBox().grow(25, 15, 25).offset(0, -15, 0), e -> !e.getIsInvulnerable());
                if(!nearbyEntities.isEmpty()) {
                    for(EntitySkyTornado tornado : nearbyEntities) {
                        tornado.setDead();
                    }
                }
            }

            if(this.isGrounded()) {
                this.dataManager.set(LOOK, 0F);
            }

            if(!this.isGrounded()) {
                wingFlapTimer--;
                if(wingFlapTimer < 0 && !this.isGroundTransitionAttack()) {
                       world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundsHandler.HIGH_DRAKE_WING_FLAP, SoundCategory.HOSTILE, 2.0f, 0.9f / (world.rand.nextFloat() * 0.4F + 0.4f));
                    //this.playSound(SoundsHandler.HIGH_DRAKE_WING_FLAP, 2.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
                    wingFlapTimer = 100;
                }

                lightningAmbienceTimer--;

                if(lightningAmbienceTimer < 0) {
                    Vec3d posToo = this.getPositionVector().add((ModRand.range(-20, 20) + 40),0,ModRand.range(-20, 20) + 40);
                    EntityLightningBolt bolt = new EntityLightningBolt(world, posToo.x, posToo.y, posToo.z, false);
                    world.spawnEntity(bolt);
                    lightningAmbienceTimer = 80 + ModRand.range(10, 300);
                }

            }

            if(target != null) {
                timeUntilTransition--;
                if(!this.isGrounded()) {
                    AxisAlignedBB box = getEntityBoundingBox().grow(1.25, 1.0, 1.25).offset(0, -0.1, 0);
                    ModUtils.destroyBlocksInAABB(box, world, this);

                    //King Separate Cooldown
                    if(!this.isKingFightMode()) {
                        kingAttackCooldown--;

                        if(kingAttackCooldown < 0 && timeUntilTransition > 200) {
                            if(switchAttacks) {
                                king_cast_magic.accept(target);
                            } else {
                                king_summon_lightning.accept(target);
                            }
                            kingAttackCooldown = 300;
                        }
                    }


                }


                //Breath Attack Updater
                if(this.isDoingBreathAttack) {
                    if(targetBreathPosition != null) {
                        Vec3d targetPos = targetBreathPosition;
                        this.getLookHelper().setLookPosition(targetPos.x, targetPos.y + 1.5, targetPos.z, 35F, 35F);
                    }
                }
            }


            //Boss Reset Timer
            if(target == null && this.isHadPreviousTarget() && ModConfig.boss_reset_enabled) {
                int nearbyPlayers = ServerScaleUtil.getPlayersForReset(this, world);
                if (nearbyPlayers == 0) {
                    if (targetTrackingTimer > 0) {
                        targetTrackingTimer--;
                    }
                    if (targetTrackingTimer < 1) {
                        if(this.timesUsed != 0) {
                            this.timesUsed--;
                            turnBossIntoSummonSpawner(this.getSpawnLocation());
                            this.setDead();
                        } else {
                            this.resetBossTask();
                        }
                    }
                }
            }

            //Teleport Boss if the spawn location is not null and is too far from its spawnpoint
            if(this.getSpawnLocation() != null && this.isHasSpawn()) {
                Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());
                double distSq = this.getDistanceSq(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                double distance = Math.sqrt(distSq);
                if(!world.isRemote) {
                    if (distance > 60) {
                        this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                    }
                }
            }

            if(this.isSetupFlyAttack) {
                this.motionY = 0.25;
            }

            //Ground Fall Attack
            if(this.isSetUpFallAttack && !world.isAirBlock(this.getPosition().add(0, -2, 0)) && this.isGroundTransitionAttack()) {
                if(target != null) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = this.getAttack();
                    ModUtils.handleAreaImpact(4f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                    new ActionGroundTransition((int) this.getDistance(target) + 3).performAction(this, target);
                    this.playSound(SoundsHandler.HIGH_DRAKE_IMPACT_GROUND, 1.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
                    this.isSetUpFallAttack = false;
                }
            } else if(!this.isGroundTransitionAttack()) {
                this.isSetUpFallAttack = false;
            }
        }
    }



    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityHighKingDrakeAI(this, 40, 30, 35,new TimedAttackInitiator<>(this, 10)));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double healthFactor = this.getHealth() / this.getMaxHealth();

        if(!this.isFightMode()) {
            //This is picking between transitions to use, from ground to air and vise versa
            if(timeUntilTransition < 0) {

                if(!this.isGrounded()) {
                    dragon_transition_to_ground.accept(target); // Transition to Ground Attack
                    timeUntilTransition = ((int) MobConfig.high_dragon_ground_time * 20) + ModRand.range(0, 100);
                } else {
                    dragon_transition_to_air.accept(target); // Transition to Air Attack
                    timeUntilTransition = ((int) MobConfig.high_dragon_fly_time * 20) + ModRand.range(0, 400);
                }
                return 60;
            }
            // Specifically Flying Attacks
            if(!this.isGrounded()) {
                List<Consumer<EntityLivingBase>> attacksFlying = new ArrayList<>(Arrays.asList(dragon_roar_attack, dragon_storm_breath));
                double[] weights = {
                        (prevAttacks != dragon_roar_attack) ? 1/distance : 0,
                        (prevAttacks != dragon_storm_breath) ? 1/distance : 0
                };
                prevAttacks = ModRand.choice(attacksFlying, rand, weights).next();
                prevAttacks.accept(target);
            }

            //Specifically Ground Attacks
            if(this.isGrounded()) {
                List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(dragon_bite_attack, dragon_storm_breath_ground, dragon_roar_ground, dragon_stomp_ground));
                double[] weights = {
                        (prevAttacks != dragon_bite_attack && distance <= 7) ? 1/distance : 0, // Bite Attack, can do different things
                        (prevAttacks != dragon_storm_breath && distance > 6) ? 1/distance : 0, // Storm Breath, can do different things
                        (prevAttacks != dragon_roar_ground && distance > 7) ? 1/distance : 0, // Dragon Roar
                        (prevAttacks != dragon_stomp_ground && distance <= 7) ? 1/distance : 0 //Dragon Stomp
                };
                prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
                prevAttacks.accept(target);
            }
        }
        //Dragon Attacks
        return !this.isGrounded() ? 300 : 30 + ModRand.range(40, 90);
    }

    private final Consumer<EntityLivingBase> dragon_transition_to_air = (target) -> {
      this.setAirGroundTransitionAttack(true);
      this.setImmovable(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.lockLook = true;

        addEvent(()-> this.playSound(SoundsHandler.DRAKE_CAST_LIGHTNING, 1.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f)), 15);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_DRAKE_WING_FLAP, 1.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f)), 45);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_DRAKE_WING_FLAP, 1.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f)), 75);
        addEvent(()-> this.playSound(SoundsHandler.HIGH_DRAKE_WING_FLAP, 1.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f)), 100);

      addEvent(()-> {
          //king summons forth alt rings of lightning
          new ActionKingCastQuickAOE().performAction(this, target);
      }, 25);

      addEvent(()-> {
          this.lockLook = false;
          this.setImmovable(false);
          this.isSetupFlyAttack = true;
          this.setNoGravity(true);
      }, 45);

      addEvent(()-> {
          this.isSetupFlyAttack = false;
          this.setGrounded(false);
          this.moveHelper = new FlyingMoveHelper(this);
          this.navigator = new PathNavigateFlying(this, world);
      }, 120);

      addEvent(()-> {
          this.setAirGroundTransitionAttack(false);
          this.setImmovable(false);
          this.setFullBodyUsage(false);
          this.setFightMode(false);
          wingFlapTimer = 45;
      }, 135);
    };

    private final Consumer<EntityLivingBase> dragon_transition_to_ground = (target) -> {
      this.setGroundTransitionAttack(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      addEvent(()-> this.setImmovable(true), 30);
      this.isTeleportingAttack = true;

      //Switch to Ground Mode and be affected by Gravity

        addEvent(()-> world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE), 40);
      addEvent(()-> {
        this.setGrounded(true);
        this.setNoGravity(false);
          this.moveHelper = new EntityMoveHelper(this);
          this.navigator = new PathNavigateGround(this, world);
          this.setImmovable(false);
          this.setPosition(target.posX, target.posY + 23, target.posZ);
          this.isSetUpFallAttack = true;
          this.lockLook = true;
          this.isTeleportingAttack = false;
      }, 55);

        addEvent(()-> world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE), 60);

        addEvent(()-> {
            this.setShaking(true);
            this.shakeTime = 80;
        }, 90);

      addEvent(()-> this.setImmovable(true), 100);

      addEvent(()-> this.lockLook = false, 115);

      addEvent(()-> this.setShaking(false), 120);

      addEvent(()-> {
        this.setImmovable(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setGroundTransitionAttack(false);
      }, 125);
    };

    private final Consumer<EntityLivingBase> dragon_stomp_ground = (target) -> {
      this.setGroundStomp(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);

      addEvent(()-> {
          this.lockLook = true;
      }, 18);

      addEvent(() -> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, -1)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(2.75f, (e) -> damage, this, offset, source, 0.2f, 0, false);
          //Do Stomp AOE RIGHT FOOT
          new ActionDragonStomp(this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,0,-1.8)))).performAction(this, target);
          this.playSound(SoundsHandler.HIGH_DRAKE_IMPACT_GROUND, 1.3f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
          this.setShaking(true);
          this.shakeTime = 30;
      }, 30);

      addEvent(()-> {
          this.setShaking(false);
      }, 60);

      addEvent(()-> {
          this.lockLook = false;
      }, 80);

      addEvent(()-> {
          this.setGroundStomp(false);
          this.setFightMode(false);
          this.setFullBodyUsage(false);
          this.setImmovable(false);
      }, 90);
    };

    private final Consumer<EntityLivingBase> dragon_roar_ground = (target) -> {
      this.setGroundRoar(true);
      this.setImmovable(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);

      addEvent(()-> this.lockLook = true, 30);
      addEvent(()-> {
          this.playSound(SoundsHandler.HIGH_DRAKE_ROAR_GROUND, 1.75f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
          this.setShaking(true);
          this.shakeTime = 80;
      }, 30);

      addEvent(()-> new ActionDragonSpecialGround().performAction(this, target), 40);

      addEvent(()-> this.lockLook = false, 95);
      addEvent(()-> {
          this.setGroundRoar(false);
          this.setImmovable(false);
          this.setFightMode(false);
          this.setFullBodyUsage(false);
          this.setShaking(false);
      }, 105);

    };

    private final Consumer<EntityLivingBase> dragon_storm_breath_ground = (target) -> {
      this.setBreathBegin(true);
      this.setImmovable(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);

        addEvent(()-> this.playSound(SoundsHandler.HIGH_DRAKE_BREATH_GROUND, 1.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f)), 25);
      addEvent(()-> {
          AtomicReference<Vec3d> relBackPos = new AtomicReference<>(target.getPositionVector());
          this.targetBreathPosition = target.getPositionVector();
          this.isDoingBreathAttack = true;
          //Do Storm Breath
          for(int i = 0; i <= 80; i += 4) {
              addEvent(()-> {
                  Vec3d PrevPos = target.getPositionVector();
                  addEvent(()-> {
                      if(this.getDistanceSq(target) > 49) {
                          relBackPos.set(PrevPos);
                          this.targetBreathPosition = PrevPos;
                      } else {
                          this.targetBreathPosition = relBackPos.get();
                      }

                  }, 3);
              }, i);
          }

          new ActionStormBreathGround().performAction(this, target);
      }, 35);

      addEvent(()-> {
        this.setBreathBegin(false);
        Vec3d targetPos = target.getPositionVector();
          double distSq = this.getDistanceSq(targetPos.x, targetPos.y, targetPos.z);
          double distance = Math.sqrt(distSq);

          if(distance <= 8) {
            setBreathTooStomp(target);
          } else {
            this.setBreathEnd(true);

              addEvent(()-> {
                  this.isDoingBreathAttack = false;
                  this.targetBreathPosition = null;
              }, 35);

            addEvent(()-> {
            this.setBreathEnd(false);
            this.setImmovable(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            }, 60);
          }
      }, 90);
    };


    private void setBreathTooStomp(EntityLivingBase target) {
        breath_attack_stomp.accept(target);
    }

    private final Consumer<EntityLivingBase> breath_attack_stomp = (target) -> {
      this.setBreathAttack(true);

      addEvent(()-> {
        this.isDoingBreathAttack = false;
        this.targetBreathPosition = null;
      }, 35);

      addEvent(()-> this.lockLook = true, 50);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 1)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(2.75f, (e) -> damage, this, offset, source, 0.2f, 0, false);
        //Do Stomp Attack LEFT FOOT
          new ActionDragonStomp(this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,0,1.8)))).performAction(this, target);
          this.playSound(SoundsHandler.HIGH_DRAKE_IMPACT_GROUND, 1.3f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
          this.setShaking(true);
          this.shakeTime = 30;
      }, 70);

        addEvent(()-> {
            this.setShaking(false);
        }, 100);

      addEvent(()-> this.lockLook = false, 95);

      addEvent(()-> {
        this.setBreathAttack(false);
          this.setImmovable(false);
          this.setFightMode(false);
          this.setFullBodyUsage(false);
      }, 105);

    };
    //Bite Attack
    private final Consumer<EntityLivingBase> dragon_bite_attack = (target) -> {
      this.setBiteBegin(true);
      this.setFightMode(true);
      this.setImmovable(true);
      this.setFullBodyUsage(true);

      addEvent(()-> this.lockLook = true, 42);

      addEvent(()-> {
          this.playSound(SoundsHandler.HIGH_DRAKE_BITE, 1.5f, 0.5f / (rand.nextFloat() * 0.4F + 0.4f));
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(4, 1.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(3.0f, (e) -> damage, this, offset, source, 0.2f, 0, false);
          this.setBiteBegin(false);

          int chanceTooFail = ModRand.range(1, 11);

          if(this.checkSightLineKing(target) && chanceTooFail > 3 || chanceTooFail > 8) {
              //continue bite with a swing and cast
              this.setTooCastAndBite(target);
          } else if (chanceTooFail > 4){
              //continue bite with a tail swipe
              this.setBiteTooTailSwipe(target);
          } else {
              //end bite
              this.setToEndBite(target);
          }

          }, 50);
    };

    private void setBiteTooTailSwipe(EntityLivingBase target) {
        bite_tail_swipe.accept(target);
    }

    private final Consumer<EntityLivingBase> bite_tail_swipe = (target) -> {
      this.setBiteTailSwipe(true);
      this.lockLook = false;
        this.playSound(SoundsHandler.HIGH_DRAKE_BITE, 1.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
      addEvent(()-> {
        Vec3d targetedPos = target.getPositionVector();
        addEvent(()-> {
            this.setImmovable(false);
            double distance = this.getPositionVector().distanceTo(targetedPos);
            ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.12),0.2F);
            this.lockLook = true;
        }, 12);
      }, 23);

      addEvent(()-> {
        for(int i = 0; i < 15; i += 5) {
            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(5.5f, (e) -> damage, this, offset, source, 0.2f, 0, false);
            }, i);
        }
      }, 35);

      addEvent(()-> this.setImmovable(true), 63);
      addEvent(()-> this.lockLook = false, 90);

      addEvent(()-> {
        this.setBiteTailSwipe(false);
          this.setFightMode(false);
          this.setFullBodyUsage(false);
          this.setImmovable(false);
      }, 100);
    };

    private void setToEndBite(EntityLivingBase target) {
        bite_end.accept(target);
    }

    private final Consumer<EntityLivingBase> bite_end = (target) -> {
      this.setBiteEnd(true);

      addEvent(()-> {
          this.lockLook = false;
      }, 5);

      addEvent(()-> {
        this.setBiteEnd(false);
          this.setFightMode(false);
          this.setFullBodyUsage(false);
          this.setImmovable(false);
      }, 30);
    };

    private void setTooCastAndBite(EntityLivingBase target) {
        bite_continue_cast.accept(target);
    }
    private final Consumer<EntityLivingBase> bite_continue_cast = (target) -> {
      this.setBiteCast(true);
      this.lockLook = false;

      addEvent(()-> this.lockLook = true, 17);

      addEvent(()-> {
          this.playSound(SoundsHandler.DRAUGR_ELITE_SWING, 1.0f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f));
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(3, 1.2, -0.4)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.2f, 0, false);
      }, 25);

        addEvent(()-> this.playSound(SoundsHandler.DRAKE_CAST_LIGHTNING, 1.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f)), 55);

      addEvent(()-> {
          //King Casts AOE Lightning
          new ActionKingSelfAOE().performAction(this, target);
      }, 61);

      addEvent(()-> {
          this.lockLook = false;
      }, 80);

      addEvent(()-> {
        this.setBiteCast(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
      }, 90);
    };

    private final Consumer<EntityLivingBase> dragon_storm_breath = (target) -> {
      this.setDragonStorm(true);
      this.setFightMode(true);

        addEvent(()-> this.playSound(SoundsHandler.HIGH_DRAKE_BREATH_AIR, 3.0f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f)), 15);

      addEvent(()-> {
          new ActionDrakeStormBreathFlying().performAction(this, target);
      }, 25);

      addEvent(()-> {
        this.setDragonStorm(false);
        this.setFightMode(false);
      }, 100);
    };

    private final Consumer<EntityLivingBase> dragon_roar_attack = (target) -> {
      this.setDragonRoar(true);
      this.setFightMode(true);

        addEvent(()-> this.playSound(SoundsHandler.HIGH_DRAKE_ROAR_AIR, 3.0f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f)), 70);
        addEvent(()-> {
            this.setShaking(true);
            this.shakeTime = 40;
        }, 70);
      addEvent(()-> {
        new ActionDragonSpecial().performAction(this, target);
      }, 85);

      addEvent(()-> {
          this.setShaking(false);
      }, 120);

      addEvent(()-> {
        this.setDragonRoar(false);
        this.setFightMode(false);
      }, 140);
    };

    private final Consumer<EntityLivingBase> king_summon_lightning = (target) -> {
        this.setKingCastLightning(true);
        this.setKingFightMode(true);
        this.switchAttacks = true;

        addEvent(()-> this.playSound(SoundsHandler.DRAKE_CAST_LIGHTNING, 2.5f, 0.9f / (rand.nextFloat() * 0.4F + 0.4f)), 40);
        addEvent(() -> {
            new ActionKingCastLighning().performAction(this, target);
        }, 50);

        addEvent(()-> {
      this.setKingCastLightning(false);
      this.setKingFightMode(false);
        }, 95);
    };

    private final Consumer<EntityLivingBase> king_cast_magic = (target) -> {
        this.setKingCast(true);
        this.setKingFightMode(true);
        this.switchAttacks = false;
        addEvent(()-> new ActionKingCastMagic().performAction(this, target), 37);

        addEvent(()-> {
        this.setKingCast(false);
        this.setKingFightMode(false);
        }, 95);
    };


    @Override
    public void travel(float strafe, float vertical, float forward) {
        if(!this.isGrounded()) {
            ModUtils.aerialTravel(this, strafe, vertical, forward);
        } else {
            super.travel(strafe, vertical, forward);
        }
    }

    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(this.isTeleportingAttack) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
    }


    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnDust(world, this.getPositionVector().add(ModRand.range(-6, 6), ModRand.range(-3, 4), ModRand.range(-6, 6)), ModColors.YELLOW, Vec3d.ZERO, ModRand.range(10, 15));
            ParticleManager.spawnDust(world, this.getPositionVector().add(ModRand.range(-6, 6), ModRand.range(-3, 4), ModRand.range(-6, 6)), ModColors.YELLOW, Vec3d.ZERO, ModRand.range(10, 15));
            ParticleManager.spawnDust(world, this.getPositionVector().add(ModRand.range(-6, 6), ModRand.range(-3, 4), ModRand.range(-6, 6)), ModColors.YELLOW, Vec3d.ZERO, ModRand.range(10, 15));
            ParticleManager.spawnDust(world, this.getPositionVector().add(ModRand.range(-6, 6), ModRand.range(-3, 4), ModRand.range(-6, 6)), ModColors.YELLOW, Vec3d.ZERO, ModRand.range(10, 15));
        }

        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ModUtils.circleCallback(4, 30, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(1)), ModColors.YELLOW, pos.normalize().scale(0.1), ModRand.range(70, 90));
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(3)), ModColors.YELLOW, pos.normalize().scale(0.1), ModRand.range(70, 90));
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(5)), ModColors.YELLOW, pos.normalize().scale(0.1), ModRand.range(70, 90));
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(7)), ModColors.YELLOW, pos.normalize().scale(0.1), ModRand.range(70, 90));
                ParticleManager.spawnDust(world, this.getPositionVector().add(ModUtils.yVec(-2)), ModColors.YELLOW, pos.normalize().scale(0.1), ModRand.range(70, 90));
            });
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        //Ground Idle Controllers
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdleGround));
        data.addAnimationController(new AnimationController(this, "walk_controller", 0, this::predicateWalkGround));
        //Fly Idle Controllers
        data.addAnimationController(new AnimationController(this, "fly_base_controller", 0, this::predicateIdleFlyBase));
        data.addAnimationController(new AnimationController(this, "fly_king_controller", 0, this::predicateIdleFlyKing));
        data.addAnimationController(new AnimationController(this, "fly_dragon_controller", 0, this::predicateIdleFlyDragon));
        //Attack Handlers King
        data.addAnimationController(new AnimationController(this, "king_attacks_controller", 0, this::predicateKingAttacks));
        //Attack Handler Dragon + Grounded Attacks
        data.addAnimationController(new AnimationController(this, "dragon_attacks_controller", 0, this::predicateDragonAttacks));
        //Follow Up Attack Handler
        data.addAnimationController(new AnimationController(this, "dragon_follow_up_controller", 0, this::predicateFollowUpAttacks));
    }


    private <E extends IAnimatable> PlayState predicateFollowUpAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isBiteCast()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BITE_CAST, false));
                return PlayState.CONTINUE;
            }
            if(this.isBiteTailSwipe()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BITE_TAIL_SWIPE, false));
                return PlayState.CONTINUE;
            }
            if(this.isBiteEnd()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BITE_END, false));
                return PlayState.CONTINUE;
            }
            if(this.isBreathEnd()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BREATH_END, false));
                return PlayState.CONTINUE;
            }
            if(this.isBreathAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BREATH_ATTACK, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateDragonAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isDragonRoar()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DRAGON_ROAR, false));
                return PlayState.CONTINUE;
            }
            if(this.isDragonStorm()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DRAGON_STORM, false));
                return PlayState.CONTINUE;
            }
            if(this.isGroundTransitionAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DRAGON_GROUND_TRANSITION, false));
                return PlayState.CONTINUE;
            }
            if(this.isAirTransitionAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DRAGON_AIR_TRANSITION, false));
                return PlayState.CONTINUE;
            }
            if(this.isBiteBegin()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BITE_BEGIN, false));
                return PlayState.CONTINUE;
            }
            if(this.isGroundRoar()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_GROUND_ROAR, false));
                return PlayState.CONTINUE;
            }
            if(this.isGroundStomp()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STOMP, false));
                return PlayState.CONTINUE;
            }
            if(this.isBreathBegin()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BREATH_BEGIN, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateKingAttacks(AnimationEvent<E> event) {
        if(this.isKingFightMode()) {
            if(this.isKingCast()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_KING_CAST_MAGIC, false));
                return PlayState.CONTINUE;
            }
            if(this.isKingCastLightning()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_KING_CAST_LIGHTNING, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdleGround(AnimationEvent<E> event) {
            if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode() && this.isGrounded()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_GROUND_IDLE, true));
                return PlayState.CONTINUE;
            }

        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateWalkGround(AnimationEvent<E> event) {

            if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage() && this.isGrounded()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_GROUND_WALK, true));
                return PlayState.CONTINUE;
            }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdleFlyBase(AnimationEvent<E> event) {
        if(!this.isGrounded() && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLY_BASE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdleFlyKing(AnimationEvent<E> event) {
        if(!this.isGrounded() && !this.isKingFightMode() && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLY_KING, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdleFlyDragon(AnimationEvent<E> event) {
        if(!this.isGrounded() && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLY_DRAGON, true));
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
    public int tickTimer() {
        return this.ticksExisted;
    }

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook/2);
            this.dataManager.set(LOOK, clampedLook);
    }

    private boolean checkSightLineKing(EntityLivingBase target) {
        List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(2.0D, 1.0D, 2.0D), e -> !e.getIsInvulnerable());
        if(!nearbyEntities.isEmpty()) {
            for(EntityLivingBase base : nearbyEntities) {
                if (base == target) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
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
    public final boolean attackEntityFrom(DamageSource source, float amount) {

        if(source.getImmediateSource() instanceof ProjectileStormWind || source.getImmediateSource() instanceof ProjectileLightRing) {
            return false;
        }

       return super.attackEntityFrom(source, amount);
    }



    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
        if(part == this.headPart || part == this.neckPart1 || part == this.neckPart2 ||  part == this.torso || part == this.tailPart1 || part == this.tailPart2 ||
        part == this.tailPart3 || part == this.tailPart4 || part == this.tailPart5 || part == this.legPartRight || part == this.legPartLeft) {
            return super.attackEntityFrom(source, damage);
        }
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
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
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.HIGH_DRAKE_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.HIGH_DRAKE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.HIGH_DRAKE_ROAR_AIR;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        if(this.isGrounded()) {
            this.playSound(SoundsHandler.HIGH_DRAKE_STEP, 0.8F, 0.9f + ModRand.getFloat(0.3F));
        }
    }

    private static final ResourceLocation LOOT_BOSS = new ResourceLocation(ModReference.MOD_ID, "high_dragon");

    @Override
    public void onDeath(DamageSource cause) {
        this.setImmovable(true);
        if(!world.isRemote) {
            if(MobConfig.high_dragon_after_death && this.getSpawnLocation() != null) {
                this.experienceValue = 0;
                //Spawn the High King for the second part of the boss fight
                EntityHighKing king;
                if(timesUsed != 0) {
                    king = new EntityHighKing(world, this.timesUsed, this.getSpawnLocation());
                    king.setPosition(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());
                } else {
                    king = new EntityHighKing(world, this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());
                }

                this.world.spawnEntity(king);
            } else {
                //Spawn a chest with the loot table for this boss
                if(this.getSpawnLocation() != null) {
                    BlockPos pos = this.getSpawnLocation();
                    world.setBlockState(pos, Blocks.CHEST.getDefaultState());
                    TileEntity te = world.getTileEntity(pos);
                    if(te instanceof TileEntityChest) {
                        TileEntityChest chest = (TileEntityChest) te;
                        chest.setLootTable(LOOT_BOSS, rand.nextLong());
                    }
                } else {
                    BlockPos pos = this.getPosition();
                    world.setBlockState(pos, Blocks.CHEST.getDefaultState());
                    TileEntity te = world.getTileEntity(pos);
                    if(te instanceof TileEntityChest) {
                        TileEntityChest chest = (TileEntityChest) te;
                        chest.setLootTable(LOOT_BOSS, rand.nextLong());
                    }
                }
            }
        }


        super.onDeath(cause);
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 50.0F);
            if (dist >= 50.0F) {
                return 0.0F;
            }
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * 2F * screamMult);
        }
        return 0;
    }

    @Nullable
    @Override
    public SoundEvent getBossMusic() {
        return SoundsHandler.HIGH_DRAGON_TRACK;
    }
}
