package com.dungeon_additions.da.entity.gaelon_dungeon.apathyr;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimDispatcher;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityGaelonBase;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class EntityApathyrGhost extends EntityGaelonBase {

    private final DaEntityAnimDispatcher animator = new DaEntityAnimDispatcher(this, false);

    private final String ANIM_SWING = "swing";
    private final String ANIM_SWING_CONNECT = "swing_connect";
    private final String ANIM_JAB = "jab";
    private final String ANIM_CIRCLE_ATTACK = "circle_swing";


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
        animator.setStateAnim(ANIM_CIRCLE_ATTACK);

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
            ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.9f, 0, false, 0.5F);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 30);

        addEvent(() -> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage =(float) (damageIn);
            ModUtils.handleAreaImpact(4f, (e) -> damage, this, offset, source, 0.9f, 0, false, 0.5F);
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
            this.setDead();
        }, 75);
    };

    private final Consumer<EntityLivingBase> swing = (target) -> {
        this.setFightMode(true);
        this.setImmovable(true);
        boolean randI = rand.nextBoolean();

        if(randI) {
            animator.setStateAnim(ANIM_SWING_CONNECT);
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
                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.6f, 0, false, 0.5F);
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
                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.6f, 0, false, 0.5F);
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
                this.setFightMode(false);
                this.setDead();
            }, 85);
        } else {
            animator.setStateAnim(ANIM_SWING);
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
                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.6f, 0, false, 0.5F);
                this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
            }, 23);

            addEvent(()-> {
                this.setImmovable(true);
                this.lockLook = false;
            }, 30);

            addEvent(()-> {
                this.setImmovable(false);
                this.setFightMode(false);
                this.setDead();
            }, 50);
        }
    };


    private final Consumer<EntityLivingBase> jab_attack = (target) -> {
        this.setFightMode(true);
        this.setImmovable(true);
        animator.setStateAnim(ANIM_JAB);

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
            ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.4f, 0, false, 0.7F);
            this.playSound(SoundsHandler.APATHYR_SWING, 0.8f, 0.7f / (rand.nextFloat() * 0.4f + 0.2f));
        }, 30);

        addEvent(()-> this.setImmovable(true), 35);

        addEvent(()-> {
            this.lockLook = false;
            this.setImmovable(false);
            this.setFightMode(false);
            this.setDead();
        }, 55);
    };

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        super.entityInit();
    }


    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }


  @Override
  public void updateAnimations() {

  }
}
