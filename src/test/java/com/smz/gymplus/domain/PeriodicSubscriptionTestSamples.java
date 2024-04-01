package com.smz.gymplus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PeriodicSubscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PeriodicSubscription getPeriodicSubscriptionSample1() {
        return new PeriodicSubscription().id(1L).name("name1").description("description1");
    }

    public static PeriodicSubscription getPeriodicSubscriptionSample2() {
        return new PeriodicSubscription().id(2L).name("name2").description("description2");
    }

    public static PeriodicSubscription getPeriodicSubscriptionRandomSampleGenerator() {
        return new PeriodicSubscription()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
