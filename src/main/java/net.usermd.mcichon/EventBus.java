package net.usermd.mcichon;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class EventBus implements MessageBus {
    private ExecutorService pool;
    private Map<String, List<Action>> actions;

    void setActions(Map<String, List<Action>> actions) {
        this.actions = actions;
    }

    void setThreads(int threads) {
        if (threads <= 0) {
            throw new IllegalArgumentException("Negative threads number");
        }
        this.pool = Executors.newFixedThreadPool(threads);
    }

    @Override
    public SubscriberBuilder subscribeFor(String subject) {
        return new SubscriberBuilder(this, subject);
    }

    @Override
    public MessageBuilder message(String subject) {
        return new MessageBuilder(this, subject);
    }

    void addSubscription(String subject, Action action) {
        actions.computeIfAbsent(subject, s -> new LinkedList<>()).add(action);
    }

    void removeSubscription(String subject, Action action) {
        actions.get(subject).remove(action);
    }

    void processMessage(String subject) {
        pool.submit(() -> {
            actions.get(subject).stream()
                    .<Runnable>map(action -> action::execute)
                    .forEach(pool::submit);
        });
    }

    @Override
    public void closeBus() {
        pool.shutdownNow();
    }
}
