package com.dungeon_additions.da.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventScheduler {
    private int ticks = 0;
    private final List<IEvent> eventQueue = new ArrayList<>();
    private final Set<IEvent> eventsToAdd = new HashSet<>();

    public void updateEvents() {
        eventQueue.addAll(eventsToAdd);
        eventsToAdd.clear();

        for (IEvent iEvent : eventQueue) {
            if (ticks % iEvent.tickSize() == 0 && iEvent.shouldDoEvent())
                iEvent.doEvent();
        }
        eventQueue.removeIf(IEvent::shouldRemoveEvent);

        this.ticks++;
    }

    public void addEvent(IEvent event) {
        eventsToAdd.add(event);
    }
}
