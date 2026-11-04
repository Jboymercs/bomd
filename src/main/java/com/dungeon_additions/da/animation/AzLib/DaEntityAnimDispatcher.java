package com.dungeon_additions.da.animation.AzLib;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import mod.azure.azurelib.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.animation.dispatch.command.sequence.AzSequence;
import mod.azure.azurelib.animation.play_behavior.AzPlayBehavior;
import mod.azure.azurelib.animation.play_behavior.AzPlayBehaviors;

/**
 * Base class for calling animations from the entity class for mobs.
 */
public class DaEntityAnimDispatcher {

    private final String ANIM_IDLE = "idle";
    private final String ANIM_WALK = "walk";

    private final String ANIM_UPPER_IDLE = "idle_upper";
    private final String ANIM_LOWER_IDLE = "idle_lower";
    private final String ANIM_UPPER_WALK = "walk_upper";
    private final String ANIM_LOWER_WALK = "walk_lower";

    private final AzCommand idleMode = AzCommand.create(DaReferenceStrings.IDLE_FULL_CTR, ANIM_IDLE, AzPlayBehaviors.LOOP);
    private final AzCommand walkMode = AzCommand.create(DaReferenceStrings.WALK_FULL_CTR, ANIM_WALK, AzPlayBehaviors.LOOP);

    private final AzCommand walkUpperMode = AzCommand.create(DaReferenceStrings.WALK_UPPER_CTR, ANIM_UPPER_WALK, AzPlayBehaviors.LOOP);
    private final AzCommand walkLowerMode = AzCommand.create(DaReferenceStrings.WALK_LOWER_CTR, ANIM_LOWER_WALK, AzPlayBehaviors.LOOP);
    private final AzCommand idleUpperMode = AzCommand.create(DaReferenceStrings.IDLE_UPPER_CTR, ANIM_UPPER_IDLE, AzPlayBehaviors.LOOP);
    private final AzCommand idleLowerMode = AzCommand.create(DaReferenceStrings.IDLE_LOWER_CTR, ANIM_LOWER_IDLE, AzPlayBehaviors.LOOP);

    private final EntityAbstractBase mob;
    private boolean hasUpperLower;

    public DaEntityAnimDispatcher(EntityAbstractBase mob, boolean hasUpperLower) {
        this.mob = mob;
        this.hasUpperLower = hasUpperLower;
    }

    public void setCustomUpperIdle(String animationName) {
        AzCommand command = AzCommand.create(DaReferenceStrings.IDLE_UPPER_CTR, animationName, AzPlayBehaviors.LOOP);
        command.sendForEntity(mob);
    }

    public void setCustomLowerIdle(String animationName) {
        AzCommand command = AzCommand.create(DaReferenceStrings.IDLE_LOWER_CTR, animationName, AzPlayBehaviors.LOOP);
        command.sendForEntity(mob);
    }

    public void setCustomUpperWalk(String animationName) {
        AzCommand command = AzCommand.create(DaReferenceStrings.WALK_UPPER_CTR, animationName, AzPlayBehaviors.LOOP);
        command.sendForEntity(mob);
    }

    public void setCustomLowerWalk(String animationName) {
        AzCommand command = AzCommand.create(DaReferenceStrings.WALK_LOWER_CTR, animationName, AzPlayBehaviors.LOOP);
        command.sendForEntity(mob);
    }

    public void setSpellIdle(String animationName) {
        AzCommand command = AzCommand.create(DaReferenceStrings.IDLE_FULL_CTR, animationName, AzPlayBehaviors.LOOP);
    }

    public void handleIdleStates(boolean walking) {
        if(hasUpperLower) {
            if(!mob.isFullBodyUsage()) {
                if (walking) {
                    //utilizes just the upper half when not attacking
                    if (!mob.isFightMode()) {
                        walkUpperMode.sendForEntity(mob);
                    }
                    //utilizes all the body
                        walkLowerMode.sendForEntity(mob);

                } else {
                    if(!mob.isFightMode()) {
                        idleUpperMode.sendForEntity(mob);
                    }
                    idleLowerMode.sendForEntity(mob);
                }
            }
        } else {
            if(walking) {
                walkMode.sendForEntity(mob);
            } else {
                idleMode.sendForEntity(mob);
            }
        }
    }


    //This is for entities that have like a rotating part that doesn't interact with combat or other states.
    public void handleIdleSecondary(String animationName) {
        if(hasUpperLower) {
            AzCommand command = AzCommand.create(DaReferenceStrings.IDLE_FULL_CTR, animationName, AzPlayBehaviors.LOOP);
            command.sendForEntity(mob);
        } else {
            AzCommand command = AzCommand.create(DaReferenceStrings.IDLE_UPPER_CTR, animationName, AzPlayBehaviors.LOOP);
            command.sendForEntity(mob);
        }
    }


    /**
     * A list of various callings from different controllers for the mobs.
     * @param animationName
     */
    public void performAttack(String animationName) {
        AzCommand command = AzCommand.create(DaReferenceStrings.ATTACK_CTR, animationName, AzPlayBehaviors.PLAY_ONCE);
        command.sendForEntity(mob);
    }

    public void performAttack(String animationName, boolean loop) {
        AzCommand command = AzCommand.create(DaReferenceStrings.ATTACK_CTR, animationName, loop ? AzPlayBehaviors.LOOP : AzPlayBehaviors.PLAY_ONCE);
        command.sendForEntity(mob);
    }

    public void performAttack(String animationName, double animationSpeed) {
        AzSequence sequence = AzSequence.builder().then(animationName, AzPlayBehaviors.PLAY_ONCE, props -> props.withAnimationSpeed(animationSpeed)).build();
        AzCommand command = AzCommand.create(DaReferenceStrings.ATTACK_CTR, sequence);
        command.sendForEntity(mob);
    }

    public void performCombo(String animationName) {
        AzCommand command = AzCommand.create(DaReferenceStrings.COMBO_CTR, animationName + "t", AzPlayBehaviors.PLAY_ONCE);
        command.sendForEntity(mob);
    }

    public void performCombo(String animationName, int tickOffset, int transitionTicks) {
        AzSequence sequence = AzSequence.builder().then(animationName, AzPlayBehaviors.PLAY_ONCE, props -> props.withStartTickOffset(tickOffset).withTransitionLength(transitionTicks)).build();
        AzCommand command = AzCommand.create(DaReferenceStrings.COMBO_CTR, sequence);
        command.sendForEntity(mob);
    }

    public void performCombo(String animationName, boolean loop) {
        AzCommand command = AzCommand.create(DaReferenceStrings.COMBO_CTR, animationName + "t", loop ? AzPlayBehaviors.LOOP : AzPlayBehaviors.PLAY_ONCE);
        command.sendForEntity(mob);
    }

    public void setStateAnim(String animationName) {
        AzCommand command = AzCommand.create(DaReferenceStrings.STATE_CTR_ONE, animationName, AzPlayBehaviors.PLAY_ONCE);
        command.sendForEntity(mob);
    }

    public void setStateAnim(String animationName, boolean loop) {
        AzCommand command = AzCommand.create(DaReferenceStrings.STATE_CTR_ONE, animationName, loop ? AzPlayBehaviors.LOOP : AzPlayBehaviors.PLAY_ONCE);
        command.sendForEntity(mob);
    }

}
