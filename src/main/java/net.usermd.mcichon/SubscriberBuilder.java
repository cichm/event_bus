package net.usermd.mcichon;
import java.util.Objects;

public class SubscriberBuilder<T> {
    private EventBus eventBus;
    private String subject;
    private Action action;

    SubscriberBuilder(EventBus eventBus, String subject) {
        this.eventBus = eventBus;
        this.subject = subject;
    }

    public SubscriberBuilder then(Action subscriber) {
        Objects.requireNonNull(subscriber, "subscriber");
        this.action = subscriber;
        return this;
    }

    public Subscription subscribe() {
        eventBus.addSubscription(this.subject, this.action);
        return () -> eventBus.removeSubscription(this.subject, this.action);
    }
}
