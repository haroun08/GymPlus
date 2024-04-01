package com.smz.gymplus.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PeriodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Period getPeriodSample1() {
        return new Period().id(1L).monthOccurrence(1).dayOccurrence(1);
    }

    public static Period getPeriodSample2() {
        return new Period().id(2L).monthOccurrence(2).dayOccurrence(2);
    }

    public static Period getPeriodRandomSampleGenerator() {
        return new Period()
            .id(longCount.incrementAndGet())
            .monthOccurrence(intCount.incrementAndGet())
            .dayOccurrence(intCount.incrementAndGet());
    }
}
