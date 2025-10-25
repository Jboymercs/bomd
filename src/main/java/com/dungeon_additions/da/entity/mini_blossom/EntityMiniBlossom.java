package com.dungeon_additions.da.entity.mini_blossom;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.EntityAIBlossom;
import com.dungeon_additions.da.entity.ai.EntityAiMiniBlossom;
import com.dungeon_additions.da.entity.ai.IAttack;
import com.dungeon_additions.da.entity.sky_dungeon.EntityFarumSpike;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityMiniBlossom extends EntityAbstractBase implements IAnimatable, IAttack, IAnimationTickable {

    private final String ANIM_IDLE = "idle";
    private final String ANIM_TRAVEL = "travel";

    private final String ANIM_BURROW = "burrow";
    private final String ANIM_SPROUT = "spawn";

    private final String ANIM_ATTACK = "attack";
    private final String ANIM_SHOOT_THORN = "shoot_thorn";

    private static final DataParameter<Boolean> THORN_ATTACK = EntityDataManager.createKey(EntityMiniBlossom.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEE_ATTACK = EntityDataManager.createKey(EntityMiniBlossom.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> SPROUT = EntityDataManager.createKey(EntityMiniBlossom.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BURROW = EntityDataManager.createKey(EntityMiniBlossom.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> TRAVEL = EntityDataManager.createKey(EntityMiniBlossom.class, DataSerializers.BOOLEAN);

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;
    public EntityMiniBlossom(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.5f, 3.0f);
        playSproutAnimation();
    }

    public EntityMiniBlossom(World worldIn) {
        super(worldIn);
        this.setSize(1.5f, 3.0f);
        playSproutAnimation();
    }

    public boolean isCurrentlyTravelling = false;
    public boolean hasPlayedAnimation = false;

    public boolean isThornAttack() {return this.dataManager.get(THORN_ATTACK);}
    public boolean isMeleeAttack() {return this.dataManager.get(MELEE_ATTACK);}
    public boolean isSprout() {return this.dataManager.get(SPROUT);}
    public boolean isBurrow(){return this.dataManager.get(BURROW);}
    public boolean isTravel() {return this.dataManager.get(TRAVEL);}

    public void setThornAttack(boolean value) {this.dataManager.set(THORN_ATTACK, Boolean.valueOf(value));}
    public void setMeleeAttack(boolean value) {this.dataManager.set(MELEE_ATTACK, Boolean.valueOf(value));}
    public void setSprout(boolean value) {this.dataManager.set(SPROUT, Boolean.valueOf(value));}
    public void setBurrow(boolean value) {this.dataManager.set(BURROW, Boolean.valueOf(value));}
    public void setTravel(boolean value) {this.dataManager.set(TRAVEL, Boolean.valueOf(value));}

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(THORN_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(MELEE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SPROUT, Boolean.valueOf(false));
        this.dataManager.register(BURROW, Boolean.valueOf(false));
        this.dataManager.register(TRAVEL, Boolean.valueOf(false));

    }



    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Thorn_Attack", this.isThornAttack());
        nbt.setBoolean("Melee_Attack", this.isMeleeAttack());
        nbt.setBoolean("Sprout", this.isSprout());
        nbt.setBoolean("Burrow", this.isBurrow());
        nbt.setBoolean("Travel", this.isTravel());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setThornAttack(nbt.getBoolean("Thorn_Attack"));
        this.setMeleeAttack(nbt.getBoolean("Melee_Attack"));
        this.setSprout(nbt.getBoolean("Sprout"));
        this.setBurrow(nbt.getBoolean("Burrow"));
        this.setTravel(nbt.getBoolean("Travel"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        IBlockState block = world.getBlockState(new BlockPos(this.posX, this.posY - 1, this.posZ));
        if(block.isFullBlock()) {
            if (id == ModUtils.PARTICLE_BYTE) {
                ParticleManager.spawnBreak(world, this.getPositionVector().add(ModRand.randVec().scale(1.0f).add(ModUtils.yVec(0.75f))), Item.getItemFromBlock(block.getBlock()), ModRand.randVec().scale(0.1).add(ModUtils.yVec(0.1f)));
            }
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.mini_blossom_health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }
    public int moveDelay = 60;

    //Puts the Blossom into a stationary State
    public void playSproutAnimation() {
        this.setTravel(false);
        this.setImmovable(true);
        this.setSprout(true);
        addEvent(()-> {
            this.setSprout(false);
            this.hasPlayedAnimation =false;
            moveDelay = 80;

        }, 35);
    }

    //Puts the Mini-blossom into a movement State
    public void playBurrowAnimation() {
        this.setBurrow(true);
        addEvent(()-> {
      this.setImmovable(false);
        this.setBurrow(false);
        this.setTravel(true);
        this.hasPlayedAnimation = false;
        }, 35);
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isSprout() && !this.isFightMode() && !this.isBurrow() && !this.isTravel()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
                return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateTravel(AnimationEvent<E> event) {
        if(this.isTravel() && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_TRAVEL, true));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateSproutBurrow(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            if(this.isBurrow()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BURROW, false));
                return PlayState.CONTINUE;
            }
            if(this.isSprout()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SPROUT, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isThornAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT_THORN, false));
                return PlayState.CONTINUE;
            }
            if(this.isMeleeAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }



    @Override
    public void onUpdate() {
        super.onUpdate();


       if(this.isTravel()) {
           isCurrentlyTravelling = false;
           world.setEntityState(this, ModUtils.PARTICLE_BYTE);

       } else {
           isCurrentlyTravelling = true;

       }
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAiMiniBlossom<>(this, 1.5, 40, 9F, 0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    /**
     * Add a bit of brightness to the entity for it's Arena
     */
    @Override
    public int getBrightnessForRender() {
        return Math.min(super.getBrightnessForRender() + 30, 200);
    }


    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
    if(!this.isFightMode() && !this.isTravel() && !this.isBurrow() && !this.isSprout()) {
        List<Consumer<EntityLivingBase>> attacksMelee = new ArrayList<>(Arrays.asList(meleeAttack, projectileAttack));
        double[] weights = {
                (distance < 5) ? 1/distance : 0,    //Basic Melee Attack
                (distance > 4) ? distance * 0.02 : 0 //Projectile Thorn Attack
        };

        prevAttacks = ModRand.choice(attacksMelee, rand, weights).next();
        prevAttacks.accept(target);
    }

        return (prevAttacks == meleeAttack) ? 100 : 40;
    }


    private Consumer<EntityLivingBase> meleeAttack = (target) -> {
      this.setMeleeAttack(true);
      this.setFightMode(true);
      //This Attack will launch the player

      addEvent(()-> {
          this.lockLook = true;
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = MobConfig.mini_blossom_attack_damage;
          //Weird Damage Handling
          ModUtils.handleAreaImpact(3.5f, (e) -> damage, this, offset, source, 1.4f, 0, false);
          Vec3d relPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2.5, 0.5, 0)));
          Main.proxy.spawnParticle(18, relPos.x, this.posY, relPos.z, 0, 0, 0);
      }, 20);

      addEvent(()-> this.lockLook =false, 40);
      addEvent(()-> {
        this.setMeleeAttack(false);
        this.setFightMode(false);
      }, 40);
    };

    private Consumer<EntityLivingBase> projectileAttack = (target)-> {
      this.setThornAttack(true);
      this.setFightMode(true);

      //Thorn Attack
      addEvent(()-> {

        if(!world.isRemote && target != null) {
            Vec3d Pos = this.getPositionVector().add(0, 3.7, 0);
            EntityFarumSpike dart = new EntityFarumSpike(this.world);
            dart.setPosition(Pos.x, Pos.y, Pos.z);


            Vec3d targetPos = target.getPositionVector().add(ModUtils.yVec(target.getEyeHeight()));
            Vec3d velocity = targetPos.subtract(Pos).scale(0.15D);
            ModUtils.setEntityVelocity(dart, velocity);
            world.spawnEntity(dart);
        }

      }, 16);
      addEvent(()-> {
        this.setFightMode(false);
        this.setThornAttack(false);
      }, 30);
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "travel_controller", 0, this::predicateTravel));
        data.addAnimationController(new AnimationController(this, "sp_bu_controller", 0, this::predicateSproutBurrow));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttack));
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
      if(this.isTravel()) {
          return false;
      }
        return super.attackEntityFrom(source, amount);
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
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
