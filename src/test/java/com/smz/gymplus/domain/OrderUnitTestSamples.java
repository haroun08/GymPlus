package com.smz.gymplus.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OrderUnitTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OrderUnit getOrderUnitSample1() {
        return new OrderUnit().id(1L).quantity(1);
    }

    public static OrderUnit getOrderUnitSample2() {
        return new OrderUnit().id(2L).quantity(2);
    }

    public static OrderUnit getOrderUnitRandomSampleGenerator() {
        return new OrderUnit().id(longCount.incrementAndGet()).quantity(intCount.incrementAndGet());
    }
}
