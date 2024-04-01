package com.smz.gymplus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProductHistory getProductHistorySample1() {
        return new ProductHistory().id(1L).name("name1").description("description1").availableStockQuantity(1L);
    }

    public static ProductHistory getProductHistorySample2() {
        return new ProductHistory().id(2L).name("name2").description("description2").availableStockQuantity(2L);
    }

    public static ProductHistory getProductHistoryRandomSampleGenerator() {
        return new ProductHistory()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .availableStockQuantity(longCount.incrementAndGet());
    }
}
