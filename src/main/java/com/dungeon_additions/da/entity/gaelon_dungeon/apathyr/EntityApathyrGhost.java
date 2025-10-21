package com.dungeon_additions.da.entity.gaelon_dungeon.apathyr;

import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityGaelonBase;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.function.Consumer;

public class EntityApathyrGhost extends EntityGaelonBase implements IAnimatable, IAnimationTickable {

    private final AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_SWING = "swing";
    private final String ANIM_SWING_CONNECT = "swing_connect";
    private final String ANIM_JAB = "jab";
    private final String ANIM_CIRCLE_ATTACK = "circle_swing";

    private static final DataParameter<Boolean> SWING = EntityDataManager.createKey(EntityApathyrGhost.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_CONNECT = EntityDataManager.createKey(EntityApathyrGhost.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JAB = EntityDataManager.createKey(EntityApathyrGhost.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CIRCLE_SWING = EntityDataManager.createKey(EntityApathyrGhost.class, DataSerializers.BOOLEAN);

    private void setSwingAttack(boolean value) {this.dataManager.set(SWING, Boolean.valueOf(value));}
    private boolean isSwingAttack() {return this.dataManager.get(SWING);}
    private void setSwingConnect(boolean value) {this.dataManager.set(SWING_CONNECT, Boolean.valueOf(value));}
    private boolean isSwingConnect() {return this.dataManager.get(SWING_CONNECT);}
    private void setJab(boolean value) {this.dataManager.set(JAB, Boolean.valueOf(value));}
    private boolean isJab() {return this.dataManager.get(JAB);}
    private void setCircleSwing(boolean value) {this.dataManager.set(CIRCLE_SWING, Boolean.valueOf(value));}
    private boolean isCircleSwing() {return this.dataManager.get(CIRCLE_SWING);}
    private EntityApathyr parent;

    private float damageIn = 0;
    private EntityPlayer player;

    public EntityApathyrGhost(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityApathyrGhost(World worldIn) {
        super(worldIn);
    }

    public EntityApathyrGhost(World world, EntityApathyr parent) {
        super(world);
        this.parent = parent;
        damageIn = parent.getAttack();
        this.selectAttack();
    }

    public EntityApathyrGhost(World world, EntityPlayer player) {
        super(world);
        this.player = player;
    }

    private void selectAttack() {
        if(this.parent != null) {
            EntityLivingBase target = this.parent.getAttackTarget();
            if(target != null) {
                    int randI = ModRand.range(1, 4);
                    if(randI == 1) {
                        circle_swing_attack.accept(target);
                    } else if (randI == 2) {
                        jab_attack.accept(target);
                    } else {
                        swing.accept(target);
                    }
            }
        } else {
            this.setDead();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!world.isRemote) {
            if(parent != null) {
                EntityLivingBase target = this.parent.getAttackTarget();
                if(target != null && !this.lockLook) {
                    this.faceEntity(target, 30F, 30F);
                }
            } else {
                this.setDead();
            }
        }
    }

    private final Consumer<EntityLivingBase> circle_swing_attack = (target) -> {
        this.setFightMode(true);
        this.setImmovable(true);
        this.setCircleSwing(true);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(3));
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.33),0.1F);
            }, 4);
        }, 21);


        addEvent(() -> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (damageIn);
            ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.9f, 0, false);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 30);

        addEvent(() -> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (damageIn);
            ModUtils.handleAreaImpact(4f, (e) -> damage, this, offset, source, 0.9f, 0, false);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 35);

        addEvent(()-> {
            this.setImmovable(true);
        }, 45);

        addEvent(()-> {
            this.lockLook = false;
        }, 65);

        addEvent(() -> {
            this.setImmovable(false);
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setCircleSwing(false);
            this.setDead();
        }, 75);
    };

    private final Consumer<EntityLivingBase> swing = (target) -> {
        this.setFightMode(true);
        this.setImmovable(true);
        boolean randI = rand.nextBoolean();

        if(randI) {
            this.setSwingConnect(true);
            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1.5));
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
                }, 4);
            }, 14);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (damageIn);
                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 23);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 30);

            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1.5));
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
                }, 4);
            }, 45);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (damageIn);
                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 54);

            addEvent(()-> {
                this.setImmovable(true);
            }, 60);

            addEvent(()-> {
                this.lockLook = false;
            }, 70);

            addEvent(()-> {
                this.setImmovable(false);
                this.setSwingConnect(false);
                this.setFightMode(false);
                this.setDead();
            }, 85);
        } else {
            this.setSwingAttack(true);
            addEvent(()-> {
                this.lockLook = true;
                Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
                Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1.5));
                addEvent(()-> {
                    this.setImmovable(false);
                    double distance = this.getPositionVector().distanceTo(targetedPos);
                    ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.23),0.1F);
                }, 4);
            }, 14);

            addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage =(float) (damageIn);
                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 23);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 30);

            addEvent(()-> {
                this.setImmovable(false);
                this.setSwingAttack(false);
                this.setFightMode(false);
                this.setDead();
            }, 50);
        }
    };


    private final Consumer<EntityLivingBase> jab_attack = (target) -> {
        this.setFightMode(true);
        this.setImmovable(true);
        this.setJab(true);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d targetedPos = target.getPositionVector().add(posSet.scale(-1));
            addEvent(()-> {
                this.lockLook = true;
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.33),0.1F);
            }, 4);
        }, 19);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (damageIn);
            ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.4f, 0, false);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 30);

        addEvent(()-> this.setImmovable(true), 35);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(false);
            this.setJab(false);
            this.setFightMode(false);
            this.setDead();
        }, 55);
    };

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Swing", this.isSwingAttack());
        nbt.setBoolean("Swing_Connect", this.isSwingConnect());
        nbt.setBoolean("Jab", this.isJab());
        nbt.setBoolean("Circle_Swing", this.isCircleSwing());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setSwingAttack(nbt.getBoolean("Swing"));
        this.setSwingConnect(nbt.getBoolean("Swing_Connect"));
        this.setJab(nbt.getBoolean("Jab"));
        this.setCircleSwing(nbt.getBoolean("Circle_Swing"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(SWING, Boolean.valueOf(false));
        this.dataManager.register(SWING_CONNECT, Boolean.valueOf(false));
        this.dataManager.register(JAB, Boolean.valueOf(false));
        this.dataManager.register(CIRCLE_SWING, Boolean.valueOf(false));
        super.entityInit();
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "attacks_controller_main_hold", 0, this::predicateAttacksMain));
    }

    private <E extends IAnimatable> PlayState predicateAttacksMain(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            AnimationBuilder builder = new AnimationBuilder();

            if(this.isSwingAttack()) {
                event.getController().setAnimation(builder.playOnce(ANIM_SWING));
            }
            if(this.isSwingConnect()) {
                event.getController().setAnimation(builder.playOnce(ANIM_SWING_CONNECT));
            }
            if(this.isJab()) {
                event.getController().setAnimation(builder.playOnce(ANIM_JAB));
            }
            if(this.isCircleSwing()) {
                event.getController().setAnimation( builder.playOnce(ANIM_CIRCLE_ATTACK));
            }
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
}
