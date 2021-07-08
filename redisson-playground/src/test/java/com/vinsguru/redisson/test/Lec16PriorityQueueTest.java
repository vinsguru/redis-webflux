package com.vinsguru.redisson.test;

import com.vinsguru.redisson.test.assignment.Category;
import com.vinsguru.redisson.test.assignment.PriorityQueue;
import com.vinsguru.redisson.test.assignment.UserOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec16PriorityQueueTest extends BaseTest {

    private PriorityQueue priorityQueue;

    @BeforeAll
    public void setupQueue(){
        RScoredSortedSetReactive<UserOrder> sortedSet = this.client.getScoredSortedSet("user:order:queue", new TypedJsonJacksonCodec(UserOrder.class));
        this.priorityQueue = new PriorityQueue(sortedSet);
    }

    @Test
    public void producer(){
        Flux.interval(Duration.ofSeconds(1))
                .map(l -> (l.intValue() * 5))
                .doOnNext(i -> {
                    UserOrder u1 = new UserOrder(i + 1, Category.GUEST);
                    UserOrder u2 = new UserOrder(i + 2, Category.STD);
                    UserOrder u3 = new UserOrder(i + 3, Category.PRIME);
                    UserOrder u4 = new UserOrder(i + 4, Category.STD);
                    UserOrder u5 = new UserOrder(i + 5, Category.GUEST);
                    Mono<Void> mono = Flux.just(u1, u2, u3, u4, u5)
                            .flatMap(this.priorityQueue::add)
                            .then();
                    StepVerifier.create(mono)
                            .verifyComplete();
                }).subscribe();
        sleep(60_000);
    }

    @Test
    public void consumer(){
        this.priorityQueue.takeItems()
                .delayElements(Duration.ofMillis(500))
                .doOnNext(System.out::println)
                .subscribe();
        sleep(600_000);
    }

}
