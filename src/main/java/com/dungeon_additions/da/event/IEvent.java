package com.dungeon_additions.da.event;

public interface IEvent {
    boolean shouldDoEvent();
    void doEvent();
    boolean shouldRemoveEvent();
    int tickSize();
}
