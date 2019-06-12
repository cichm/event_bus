package net.usermd.mcichon;

public class MessageBuilder {
    private EventBus eventBus;
    private String subject;

    MessageBuilder(EventBus eventBus, String subject) {
        this.eventBus = eventBus;
        this.subject = subject;
    }

    public void send() {
        eventBus.processMessage(subject);
    }
}
