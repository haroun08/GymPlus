package com.smz.gymplus.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Invoice getInvoiceSample1() {
        return new Invoice().id(1L);
    }

    public static Invoice getInvoiceSample2() {
        return new Invoice().id(2L);
    }

    public static Invoice getInvoiceRandomSampleGenerator() {
        return new Invoice().id(longCount.incrementAndGet());
    }
}
