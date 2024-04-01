package com.smz.gymplus.domain;

import static com.smz.gymplus.domain.PeriodTestSamples.*;
import static com.smz.gymplus.domain.PeriodicSubscriptionTestSamples.*;
import static com.smz.gymplus.domain.PlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smz.gymplus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plan.class);
        Plan plan1 = getPlanSample1();
        Plan plan2 = new Plan();
        assertThat(plan1).isNotEqualTo(plan2);

        plan2.setId(plan1.getId());
        assertThat(plan1).isEqualTo(plan2);

        plan2 = getPlanSample2();
        assertThat(plan1).isNotEqualTo(plan2);
    }

    @Test
    void idTest() throws Exception {
        Plan plan = getPlanRandomSampleGenerator();
        PeriodicSubscription periodicSubscriptionBack = getPeriodicSubscriptionRandomSampleGenerator();

        plan.addId(periodicSubscriptionBack);
        assertThat(plan.getIds()).containsOnly(periodicSubscriptionBack);
        assertThat(periodicSubscriptionBack.getPlans()).isEqualTo(plan);

        plan.removeId(periodicSubscriptionBack);
        assertThat(plan.getIds()).doesNotContain(periodicSubscriptionBack);
        assertThat(periodicSubscriptionBack.getPlans()).isNull();

        plan.ids(new HashSet<>(Set.of(periodicSubscriptionBack)));
        assertThat(plan.getIds()).containsOnly(periodicSubscriptionBack);
        assertThat(periodicSubscriptionBack.getPlans()).isEqualTo(plan);

        plan.setIds(new HashSet<>());
        assertThat(plan.getIds()).doesNotContain(periodicSubscriptionBack);
        assertThat(periodicSubscriptionBack.getPlans()).isNull();
    }

    @Test
    void periodsTest() throws Exception {
        Plan plan = getPlanRandomSampleGenerator();
        Period periodBack = getPeriodRandomSampleGenerator();

        plan.setPeriods(periodBack);
        assertThat(plan.getPeriods()).isEqualTo(periodBack);

        plan.periods(null);
        assertThat(plan.getPeriods()).isNull();
    }
}
