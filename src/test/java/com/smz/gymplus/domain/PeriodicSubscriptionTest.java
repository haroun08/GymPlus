package com.smz.gymplus.domain;

import static com.smz.gymplus.domain.InvoiceTestSamples.*;
import static com.smz.gymplus.domain.PeriodicSubscriptionTestSamples.*;
import static com.smz.gymplus.domain.PlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smz.gymplus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeriodicSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeriodicSubscription.class);
        PeriodicSubscription periodicSubscription1 = getPeriodicSubscriptionSample1();
        PeriodicSubscription periodicSubscription2 = new PeriodicSubscription();
        assertThat(periodicSubscription1).isNotEqualTo(periodicSubscription2);

        periodicSubscription2.setId(periodicSubscription1.getId());
        assertThat(periodicSubscription1).isEqualTo(periodicSubscription2);

        periodicSubscription2 = getPeriodicSubscriptionSample2();
        assertThat(periodicSubscription1).isNotEqualTo(periodicSubscription2);
    }

    @Test
    void plansTest() throws Exception {
        PeriodicSubscription periodicSubscription = getPeriodicSubscriptionRandomSampleGenerator();
        Plan planBack = getPlanRandomSampleGenerator();

        periodicSubscription.setPlans(planBack);
        assertThat(periodicSubscription.getPlans()).isEqualTo(planBack);

        periodicSubscription.plans(null);
        assertThat(periodicSubscription.getPlans()).isNull();
    }

    @Test
    void invoicesTest() throws Exception {
        PeriodicSubscription periodicSubscription = getPeriodicSubscriptionRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        periodicSubscription.setInvoices(invoiceBack);
        assertThat(periodicSubscription.getInvoices()).isEqualTo(invoiceBack);

        periodicSubscription.invoices(null);
        assertThat(periodicSubscription.getInvoices()).isNull();
    }
}
