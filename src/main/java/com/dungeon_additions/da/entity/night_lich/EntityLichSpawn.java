package com.dungeon_additions.da.entity.night_lich;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.lang.ref.WeakReference;
import java.util.AbstractSequentialList;
import java.util.Objects;

public class EntityLichSpawn extends EntityAbstractBase implements IAnimatable, IAnimationTickable {

    private AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_IDLE = "idle";
    public EntityLichSpawn(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.8F, 0.8F);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public EntityLichSpawn(World worldIn) {
        super(worldIn);
        this.setSize(0.8F, 0.8F);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
    }

    private EntityNightLich owner;

    public EntityLichSpawn(World worldIn, EntityNightLich owner) {
        super(worldIn);
        this.setSize(0.8F, 0.8F);
        this.noClip = true;
        this.setImmovable(true);
        this.setNoAI(true);
        this.owner = owner;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(ticksExisted == 2) {
            this.playSound(SoundsHandler.LICH_MINION_RUNE, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        }

        if(ticksExisted == 25) {
            this.playSound(SoundsHandler.LICH_SUMMON_MINION, 1.0f, 0.8f / (rand.nextFloat() * 0.4f + 0.6f));
        }

        if(ticksExisted == 30 && owner != null) {
            //summon mob and set dead
            double currHealth = owner.getHealth() / owner.getMaxHealth();
            Entity entityToo;

            if(currHealth <= 0.5) {
               entityToo = Objects.requireNonNull(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(ModRand.choice(MobConfig.mob_list_two)))).newInstance(world);
            } else {
                entityToo = Objects.requireNonNull(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(ModRand.choice(MobConfig.mob_list_one)))).newInstance(world);
            }

            if(entityToo instanceof EntityLivingBase) {
                EntityLivingBase spawn = ((EntityLivingBase) entityToo);

                if(spawn instanceof EntityMob) {
                    EntityMob spawnMob = ((EntityMob) spawn);

                    if(spawnMob instanceof AbstractSkeleton) {
                        if(MobConfig.lich_enable_gear && world.rand.nextInt(3) == 0) {
                            spawnMob.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                            spawnMob.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
                        } else {
                            spawnMob.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                        }
                    }

                    if(MobConfig.lich_enable_gear) {
                        if (spawnMob instanceof EntityZombie) {
                            spawnMob.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                            spawnMob.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                        }
                    }

                    spawnMob.setPosition(this.posX, this.posY, this.posZ);
                    if(owner != null) {
                        owner.current_mobs.add(new WeakReference<>(spawnMob));
                    }
                    world.spawnEntity(spawnMob);
                } else {
                    //if not of a mob still spawn it and add it as a reference
                    spawn.setPosition(this.posX, this.posY, this.posZ);
                    if(owner != null) {
                        owner.current_mobs.add(new WeakReference<>(spawn));
                    }
                    world.spawnEntity(spawn);
                }
            }

        }

        if(ticksExisted == 31) {
            this.setDead();
        }
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
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
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
