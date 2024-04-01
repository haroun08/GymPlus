package com.smz.gymplus.domain;

import static com.smz.gymplus.domain.CategoryTestSamples.*;
import static com.smz.gymplus.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smz.gymplus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void nameTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        category.addName(productBack);
        assertThat(category.getNames()).containsOnly(productBack);
        assertThat(productBack.getCategory()).isEqualTo(category);

        category.removeName(productBack);
        assertThat(category.getNames()).doesNotContain(productBack);
        assertThat(productBack.getCategory()).isNull();

        category.names(new HashSet<>(Set.of(productBack)));
        assertThat(category.getNames()).containsOnly(productBack);
        assertThat(productBack.getCategory()).isEqualTo(category);

        category.setNames(new HashSet<>());
        assertThat(category.getNames()).doesNotContain(productBack);
        assertThat(productBack.getCategory()).isNull();
    }
}
