package com.smz.gymplus.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PeriodicSubscriptionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPeriodicSubscriptionAllPropertiesEquals(PeriodicSubscription expected, PeriodicSubscription actual) {
        assertPeriodicSubscriptionAutoGeneratedPropertiesEquals(expected, actual);
        assertPeriodicSubscriptionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPeriodicSubscriptionAllUpdatablePropertiesEquals(PeriodicSubscription expected, PeriodicSubscription actual) {
        assertPeriodicSubscriptionUpdatableFieldsEquals(expected, actual);
        assertPeriodicSubscriptionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPeriodicSubscriptionAutoGeneratedPropertiesEquals(PeriodicSubscription expected, PeriodicSubscription actual) {
        assertThat(expected)
            .as("Verify PeriodicSubscription auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPeriodicSubscriptionUpdatableFieldsEquals(PeriodicSubscription expected, PeriodicSubscription actual) {
        assertThat(expected)
            .as("Verify PeriodicSubscription relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getPaymentStatus()).as("check paymentStatus").isEqualTo(actual.getPaymentStatus()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPeriodicSubscriptionUpdatableRelationshipsEquals(PeriodicSubscription expected, PeriodicSubscription actual) {
        assertThat(expected)
            .as("Verify PeriodicSubscription relationships")
            .satisfies(e -> assertThat(e.getPlans()).as("check plans").isEqualTo(actual.getPlans()))
            .satisfies(e -> assertThat(e.getInvoices()).as("check invoices").isEqualTo(actual.getInvoices()));
    }
}
