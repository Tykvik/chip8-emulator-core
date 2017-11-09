package com.github.chip.emulator.core.services;

import com.google.common.eventbus.EventBus;

/**
 * @author helloween
 */
public class EventService {
    private static final EventService INSTANCE = new EventService();

    private EventBus eventBus;

    private EventService() {
        eventBus = new EventBus();
    }

    public static EventService getInstance() {
        return INSTANCE;
    }

    public void registerHandler(Object handler) {
        eventBus.register(handler);
    }

    public void postEvent(Object event) {
        eventBus.post(event);
    }
}
