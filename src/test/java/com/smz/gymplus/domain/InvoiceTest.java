package com.smz.gymplus.domain;

import static com.smz.gymplus.domain.InvoiceTestSamples.*;
import static com.smz.gymplus.domain.PaymentTestSamples.*;
import static com.smz.gymplus.domain.PeriodicSubscriptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smz.gymplus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = getInvoiceSample1();
        Invoice invoice2 = new Invoice();
        assertThat(invoice1).isNotEqualTo(invoice2);

        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);

        invoice2 = getInvoiceSample2();
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    void idTest() throws Exception {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        PeriodicSubscription periodicSubscriptionBack = getPeriodicSubscriptionRandomSampleGenerator();

        invoice.addId(periodicSubscriptionBack);
        assertThat(invoice.getIds()).containsOnly(periodicSubscriptionBack);
        assertThat(periodicSubscriptionBack.getInvoices()).isEqualTo(invoice);

        invoice.removeId(periodicSubscriptionBack);
        assertThat(invoice.getIds()).doesNotContain(periodicSubscriptionBack);
        assertThat(periodicSubscriptionBack.getInvoices()).isNull();

        invoice.ids(new HashSet<>(Set.of(periodicSubscriptionBack)));
        assertThat(invoice.getIds()).containsOnly(periodicSubscriptionBack);
        assertThat(periodicSubscriptionBack.getInvoices()).isEqualTo(invoice);

        invoice.setIds(new HashSet<>());
        assertThat(invoice.getIds()).doesNotContain(periodicSubscriptionBack);
        assertThat(periodicSubscriptionBack.getInvoices()).isNull();
    }

    @Test
    void paymentTest() throws Exception {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        invoice.addPayment(paymentBack);
        assertThat(invoice.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getInvoice()).isEqualTo(invoice);

        invoice.removePayment(paymentBack);
        assertThat(invoice.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getInvoice()).isNull();

        invoice.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(invoice.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getInvoice()).isEqualTo(invoice);

        invoice.setPayments(new HashSet<>());
        assertThat(invoice.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getInvoice()).isNull();
    }
}
