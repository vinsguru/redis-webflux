package com.vinsguru.redisson.test;

import org.junit.jupiter.api.Test;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

public class Lec15SortedSetTest extends BaseTest {

    @Test
    public void sortedSet(){
        RScoredSortedSetReactive<String> sortedSet = this.client.getScoredSortedSet("student:score", StringCodec.INSTANCE);

        Mono<Void> mono = sortedSet.addScore("sam", 12.25)
                .then(sortedSet.add(23.25, "mike"))
                .then(sortedSet.addScore("jake", 7))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        sortedSet.entryRange(0, 1)
                .flatMapIterable(Function.identity()) // flux
                .map(se -> se.getScore() + " : " + se.getValue())
                .doOnNext(System.out::println)
                .subscribe();

        sleep(1000);


    }

    @Test
    public void test(){
        RScoredSortedSetReactive<String> sortedSet = this.client.getScoredSortedSet("prod:score", StringCodec.INSTANCE);
        Map<String, Long> a = Map.of(
                "a", 10L,
                "b", 15L
        );
        RBatchReactive batch = this.client.createBatch(BatchOptions.defaults());
        RScoredSortedSetReactive<String> set = batch.getScoredSortedSet("prod:score", StringCodec.INSTANCE);

        Flux.fromIterable(a.entrySet())
                .doFinally(s -> System.out.println("done1"))
                .doOnNext(System.out::println)
                .map(e -> set.addScore(e.getKey(), e.getValue()))
                .then(batch.execute())
                .doOnNext(r -> System.out.println(r.getResponses()))
                .subscribe();

        Flux.interval(Duration.ofSeconds(3))
                .buffer(Duration.ofSeconds(1))
                .subscribe(System.out::println);
        sleep(10_000);
    }
}
