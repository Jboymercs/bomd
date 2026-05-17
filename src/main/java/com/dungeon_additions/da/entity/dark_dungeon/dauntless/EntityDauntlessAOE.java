package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import net.minecraft.entity.SharedMonsterAttributes;
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

public class EntityDauntlessAOE extends EntityDarkBase implements IAnimatable, IAnimationTickable {

    private final String ANIM_IDLE = "idle";
    private AnimationFactory factory = new AnimationFactory(this);
    protected static final DataParameter<Integer> TIME_ALIVE = EntityDataManager.createKey(EntityDauntlessAOE.class, DataSerializers.VARINT);
    private float hitBoxWidth = 1;
    public void setTimeAlive(int skinType)
    {
        this.dataManager.set(TIME_ALIVE, Integer.valueOf(skinType));
    }
    public int getTimeAlive()
    {
        return this.dataManager.get(TIME_ALIVE).intValue();
    }

    public EntityDauntlessAOE(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.0F, 1.5F);
        this.setTimeAlive(40);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public EntityDauntlessAOE(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.5F);
        this.setTimeAlive(40);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public EntityDauntlessAOE(World worldIn, int timeAlive, float hitBoxSize) {
        super(worldIn);
        this.setSize(hitBoxSize, 1.5F);
        this.setTimeAlive(timeAlive);
        this.hitBoxWidth = hitBoxSize;
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Time_Alive", this.getTimeAlive());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setTimeAlive(nbt.getInteger("Time_Alive"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(TIME_ALIVE, 40);
        super.entityInit();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(this.getTimeAlive() == 4) {
            Main.proxy.spawnParticle(35, world, this.posX, this.posY + 0.3F, this.posZ, 0,0,0);
        }
        if(this.getTimeAlive() > 0) {
            this.setTimeAlive(this.getTimeAlive() - 1);
        } else {
            //do damage
            Vec3d offset = this.getPositionVector().add(ModUtils.yVec(0.25D));
            DamageSource source;
            source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MAGIC).directEntity(this).build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(this.hitBoxWidth * 0.6F, (e) -> damage, this, offset, source, 0.15f, 0, false, 0.4F);
            //play particles and die
            this.setDead();
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(14D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
