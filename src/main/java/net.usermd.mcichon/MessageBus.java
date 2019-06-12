package net.usermd.mcichon;

import java.util.HashMap;

public interface MessageBus {
    SubscriberBuilder subscribeFor(String subject);
    MessageBuilder message(String subject);
    void closeBus();

    static MessageBus create(int threads) {
        EventBus eventBus = new EventBus();
        eventBus.setActions(new HashMap<>());
        eventBus.setThreads(threads);
        return eventBus;
    }
}
