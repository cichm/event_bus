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

    def 'should be able to multi increment with unsubscribe between second sent action and get number one'() {
        given:
        Subscription subscription = this.eventBus.subscribeFor(this.subject)
                .then(this.x.incrementAndGet() as Action)
                .subscribe()
        when:
        this.eventBus.message(this.subject).send()
        and:
        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .until({this.x.get() == 1})
        and:
        subscription.unsubscribe()
        and:
        this.eventBus.message(this.subject).send()
        then:
        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .until({this.x.get() == 1})
    }

    def cleanupSpec() {
        this.eventBus.closeBus()
    }
}
