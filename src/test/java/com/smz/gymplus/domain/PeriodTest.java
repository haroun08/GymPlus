package com.smz.gymplus.domain;

import static com.smz.gymplus.domain.PeriodTestSamples.*;
import static com.smz.gymplus.domain.PlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smz.gymplus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PeriodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Period.class);
        Period period1 = getPeriodSample1();
        Period period2 = new Period();
        assertThat(period1).isNotEqualTo(period2);

        period2.setId(period1.getId());
        assertThat(period1).isEqualTo(period2);

        period2 = getPeriodSample2();
        assertThat(period1).isNotEqualTo(period2);
    }

    @Test
    void idTest() throws Exception {
        Period period = getPeriodRandomSampleGenerator();
        Plan planBack = getPlanRandomSampleGenerator();

        period.addId(planBack);
        assertThat(period.getIds()).containsOnly(planBack);
        assertThat(planBack.getPeriods()).isEqualTo(period);

        period.removeId(planBack);
        assertThat(period.getIds()).doesNotContain(planBack);
        assertThat(planBack.getPeriods()).isNull();

        period.ids(new HashSet<>(Set.of(planBack)));
        assertThat(period.getIds()).containsOnly(planBack);
        assertThat(planBack.getPeriods()).isEqualTo(period);

        period.setIds(new HashSet<>());
        assertThat(period.getIds()).doesNotContain(planBack);
        assertThat(planBack.getPeriods()).isNull();
    }
}
