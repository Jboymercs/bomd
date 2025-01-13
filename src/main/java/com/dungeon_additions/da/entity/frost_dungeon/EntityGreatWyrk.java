package com.dungeon_additions.da.entity.frost_dungeon;

import com.dungeon_additions.da.entity.ai.IAttack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
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
import java.util.function.Consumer;

public class EntityGreatWyrk extends EntityAbstractGreatWyrk implements IAnimatable, IAnimationTickable, IAttack {

    private final String ANIM_WALK = "walk";
    private final String ANIM_IDLE = "idle";

    private final String ANIM_MEGA_STOMP = "mega_stomp";
    private final String ANIM_ROLL = "roll";
    private final String ANIM_MELEE_STRIKE = "melee_strike";
    private final String ANIM_SHAKE = "shake";
    private final String ANIM_SMALL_STOMPS = "small_stomps";
    private final String ANIM_DROP = "drop";
    private final String ANIM_LAZER_SHOOT = "lazer_shoot";
    private final String ANIM_SUMMON_AID = "summon_aid";
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.NOTCHED_6));

    private final AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttacks;


    public EntityGreatWyrk(World worldIn) {
        super(worldIn);
        this.iAmBossMob = true;
        this.experienceValue = 200;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        this.bossInfo.setPercent(getHealth() / getMaxHealth());

    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        return 0;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
    }

    private <E extends IAnimatable> PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isMegaStomp()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MEGA_STOMP, false));
                return PlayState.CONTINUE;
            }
            if(this.isRoll()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ROLL, false));
                return PlayState.CONTINUE;
            }
            if(this.isMeleeStrike()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MELEE_STRIKE, false));
                return PlayState.CONTINUE;
            }
            if(this.isShake()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHAKE, false));
                return PlayState.CONTINUE;
            }
            if(this.isSmallStomps()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SMALL_STOMPS, false));
                return PlayState.CONTINUE;
            }
            if(this.isDropAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DROP, false));
                return PlayState.CONTINUE;
            }
            if(this.isLazerAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LAZER_SHOOT, false));
                return PlayState.CONTINUE;
            }
            if(this.isSummonAid()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_AID, false));
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

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    /**
     * Add a bit of brightness to the entity for it's Arena
     */
    @Override
    public int getBrightnessForRender() {
        return Math.min(super.getBrightnessForRender() + 30, 200);
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
}
