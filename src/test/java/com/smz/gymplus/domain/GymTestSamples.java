package com.smz.gymplus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GymTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Gym getGymSample1() {
        return new Gym()
            .id(1L)
            .name("name1")
            .streetAddress("streetAddress1")
            .postalCode("postalCode1")
            .city("city1")
            .stateProvince("stateProvince1")
            .phoneNumber(1);
    }

    public static Gym getGymSample2() {
        return new Gym()
            .id(2L)
            .name("name2")
            .streetAddress("streetAddress2")
            .postalCode("postalCode2")
            .city("city2")
            .stateProvince("stateProvince2")
            .phoneNumber(2);
    }

    public static Gym getGymRandomSampleGenerator() {
        return new Gym()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .streetAddress(UUID.randomUUID().toString())
            .postalCode(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .stateProvince(UUID.randomUUID().toString())
            .phoneNumber(intCount.incrementAndGet());
    }
}
