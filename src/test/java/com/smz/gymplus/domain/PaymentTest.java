package com.smz.gymplus.domain;

import static com.smz.gymplus.domain.InvoiceTestSamples.*;
import static com.smz.gymplus.domain.OrderTestSamples.*;
import static com.smz.gymplus.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smz.gymplus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void invoiceTest() throws Exception {
        Payment payment = getPaymentRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        payment.setInvoice(invoiceBack);
        assertThat(payment.getInvoice()).isEqualTo(invoiceBack);

        payment.invoice(null);
        assertThat(payment.getInvoice()).isNull();
    }

    @Test
    void orderTest() throws Exception {
        Payment payment = getPaymentRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        payment.setOrder(orderBack);
        assertThat(payment.getOrder()).isEqualTo(orderBack);

        payment.order(null);
        assertThat(payment.getOrder()).isNull();
    }
}
