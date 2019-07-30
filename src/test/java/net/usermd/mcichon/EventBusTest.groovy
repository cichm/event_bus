package net.usermd.mcichon

import org.awaitility.Awaitility
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class EventBusTest extends Specification {

    @Shared
    private String subject
    @Shared
    private AtomicInteger x;
    @Shared
    private MessageBus eventBus;

    def setupSpec() {
        this.subject = "Foo"
        this.x = new AtomicInteger(0);
        this.eventBus = MessageBus.create(2);
    }

    def 'should be able to increment and get number one'() {
        given:
        Subscription subscription = eventBus.subscribeFor(this.subject).then(x.incrementAndGet() as Action).subscribe()
        when:
        eventBus.message("Foo").send()
        and:
        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .until({x.get() == 1})
        and:
        subscription.unsubscribe()
        and:
        eventBus.message("Foo").send()
        then:
        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .until({x.get() == 1})
    }

    def cleanupSpec() {
        this.eventBus.closeBus()
    }
}
