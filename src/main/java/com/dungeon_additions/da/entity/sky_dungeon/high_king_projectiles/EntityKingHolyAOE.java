package com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
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

import java.util.List;

public class EntityKingHolyAOE extends EntitySkyBase implements IAnimatable, IAnimationTickable {
    private AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_SHOOT = "summon";

    private final String ANIM_SHOOT_2 = "summon_1";
    private final String ANIM_SHOOT_3 = "summon_2";
    private final String ANIM_SHOOT_4 = "summon_3";
    private final String ANIM_SHOOT_5 = "summon_4";

    protected int selection = ModRand.range(1, 5);

    private String ANIM_SELECTION_STRING;

    public EntityKingHolyAOE(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 1.7F);
        this.noClip = true;
        selectAnimationTooPlay();
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public EntityKingHolyAOE(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 1.7F);
        this.noClip = true;
        selectAnimationTooPlay();
        this.setImmovable(true);
        this.setNoAI(true);
    }

    private EntityPlayer ownerFrom;

    public EntityKingHolyAOE(World worldIn, EntityPlayer owner) {
        super(worldIn);
        this.setSize(0.7F, 1.7F);
        this.noClip = true;
        this.ownerFrom = owner;
        selectAnimationTooPlay();
        this.setImmovable(true);
        this.setNoAI(true);
    }

    public void selectAnimationTooPlay() {
        if(selection == 1) {
            ANIM_SELECTION_STRING = ANIM_SHOOT_2;
        } else if (selection == 2) {
            ANIM_SELECTION_STRING = ANIM_SHOOT_3;
        }else if(selection == 3) {
            ANIM_SELECTION_STRING = ANIM_SHOOT_4;
        }else if(selection == 4) {
            ANIM_SELECTION_STRING = ANIM_SHOOT_5;
        } else {
            ANIM_SELECTION_STRING = ANIM_SHOOT;
        }
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

        if(ticksExisted == 2) {
         //   this.playSound(SoundsHandler.HOLY_SPIKE_SUMMON, 0.5f, 0.8f / (rand.nextFloat() * 0.4f + 0.2f));
        }
        if(!world.isRemote) {
            if (this.ticksExisted == 15) {
                if(ownerFrom != null) {
                    List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityPlayer)));

                    if (!targets.isEmpty()) {
                        for (EntityLivingBase base : targets) {
                            if (base != this && base != ownerFrom && !(base instanceof EntityKingHolyAOE)) {
                                boolean hasHelmet = ownerFrom.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.KINGS_HELMET;
                                Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.0D));
                                DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.PLAYER).directEntity(ownerFrom).build();
                                float damage = hasHelmet ? 24 : 15;
                                ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
                            }
                        }

                    }
                } else {
                    List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntitySkyBase)));

                    if (!targets.isEmpty()) {
                        for (EntityLivingBase base : targets) {
                            if (!(base instanceof EntitySkyBase)) {
                                Vec3d offset = base.getPositionVector().add(ModUtils.yVec(1.0D));
                                DamageSource source = ModDamageSource.builder().disablesShields().type(ModDamageSource.MOB).directEntity(this).build();
                                float damage = this.getAttack();
                                ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, offset, source, 0.2f, 0, false);
                            }
                        }

                    }
                }
            }
        }

        if (this.ticksExisted >= 44) {
            this.setDead();
        }

    }
    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.high_dragon_king_damage);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "spike_controller", 0, this::predicateIdle));
    }

    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SELECTION_STRING, false));
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
}
