package com.smz.gymplus.domain;

import static com.smz.gymplus.domain.OrderTestSamples.*;
import static com.smz.gymplus.domain.OrderUnitTestSamples.*;
import static com.smz.gymplus.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smz.gymplus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderUnit.class);
        OrderUnit orderUnit1 = getOrderUnitSample1();
        OrderUnit orderUnit2 = new OrderUnit();
        assertThat(orderUnit1).isNotEqualTo(orderUnit2);

        orderUnit2.setId(orderUnit1.getId());
        assertThat(orderUnit1).isEqualTo(orderUnit2);

        orderUnit2 = getOrderUnitSample2();
        assertThat(orderUnit1).isNotEqualTo(orderUnit2);
    }

    @Test
    void orderUnitsTest() throws Exception {
        OrderUnit orderUnit = getOrderUnitRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        orderUnit.setOrderUnits(orderBack);
        assertThat(orderUnit.getOrderUnits()).isEqualTo(orderBack);

        orderUnit.orderUnits(null);
        assertThat(orderUnit.getOrderUnits()).isNull();
    }

    @Test
    void productsTest() throws Exception {
        OrderUnit orderUnit = getOrderUnitRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        orderUnit.setProducts(productBack);
        assertThat(orderUnit.getProducts()).isEqualTo(productBack);

        orderUnit.products(null);
        assertThat(orderUnit.getProducts()).isNull();
    }
}
