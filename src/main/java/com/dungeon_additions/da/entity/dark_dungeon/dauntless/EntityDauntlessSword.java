package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.ai.IScreenShake;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.util.DauntlessUtils;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
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

public class EntityDauntlessSword extends EntityDarkBase implements IAnimatable, IAnimationTickable, IScreenShake {

    private static final DataParameter<Boolean> LARGE_SWORD = EntityDataManager.createKey(EntityDauntlessSword.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHAKING = EntityDataManager.createKey(EntityDauntlessSword.class, DataSerializers.BOOLEAN);
    public void setLargeSword(boolean value) {this.dataManager.set(LARGE_SWORD, Boolean.valueOf(value));}
    public boolean isLargeSword() {return this.dataManager.get(LARGE_SWORD);}
    public void setShaking(boolean value) {this.dataManager.set(SHAKING, Boolean.valueOf(value));}
    public boolean isShaking() {return this.dataManager.get(SHAKING);}
    private final String ANIM_IDLE = "sword_idle";
    private final String ANIM_IDLE_LARGE = "sword_idle_large";
    private AnimationFactory factory = new AnimationFactory(this);
    private int shakeTime = 0;

    public EntityDauntlessSword(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.8F, 1.8F);
    }

    public EntityDauntlessSword(World worldIn) {
        super(worldIn);
        this.setSize(0.8F, 1.8F);
    }

    public EntityDauntlessSword(World worldIn, boolean isLarge) {
        super(worldIn);
        this.setLargeSword(isLarge);
        this.setSize(1.3F, 4.25F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Large_Sword", this.isLargeSword());
        nbt.setBoolean("Shaking", this.isShaking());
        super.writeEntityToNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.setLargeSword(nbt.getBoolean("Large_Sword"));
        this.setShaking(nbt.getBoolean("Shaking"));
        super.readEntityFromNBT(nbt);
    }

    @Override
    public void entityInit() {
        this.dataManager.register(LARGE_SWORD, Boolean.valueOf(false));
        this.dataManager.register(SHAKING, Boolean.valueOf(false));
        super.entityInit();
    }

    private int timeAlive = 35;
    private boolean iniatedAttack = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;
        this.shakeTime--;

        if(this.onGround && !world.isRemote) {
            if(this.timeAlive > 0) {
                this.timeAlive--;
            } else {
                this.setDead();
            }

            if(!this.iniatedAttack) {
                if(this.isLargeSword()) {
                    this.timeAlive = 200;
                    this.doLargeSwordAttackStart();
                } else {
                    this.doRegularSwordAttack();
                }
            } else if (this.isLargeSword() && timeAlive % 20 == 0) {
                this.doLargeSwordContinue();
            }
        }
    }

    private int largeSwordAOE = 3;

    private void doLargeSwordContinue() {
        this.setShaking(true);
        this.shakeTime = 10;
        this.playSound(SoundsHandler.LICH_MAGIC_SWING, 2.4f, 0.2f / (rand.nextFloat() * 0.4f + 0.6f));
        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0)));
        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(this).disablesShields().build();
        float damage = (float) (this.getAttack());
        ModUtils.handleAreaImpact(largeSwordAOE, (e) -> damage, this, offset, source, 0.1f, 0, false, ModPotions.HEMORRHAGE, 1, 200, 0.5F);
        Main.proxy.spawnParticle(20,world, this.posX, this.posY + 0.1, this.posZ, 0, 0, 0);
        Main.proxy.spawnParticle(20,world, this.posX, this.posY + 1.6, this.posZ, 0, 0, 0);
        Main.proxy.spawnParticle(20,world, this.posX, this.posY + 3.1, this.posZ, 0, 0, 0);
        if(this.largeSwordAOE < 6) {
            this.largeSwordAOE += 3;
        }
        addEvent(()-> this.setShaking(false), 10);
    }

    private void doLargeSwordAttackStart() {
            this.setShaking(true);
            this.shakeTime = 10;
            this.playSound(SoundsHandler.LICH_MAGIC_SWING, 2.4f, 0.2f / (rand.nextFloat() * 0.4f + 0.6f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(this).disablesShields().build();
            float damage = (float) (this.getAttack() * 1.75F);
            ModUtils.handleAreaImpact(2f, (e) -> damage, this, offset, source, 0.1f, 0, false, ModPotions.HEMORRHAGE, 1, 200, 1.0F);
        Main.proxy.spawnParticle(20,world, this.posX, this.posY + 0.1, this.posZ, 0, 0, 0);
            addEvent(()-> this.setShaking(false), 10);
            this.iniatedAttack = true;
            this.setImmovable(true);
    }

    private void doRegularSwordAttack() {
        this.iniatedAttack = true;
        this.setShaking(true);
        this.shakeTime = 10;
        new ActionDauntlessSword().performAction(this, null);
        this.playSound(SoundsHandler.LICH_MAGIC_SWING, 1.0f, 0.2f / (rand.nextFloat() * 0.4f + 0.6f));
        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.5, 0)));
        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MAGIC).directEntity(this).disablesShields().build();
        float damage = (float) (this.getAttack());
        ModUtils.handleAreaImpact(1f, (e) -> damage, this, offset, source, 0.1f, 0, false, 0.6F);
        Main.proxy.spawnParticle(22,world, this.posX, this.posY + 0.1, this.posZ, 0, 0, 0);
        addEvent(()-> {
            this.setShaking(false);
        }, 10);
        this.setImmovable(true);
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
        if(this.isLargeSword()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LARGE, true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    @Override
    public float getShakeIntensity(Entity viewer, float partialTicks) {
        if(this.isShaking()) {
            double dist = getDistance(viewer);
            float screamMult = (float) (1.0F - dist / 20.0F + 0.2F);
            if (dist >= 20.0F) {
                return 0.0F;
            }
            float isLarge = this.isLargeSword() ? 1.8F : 0.9F;
            return (float) ((Math.sin(((partialTicks)/this.shakeTime) * Math.PI) + 0.1F) * isLarge * screamMult);
        }
        return 0;
    }
}
