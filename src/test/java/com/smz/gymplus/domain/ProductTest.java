package com.smz.gymplus.domain;

import static com.smz.gymplus.domain.CategoryTestSamples.*;
import static com.smz.gymplus.domain.OrderUnitTestSamples.*;
import static com.smz.gymplus.domain.ProductHistoryTestSamples.*;
import static com.smz.gymplus.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smz.gymplus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void idTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        ProductHistory productHistoryBack = getProductHistoryRandomSampleGenerator();

        product.addId(productHistoryBack);
        assertThat(product.getIds()).containsOnly(productHistoryBack);
        assertThat(productHistoryBack.getProduct()).isEqualTo(product);

        product.removeId(productHistoryBack);
        assertThat(product.getIds()).doesNotContain(productHistoryBack);
        assertThat(productHistoryBack.getProduct()).isNull();

        product.ids(new HashSet<>(Set.of(productHistoryBack)));
        assertThat(product.getIds()).containsOnly(productHistoryBack);
        assertThat(productHistoryBack.getProduct()).isEqualTo(product);

        product.setIds(new HashSet<>());
        assertThat(product.getIds()).doesNotContain(productHistoryBack);
        assertThat(productHistoryBack.getProduct()).isNull();
    }

    @Test
    void orderUnitTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        OrderUnit orderUnitBack = getOrderUnitRandomSampleGenerator();

        product.addOrderUnit(orderUnitBack);
        assertThat(product.getOrderUnits()).containsOnly(orderUnitBack);
        assertThat(orderUnitBack.getProducts()).isEqualTo(product);

        product.removeOrderUnit(orderUnitBack);
        assertThat(product.getOrderUnits()).doesNotContain(orderUnitBack);
        assertThat(orderUnitBack.getProducts()).isNull();

        product.orderUnits(new HashSet<>(Set.of(orderUnitBack)));
        assertThat(product.getOrderUnits()).containsOnly(orderUnitBack);
        assertThat(orderUnitBack.getProducts()).isEqualTo(product);

        product.setOrderUnits(new HashSet<>());
        assertThat(product.getOrderUnits()).doesNotContain(orderUnitBack);
        assertThat(orderUnitBack.getProducts()).isNull();
    }

    @Test
    void categoryTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.setCategory(categoryBack);
        assertThat(product.getCategory()).isEqualTo(categoryBack);

        product.category(null);
        assertThat(product.getCategory()).isNull();
    }
}
