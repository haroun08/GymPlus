package com.smz.gymplus.domain;

import static com.smz.gymplus.domain.OrderTestSamples.*;
import static com.smz.gymplus.domain.OrderUnitTestSamples.*;
import static com.smz.gymplus.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smz.gymplus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void idTest() throws Exception {
        Order order = getOrderRandomSampleGenerator();
        OrderUnit orderUnitBack = getOrderUnitRandomSampleGenerator();

        order.addId(orderUnitBack);
        assertThat(order.getIds()).containsOnly(orderUnitBack);
        assertThat(orderUnitBack.getOrderUnits()).isEqualTo(order);

        order.removeId(orderUnitBack);
        assertThat(order.getIds()).doesNotContain(orderUnitBack);
        assertThat(orderUnitBack.getOrderUnits()).isNull();

        order.ids(new HashSet<>(Set.of(orderUnitBack)));
        assertThat(order.getIds()).containsOnly(orderUnitBack);
        assertThat(orderUnitBack.getOrderUnits()).isEqualTo(order);

        order.setIds(new HashSet<>());
        assertThat(order.getIds()).doesNotContain(orderUnitBack);
        assertThat(orderUnitBack.getOrderUnits()).isNull();
    }

    @Test
    void paymentTest() throws Exception {
        Order order = getOrderRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        order.addPayment(paymentBack);
        assertThat(order.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getOrder()).isEqualTo(order);

        order.removePayment(paymentBack);
        assertThat(order.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getOrder()).isNull();

        order.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(order.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getOrder()).isEqualTo(order);

        order.setPayments(new HashSet<>());
        assertThat(order.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getOrder()).isNull();
    }
}
