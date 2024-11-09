package com.dungeon_additions.da.event;

import java.util.function.Supplier;

public class TimedEvent implements IEvent{
    private final Runnable callback;
    private final Supplier<Boolean> shouldCancel;
    private final int delay;
    private final int duration;
    private int age;

    public TimedEvent(Runnable callback, int delay, int duration, Supplier<Boolean> shouldCancel) {
        this.callback = callback;
        this.delay = delay;
        this.duration = duration;
        this.shouldCancel = shouldCancel;
        this.age = 0;
    }

    public TimedEvent(Runnable callback, int delay, int duration) {
        this(callback, delay, duration, () -> false);
    }

    public TimedEvent(Runnable callback, int delay, Supplier<Boolean> shouldCancel) {
        this(callback, delay, 1, shouldCancel);
    }

    public TimedEvent(Runnable callback, int delay) {
        this(callback, delay, 1, () -> false);
    }

    @Override
    public boolean shouldDoEvent() {
        return age++ >= delay && !shouldCancel.get();
    }

    @Override
    public void doEvent() {
        callback.run();
    }

    @Override
    public boolean shouldRemoveEvent() {
        return shouldCancel.get() || age >= delay + duration;
    }

    @Override
    public int tickSize() {
        return 1;
    }
}
