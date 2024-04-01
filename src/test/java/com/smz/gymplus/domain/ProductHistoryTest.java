package com.smz.gymplus.domain;

import static com.smz.gymplus.domain.ProductHistoryTestSamples.*;
import static com.smz.gymplus.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smz.gymplus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductHistory.class);
        ProductHistory productHistory1 = getProductHistorySample1();
        ProductHistory productHistory2 = new ProductHistory();
        assertThat(productHistory1).isNotEqualTo(productHistory2);

        productHistory2.setId(productHistory1.getId());
        assertThat(productHistory1).isEqualTo(productHistory2);

        productHistory2 = getProductHistorySample2();
        assertThat(productHistory1).isNotEqualTo(productHistory2);
    }

    @Test
    void productTest() throws Exception {
        ProductHistory productHistory = getProductHistoryRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productHistory.setProduct(productBack);
        assertThat(productHistory.getProduct()).isEqualTo(productBack);

        productHistory.product(null);
        assertThat(productHistory.getProduct()).isNull();
    }
}
