package com.smz.gymplus.web.rest;

import static com.smz.gymplus.domain.PeriodicSubscriptionAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smz.gymplus.IntegrationTest;
import com.smz.gymplus.domain.PeriodicSubscription;
import com.smz.gymplus.repository.PeriodicSubscriptionRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PeriodicSubscriptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PeriodicSubscriptionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Boolean DEFAULT_PAYMENT_STATUS = false;
    private static final Boolean UPDATED_PAYMENT_STATUS = true;

    private static final String ENTITY_API_URL = "/api/periodic-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PeriodicSubscriptionRepository periodicSubscriptionRepository;

    @Mock
    private PeriodicSubscriptionRepository periodicSubscriptionRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeriodicSubscriptionMockMvc;

    private PeriodicSubscription periodicSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodicSubscription createEntity(EntityManager em) {
        PeriodicSubscription periodicSubscription = new PeriodicSubscription()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .paymentStatus(DEFAULT_PAYMENT_STATUS);
        return periodicSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodicSubscription createUpdatedEntity(EntityManager em) {
        PeriodicSubscription periodicSubscription = new PeriodicSubscription()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .paymentStatus(UPDATED_PAYMENT_STATUS);
        return periodicSubscription;
    }

    @BeforeEach
    public void initTest() {
        periodicSubscription = createEntity(em);
    }

    @Test
    @Transactional
    void createPeriodicSubscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PeriodicSubscription
        var returnedPeriodicSubscription = om.readValue(
            restPeriodicSubscriptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(periodicSubscription)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PeriodicSubscription.class
        );

        // Validate the PeriodicSubscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPeriodicSubscriptionUpdatableFieldsEquals(
            returnedPeriodicSubscription,
            getPersistedPeriodicSubscription(returnedPeriodicSubscription)
        );
    }

    @Test
    @Transactional
    void createPeriodicSubscriptionWithExistingId() throws Exception {
        // Create the PeriodicSubscription with an existing ID
        periodicSubscription.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodicSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(periodicSubscription)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodicSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPeriodicSubscriptions() throws Exception {
        // Initialize the database
        periodicSubscriptionRepository.saveAndFlush(periodicSubscription);

        // Get all the periodicSubscriptionList
        restPeriodicSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodicSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeriodicSubscriptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(periodicSubscriptionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPeriodicSubscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(periodicSubscriptionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeriodicSubscriptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(periodicSubscriptionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPeriodicSubscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(periodicSubscriptionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPeriodicSubscription() throws Exception {
        // Initialize the database
        periodicSubscriptionRepository.saveAndFlush(periodicSubscription);

        // Get the periodicSubscription
        restPeriodicSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, periodicSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(periodicSubscription.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPeriodicSubscription() throws Exception {
        // Get the periodicSubscription
        restPeriodicSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPeriodicSubscription() throws Exception {
        // Initialize the database
        periodicSubscriptionRepository.saveAndFlush(periodicSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the periodicSubscription
        PeriodicSubscription updatedPeriodicSubscription = periodicSubscriptionRepository
            .findById(periodicSubscription.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPeriodicSubscription are not directly saved in db
        em.detach(updatedPeriodicSubscription);
        updatedPeriodicSubscription
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .paymentStatus(UPDATED_PAYMENT_STATUS);

        restPeriodicSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPeriodicSubscription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPeriodicSubscription))
            )
            .andExpect(status().isOk());

        // Validate the PeriodicSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPeriodicSubscriptionToMatchAllProperties(updatedPeriodicSubscription);
    }

    @Test
    @Transactional
    void putNonExistingPeriodicSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        periodicSubscription.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodicSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, periodicSubscription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(periodicSubscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeriodicSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeriodicSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        periodicSubscription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodicSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(periodicSubscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeriodicSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeriodicSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        periodicSubscription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodicSubscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(periodicSubscription)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PeriodicSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeriodicSubscriptionWithPatch() throws Exception {
        // Initialize the database
        periodicSubscriptionRepository.saveAndFlush(periodicSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the periodicSubscription using partial update
        PeriodicSubscription partialUpdatedPeriodicSubscription = new PeriodicSubscription();
        partialUpdatedPeriodicSubscription.setId(periodicSubscription.getId());

        partialUpdatedPeriodicSubscription.description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);

        restPeriodicSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriodicSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPeriodicSubscription))
            )
            .andExpect(status().isOk());

        // Validate the PeriodicSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPeriodicSubscriptionUpdatableFieldsEquals(
            partialUpdatedPeriodicSubscription,
            getPersistedPeriodicSubscription(periodicSubscription)
        );
    }

    @Test
    @Transactional
    void fullUpdatePeriodicSubscriptionWithPatch() throws Exception {
        // Initialize the database
        periodicSubscriptionRepository.saveAndFlush(periodicSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the periodicSubscription using partial update
        PeriodicSubscription partialUpdatedPeriodicSubscription = new PeriodicSubscription();
        partialUpdatedPeriodicSubscription.setId(periodicSubscription.getId());

        partialUpdatedPeriodicSubscription
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .paymentStatus(UPDATED_PAYMENT_STATUS);

        restPeriodicSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriodicSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPeriodicSubscription))
            )
            .andExpect(status().isOk());

        // Validate the PeriodicSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPeriodicSubscriptionUpdatableFieldsEquals(
            partialUpdatedPeriodicSubscription,
            getPersistedPeriodicSubscription(partialUpdatedPeriodicSubscription)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPeriodicSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        periodicSubscription.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodicSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, periodicSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(periodicSubscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeriodicSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeriodicSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        periodicSubscription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodicSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(periodicSubscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeriodicSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeriodicSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        periodicSubscription.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodicSubscriptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(periodicSubscription)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PeriodicSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeriodicSubscription() throws Exception {
        // Initialize the database
        periodicSubscriptionRepository.saveAndFlush(periodicSubscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the periodicSubscription
        restPeriodicSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, periodicSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return periodicSubscriptionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PeriodicSubscription getPersistedPeriodicSubscription(PeriodicSubscription periodicSubscription) {
        return periodicSubscriptionRepository.findById(periodicSubscription.getId()).orElseThrow();
    }

    protected void assertPersistedPeriodicSubscriptionToMatchAllProperties(PeriodicSubscription expectedPeriodicSubscription) {
        assertPeriodicSubscriptionAllPropertiesEquals(
            expectedPeriodicSubscription,
            getPersistedPeriodicSubscription(expectedPeriodicSubscription)
        );
    }

    protected void assertPersistedPeriodicSubscriptionToMatchUpdatableProperties(PeriodicSubscription expectedPeriodicSubscription) {
        assertPeriodicSubscriptionAllUpdatablePropertiesEquals(
            expectedPeriodicSubscription,
            getPersistedPeriodicSubscription(expectedPeriodicSubscription)
        );
    }
}
