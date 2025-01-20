package com.dungeon_additions.da.entity.frost_dungeon.great_wyrk;

public interface IMultiAction {
    void doAction();
    default void update() {
    }
    default boolean shouldExplodeUponImpact() {
        return false;
    }
    default boolean isImmuneToDamage() {
        return false;
    }
    default int attackLength() {
        return 100;
    }
    default int attackCooldown() {
        return 50;
    }
}
