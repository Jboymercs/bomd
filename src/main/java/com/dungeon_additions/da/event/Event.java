package com.dungeon_additions.da.event;

import java.util.function.Supplier;

public class Event implements IEvent{
    private final Supplier<Boolean> condition;
    private final Supplier<Boolean> shouldCancel;
    private final Runnable callback;
    private final int tickSize;

    public Event(Supplier<Boolean> condition, Runnable callback, Supplier<Boolean> shouldCancel, int tickSize) {
        this.condition = condition;
        this.callback = callback;
        this.shouldCancel = shouldCancel;
        this.tickSize = tickSize;
    }

    public Event(Supplier<Boolean> condition, Runnable callback, Supplier<Boolean> shouldCancel) {
        this(condition, callback, shouldCancel, 1);
    }

    @Override
    public boolean shouldDoEvent() {
        return condition.get();
    }

    @Override
    public void doEvent() {
        callback.run();
    }

    @Override
    public boolean shouldRemoveEvent() {
        return shouldCancel.get();
    }

    @Override
    public int tickSize() {
        return tickSize;
    }
}
