package net.usermd.mcichon;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final String SUBJECT = "Foo";

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger x = new AtomicInteger(0);

        MessageBus eventBus = MessageBus.create(2);
        Subscription subscription = eventBus.subscribeFor(SUBJECT).then(x::incrementAndGet).subscribe();
        eventBus.message("Foo").send();

        Thread.sleep(1000);

        //Awaitility
        System.out.println(x);

        subscription.unsubscribe();

        eventBus.message("Foo").send();

        Thread.sleep(1000);

        System.out.println(x);

        eventBus.closeBus();
    }
}
